package com.parrot.freeflight.ui.controlhandlers;

import android.view.View.OnKeyListener;
import com.parrot.freeflight.settings.ApplicationSettings;

public interface InputEventsHandler extends OnKeyListener {
    int getInputSourceOrientation();

    void setOnFlipListener(OnDroneFlipListener onDroneFlipListener);

    void setOnGazYawChangedListener(OnGazYawChangedListener onGazYawChangedListener);

    void setOnRollPitchChangedListener(OnRollPitchChangedListener onRollPitchChangedListener);

    void setOnUiActionsListener(OnUiActionsListener onUiActionsListener);

    void setSettings(ApplicationSettings applicationSettings);
}
