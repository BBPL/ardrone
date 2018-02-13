package org.mortbay.jetty;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import javax.servlet.ServletOutputStream;
import org.apache.sanselan.formats.jpeg.JpegConstants;
import org.mortbay.io.Buffer;
import org.mortbay.io.Buffers;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.io.EndPoint;
import org.mortbay.io.View;
import org.mortbay.log.Log;
import org.mortbay.util.ByteArrayOutputStream2;
import org.mortbay.util.StringUtil;
import org.mortbay.util.TypeUtil;

public abstract class AbstractGenerator implements Generator {
    private static int MAX_OUTPUT_CHARS = 512;
    private static final byte[] NO_BYTES = new byte[0];
    public static final int STATE_CONTENT = 2;
    public static final int STATE_END = 4;
    public static final int STATE_FLUSHING = 3;
    public static final int STATE_HEADER = 0;
    private static Buffer[] __reasons = new Buffer[505];
    static Class class$javax$servlet$http$HttpServletResponse;
    protected Buffer _buffer;
    protected Buffers _buffers;
    protected boolean _close = false;
    protected Buffer _content;
    protected int _contentBufferSize;
    protected long _contentLength = -3;
    protected long _contentWritten = 0;
    protected EndPoint _endp;
    protected boolean _head = false;
    protected Buffer _header;
    protected int _headerBufferSize;
    protected boolean _last = false;
    protected Buffer _method;
    protected boolean _noContent = false;
    protected Buffer _reason;
    private boolean _sendServerVersion;
    protected int _state = 0;
    protected int _status = 0;
    protected String _uri;
    protected int _version = 11;

    public static class Output extends ServletOutputStream {
        protected ByteArrayBuffer _buf = new ByteArrayBuffer(AbstractGenerator.access$000());
        ByteArrayOutputStream2 _bytes;
        String _characterEncoding;
        char[] _chars;
        protected boolean _closed;
        Writer _converter;
        protected AbstractGenerator _generator;
        protected long _maxIdleTime;

        public Output(AbstractGenerator abstractGenerator, long j) {
            this._generator = abstractGenerator;
            this._maxIdleTime = j;
        }

        private void write(Buffer buffer) throws IOException {
            if (this._closed) {
                throw new IOException("Closed");
            } else if (this._generator._endp.isOpen()) {
                while (this._generator.isBufferFull()) {
                    blockForOutput();
                    if (this._closed) {
                        throw new IOException("Closed");
                    } else if (!this._generator._endp.isOpen()) {
                        throw new EofException();
                    }
                }
                this._generator.addContent(buffer, false);
                if (this._generator.isBufferFull()) {
                    flush();
                }
                if (this._generator.isContentWritten()) {
                    flush();
                    close();
                }
                while (buffer.length() > 0 && this._generator._endp.isOpen()) {
                    blockForOutput();
                }
            } else {
                throw new EofException();
            }
        }

        void blockForOutput() throws IOException {
            if (this._generator._endp.isBlocking()) {
                try {
                    flush();
                } catch (IOException e) {
                    this._generator._endp.close();
                    throw e;
                }
            } else if (this._generator._endp.blockWritable(this._maxIdleTime)) {
                this._generator.flush();
            } else {
                this._generator._endp.close();
                throw new EofException("timeout");
            }
        }

        public void close() throws IOException {
            this._closed = true;
        }

        public void flush() throws IOException {
            Buffer buffer = this._generator._content;
            Buffer buffer2 = this._generator._buffer;
            if ((buffer != null && buffer.length() > 0) || ((buffer2 != null && buffer2.length() > 0) || this._generator.isBufferFull())) {
                this._generator.flush();
                while (true) {
                    if (((buffer != null && buffer.length() > 0) || (buffer2 != null && buffer2.length() > 0)) && this._generator._endp.isOpen()) {
                        blockForOutput();
                    } else {
                        return;
                    }
                }
            }
        }

        public void print(String str) throws IOException {
            write(str.getBytes());
        }

        void reopen() {
            this._closed = false;
        }

        public void write(int i) throws IOException {
            if (this._closed) {
                throw new IOException("Closed");
            } else if (this._generator._endp.isOpen()) {
                while (this._generator.isBufferFull()) {
                    blockForOutput();
                    if (this._closed) {
                        throw new IOException("Closed");
                    } else if (!this._generator._endp.isOpen()) {
                        throw new EofException();
                    }
                }
                if (this._generator.addContent((byte) i)) {
                    flush();
                }
                if (this._generator.isContentWritten()) {
                    flush();
                    close();
                }
            } else {
                throw new EofException();
            }
        }

        public void write(byte[] bArr) throws IOException {
            this._buf.wrap(bArr);
            write(this._buf);
            this._buf.wrap(AbstractGenerator.access$000());
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            this._buf.wrap(bArr, i, i2);
            write(this._buf);
            this._buf.wrap(AbstractGenerator.access$000());
        }
    }

