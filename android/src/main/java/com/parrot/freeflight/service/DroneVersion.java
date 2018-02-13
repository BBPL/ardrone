package com.parrot.freeflight.service;

public enum DroneVersion {
    UNKNOWN,
    DRONE_1,
    DRONE_2;

    public static int getMajorVersion(DroneVersion droneVersion) {
        if (droneVersion != null) {
            switch (droneVersion) {
                case DRONE_1:
                    return 1;
                case DRONE_2:
                    return 2;
            }
        }
        return -1;
    }
}
