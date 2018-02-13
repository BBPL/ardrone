package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class AbstractSessionOutputBuffer implements SessionOutputBuffer, BufferInfo {
    private static final Charset ASCII = Charset.forName("US-ASCII");
    private static final byte[] CRLF = new byte[]{(byte) 13, (byte) 10};
    private boolean ascii = true;
    private ByteBuffer bbuf;
    private ByteArrayBuffer buffer;
    private Charset charset;
    private CharsetEncoder encoder;
    private HttpTransportMetricsImpl metrics;
    private int minChunkLimit = 512;
    private CodingErrorAction onMalformedInputAction;
    private CodingErrorAction onUnMappableInputAction;
    private OutputStream outstream;

    private void handleEncodingResult(CoderResult coderResult) throws IOException {
        if (coderResult.isError()) {
            coderResult.throwException();
        }
        this.bbuf.flip();
        while (this.bbuf.hasRemaining()) {
            write(this.bbuf.get());
        }
        this.bbuf.compact();
    }

    private void writeEncoded(CharBuffer charBuffer) throws IOException {
        if (charBuffer.hasRemaining()) {
            if (this.encoder == null) {
                this.encoder = this.charset.newEncoder();
                this.encoder.onMalformedInput(this.onMalformedInputAction);
                this.encoder.onUnmappableCharacter(this.onUnMappableInputAction);
            }
            if (this.bbuf == null) {
                this.bbuf = ByteBuffer.allocate(1024);
            }
            this.encoder.reset();
            while (charBuffer.hasRemaining()) {
                handleEncodingResult(this.encoder.encode(charBuffer, this.bbuf, true));
            }
            handleEncodingResult(this.encoder.flush(this.bbuf));
            this.bbuf.clear();
        }
    }

    public int available() {
        return capacity() - length();
    }

    public int capacity() {
        return this.buffer.capacity();
    }

    protected HttpTransportMetricsImpl createTransportMetrics() {
        return new HttpTransportMetricsImpl();
    }

    public void flush() throws IOException {
        flushBuffer();
        this.outstream.flush();
    }

    protected void flushBuffer() throws IOException {
        int length = this.buffer.length();
        if (length > 0) {
            this.outstream.write(this.buffer.buffer(), 0, length);
            this.buffer.clear();
            this.metrics.incrementBytesTransferred((long) length);
        }
    }

    public HttpTransportMetrics getMetrics() {
        return this.metrics;
    }

    protected void init(OutputStream outputStream, int i, HttpParams httpParams) {
        if (outputStream == null) {
            throw new IllegalArgumentException("Input stream may not be null");
        } else if (i <= 0) {
            throw new IllegalArgumentException("Buffer size may not be negative or zero");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.outstream = outputStream;
            this.buffer = new ByteArrayBuffer(i);
            this.charset = Charset.forName(HttpProtocolParams.getHttpElementCharset(httpParams));
            this.ascii = this.charset.equals(ASCII);
            this.encoder = null;
            this.minChunkLimit = httpParams.getIntParameter(CoreConnectionPNames.MIN_CHUNK_LIMIT, 512);
            this.metrics = createTransportMetrics();
            this.onMalformedInputAction = HttpProtocolParams.getMalformedInputAction(httpParams);
            this.onUnMappableInputAction = HttpProtocolParams.getUnmappableInputAction(httpParams);
        }
    }

    public int length() {
        return this.buffer.length();
    }

    public void write(int i) throws IOException {
        if (this.buffer.isFull()) {
            flushBuffer();
        }
        this.buffer.append(i);
    }

    public void write(byte[] bArr) throws IOException {
        if (bArr != null) {
            write(bArr, 0, bArr.length);
        }
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (bArr != null) {
            if (i2 > this.minChunkLimit || i2 > this.buffer.capacity()) {
                flushBuffer();
                this.outstream.write(bArr, i, i2);
                this.metrics.incrementBytesTransferred((long) i2);
                return;
            }
            if (i2 > this.buffer.capacity() - this.buffer.length()) {
                flushBuffer();
            }
            this.buffer.append(bArr, i, i2);
        }
    }

    public void writeLine(String str) throws IOException {
        if (str != null) {
            if (str.length() > 0) {
                if (this.ascii) {
                    for (int i = 0; i < str.length(); i++) {
                        write(str.charAt(i));
                    }
                } else {
                    writeEncoded(CharBuffer.wrap(str));
                }
            }
            write(CRLF);
        }
    }

    public void writeLine(CharArrayBuffer charArrayBuffer) throws IOException {
        int i = 0;
        if (charArrayBuffer != null) {
            if (this.ascii) {
                int length = charArrayBuffer.length();
                while (length > 0) {
                    int min = Math.min(this.buffer.capacity() - this.buffer.length(), length);
                    if (min > 0) {
                        this.buffer.append(charArrayBuffer, i, min);
                    }
                    if (this.buffer.isFull()) {
                        flushBuffer();
                    }
                    i += min;
                    length -= min;
                }
            } else {
                writeEncoded(CharBuffer.wrap(charArrayBuffer.buffer(), 0, charArrayBuffer.length()));
            }
            write(CRLF);
        }
    }
}
