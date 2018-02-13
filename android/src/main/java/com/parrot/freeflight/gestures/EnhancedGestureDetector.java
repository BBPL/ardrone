package com.parrot.freeflight.gestures;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class EnhancedGestureDetector extends GestureDetector {
    private static final int COORDINATE_DELTA = 50;
    private static final int DOUBLE_TAP_TIMESTAMP_DELTA = 200;
    private OnDoubleTapListener listener;
    private long timestampLast;
    private float xLast;
    private float yLast;

    public EnhancedGestureDetector(Context context, OnGestureListener onGestureListener) {
        super(context, onGestureListener);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 6) {
            long eventTime = motionEvent.getEventTime();
            if (motionEvent.getPointerCount() > 1) {
                if (eventTime - this.timestampLast < 200 && Math.abs(motionEvent.getX(1) - this.xLast) < 50.0f && Math.abs(motionEvent.getY(1) - this.yLast) < 50.0f && this.listener != null) {
                    return this.listener.onDoubleTap(motionEvent);
                }
                this.xLast = motionEvent.getX(1);
                this.yLast = motionEvent.getY(1);
                this.timestampLast = motionEvent.getEventTime();
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        super.setOnDoubleTapListener(onDoubleTapListener);
        this.listener = onDoubleTapListener;
    }
}
