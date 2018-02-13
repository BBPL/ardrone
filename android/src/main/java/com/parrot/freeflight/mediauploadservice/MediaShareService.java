package com.parrot.freeflight.mediauploadservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ExponentialBackOffPolicy;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.picasa.Picasa;
import com.google.api.services.picasa.Picasa.Builder;
import com.google.api.services.picasa.Picasa.Photos.Insert;
import com.google.api.services.picasa.PicasaScopes;
import com.google.api.services.picasa.model.AlbumEntry;
import com.google.api.services.picasa.model.MediaGroup;
import com.google.api.services.picasa.model.MediaThumbnail;
import com.google.api.services.picasa.model.Photo;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Videos;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.parrot.ardronetool.video.ArdtAtom;
import com.parrot.ardronetool.video.VideoAtomRetriever;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.academy.utils.AcademyFormatUtils;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.SplashActivity;
import com.parrot.freeflight.media.Exif2Interface;
import com.parrot.freeflight.media.Exif2Interface.Tag;
import com.parrot.freeflight.media.LocalMediaProvider;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.receivers.NetworkChangeReceiver;
import com.parrot.freeflight.receivers.NetworkChangeReceiverDelegate;
import com.parrot.freeflight.settings.AccountChecker;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.tasks.CreatePicasaAlbumTask;
import com.parrot.freeflight.tasks.GetPicasaAlbumsTask;
import com.parrot.freeflight.tasks.RegisterThumbnailUrlTask;
import com.parrot.freeflight.utils.FileUtils;
import com.parrot.freeflight.utils.InternetUtils;
import com.parrot.freeflight.vo.MediaVO;
import com.parrot.freeflight.vo.VideoCategoryVO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.sanselan.ImageInfo;
import org.json.JSONException;
import org.mortbay.util.URIUtil;

public class MediaShareService extends Service implements NetworkChangeReceiverDelegate {
    public static final String ACTION_UPLOAD_STATE_CHANGED = "mediashareservice.state.changed";
    private static final String[] DRONE1_PHOTO_TAGS = new String[]{"ardroneacademy", "Parrot", FileUtils.MEDIA_PUBLIC_FOLDER_NAME, "ardrone", "drone", "quadrocopter", ImageInfo.COMPRESSION_ALGORITHM_JPEG, "flying camera", "AR.Drone Academy", "AR.FreeFlight 2", "AR.FreeFlight", SplashActivity.VIDEO_FILE_NAME, "Android", "quadrotor", "aerial photo", "magnetometer", "ardrone-1.0-JPEGPHOTO"};
    private static final String[] DRONE1_VIDEO_TAGS = new String[]{"ardroneacademy", "Parrot", FileUtils.MEDIA_PUBLIC_FOLDER_NAME, "ardrone", "drone", "quadrocopter", "flying camera", "AR.Drone Academy", "AR.FreeFlight 2", "AR.FreeFlight", SplashActivity.VIDEO_FILE_NAME, "Android", "quadrotor", "aerial photo", "magnetometer", "ardrone-1.0-VIDEO"};
    private static final String[] DRONE2_PHOTO_TAGS = new String[]{"ardroneacademy", "Parrot", "AR.Drone 2.0", "ARDrone 2", "ardrone2", FileUtils.MEDIA_PUBLIC_FOLDER_NAME, "ardrone", "drone", "quadrocopter", ImageInfo.COMPRESSION_ALGORITHM_JPEG, "flying camera", "AR.Drone Academy", "AR.FreeFlight 2", "AR.FreeFlight", SplashActivity.VIDEO_FILE_NAME, "Android", "quadrotor", "aerial photo", "magnetometer", "ardrone-2.0-JPEGPHOTO"};
    private static final String[] DRONE2_VIDEO_TAGS = new String[]{"ardroneacademy", "Parrot", "AR.Drone 2.0", "ARDrone 2", "ardrone2", FileUtils.MEDIA_PUBLIC_FOLDER_NAME, "ardrone", "drone", "quadrocopter", "HD", "HDvideo", "720p", "flying camera", "AR.Drone Academy", "AR.FreeFlight 2", "AR.FreeFlight", SplashActivity.VIDEO_FILE_NAME, "Android", "quadrotor", "aerial photo", "magnetometer", "ardrone-2.0-HD720P"};
    public static final String EXTRA_UPLOADING_MEDIA = "mediashareservice.extra.media";
    public static final String EXTRA_UPLOADING_PROGRESS = "mediashareservice.extra.progress";
    public static final String EXTRA_UPLOADING_STATE = "mediadhareservice.extra.uploading";
    private static String FILENAME_FILES_UPLOADED = "media_uploaded.json";
    private static String FILENAME_FILES_UPLOADING = "media_uploading.json";
    private static final String TAG = MediaShareService.class.getSimpleName();
    private static final boolean UPLOAD_MEDIA_AS_PUBLIC = true;
    private static State state;
    private final IBinder binder = new LocalBinder();
    private GoogleAccountCredential credential;
    private MediaVO currentMedia;
    private double currentProgress;
    private ExecutorService executor;
    private Set<String> filesToUpload;
    private Map<String, String> filesUploaded;
    private Map<String, String> filesUploading;
    private boolean isAutoUploadPreempted;
    private final JsonFactory jsonFactory = new GsonFactory();
    private LocalMediaProvider mediaProvider;
    private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(this);
    private Set<String> photosToUpload;
    private Picasa picasa;
    private AlbumEntry picasaAlbum = null;
    private List<Runnable> preemptedRunnables;
    private State prevState;
    private ApplicationSettings settings;
    private boolean shutdownRequested;
    private final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    private UploadLock uploadLock;
    private UploadPhotoTask uploadPhotoTask;
    private int uploadStateRequestsCount;
    private UploadVideoTask uploadVideoTask;
    private YouTube youtube;

