package com.parrot.freeflight.ui.controlhandlers;

import android.app.Activity;
import android.util.Log;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.ui.HudViewController;
import com.parrot.freeflight.utils.SystemUtils;

public final class InputEventsHandlerFactory {
    private static final String TAG = InputEventsHandlerFactory.class.getSimpleName();

    public static InputEventsHandler create(Activity activity, HudViewController hudViewController, ApplicationSettings applicationSettings) {
        if (SystemUtils.isGoogleTV(activity)) {
            Log.d(TAG, "Configuring input controls for Sony GTV (Touch + Sony Remote)");
            return new SonyRemoteInputEventsHandler(activity, hudViewController, applicationSettings);
        } else if (SystemUtils.isMoverioGlasses()) {
            Log.d(TAG, "Configuring input controls for Epson Moverio (Touch + D-Pad)");
            return new MoverioInputEventsHandler(activity, hudViewController, applicationSettings);
        } else {
            Log.d(TAG, "Using default input control source (Touch)");
            return new InputEventsHandlerDefault(activity, hudViewController, applicationSettings);
        }
    }
}
