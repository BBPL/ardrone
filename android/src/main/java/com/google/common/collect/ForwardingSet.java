package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSet<E> extends ForwardingCollection<E> implements Set<E> {
    protected ForwardingSet() {
    }

    protected abstract Set<E> delegate();

    public boolean equals(@Nullable Object obj) {
        return obj == this || delegate().equals(obj);
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    @Beta
    protected boolean standardEquals(@Nullable Object obj) {
        return Sets.equalsImpl(this, obj);
    }

    @Beta
    protected int standardHashCode() {
        return Sets.hashCodeImpl(this);
    }

    protected boolean standardRemoveAll(Collection<?> collection) {
        return Sets.removeAllImpl((Set) this, (Collection) Preconditions.checkNotNull(collection));
    }
}
