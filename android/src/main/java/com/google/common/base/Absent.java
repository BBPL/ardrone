package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
final class Absent extends Optional<Object> {
    static final Absent INSTANCE = new Absent();
    private static final long serialVersionUID = 0;

    Absent() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public Set<Object> asSet() {
        return Collections.emptySet();
    }

    public boolean equals(@Nullable Object obj) {
        return obj == this;
    }

    public Object get() {
        throw new IllegalStateException("value is absent");
    }

    public int hashCode() {
        return 1502476572;
    }

    public boolean isPresent() {
        return false;
    }

    public Optional<Object> or(Optional<?> optional) {
        return (Optional) Preconditions.checkNotNull(optional);
    }

    public Object or(Supplier<?> supplier) {
        return Preconditions.checkNotNull(supplier.get(), "use orNull() instead of a Supplier that returns null");
    }

    public Object or(Object obj) {
        return Preconditions.checkNotNull(obj, "use orNull() instead of or(null)");
    }

    @Nullable
    public Object orNull() {
        return null;
    }

    public String toString() {
        return "Optional.absent()";
    }

    public <V> Optional<V> transform(Function<Object, V> function) {
        Preconditions.checkNotNull(function);
        return Optional.absent();
    }
}