    public static class OutputWriter extends Writer {
        private static final int WRITE_CONV = 0;
        private static final int WRITE_ISO1 = 1;
        private static final int WRITE_UTF8 = 2;
        AbstractGenerator _generator = this._out._generator;
        Output _out;
        int _surrogate;
        int _writeMode;

        public OutputWriter(Output output) {
            this._out = output;
        }

        private Writer getConverter() throws IOException {
            if (this._out._converter == null) {
                this._out._converter = new OutputStreamWriter(this._out._bytes, this._out._characterEncoding);
            }
            return this._out._converter;
        }

        public void close() throws IOException {
            this._out.close();
        }

        public void flush() throws IOException {
            this._out.flush();
        }

        public void setCharacterEncoding(String str) {
            if (str == null || StringUtil.__ISO_8859_1.equalsIgnoreCase(str)) {
                this._writeMode = 1;
            } else if ("UTF-8".equalsIgnoreCase(str)) {
                this._writeMode = 2;
            } else {
                this._writeMode = 0;
                if (this._out._characterEncoding == null || !this._out._characterEncoding.equalsIgnoreCase(str)) {
                    this._out._converter = null;
                }
            }
            this._out._characterEncoding = str;
            if (this._out._bytes == null) {
                this._out._bytes = new ByteArrayOutputStream2(AbstractGenerator.access$100());
            }
        }

        public void write(String str, int i, int i2) throws IOException {
            while (i2 > AbstractGenerator.access$100()) {
                write(str, i, AbstractGenerator.access$100());
                i += AbstractGenerator.access$100();
                i2 -= AbstractGenerator.access$100();
            }
            if (this._out._chars == null) {
                this._out._chars = new char[AbstractGenerator.access$100()];
            }
            char[] cArr = this._out._chars;
            str.getChars(i, i + i2, cArr, 0);
            write(cArr, 0, i2);
        }

        public void write(char[] cArr, int i, int i2) throws IOException {
            Output output = this._out;
            int i3 = i2;
            while (i3 > 0) {
                output._bytes.reset();
                int access$100 = i3 > AbstractGenerator.access$100() ? AbstractGenerator.access$100() : i3;
                byte[] buf;
                int count;
                int i4;
                int i5;
                switch (this._writeMode) {
                    case 0:
                        Writer converter = getConverter();
                        converter.write(cArr, i, access$100);
                        converter.flush();
                        break;
                    case 1:
                        buf = output._bytes.getBuf();
                        count = output._bytes.getCount();
                        if (access$100 > buf.length - count) {
                            access$100 = buf.length - count;
                        }
                        i4 = count;
                        i5 = 0;
                        while (i5 < access$100) {
                            count = cArr[i + i5];
                            if (count >= 256) {
                                count = 63;
                            }
                            buf[i4] = (byte) count;
                            i5++;
                            i4++;
                        }
                        if (i4 < 0) {
                            break;
                        }
                        output._bytes.setCount(i4);
                        break;
                    case 2:
                        buf = output._bytes.getBuf();
                        i4 = output._bytes.getCount();
                        if (i4 + access$100 > buf.length) {
                            access$100 = buf.length - i4;
                        }
                        i5 = 0;
                        while (i5 < access$100) {
                            char c = cArr[i + i5];
                            if ((c & -128) != 0) {
                                if ((c & -2048) == 0) {
                                    if (i4 + 2 > buf.length) {
                                        count = i4;
                                        access$100 = i5;
                                        output._bytes.setCount(count);
                                    } else {
                                        count = i4 + 1;
                                        buf[i4] = (byte) ((c >> 6) | 192);
                                        buf[count] = (byte) ((c & 63) | 128);
                                        count++;
                                    }
                                } else if ((-65536 & c) == 0) {
                                    if (i4 + 3 > buf.length) {
                                        count = i4;
                                        access$100 = i5;
                                        output._bytes.setCount(count);
                                    } else {
                                        count = i4 + 1;
                                        buf[i4] = (byte) ((c >> 12) | JpegConstants.JPEG_APP0);
                                        i4 = count + 1;
                                        buf[count] = (byte) (((c >> 6) & 63) | 128);
                                        count = i4 + 1;
                                        buf[i4] = (byte) ((c & 63) | 128);
                                    }
                                } else if ((-14680064 & c) == 0) {
                                    if (i4 + 4 > buf.length) {
                                        count = i4;
                                        access$100 = i5;
                                        output._bytes.setCount(count);
                                    } else {
                                        count = i4 + 1;
                                        buf[i4] = (byte) ((c >> 18) | 240);
                                        i4 = count + 1;
                                        buf[count] = (byte) (((c >> 12) & 63) | 128);
                                        count = i4 + 1;
                                        buf[i4] = (byte) (((c >> 6) & 63) | 128);
                                        buf[count] = (byte) ((c & 63) | 128);
                                        count++;
                                    }
                                } else if ((-201326592 & c) == 0) {
                                    if (i4 + 5 > buf.length) {
                                        count = i4;
                                        access$100 = i5;
                                        output._bytes.setCount(count);
                                    } else {
                                        count = i4 + 1;
                                        buf[i4] = (byte) ((c >> 24) | 248);
                                        i4 = count + 1;
                                        buf[count] = (byte) (((c >> 18) & 63) | 128);
                                        count = i4 + 1;
                                        buf[i4] = (byte) (((c >> 12) & 63) | 128);
                                        i4 = count + 1;
                                        buf[count] = (byte) (((c >> 6) & 63) | 128);
                                        count = i4 + 1;
                                        buf[i4] = (byte) ((c & 63) | 128);
                                    }
                                } else if ((Integer.MIN_VALUE & c) != 0) {
                                    count = i4 + 1;
                                    buf[i4] = (byte) 63;
                                } else if (i4 + 6 > buf.length) {
                                    count = i4;
                                    access$100 = i5;
                                    output._bytes.setCount(count);
                                } else {
                                    count = i4 + 1;
                                    buf[i4] = (byte) ((c >> 30) | 252);
                                    i4 = count + 1;
                                    buf[count] = (byte) (((c >> 24) & 63) | 128);
                                    count = i4 + 1;
                                    buf[i4] = (byte) (((c >> 18) & 63) | 128);
                                    i4 = count + 1;
                                    buf[count] = (byte) (((c >> 12) & 63) | 128);
                                    count = i4 + 1;
                                    buf[i4] = (byte) (((c >> 6) & 63) | 128);
                                    buf[count] = (byte) ((c & 63) | 128);
                                    count++;
                                }
                                if (count == buf.length) {
                                    access$100 = i5 + 1;
                                    output._bytes.setCount(count);
                                }
                            } else if (i4 + 1 > buf.length) {
                                count = i4;
                                access$100 = i5;
                                output._bytes.setCount(count);
                                break;
                            } else {
                                count = i4 + 1;
                                buf[i4] = (byte) c;
                            }
                            i5++;
                            i4 = count;
                        }
                        count = i4;
                        output._bytes.setCount(count);
                    default:
                        throw new IllegalStateException();
                }
                output._bytes.writeTo(output);
                i3 -= access$100;
                i += access$100;
            }
        }
    }

