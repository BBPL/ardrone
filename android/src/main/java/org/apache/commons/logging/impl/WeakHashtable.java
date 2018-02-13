package org.apache.commons.logging.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public final class WeakHashtable extends Hashtable {
    private static final int MAX_CHANGES_BEFORE_PURGE = 100;
    private static final int PARTIAL_PURGE_COUNT = 10;
    private int changeCount = 0;
    private ReferenceQueue queue = new ReferenceQueue();

    class C12921 implements Enumeration {
        private final WeakHashtable this$0;
        private final Enumeration val$enumer;

        C12921(WeakHashtable weakHashtable, Enumeration enumeration) {
            this.this$0 = weakHashtable;
            this.val$enumer = enumeration;
        }

        public boolean hasMoreElements() {
            return this.val$enumer.hasMoreElements();
        }

        public Object nextElement() {
            return Referenced.access$100((Referenced) this.val$enumer.nextElement());
        }
    }

    private static final class Entry implements java.util.Map.Entry {
        private final Object key;
        private final Object value;

        private Entry(Object obj, Object obj2) {
            this.key = obj;
            this.value = obj2;
        }

        Entry(Object obj, Object obj2, C12921 c12921) {
            this(obj, obj2);
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry) obj;
            if (getKey() == null) {
                if (entry.getKey() != null) {
                    return false;
                }
            } else if (!getKey().equals(entry.getKey())) {
                return false;
            }
            if (getValue() == null) {
                if (entry.getValue() != null) {
                    return false;
                }
            } else if (!getValue().equals(entry.getValue())) {
                return false;
            }
            return true;
        }

        public Object getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = getKey() == null ? 0 : getKey().hashCode();
            if (getValue() != null) {
                i = getValue().hashCode();
            }
            return hashCode ^ i;
        }

        public Object setValue(Object obj) {
            throw new UnsupportedOperationException("Entry.setValue is not supported.");
        }
    }

    private static final class Referenced {
        private final int hashCode;
        private final WeakReference reference;

        private Referenced(Object obj) {
            this.reference = new WeakReference(obj);
            this.hashCode = obj.hashCode();
        }

        private Referenced(Object obj, ReferenceQueue referenceQueue) {
            this.reference = new WeakKey(obj, referenceQueue, this, null);
            this.hashCode = obj.hashCode();
        }

        Referenced(Object obj, ReferenceQueue referenceQueue, C12921 c12921) {
            this(obj, referenceQueue);
        }

        Referenced(Object obj, C12921 c12921) {
            this(obj);
        }

        static Object access$100(Referenced referenced) {
            return referenced.getValue();
        }

        private Object getValue() {
            return this.reference.get();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj instanceof Referenced) {
                Referenced referenced = (Referenced) obj;
                Object value = getValue();
                Object value2 = referenced.getValue();
                if (value != null) {
                    return value.equals(value2);
                }
                boolean z2 = value2 == null;
                if (!z2) {
                    z = z2;
                } else if (hashCode() != referenced.hashCode()) {
                    return false;
                }
            }
            z = false;
            return z;
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    private static final class WeakKey extends WeakReference {
        private final Referenced referenced;

        private WeakKey(Object obj, ReferenceQueue referenceQueue, Referenced referenced) {
            super(obj, referenceQueue);
            this.referenced = referenced;
        }

        WeakKey(Object obj, ReferenceQueue referenceQueue, Referenced referenced, C12921 c12921) {
            this(obj, referenceQueue, referenced);
        }

        static Referenced access$400(WeakKey weakKey) {
            return weakKey.getReferenced();
        }

        private Referenced getReferenced() {
            return this.referenced;
        }
    }

    private void purge() {
        synchronized (this.queue) {
            while (true) {
                WeakKey weakKey = (WeakKey) this.queue.poll();
                if (weakKey != null) {
                    super.remove(WeakKey.access$400(weakKey));
                }
            }
        }
    }

    private void purgeOne() {
        synchronized (this.queue) {
            WeakKey weakKey = (WeakKey) this.queue.poll();
            if (weakKey != null) {
                super.remove(WeakKey.access$400(weakKey));
            }
        }
    }

    public boolean containsKey(Object obj) {
        return super.containsKey(new Referenced(obj, null));
    }

    public Enumeration elements() {
        purge();
        return super.elements();
    }

    public Set entrySet() {
        purge();
        Set<java.util.Map.Entry> entrySet = super.entrySet();
        Set hashSet = new HashSet();
        for (java.util.Map.Entry entry : entrySet) {
            Object access$100 = Referenced.access$100((Referenced) entry.getKey());
            Object value = entry.getValue();
            if (access$100 != null) {
                hashSet.add(new Entry(access$100, value, null));
            }
        }
        return hashSet;
    }

    public Object get(Object obj) {
        return super.get(new Referenced(obj, null));
    }

    public boolean isEmpty() {
        purge();
        return super.isEmpty();
    }

    public Set keySet() {
        purge();
        Set<Referenced> keySet = super.keySet();
        Set hashSet = new HashSet();
        for (Referenced access$100 : keySet) {
            Object access$1002 = Referenced.access$100(access$100);
            if (access$1002 != null) {
                hashSet.add(access$1002);
            }
        }
        return hashSet;
    }

    public Enumeration keys() {
        purge();
        return new C12921(this, super.keys());
    }

    public Object put(Object obj, Object obj2) {
        if (obj == null) {
            throw new NullPointerException("Null keys are not allowed");
        } else if (obj2 == null) {
            throw new NullPointerException("Null values are not allowed");
        } else {
            int i = this.changeCount;
            this.changeCount = i + 1;
            if (i > 100) {
                purge();
                this.changeCount = 0;
            } else if (this.changeCount % 10 == 0) {
                purgeOne();
            }
            return super.put(new Referenced(obj, this.queue, null), obj2);
        }
    }

    public void putAll(Map map) {
        if (map != null) {
            for (java.util.Map.Entry entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    protected void rehash() {
        purge();
        super.rehash();
    }

    public Object remove(Object obj) {
        int i = this.changeCount;
        this.changeCount = i + 1;
        if (i > 100) {
            purge();
            this.changeCount = 0;
        } else if (this.changeCount % 10 == 0) {
            purgeOne();
        }
        return super.remove(new Referenced(obj, null));
    }

    public int size() {
        purge();
        return super.size();
    }

    public String toString() {
        purge();
        return super.toString();
    }

    public Collection values() {
        purge();
        return super.values();
    }
}
