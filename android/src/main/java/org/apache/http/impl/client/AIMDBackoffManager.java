package org.apache.http.impl.client;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.BackoffManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;

public class AIMDBackoffManager implements BackoffManager {
    private double backoffFactor;
    private int cap;
    private final Clock clock;
    private final ConnPoolControl<HttpRoute> connPerRoute;
    private long coolDown;
    private final Map<HttpRoute, Long> lastRouteBackoffs;
    private final Map<HttpRoute, Long> lastRouteProbes;

    public AIMDBackoffManager(ConnPoolControl<HttpRoute> connPoolControl) {
        this(connPoolControl, new SystemClock());
    }

    AIMDBackoffManager(ConnPoolControl<HttpRoute> connPoolControl, Clock clock) {
        this.coolDown = 5000;
        this.backoffFactor = ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR;
        this.cap = 2;
        this.clock = clock;
        this.connPerRoute = connPoolControl;
        this.lastRouteProbes = new HashMap();
        this.lastRouteBackoffs = new HashMap();
    }

    private int getBackedOffPoolSize(int i) {
        return i <= 1 ? 1 : (int) Math.floor(this.backoffFactor * ((double) i));
    }

    private Long getLastUpdate(Map<HttpRoute, Long> map, HttpRoute httpRoute) {
        Long l = (Long) map.get(httpRoute);
        return l == null ? Long.valueOf(0) : l;
    }

    public void backOff(HttpRoute httpRoute) {
        synchronized (this.connPerRoute) {
            int maxPerRoute = this.connPerRoute.getMaxPerRoute(httpRoute);
            Long lastUpdate = getLastUpdate(this.lastRouteBackoffs, httpRoute);
            long currentTime = this.clock.getCurrentTime();
            if (currentTime - lastUpdate.longValue() < this.coolDown) {
                return;
            }
            this.connPerRoute.setMaxPerRoute(httpRoute, getBackedOffPoolSize(maxPerRoute));
            this.lastRouteBackoffs.put(httpRoute, Long.valueOf(currentTime));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void probe(org.apache.http.conn.routing.HttpRoute r11) {
        /*
        r10 = this;
        r1 = r10.connPerRoute;
        monitor-enter(r1);
        r0 = r10.connPerRoute;	 Catch:{ all -> 0x004b }
        r0 = r0.getMaxPerRoute(r11);	 Catch:{ all -> 0x004b }
        r2 = r10.cap;	 Catch:{ all -> 0x004b }
        if (r0 < r2) goto L_0x004e;
    L_0x000d:
        r0 = r10.cap;	 Catch:{ all -> 0x004b }
    L_0x000f:
        r2 = r10.lastRouteProbes;	 Catch:{ all -> 0x004b }
        r2 = r10.getLastUpdate(r2, r11);	 Catch:{ all -> 0x004b }
        r3 = r10.lastRouteBackoffs;	 Catch:{ all -> 0x004b }
        r3 = r10.getLastUpdate(r3, r11);	 Catch:{ all -> 0x004b }
        r4 = r10.clock;	 Catch:{ all -> 0x004b }
        r4 = r4.getCurrentTime();	 Catch:{ all -> 0x004b }
        r6 = r2.longValue();	 Catch:{ all -> 0x004b }
        r6 = r4 - r6;
        r8 = r10.coolDown;	 Catch:{ all -> 0x004b }
        r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r2 < 0) goto L_0x0039;
    L_0x002d:
        r2 = r3.longValue();	 Catch:{ all -> 0x004b }
        r2 = r4 - r2;
        r6 = r10.coolDown;	 Catch:{ all -> 0x004b }
        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r2 >= 0) goto L_0x003b;
    L_0x0039:
        monitor-exit(r1);	 Catch:{ all -> 0x004b }
    L_0x003a:
        return;
    L_0x003b:
        r2 = r10.connPerRoute;	 Catch:{ all -> 0x004b }
        r2.setMaxPerRoute(r11, r0);	 Catch:{ all -> 0x004b }
        r0 = r10.lastRouteProbes;	 Catch:{ all -> 0x004b }
        r2 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x004b }
        r0.put(r11, r2);	 Catch:{ all -> 0x004b }
        monitor-exit(r1);	 Catch:{ all -> 0x004b }
        goto L_0x003a;
    L_0x004b:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x004b }
        throw r0;
    L_0x004e:
        r0 = r0 + 1;
        goto L_0x000f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.client.AIMDBackoffManager.probe(org.apache.http.conn.routing.HttpRoute):void");
    }

    public void setBackoffFactor(double d) {
        if (d <= 0.0d || d >= 1.0d) {
            throw new IllegalArgumentException("backoffFactor must be 0.0 < f < 1.0");
        }
        this.backoffFactor = d;
    }

    public void setCooldownMillis(long j) {
        if (this.coolDown <= 0) {
            throw new IllegalArgumentException("cooldownMillis must be positive");
        }
        this.coolDown = j;
    }

    public void setPerHostConnectionCap(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("perHostConnectionCap must be >= 1");
        }
        this.cap = i;
    }
}
