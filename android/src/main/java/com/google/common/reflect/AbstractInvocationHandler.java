package com.google.common.reflect;

import com.google.common.annotations.Beta;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractInvocationHandler implements InvocationHandler {
    private static final Object[] NO_ARGS = new Object[0];

    protected abstract Object handleInvocation(Object obj, Method method, Object[] objArr) throws Throwable;

    public final Object invoke(Object obj, Method method, @Nullable Object[] objArr) throws Throwable {
        boolean z = false;
        if (objArr == null) {
            objArr = NO_ARGS;
        }
        if (objArr.length == 0 && method.getName().equals("hashCode")) {
            return Integer.valueOf(System.identityHashCode(obj));
        }
        if (objArr.length != 1 || !method.getName().equals("equals") || method.getParameterTypes()[0] != Object.class) {
            return (objArr.length == 0 && method.getName().equals("toString")) ? toString() : handleInvocation(obj, method, objArr);
        } else {
            if (obj == objArr[0]) {
                z = true;
            }
            return Boolean.valueOf(z);
        }
    }

    public String toString() {
        return super.toString();
    }
}
