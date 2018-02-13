package com.parrot.freeflight.ui.hud;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.ui.gl.GLSprite;
import com.parrot.freeflight.ui.gl.ResourceBitmapLoader;
import com.parrot.freeflight.ui.hud.Sprite.Align;
import javax.microedition.khronos.opengles.GL10;

public abstract class JoystickBase extends Sprite {
    private static final double CONTROL_RATIO = 0.0d;
    protected boolean absolute;
    protected RectF activationRect;
    protected JoystickListener analogueListener;
    private float baseX;
    private float baseY;
    protected GLSprite bg;
    protected float centerX;
    protected float centerY;
    private float controlRatio;
    protected int fingerId;
    protected boolean inverseY;
    private boolean isInitialized = false;
    protected boolean isPressed = false;
    private float opacity = 1.0f;
    protected GLSprite thumbAbsolute;
    private float thumbCenterX;
    private float thumbCenterY;
    protected GLSprite thumbNormal;

    public JoystickBase(Context context, Align align, boolean z) {
        super(align);
        this.absolute = z;
        this.centerX = 0.0f;
        this.centerY = 0.0f;
        int backgroundDrawableId = getBackgroundDrawableId();
        int tumbDrawableId = getTumbDrawableId();
        int thumbAbsoluteDrawableId = getThumbAbsoluteDrawableId();
        if (backgroundDrawableId != 0) {
            this.bg = new GLSprite(new ResourceBitmapLoader(context.getResources(), backgroundDrawableId));
        }
        if (tumbDrawableId != 0) {
            this.thumbNormal = new GLSprite(new ResourceBitmapLoader(context.getResources(), tumbDrawableId));
        }
        if (thumbAbsoluteDrawableId != 0) {
            this.thumbAbsolute = new GLSprite(new ResourceBitmapLoader(context.getResources(), thumbAbsoluteDrawableId));
        }
        this.fingerId = -1;
        this.controlRatio = 0.5f;
    }

    private int getThumbAbsoluteDrawableId() {
        return C0984R.drawable.joystick_absolut_control;
    }

    private float getXValue(float f, float f2, float f3) {
        return (GroundOverlayOptions.NO_DIMENSION * ((f - f2) - (f3 - (this.controlRatio * (f3 * 2.0f))))) / (this.controlRatio * (f3 * 2.0f));
    }

    private float getYValue(float f, float f2, float f3) {
        return (GroundOverlayOptions.NO_DIMENSION * ((f - inverseY(f2)) - (f3 - (this.controlRatio * (f3 * 2.0f))))) / (this.controlRatio * (f3 * 2.0f));
    }

    private void updateActivatonRegion(int i, int i2) {
        switch (this.alignment) {
            case BOTTOM_LEFT:
                setActivationRect(new Rect(0, 0, i / 2, i2));
                return;
            case BOTTOM_RIGHT:
                setActivationRect(new Rect(i / 2, 0, i, i2));
                return;
            case BOTTOM_CENTER:
                setActivationRect(new Rect(0, 0, i, i2));
                return;
            default:
                return;
        }
    }

    private void updateControlOpacity() {
        if (this.isPressed) {
            if (this.bg != null) {
                this.bg.alpha = 1.0f;
            }
            if (this.bg != null) {
                this.thumbNormal.alpha = 1.0f;
            }
            if (this.thumbAbsolute != null) {
                this.thumbAbsolute.alpha = 1.0f;
                return;
            }
            return;
        }
        if (this.bg != null) {
            this.bg.alpha = this.opacity;
        }
        if (this.thumbNormal != null) {
            this.thumbNormal.alpha = this.opacity;
        }
        if (this.thumbAbsolute != null) {
            this.thumbAbsolute.alpha = this.opacity;
        }
    }

    public void clearPressedState() {
        onActionUp();
        this.isPressed = false;
        this.fingerId = -1;
    }

