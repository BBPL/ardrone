package com.parrot.ardronetool;

import org.mortbay.jetty.HttpVersions;

public enum ARDroneAnimation {
    PHI_M30_DEG("1000"),
    PHI_30_DEG("1000"),
    THETA_M30_DEG("1000"),
    THETA_30_DEG("1000"),
    THETA_20DEG_YAW_200DEG("1000"),
    THETA_20DEG_YAW_M200DEG("1000"),
    TURNAROUND("5000"),
    TURNAROUND_GODOWN("5000"),
    YAW_SHAKE("5000"),
    YAW_DANCE("5000"),
    PHI_DANCE("5000"),
    THETA_DANCE("5000"),
    VZ_DANCE("5000"),
    WAVE("5000"),
    PHI_THETA_MIXED("5000"),
    DOUBLE_PHI_THETA_MIXED("5000"),
    FLIP_AHEAD("15"),
    FLIP_BEHIND("15"),
    FLIP_LEFT("15"),
    FLIP_RIGHT("15");
    
    private String strConfigValue;

    private ARDroneAnimation(String str) {
        this.strConfigValue = HttpVersions.HTTP_0_9 + ordinal() + "," + str;
    }

    public String toString() {
        return this.strConfigValue;
    }
}
