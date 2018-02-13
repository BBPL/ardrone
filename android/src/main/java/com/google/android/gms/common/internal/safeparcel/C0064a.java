package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class C0064a {

    public static class C0063a extends RuntimeException {
        public C0063a(String str, Parcel parcel) {
            super(str + " Parcel: pos=" + parcel.dataPosition() + " size=" + parcel.dataSize());
        }
    }

    public static int m98a(Parcel parcel, int i) {
        return (i & -65536) != -65536 ? (i >> 16) & 65535 : parcel.readInt();
    }

    public static <T extends Parcelable> T m99a(Parcel parcel, int i, Creator<T> creator) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        Parcelable parcelable = (Parcelable) creator.createFromParcel(parcel);
        parcel.setDataPosition(a + dataPosition);
        return parcelable;
    }

    private static void m100a(Parcel parcel, int i, int i2) {
        int a = C0064a.m98a(parcel, i);
        if (a != i2) {
            throw new C0063a("Expected size " + i2 + " got " + a + " (0x" + Integer.toHexString(a) + ")", parcel);
        }
    }

    public static void m101a(Parcel parcel, int i, List list, ClassLoader classLoader) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a != 0) {
            parcel.readList(list, classLoader);
            parcel.setDataPosition(a + dataPosition);
        }
    }

    public static int m102b(Parcel parcel) {
        return parcel.readInt();
    }

    public static void m103b(Parcel parcel, int i) {
        parcel.setDataPosition(C0064a.m98a(parcel, i) + parcel.dataPosition());
    }

    public static <T> T[] m104b(Parcel parcel, int i, Creator<T> creator) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        T[] createTypedArray = parcel.createTypedArray(creator);
        parcel.setDataPosition(a + dataPosition);
        return createTypedArray;
    }

    public static int m105c(Parcel parcel) {
        int b = C0064a.m102b(parcel);
        int a = C0064a.m98a(parcel, b);
        int dataPosition = parcel.dataPosition();
        if (C0064a.m117m(b) != 20293) {
            throw new C0063a("Expected object header. Got 0x" + Integer.toHexString(b), parcel);
        }
        b = dataPosition + a;
        if (b >= dataPosition && b <= parcel.dataSize()) {
            return b;
        }
        throw new C0063a("Size read is invalid start=" + dataPosition + " end=" + b, parcel);
    }

    public static <T> ArrayList<T> m106c(Parcel parcel, int i, Creator<T> creator) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        ArrayList<T> createTypedArrayList = parcel.createTypedArrayList(creator);
        parcel.setDataPosition(a + dataPosition);
        return createTypedArrayList;
    }

    public static boolean m107c(Parcel parcel, int i) {
        C0064a.m100a(parcel, i, 4);
        return parcel.readInt() != 0;
    }

    public static byte m108d(Parcel parcel, int i) {
        C0064a.m100a(parcel, i, 4);
        return (byte) parcel.readInt();
    }

    public static short m109e(Parcel parcel, int i) {
        C0064a.m100a(parcel, i, 4);
        return (short) parcel.readInt();
    }

    public static int m110f(Parcel parcel, int i) {
        C0064a.m100a(parcel, i, 4);
        return parcel.readInt();
    }

    public static long m111g(Parcel parcel, int i) {
        C0064a.m100a(parcel, i, 8);
        return parcel.readLong();
    }

    public static BigInteger m112h(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        byte[] createByteArray = parcel.createByteArray();
        parcel.setDataPosition(a + dataPosition);
        return new BigInteger(createByteArray);
    }

    public static float m113i(Parcel parcel, int i) {
        C0064a.m100a(parcel, i, 4);
        return parcel.readFloat();
    }

    public static double m114j(Parcel parcel, int i) {
        C0064a.m100a(parcel, i, 8);
        return parcel.readDouble();
    }

    public static BigDecimal m115k(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        byte[] createByteArray = parcel.createByteArray();
        int readInt = parcel.readInt();
        parcel.setDataPosition(a + dataPosition);
        return new BigDecimal(new BigInteger(createByteArray), readInt);
    }

    public static String m116l(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        String readString = parcel.readString();
        parcel.setDataPosition(a + dataPosition);
        return readString;
    }

    public static int m117m(int i) {
        return 65535 & i;
    }

    public static IBinder m118m(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        IBinder readStrongBinder = parcel.readStrongBinder();
        parcel.setDataPosition(a + dataPosition);
        return readStrongBinder;
    }

    public static Bundle m119n(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        Bundle readBundle = parcel.readBundle();
        parcel.setDataPosition(a + dataPosition);
        return readBundle;
    }

    public static byte[] m120o(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        byte[] createByteArray = parcel.createByteArray();
        parcel.setDataPosition(a + dataPosition);
        return createByteArray;
    }

    public static boolean[] m121p(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        boolean[] createBooleanArray = parcel.createBooleanArray();
        parcel.setDataPosition(a + dataPosition);
        return createBooleanArray;
    }

    public static int[] m122q(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        int[] createIntArray = parcel.createIntArray();
        parcel.setDataPosition(a + dataPosition);
        return createIntArray;
    }

    public static long[] m123r(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        long[] createLongArray = parcel.createLongArray();
        parcel.setDataPosition(a + dataPosition);
        return createLongArray;
    }

    public static BigInteger[] m124s(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        int readInt = parcel.readInt();
        BigInteger[] bigIntegerArr = new BigInteger[readInt];
        for (int i2 = 0; i2 < readInt; i2++) {
            bigIntegerArr[i2] = new BigInteger(parcel.createByteArray());
        }
        parcel.setDataPosition(dataPosition + a);
        return bigIntegerArr;
    }

    public static float[] m125t(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        float[] createFloatArray = parcel.createFloatArray();
        parcel.setDataPosition(a + dataPosition);
        return createFloatArray;
    }

    public static double[] m126u(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        double[] createDoubleArray = parcel.createDoubleArray();
        parcel.setDataPosition(a + dataPosition);
        return createDoubleArray;
    }

    public static BigDecimal[] m127v(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        int readInt = parcel.readInt();
        BigDecimal[] bigDecimalArr = new BigDecimal[readInt];
        for (int i2 = 0; i2 < readInt; i2++) {
            byte[] createByteArray = parcel.createByteArray();
            bigDecimalArr[i2] = new BigDecimal(new BigInteger(createByteArray), parcel.readInt());
        }
        parcel.setDataPosition(dataPosition + a);
        return bigDecimalArr;
    }

    public static String[] m128w(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        String[] createStringArray = parcel.createStringArray();
        parcel.setDataPosition(a + dataPosition);
        return createStringArray;
    }

    public static ArrayList<String> m129x(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        ArrayList<String> createStringArrayList = parcel.createStringArrayList();
        parcel.setDataPosition(a + dataPosition);
        return createStringArrayList;
    }

    public static Parcel m130y(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        obtain.appendFrom(parcel, dataPosition, a);
        parcel.setDataPosition(a + dataPosition);
        return obtain;
    }

    public static Parcel[] m131z(Parcel parcel, int i) {
        int a = C0064a.m98a(parcel, i);
        int dataPosition = parcel.dataPosition();
        if (a == 0) {
            return null;
        }
        int readInt = parcel.readInt();
        Parcel[] parcelArr = new Parcel[readInt];
        for (int i2 = 0; i2 < readInt; i2++) {
            int readInt2 = parcel.readInt();
            if (readInt2 != 0) {
                int dataPosition2 = parcel.dataPosition();
                Parcel obtain = Parcel.obtain();
                obtain.appendFrom(parcel, dataPosition2, readInt2);
                parcelArr[i2] = obtain;
                parcel.setDataPosition(readInt2 + dataPosition2);
            } else {
                parcelArr[i2] = null;
            }
        }
        parcel.setDataPosition(dataPosition + a);
        return parcelArr;
    }
}
