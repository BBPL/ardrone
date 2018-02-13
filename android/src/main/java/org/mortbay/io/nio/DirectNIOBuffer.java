package org.mortbay.io.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import org.mortbay.io.AbstractBuffer;
import org.mortbay.io.Buffer;

public class DirectNIOBuffer extends AbstractBuffer implements NIOBuffer {
    protected ByteBuffer _buf;
    private ReadableByteChannel _in;
    private InputStream _inStream;
    private WritableByteChannel _out;
    private OutputStream _outStream;

    public DirectNIOBuffer(int i) {
        super(2, false);
        this._buf = ByteBuffer.allocateDirect(i);
        this._buf.position(0);
        this._buf.limit(this._buf.capacity());
    }

    public DirectNIOBuffer(File file) throws IOException {
        super(1, false);
        this._buf = new FileInputStream(file).getChannel().map(MapMode.READ_ONLY, 0, file.length());
        setGetIndex(0);
        setPutIndex((int) file.length());
        this._access = 0;
    }

    public DirectNIOBuffer(ByteBuffer byteBuffer, boolean z) {
        super(z ? 0 : 2, false);
        if (byteBuffer.isDirect()) {
            this._buf = byteBuffer;
            setGetIndex(byteBuffer.position());
            setPutIndex(byteBuffer.limit());
            return;
        }
        throw new IllegalArgumentException();
    }

    public byte[] array() {
        return null;
    }

    public int capacity() {
        return this._buf.capacity();
    }

    public ByteBuffer getByteBuffer() {
        return this._buf;
    }

    public boolean isDirect() {
        return true;
    }

