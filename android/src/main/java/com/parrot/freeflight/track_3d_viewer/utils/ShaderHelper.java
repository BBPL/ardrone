package com.parrot.freeflight.track_3d_viewer.utils;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileShader(int i, String str) {
        int i2 = 0;
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(glCreateShader));
                GLES20.glDeleteShader(glCreateShader);
                if (i2 == 0) {
                    return i2;
                }
                throw new RuntimeException("Error creating shader.");
            }
        }
        i2 = glCreateShader;
        if (i2 == 0) {
            return i2;
        }
        throw new RuntimeException("Error creating shader.");
    }

    public static int createAndLinkProgram(int i, int i2, String[] strArr) {
        int i3 = 0;
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram != 0) {
            GLES20.glAttachShader(glCreateProgram, i);
            GLES20.glAttachShader(glCreateProgram, i2);
            if (strArr != null) {
                int length = strArr.length;
                for (int i4 = 0; i4 < length; i4++) {
                    GLES20.glBindAttribLocation(glCreateProgram, i4, strArr[i4]);
                }
            }
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(glCreateProgram));
                GLES20.glDeleteProgram(glCreateProgram);
                if (i3 == 0) {
                    return i3;
                }
                throw new RuntimeException("Error creating program.");
            }
        }
        i3 = glCreateProgram;
        if (i3 == 0) {
            return i3;
        }
        throw new RuntimeException("Error creating program.");
    }
}
