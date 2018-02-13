package com.parrot.freeflight.ui.controlhandlers;

public interface OnUiActionsListener {
    void onBackClicked();

    void onBackToHomeClicked();

    void onCameraSwitchClicked();

    void onEmergencyClicked();

    void onGpsClicked();

    void onMapGoClicked();

    void onMapLeftPressed();

    void onMapLeftReleased();

    void onMapLeftRepeated();

    void onMapRightPressed();

    void onMapRightReleased();

    void onMapRightRepeated();

    void onOverBalanceClicked();

    void onPhotoTakeClicked();

    void onRandomShakeClicked();

    void onRescueClicked();

    void onSettingsClicked();

    void onTakeOffLandClicked();

    void onVideoRecordClicked();
}
