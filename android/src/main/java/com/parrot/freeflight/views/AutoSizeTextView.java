package com.parrot.freeflight.views;

import android.content.Context;
import android.graphics.Paint.FontMetrics;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoSizeTextView extends TextView {
    public AutoSizeTextView(Context context) {
        super(context);
    }

    public AutoSizeTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void setFontSize2(int i) {
        TextPaint paint = getPaint();
        if (paint != null) {
            setTextSize(0, 100.0f);
            FontMetrics fontMetrics = paint.getFontMetrics();
            setTextSize(0, ((float) i) - ((float) Math.ceil((double) (Math.abs(fontMetrics.bottom / fontMetrics.top) * ((float) i)))));
        }
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setFontSize2(i2);
    }
}
