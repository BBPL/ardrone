package com.google.api.client.repackaged.com.google.common.base;

import com.google.api.client.repackaged.com.google.common.annotations.Beta;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible
public class Joiner {
    private final String separator;

    public static final class MapJoiner {
        private final Joiner joiner;
        private final String keyValueSeparator;

        private MapJoiner(Joiner joiner, String str) {
            this.joiner = joiner;
            this.keyValueSeparator = (String) Preconditions.checkNotNull(str);
        }

        @Beta
        public <A extends Appendable> A appendTo(A a, Iterable<? extends Entry<?, ?>> iterable) throws IOException {
            return appendTo((Appendable) a, iterable.iterator());
        }

        @Beta
        @Deprecated
        public <A extends Appendable, I extends Iterable<? extends Entry<?, ?>> & Iterator<? extends Entry<?, ?>>> A appendTo(A a, I i) throws IOException {
            return appendTo((Appendable) a, (Iterator) i);
        }

        @Beta
        public <A extends Appendable> A appendTo(A a, Iterator<? extends Entry<?, ?>> it) throws IOException {
            Preconditions.checkNotNull(a);
            if (it.hasNext()) {
                Entry entry = (Entry) it.next();
                a.append(this.joiner.toString(entry.getKey()));
                a.append(this.keyValueSeparator);
                a.append(this.joiner.toString(entry.getValue()));
                while (it.hasNext()) {
                    a.append(this.joiner.separator);
                    entry = (Entry) it.next();
                    a.append(this.joiner.toString(entry.getKey()));
                    a.append(this.keyValueSeparator);
                    a.append(this.joiner.toString(entry.getValue()));
                }
            }
            return a;
        }

        public <A extends Appendable> A appendTo(A a, Map<?, ?> map) throws IOException {
            return appendTo((Appendable) a, map.entrySet());
        }

        @Beta
        public StringBuilder appendTo(StringBuilder stringBuilder, Iterable<? extends Entry<?, ?>> iterable) {
            return appendTo(stringBuilder, iterable.iterator());
        }

        @Beta
        @Deprecated
        public <I extends Iterable<? extends Entry<?, ?>> & Iterator<? extends Entry<?, ?>>> StringBuilder appendTo(StringBuilder stringBuilder, I i) throws IOException {
            return appendTo(stringBuilder, (Iterator) i);
        }

