package org.apache.http.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.HttpTransportMetrics;

@NotThreadSafe
public class HttpConnectionMetricsImpl implements HttpConnectionMetrics {
    public static final String RECEIVED_BYTES_COUNT = "http.received-bytes-count";
    public static final String REQUEST_COUNT = "http.request-count";
    public static final String RESPONSE_COUNT = "http.response-count";
    public static final String SENT_BYTES_COUNT = "http.sent-bytes-count";
    private final HttpTransportMetrics inTransportMetric;
    private Map<String, Object> metricsCache;
    private final HttpTransportMetrics outTransportMetric;
    private long requestCount = 0;
    private long responseCount = 0;

    public HttpConnectionMetricsImpl(HttpTransportMetrics httpTransportMetrics, HttpTransportMetrics httpTransportMetrics2) {
        this.inTransportMetric = httpTransportMetrics;
        this.outTransportMetric = httpTransportMetrics2;
    }

    public Object getMetric(String str) {
        Object obj = this.metricsCache != null ? this.metricsCache.get(str) : null;
        return obj == null ? REQUEST_COUNT.equals(str) ? new Long(this.requestCount) : RESPONSE_COUNT.equals(str) ? new Long(this.responseCount) : RECEIVED_BYTES_COUNT.equals(str) ? this.inTransportMetric != null ? new Long(this.inTransportMetric.getBytesTransferred()) : null : SENT_BYTES_COUNT.equals(str) ? this.outTransportMetric != null ? new Long(this.outTransportMetric.getBytesTransferred()) : null : obj : obj;
    }

    public long getReceivedBytesCount() {
        return this.inTransportMetric != null ? this.inTransportMetric.getBytesTransferred() : -1;
    }

    public long getRequestCount() {
        return this.requestCount;
    }

    public long getResponseCount() {
        return this.responseCount;
    }

    public long getSentBytesCount() {
        return this.outTransportMetric != null ? this.outTransportMetric.getBytesTransferred() : -1;
    }

    public void incrementRequestCount() {
        this.requestCount++;
    }

    public void incrementResponseCount() {
        this.responseCount++;
    }

    public void reset() {
        if (this.outTransportMetric != null) {
            this.outTransportMetric.reset();
        }
        if (this.inTransportMetric != null) {
            this.inTransportMetric.reset();
        }
        this.requestCount = 0;
        this.responseCount = 0;
        this.metricsCache = null;
    }

    public void setMetric(String str, Object obj) {
        if (this.metricsCache == null) {
            this.metricsCache = new HashMap();
        }
        this.metricsCache.put(str, obj);
    }
}