    public void draw(GL10 gl10) {
        if (this.visible) {
            updateControlOpacity();
            if (this.bg != null) {
                this.bg.onDraw(gl10, this.centerX - ((float) (this.bg.width >> 1)), this.centerY - ((float) (this.bg.height >> 1)));
            }
            if (this.absolute) {
                if (this.thumbAbsolute != null) {
                    this.thumbAbsolute.onDraw(gl10, this.thumbCenterX - ((float) (this.thumbNormal.width >> 1)), this.thumbCenterY - ((float) (this.thumbNormal.height >> 1)));
                }
            } else if (this.thumbNormal != null) {
                this.thumbNormal.onDraw(gl10, this.thumbCenterX - ((float) (this.thumbNormal.width >> 1)), this.thumbCenterY - ((float) (this.thumbNormal.height >> 1)));
            }
        }
    }

    public void freeResources() {
        this.bg.freeResources();
        this.thumbNormal.freeResources();
        this.thumbAbsolute.freeResources();
    }

    protected int getBackgroundDrawableId() {
        return C0984R.drawable.joystick_halo;
    }

    public int getHeight() {
        return this.bg.getHeight();
    }

    protected int getTumbDrawableId() {
        return C0984R.drawable.joystick_manuel;
    }

    public int getWidth() {
        return this.bg.getWidth();
    }

    public void init(GL10 gl10, int i) {
        this.bg.init(gl10, i);
        this.thumbNormal.init(gl10, i);
        this.thumbAbsolute.init(gl10, i);
    }

    protected float inverseY(float f) {
        return this.inverseY ? ((float) this.surfaceHeight) - f : f;
    }

