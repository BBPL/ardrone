package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class TreeMultiset<E> extends AbstractSortedMultiset<E> implements Serializable {
    @GwtIncompatible("not needed in emulated source")
    private static final long serialVersionUID = 1;
    private final transient AvlNode<E> header;
    private final transient GeneralRange<E> range;
    private final transient Reference<AvlNode<E>> rootReference;

    class C07812 implements Iterator<Entry<E>> {
        AvlNode<E> current = TreeMultiset.this.firstNode();
        Entry<E> prevEntry;

        C07812() {
        }

        public boolean hasNext() {
            if (this.current == null) {
                return false;
            }
            if (!TreeMultiset.this.range.tooHigh(this.current.getElement())) {
                return true;
            }
            this.current = null;
            return false;
        }

        public Entry<E> next() {
            if (hasNext()) {
                Entry<E> access$1400 = TreeMultiset.this.wrapEntry(this.current);
                this.prevEntry = access$1400;
                if (this.current.succ == TreeMultiset.this.header) {
                    this.current = null;
                } else {
                    this.current = this.current.succ;
                }
                return access$1400;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            Preconditions.checkState(this.prevEntry != null);
            TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
            this.prevEntry = null;
        }
    }

    class C07823 implements Iterator<Entry<E>> {
        AvlNode<E> current = TreeMultiset.this.lastNode();
        Entry<E> prevEntry = null;

        C07823() {
        }

        public boolean hasNext() {
            if (this.current == null) {
                return false;
            }
            if (!TreeMultiset.this.range.tooLow(this.current.getElement())) {
                return true;
            }
            this.current = null;
            return false;
        }

        public Entry<E> next() {
            if (hasNext()) {
                Entry<E> access$1400 = TreeMultiset.this.wrapEntry(this.current);
                this.prevEntry = access$1400;
                if (this.current.pred == TreeMultiset.this.header) {
                    this.current = null;
                } else {
                    this.current = this.current.pred;
                }
                return access$1400;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            Preconditions.checkState(this.prevEntry != null);
            TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
            this.prevEntry = null;
        }
    }

    private enum Aggregate {
        SIZE {
            int nodeAggregate(AvlNode<?> avlNode) {
                return avlNode.elemCount;
            }

            long treeAggregate(@Nullable AvlNode<?> avlNode) {
                return avlNode == null ? 0 : avlNode.totalCount;
            }
        },
        DISTINCT {
            int nodeAggregate(AvlNode<?> avlNode) {
                return 1;
            }

            long treeAggregate(@Nullable AvlNode<?> avlNode) {
                return avlNode == null ? 0 : (long) avlNode.distinctElements;
            }
        };

        abstract int nodeAggregate(AvlNode<?> avlNode);

        abstract long treeAggregate(@Nullable AvlNode<?> avlNode);
    }

    private static final class AvlNode<E> extends AbstractEntry<E> {
        private int distinctElements;
        @Nullable
        private final E elem;
        private int elemCount;
        private int height;
        private AvlNode<E> left;
        private AvlNode<E> pred;
        private AvlNode<E> right;
        private AvlNode<E> succ;
        private long totalCount;

        AvlNode(@Nullable E e, int i) {
            Preconditions.checkArgument(i > 0);
            this.elem = e;
            this.elemCount = i;
            this.totalCount = (long) i;
            this.distinctElements = 1;
            this.height = 1;
            this.left = null;
            this.right = null;
        }

        private AvlNode<E> addLeftChild(E e, int i) {
            this.left = new AvlNode(e, i);
            TreeMultiset.successor(this.pred, this.left, this);
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += (long) i;
            return this;
        }

        private AvlNode<E> addRightChild(E e, int i) {
            this.right = new AvlNode(e, i);
            TreeMultiset.successor(this, this.right, this.succ);
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += (long) i;
            return this;
        }

        private int balanceFactor() {
            return height(this.left) - height(this.right);
        }

        @Nullable
        private AvlNode<E> ceiling(Comparator<? super E> comparator, E e) {
            int compare = comparator.compare(e, this.elem);
            if (compare < 0) {
                if (this.left != null) {
                    return (AvlNode) Objects.firstNonNull(this.left.ceiling(comparator, e), this);
                }
            } else if (compare != 0) {
                return this.right == null ? null : this.right.ceiling(comparator, e);
            }
            return this;
        }

        private AvlNode<E> deleteMe() {
            int i = this.elemCount;
            this.elemCount = 0;
            TreeMultiset.successor(this.pred, this.succ);
            if (this.left == null) {
                return this.right;
            }
            if (this.right == null) {
                return this.left;
            }
            if (this.left.height >= this.right.height) {
                AvlNode avlNode = this.pred;
                avlNode.left = this.left.removeMax(avlNode);
                avlNode.right = this.right;
                avlNode.distinctElements = this.distinctElements - 1;
                avlNode.totalCount = this.totalCount - ((long) i);
                return avlNode.rebalance();
            }
            avlNode = this.succ;
            avlNode.right = this.right.removeMin(avlNode);
            avlNode.left = this.left;
            avlNode.distinctElements = this.distinctElements - 1;
            avlNode.totalCount = this.totalCount - ((long) i);
            return avlNode.rebalance();
        }

        @Nullable
        private AvlNode<E> floor(Comparator<? super E> comparator, E e) {
            int compare = comparator.compare(e, this.elem);
            if (compare > 0) {
                if (this.right != null) {
                    return (AvlNode) Objects.firstNonNull(this.right.floor(comparator, e), this);
                }
            } else if (compare != 0) {
                return this.left == null ? null : this.left.floor(comparator, e);
            }
            return this;
        }

        private static int height(@Nullable AvlNode<?> avlNode) {
            return avlNode == null ? 0 : avlNode.height;
        }

        private AvlNode<E> rebalance() {
            switch (balanceFactor()) {
                case -2:
                    if (this.right.balanceFactor() > 0) {
                        this.right = this.right.rotateRight();
                    }
                    return rotateLeft();
                case 2:
                    if (this.left.balanceFactor() < 0) {
                        this.left = this.left.rotateLeft();
                    }
                    return rotateRight();
                default:
                    recomputeHeight();
                    return this;
            }
        }

        private void recompute() {
            recomputeMultiset();
            recomputeHeight();
        }

        private void recomputeHeight() {
            this.height = Math.max(height(this.left), height(this.right)) + 1;
        }

        private void recomputeMultiset() {
            this.distinctElements = (TreeMultiset.distinctElements(this.left) + 1) + TreeMultiset.distinctElements(this.right);
            this.totalCount = (((long) this.elemCount) + totalCount(this.left)) + totalCount(this.right);
        }

        private AvlNode<E> removeMax(AvlNode<E> avlNode) {
            if (this.right == null) {
                return this.left;
            }
            this.right = this.right.removeMax(avlNode);
            this.distinctElements--;
            this.totalCount -= (long) avlNode.elemCount;
            return rebalance();
        }

        private AvlNode<E> removeMin(AvlNode<E> avlNode) {
            if (this.left == null) {
                return this.right;
            }
            this.left = this.left.removeMin(avlNode);
            this.distinctElements--;
            this.totalCount -= (long) avlNode.elemCount;
            return rebalance();
        }

        private AvlNode<E> rotateLeft() {
            Preconditions.checkState(this.right != null);
            AvlNode<E> avlNode = this.right;
            this.right = avlNode.left;
            avlNode.left = this;
            avlNode.totalCount = this.totalCount;
            avlNode.distinctElements = this.distinctElements;
            recompute();
            avlNode.recomputeHeight();
            return avlNode;
        }

        private AvlNode<E> rotateRight() {
            Preconditions.checkState(this.left != null);
            AvlNode<E> avlNode = this.left;
            this.left = avlNode.right;
            avlNode.right = this;
            avlNode.totalCount = this.totalCount;
            avlNode.distinctElements = this.distinctElements;
            recompute();
            avlNode.recomputeHeight();
            return avlNode;
        }

        private static long totalCount(@Nullable AvlNode<?> avlNode) {
            return avlNode == null ? 0 : avlNode.totalCount;
        }

        AvlNode<E> add(Comparator<? super E> comparator, @Nullable E e, int i, int[] iArr) {
            boolean z = false;
            int compare = comparator.compare(e, this.elem);
            AvlNode avlNode;
            int i2;
            if (compare < 0) {
                avlNode = this.left;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return addLeftChild(e, i);
                }
                i2 = avlNode.height;
                this.left = avlNode.add(comparator, e, i, iArr);
                if (iArr[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += (long) i;
                return this.left.height != i2 ? rebalance() : this;
            } else if (compare > 0) {
                avlNode = this.right;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return addRightChild(e, i);
                }
                i2 = avlNode.height;
                this.right = avlNode.add(comparator, e, i, iArr);
                if (iArr[0] == 0) {
                    this.distinctElements++;
                }
                this.totalCount += (long) i;
                return this.right.height != i2 ? rebalance() : this;
            } else {
                iArr[0] = this.elemCount;
                if (((long) this.elemCount) + ((long) i) <= 2147483647L) {
                    z = true;
                }
                Preconditions.checkArgument(z);
                this.elemCount += i;
                this.totalCount += (long) i;
                return this;
            }
        }

        public int count(Comparator<? super E> comparator, E e) {
            int compare = comparator.compare(e, this.elem);
            if (compare < 0) {
                if (this.left != null) {
                    return this.left.count(comparator, e);
                }
            } else if (compare <= 0) {
                return this.elemCount;
            } else {
                if (this.right != null) {
                    return this.right.count(comparator, e);
                }
            }
            return 0;
        }

        public int getCount() {
            return this.elemCount;
        }

        public E getElement() {
            return this.elem;
        }

        AvlNode<E> remove(Comparator<? super E> comparator, @Nullable E e, int i, int[] iArr) {
            int compare = comparator.compare(e, this.elem);
            AvlNode avlNode;
            if (compare < 0) {
                avlNode = this.left;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return this;
                }
                this.left = avlNode.remove(comparator, e, i, iArr);
                if (iArr[0] > 0) {
                    if (i >= iArr[0]) {
                        this.distinctElements--;
                        this.totalCount -= (long) iArr[0];
                    } else {
                        this.totalCount -= (long) i;
                    }
                }
                return iArr[0] != 0 ? rebalance() : this;
            } else if (compare > 0) {
                avlNode = this.right;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return this;
                }
                this.right = avlNode.remove(comparator, e, i, iArr);
                if (iArr[0] > 0) {
                    if (i >= iArr[0]) {
                        this.distinctElements--;
                        this.totalCount -= (long) iArr[0];
                    } else {
                        this.totalCount -= (long) i;
                    }
                }
                return rebalance();
            } else {
                iArr[0] = this.elemCount;
                if (i >= this.elemCount) {
                    return deleteMe();
                }
                this.elemCount -= i;
                this.totalCount -= (long) i;
                return this;
            }
        }

        AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int i, int i2, int[] iArr) {
            int compare = comparator.compare(e, this.elem);
            AvlNode avlNode;
            if (compare < 0) {
                avlNode = this.left;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return (i != 0 || i2 <= 0) ? this : addLeftChild(e, i2);
                } else {
                    this.left = avlNode.setCount(comparator, e, i, i2, iArr);
                    if (iArr[0] == i) {
                        if (i2 == 0 && iArr[0] != 0) {
                            this.distinctElements--;
                        } else if (i2 > 0 && iArr[0] == 0) {
                            this.distinctElements++;
                        }
                        this.totalCount += (long) (i2 - iArr[0]);
                    }
                    return rebalance();
                }
            } else if (compare > 0) {
                avlNode = this.right;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return (i != 0 || i2 <= 0) ? this : addRightChild(e, i2);
                } else {
                    this.right = avlNode.setCount(comparator, e, i, i2, iArr);
                    if (iArr[0] == i) {
                        if (i2 == 0 && iArr[0] != 0) {
                            this.distinctElements--;
                        } else if (i2 > 0 && iArr[0] == 0) {
                            this.distinctElements++;
                        }
                        this.totalCount += (long) (i2 - iArr[0]);
                    }
                    return rebalance();
                }
            } else {
                iArr[0] = this.elemCount;
                if (i != this.elemCount) {
                    return this;
                }
                if (i2 == 0) {
                    return deleteMe();
                }
                this.totalCount += (long) (i2 - this.elemCount);
                this.elemCount = i2;
                return this;
            }
        }

        AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int i, int[] iArr) {
            int compare = comparator.compare(e, this.elem);
            AvlNode avlNode;
            if (compare < 0) {
                avlNode = this.left;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return i > 0 ? addLeftChild(e, i) : this;
                } else {
                    this.left = avlNode.setCount(comparator, e, i, iArr);
                    if (i == 0 && iArr[0] != 0) {
                        this.distinctElements--;
                    } else if (i > 0 && iArr[0] == 0) {
                        this.distinctElements++;
                    }
                    this.totalCount += (long) (i - iArr[0]);
                    return rebalance();
                }
            } else if (compare > 0) {
                avlNode = this.right;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return i > 0 ? addRightChild(e, i) : this;
                } else {
                    this.right = avlNode.setCount(comparator, e, i, iArr);
                    if (i == 0 && iArr[0] != 0) {
                        this.distinctElements--;
                    } else if (i > 0 && iArr[0] == 0) {
                        this.distinctElements++;
                    }
                    this.totalCount += (long) (i - iArr[0]);
                    return rebalance();
                }
            } else {
                iArr[0] = this.elemCount;
                if (i == 0) {
                    return deleteMe();
                }
                this.totalCount += (long) (i - this.elemCount);
                this.elemCount = i;
                return this;
            }
        }

        public String toString() {
            return Multisets.immutableEntry(getElement(), getCount()).toString();
        }
    }

    private static final class Reference<T> {
        @Nullable
        private T value;

        private Reference() {
        }

        public void checkAndSet(@Nullable T t, T t2) {
            if (this.value != t) {
                throw new ConcurrentModificationException();
            }
            this.value = t2;
        }

        @Nullable
        public T get() {
            return this.value;
        }
    }

    TreeMultiset(Reference<AvlNode<E>> reference, GeneralRange<E> generalRange, AvlNode<E> avlNode) {
        super(generalRange.comparator());
        this.rootReference = reference;
        this.range = generalRange;
        this.header = avlNode;
    }

    TreeMultiset(Comparator<? super E> comparator) {
        super(comparator);
        this.range = GeneralRange.all(comparator);
        this.header = new AvlNode(null, 1);
        successor(this.header, this.header);
        this.rootReference = new Reference();
    }

    private long aggregateAboveRange(Aggregate aggregate, @Nullable AvlNode<E> avlNode) {
        if (avlNode == null) {
            return 0;
        }
        int compare = comparator().compare(this.range.getUpperEndpoint(), avlNode.elem);
        if (compare > 0) {
            return aggregateAboveRange(aggregate, avlNode.right);
        }
        if (compare != 0) {
            return (aggregate.treeAggregate(avlNode.right) + ((long) aggregate.nodeAggregate(avlNode))) + aggregateAboveRange(aggregate, avlNode.left);
        }
        switch (this.range.getUpperBoundType()) {
            case OPEN:
                return ((long) aggregate.nodeAggregate(avlNode)) + aggregate.treeAggregate(avlNode.right);
            case CLOSED:
                return aggregate.treeAggregate(avlNode.right);
            default:
                throw new AssertionError();
        }
    }

    private long aggregateBelowRange(Aggregate aggregate, @Nullable AvlNode<E> avlNode) {
        if (avlNode == null) {
            return 0;
        }
        int compare = comparator().compare(this.range.getLowerEndpoint(), avlNode.elem);
        if (compare < 0) {
            return aggregateBelowRange(aggregate, avlNode.left);
        }
        if (compare != 0) {
            return (aggregate.treeAggregate(avlNode.left) + ((long) aggregate.nodeAggregate(avlNode))) + aggregateBelowRange(aggregate, avlNode.right);
        }
        switch (this.range.getLowerBoundType()) {
            case OPEN:
                return ((long) aggregate.nodeAggregate(avlNode)) + aggregate.treeAggregate(avlNode.left);
            case CLOSED:
                return aggregate.treeAggregate(avlNode.left);
            default:
                throw new AssertionError();
        }
    }

    private long aggregateForEntries(Aggregate aggregate) {
        AvlNode avlNode = (AvlNode) this.rootReference.get();
        long treeAggregate = aggregate.treeAggregate(avlNode);
        if (this.range.hasLowerBound()) {
            treeAggregate -= aggregateBelowRange(aggregate, avlNode);
        }
        return this.range.hasUpperBound() ? treeAggregate - aggregateAboveRange(aggregate, avlNode) : treeAggregate;
    }

    public static <E extends Comparable> TreeMultiset<E> create() {
        return new TreeMultiset(Ordering.natural());
    }

    public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> iterable) {
        Object create = create();
        Iterables.addAll(create, iterable);
        return create;
    }

    public static <E> TreeMultiset<E> create(@Nullable Comparator<? super E> comparator) {
        return comparator == null ? new TreeMultiset(Ordering.natural()) : new TreeMultiset(comparator);
    }

    static int distinctElements(@Nullable AvlNode<?> avlNode) {
        return avlNode == null ? 0 : avlNode.distinctElements;
    }

    @Nullable
    private AvlNode<E> firstNode() {
        if (((AvlNode) this.rootReference.get()) == null) {
            return null;
        }
        AvlNode<E> access$800;
        if (this.range.hasLowerBound()) {
            Object lowerEndpoint = this.range.getLowerEndpoint();
            access$800 = ((AvlNode) this.rootReference.get()).ceiling(comparator(), lowerEndpoint);
            if (access$800 == null) {
                return null;
            }
            if (this.range.getLowerBoundType() == BoundType.OPEN && comparator().compare(lowerEndpoint, access$800.getElement()) == 0) {
                access$800 = access$800.succ;
            }
        } else {
            access$800 = this.header.succ;
        }
        if (access$800 == this.header || !this.range.contains(access$800.getElement())) {
            access$800 = null;
        }
        return access$800;
    }

    @Nullable
    private AvlNode<E> lastNode() {
        if (((AvlNode) this.rootReference.get()) == null) {
            return null;
        }
        AvlNode<E> access$1000;
        if (this.range.hasUpperBound()) {
            Object upperEndpoint = this.range.getUpperEndpoint();
            access$1000 = ((AvlNode) this.rootReference.get()).floor(comparator(), upperEndpoint);
            if (access$1000 == null) {
                return null;
            }
            if (this.range.getUpperBoundType() == BoundType.OPEN && comparator().compare(upperEndpoint, access$1000.getElement()) == 0) {
                access$1000 = access$1000.pred;
            }
        } else {
            access$1000 = this.header.pred;
        }
        if (access$1000 == this.header || !this.range.contains(access$1000.getElement())) {
            access$1000 = null;
        }
        return access$1000;
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Object obj = (Comparator) objectInputStream.readObject();
        Serialization.getFieldSetter(AbstractSortedMultiset.class, "comparator").set((Object) this, obj);
        Serialization.getFieldSetter(TreeMultiset.class, "range").set((Object) this, GeneralRange.all(obj));
        Serialization.getFieldSetter(TreeMultiset.class, "rootReference").set((Object) this, new Reference());
        obj = new AvlNode(null, 1);
        Serialization.getFieldSetter(TreeMultiset.class, "header").set((Object) this, obj);
        successor(obj, obj);
        Serialization.populateMultiset(this, objectInputStream);
    }

    private static <T> void successor(AvlNode<T> avlNode, AvlNode<T> avlNode2) {
        avlNode.succ = avlNode2;
        avlNode2.pred = avlNode;
    }

    private static <T> void successor(AvlNode<T> avlNode, AvlNode<T> avlNode2, AvlNode<T> avlNode3) {
        successor(avlNode, avlNode2);
        successor(avlNode2, avlNode3);
    }

    private Entry<E> wrapEntry(final AvlNode<E> avlNode) {
        return new AbstractEntry<E>() {
            public int getCount() {
                int count = avlNode.getCount();
                return count == 0 ? TreeMultiset.this.count(getElement()) : count;
            }

            public E getElement() {
                return avlNode.getElement();
            }
        };
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(elementSet().comparator());
        Serialization.writeMultiset(this, objectOutputStream);
    }

    public int add(@Nullable E e, int i) {
        Preconditions.checkArgument(i >= 0, "occurrences must be >= 0 but was %s", Integer.valueOf(i));
        if (i == 0) {
            return count(e);
        }
        Preconditions.checkArgument(this.range.contains(e));
        AvlNode avlNode = (AvlNode) this.rootReference.get();
        if (avlNode == null) {
            comparator().compare(e, e);
            AvlNode avlNode2 = new AvlNode(e, i);
            successor(this.header, avlNode2, this.header);
            this.rootReference.checkAndSet(avlNode, avlNode2);
            return 0;
        }
        int[] iArr = new int[1];
        this.rootReference.checkAndSet(avlNode, avlNode.add(comparator(), e, i, iArr));
        return iArr[0];
    }

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        return super.add(obj);
    }

    public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
        return super.addAll(collection);
    }

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ Comparator comparator() {
        return super.comparator();
    }

    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    public int count(@Nullable Object obj) {
        try {
            AvlNode avlNode = (AvlNode) this.rootReference.get();
            if (this.range.contains(obj)) {
                return avlNode == null ? 0 : avlNode.count(comparator(), obj);
            }
        } catch (ClassCastException e) {
            return 0;
        } catch (NullPointerException e2) {
        }
        return 0;
    }

    Iterator<Entry<E>> descendingEntryIterator() {
        return new C07823();
    }

    public /* bridge */ /* synthetic */ SortedMultiset descendingMultiset() {
        return super.descendingMultiset();
    }

    int distinctElements() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
    }

    public /* bridge */ /* synthetic */ SortedSet elementSet() {
        return super.elementSet();
    }

    Iterator<Entry<E>> entryIterator() {
        return new C07812();
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ Entry firstEntry() {
        return super.firstEntry();
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public SortedMultiset<E> headMultiset(@Nullable E e, BoundType boundType) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.upTo(comparator(), e, boundType)), this.header);
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    public /* bridge */ /* synthetic */ Entry lastEntry() {
        return super.lastEntry();
    }

    public /* bridge */ /* synthetic */ Entry pollFirstEntry() {
        return super.pollFirstEntry();
    }

    public /* bridge */ /* synthetic */ Entry pollLastEntry() {
        return super.pollLastEntry();
    }

    public int remove(@Nullable Object obj, int i) {
        Preconditions.checkArgument(i >= 0, "occurrences must be >= 0 but was %s", Integer.valueOf(i));
        if (i == 0) {
            return count(obj);
        }
        AvlNode avlNode = (AvlNode) this.rootReference.get();
        int[] iArr = new int[1];
        try {
            if (!this.range.contains(obj) || avlNode == null) {
                return 0;
            }
            this.rootReference.checkAndSet(avlNode, avlNode.remove(comparator(), obj, i, iArr));
            return iArr[0];
        } catch (ClassCastException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return super.remove(obj);
    }

    public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    public int setCount(@Nullable E e, int i) {
        boolean z = true;
        Preconditions.checkArgument(i >= 0);
        if (this.range.contains(e)) {
            AvlNode avlNode = (AvlNode) this.rootReference.get();
            if (avlNode != null) {
                int[] iArr = new int[1];
                this.rootReference.checkAndSet(avlNode, avlNode.setCount(comparator(), e, i, iArr));
                return iArr[0];
            } else if (i <= 0) {
                return 0;
            } else {
                add(e, i);
                return 0;
            }
        }
        if (i != 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        return 0;
    }

    public boolean setCount(@Nullable E e, int i, int i2) {
        Preconditions.checkArgument(i2 >= 0);
        Preconditions.checkArgument(i >= 0);
        Preconditions.checkArgument(this.range.contains(e));
        AvlNode avlNode = (AvlNode) this.rootReference.get();
        if (avlNode != null) {
            int[] iArr = new int[1];
            this.rootReference.checkAndSet(avlNode, avlNode.setCount(comparator(), e, i, i2, iArr));
            if (iArr[0] != i) {
                return false;
            }
        } else if (i != 0) {
            return false;
        } else {
            if (i2 > 0) {
                add(e, i2);
            }
        }
        return true;
    }

    public int size() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
    }

    public /* bridge */ /* synthetic */ SortedMultiset subMultiset(Object obj, BoundType boundType, Object obj2, BoundType boundType2) {
        return super.subMultiset(obj, boundType, obj2, boundType2);
    }

    public SortedMultiset<E> tailMultiset(@Nullable E e, BoundType boundType) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.downTo(comparator(), e, boundType)), this.header);
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }
}
