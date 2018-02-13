package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.mortbay.jetty.HttpHeaders;

@Immutable
public class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy {
    private boolean canResponseHaveBody(HttpResponse httpResponse) {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        return (statusCode < 200 || statusCode == 204 || statusCode == 304 || statusCode == 205) ? false : true;
    }

    protected TokenIterator createTokenIterator(HeaderIterator headerIterator) {
        return new BasicTokenIterator(headerIterator);
    }

    public boolean keepAlive(HttpResponse httpResponse, HttpContext httpContext) {
        if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null.");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null.");
        } else {
            ProtocolVersion protocolVersion = httpResponse.getStatusLine().getProtocolVersion();
            Header firstHeader = httpResponse.getFirstHeader("Transfer-Encoding");
            if (firstHeader != null) {
                if (!"chunked".equalsIgnoreCase(firstHeader.getValue())) {
                    return false;
                }
            } else if (canResponseHaveBody(httpResponse)) {
                Header[] headers = httpResponse.getHeaders("Content-Length");
                if (headers.length != 1) {
                    return false;
                }
                try {
                    if (Integer.parseInt(headers[0].getValue()) < 0) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            HeaderIterator headerIterator = httpResponse.headerIterator("Connection");
            if (!headerIterator.hasNext()) {
                headerIterator = httpResponse.headerIterator(HttpHeaders.PROXY_CONNECTION);
            }
            if (headerIterator.hasNext()) {
                try {
                    TokenIterator createTokenIterator = createTokenIterator(headerIterator);
                    int i = 0;
                    while (createTokenIterator.hasNext()) {
                        String nextToken = createTokenIterator.nextToken();
                        if (HTTP.CONN_CLOSE.equalsIgnoreCase(nextToken)) {
                            return false;
                        }
                        if ("Keep-Alive".equalsIgnoreCase(nextToken)) {
                            i = 1;
                        }
                    }
                    if (i != 0) {
                        return true;
                    }
                } catch (ParseException e2) {
                    return false;
                }
            }
            return !protocolVersion.lessEquals(HttpVersion.HTTP_1_0);
        }
    }
}
