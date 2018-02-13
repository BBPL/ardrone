package com.parrot.freeflight.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import java.util.Hashtable;
import org.mortbay.jetty.HttpVersions;

public class ConnectScreenViewController {
    private static final String TAG = "ConnectScreenViewController";
    private Activity context;
    private String message;
    private ProgressBar progressUpload;
    private Hashtable<IndicatorState, BitmapDrawable> stateDrawables = new Hashtable();
    private String status;
    private TextView txtCheckingBootloader;
    private TextView txtCurrStep;
    private TextView txtInstalling;
    private TextView txtRestartingDrone;
    private TextView txtSendingFile;
    private TextView txtStatusMsg;

    class C12101 implements Runnable {
        C12101() {
        }

        public void run() {
            CharSequence charSequence = HttpVersions.HTTP_0_9;
            if (ConnectScreenViewController.this.status != null) {
                charSequence = HttpVersions.HTTP_0_9 + ConnectScreenViewController.this.status + "\n";
            }
            if (ConnectScreenViewController.this.message != null) {
                charSequence = charSequence + ConnectScreenViewController.this.message;
            }
            if (ConnectScreenViewController.this.txtStatusMsg != null) {
                ConnectScreenViewController.this.txtStatusMsg.setText(charSequence);
                if (ConnectScreenViewController.this.message.length() > 0) {
                    ConnectScreenViewController.this.txtStatusMsg.setVisibility(0);
                    return;
                } else {
                    ConnectScreenViewController.this.txtStatusMsg.setVisibility(4);
                    return;
                }
            }
            Log.e(ConnectScreenViewController.TAG, "Can't set status message. Field is null");
        }
    }

    public enum IndicatorState {
        EMPTY,
        ACTIVE,
        PASSED,
        FAILED
    }

    public ConnectScreenViewController(Activity activity) {
        this.context = activity;
        this.txtStatusMsg = (TextView) activity.findViewById(C0984R.id.txtMessage);
        this.txtCurrStep = (TextView) activity.findViewById(C0984R.id.txtCurrStepTitle);
        this.progressUpload = (ProgressBar) activity.findViewById(C0984R.id.progressBar);
        this.txtCheckingBootloader = (TextView) activity.findViewById(C0984R.id.txtCheckingRepairing);
        this.txtSendingFile = (TextView) activity.findViewById(C0984R.id.txtSendingFile);
        this.txtRestartingDrone = (TextView) activity.findViewById(C0984R.id.txtRestarting);
        this.txtInstalling = (TextView) activity.findViewById(C0984R.id.txtInstalling);
        Resources resources = activity.getResources();
        this.stateDrawables.put(IndicatorState.EMPTY, (BitmapDrawable) resources.getDrawable(C0984R.drawable.ff2_updater_empty));
        this.stateDrawables.put(IndicatorState.ACTIVE, (BitmapDrawable) resources.getDrawable(C0984R.drawable.ff2_updater_in_progress));
        this.stateDrawables.put(IndicatorState.PASSED, (BitmapDrawable) resources.getDrawable(C0984R.drawable.ff2_updater_ok));
        this.stateDrawables.put(IndicatorState.FAILED, (BitmapDrawable) resources.getDrawable(C0984R.drawable.ff2_updater_ko));
        initActionBar();
    }

    private void disableAllStepLabels() {
        this.txtCheckingBootloader.setEnabled(false);
        this.txtInstalling.setEnabled(false);
        this.txtRestartingDrone.setEnabled(false);
        this.txtSendingFile.setEnabled(false);
    }

    private void initActionBar() {
        ActionBar actionBar = new ActionBar(this.context, this.context.findViewById(C0984R.id.actionBar));
        actionBar.setTitle(this.context.getString(C0984R.string.ff_ID000102));
        actionBar.initHomeButton();
    }

    private void setControlVisibleOnUiThread(View view, int i) {
        if (view.getVisibility() != i) {
            view.setVisibility(i);
        }
    }

    private void setStateOnUiThread(TextView textView, IndicatorState indicatorState) {
        disableAllStepLabels();
        Drawable drawable = (Drawable) this.stateDrawables.get(indicatorState);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        }
        textView.setEnabled(true);
    }

    private void updateStatusMessage() {
        this.context.runOnUiThread(new C12101());
    }

    public void setCheckingRepairingState(IndicatorState indicatorState) {
        setStateOnUiThread(this.txtCheckingBootloader, indicatorState);
    }

    public void setInstallingState(IndicatorState indicatorState) {
        setStateOnUiThread(this.txtInstalling, indicatorState);
    }

    public void setMessage(String str) {
        this.message = str;
        updateStatusMessage();
    }

    public void setProgressMaxValue(int i) {
        this.progressUpload.setMax(i);
    }

    public void setProgressValue(int i) {
        this.progressUpload.setProgress(i);
    }

    public void setProgressVisible(boolean z) {
        setControlVisibleOnUiThread(this.progressUpload, z ? 0 : 4);
    }

    public void setRestartingDroneState(IndicatorState indicatorState) {
        setStateOnUiThread(this.txtRestartingDrone, indicatorState);
    }

    public void setSendingFileState(IndicatorState indicatorState) {
        setStateOnUiThread(this.txtSendingFile, indicatorState);
    }

    public void setStatus(String str) {
        this.txtCurrStep.setText(str);
    }
}
