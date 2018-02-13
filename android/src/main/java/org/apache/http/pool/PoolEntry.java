package org.apache.http.pool;

import java.util.concurrent.TimeUnit;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public abstract class PoolEntry<T, C> {
    private final C conn;
    private final long created;
    @GuardedBy("this")
    private long expiry;
    private final String id;
    private final T route;
    private volatile Object state;
    @GuardedBy("this")
    private long updated;
    private final long validUnit;

    public PoolEntry(String str, T t, C c) {
        this(str, t, c, 0, TimeUnit.MILLISECONDS);
    }

    public PoolEntry(String str, T t, C c, long j, TimeUnit timeUnit) {
        if (t == null) {
            throw new IllegalArgumentException("Route may not be null");
        } else if (c == null) {
            throw new IllegalArgumentException("Connection may not be null");
        } else if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit may not be null");
        } else {
            this.id = str;
            this.route = t;
            this.conn = c;
            this.created = System.currentTimeMillis();
            if (j > 0) {
                this.validUnit = this.created + timeUnit.toMillis(j);
            } else {
                this.validUnit = Long.MAX_VALUE;
            }
            this.expiry = this.validUnit;
        }
    }

    public abstract void close();

    public C getConnection() {
        return this.conn;
    }

    public long getCreated() {
        return this.created;
    }

    public long getExpiry() {
        long j;
        synchronized (this) {
            j = this.expiry;
        }
        return j;
    }

    public String getId() {
        return this.id;
    }

    public T getRoute() {
        return this.route;
    }

    public Object getState() {
        return this.state;
    }

    public long getUpdated() {
        long j;
        synchronized (this) {
            j = this.updated;
        }
        return j;
    }

    public long getValidUnit() {
        return this.validUnit;
    }

    public abstract boolean isClosed();

    public boolean isExpired(long j) {
        boolean z;
        synchronized (this) {
            z = j >= this.expiry;
        }
        return z;
    }

    public void setState(Object obj) {
        this.state = obj;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[id:");
        stringBuilder.append(this.id);
        stringBuilder.append("][route:");
        stringBuilder.append(this.route);
        stringBuilder.append("][state:");
        stringBuilder.append(this.state);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void updateExpiry(long j, TimeUnit timeUnit) {
        synchronized (this) {
            if (timeUnit == null) {
                throw new IllegalArgumentException("Time unit may not be null");
            }
            this.updated = System.currentTimeMillis();
            this.expiry = Math.min(j > 0 ? this.updated + timeUnit.toMillis(j) : Long.MAX_VALUE, this.validUnit);
        }
    }
}
