package com.parrot.freeflight.ui.hud;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.parrot.freeflight.ui.gl.ChangeContrastBrightnessBitmapLoader;
import com.parrot.freeflight.ui.gl.GLSprite;
import com.parrot.freeflight.ui.gl.IBitmapLoader;
import com.parrot.freeflight.ui.gl.ResourceBitmapLoader;
import com.parrot.freeflight.ui.hud.Sprite.Align;
import com.parrot.freeflight.utils.OnPressListener;
import javax.microedition.khronos.opengles.GL10;

public class Button extends Sprite {
    private static final float PRESSED_BTN_BRIGHTNESS = 30.0f;
    private static final float PRESSED_BTN_CONTRAST = 1.0f;
    private final int INITIAL_REPEAT_INTERVAL;
    private final int SUBSEQUENT_REPEAT_INTERVAL;
    private OnClickListener clickListener;
    private final Runnable clickRepeatRunnable;
    private long fadingDuration;
    private boolean fadingEnabled;
    private long fadingStartTimeMillis;
    private boolean isInitialized;
    protected boolean isPressed;
    private int pointerId;
    private OnPressListener pressListener;
    private View pressedView;
    private int prevNormalResId;
    private int prevPressedResId;
    protected GLSprite spriteNormal;
    protected GLSprite spritePressed;

    class C12381 implements Runnable {
        C12381() {
        }

        public void run() {
            Button.this.pressedView.postDelayed(this, 20);
            if (Button.this.pressListener != null) {
                Button.this.pressListener.onRepeated(Button.this.pressedView);
            }
        }
    }

    public Button(Resources resources, int i, int i2, Align align) {
        this(resources, new ResourceBitmapLoader(resources, i), new ResourceBitmapLoader(resources, i2), align);
    }

    public Button(Resources resources, int i, Align align) {
        this(resources, new ResourceBitmapLoader(resources, i), new ChangeContrastBrightnessBitmapLoader(resources, i, PRESSED_BTN_CONTRAST, 30.0f), align);
    }

    public Button(Resources resources, IBitmapLoader iBitmapLoader, IBitmapLoader iBitmapLoader2, Align align) {
        super(align);
        this.INITIAL_REPEAT_INTERVAL = 200;
        this.SUBSEQUENT_REPEAT_INTERVAL = 20;
        this.fadingEnabled = false;
        this.fadingDuration = 0;
        this.fadingStartTimeMillis = 0;
        this.prevNormalResId = -1;
        this.prevPressedResId = -1;
        this.clickRepeatRunnable = new C12381();
        this.spriteNormal = new GLSprite(iBitmapLoader);
        this.spritePressed = new GLSprite(iBitmapLoader2);
        this.pointerId = -1;
        this.isInitialized = false;
    }

