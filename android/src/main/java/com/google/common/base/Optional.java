package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0;

    Optional() {
    }

    public static <T> Optional<T> absent() {
        return Absent.INSTANCE;
    }

    public static <T> Optional<T> fromNullable(@Nullable T t) {
        return t == null ? absent() : new Present(t);
    }

    public static <T> Optional<T> of(T t) {
        return new Present(Preconditions.checkNotNull(t));
    }

    @Beta
    public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> iterable) {
        Preconditions.checkNotNull(iterable);
        return new Iterable<T>() {

            class C04881 extends AbstractIterator<T> {
                private final Iterator<? extends Optional<? extends T>> iterator = ((Iterator) Preconditions.checkNotNull(iterable.iterator()));

                C04881() {
                }

                protected T computeNext() {
                    while (this.iterator.hasNext()) {
                        Optional optional = (Optional) this.iterator.next();
                        if (optional.isPresent()) {
                            return optional.get();
                        }
                    }
                    return endOfData();
                }
            }

            public Iterator<T> iterator() {
                return new C04881();
            }
        };
    }

    public abstract Set<T> asSet();

    public abstract boolean equals(@Nullable Object obj);

    public abstract T get();

    public abstract int hashCode();

    public abstract boolean isPresent();

    @Beta
    public abstract Optional<T> or(Optional<? extends T> optional);

    @Beta
    public abstract T or(Supplier<? extends T> supplier);

    public abstract T or(T t);

    @Nullable
    public abstract T orNull();

    public abstract String toString();

    @Beta
    public abstract <V> Optional<V> transform(Function<? super T, V> function);
}
