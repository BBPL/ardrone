package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.dialogs.ProgressDialog;
import com.parrot.freeflight.mediauploadservice.MediaShareService;
import com.parrot.freeflight.mediauploadservice.MediaShareService.LocalBinder;
import com.parrot.freeflight.mediauploadservice.MediaShareService.State;
import com.parrot.freeflight.mediauploadservice.broadcastreceivers.MediaShareStateChangedReceiver;
import com.parrot.freeflight.mediauploadservice.broadcastreceivers.MediaShareStateChangedReceiverDelegate;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.tasks.CheckDroneNetworkAvailabilityTask;
import com.parrot.freeflight.tasks.GetYouTubeVideoCategoriesTask;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.AsyncTaskResult;
import com.parrot.freeflight.utils.EnhancedArrayAdapter;
import com.parrot.freeflight.vo.MediaVO;
import com.parrot.freeflight.vo.VideoCategoryVO;

public class VideoUploadActivity extends ParrotActivity implements MediaShareStateChangedReceiverDelegate, MediaLoadingDialogDelegate {
    private static final long DIALOG_TIMEOUT = 1000;
    public static final String EXTRA_UPLOADVIDEO_FILENAME = "uploadvideo.extra.filename";
    private static final String TAG = VideoUploadActivity.class.getSimpleName();
    private OnTouchListener OnTouchOutOfFocusListener = new C11336();
    private ActionBar actionBar;
    private boolean categoriesReady;
    private VideoCategoryVO currentCategory;
    private MediaVO currentMedia;
    private EditText descriptionEdit;
    private Point firstPointTouch = new Point();
    GetYouTubeVideoCategoriesTask getYouTubeVideoCategoriesTask;
    private final Handler handler = new Handler();
    private MediaLoadingDialog loadingDialog;
    private MediaShareService mediaShareService;
    private MediaShareServiceConnection mediaShareServiceConnection;
    private boolean movedEnough = false;
    private int movementPrecision = 10;
    private boolean pausedAutoUpload = false;
    private RadioButton privateButton;
    private ProgressBar progressCategories;
    private ProgressDialog progressDialog;
    private Spinner spinnerVideoCategories;
    private EditText tagsEdit;
    private EditText titleEdit;
    private Button uploadNowButton;
    private BroadcastReceiver uploadUpdateReceiver;
    private boolean uploadWasStarted;
    private VideoCategoryVO[] videoCategories;
    private ArrayAdapter<VideoCategoryVO> videoCategoriesAdapter;

    class C11261 implements OnClickListener {
        C11261() {
        }

        public void onClick(View view) {
            VideoUploadActivity.this.pauseMediaShareService();
            VideoUploadActivity.this.uploadVideo();
        }
    }

    class C11272 implements OnItemSelectedListener {
        C11272() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (VideoUploadActivity.this.categoriesReady) {
                VideoUploadActivity.this.currentCategory = (VideoCategoryVO) adapterView.getItemAtPosition(i);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            VideoUploadActivity.this.currentCategory = null;
        }
    }

    class C11293 extends GetYouTubeVideoCategoriesTask {

        class C11281 implements Runnable {
            C11281() {
            }

            public void run() {
                VideoUploadActivity.this.dismissProgressDialog();
                VideoUploadActivity.this.finish();
            }
        }

        C11293() {
        }

