package com.google.api.client.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    public static final BigDecimal NULL_BIG_DECIMAL = new BigDecimal("0");
    public static final BigInteger NULL_BIG_INTEGER = new BigInteger("0");
    public static final Boolean NULL_BOOLEAN = new Boolean(true);
    public static final Byte NULL_BYTE = new Byte((byte) 0);
    private static final ConcurrentHashMap<Class<?>, Object> NULL_CACHE = new ConcurrentHashMap();
    public static final Character NULL_CHARACTER = new Character('\u0000');
    public static final DateTime NULL_DATE_TIME = new DateTime(0);
    public static final Double NULL_DOUBLE = new Double(0.0d);
    public static final Float NULL_FLOAT = new Float(0.0f);
    public static final Integer NULL_INTEGER = new Integer(0);
    public static final Long NULL_LONG = new Long(0);
    public static final Short NULL_SHORT = new Short((short) 0);
    public static final String NULL_STRING = new String();

    static {
        NULL_CACHE.put(Boolean.class, NULL_BOOLEAN);
        NULL_CACHE.put(String.class, NULL_STRING);
        NULL_CACHE.put(Character.class, NULL_CHARACTER);
        NULL_CACHE.put(Byte.class, NULL_BYTE);
        NULL_CACHE.put(Short.class, NULL_SHORT);
        NULL_CACHE.put(Integer.class, NULL_INTEGER);
        NULL_CACHE.put(Float.class, NULL_FLOAT);
        NULL_CACHE.put(Long.class, NULL_LONG);
        NULL_CACHE.put(Double.class, NULL_DOUBLE);
        NULL_CACHE.put(BigInteger.class, NULL_BIG_INTEGER);
        NULL_CACHE.put(BigDecimal.class, NULL_BIG_DECIMAL);
        NULL_CACHE.put(DateTime.class, NULL_DATE_TIME);
    }

    public static <T> T clone(T t) {
        if (t == null || isPrimitive(t.getClass())) {
            return t;
        }
        if (t instanceof GenericData) {
            return ((GenericData) t).clone();
        }
        Class cls = t.getClass();
        T newInstance = cls.isArray() ? Array.newInstance(cls.getComponentType(), Array.getLength(t)) : t instanceof ArrayMap ? ((ArrayMap) t).clone() : Types.newInstance(cls);
        deepCopy(t, newInstance);
        return newInstance;
    }

    public static void deepCopy(Object obj, Object obj2) {
        boolean z = true;
        int i = 0;
        Class cls = obj.getClass();
        Preconditions.checkArgument(cls == obj2.getClass());
        if (cls.isArray()) {
            if (Array.getLength(obj) != Array.getLength(obj2)) {
                z = false;
            }
            Preconditions.checkArgument(z);
            for (Object clone : Types.iterableOf(obj)) {
                Array.set(obj2, i, clone(clone));
                i++;
            }
        } else if (Collection.class.isAssignableFrom(cls)) {
            Collection<Object> collection = (Collection) obj;
            if (ArrayList.class.isAssignableFrom(cls)) {
                ((ArrayList) obj2).ensureCapacity(collection.size());
            }
            Collection collection2 = (Collection) obj2;
            for (Object clone2 : collection) {
                collection2.add(clone(clone2));
            }
        } else {
            boolean isAssignableFrom = GenericData.class.isAssignableFrom(cls);
            if (isAssignableFrom || !Map.class.isAssignableFrom(cls)) {
                ClassInfo of = isAssignableFrom ? ((GenericData) obj).classInfo : ClassInfo.of(cls);
                for (String fieldInfo : of.names) {
                    FieldInfo fieldInfo2 = of.getFieldInfo(fieldInfo);
                    if (!(fieldInfo2.isFinal() || (isAssignableFrom && fieldInfo2.isPrimitive()))) {
                        Object value = fieldInfo2.getValue(obj);
                        if (value != null) {
                            fieldInfo2.setValue(obj2, clone(value));
                        }
                    }
                }
            } else if (ArrayMap.class.isAssignableFrom(cls)) {
                ArrayMap arrayMap = (ArrayMap) obj2;
                ArrayMap arrayMap2 = (ArrayMap) obj;
                int size = arrayMap2.size();
                while (i < size) {
                    arrayMap.set(i, clone(arrayMap2.getValue(i)));
                    i++;
                }
            } else {
                Map map = (Map) obj2;
                for (Entry entry : ((Map) obj).entrySet()) {
                    map.put(entry.getKey(), clone(entry.getValue()));
                }
            }
        }
    }

    public static boolean isNull(Object obj) {
        return obj != null && obj == NULL_CACHE.get(obj.getClass());
    }

    public static boolean isPrimitive(Type type) {
        Type bound = type instanceof WildcardType ? Types.getBound((WildcardType) type) : type;
        if (bound instanceof Class) {
            Class cls = (Class) bound;
            if (cls.isPrimitive() || cls == Character.class || cls == String.class || cls == Integer.class || cls == Long.class || cls == Short.class || cls == Byte.class || cls == Float.class || cls == Double.class || cls == BigInteger.class || cls == BigDecimal.class || cls == DateTime.class || cls == Boolean.class) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValueOfPrimitiveType(Object obj) {
        return obj == null || isPrimitive(obj.getClass());
    }

    public static Map<String, Object> mapOf(Object obj) {
        return (obj == null || isNull(obj)) ? Collections.emptyMap() : obj instanceof Map ? (Map) obj : new DataMap(obj, false);
    }

    public static Collection<Object> newCollectionInstance(Type type) {
        Object rawType;
        Type bound = type instanceof WildcardType ? Types.getBound((WildcardType) type) : type;
        if (bound instanceof ParameterizedType) {
            rawType = ((ParameterizedType) bound).getRawType();
        } else {
            Type type2 = bound;
        }
        Class cls = rawType instanceof Class ? (Class) rawType : null;
        if (rawType == null || (rawType instanceof GenericArrayType) || (cls != null && (cls.isArray() || cls.isAssignableFrom(ArrayList.class)))) {
            return new ArrayList();
        }
        if (cls != null) {
            return cls.isAssignableFrom(HashSet.class) ? new HashSet() : cls.isAssignableFrom(TreeSet.class) ? new TreeSet() : (Collection) Types.newInstance(cls);
        } else {
            throw new IllegalArgumentException("unable to create new instance of type: " + rawType);
        }
    }

    public static Map<String, Object> newMapInstance(Class<?> cls) {
        return (cls == null || cls.isAssignableFrom(ArrayMap.class)) ? ArrayMap.create() : cls.isAssignableFrom(TreeMap.class) ? new TreeMap() : (Map) Types.newInstance(cls);
    }

    public static <T> T nullOf(Class<?> cls) {
        int i = 0;
        T t = NULL_CACHE.get(cls);
        if (t == null) {
            synchronized (NULL_CACHE) {
                t = NULL_CACHE.get(cls);
                if (t == null) {
                    if (cls.isArray()) {
                        Class cls2 = cls;
                        do {
                            cls2 = cls2.getComponentType();
                            i++;
                        } while (cls2.isArray());
                        t = Array.newInstance(cls2, new int[i]);
                    } else if (cls.isEnum()) {
                        FieldInfo fieldInfo = ClassInfo.of(cls).getFieldInfo(null);
                        Preconditions.checkNotNull(fieldInfo, "enum missing constant with @NullValue annotation: %s", cls);
                        t = fieldInfo.enumValue();
                    } else {
                        t = Types.newInstance(cls);
                    }
                    NULL_CACHE.put(cls, t);
                }
            }
        }
        return t;
    }

    public static Object parsePrimitiveValue(Type type, String str) {
        Class cls = type instanceof Class ? (Class) type : null;
        if (type == null || cls != null) {
            if (str == null || cls == null || cls.isAssignableFrom(String.class)) {
                return str;
            }
            if (cls == Character.class || cls == Character.TYPE) {
                if (str.length() == 1) {
                    return Character.valueOf(str.charAt(0));
                }
                throw new IllegalArgumentException("expected type Character/char but got " + cls);
            } else if (cls == Boolean.class || cls == Boolean.TYPE) {
                return Boolean.valueOf(str);
            } else {
                if (cls == Byte.class || cls == Byte.TYPE) {
                    return Byte.valueOf(str);
                }
                if (cls == Short.class || cls == Short.TYPE) {
                    return Short.valueOf(str);
                }
                if (cls == Integer.class || cls == Integer.TYPE) {
                    return Integer.valueOf(str);
                }
                if (cls == Long.class || cls == Long.TYPE) {
                    return Long.valueOf(str);
                }
                if (cls == Float.class || cls == Float.TYPE) {
                    return Float.valueOf(str);
                }
                if (cls == Double.class || cls == Double.TYPE) {
                    return Double.valueOf(str);
                }
                if (cls == DateTime.class) {
                    return DateTime.parseRfc3339(str);
                }
                if (cls == BigInteger.class) {
                    return new BigInteger(str);
                }
                if (cls == BigDecimal.class) {
                    return new BigDecimal(str);
                }
                if (cls.isEnum()) {
                    return ClassInfo.of(cls).getFieldInfo(str).enumValue();
                }
            }
        }
        throw new IllegalArgumentException("expected primitive class, but got: " + type);
    }

    public static Type resolveWildcardTypeOrTypeVariable(List<Type> list, Type type) {
        Type bound = type instanceof WildcardType ? Types.getBound((WildcardType) type) : type;
        while (bound instanceof TypeVariable) {
            Type resolveTypeVariable = Types.resolveTypeVariable(list, (TypeVariable) bound);
            if (resolveTypeVariable == null) {
                resolveTypeVariable = bound;
            }
            bound = resolveTypeVariable instanceof TypeVariable ? ((TypeVariable) resolveTypeVariable).getBounds()[0] : resolveTypeVariable;
        }
        return bound;
    }
}
