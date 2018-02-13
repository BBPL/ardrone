package org.mortbay.util;

import com.google.common.base.Ascii;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ByteArrayISO8859Writer extends Writer {
    private ByteArrayOutputStream2 _bout;
    private byte[] _buf;
    private boolean _fixed;
    private int _size;
    private OutputStreamWriter _writer;

    public ByteArrayISO8859Writer() {
        this._bout = null;
        this._writer = null;
        this._fixed = false;
        this._buf = new byte[2048];
    }

    public ByteArrayISO8859Writer(int i) {
        this._bout = null;
        this._writer = null;
        this._fixed = false;
        this._buf = new byte[i];
    }

    public ByteArrayISO8859Writer(byte[] bArr) {
        this._bout = null;
        this._writer = null;
        this._fixed = false;
        this._buf = bArr;
        this._fixed = true;
    }

    private void writeEncoded(char[] cArr, int i, int i2) throws IOException {
        if (this._bout == null) {
            this._bout = new ByteArrayOutputStream2(i2 * 2);
            this._writer = new OutputStreamWriter(this._bout, StringUtil.__ISO_8859_1);
        } else {
            this._bout.reset();
        }
        this._writer.write(cArr, i, i2);
        this._writer.flush();
        ensureSpareCapacity(this._bout.getCount());
        System.arraycopy(this._bout.getBuf(), 0, this._buf, this._size, this._bout.getCount());
        this._size += this._bout.getCount();
    }

    public int capacity() {
        return this._buf.length;
    }

    public void close() {
    }

    public void destroy() {
        this._buf = null;
    }

    public void ensureSpareCapacity(int i) throws IOException {
        if (this._size + i <= this._buf.length) {
            return;
        }
        if (this._fixed) {
            throw new IOException(new StringBuffer().append("Buffer overflow: ").append(this._buf.length).toString());
        }
        Object obj = new byte[(((this._buf.length + i) * 4) / 3)];
        System.arraycopy(this._buf, 0, obj, 0, this._size);
        this._buf = obj;
    }

    public void flush() {
    }

    public byte[] getBuf() {
        return this._buf;
    }

    public byte[] getByteArray() {
        Object obj = new byte[this._size];
        System.arraycopy(this._buf, 0, obj, 0, this._size);
        return obj;
    }

    public Object getLock() {
        return this.lock;
    }

    public void resetWriter() {
        this._size = 0;
    }

    public void setLength(int i) {
        this._size = i;
    }

    public int size() {
        return this._size;
    }

    public int spareCapacity() {
        return this._buf.length - this._size;
    }

    public void write(char c) throws IOException {
        ensureSpareCapacity(1);
        if (c < '\u0000' || c > Ascii.MAX) {
            writeEncoded(new char[]{c}, 0, 1);
            return;
        }
        byte[] bArr = this._buf;
        int i = this._size;
        this._size = i + 1;
        bArr[i] = (byte) c;
    }

    public void write(String str) throws IOException {
        int i = 0;
        if (str == null) {
            write("null", 0, 4);
            return;
        }
        int length = str.length();
        ensureSpareCapacity(length);
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt < '\u0000' || charAt > Ascii.MAX) {
                writeEncoded(str.toCharArray(), i, length - i);
                return;
            }
            byte[] bArr = this._buf;
            int i2 = this._size;
            this._size = i2 + 1;
            bArr[i2] = (byte) charAt;
            i++;
        }
    }

    public void write(String str, int i, int i2) throws IOException {
        ensureSpareCapacity(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            char charAt = str.charAt(i + i3);
            if (charAt < '\u0000' || charAt > Ascii.MAX) {
                writeEncoded(str.toCharArray(), i + i3, i2 - i3);
                return;
            }
            byte[] bArr = this._buf;
            int i4 = this._size;
            this._size = i4 + 1;
            bArr[i4] = (byte) charAt;
        }
    }

    public void write(char[] cArr) throws IOException {
        ensureSpareCapacity(cArr.length);
        for (int i = 0; i < cArr.length; i++) {
            char c = cArr[i];
            if (c < '\u0000' || c > Ascii.MAX) {
                writeEncoded(cArr, i, cArr.length - i);
                return;
            }
            byte[] bArr = this._buf;
            int i2 = this._size;
            this._size = i2 + 1;
            bArr[i2] = (byte) c;
        }
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        ensureSpareCapacity(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            char c = cArr[i + i3];
            if (c < '\u0000' || c > Ascii.MAX) {
                writeEncoded(cArr, i + i3, i2 - i3);
                return;
            }
            byte[] bArr = this._buf;
            int i4 = this._size;
            this._size = i4 + 1;
            bArr[i4] = (byte) c;
        }
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this._buf, 0, this._size);
    }
}
