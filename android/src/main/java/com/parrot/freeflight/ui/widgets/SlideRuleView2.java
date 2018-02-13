package com.parrot.freeflight.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

public class SlideRuleView2 extends View {
    private static final int ANIMATION_DELAY = 16;
    private final AnimationData animData1 = new AnimationData();
    private final AnimationData animData2 = new AnimationData();
    private int animationTime = 1000;
    private int barBgColor = 1077952576;
    private final Paint barBgPaint = new Paint();
    private final Rect barMargins = new Rect();
    private SlideRuleValueChangeListener changeListener;
    private Drawable currValue1Bg;
    private Drawable currValue2Bg;
    private ElementDataProvider elementDataProvider;
    private int elementsCount = 12;
    private final GestureDetector gestureDetector;
    private final SimpleOnGestureListener gestureListener = new C12442();
    private final Handler handler = new Handler();
    private Interpolator interpolator;
    private int lineHeight = 3;
    private int linesColor = -16744449;
    private final Paint linesPaint = new Paint();
    private int stripePadding = 10;
    private float textCenterDelta;
    private int textColor = -1;
    private final Paint textPaint = new Paint();
    private int textSize = 25;
    private final Runnable timerRunnable = new C12431();
    private int viewHeight;

    public interface ElementDataProvider {
        float getAbsValue(float f);

        String getCurrElementData(float f);

        String getElementData(int i, int i2);
    }

    public interface SlideRuleValueChangeListener {
        void onSlideValueChanged(float f);
    }

    class C12431 implements Runnable {
        C12431() {
        }

        public void run() {
            SlideRuleView2.this.updateCurrValue();
        }
    }

    class C12442 extends SimpleOnGestureListener {
        C12442() {
        }

        public boolean onDown(MotionEvent motionEvent) {
            SlideRuleView2.this.setValue2(1.0f - (motionEvent.getY() / ((float) SlideRuleView2.this.getHeight())));
            return true;
        }
    }

    private static class AnimationData {
        private float currValue;
        private float deltaValue;
        private float prevValue;
        private long startTime;

        private AnimationData() {
        }
    }

    public SlideRuleView2(Context context) {
        super(context);
        this.gestureDetector = new GestureDetector(context, this.gestureListener);
        init();
    }