    public byte peek(int i) {
        return this._buf.get(i);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int peek(int r4, byte[] r5, int r6, int r7) {
        /*
        r3 = this;
        r2 = 0;
        r0 = r4 + r7;
        r1 = r3.capacity();
        if (r0 <= r1) goto L_0x0012;
    L_0x0009:
        r0 = r3.capacity();
        r0 = r0 - r4;
        if (r0 != 0) goto L_0x0013;
    L_0x0010:
        r0 = -1;
    L_0x0011:
        return r0;
    L_0x0012:
        r0 = r7;
    L_0x0013:
        if (r0 < 0) goto L_0x0010;
    L_0x0015:
        r1 = r3._buf;	 Catch:{ all -> 0x0025 }
        r1.position(r4);	 Catch:{ all -> 0x0025 }
        r1 = r3._buf;	 Catch:{ all -> 0x0025 }
        r1.get(r5, r6, r0);	 Catch:{ all -> 0x0025 }
        r1 = r3._buf;
        r1.position(r2);
        goto L_0x0011;
    L_0x0025:
        r0 = move-exception;
        r1 = r3._buf;
        r1.position(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.io.nio.DirectNIOBuffer.peek(int, byte[], int, int):int");
    }

    public int poke(int i, Buffer buffer) {
        if (isReadOnly()) {
            throw new IllegalStateException("READONLY");
        }
        byte[] array = buffer.array();
        if (array != null) {
            return poke(i, array, buffer.getIndex(), buffer.length());
        }
        Buffer buffer2 = buffer.buffer();
        if (!(buffer2 instanceof DirectNIOBuffer)) {
            return super.poke(i, buffer);
        }
        ByteBuffer byteBuffer = ((DirectNIOBuffer) buffer2)._buf;
        ByteBuffer duplicate = byteBuffer == this._buf ? this._buf.duplicate() : byteBuffer;
        try {
            this._buf.position(i);
            int remaining = this._buf.remaining();
            int length = buffer.length();
            if (length <= remaining) {
                remaining = length;
            }
            duplicate.position(buffer.getIndex());
            duplicate.limit(buffer.getIndex() + remaining);
            this._buf.put(duplicate);
            return remaining;
        } finally {
            this._buf.position(0);
            duplicate.limit(duplicate.capacity());
            duplicate.position(0);
        }
    }

    public int poke(int i, byte[] bArr, int i2, int i3) {
        if (isReadOnly()) {
            throw new IllegalStateException("READONLY");
        } else if (i < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("index<0: ").append(i).append("<0").toString());
        } else {
            int capacity;
            if (i + i3 > capacity()) {
                capacity = capacity() - i;
                if (capacity < 0) {
                    throw new IllegalArgumentException(new StringBuffer().append("index>capacity(): ").append(i).append(">").append(capacity()).toString());
                }
            }
            capacity = i3;
            try {
                this._buf.position(i);
                int remaining = this._buf.remaining();
                if (capacity <= remaining) {
                    remaining = capacity;
                }
                if (remaining > 0) {
                    this._buf.put(bArr, i2, remaining);
                }
                this._buf.position(0);
                return remaining;
            } catch (Throwable th) {
                this._buf.position(0);
            }
        }
    }

    public void poke(int i, byte b) {
        if (isReadOnly()) {
            throw new IllegalStateException("READONLY");
        } else if (i < 0) {
            throw new IllegalArgumentException(new StringBuffer().append("index<0: ").append(i).append("<0").toString());
        } else if (i > capacity()) {
            throw new IllegalArgumentException(new StringBuffer().append("index>capacity(): ").append(i).append(">").append(capacity()).toString());
        } else {
            this._buf.put(i, b);
        }
    }

    public int readFrom(InputStream inputStream, int i) throws IOException {
        int i2;
        IOException e;
        if (!(this._in != null && this._in.isOpen() && inputStream == this._inStream)) {
            this._in = Channels.newChannel(inputStream);
            this._inStream = inputStream;
        }
        if (i < 0 || i > space()) {
            i = space();
        }
        int putIndex = putIndex();
        int i3 = i;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i6 < i) {
            try {
                this._buf.position(putIndex);
                this._buf.limit(putIndex + i3);
                i5 = this._in.read(this._buf);
                if (i5 < 0) {
                    this._in = null;
                    this._inStream = inputStream;
                    i2 = i5;
                    i5 = i6;
                    i6 = i2;
                    break;
                }
                int i7;
                if (i5 <= 0) {
                    i7 = i4 + 1;
                    if (i4 > 1) {
                        i2 = i5;
                        i5 = i6;
                        i6 = i2;
                        break;
                    }
                }
                putIndex += i5;
                i7 = i6 + i5;
                i6 = i3 - i5;
                setPutIndex(putIndex);
                i3 = i6;
                i6 = i7;
                i7 = 0;
                try {
                    if (inputStream.available() <= 0) {
                        i2 = i5;
                        i5 = i6;
                        i6 = i2;
                        break;
                    }
                    i4 = i7;
                } catch (IOException e2) {
                    e = e2;
                }
            } catch (IOException e3) {
                e = e3;
            } catch (Throwable th) {
                Throwable th2 = th;
            }
        }
        i2 = i5;
        i5 = i6;
        i6 = i2;
        if (i6 >= 0 || i5 != 0) {
            if (!(this._in == null || this._in.isOpen())) {
                this._in = null;
                this._inStream = inputStream;
            }
            this._buf.position(0);
            this._buf.limit(this._buf.capacity());
            return i5;
        }
        if (!(this._in == null || this._in.isOpen())) {
            this._in = null;
            this._inStream = inputStream;
        }
        this._buf.position(0);
        this._buf.limit(this._buf.capacity());
        return -1;
        try {
            this._in = null;
            this._inStream = inputStream;
            throw e;
        } catch (Throwable th3) {
            th2 = th3;
            if (!(this._in == null || this._in.isOpen())) {
                this._in = null;
                this._inStream = inputStream;
            }
            this._buf.position(0);
            this._buf.limit(this._buf.capacity());
            throw th2;
        }
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (!(this._out != null && this._out.isOpen() && this._out == this._outStream)) {
            this._out = Channels.newChannel(outputStream);
            this._outStream = outputStream;
        }
        synchronized (this._buf) {
            int i = 0;
            while (hasContent() && this._out.isOpen()) {
                this._buf.position(getIndex());
                this._buf.limit(putIndex());
                int write = this._out.write(this._buf);
                if (write >= 0) {
                    if (write > 0) {
                        try {
                            skip(write);
                            write = 0;
                        } catch (IOException e) {
                            this._out = null;
                            this._outStream = null;
                            throw e;
                        } catch (Throwable th) {
                            if (!(this._out == null || this._out.isOpen())) {
                                this._out = null;
                                this._outStream = null;
                            }
                            this._buf.position(0);
                            this._buf.limit(this._buf.capacity());
                        }
                    } else {
                        write = i + 1;
                        if (i > 1) {
                            break;
                        }
                    }
                    i = write;
                }
            }
            if (!(this._out == null || this._out.isOpen())) {
                this._out = null;
                this._outStream = null;
            }
            this._buf.position(0);
            this._buf.limit(this._buf.capacity());
        }
    }
}
