package com.parrot.freeflight.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.google.common.primitives.Ints;
import com.parrot.freeflight.C0984R;

public class ViewPagerIndicator extends View implements OnPageChangeListener {
    private int currentPage;
    private int dy;
    private float gap;
    private int pageCount;
    private Paint pageIndicatorCurrentPaint;
    private Paint pageIndicatorPaint;
    private float radius;
    private int startX;
    private OnPageChangeListener viewPagerListener;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0984R.attr.viewPagerIndicatorStyle);
    }

    public ViewPagerIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.currentPage = 1;
        this.pageCount = 5;
        Resources resources = context.getResources();
        this.radius = resources.getDimension(C0984R.dimen.default_circle_indicator_radius);
        this.gap = resources.getDimension(C0984R.dimen.default_circle_indicator_gap);
        int color = resources.getColor(C0984R.color.default_circle_indicator_active_color);
        int color2 = resources.getColor(C0984R.color.default_circle_indicator_color);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0984R.styleable.ViewPagerIndicator, i, C0984R.style.ViewPagerIndicatorStyle);
        this.radius = obtainStyledAttributes.getDimension(3, this.radius);
        this.gap = obtainStyledAttributes.getDimension(4, this.gap);
        color2 = obtainStyledAttributes.getColor(1, color2);
        color = obtainStyledAttributes.getColor(2, color);
        obtainStyledAttributes.recycle();
        this.pageIndicatorPaint = new Paint();
        this.pageIndicatorPaint.setAntiAlias(true);
        this.pageIndicatorPaint.setColor(color2);
        this.pageIndicatorPaint.setStyle(Style.FILL);
        this.pageIndicatorCurrentPaint = new Paint(this.pageIndicatorPaint);
        this.pageIndicatorCurrentPaint.setColor(color);
    }

    private int measureHeight(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (mode == Ints.MAX_POWER_OF_TWO) {
            return size;
        }
        int paddingTop = (getPaddingTop() + getPaddingBottom()) + ((int) (this.radius * 3.0f));
        return mode == Integer.MIN_VALUE ? Math.min(size, paddingTop) : paddingTop;
    }

    private int measureWidth(int i) {
        int mode = MeasureSpec.getMode(i);
        int size = MeasureSpec.getSize(i);
        if (mode == Ints.MAX_POWER_OF_TWO) {
            return size;
        }
        int paddingLeft = (getPaddingLeft() + getPaddingRight()) + ((int) (((((float) this.pageCount) * this.radius) * 3.0f) + (this.gap * ((float) this.pageCount))));
        return mode == Integer.MIN_VALUE ? Math.min(size, paddingLeft) : paddingLeft;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < this.pageCount; i++) {
            int paddingLeft = this.startX + ((int) (((this.radius + ((float) getPaddingLeft())) + ((((float) i) * this.radius) * 3.0f)) + (((float) i) * this.gap)));
            if (this.currentPage == i) {
                canvas.drawCircle((float) paddingLeft, (float) this.dy, this.radius, this.pageIndicatorCurrentPaint);
            } else {
                canvas.drawCircle((float) paddingLeft, (float) this.dy, this.radius, this.pageIndicatorPaint);
            }
        }
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.dy = (getHeight() / 2) + 1;
        this.startX = (getWidth() - ((int) ((((this.radius + ((float) getPaddingLeft())) + ((float) getPaddingRight())) + ((((float) this.pageCount) * this.radius) * 3.0f)) + (this.gap * ((float) (this.pageCount - 1)))))) / 2;
    }

    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(measureWidth(i), measureHeight(i2));
    }

    public void onPageScrollStateChanged(int i) {
        if (this.viewPagerListener != null) {
            this.viewPagerListener.onPageScrollStateChanged(i);
        }
    }

    public void onPageScrolled(int i, float f, int i2) {
        if (this.viewPagerListener != null) {
            this.viewPagerListener.onPageScrolled(i, f, i2);
        }
    }

    public void onPageSelected(int i) {
        this.currentPage = i;
        invalidate();
        if (this.viewPagerListener != null) {
            this.viewPagerListener.onPageSelected(i);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.viewPagerListener = onPageChangeListener;
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("View pager should be bind to adapter first.");
        }
        this.pageCount = viewPager.getAdapter().getCount();
        this.currentPage = viewPager.getCurrentItem();
        viewPager.setOnPageChangeListener(this);
        invalidate();
    }
}
