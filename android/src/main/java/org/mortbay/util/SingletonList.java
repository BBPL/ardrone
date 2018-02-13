package org.mortbay.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class SingletonList extends AbstractList {
    private Object f371o;

    private class SIterator implements ListIterator {
        int f370i;
        private final SingletonList this$0;

        SIterator(SingletonList singletonList) {
            this.this$0 = singletonList;
            this.f370i = 0;
        }

        SIterator(SingletonList singletonList, int i) {
            this.this$0 = singletonList;
            if (i < 0 || i > 1) {
                throw new IndexOutOfBoundsException(new StringBuffer().append("index ").append(i).toString());
            }
            this.f370i = i;
        }

        public void add(Object obj) {
            throw new UnsupportedOperationException("SingletonList.add()");
        }

        public boolean hasNext() {
            return this.f370i == 0;
        }

        public boolean hasPrevious() {
            return this.f370i == 1;
        }

        public Object next() {
            if (this.f370i != 0) {
                throw new NoSuchElementException();
            }
            this.f370i++;
            return SingletonList.access$000(this.this$0);
        }

        public int nextIndex() {
            return this.f370i;
        }

        public Object previous() {
            if (this.f370i != 1) {
                throw new NoSuchElementException();
            }
            this.f370i--;
            return SingletonList.access$000(this.this$0);
        }

        public int previousIndex() {
            return this.f370i - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException("SingletonList.remove()");
        }

        public void set(Object obj) {
            throw new UnsupportedOperationException("SingletonList.add()");
        }
    }

    private SingletonList(Object obj) {
        this.f371o = obj;
    }

    static Object access$000(SingletonList singletonList) {
        return singletonList.f371o;
    }

    public static SingletonList newSingletonList(Object obj) {
        return new SingletonList(obj);
    }

    public Object get(int i) {
        if (i == 0) {
            return this.f371o;
        }
        throw new IndexOutOfBoundsException(new StringBuffer().append("index ").append(i).toString());
    }

    public Iterator iterator() {
        return new SIterator(this);
    }

    public ListIterator listIterator() {
        return new SIterator(this);
    }

    public ListIterator listIterator(int i) {
        return new SIterator(this, i);
    }

    public int size() {
        return 1;
    }
}
