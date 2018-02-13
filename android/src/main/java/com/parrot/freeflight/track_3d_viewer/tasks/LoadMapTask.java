package com.parrot.freeflight.track_3d_viewer.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Locale;

public class LoadMapTask extends AsyncTask<TaskParams, Void, TaskResult> {

    public static class TaskParams {
        public final float latitude;
        public final float longitude;
        public final int width;
        public final int zoom;

        public TaskParams(float f, float f2, int i, int i2) {
            this.latitude = f;
            this.longitude = f2;
            this.zoom = i;
            this.width = i2;
        }
    }

    public static class TaskResult {
        public final Bitmap bitmap;
        public final Exception exception;

        public TaskResult(Bitmap bitmap, Exception exception) {
            this.bitmap = bitmap;
            this.exception = exception;
        }
    }

    private String formatGoogleMapsUrl(float f, float f2, int i, int i2) {
        return String.format(Locale.US, "http://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=%d&size=%dx%d&sensor=false&maptype=hybrid", new Object[]{Float.valueOf(f), Float.valueOf(f2), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i2)});
    }

    protected TaskResult doInBackground(TaskParams... taskParamsArr) {
        try {
            return new TaskResult(BitmapFactory.decodeStream(new BufferedInputStream(new URL(formatGoogleMapsUrl(taskParamsArr[0].latitude, taskParamsArr[0].longitude, taskParamsArr[0].zoom, taskParamsArr[0].width)).openStream(), 8192)), null);
        } catch (Exception e) {
            return new TaskResult(null, e);
        }
    }
}
