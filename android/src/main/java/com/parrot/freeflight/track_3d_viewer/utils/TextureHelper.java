package com.parrot.freeflight.track_3d_viewer.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureHelper {
    private static void checkGlError() {
        if (GLES20.glGetError() != 0) {
            Log.e("opengl", String.format("OpenGl error: %d (%s)", new Object[]{Integer.valueOf(GLES20.glGetError()), GLUtils.getEGLErrorString(GLES20.glGetError())}));
        }
    }

    public static int loadTexture(Resources resources, int i) {
        Bitmap decodeResource = BitmapFactory.decodeResource(resources, i, null);
        try {
            int loadTexture = loadTexture(decodeResource);
            return loadTexture;
        } finally {
            decodeResource.recycle();
        }
    }

    public static int loadTexture(Bitmap bitmap) {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        if (iArr[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        GLES20.glBindTexture(3553, iArr[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLES20.glBindTexture(3553, 0);
        return iArr[0];
    }

    public static void updateTexture(int i, Bitmap bitmap) {
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLES20.glBindTexture(3553, 0);
    }
}
