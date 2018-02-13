package com.parrot.freeflight;

import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import com.parrot.freeflight.settings.ApplicationSettings;
import java.util.Date;
import org.mortbay.jetty.HttpVersions;

public class RatingReminder {
    private static final int RATING_REMINDER_DAYS_BEFORE_REMINDING = 10;
    private static final int RATING_REMINDER_DAYS_UNTIL_PROMPT = 10;
    private static final boolean RATING_REMINDER_DEBUG = false;
    private static final int RATING_REMINDER_USES_UNTIL_PROMPT = 20;
    private final Context context;
    private final OnClickListener rateListener = new C09851();
    private final OnClickListener remindLaterListener = new C09862();
    private final ApplicationSettings settings;

    class C09851 implements OnClickListener {
        C09851() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            RatingReminder.this.rateApp();
        }
    }

    class C09862 implements OnClickListener {
        C09862() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            RatingReminder.this.settings.setRatingReminderReminderRequestDate(new Date().getTime());
        }
    }

    public RatingReminder(Context context, ApplicationSettings applicationSettings) {
        this.context = context;
        this.settings = applicationSettings;
    }

    private String getAppVersion() {
        try {
            return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void incrementAndRate(boolean z) {
        incrementUseCount();
        if (z && ratingConditionsHaveBeenMet()) {
            showRatingAlert();
        }
    }

    private void incrementUseCount() {
        String appVersion = getAppVersion();
        String ratingReminderCurrentVersion = this.settings.getRatingReminderCurrentVersion();
        if (ratingReminderCurrentVersion == null) {
            this.settings.setRatingReminderCurrentVersion(appVersion);
            ratingReminderCurrentVersion = appVersion;
        }
        if (ratingReminderCurrentVersion.equals(appVersion)) {
            if (this.settings.getRatingReminderFirstUseDate() == 0) {
                this.settings.setRatingReminderFirstUseDate(new Date().getTime());
            }
            this.settings.setRatingReminderUseCount(this.settings.getRatingReminderUseCount() + 1);
            return;
        }
        this.settings.setRatingReminderCurrentVersion(appVersion);
        this.settings.setRatingReminderFirstUseDate(new Date().getTime());
        this.settings.setRatingReminderUseCount(1);
        this.settings.setRatingReminderRatedCurrentVersion(false);
        this.settings.setRatingReminderDeclinedToRate(false);
        this.settings.setRatingReminderReminderRequestDate(0);
    }

    private boolean ratingConditionsHaveBeenMet() {
        long ratingReminderFirstUseDate = this.settings.getRatingReminderFirstUseDate();
        long time = new Date().getTime();
        int ratingReminderUseCount = this.settings.getRatingReminderUseCount();
        if ((time - ratingReminderFirstUseDate < 864000 && ratingReminderUseCount <= 20) || this.settings.isRatingReminderDeclinedToRate() || this.settings.isRatingReminderRatedCurrentVersion()) {
            return false;
        }
        return new Date().getTime() - this.settings.getRatingReminderReminderRequestDate() >= 864000;
    }

    private void showRatingAlert() {
        Builder builder = new Builder(this.context);
        builder.setTitle(String.format(this.context.getString(C0984R.string.ff_ID000168), new Object[]{this.context.getString(2131165184)}));
        View textView = new TextView(this.context);
        textView.setText(String.format(this.context.getString(C0984R.string.ff_ID000167), new Object[]{this.context.getString(2131165184)}));
        textView.setPadding(30, 30, 30, 30);
        textView.setGravity(17);
        builder.setPositiveButton(String.format(this.context.getString(C0984R.string.ff_ID000168), new Object[]{HttpVersions.HTTP_0_9}), this.rateListener);
        builder.setNegativeButton(this.context.getString(C0984R.string.ff_ID000170), this.remindLaterListener);
        builder.setView(textView);
        builder.show();
    }

    public void appLaunched(boolean z) {
        incrementAndRate(z);
    }

    public void rateApp() {
        this.settings.setRatingReminderRatedCurrentVersion(true);
        try {
            this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.context.getPackageName())));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
