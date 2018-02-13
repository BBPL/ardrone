package com.parrot.ardronetool.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.parrot.ardronetool.ARDroneEngine.EVideoRecorderCapability;
import com.parrot.freeflight.utils.ThumbnailUtils;
import org.mortbay.jetty.HttpVersions;

public class DeviceCapabilitiesUtils {
    public static void dumpScreenSizeInDips(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        float f = activity.getResources().getDisplayMetrics().density;
        float f2 = ((float) displayMetrics.heightPixels) / f;
        Log.i("Display", HttpVersions.HTTP_0_9 + f2 + "dp x " + (((float) displayMetrics.widthPixels) / f) + "dp" + " density: " + f);
    }

    public static EVideoRecorderCapability getMaxSupportedVideoRes(Context context) {
        int min;
        EVideoRecorderCapability eVideoRecorderCapability = EVideoRecorderCapability.VIDEO_720P;
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (VERSION.SDK_INT >= 13) {
            Point point = new Point();
            defaultDisplay.getSize(point);
            min = Math.min(point.x, point.y);
        } else {
            min = Math.min(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        }
        return min >= 480 ? EVideoRecorderCapability.VIDEO_720P : min >= ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL ? EVideoRecorderCapability.VIDEO_360P : EVideoRecorderCapability.NOT_SUPPORTED;
    }
}