    public void draw(GL10 gl10) {
        long currentTimeMillis = this.fadingEnabled ? System.currentTimeMillis() : 0;
        if (this.fadingEnabled) {
            if (this.fadingStartTimeMillis == 0) {
                this.fadingStartTimeMillis = currentTimeMillis;
            } else if (this.fadingDuration != 0) {
                long j = currentTimeMillis - this.fadingStartTimeMillis;
                float f = ((float) (j % this.fadingDuration)) / ((float) (this.fadingDuration - 1));
                setAlpha(((((j / this.fadingDuration) % 2) > 0 ? 1 : (((j / this.fadingDuration) % 2) == 0 ? 0 : -1)) == 0 ? 1 : null) != null ? PRESSED_BTN_CONTRAST - f : f);
            }
        }
        if (this.bounds != null && this.visible) {
            if (this.isPressed) {
                this.spritePressed.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.spritePressed.height));
            } else {
                this.spriteNormal.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.spriteNormal.height));
            }
        }
    }

    public void freeResources() {
        this.spriteNormal.freeResources();
        this.spritePressed.freeResources();
    }

    public int getHeight() {
        return this.spriteNormal.getHeight();
    }

    public int getWidth() {
        return this.spriteNormal.getWidth();
    }

    public void init(GL10 gl10, int i) {
        this.spriteNormal.init(gl10, i);
        this.spritePressed.init(gl10, i);
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    protected void onAlphaChanged(float f) {
        this.spriteNormal.alpha = f;
        this.spritePressed.alpha = f;
    }

    public boolean onTouchEvent(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int i = action >> 8;
        switch (action & 255) {
            case 0:
            case 5:
                if (this.pointerId != -1) {
                    return false;
                }
                action = (int) motionEvent.getX(i);
                int y = (int) motionEvent.getY(i);
                if (this.bounds == null || !this.bounds.contains(action, y)) {
                    return false;
                }
                if (!this.enabled || !this.visible) {
                    return !this.enabled && this.visible;
                } else {
                    this.pointerId = motionEvent.getPointerId(i);
                    this.isPressed = true;
                    if (this.pressListener != null) {
                        this.pressListener.onClick(view);
                        view.removeCallbacks(this.clickRepeatRunnable);
                        view.postDelayed(this.clickRepeatRunnable, 200);
                        this.pressedView = view;
                    }
                    return true;
                }
            case 1:
            case 6:
                if (this.pointerId == -1 || this.pointerId != motionEvent.getPointerId(i)) {
                    return false;
                }
                if (this.bounds != null && this.bounds.contains((int) motionEvent.getX(i), (int) motionEvent.getY(i)) && this.clickListener != null && this.enabled && this.visible) {
                    this.clickListener.onClick(null);
                }
                this.isPressed = false;
                this.pointerId = -1;
                if (this.pressListener == null) {
                    return false;
                }
                this.pressListener.onRelease(view);
                view.removeCallbacks(this.clickRepeatRunnable);
                this.pressedView = null;
                return false;
            case 2:
                if (this.pointerId == -1) {
                    return false;
                }
                action = motionEvent.getPointerCount();
                for (int i2 = 0; i2 < action; i2++) {
                    if (this.pointerId == motionEvent.getPointerId(i2)) {
                        if (!this.bounds.contains((int) motionEvent.getX(i2), (int) motionEvent.getY(i2))) {
                            this.isPressed = false;
                            this.pointerId = -1;
                            if (this.pressListener != null) {
                                this.pressListener.onRelease(view);
                                view.removeCallbacks(this.clickRepeatRunnable);
                                this.pressedView = null;
                            }
                        }
                    }
                }
                return false;
            default:
                return false;
        }
    }

    public void setFadingDuration(long j) {
        this.fadingDuration = j;
    }

    public void setFadingEnabled(boolean z) {
        if (z != this.fadingEnabled) {
            if (z) {
                this.fadingStartTimeMillis = 0;
            } else {
                setAlpha(PRESSED_BTN_CONTRAST);
            }
            this.fadingEnabled = z;
        }
    }

    public void setImages(Resources resources, int i, int i2) {
        if (i != this.prevNormalResId) {
            Bitmap decodeResource = BitmapFactory.decodeResource(resources, i);
            this.spriteNormal.updateTexture(decodeResource);
            decodeResource.recycle();
            this.prevNormalResId = i;
        }
        if (this.prevPressedResId != i2) {
            decodeResource = BitmapFactory.decodeResource(resources, i2);
            this.spriteNormal.updateTexture(decodeResource);
            decodeResource.recycle();
            this.prevPressedResId = i2;
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    public void setOnClickRepeatListener(OnPressListener onPressListener) {
        this.pressListener = onPressListener;
    }

    public void setViewAndProjectionMatrices(float[] fArr, float[] fArr2) {
        this.spriteNormal.setViewAndProjectionMatrices(fArr, fArr2);
        this.spritePressed.setViewAndProjectionMatrices(fArr, fArr2);
    }

    public void surfaceChanged(GL10 gl10, int i, int i2) {
        this.spriteNormal.onSurfaceChanged(gl10, i, i2);
        this.spritePressed.onSurfaceChanged(gl10, i, i2);
        super.surfaceChanged(gl10, i, i2);
        this.isInitialized = true;
    }
}
