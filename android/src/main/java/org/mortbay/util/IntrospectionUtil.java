package org.mortbay.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.mortbay.jetty.HttpVersions;

public class IntrospectionUtil {
    public static boolean checkParams(Class[] clsArr, Class[] clsArr2, boolean z) {
        if (!(clsArr == null && clsArr2 == null)) {
            if (clsArr == null && clsArr2 != null) {
                return false;
            }
            if ((clsArr != null && clsArr2 == null) || clsArr.length != clsArr2.length) {
                return false;
            }
            if (clsArr.length != 0) {
                int i;
                if (z) {
                    i = 0;
                    while (i < clsArr.length && clsArr[i].equals(clsArr2[i])) {
                        i++;
                    }
                } else {
                    i = 0;
                    while (i < clsArr.length && clsArr[i].isAssignableFrom(clsArr2[i])) {
                        i++;
                    }
                }
                if (i != clsArr.length) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean containsSameFieldName(Field field, Class cls, boolean z) {
        int i = 0;
        if (z && !cls.getPackage().equals(field.getDeclaringClass().getPackage())) {
            return false;
        }
        Field[] declaredFields = cls.getDeclaredFields();
        boolean z2 = false;
        while (i < declaredFields.length && !z2) {
            if (declaredFields[i].getName().equals(field.getName())) {
                z2 = true;
            }
            i++;
        }
        return z2;
    }

    public static boolean containsSameMethodSignature(Method method, Class cls, boolean z) {
        int i = 0;
        if (z && !cls.getPackage().equals(method.getDeclaringClass().getPackage())) {
            return false;
        }
        Method[] declaredMethods = cls.getDeclaredMethods();
        boolean z2 = false;
        while (i < declaredMethods.length && !z2) {
            if (isSameSignature(method, declaredMethods[i])) {
                z2 = true;
            }
            i++;
        }
        return z2;
    }

    public static Field findField(Class cls, String str, Class cls2, boolean z, boolean z2) throws NoSuchFieldException {
        if (cls == null) {
            throw new NoSuchFieldException("No class");
        } else if (str == null) {
            throw new NoSuchFieldException("No field name");
        } else {
            try {
                Field declaredField = cls.getDeclaredField(str);
                if (z2) {
                    if (declaredField.getType().equals(cls2)) {
                        return declaredField;
                    }
                } else if (declaredField.getType().isAssignableFrom(cls2)) {
                    return declaredField;
                }
                if (z) {
                    return findInheritedField(cls.getPackage(), cls.getSuperclass(), str, cls2, z2);
                }
                throw new NoSuchFieldException(new StringBuffer().append("No field with name ").append(str).append(" in class ").append(cls.getName()).append(" of type ").append(cls2).toString());
            } catch (NoSuchFieldException e) {
                return findInheritedField(cls.getPackage(), cls.getSuperclass(), str, cls2, z2);
            }
        }
    }

    protected static Field findInheritedField(Package packageR, Class cls, String str, Class cls2, boolean z) throws NoSuchFieldException {
        if (cls == null) {
            throw new NoSuchFieldException("No class");
        } else if (str == null) {
            throw new NoSuchFieldException("No field name");
        } else {
            try {
                Field declaredField = cls.getDeclaredField(str);
                return (isInheritable(packageR, declaredField) && isTypeCompatible(cls2, declaredField.getType(), z)) ? declaredField : findInheritedField(cls.getPackage(), cls.getSuperclass(), str, cls2, z);
            } catch (NoSuchFieldException e) {
                return findInheritedField(cls.getPackage(), cls.getSuperclass(), str, cls2, z);
            }
        }
    }

    protected static Method findInheritedMethod(Package packageR, Class cls, String str, Class[] clsArr, boolean z) throws NoSuchMethodException {
        if (cls == null) {
            throw new NoSuchMethodException("No class");
        } else if (str == null) {
            throw new NoSuchMethodException("No method name");
        } else {
            Method method = null;
            Method[] declaredMethods = cls.getDeclaredMethods();
            int i = 0;
            while (i < declaredMethods.length && method == null) {
                if (declaredMethods[i].getName().equals(str) && isInheritable(packageR, declaredMethods[i]) && checkParams(declaredMethods[i].getParameterTypes(), clsArr, z)) {
                    method = declaredMethods[i];
                }
                i++;
            }
            return method != null ? method : findInheritedMethod(cls.getPackage(), cls.getSuperclass(), str, clsArr, z);
        }
    }

    public static Method findMethod(Class cls, String str, Class[] clsArr, boolean z, boolean z2) throws NoSuchMethodException {
        if (cls == null) {
            throw new NoSuchMethodException("No class");
        } else if (str == null || str.trim().equals(HttpVersions.HTTP_0_9)) {
            throw new NoSuchMethodException("No method name");
        } else {
            Method method = null;
            Method[] declaredMethods = cls.getDeclaredMethods();
            for (int i = 0; i < declaredMethods.length && method == null; i++) {
                if (declaredMethods[i].getName().equals(str)) {
                    if (checkParams(declaredMethods[i].getParameterTypes(), clsArr == null ? new Class[0] : clsArr, z2)) {
                        method = declaredMethods[i];
                    }
                }
            }
            if (method != null) {
                return method;
            }
            if (z) {
                return findInheritedMethod(cls.getPackage(), cls.getSuperclass(), str, clsArr, z2);
            }
            throw new NoSuchMethodException(new StringBuffer().append("No such method ").append(str).append(" on class ").append(cls.getName()).toString());
        }
    }

    public static boolean isInheritable(Package packageR, Member member) {
        if (!(packageR == null || member == null)) {
            int modifiers = member.getModifiers();
            if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                return true;
            }
            if (!Modifier.isPrivate(modifiers) && packageR.equals(member.getDeclaringClass().getPackage())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isJavaBeanCompliantSetter(Method method) {
        return method != null && method.getReturnType() == Void.TYPE && method.getName().startsWith("set") && method.getParameterTypes().length == 1;
    }

    public static boolean isSameSignature(Method method, Method method2) {
        if (!(method == null || method2 == null)) {
            List asList = Arrays.asList(method.getParameterTypes());
            Collection asList2 = Arrays.asList(method2.getParameterTypes());
            if (method.getName().equals(method2.getName()) && asList.containsAll(asList2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTypeCompatible(Class cls, Class cls2, boolean z) {
        return ((cls != null || cls2 == null) && (cls == null || cls2 != null)) ? (cls == null && cls2 == null) ? true : z ? cls.equals(cls2) : cls.isAssignableFrom(cls2) : false;
    }
}
