package org.mortbay.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.mortbay.io.Buffer.CaseInsensitve;

public class ByteArrayBuffer extends AbstractBuffer {
    protected byte[] _bytes;

    public static class CaseInsensitive extends ByteArrayBuffer implements CaseInsensitve {
        public CaseInsensitive(String str) {
            super(str);
        }

        public CaseInsensitive(byte[] bArr, int i, int i2, int i3) {
            super(bArr, i, i2, i3);
        }

        public boolean equals(Object obj) {
            return equalsIgnoreCase((Buffer) obj);
        }
    }

    public ByteArrayBuffer(int i) {
        this(new byte[i], 0, i, 2);
        setPutIndex(0);
    }

    protected ByteArrayBuffer(int i, boolean z) {
        super(i, z);
    }

    public ByteArrayBuffer(String str) {
        super(2, false);
        this._bytes = Portable.getBytes(str);
        setGetIndex(0);
        setPutIndex(this._bytes.length);
        this._access = 0;
        this._string = str;
    }

    public ByteArrayBuffer(String str, String str2) throws UnsupportedEncodingException {
        super(2, false);
        this._bytes = str.getBytes(str2);
        setGetIndex(0);
        setPutIndex(this._bytes.length);
        this._access = 0;
        this._string = str;
    }

    public ByteArrayBuffer(byte[] bArr) {
        this(bArr, 0, bArr.length, 2);
    }

    public ByteArrayBuffer(byte[] bArr, int i, int i2) {
        this(bArr, i, i2, 2);
    }

    public ByteArrayBuffer(byte[] bArr, int i, int i2, int i3) {
        super(2, false);
        this._bytes = bArr;
        setPutIndex(i + i2);
        setGetIndex(i);
        this._access = i3;
    }

    public ByteArrayBuffer(byte[] bArr, int i, int i2, int i3, boolean z) {
        super(2, z);
        this._bytes = bArr;
        setPutIndex(i + i2);
        setGetIndex(i);
        this._access = i3;
    }

    public byte[] array() {
        return this._bytes;
    }

    public int capacity() {
        return this._bytes.length;
    }

    public void compact() {
        if (isReadOnly()) {
            throw new IllegalStateException("READONLY");
        }
        int markIndex = markIndex() >= 0 ? markIndex() : getIndex();
        if (markIndex > 0) {
            int putIndex = putIndex() - markIndex;
            if (putIndex > 0) {
                Portable.arraycopy(this._bytes, markIndex, this._bytes, 0, putIndex);
            }
            if (markIndex() > 0) {
                setMarkIndex(markIndex() - markIndex);
            }
            setGetIndex(getIndex() - markIndex);
            setPutIndex(putIndex() - markIndex);
        }
    }

    public boolean equals(Object obj) {
        if (obj != this) {
            if (obj != null && (obj instanceof Buffer)) {
                if (!(obj instanceof CaseInsensitve)) {
                    Buffer buffer = (Buffer) obj;
                    if (buffer.length() == length()) {
                        if (this._hash != 0 && (obj instanceof AbstractBuffer)) {
                            AbstractBuffer abstractBuffer = (AbstractBuffer) obj;
                            if (!(abstractBuffer._hash == 0 || this._hash == abstractBuffer._hash)) {
                                return false;
                            }
                        }
                        int index = getIndex();
                        int putIndex = buffer.putIndex();
                        int putIndex2 = putIndex();
                        int i = putIndex;
                        while (true) {
                            putIndex = putIndex2 - 1;
                            if (putIndex2 <= index) {
                                break;
                            }
                            putIndex2 = i - 1;
                            if (this._bytes[putIndex] != buffer.peek(putIndex2)) {
                                return false;
                            }
                            i = putIndex2;
                            putIndex2 = putIndex;
                        }
                    } else {
                        return false;
                    }
                }
                return equalsIgnoreCase((Buffer) obj);
            }
            return false;
        }
        return true;
    }

