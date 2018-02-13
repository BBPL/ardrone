package com.parrot.freeflight.ui.hud;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import com.parrot.freeflight.ui.gl.GLSprite;
import com.parrot.freeflight.ui.gl.IBitmapLoader;
import com.parrot.freeflight.ui.hud.Sprite.Align;
import javax.microedition.khronos.opengles.GL10;

public class Image extends Sprite {
    private boolean blinkVisible = true;
    private long blinkingDuration = 0;
    private boolean blinkingEnabled = false;
    private long blinkingStartTimeMillis = 0;
    private float currAngle = 0.0f;
    private SizeParams heightParam = SizeParams.NONE;
    private boolean isInitialized = false;
    private long prevDrawTimeMillis = 0;
    private float rotationDegreePerSecond = 0.0f;
    private GLSprite sprite;
    private SizeParams widthParam = SizeParams.NONE;

    public enum SizeParams {
        NONE,
        FILL_SCREEN
    }

    public Image(IBitmapLoader iBitmapLoader, Align align) {
        super(align);
        this.sprite = new GLSprite(iBitmapLoader);
    }

    public void draw(GL10 gl10) {
        long currentTimeMillis = (this.rotationDegreePerSecond != 0.0f || this.blinkingEnabled) ? System.currentTimeMillis() : 0;
        if (this.rotationDegreePerSecond != 0.0f) {
            if (this.prevDrawTimeMillis != 0) {
                this.currAngle = ((((float) (currentTimeMillis - this.prevDrawTimeMillis)) * this.rotationDegreePerSecond) / 1000.0f) + this.currAngle;
                this.sprite.setRotation(this.currAngle);
            }
            this.prevDrawTimeMillis = currentTimeMillis;
        }
        if (this.blinkingEnabled) {
            if (this.blinkingStartTimeMillis == 0) {
                this.blinkingStartTimeMillis = currentTimeMillis;
            } else if (this.blinkingDuration != 0) {
                this.blinkVisible = ((currentTimeMillis - this.blinkingStartTimeMillis) / this.blinkingDuration) % 2 == 0;
            }
        }
        if (!this.visible) {
            return;
        }
        if (!this.blinkingEnabled || (this.blinkingEnabled && this.blinkVisible)) {
            this.sprite.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.sprite.height));
        }
    }

    public void freeResources() {
        this.sprite.freeResources();
    }

    public int getHeight() {
        return this.sprite.getHeight();
    }

    public GLSprite getSprite() {
        return this.sprite;
    }

    public int getWidth() {
        return this.sprite.getWidth();
    }

    public void init(GL10 gl10, int i) {
        this.sprite.init(gl10, i);
        this.isInitialized = true;
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    protected void onAlphaChanged(float f) {
        this.sprite.setAlpha(f);
    }

    public boolean onTouchEvent(View view, MotionEvent motionEvent) {
        return false;
    }

    public void setBlinkingDuration(long j) {
        this.blinkingDuration = j;
    }

    public void setBlinkingEnabled(boolean z) {
        if (z != this.blinkingEnabled) {
            if (z) {
                this.blinkingStartTimeMillis = 0;
            }
            this.blinkingEnabled = z;
        }
    }

    public void setBounds(Rect rect) {
        this.bounds.set(rect);
    }

    public void setClampMode(int i) {
        this.sprite.setClampMode(i);
    }

    public void setFilteringMode(int i) {
        this.sprite.setFilteringMode(i);
    }

    public void setImage(Bitmap bitmap) {
        this.sprite.updateTexture(bitmap);
    }

    public void setPosition(int i, int i2) {
        this.bounds.offsetTo(i, i2);
    }

    public void setRotation(float f) {
        this.currAngle = f;
        this.sprite.setRotation(this.currAngle);
    }

    public void setRotationPerSecond(float f) {
        this.rotationDegreePerSecond = f;
    }

    public void setSizeParams(SizeParams sizeParams, SizeParams sizeParams2) {
        this.widthParam = sizeParams;
        this.heightParam = sizeParams2;
    }

    public void setViewAndProjectionMatrices(float[] fArr, float[] fArr2) {
        this.sprite.setViewAndProjectionMatrices(fArr, fArr2);
    }

    public void surfaceChanged(GL10 gl10, int i, int i2) {
        this.sprite.onSurfaceChanged(gl10, i, i2);
        if (this.widthParam == SizeParams.FILL_SCREEN || this.heightParam == SizeParams.FILL_SCREEN) {
            this.sprite.setSize(this.widthParam == SizeParams.FILL_SCREEN ? i : this.sprite.width, this.heightParam == SizeParams.FILL_SCREEN ? i2 : this.sprite.height);
        }
        super.surfaceChanged(gl10, i, i2);
    }
}
