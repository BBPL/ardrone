package org.mortbay.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import org.mortbay.io.Buffer;
import org.mortbay.io.EndPoint;
import org.mortbay.io.Portable;
import org.mortbay.jetty.HttpStatus;

public class ChannelEndPoint implements EndPoint {
    protected final ByteChannel _channel;
    protected final ByteBuffer[] _gather2 = new ByteBuffer[2];
    protected final InetSocketAddress _local;
    protected final InetSocketAddress _remote;
    protected final Socket _socket;

    public ChannelEndPoint(ByteChannel byteChannel) {
        this._channel = byteChannel;
        if (byteChannel instanceof SocketChannel) {
            this._socket = ((SocketChannel) byteChannel).socket();
            this._local = (InetSocketAddress) this._socket.getLocalSocketAddress();
            this._remote = (InetSocketAddress) this._socket.getRemoteSocketAddress();
            return;
        }
        this._socket = null;
        this._local = null;
        this._remote = null;
    }

    public boolean blockReadable(long j) throws IOException {
        return true;
    }

    public boolean blockWritable(long j) throws IOException {
        return true;
    }

    public void close() throws IOException {
        if (!(this._socket == null || this._socket.isOutputShutdown())) {
            this._socket.shutdownOutput();
        }
        this._channel.close();
    }

    public int fill(Buffer buffer) throws IOException {
        Buffer buffer2 = buffer.buffer();
        if (buffer2 instanceof NIOBuffer) {
            int read;
            NIOBuffer nIOBuffer = (NIOBuffer) buffer2;
            ByteBuffer byteBuffer = nIOBuffer.getByteBuffer();
            synchronized (nIOBuffer) {
                try {
                    byteBuffer.position(buffer.putIndex());
                    read = this._channel.read(byteBuffer);
                    if (read < 0) {
                        this._channel.close();
                    }
                    buffer.setPutIndex(byteBuffer.position());
                    byteBuffer.position(0);
                } catch (Throwable th) {
                    buffer.setPutIndex(byteBuffer.position());
                    byteBuffer.position(0);
                }
            }
            return read;
        }
        throw new IOException(HttpStatus.Not_Implemented);
    }

    public int flush(Buffer buffer) throws IOException {
        int write;
        Buffer buffer2 = buffer.buffer();
        if (buffer2 instanceof NIOBuffer) {
            ByteBuffer byteBuffer = ((NIOBuffer) buffer2).getByteBuffer();
            synchronized (byteBuffer) {
                try {
                    byteBuffer.position(buffer.getIndex());
                    byteBuffer.limit(buffer.putIndex());
                    write = this._channel.write(byteBuffer);
                    if (write > 0) {
                        buffer.skip(write);
                    }
                    byteBuffer.position(0);
                    byteBuffer.limit(byteBuffer.capacity());
                } catch (Throwable th) {
                    byteBuffer.position(0);
                    byteBuffer.limit(byteBuffer.capacity());
                }
            }
        } else if (buffer.array() != null) {
            write = this._channel.write(ByteBuffer.wrap(buffer.array(), buffer.getIndex(), buffer.length()));
            if (write > 0) {
                buffer.skip(write);
            }
        } else {
            throw new IOException(HttpStatus.Not_Implemented);
        }
        return write;
    }

