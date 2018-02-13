package com.parrot.freeflight.academy.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.ui.ActionBar;

public class AcademyActivity extends ParrotActivity implements OnClickListener {
    private ActionBar actionBar;
    private CheckedTextView btnFlights;
    private CheckedTextView btnMap;
    private CheckedTextView btnProfile;
    private LinearLayout invite_a_friend;
    private TextView logged;

    private void initActionBar() {
        this.actionBar = getParrotActionBar();
        setTitle(getString(C0984R.string.aa_ID000020).toUpperCase());
        this.actionBar.initHomeButton();
        this.actionBar.initLogoutButton();
    }

    private void initListeners() {
        this.btnProfile.setOnClickListener(this);
        this.btnFlights.setOnClickListener(this);
        this.btnMap.setOnClickListener(this);
        this.invite_a_friend.setOnClickListener(this);
    }

    private void initUI() {
        this.btnProfile = (CheckedTextView) findViewById(C0984R.id.btnProfile);
        this.btnFlights = (CheckedTextView) findViewById(C0984R.id.btnFlights);
        this.btnMap = (CheckedTextView) findViewById(C0984R.id.btnMap);
        this.logged = (TextView) findViewById(C0984R.id.academy_logged_text);
        this.logged.setText(String.format(getString(C0984R.string.aa_ID000051), new Object[]{AcademyUtils.profile.getUser().getUsername().toUpperCase()}));
        this.invite_a_friend = (LinearLayout) findViewById(C0984R.id.invite_a_friend);
    }

    private void sendEmail() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("message/rfc822");
        intent.putExtra("android.intent.extra.SUBJECT", getString(C0984R.string.aa_ID000020));
        intent.putExtra("android.intent.extra.TEXT", getString(C0984R.string.aa_ID000052));
        startActivity(Intent.createChooser(intent, getString(C0984R.string.aa_ID000049) + " " + getString(C0984R.string.aa_ID000050)));
    }

    @SuppressLint({"NewApi"})
    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.btnProfile /*2131361835*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_ACADEMY_PROFILE);
                startActivity(new Intent(this, AcademyProfileActivity.class));
                return;
            case C0984R.id.btnFlights /*2131361836*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_ACADEMY_MY_FLIGHTS);
                startActivity(new Intent(this, AcademyMyFlightsActivity.class));
                return;
            case C0984R.id.btnMap /*2131361837*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_ACADEMY_MAP);
                int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (isGooglePlayServicesAvailable == 0) {
                    startActivity(new Intent(this, AcademyMapActivity.class));
                    return;
                } else {
                    GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, this, 102).show();
                    return;
                }
            case C0984R.id.invite_a_friend /*2131361839*/:
                this.invite_a_friend.setPressed(true);
                sendEmail();
                this.invite_a_friend.setPressed(false);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.academy_main_screen);
        if (TextUtils.isEmpty(AcademyUtils.login)) {
            AcademyUtils.getCredentials(this);
        }
        initActionBar();
        initUI();
        initListeners();
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_ZONE_OPEN);
    }

    protected void onDestroy() {
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_ZONE_CLOSE);
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
}