    static {
        Class class$;
        if (class$javax$servlet$http$HttpServletResponse == null) {
            class$ = class$("javax.servlet.http.HttpServletResponse");
            class$javax$servlet$http$HttpServletResponse = class$;
        } else {
            class$ = class$javax$servlet$http$HttpServletResponse;
        }
        Field[] declaredFields = class$.getDeclaredFields();
        int i = 0;
        while (i < declaredFields.length) {
            if ((declaredFields[i].getModifiers() & 8) != 0 && declaredFields[i].getName().startsWith("SC_")) {
                try {
                    int i2 = declaredFields[i].getInt(null);
                    if (i2 < __reasons.length) {
                        __reasons[i2] = new ByteArrayBuffer(declaredFields[i].getName().substring(3));
                    }
                } catch (IllegalAccessException e) {
                }
            }
            i++;
        }
    }

    public AbstractGenerator(Buffers buffers, EndPoint endPoint, int i, int i2) {
        this._buffers = buffers;
        this._endp = endPoint;
        this._headerBufferSize = i;
        this._contentBufferSize = i2;
    }

    static byte[] access$000() {
        return NO_BYTES;
    }

    static int access$100() {
        return MAX_OUTPUT_CHARS;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public static String getReason(int i) {
        Object obj = i < __reasons.length ? __reasons[i] : null;
        return obj == null ? TypeUtil.toString(i) : obj.toString();
    }

    protected static Buffer getReasonBuffer(int i) {
        Buffer buffer = i < __reasons.length ? __reasons[i] : null;
        return buffer == null ? null : buffer;
    }

    public void complete() throws IOException {
        if (this._state == 0) {
            throw new IllegalStateException("State==HEADER");
        } else if (this._contentLength >= 0 && this._contentLength != this._contentWritten && !this._head) {
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("ContentLength written==").append(this._contentWritten).append(" != contentLength==").append(this._contentLength).toString());
            }
            this._close = true;
        }
    }

    public abstract void completeHeader(HttpFields httpFields, boolean z) throws IOException;

    void completeUncheckedAddContent() {
        if (!this._noContent) {
            this._contentWritten += (long) this._buffer.length();
            if (this._head) {
                this._buffer.clear();
            }
        } else if (this._buffer != null) {
            this._buffer.clear();
        }
    }

    public abstract long flush() throws IOException;

    public int getContentBufferSize() {
        return this._contentBufferSize;
    }

    public long getContentWritten() {
        return this._contentWritten;
    }

    public boolean getSendServerVersion() {
        return this._sendServerVersion;
    }

    public int getState() {
        return this._state;
    }

    public Buffer getUncheckedBuffer() {
        return this._buffer;
    }

    public int getVersion() {
        return this._version;
    }

    public void increaseContentBufferSize(int i) {
        if (i > this._contentBufferSize) {
            this._contentBufferSize = i;
            if (this._buffer != null) {
                Buffer buffer = this._buffers.getBuffer(this._contentBufferSize);
                buffer.put(this._buffer);
                this._buffers.returnBuffer(this._buffer);
                this._buffer = buffer;
            }
        }
    }

    public boolean isBufferFull() {
        if (this._buffer != null && this._buffer.space() == 0) {
            if (this._buffer.length() == 0 && !this._buffer.isImmutable()) {
                this._buffer.compact();
            }
            if (this._buffer.space() != 0) {
                return false;
            }
        } else if (this._content == null) {
            return false;
        } else {
            if (this._content.length() <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isCommitted() {
        return this._state != 0;
    }

    public boolean isComplete() {
        return this._state == 4;
    }

    public boolean isContentWritten() {
        return this._contentLength >= 0 && this._contentWritten >= this._contentLength;
    }

    public boolean isHead() {
        return this._head;
    }

    public boolean isIdle() {
        return this._state == 0 && this._method == null && this._status == 0;
    }

    public boolean isPersistent() {
        return !this._close;
    }

    public boolean isState(int i) {
        return this._state == i;
    }

    protected abstract int prepareUncheckedAddContent() throws IOException;

    public void reset(boolean z) {
        this._state = 0;
        this._status = 0;
        this._version = 11;
        this._reason = null;
        this._last = false;
        this._head = false;
        this._noContent = false;
        this._close = false;
        this._contentWritten = 0;
        this._contentLength = -3;
        synchronized (this) {
            if (z) {
                if (this._header != null) {
                    this._buffers.returnBuffer(this._header);
                }
                this._header = null;
                if (this._buffer != null) {
                    this._buffers.returnBuffer(this._buffer);
                }
                this._buffer = null;
            } else {
                if (this._header != null) {
                    this._header.clear();
                }
                if (this._buffer != null) {
                    this._buffers.returnBuffer(this._buffer);
                    this._buffer = null;
                }
            }
        }
        this._content = null;
        this._method = null;
    }

    public void resetBuffer() {
        if (this._state >= 3) {
            throw new IllegalStateException("Flushed");
        }
        this._last = false;
        this._close = false;
        this._contentWritten = 0;
        this._contentLength = -3;
        this._content = null;
        if (this._buffer != null) {
            this._buffer.clear();
        }
    }

    public void sendError(int i, String str, String str2, boolean z) throws IOException {
        if (z) {
            this._close = z;
        }
        if (!isCommitted()) {
            setResponse(i, str);
            completeHeader(null, false);
            if (str2 != null) {
                addContent(new View(new ByteArrayBuffer(str2)), true);
            }
            complete();
        }
    }

    public void setContentLength(long j) {
        if (j < 0) {
            this._contentLength = -3;
        } else {
            this._contentLength = j;
        }
    }

    public void setHead(boolean z) {
        this._head = z;
    }

    public void setPersistent(boolean z) {
        this._close = !z;
    }

    public void setRequest(String str, String str2) {
        if (str == null || "GET".equals(str)) {
            this._method = HttpMethods.GET_BUFFER;
        } else {
            this._method = HttpMethods.CACHE.lookup(str);
        }
        this._uri = str2;
        if (this._version == 9) {
            this._noContent = true;
        }
    }

    public void setResponse(int i, String str) {
        if (this._state != 0) {
            throw new IllegalStateException("STATE!=START");
        }
        this._status = i;
        if (str != null) {
            int length = str.length();
            if (length > this._headerBufferSize / 2) {
                length = this._headerBufferSize / 2;
            }
            this._reason = new ByteArrayBuffer(length);
            for (int i2 = 0; i2 < length; i2++) {
                char charAt = str.charAt(i2);
                if (charAt == '\r' || charAt == '\n') {
                    this._reason.put((byte) 32);
                } else {
                    this._reason.put((byte) charAt);
                }
            }
        }
    }

    public void setSendServerVersion(boolean z) {
        this._sendServerVersion = z;
    }

    public void setVersion(int i) {
        if (this._state != 0) {
            throw new IllegalStateException("STATE!=START");
        }
        this._version = i;
        if (this._version == 9 && this._method != null) {
            this._noContent = true;
        }
    }

    void uncheckedAddContent(int i) {
        this._buffer.put((byte) i);
    }
}
