package com.parrot.freeflight.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.google.api.services.picasa.Picasa;
import com.google.api.services.picasa.model.AlbumEntry;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.mediauploadservice.MediaShareService;
import com.parrot.freeflight.mediauploadservice.MediaShareService.LocalBinder;
import com.parrot.freeflight.mediauploadservice.broadcastreceivers.MediaShareStateChangedReceiver;
import com.parrot.freeflight.mediauploadservice.broadcastreceivers.MediaShareStateChangedReceiverDelegate;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.tasks.CheckDroneNetworkAvailabilityTask;
import com.parrot.freeflight.tasks.CreatePicasaAlbumTask;
import com.parrot.freeflight.tasks.GetPicasaAlbumsTask;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.EnhancedArrayAdapter;
import com.parrot.freeflight.vo.MediaVO;
import java.util.List;
import org.mortbay.jetty.HttpVersions;

public class PhotoUploadActivity extends ParrotActivity implements MediaShareStateChangedReceiverDelegate, MediaLoadingDialogDelegate {
    private static final long DIALOG_TIMEOUT = 1000;
    public static final String EXTRA_UPLOADPHOTO_FILENAME = "uploadphoto.extra.filename";
    private static final String TAG = PhotoUploadActivity.class.getSimpleName();
    private OnTouchListener OnTouchOutOfFocusListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionIndex() == 0) {
                switch (motionEvent.getAction()) {
                    case 0:
                        PhotoUploadActivity.this.firstPointTouch.x = (int) motionEvent.getX(0);
                        PhotoUploadActivity.this.firstPointTouch.y = (int) motionEvent.getY(0);
                        break;
                    case 1:
                        if (PhotoUploadActivity.this.wantToCreateNewAlbum) {
                            PhotoUploadActivity.this.wantToCreateNewAlbum = false;
                            PhotoUploadActivity.this.checkDroneConnectivityToinitAlbumsSpinner();
                        }
                        if (!(PhotoUploadActivity.this.getCurrentFocus() == null || PhotoUploadActivity.this.getCurrentFocus() == view || PhotoUploadActivity.this.movedEnough)) {
                            ((InputMethodManager) PhotoUploadActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(PhotoUploadActivity.this.getCurrentFocus().getWindowToken(), 2);
                        }
                        PhotoUploadActivity.this.movedEnough = false;
                        break;
                    case 2:
                        if (Math.pow((double) (motionEvent.getX(0) - ((float) PhotoUploadActivity.this.firstPointTouch.x)), 2.0d) + Math.pow((double) (motionEvent.getY(0) - ((float) PhotoUploadActivity.this.firstPointTouch.y)), 2.0d) > Math.pow((double) PhotoUploadActivity.this.movementPrecision, 2.0d)) {
                            PhotoUploadActivity.this.movedEnough = true;
                            break;
                        }
                        break;
                }
            }
            return true;
        }
    };
    private ActionBar actionBar;
    private EditText albumEdit;
    private boolean albumsReady;
    private AlbumEntry currentAlbum;
    private MediaVO currentMedia;
    private EditText descriptionEdit;
    private Point firstPointTouch = new Point();
    private MediaLoadingDialog loadingDialog;
    private MediaShareService mediaShareService;
    private MediaShareServiceConnection mediaShareServiceConnection;
    private boolean movedEnough = false;
    private int movementPrecision = 10;
    private boolean pausedAutoUpload = false;
    private AlbumEntry[] photoAlbums;
    private ArrayAdapter<AlbumEntry> photoAlbumsAdapter;
    private ImageButton photoUploadAdd;
    private RadioButton privateButton;
    private ProgressBar progressAlbums;
    private RadioButton publicButton;
    private Spinner spinnerPhotoAlbums;
    private EditText tagsEdit;
    private EditText titleEdit;
    private Button uploadNowButton;
    private BroadcastReceiver uploadUpdateReceiver;
    private boolean uploadWasStarted;
    private boolean wantToCreateNewAlbum;

    class C10941 implements OnClickListener {
        C10941() {
        }

        public void onClick(View view) {
            PhotoUploadActivity.this.pauseMediaShareService();
            PhotoUploadActivity.this.uploadPhoto();
        }
    }

    class C10952 implements OnItemSelectedListener {
        C10952() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (PhotoUploadActivity.this.albumsReady) {
                PhotoUploadActivity.this.currentAlbum = (AlbumEntry) adapterView.getItemAtPosition(i);
                PhotoUploadActivity.this.albumEdit.setText(PhotoUploadActivity.this.currentAlbum.getTitle());
                if (PhotoUploadActivity.this.currentAlbum.getRights().equals("public")) {
                    PhotoUploadActivity.this.publicButton.setChecked(true);
                } else {
                    PhotoUploadActivity.this.privateButton.setChecked(true);
                }
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            PhotoUploadActivity.this.currentAlbum = null;
        }
    }

    class C10963 implements OnEditorActionListener {
        C10963() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i) {
                case 0:
                case 2:
                case 6:
                    if (!PhotoUploadActivity.this.wantToCreateNewAlbum || PhotoUploadActivity.this.albumEdit.getText().toString().equals(HttpVersions.HTTP_0_9)) {
                        PhotoUploadActivity.this.checkDroneConnectivityToinitAlbumsSpinner();
                    } else {
                        PhotoUploadActivity.this.addAlbum(PhotoUploadActivity.this.albumEdit.getText().toString());
                    }
                    PhotoUploadActivity.this.wantToCreateNewAlbum = false;
                    return true;
                default:
                    return false;
            }
        }
    }

    class C10984 implements OnFocusChangeListener {

        class C10971 implements Runnable {
            C10971() {
            }

            public void run() {
                ((InputMethodManager) PhotoUploadActivity.this.getSystemService("input_method")).showSoftInput(PhotoUploadActivity.this.albumEdit, 1);
            }
        }

        C10984() {
        }

        public void onFocusChange(View view, boolean z) {
            if (z) {
                PhotoUploadActivity.this.albumEdit.post(new C10971());
                PhotoUploadActivity.this.albumEdit.setText(HttpVersions.HTTP_0_9);
                PhotoUploadActivity.this.uploadNowButton.setEnabled(false);
                PhotoUploadActivity.this.privateButton.setEnabled(true);
                PhotoUploadActivity.this.publicButton.setEnabled(true);
                PhotoUploadActivity.this.publicButton.setChecked(true);
                PhotoUploadActivity.this.wantToCreateNewAlbum = true;
            } else if (PhotoUploadActivity.this.wantToCreateNewAlbum) {
                PhotoUploadActivity.this.wantToCreateNewAlbum = false;
                PhotoUploadActivity.this.checkDroneConnectivityToinitAlbumsSpinner();
            }
        }
    }

    class C10995 implements OnClickListener {
        C10995() {
        }

        public void onClick(View view) {
            PhotoUploadActivity.this.albumEdit.requestFocus();
        }
    }

    class C11016 extends GetPicasaAlbumsTask {

        class C11001 extends CreatePicasaAlbumTask {
            C11001() {
            }

            protected void onPostExecute(AlbumEntry albumEntry) {
                super.onPostExecute(albumEntry);
                if (albumEntry != null) {
                    PhotoUploadActivity.this.albumsReady = true;
                    PhotoUploadActivity.this.photoAlbums[0] = albumEntry;
                    PhotoUploadActivity.this.photoAlbumsAdapter = new EnhancedArrayAdapter(PhotoUploadActivity.this, C0984R.layout.view_google_settings_spinner_item, PhotoUploadActivity.this.photoAlbums);
                    PhotoUploadActivity.this.photoAlbumsAdapter.setDropDownViewResource(17367043);
                    PhotoUploadActivity.this.spinnerPhotoAlbums.setAdapter(PhotoUploadActivity.this.photoAlbumsAdapter);
                    PhotoUploadActivity.this.spinnerPhotoAlbums.setEnabled(true);
                    PhotoUploadActivity.this.uploadNowButton.setEnabled(true);
                    PhotoUploadActivity.this.uploadNowButton.requestFocus();
                    PhotoUploadActivity.this.privateButton.setEnabled(false);
                    PhotoUploadActivity.this.publicButton.setEnabled(false);
                    PhotoUploadActivity.this.photoUploadAdd.setEnabled(true);
                    PhotoUploadActivity.this.currentAlbum = PhotoUploadActivity.this.photoAlbums[0];
                    PhotoUploadActivity.this.albumEdit.setText(PhotoUploadActivity.this.currentAlbum.getTitle());
                    PhotoUploadActivity.this.albumEdit.setEnabled(true);
                    if (PhotoUploadActivity.this.currentAlbum.getRights().equals("public")) {
                        PhotoUploadActivity.this.publicButton.setChecked(true);
                    } else {
                        PhotoUploadActivity.this.privateButton.setChecked(true);
                    }
                    PhotoUploadActivity.this.spinnerPhotoAlbums.setSelection(0);
                } else {
                    PhotoUploadActivity.this.photoAlbums[0] = new AlbumEntry();
                    PhotoUploadActivity.this.photoAlbums[0].setTitle(PhotoUploadActivity.this.getString(C0984R.string.ff_ID000057));
                    PhotoUploadActivity.this.spinnerPhotoAlbums.setAdapter(new EnhancedArrayAdapter(PhotoUploadActivity.this, C0984R.layout.view_google_settings_spinner_item, PhotoUploadActivity.this.photoAlbums));
                    PhotoUploadActivity.this.photoAlbumsAdapter.notifyDataSetChanged();
                }
                PhotoUploadActivity.this.progressAlbums.setVisibility(8);
            }

            protected void onPreExecute() {
                super.onPreExecute();
                PhotoUploadActivity.this.progressAlbums.setVisibility(0);
            }
        }

        C11016() {
        }

        protected void onPostExecute(List<AlbumEntry> list) {
            super.onPostExecute(list);
            if (list == null || list.size() == 0) {
                CreatePicasaAlbumTask c11001 = new C11001();
                if (PhotoUploadActivity.this.mediaShareService.getPicasaInstance() != null) {
                    c11001.execute(new Object[]{PhotoUploadActivity.this, PhotoUploadActivity.this.mediaShareService.getPicasaInstance(), PhotoUploadActivity.this.getString(C0984R.string.ff_ID000040), Boolean.FALSE});
                } else {
                    Toast.makeText(PhotoUploadActivity.this, "Not connected to a google account !", 0).show();
                }
            } else {
                PhotoUploadActivity.this.albumsReady = true;
                PhotoUploadActivity.this.photoAlbums = new AlbumEntry[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    PhotoUploadActivity.this.photoAlbums[i] = (AlbumEntry) list.get(i);
                }
                PhotoUploadActivity.this.photoAlbumsAdapter = new EnhancedArrayAdapter(PhotoUploadActivity.this, C0984R.layout.view_google_settings_spinner_item, PhotoUploadActivity.this.photoAlbums);
                PhotoUploadActivity.this.photoAlbumsAdapter.setDropDownViewResource(17367043);
                PhotoUploadActivity.this.spinnerPhotoAlbums.setAdapter(PhotoUploadActivity.this.photoAlbumsAdapter);
                PhotoUploadActivity.this.spinnerPhotoAlbums.setEnabled(true);
                PhotoUploadActivity.this.uploadNowButton.setEnabled(true);
                PhotoUploadActivity.this.uploadNowButton.requestFocus();
                PhotoUploadActivity.this.privateButton.setEnabled(false);
                PhotoUploadActivity.this.publicButton.setEnabled(false);
                PhotoUploadActivity.this.photoUploadAdd.setEnabled(true);
                PhotoUploadActivity.this.progressAlbums.setVisibility(8);
                PhotoUploadActivity.this.currentAlbum = PhotoUploadActivity.this.photoAlbums[0];
                PhotoUploadActivity.this.albumEdit.setText(PhotoUploadActivity.this.currentAlbum.getTitle());
                PhotoUploadActivity.this.albumEdit.setEnabled(true);
                if (PhotoUploadActivity.this.currentAlbum.getRights().equals("public")) {
                    PhotoUploadActivity.this.publicButton.setChecked(true);
                } else {
                    PhotoUploadActivity.this.privateButton.setChecked(true);
                }
                PhotoUploadActivity.this.spinnerPhotoAlbums.setSelection(0);
            }
            PhotoUploadActivity.this.progressAlbums.setVisibility(8);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PhotoUploadActivity.this.albumsReady = false;
            PhotoUploadActivity.this.photoAlbums[0] = new AlbumEntry();
            PhotoUploadActivity.this.photoAlbums[0].setTitle(PhotoUploadActivity.this.getString(C0984R.string.ff_ID000042));
            PhotoUploadActivity.this.albumEdit.setText(PhotoUploadActivity.this.photoAlbums[0].getTitle());
            PhotoUploadActivity.this.albumEdit.setEnabled(false);
            PhotoUploadActivity.this.spinnerPhotoAlbums.setAdapter(new EnhancedArrayAdapter(PhotoUploadActivity.this, C0984R.layout.view_google_settings_spinner_item, PhotoUploadActivity.this.photoAlbums));
            PhotoUploadActivity.this.progressAlbums.setVisibility(0);
            PhotoUploadActivity.this.spinnerPhotoAlbums.setEnabled(false);
        }
    }

    class C11027 extends CreatePicasaAlbumTask {
        C11027() {
        }

        protected void onPostExecute(AlbumEntry albumEntry) {
            PhotoUploadActivity.this.loadingDialog.setText(PhotoUploadActivity.this.getString(C0984R.string.ff_ID000044));
            PhotoUploadActivity.this.checkDroneConnectivityToinitAlbumsSpinner();
            PhotoUploadActivity.this.loadingDialog.dismissAfter(PhotoUploadActivity.DIALOG_TIMEOUT);
        }

        protected void onPreExecute() {
            FragmentTransaction beginTransaction = PhotoUploadActivity.this.getSupportFragmentManager().beginTransaction();
            if (PhotoUploadActivity.this.getSupportFragmentManager().findFragmentByTag("creating album") == null) {
                beginTransaction.addToBackStack(null);
                MediaLoadingDialog mediaLoadingDialog = new MediaLoadingDialog();
                mediaLoadingDialog.setIsVideo(false);
                mediaLoadingDialog.setDelegate(PhotoUploadActivity.this);
                mediaLoadingDialog.setCancelable(false);
                mediaLoadingDialog.setProgress(-1);
                mediaLoadingDialog.setText(PhotoUploadActivity.this.getString(C0984R.string.ff_ID000043));
                mediaLoadingDialog.show(beginTransaction, "creating album");
                PhotoUploadActivity.this.loadingDialog = mediaLoadingDialog;
            }
        }
    }

    class C11049 extends CheckDroneNetworkAvailabilityTask {
        C11049() {
        }

        protected void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                Log.i(PhotoUploadActivity.TAG, "Can't initAlbumsSpinner because Drone on Network");
                PhotoUploadActivity.this.photoAlbums[0] = new AlbumEntry();
                PhotoUploadActivity.this.photoAlbums[0].setTitle(PhotoUploadActivity.this.getString(C0984R.string.ff_ID000046));
                PhotoUploadActivity.this.albumEdit.setText(PhotoUploadActivity.this.photoAlbums[0].getTitle());
                PhotoUploadActivity.this.albumEdit.setEnabled(false);
                PhotoUploadActivity.this.spinnerPhotoAlbums.setAdapter(new EnhancedArrayAdapter(PhotoUploadActivity.this, C0984R.layout.view_google_settings_spinner_item, PhotoUploadActivity.this.photoAlbums));
                PhotoUploadActivity.this.photoAlbumsAdapter.notifyDataSetChanged();
                return;
            }
            PhotoUploadActivity.this.initAlbumsSpinner();
        }
    }

    private class MediaShareServiceConnection implements ServiceConnection {
        private final String TAG;

        private MediaShareServiceConnection() {
            this.TAG = MediaShareServiceConnection.class.getSimpleName();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(this.TAG, "Connected to Media Share Service");
            PhotoUploadActivity.this.mediaShareService = ((LocalBinder) iBinder).getService();
            PhotoUploadActivity.this.mediaShareService.resendBroadcasts();
            PhotoUploadActivity.this.initTextFields();
            PhotoUploadActivity.this.checkDroneConnectivityToinitAlbumsSpinner();
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    private class PhotoUploadCancelAsyncTask extends AsyncTask<Object, Void, Void> {
        private PhotoUploadCancelAsyncTask() {
        }

        protected Void doInBackground(Object... objArr) {
            PhotoUploadActivity.this.mediaShareService.waitForCancellation();
            return null;
        }
    }

    private void addAlbum(String str) {
        CreatePicasaAlbumTask c11027 = new C11027();
        Picasa picasaInstance = this.mediaShareService.getPicasaInstance();
        Boolean bool = this.privateButton.isChecked() ? Boolean.TRUE : Boolean.FALSE;
        c11027.execute(new Object[]{this, picasaInstance, str, bool});
    }

    private void checkDroneConnectivityToinitAlbumsSpinner() {
        new C11049().execute(new Context[]{this});
    }

    private void initActionBar() {
        this.actionBar = getParrotActionBar();
        setTitle(getString(C0984R.string.ff_ID000041).toUpperCase());
        this.actionBar.initBackButton();
    }

    private void initAlbumsSpinner() {
        GetPicasaAlbumsTask c11016 = new C11016();
        if (this.mediaShareService.getPicasaInstance() != null) {
            c11016.execute(new Object[]{this, this.mediaShareService.getPicasaInstance()});
            return;
        }
        Toast.makeText(this, "Not connected to a google account !", 0).show();
    }

    private void initBroadcastReceiver() {
        this.uploadUpdateReceiver = new MediaShareStateChangedReceiver(this);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.uploadUpdateReceiver, MediaShareStateChangedReceiver.createFilter());
        Log.d(TAG, "Receiver registered");
    }

    private void initTextFields() {
        if (this.mediaShareService != null) {
            this.titleEdit.setText(this.mediaShareService.getTitle(this.currentMedia));
            this.descriptionEdit.setText(this.mediaShareService.getPhotoDescription());
            this.tagsEdit.setText(this.mediaShareService.getTags(this.currentMedia));
        }
    }

    public static void start(Context context, MediaVO mediaVO) {
        Intent intent = new Intent(context, PhotoUploadActivity.class);
        intent.putExtra(EXTRA_UPLOADPHOTO_FILENAME, mediaVO);
        context.startActivity(intent);
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.uploadUpdateReceiver);
        Log.d(TAG, "Receiver UNregistered");
    }

    private void uploadPhoto() {
        if (this.mediaShareService.uploadPhotoFile(this.currentMedia, this.titleEdit.getText().toString(), this.descriptionEdit.getText().toString(), this.tagsEdit.getText().toString(), this.currentAlbum.getAlbumId())) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            if (getSupportFragmentManager().findFragmentByTag("uploading") == null) {
                beginTransaction.addToBackStack(null);
                MediaLoadingDialog mediaLoadingDialog = new MediaLoadingDialog();
                mediaLoadingDialog.setDelegate(this);
                mediaLoadingDialog.setIsVideo(false);
                mediaLoadingDialog.setCancelable(false);
                mediaLoadingDialog.setProgress(0);
                mediaLoadingDialog.show(beginTransaction, "uploading");
                this.loadingDialog = mediaLoadingDialog;
                return;
            }
            return;
        }
        Toast.makeText(this, getString(C0984R.string.ff_ID000216), 0).show();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.currentMedia = (MediaVO) getIntent().getExtras().getParcelable(EXTRA_UPLOADPHOTO_FILENAME);
        this.currentAlbum = null;
        this.wantToCreateNewAlbum = false;
        this.uploadWasStarted = false;
        setContentView(C0984R.layout.photo_upload);
        findViewById(16908290).setOnTouchListener(this.OnTouchOutOfFocusListener);
        this.photoAlbums = new AlbumEntry[]{new AlbumEntry()};
        this.photoAlbums[0].setTitle(getString(C0984R.string.ff_ID000010));
        this.titleEdit = (EditText) findViewById(C0984R.id.photoUploadTitle);
        this.descriptionEdit = (EditText) findViewById(C0984R.id.photoUploadDescription);
        this.tagsEdit = (EditText) findViewById(C0984R.id.photoUploadTags);
        this.uploadNowButton = (Button) findViewById(C0984R.id.photoUploadButton);
        this.privateButton = (RadioButton) findViewById(C0984R.id.photoUploadPrivateButton);
        this.publicButton = (RadioButton) findViewById(C0984R.id.photoUploadPublicButton);
        this.privateButton.setEnabled(false);
        this.publicButton.setEnabled(false);
        this.spinnerPhotoAlbums = (Spinner) findViewById(C0984R.id.photoUploadAlbumSpinner);
        this.progressAlbums = (ProgressBar) findViewById(C0984R.id.photoUploadAlbumProgress);
        this.photoAlbumsAdapter = new ArrayAdapter(this, C0984R.layout.view_google_settings_spinner_item, this.photoAlbums);
        this.albumsReady = false;
        this.spinnerPhotoAlbums.setAdapter(this.photoAlbumsAdapter);
        this.spinnerPhotoAlbums.setEnabled(false);
        this.mediaShareServiceConnection = new MediaShareServiceConnection();
        if (!bindService(new Intent(this, MediaShareService.class), this.mediaShareServiceConnection, 1)) {
            Log.w(TAG, "Unable to bind to MediaShareService");
        }
        this.uploadNowButton.setEnabled(false);
        this.uploadNowButton.setOnClickListener(new C10941());
        this.spinnerPhotoAlbums.setOnItemSelectedListener(new C10952());
        this.albumEdit = (EditText) findViewById(C0984R.id.photoUploadAlbumEdit);
        this.albumEdit.setText(this.photoAlbums[0].getTitle());
        this.albumEdit.setEnabled(false);
        this.albumEdit.setFocusable(true);
        this.albumEdit.setHint("New Album");
        this.albumEdit.setOnEditorActionListener(new C10963());
        this.albumEdit.setOnFocusChangeListener(new C10984());
        this.photoUploadAdd = (ImageButton) findViewById(C0984R.id.photoUploadAdd);
        this.photoUploadAdd.setEnabled(false);
        this.photoUploadAdd.setOnClickListener(new C10995());
        initActionBar();
        initTextFields();
        initBroadcastReceiver();
    }

    public void onDialogClosed() {
        finish();
    }

    public void onDialogDidCancel() {
    }

    public void onDialogWillCancel() {
        if (this.mediaShareService != null) {
            final MediaLoadingDialog mediaLoadingDialog = this.loadingDialog;
            AsyncTask c11038 = new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... voidArr) {
                    PhotoUploadActivity.this.mediaShareService.waitForCancellation();
                    return null;
                }

                protected void onPostExecute(Void voidR) {
                    mediaLoadingDialog.dismiss(true);
                }

                protected void onPreExecute() {
                    mediaLoadingDialog.setText(PhotoUploadActivity.this.getString(C0984R.string.ff_ID000010));
                    mediaLoadingDialog.setProgress(-1);
                }
            };
            this.loadingDialog = null;
            this.uploadWasStarted = false;
            this.mediaShareService.abortCurrentUpload();
            c11038.execute(new Void[0]);
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
            this.loadingDialog.setText(getString(C0984R.string.ff_ID000049));
            this.loadingDialog.dismissAfter(DIALOG_TIMEOUT);
            this.uploadWasStarted = false;
            DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MEDIA_SHARED_PHOTO);
        }
    }

    protected void onStop() {
        if (this.pausedAutoUpload) {
            getAppSettings().setAutoPhotoUploadEnabled(true);
        }
        unbindService(this.mediaShareServiceConnection);
        unregisterBroadcastReceiver();
        super.onStop();
    }

    public void pauseMediaShareService() {
        ApplicationSettings appSettings = getAppSettings();
        if (appSettings.isAutoPhotoUploadEnabled()) {
            appSettings.setAutoPhotoUploadEnabled(false);
            this.pausedAutoUpload = true;
        }
    }
}
