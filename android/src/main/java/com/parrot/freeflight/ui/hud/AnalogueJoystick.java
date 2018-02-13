package com.parrot.freeflight.ui.hud;

import android.content.Context;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.ui.hud.Sprite.Align;

public class AnalogueJoystick extends JoystickBase {
    public AnalogueJoystick(Context context, Align align, boolean z) {
        super(context, align, z);
    }

    protected int getBackgroundDrawableId() {
        return C0984R.drawable.joystick_halo;
    }

    protected int getTumbDrawableId() {
        return C0984R.drawable.joystick_manuel;
    }
}
