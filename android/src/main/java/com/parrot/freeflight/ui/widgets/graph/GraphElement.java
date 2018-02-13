package com.parrot.freeflight.ui.widgets.graph;

import android.graphics.Canvas;

public abstract class GraphElement {
    protected int height;
    protected boolean visible = true;
    protected int width;

    public abstract void draw(Canvas canvas);

    public boolean isVisible() {
        return this.visible;
    }

    public abstract void onLayout(boolean z, int i, int i2, int i3, int i4);

    public abstract void onMeasure(int i, int i2);

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        setSize(i, i2);
    }

    public void setSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }
}
