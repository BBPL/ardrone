package com.parrot.freeflight.academy.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CustomMapLayout extends FrameLayout {
    private boolean touched = false;

    public CustomMapLayout(Context context) {
        super(context);
        init();
    }

    public CustomMapLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CustomMapLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.touched = false;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.touched = true;
                break;
            case 1:
                this.touched = false;
                break;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean isTouched() {
        return this.touched;
    }
}
