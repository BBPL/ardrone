package com.google.protobuf;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;

public class UnmodifiableLazyStringList extends AbstractList<String> implements LazyStringList, RandomAccess {
    private final LazyStringList list;

    class C09612 implements Iterator<String> {
        Iterator<String> iter = UnmodifiableLazyStringList.this.list.iterator();

        C09612() {
        }

        public boolean hasNext() {
            return this.iter.hasNext();
        }

        public String next() {
            return (String) this.iter.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public UnmodifiableLazyStringList(LazyStringList lazyStringList) {
        this.list = lazyStringList;
    }

    public void add(ByteString byteString) {
        throw new UnsupportedOperationException();
    }

    public String get(int i) {
        return (String) this.list.get(i);
    }

    public ByteString getByteString(int i) {
        return this.list.getByteString(i);
    }

    public Iterator<String> iterator() {
        return new C09612();
    }

    public ListIterator<String> listIterator(final int i) {
        return new ListIterator<String>() {
            ListIterator<String> iter = UnmodifiableLazyStringList.this.list.listIterator(i);

            public void add(String str) {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return this.iter.hasNext();
            }

            public boolean hasPrevious() {
                return this.iter.hasPrevious();
            }

            public String next() {
                return (String) this.iter.next();
            }

            public int nextIndex() {
                return this.iter.nextIndex();
            }

            public String previous() {
                return (String) this.iter.previous();
            }

            public int previousIndex() {
                return this.iter.previousIndex();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public void set(String str) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public int size() {
        return this.list.size();
    }
}
