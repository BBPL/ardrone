package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class Equivalence<T> {

    static final class Equals extends Equivalence<Object> implements Serializable {
        static final Equals INSTANCE = new Equals();
        private static final long serialVersionUID = 1;

        Equals() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        protected boolean doEquivalent(Object obj, Object obj2) {
            return obj.equals(obj2);
        }

        public int doHash(Object obj) {
            return obj.hashCode();
        }
    }

    private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Equivalence<T> equivalence;
        @Nullable
        private final T target;

        EquivalentToPredicate(Equivalence<T> equivalence, @Nullable T t) {
            this.equivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
            this.target = t;
        }

        public boolean apply(@Nullable T t) {
            return this.equivalence.equivalent(t, this.target);
        }

        public boolean equals(@Nullable Object obj) {
            if (this != obj) {
                if (!(obj instanceof EquivalentToPredicate)) {
                    return false;
                }
                EquivalentToPredicate equivalentToPredicate = (EquivalentToPredicate) obj;
                if (!this.equivalence.equals(equivalentToPredicate.equivalence)) {
                    return false;
                }
                if (!Objects.equal(this.target, equivalentToPredicate.target)) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            return Objects.hashCode(this.equivalence, this.target);
        }

        public String toString() {
            return this.equivalence + ".equivalentTo(" + this.target + ")";
        }
    }

    static final class Identity extends Equivalence<Object> implements Serializable {
        static final Identity INSTANCE = new Identity();
        private static final long serialVersionUID = 1;

        Identity() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        protected boolean doEquivalent(Object obj, Object obj2) {
            return false;
        }

        protected int doHash(Object obj) {
            return System.identityHashCode(obj);
        }
    }

    public static final class Wrapper<T> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Equivalence<? super T> equivalence;
        @Nullable
        private final T reference;

        private Wrapper(Equivalence<? super T> equivalence, @Nullable T t) {
            this.equivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
            this.reference = t;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj != this) {
                if (!(obj instanceof Wrapper)) {
                    return false;
                }
                Wrapper wrapper = (Wrapper) obj;
                Equivalence equivalence = this.equivalence;
                if (!equivalence.equals(wrapper.equivalence)) {
                    return false;
                }
                if (!equivalence.equivalent(this.reference, wrapper.reference)) {
                    return false;
                }
            }
            return true;
        }

        @Nullable
        public T get() {
            return this.reference;
        }

        public int hashCode() {
            return this.equivalence.hash(this.reference);
        }

        public String toString() {
            return this.equivalence + ".wrap(" + this.reference + ")";
        }
    }

    protected Equivalence() {
    }

    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }

    protected abstract boolean doEquivalent(T t, T t2);

    protected abstract int doHash(T t);

    public final boolean equivalent(@Nullable T t, @Nullable T t2) {
        return t == t2 ? true : (t == null || t2 == null) ? false : doEquivalent(t, t2);
    }

    @Beta
    public final Predicate<T> equivalentTo(@Nullable T t) {
        return new EquivalentToPredicate(this, t);
    }

    public final int hash(@Nullable T t) {
        return t == null ? 0 : doHash(t);
    }

    public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
        return new FunctionalEquivalence(function, this);
    }

    @GwtCompatible(serializable = true)
    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        return new PairwiseEquivalence(this);
    }

    public final <S extends T> Wrapper<S> wrap(@Nullable S s) {
        return new Wrapper(s);
    }
}