        protected void onPostExecute(AsyncTaskResult<VideoCategoryVO[]> asyncTaskResult) {
            if (asyncTaskResult.succeeded()) {
                VideoUploadActivity.this.dismissProgressDialog();
                VideoUploadActivity.this.categoriesReady = true;
                VideoUploadActivity.this.videoCategoriesAdapter = new EnhancedArrayAdapter(VideoUploadActivity.this, C0984R.layout.view_google_settings_spinner_item, (Object[]) asyncTaskResult.result);
                VideoUploadActivity.this.videoCategoriesAdapter.setDropDownViewResource(17367043);
                VideoUploadActivity.this.spinnerVideoCategories.setAdapter(VideoUploadActivity.this.videoCategoriesAdapter);
                VideoUploadActivity.this.spinnerVideoCategories.setEnabled(true);
                VideoUploadActivity.this.uploadNowButton.setEnabled(true);
                String youTubeVideoCategoryId = VideoUploadActivity.this.getAppSettings().getYouTubeVideoCategoryId();
                if (youTubeVideoCategoryId != null) {
                    int length = ((VideoCategoryVO[]) asyncTaskResult.result).length;
                    int i = 0;
                    int i2 = -1;
                    while (i < length) {
                        int i3;
                        VideoCategoryVO videoCategoryVO = ((VideoCategoryVO[]) asyncTaskResult.result)[i];
                        if (videoCategoryVO.id.equals(youTubeVideoCategoryId)) {
                            VideoUploadActivity.this.currentCategory = videoCategoryVO;
                            i3 = i;
                        } else {
                            i3 = i2;
                        }
                        i++;
                        i2 = i3;
                    }
                    if (i2 != -1) {
                        VideoUploadActivity.this.spinnerVideoCategories.setSelection(i2);
                    }
                }
            } else if (asyncTaskResult.exception instanceof UserRecoverableAuthIOException) {
                Intent intent = ((UserRecoverableAuthIOException) asyncTaskResult.exception).getIntent();
                if (intent != null) {
                    VideoUploadActivity.this.startActivityForResult(intent, 1);
                    VideoUploadActivity.this.setResult(1);
                }
                VideoUploadActivity.this.dismissProgressDialog();
                VideoUploadActivity.this.finish();
            } else {
                VideoUploadActivity.this.progressDialog.setMessage(VideoUploadActivity.this.getString(C0984R.string.ff_ID000057));
                VideoUploadActivity.this.handler.postDelayed(new C11281(), VideoUploadActivity.DIALOG_TIMEOUT);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            VideoUploadActivity.this.categoriesReady = false;
            VideoUploadActivity.this.videoCategories[0] = new VideoCategoryVO(null, VideoUploadActivity.this.getString(C0984R.string.ff_ID000055));
            VideoUploadActivity.this.spinnerVideoCategories.setAdapter(new EnhancedArrayAdapter(VideoUploadActivity.this, C0984R.layout.view_google_settings_spinner_item, VideoUploadActivity.this.videoCategories));
            VideoUploadActivity.this.spinnerVideoCategories.setEnabled(false);
        }
    }

    class C11325 extends CheckDroneNetworkAvailabilityTask {

        class C11311 implements Runnable {
            C11311() {
            }

            public void run() {
                VideoUploadActivity.this.dismissProgressDialog();
                VideoUploadActivity.this.finish();
            }
        }

        C11325() {
        }

        protected void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                Log.i(VideoUploadActivity.TAG, "Can't initCateforiesSpinner because Drone on Network");
                VideoUploadActivity.this.progressDialog.setMessage(VideoUploadActivity.this.getString(C0984R.string.ff_ID000057));
                VideoUploadActivity.this.handler.postDelayed(new C11311(), VideoUploadActivity.DIALOG_TIMEOUT);
                return;
            }
            VideoUploadActivity.this.initCategoriesSpinner();
        }

