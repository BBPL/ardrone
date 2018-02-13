package com.parrot.freeflight.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;

public class ParrotSeekBar extends SeekBar {
    private TextView lowerBoundView;
    private int textColor = -1;
    private TextView upperBoundView;

    public ParrotSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setThumbOffset(2);
        this.lowerBoundView = new TextView(context, attributeSet);
        this.lowerBoundView.setLayoutParams(new LayoutParams(-2, -1));
        this.lowerBoundView.setGravity(3);
        this.upperBoundView = new TextView(context, attributeSet);
        this.upperBoundView.setLayoutParams(new LayoutParams(-2, -1));
        this.upperBoundView.setGravity(5);
        initControl(context, attributeSet);
        updateTextColor();
    }

    public ParrotSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setThumbOffset(2);
        this.lowerBoundView = new TextView(context, attributeSet, i);
        this.lowerBoundView.setLayoutParams(new LayoutParams(-2, -1));
        this.lowerBoundView.setGravity(3);
        this.upperBoundView = new TextView(context, attributeSet, i);
        this.upperBoundView.setLayoutParams(new LayoutParams(-2, -1));
        this.upperBoundView.setGravity(5);
        initControl(context, attributeSet);
        updateTextColor();
    }

    private void initControl(Context context, AttributeSet attributeSet) {
        float dimension = context.getResources().getDimension(C0984R.dimen.settings_seek_text_padding_top);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0984R.styleable.ParrotSeekBar);
        float dimension2 = obtainStyledAttributes.getDimension(2, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(3, 0.0f);
        dimension = obtainStyledAttributes.getDimension(6, dimension);
        CharSequence string = obtainStyledAttributes.getString(4);
        this.lowerBoundView.setText(obtainStyledAttributes.getString(5));
        this.upperBoundView.setText(string);
        this.lowerBoundView.setPadding((int) dimension2, (int) dimension, 0, 0);
        this.upperBoundView.setPadding(0, (int) dimension, (int) dimension3, 0);
        obtainStyledAttributes.recycle();
    }

    private void updateTextColor() {
        float progress = ((float) getProgress()) / ((float) getMax());
        if (this.lowerBoundView != null) {
            if (((double) progress) < 0.05d) {
                this.lowerBoundView.setTextColor(-16777216);
            } else {
                this.lowerBoundView.setTextColor(this.textColor);
            }
        }
        if (this.upperBoundView == null) {
            return;
        }
        if (((double) progress) > 0.93d) {
            this.upperBoundView.setTextColor(-16777216);
        } else {
            this.upperBoundView.setTextColor(this.textColor);
        }
    }

    public void invalidate() {
        updateTextColor();
        super.invalidate();
    }

    protected void onDraw(Canvas canvas) {
        synchronized (this) {
            super.onDraw(canvas);
            this.lowerBoundView.draw(canvas);
            this.upperBoundView.draw(canvas);
        }
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.lowerBoundView.layout(i, i2, i3, i4);
        this.upperBoundView.layout(i, i2, i3, i4);
    }

    protected void onMeasure(int i, int i2) {
        synchronized (this) {
            this.lowerBoundView.measure(i, i2);
            this.upperBoundView.measure(i, i2);
            super.onMeasure(i, i2);
        }
    }

    public void setTypeface(Typeface typeface) {
        this.lowerBoundView.setTypeface(typeface);
        this.upperBoundView.setTypeface(typeface);
    }
}