        @Beta
        public StringBuilder appendTo(StringBuilder stringBuilder, Iterator<? extends Entry<?, ?>> it) {
            try {
                appendTo((Appendable) stringBuilder, (Iterator) it);
                return stringBuilder;
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public StringBuilder appendTo(StringBuilder stringBuilder, Map<?, ?> map) {
            return appendTo(stringBuilder, map.entrySet());
        }

        @Beta
        public String join(Iterable<? extends Entry<?, ?>> iterable) {
            return join(iterable.iterator());
        }

        @Beta
        @Deprecated
        public <I extends Iterable<? extends Entry<?, ?>> & Iterator<? extends Entry<?, ?>>> String join(I i) throws IOException {
            return join((Iterator) i);
        }

        @Beta
        public String join(Iterator<? extends Entry<?, ?>> it) {
            return appendTo(new StringBuilder(), (Iterator) it).toString();
        }

        public String join(Map<?, ?> map) {
            return join(map.entrySet());
        }

        @CheckReturnValue
        public MapJoiner useForNull(String str) {
            return new MapJoiner(this.joiner.useForNull(str), this.keyValueSeparator);
        }
    }

    private Joiner(Joiner joiner) {
        this.separator = joiner.separator;
    }

    private Joiner(String str) {
        this.separator = (String) Preconditions.checkNotNull(str);
    }

    private static Iterable<Object> iterable(final Object obj, final Object obj2, final Object[] objArr) {
        Preconditions.checkNotNull(objArr);
        return new AbstractList<Object>() {
            public Object get(int i) {
                switch (i) {
                    case 0:
                        return obj;
                    case 1:
                        return obj2;
                    default:
                        return objArr[i - 2];
                }
            }

            public int size() {
                return objArr.length + 2;
            }
        };
    }

    public static Joiner on(char c) {
        return new Joiner(String.valueOf(c));
    }

    public static Joiner on(String str) {
        return new Joiner(str);
    }

    public <A extends Appendable> A appendTo(A a, Iterable<?> iterable) throws IOException {
        return appendTo((Appendable) a, iterable.iterator());
    }

    @Beta
    @Deprecated
    public final <A extends Appendable, I extends Iterable<?> & Iterator<?>> A appendTo(A a, I i) throws IOException {
        return appendTo((Appendable) a, (Iterator) i);
    }

    public final <A extends Appendable> A appendTo(A a, @Nullable Object obj, @Nullable Object obj2, Object... objArr) throws IOException {
        return appendTo((Appendable) a, iterable(obj, obj2, objArr));
    }

    public <A extends Appendable> A appendTo(A a, Iterator<?> it) throws IOException {
        Preconditions.checkNotNull(a);
        if (it.hasNext()) {
            a.append(toString(it.next()));
            while (it.hasNext()) {
                a.append(this.separator);
                a.append(toString(it.next()));
            }
        }
        return a;
    }

    public final <A extends Appendable> A appendTo(A a, Object[] objArr) throws IOException {
        return appendTo((Appendable) a, Arrays.asList(objArr));
    }

    public final StringBuilder appendTo(StringBuilder stringBuilder, Iterable<?> iterable) {
        return appendTo(stringBuilder, iterable.iterator());
    }

    @Beta
    @Deprecated
    public final <I extends Iterable<?> & Iterator<?>> StringBuilder appendTo(StringBuilder stringBuilder, I i) {
        return appendTo(stringBuilder, (Iterator) i);
    }

    public final StringBuilder appendTo(StringBuilder stringBuilder, @Nullable Object obj, @Nullable Object obj2, Object... objArr) {
        return appendTo(stringBuilder, iterable(obj, obj2, objArr));
    }

    public final StringBuilder appendTo(StringBuilder stringBuilder, Iterator<?> it) {
        try {
            appendTo((Appendable) stringBuilder, (Iterator) it);
            return stringBuilder;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public final StringBuilder appendTo(StringBuilder stringBuilder, Object[] objArr) {
        return appendTo(stringBuilder, Arrays.asList(objArr));
    }

    public final String join(Iterable<?> iterable) {
        return join(iterable.iterator());
    }

    @Beta
    @Deprecated
    public final <I extends Iterable<?> & Iterator<?>> String join(I i) {
        return join((Iterator) i);
    }

    public final String join(@Nullable Object obj, @Nullable Object obj2, Object... objArr) {
        return join(iterable(obj, obj2, objArr));
    }

    public final String join(Iterator<?> it) {
        return appendTo(new StringBuilder(), (Iterator) it).toString();
    }

    public final String join(Object[] objArr) {
        return join(Arrays.asList(objArr));
    }

    @CheckReturnValue
    public Joiner skipNulls() {
        return new Joiner(this) {
            public <A extends Appendable> A appendTo(A a, Iterator<?> it) throws IOException {
                Preconditions.checkNotNull(a, "appendable");
                Preconditions.checkNotNull(it, "parts");
                while (it.hasNext()) {
                    Object next = it.next();
                    if (next != null) {
                        a.append(Joiner.this.toString(next));
                        break;
                    }
                }
                while (it.hasNext()) {
                    next = it.next();
                    if (next != null) {
                        a.append(Joiner.this.separator);
                        a.append(Joiner.this.toString(next));
                    }
                }
                return a;
            }

            public Joiner useForNull(String str) {
                Preconditions.checkNotNull(str);
                throw new UnsupportedOperationException("already specified skipNulls");
            }

            public MapJoiner withKeyValueSeparator(String str) {
                Preconditions.checkNotNull(str);
                throw new UnsupportedOperationException("can't use .skipNulls() with maps");
            }
        };
    }

    CharSequence toString(Object obj) {
        Preconditions.checkNotNull(obj);
        return obj instanceof CharSequence ? (CharSequence) obj : obj.toString();
    }

    @CheckReturnValue
    public Joiner useForNull(final String str) {
        Preconditions.checkNotNull(str);
        return new Joiner(this) {
            public Joiner skipNulls() {
                throw new UnsupportedOperationException("already specified useForNull");
            }

            CharSequence toString(Object obj) {
                return obj == null ? str : Joiner.this.toString(obj);
            }

            public Joiner useForNull(String str) {
                Preconditions.checkNotNull(str);
                throw new UnsupportedOperationException("already specified useForNull");
            }
        };
    }

    @CheckReturnValue
    public MapJoiner withKeyValueSeparator(String str) {
        return new MapJoiner(str);
    }
}
