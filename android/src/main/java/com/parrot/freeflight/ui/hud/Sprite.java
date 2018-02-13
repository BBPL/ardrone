package com.parrot.freeflight.ui.hud;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import javax.microedition.khronos.opengles.GL10;

public abstract class Sprite {
    protected Align alignment;
    private float alpha;
    protected boolean alphaEnabled;
    protected final Rect bounds = new Rect();
    protected boolean enabled;
    protected final Rect margin = new Rect();
    protected final Point offset = new Point();
    private float prevAlpha;
    protected int surfaceHeight;
    protected int surfaceWidth;
    protected boolean visible;

    public enum Align {
        NO_ALIGN,
        CENTER,
        TOP_CENTER,
        BOTTOM_CENTER,
        TOP_RIGHT,
        TOP_LEFT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }

    public Sprite(Align align) {
        this.alignment = align;
        this.alphaEnabled = true;
        this.visible = true;
        this.alpha = 1.0f;
        this.enabled = true;
    }

    public abstract void draw(GL10 gl10);

    public abstract void freeResources();

    public final float getAlpha() {
        return this.alpha;
    }

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract void init(GL10 gl10, int i);

    public boolean isEnabled() {
        return this.enabled;
    }

    public abstract boolean isInitialized();

    public boolean isVisible() {
        return this.visible;
    }

    protected void layout(int i, int i2) {
        switch (this.alignment) {
            case TOP_CENTER:
                this.bounds.set(((i - getWidth()) / 2) + this.offset.x, this.margin.top + this.offset.y, (((i - getWidth()) / 2) + getWidth()) + this.offset.x, (getHeight() + this.margin.top) + this.offset.y);
                return;
            case BOTTOM_CENTER:
                this.bounds.set(((i - getWidth()) / 2) + this.offset.x, (i2 - getHeight()) + this.offset.y, (((i - getWidth()) / 2) + getWidth()) + this.offset.x, this.offset.y + i2);
                return;
            case TOP_RIGHT:
                this.bounds.set(((i - getWidth()) - this.margin.right) + this.offset.x, this.margin.top + this.offset.y, (i - this.margin.right) + this.offset.x, (this.margin.top + getHeight()) + this.offset.y);
                return;
            case TOP_LEFT:
                this.bounds.set(this.margin.left + this.offset.x, this.margin.top + this.offset.y, (this.margin.left + getWidth()) + this.offset.x, (this.margin.top + getHeight()) + this.offset.y);
                return;
            case BOTTOM_LEFT:
                this.bounds.set(this.margin.left + this.offset.x, ((i2 - getHeight()) - this.margin.bottom) + this.offset.y, (getWidth() + this.margin.left) + this.offset.x, (i2 - this.margin.bottom) + this.offset.y);
                return;
            case BOTTOM_RIGHT:
                this.bounds.set(((i - getWidth()) - this.margin.right) + this.offset.x, ((i2 - getHeight()) - this.margin.bottom) + this.offset.y, (i - this.margin.right) + this.offset.x, (i2 - this.margin.bottom) + this.offset.y);
                return;
            case CENTER:
                this.bounds.set(((i - getWidth()) / 2) + this.offset.x, ((i2 - getHeight()) / 2) + this.offset.y, (((i - getWidth()) / 2) + getWidth()) + this.offset.x, (((i2 - getHeight()) / 2) + getHeight()) + this.offset.y);
                return;
            default:
                return;
        }
    }

    protected void onAlphaChanged(float f) {
    }

    public abstract boolean onTouchEvent(View view, MotionEvent motionEvent);

    public boolean processTouch(View view, MotionEvent motionEvent) {
        return onTouchEvent(view, motionEvent);
    }

    public void setAlign(Align align) {
        this.alignment = align;
    }

    public final void setAlpha(float f) {
        if (this.alphaEnabled) {
            Object obj = f != this.alpha ? 1 : null;
            this.alpha = f;
            if (obj != null && this.alphaEnabled) {
                onAlphaChanged(this.alpha);
            }
        }
    }

    public void setAlphaEnabled(boolean z) {
        this.alphaEnabled = z;
    }

    public void setEnabled(boolean z) {
        if (this.enabled != z) {
            this.enabled = z;
            if (!z) {
                this.prevAlpha = getAlpha();
                setAlpha(0.5f);
            } else if (this.prevAlpha != 0.0f) {
                setAlpha(this.prevAlpha);
            } else {
                setAlpha(1.0f);
            }
        }
    }

    public void setMargin(int i, int i2, int i3, int i4) {
        this.margin.set(i4, i, i2, i3);
    }

    public void setOffset(int i, int i2) {
        this.offset.set(i, i2);
    }

    public abstract void setViewAndProjectionMatrices(float[] fArr, float[] fArr2);

    public void setVisible(boolean z) {
        this.visible = z;
    }

    public void surfaceChanged(GL10 gl10, int i, int i2) {
        this.surfaceWidth = i;
        this.surfaceHeight = i2;
        layout(i, i2);
    }
}
