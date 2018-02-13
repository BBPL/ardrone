package org.apache.http.protocol;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.http.HttpConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;

@Immutable
public class RequestTargetHost implements HttpRequestInterceptor {
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            ProtocolVersion protocolVersion = httpRequest.getRequestLine().getProtocolVersion();
            if ((!httpRequest.getRequestLine().getMethod().equalsIgnoreCase("CONNECT") || !protocolVersion.lessEquals(HttpVersion.HTTP_1_0)) && !httpRequest.containsHeader("Host")) {
                HttpHost httpHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (httpHost == null) {
                    HttpConnection httpConnection = (HttpConnection) httpContext.getAttribute(ExecutionContext.HTTP_CONNECTION);
                    if (httpConnection instanceof HttpInetConnection) {
                        InetAddress remoteAddress = ((HttpInetConnection) httpConnection).getRemoteAddress();
                        int remotePort = ((HttpInetConnection) httpConnection).getRemotePort();
                        if (remoteAddress != null) {
                            httpHost = new HttpHost(remoteAddress.getHostName(), remotePort);
                        }
                    }
                    if (httpHost == null) {
                        if (!protocolVersion.lessEquals(HttpVersion.HTTP_1_0)) {
                            throw new ProtocolException("Target host missing");
                        }
                        return;
                    }
                }
                httpRequest.addHeader("Host", httpHost.toHostString());
            }
        }
    }
}
