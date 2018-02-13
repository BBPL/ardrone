package org.mortbay.jetty;

import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.io.Buffer;
import org.mortbay.io.Buffers;

public abstract class AbstractBuffers extends AbstractLifeCycle implements Buffers {
    private static final int __HEADER = 0;
    private static final int __OTHER = 3;
    private static final int __REQUEST = 1;
    private static final int __RESPONSE = 2;
    private final ThreadLocal _buffers = new C13221(this);
    private int _headerBufferSize = 4096;
    private final int[] _pool = new int[]{2, 1, 1, 2};
    private int _requestBufferSize = 8192;
    private int _responseBufferSize = 24576;

    class C13221 extends ThreadLocal {
        private final AbstractBuffers this$0;

        C13221(AbstractBuffers abstractBuffers) {
            this.this$0 = abstractBuffers;
        }

        protected Object initialValue() {
            return new ThreadBuffers(AbstractBuffers.access$000(this.this$0)[0], AbstractBuffers.access$000(this.this$0)[1], AbstractBuffers.access$000(this.this$0)[2], AbstractBuffers.access$000(this.this$0)[3]);
        }
    }

    protected static class ThreadBuffers {
        final Buffer[][] _buffers = new Buffer[4][];

        ThreadBuffers(int i, int i2, int i3, int i4) {
            this._buffers[0] = new Buffer[i];
            this._buffers[1] = new Buffer[i2];
            this._buffers[2] = new Buffer[i3];
            this._buffers[3] = new Buffer[i4];
        }
    }

    static int[] access$000(AbstractBuffers abstractBuffers) {
        return abstractBuffers._pool;
    }

    protected void doStart() throws Exception {
        super.doStart();
        int[] iArr;
        if (this._headerBufferSize == this._requestBufferSize && this._headerBufferSize == this._responseBufferSize) {
            iArr = this._pool;
            iArr[0] = iArr[0] + (this._pool[1] + this._pool[2]);
            this._pool[1] = 0;
            this._pool[2] = 0;
        } else if (this._headerBufferSize == this._requestBufferSize) {
            iArr = this._pool;
            iArr[0] = iArr[0] + this._pool[1];
            this._pool[1] = 0;
        } else if (this._headerBufferSize == this._responseBufferSize) {
            iArr = this._pool;
            iArr[0] = iArr[0] + this._pool[2];
            this._pool[2] = 0;
        } else if (this._requestBufferSize == this._responseBufferSize) {
            iArr = this._pool;
            iArr[2] = iArr[2] + this._pool[1];
            this._pool[1] = 0;
        }
    }

    public Buffer getBuffer(int i) {
        int i2 = 0;
        int i3 = i == this._headerBufferSize ? 0 : i == this._responseBufferSize ? 2 : i == this._requestBufferSize ? 1 : 3;
        Buffer[] bufferArr = ((ThreadBuffers) this._buffers.get())._buffers[i3];
        while (i2 < bufferArr.length) {
            Buffer buffer = bufferArr[i2];
            if (buffer == null || buffer.capacity() != i) {
                i2++;
            } else {
                bufferArr[i2] = null;
                return buffer;
            }
        }
        return newBuffer(i);
    }

    public int getHeaderBufferSize() {
        return this._headerBufferSize;
    }

    public int getRequestBufferSize() {
        return this._requestBufferSize;
    }

    public int getResponseBufferSize() {
        return this._responseBufferSize;
    }

    protected abstract Buffer newBuffer(int i);

    public void returnBuffer(Buffer buffer) {
        int i = 0;
        buffer.clear();
        if (!buffer.isVolatile() && !buffer.isImmutable()) {
            int capacity = buffer.capacity();
            int i2 = capacity == this._headerBufferSize ? 0 : capacity == this._responseBufferSize ? 2 : capacity == this._requestBufferSize ? 1 : 3;
            Buffer[] bufferArr = ((ThreadBuffers) this._buffers.get())._buffers[i2];
            while (i < bufferArr.length) {
                if (bufferArr[i] == null) {
                    bufferArr[i] = buffer;
                    return;
                }
                i++;
            }
        }
    }

    public void setHeaderBufferSize(int i) {
        if (isStarted()) {
            throw new IllegalStateException();
        }
        this._headerBufferSize = i;
    }

    public void setRequestBufferSize(int i) {
        if (isStarted()) {
            throw new IllegalStateException();
        }
        this._requestBufferSize = i;
    }

    public void setResponseBufferSize(int i) {
        if (isStarted()) {
            throw new IllegalStateException();
        }
        this._responseBufferSize = i;
    }

    public String toString() {
        return new StringBuffer().append("{{").append(this._headerBufferSize).append(",").append(this._requestBufferSize).append(",").append(this._responseBufferSize).append("}}").toString();
    }
}
