package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@ThreadSafe
public class DefaultClientConnectionOperator implements ClientConnectionOperator {
    protected final DnsResolver dnsResolver;
    private final Log log = LogFactory.getLog(getClass());
    protected final SchemeRegistry schemeRegistry;

    public DefaultClientConnectionOperator(SchemeRegistry schemeRegistry) {
        if (schemeRegistry == null) {
            throw new IllegalArgumentException("Scheme registry amy not be null");
        }
        this.schemeRegistry = schemeRegistry;
        this.dnsResolver = new SystemDefaultDnsResolver();
    }

    public DefaultClientConnectionOperator(SchemeRegistry schemeRegistry, DnsResolver dnsResolver) {
        if (schemeRegistry == null) {
            throw new IllegalArgumentException("Scheme registry may not be null");
        } else if (dnsResolver == null) {
            throw new IllegalArgumentException("DNS resolver may not be null");
        } else {
            this.schemeRegistry = schemeRegistry;
            this.dnsResolver = dnsResolver;
        }
    }

    public OperatedClientConnection createConnection() {
        return new DefaultClientConnection();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void openConnection(org.apache.http.conn.OperatedClientConnection r14, org.apache.http.HttpHost r15, java.net.InetAddress r16, org.apache.http.protocol.HttpContext r17, org.apache.http.params.HttpParams r18) throws java.io.IOException {
        /*
        r13 = this;
        if (r14 != 0) goto L_0x000a;
    L_0x0002:
        r2 = new java.lang.IllegalArgumentException;
        r3 = "Connection may not be null";
        r2.<init>(r3);
        throw r2;
    L_0x000a:
        if (r15 != 0) goto L_0x0014;
    L_0x000c:
        r2 = new java.lang.IllegalArgumentException;
        r3 = "Target host may not be null";
        r2.<init>(r3);
        throw r2;
    L_0x0014:
        if (r18 != 0) goto L_0x001e;
    L_0x0016:
        r2 = new java.lang.IllegalArgumentException;
        r3 = "Parameters may not be null";
        r2.<init>(r3);
        throw r2;
    L_0x001e:
        r2 = r14.isOpen();
        if (r2 == 0) goto L_0x002c;
    L_0x0024:
        r2 = new java.lang.IllegalStateException;
        r3 = "Connection must not be open";
        r2.<init>(r3);
        throw r2;
    L_0x002c:
        r2 = r13.schemeRegistry;
        r3 = r15.getSchemeName();
        r2 = r2.getScheme(r3);
        r6 = r2.getSchemeSocketFactory();
        r3 = r15.getHostName();
        r7 = r13.resolveHostname(r3);
        r3 = r15.getPort();
        r8 = r2.resolvePort(r3);
        r2 = 0;
        r4 = r2;
    L_0x004c:
        r2 = r7.length;
        if (r4 >= r2) goto L_0x00ab;
    L_0x004f:
        r3 = r7[r4];
        r2 = r7.length;
        r2 = r2 + -1;
        if (r4 != r2) goto L_0x00ac;
    L_0x0056:
        r2 = 1;
    L_0x0057:
        r0 = r18;
        r5 = r6.createSocket(r0);
        r14.opening(r5, r15);
        r9 = new org.apache.http.conn.HttpInetSocketAddress;
        r9.<init>(r15, r3, r8);
        r3 = 0;
        if (r16 == 0) goto L_0x0070;
    L_0x0068:
        r3 = new java.net.InetSocketAddress;
        r10 = 0;
        r0 = r16;
        r3.<init>(r0, r10);
    L_0x0070:
        r10 = r13.log;
        r10 = r10.isDebugEnabled();
        if (r10 == 0) goto L_0x0090;
    L_0x0078:
        r10 = r13.log;
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r12 = "Connecting to ";
        r11 = r11.append(r12);
        r11 = r11.append(r9);
        r11 = r11.toString();
        r10.debug(r11);
    L_0x0090:
        r0 = r18;
        r3 = r6.connectSocket(r5, r9, r3, r0);	 Catch:{ ConnectException -> 0x00ae, ConnectTimeoutException -> 0x00b7 }
        if (r5 == r3) goto L_0x00ec;
    L_0x0098:
        r14.opening(r3, r15);	 Catch:{ ConnectException -> 0x00ae, ConnectTimeoutException -> 0x00b7 }
    L_0x009b:
        r0 = r17;
        r1 = r18;
        r13.prepareSocket(r3, r0, r1);	 Catch:{ ConnectException -> 0x00ae, ConnectTimeoutException -> 0x00b7 }
        r3 = r6.isSecure(r3);	 Catch:{ ConnectException -> 0x00ae, ConnectTimeoutException -> 0x00b7 }
        r0 = r18;
        r14.openCompleted(r3, r0);	 Catch:{ ConnectException -> 0x00ae, ConnectTimeoutException -> 0x00b7 }
    L_0x00ab:
        return;
    L_0x00ac:
        r2 = 0;
        goto L_0x0057;
    L_0x00ae:
        r3 = move-exception;
        if (r2 == 0) goto L_0x00bb;
    L_0x00b1:
        r2 = new org.apache.http.conn.HttpHostConnectException;
        r2.<init>(r15, r3);
        throw r2;
    L_0x00b7:
        r3 = move-exception;
        if (r2 == 0) goto L_0x00bb;
    L_0x00ba:
        throw r3;
    L_0x00bb:
        r2 = r13.log;
        r2 = r2.isDebugEnabled();
        if (r2 == 0) goto L_0x00e7;
    L_0x00c3:
        r2 = r13.log;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Connect to ";
        r3 = r3.append(r5);
        r3 = r3.append(r9);
        r5 = " timed out. ";
        r3 = r3.append(r5);
        r5 = "Connection will be retried using another IP address";
        r3 = r3.append(r5);
        r3 = r3.toString();
        r2.debug(r3);
    L_0x00e7:
        r2 = r4 + 1;
        r4 = r2;
        goto L_0x004c;
    L_0x00ec:
        r3 = r5;
        goto L_0x009b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.DefaultClientConnectionOperator.openConnection(org.apache.http.conn.OperatedClientConnection, org.apache.http.HttpHost, java.net.InetAddress, org.apache.http.protocol.HttpContext, org.apache.http.params.HttpParams):void");
    }

    protected void prepareSocket(Socket socket, HttpContext httpContext, HttpParams httpParams) throws IOException {
        socket.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(httpParams));
        socket.setSoTimeout(HttpConnectionParams.getSoTimeout(httpParams));
        int linger = HttpConnectionParams.getLinger(httpParams);
        if (linger >= 0) {
            socket.setSoLinger(linger > 0, linger);
        }
    }

    protected InetAddress[] resolveHostname(String str) throws UnknownHostException {
        return this.dnsResolver.resolve(str);
    }

    public void updateSecureConnection(OperatedClientConnection operatedClientConnection, HttpHost httpHost, HttpContext httpContext, HttpParams httpParams) throws IOException {
        if (operatedClientConnection == null) {
            throw new IllegalArgumentException("Connection may not be null");
        } else if (httpHost == null) {
            throw new IllegalArgumentException("Target host may not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        } else if (operatedClientConnection.isOpen()) {
            Scheme scheme = this.schemeRegistry.getScheme(httpHost.getSchemeName());
            if (scheme.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory) {
                SchemeLayeredSocketFactory schemeLayeredSocketFactory = (SchemeLayeredSocketFactory) scheme.getSchemeSocketFactory();
                try {
                    Socket createLayeredSocket = schemeLayeredSocketFactory.createLayeredSocket(operatedClientConnection.getSocket(), httpHost.getHostName(), httpHost.getPort(), httpParams);
                    prepareSocket(createLayeredSocket, httpContext, httpParams);
                    operatedClientConnection.update(createLayeredSocket, httpHost, schemeLayeredSocketFactory.isSecure(createLayeredSocket), httpParams);
                    return;
                } catch (ConnectException e) {
                    throw new HttpHostConnectException(httpHost, e);
                }
            }
            throw new IllegalArgumentException("Target scheme (" + scheme.getName() + ") must have layered socket factory.");
        } else {
            throw new IllegalStateException("Connection must be open");
        }
    }
}
