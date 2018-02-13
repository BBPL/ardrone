package com.parrot.freeflight.ui.hud;

import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import com.parrot.freeflight.ui.gl.GLSprite;
import com.parrot.freeflight.ui.gl.ResourceBitmapLoader;
import com.parrot.freeflight.ui.hud.Sprite.Align;
import javax.microedition.khronos.opengles.GL10;

public class Indicator extends Sprite {
    private GLSprite[] indicatorStates;
    private boolean initialized;
    private int value;

    public Indicator(Resources resources, int[] iArr, Align align) {
        super(align);
        this.indicatorStates = new GLSprite[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            this.indicatorStates[i] = new GLSprite(new ResourceBitmapLoader(resources, iArr[i]));
        }
    }

    public void draw(GL10 gl10) {
        GLSprite gLSprite = this.indicatorStates[this.value];
        gLSprite.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - gLSprite.height));
    }

    public void freeResources() {
        for (GLSprite freeResources : this.indicatorStates) {
            freeResources.freeResources();
        }
    }

    public int getHeight() {
        return this.indicatorStates[this.value].getHeight();
    }

    public int getWidth() {
        return this.indicatorStates[this.value].getWidth();
    }

    public void init(GL10 gl10, int i) {
        for (GLSprite init : this.indicatorStates) {
            init.init(gl10, i);
        }
        this.initialized = true;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public boolean onTouchEvent(View view, MotionEvent motionEvent) {
        return false;
    }

    public void setValue(int i) {
        if (i < 0 || i >= this.indicatorStates.length) {
            throw new IllegalArgumentException("Value " + i + " is out of bounds");
        }
        this.value = i;
    }

    public void setViewAndProjectionMatrices(float[] fArr, float[] fArr2) {
        for (GLSprite viewAndProjectionMatrices : this.indicatorStates) {
            viewAndProjectionMatrices.setViewAndProjectionMatrices(fArr, fArr2);
        }
    }
}
