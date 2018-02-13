package com.parrot.freeflight.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.settings.ApplicationSettings;

public class TrackingActivity extends ParrotActivity {
    private RadioButton agreeButton;
    private RadioButton declineButton;
    private ApplicationSettings settings;

    private void onCloseDialog() {
        overridePendingTransition(C0984R.anim.tracking_in_anim_back, C0984R.anim.tracking_out_anim_back);
        this.agreeButton.isChecked();
        this.settings.setTrackingEnabled(false);
        DataTracker.trackInfoInt(TRACK_KEY_ENUM.TRACK_KEY_EVENT__USER_ACCEPTED, 0);
    }

    public void okClickListener(View view) {
        finish();
        onCloseDialog();
    }

    public void onBackPressed() {
        super.onBackPressed();
        onCloseDialog();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.settings = new ApplicationSettings(this);
        this.settings.setTrackingDlgShowed(true);
        setContentView(C0984R.layout.tracking_screen);
        this.agreeButton = (RadioButton) findViewById(C0984R.id.tracking_radio_agree);
        this.declineButton = (RadioButton) findViewById(C0984R.id.tracking_radio_decline);
        if (this.settings.isTrackingEnabled()) {
            this.agreeButton.setChecked(true);
        } else {
            this.declineButton.setChecked(true);
        }
    }
}
