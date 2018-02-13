package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.AbstractConnPool;
import org.apache.http.pool.ConnFactory;

class HttpConnPool extends AbstractConnPool<HttpRoute, OperatedClientConnection, HttpPoolEntry> {
    private static AtomicLong COUNTER = new AtomicLong();
    private final Log log;
    private final long timeToLive;
    private final TimeUnit tunit;

    static class InternalConnFactory implements ConnFactory<HttpRoute, OperatedClientConnection> {
        InternalConnFactory() {
        }

        public OperatedClientConnection create(HttpRoute httpRoute) throws IOException {
            return new DefaultClientConnection();
        }
    }

    public HttpConnPool(Log log, int i, int i2, long j, TimeUnit timeUnit) {
        super(new InternalConnFactory(), i, i2);
        this.log = log;
        this.timeToLive = j;
        this.tunit = timeUnit;
    }

    protected HttpPoolEntry createEntry(HttpRoute httpRoute, OperatedClientConnection operatedClientConnection) {
        return new HttpPoolEntry(this.log, Long.toString(COUNTER.getAndIncrement()), httpRoute, operatedClientConnection, this.timeToLive, this.tunit);
    }
}