    public boolean isAbsoluteControl() {
        return this.absolute;
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    public void moveThumbTo(float f, float f2) {
        double d = (double) (f - this.centerX);
        double d2 = (double) (f2 - this.centerY);
        double sqrt = Math.sqrt((d * d) + (d2 * d2));
        d = Math.atan2(d2, d);
        float f3 = (((float) this.bg.width) / 2.0f) - ((((float) this.thumbNormal.width) * 0.33f) / 2.0f);
        if (sqrt > ((double) f3)) {
            sqrt = Math.cos(d);
            double d3 = (double) f3;
            d = Math.sin(d);
            d2 = (double) f3;
            this.thumbCenterX = ((float) (sqrt * d3)) + this.centerX;
            this.thumbCenterY = ((float) (d * d2)) + this.centerY;
            return;
        }
        this.thumbCenterX = f;
        this.thumbCenterY = f2;
    }

    public void moveTo(float f, float f2) {
        this.centerX = f;
        this.centerY = inverseY(f2);
        moveThumbTo(f, inverseY(f2));
    }

    protected void moveToBase(RectF rectF) {
        moveTo(this.baseX, this.baseY);
    }

    protected void onActionDown(float f, float f2) {
        this.isPressed = true;
        moveTo(f, f2);
        if (this.analogueListener != null) {
            this.analogueListener.onChanged(this, 0.0f, 0.0f);
            this.analogueListener.onPressed(this);
        }
    }

    protected void onActionMove(float f, float f2) {
        float f3 = 0.0f;
        float f4 = (((float) this.bg.width) / 2.0f) - ((((float) this.thumbNormal.width) * 0.33f) / 2.0f);
        moveThumbTo(f, inverseY(f2));
        if (this.analogueListener != null) {
            float xValue = this.centerX - f > f4 - ((this.controlRatio * ((float) this.bg.width)) / 2.0f) ? getXValue(this.centerX, f, f4) : f - this.centerX > f4 - ((this.controlRatio * ((float) this.bg.width)) / 2.0f) ? getXValue(this.centerX, f, f4) : 0.0f;
            if (this.centerY - inverseY(f2) > f4 - ((this.controlRatio * ((float) this.bg.width)) / 2.0f)) {
                f3 = getYValue(this.centerY, f2, f4);
            } else if (inverseY(f2) - this.centerY > f4 - ((this.controlRatio * ((float) this.bg.width)) / 2.0f)) {
                f3 = getYValue(this.centerY, f2, f4);
            }
            this.analogueListener.onChanged(this, xValue, f3);
        }
    }

    protected void onActionUp() {
        moveToBase(this.activationRect);
        if (this.analogueListener != null) {
            this.analogueListener.onChanged(this, 0.0f, 0.0f);
            this.analogueListener.onReleased(this);
        }
    }

    protected void onAlphaChanged(float f) {
        this.opacity = f;
    }

    public boolean onTouchEvent(View view, MotionEvent motionEvent) {
        if (this.activationRect == null) {
            return false;
        }
        int action = motionEvent.getAction();
        int i = action >> 8;
        switch (action & 255) {
            case 0:
            case 5:
                if (this.fingerId != -1 || !this.activationRect.contains(motionEvent.getX(i), motionEvent.getY(i))) {
                    return false;
                }
                this.fingerId = motionEvent.getPointerId(i);
                this.isPressed = true;
                onActionDown(motionEvent.getX(i), motionEvent.getY(i));
                return true;
            case 1:
            case 6:
                if (this.fingerId == -1 || motionEvent.getPointerId(i) != this.fingerId) {
                    return false;
                }
                this.fingerId = -1;
                onActionUp();
                this.isPressed = false;
                return true;
            case 2:
                if (this.fingerId == -1) {
                    return false;
                }
                for (int i2 = 0; i2 < motionEvent.getPointerCount(); i2++) {
                    if (motionEvent.getPointerId(i2) == this.fingerId) {
                        onActionMove(motionEvent.getX(i2), motionEvent.getY(i2));
                        return false;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    public void setAbsolute(boolean z) {
        this.absolute = z;
    }

    public void setActivationRect(Rect rect) {
        this.activationRect = new RectF(rect);
        switch (this.alignment) {
            case BOTTOM_LEFT:
                this.baseX = (this.activationRect.left + (((float) this.bg.width) / 2.0f)) + ((float) this.margin.left);
                this.baseY = (this.activationRect.bottom - (((float) this.bg.height) / 2.0f)) - ((float) this.margin.bottom);
                break;
            case BOTTOM_RIGHT:
                this.baseX = (this.activationRect.right - (((float) this.bg.width) / 2.0f)) - ((float) this.margin.right);
                this.baseY = (this.activationRect.bottom - (((float) this.bg.height) / 2.0f)) - ((float) this.margin.bottom);
                break;
            case BOTTOM_CENTER:
                this.baseX = (this.activationRect.right - this.activationRect.left) / 2.0f;
                this.baseY = (this.activationRect.bottom - (((float) this.bg.height) / 2.0f)) - ((float) this.margin.bottom);
                break;
        }
        moveToBase(this.activationRect);
    }

    public void setInverseYWhenDraw(boolean z) {
        this.inverseY = z;
    }

    public void setOnAnalogueChangedListener(JoystickListener joystickListener) {
        this.analogueListener = joystickListener;
    }

    public void setViewAndProjectionMatrices(float[] fArr, float[] fArr2) {
        this.bg.setViewAndProjectionMatrices(fArr, fArr2);
        this.thumbNormal.setViewAndProjectionMatrices(fArr, fArr2);
        this.thumbAbsolute.setViewAndProjectionMatrices(fArr, fArr2);
    }

    public void surfaceChanged(GL10 gl10, int i, int i2) {
        if (this.bg != null) {
            this.bg.onSurfaceChanged(gl10, i, i2);
        }
        if (this.thumbNormal != null) {
            this.thumbNormal.onSurfaceChanged(gl10, i, i2);
        }
        if (this.thumbAbsolute != null) {
            this.thumbAbsolute.onSurfaceChanged(gl10, i, i2);
        }
        super.surfaceChanged(gl10, i, i2);
        updateActivatonRegion(i, i2);
        this.isInitialized = true;
    }
}
