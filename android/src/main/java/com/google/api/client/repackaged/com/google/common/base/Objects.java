package com.google.api.client.repackaged.com.google.common.base;

import com.google.api.client.repackaged.com.google.common.annotations.Beta;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
public final class Objects {

    public static final class ToStringHelper {
        private final String className;
        private boolean omitNullValues;
        private final List<ValueHolder> valueHolders;

        private static final class ValueHolder {
            final StringBuilder builder;
            boolean isNull;

            private ValueHolder() {
                this.builder = new StringBuilder();
            }
        }

        private ToStringHelper(String str) {
            this.valueHolders = new LinkedList();
            this.omitNullValues = false;
            this.className = (String) Preconditions.checkNotNull(str);
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder = new ValueHolder();
            this.valueHolders.add(valueHolder);
            return valueHolder;
        }

        private ValueHolder addHolder(@Nullable Object obj) {
            ValueHolder addHolder = addHolder();
            addHolder.isNull = obj == null;
            return addHolder;
        }

        private StringBuilder checkNameAndAppend(String str) {
            Preconditions.checkNotNull(str);
            return addHolder().builder.append(str).append('=');
        }

        public ToStringHelper add(String str, char c) {
            checkNameAndAppend(str).append(c);
            return this;
        }

        public ToStringHelper add(String str, double d) {
            checkNameAndAppend(str).append(d);
            return this;
        }

        public ToStringHelper add(String str, float f) {
            checkNameAndAppend(str).append(f);
            return this;
        }

        public ToStringHelper add(String str, int i) {
            checkNameAndAppend(str).append(i);
            return this;
        }

        public ToStringHelper add(String str, long j) {
            checkNameAndAppend(str).append(j);
            return this;
        }

        public ToStringHelper add(String str, @Nullable Object obj) {
            Preconditions.checkNotNull(str);
            addHolder(obj).builder.append(str).append('=').append(obj);
            return this;
        }

        public ToStringHelper add(String str, boolean z) {
            checkNameAndAppend(str).append(z);
            return this;
        }

        public ToStringHelper addValue(char c) {
            addHolder().builder.append(c);
            return this;
        }

        public ToStringHelper addValue(double d) {
            addHolder().builder.append(d);
            return this;
        }

        public ToStringHelper addValue(float f) {
            addHolder().builder.append(f);
            return this;
        }

        public ToStringHelper addValue(int i) {
            addHolder().builder.append(i);
            return this;
        }

        public ToStringHelper addValue(long j) {
            addHolder().builder.append(j);
            return this;
        }

        public ToStringHelper addValue(@Nullable Object obj) {
            addHolder(obj).builder.append(obj);
            return this;
        }

        public ToStringHelper addValue(boolean z) {
            addHolder().builder.append(z);
            return this;
        }

        @Beta
        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }

        public String toString() {
            boolean z = this.omitNullValues;
            StringBuilder append = new StringBuilder(32).append(this.className).append('{');
            Object obj = null;
            for (ValueHolder valueHolder : this.valueHolders) {
                if (!z || !valueHolder.isNull) {
                    if (obj != null) {
                        append.append(", ");
                    } else {
                        obj = 1;
                    }
                    append.append(valueHolder.builder);
                }
            }
            return append.append('}').toString();
        }
    }

    private Objects() {
    }

    public static boolean equal(@Nullable Object obj, @Nullable Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static <T> T firstNonNull(@Nullable T t, @Nullable T t2) {
        return t != null ? t : Preconditions.checkNotNull(t2);
    }

    public static int hashCode(@Nullable Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    private static String simpleName(Class<?> cls) {
        String replaceAll = cls.getName().replaceAll("\\$[0-9]+", "\\$");
        int lastIndexOf = replaceAll.lastIndexOf(36);
        if (lastIndexOf == -1) {
            lastIndexOf = replaceAll.lastIndexOf(46);
        }
        return replaceAll.substring(lastIndexOf + 1);
    }

    public static ToStringHelper toStringHelper(Class<?> cls) {
        return new ToStringHelper(simpleName(cls));
    }

    public static ToStringHelper toStringHelper(Object obj) {
        return new ToStringHelper(simpleName(obj.getClass()));
    }

    public static ToStringHelper toStringHelper(String str) {
        return new ToStringHelper(str);
    }
}
