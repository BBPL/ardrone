package org.mortbay.util;

import org.apache.sanselan.formats.jpeg.JpegConstants;

public class Utf8StringBuffer {
    int _bits;
    StringBuffer _buffer;
    boolean _errors;
    int _more;

    public Utf8StringBuffer() {
        this._buffer = new StringBuffer();
    }

    public Utf8StringBuffer(int i) {
        this._buffer = new StringBuffer(i);
    }

    public void append(byte b) {
        if (b >= (byte) 0) {
            if (this._more > 0) {
                this._buffer.append('?');
                this._more = 0;
                this._bits = 0;
                return;
            }
            this._buffer.append((char) (b & 127));
        } else if (this._more == 0) {
            if ((b & 192) != 192) {
                this._buffer.append('?');
                this._more = 0;
                this._bits = 0;
            } else if ((b & JpegConstants.JPEG_APP0) == 192) {
                this._more = 1;
                this._bits = b & 31;
            } else if ((b & 240) == JpegConstants.JPEG_APP0) {
                this._more = 2;
                this._bits = b & 15;
            } else if ((b & 248) == 240) {
                this._more = 3;
                this._bits = b & 7;
            } else if ((b & 252) == 248) {
                this._more = 4;
                this._bits = b & 3;
            } else if ((b & 254) == 252) {
                this._more = 5;
                this._bits = b & 1;
            }
        } else if ((b & 192) == 192) {
            this._buffer.append('?');
            this._more = 0;
            this._bits = 0;
            this._errors = true;
        } else {
            this._bits = (this._bits << 6) | (b & 63);
            int i = this._more - 1;
            this._more = i;
            if (i == 0) {
                this._buffer.append((char) this._bits);
            }
        }
    }

    public void append(byte[] bArr, int i, int i2) {
        for (int i3 = i; i3 < i + i2; i3++) {
            append(bArr[i3]);
        }
    }

    public StringBuffer getStringBuffer() {
        return this._buffer;
    }

    public boolean isError() {
        return this._errors || this._more > 0;
    }

    public int length() {
        return this._buffer.length();
    }

    public void reset() {
        this._buffer.setLength(0);
        this._more = 0;
        this._bits = 0;
        this._errors = false;
    }

    public String toString() {
        return this._buffer.toString();
    }
}
