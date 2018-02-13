package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ae.C0113a;
import java.util.ArrayList;
import java.util.HashMap;

public class ah implements SafeParcelable {
    public static final ai CREATOR = new ai();
    private final int ab;
    private final HashMap<String, HashMap<String, C0113a<?, ?>>> cD;
    private final ArrayList<C0114a> cE;
    private final String cF;

    public static class C0114a implements SafeParcelable {
        public static final aj CREATOR = new aj();
        final ArrayList<C0115b> cG;
        final String className;
        final int versionCode;

        C0114a(int i, String str, ArrayList<C0115b> arrayList) {
            this.versionCode = i;
            this.className = str;
            this.cG = arrayList;
        }

        C0114a(String str, HashMap<String, C0113a<?, ?>> hashMap) {
            this.versionCode = 1;
            this.className = str;
            this.cG = C0114a.m305a(hashMap);
        }

        private static ArrayList<C0115b> m305a(HashMap<String, C0113a<?, ?>> hashMap) {
            if (hashMap == null) {
                return null;
            }
            ArrayList<C0115b> arrayList = new ArrayList();
            for (String str : hashMap.keySet()) {
                arrayList.add(new C0115b(str, (C0113a) hashMap.get(str)));
            }
            return arrayList;
        }

        HashMap<String, C0113a<?, ?>> ak() {
            HashMap<String, C0113a<?, ?>> hashMap = new HashMap();
            int size = this.cG.size();
            for (int i = 0; i < size; i++) {
                C0115b c0115b = (C0115b) this.cG.get(i);
                hashMap.put(c0115b.cH, c0115b.cI);
            }
            return hashMap;
        }

        public int describeContents() {
            aj ajVar = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            aj ajVar = CREATOR;
            aj.m314a(this, parcel, i);
        }
    }

    public static class C0115b implements SafeParcelable {
        public static final ag CREATOR = new ag();
        final String cH;
        final C0113a<?, ?> cI;
        final int versionCode;

        C0115b(int i, String str, C0113a<?, ?> c0113a) {
            this.versionCode = i;
            this.cH = str;
            this.cI = c0113a;
        }

        C0115b(String str, C0113a<?, ?> c0113a) {
            this.versionCode = 1;
            this.cH = str;
            this.cI = c0113a;
        }

        public int describeContents() {
            ag agVar = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            ag agVar = CREATOR;
            ag.m302a(this, parcel, i);
        }
    }

    ah(int i, ArrayList<C0114a> arrayList, String str) {
        this.ab = i;
        this.cE = null;
        this.cD = m306b((ArrayList) arrayList);
        this.cF = (String) C0242s.m1208d(str);
        ag();
    }

    public ah(Class<? extends ae> cls) {
        this.ab = 1;
        this.cE = null;
        this.cD = new HashMap();
        this.cF = cls.getCanonicalName();
    }

    private static HashMap<String, HashMap<String, C0113a<?, ?>>> m306b(ArrayList<C0114a> arrayList) {
        HashMap<String, HashMap<String, C0113a<?, ?>>> hashMap = new HashMap();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            C0114a c0114a = (C0114a) arrayList.get(i);
            hashMap.put(c0114a.className, c0114a.ak());
        }
        return hashMap;
    }

    public void m307a(Class<? extends ae> cls, HashMap<String, C0113a<?, ?>> hashMap) {
        this.cD.put(cls.getCanonicalName(), hashMap);
    }

    public void ag() {
        for (String str : this.cD.keySet()) {
            HashMap hashMap = (HashMap) this.cD.get(str);
            for (String str2 : hashMap.keySet()) {
                ((C0113a) hashMap.get(str2)).m284a(this);
            }
        }
    }

    public void ah() {
        for (String str : this.cD.keySet()) {
            HashMap hashMap = (HashMap) this.cD.get(str);
            HashMap hashMap2 = new HashMap();
            for (String str2 : hashMap.keySet()) {
                hashMap2.put(str2, ((C0113a) hashMap.get(str2)).m280W());
            }
            this.cD.put(str, hashMap2);
        }
    }

    ArrayList<C0114a> ai() {
        ArrayList<C0114a> arrayList = new ArrayList();
        for (String str : this.cD.keySet()) {
            arrayList.add(new C0114a(str, (HashMap) this.cD.get(str)));
        }
        return arrayList;
    }

    public String aj() {
        return this.cF;
    }

    public boolean m308b(Class<? extends ae> cls) {
        return this.cD.containsKey(cls.getCanonicalName());
    }

    public int describeContents() {
        ai aiVar = CREATOR;
        return 0;
    }

    int m309i() {
        return this.ab;
    }

    public HashMap<String, C0113a<?, ?>> m310q(String str) {
        return (HashMap) this.cD.get(str);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : this.cD.keySet()) {
            stringBuilder.append(str).append(":\n");
            HashMap hashMap = (HashMap) this.cD.get(str);
            for (String str2 : hashMap.keySet()) {
                stringBuilder.append("  ").append(str2).append(": ");
                stringBuilder.append(hashMap.get(str2));
            }
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        ai aiVar = CREATOR;
        ai.m311a(this, parcel, i);
    }
}