    public SlideRuleView2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.gestureDetector = new GestureDetector(context, this.gestureListener);
        init();
    }

    private void drawCurrent(Canvas canvas, float f, Drawable drawable) {
        int width = canvas.getWidth();
        int i = this.barMargins.left;
        int i2 = this.barMargins.right;
        int i3 = this.stripePadding;
        int round = Math.round(((float) (canvas.getHeight() - drawable.getIntrinsicHeight())) * f);
        int i4 = this.barMargins.left;
        int i5 = this.stripePadding;
        drawable.setBounds(0, round, canvas.getWidth(), drawable.getIntrinsicHeight() + round);
        drawable.draw(canvas);
        canvas.drawText(this.elementDataProvider.getCurrElementData(1.0f - f), (float) (((((width - i) - i2) - (i3 * 2)) / 2) + (i4 + i5)), ((float) ((drawable.getIntrinsicHeight() / 2) + round)) + this.textCenterDelta, this.textPaint);
    }

    private void drawValues(Canvas canvas) {
        int width = (canvas.getWidth() - this.barMargins.left) - this.barMargins.right;
        int height = (canvas.getHeight() - this.barMargins.top) - this.barMargins.bottom;
        int i = width - (this.stripePadding * 2);
        int i2 = (height - (this.stripePadding * 2)) / this.elementsCount;
        canvas.drawRect((float) this.barMargins.left, (float) this.barMargins.top, (float) (width + this.barMargins.left), (float) (this.barMargins.top + height), this.barBgPaint);
        int i3 = this.barMargins.left + this.stripePadding;
        int i4 = this.barMargins.top + this.stripePadding;
        for (int i5 = 0; i5 < this.elementsCount; i5++) {
            if (this.elementDataProvider != null) {
                canvas.drawText(this.elementDataProvider.getElementData(i5, this.elementsCount), (float) ((i / 2) + i3), ((float) ((i2 / 2) + i4)) + this.textCenterDelta, this.textPaint);
            }
            canvas.drawRect((float) i3, (float) i4, (float) (i3 + i), (float) (this.lineHeight + i4), this.linesPaint);
            canvas.drawRect((float) i3, (float) ((i4 + i2) - this.lineHeight), (float) (i3 + i), (float) (i4 + i2), this.linesPaint);
            i4 += i2;
        }
    }

    private void init() {
        this.barBgPaint.setColor(this.barBgColor);
        this.textPaint.setColor(this.textColor);
        this.textPaint.setTextSize((float) this.textSize);
        this.textPaint.setTextAlign(Align.CENTER);
        this.textPaint.setFlags(1);
        this.linesPaint.setColor(this.linesColor);
        this.linesPaint.setStyle(Style.FILL);
        this.textCenterDelta = Math.abs(this.textPaint.ascent()) - ((float) (Math.round(Math.abs(this.textPaint.ascent()) + this.textPaint.descent()) / 2));
    }

    private boolean updateAnimData(AnimationData animationData) {
        long currentTimeMillis = System.currentTimeMillis() - animationData.startTime;
        if (currentTimeMillis > ((long) this.animationTime)) {
            currentTimeMillis = (long) this.animationTime;
        }
        float f = ((float) currentTimeMillis) / ((float) this.animationTime);
        if (this.interpolator != null) {
            f = this.interpolator.getInterpolation(f);
        }
        animationData.currValue = (f * animationData.deltaValue) + animationData.prevValue;
        return currentTimeMillis == ((long) this.animationTime);
    }

    private void updateCurrValue() {
        boolean updateAnimData = updateAnimData(this.animData1);
        boolean updateAnimData2 = updateAnimData(this.animData2);
        invalidate();
        if (!updateAnimData || !updateAnimData2) {
            this.handler.removeCallbacks(this.timerRunnable);
            this.handler.postDelayed(this.timerRunnable, 16);
        }
    }

    public int getAnimationTime() {
        return this.animationTime;
    }

    public int getBarBgColor() {
        return this.barBgColor;
    }

    public Rect getBarMargins() {
        return this.barMargins;
    }

    public SlideRuleValueChangeListener getChangeListener() {
        return this.changeListener;
    }

    public Drawable getCurrValue1Bg() {
        return this.currValue1Bg;
    }

    public Drawable getCurrValue2Bg() {
        return this.currValue2Bg;
    }

    public ElementDataProvider getElementDataProvider() {
        return this.elementDataProvider;
    }

    public int getElementsCount() {
        return this.elementsCount;
    }

    public Interpolator getInterpolator() {
        return this.interpolator;
    }

    public int getLinesColor() {
        return this.linesColor;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public float getValue() {
        return this.animData2.currValue;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawValues(canvas);
        drawCurrent(canvas, 1.0f - this.animData1.currValue, this.currValue1Bg);
        drawCurrent(canvas, 1.0f - this.animData2.currValue, this.currValue2Bg);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    public void setAnimationTime(int i) {
        this.animationTime = i;
    }

    public void setBarBgColor(int i) {
        this.barBgColor = i;
        init();
        invalidate();
    }

    public void setBarMargins(int i, int i2, int i3, int i4) {
        this.barMargins.set(i, i2, i3, i4);
        init();
        invalidate();
    }

    public void setChangeListener(SlideRuleValueChangeListener slideRuleValueChangeListener) {
        this.changeListener = slideRuleValueChangeListener;
    }

    public void setCurrValue1Bg(Drawable drawable) {
        this.currValue1Bg = drawable;
        init();
        invalidate();
    }

    public void setCurrValue2Bg(Drawable drawable) {
        this.currValue2Bg = drawable;
    }

    public void setElementDataProvider(ElementDataProvider elementDataProvider) {
        this.elementDataProvider = elementDataProvider;
        invalidate();
    }

    public void setElementsCount(int i) {
        this.elementsCount = i;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setLinesColor(int i) {
        this.linesColor = i;
        init();
        invalidate();
    }

    public void setTextColor(int i) {
        this.textColor = i;
        init();
        invalidate();
    }

    public void setTextSize(int i) {
        this.textSize = i;
        init();
        invalidate();
    }

    public void setValue(float f) {
        this.animData1.prevValue = this.animData1.currValue;
        this.animData1.deltaValue = f - this.animData1.prevValue;
        this.animData1.startTime = System.currentTimeMillis();
        updateCurrValue();
    }

    public void setValue2(float f) {
        if (this.changeListener != null) {
            this.changeListener.onSlideValueChanged(f);
        }
        this.animData2.prevValue = this.animData2.currValue;
        this.animData2.deltaValue = f - this.animData2.prevValue;
        this.animData2.startTime = System.currentTimeMillis();
        updateCurrValue();
    }
}
