package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ae.C0113a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class ak extends ae implements SafeParcelable {
    public static final al CREATOR = new al();
    private final int ab;
    private final ah cB;
    private final Parcel cJ;
    private final int cK;
    private int cL;
    private int cM;
    private final String mClassName;

    ak(int i, Parcel parcel, ah ahVar) {
        this.ab = i;
        this.cJ = (Parcel) C0242s.m1208d(parcel);
        this.cK = 2;
        this.cB = ahVar;
        if (this.cB == null) {
            this.mClassName = null;
        } else {
            this.mClassName = this.cB.aj();
        }
        this.cL = 2;
    }

    private ak(SafeParcelable safeParcelable, ah ahVar, String str) {
        this.ab = 1;
        this.cJ = Parcel.obtain();
        safeParcelable.writeToParcel(this.cJ, 0);
        this.cK = 1;
        this.cB = (ah) C0242s.m1208d(ahVar);
        this.mClassName = (String) C0242s.m1208d(str);
        this.cL = 2;
    }

    public static <T extends ae & SafeParcelable> ak m317a(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        return new ak((SafeParcelable) t, m324b((ae) t), canonicalName);
    }

    public static HashMap<String, String> m318a(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    private static void m319a(ah ahVar, ae aeVar) {
        Class cls = aeVar.getClass();
        if (!ahVar.m308b(cls)) {
            HashMap T = aeVar.mo477T();
            ahVar.m307a(cls, aeVar.mo477T());
            for (String str : T.keySet()) {
                C0113a c0113a = (C0113a) T.get(str);
                Class ab = c0113a.ab();
                if (ab != null) {
                    try {
                        m319a(ahVar, (ae) ab.newInstance());
                    } catch (Throwable e) {
                        throw new IllegalStateException("Could not instantiate an object of type " + c0113a.ab().getCanonicalName(), e);
                    } catch (Throwable e2) {
                        throw new IllegalStateException("Could not access object of type " + c0113a.ab().getCanonicalName(), e2);
                    }
                }
            }
        }
    }

    private void m320a(StringBuilder stringBuilder, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                stringBuilder.append(obj);
                return;
            case 7:
                stringBuilder.append("\"").append(aq.m347r(obj.toString())).append("\"");
                return;
            case 8:
                stringBuilder.append("\"").append(an.m343a((byte[]) obj)).append("\"");
                return;
            case 9:
                stringBuilder.append("\"").append(an.m344b((byte[]) obj));
                stringBuilder.append("\"");
                return;
            case 10:
                ar.m348a(stringBuilder, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i);
        }
    }

    private void m321a(StringBuilder stringBuilder, C0113a<?, ?> c0113a, Parcel parcel, int i) {
        switch (c0113a.m279S()) {
            case 0:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, Integer.valueOf(C0064a.m110f(parcel, i))));
                return;
            case 1:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, C0064a.m112h(parcel, i)));
                return;
            case 2:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, Long.valueOf(C0064a.m111g(parcel, i))));
                return;
            case 3:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, Float.valueOf(C0064a.m113i(parcel, i))));
                return;
            case 4:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, Double.valueOf(C0064a.m114j(parcel, i))));
                return;
            case 5:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, C0064a.m115k(parcel, i)));
                return;
            case 6:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, Boolean.valueOf(C0064a.m107c(parcel, i))));
                return;
            case 7:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, C0064a.m116l(parcel, i)));
                return;
            case 8:
            case 9:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, C0064a.m120o(parcel, i)));
                return;
            case 10:
                m327b(stringBuilder, (C0113a) c0113a, m292a(c0113a, m318a(C0064a.m119n(parcel, i))));
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown field out type = " + c0113a.m279S());
        }
    }

    private void m322a(StringBuilder stringBuilder, String str, C0113a<?, ?> c0113a, Parcel parcel, int i) {
        stringBuilder.append("\"").append(str).append("\":");
        if (c0113a.ad()) {
            m321a(stringBuilder, c0113a, parcel, i);
        } else {
            m326b(stringBuilder, c0113a, parcel, i);
        }
    }

    private void m323a(StringBuilder stringBuilder, HashMap<String, C0113a<?, ?>> hashMap, Parcel parcel) {
        HashMap b = m325b((HashMap) hashMap);
        stringBuilder.append('{');
        int c = C0064a.m105c(parcel);
        Object obj = null;
        while (parcel.dataPosition() < c) {
            int b2 = C0064a.m102b(parcel);
            Entry entry = (Entry) b.get(Integer.valueOf(C0064a.m117m(b2)));
            if (entry != null) {
                if (obj != null) {
                    stringBuilder.append(",");
                }
                m322a(stringBuilder, (String) entry.getKey(), (C0113a) entry.getValue(), parcel, b2);
                obj = 1;
            }
        }
        if (parcel.dataPosition() != c) {
            throw new C0063a("Overread allowed size end=" + c, parcel);
        }
        stringBuilder.append('}');
    }

    private static ah m324b(ae aeVar) {
        ah ahVar = new ah(aeVar.getClass());
        m319a(ahVar, aeVar);
        ahVar.ah();
        ahVar.ag();
        return ahVar;
    }

    private static HashMap<Integer, Entry<String, C0113a<?, ?>>> m325b(HashMap<String, C0113a<?, ?>> hashMap) {
        HashMap<Integer, Entry<String, C0113a<?, ?>>> hashMap2 = new HashMap();
        for (Entry entry : hashMap.entrySet()) {
            hashMap2.put(Integer.valueOf(((C0113a) entry.getValue()).aa()), entry);
        }
        return hashMap2;
    }

    private void m326b(StringBuilder stringBuilder, C0113a<?, ?> c0113a, Parcel parcel, int i) {
        if (c0113a.m282Y()) {
            stringBuilder.append("[");
            switch (c0113a.m279S()) {
                case 0:
                    am.m338a(stringBuilder, C0064a.m122q(parcel, i));
                    break;
                case 1:
                    am.m340a(stringBuilder, C0064a.m124s(parcel, i));
                    break;
                case 2:
                    am.m339a(stringBuilder, C0064a.m123r(parcel, i));
                    break;
                case 3:
                    am.m337a(stringBuilder, C0064a.m125t(parcel, i));
                    break;
                case 4:
                    am.m336a(stringBuilder, C0064a.m126u(parcel, i));
                    break;
                case 5:
                    am.m340a(stringBuilder, C0064a.m127v(parcel, i));
                    break;
                case 6:
                    am.m342a(stringBuilder, C0064a.m121p(parcel, i));
                    break;
                case 7:
                    am.m341a(stringBuilder, C0064a.m128w(parcel, i));
                    break;
                case 8:
                case 9:
                case 10:
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                case 11:
                    Parcel[] z = C0064a.m131z(parcel, i);
                    int length = z.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        if (i2 > 0) {
                            stringBuilder.append(",");
                        }
                        z[i2].setDataPosition(0);
                        m323a(stringBuilder, c0113a.af(), z[i2]);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown field type out.");
            }
            stringBuilder.append("]");
            return;
        }
        switch (c0113a.m279S()) {
            case 0:
                stringBuilder.append(C0064a.m110f(parcel, i));
                return;
            case 1:
                stringBuilder.append(C0064a.m112h(parcel, i));
                return;
            case 2:
                stringBuilder.append(C0064a.m111g(parcel, i));
                return;
            case 3:
                stringBuilder.append(C0064a.m113i(parcel, i));
                return;
            case 4:
                stringBuilder.append(C0064a.m114j(parcel, i));
                return;
            case 5:
                stringBuilder.append(C0064a.m115k(parcel, i));
                return;
            case 6:
                stringBuilder.append(C0064a.m107c(parcel, i));
                return;
            case 7:
                stringBuilder.append("\"").append(aq.m347r(C0064a.m116l(parcel, i))).append("\"");
                return;
            case 8:
                stringBuilder.append("\"").append(an.m343a(C0064a.m120o(parcel, i))).append("\"");
                return;
            case 9:
                stringBuilder.append("\"").append(an.m344b(C0064a.m120o(parcel, i)));
                stringBuilder.append("\"");
                return;
            case 10:
                Bundle n = C0064a.m119n(parcel, i);
                Set<String> keySet = n.keySet();
                keySet.size();
                stringBuilder.append("{");
                int i3 = 1;
                for (String str : keySet) {
                    if (i3 == 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("\"").append(str).append("\"");
                    stringBuilder.append(":");
                    stringBuilder.append("\"").append(aq.m347r(n.getString(str))).append("\"");
                    i3 = 0;
                }
                stringBuilder.append("}");
                return;
            case 11:
                Parcel y = C0064a.m130y(parcel, i);
                y.setDataPosition(0);
                m323a(stringBuilder, c0113a.af(), y);
                return;
            default:
                throw new IllegalStateException("Unknown field type out");
        }
    }

    private void m327b(StringBuilder stringBuilder, C0113a<?, ?> c0113a, Object obj) {
        if (c0113a.m281X()) {
            m328b(stringBuilder, (C0113a) c0113a, (ArrayList) obj);
        } else {
            m320a(stringBuilder, c0113a.m278R(), obj);
        }
    }

    private void m328b(StringBuilder stringBuilder, C0113a<?, ?> c0113a, ArrayList<?> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            m320a(stringBuilder, c0113a.m278R(), arrayList.get(i));
        }
        stringBuilder.append("]");
    }

    public HashMap<String, C0113a<?, ?>> mo477T() {
        return this.cB == null ? null : this.cB.m310q(this.mClassName);
    }

    public Parcel al() {
        switch (this.cL) {
            case 0:
                this.cM = C0065b.m155d(this.cJ);
                C0065b.m134C(this.cJ, this.cM);
                this.cL = 2;
                break;
            case 1:
                C0065b.m134C(this.cJ, this.cM);
                this.cL = 2;
                break;
        }
        return this.cJ;
    }

    ah am() {
        switch (this.cK) {
            case 0:
                return null;
            case 1:
                return this.cB;
            case 2:
                return this.cB;
            default:
                throw new IllegalStateException("Invalid creation type: " + this.cK);
        }
    }

    public int describeContents() {
        al alVar = CREATOR;
        return 0;
    }

    public int m330i() {
        return this.ab;
    }

    protected Object mo478m(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    protected boolean mo479n(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public String toString() {
        C0242s.m1205b(this.cB, (Object) "Cannot convert to JSON on client side.");
        Parcel al = al();
        al.setDataPosition(0);
        StringBuilder stringBuilder = new StringBuilder(100);
        m323a(stringBuilder, this.cB.m310q(this.mClassName), al);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        al alVar = CREATOR;
        al.m333a(this, parcel, i);
    }
}
