package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public final class ConstructorConstructor {
    private final Map<Type, InstanceCreator<?>> instanceCreators;

    class C09043 implements ObjectConstructor<T> {
        C09043() {
        }

        public T construct() {
            return new TreeSet();
        }
    }

    class C09054 implements ObjectConstructor<T> {
        C09054() {
        }

        public T construct() {
            return new LinkedHashSet();
        }
    }

    class C09065 implements ObjectConstructor<T> {
        C09065() {
        }

        public T construct() {
            return new LinkedList();
        }
    }

    class C09076 implements ObjectConstructor<T> {
        C09076() {
        }

        public T construct() {
            return new ArrayList();
        }
    }

    class C09087 implements ObjectConstructor<T> {
        C09087() {
        }

        public T construct() {
            return new LinkedHashMap();
        }
    }

    public ConstructorConstructor() {
        this(Collections.emptyMap());
    }

    public ConstructorConstructor(Map<Type, InstanceCreator<?>> map) {
        this.instanceCreators = map;
    }

    private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> cls) {
        try {
            final Constructor declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return new ObjectConstructor<T>() {
                public T construct() {
                    try {
                        return declaredConstructor.newInstance(null);
                    } catch (Throwable e) {
                        throw new RuntimeException("Failed to invoke " + declaredConstructor + " with no args", e);
                    } catch (InvocationTargetException e2) {
                        throw new RuntimeException("Failed to invoke " + declaredConstructor + " with no args", e2.getTargetException());
                    } catch (IllegalAccessException e3) {
                        throw new AssertionError(e3);
                    }
                }
            };
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(Class<? super T> cls) {
        return Collection.class.isAssignableFrom(cls) ? SortedSet.class.isAssignableFrom(cls) ? new C09043() : Set.class.isAssignableFrom(cls) ? new C09054() : Queue.class.isAssignableFrom(cls) ? new C09065() : new C09076() : Map.class.isAssignableFrom(cls) ? new C09087() : null;
    }

    private <T> ObjectConstructor<T> newUnsafeAllocator(final Type type, final Class<? super T> cls) {
        return new ObjectConstructor<T>() {
            private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

            public T construct() {
                try {
                    return this.unsafeAllocator.newInstance(cls);
                } catch (Throwable e) {
                    throw new RuntimeException("Unable to invoke no-args constructor for " + type + ". " + "Register an InstanceCreator with Gson for this type may fix this problem.", e);
                }
            }
        };
    }

    public <T> ObjectConstructor<T> getConstructor(TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        Class rawType = typeToken.getRawType();
        final InstanceCreator instanceCreator = (InstanceCreator) this.instanceCreators.get(type);
        if (instanceCreator != null) {
            return new ObjectConstructor<T>() {
                public T construct() {
                    return instanceCreator.createInstance(type);
                }
            };
        }
        ObjectConstructor<T> newDefaultConstructor = newDefaultConstructor(rawType);
        if (newDefaultConstructor != null) {
            return newDefaultConstructor;
        }
        newDefaultConstructor = newDefaultImplementationConstructor(rawType);
        return newDefaultConstructor == null ? newUnsafeAllocator(type, rawType) : newDefaultConstructor;
    }

    public String toString() {
        return this.instanceCreators.toString();
    }
}
