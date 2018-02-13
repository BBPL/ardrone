package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import org.apache.http.annotation.Immutable;

@Immutable
public class CloneUtils {
    private CloneUtils() {
    }

    public static Object clone(Object obj) throws CloneNotSupportedException {
        Object obj2 = null;
        if (obj != null) {
            if (obj instanceof Cloneable) {
                try {
                    try {
                        obj2 = obj.getClass().getMethod("clone", (Class[]) null).invoke(obj, (Object[]) null);
                    } catch (InvocationTargetException e) {
                        Throwable cause = e.getCause();
                        if (cause instanceof CloneNotSupportedException) {
                            throw ((CloneNotSupportedException) cause);
                        }
                        throw new Error("Unexpected exception", cause);
                    } catch (IllegalAccessException e2) {
                        throw new IllegalAccessError(e2.getMessage());
                    }
                } catch (NoSuchMethodException e3) {
                    throw new NoSuchMethodError(e3.getMessage());
                }
            }
            throw new CloneNotSupportedException();
        }
        return obj2;
    }
}
