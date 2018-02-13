package com.google.android.gms.internal;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;

public final class C0217f extends Drawable implements Callback {
    private boolean aP;
    private int aR;
    private long aS;
    private int aT;
    private int aU;
    private int aV;
    private int aW;
    private int aX;
    private boolean aY;
    private C0216b aZ;
    private Drawable ba;
    private Drawable bb;
    private boolean bc;
    private boolean bd;
    private boolean be;
    private int bf;

    private static final class C0215a extends Drawable {
        private static final C0215a bg = new C0215a();
        private static final C0214a bh = new C0214a();

        private static final class C0214a extends ConstantState {
            private C0214a() {
            }

            public int getChangingConfigurations() {
                return 0;
            }

            public Drawable newDrawable() {
                return C0215a.bg;
            }
        }

        private C0215a() {
        }

        public void draw(Canvas canvas) {
        }

        public ConstantState getConstantState() {
            return bh;
        }

        public int getOpacity() {
            return -2;
        }

        public void setAlpha(int i) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    static final class C0216b extends ConstantState {
        int bi;
        int bj;

        C0216b(C0216b c0216b) {
            if (c0216b != null) {
                this.bi = c0216b.bi;
                this.bj = c0216b.bj;
            }
        }

        public int getChangingConfigurations() {
            return this.bi;
        }

        public Drawable newDrawable() {
            return new C0217f(this);
        }
    }

    public C0217f(Drawable drawable, Drawable drawable2) {
        this(null);
        if (drawable == null) {
            drawable = C0215a.bg;
        }
        this.ba = drawable;
        drawable.setCallback(this);
        C0216b c0216b = this.aZ;
        c0216b.bj |= drawable.getChangingConfigurations();
        if (drawable2 == null) {
            drawable2 = C0215a.bg;
        }
        this.bb = drawable2;
        drawable2.setCallback(this);
        c0216b = this.aZ;
        c0216b.bj |= drawable2.getChangingConfigurations();
    }

    C0217f(C0216b c0216b) {
        this.aR = 0;
        this.aV = 255;
        this.aX = 0;
        this.aP = true;
        this.aZ = new C0216b(c0216b);
    }

    public boolean canConstantState() {
        if (!this.bc) {
            boolean z = (this.ba.getConstantState() == null || this.bb.getConstantState() == null) ? false : true;
            this.bd = z;
            this.bc = true;
        }
        return this.bd;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void draw(android.graphics.Canvas r8) {
        /*
        r7 = this;
        r1 = 1;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = 0;
        r2 = r7.aR;
        switch(r2) {
            case 1: goto L_0x0028;
            case 2: goto L_0x0032;
            default: goto L_0x0009;
        };
    L_0x0009:
        r0 = r1;
    L_0x000a:
        r1 = r7.aX;
        r2 = r7.aP;
        r3 = r7.ba;
        r4 = r7.bb;
        if (r0 == 0) goto L_0x0064;
    L_0x0014:
        if (r2 == 0) goto L_0x0018;
    L_0x0016:
        if (r1 != 0) goto L_0x001b;
    L_0x0018:
        r3.draw(r8);
    L_0x001b:
        r0 = r7.aV;
        if (r1 != r0) goto L_0x0027;
    L_0x001f:
        r0 = r7.aV;
        r4.setAlpha(r0);
        r4.draw(r8);
    L_0x0027:
        return;
    L_0x0028:
        r2 = android.os.SystemClock.uptimeMillis();
        r7.aS = r2;
        r1 = 2;
        r7.aR = r1;
        goto L_0x000a;
    L_0x0032:
        r2 = r7.aS;
        r4 = 0;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 < 0) goto L_0x0009;
    L_0x003a:
        r2 = android.os.SystemClock.uptimeMillis();
        r4 = r7.aS;
        r2 = r2 - r4;
        r2 = (float) r2;
        r3 = r7.aW;
        r3 = (float) r3;
        r2 = r2 / r3;
        r3 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r3 < 0) goto L_0x0062;
    L_0x004a:
        if (r1 == 0) goto L_0x004e;
    L_0x004c:
        r7.aR = r0;
    L_0x004e:
        r0 = java.lang.Math.min(r2, r6);
        r2 = r7.aT;
        r2 = (float) r2;
        r3 = r7.aU;
        r4 = r7.aT;
        r3 = r3 - r4;
        r3 = (float) r3;
        r0 = r0 * r3;
        r0 = r0 + r2;
        r0 = (int) r0;
        r7.aX = r0;
        r0 = r1;
        goto L_0x000a;
    L_0x0062:
        r1 = r0;
        goto L_0x004a;
    L_0x0064:
        if (r2 == 0) goto L_0x006c;
    L_0x0066:
        r0 = r7.aV;
        r0 = r0 - r1;
        r3.setAlpha(r0);
    L_0x006c:
        r3.draw(r8);
        if (r2 == 0) goto L_0x0076;
    L_0x0071:
        r0 = r7.aV;
        r3.setAlpha(r0);
    L_0x0076:
        if (r1 <= 0) goto L_0x0083;
    L_0x0078:
        r4.setAlpha(r1);
        r4.draw(r8);
        r0 = r7.aV;
        r4.setAlpha(r0);
    L_0x0083:
        r7.invalidateSelf();
        goto L_0x0027;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.f.draw(android.graphics.Canvas):void");
    }

    public int getChangingConfigurations() {
        return (super.getChangingConfigurations() | this.aZ.bi) | this.aZ.bj;
    }

    public ConstantState getConstantState() {
        if (!canConstantState()) {
            return null;
        }
        this.aZ.bi = getChangingConfigurations();
        return this.aZ;
    }

    public int getIntrinsicHeight() {
        return Math.max(this.ba.getIntrinsicHeight(), this.bb.getIntrinsicHeight());
    }

    public int getIntrinsicWidth() {
        return Math.max(this.ba.getIntrinsicWidth(), this.bb.getIntrinsicWidth());
    }

    public int getOpacity() {
        if (!this.be) {
            this.bf = Drawable.resolveOpacity(this.ba.getOpacity(), this.bb.getOpacity());
            this.be = true;
        }
        return this.bf;
    }

    public void invalidateDrawable(Drawable drawable) {
        if (as.an()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.invalidateDrawable(this);
            }
        }
    }

    public Drawable mutate() {
        if (!this.aY && super.mutate() == this) {
            if (canConstantState()) {
                this.ba.mutate();
                this.bb.mutate();
                this.aY = true;
            } else {
                throw new IllegalStateException("One or more children of this LayerDrawable does not have constant state; this drawable cannot be mutated.");
            }
        }
        return this;
    }

    protected void onBoundsChange(Rect rect) {
        this.ba.setBounds(rect);
        this.bb.setBounds(rect);
    }

    public Drawable m1128r() {
        return this.bb;
    }

    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        if (as.an()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.scheduleDrawable(this, runnable, j);
            }
        }
    }

    public void setAlpha(int i) {
        if (this.aX == this.aV) {
            this.aX = i;
        }
        this.aV = i;
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.ba.setColorFilter(colorFilter);
        this.bb.setColorFilter(colorFilter);
    }

    public void startTransition(int i) {
        this.aT = 0;
        this.aU = this.aV;
        this.aX = 0;
        this.aW = i;
        this.aR = 1;
        invalidateSelf();
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        if (as.an()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.unscheduleDrawable(this, runnable);
            }
        }
    }
}
