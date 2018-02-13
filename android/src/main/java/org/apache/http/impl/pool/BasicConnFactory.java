package org.apache.http.impl.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.ConnFactory;

@Immutable
public class BasicConnFactory implements ConnFactory<HttpHost, HttpClientConnection> {
    private final HttpParams params;
    private final SSLSocketFactory sslfactory;

    public BasicConnFactory(SSLSocketFactory sSLSocketFactory, HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP params may not be null");
        }
        this.sslfactory = sSLSocketFactory;
        this.params = httpParams;
    }

    public BasicConnFactory(HttpParams httpParams) {
        this(null, httpParams);
    }

    protected HttpClientConnection create(Socket socket, HttpParams httpParams) throws IOException {
        HttpClientConnection defaultHttpClientConnection = new DefaultHttpClientConnection();
        defaultHttpClientConnection.bind(socket, httpParams);
        return defaultHttpClientConnection;
    }

    public HttpClientConnection create(HttpHost httpHost) throws IOException {
        String schemeName = httpHost.getSchemeName();
        Socket socket = null;
        if ("http".equalsIgnoreCase(schemeName)) {
            socket = new Socket();
        }
        if ("https".equalsIgnoreCase(schemeName) && this.sslfactory != null) {
            socket = this.sslfactory.createSocket();
        }
        if (socket == null) {
            throw new IOException(schemeName + " scheme is not supported");
        }
        int connectionTimeout = HttpConnectionParams.getConnectionTimeout(this.params);
        socket.setSoTimeout(HttpConnectionParams.getSoTimeout(this.params));
        socket.connect(new InetSocketAddress(httpHost.getHostName(), httpHost.getPort()), connectionTimeout);
        return create(socket, this.params);
    }
}
