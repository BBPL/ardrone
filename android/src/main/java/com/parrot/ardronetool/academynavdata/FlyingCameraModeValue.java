package com.parrot.ardronetool.academynavdata;

import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.HashMap;
import java.util.Map;

public enum FlyingCameraModeValue {
    FLYING_CAMERA_DEFAULT(10),
    FLYING_CAMERA_TRAVELLING(0),
    FLYING_CAMERA_PANORAMA(1),
    FLYING_CAMERA_STEADY_MODE(3),
    FLYING_CAMERA_CRANE(4),
    FLYING_CAMERA_DEMO_SWING(1000),
    FLYING_CAMERA_DEMO_WHEEL_FRONT(LocationStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES),
    FLYING_CAMERA_DEMO_WHEEL_SIDE(LocationStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS),
    FLYING_CAMERA_DEMO_GUSH(1003),
    FLYING_CAMERA_DEMO_PLANAR_WHEEL(1004),
    FLYING_CAMERA_GPS_MODE_WAYPOINT(10000),
    FLYING_CAMERA_GPS_MODE_BACKTOHOME(GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED);
    
    private static final Map<Integer, FlyingCameraModeValue> intToTypeMap = null;
    public final int value;

    static {
        intToTypeMap = new HashMap();
        FlyingCameraModeValue[] values = values();
        int length = values.length;
        int i;
        while (i < length) {
            FlyingCameraModeValue flyingCameraModeValue = values[i];
            intToTypeMap.put(Integer.valueOf(flyingCameraModeValue.value), flyingCameraModeValue);
            i++;
        }
    }

    private FlyingCameraModeValue(int i) {
        this.value = i;
    }

    public static FlyingCameraModeValue fromInteger(int i) {
        return (FlyingCameraModeValue) intToTypeMap.get(Integer.valueOf(i));
    }
}