    class C11621 implements Runnable {
        C11621() {
        }

        public void run() {
            MediaShareService.this.restoreState();
        }
    }

    class C11632 implements Runnable {
        C11632() {
        }

        public void run() {
            if (MediaShareService.state == State.IDLE) {
                MediaShareService.this.shutdownRequested = false;
                if (MediaShareService.this.networkChangeReceiver.isAutoUploadAllowed()) {
                    MediaShareService.this.restoreState();
                    MediaShareService.this.getPicasaAlbums();
                    return;
                }
                Log.i(MediaShareService.TAG, "Service is stopping beacuse we are on mobile.");
                MediaShareService.this.stopSelf();
                return;
            }
            Log.i(MediaShareService.TAG, "Uploading is already in progress. startUpload() ignored.");
        }
    }

    class C11643 extends GetPicasaAlbumsTask {
        C11643() {
        }

        protected void onPostExecute(List<AlbumEntry> list) {
            super.onPostExecute(list);
            MediaShareService.this.onPicasaAlbumsReceived(list, list != null);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(MediaShareService.TAG, "Retrieving Picasa albums...");
        }
    }

    private static class UploadPhotoTask extends AsyncTask<Object, Double, Photo> implements Runnable {
        private Context context;
        private String description;
        private MediaVO media;
        private String path;
        private String photoAlbumId;
        private Picasa picasa;
        private StatusResult result = StatusResult.ERROR_UNKNOWN;
        private MediaShareService service;
        private String tagsString;
        private String title;

        public enum StatusResult {
            ERROR_UNKNOWN,
            OK,
            ERROR_AUTH_REQUIRED,
            ERROR_NETWORK,
            ERROR_INTERRUPTED
        }

        public UploadPhotoTask(Context context, Picasa picasa, MediaVO mediaVO, MediaShareService mediaShareService, String str, String str2, String str3, String str4) {
            this.context = context;
            this.picasa = picasa;
            this.media = mediaVO;
            this.path = mediaVO.getPath();
            this.title = str;
            this.description = str2;
            this.tagsString = str3;
            this.photoAlbumId = str4;
            this.service = mediaShareService;
        }

        protected Photo doInBackground(Object... objArr) {
            try {
                File file = new File(this.path);
                if (file.exists() && file.isFile()) {
                    try {
                        Insert insert = this.picasa.photos().insert(this.photoAlbumId, new InputStreamContent("image/jpeg", new BufferedInputStream(new FileInputStream(file))));
                        insert.getMediaHttpUploader().setDirectUploadEnabled(true);
                        publishProgress(new Double[]{Double.valueOf(0.0d)});
                        Photo photo = (Photo) insert.execute();
                        publishProgress(new Double[]{Double.valueOf(ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR)});
                        if (this.title == null) {
                            this.title = this.service.getTitle(this.media);
                        }
                        if (this.description == null) {
                            this.description = this.service.getPhotoDescription();
                        }
                        photo.setTitle(this.title);
                        photo.setSummary(this.description);
                        photo.getMediaGroup().setKeywords(this.tagsString == null ? this.service.getTagsList(this.media) : Arrays.asList(this.tagsString.split("\\s*,\\s*")));
                        photo = (Photo) this.picasa.photos().update(photo).execute();
                        publishProgress(new Double[]{Double.valueOf(1.0d)});
                        this.result = StatusResult.OK;
                        Log.d(MediaShareService.TAG, "Photo " + this.path + " uploaded successfully");
                        return photo;
                    } catch (UserRecoverableAuthIOException e) {
                        e = e;
                        this.result = StatusResult.ERROR_AUTH_REQUIRED;
                        e.printStackTrace();
                        Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                        return null;
                    } catch (GoogleAuthIOException e2) {
                        Log.w(MediaShareService.TAG, "Upload cancelled");
                        this.result = StatusResult.ERROR_INTERRUPTED;
                        Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                        return null;
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        e.printStackTrace();
                        Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                        return null;
                    } catch (IOException e4) {
                        e = e4;
                        e.printStackTrace();
                        this.result = StatusResult.ERROR_NETWORK;
                        Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                        return null;
                    } catch (IllegalArgumentException e5) {
                        e = e5;
                        this.result = StatusResult.ERROR_AUTH_REQUIRED;
                        e.printStackTrace();
                        Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                        return null;
                    }
                }
            } catch (UserRecoverableAuthIOException e6) {
                UserRecoverableAuthIOException e7;
                e7 = e6;
                this.result = StatusResult.ERROR_AUTH_REQUIRED;
                e7.printStackTrace();
                Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                return null;
            } catch (GoogleAuthIOException e8) {
                Log.w(MediaShareService.TAG, "Upload cancelled");
                this.result = StatusResult.ERROR_INTERRUPTED;
                Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                return null;
            } catch (FileNotFoundException e9) {
                FileNotFoundException e10;
                e10 = e9;
                e10.printStackTrace();
                Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                return null;
            } catch (IOException e11) {
                IOException e12;
                e12 = e11;
                e12.printStackTrace();
                this.result = StatusResult.ERROR_NETWORK;
                Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                return null;
            } catch (IllegalArgumentException e13) {
                IllegalArgumentException e14;
                e14 = e13;
                this.result = StatusResult.ERROR_AUTH_REQUIRED;
                e14.printStackTrace();
                Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
                return null;
            }
            Log.w(MediaShareService.TAG, "Unable to upload photo " + this.path);
            return null;
        }

        public StatusResult getLastStatus() {
            return this.result;
        }

