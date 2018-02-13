package com.parrot.freeflight.ui.controlhandlers;

import android.app.Activity;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.settings.ApplicationSettings.ControlMode;
import com.parrot.freeflight.ui.HudViewController;
import com.parrot.freeflight.ui.HudViewController.JoystickType;
import com.parrot.freeflight.ui.hud.JoystickBase;

public class MoverioInputEventsHandler extends InputEventsHandlerDefault {
    MoverioInputEventsHandler(Activity activity, HudViewController hudViewController, ApplicationSettings applicationSettings) {
        super(activity, hudViewController, applicationSettings);
    }

    private void initButtonsMapping() {
        this.buttonsMapping.put(21, Action.TURN_LEFT);
        this.buttonsMapping.put(22, Action.TURN_RIGHT);
        this.buttonsMapping.put(20, Action.DOWN);
        this.buttonsMapping.put(19, Action.UP);
        this.buttonsMapping.put(23, Action.TAKE_OFF);
        this.buttonsMapping.put(82, Action.SETTINGS);
    }

    public int getInputSourceOrientation() {
        return 0;
    }

    protected void onInitButtonsMappingLeftHanded() {
        super.onInitButtonsMappingRightHanded();
        initButtonsMapping();
    }

    protected void onInitButtonsMappingRightHanded() {
        super.onInitButtonsMappingRightHanded();
        initButtonsMapping();
    }

    protected void onInitOnScreenJoysticks(ControlMode controlMode, boolean z) {
        initVirtualJoysticks(JoystickType.ANALOGUE, JoystickType.NONE, z);
    }

    protected void onJoypadsReady(boolean z, JoystickBase joystickBase, JoystickBase joystickBase2) {
        this.view.setJoystick(joystickBase);
    }
}
