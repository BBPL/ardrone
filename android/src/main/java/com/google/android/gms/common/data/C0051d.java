package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.C0242s;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class C0051d implements SafeParcelable {
    public static final C0052e CREATOR = new C0052e();
    private static final HashMap<CursorWindow, Throwable> f46Z = ((HashMap) null);
    private static final Object aa = new Object();
    private static final C0049a ai = new C0049a(new String[0], null) {
    };
    private final int ab;
    private final String[] ac;
    Bundle ad;
    private final CursorWindow[] ae;
    private final Bundle af;
    int[] ag;
    int ah;
    boolean mClosed;
    private final int f47p;

    public static class C0049a {
        private final String[] ac;
        private final ArrayList<HashMap<String, Object>> aj;
        private final String ak;
        private final HashMap<Object, Integer> al;
        private boolean am;
        private String an;

        private C0049a(String[] strArr, String str) {
            this.ac = (String[]) C0242s.m1208d(strArr);
            this.aj = new ArrayList();
            this.ak = str;
            this.al = new HashMap();
            this.am = false;
            this.an = null;
        }
    }

    C0051d(int i, String[] strArr, CursorWindow[] cursorWindowArr, int i2, Bundle bundle) {
        this.mClosed = false;
        this.ab = i;
        this.ac = strArr;
        this.ae = cursorWindowArr;
        this.f47p = i2;
        this.af = bundle;
    }

    private C0051d(C0049a c0049a, int i, Bundle bundle) {
        this(c0049a.ac, C0051d.m39a(c0049a), i, bundle);
    }

    public C0051d(String[] strArr, CursorWindow[] cursorWindowArr, int i, Bundle bundle) {
        this.mClosed = false;
        this.ab = 1;
        this.ac = (String[]) C0242s.m1208d(strArr);
        this.ae = (CursorWindow[]) C0242s.m1208d(cursorWindowArr);
        this.f47p = i;
        this.af = bundle;
        m50h();
    }

    public static C0051d m36a(int i, Bundle bundle) {
        return new C0051d(ai, i, bundle);
    }

    private static void m37a(CursorWindow cursorWindow) {
    }

    private void m38a(String str, int i) {
        if (this.ad == null || !this.ad.containsKey(str)) {
            throw new IllegalArgumentException("No such column: " + str);
        } else if (isClosed()) {
            throw new IllegalArgumentException("Buffer is closed.");
        } else if (i < 0 || i >= this.ah) {
            throw new CursorIndexOutOfBoundsException(i, this.ah);
        }
    }

    private static CursorWindow[] m39a(C0049a c0049a) {
        if (c0049a.ac.length == 0) {
            return new CursorWindow[0];
        }
        ArrayList c = c0049a.aj;
        int size = c.size();
        CursorWindow cursorWindow = new CursorWindow(false);
        cursorWindow.setNumColumns(c0049a.ac.length);
        int i = 0;
        while (i < size) {
            try {
                if (cursorWindow.allocRow()) {
                    Map map = (Map) c.get(i);
                    for (int i2 = 0; i2 < c0049a.ac.length; i2++) {
                        String str = c0049a.ac[i2];
                        Object obj = map.get(str);
                        if (obj == null) {
                            cursorWindow.putNull(i, i2);
                        } else if (obj instanceof String) {
                            cursorWindow.putString((String) obj, i, i2);
                        } else if (obj instanceof Long) {
                            cursorWindow.putLong(((Long) obj).longValue(), i, i2);
                        } else if (obj instanceof Integer) {
                            cursorWindow.putLong((long) ((Integer) obj).intValue(), i, i2);
                        } else if (obj instanceof Boolean) {
                            cursorWindow.putLong(((Boolean) obj).booleanValue() ? 1 : 0, i, i2);
                        } else if (obj instanceof byte[]) {
                            cursorWindow.putBlob((byte[]) obj, i, i2);
                        } else {
                            throw new IllegalArgumentException("Unsupported object for column " + str + ": " + obj);
                        }
                    }
                    i++;
                } else {
                    throw new RuntimeException("Cursor window out of memory");
                }
            } catch (RuntimeException e) {
                cursorWindow.close();
                throw e;
            }
        }
        return new CursorWindow[]{cursorWindow};
    }

    public static C0051d m40f(int i) {
        return C0051d.m36a(i, null);
    }

    public long m41a(String str, int i, int i2) {
        m38a(str, i);
        return this.ae[i2].getLong(i - this.ag[i2], this.ad.getInt(str));
    }

    public void m42a(String str, int i, int i2, CharArrayBuffer charArrayBuffer) {
        m38a(str, i);
        this.ae[i2].copyStringToBuffer(i - this.ag[i2], this.ad.getInt(str), charArrayBuffer);
    }

    public int m43b(String str, int i, int i2) {
        m38a(str, i);
        return this.ae[i2].getInt(i - this.ag[i2], this.ad.getInt(str));
    }

    public String m44c(String str, int i, int i2) {
        m38a(str, i);
        return this.ae[i2].getString(i - this.ag[i2], this.ad.getInt(str));
    }

    public void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (int i = 0; i < this.ae.length; i++) {
                    this.ae[i].close();
                    C0051d.m37a(this.ae[i]);
                }
            }
        }
    }

    public boolean m45d(String str, int i, int i2) {
        m38a(str, i);
        return Long.valueOf(this.ae[i2].getLong(i - this.ag[i2], this.ad.getInt(str))).longValue() == 1;
    }

    public int describeContents() {
        return 0;
    }

    public int m46e(int i) {
        int i2 = 0;
        boolean z = i >= 0 && i < this.ah;
        C0242s.m1202a(z);
        while (i2 < this.ag.length) {
            if (i < this.ag[i2]) {
                i2--;
                break;
            }
            i2++;
        }
        return i2 == this.ag.length ? i2 - 1 : i2;
    }

    public byte[] m47e(String str, int i, int i2) {
        m38a(str, i);
        return this.ae[i2].getBlob(i - this.ag[i2], this.ad.getInt(str));
    }

    public Uri m48f(String str, int i, int i2) {
        String c = m44c(str, i, i2);
        return c == null ? null : Uri.parse(c);
    }

    public boolean m49g(String str, int i, int i2) {
        m38a(str, i);
        return this.ae[i2].isNull(i - this.ag[i2], this.ad.getInt(str));
    }

    public int getCount() {
        return this.ah;
    }

    public int getStatusCode() {
        return this.f47p;
    }

    public void m50h() {
        int i;
        int i2 = 0;
        this.ad = new Bundle();
        for (i = 0; i < this.ac.length; i++) {
            this.ad.putInt(this.ac[i], i);
        }
        this.ag = new int[this.ae.length];
        for (i = 0; i < this.ae.length; i++) {
            this.ag[i] = i2;
            i2 += this.ae[i].getNumRows();
        }
        this.ah = i2;
    }

    int m51i() {
        return this.ab;
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    String[] m52j() {
        return this.ac;
    }

    CursorWindow[] m53k() {
        return this.ae;
    }

    public Bundle m54l() {
        return this.af;
    }

    public void writeToParcel(Parcel parcel, int i) {
        C0052e.m55a(this, parcel, i);
    }
}