    public int flush(Buffer buffer, Buffer buffer2, Buffer buffer3) throws IOException {
        Buffer buffer4 = null;
        Buffer buffer5 = buffer == null ? null : buffer.buffer();
        if (buffer2 != null) {
            buffer4 = buffer2.buffer();
        }
        int flush;
        if (!(this._channel instanceof GatheringByteChannel) || buffer == null || buffer.length() == 0 || !(buffer instanceof NIOBuffer) || buffer2 == null || buffer2.length() == 0 || !(buffer2 instanceof NIOBuffer)) {
            if (buffer != null) {
                if (buffer2 != null && buffer2.length() > 0 && buffer.space() > buffer2.length()) {
                    buffer.put(buffer2);
                    buffer2.clear();
                }
                if (buffer3 != null && buffer3.length() > 0 && buffer.space() > buffer3.length()) {
                    buffer.put(buffer3);
                    buffer3.clear();
                }
            }
            flush = (buffer == null || buffer.length() <= 0) ? 0 : flush(buffer);
            if ((buffer == null || buffer.length() == 0) && buffer2 != null && buffer2.length() > 0) {
                flush += flush(buffer2);
            }
            return (buffer == null || buffer.length() == 0) ? ((buffer2 == null || buffer2.length() == 0) && buffer3 != null && buffer3.length() > 0) ? flush + flush(buffer3) : flush : flush;
        } else {
            ByteBuffer byteBuffer = ((NIOBuffer) buffer5).getByteBuffer();
            ByteBuffer byteBuffer2 = ((NIOBuffer) buffer4).getByteBuffer();
            synchronized (this) {
                synchronized (byteBuffer) {
                    synchronized (byteBuffer2) {
                        try {
                            byteBuffer.position(buffer.getIndex());
                            byteBuffer.limit(buffer.putIndex());
                            byteBuffer2.position(buffer2.getIndex());
                            byteBuffer2.limit(buffer2.putIndex());
                            this._gather2[0] = byteBuffer;
                            this._gather2[1] = byteBuffer2;
                            flush = (int) ((GatheringByteChannel) this._channel).write(this._gather2);
                            int length = buffer.length();
                            if (flush > length) {
                                buffer.clear();
                                buffer2.skip(flush - length);
                            } else if (flush > 0) {
                                buffer.skip(flush);
                            }
                            if (!buffer.isImmutable()) {
                                buffer.setGetIndex(byteBuffer.position());
                            }
                            if (!buffer2.isImmutable()) {
                                buffer2.setGetIndex(byteBuffer2.position());
                            }
                            byteBuffer.position(0);
                            byteBuffer2.position(0);
                            byteBuffer.limit(byteBuffer.capacity());
                            byteBuffer2.limit(byteBuffer2.capacity());
                        } catch (Throwable th) {
                            if (!buffer.isImmutable()) {
                                buffer.setGetIndex(byteBuffer.position());
                            }
                            if (!buffer2.isImmutable()) {
                                buffer2.setGetIndex(byteBuffer2.position());
                            }
                            byteBuffer.position(0);
                            byteBuffer2.position(0);
                            byteBuffer.limit(byteBuffer.capacity());
                            byteBuffer2.limit(byteBuffer2.capacity());
                        }
                    }
                }
            }
            return flush;
        }
    }

    public void flush() throws IOException {
    }

    public ByteChannel getChannel() {
        return this._channel;
    }

    public String getLocalAddr() {
        return this._socket == null ? null : (this._local == null || this._local.getAddress() == null || this._local.getAddress().isAnyLocalAddress()) ? Portable.ALL_INTERFACES : this._local.getAddress().getHostAddress();
    }

    public String getLocalHost() {
        return this._socket == null ? null : (this._local == null || this._local.getAddress() == null || this._local.getAddress().isAnyLocalAddress()) ? Portable.ALL_INTERFACES : this._local.getAddress().getCanonicalHostName();
    }

    public int getLocalPort() {
        return this._socket == null ? 0 : this._local == null ? -1 : this._local.getPort();
    }

    public String getRemoteAddr() {
        return (this._socket == null || this._remote == null) ? null : this._remote.getAddress().getHostAddress();
    }

    public String getRemoteHost() {
        return (this._socket == null || this._remote == null) ? null : this._remote.getAddress().getCanonicalHostName();
    }

    public int getRemotePort() {
        return this._socket == null ? 0 : (this._remote == null || this._remote == null) ? -1 : this._remote.getPort();
    }

    public Object getTransport() {
        return this._channel;
    }

    public boolean isBlocking() {
        return this._channel instanceof SelectableChannel ? ((SelectableChannel) this._channel).isBlocking() : true;
    }

    public boolean isBufferingInput() {
        return false;
    }

    public boolean isBufferingOutput() {
        return false;
    }

    public boolean isBufferred() {
        return false;
    }

    public boolean isOpen() {
        return this._channel.isOpen();
    }

    public void shutdownOutput() throws IOException {
        if (this._channel.isOpen() && (this._channel instanceof SocketChannel)) {
            Socket socket = ((SocketChannel) this._channel).socket();
            if (!socket.isClosed() && !socket.isOutputShutdown()) {
                socket.shutdownOutput();
            }
        }
    }
}