    public boolean equalsIgnoreCase(Buffer buffer) {
        if (buffer != this) {
            if (buffer != null && buffer.length() == length()) {
                if (this._hash != 0 && (buffer instanceof AbstractBuffer)) {
                    AbstractBuffer abstractBuffer = (AbstractBuffer) buffer;
                    if (!(abstractBuffer._hash == 0 || this._hash == abstractBuffer._hash)) {
                        return false;
                    }
                }
                int index = getIndex();
                int putIndex = buffer.putIndex();
                byte[] array = buffer.array();
                int putIndex2;
                int i;
                int i2;
                byte b;
                byte b2;
                if (array != null) {
                    putIndex2 = putIndex();
                    i = putIndex;
                    while (true) {
                        i2 = putIndex2 - 1;
                        if (putIndex2 <= index) {
                            break;
                        }
                        b = this._bytes[i2];
                        i--;
                        b2 = array[i];
                        if (b != b2) {
                            if ((byte) 97 <= b && b <= (byte) 122) {
                                b = (byte) ((b - 97) + 65);
                            }
                            if ((byte) 97 <= b2 && b2 <= (byte) 122) {
                                b2 = (byte) ((b2 - 97) + 65);
                            }
                            if (b != b2) {
                                return false;
                            }
                        }
                        putIndex2 = i2;
                    }
                } else {
                    putIndex2 = putIndex();
                    i = putIndex;
                    while (true) {
                        i2 = putIndex2 - 1;
                        if (putIndex2 <= index) {
                            break;
                        }
                        b = this._bytes[i2];
                        i--;
                        b2 = buffer.peek(i);
                        if (b != b2) {
                            if ((byte) 97 <= b && b <= (byte) 122) {
                                b = (byte) ((b - 97) + 65);
                            }
                            if ((byte) 97 <= b2 && b2 <= (byte) 122) {
                                b2 = (byte) ((b2 - 97) + 65);
                            }
                            if (b != b2) {
                                return false;
                            }
                        }
                        putIndex2 = i2;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public byte get() {
        byte[] bArr = this._bytes;
        int i = this._get;
        this._get = i + 1;
        return bArr[i];
    }

    public int hashCode() {
        if (!(this._hash != 0 && this._hashGet == this._get && this._hashPut == this._put)) {
            int index = getIndex();
            int putIndex = putIndex();
            while (true) {
                int i = putIndex - 1;
                if (putIndex <= index) {
                    break;
                }
                putIndex = this._bytes[i];
                if (97 <= putIndex && putIndex <= 122) {
                    putIndex = (byte) ((putIndex - 97) + 65);
                }
                this._hash = putIndex + (this._hash * 31);
                putIndex = i;
            }
            if (this._hash == 0) {
                this._hash = -1;
            }
            this._hashGet = this._get;
            this._hashPut = this._put;
        }
        return this._hash;
    }

    public byte peek(int i) {
        return this._bytes[i];
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int peek(int r3, byte[] r4, int r5, int r6) {
        /*
        r2 = this;
        r0 = r3 + r6;
        r1 = r2.capacity();
        if (r0 <= r1) goto L_0x0011;
    L_0x0008:
        r0 = r2.capacity();
        r0 = r0 - r3;
        if (r0 != 0) goto L_0x0012;
    L_0x000f:
        r0 = -1;
    L_0x0010:
        return r0;
    L_0x0011:
        r0 = r6;
    L_0x0012:
        if (r0 < 0) goto L_0x000f;
    L_0x0014:
        r1 = r2._bytes;
        org.mortbay.io.Portable.arraycopy(r1, r3, r4, r5, r0);
        goto L_0x0010;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.io.ByteArrayBuffer.peek(int, byte[], int, int):int");
    }

    public int poke(int i, Buffer buffer) {
        int i2 = 0;
        this._hash = 0;
        int length = buffer.length();
        if (i + length > capacity()) {
            length = capacity() - i;
        }
        byte[] array = buffer.array();
        if (array != null) {
            Portable.arraycopy(array, buffer.getIndex(), this._bytes, i, length);
        } else if (array != null) {
            r2 = buffer.getIndex();
            while (i2 < length) {
                poke(i, array[r2]);
                i2++;
                r2++;
                i++;
            }
        } else {
            r2 = buffer.getIndex();
            while (i2 < length) {
                this._bytes[i] = buffer.peek(r2);
                i2++;
                r2++;
                i++;
            }
        }
        return length;
    }

    public int poke(int i, byte[] bArr, int i2, int i3) {
        this._hash = 0;
        if (i + i3 > capacity()) {
            i3 = capacity() - i;
        }
        Portable.arraycopy(bArr, i2, this._bytes, i, i3);
        return i3;
    }

    public void poke(int i, byte b) {
        this._bytes[i] = b;
    }

    public int readFrom(InputStream inputStream, int i) throws IOException {
        int i2 = 0;
        if (i < 0 || i > space()) {
            i = space();
        }
        int putIndex = putIndex();
        int i3 = i;
        int i4 = 0;
        while (i4 < i) {
            i2 = inputStream.read(this._bytes, putIndex, i3);
            if (i2 >= 0) {
                if (i2 > 0) {
                    putIndex += i2;
                    i4 += i2;
                    i3 -= i2;
                    setPutIndex(putIndex);
                }
                if (inputStream.available() <= 0) {
                    i3 = i2;
                    putIndex = i4;
                    break;
                }
            }
            i3 = i2;
            putIndex = i4;
            break;
        }
        i3 = i2;
        putIndex = i4;
        return (i3 >= 0 || putIndex != 0) ? putIndex : -1;
    }

    public int space() {
        return this._bytes.length - this._put;
    }

    public void wrap(byte[] bArr) {
        if (isReadOnly()) {
            throw new IllegalStateException("READONLY");
        } else if (isImmutable()) {
            throw new IllegalStateException("IMMUTABLE");
        } else {
            this._bytes = bArr;
            setGetIndex(0);
            setPutIndex(bArr.length);
        }
    }

    public void wrap(byte[] bArr, int i, int i2) {
        if (isReadOnly()) {
            throw new IllegalStateException("READONLY");
        } else if (isImmutable()) {
            throw new IllegalStateException("IMMUTABLE");
        } else {
            this._bytes = bArr;
            clear();
            setGetIndex(i);
            setPutIndex(i + i2);
        }
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this._bytes, getIndex(), length());
        clear();
    }
}
