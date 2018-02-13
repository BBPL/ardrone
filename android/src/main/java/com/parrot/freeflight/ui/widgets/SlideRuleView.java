package com.parrot.freeflight.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.freeflight.C0984R;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.impl.client.cache.CacheConfig;

public class SlideRuleView extends View {
    private Rect bgStripeRect;
    private LruCache<Float, String> cache;
    private String currAltSpeed;
    private Drawable currValueBg;
    private PointF currValueTextPos;
    private Paint mBgPaint;
    private int mBgStripeColor;
    private int mDividerStrokeColor;
    private float mDividerStrokeSize;
    private float mOldValue;
    private float mStripeMarginLeft;
    private float mStripeMarginRight;
    private float mStripePadding;
    private int mTextColor;
    private float mTextHeight;
    private TextPaint mTextPaint;
    private float mTextSize;
    private Timer mTimer;
    private float mValue;
    private float mValuesInterval;
    private float mValuesStep;
    private TextPaint mValuesTextPaint;
    private Rect paddings;
    private Paint strokePaint;

    private class ScrollTimerTask extends TimerTask {
        private static final long DURATION_MILLIS = 1000;
        private float coefInterpolated;
        private Interpolator interpolator = new DecelerateInterpolator(2.0f);
        private float mValue1;
        private float mValue2;
        private Runnable runnable = new C12421();
        private long startTime = System.currentTimeMillis();

        class C12421 implements Runnable {
            C12421() {
            }

            public void run() {
                SlideRuleView.this.setValue(ScrollTimerTask.this.mValue1 + ((ScrollTimerTask.this.mValue2 - ScrollTimerTask.this.mValue1) * ScrollTimerTask.this.coefInterpolated), false);
            }
        }

