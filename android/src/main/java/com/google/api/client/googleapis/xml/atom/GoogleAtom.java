package com.google.api.client.googleapis.xml.atom;

import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Types;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class GoogleAtom {
    public static final String ERROR_CONTENT_TYPE = "application/vnd.google.gdata.error+xml";
    public static final String GD_NAMESPACE = "http://schemas.google.com/g/2005";

    static class FieldsMask {
        StringBuilder buf = new StringBuilder();
        int numDifferences;

        FieldsMask() {
        }

        void append(String str) {
            StringBuilder stringBuilder = this.buf;
            int i = this.numDifferences + 1;
            this.numDifferences = i;
            if (i != 1) {
                stringBuilder.append(',');
            }
            stringBuilder.append(str);
        }

        void append(String str, FieldsMask fieldsMask) {
            Object obj = 1;
            append(str);
            StringBuilder stringBuilder = this.buf;
            if (fieldsMask.numDifferences != 1) {
                obj = null;
            }
            if (obj != null) {
                stringBuilder.append('/');
            } else {
                stringBuilder.append('(');
            }
            stringBuilder.append(fieldsMask.buf);
            if (obj == null) {
                stringBuilder.append(')');
            }
        }
    }

    private GoogleAtom() {
    }

    private static void appendFeedFields(StringBuilder stringBuilder, Class<?> cls, Class<?> cls2) {
        int[] iArr = new int[1];
        appendFieldsFor(stringBuilder, cls, iArr);
        if (iArr[0] != 0) {
            stringBuilder.append(",");
        }
        stringBuilder.append("entry(");
        int length = stringBuilder.length();
        iArr[0] = 0;
        appendFieldsFor(stringBuilder, cls2, iArr);
        updateFieldsBasedOnNumFields(stringBuilder, length - 1, iArr[0]);
    }

    private static void appendFieldsFor(StringBuilder stringBuilder, Class<?> cls, int[] iArr) {
        if (Map.class.isAssignableFrom(cls) || Collection.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("cannot specify field mask for a Map or Collection class: " + cls);
        }
        ClassInfo of = ClassInfo.of(cls);
        Iterator it = new TreeSet(of.getNames()).iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            FieldInfo fieldInfo = of.getFieldInfo(str);
            if (!fieldInfo.isFinal()) {
                int i = iArr[0] + 1;
                iArr[0] = i;
                if (i != 1) {
                    stringBuilder.append(',');
                }
                stringBuilder.append(str);
                Class type = fieldInfo.getType();
                if (Collection.class.isAssignableFrom(type)) {
                    type = (Class) Types.getIterableParameter(fieldInfo.getField().getGenericType());
                }
                if (type != null) {
                    if (fieldInfo.isPrimitive()) {
                        if (!(str.charAt(0) == '@' || str.equals("text()"))) {
                        }
                    } else if (!(Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type))) {
                        int[] iArr2 = new int[1];
                        int length = stringBuilder.length();
                        stringBuilder.append('(');
                        appendFieldsFor(stringBuilder, type, iArr2);
                        updateFieldsBasedOnNumFields(stringBuilder, length, iArr2[0]);
                    }
                }
            }
        }
    }

    public static Map<String, Object> computePatch(Object obj, Object obj2) {
        FieldsMask fieldsMask = new FieldsMask();
        Map computePatchInternal = computePatchInternal(fieldsMask, obj, obj2);
        if (fieldsMask.numDifferences != 0) {
            computePatchInternal.put("@gd:fields", fieldsMask.buf.toString());
        }
        return computePatchInternal;
    }

    private static ArrayMap<String, Object> computePatchInternal(FieldsMask fieldsMask, Object obj, Object obj2) {
        ArrayMap<String, Object> create = ArrayMap.create();
        Map mapOf = Data.mapOf(obj);
        Map mapOf2 = Data.mapOf(obj2);
        TreeSet treeSet = new TreeSet();
        treeSet.addAll(mapOf.keySet());
        treeSet.addAll(mapOf2.keySet());
        Iterator it = treeSet.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Object obj3 = mapOf2.get(str);
            Object obj4 = mapOf.get(str);
            if (obj3 != obj4) {
                Object obj5 = obj3 == null ? obj4.getClass() : obj3.getClass();
                if (Data.isPrimitive(obj5)) {
                    if (obj3 == null || !obj3.equals(obj4)) {
                        fieldsMask.append(str);
                        if (obj4 != null) {
                            create.add(str, obj4);
                        }
                    }
                } else if (Collection.class.isAssignableFrom(obj5)) {
                    if (!(obj3 == null || obj4 == null)) {
                        Collection collection = (Collection) obj4;
                        int size = ((Collection) obj3).size();
                        if (size == collection.size()) {
                            int i = 0;
                            while (i < size) {
                                r2 = new FieldsMask();
                                computePatchInternal(r2, obj4, obj3);
                                if (r2.numDifferences != 0) {
                                    break;
                                }
                                i++;
                            }
                            if (i != size) {
                            }
                        }
                    }
                    throw new UnsupportedOperationException("not yet implemented: support for patching collections");
                } else if (obj3 == null) {
                    fieldsMask.append(str);
                    create.add(str, Data.mapOf(obj4));
                } else if (obj4 == null) {
                    fieldsMask.append(str);
                } else {
                    r2 = new FieldsMask();
                    ArrayMap computePatchInternal = computePatchInternal(r2, obj4, obj3);
                    if (r2.numDifferences != 0) {
                        fieldsMask.append(str, r2);
                        create.add(str, computePatchInternal);
                    }
                }
            }
        }
        return create;
    }

    public static String getFeedFields(Class<?> cls, Class<?> cls2) {
        StringBuilder stringBuilder = new StringBuilder();
        appendFeedFields(stringBuilder, cls, cls2);
        return stringBuilder.toString();
    }

    public static String getFieldsFor(Class<?> cls) {
        StringBuilder stringBuilder = new StringBuilder();
        appendFieldsFor(stringBuilder, cls, new int[1]);
        return stringBuilder.toString();
    }

    private static void updateFieldsBasedOnNumFields(StringBuilder stringBuilder, int i, int i2) {
        switch (i2) {
            case 0:
                stringBuilder.deleteCharAt(i);
                return;
            case 1:
                stringBuilder.setCharAt(i, '/');
                return;
            default:
                stringBuilder.append(')');
                return;
        }
    }
}
