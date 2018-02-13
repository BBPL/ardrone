package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public final class Functions {

    private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
        private static final long serialVersionUID = 0;
        private final E value;

        public ConstantFunction(@Nullable E e) {
            this.value = e;
        }

        public E apply(@Nullable Object obj) {
            return this.value;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ConstantFunction)) {
                return false;
            }
            return Objects.equal(this.value, ((ConstantFunction) obj).value);
        }

        public int hashCode() {
            return this.value == null ? 0 : this.value.hashCode();
        }

        public String toString() {
            return "constant(" + this.value + ")";
        }
    }

    private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final V defaultValue;
        final Map<K, ? extends V> map;

        ForMapWithDefault(Map<K, ? extends V> map, @Nullable V v) {
            this.map = (Map) Preconditions.checkNotNull(map);
            this.defaultValue = v;
        }

        public V apply(K k) {
            V v = this.map.get(k);
            return (v != null || this.map.containsKey(k)) ? v : this.defaultValue;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ForMapWithDefault)) {
                return false;
            }
            ForMapWithDefault forMapWithDefault = (ForMapWithDefault) obj;
            return this.map.equals(forMapWithDefault.map) && Objects.equal(this.defaultValue, forMapWithDefault.defaultValue);
        }

        public int hashCode() {
            return Objects.hashCode(this.map, this.defaultValue);
        }

        public String toString() {
            return "forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
        }
    }

    private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {
        private static final long serialVersionUID = 0;
        private final Function<A, ? extends B> f265f;
        private final Function<B, C> f266g;

        public FunctionComposition(Function<B, C> function, Function<A, ? extends B> function2) {
            this.f266g = (Function) Preconditions.checkNotNull(function);
            this.f265f = (Function) Preconditions.checkNotNull(function2);
        }

        public C apply(A a) {
            return this.f266g.apply(this.f265f.apply(a));
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof FunctionComposition)) {
                return false;
            }
            FunctionComposition functionComposition = (FunctionComposition) obj;
            return this.f265f.equals(functionComposition.f265f) && this.f266g.equals(functionComposition.f266g);
        }

        public int hashCode() {
            return this.f265f.hashCode() ^ this.f266g.hashCode();
        }

        public String toString() {
            return this.f266g.toString() + "(" + this.f265f.toString() + ")";
        }
    }

    private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final Map<K, V> map;

        FunctionForMapNoDefault(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        public V apply(K k) {
            V v = this.map.get(k);
            boolean z = v != null || this.map.containsKey(k);
            Preconditions.checkArgument(z, "Key '%s' not present in map", k);
            return v;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof FunctionForMapNoDefault)) {
                return false;
            }
            return this.map.equals(((FunctionForMapNoDefault) obj).map);
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            return "forMap(" + this.map + ")";
        }
    }

    private enum IdentityFunction implements Function<Object, Object> {
        INSTANCE;

        public Object apply(Object obj) {
            return obj;
        }

        public String toString() {
            return "identity";
        }
    }

    private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
        private static final long serialVersionUID = 0;
        private final Predicate<T> predicate;

        private PredicateFunction(Predicate<T> predicate) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        public Boolean apply(T t) {
            return Boolean.valueOf(this.predicate.apply(t));
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof PredicateFunction)) {
                return false;
            }
            return this.predicate.equals(((PredicateFunction) obj).predicate);
        }

        public int hashCode() {
            return this.predicate.hashCode();
        }

        public String toString() {
            return "forPredicate(" + this.predicate + ")";
        }
    }

    private static class SupplierFunction<T> implements Function<Object, T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Supplier<T> supplier;

        private SupplierFunction(Supplier<T> supplier) {
            this.supplier = (Supplier) Preconditions.checkNotNull(supplier);
        }

        public T apply(@Nullable Object obj) {
            return this.supplier.get();
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof SupplierFunction)) {
                return false;
            }
            return this.supplier.equals(((SupplierFunction) obj).supplier);
        }

        public int hashCode() {
            return this.supplier.hashCode();
        }

        public String toString() {
            return "forSupplier(" + this.supplier + ")";
        }
    }

    private enum ToStringFunction implements Function<Object, String> {
        INSTANCE;

        public String apply(Object obj) {
            Preconditions.checkNotNull(obj);
            return obj.toString();
        }

        public String toString() {
            return "toString";
        }
    }

    private Functions() {
    }

    public static <A, B, C> Function<A, C> compose(Function<B, C> function, Function<A, ? extends B> function2) {
        return new FunctionComposition(function, function2);
    }

    public static <E> Function<Object, E> constant(@Nullable E e) {
        return new ConstantFunction(e);
    }

    public static <K, V> Function<K, V> forMap(Map<K, V> map) {
        return new FunctionForMapNoDefault(map);
    }

    public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @Nullable V v) {
        return new ForMapWithDefault(map, v);
    }

    public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
        return new PredicateFunction(predicate);
    }

    @Beta
    public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) {
        return new SupplierFunction(supplier);
    }

    public static <E> Function<E, E> identity() {
        return IdentityFunction.INSTANCE;
    }

    public static Function<Object, String> toStringFunction() {
        return ToStringFunction.INSTANCE;
    }
}
