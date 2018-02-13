package com.google.android.youtube.player.internal;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.common.primitives.Ints;

public final class C0418n extends FrameLayout {
    private final ProgressBar f209a;
    private final TextView f210b;

    public C0418n(Context context) {
        super(context, null, C0444z.m1738c(context));
        C0417m c0417m = new C0417m(context);
        setBackgroundColor(-16777216);
        this.f209a = new ProgressBar(context);
        this.f209a.setVisibility(8);
        addView(this.f209a, new LayoutParams(-2, -2, 17));
        int i = (int) ((10.0f * context.getResources().getDisplayMetrics().density) + 0.5f);
        this.f210b = new TextView(context);
        this.f210b.setTextAppearance(context, 16973894);
        this.f210b.setTextColor(-1);
        this.f210b.setVisibility(8);
        this.f210b.setPadding(i, i, i, i);
        this.f210b.setGravity(17);
        this.f210b.setText(c0417m.f199a);
        addView(this.f210b, new LayoutParams(-2, -2, 17));
    }

    public final void m1640a() {
        this.f209a.setVisibility(8);
        this.f210b.setVisibility(8);
    }

    public final void m1641b() {
        this.f209a.setVisibility(0);
        this.f210b.setVisibility(8);
    }

    public final void m1642c() {
        this.f209a.setVisibility(8);
        this.f210b.setVisibility(0);
    }

    protected final void onMeasure(int i, int i2) {
        int i3 = 0;
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        if (mode == Ints.MAX_POWER_OF_TWO && mode2 == Ints.MAX_POWER_OF_TWO) {
            i3 = size;
        } else if (mode == Ints.MAX_POWER_OF_TWO || (mode == Integer.MIN_VALUE && mode2 == 0)) {
            size2 = (int) (((float) size) / 1.777f);
            i3 = size;
        } else if (mode2 == Ints.MAX_POWER_OF_TWO || (mode2 == Integer.MIN_VALUE && mode == 0)) {
            i3 = (int) (((float) size2) * 1.777f);
        } else if (mode != Integer.MIN_VALUE || mode2 != Integer.MIN_VALUE) {
            size2 = 0;
        } else if (((float) size2) < ((float) size) / 1.777f) {
            i3 = (int) (((float) size2) * 1.777f);
        } else {
            size2 = (int) (((float) size) / 1.777f);
            i3 = size;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(C0418n.resolveSize(i3, i), Ints.MAX_POWER_OF_TWO), MeasureSpec.makeMeasureSpec(C0418n.resolveSize(size2, i2), Ints.MAX_POWER_OF_TWO));
    }
}
