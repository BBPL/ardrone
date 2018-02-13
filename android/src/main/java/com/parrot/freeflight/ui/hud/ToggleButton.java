package com.parrot.freeflight.ui.hud;

import android.content.res.Resources;
import com.parrot.freeflight.ui.gl.GLSprite;
import com.parrot.freeflight.ui.gl.ResourceBitmapLoader;
import com.parrot.freeflight.ui.hud.Sprite.Align;
import javax.microedition.khronos.opengles.GL10;

public class ToggleButton extends Button {
    private int alphaCoef = -1;
    private boolean checked = false;
    private float lightAlpha = 1.0f;
    private long prevNano;
    private GLSprite spriteCheckedNormal;
    private GLSprite spriteCheckedPressed;
    private GLSprite spriteGlow;

    public ToggleButton(Resources resources, int i, int i2, int i3, int i4, int i5, Align align) {
        super(resources, i, i2, align);
        this.spriteGlow = new GLSprite(new ResourceBitmapLoader(resources, i5));
        this.spriteCheckedNormal = new GLSprite(new ResourceBitmapLoader(resources, i3));
        this.spriteCheckedPressed = new GLSprite(new ResourceBitmapLoader(resources, i4));
    }

    public void draw(GL10 gl10) {
        if (this.bounds == null) {
            return;
        }
        if (this.checked) {
            long nanoTime = System.nanoTime();
            if (nanoTime - this.prevNano > 100) {
                this.prevNano = nanoTime;
                this.lightAlpha = (float) (((double) this.lightAlpha) + (0.05d * ((double) this.alphaCoef)));
                if (this.lightAlpha < 0.0f) {
                    this.alphaCoef = 1;
                } else if (this.lightAlpha > 1.0f) {
                    this.alphaCoef = -1;
                }
                this.spriteGlow.setAlpha(this.lightAlpha);
            }
            if (this.isPressed) {
                this.spriteCheckedPressed.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.spritePressed.height));
            } else {
                this.spriteCheckedNormal.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.spritePressed.height));
            }
            this.spriteGlow.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.spriteGlow.height));
        } else if (this.isPressed) {
            this.spritePressed.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.spriteNormal.height));
        } else {
            this.spriteNormal.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.spriteNormal.height));
        }
    }

    public void init(GL10 gl10, int i) {
        super.init(gl10, i);
        this.spriteGlow.init(gl10, i);
        this.spriteCheckedNormal.init(gl10, i);
        this.spriteCheckedPressed.init(gl10, i);
    }

    public void setChecked(boolean z) {
        this.checked = z;
    }

    public void setViewAndProjectionMatrices(float[] fArr, float[] fArr2) {
        super.setViewAndProjectionMatrices(fArr, fArr2);
        this.spriteGlow.setViewAndProjectionMatrices(fArr, fArr2);
        this.spriteCheckedNormal.setViewAndProjectionMatrices(fArr, fArr2);
        this.spriteCheckedPressed.setViewAndProjectionMatrices(fArr, fArr2);
    }

    public void surfaceChanged(GL10 gl10, int i, int i2) {
        super.surfaceChanged(gl10, i, i2);
        this.spriteGlow.onSurfaceChanged(gl10, i, i2);
        this.spriteCheckedNormal.onSurfaceChanged(gl10, i, i2);
        this.spriteCheckedPressed.onSurfaceChanged(gl10, i, i2);
    }
}
