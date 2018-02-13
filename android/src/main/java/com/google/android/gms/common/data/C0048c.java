package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.sony.rdis.receiver.ServiceComuncationProtocol;

public class C0048c<T extends SafeParcelable> extends DataBuffer<T> {
    private static final String[] f44X = new String[]{ServiceComuncationProtocol.DATA};
    private final Creator<T> f45Y;

    public C0048c(C0051d c0051d, Creator<T> creator) {
        super(c0051d);
        this.f45Y = creator;
    }

    public T m33d(int i) {
        byte[] e = this.S.m47e(ServiceComuncationProtocol.DATA, i, 0);
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(e, 0, e.length);
        obtain.setDataPosition(0);
        SafeParcelable safeParcelable = (SafeParcelable) this.f45Y.createFromParcel(obtain);
        obtain.recycle();
        return safeParcelable;
    }

    public /* synthetic */ Object get(int i) {
        return m33d(i);
    }
}
