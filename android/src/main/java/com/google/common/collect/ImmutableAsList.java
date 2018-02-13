package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

@GwtCompatible(emulated = true, serializable = true)
abstract class ImmutableAsList<E> extends ImmutableList<E> {

    @GwtIncompatible("serialization")
    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableCollection<?> collection;

        SerializedForm(ImmutableCollection<?> immutableCollection) {
            this.collection = immutableCollection;
        }

        Object readResolve() {
            return this.collection.asList();
        }
    }

    ImmutableAsList() {
    }

    @GwtIncompatible("serialization")
    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public boolean contains(Object obj) {
        return delegateCollection().contains(obj);
    }

    abstract ImmutableCollection<E> delegateCollection();

    public boolean isEmpty() {
        return delegateCollection().isEmpty();
    }

    boolean isPartialView() {
        return delegateCollection().isPartialView();
    }

    public int size() {
        return delegateCollection().size();
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new SerializedForm(delegateCollection());
    }
}
