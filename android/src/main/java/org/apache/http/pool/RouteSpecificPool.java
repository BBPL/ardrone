package org.apache.http.pool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>> {
    private final LinkedList<E> available = new LinkedList();
    private final Set<E> leased = new HashSet();
    private final LinkedList<PoolEntryFuture<E>> pending = new LinkedList();
    private final T route;

    RouteSpecificPool(T t) {
        this.route = t;
    }

    public E add(C c) {
        E createEntry = createEntry(c);
        this.leased.add(createEntry);
        return createEntry;
    }

    protected abstract E createEntry(C c);

    public void free(E e, boolean z) {
        if (e == null) {
            throw new IllegalArgumentException("Pool entry may not be null");
        } else if (!this.leased.remove(e)) {
            throw new IllegalStateException("Entry " + e + " has not been leased from this pool");
        } else if (z) {
            this.available.addFirst(e);
        }
    }

    public int getAllocatedCount() {
        return this.available.size() + this.leased.size();
    }

    public int getAvailableCount() {
        return this.available.size();
    }

    public E getFree(Object obj) {
        if (!this.available.isEmpty()) {
            Iterator it;
            PoolEntry poolEntry;
            if (obj != null) {
                it = this.available.iterator();
                while (it.hasNext()) {
                    poolEntry = (PoolEntry) it.next();
                    if (obj.equals(poolEntry.getState())) {
                        it.remove();
                        this.leased.add(poolEntry);
                        return poolEntry;
                    }
                }
            }
            it = this.available.iterator();
            while (it.hasNext()) {
                poolEntry = (PoolEntry) it.next();
                if (poolEntry.getState() == null) {
                    it.remove();
                    this.leased.add(poolEntry);
                    return poolEntry;
                }
            }
        }
        return null;
    }

    public E getLastUsed() {
        return !this.available.isEmpty() ? (PoolEntry) this.available.getLast() : null;
    }

    public int getLeasedCount() {
        return this.leased.size();
    }

    public int getPendingCount() {
        return this.pending.size();
    }

    public final T getRoute() {
        return this.route;
    }

    public PoolEntryFuture<E> nextPending() {
        return (PoolEntryFuture) this.pending.poll();
    }

    public void queue(PoolEntryFuture<E> poolEntryFuture) {
        if (poolEntryFuture != null) {
            this.pending.add(poolEntryFuture);
        }
    }

    public boolean remove(E e) {
        if (e != null) {
            return this.available.remove(e) || this.leased.remove(e);
        } else {
            throw new IllegalArgumentException("Pool entry may not be null");
        }
    }

    public void shutdown() {
        Iterator it = this.pending.iterator();
        while (it.hasNext()) {
            ((PoolEntryFuture) it.next()).cancel(true);
        }
        this.pending.clear();
        it = this.available.iterator();
        while (it.hasNext()) {
            ((PoolEntry) it.next()).close();
        }
        this.available.clear();
        for (PoolEntry close : this.leased) {
            close.close();
        }
        this.leased.clear();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[route: ");
        stringBuilder.append(this.route);
        stringBuilder.append("][leased: ");
        stringBuilder.append(this.leased.size());
        stringBuilder.append("][available: ");
        stringBuilder.append(this.available.size());
        stringBuilder.append("][pending: ");
        stringBuilder.append(this.pending.size());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void unqueue(PoolEntryFuture<E> poolEntryFuture) {
        if (poolEntryFuture != null) {
            this.pending.remove(poolEntryFuture);
        }
    }
}
