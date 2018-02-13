package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Sets {

    static abstract class ImprovedAbstractSet<E> extends AbstractSet<E> {
        ImprovedAbstractSet() {
        }

        public boolean removeAll(Collection<?> collection) {
            return Sets.removeAllImpl((Set) this, (Collection) collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return super.retainAll((Collection) Preconditions.checkNotNull(collection));
        }
    }

    public static abstract class SetView<E> extends AbstractSet<E> {
        private SetView() {
        }

        public <S extends Set<E>> S copyInto(S s) {
            s.addAll(this);
            return s;
        }

        public ImmutableSet<E> immutableCopy() {
            return ImmutableSet.copyOf((Collection) this);
        }
    }

    private static class CartesianSet<B> extends AbstractSet<List<B>> {
        final ImmutableList<Axis> axes;
        final int size;

        private class Axis {
            final ImmutableSet<? extends B> choices;
            final ImmutableList<? extends B> choicesList = this.choices.asList();
            final int dividend;

            Axis(Set<? extends B> set, int i) {
                this.choices = ImmutableSet.copyOf((Collection) set);
                this.dividend = i;
            }

            boolean contains(Object obj) {
                return this.choices.contains(obj);
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof Axis)) {
                    return false;
                }
                return this.choices.equals(((Axis) obj).choices);
            }

            B getForIndex(int i) {
                return this.choicesList.get((i / this.dividend) % size());
            }

            public int hashCode() {
                return (CartesianSet.this.size / this.choices.size()) * this.choices.hashCode();
            }

            int size() {
                return this.choices.size();
            }
        }

        CartesianSet(List<? extends Set<? extends B>> list) {
            Builder builder = ImmutableList.builder();
            try {
                int i = 1;
                for (Set axis : list) {
                    Object axis2 = new Axis(axis, i);
                    builder.add(axis2);
                    i = IntMath.checkedMultiply(i, axis2.size());
                }
                this.axes = builder.build();
                this.size = i;
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("cartesian product too big");
            }
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof List)) {
                return false;
            }
            List list = (List) obj;
            int size = this.axes.size();
            if (list.size() != size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!((Axis) this.axes.get(i)).contains(list.get(i))) {
                    return false;
                }
            }
            return true;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof CartesianSet)) {
                return super.equals(obj);
            }
            return this.axes.equals(((CartesianSet) obj).axes);
        }

        public int hashCode() {
            int i = this.size - 1;
            for (int i2 = 0; i2 < this.axes.size(); i2++) {
                i *= 31;
            }
            return i + this.axes.hashCode();
        }

        public UnmodifiableIterator<List<B>> iterator() {
            return new AbstractIndexedListIterator<List<B>>(this.size) {
                protected List<B> get(int i) {
                    Object[] objArr = new Object[CartesianSet.this.axes.size()];
                    for (int i2 = 0; i2 < objArr.length; i2++) {
                        objArr[i2] = ((Axis) CartesianSet.this.axes.get(i2)).getForIndex(i);
                    }
                    return ImmutableList.copyOf(objArr);
                }
            };
        }

        public int size() {
            return this.size;
        }
    }

    private static class FilteredSet<E> extends FilteredCollection<E> implements Set<E> {
        FilteredSet(Set<E> set, Predicate<? super E> predicate) {
            super(set, predicate);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    private static class FilteredSortedSet<E> extends FilteredCollection<E> implements SortedSet<E> {
        FilteredSortedSet(SortedSet<E> sortedSet, Predicate<? super E> predicate) {
            super(sortedSet, predicate);
        }

        public Comparator<? super E> comparator() {
            return ((SortedSet) this.unfiltered).comparator();
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }

        public E first() {
            return iterator().next();
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        public SortedSet<E> headSet(E e) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).headSet(e), this.predicate);
        }

        public E last() {
            SortedSet sortedSet = (SortedSet) this.unfiltered;
            while (true) {
                E last = sortedSet.last();
                if (this.predicate.apply(last)) {
                    return last;
                }
                sortedSet = sortedSet.headSet(last);
            }
        }

        public SortedSet<E> subSet(E e, E e2) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).subSet(e, e2), this.predicate);
        }

        public SortedSet<E> tailSet(E e) {
            return new FilteredSortedSet(((SortedSet) this.unfiltered).tailSet(e), this.predicate);
        }
    }

    private static final class PowerSet<E> extends AbstractSet<Set<E>> {
        final ImmutableList<E> inputList;
        final ImmutableSet<E> inputSet;
        final int powerSetSize;

        private static final class BitFilteredSetIterator<E> extends UnmodifiableIterator<E> {
            final ImmutableList<E> input;
            int remainingSetBits;

            BitFilteredSetIterator(ImmutableList<E> immutableList, int i) {
                this.input = immutableList;
                this.remainingSetBits = i;
            }

            public boolean hasNext() {
                return this.remainingSetBits != 0;
            }

            public E next() {
                int numberOfTrailingZeros = Integer.numberOfTrailingZeros(this.remainingSetBits);
                if (numberOfTrailingZeros == 32) {
                    throw new NoSuchElementException();
                }
                this.remainingSetBits &= (1 << numberOfTrailingZeros) ^ -1;
                return this.input.get(numberOfTrailingZeros);
            }
        }

        PowerSet(ImmutableSet<E> immutableSet) {
            this.inputSet = immutableSet;
            this.inputList = immutableSet.asList();
            this.powerSetSize = 1 << immutableSet.size();
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof Set)) {
                return false;
            }
            return this.inputSet.containsAll((Set) obj);
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof PowerSet)) {
                return super.equals(obj);
            }
            return this.inputSet.equals(((PowerSet) obj).inputSet);
        }

        public int hashCode() {
            return this.inputSet.hashCode() << (this.inputSet.size() - 1);
        }

        public boolean isEmpty() {
            return false;
        }

        public Iterator<Set<E>> iterator() {
            return new AbstractIndexedListIterator<Set<E>>(this.powerSetSize) {
                protected Set<E> get(final int i) {
                    return new AbstractSet<E>() {
                        public Iterator<E> iterator() {
                            return new BitFilteredSetIterator(PowerSet.this.inputList, i);
                        }

                        public int size() {
                            return Integer.bitCount(i);
                        }
                    };
                }
            };
        }

        public int size() {
            return this.powerSetSize;
        }

        public String toString() {
            return "powerSet(" + this.inputSet + ")";
        }
    }

    private static class SetFromMap<E> extends AbstractSet<E> implements Set<E>, Serializable {
        @GwtIncompatible("not needed in emulated source")
        private static final long serialVersionUID = 0;
        private final Map<E, Boolean> f274m;
        private transient Set<E> f275s;

        SetFromMap(Map<E, Boolean> map) {
            Preconditions.checkArgument(map.isEmpty(), "Map is non-empty");
            this.f274m = map;
            this.f275s = map.keySet();
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.f275s = this.f274m.keySet();
        }

        public boolean add(E e) {
            return this.f274m.put(e, Boolean.TRUE) == null;
        }

        public void clear() {
            this.f274m.clear();
        }

        public boolean contains(Object obj) {
            return this.f274m.containsKey(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return this.f275s.containsAll(collection);
        }

        public boolean equals(@Nullable Object obj) {
            return this == obj || this.f275s.equals(obj);
        }

        public int hashCode() {
            return this.f275s.hashCode();
        }

        public boolean isEmpty() {
            return this.f274m.isEmpty();
        }

        public Iterator<E> iterator() {
            return this.f275s.iterator();
        }

        public boolean remove(Object obj) {
            return this.f274m.remove(obj) != null;
        }

        public boolean removeAll(Collection<?> collection) {
            return this.f275s.removeAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return this.f275s.retainAll(collection);
        }

        public int size() {
            return this.f274m.size();
        }

        public Object[] toArray() {
            return this.f275s.toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return this.f275s.toArray(tArr);
        }

        public String toString() {
            return this.f275s.toString();
        }
    }

    private Sets() {
    }

    public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> list) {
        for (Set isEmpty : list) {
            if (isEmpty.isEmpty()) {
                return ImmutableSet.of();
            }
        }
        return new CartesianSet(list);
    }

    public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... setArr) {
        return cartesianProduct(Arrays.asList(setArr));
    }

    static <T> SortedSet<T> cast(Iterable<T> iterable) {
        return (SortedSet) iterable;
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
        if (collection instanceof EnumSet) {
            return EnumSet.complementOf((EnumSet) collection);
        }
        Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");
        return makeComplementByHand(collection, ((Enum) collection.iterator().next()).getDeclaringClass());
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> cls) {
        Preconditions.checkNotNull(collection);
        return collection instanceof EnumSet ? EnumSet.complementOf((EnumSet) collection) : makeComplementByHand(collection, cls);
    }

    public static <E> SetView<E> difference(final Set<E> set, final Set<?> set2) {
        Preconditions.checkNotNull(set, "set1");
        Preconditions.checkNotNull(set2, "set2");
        final Predicate not = Predicates.not(Predicates.in(set2));
        return new SetView<E>() {
            public boolean contains(Object obj) {
                return set.contains(obj) && !set2.contains(obj);
            }

            public boolean isEmpty() {
                return set2.containsAll(set);
            }

            public Iterator<E> iterator() {
                return Iterators.filter(set.iterator(), not);
            }

            public int size() {
                return Iterators.size(iterator());
            }
        };
    }

    static boolean equalsImpl(Set<?> set, @Nullable Object obj) {
        if (set == obj) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set set2 = (Set) obj;
        try {
            return set.size() == set2.size() && set.containsAll(set2);
        } catch (NullPointerException e) {
            return false;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public static <E> Set<E> filter(Set<E> set, Predicate<? super E> predicate) {
        if (set instanceof SortedSet) {
            return filter((SortedSet) set, (Predicate) predicate);
        }
        if (!(set instanceof FilteredSet)) {
            return new FilteredSet((Set) Preconditions.checkNotNull(set), (Predicate) Preconditions.checkNotNull(predicate));
        }
        FilteredSet filteredSet = (FilteredSet) set;
        return new FilteredSet((Set) filteredSet.unfiltered, Predicates.and(filteredSet.predicate, predicate));
    }

    public static <E> SortedSet<E> filter(SortedSet<E> sortedSet, Predicate<? super E> predicate) {
        if (!(sortedSet instanceof FilteredSet)) {
            return new FilteredSortedSet((SortedSet) Preconditions.checkNotNull(sortedSet), (Predicate) Preconditions.checkNotNull(predicate));
        }
        FilteredSet filteredSet = (FilteredSet) sortedSet;
        return new FilteredSortedSet((SortedSet) filteredSet.unfiltered, Predicates.and(filteredSet.predicate, predicate));
    }

    static int hashCodeImpl(Set<?> set) {
        int i = 0;
        for (Object next : set) {
            i += next != null ? next.hashCode() : 0;
        }
        return i;
    }

    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E e, E... eArr) {
        return new ImmutableEnumSet(EnumSet.of(e, eArr));
    }

    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> iterable) {
        Iterator it = iterable.iterator();
        if (!it.hasNext()) {
            return ImmutableSet.of();
        }
        if (iterable instanceof EnumSet) {
            return new ImmutableEnumSet(EnumSet.copyOf((EnumSet) iterable));
        }
        EnumSet of = EnumSet.of((Enum) it.next());
        while (it.hasNext()) {
            of.add(it.next());
        }
        return new ImmutableEnumSet(of);
    }

    public static <E> SetView<E> intersection(final Set<E> set, final Set<?> set2) {
        Preconditions.checkNotNull(set, "set1");
        Preconditions.checkNotNull(set2, "set2");
        final Predicate in = Predicates.in(set2);
        return new SetView<E>() {
            public boolean contains(Object obj) {
                return set.contains(obj) && set2.contains(obj);
            }

            public boolean containsAll(Collection<?> collection) {
                return set.containsAll(collection) && set2.containsAll(collection);
            }

            public boolean isEmpty() {
                return !iterator().hasNext();
            }

            public Iterator<E> iterator() {
                return Iterators.filter(set.iterator(), in);
            }

            public int size() {
                return Iterators.size(iterator());
            }
        };
    }

    private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> cls) {
        EnumSet<E> allOf = EnumSet.allOf(cls);
        allOf.removeAll(collection);
        return allOf;
    }

    @GwtIncompatible("CopyOnWriteArraySet")
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet();
    }

    @GwtIncompatible("CopyOnWriteArraySet")
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> iterable) {
        return new CopyOnWriteArraySet(iterable instanceof Collection ? Collections2.cast(iterable) : Lists.newArrayList((Iterable) iterable));
    }

    public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> cls) {
        Preconditions.checkNotNull(iterable);
        Object noneOf = EnumSet.noneOf(cls);
        Iterables.addAll(noneOf, iterable);
        return noneOf;
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet();
    }

    public static <E> HashSet<E> newHashSet(Iterable<? extends E> iterable) {
        return iterable instanceof Collection ? new HashSet(Collections2.cast(iterable)) : newHashSet(iterable.iterator());
    }

    public static <E> HashSet<E> newHashSet(Iterator<? extends E> it) {
        HashSet<E> newHashSet = newHashSet();
        while (it.hasNext()) {
            newHashSet.add(it.next());
        }
        return newHashSet;
    }

    public static <E> HashSet<E> newHashSet(E... eArr) {
        Object newHashSetWithExpectedSize = newHashSetWithExpectedSize(eArr.length);
        Collections.addAll(newHashSetWithExpectedSize, eArr);
        return newHashSetWithExpectedSize;
    }

    public static <E> HashSet<E> newHashSetWithExpectedSize(int i) {
        return new HashSet(Maps.capacity(i));
    }

    public static <E> Set<E> newIdentityHashSet() {
        return newSetFromMap(Maps.newIdentityHashMap());
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new LinkedHashSet(Collections2.cast(iterable));
        }
        LinkedHashSet<E> newLinkedHashSet = newLinkedHashSet();
        for (Object add : iterable) {
            newLinkedHashSet.add(add);
        }
        return newLinkedHashSet;
    }

    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int i) {
        return new LinkedHashSet(Maps.capacity(i));
    }

    public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        return new SetFromMap(map);
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet();
    }

    public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> iterable) {
        TreeSet<E> newTreeSet = newTreeSet();
        Iterator it = iterable.iterator();
        while (it.hasNext()) {
            newTreeSet.add((Comparable) it.next());
        }
        return newTreeSet;
    }

    public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
        return new TreeSet((Comparator) Preconditions.checkNotNull(comparator));
    }

    @GwtCompatible(serializable = false)
    public static <E> Set<Set<E>> powerSet(Set<E> set) {
        ImmutableSet copyOf = ImmutableSet.copyOf((Collection) set);
        Preconditions.checkArgument(copyOf.size() <= 30, "Too many elements to create power set: %s > 30", Integer.valueOf(copyOf.size()));
        return new PowerSet(copyOf);
    }

    static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        if (collection instanceof Multiset) {
            Collection elementSet = ((Multiset) collection).elementSet();
        }
        if (!(elementSet instanceof Set) || elementSet.size() <= set.size()) {
            return removeAllImpl((Set) set, elementSet.iterator());
        }
        Iterator it = set.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (elementSet.contains(it.next())) {
                z = true;
                it.remove();
            }
        }
        return z;
    }

    static boolean removeAllImpl(Set<?> set, Iterator<?> it) {
        boolean z = false;
        while (it.hasNext()) {
            z |= set.remove(it.next());
        }
        return z;
    }

    public static <E> SetView<E> symmetricDifference(Set<? extends E> set, Set<? extends E> set2) {
        Preconditions.checkNotNull(set, "set1");
        Preconditions.checkNotNull(set2, "set2");
        return difference(union(set, set2), intersection(set, set2));
    }

    public static <E> SetView<E> union(final Set<? extends E> set, final Set<? extends E> set2) {
        Preconditions.checkNotNull(set, "set1");
        Preconditions.checkNotNull(set2, "set2");
        final Set difference = difference(set2, set);
        return new SetView<E>() {
            public boolean contains(Object obj) {
                return set.contains(obj) || set2.contains(obj);
            }

            public <S extends Set<E>> S copyInto(S s) {
                s.addAll(set);
                s.addAll(set2);
                return s;
            }

            public ImmutableSet<E> immutableCopy() {
                return new ImmutableSet.Builder().addAll(set).addAll(set2).build();
            }

            public boolean isEmpty() {
                return set.isEmpty() && set2.isEmpty();
            }

            public Iterator<E> iterator() {
                return Iterators.unmodifiableIterator(Iterators.concat(set.iterator(), difference.iterator()));
            }

            public int size() {
                return set.size() + difference.size();
            }
        };
    }
}
