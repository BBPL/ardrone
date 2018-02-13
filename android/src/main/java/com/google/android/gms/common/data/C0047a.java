package com.google.android.gms.common.data;

import com.google.android.gms.internal.C0242s;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class C0047a<T> implements Iterator<T> {
    private final DataBuffer<T> f42T;
    private int f43U = -1;

    public C0047a(DataBuffer<T> dataBuffer) {
        this.f42T = (DataBuffer) C0242s.m1208d(dataBuffer);
    }

    public boolean hasNext() {
        return this.f43U < this.f42T.getCount() + -1;
    }

    public T next() {
        if (hasNext()) {
            DataBuffer dataBuffer = this.f42T;
            int i = this.f43U + 1;
            this.f43U = i;
            return dataBuffer.get(i);
        }
        throw new NoSuchElementException("Cannot advance the iterator beyond " + this.f43U);
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}
