package com.google.android.gms.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.gms.dynamic.C0080e.C0079a;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.internal.C0243t;
import com.google.android.gms.internal.C0244u;

public final class SignInButton extends FrameLayout implements OnClickListener {
    public static final int COLOR_DARK = 0;
    public static final int COLOR_LIGHT = 1;
    public static final int SIZE_ICON_ONLY = 2;
    public static final int SIZE_STANDARD = 0;
    public static final int SIZE_WIDE = 1;
    private int f36O;
    private int f37P;
    private View f38Q;
    private OnClickListener f39R;

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SignInButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f39R = null;
        setStyle(0, 0);
    }

    private static Button m30c(Context context, int i, int i2) {
        Button c0244u = new C0244u(context);
        c0244u.m1217a(context.getResources(), i, i2);
        return c0244u;
    }

    private void m31d(Context context) {
        if (this.f38Q != null) {
            removeView(this.f38Q);
        }
        try {
            this.f38Q = C0243t.m1209d(context, this.f36O, this.f37P);
        } catch (C0079a e) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            this.f38Q = m30c(context, this.f36O, this.f37P);
        }
        addView(this.f38Q);
        this.f38Q.setEnabled(isEnabled());
        this.f38Q.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (this.f39R != null && view == this.f38Q) {
            this.f39R.onClick(this);
        }
    }

    public void setColorScheme(int i) {
        setStyle(this.f36O, i);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.f38Q.setEnabled(z);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.f39R = onClickListener;
        if (this.f38Q != null) {
            this.f38Q.setOnClickListener(this);
        }
    }

    public void setSize(int i) {
        setStyle(i, this.f37P);
    }

    public void setStyle(int i, int i2) {
        boolean z = true;
        boolean z2 = i >= 0 && i < 3;
        C0242s.m1203a(z2, "Unknown button size " + i);
        if (i2 < 0 || i2 >= 2) {
            z = false;
        }
        C0242s.m1203a(z, "Unknown color scheme " + i2);
        this.f36O = i;
        this.f37P = i2;
        m31d(getContext());
    }
}
