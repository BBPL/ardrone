package com.parrot.freeflight.track_3d_viewer.utils;

import android.opengl.GLSurfaceView.EGLConfigChooser;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class MultisampleConfigChooser implements EGLConfigChooser {
    private static final String kTag = "GDC11";
    private boolean mUsesCoverageAa;
    private int[] mValue;

    private int findConfigAttrib(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
        return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.mValue) ? this.mValue[0] : i2;
    }

    public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
        this.mValue = new int[1];
        int[] iArr = new int[]{12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12338, 1, 12337, 2, 12344};
        if (egl10.eglChooseConfig(eGLDisplay, iArr, null, 0, this.mValue)) {
            int i = this.mValue[0];
            if (i <= 0) {
                iArr = new int[]{12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12512, 1, 12513, 2, 12344};
                if (egl10.eglChooseConfig(eGLDisplay, iArr, null, 0, this.mValue)) {
                    i = this.mValue[0];
                    if (i <= 0) {
                        iArr = new int[]{12324, 5, 12323, 6, 12322, 5, 12325, 16, 12352, 4, 12344};
                        if (egl10.eglChooseConfig(eGLDisplay, iArr, null, 0, this.mValue)) {
                            i = this.mValue[0];
                            if (i <= 0) {
                                throw new IllegalArgumentException("No configs match configSpec");
                            }
                        }
                        throw new IllegalArgumentException("3rd eglChooseConfig failed");
                    }
                    this.mUsesCoverageAa = true;
                } else {
                    throw new IllegalArgumentException("2nd eglChooseConfig failed");
                }
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[i];
            if (egl10.eglChooseConfig(eGLDisplay, iArr, eGLConfigArr, i, this.mValue)) {
                int i2 = 0;
                while (i2 < eGLConfigArr.length) {
                    if (findConfigAttrib(egl10, eGLDisplay, eGLConfigArr[i2], 12324, 0) == 5) {
                        break;
                    }
                    i2++;
                }
                i2 = -1;
                if (i2 == -1) {
                    Log.w(kTag, "Did not find sane config, using first");
                }
                EGLConfig eGLConfig = eGLConfigArr.length > 0 ? eGLConfigArr[i2] : null;
                if (eGLConfig != null) {
                    return eGLConfig;
                }
                throw new IllegalArgumentException("No config chosen");
            }
            throw new IllegalArgumentException("data eglChooseConfig failed");
        }
        throw new IllegalArgumentException("eglChooseConfig failed");
    }

    public boolean usesCoverageAa() {
        return this.mUsesCoverageAa;
    }
}
