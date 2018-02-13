package com.parrot.freeflight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.settings.AccountChecker;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.GooglePlayServicesChecker;

public final class PreferencesActivity extends ParrotActivity implements OnClickListener, OnCheckedChangeListener {
    private static final int REQUEST_ACCOUNT_PICKER = 1;
    private static final int REQUEST_CHECK_GOOGLE_PLAY_SERVICES = 3;
    private static final int REQUEST_UPDATE_GOOGLEPLAYSERVICES = 2;
    private Button btnAbout;
    private Button btnMapStoring;
    private Button btnSettingsDataCollection;
    private Button btnSettingsGoogle;
    private String googleAccountName;
    private GoogleAccountCredential googleCredential;
    private boolean isSignedInGoogle;
    private ApplicationSettings settings;
    private TextView textAccountName;
    private ToggleButton toggle3gEdgeSync;

    private void initActionBar() {
        ActionBar parrotActionBar = getParrotActionBar();
        parrotActionBar.setTitle(getString(C0984R.string.ff_ID000011).toUpperCase());
        parrotActionBar.initBackButton();
    }

    private void initGoogleAccount() {
        this.googleCredential = GoogleAccountCredential.usingOAuth2(this, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_UPLOAD);
    }

    private void initListeners() {
        this.btnSettingsGoogle.setOnClickListener(this);
        this.btnMapStoring.setOnClickListener(this);
        this.btnSettingsDataCollection.setOnClickListener(this);
        this.btnAbout.setOnClickListener(this);
        this.toggle3gEdgeSync.setOnCheckedChangeListener(this);
    }

    private void initPreferences() {
        this.settings = ((FreeFlightApplication) getApplication()).getAppSettings();
        this.googleAccountName = AccountChecker.getGoogleAccountName(this.settings, getApplicationContext());
    }

    private void initUI() {
        ApplicationSettings appSettings = getAppSettings();
        this.btnSettingsGoogle = (Button) findViewById(C0984R.id.activity_preferences_button_settings_google);
        this.btnMapStoring = (Button) findViewById(C0984R.id.activity_preferences_button_settings_mapstoring);
        this.btnSettingsDataCollection = (Button) findViewById(C0984R.id.activity_preferences_button_settings_datacollection);
        this.btnAbout = (Button) findViewById(C0984R.id.activity_preferences_button_about);
        this.toggle3gEdgeSync = (ToggleButton) findViewById(C0984R.id.activity_preferences_toggle_3gsync);
        this.toggle3gEdgeSync.setChecked(appSettings.is3gEdgeDataSyncEnabled());
        this.textAccountName = (TextView) findViewById(C0984R.id.activity_preferences_textview_googleaccount);
    }

    private void onChooseGoogleAccount() {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isGooglePlayServicesAvailable == 0) {
            startActivityForResult(this.googleCredential.newChooseAccountIntent(), 1);
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isGooglePlayServicesAvailable)) {
            GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, this, 2).show();
        }
    }

    private void onMapStoringClicked(View view) {
        if (GooglePlayServicesChecker.check(this, 3)) {
            startActivity(new Intent(this, MapLoadingActivity.class));
        }
    }

    private void setGoogleAccountName(String str) {
        this.settings.setGoogleAccountName(str);
        this.googleAccountName = str;
        this.googleCredential.setSelectedAccountName(str);
    }

    private void updateUI() {
        this.textAccountName.setText(this.googleAccountName);
        if (this.googleAccountName != null) {
            this.btnSettingsGoogle.setText(C0984R.string.ff_ID000161);
        } else {
            this.btnSettingsGoogle.setText(C0984R.string.ff_ID000014);
        }
    }

    public void on3gEdgeToggleChanged(CompoundButton compoundButton, boolean z) {
        getAppSettings().set3gEdgeDataSyncEnabled(z);
    }

    public void onAboutClicked(View view) {
        startActivity(new Intent(this, ReadTermsActivity.class));
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        switch (i) {
            case 1:
                if (i2 == -1 && intent != null && intent.getExtras() != null) {
                    String stringExtra = intent.getStringExtra("authAccount");
                    if (stringExtra != null) {
                        setGoogleAccountName(stringExtra);
                        updateUI();
                        return;
                    }
                    return;
                }
                return;
            case 3:
                if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == 0) {
                    startActivity(new Intent(this, MapLoadingActivity.class));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        switch (compoundButton.getId()) {
            case C0984R.id.activity_preferences_toggle_3gsync /*2131362032*/:
                on3gEdgeToggleChanged(compoundButton, z);
                return;
            default:
                onNotifyNotImplemented();
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.activity_preferences_button_settings_google /*2131362029*/:
                onSettingsGoogleClicked(view);
                return;
            case C0984R.id.activity_preferences_button_settings_mapstoring /*2131362033*/:
                onMapStoringClicked(view);
                return;
            case C0984R.id.activity_preferences_button_settings_datacollection /*2131362036*/:
                onSettingsDataCollectionClicked(view);
                return;
            case C0984R.id.activity_preferences_button_about /*2131362039*/:
                onAboutClicked(view);
                return;
            default:
                onNotifyNotImplemented();
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_preferences);
        initPreferences();
        initGoogleAccount();
        initActionBar();
        initUI();
        initListeners();
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__PREFERENCES_ZONE_OPEN);
    }

    protected void onDestroy() {
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__PREFERENCES_ZONE_CLOSE);
        super.onDestroy();
    }

    public void onSettingsDataCollectionClicked(View view) {
        startActivity(new Intent(this, TrackingActivity.class));
        overridePendingTransition(C0984R.anim.tracking_in_anim, C0984R.anim.tracking_out_anim);
    }

    public void onSettingsGoogleClicked(View view) {
        if (this.googleAccountName == null) {
            onChooseGoogleAccount();
        } else {
            GoogleSettingsActivity.start(this);
        }
    }

    protected void onStart() {
        super.onStart();
        setGoogleAccountName(AccountChecker.getGoogleAccountName(this.settings, getApplicationContext()));
        updateUI();
    }

    protected void onStop() {
        super.onStop();
    }
}
