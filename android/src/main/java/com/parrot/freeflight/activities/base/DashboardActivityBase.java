package com.parrot.freeflight.activities.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.receivers.MediaStorageReceiver;
import com.parrot.freeflight.receivers.MediaStorageReceiverDelegate;
import com.parrot.freeflight.settings.AccountChecker;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.utils.GPSHelper;

@SuppressLint({"Registered"})
public class DashboardActivityBase extends ParrotActivity implements OnClickListener, MediaStorageReceiverDelegate {
    private static final int REQUEST_ACCOUNT_PICKER = 1;
    private static final int REQUEST_UPDATE_GOOGLEPLAYSERVICES = 2;
    protected static final String TAG = "DashboardActivity";
    private AlertDialog alertDialog;
    private CheckedTextView btnAcademy;
    private CheckedTextView btnFirmwareUpdate;
    private CheckedTextView btnFreeFlight;
    private CheckedTextView btnGetYourDrone;
    private CheckedTextView btnParrotGames;
    private CheckedTextView btnPhotosVideos;
    private MediaStorageReceiver externalStorageStateReceiver;
    private String googleAccountName;
    private GoogleAccountCredential googleCredential;
    protected ApplicationSettings settings;

    class C11524 implements DialogInterface.OnClickListener {
        C11524() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
        }
    }

    public enum EPhotoVideoState {
        UNKNOWN,
        READY,
        NO_MEDIA,
        NO_SDCARD
    }

    private void initBroadcastReceivers() {
        this.externalStorageStateReceiver = new MediaStorageReceiver(this);
    }

    private void initListeners() {
        this.btnFreeFlight.setOnClickListener(this);
        this.btnAcademy.setOnClickListener(this);
        this.btnPhotosVideos.setOnClickListener(this);
        this.btnFirmwareUpdate.setOnClickListener(this);
        this.btnParrotGames.setOnClickListener(this);
        this.btnGetYourDrone.setOnClickListener(this);
    }

    private void initUI() {
        this.btnFreeFlight = (CheckedTextView) findViewById(C0984R.id.btnFreeFlight);
        this.btnAcademy = (CheckedTextView) findViewById(C0984R.id.btnAcademy);
        this.btnPhotosVideos = (CheckedTextView) findViewById(C0984R.id.btnPhotosVideos);
        this.btnFirmwareUpdate = (CheckedTextView) findViewById(C0984R.id.btnFirmwareUpdate);
        this.btnParrotGames = (CheckedTextView) findViewById(C0984R.id.btnGames);
        this.btnGetYourDrone = (CheckedTextView) findViewById(C0984R.id.btnGetYourDrone);
    }

    private void notifyAboutAutoUpload() {
        ApplicationSettings applicationSettings = new ApplicationSettings(this);
        if (applicationSettings.getTimesAppAskedUserToAllowAutoUpload() < 1) {
            showAutoUploadAlertDialog(getString(C0984R.string.ff_ID000207), getString(C0984R.string.ff_ID000208), null, this, applicationSettings);
            applicationSettings.appAskedUserToAllowAutoUpload();
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
        showAlertDialog(getString(C0984R.string.ae_ID000063), getString(C0984R.string.ff_ID000232), null);
        if (this.settings.getTimesAppAskedUserToAllowGPSLocation() < 2) {
            showGPSAlertDialog(getString(C0984R.string.ae_ID000063), getString(C0984R.string.ae_ID000067), this);
            this.settings.appAskedUserToAllowGPSLocation();
        }
    }

    private void onChooseGoogleAccount() {
        this.googleCredential = GoogleAccountCredential.usingOAuth2(this, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_UPLOAD);
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
        if (this.googleCredential == null) {
            this.googleCredential = GoogleAccountCredential.usingOAuth2(this, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_UPLOAD);
        }
        this.googleCredential.setSelectedAccountName(str);
    }

    private void showErrorMessageForTime(final View view, String str, int i) {
        final ViewGroup viewGroup = (ViewGroup) view.getParent();
        final int indexOfChild = viewGroup.indexOfChild(view);
        View view2 = (TextView) view.getTag();
        if (view2 == null) {
            view2 = (TextView) inflateView(C0984R.layout.dashboard_button_nok, viewGroup, false);
            view2.setLayoutParams(view.getLayoutParams());
            view.setTag(view2);
        }
        view2.setText(str);
        viewGroup.removeView(view);
        viewGroup.addView(view2, indexOfChild);
        viewGroup.postDelayed(new Runnable() {
            public void run() {
                viewGroup.removeViewAt(indexOfChild);
                viewGroup.addView(view, indexOfChild);
            }
        }, (long) i);
    }

    protected EPhotoVideoState getPhotoVideoState() {
        return EPhotoVideoState.NO_SDCARD;
    }

    protected void handleServices() {
    }

    protected boolean isARDroneUpdateEnabled() {
        return false;
    }

    protected boolean isAcademyEnabled() {
        return false;
    }

    protected boolean isFirmwareUpdateEnabled() {
        return false;
    }

    protected boolean isFreeFlightEnabled() {
        return false;
    }

    protected boolean isGuestSpaceEnabled() {
        return false;
    }

    protected boolean isParrotGamesEnabled() {
        return false;
    }

    @Deprecated
    protected boolean isPhotoVideosEnabled() {
        return false;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        switch (i) {
            case 1:
                if (i2 == -1 && intent != null && intent.getExtras() != null) {
                    String stringExtra = intent.getStringExtra("authAccount");
                    if (stringExtra != null) {
                        setGoogleAccountName(stringExtra);
                        handleServices();
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.btnFreeFlight /*2131362064*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HOME_TAKE_OFF);
                if (!isFreeFlightEnabled() || !onStartFreeflight()) {
                    showErrorMessageForTime(view, getString(C0984R.string.ff_ID000091), 2000);
                    return;
                }
                return;
            case C0984R.id.btnAcademy /*2131362065*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HOME_ACADEMY);
                if (!isAcademyEnabled() || !onStartAcademy()) {
                    showErrorMessageForTime(view, getString(C0984R.string.ff_ID000092), 2000);
                    return;
                }
                return;
            case C0984R.id.btnPhotosVideos /*2131362066*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HOME_MEDIAS);
                EPhotoVideoState photoVideoState = getPhotoVideoState();
                switch (photoVideoState) {
                    case READY:
                        onStartPhotosVideos();
                        return;
                    case NO_MEDIA:
                        showErrorMessageForTime(view, getString(C0984R.string.ff_ID000093), 2000);
                        return;
                    case NO_SDCARD:
                        showErrorMessageForTime(view, getString(C0984R.string.NO_SD_CARD_INSERTED), 2000);
                        return;
                    default:
                        Log.w(TAG, "Unknown media state " + photoVideoState.name());
                        return;
                }
            case C0984R.id.btnFirmwareUpdate /*2131362067*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HOME_UPDATE);
                if (!isFreeFlightEnabled()) {
                    showErrorMessageForTime(view, getString(C0984R.string.ff_ID000091), 2000);
                    return;
                } else if (!isFirmwareUpdateEnabled() || !onStartFirmwareUpdate()) {
                    showErrorMessageForTime(view, getString(C0984R.string.ff_ID000094), 2000);
                    return;
                } else {
                    return;
                }
            case C0984R.id.btnGames /*2131362068*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HOME_PARROT_GAMES);
                if (!isParrotGamesEnabled() || !onStartGames()) {
                    showErrorMessageForTime(view, getString(C0984R.string.we_have_no_games_yet), 2000);
                    return;
                }
                return;
            case C0984R.id.btnGetYourDrone /*2131362069*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HOME_GET_YOUR_ARDRONE);
                if (!isGuestSpaceEnabled() || !onStartGuestSpace()) {
                    showErrorMessageForTime(view, getString(C0984R.string.not_implemented_yet), 2000);
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.dashboard_screen);
        this.settings = new ApplicationSettings(this);
        this.googleAccountName = AccountChecker.getGoogleAccountName(this.settings, getApplicationContext());
        initUI();
        initListeners();
        initBroadcastReceivers();
        notifyAboutAutoUpload();
        notifyAboutGPSDisabled();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onMediaEject() {
    }

    public void onMediaStorageMounted() {
    }

    public void onMediaStorageUnmounted() {
    }

    protected void onPause() {
        super.onPause();
        if (this.alertDialog != null && this.alertDialog.isShowing()) {
            this.alertDialog.dismiss();
        }
        this.externalStorageStateReceiver.unregisterFromEvents(this);
    }

    protected void onResume() {
        super.onResume();
        requestUpdateButtonsState();
        this.externalStorageStateReceiver.registerForEvents(this);
    }

    protected boolean onStartAcademy() {
        return false;
    }

    protected boolean onStartFirmwareUpdate() {
        return false;
    }

    protected boolean onStartFreeflight() {
        return false;
    }

    protected boolean onStartGames() {
        return false;
    }

    protected boolean onStartGuestSpace() {
        return false;
    }

    protected boolean onStartPhotosVideos() {
        return false;
    }

    public void requestUpdateButtonsState() {
        if (Looper.myLooper() == null) {
            throw new IllegalStateException("Should be called from UI thread");
        }
        this.btnFreeFlight.setChecked(isFreeFlightEnabled());
        this.btnAcademy.setChecked(isAcademyEnabled());
        this.btnPhotosVideos.setChecked(getPhotoVideoState().equals(EPhotoVideoState.READY));
        this.btnFirmwareUpdate.setChecked(isFirmwareUpdateEnabled());
        this.btnParrotGames.setChecked(isParrotGamesEnabled());
        this.btnGetYourDrone.setChecked(isGuestSpaceEnabled());
    }

    protected void showAlertDialog(String str, String str2, final Runnable runnable) {
        Builder builder = new Builder(this);
        View textView = new TextView(this);
        textView.setGravity(17);
        textView.setText(str);
        textView.setTextAppearance(this, 16974264);
        if ((super.getResources().getConfiguration().screenLayout & 15) >= 3) {
            textView.setTextSize(22.0f);
        } else {
            textView.setTextSize(18.0f);
        }
        textView.setHeight(((int) textView.getTextSize()) * 4);
        builder.setCustomTitle(textView);
        this.alertDialog = builder.setMessage(str2).setCancelable(false).setNegativeButton(getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).create();
        this.alertDialog.show();
        TextView textView2 = (TextView) this.alertDialog.findViewById(16908299);
        ((TextView) this.alertDialog.findViewById(16908299)).setGravity(17);
    }

    protected void showAutoUploadAlertDialog(String str, String str2, Context context, Activity activity, final ApplicationSettings applicationSettings) {
        Builder builder = new Builder(this);
        View textView = new TextView(this);
        textView.setGravity(17);
        textView.setText(str);
        textView.setTextAppearance(this, 16974264);
        if ((super.getResources().getConfiguration().screenLayout & 15) >= 3) {
            textView.setTextSize(22.0f);
        } else {
            textView.setTextSize(18.0f);
        }
        textView.setHeight(((int) textView.getTextSize()) * 4);
        builder.setCustomTitle(textView);
        this.alertDialog = builder.setMessage(str2).setCancelable(false).setNegativeButton(getString(C0984R.string.ff_ID000013), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                applicationSettings.setAutoPhotoUploadEnabled(false);
                applicationSettings.setAutoVideoUploadEnabled(false);
            }
        }).setPositiveButton(getString(C0984R.string.ff_ID000012), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                applicationSettings.setAutoPhotoUploadEnabled(true);
                applicationSettings.setAutoVideoUploadEnabled(true);
                if (DashboardActivityBase.this.googleAccountName == null) {
                    DashboardActivityBase.this.onChooseGoogleAccount();
                }
            }
        }).create();
        this.alertDialog.show();
        ((TextView) this.alertDialog.findViewById(16908299)).setGravity(17);
    }

    protected void showGPSAlertDialog(String str, String str2, final Context context) {
        Builder builder = new Builder(this);
        View textView = new TextView(this);
        textView.setGravity(17);
        textView.setText(str);
        textView.setTextAppearance(this, 16974264);
        if ((super.getResources().getConfiguration().screenLayout & 15) >= 3) {
            textView.setTextSize(22.0f);
        } else {
            textView.setTextSize(18.0f);
        }
        textView.setHeight(((int) textView.getTextSize()) * 4);
        builder.setCustomTitle(textView);
        this.alertDialog = builder.setMessage(str2).setCancelable(false).setNegativeButton(getString(C0984R.string.ae_ID000123), new C11524()).setPositiveButton(getString(C0984R.string.ff_ID000161), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                GPSHelper.callGpsSettingsScreen(context);
            }
        }).create();
        this.alertDialog.show();
        ((TextView) this.alertDialog.findViewById(16908299)).setGravity(17);
    }
}
