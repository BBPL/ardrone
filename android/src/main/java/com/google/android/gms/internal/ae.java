package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ae {

    public interface C0112b<I, O> {
        int mo474R();

        int mo475S();

        I mo476e(O o);
    }

    public static class C0113a<I, O> implements SafeParcelable {
        public static final af CREATOR = new af();
        private final int ab;
        protected final String cA;
        private ah cB;
        private C0112b<I, O> cC;
        protected final int ct;
        protected final boolean cu;
        protected final int cv;
        protected final boolean cw;
        protected final String cx;
        protected final int cy;
        protected final Class<? extends ae> cz;

        C0113a(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, C0248z c0248z) {
            this.ab = i;
            this.ct = i2;
            this.cu = z;
            this.cv = i3;
            this.cw = z2;
            this.cx = str;
            this.cy = i4;
            if (str2 == null) {
                this.cz = null;
                this.cA = null;
            } else {
                this.cz = ak.class;
                this.cA = str2;
            }
            if (c0248z == null) {
                this.cC = null;
            } else {
                this.cC = c0248z.m1231P();
            }
        }

        protected C0113a(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends ae> cls, C0112b<I, O> c0112b) {
            this.ab = 1;
            this.ct = i;
            this.cu = z;
            this.cv = i2;
            this.cw = z2;
            this.cx = str;
            this.cy = i3;
            this.cz = cls;
            if (cls == null) {
                this.cA = null;
            } else {
                this.cA = cls.getCanonicalName();
            }
            this.cC = c0112b;
        }

        public static C0113a m269a(String str, int i, C0112b<?, ?> c0112b, boolean z) {
            return new C0113a(c0112b.mo474R(), z, c0112b.mo475S(), false, str, i, null, c0112b);
        }

        public static <T extends ae> C0113a<T, T> m270a(String str, int i, Class<T> cls) {
            return new C0113a(11, false, 11, false, str, i, cls, null);
        }

        public static <T extends ae> C0113a<ArrayList<T>, ArrayList<T>> m271b(String str, int i, Class<T> cls) {
            return new C0113a(11, true, 11, true, str, i, cls, null);
        }

        public static C0113a<Integer, Integer> m272c(String str, int i) {
            return new C0113a(0, false, 0, false, str, i, null, null);
        }

        public static C0113a<Double, Double> m274d(String str, int i) {
            return new C0113a(4, false, 4, false, str, i, null, null);
        }

        public static C0113a<Boolean, Boolean> m275e(String str, int i) {
            return new C0113a(6, false, 6, false, str, i, null, null);
        }

        public static C0113a<String, String> m276f(String str, int i) {
            return new C0113a(7, false, 7, false, str, i, null, null);
        }

        public static C0113a<ArrayList<String>, ArrayList<String>> m277g(String str, int i) {
            return new C0113a(7, true, 7, true, str, i, null, null);
        }

        public int m278R() {
            return this.ct;
        }

        public int m279S() {
            return this.cv;
        }

        public C0113a<I, O> m280W() {
            return new C0113a(this.ab, this.ct, this.cu, this.cv, this.cw, this.cx, this.cy, this.cA, ae());
        }

        public boolean m281X() {
            return this.cu;
        }

        public boolean m282Y() {
            return this.cw;
        }

        public String m283Z() {
            return this.cx;
        }

        public void m284a(ah ahVar) {
            this.cB = ahVar;
        }

        public int aa() {
            return this.cy;
        }

        public Class<? extends ae> ab() {
            return this.cz;
        }

        String ac() {
            return this.cA == null ? null : this.cA;
        }

        public boolean ad() {
            return this.cC != null;
        }

        C0248z ae() {
            return this.cC == null ? null : C0248z.m1229a(this.cC);
        }

        public HashMap<String, C0113a<?, ?>> af() {
            C0242s.m1208d(this.cA);
            C0242s.m1208d(this.cB);
            return this.cB.m310q(this.cA);
        }

        public int describeContents() {
            af afVar = CREATOR;
            return 0;
        }

        public I m285e(O o) {
            return this.cC.mo476e(o);
        }

        public int m286i() {
            return this.ab;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field\n");
            stringBuilder.append("            versionCode=").append(this.ab).append('\n');
            stringBuilder.append("                 typeIn=").append(this.ct).append('\n');
            stringBuilder.append("            typeInArray=").append(this.cu).append('\n');
            stringBuilder.append("                typeOut=").append(this.cv).append('\n');
            stringBuilder.append("           typeOutArray=").append(this.cw).append('\n');
            stringBuilder.append("        outputFieldName=").append(this.cx).append('\n');
            stringBuilder.append("      safeParcelFieldId=").append(this.cy).append('\n');
            stringBuilder.append("       concreteTypeName=").append(ac()).append('\n');
            if (ab() != null) {
                stringBuilder.append("     concreteType.class=").append(ab().getCanonicalName()).append('\n');
            }
            stringBuilder.append("          converterName=").append(this.cC == null ? "null" : this.cC.getClass().getCanonicalName()).append('\n');
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            af afVar = CREATOR;
            af.m299a(this, parcel, i);
        }
    }

    private void m287a(StringBuilder stringBuilder, C0113a c0113a, Object obj) {
        if (c0113a.m278R() == 11) {
            stringBuilder.append(((ae) c0113a.ab().cast(obj)).toString());
        } else if (c0113a.m278R() == 7) {
            stringBuilder.append("\"");
            stringBuilder.append(aq.m347r((String) obj));
            stringBuilder.append("\"");
        } else {
            stringBuilder.append(obj);
        }
    }

    private void m288a(StringBuilder stringBuilder, C0113a c0113a, ArrayList<Object> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                m287a(stringBuilder, c0113a, obj);
            }
        }
        stringBuilder.append("]");
    }

    public abstract HashMap<String, C0113a<?, ?>> mo477T();

    public HashMap<String, Object> m290U() {
        return null;
    }

    public HashMap<String, Object> m291V() {
        return null;
    }

    protected <O, I> I m292a(C0113a<I, O> c0113a, Object obj) {
        return c0113a.cC != null ? c0113a.m285e(obj) : obj;
    }

    protected boolean mo717a(C0113a c0113a) {
        return c0113a.m279S() == 11 ? c0113a.m282Y() ? m298p(c0113a.m283Z()) : m297o(c0113a.m283Z()) : mo479n(c0113a.m283Z());
    }

    protected Object mo718b(C0113a c0113a) {
        boolean z = false;
        String Z = c0113a.m283Z();
        if (c0113a.ab() == null) {
            return mo478m(c0113a.m283Z());
        }
        if (mo478m(c0113a.m283Z()) == null) {
            z = true;
        }
        C0242s.m1203a(z, "Concrete field shouldn't be value object: " + c0113a.m283Z());
        Map V = c0113a.m282Y() ? m291V() : m290U();
        if (V != null) {
            return V.get(Z);
        }
        try {
            return getClass().getMethod("get" + Character.toUpperCase(Z.charAt(0)) + Z.substring(1), new Class[0]).invoke(this, new Object[0]);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Object mo478m(String str);

    protected abstract boolean mo479n(String str);

    protected boolean m297o(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected boolean m298p(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    public String toString() {
        HashMap T = mo477T();
        StringBuilder stringBuilder = new StringBuilder(100);
        for (String str : T.keySet()) {
            C0113a c0113a = (C0113a) T.get(str);
            if (mo717a(c0113a)) {
                Object a = m292a(c0113a, mo718b(c0113a));
                if (stringBuilder.length() == 0) {
                    stringBuilder.append("{");
                } else {
                    stringBuilder.append(",");
                }
                stringBuilder.append("\"").append(str).append("\":");
                if (a != null) {
                    switch (c0113a.m279S()) {
                        case 8:
                            stringBuilder.append("\"").append(an.m343a((byte[]) a)).append("\"");
                            break;
                        case 9:
                            stringBuilder.append("\"").append(an.m344b((byte[]) a)).append("\"");
                            break;
                        case 10:
                            ar.m348a(stringBuilder, (HashMap) a);
                            break;
                        default:
                            if (!c0113a.m281X()) {
                                m287a(stringBuilder, c0113a, a);
                                break;
                            }
                            m288a(stringBuilder, c0113a, (ArrayList) a);
                            break;
                    }
                }
                stringBuilder.append("null");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append("}");
        } else {
            stringBuilder.append("{}");
        }
        return stringBuilder.toString();
    }
}
