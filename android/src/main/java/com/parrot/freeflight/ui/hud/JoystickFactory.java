package com.parrot.freeflight.ui.hud;

import android.content.Context;
import com.parrot.freeflight.ui.hud.Sprite.Align;

public class JoystickFactory {
    public static JoystickBase createAcceleroJoystick(Context context, boolean z, JoystickListener joystickListener) {
        JoystickBase acceleroJoystick = new AcceleroJoystick(context, Align.NO_ALIGN, z);
        acceleroJoystick.setOnAnalogueChangedListener(joystickListener);
        return acceleroJoystick;
    }

    public static JoystickBase createAnalogueJoystick(Context context, boolean z, JoystickListener joystickListener) {
        JoystickBase analogueJoystick = new AnalogueJoystick(context, Align.NO_ALIGN, z);
        analogueJoystick.setOnAnalogueChangedListener(joystickListener);
        return analogueJoystick;
    }

    public static JoystickBase createCombinedJoystick(Context context, boolean z, JoystickListener joystickListener, JoystickListener joystickListener2) {
        JoystickBase analogueJoystick = new AnalogueJoystick(context, Align.NO_ALIGN, z);
        analogueJoystick.setOnAnalogueChangedListener(joystickListener);
        return analogueJoystick;
    }
}
