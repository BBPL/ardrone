package com.parrot.freeflight.activities.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.ui.StatusBar;
import com.parrot.freeflight.utils.FontUtils;
import com.parrot.freeflight.utils.GPSHelper;

@SuppressLint({"Registered"})
public class ParrotActivity extends FragmentActivity {
    private static final boolean DEBUG_VIEW_HIERARCHY = false;
    private static final String TAG = ParrotActivity.class.getSimpleName();
    private LayoutInflater inflater;
    private ActionBar mActionBar;
    private StatusBar mStatusBar;
    private boolean mViewServerStarted;
    private boolean startingActivity = false;

    private void initNavigationBar() {
        View findViewById = findViewById(C0984R.id.navigation_bar);
        if (findViewById != null) {
            this.mActionBar = new ActionBar(this, findViewById);
        }
    }

    private void initStatusBar() {
        View findViewById = findViewById(C0984R.id.header_preferences);
        if (findViewById != null) {
            this.mStatusBar = new StatusBar(this, findViewById);
            notifyAboutGPSDisabled();
        }
    }

    private void notifyAboutGPSDisabled() {
        GPSHelper.getInstance(this);
        TextView textView = (TextView) findViewById(C0984R.id.txtGPS);
        if (GPSHelper.isGpsOn(this)) {
            Log.i("gpsStatus", "gps is now ON!");
            textView.setTextColor(getResources().getColor(C0984R.color.accent));
            return;
        }
        textView.setTextColor(getResources().getColor(C0984R.color.text_color));
        Log.i("gpsStatus", "gps is now OFF!");
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.startingActivity ? true : super.dispatchTouchEvent(motionEvent);
    }

    public ApplicationSettings getAppSettings() {
        return ((FreeFlightApplication) getApplication()).getAppSettings();
    }

    public ActionBar getParrotActionBar() {
        return this.mActionBar;
    }

    public View inflateView(int i, ViewGroup viewGroup, boolean z) {
        View inflate = this.inflater.inflate(i, viewGroup, z);
        if (inflate != null) {
            FontUtils.applyFont((Context) this, inflate);
        }
        return inflate;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.inflater = (LayoutInflater) getSystemService("layout_inflater");
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onNotifyNotImplemented() {
        Toast.makeText(this, "Sorry, not implemented yet", 0).show();
    }

    protected void onPause() {
        super.onPause();
        if (this.mStatusBar != null) {
            this.mStatusBar.stopUpdating();
        }
    }

    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        FontUtils.applyFont((Context) this, (ViewGroup) findViewById(16908290));
    }

    protected void onResume() {
        super.onResume();
        if (this.mStatusBar != null) {
            this.mStatusBar.startUpdating();
            notifyAboutGPSDisabled();
        }
        this.startingActivity = false;
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    public void setContentView(int i) {
        super.setContentView(i);
        initStatusBar();
        initNavigationBar();
    }

    public void setTitle(int i) {
        if (this.mActionBar != null) {
            this.mActionBar.setTitle(getString(i));
        }
        super.setTitle(i);
    }

    public void setTitle(CharSequence charSequence) {
        if (this.mActionBar != null) {
            this.mActionBar.setTitle(charSequence.toString());
        }
        super.setTitle(charSequence);
    }

    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        this.startingActivity = true;
        super.startActivityForResult(intent, i, bundle);
    }
}
