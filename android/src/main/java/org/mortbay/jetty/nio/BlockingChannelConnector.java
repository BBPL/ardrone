package org.mortbay.jetty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.mortbay.io.EndPoint;
import org.mortbay.io.nio.ChannelEndPoint;
import org.mortbay.jetty.EofException;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpException;
import org.mortbay.jetty.Request;
import org.mortbay.log.Log;

public class BlockingChannelConnector extends AbstractNIOConnector {
    private transient ServerSocketChannel _acceptChannel;

    private class Connection extends ChannelEndPoint implements Runnable {
        HttpConnection _connection;
        boolean _dispatched = false;
        int _sotimeout;
        private final BlockingChannelConnector this$0;

        Connection(BlockingChannelConnector blockingChannelConnector, ByteChannel byteChannel) {
            this.this$0 = blockingChannelConnector;
            super(byteChannel);
            this._connection = new HttpConnection(blockingChannelConnector, this, blockingChannelConnector.getServer());
        }

        void dispatch() throws IOException {
            if (!this.this$0.getThreadPool().dispatch(this)) {
                Log.warn("dispatch failed for  {}", this._connection);
                close();
            }
        }

        public void run() {
            BlockingChannelConnector blockingChannelConnector;
            HttpConnection httpConnection;
            try {
                BlockingChannelConnector.access$000(this.this$0, this._connection);
                while (isOpen()) {
                    if (this._connection.isIdle() && this.this$0.getServer().getThreadPool().isLowOnThreads()) {
                        int lowResourceMaxIdleTime = this.this$0.getLowResourceMaxIdleTime();
                        if (lowResourceMaxIdleTime >= 0 && this._sotimeout != lowResourceMaxIdleTime) {
                            this._sotimeout = lowResourceMaxIdleTime;
                            ((SocketChannel) getTransport()).socket().setSoTimeout(this._sotimeout);
                        }
                    }
                    this._connection.handle();
                }
                blockingChannelConnector = this.this$0;
                httpConnection = this._connection;
            } catch (EofException e) {
                Log.debug("EOF", e);
                try {
                    close();
                } catch (Throwable e2) {
                    Log.ignore(e2);
                }
                blockingChannelConnector = this.this$0;
                httpConnection = this._connection;
            } catch (HttpException e3) {
                Log.debug("BAD", e3);
                try {
                    close();
                } catch (Throwable e22) {
                    Log.ignore(e22);
                }
                blockingChannelConnector = this.this$0;
                httpConnection = this._connection;
            } catch (Throwable th) {
                BlockingChannelConnector.access$100(this.this$0, this._connection);
            }
            BlockingChannelConnector.access$100(blockingChannelConnector, httpConnection);
        }
    }

    static void access$000(BlockingChannelConnector blockingChannelConnector, HttpConnection httpConnection) {
        blockingChannelConnector.connectionOpened(httpConnection);
    }

    static void access$100(BlockingChannelConnector blockingChannelConnector, HttpConnection httpConnection) {
        blockingChannelConnector.connectionClosed(httpConnection);
    }

    public void accept(int i) throws IOException, InterruptedException {
        ByteChannel accept = this._acceptChannel.accept();
        accept.configureBlocking(true);
        configure(accept.socket());
        new Connection(this, accept).dispatch();
    }

    public void close() throws IOException {
        if (this._acceptChannel != null) {
            this._acceptChannel.close();
        }
        this._acceptChannel = null;
    }

    public void customize(EndPoint endPoint, Request request) throws IOException {
        Connection connection = (Connection) endPoint;
        if (connection._sotimeout != this._maxIdleTime) {
            connection._sotimeout = this._maxIdleTime;
            ((SocketChannel) endPoint.getTransport()).socket().setSoTimeout(this._maxIdleTime);
        }
        super.customize(endPoint, request);
        configure(((SocketChannel) endPoint.getTransport()).socket());
    }

    public Object getConnection() {
        return this._acceptChannel;
    }

    public int getLocalPort() {
        return (this._acceptChannel == null || !this._acceptChannel.isOpen()) ? -1 : this._acceptChannel.socket().getLocalPort();
    }

    public void open() throws IOException {
        this._acceptChannel = ServerSocketChannel.open();
        this._acceptChannel.configureBlocking(true);
        this._acceptChannel.socket().bind(getHost() == null ? new InetSocketAddress(getPort()) : new InetSocketAddress(getHost(), getPort()), getAcceptQueueSize());
    }
}
