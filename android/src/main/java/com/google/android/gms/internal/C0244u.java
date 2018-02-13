package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import com.google.android.gms.C0041R;

public final class C0244u extends Button {
    public C0244u(Context context) {
        this(context, null);
    }

    public C0244u(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 16842824);
    }

    private int m1213a(int i, int i2, int i3) {
        switch (i) {
            case 0:
                return i2;
            case 1:
                return i3;
            default:
                throw new IllegalStateException("Unknown color scheme: " + i);
        }
    }

    private void m1214b(Resources resources, int i, int i2) {
        int a;
        switch (i) {
            case 0:
            case 1:
                a = m1213a(i2, C0041R.drawable.common_signin_btn_text_dark, C0041R.drawable.common_signin_btn_text_light);
                break;
            case 2:
                a = m1213a(i2, C0041R.drawable.common_signin_btn_icon_dark, C0041R.drawable.common_signin_btn_icon_light);
                break;
            default:
                throw new IllegalStateException("Unknown button size: " + i);
        }
        if (a == -1) {
            throw new IllegalStateException("Could not find background resource!");
        }
        setBackgroundDrawable(resources.getDrawable(a));
    }

    private void m1215c(Resources resources) {
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(14.0f);
        float f = resources.getDisplayMetrics().density;
        setMinHeight((int) ((f * 48.0f) + 0.5f));
        setMinWidth((int) ((f * 48.0f) + 0.5f));
    }

    private void m1216c(Resources resources, int i, int i2) {
        setTextColor(resources.getColorStateList(m1213a(i2, C0041R.color.common_signin_btn_text_dark, C0041R.color.common_signin_btn_text_light)));
        switch (i) {
            case 0:
                setText(resources.getString(C0041R.string.common_signin_button_text));
                return;
            case 1:
                setText(resources.getString(C0041R.string.common_signin_button_text_long));
                return;
            case 2:
                setText(null);
                return;
            default:
                throw new IllegalStateException("Unknown button size: " + i);
        }
    }

    public void m1217a(Resources resources, int i, int i2) {
        boolean z = true;
        boolean z2 = i >= 0 && i < 3;
        C0242s.m1203a(z2, "Unknown button size " + i);
        if (i2 < 0 || i2 >= 2) {
            z = false;
        }
        C0242s.m1203a(z, "Unknown color scheme " + i2);
        m1215c(resources);
        m1214b(resources, i, i2);
        m1216c(resources, i, i2);
    }
}
