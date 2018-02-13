package com.google.common.eventbus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Method;
import java.util.Set;

class AnnotatedHandlerFinder implements HandlerFindingStrategy {
    AnnotatedHandlerFinder() {
    }

    private static EventHandler makeHandler(Object obj, Method method) {
        return methodIsDeclaredThreadSafe(method) ? new EventHandler(obj, method) : new SynchronizedEventHandler(obj, method);
    }

    private static boolean methodIsDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }

    public Multimap<Class<?>, EventHandler> findAllHandlers(Object obj) {
        Multimap<Class<?>, EventHandler> create = HashMultimap.create();
        Class cls = obj.getClass();
        Set<Class> rawTypes = TypeToken.of(cls).getTypes().rawTypes();
        for (Method method : cls.getMethods()) {
            for (Class method2 : rawTypes) {
                try {
                    if (method2.getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Subscribe.class)) {
                        Class[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length != 1) {
                            throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods must require a single argument.");
                        }
                        create.put(parameterTypes[0], makeHandler(obj, method));
                    }
                } catch (NoSuchMethodException e) {
                }
            }
        }
        return create;
    }
}
