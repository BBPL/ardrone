package com.parrot.freeflight;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.utils.SystemUtils;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class FreeFlightApplication extends Application {
    private static final boolean DEVELOPER_MODE = false;
    private static final String TAG = "FreeFlightApplication";
    private final UncaughtExceptionHandler handler = new C09831();
    private UncaughtExceptionHandler prevHandler;
    private ApplicationSettings settings;

    class C09831 implements UncaughtExceptionHandler {
        C09831() {
        }

        public void uncaughtException(Thread thread, Throwable th) {
            Log.d("FREEFLIGHT", String.format("Uncaught exception: %s", new Object[]{th.toString()}));
            SystemUtils.appendStrToFile(BuildSettings.LOG_CRASHES_FILE_NAME, String.format("%s, thread: %d, exception: %s\n", new Object[]{new Date().toString(), Long.valueOf(thread.getId()), Log.getStackTraceString(th)}));
            FreeFlightApplication.this.prevHandler.uncaughtException(thread, th);
        }
    }

    static {
        System.loadLibrary("avutil");
        System.loadLibrary("swscale");
        System.loadLibrary("avcodec");
        System.loadLibrary("avfilter");
        System.loadLibrary("avformat");
        System.loadLibrary("avdevice");
        System.loadLibrary("adfreeflight");
    }

    public ApplicationSettings getAppSettings() {
        return this.settings;
    }

    @SuppressLint({"NewApi"})
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");
        this.settings = new ApplicationSettings(this);
    }

    public void onTerminate() {
        Log.d(TAG, "OnTerminate");
        super.onTerminate();
    }
}
