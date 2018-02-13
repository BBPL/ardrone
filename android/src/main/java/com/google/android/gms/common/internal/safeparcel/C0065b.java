package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class C0065b {
    private static int m132A(Parcel parcel, int i) {
        parcel.writeInt(-65536 | i);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    private static void m133B(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.setDataPosition(i - 4);
        parcel.writeInt(dataPosition - i);
        parcel.setDataPosition(dataPosition);
    }

    public static void m134C(Parcel parcel, int i) {
        C0065b.m133B(parcel, i);
    }

    public static void m135a(Parcel parcel, int i, byte b) {
        C0065b.m151b(parcel, i, 4);
        parcel.writeInt(b);
    }

    public static void m136a(Parcel parcel, int i, double d) {
        C0065b.m151b(parcel, i, 8);
        parcel.writeDouble(d);
    }

    public static void m137a(Parcel parcel, int i, float f) {
        C0065b.m151b(parcel, i, 4);
        parcel.writeFloat(f);
    }

    public static void m138a(Parcel parcel, int i, long j) {
        C0065b.m151b(parcel, i, 8);
        parcel.writeLong(j);
    }

    public static void m139a(Parcel parcel, int i, Bundle bundle, boolean z) {
        if (bundle != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.writeBundle(bundle);
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static void m140a(Parcel parcel, int i, IBinder iBinder, boolean z) {
        if (iBinder != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.writeStrongBinder(iBinder);
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static void m141a(Parcel parcel, int i, Parcel parcel2, boolean z) {
        if (parcel2 != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.appendFrom(parcel2, 0, parcel2.dataSize());
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static void m142a(Parcel parcel, int i, Parcelable parcelable, int i2, boolean z) {
        if (parcelable != null) {
            int A = C0065b.m132A(parcel, i);
            parcelable.writeToParcel(parcel, i2);
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static void m143a(Parcel parcel, int i, String str, boolean z) {
        if (str != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.writeString(str);
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static void m144a(Parcel parcel, int i, List<String> list, boolean z) {
        if (list != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.writeStringList(list);
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static void m145a(Parcel parcel, int i, short s) {
        C0065b.m151b(parcel, i, 4);
        parcel.writeInt(s);
    }

    public static void m146a(Parcel parcel, int i, boolean z) {
        C0065b.m151b(parcel, i, 4);
        parcel.writeInt(z ? 1 : 0);
    }

    public static void m147a(Parcel parcel, int i, byte[] bArr, boolean z) {
        if (bArr != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.writeByteArray(bArr);
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static <T extends Parcelable> void m148a(Parcel parcel, int i, T[] tArr, int i2, boolean z) {
        if (tArr != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.writeInt(r3);
            for (Parcelable parcelable : tArr) {
                if (parcelable == null) {
                    parcel.writeInt(0);
                } else {
                    C0065b.m150a(parcel, parcelable, i2);
                }
            }
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static void m149a(Parcel parcel, int i, String[] strArr, boolean z) {
        if (strArr != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.writeStringArray(strArr);
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    private static <T extends Parcelable> void m150a(Parcel parcel, T t, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(1);
        int dataPosition2 = parcel.dataPosition();
        t.writeToParcel(parcel, i);
        int dataPosition3 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition3 - dataPosition2);
        parcel.setDataPosition(dataPosition3);
    }

    private static void m151b(Parcel parcel, int i, int i2) {
        if (i2 >= 65535) {
            parcel.writeInt(-65536 | i);
            parcel.writeInt(i2);
            return;
        }
        parcel.writeInt((i2 << 16) | i);
    }

    public static <T extends Parcelable> void m152b(Parcel parcel, int i, List<T> list, boolean z) {
        if (list != null) {
            int A = C0065b.m132A(parcel, i);
            int size = list.size();
            parcel.writeInt(size);
            for (int i2 = 0; i2 < size; i2++) {
                Parcelable parcelable = (Parcelable) list.get(i2);
                if (parcelable == null) {
                    parcel.writeInt(0);
                } else {
                    C0065b.m150a(parcel, parcelable, 0);
                }
            }
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static void m153c(Parcel parcel, int i, int i2) {
        C0065b.m151b(parcel, i, 4);
        parcel.writeInt(i2);
    }

    public static void m154c(Parcel parcel, int i, List list, boolean z) {
        if (list != null) {
            int A = C0065b.m132A(parcel, i);
            parcel.writeList(list);
            C0065b.m133B(parcel, A);
        } else if (z) {
            C0065b.m151b(parcel, i, 0);
        }
    }

    public static int m155d(Parcel parcel) {
        return C0065b.m132A(parcel, 20293);
    }
}
