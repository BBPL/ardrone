package com.parrot.freeflight.ui.controlhandlers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build.VERSION;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.settings.ApplicationSettings.ControlMode;
import com.parrot.freeflight.ui.HudViewController;
import com.parrot.freeflight.ui.HudViewController.JoystickType;

public class SonyRemoteInputEventsHandler extends InputEventsHandlerDefault {
    static final String TAG = SonyRemoteInputEventsHandler.class.getSimpleName();

    SonyRemoteInputEventsHandler(Activity activity, HudViewController hudViewController, ApplicationSettings applicationSettings) {
        super(activity, hudViewController, applicationSettings);
    }

    public int getInputSourceOrientation() {
        return this.settings.isLeftHanded() ? 1 : 3;
    }

    @SuppressLint({"NewApi"})
    protected void onInitButtonsMappingLeftHanded() {
        super.onInitButtonsMappingLeftHanded();
        this.buttonsMapping.put(22, Action.UP);
        this.buttonsMapping.put(21, Action.DOWN);
        this.buttonsMapping.put(19, Action.TURN_LEFT);
        this.buttonsMapping.put(20, Action.TURN_RIGHT);
        this.buttonsMapping.put(23, Action.FLIP);
        this.buttonsMapping.put(82, Action.SETTINGS);
        if (VERSION.SDK_INT >= 11) {
            this.buttonsMapping.put(126, Action.ACCELERO);
            this.buttonsMapping.put(184, Action.CAMERA);
            this.buttonsMapping.put(186, Action.EMERGENCY);
            this.buttonsMapping.put(183, Action.TAKE_OFF);
            this.buttonsMapping.put(127, Action.VIDEO_RECORD);
            this.buttonsMapping.put(165, Action.PHOTO);
        }
    }

    @SuppressLint({"NewApi"})
    protected void onInitButtonsMappingRightHanded() {
        super.onInitButtonsMappingRightHanded();
        this.buttonsMapping.put(21, Action.UP);
        this.buttonsMapping.put(22, Action.DOWN);
        this.buttonsMapping.put(20, Action.TURN_LEFT);
        this.buttonsMapping.put(19, Action.TURN_RIGHT);
        this.buttonsMapping.put(23, Action.FLIP);
        this.buttonsMapping.put(82, Action.SETTINGS);
        if (VERSION.SDK_INT >= 11) {
            this.buttonsMapping.put(126, Action.ACCELERO);
            this.buttonsMapping.put(185, Action.CAMERA);
            this.buttonsMapping.put(183, Action.EMERGENCY);
            this.buttonsMapping.put(186, Action.TAKE_OFF);
            this.buttonsMapping.put(127, Action.VIDEO_RECORD);
            this.buttonsMapping.put(165, Action.PHOTO);
        }
    }

    protected void onInitOnScreenJoysticks(ControlMode controlMode, boolean z) {
        initVirtualJoysticks(JoystickType.NONE, JoystickType.NONE, z);
    }
}
