package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.lang.reflect.Field;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
@Beta
public final class Enums {

    private static final class ValueOfFunction<T extends Enum<T>> implements Function<String, T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Class<T> enumClass;

        private ValueOfFunction(Class<T> cls) {
            this.enumClass = (Class) Preconditions.checkNotNull(cls);
        }

        public T apply(String str) {
            try {
                return Enum.valueOf(this.enumClass, str);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public boolean equals(@Nullable Object obj) {
            return (obj instanceof ValueOfFunction) && this.enumClass.equals(((ValueOfFunction) obj).enumClass);
        }

        public int hashCode() {
            return this.enumClass.hashCode();
        }

        public String toString() {
            return "Enums.valueOf(" + this.enumClass + ")";
        }
    }

    private Enums() {
    }

    @GwtIncompatible("reflection")
    public static Field getField(Enum<?> enumR) {
        try {
            return enumR.getDeclaringClass().getDeclaredField(enumR.name());
        } catch (NoSuchFieldException e) {
            throw new AssertionError(e);
        }
    }

    public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> cls, String str) {
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(str);
        try {
            return Optional.of(Enum.valueOf(cls, str));
        } catch (IllegalArgumentException e) {
            return Optional.absent();
        }
    }

    public static <T extends Enum<T>> Function<String, T> valueOfFunction(Class<T> cls) {
        return new ValueOfFunction(cls);
    }
}
