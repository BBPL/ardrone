package org.apache.http.impl.client;

import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.protocol.HttpContext;

@Immutable
public class DefaultServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {
    private final int maxRetries;
    private final long retryInterval;

    public DefaultServiceUnavailableRetryStrategy() {
        this(1, 1000);
    }

    public DefaultServiceUnavailableRetryStrategy(int i, int i2) {
        if (i < 1) {
            throw new IllegalArgumentException("MaxRetries must be greater than 1");
        } else if (i2 < 1) {
            throw new IllegalArgumentException("Retry interval must be greater than 1");
        } else {
            this.maxRetries = i;
            this.retryInterval = (long) i2;
        }
    }

    public long getRetryInterval() {
        return this.retryInterval;
    }

    public boolean retryRequest(HttpResponse httpResponse, int i, HttpContext httpContext) {
        return i <= this.maxRetries && httpResponse.getStatusLine().getStatusCode() == 503;
    }
}
