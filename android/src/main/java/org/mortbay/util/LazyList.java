package org.mortbay.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LazyList implements Cloneable, Serializable {
    private static final String[] __EMTPY_STRING_ARRAY = new String[0];

    private LazyList() {
    }

    public static Object add(Object obj, int i, Object obj2) {
        if (obj == null) {
            if (i <= 0 && !(obj2 instanceof List) && obj2 != null) {
                return obj2;
            }
            List arrayList = new ArrayList();
            arrayList.add(i, obj2);
            return arrayList;
        } else if (obj instanceof List) {
            ((List) obj).add(i, obj2);
            return obj;
        } else {
            List arrayList2 = new ArrayList();
            arrayList2.add(obj);
            arrayList2.add(i, obj2);
            return arrayList2;
        }
    }

    public static Object add(Object obj, Object obj2) {
        if (obj == null) {
            if (!(obj2 instanceof List) && obj2 != null) {
                return obj2;
            }
            List arrayList = new ArrayList();
            arrayList.add(obj2);
            return arrayList;
        } else if (obj instanceof List) {
            ((List) obj).add(obj2);
            return obj;
        } else {
            List arrayList2 = new ArrayList();
            arrayList2.add(obj);
            arrayList2.add(obj2);
            return arrayList2;
        }
    }

    public static Object addArray(Object obj, Object[] objArr) {
        int i = 0;
        while (objArr != null && i < objArr.length) {
            obj = add(obj, objArr[i]);
            i++;
        }
        return obj;
    }

    public static Object addCollection(Object obj, Collection collection) {
        for (Object add : collection) {
            obj = add(obj, add);
        }
        return obj;
    }

    public static Object[] addToArray(Object[] objArr, Object obj, Class cls) {
        if (objArr == null) {
            if (cls == null && obj != null) {
                cls = obj.getClass();
            }
            Object[] objArr2 = (Object[]) Array.newInstance(cls, 1);
            objArr2[0] = obj;
            return objArr2;
        }
        objArr2 = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), Array.getLength(objArr) + 1);
        System.arraycopy(objArr, 0, objArr2, 0, objArr.length);
        objArr2[objArr.length] = obj;
        return objArr2;
    }

    public static List array2List(Object[] objArr) {
        return (objArr == null || objArr.length == 0) ? new ArrayList() : new ArrayList(Arrays.asList(objArr));
    }

    public static Object clone(Object obj) {
        return obj == null ? null : obj instanceof List ? new ArrayList((List) obj) : obj;
    }

    public static boolean contains(Object obj, Object obj2) {
        return obj == null ? false : obj instanceof List ? ((List) obj).contains(obj2) : obj.equals(obj2);
    }

    public static Object ensureSize(Object obj, int i) {
        if (obj == null) {
            return new ArrayList(i);
        }
        if (obj instanceof ArrayList) {
            ArrayList arrayList = (ArrayList) obj;
            if (arrayList.size() > i) {
                return arrayList;
            }
            ArrayList arrayList2 = new ArrayList(i);
            arrayList2.addAll(arrayList);
            return arrayList2;
        }
        List arrayList3 = new ArrayList(i);
        arrayList3.add(obj);
        return arrayList3;
    }

    public static Object get(Object obj, int i) {
        if (obj == null) {
            throw new IndexOutOfBoundsException();
        } else if (obj instanceof List) {
            return ((List) obj).get(i);
        } else {
            if (i == 0) {
                return obj;
            }
            throw new IndexOutOfBoundsException();
        }
    }

    public static List getList(Object obj) {
        return getList(obj, false);
    }

    public static List getList(Object obj, boolean z) {
        if (obj == null) {
            return z ? null : Collections.EMPTY_LIST;
        } else {
            if (obj instanceof List) {
                return (List) obj;
            }
            List arrayList = new ArrayList(1);
            arrayList.add(obj);
            return arrayList;
        }
    }

    public static Iterator iterator(Object obj) {
        return obj == null ? Collections.EMPTY_LIST.iterator() : obj instanceof List ? ((List) obj).iterator() : getList(obj).iterator();
    }

    public static ListIterator listIterator(Object obj) {
        return obj == null ? Collections.EMPTY_LIST.listIterator() : obj instanceof List ? ((List) obj).listIterator() : getList(obj).listIterator();
    }

    public static Object remove(Object obj, int i) {
        if (obj == null) {
            obj = null;
        } else if (obj instanceof List) {
            List list = (List) obj;
            list.remove(i);
            if (list.size() == 0) {
                return null;
            }
        } else if (i == 0) {
            return null;
        }
        return obj;
    }

    public static Object remove(Object obj, Object obj2) {
        if (obj == null) {
            obj = null;
        } else if (obj instanceof List) {
            List list = (List) obj;
            list.remove(obj2);
            if (list.size() == 0) {
                return null;
            }
        } else if (obj.equals(obj2)) {
            return null;
        }
        return obj;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object[] removeFromArray(java.lang.Object[] r5, java.lang.Object r6) {
        /*
        r3 = 0;
        if (r6 == 0) goto L_0x0005;
    L_0x0003:
        if (r5 != 0) goto L_0x0007;
    L_0x0005:
        r0 = r5;
    L_0x0006:
        return r0;
    L_0x0007:
        r0 = r5.length;
    L_0x0008:
        r1 = r0 + -1;
        if (r0 <= 0) goto L_0x0005;
    L_0x000c:
        r0 = r5[r1];
        r0 = r6.equals(r0);
        if (r0 == 0) goto L_0x0045;
    L_0x0014:
        if (r5 != 0) goto L_0x003c;
    L_0x0016:
        r0 = r6.getClass();
    L_0x001a:
        r2 = java.lang.reflect.Array.getLength(r5);
        r2 = r2 + -1;
        r0 = java.lang.reflect.Array.newInstance(r0, r2);
        r0 = (java.lang.Object[]) r0;
        r0 = (java.lang.Object[]) r0;
        if (r1 <= 0) goto L_0x002d;
    L_0x002a:
        java.lang.System.arraycopy(r5, r3, r0, r3, r1);
    L_0x002d:
        r2 = r1 + 1;
        r3 = r5.length;
        if (r2 >= r3) goto L_0x0006;
    L_0x0032:
        r2 = r1 + 1;
        r3 = r5.length;
        r4 = r1 + 1;
        r3 = r3 - r4;
        java.lang.System.arraycopy(r5, r2, r0, r1, r3);
        goto L_0x0006;
    L_0x003c:
        r0 = r5.getClass();
        r0 = r0.getComponentType();
        goto L_0x001a;
    L_0x0045:
        r0 = r1;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.util.LazyList.removeFromArray(java.lang.Object[], java.lang.Object):java.lang.Object[]");
    }

    public static int size(Object obj) {
        return obj == null ? 0 : obj instanceof List ? ((List) obj).size() : 1;
    }

    public static Object toArray(Object obj, Class cls) {
        int i = 0;
        if (obj == null) {
            return (Object[]) Array.newInstance(cls, 0);
        }
        Object newInstance;
        if (obj instanceof List) {
            List list = (List) obj;
            if (!cls.isPrimitive()) {
                return list.toArray((Object[]) Array.newInstance(cls, list.size()));
            }
            newInstance = Array.newInstance(cls, list.size());
            while (i < list.size()) {
                Array.set(newInstance, i, list.get(i));
                i++;
            }
            return newInstance;
        }
        newInstance = Array.newInstance(cls, 1);
        Array.set(newInstance, 0, obj);
        return newInstance;
    }

    public static String toString(Object obj) {
        return obj == null ? "[]" : obj instanceof List ? ((List) obj).toString() : new StringBuffer().append("[").append(obj).append("]").toString();
    }

    public static String[] toStringArray(Object obj) {
        if (obj == null) {
            return __EMTPY_STRING_ARRAY;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            String[] strArr = new String[list.size()];
            int size = list.size();
            while (true) {
                int i = size - 1;
                if (size <= 0) {
                    return strArr;
                }
                Object obj2 = list.get(i);
                if (obj2 != null) {
                    strArr[i] = obj2.toString();
                }
                size = i;
            }
        } else {
            return new String[]{obj.toString()};
        }
    }
}
