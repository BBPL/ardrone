package com.parrot.freeflight.transcodeservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.parrot.freeflight.tasks.MoveFileTask;
import com.parrot.freeflight.transcodeservice.tasks.CleanupCacheFolderTask;
import com.parrot.freeflight.utils.ARDroneMediaGallery;
import com.parrot.freeflight.utils.FileUtils;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TranscodingService extends Service {
    public static final String EXTRA_MEDIA_PATH = "extra.video.path";
    public static final String NEW_MEDIA_IS_AVAILABLE_ACTION = "transcoding.media.available";
    private static final String TAG = TranscodingService.class.getSimpleName();
    private IBinder binder = new LocalBinder();
    private ExecutorService executor;
    private ARDroneMediaGallery mediaGallery;
    private int refcounter;
    private CleanupCacheFolderTask removeBak;
    private boolean running;
    private File videoPath;

    class C12001 extends CleanupCacheFolderTask {
        C12001() {
        }

        protected void onPostExecute(Boolean bool) {
            TranscodingService.this.onTemporaryFilesCleared();
        }
    }

    class C12012 extends MoveFileTask {
        C12012() {
        }

        protected void onPostExecute(Boolean bool) {
            if (bool.equals(Boolean.TRUE)) {
                File resultFile = getResultFile();
                if (resultFile != null) {
                    Log.d(TranscodingService.TAG, "Going to add file " + resultFile.getAbsolutePath() + " to media gallery");
                    TranscodingService.this.mediaGallery.insertMedia(resultFile);
                    TranscodingService.this.notifyNewMediaAvailable(resultFile);
                    return;
                }
                Log.w(TranscodingService.TAG, "File transcoded successfully but getResultFile() returned null. Looks like a bug.");
            }
        }
    }

    public class LocalBinder extends Binder {
        public TranscodingService getService() {
            return TranscodingService.this;
        }
    }

    private native void encoderThreadStart();

    private void removeTempFiles() {
        if (this.removeBak == null || this.removeBak.getStatus() == Status.FINISHED) {
            this.removeBak = new C12001();
            if (VERSION.SDK_INT < 11) {
                this.removeBak.execute(new File[]{this.videoPath});
                return;
            }
            this.removeBak.executeOnExecutor(this.executor, new File[]{this.videoPath});
            return;
        }
        onTemporaryFilesCleared();
    }

    protected String getNextFile() {
        return this.videoPath != null ? FileUtils.getNextFile(this.videoPath, "enc") : null;
    }

    protected void notifyNewMediaAvailable(File file) {
        if (file != null) {
            Intent intent = new Intent(NEW_MEDIA_IS_AVAILABLE_ACTION);
            intent.putExtra(EXTRA_MEDIA_PATH, file.getAbsolutePath());
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    public void onCreate() {
        super.onCreate();
        this.mediaGallery = new ARDroneMediaGallery(this);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void onDestroy() {
        this.executor.shutdownNow();
        super.onDestroy();
        this.mediaGallery.onDestroy();
    }

    public void onMediaReady(String str) {
        File file = new File(str);
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            Log.w(TAG, "OnMediaReady() received but filename looks like broken. Filename: " + str);
            return;
        }
        File file2 = new File(file.getParentFile().getAbsolutePath(), name.substring(0, lastIndexOf) + ".mp4");
        if (file2.exists()) {
            Log.w(TAG, "Can't rename file to " + file2.getAbsolutePath() + ". Already exists!");
        } else if (!file.renameTo(file2)) {
            Log.w(TAG, "Can't rename file " + str + " due to error");
        }
        MoveFileTask c12012 = new C12012();
        if (FileUtils.getMediaFolder(this) != null) {
            try {
                if (VERSION.SDK_INT < 11) {
                    c12012.execute(new File[]{file2, new File(r1, file2.getName())}).get();
                    return;
                }
                c12012.executeOnExecutor(this.executor, new File[]{file2, new File(r1, file2.getName())}).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            stopSelf(i2);
        } else {
            String stringExtra = intent.getStringExtra(EXTRA_MEDIA_PATH);
            if (this.running) {
                if (this.running) {
                    Log.w(TAG, "Transcoding already running. Ignoring request.");
                }
            } else if (stringExtra != null) {
                this.videoPath = new File(stringExtra);
                removeTempFiles();
            } else {
                stopSelf(i2);
            }
        }
        return 1;
    }

    protected void onTemporaryFilesCleared() {
        if (!this.running) {
            Log.d(TAG, "Transcoding started...");
            this.running = true;
            encoderThreadStart();
        }
    }

    protected void onTranscodingFinished() {
        Log.d(TAG, "Transcoding stopped.");
        this.running = false;
        stopSelf();
    }
}
