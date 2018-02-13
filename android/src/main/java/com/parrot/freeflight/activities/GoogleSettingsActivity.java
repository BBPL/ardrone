package com.parrot.freeflight.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.ToggleButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.picasa.PicasaScopes;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Builder;
import com.google.api.services.youtube.YouTubeScopes;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.settings.AccountChecker;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.tasks.CheckDroneNetworkAvailabilityTask;
import com.parrot.freeflight.tasks.GetYouTubeVideoCategoriesTask;
import com.parrot.freeflight.utils.AsyncTaskResult;
import com.parrot.freeflight.utils.EnhancedArrayAdapter;
import com.parrot.freeflight.vo.VideoCategoryVO;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

public class GoogleSettingsActivity extends ParrotActivity implements OnCheckedChangeListener, OnClickListener, OnItemSelectedListener {
    boolean categoriesReady;
    private ExecutorService executor;
    private GetYouTubeVideoCategoriesTask getYouTubeVideoCategoriesTask;
    private GoogleAccountCredential googleCredential;
    private final JsonFactory jsonFactory = new GsonFactory();
    private Spinner spinnerVideoCategories;
    private HttpTransport transport;
    private VideoCategoryVO[] videoCategories = new VideoCategoryVO[]{new VideoCategoryVO(null, "loading...")};
    private ArrayAdapter<VideoCategoryVO> videoCategoriesAdapter;
    private YouTube youtube;

    class C10761 extends GetYouTubeVideoCategoriesTask {
        C10761() {
        }

        protected void onPostExecute(AsyncTaskResult<VideoCategoryVO[]> asyncTaskResult) {
            int i = 0;
            Log.i("GoogleSettingsActivity", "onPostExecute");
            if (asyncTaskResult.succeeded()) {
                GoogleSettingsActivity.this.categoriesReady = true;
                GoogleSettingsActivity.this.videoCategoriesAdapter = new EnhancedArrayAdapter(GoogleSettingsActivity.this, C0984R.layout.view_google_settings_spinner_item, (Object[]) asyncTaskResult.result);
                GoogleSettingsActivity.this.videoCategoriesAdapter.setDropDownViewResource(17367043);
                GoogleSettingsActivity.this.spinnerVideoCategories.setAdapter(GoogleSettingsActivity.this.videoCategoriesAdapter);
                GoogleSettingsActivity.this.spinnerVideoCategories.setEnabled(true);
                String youTubeVideoCategoryId = GoogleSettingsActivity.this.getAppSettings().getYouTubeVideoCategoryId();
                if (youTubeVideoCategoryId != null) {
                    int length = ((VideoCategoryVO[]) asyncTaskResult.result).length;
                    int i2 = -1;
                    while (i < length) {
                        int i3 = ((VideoCategoryVO[]) asyncTaskResult.result)[i].id.equals(youTubeVideoCategoryId) ? i : i2;
                        i++;
                        i2 = i3;
                    }
                    if (i2 != -1) {
                        GoogleSettingsActivity.this.spinnerVideoCategories.setSelection(i2);
                    }
                }
            } else if (asyncTaskResult.exception instanceof UserRecoverableAuthIOException) {
                Intent intent = ((UserRecoverableAuthIOException) asyncTaskResult.exception).getIntent();
                if (intent != null) {
                    GoogleSettingsActivity.this.startActivityForResult(intent, 1);
                    GoogleSettingsActivity.this.setResult(1);
                }
                GoogleSettingsActivity.this.finish();
            } else {
                GoogleSettingsActivity.this.videoCategories[0] = new VideoCategoryVO(null, GoogleSettingsActivity.this.getString(C0984R.string.ff_ID000057));
                GoogleSettingsActivity.this.spinnerVideoCategories.setAdapter(new EnhancedArrayAdapter(GoogleSettingsActivity.this, C0984R.layout.view_google_settings_spinner_item, GoogleSettingsActivity.this.videoCategories));
                GoogleSettingsActivity.this.videoCategoriesAdapter.notifyDataSetChanged();
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("GoogleSettingsActivity", "onPreExecute");
            GoogleSettingsActivity.this.categoriesReady = false;
            GoogleSettingsActivity.this.videoCategories[0] = new VideoCategoryVO(null, GoogleSettingsActivity.this.getString(C0984R.string.ff_ID000055));
            GoogleSettingsActivity.this.spinnerVideoCategories.setAdapter(new EnhancedArrayAdapter(GoogleSettingsActivity.this, C0984R.layout.view_google_settings_spinner_item, GoogleSettingsActivity.this.videoCategories));
            GoogleSettingsActivity.this.spinnerVideoCategories.setEnabled(false);
        }
    }

    class C10772 extends CheckDroneNetworkAvailabilityTask {
        C10772() {
        }

