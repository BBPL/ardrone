package com.google.api.client.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.WeakHashMap;
import org.mortbay.jetty.HttpVersions;

public final class ClassInfo {
    private static final Map<Class<?>, ClassInfo> CACHE = new WeakHashMap();
    private static final Map<Class<?>, ClassInfo> CACHE_IGNORE_CASE = new WeakHashMap();
    private final Class<?> clazz;
    private final boolean ignoreCase;
    private final IdentityHashMap<String, FieldInfo> nameToFieldInfoMap = new IdentityHashMap();
    final List<String> names;

    class C04621 implements Comparator<String> {
        C04621() {
        }

        public int compare(String str, String str2) {
            return str == str2 ? 0 : str == null ? -1 : str2 == null ? 1 : str.compareTo(str2);
        }
    }

    private ClassInfo(Class<?> cls, boolean z) {
        this.clazz = cls;
        this.ignoreCase = z;
        boolean z2 = (z && cls.isEnum()) ? false : true;
        Preconditions.checkArgument(z2, "cannot ignore case on an enum: " + cls);
        Collection treeSet = new TreeSet(new C04621());
        for (Field of : cls.getDeclaredFields()) {
            FieldInfo of2 = FieldInfo.of(of);
            if (of2 != null) {
                Object intern;
                String name = of2.getName();
                if (z) {
                    intern = name.toLowerCase().intern();
                } else {
                    String str = name;
                }
                FieldInfo fieldInfo = (FieldInfo) this.nameToFieldInfoMap.get(intern);
                boolean z3 = fieldInfo == null;
                String str2 = z ? "case-insensitive " : HttpVersions.HTTP_0_9;
                Field field = fieldInfo == null ? null : fieldInfo.getField();
                Preconditions.checkArgument(z3, "two fields have the same %sname <%s>: %s and %s", str2, intern, of, field);
                this.nameToFieldInfoMap.put(intern, of2);
                treeSet.add(intern);
            }
        }
        Class superclass = cls.getSuperclass();
        if (superclass != null) {
            ClassInfo of3 = of(superclass, z);
            treeSet.addAll(of3.names);
            for (Entry entry : of3.nameToFieldInfoMap.entrySet()) {
                str = (String) entry.getKey();
                if (!this.nameToFieldInfoMap.containsKey(str)) {
                    this.nameToFieldInfoMap.put(str, entry.getValue());
                }
            }
        }
        this.names = treeSet.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(treeSet));
    }

    public static ClassInfo of(Class<?> cls) {
        return of(cls, false);
    }

    public static ClassInfo of(Class<?> cls, boolean z) {
        if (cls == null) {
            return null;
        }
        ClassInfo classInfo;
        Map map = z ? CACHE_IGNORE_CASE : CACHE;
        synchronized (map) {
            classInfo = (ClassInfo) map.get(cls);
            if (classInfo == null) {
                classInfo = new ClassInfo(cls, z);
                map.put(cls, classInfo);
            }
        }
        return classInfo;
    }

    public Field getField(String str) {
        FieldInfo fieldInfo = getFieldInfo(str);
        return fieldInfo == null ? null : fieldInfo.getField();
    }

    public FieldInfo getFieldInfo(String str) {
        Object intern;
        if (str != null) {
            if (this.ignoreCase) {
                str = str.toLowerCase();
            }
            intern = str.intern();
        }
        return (FieldInfo) this.nameToFieldInfoMap.get(intern);
    }

    public final boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    public Collection<String> getNames() {
        return this.names;
    }

    public Class<?> getUnderlyingClass() {
        return this.clazz;
    }

    public boolean isEnum() {
        return this.clazz.isEnum();
    }
}
