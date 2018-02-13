package org.apache.http.impl.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class RedirectLocations {
    private final List<URI> all = new ArrayList();
    private final Set<URI> unique = new HashSet();

    public void add(URI uri) {
        this.unique.add(uri);
        this.all.add(uri);
    }

    public boolean contains(URI uri) {
        return this.unique.contains(uri);
    }

    public List<URI> getAll() {
        return new ArrayList(this.all);
    }

    public boolean remove(URI uri) {
        boolean remove = this.unique.remove(uri);
        if (remove) {
            Iterator it = this.all.iterator();
            while (it.hasNext()) {
                if (((URI) it.next()).equals(uri)) {
                    it.remove();
                }
            }
        }
        return remove;
    }
}
