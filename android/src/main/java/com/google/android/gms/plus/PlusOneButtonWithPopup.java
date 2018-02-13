package com.google.android.gms.plus;

import android.app.PendingIntent;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.internal.br;
import com.google.android.gms.internal.br.C0180a;
import com.google.android.gms.internal.bu;
import com.google.common.primitives.Ints;

public final class PlusOneButtonWithPopup extends ViewGroup {
    private int f129O;
    private String f130g;
    private View ic;
    private int id;
    private String ie;
    private OnClickListener ij;

    public PlusOneButtonWithPopup(Context context) {
        this(context, null);
    }

    public PlusOneButtonWithPopup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f129O = PlusOneButton.getSize(context, attributeSet);
        this.id = PlusOneButton.getAnnotation(context, attributeSet);
        this.ic = new PlusOneDummyView(context, this.f129O);
        addView(this.ic);
    }

    private void bv() {
        if (this.ic != null) {
            removeView(this.ic);
        }
        this.ic = bu.m976a(getContext(), this.f129O, this.id, this.ie, this.f130g);
        if (this.ij != null) {
            setOnClickListener(this.ij);
        }
        addView(this.ic);
    }

    private br bw() throws RemoteException {
        br aa = C0180a.aa((IBinder) this.ic.getTag());
        if (aa != null) {
            return aa;
        }
        if (Log.isLoggable("PlusOneButtonWithPopup", 5)) {
            Log.w("PlusOneButtonWithPopup", "Failed to get PlusOneDelegate");
        }
        throw new RemoteException();
    }

    private int m1400c(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        switch (mode) {
            case Integer.MIN_VALUE:
            case Ints.MAX_POWER_OF_TWO /*1073741824*/:
                return MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i) - i2, mode);
            default:
                return i;
        }
    }

    public void cancelClick() {
        if (this.ic != null) {
            try {
                bw().cancelClick();
            } catch (RemoteException e) {
            }
        }
    }

    public PendingIntent getResolution() {
        if (this.ic != null) {
            try {
                return bw().getResolution();
            } catch (RemoteException e) {
            }
        }
        return null;
    }

    public void initialize(String str, String str2) {
        C0242s.m1205b((Object) str, (Object) "Url must not be null");
        this.ie = str;
        this.f130g = str2;
        bv();
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.ic.layout(getPaddingLeft(), getPaddingTop(), (i3 - i) - getPaddingRight(), (i4 - i2) - getPaddingBottom());
    }

    protected void onMeasure(int i, int i2) {
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        this.ic.measure(m1400c(i, paddingLeft), m1400c(i2, paddingTop));
        setMeasuredDimension(paddingLeft + this.ic.getMeasuredWidth(), paddingTop + this.ic.getMeasuredHeight());
    }

    public void reinitialize() {
        if (this.ic != null) {
            try {
                bw().reinitialize();
            } catch (RemoteException e) {
            }
        }
    }

    public void setAnnotation(int i) {
        this.id = i;
        bv();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.ij = onClickListener;
        this.ic.setOnClickListener(onClickListener);
    }

    public void setSize(int i) {
        this.f129O = i;
        bv();
    }
}
