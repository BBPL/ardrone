package com.parrot.freeflight.ui.gl;

import android.opengl.GLES20;
import javax.microedition.khronos.opengles.GL10;

public class GLBGVideoSprite extends GLSprite {
    private static final String TAG = GLBGVideoSprite.class.getSimpleName();
    private boolean isVideoReady = false;
    private int prevImgHeight;
    private int prevImgWidth;
    public int screenHeight;
    public int screenWidth;
    private int f348x;
    private int f349y;

    public GLBGVideoSprite() {
        super(null);
    }

    private native void onSurfaceChangedNative(int i, int i2);

    private native boolean onUpdateVideoTextureNative(int i, int i2);

    public void init(GL10 gl10, int i) {
        super.init(gl10, i);
        this.isVideoReady = false;
        this.prevImgHeight = 0;
        this.prevImgWidth = 0;
    }

    public void onDraw(GL10 gl10, float f, float f2) {
        if (!this.isVideoReady) {
            GLES20.glClear(16384);
        }
        super.onDraw(gl10, (float) this.f348x, (float) this.f349y);
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.screenWidth = i;
        this.screenHeight = i2;
        onSurfaceChangedNative(i, i2);
        super.onSurfaceChanged(gl10, i, i2);
    }

    protected void onUpdateTexture() {
        if (!onUpdateVideoTextureNative(this.program, this.textures[0])) {
            return;
        }
        if (this.prevImgWidth != this.imageWidth || this.prevImgHeight != this.imageHeight) {
            if (!this.isVideoReady) {
                this.isVideoReady = true;
            }
            float max = Math.max(((float) this.screenWidth) / ((float) this.imageWidth), ((float) this.screenHeight) / ((float) this.imageHeight));
            setSize((int) (((float) this.imageWidth) * max), (int) (max * ((float) this.imageHeight)));
            this.f348x = (this.screenWidth - this.width) / 2;
            this.f349y = (this.screenHeight - this.height) / 2;
            this.prevImgWidth = this.imageWidth;
            this.prevImgHeight = this.imageHeight;
        }
    }
}
