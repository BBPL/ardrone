package com.parrot.freeflight.ui.hud;

public abstract class JoystickListener {
    public abstract void onChanged(JoystickBase joystickBase, float f, float f2);

    public abstract void onPressed(JoystickBase joystickBase);

    public abstract void onReleased(JoystickBase joystickBase);
}
