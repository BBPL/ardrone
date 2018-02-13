package com.parrot.freeflight.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

public class TextureUtils {
    public static Bitmap makeTexture(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap can't be null");
        }
        int roundPower2 = (int) roundPower2((long) bitmap.getHeight());
        int roundPower22 = (int) roundPower2((long) bitmap.getWidth());
        if (z) {
            return Bitmap.createScaledBitmap(bitmap, roundPower22, roundPower2, true);
        }
        Bitmap createBitmap = Bitmap.createBitmap(roundPower22, roundPower2, Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, 0.0f, 0.0f, null);
        return createBitmap;
    }

    public static long roundPower2(long j) {
        int i = 256;
        while (((long) i) < j) {
            i <<= 1;
        }
        return (long) i;
    }
}