        protected void onProgressUpdate(Double... dArr) {
            super.onProgressUpdate(dArr);
            this.service.setProgress(dArr[0].doubleValue());
        }

        public void run() {
            onPreExecute();
            onPostExecute(doInBackground(this.context, this.picasa, this.path));
        }
    }

    private static class UploadVideoTask extends AsyncTask<Object, Double, Video> implements Runnable {
        private VideoCategoryVO category;
        private Context context;
        private String description;
        private boolean isPrivateMedia;
        private MediaVO media;
        private String path;
        private MediaShareService service;
        private StatusResult status = StatusResult.ERROR_UNKNOWN;
        private String tagsString;
        private String title;
        private String videoCategoryId;
        private Videos.Insert videoInsert;
        private YouTube youtube;

        class C11711 implements MediaHttpUploaderProgressListener {
            C11711() {
            }

            public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {
                UploadVideoTask.this.service.setProgress(mediaHttpUploader.getProgress());
            }
        }

        public enum StatusResult {
            ERROR_UNKNOWN,
            OK,
            ERROR_AUTH_REQUIRED,
            ERROR_NETWORK,
            ERROR_INTERRUPTED
        }

        public UploadVideoTask(Context context, YouTube youTube, String str, MediaVO mediaVO, MediaShareService mediaShareService, String str2, String str3, String str4, boolean z, VideoCategoryVO videoCategoryVO) {
            this.context = context;
            this.youtube = youTube;
            this.media = mediaVO;
            this.category = videoCategoryVO;
            this.path = mediaVO.getPath();
            this.videoCategoryId = str;
            this.service = mediaShareService;
            this.title = str2;
            this.tagsString = str4;
            this.description = str3;
            this.isPrivateMedia = z;
        }

        protected Video doInBackground(Object... objArr) {
            FileNotFoundException e;
            Throwable e2;
            IllegalArgumentException e3;
            Log.d(MediaShareService.TAG, "Started uploading of " + this.path);
            File file = new File(this.path);
            try {
                AbstractInputStreamContent inputStreamContent = new InputStreamContent("video/mp4", new BufferedInputStream(new FileInputStream(file)));
                try {
                    inputStreamContent.setLength(file.length());
                    inputStreamContent.setRetrySupported(true);
                    Video video = new Video();
                    VideoStatus videoStatus = new VideoStatus();
                    if (this.isPrivateMedia) {
                        videoStatus.setPrivacyStatus("private");
                    } else {
                        videoStatus.setPrivacyStatus("public");
                    }
                    video.setStatus(videoStatus);
                    if (this.title == null) {
                        this.title = this.service.getTitle(this.media);
                    }
                    List tagsList = this.tagsString == null ? this.service.getTagsList(this.media) : Arrays.asList(this.tagsString.split("\\s*,\\s*"));
                    if (this.description == null) {
                        this.description = this.service.getVideoDescription();
                    }
                    VideoSnippet videoSnippet = new VideoSnippet();
                    videoSnippet.setTitle(this.title);
                    videoSnippet.setPublishedAt(new DateTime(System.currentTimeMillis()));
                    videoSnippet.setDescription(this.description);
                    videoSnippet.setTags(tagsList);
                    if (this.category != null) {
                        videoSnippet.setCategoryId(this.category.id);
                    } else {
                        videoSnippet.setCategoryId(this.videoCategoryId);
                    }
                    video.setSnippet(videoSnippet);
                    this.videoInsert = this.youtube.videos().insert("snippet,statistics,status", video, inputStreamContent);
                    MediaHttpUploader mediaHttpUploader = this.videoInsert.getMediaHttpUploader();
                    mediaHttpUploader.setChunkSize(262144);
                    mediaHttpUploader.setProgressListener(new C11711());
                    mediaHttpUploader.setDirectUploadEnabled(false);
                    Video video2 = (Video) this.videoInsert.execute();
                    Log.d(MediaShareService.TAG, "Video " + this.path + " uploaded successfully");
                    this.status = StatusResult.OK;
                    return video2;
                } catch (GoogleAuthIOException e4) {
                    Log.w(MediaShareService.TAG, "Upload cancelled");
                    this.status = StatusResult.ERROR_INTERRUPTED;
                    Log.w(MediaShareService.TAG, "Unable to upload " + this.path);
                    return null;
                } catch (FileNotFoundException e5) {
                    e = e5;
                    e.printStackTrace();
                    Log.w(MediaShareService.TAG, "Unable to upload " + this.path);
                    return null;
                } catch (IOException e6) {
                    e2 = e6;
                    this.status = StatusResult.ERROR_NETWORK;
                    Log.e(MediaShareService.TAG, "IOException : ", e2);
                    Log.w(MediaShareService.TAG, "Unable to upload " + this.path);
                    return null;
                } catch (IllegalArgumentException e7) {
                    e3 = e7;
                    this.status = StatusResult.ERROR_AUTH_REQUIRED;
                    e3.printStackTrace();
                    Log.w(MediaShareService.TAG, "Unable to upload " + this.path);
                    return null;
                }
            } catch (GoogleAuthIOException e8) {
                Log.w(MediaShareService.TAG, "Upload cancelled");
                this.status = StatusResult.ERROR_INTERRUPTED;
                Log.w(MediaShareService.TAG, "Unable to upload " + this.path);
                return null;
            } catch (FileNotFoundException e9) {
                e = e9;
                e.printStackTrace();
                Log.w(MediaShareService.TAG, "Unable to upload " + this.path);
                return null;
            } catch (IOException e10) {
                e2 = e10;
                this.status = StatusResult.ERROR_NETWORK;
                Log.e(MediaShareService.TAG, "IOException : ", e2);
                Log.w(MediaShareService.TAG, "Unable to upload " + this.path);
                return null;
            } catch (IllegalArgumentException e11) {
                e3 = e11;
                this.status = StatusResult.ERROR_AUTH_REQUIRED;
                e3.printStackTrace();
                Log.w(MediaShareService.TAG, "Unable to upload " + this.path);
                return null;
            }
        }

