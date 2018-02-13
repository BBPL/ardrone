package org.mortbay.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mortbay.io.Buffer.CaseInsensitve;
import org.mortbay.io.ByteArrayBuffer.CaseInsensitive;

public abstract class AbstractBuffer implements Buffer {
    static final boolean $assertionsDisabled;
    protected static final String __IMMUTABLE = "IMMUTABLE";
    protected static final String __READONLY = "READONLY";
    protected static final String __READWRITE = "READWRITE";
    protected static final String __VOLATILE = "VOLATILE";
    static Class class$org$mortbay$io$AbstractBuffer;
    protected int _access;
    protected int _get;
    protected int _hash;
    protected int _hashGet;
    protected int _hashPut;
    protected int _mark;
    protected int _put;
    protected String _string;
    protected View _view;
    protected boolean _volatile;

    static {
        Class class$;
        if (class$org$mortbay$io$AbstractBuffer == null) {
            class$ = class$("org.mortbay.io.AbstractBuffer");
            class$org$mortbay$io$AbstractBuffer = class$;
        } else {
            class$ = class$org$mortbay$io$AbstractBuffer;
        }
        $assertionsDisabled = !class$.desiredAssertionStatus();
    }

    public AbstractBuffer(int i, boolean z) {
        if (i == 0 && z) {
            throw new IllegalArgumentException("IMMUTABLE && VOLATILE");
        }
        setMarkIndex(-1);
        this._access = i;
        this._volatile = z;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public byte[] asArray() {
        byte[] bArr = new byte[length()];
        byte[] array = array();
        if (array != null) {
            Portable.arraycopy(array, getIndex(), bArr, 0, bArr.length);
        } else {
            peek(getIndex(), bArr, 0, length());
        }
        return bArr;
    }

    public Buffer asImmutableBuffer() {
        return isImmutable() ? this : duplicate(0);
    }

    public Buffer asMutableBuffer() {
        if (!isImmutable()) {
            return this;
        }
        Buffer buffer = buffer();
        return buffer.isReadOnly() ? duplicate(2) : new View(buffer, markIndex(), getIndex(), putIndex(), this._access);
    }

    public Buffer asNonVolatileBuffer() {
        return !isVolatile() ? this : duplicate(this._access);
    }

    public Buffer asReadOnlyBuffer() {
        return isReadOnly() ? this : new View(this, markIndex(), getIndex(), putIndex(), 1);
    }

    public Buffer buffer() {
        return this;
    }

    public void clear() {
        setMarkIndex(-1);
        setGetIndex(0);
        setPutIndex(0);
    }

    public void compact() {
        if (isReadOnly()) {
            throw new IllegalStateException(__READONLY);
        }
        int markIndex = markIndex() >= 0 ? markIndex() : getIndex();
        if (markIndex > 0) {
            byte[] array = array();
            int putIndex = putIndex() - markIndex;
            if (putIndex > 0) {
                if (array != null) {
                    Portable.arraycopy(array(), markIndex, array(), 0, putIndex);
                } else {
                    poke(0, peek(markIndex, putIndex));
                }
            }
            if (markIndex() > 0) {
                setMarkIndex(markIndex() - markIndex);
            }
            setGetIndex(getIndex() - markIndex);
            setPutIndex(putIndex() - markIndex);
        }
    }

    public ByteArrayBuffer duplicate(int i) {
        return buffer() instanceof CaseInsensitve ? new CaseInsensitive(asArray(), 0, length(), i) : new ByteArrayBuffer(asArray(), 0, length(), i);
    }

    public boolean equals(Object obj) {
        if (obj != this) {
            if (obj != null && (obj instanceof Buffer)) {
                Buffer buffer = (Buffer) obj;
                if (!(this instanceof CaseInsensitve) && !(buffer instanceof CaseInsensitve)) {
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
                            if (peek(putIndex) != buffer.peek(putIndex2)) {
                                return false;
                            }
                            i = putIndex2;
                            putIndex2 = putIndex;
                        }
                    } else {
                        return false;
                    }
                }
                return equalsIgnoreCase(buffer);
            }
            return false;
        }
        return true;
    }

    public boolean equalsIgnoreCase(Buffer buffer) {
        if (buffer == this) {
            return true;
        }
        if (buffer.length() != length()) {
            return false;
        }
        if (this._hash != 0 && (buffer instanceof AbstractBuffer)) {
            AbstractBuffer abstractBuffer = (AbstractBuffer) buffer;
            if (!(abstractBuffer._hash == 0 || this._hash == abstractBuffer._hash)) {
                return false;
            }
        }
        int index = getIndex();
        int putIndex = buffer.putIndex();
        byte[] array = array();
        byte[] array2 = buffer.array();
        int putIndex2;
        int i;
        int i2;
        byte b;
        byte b2;
        if (array != null && array2 != null) {
            putIndex2 = putIndex();
            i = putIndex;
            while (true) {
                i2 = putIndex2 - 1;
                if (putIndex2 <= index) {
                    break;
                }
                b = array[i2];
                i--;
                b2 = array2[i];
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
                b = peek(i2);
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
        return true;
    }

    public byte get() {
        int i = this._get;
        this._get = i + 1;
        return peek(i);
    }

    public int get(byte[] bArr, int i, int i2) {
        int index = getIndex();
        int length = length();
        if (length == 0) {
            return -1;
        }
        if (i2 > length) {
            i2 = length;
        }
        length = peek(index, bArr, i, i2);
        if (length <= 0) {
            return length;
        }
        setGetIndex(index + length);
        return length;
    }

    public Buffer get(int i) {
        int index = getIndex();
        Buffer peek = peek(index, i);
        setGetIndex(index + i);
        return peek;
    }

    public final int getIndex() {
        return this._get;
    }

    public boolean hasContent() {
        return this._put > this._get;
    }

    public int hashCode() {
        if (!(this._hash != 0 && this._hashGet == this._get && this._hashPut == this._put)) {
            int index = getIndex();
            byte[] array = array();
            int putIndex;
            int i;
            if (array != null) {
                putIndex = putIndex();
                while (true) {
                    i = putIndex - 1;
                    if (putIndex <= index) {
                        break;
                    }
                    putIndex = array[i];
                    if (97 <= putIndex && putIndex <= 122) {
                        putIndex = (byte) ((putIndex - 97) + 65);
                    }
                    this._hash = putIndex + (this._hash * 31);
                    putIndex = i;
                }
            } else {
                putIndex = putIndex();
                while (true) {
                    i = putIndex - 1;
                    if (putIndex <= index) {
                        break;
                    }
                    putIndex = peek(i);
                    if (97 <= putIndex && putIndex <= 122) {
                        putIndex = (byte) ((putIndex - 97) + 65);
                    }
                    this._hash = putIndex + (this._hash * 31);
                    putIndex = i;
                }
            }
            if (this._hash == 0) {
                this._hash = -1;
            }
            this._hashGet = this._get;
            this._hashPut = this._put;
        }
        return this._hash;
    }

    public boolean isImmutable() {
        return this._access <= 0;
    }

    public boolean isReadOnly() {
        return this._access <= 1;
    }

    public boolean isVolatile() {
        return this._volatile;
    }

    public int length() {
        return this._put - this._get;
    }

    public void mark() {
        setMarkIndex(this._get - 1);
    }

    public void mark(int i) {
        setMarkIndex(this._get + i);
    }

    public int markIndex() {
        return this._mark;
    }

    public byte peek() {
        return peek(this._get);
    }

    public Buffer peek(int i, int i2) {
        if (this._view == null) {
            this._view = new View(this, -1, i, i + i2, isReadOnly() ? 1 : 2);
        } else {
            this._view.update(buffer());
            this._view.setMarkIndex(-1);
            this._view.setGetIndex(0);
            this._view.setPutIndex(i + i2);
            this._view.setGetIndex(i);
        }
        return this._view;
    }

    public int poke(int i, Buffer buffer) {
        int i2 = 0;
        this._hash = 0;
        int length = buffer.length();
        if (i + length > capacity()) {
            length = capacity() - i;
        }
        byte[] array = buffer.array();
        byte[] array2 = array();
        if (array != null && array2 != null) {
            Portable.arraycopy(array, buffer.getIndex(), array2, i, length);
        } else if (array != null) {
            r2 = buffer.getIndex();
            while (i2 < length) {
                poke(i, array[r2]);
                i2++;
                r2++;
                i++;
            }
        } else if (array2 != null) {
            r2 = buffer.getIndex();
            while (i2 < length) {
                array2[i] = buffer.peek(r2);
                i2++;
                r2++;
                i++;
            }
        } else {
            r2 = buffer.getIndex();
            while (i2 < length) {
                poke(i, buffer.peek(r2));
                i2++;
                r2++;
                i++;
            }
        }
        return length;
    }

    public int poke(int i, byte[] bArr, int i2, int i3) {
        int i4 = 0;
        this._hash = 0;
        if (i + i3 > capacity()) {
            i3 = capacity() - i;
        }
        byte[] array = array();
        if (array != null) {
            Portable.arraycopy(bArr, i2, array, i, i3);
        } else {
            while (i4 < i3) {
                poke(i, bArr[i2]);
                i4++;
                i2++;
                i++;
            }
        }
        return i3;
    }

    public int put(Buffer buffer) {
        int putIndex = putIndex();
        int poke = poke(putIndex, buffer);
        setPutIndex(putIndex + poke);
        return poke;
    }

    public int put(byte[] bArr) {
        int putIndex = putIndex();
        int poke = poke(putIndex, bArr, 0, bArr.length);
        setPutIndex(putIndex + poke);
        return poke;
    }

    public int put(byte[] bArr, int i, int i2) {
        int putIndex = putIndex();
        int poke = poke(putIndex, bArr, i, i2);
        setPutIndex(putIndex + poke);
        return poke;
    }

    public void put(byte b) {
        int putIndex = putIndex();
        poke(putIndex, b);
        setPutIndex(putIndex + 1);
    }

    public final int putIndex() {
        return this._put;
    }

    public int readFrom(InputStream inputStream, int i) throws IOException {
        int i2 = 1024;
        byte[] array = array();
        int space = space();
        if (space > i) {
            space = i;
        }
        if (array != null) {
            int read = inputStream.read(array, this._put, space);
            if (read <= 0) {
                return read;
            }
            this._put += read;
            return read;
        }
        if (space <= 1024) {
            i2 = space;
        }
        byte[] bArr = new byte[i2];
        while (space > 0) {
            int read2 = inputStream.read(bArr, 0, bArr.length);
            if (read2 < 0) {
                return -1;
            }
            int put = put(bArr, 0, read2);
            if ($assertionsDisabled || read2 == put) {
                space -= read2;
            } else {
                throw new AssertionError();
            }
        }
        return 0;
    }

    public void reset() {
        if (markIndex() >= 0) {
            setGetIndex(markIndex());
        }
    }

    public void rewind() {
        setGetIndex(0);
        setMarkIndex(-1);
    }

    public void setGetIndex(int i) {
        this._get = i;
        this._hash = 0;
    }

    public void setMarkIndex(int i) {
        this._mark = i;
    }

    public void setPutIndex(int i) {
        this._put = i;
        this._hash = 0;
    }

    public int skip(int i) {
        if (length() < i) {
            i = length();
        }
        setGetIndex(getIndex() + i);
        return i;
    }

    public Buffer slice() {
        return peek(getIndex(), length());
    }

    public Buffer sliceFromMark() {
        return sliceFromMark((getIndex() - markIndex()) - 1);
    }

    public Buffer sliceFromMark(int i) {
        if (markIndex() < 0) {
            return null;
        }
        Buffer peek = peek(markIndex(), i);
        setMarkIndex(-1);
        return peek;
    }

    public int space() {
        return capacity() - this._put;
    }

    public String toDebugString() {
        return new StringBuffer().append(getClass()).append("@").append(super.hashCode()).toString();
    }

    public String toDetailString() {
        int markIndex;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        stringBuffer.append(super.hashCode());
        stringBuffer.append(",");
        stringBuffer.append(array().hashCode());
        stringBuffer.append(",m=");
        stringBuffer.append(markIndex());
        stringBuffer.append(",g=");
        stringBuffer.append(getIndex());
        stringBuffer.append(",p=");
        stringBuffer.append(putIndex());
        stringBuffer.append(",c=");
        stringBuffer.append(capacity());
        stringBuffer.append("]={");
        if (markIndex() >= 0) {
            for (markIndex = markIndex(); markIndex < getIndex(); markIndex++) {
                char peek = (char) peek(markIndex);
                if (Character.isISOControl(peek)) {
                    stringBuffer.append(peek < '\u0010' ? "\\0" : "\\");
                    stringBuffer.append(Integer.toString(peek, 16));
                } else {
                    stringBuffer.append(peek);
                }
            }
            stringBuffer.append("}{");
        }
        markIndex = 0;
        int index = getIndex();
        while (index < putIndex()) {
            char peek2 = (char) peek(index);
            if (Character.isISOControl(peek2)) {
                stringBuffer.append(peek2 < '\u0010' ? "\\0" : "\\");
                stringBuffer.append(Integer.toString(peek2, 16));
            } else {
                stringBuffer.append(peek2);
            }
            if (markIndex == 50 && putIndex() - index > 20) {
                stringBuffer.append(" ... ");
                index = putIndex() - 20;
            }
            index++;
            markIndex++;
        }
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    public String toString() {
        if (!isImmutable()) {
            return new String(asArray(), 0, length());
        }
        if (this._string == null) {
            this._string = new String(asArray(), 0, length());
        }
        return this._string;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        int i = 1024;
        byte[] array = array();
        if (array != null) {
            outputStream.write(array, getIndex(), length());
        } else {
            int length = length();
            if (length <= 1024) {
                i = length;
            }
            byte[] bArr = new byte[i];
            int i2 = this._get;
            while (length > 0) {
                int peek = peek(i2, bArr, 0, length > bArr.length ? bArr.length : length);
                outputStream.write(bArr, 0, peek);
                length -= peek;
                i2 += peek;
            }
        }
        clear();
    }
}
