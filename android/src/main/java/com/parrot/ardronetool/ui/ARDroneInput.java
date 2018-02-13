package com.parrot.ardronetool.ui;

public class ARDroneInput {
    public static final int ARDRONE_UI_BIT_AB = 1;
    public static final int ARDRONE_UI_BIT_AD = 2;
    public static final int ARDRONE_UI_BIT_AG = 0;
    public static final int ARDRONE_UI_BIT_AH = 3;
    public static final int ARDRONE_UI_BIT_L1 = 4;
    public static final int ARDRONE_UI_BIT_L2 = 6;
    public static final int ARDRONE_UI_BIT_R1 = 5;
    public static final int ARDRONE_UI_BIT_R2 = 7;
    public static final int ARDRONE_UI_BIT_SELECT = 8;
    public static final int ARDRONE_UI_BIT_START = 9;
    public static final int ARDRONE_UI_BIT_TRIM_PHI = 20;
    public static final int ARDRONE_UI_BIT_TRIM_THETA = 18;
    public static final int ARDRONE_UI_BIT_TRIM_YAW = 22;
    public static final int ARDRONE_UI_BIT_X = 24;
    public static final int ARDRONE_UI_BIT_Y = 28;
    private static ARDroneInput instance;

    public enum ProgressiveFlag {
        ARDRONE_PROGRESSIVE_CMD_ENABLE,
        ARDRONE_PROGRESSIVE_CMD_COMBINED_YAW_ACTIVE,
        ARDRONE_MAGNETO_CMD_ENABLE
    }

    private ARDroneInput() {
    }

    public static ARDroneInput instance() {
        if (instance == null) {
            instance = new ARDroneInput();
        }
        return instance;
    }

    public final native int getUserInputState();

    public final native boolean setProgressiveCmd(int i, float f, float f2, float f3, float f4, float f5, float f6);

    public final native boolean setUiPadSelect(int i);

    public final native boolean setUiPadStart(int i);

    public final boolean startReset() {
        return setUiPadStart(0);
    }
}