        public StatusResult getLastStatus() {
            return this.status;
        }

        public void run() {
            onPreExecute();
            onPostExecute(doInBackground(this.context, this.youtube, this.path));
        }
    }

    public class LocalBinder extends Binder {
        public MediaShareService getService() {
            return MediaShareService.this;
        }
    }

    public enum State {
        UNKNOWN,
        IDLE,
        UPLOADING
    }

    public enum StatusResult {
        ERROR_UNKNOWN,
        OK,
        ERROR_AUTH_REQUIRED,
        ERROR_NETWORK,
        ERROR_INTERRUPTED
    }

    private static class UploadLock implements Lock {
        private boolean isLocked;

        private UploadLock() {
            this.isLocked = false;
        }

        public void lock() {
            synchronized (this) {
                while (this.isLocked) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                this.isLocked = true;
            }
        }

        public void lockInterruptibly() throws InterruptedException {
            synchronized (this) {
                while (this.isLocked) {
                    wait();
                }
                this.isLocked = true;
            }
        }

        public Condition newCondition() {
            return null;
        }

        public boolean tryLock() {
            boolean z = true;
            synchronized (this) {
                if (this.isLocked) {
                    z = false;
                } else {
                    this.isLocked = true;
                }
            }
            return z;
        }

        public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
            synchronized (this) {
            }
            return false;
        }

