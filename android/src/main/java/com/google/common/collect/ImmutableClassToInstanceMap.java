package com.google.common.collect;

import com.google.common.primitives.Primitives;
import java.util.Map;
import java.util.Map.Entry;

public final class ImmutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B> {
    private final ImmutableMap<Class<? extends B>, B> delegate;

    public static final class Builder<B> {
        private final com.google.common.collect.ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();

        private static <B, T extends B> T cast(Class<T> cls, B b) {
            return Primitives.wrap(cls).cast(b);
        }

        public ImmutableClassToInstanceMap<B> build() {
            return new ImmutableClassToInstanceMap(this.mapBuilder.build());
        }

        public <T extends B> Builder<B> put(Class<T> cls, T t) {
            this.mapBuilder.put(cls, t);
            return this;
        }

        public <T extends B> Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> map) {
            for (Entry entry : map.entrySet()) {
                Class cls = (Class) entry.getKey();
                this.mapBuilder.put(cls, cast(cls, entry.getValue()));
            }
            return this;
        }
    }

    private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> immutableMap) {
        this.delegate = immutableMap;
    }

    public static <B> Builder<B> builder() {
        return new Builder();
    }

    public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> map) {
        return map instanceof ImmutableClassToInstanceMap ? (ImmutableClassToInstanceMap) map : new Builder().putAll(map).build();
    }

    protected Map<Class<? extends B>, B> delegate() {
        return this.delegate;
    }

    public <T extends B> T getInstance(Class<T> cls) {
        return this.delegate.get(cls);
    }

    public <T extends B> T putInstance(Class<T> cls, T t) {
        throw new UnsupportedOperationException();
    }
}
