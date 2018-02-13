package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import com.google.android.gms.dynamic.C0078c;
import com.google.android.gms.dynamic.C0080e;
import com.google.android.gms.dynamic.C0080e.C0079a;
import com.google.android.gms.internal.C0236q.C0238a;

public final class C0243t extends C0080e<C0236q> {
    private static final C0243t ca = new C0243t();

    private C0243t() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }

    public static View m1209d(Context context, int i, int i2) throws C0079a {
        return ca.m1210e(context, i, i2);
    }

    private View m1210e(Context context, int i, int i2) throws C0079a {
        try {
            return (View) C0078c.m172a(((C0236q) m174h(context)).mo972a(C0078c.m173f(context), i, i2));
        } catch (Throwable e) {
            throw new C0079a("Could not get button with size " + i + " and color " + i2, e);
        }
    }

    public C0236q m1211j(IBinder iBinder) {
        return C0238a.m1198i(iBinder);
    }

    public /* synthetic */ Object mo973k(IBinder iBinder) {
        return m1211j(iBinder);
    }
}
