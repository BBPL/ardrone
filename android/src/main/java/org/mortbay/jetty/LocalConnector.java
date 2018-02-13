package org.mortbay.jetty;

import java.io.IOException;
import org.mortbay.io.Buffer;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.io.ByteArrayEndPoint;

public class LocalConnector extends AbstractConnector {
    boolean _accepting;
    ByteArrayEndPoint _endp;
    ByteArrayBuffer _in;
    boolean _keepOpen;
    ByteArrayBuffer _out;
    Server _server;

    public LocalConnector() {
        setPort(1);
    }

    protected void accept(int i) throws IOException, InterruptedException {
        Throwable th;
        HttpConnection httpConnection = null;
        while (isRunning()) {
            HttpConnection httpConnection2;
            synchronized (this) {
                while (!this._accepting) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            if (httpConnection == null) {
                try {
                    httpConnection2 = new HttpConnection(this, this._endp, getServer());
                    try {
                        connectionOpened(httpConnection2);
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    httpConnection2 = httpConnection;
                    th = th4;
                }
            } else {
                httpConnection2 = httpConnection;
            }
            while (this._in.length() > 0) {
                try {
                    httpConnection2.handle();
                } catch (Throwable th5) {
                    th = th5;
                }
            }
            if (!this._keepOpen) {
                connectionClosed(httpConnection2);
                httpConnection2.destroy();
                httpConnection2 = null;
            }
            synchronized (this) {
                this._accepting = false;
                notify();
            }
            httpConnection = httpConnection2;
        }
        return;
        if (!this._keepOpen) {
            connectionClosed(httpConnection2);
            httpConnection2.destroy();
        }
        synchronized (this) {
            this._accepting = false;
            notify();
        }
        throw th;
    }

    public void clear() {
        this._in.clear();
        this._out.clear();
    }

    public void close() throws IOException {
    }

    public void doStart() throws Exception {
        this._in = new ByteArrayBuffer(8192);
        this._out = new ByteArrayBuffer(8192);
        this._endp = new ByteArrayEndPoint();
        this._endp.setIn(this._in);
        this._endp.setOut(this._out);
        this._endp.setGrowOutput(true);
        this._accepting = false;
        super.doStart();
    }

    public Object getConnection() {
        return this._endp;
    }

    public int getLocalPort() {
        return -1;
    }

    public String getResponses(String str) throws Exception {
        return getResponses(str, false);
    }

    public String getResponses(String str, boolean z) throws Exception {
        Buffer byteArrayBuffer = new ByteArrayBuffer(str);
        if (this._in.space() < byteArrayBuffer.length()) {
            ByteArrayBuffer byteArrayBuffer2 = new ByteArrayBuffer(this._in.length() + byteArrayBuffer.length());
            byteArrayBuffer2.put(this._in);
            this._in = byteArrayBuffer2;
            this._endp.setIn(this._in);
        }
        this._in.put(byteArrayBuffer);
        synchronized (this) {
            this._keepOpen = z;
            this._accepting = true;
            notify();
            while (this._accepting) {
                wait();
            }
        }
        this._out = this._endp.getOut();
        return this._out.toString();
    }

    public ByteArrayBuffer getResponses(ByteArrayBuffer byteArrayBuffer, boolean z) throws Exception {
        if (this._in.space() < byteArrayBuffer.length()) {
            ByteArrayBuffer byteArrayBuffer2 = new ByteArrayBuffer(this._in.length() + byteArrayBuffer.length());
            byteArrayBuffer2.put(this._in);
            this._in = byteArrayBuffer2;
            this._endp.setIn(this._in);
        }
        this._in.put((Buffer) byteArrayBuffer);
        synchronized (this) {
            this._keepOpen = z;
            this._accepting = true;
            notify();
            while (this._accepting) {
                wait();
            }
        }
        this._out = this._endp.getOut();
        return this._out;
    }

    protected Buffer newBuffer(int i) {
        return new ByteArrayBuffer(i);
    }

    public void open() throws IOException {
    }

    public void reopen() {
        this._in.clear();
        this._out.clear();
        this._endp = new ByteArrayEndPoint();
        this._endp.setIn(this._in);
        this._endp.setOut(this._out);
        this._endp.setGrowOutput(true);
        this._accepting = false;
    }

    public void setServer(Server server) {
        super.setServer(server);
        this._server = server;
    }
}
