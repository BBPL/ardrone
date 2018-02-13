package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ae.C0112b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class ab implements SafeParcelable, C0112b<String, Integer> {
    public static final ac CREATOR = new ac();
    private final int ab;
    private final HashMap<String, Integer> co;
    private final HashMap<Integer, String> cp;
    private final ArrayList<C0111a> cq;

    public static final class C0111a implements SafeParcelable {
        public static final ad CREATOR = new ad();
        final String cr;
        final int cs;
        final int versionCode;

        C0111a(int i, String str, int i2) {
            this.versionCode = i;
            this.cr = str;
            this.cs = i2;
        }

        C0111a(String str, int i) {
            this.versionCode = 1;
            this.cr = str;
            this.cs = i;
        }

        public int describeContents() {
            ad adVar = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            ad adVar = CREATOR;
            ad.m266a(this, parcel, i);
        }
    }

    public ab() {
        this.ab = 1;
        this.co = new HashMap();
        this.cp = new HashMap();
        this.cq = null;
    }

    ab(int i, ArrayList<C0111a> arrayList) {
        this.ab = i;
        this.co = new HashMap();
        this.cp = new HashMap();
        this.cq = null;
        m255a((ArrayList) arrayList);
    }

    private void m255a(ArrayList<C0111a> arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0111a c0111a = (C0111a) it.next();
            m260b(c0111a.cr, c0111a.cs);
        }
    }

    ArrayList<C0111a> m256Q() {
        ArrayList<C0111a> arrayList = new ArrayList();
        for (String str : this.co.keySet()) {
            arrayList.add(new C0111a(str, ((Integer) this.co.get(str)).intValue()));
        }
        return arrayList;
    }

    public int mo474R() {
        return 7;
    }

    public int mo475S() {
        return 0;
    }

    public String m259a(Integer num) {
        String str = (String) this.cp.get(num);
        return (str == null && this.co.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    public ab m260b(String str, int i) {
        this.co.put(str, Integer.valueOf(i));
        this.cp.put(Integer.valueOf(i), str);
        return this;
    }

    public int describeContents() {
        ac acVar = CREATOR;
        return 0;
    }

    public /* synthetic */ Object mo476e(Object obj) {
        return m259a((Integer) obj);
    }

    int m262i() {
        return this.ab;
    }

    public void writeToParcel(Parcel parcel, int i) {
        ac acVar = CREATOR;
        ac.m263a(this, parcel, i);
    }
}
