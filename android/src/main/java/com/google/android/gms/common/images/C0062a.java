package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;
import com.google.android.gms.internal.C0217f;
import com.google.android.gms.internal.C0218g;
import com.google.android.gms.internal.C0219h;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.as;
import java.lang.ref.WeakReference;

public final class C0062a {
    final C0061a aG;
    private int aH;
    private int aI;
    int aJ;
    private int aK;
    private WeakReference<OnImageLoadedListener> aL;
    private WeakReference<ImageView> aM;
    private WeakReference<TextView> aN;
    private int aO;
    private boolean aP;
    private boolean aQ;

    public static final class C0061a {
        public final Uri uri;

        public C0061a(Uri uri) {
            this.uri = uri;
        }

        public boolean equals(Object obj) {
            return !(obj instanceof C0061a) ? false : this == obj || ((C0061a) obj).hashCode() == hashCode();
        }

        public int hashCode() {
            return C0241r.hashCode(this.uri);
        }
    }

    public C0062a(int i) {
        this.aH = 0;
        this.aI = 0;
        this.aO = -1;
        this.aP = true;
        this.aQ = false;
        this.aG = new C0061a(null);
        this.aI = i;
    }

    public C0062a(Uri uri) {
        this.aH = 0;
        this.aI = 0;
        this.aO = -1;
        this.aP = true;
        this.aQ = false;
        this.aG = new C0061a(uri);
        this.aI = 0;
    }

    private C0217f m87a(Drawable drawable, Drawable drawable2) {
        if (drawable == null) {
            drawable = null;
        } else if (drawable instanceof C0217f) {
            drawable = ((C0217f) drawable).m1128r();
        }
        return new C0217f(drawable, drawable2);
    }

    private void m88a(Drawable drawable, boolean z, boolean z2, boolean z3) {
        switch (this.aJ) {
            case 1:
                if (!z2) {
                    OnImageLoadedListener onImageLoadedListener = (OnImageLoadedListener) this.aL.get();
                    if (onImageLoadedListener != null) {
                        onImageLoadedListener.onImageLoaded(this.aG.uri, drawable);
                        return;
                    }
                    return;
                }
                return;
            case 2:
                ImageView imageView = (ImageView) this.aM.get();
                if (imageView != null) {
                    m89a(imageView, drawable, z, z2, z3);
                    return;
                }
                return;
            case 3:
                TextView textView = (TextView) this.aN.get();
                if (textView != null) {
                    m90a(textView, this.aO, drawable, z, z2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void m89a(ImageView imageView, Drawable drawable, boolean z, boolean z2, boolean z3) {
        Object obj = (z2 || z3) ? null : 1;
        if (obj != null && (imageView instanceof C0218g)) {
            int t = ((C0218g) imageView).m1131t();
            if (this.aI != 0 && t == this.aI) {
                return;
            }
        }
        boolean a = m91a(z, z2);
        Drawable a2 = a ? m87a(imageView.getDrawable(), drawable) : drawable;
        imageView.setImageDrawable(a2);
        if (imageView instanceof C0218g) {
            C0218g c0218g = (C0218g) imageView;
            c0218g.m1129a(z3 ? this.aG.uri : null);
            c0218g.m1130k(obj != null ? this.aI : 0);
        }
        if (a) {
            ((C0217f) a2).startTransition(250);
        }
    }

    private void m90a(TextView textView, int i, Drawable drawable, boolean z, boolean z2) {
        boolean a = m91a(z, z2);
        Drawable[] compoundDrawablesRelative = as.as() ? textView.getCompoundDrawablesRelative() : textView.getCompoundDrawables();
        Drawable a2 = a ? m87a(compoundDrawablesRelative[i], drawable) : drawable;
        Drawable drawable2 = i == 0 ? a2 : compoundDrawablesRelative[0];
        Drawable drawable3 = i == 1 ? a2 : compoundDrawablesRelative[1];
        Drawable drawable4 = i == 2 ? a2 : compoundDrawablesRelative[2];
        Drawable drawable5 = i == 3 ? a2 : compoundDrawablesRelative[3];
        if (as.as()) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        }
        if (a) {
            ((C0217f) a2).startTransition(250);
        }
    }

    private boolean m91a(boolean z, boolean z2) {
        return this.aP && !z2 && (!z || this.aQ);
    }

    void m92a(Context context, Bitmap bitmap, boolean z) {
        C0219h.m1134b(bitmap);
        m88a(new BitmapDrawable(context.getResources(), bitmap), z, false, true);
    }

    public void m93a(ImageView imageView) {
        C0219h.m1134b(imageView);
        this.aL = null;
        this.aM = new WeakReference(imageView);
        this.aN = null;
        this.aO = -1;
        this.aJ = 2;
        this.aK = imageView.hashCode();
    }

    public void m94a(OnImageLoadedListener onImageLoadedListener) {
        C0219h.m1134b(onImageLoadedListener);
        this.aL = new WeakReference(onImageLoadedListener);
        this.aM = null;
        this.aN = null;
        this.aO = -1;
        this.aJ = 1;
        this.aK = C0241r.hashCode(onImageLoadedListener, this.aG);
    }

    void m95b(Context context, boolean z) {
        Drawable drawable = null;
        if (this.aI != 0) {
            drawable = context.getResources().getDrawable(this.aI);
        }
        m88a(drawable, z, false, false);
    }

    public boolean equals(Object obj) {
        return !(obj instanceof C0062a) ? false : this == obj || ((C0062a) obj).hashCode() == hashCode();
    }

    void m96f(Context context) {
        Drawable drawable = null;
        if (this.aH != 0) {
            drawable = context.getResources().getDrawable(this.aH);
        }
        m88a(drawable, false, true, false);
    }

    public int hashCode() {
        return this.aK;
    }

    public void m97j(int i) {
        this.aI = i;
    }
}
