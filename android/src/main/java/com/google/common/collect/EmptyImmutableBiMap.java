package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class EmptyImmutableBiMap extends ImmutableBiMap<Object, Object> {
    static final EmptyImmutableBiMap INSTANCE = new EmptyImmutableBiMap();

    private EmptyImmutableBiMap() {
    }

    ImmutableMap<Object, Object> delegate() {
        return ImmutableMap.of();
    }

    public ImmutableBiMap<Object, Object> inverse() {
        return this;
    }

    boolean isPartialView() {
        return false;
    }

    Object readResolve() {
        return INSTANCE;
    }
}