        protected void onPreExecute() {
            VideoUploadActivity.this.progressDialog = new ProgressDialog(VideoUploadActivity.this, C0984R.style.FreeFlightDialog_Hotspot);
            VideoUploadActivity.this.progressDialog.setMessage(VideoUploadActivity.this.getString(C0984R.string.ae_ID000068).toUpperCase());
            VideoUploadActivity.this.progressDialog.setSubMessage(" ");
            VideoUploadActivity.this.progressDialog.setCanceledOnTouchOutside(false);
            VideoUploadActivity.this.progressDialog.setCancelable(false);
            VideoUploadActivity.this.progressDialog.show();
        }
    }

    class C11336 implements OnTouchListener {
        C11336() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionIndex() == 0) {
                switch (motionEvent.getAction()) {
                    case 0:
                        VideoUploadActivity.this.firstPointTouch.x = (int) motionEvent.getX(0);
                        VideoUploadActivity.this.firstPointTouch.y = (int) motionEvent.getY(0);
                        break;
                    case 1:
                        if (!(VideoUploadActivity.this.getCurrentFocus() == null || VideoUploadActivity.this.getCurrentFocus() == view || VideoUploadActivity.this.movedEnough)) {
                            ((InputMethodManager) VideoUploadActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(VideoUploadActivity.this.getCurrentFocus().getWindowToken(), 2);
                        }
                        VideoUploadActivity.this.movedEnough = false;
                        break;
                    case 2:
                        if (Math.pow((double) (motionEvent.getX(0) - ((float) VideoUploadActivity.this.firstPointTouch.x)), 2.0d) + Math.pow((double) (motionEvent.getY(0) - ((float) VideoUploadActivity.this.firstPointTouch.y)), 2.0d) > Math.pow((double) VideoUploadActivity.this.movementPrecision, 2.0d)) {
                            VideoUploadActivity.this.movedEnough = true;
                            break;
                        }
                        break;
                }
            }
            return true;
        }
    }

    private class MediaShareServiceConnection implements ServiceConnection {
        private final String TAG;

        private MediaShareServiceConnection() {
            this.TAG = MediaShareServiceConnection.class.getSimpleName();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(this.TAG, "Connected to Media Share Service");
            VideoUploadActivity.this.mediaShareService = ((LocalBinder) iBinder).getService();
            VideoUploadActivity.this.mediaShareService.resendBroadcasts();
            VideoUploadActivity.this.initTextFields();
            VideoUploadActivity.this.checkDroneConnectivityToInitCategoriesSpinner();
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    @SuppressLint({"NewApi"})
    private void checkDroneConnectivityToInitCategoriesSpinner() {
        new C11325().execute(new Context[]{this});
    }

    private void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    private void initActionBar() {
        this.actionBar = getParrotActionBar();
        setTitle(getString(C0984R.string.ff_ID000056).toUpperCase());
        this.actionBar.initBackButton();
    }

    private void initBroadcastReceiver() {
        this.uploadUpdateReceiver = new MediaShareStateChangedReceiver(this);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.uploadUpdateReceiver, MediaShareStateChangedReceiver.createFilter());
        Log.d(TAG, "Receiver registered");
    }

    private void initCategoriesSpinner() {
        this.getYouTubeVideoCategoriesTask = new C11293();
        if (this.mediaShareService.getYoutubeInstance() != null) {
            this.getYouTubeVideoCategoriesTask.execute(new Object[]{this, r0});
            return;
        }
        Toast.makeText(this, "Not connected to a google account !", 0).show();
    }

    private void initTextFields() {
        if (this.mediaShareService != null) {
            this.titleEdit.setText(this.mediaShareService.getTitle(this.currentMedia));
            this.descriptionEdit.setText(this.mediaShareService.getVideoDescription());
            this.tagsEdit.setText(this.mediaShareService.getTags(this.currentMedia));
        }
    }

    public static void start(Context context, MediaVO mediaVO) {
        Intent intent = new Intent(context, VideoUploadActivity.class);
        intent.putExtra(EXTRA_UPLOADVIDEO_FILENAME, mediaVO);
        context.startActivity(intent);
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.uploadUpdateReceiver);
        Log.d(TAG, "Receiver UNregistered");
    }

    private void uploadVideo() {
        if (this.mediaShareService.uploadVideoFile(this.currentMedia, this.titleEdit.getText().toString(), this.descriptionEdit.getText().toString(), this.tagsEdit.getText().toString(), this.privateButton.isChecked(), this.currentCategory)) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            if (getSupportFragmentManager().findFragmentByTag("uploading") == null) {
                beginTransaction.addToBackStack(null);
                MediaLoadingDialog mediaLoadingDialog = new MediaLoadingDialog();
                mediaLoadingDialog.setDelegate(this);
                mediaLoadingDialog.setIsVideo(true);
                mediaLoadingDialog.setCancelable(false);
                mediaLoadingDialog.show(beginTransaction, "uploading");
                mediaLoadingDialog.setProgress(0);
                this.loadingDialog = mediaLoadingDialog;
                return;
            }
            return;
        }
        Toast.makeText(this, getString(C0984R.string.ff_ID000211), 0).show();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.currentMedia = (MediaVO) getIntent().getExtras().getParcelable(EXTRA_UPLOADVIDEO_FILENAME);
        this.currentCategory = null;
        this.uploadWasStarted = false;
        this.videoCategories = new VideoCategoryVO[]{new VideoCategoryVO(null, getString(C0984R.string.ff_ID000010))};
        setContentView(C0984R.layout.video_upload);
        findViewById(16908290).setOnTouchListener(this.OnTouchOutOfFocusListener);
        this.titleEdit = (EditText) findViewById(C0984R.id.videoUploadTitle);
        this.descriptionEdit = (EditText) findViewById(C0984R.id.videoUploadDescription);
        this.tagsEdit = (EditText) findViewById(C0984R.id.videoUploadTags);
        this.uploadNowButton = (Button) findViewById(C0984R.id.videoUploadButton);
        this.privateButton = (RadioButton) findViewById(C0984R.id.videoUploadPrivateButton);
        this.spinnerVideoCategories = (Spinner) findViewById(C0984R.id.videoUploadCategorySpinner);
        this.progressCategories = (ProgressBar) findViewById(C0984R.id.videoUploadCategoryProgress);
        this.videoCategoriesAdapter = new ArrayAdapter(this, C0984R.layout.view_google_settings_spinner_item, this.videoCategories);
        this.categoriesReady = false;
        this.spinnerVideoCategories.setAdapter(this.videoCategoriesAdapter);
        this.spinnerVideoCategories.setEnabled(false);
        this.mediaShareServiceConnection = new MediaShareServiceConnection();
        if (!bindService(new Intent(this, MediaShareService.class), this.mediaShareServiceConnection, 1)) {
            Log.w(TAG, "Unable to bind to MediaShareService");
        }
        this.uploadNowButton.setEnabled(false);
        this.uploadNowButton.setOnClickListener(new C11261());
        this.spinnerVideoCategories.setOnItemSelectedListener(new C11272());
        initActionBar();
        initTextFields();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.getYouTubeVideoCategoriesTask != null) {
            this.getYouTubeVideoCategoriesTask.cancel(true);
            Log.i("VideoUpload", "Status =" + this.getYouTubeVideoCategoriesTask.getStatus());
        }
        if (this.mediaShareService != null) {
            Log.i("VideoUpload", "unbindService(mediaShareServiceConnection)");
            unbindService(this.mediaShareServiceConnection);
        }
    }

    public void onDialogClosed() {
        finish();
    }

    public void onDialogDidCancel() {
    }

    public void onDialogWillCancel() {
        if (this.mediaShareService != null) {
            final MediaLoadingDialog mediaLoadingDialog = this.loadingDialog;
            AsyncTask c11304 = new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    VideoUploadActivity.this.mediaShareService.waitForCancellation();
                    return null;
                }

                protected void onPostExecute(Void voidR) {
                    mediaLoadingDialog.dismiss(true);
                }

                protected void onPreExecute() {
                    mediaLoadingDialog.setText(VideoUploadActivity.this.getString(C0984R.string.ff_ID000010));
                    mediaLoadingDialog.setProgress(-1);
                }
            };
            this.loadingDialog = null;
            this.uploadWasStarted = false;
            this.mediaShareService.abortCurrentUpload();
            c11304.execute(new Void[0]);
        }
    }

    public void onMediaShareStateChanged(MediaVO mediaVO, boolean z, double d) {
        if (this.loadingDialog == null || mediaVO != this.currentMedia) {
            return;
        }
        if (z) {
            this.loadingDialog.setProgress((int) (100.0d * d));
            this.uploadWasStarted = true;
        } else if (this.uploadWasStarted) {
            this.loadingDialog.setText(getString(C0984R.string.ff_ID000058));
            this.loadingDialog.dismissAfter(DIALOG_TIMEOUT);
            this.uploadWasStarted = false;
            DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MEDIA_SHARED_VIDEO);
        }
    }

    protected void onStart() {
        super.onStart();
        initBroadcastReceiver();
        if (MediaShareService.getState() != State.UPLOADING) {
            if (this.loadingDialog != null) {
                this.loadingDialog.dismiss();
                this.loadingDialog = null;
            }
            this.uploadWasStarted = false;
        }
    }

    protected void onStop() {
        if (this.pausedAutoUpload) {
            getAppSettings().setAutoVideoUploadEnabled(true);
        }
        unregisterBroadcastReceiver();
        super.onStop();
    }

    public void pauseMediaShareService() {
        ApplicationSettings appSettings = getAppSettings();
        if (appSettings.isAutoVideoUploadEnabled()) {
            appSettings.setAutoVideoUploadEnabled(false);
            this.pausedAutoUpload = true;
        }
    }
}
