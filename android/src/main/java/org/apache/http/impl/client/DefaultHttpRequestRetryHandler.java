package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

@Immutable
public class DefaultHttpRequestRetryHandler implements HttpRequestRetryHandler {
    private final boolean requestSentRetryEnabled;
    private final int retryCount;

    public DefaultHttpRequestRetryHandler() {
        this(3, false);
    }

    public DefaultHttpRequestRetryHandler(int i, boolean z) {
        this.retryCount = i;
        this.requestSentRetryEnabled = z;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    protected boolean handleAsIdempotent(HttpRequest httpRequest) {
        return !(httpRequest instanceof HttpEntityEnclosingRequest);
    }

    public boolean isRequestSentRetryEnabled() {
        return this.requestSentRetryEnabled;
    }

    protected boolean requestIsAborted(HttpRequest httpRequest) {
        HttpRequest original = httpRequest instanceof RequestWrapper ? ((RequestWrapper) httpRequest).getOriginal() : httpRequest;
        return (original instanceof HttpUriRequest) && ((HttpUriRequest) original).isAborted();
    }

    public boolean retryRequest(IOException iOException, int i, HttpContext httpContext) {
        if (iOException == null) {
            throw new IllegalArgumentException("Exception parameter may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            if (!(i > this.retryCount || (iOException instanceof InterruptedIOException) || (iOException instanceof UnknownHostException) || (iOException instanceof ConnectException) || (iOException instanceof SSLException))) {
                HttpRequest httpRequest = (HttpRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
                if (!requestIsAborted(httpRequest)) {
                    if (handleAsIdempotent(httpRequest)) {
                        return true;
                    }
                    Boolean bool = (Boolean) httpContext.getAttribute(ExecutionContext.HTTP_REQ_SENT);
                    Object obj = (bool == null || !bool.booleanValue()) ? null : 1;
                    if (obj == null || this.requestSentRetryEnabled) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
