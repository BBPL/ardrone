package com.parrot.freeflight.track_3d_viewer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.parrot.freeflight.utils.SimpleDelegate;

public class LinearLayout2 extends LinearLayout {
    private SimpleDelegate layoutDelegate;

    public LinearLayout2(Context context) {
        super(context);
    }

    public LinearLayout2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SimpleDelegate getLayoutDelegate() {
        return this.layoutDelegate;
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.layoutDelegate != null) {
            this.layoutDelegate.call();
        }
    }

    public void setLayoutDelegate(SimpleDelegate simpleDelegate) {
        this.layoutDelegate = simpleDelegate;
    }
}