        public void unlock() {
            synchronized (this) {
                this.isLocked = false;
                Log.d(MediaShareService.TAG, "Unlocked");
                notify();
            }
        }
    }

    private void createPicasaAlbum(final String str) {
        CreatePicasaAlbumTask c11654 = new CreatePicasaAlbumTask() {
            protected void onPostExecute(AlbumEntry albumEntry) {
                super.onPostExecute(albumEntry);
                MediaShareService.this.onCreatePicasaAlbumFinished(albumEntry, albumEntry != null);
            }

            protected void onPreExecute() {
                super.onPreExecute();
                Log.i(MediaShareService.TAG, "Creating new picasa album: " + str);
            }
        };
        if (!this.shutdownRequested) {
            if (VERSION.SDK_INT >= 11) {
                c11654.executeOnExecutor(this.executor, new Object[]{null, this.picasa, str, Boolean.FALSE});
                return;
            }
            c11654.execute(new Object[]{null, this.picasa, str, Boolean.FALSE});
        }
    }

    private void doUploadPhotoFile(MediaVO mediaVO, String str, String str2, String str3, String str4, boolean z) {
        final MediaVO mediaVO2 = mediaVO;
        final boolean z2 = z;
        this.uploadPhotoTask = new UploadPhotoTask(this, this.picasa, mediaVO, this, str, str2, str3, str4) {
            protected void onPostExecute(Photo photo) {
                MediaShareService.this.setState(State.IDLE);
                switch (getLastStatus()) {
                    case ERROR_AUTH_REQUIRED:
                        MediaShareService.this.settings.setGoogleAccountName(null);
                        Log.i(MediaShareService.TAG, "Stopped service because user needs to authenticate");
                        MediaShareService.this.stopSelf();
                        break;
                }
                MediaShareService.this.onPhotoUploaded(mediaVO2.getPath(), photo, z2);
                MediaShareService.this.uploadLock.unlock();
            }

            protected void onPreExecute() {
                MediaShareService.this.uploadLock.lock();
                MediaShareService.this.setState(State.UPLOADING);
                synchronized (MediaShareService.this.filesUploading) {
                    MediaShareService.this.currentMedia = mediaVO2;
                    MediaShareService.this.filesUploading.put(mediaVO2.getPath(), null);
                }
            }
        };
        try {
            this.executor.execute(this.uploadPhotoTask);
        } catch (RejectedExecutionException e) {
            Log.d(TAG, "Unable to start photo upload because executor rejected the task.");
        }
    }

    private void doUploadVideoFile(MediaVO mediaVO, String str, String str2, String str3, boolean z, VideoCategoryVO videoCategoryVO, boolean z2) {
        final MediaVO mediaVO2 = mediaVO;
        final boolean z3 = z2;
        this.uploadVideoTask = new UploadVideoTask(this, this.youtube, this.settings.getYouTubeVideoCategoryId(), mediaVO, this, str, str2, str3, z, videoCategoryVO) {
            protected void onPostExecute(Video video) {
                MediaShareService.this.setState(State.IDLE);
                switch (getLastStatus()) {
                    case ERROR_AUTH_REQUIRED:
                        MediaShareService.this.settings.setGoogleAccountName(null);
                        Log.i(MediaShareService.TAG, "Stopped service because user needs to authenticate");
                        MediaShareService.this.stopSelf();
                        break;
                }
                MediaShareService.this.onVideoUploaded(mediaVO2.getPath(), video, z3);
                MediaShareService.this.uploadLock.unlock();
            }

            protected void onPreExecute() {
                MediaShareService.this.uploadLock.lock();
                MediaShareService.this.setState(State.UPLOADING);
                synchronized (MediaShareService.this.filesUploading) {
                    MediaShareService.this.currentMedia = mediaVO2;
                    MediaShareService.this.filesUploading.put(mediaVO2.getPath(), null);
                }
            }
        };
        try {
            this.executor.execute(this.uploadVideoTask);
        } catch (RejectedExecutionException e) {
            Log.d(TAG, "Unable to start video upload because executor rejected the task.");
        }
    }

    private ArdtAtom extractArdtData(String str) {
        return !str.endsWith("mp4") ? null : new VideoAtomRetriever().getArdtAtom(str);
    }

    private String generateMediaTitleFromDate(int i, Date date, boolean z) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int i2 = instance.get(5);
        int i3 = instance.get(2);
        String format = String.format("%d/%02d/%02d", new Object[]{Integer.valueOf(instance.get(1)), Integer.valueOf(i3 + 1), Integer.valueOf(i2)});
        if (z) {
            return String.format(getString(C0984R.string.ff_ID000097), new Object[]{Integer.valueOf(i), Integer.valueOf(0), format});
        }
        return String.format(getString(C0984R.string.ff_ID000099), new Object[]{Integer.valueOf(i), Integer.valueOf(0), format});
    }

    private String generateMediaTitleFromFilename(int i, MediaVO mediaVO) {
        String str = null;
        String name = mediaVO.getFilePath().getName();
        if (name.startsWith("video_") || name.startsWith("picture_")) {
            String[] split = name.split("[_]");
            if (split.length == 3) {
                try {
                    str = generateMediaTitleFromDate(i, new SimpleDateFormat("yyyyMMdd").parse(split[1]), mediaVO.isVideo());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    private String generatePhotoTitle(String str) {
        String str2 = null;
        if (str != null) {
            try {
                String attribute = new Exif2Interface(str).getAttribute(Tag.IMAGE_DESCRIPTION);
                if (!(attribute == null || attribute.lastIndexOf(47) == -1)) {
                    String[] split = attribute.split(URIUtil.SLASH);
                    if (split.length == 2) {
                        String str3 = split[0];
                        str3 = split[1];
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("'media_'yyyyMMdd'_'HHmmss", Locale.US);
                        try {
                            Date parse = new SimpleDateFormat("'picture_'yyyyMMdd'_'HHmmss", Locale.US).parse(str3);
                            if (parse == null) {
                                parse = simpleDateFormat.parse(str3);
                            }
                            if (parse != null) {
                                str2 = generateMediaTitleFromDate(getDroneVersion(str), parse, false);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return str2;
    }

    private String generateVideoTitle(ArdtAtom ardtAtom) {
        if (ardtAtom == null) {
            return null;
        }
        int version = ardtAtom.getVersion();
        Date mediaDate = ardtAtom.getMediaDate();
        if (mediaDate == null) {
            mediaDate = ardtAtom.getUserboxDate();
        }
        return mediaDate != null ? generateMediaTitleFromDate(version, mediaDate, true) : null;
    }

    private int getDroneVersion(String str) {
        try {
            ExifInterface exifInterface = new ExifInterface(str);
            return (exifInterface.getAttributeInt("ImageWidth", 0) == 1280 && exifInterface.getAttributeInt("ImageLength", 0) == 720) ? 2 : 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private List<MediaVO> getLocalMediaFiles() {
        return this.mediaProvider.getMediaList(MediaType.ALL);
    }

    private void getPicasaAlbums() {
        GetPicasaAlbumsTask c11643 = new C11643();
        if (!this.shutdownRequested && this.picasa != null) {
            if (VERSION.SDK_INT >= 11) {
                c11643.executeOnExecutor(this.executor, new Object[]{null, this.picasa});
                return;
            }
            c11643.execute(new Object[]{null, this.picasa});
        }
    }

    public static State getState() {
        return state;
    }

    private void handlePhotoUpload() {
        if (this.photosToUpload != null && this.photosToUpload.size() > 0) {
            String[] strArr;
            synchronized (this.photosToUpload) {
                strArr = (String[]) this.photosToUpload.toArray(new String[this.photosToUpload.size()]);
            }
            uploadMediaFile(strArr[0]);
        }
    }

    private void handleToUploadFiles() throws InterruptedException, ExecutionException {
        if (this.filesToUpload != null && this.filesToUpload.size() > 0) {
            String[] strArr;
            synchronized (this.filesToUpload) {
                strArr = (String[]) this.filesToUpload.toArray(new String[this.filesToUpload.size()]);
            }
            uploadMediaFile(strArr[0]);
        }
    }

    private void handleUploadingFiles() throws InterruptedException, ExecutionException {
        if (this.filesUploading != null && this.filesUploading.size() > 0) {
            uploadMediaFile(((String[]) this.filesUploading.keySet().toArray(new String[this.filesUploading.size()]))[0]);
        }
    }

    private void initGoogleAccount() {
        String googleAccountName = AccountChecker.getGoogleAccountName(this.settings, getApplicationContext());
        if (googleAccountName != null) {
            this.credential = GoogleAccountCredential.usingOAuth2(this, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_UPLOAD, PicasaScopes.DATA);
            this.credential.setSelectedAccountName(googleAccountName);
        }
    }

    private void initPicasaClient() {
        initGoogleAccount();
        if (this.credential != null) {
            this.picasa = new Builder(this.transport, this.credential).setApplicationName(getString(2131165184)).build();
        }
    }

    private void initYouTubeClient() {
        initGoogleAccount();
        if (this.credential != null) {
            this.youtube = new YouTube.Builder(this.transport, this.jsonFactory, this.credential).setApplicationName(getString(2131165184)).build();
        }
    }

    private void notifyCurrentState() {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        Intent intent = new Intent(ACTION_UPLOAD_STATE_CHANGED);
        intent.putExtra(EXTRA_UPLOADING_STATE, state == State.UPLOADING);
        intent.putExtra(EXTRA_UPLOADING_PROGRESS, this.currentProgress);
        intent.putExtra(EXTRA_UPLOADING_MEDIA, this.currentMedia);
        instance.sendBroadcast(intent);
    }

    private void onRegisterPhotoOnAcademy(String str, String str2, boolean z) {
        Log.d(TAG, "Photo thumb url to register with academy: " + str2);
        final boolean z2 = z;
        try {
            this.executor.execute(new RegisterThumbnailUrlTask(this, str, str2, true) {
                protected void onPostExecute(Boolean bool) {
                    super.onPostExecute(bool);
                    if (!z2) {
                        MediaShareService.this.proceedUploads();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            Log.d(TAG, "Unable to start register photo thumb task because executor rejected the task.");
        }
    }

    private void onRegisterVideoOnAcademy(String str, String str2, boolean z) {
        Log.d(TAG, "Video thumb url to register with academy: " + str2);
        final boolean z2 = z;
        try {
            this.executor.execute(new RegisterThumbnailUrlTask(this, str, str2, true) {
                protected void onPostExecute(Boolean bool) {
                    super.onPostExecute(bool);
                    if (!z2) {
                        MediaShareService.this.proceedUploads();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            Log.d(TAG, "Unable to start register video thumb task because executor rejected the task.");
        }
    }

    private void onStateChanged() {
        notifyCurrentState();
    }

    private void preemptAutoUpload() {
        this.preemptedRunnables = this.executor.shutdownNow();
        this.executor = Executors.newSingleThreadExecutor();
        this.uploadLock.lock();
        this.isAutoUploadPreempted = true;
        stopSelf();
        this.uploadLock.unlock();
    }

    private void proceedUploads() {
        Object obj = null;
        if (!this.shutdownRequested) {
            try {
                handleUploadingFiles();
                handleToUploadFiles();
                handlePhotoUpload();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
        Object obj2 = (!this.settings.isAutoPhotoUploadEnabled() || this.photosToUpload.size() <= 0) ? null : 1;
        if (this.settings.isAutoVideoUploadEnabled() && this.filesToUpload.size() > 0) {
            obj = 1;
        }
        if (this.filesUploading.size() == 0 && obj2 == null && r2 == null) {
            Log.i(TAG, "Stopped service as there is nothing to upload");
            stopSelf();
        }
    }

    private void registerBroadcastListeners() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.networkChangeReceiver, NetworkChangeReceiver.createLocalFilter());
        registerReceiver(this.networkChangeReceiver, NetworkChangeReceiver.createSystemFilter());
    }

    private void restoreAutoUpload() {
        if (this.isAutoUploadPreempted && this.preemptedRunnables != null) {
            for (Runnable execute : this.preemptedRunnables) {
                this.executor.execute(execute);
            }
            this.isAutoUploadPreempted = false;
            startUpload();
        }
    }

    private void restoreUploadingMedia() {
        try {
            SerializationUtils.restoreMap(this, FILENAME_FILES_UPLOADING, this.filesUploading);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    private void setProgress(double d) {
        synchronized (this) {
            this.currentProgress = d;
            Log.d(TAG, "Progress changed : " + this.currentProgress);
            onStateChanged();
        }
    }

    private void setState(State state) {
        synchronized (this) {
            if (state == State.UPLOADING) {
                this.uploadStateRequestsCount++;
            } else if (state == State.IDLE) {
                this.uploadStateRequestsCount--;
            }
            if (this.uploadStateRequestsCount <= 0) {
                this.uploadStateRequestsCount = 0;
            }
            if (this.prevState != state) {
                this.prevState = state;
                state = state;
                onStateChanged();
            }
        }
    }

    private void unregisterBroadcastListeners() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.networkChangeReceiver);
        unregisterReceiver(this.networkChangeReceiver);
    }

    private void uploadMediaFile(String str) {
        if (str.endsWith("mp4") && this.settings.isAutoVideoUploadEnabled()) {
            uploadVideoFile(str);
        } else if (str.endsWith("jpg") && this.settings.isAutoPhotoUploadEnabled()) {
            uploadPhotoFile(str);
        }
    }

    private void uploadPhotoFile(String str) {
        MediaVO mediaVO = new MediaVO();
        mediaVO.setVideo(false);
        mediaVO.setPath(str);
        doUploadPhotoFile(mediaVO, getTitle(mediaVO), getPhotoDescription(), getTags(mediaVO), this.picasaAlbum.getAlbumId(), false);
    }

    private void uploadVideoFile(String str) {
        MediaVO mediaVO = new MediaVO();
        mediaVO.setPath(str);
        mediaVO.setVideo(true);
        doUploadVideoFile(mediaVO, getTitle(mediaVO), getVideoDescription(), getTags(mediaVO), false, new VideoCategoryVO(this.settings.getYouTubeVideoCategoryId(), null), false);
    }

    public void abortCurrentUpload() {
        this.executor.shutdownNow();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public String getPhotoDescription() {
        return getString(C0984R.string.ff_ID000100);
    }

    public Picasa getPicasaInstance() {
        if (this.picasa == null) {
            initPicasaClient();
        }
        return this.picasa;
    }

    public String getTags(MediaVO mediaVO) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String append : getTagsList(mediaVO)) {
            stringBuilder.append(append);
            stringBuilder.append(", ");
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        return stringBuilder.toString();
    }

    public List<String> getTagsList(MediaVO mediaVO) {
        int i = 1;
        if (!mediaVO.isVideo()) {
            return getDroneVersion(mediaVO.getPath()) == 2 ? Arrays.asList(DRONE2_PHOTO_TAGS) : Arrays.asList(DRONE1_PHOTO_TAGS);
        } else {
            ArdtAtom extractArdtData = extractArdtData(mediaVO.getPath());
            if (extractArdtData != null) {
                i = extractArdtData.getVersion();
            }
            return i == 2 ? Arrays.asList(DRONE2_VIDEO_TAGS) : Arrays.asList(DRONE1_VIDEO_TAGS);
        }
    }

    public String getTitle(MediaVO mediaVO) {
        String generateVideoTitle;
        if (mediaVO.isVideo()) {
            ArdtAtom extractArdtData = extractArdtData(mediaVO.getPath());
            if (extractArdtData == null) {
                return null;
            }
            generateVideoTitle = generateVideoTitle(extractArdtData);
            if (generateVideoTitle == null) {
                generateVideoTitle = generateMediaTitleFromFilename(extractArdtData.getVersion(), mediaVO);
            }
            return generateVideoTitle == null ? new File(mediaVO.getPath()).getName() : generateVideoTitle;
        } else {
            generateVideoTitle = generatePhotoTitle(mediaVO.getPath());
            if (generateVideoTitle == null) {
                generateVideoTitle = generateMediaTitleFromFilename(getDroneVersion(mediaVO.getPath()), mediaVO);
            }
            return generateVideoTitle == null ? new File(mediaVO.getPath()).getName() : generateVideoTitle;
        }
    }

    public String getVideoDescription() {
        return getString(C0984R.string.ff_ID000098);
    }

    public YouTube getYoutubeInstance() {
        if (this.youtube == null) {
            initYouTubeClient();
        }
        return this.youtube;
    }

    public boolean mediaIsUploaded(String str) {
        return this.filesUploaded.containsKey(str);
    }

    public boolean mediaIsUploading(String str) {
        return this.filesUploading.containsKey(str);
    }

    public void onAutoUploadPermissionChanged() {
        Log.d(TAG, "onAutoUploadPermissionChanged");
        if (this.networkChangeReceiver.isAutoUploadAllowed()) {
            Log.d(TAG, "Service restart");
            startUpload();
            return;
        }
        Log.i(TAG, "Service stopped because we are on mobile.");
        abortCurrentUpload();
        stopSelf();
    }

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "New service connection established.");
        return this.binder;
    }

    public void onCreate() {
        super.onCreate();
        state = State.IDLE;
        this.prevState = State.UNKNOWN;
        this.executor = Executors.newSingleThreadExecutor();
        this.uploadLock = new UploadLock();
        this.currentProgress = 0.0d;
        this.currentMedia = null;
        this.filesUploaded = new HashMap();
        this.filesUploading = new HashMap();
        this.mediaProvider = new LocalMediaProvider(this);
        this.settings = ((FreeFlightApplication) getApplication()).getAppSettings();
        initYouTubeClient();
        initPicasaClient();
        this.networkChangeReceiver.init(((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo(), ((FreeFlightApplication) getApplication()).getAppSettings().is3gEdgeDataSyncEnabled());
        registerBroadcastListeners();
        try {
            this.executor.execute(new C11621());
        } catch (RejectedExecutionException e) {
            Log.d(TAG, "Rejected execution of restoreState()");
        }
        Log.d(TAG, "Service created");
    }

    protected void onCreatePicasaAlbumFinished(AlbumEntry albumEntry, boolean z) {
        if (z) {
            Log.i(TAG, "Picasa album has been created. Album name: " + albumEntry.getTitle() + ", album id: " + albumEntry.getAlbumId());
            this.picasaAlbum = albumEntry;
            proceedUploads();
            return;
        }
        Log.w(TAG, "Unable to create picasa album.");
    }

    public void onDestroy() {
        super.onDestroy();
        this.executor.shutdownNow();
        try {
            this.executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setState(State.IDLE);
        unregisterBroadcastListeners();
        Log.d(TAG, "Service destroyed");
    }

    public void onNetworkChanged(NetworkInfo networkInfo) {
    }

    protected void onPhotoUploaded(String str, Photo photo, boolean z) {
        synchronized (this) {
            synchronized (this.filesUploading) {
                this.currentMedia = null;
                this.filesUploading.remove(str);
            }
            if (photo != null) {
                Object obj;
                synchronized (this.photosToUpload) {
                    this.photosToUpload.remove(str);
                }
                synchronized (this.filesUploaded) {
                    this.filesUploaded.put(str, String.format(getString(C0984R.string.url_picasa_share), new Object[]{photo.getId()}));
                }
                MediaGroup mediaGroup = photo.getMediaGroup();
                if (mediaGroup != null) {
                    List thumbnails = mediaGroup.getThumbnails();
                    if (thumbnails != null && thumbnails.size() > 0) {
                        String url = ((MediaThumbnail) thumbnails.get(0)).getUrl();
                        if (TextUtils.isEmpty(AcademyUtils.login)) {
                            AcademyUtils.getCredentials(this);
                        }
                        if (AcademyUtils.isConnectedAcademy) {
                            onRegisterPhotoOnAcademy(str, AcademyFormatUtils.academyThumbUrlFromPicasaThumbUrl(url), z);
                            obj = 1;
                        } else {
                            Log.i(TAG, "Photo is not registered with Academy as user is not logged in.");
                            obj = null;
                        }
                        saveState();
                        if (obj == null && !z) {
                            proceedUploads();
                        }
                    }
                }
                obj = null;
                saveState();
                proceedUploads();
            } else {
                Log.w(TAG, "Looks like we have problems uploading photo " + str);
            }
            this.uploadPhotoTask = null;
            restoreAutoUpload();
        }
    }

    protected void onPicasaAlbumsReceived(List<AlbumEntry> list, boolean z) {
        if (z) {
            for (AlbumEntry albumEntry : list) {
                String title = albumEntry.getTitle();
                if (title != null && title.equals(getString(C0984R.string.ff_ID000040))) {
                    this.picasaAlbum = albumEntry;
                    break;
                }
            }
            if (this.picasaAlbum == null) {
                Log.i(TAG, "Picasa albums received successfully, but no suitable album.");
                createPicasaAlbum(getString(C0984R.string.ff_ID000040));
                return;
            }
            Log.i(TAG, "Picasa albums received successfully.");
            proceedUploads();
            return;
        }
        Log.w(TAG, "Unable to get picasa albums");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "Starting service");
        if (state != State.IDLE) {
            Log.d(TAG, "Service already started. Ignoring start service request");
        } else if (InternetUtils.isConnected(this)) {
            String googleAccountName = AccountChecker.getGoogleAccountName(this.settings, getApplicationContext());
            if (this.settings.isAutoVideoUploadEnabled() || (this.settings.isAutoPhotoUploadEnabled() && googleAccountName != null)) {
                if (this.youtube == null) {
                    initYouTubeClient();
                }
                if (this.picasa == null) {
                    initPicasaClient();
                }
                startUpload();
            } else {
                if (googleAccountName == null) {
                    Log.i(TAG, "Service is not started because user needs to authenticate.");
                } else {
                    Log.i(TAG, "Service is not started because automatic media uploading is not enabled");
                }
                stopSelf();
            }
        } else {
            Log.i(TAG, "Service is not started because network is not available.");
            stopSelf();
        }
        notifyCurrentState();
        return 1;
    }

    protected void onVideoUploaded(String str, Video video, boolean z) {
        Object obj = 1;
        String str2 = null;
        synchronized (this) {
            synchronized (this.filesUploading) {
                this.currentMedia = null;
                this.filesUploading.remove(str);
            }
            if (video != null) {
                synchronized (this.filesToUpload) {
                    this.filesToUpload.remove(str);
                }
                synchronized (this.filesUploaded) {
                    this.filesUploaded.put(str, String.format(getString(C0984R.string.url_youtube_share), new Object[]{video.getId()}));
                }
                ThumbnailDetails thumbnails = video.getSnippet().getThumbnails();
                if (thumbnails != null) {
                    Thumbnail thumbnail = thumbnails.getDefault();
                    if (thumbnail != null) {
                        str2 = AcademyFormatUtils.academyThumbUrlFromYouTubeThumbUrl(thumbnail.getUrl());
                    }
                    Log.d(TAG, "Video thumb url to register with academy: " + str2);
                    if (TextUtils.isEmpty(AcademyUtils.login)) {
                        AcademyUtils.getCredentials(this);
                    }
                    if (!AcademyUtils.isConnectedAcademy || str2 == null) {
                        Log.i(TAG, "Video is not registered on Academy as user is not connected to it.");
                        obj = null;
                    } else {
                        onRegisterVideoOnAcademy(str, str2, z);
                    }
                } else {
                    obj = null;
                }
                saveState();
                if (obj == null && !z) {
                    proceedUploads();
                }
            } else {
                Log.w(TAG, "Looks like we have problems uploading video " + str);
            }
            this.uploadVideoTask = null;
            restoreAutoUpload();
        }
    }

    public void resendBroadcasts() {
        notifyCurrentState();
    }

    public void restoreState() {
        restoreUploadedMedia();
        restoreUploadingMedia();
        List<MediaVO> localMediaFiles = getLocalMediaFiles();
        this.filesToUpload = new HashSet(localMediaFiles.size());
        this.photosToUpload = new HashSet();
        for (MediaVO path : localMediaFiles) {
            this.filesToUpload.add(path.getPath());
        }
        for (String remove : this.filesUploaded.keySet()) {
            this.filesToUpload.remove(remove);
        }
        for (String remove2 : this.filesUploading.keySet()) {
            this.filesToUpload.remove(remove2);
        }
        for (String remove22 : this.filesToUpload) {
            if (remove22.endsWith(".jpg")) {
                this.photosToUpload.add(remove22);
            }
        }
        this.filesToUpload.removeAll(this.photosToUpload);
    }

    public void restoreUploadedMedia() {
        try {
            SerializationUtils.restoreMap(this, FILENAME_FILES_UPLOADED, this.filesUploaded);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public void saveState() {
        saveUploadedMedia();
        saveUploadingMedia();
    }

    public void saveUploadedMedia() {
        try {
            SerializationUtils.saveMap(this, FILENAME_FILES_UPLOADED, this.filesUploaded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUploadingMedia() {
        try {
            SerializationUtils.saveMap(this, FILENAME_FILES_UPLOADING, this.filesUploading);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startUpload() {
        try {
            this.executor.execute(new C11632());
        } catch (RejectedExecutionException e) {
            Log.d(TAG, "Rejected execution of startUpload()");
        }
    }

    public boolean uploadPhotoFile(MediaVO mediaVO, String str, String str2, String str3, String str4) {
        String path = mediaVO.getPath();
        if (!path.endsWith("jpg") || this.filesUploaded.containsKey(path) || this.filesUploading.containsKey(path)) {
            return false;
        }
        preemptAutoUpload();
        doUploadPhotoFile(mediaVO, str, str2, str3, str4, true);
        return true;
    }

    public boolean uploadVideoFile(MediaVO mediaVO, String str, String str2, String str3, boolean z, VideoCategoryVO videoCategoryVO) {
        String path = mediaVO.getPath();
        if (!path.endsWith("mp4") || this.filesUploaded.containsKey(path) || this.filesUploading.containsKey(path)) {
            return false;
        }
        preemptAutoUpload();
        doUploadVideoFile(mediaVO, str, str2, str3, z, videoCategoryVO, true);
        return true;
    }

    public void waitForCancellation() {
        this.uploadLock.lock();
        this.uploadLock.unlock();
    }
}
