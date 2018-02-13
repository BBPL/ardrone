package com.parrot.freeflight.utils;

import android.graphics.Bitmap;

public class ThumbnailUtils {
    public static final int OPTIONS_RECYCLE_INPUT = 2;
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;
    public static final int TARGET_SIZE_MINI_THUMBNAIL = 320;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"NewApi"})
    public static android.graphics.Bitmap createVideoThumbnail(java.lang.String r7, int r8) {
        /*
        r6 = 96;
        r5 = 1;
        r2 = 0;
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 10;
        if (r0 >= r1) goto L_0x000f;
    L_0x000a:
        r2 = android.media.ThumbnailUtils.createVideoThumbnail(r7, r8);
    L_0x000e:
        return r2;
    L_0x000f:
        r3 = new android.media.MediaMetadataRetriever;
        r3.<init>();
        r3.setDataSource(r7);	 Catch:{ IllegalArgumentException -> 0x005a, RuntimeException -> 0x007c, all -> 0x004c }
        r0 = 0;
        r1 = r3.getFrameAtTime(r0);	 Catch:{ IllegalArgumentException -> 0x005a, RuntimeException -> 0x007c, all -> 0x004c }
        r3.release();	 Catch:{ RuntimeException -> 0x00b2 }
        r0 = r1;
    L_0x0021:
        if (r0 == 0) goto L_0x000e;
    L_0x0023:
        if (r8 != r5) goto L_0x0051;
    L_0x0025:
        r1 = r0.getWidth();
        r2 = r0.getHeight();
        r3 = java.lang.Math.max(r1, r2);
        r4 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        if (r3 <= r4) goto L_0x004a;
    L_0x0035:
        r4 = 1140850688; // 0x44000000 float:512.0 double:5.63655132E-315;
        r3 = (float) r3;
        r3 = r4 / r3;
        r1 = (float) r1;
        r1 = r1 * r3;
        r1 = java.lang.Math.round(r1);
        r2 = (float) r2;
        r2 = r2 * r3;
        r2 = java.lang.Math.round(r2);
        r0 = android.graphics.Bitmap.createScaledBitmap(r0, r1, r2, r5);
    L_0x004a:
        r2 = r0;
        goto L_0x000e;
    L_0x004c:
        r0 = move-exception;
        r3.release();	 Catch:{ RuntimeException -> 0x0098 }
    L_0x0050:
        throw r0;
    L_0x0051:
        r1 = 3;
        if (r8 != r1) goto L_0x004a;
    L_0x0054:
        r1 = 2;
        r0 = android.media.ThumbnailUtils.extractThumbnail(r0, r6, r6, r1);
        goto L_0x004a;
    L_0x005a:
        r0 = move-exception;
        r3.release();	 Catch:{ RuntimeException -> 0x0060 }
        r0 = r2;
        goto L_0x0021;
    L_0x0060:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "MediaMetadataRetriever failed with exception: ";
        r1 = r1.append(r3);
        r0 = r1.append(r0);
        r0 = r0.toString();
        r1 = r2;
    L_0x0075:
        r3 = "ThumbnailUtils";
        android.util.Log.w(r3, r0);
        r0 = r1;
        goto L_0x0021;
    L_0x007c:
        r0 = move-exception;
        r3.release();	 Catch:{ RuntimeException -> 0x0082 }
        r0 = r2;
        goto L_0x0021;
    L_0x0082:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "MediaMetadataRetriever failed with exception: ";
        r1 = r1.append(r3);
        r0 = r1.append(r0);
        r0 = r0.toString();
        r1 = r2;
        goto L_0x0075;
    L_0x0098:
        r1 = move-exception;
        r2 = "ThumbnailUtils";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "MediaMetadataRetriever failed with exception: ";
        r3 = r3.append(r4);
        r1 = r3.append(r1);
        r1 = r1.toString();
        android.util.Log.w(r2, r1);
        goto L_0x0050;
    L_0x00b2:
        r0 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "MediaMetadataRetriever failed with exception: ";
        r3 = r3.append(r4);
        r0 = r3.append(r0);
        r0 = r0.toString();
        goto L_0x0075;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.utils.ThumbnailUtils.createVideoThumbnail(java.lang.String, int):android.graphics.Bitmap");
    }

    public static Bitmap extractThumbnail(Bitmap bitmap, int i, int i2) {
        return android.media.ThumbnailUtils.extractThumbnail(bitmap, 96, 96, 2);
    }

    public static Bitmap extractThumbnail(Bitmap bitmap, int i, int i2, int i3) {
        return android.media.ThumbnailUtils.extractThumbnail(bitmap, 96, 96, i3);
    }
}
