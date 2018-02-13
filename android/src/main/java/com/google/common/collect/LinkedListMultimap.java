package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public class LinkedListMultimap<K, V> implements ListMultimap<K, V>, Serializable {
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 0;
    private transient List<Entry<K, V>> entries;
    private transient Node<K, V> head;
    private transient Multiset<K> keyCount;
    private transient Set<K> keySet;
    private transient Map<K, Node<K, V>> keyToKeyHead;
    private transient Map<K, Node<K, V>> keyToKeyTail;
    private transient Multiset<K> keys;
    private transient Map<K, Collection<V>> map;
    private transient Node<K, V> tail;
    private transient List<V> valuesList;

    class C06122 extends ImprovedAbstractSet<K> {
        C06122() {
        }

        public boolean contains(Object obj) {
            return LinkedListMultimap.this.containsKey(obj);
        }

        public Iterator<K> iterator() {
            return new DistinctKeyIterator();
        }

        public boolean remove(Object obj) {
            return !LinkedListMultimap.this.removeAll(obj).isEmpty();
        }

        public int size() {
            return LinkedListMultimap.this.keyCount.elementSet().size();
        }
    }

    class C06143 extends AbstractSequentialList<V> {
        C06143() {
        }

        public ListIterator<V> listIterator(int i) {
            final Object nodeIterator = new NodeIterator(i);
            return new TransformedListIterator<Node<K, V>, V>(nodeIterator) {
                public void set(V v) {
                    nodeIterator.setValue(v);
                }

                V transform(Node<K, V> node) {
                    return node.value;
                }
            };
        }

        public int size() {
            return LinkedListMultimap.this.keyCount.size();
        }
    }

    class C06175 extends AbstractSequentialList<Entry<K, V>> {
        C06175() {
        }

        public ListIterator<Entry<K, V>> listIterator(int i) {
            return new TransformedListIterator<Node<K, V>, Entry<K, V>>(new NodeIterator(i)) {
                Entry<K, V> transform(Node<K, V> node) {
                    return LinkedListMultimap.createEntry(node);
                }
            };
        }

        public int size() {
            return LinkedListMultimap.this.keyCount.size();
        }
    }

    class C06206 extends AsMap<K, V> {
        C06206() {
        }

        Iterator<Entry<K, Collection<V>>> entryIterator() {
            return new TransformedIterator<K, Entry<K, Collection<V>>>(new DistinctKeyIterator()) {
                Entry<K, Collection<V>> transform(final K k) {
                    return new AbstractMapEntry<K, Collection<V>>() {
                        public K getKey() {
                            return k;
                        }

                        public Collection<V> getValue() {
                            return LinkedListMultimap.this.get(k);
                        }
                    };
                }
            };
        }

        Multimap<K, V> multimap() {
            return LinkedListMultimap.this;
        }

        public int size() {
            return LinkedListMultimap.this.keyCount.elementSet().size();
        }
    }

    private class DistinctKeyIterator implements Iterator<K> {
        Node<K, V> current;
        Node<K, V> next;
        final Set<K> seenKeys;

        private DistinctKeyIterator() {
            this.seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
            this.next = LinkedListMultimap.this.head;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public K next() {
            LinkedListMultimap.checkElement(this.next);
            this.current = this.next;
            this.seenKeys.add(this.current.key);
            do {
                this.next = this.next.next;
                if (this.next == null) {
                    break;
                }
            } while (!this.seenKeys.add(this.next.key));
            return this.current.key;
        }

        public void remove() {
            Preconditions.checkState(this.current != null);
            LinkedListMultimap.this.removeAllNodes(this.current.key);
            this.current = null;
        }
    }

    private class MultisetView extends AbstractMultiset<K> {
        private MultisetView() {
        }

        public int count(Object obj) {
            return LinkedListMultimap.this.keyCount.count(obj);
        }

        int distinctElements() {
            return elementSet().size();
        }

        public Set<K> elementSet() {
            return LinkedListMultimap.this.keySet();
        }

        Iterator<Multiset.Entry<K>> entryIterator() {
            return new TransformedIterator<K, Multiset.Entry<K>>(new DistinctKeyIterator()) {
                Multiset.Entry<K> transform(final K k) {
                    return new AbstractEntry<K>() {
                        public int getCount() {
                            return LinkedListMultimap.this.keyCount.count(k);
                        }

                        public K getElement() {
                            return k;
                        }
                    };
                }
            };
        }

        public boolean equals(@Nullable Object obj) {
            return LinkedListMultimap.this.keyCount.equals(obj);
        }

        public int hashCode() {
            return LinkedListMultimap.this.keyCount.hashCode();
        }

        public Iterator<K> iterator() {
            return new TransformedIterator<Node<K, V>, K>(new NodeIterator()) {
                K transform(Node<K, V> node) {
                    return node.key;
                }
            };
        }

        public int remove(@Nullable Object obj, int i) {
            Preconditions.checkArgument(i >= 0);
            int count = count(obj);
            Iterator valueForKeyIterator = new ValueForKeyIterator(obj);
            while (i > 0 && valueForKeyIterator.hasNext()) {
                valueForKeyIterator.next();
                valueForKeyIterator.remove();
                i--;
            }
            return count;
        }

        public int size() {
            return LinkedListMultimap.this.keyCount.size();
        }

        public String toString() {
            return LinkedListMultimap.this.keyCount.toString();
        }
    }

    private static final class Node<K, V> {
        final K key;
        Node<K, V> next;
        Node<K, V> nextSibling;
        Node<K, V> previous;
        Node<K, V> previousSibling;
        V value;

        Node(@Nullable K k, @Nullable V v) {
            this.key = k;
            this.value = v;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    private class NodeIterator implements ListIterator<Node<K, V>> {
        Node<K, V> current;
        Node<K, V> next;
        int nextIndex;
        Node<K, V> previous;

        NodeIterator() {
            this.next = LinkedListMultimap.this.head;
        }

        NodeIterator(int i) {
            int size = LinkedListMultimap.this.size();
            Preconditions.checkPositionIndex(i, size);
            if (i >= size / 2) {
                this.previous = LinkedListMultimap.this.tail;
                this.nextIndex = size;
                while (i < size) {
                    previous();
                    i++;
                }
            } else {
                this.next = LinkedListMultimap.this.head;
                while (i > 0) {
                    next();
                    i--;
                }
            }
            this.current = null;
        }

        public void add(Node<K, V> node) {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public boolean hasPrevious() {
            return this.previous != null;
        }

        public Node<K, V> next() {
            LinkedListMultimap.checkElement(this.next);
            Node node = this.next;
            this.current = node;
            this.previous = node;
            this.next = this.next.next;
            this.nextIndex++;
            return this.current;
        }

        public int nextIndex() {
            return this.nextIndex;
        }

        public Node<K, V> previous() {
            LinkedListMultimap.checkElement(this.previous);
            Node node = this.previous;
            this.current = node;
            this.next = node;
            this.previous = this.previous.previous;
            this.nextIndex--;
            return this.current;
        }

        public int previousIndex() {
            return this.nextIndex - 1;
        }

        public void remove() {
            Preconditions.checkState(this.current != null);
            if (this.current != this.next) {
                this.previous = this.current.previous;
                this.nextIndex--;
            } else {
                this.next = this.current.next;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }

        public void set(Node<K, V> node) {
            throw new UnsupportedOperationException();
        }

        void setValue(V v) {
            Preconditions.checkState(this.current != null);
            this.current.value = v;
        }
    }

    private class ValueForKeyIterator implements ListIterator<V> {
        Node<K, V> current;
        final Object key;
        Node<K, V> next;
        int nextIndex;
        Node<K, V> previous;

        ValueForKeyIterator(@Nullable Object obj) {
            this.key = obj;
            this.next = (Node) LinkedListMultimap.this.keyToKeyHead.get(obj);
        }

        public ValueForKeyIterator(@Nullable Object obj, int i) {
            int count = LinkedListMultimap.this.keyCount.count(obj);
            Preconditions.checkPositionIndex(i, count);
            if (i >= count / 2) {
                this.previous = (Node) LinkedListMultimap.this.keyToKeyTail.get(obj);
                this.nextIndex = count;
                while (i < count) {
                    previous();
                    i++;
                }
            } else {
                this.next = (Node) LinkedListMultimap.this.keyToKeyHead.get(obj);
                while (i > 0) {
                    next();
                    i--;
                }
            }
            this.key = obj;
            this.current = null;
        }

        public void add(V v) {
            this.previous = LinkedListMultimap.this.addNode(this.key, v, this.next);
            this.nextIndex++;
            this.current = null;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public boolean hasPrevious() {
            return this.previous != null;
        }

        public V next() {
            LinkedListMultimap.checkElement(this.next);
            Node node = this.next;
            this.current = node;
            this.previous = node;
            this.next = this.next.nextSibling;
            this.nextIndex++;
            return this.current.value;
        }

        public int nextIndex() {
            return this.nextIndex;
        }

        public V previous() {
            LinkedListMultimap.checkElement(this.previous);
            Node node = this.previous;
            this.current = node;
            this.next = node;
            this.previous = this.previous.previousSibling;
            this.nextIndex--;
            return this.current.value;
        }

        public int previousIndex() {
            return this.nextIndex - 1;
        }

        public void remove() {
            Preconditions.checkState(this.current != null);
            if (this.current != this.next) {
                this.previous = this.current.previousSibling;
                this.nextIndex--;
            } else {
                this.next = this.current.nextSibling;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }

        public void set(V v) {
            Preconditions.checkState(this.current != null);
            this.current.value = v;
        }
    }

    LinkedListMultimap() {
        this.keyCount = LinkedHashMultiset.create();
        this.keyToKeyHead = Maps.newHashMap();
        this.keyToKeyTail = Maps.newHashMap();
    }

    private LinkedListMultimap(int i) {
        this.keyCount = LinkedHashMultiset.create(i);
        this.keyToKeyHead = Maps.newHashMapWithExpectedSize(i);
        this.keyToKeyTail = Maps.newHashMapWithExpectedSize(i);
    }

    private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
        this(multimap.keySet().size());
        putAll(multimap);
    }

    private Node<K, V> addNode(@Nullable K k, @Nullable V v, @Nullable Node<K, V> node) {
        Node<K, V> node2 = new Node(k, v);
        if (this.head == null) {
            this.tail = node2;
            this.head = node2;
            this.keyToKeyHead.put(k, node2);
            this.keyToKeyTail.put(k, node2);
        } else if (node == null) {
            this.tail.next = node2;
            node2.previous = this.tail;
            Node node3 = (Node) this.keyToKeyTail.get(k);
            if (node3 == null) {
                this.keyToKeyHead.put(k, node2);
            } else {
                node3.nextSibling = node2;
                node2.previousSibling = node3;
            }
            this.keyToKeyTail.put(k, node2);
            this.tail = node2;
        } else {
            node2.previous = node.previous;
            node2.previousSibling = node.previousSibling;
            node2.next = node;
            node2.nextSibling = node;
            if (node.previousSibling == null) {
                this.keyToKeyHead.put(k, node2);
            } else {
                node.previousSibling.nextSibling = node2;
            }
            if (node.previous == null) {
                this.head = node2;
            } else {
                node.previous.next = node2;
            }
            node.previous = node2;
            node.previousSibling = node2;
        }
        this.keyCount.add(k);
        return node2;
    }

    private static void checkElement(@Nullable Object obj) {
        if (obj == null) {
            throw new NoSuchElementException();
        }
    }

    public static <K, V> LinkedListMultimap<K, V> create() {
        return new LinkedListMultimap();
    }

    public static <K, V> LinkedListMultimap<K, V> create(int i) {
        return new LinkedListMultimap(i);
    }

    public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new LinkedListMultimap((Multimap) multimap);
    }

    private static <K, V> Entry<K, V> createEntry(final Node<K, V> node) {
        return new AbstractMapEntry<K, V>() {
            public K getKey() {
                return node.key;
            }

            public V getValue() {
                return node.value;
            }

            public V setValue(V v) {
                V v2 = node.value;
                node.value = v;
                return v2;
            }
        };
    }

    private List<V> getCopy(@Nullable Object obj) {
        return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(obj)));
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.keyCount = LinkedHashMultiset.create();
        this.keyToKeyHead = Maps.newHashMap();
        this.keyToKeyTail = Maps.newHashMap();
        int readInt = objectInputStream.readInt();
        for (int i = 0; i < readInt; i++) {
            put(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }

    private void removeAllNodes(@Nullable Object obj) {
        Iterator valueForKeyIterator = new ValueForKeyIterator(obj);
        while (valueForKeyIterator.hasNext()) {
            valueForKeyIterator.next();
            valueForKeyIterator.remove();
        }
    }

    private void removeNode(Node<K, V> node) {
        if (node.previous != null) {
            node.previous.next = node.next;
        } else {
            this.head = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        } else {
            this.tail = node.previous;
        }
        if (node.previousSibling != null) {
            node.previousSibling.nextSibling = node.nextSibling;
        } else if (node.nextSibling != null) {
            this.keyToKeyHead.put(node.key, node.nextSibling);
        } else {
            this.keyToKeyHead.remove(node.key);
        }
        if (node.nextSibling != null) {
            node.nextSibling.previousSibling = node.previousSibling;
        } else if (node.previousSibling != null) {
            this.keyToKeyTail.put(node.key, node.previousSibling);
        } else {
            this.keyToKeyTail.remove(node.key);
        }
        this.keyCount.remove(node.key);
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        for (Entry entry : entries()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> map = this.map;
        if (map != null) {
            return map;
        }
        map = new C06206();
        this.map = map;
        return map;
    }

    public void clear() {
        this.head = null;
        this.tail = null;
        this.keyCount.clear();
        this.keyToKeyHead.clear();
        this.keyToKeyTail.clear();
    }

    public boolean containsEntry(@Nullable Object obj, @Nullable Object obj2) {
        Iterator valueForKeyIterator = new ValueForKeyIterator(obj);
        while (valueForKeyIterator.hasNext()) {
            if (Objects.equal(valueForKeyIterator.next(), obj2)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.keyToKeyHead.containsKey(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        Iterator nodeIterator = new NodeIterator();
        while (nodeIterator.hasNext()) {
            if (Objects.equal(((Node) nodeIterator.next()).value, obj)) {
                return true;
            }
        }
        return false;
    }

    public List<Entry<K, V>> entries() {
        List<Entry<K, V>> list = this.entries;
        if (list != null) {
            return list;
        }
        list = new C06175();
        this.entries = list;
        return list;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Multimap)) {
            return false;
        }
        return asMap().equals(((Multimap) obj).asMap());
    }

    public List<V> get(@Nullable final K k) {
        return new AbstractSequentialList<V>() {
            public ListIterator<V> listIterator(int i) {
                return new ValueForKeyIterator(k, i);
            }

            public boolean removeAll(Collection<?> collection) {
                return Iterators.removeAll(iterator(), collection);
            }

            public boolean retainAll(Collection<?> collection) {
                return Iterators.retainAll(iterator(), collection);
            }

            public int size() {
                return LinkedListMultimap.this.keyCount.count(k);
            }
        };
    }

    public int hashCode() {
        return asMap().hashCode();
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        set = new C06122();
        this.keySet = set;
        return set;
    }

    public Multiset<K> keys() {
        Multiset<K> multiset = this.keys;
        if (multiset != null) {
            return multiset;
        }
        multiset = new MultisetView();
        this.keys = multiset;
        return multiset;
    }

    public boolean put(@Nullable K k, @Nullable V v) {
        addNode(k, v, null);
        return true;
    }

    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean z = false;
        for (Entry entry : multimap.entries()) {
            z = put(entry.getKey(), entry.getValue()) | z;
        }
        return z;
    }

    public boolean putAll(@Nullable K k, Iterable<? extends V> iterable) {
        boolean z = false;
        for (Object put : iterable) {
            z |= put(k, put);
        }
        return z;
    }

    public boolean remove(@Nullable Object obj, @Nullable Object obj2) {
        Iterator valueForKeyIterator = new ValueForKeyIterator(obj);
        while (valueForKeyIterator.hasNext()) {
            if (Objects.equal(valueForKeyIterator.next(), obj2)) {
                valueForKeyIterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<V> removeAll(@Nullable Object obj) {
        List<V> copy = getCopy(obj);
        removeAllNodes(obj);
        return copy;
    }

    public List<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        List<V> copy = getCopy(k);
        ListIterator valueForKeyIterator = new ValueForKeyIterator(k);
        Iterator it = iterable.iterator();
        while (valueForKeyIterator.hasNext() && it.hasNext()) {
            valueForKeyIterator.next();
            valueForKeyIterator.set(it.next());
        }
        while (valueForKeyIterator.hasNext()) {
            valueForKeyIterator.next();
            valueForKeyIterator.remove();
        }
        while (it.hasNext()) {
            valueForKeyIterator.add(it.next());
        }
        return copy;
    }

    public int size() {
        return this.keyCount.size();
    }

    public String toString() {
        return asMap().toString();
    }

    public List<V> values() {
        List<V> list = this.valuesList;
        if (list != null) {
            return list;
        }
        list = new C06143();
        this.valuesList = list;
        return list;
    }
}
