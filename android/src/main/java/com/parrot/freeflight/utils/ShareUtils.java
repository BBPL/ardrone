package com.parrot.freeflight.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Video.Media;
import android.util.Log;
import com.parrot.freeflight.C0984R;
import java.io.File;

public final class ShareUtils {
    private static final String TAG = ShareUtils.class.getSimpleName();

    private ShareUtils() {
    }

    public static void shareLink(Context context, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TITLE", "Check this out!");
        intent.putExtra("android.intent.extra.TEXT", str);
        context.startActivity(Intent.createChooser(intent, context.getString(C0984R.string.ff_ID000060)));
    }

    public static void sharePhoto(Context context, Uri uri, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(str);
        intent.putExtra("android.intent.extra.STREAM", uri);
        context.startActivity(Intent.createChooser(intent, context.getString(C0984R.string.ff_ID000060)));
    }

    public static void sharePhoto(Context context, String str) {
        File file = new File(str);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/jpeg");
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
        context.startActivity(Intent.createChooser(intent, "Share Image"));
    }

    public static void shareVideo(Context context, Uri uri, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(str);
        intent.putExtra("android.intent.extra.STREAM", uri);
        context.startActivity(Intent.createChooser(intent, context.getString(C0984R.string.ff_ID000060)));
    }

    public static void shareVideo(Context context, String str) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String name = new File(str).getName();
        Cursor query = contentResolver.query(uri, new String[]{"_id", "mime_type"}, "_display_name=? and bucket_display_name=?", new String[]{name, FileUtils.MEDIA_PUBLIC_FOLDER_NAME}, null);
        if (query == null) {
            Log.w(TAG, "Unknown error");
        } else if (query.moveToFirst()) {
            int columnIndex = query.getColumnIndex("_id");
            int columnIndex2 = query.getColumnIndex("mime_type");
            long j = query.getLong(columnIndex);
            String string = query.getString(columnIndex2);
            if (string == null) {
                string = "video/mp4";
            }
            Log.i("shareVideo", "Video id: " + j + " type: " + string);
            shareVideo(context, Uri.withAppendedPath(uri, String.valueOf(j)), string);
        } else {
            Log.w(TAG, "Error, no such file");
        }
        query.close();
    }
}
