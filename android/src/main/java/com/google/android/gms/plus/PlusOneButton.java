package com.google.android.gms.plus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.internal.C0245v;
import com.google.android.gms.internal.bu;
import org.mortbay.jetty.security.Constraint;

public final class PlusOneButton extends FrameLayout {
    public static final int ANNOTATION_BUBBLE = 1;
    public static final int ANNOTATION_INLINE = 2;
    public static final int ANNOTATION_NONE = 0;
    public static final int DEFAULT_ACTIVITY_REQUEST_CODE = -1;
    public static final int SIZE_MEDIUM = 1;
    public static final int SIZE_SMALL = 0;
    public static final int SIZE_STANDARD = 3;
    public static final int SIZE_TALL = 2;
    private int f127O;
    private View ic;
    private int id;
    private String ie;
    private int f128if;
    private OnPlusOneClickListener ig;

    public interface OnPlusOneClickListener {
        void onPlusOneClick(Intent intent);
    }

    protected class DefaultOnPlusOneClickListener implements OnClickListener, OnPlusOneClickListener {
        private final OnPlusOneClickListener ih;
        final /* synthetic */ PlusOneButton ii;

        public DefaultOnPlusOneClickListener(PlusOneButton plusOneButton, OnPlusOneClickListener onPlusOneClickListener) {
            this.ii = plusOneButton;
            this.ih = onPlusOneClickListener;
        }

        public void onClick(View view) {
            Intent intent = (Intent) this.ii.ic.getTag();
            if (this.ih != null) {
                this.ih.onPlusOneClick(intent);
            } else {
                onPlusOneClick(intent);
            }
        }

        public void onPlusOneClick(Intent intent) {
            Context context = this.ii.getContext();
            if ((context instanceof Activity) && intent != null) {
                ((Activity) context).startActivityForResult(intent, this.ii.f128if);
            }
        }
    }

    public PlusOneButton(Context context) {
        this(context, null);
    }

    public PlusOneButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f127O = getSize(context, attributeSet);
        this.id = getAnnotation(context, attributeSet);
        this.f128if = -1;
        m1399d(getContext());
        if (!isInEditMode()) {
        }
    }

    private void m1399d(Context context) {
        if (this.ic != null) {
            removeView(this.ic);
        }
        this.ic = bu.m975a(context, this.f127O, this.id, this.ie, this.f128if);
        setOnPlusOneClickListener(this.ig);
        addView(this.ic);
    }

    protected static int getAnnotation(Context context, AttributeSet attributeSet) {
        String a = C0245v.m1218a("http://schemas.android.com/apk/lib/com.google.android.gms.plus", "annotation", context, attributeSet, true, false, "PlusOneButton");
        return "INLINE".equalsIgnoreCase(a) ? 2 : !Constraint.NONE.equalsIgnoreCase(a) ? 1 : 0;
    }

    protected static int getSize(Context context, AttributeSet attributeSet) {
        String a = C0245v.m1218a("http://schemas.android.com/apk/lib/com.google.android.gms.plus", "size", context, attributeSet, true, false, "PlusOneButton");
        return "SMALL".equalsIgnoreCase(a) ? 0 : "MEDIUM".equalsIgnoreCase(a) ? 1 : "TALL".equalsIgnoreCase(a) ? 2 : 3;
    }

    public void initialize(String str, int i) {
        C0242s.m1203a(getContext() instanceof Activity, "To use this method, the PlusOneButton must be placed in an Activity. Use initialize(PlusClient, String, OnPlusOneClickListener).");
        this.ie = str;
        this.f128if = i;
        m1399d(getContext());
    }

    public void initialize(String str, OnPlusOneClickListener onPlusOneClickListener) {
        this.ie = str;
        this.f128if = 0;
        m1399d(getContext());
        setOnPlusOneClickListener(onPlusOneClickListener);
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.ic.layout(0, 0, i3 - i, i4 - i2);
    }

    protected void onMeasure(int i, int i2) {
        View view = this.ic;
        measureChild(view, i, i2);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public void setAnnotation(int i) {
        this.id = i;
        m1399d(getContext());
    }

    public void setOnPlusOneClickListener(OnPlusOneClickListener onPlusOneClickListener) {
        this.ig = onPlusOneClickListener;
        this.ic.setOnClickListener(new DefaultOnPlusOneClickListener(this, onPlusOneClickListener));
    }

    public void setSize(int i) {
        this.f127O = i;
        m1399d(getContext());
    }
}
