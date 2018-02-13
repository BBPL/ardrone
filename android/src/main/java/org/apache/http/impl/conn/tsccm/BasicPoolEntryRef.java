package org.apache.http.impl.conn.tsccm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import org.apache.http.conn.routing.HttpRoute;

@Deprecated
public class BasicPoolEntryRef extends WeakReference<BasicPoolEntry> {
    private final HttpRoute route;

    public BasicPoolEntryRef(BasicPoolEntry basicPoolEntry, ReferenceQueue<Object> referenceQueue) {
        super(basicPoolEntry, referenceQueue);
        if (basicPoolEntry == null) {
            throw new IllegalArgumentException("Pool entry must not be null.");
        }
        this.route = basicPoolEntry.getPlannedRoute();
    }

    public final HttpRoute getRoute() {
        return this.route;
    }
}
