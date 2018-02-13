package com.parrot.freeflight.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.activities.PhotoUploadActivity;
import com.parrot.freeflight.activities.VideoUploadActivity;
import com.parrot.freeflight.settings.AccountChecker;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.ui.ActionBar.Background;
import com.parrot.freeflight.utils.SystemUtils;
import com.parrot.freeflight.vo.MediaVO;

public abstract class WatchMediaActivity extends ParrotActivity implements OnClickListener {
    public static final String EXTRA_MEDIA_OBJ = "parrot.freeflight.media.obj";
    public static final String EXTRA_MIME_TYPE = "parrot.freeflight.media.mimetype";
    public static final String EXTRA_TITLE = "parrot.freeflight.title";
    private static final int REQUEST_ACCOUNT_PICKER = 1;
    private static final int REQUEST_UPDATE_GOOGLEPLAYSERVICES = 2;
    private static final String TAG = WatchMediaActivity.class.getSimpleName();
    private ActionBar actionBar;
    public Bundle extras;
    private String googleAccountName;
    private GoogleAccountCredential googleCredential;
    private ApplicationSettings settings;

    class C11561 implements OnClickListener {
        C11561() {
        }

        public void onClick(View view) {
            WatchMediaActivity.this.overridePendingTransition(0, 0);
            WatchMediaActivity.this.finish();
        }
    }

    private void initGoogleAccount() {
        this.googleCredential = GoogleAccountCredential.usingOAuth2(this, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_UPLOAD);
    }

    private void onChooseGoogleAccount() {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isGooglePlayServicesAvailable == 0) {
            startActivityForResult(this.googleCredential.newChooseAccountIntent(), 1);
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isGooglePlayServicesAvailable)) {
            GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, this, 2).show();
        }
    }

    private void setGoogleAccountName(String str) {
        this.settings.setGoogleAccountName(str);
        this.googleAccountName = str;
        this.googleCredential.setSelectedAccountName(str);
    }

    protected void initActionBar() {
        String string = this.extras.getString(EXTRA_TITLE);
        this.actionBar = getParrotActionBar();
        this.actionBar.initBackButton(new C11561());
        this.actionBar.changeBackground(Background.ACCENT_HALF_TRANSP);
        MediaVO mediaVO = (MediaVO) this.extras.getParcelable(EXTRA_MEDIA_OBJ);
        if (!(SystemUtils.isNook() || mediaVO == null || mediaVO.isRemote())) {
            this.actionBar.initShareButton(this);
        }
        if (string != null) {
            this.actionBar.setTitle(string);
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        MediaVO mediaVO = (MediaVO) this.extras.getParcelable(EXTRA_MEDIA_OBJ);
        super.onActivityResult(i, i2, intent);
        switch (i) {
            case 1:
                if (i2 == -1 && intent != null && intent.getExtras() != null) {
                    String stringExtra = intent.getStringExtra("authAccount");
                    if (stringExtra != null) {
                        setGoogleAccountName(stringExtra);
                        if (mediaVO.isVideo()) {
                            VideoUploadActivity.start(this, mediaVO);
                            return;
                        } else {
                            PhotoUploadActivity.start(this, mediaVO);
                            return;
                        }
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        if (view.getId() == C0984R.id.btnShare) {
            onShareClicked();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.extras = getIntent().getExtras();
        if (this.extras == null && bundle.containsKey(EXTRA_MEDIA_OBJ)) {
            this.extras = new Bundle(bundle);
        }
        this.settings = ((FreeFlightApplication) getApplication()).getAppSettings();
        this.googleAccountName = AccountChecker.getGoogleAccountName(this.settings, getApplicationContext());
        initGoogleAccount();
        setGoogleAccountName(this.googleAccountName);
    }

    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        initActionBar();
    }

    protected void onResume() {
        overridePendingTransition(0, 0);
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putAll(this.extras);
    }

    protected void onShareClicked() {
        MediaVO mediaVO = (MediaVO) this.extras.getParcelable(EXTRA_MEDIA_OBJ);
        Log.i("MEDIA", "media: " + mediaVO);
        if (mediaVO.isRemote()) {
            Toast.makeText(this, "Can't share remote media", 0).show();
        } else if (mediaVO.isVideo()) {
            if (this.googleAccountName == null) {
                onChooseGoogleAccount();
            } else {
                VideoUploadActivity.start(this, mediaVO);
            }
        } else if (this.googleAccountName == null) {
            onChooseGoogleAccount();
        } else {
            PhotoUploadActivity.start(this, mediaVO);
        }
    }

    public void setTitle(String str) {
        getParrotActionBar().setTitle(str);
    }
}