        protected void onPostExecute(Boolean bool) {
            if (bool.booleanValue()) {
                GoogleSettingsActivity.this.videoCategories[0] = new VideoCategoryVO(null, GoogleSettingsActivity.this.getString(C0984R.string.ff_ID000057));
                GoogleSettingsActivity.this.spinnerVideoCategories.setAdapter(new EnhancedArrayAdapter(GoogleSettingsActivity.this, C0984R.layout.view_google_settings_spinner_item, GoogleSettingsActivity.this.videoCategories));
                GoogleSettingsActivity.this.videoCategoriesAdapter.notifyDataSetChanged();
                return;
            }
            GoogleSettingsActivity.this.refreshVideoCategories();
        }
    }

    private void checkDroneConnectivityToRefreshVideoCategories() {
        new C10772().execute(new Context[]{this});
    }

    private void initActionBar() {
        setTitle(getString(C0984R.string.ff_ID000201).toUpperCase());
        getParrotActionBar().initBackButton();
    }

    private void initUi() {
        ApplicationSettings appSettings = getAppSettings();
        ToggleButton toggleButton = (ToggleButton) findViewById(C0984R.id.activity_google_settings_toggle_picasaupload);
        toggleButton.setChecked(appSettings.isAutoPhotoUploadEnabled());
        toggleButton.setOnCheckedChangeListener(this);
        toggleButton = (ToggleButton) findViewById(C0984R.id.activity_google_settings_toggle_youtube_upload);
        toggleButton.setChecked(appSettings.isAutoVideoUploadEnabled());
        toggleButton.setOnCheckedChangeListener(this);
        this.spinnerVideoCategories = (Spinner) findViewById(C0984R.id.activity_google_settings_spinner_videocategory);
        this.videoCategoriesAdapter = new ArrayAdapter(this, C0984R.layout.view_google_settings_spinner_item, this.videoCategories);
        this.categoriesReady = false;
        this.spinnerVideoCategories.setAdapter(this.videoCategoriesAdapter);
        this.spinnerVideoCategories.setEnabled(false);
        this.spinnerVideoCategories.setOnItemSelectedListener(this);
        ((Button) findViewById(C0984R.id.activity_google_settings_button_signoff)).setOnClickListener(this);
    }

    private void initYouTubeClient() {
        this.transport = AndroidHttp.newCompatibleTransport();
        this.googleCredential = GoogleAccountCredential.usingOAuth2(this, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_UPLOAD, PicasaScopes.DATA);
        this.googleCredential.setSelectedAccountName(AccountChecker.getGoogleAccountName(getAppSettings(), getApplicationContext()));
        this.youtube = new Builder(this.transport, this.jsonFactory, this.googleCredential).setApplicationName(getString(2131165184)).build();
    }

    private void onPicasaUploadCheckedChanged(CompoundButton compoundButton, boolean z) {
        getAppSettings().setAutoPhotoUploadEnabled(z);
    }

    private void onSignoffClicked(View view) {
        ((FreeFlightApplication) getApplication()).getAppSettings().setGoogleAccountName(null);
        finish();
    }

    private void onYoutubeUploadCheckedChanged(CompoundButton compoundButton, boolean z) {
        getAppSettings().setAutoVideoUploadEnabled(z);
    }

    private void refreshVideoCategories() {
        this.getYouTubeVideoCategoriesTask = new C10761();
        if (VERSION.SDK_INT < 11) {
            this.getYouTubeVideoCategoriesTask.execute(new Object[]{this, this.youtube});
            return;
        }
        this.getYouTubeVideoCategoriesTask.executeOnExecutor(this.executor, new Object[]{this, this.youtube});
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, GoogleSettingsActivity.class));
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        switch (i) {
            case 1:
                if (i2 == -1) {
                    checkDroneConnectivityToRefreshVideoCategories();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        switch (compoundButton.getId()) {
            case C0984R.id.activity_google_settings_toggle_picasaupload /*2131362022*/:
                onPicasaUploadCheckedChanged(compoundButton, z);
                return;
            case C0984R.id.activity_google_settings_toggle_youtube_upload /*2131362024*/:
                onYoutubeUploadCheckedChanged(compoundButton, z);
                return;
            default:
                onNotifyNotImplemented();
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.activity_google_settings_button_signoff /*2131362027*/:
                onSignoffClicked(view);
                return;
            default:
                onNotifyNotImplemented();
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_google_settings);
        this.executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory(), new DiscardPolicy());
        initActionBar();
        initUi();
        initYouTubeClient();
        checkDroneConnectivityToRefreshVideoCategories();
    }

    protected void onDestroy() {
        Log.i("GoogleSettingsActivity", "onDestroy");
        this.executor.shutdownNow();
        if (this.getYouTubeVideoCategoriesTask != null) {
            this.getYouTubeVideoCategoriesTask.cancel(true);
        }
        super.onDestroy();
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.categoriesReady) {
            getAppSettings().setYouTubeVideoCategoryId(((VideoCategoryVO) adapterView.getItemAtPosition(i)).id);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
}
