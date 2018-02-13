package com.google.api.client.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.WeakHashMap;

public class FieldInfo {
    private static final Map<Field, FieldInfo> CACHE = new WeakHashMap();
    private final Field field;
    private final boolean isPrimitive;
    private final String name;

    FieldInfo(Field field, String str) {
        this.field = field;
        this.name = str == null ? null : str.intern();
        this.isPrimitive = Data.isPrimitive(getType());
    }

    public static Object getFieldValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static FieldInfo of(Enum<?> enumR) {
        boolean z = false;
        try {
            FieldInfo of = of(enumR.getClass().getField(enumR.name()));
            if (of != null) {
                z = true;
            }
            Preconditions.checkArgument(z, "enum constant missing @Value or @NullValue annotation: %s", enumR);
            return of;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FieldInfo of(Field field) {
        FieldInfo fieldInfo = null;
        if (field != null) {
            synchronized (CACHE) {
                FieldInfo fieldInfo2 = (FieldInfo) CACHE.get(field);
                boolean isEnumConstant = field.isEnumConstant();
                if (fieldInfo2 != null || (!isEnumConstant && Modifier.isStatic(field.getModifiers()))) {
                    fieldInfo = fieldInfo2;
                } else {
                    String value;
                    if (isEnumConstant) {
                        Value value2 = (Value) field.getAnnotation(Value.class);
                        if (value2 != null) {
                            value = value2.value();
                        } else if (((NullValue) field.getAnnotation(NullValue.class)) != null) {
                            value = null;
                        }
                    } else {
                        Key key = (Key) field.getAnnotation(Key.class);
                        if (key == null) {
                        } else {
                            value = key.value();
                            field.setAccessible(true);
                        }
                    }
                    fieldInfo2 = new FieldInfo(field, "##default".equals(value) ? field.getName() : value);
                    CACHE.put(field, fieldInfo2);
                    fieldInfo = fieldInfo2;
                }
            }
        }
        return fieldInfo;
    }

    public static void setFieldValue(Field field, Object obj, Object obj2) {
        if (Modifier.isFinal(field.getModifiers())) {
            Object fieldValue = getFieldValue(field, obj);
            if (obj2 == null) {
                if (fieldValue == null) {
                    return;
                }
            } else if (obj2.equals(fieldValue)) {
                return;
            }
            throw new IllegalArgumentException("expected final value <" + fieldValue + "> but was <" + obj2 + "> on " + field.getName() + " field in " + obj.getClass().getName());
        }
        try {
            field.set(obj, obj2);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        } catch (Throwable e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public <T extends Enum<T>> T enumValue() {
        return Enum.valueOf(this.field.getDeclaringClass(), this.field.getName());
    }

    public ClassInfo getClassInfo() {
        return ClassInfo.of(this.field.getDeclaringClass());
    }

    public Field getField() {
        return this.field;
    }

    public Type getGenericType() {
        return this.field.getGenericType();
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.field.getType();
    }

    public Object getValue(Object obj) {
        return getFieldValue(this.field, obj);
    }

    public boolean isFinal() {
        return Modifier.isFinal(this.field.getModifiers());
    }

    public boolean isPrimitive() {
        return this.isPrimitive;
    }

    public void setValue(Object obj, Object obj2) {
        setFieldValue(this.field, obj, obj2);
    }
}
