package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class C0241r {

    public static final class C0240a {
        private final List<String> bY;
        private final Object bZ;

        private C0240a(Object obj) {
            this.bZ = C0242s.m1208d(obj);
            this.bY = new ArrayList();
        }

        public C0240a m1199a(String str, Object obj) {
            this.bY.add(((String) C0242s.m1208d(str)) + "=" + String.valueOf(obj));
            return this;
        }

        public String toString() {
            StringBuilder append = new StringBuilder(100).append(this.bZ.getClass().getSimpleName()).append('{');
            int size = this.bY.size();
            for (int i = 0; i < size; i++) {
                append.append((String) this.bY.get(i));
                if (i < size - 1) {
                    append.append(", ");
                }
            }
            return append.append('}').toString();
        }
    }

    public static boolean m1200a(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static C0240a m1201c(Object obj) {
        return new C0240a(obj);
    }

    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }
}
