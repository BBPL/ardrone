package com.google.android.gms.common.data;

import java.util.Iterator;

public abstract class DataBuffer<T> implements Iterable<T> {
    protected final C0051d f1S;

    protected DataBuffer(C0051d c0051d) {
        this.f1S = c0051d;
    }

    public void close() {
        this.f1S.close();
    }

    public int describeContents() {
        return 0;
    }

    public abstract T get(int i);

    public int getCount() {
        return this.f1S.getCount();
    }

    public boolean isClosed() {
        return this.f1S.isClosed();
    }

    public Iterator<T> iterator() {
        return new C0047a(this);
    }
}
