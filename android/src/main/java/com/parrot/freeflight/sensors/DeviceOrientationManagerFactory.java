package com.parrot.freeflight.sensors;

import android.app.Activity;
import com.parrot.freeflight.utils.SystemUtils;

public class DeviceOrientationManagerFactory {
    public static DeviceOrientationManager createDeviceOrienationManager(Activity activity, DeviceOrientationChangeDelegate deviceOrientationChangeDelegate) {
        return SystemUtils.isGoogleTV(activity) ? new DeviceOrientationManager(new RemoteSensorManagerWrapper(activity), deviceOrientationChangeDelegate) : new DeviceOrientationManager(new DeviceSensorManagerWrapper(activity), deviceOrientationChangeDelegate);
    }
}