        public ScrollTimerTask(float f, float f2) {
            this.mValue1 = f;
            this.mValue2 = f2;
        }

        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            long j = this.startTime + DURATION_MILLIS;
            if (currentTimeMillis <= j) {
                this.coefInterpolated = this.interpolator.getInterpolation(((float) (currentTimeMillis - this.startTime)) / ((float) (j - this.startTime)));
                SlideRuleView.this.post(this.runnable);
            } else if (SlideRuleView.this.mTimer != null) {
                SlideRuleView.this.mTimer.cancel();
                SlideRuleView.this.mTimer = null;
            }
        }
    }

    public SlideRuleView(Context context) {
        super(context);
        init(null, 0);
    }

    public SlideRuleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 0);
    }

    public SlideRuleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet, i);
    }

    private void drawValues(Canvas canvas) {
        Rect clipBounds = canvas.getClipBounds();
        int i = this.bgStripeRect.left;
        int width = this.bgStripeRect.width() / 2;
        int i2 = (int) ((((float) clipBounds.top) - (((float) clipBounds.top) % this.mValuesInterval)) - this.mValuesInterval);
        int i3 = clipBounds.bottom;
        int i4 = i2;
        while (i4 < i3) {
            float f = (this.mValuesStep * ((float) i4)) / this.mValuesInterval;
            if (f != 0.0f) {
                f *= GroundOverlayOptions.NO_DIMENSION;
            }
            Float valueOf = Float.valueOf(f);
            String str = (String) this.cache.get(valueOf);
            if (str == null) {
                str = String.format("%.1f", new Object[]{valueOf});
                this.cache.put(valueOf, str);
            }
            canvas.drawText(str, (float) (i + width), ((float) i4) + this.mTextHeight, this.mValuesTextPaint);
            f = (float) this.bgStripeRect.left;
            Canvas canvas2 = canvas;
            canvas2.drawLine(this.mStripePadding + f, ((float) i4) + ((this.mValuesInterval / 3.0f) * 1.0f), ((float) this.bgStripeRect.right) - this.mStripePadding, ((this.mValuesInterval / 3.0f) * 1.0f) + ((float) i4), this.strokePaint);
            f = (float) this.bgStripeRect.left;
            canvas2 = canvas;
            canvas2.drawLine(this.mStripePadding + f, ((float) i4) + ((this.mValuesInterval / 3.0f) * 2.0f), ((float) this.bgStripeRect.right) - this.mStripePadding, ((this.mValuesInterval / 3.0f) * 2.0f) + ((float) i4), this.strokePaint);
            i4 = (int) (((float) i4) + this.mValuesInterval);
        }
    }

    private void init(AttributeSet attributeSet, int i) {
        this.cache = new LruCache(1000);
        this.mValuesStep = CacheConfig.DEFAULT_HEURISTIC_COEFFICIENT;
        this.currAltSpeed = "0,0 m";
        this.currValueTextPos = new PointF();
        this.bgStripeRect = new Rect();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, C0984R.styleable.RateIndicator, i, 0);
        Resources resources = getResources();
        this.mBgStripeColor = obtainStyledAttributes.getColor(4, resources.getColor(C0984R.color.rate_indicator_scaleBgColor));
        this.mTextColor = obtainStyledAttributes.getColor(1, resources.getColor(C0984R.color.rate_indicator_textColor));
        this.mTextSize = obtainStyledAttributes.getDimension(2, resources.getDimension(C0984R.dimen.rate_indicator_textSize));
        this.mStripeMarginLeft = obtainStyledAttributes.getDimension(5, resources.getDimension(C0984R.dimen.rate_indicator_scaleMarginLeft));
        this.mStripeMarginRight = obtainStyledAttributes.getDimension(6, resources.getDimension(C0984R.dimen.rate_indicator_scaleMarginRight));
        this.mStripePadding = obtainStyledAttributes.getDimension(3, resources.getDimension(C0984R.dimen.rate_indicator_scalePadding));
        this.mValuesInterval = obtainStyledAttributes.getDimension(9, resources.getDimension(C0984R.dimen.rate_indicator_scaleValuesInterval));
        this.mDividerStrokeColor = obtainStyledAttributes.getColor(7, resources.getColor(C0984R.color.rate_indicator_scaleMarkColor));
        this.mDividerStrokeSize = obtainStyledAttributes.getDimension(8, resources.getDimension(C0984R.dimen.rate_indicator_scaleMarkStrokeWidth));
        if (obtainStyledAttributes.hasValue(10)) {
            this.currValueBg = obtainStyledAttributes.getDrawable(10);
            this.currValueBg.setCallback(this);
        }
        obtainStyledAttributes.recycle();
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setFlags(1);
        this.mTextPaint.setTextAlign(Align.LEFT);
        this.mValuesTextPaint = new TextPaint(this.mTextPaint);
        this.mValuesTextPaint.setTextAlign(Align.CENTER);
        this.mBgPaint = new Paint();
        this.mBgPaint.setColor(this.mBgStripeColor);
        this.strokePaint = new Paint();
        this.strokePaint.setColor(this.mDividerStrokeColor);
        this.strokePaint.setStrokeWidth(this.mDividerStrokeSize);
        this.paddings = new Rect(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mTextPaint.setColor(this.mTextColor);
        this.mValuesTextPaint.setColor(this.mTextColor);
        this.mValuesTextPaint.setTextSize(this.mTextSize);
        this.mTextHeight = this.mTextPaint.getFontMetrics().bottom;
    }

    private void recalculatePositions() {
        int height = getHeight();
        int width = getWidth();
        if (this.currValueBg != null) {
            int intrinsicHeight = ((int) (((float) (height - this.currValueBg.getIntrinsicHeight())) / 2.0f)) + getScrollY();
            this.currValueBg.setBounds(0, intrinsicHeight, this.currValueBg.getIntrinsicWidth() + 0, this.currValueBg.getIntrinsicHeight() + intrinsicHeight);
        }
        this.currValueTextPos.x = 10.0f;
        this.currValueTextPos.y = ((((float) height) / 2.0f) + 5.0f) + ((float) getScrollY());
        this.bgStripeRect.set((int) this.mStripeMarginLeft, getScrollY(), (int) (((((float) width) - this.mStripeMarginLeft) - this.mStripeMarginRight) + this.mStripeMarginLeft), height + getScrollY());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(this.bgStripeRect, this.mBgPaint);
        drawValues(canvas);
        if (this.currValueBg != null) {
            this.currValueBg.draw(canvas);
        }
        canvas.drawText(this.currAltSpeed, this.currValueTextPos.x, this.currValueTextPos.y, this.mTextPaint);
    }

    protected void onMeasure(int i, int i2) {
        if (this.currValueBg != null) {
            setMeasuredDimension(this.currValueBg.getIntrinsicWidth(), MeasureSpec.getSize(i2));
            return;
        }
        super.onMeasure(i, i2);
    }

    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        recalculatePositions();
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        recalculatePositions();
        scrollTo(0, ((-i2) / 2) - ((int) ((this.mValue / this.mValuesStep) * this.mValuesInterval)));
    }

    public void setValue(float f, boolean z) {
        this.mOldValue = this.mValue;
        this.mValue = f;
        if (z) {
            if (this.mTimer != null) {
                this.mTimer.cancel();
            }
            this.mTimer = new Timer();
            this.mTimer.scheduleAtFixedRate(new ScrollTimerTask(this.mOldValue, f), 0, 20);
        } else {
            Float valueOf = Float.valueOf(((float) Math.round(this.mValue * 10.0f)) / 10.0f);
            this.currAltSpeed = (String) this.cache.get(valueOf);
            if (this.currAltSpeed == null) {
                String format = String.format("%.1f", new Object[]{valueOf});
                this.cache.put(valueOf, format);
                this.currAltSpeed = format;
            }
            this.currAltSpeed += " m";
            scrollTo(0, ((-getHeight()) / 2) - ((int) ((this.mValue / this.mValuesStep) * this.mValuesInterval)));
        }
        invalidate();
    }
}
