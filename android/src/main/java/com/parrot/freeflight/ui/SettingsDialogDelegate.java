package com.parrot.freeflight.ui;

import com.parrot.freeflight.activities.SettingsDialog;
import com.parrot.freeflight.settings.ApplicationSettings.EAppSettingProperty;

public interface SettingsDialogDelegate {
    void onDismissed(SettingsDialog settingsDialog);

    void onOptionChangedApp(SettingsDialog settingsDialog, EAppSettingProperty eAppSettingProperty, Object obj);

    void prepareDialog(SettingsDialog settingsDialog);
}
