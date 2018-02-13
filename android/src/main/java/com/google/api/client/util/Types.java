package com.google.api.client.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Types {
    private Types() {
    }

    private static Type getActualParameterAtPosition(Type type, Class<?> cls, int i) {
        Type type2 = getSuperParameterizedType(type, cls).getActualTypeArguments()[i];
        if (!(type2 instanceof TypeVariable)) {
            return type2;
        }
        Type resolveTypeVariable = resolveTypeVariable(Arrays.asList(new Type[]{type}), (TypeVariable) type2);
        return resolveTypeVariable != null ? resolveTypeVariable : type2;
    }

    public static Type getArrayComponentType(Type type) {
        return type instanceof GenericArrayType ? ((GenericArrayType) type).getGenericComponentType() : ((Class) type).getComponentType();
    }

    public static Type getBound(WildcardType wildcardType) {
        Type[] lowerBounds = wildcardType.getLowerBounds();
        return lowerBounds.length != 0 ? lowerBounds[0] : wildcardType.getUpperBounds()[0];
    }

    public static Type getIterableParameter(Type type) {
        return getActualParameterAtPosition(type, Iterable.class, 0);
    }

    public static Type getMapValueParameter(Type type) {
        return getActualParameterAtPosition(type, Map.class, 1);
    }

    public static Class<?> getRawArrayComponentType(List<Type> list, Type type) {
        Type resolveTypeVariable = type instanceof TypeVariable ? resolveTypeVariable(list, (TypeVariable) type) : type;
        if (resolveTypeVariable instanceof GenericArrayType) {
            return Array.newInstance(getRawArrayComponentType(list, getArrayComponentType(resolveTypeVariable)), 0).getClass();
        }
        if (resolveTypeVariable instanceof Class) {
            return (Class) resolveTypeVariable;
        }
        if (resolveTypeVariable instanceof ParameterizedType) {
            return getRawClass((ParameterizedType) resolveTypeVariable);
        }
        Preconditions.checkArgument(resolveTypeVariable == null, "wildcard type is not supported: %s", resolveTypeVariable);
        return Object.class;
    }

    public static Class<?> getRawClass(ParameterizedType parameterizedType) {
        return (Class) parameterizedType.getRawType();
    }

    public static ParameterizedType getSuperParameterizedType(Type type, Class<?> cls) {
        Type type2;
        if (type instanceof Class) {
            type2 = type;
        } else {
            if (type instanceof ParameterizedType) {
                type2 = type;
            }
            return null;
        }
        while (type2 != null && type2 != Object.class) {
            Class cls2;
            if (type2 instanceof Class) {
                cls2 = (Class) type2;
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type2;
                Class<?> rawClass = getRawClass(parameterizedType);
                if (rawClass == cls) {
                    return parameterizedType;
                }
                if (cls.isInterface()) {
                    for (Type type3 : rawClass.getGenericInterfaces()) {
                        if (cls.isAssignableFrom(type3 instanceof Class ? (Class) type3 : getRawClass((ParameterizedType) type3))) {
                            type2 = type3;
                            break;
                        }
                    }
                }
                Class<?> cls3 = rawClass;
            }
            type2 = cls2.getGenericSuperclass();
        }
        return null;
    }

    private static IllegalArgumentException handleExceptionForNewInstance(Exception exception, Class<?> cls) {
        Object obj = null;
        StringBuilder append = new StringBuilder("unable to create new instance of class ").append(cls.getName());
        ArrayList arrayList = new ArrayList();
        if (cls.isArray()) {
            arrayList.add("because it is an array");
        } else if (cls.isPrimitive()) {
            arrayList.add("because it is primitive");
        } else if (cls == Void.class) {
            arrayList.add("because it is void");
        } else {
            if (Modifier.isInterface(cls.getModifiers())) {
                arrayList.add("because it is an interface");
            } else if (Modifier.isAbstract(cls.getModifiers())) {
                arrayList.add("because it is abstract");
            }
            if (!(cls.getEnclosingClass() == null || Modifier.isStatic(cls.getModifiers()))) {
                arrayList.add("because it is not static");
            }
            if (Modifier.isPublic(cls.getModifiers())) {
                try {
                    cls.getConstructor(new Class[0]);
                } catch (NoSuchMethodException e) {
                    arrayList.add("because it has no accessible default constructor");
                }
            } else {
                arrayList.add("possibly because it is not public");
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (obj != null) {
                append.append(" and");
            } else {
                obj = 1;
            }
            append.append(" ").append(str);
        }
        return new IllegalArgumentException(append.toString(), exception);
    }

    public static boolean isArray(Type type) {
        return (type instanceof GenericArrayType) || ((type instanceof Class) && ((Class) type).isArray());
    }

    public static boolean isAssignableToOrFrom(Class<?> cls, Class<?> cls2) {
        return cls.isAssignableFrom(cls2) || cls2.isAssignableFrom(cls);
    }

    public static <T> Iterable<T> iterableOf(final Object obj) {
        if (obj instanceof Iterable) {
            return (Iterable) obj;
        }
        Class cls = obj.getClass();
        Preconditions.checkArgument(cls.isArray(), "not an array or Iterable: %s", cls);
        return !cls.getComponentType().isPrimitive() ? Arrays.asList((Object[]) obj) : new Iterable<T>() {

            class C04671 implements Iterator<T> {
                int index = 0;
                final int length = Array.getLength(obj);

                C04671() {
                }

                public boolean hasNext() {
                    return this.index < this.length;
                }

                public T next() {
                    if (hasNext()) {
                        Object obj = obj;
                        int i = this.index;
                        this.index = i + 1;
                        return Array.get(obj, i);
                    }
                    throw new NoSuchElementException();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            }

            public Iterator<T> iterator() {
                return new C04671();
            }
        };
    }

    public static <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw handleExceptionForNewInstance(e, cls);
        } catch (Exception e2) {
            throw handleExceptionForNewInstance(e2, cls);
        }
    }

    public static Type resolveTypeVariable(List<Type> list, TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            Class cls = (Class) genericDeclaration;
            int size = list.size();
            ParameterizedType parameterizedType = null;
            while (parameterizedType == null) {
                size--;
                if (size < 0) {
                    break;
                }
                parameterizedType = getSuperParameterizedType((Type) list.get(size), cls);
            }
            if (parameterizedType != null) {
                TypeVariable[] typeParameters = genericDeclaration.getTypeParameters();
                int i = 0;
                while (i < typeParameters.length && !typeParameters[i].equals(typeVariable)) {
                    i++;
                }
                Type type = parameterizedType.getActualTypeArguments()[i];
                if (!(type instanceof TypeVariable)) {
                    return type;
                }
                Type resolveTypeVariable = resolveTypeVariable(list, (TypeVariable) type);
                return resolveTypeVariable != null ? resolveTypeVariable : type;
            }
        }
        return null;
    }

    public static Object toArray(Collection<?> collection, Class<?> cls) {
        if (!cls.isPrimitive()) {
            return collection.toArray((Object[]) Array.newInstance(cls, collection.size()));
        }
        Object newInstance = Array.newInstance(cls, collection.size());
        int i = 0;
        for (Object obj : collection) {
            Array.set(newInstance, i, obj);
            i++;
        }
        return newInstance;
    }
}
