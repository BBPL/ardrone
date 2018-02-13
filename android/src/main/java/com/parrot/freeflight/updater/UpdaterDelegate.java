package com.parrot.freeflight.updater;

import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;

public interface UpdaterDelegate {
    void onFinished();

    void setCheckingRepairingState(IndicatorState indicatorState, int i, String str);

    void setInstallingState(IndicatorState indicatorState, int i, String str);

    void setRestartingDroneState(IndicatorState indicatorState, int i, String str);

    void setSendingFileState(IndicatorState indicatorState, int i, String str);
}
