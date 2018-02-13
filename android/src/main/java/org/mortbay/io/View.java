package org.mortbay.io;

import org.mortbay.io.Buffer.CaseInsensitve;

public class View extends AbstractBuffer {
    Buffer _buffer;

    public static class CaseInsensitive extends View implements CaseInsensitve {
        public CaseInsensitive(Buffer buffer) {
            super(buffer);
        }

        public CaseInsensitive(Buffer buffer, int i, int i2, int i3, int i4) {
            super(buffer, i, i2, i3, i4);
        }

        public boolean equals(Object obj) {
            return this == obj || (((obj instanceof Buffer) && ((Buffer) obj).equalsIgnoreCase(this)) || super.equals(obj));
        }
    }

    public View() {
        super(2, true);
    }

    public View(Buffer buffer) {
        int i = 1;
        super(2, !buffer.isImmutable());
        this._buffer = buffer.buffer();
        setPutIndex(buffer.putIndex());
        setGetIndex(buffer.getIndex());
        setMarkIndex(buffer.markIndex());
        if (!buffer.isReadOnly()) {
            i = 2;
        }
        this._access = i;
    }

    public View(Buffer buffer, int i, int i2, int i3, int i4) {
        super(2, !buffer.isImmutable());
        this._buffer = buffer.buffer();
        setPutIndex(i3);
        setGetIndex(i2);
        setMarkIndex(i);
        this._access = i4;
    }

    public byte[] array() {
        return this._buffer.array();
    }

    public Buffer buffer() {
        return this._buffer.buffer();
    }

    public int capacity() {
        return this._buffer.capacity();
    }

    public void clear() {
        setMarkIndex(-1);
        setGetIndex(0);
        setPutIndex(this._buffer.getIndex());
        setGetIndex(this._buffer.getIndex());
    }

    public void compact() {
    }

    public boolean equals(Object obj) {
        return this == obj || (((obj instanceof Buffer) && ((Buffer) obj).equals(this)) || super.equals(obj));
    }

    public boolean isReadOnly() {
        return this._buffer.isReadOnly();
    }

    public boolean isVolatile() {
        return true;
    }

    public byte peek(int i) {
        return this._buffer.peek(i);
    }

    public int peek(int i, byte[] bArr, int i2, int i3) {
        return this._buffer.peek(i, bArr, i2, i3);
    }

    public Buffer peek(int i, int i2) {
        return this._buffer.peek(i, i2);
    }

    public int poke(int i, Buffer buffer) {
        return this._buffer.poke(i, buffer);
    }

    public int poke(int i, byte[] bArr, int i2, int i3) {
        return this._buffer.poke(i, bArr, i2, i3);
    }

    public void poke(int i, byte b) {
        this._buffer.poke(i, b);
    }

    public String toString() {
        return this._buffer == null ? "INVALID" : super.toString();
    }

    public void update(int i, int i2) {
        int i3 = this._access;
        this._access = 2;
        setGetIndex(0);
        setPutIndex(i2);
        setGetIndex(i);
        setMarkIndex(-1);
        this._access = i3;
    }

    public void update(Buffer buffer) {
        int i = 2;
        this._access = 2;
        this._buffer = buffer.buffer();
        setGetIndex(0);
        setPutIndex(buffer.putIndex());
        setGetIndex(buffer.getIndex());
        setMarkIndex(buffer.markIndex());
        if (buffer.isReadOnly()) {
            i = 1;
        }
        this._access = i;
    }
}
