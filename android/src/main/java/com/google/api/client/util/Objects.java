package com.google.api.client.util;

public final class Objects {

    public static final class ToStringHelper {
        private final com.google.api.client.repackaged.com.google.common.base.Objects.ToStringHelper wrapped;

        ToStringHelper(com.google.api.client.repackaged.com.google.common.base.Objects.ToStringHelper toStringHelper) {
            this.wrapped = toStringHelper;
        }

        public ToStringHelper add(String str, Object obj) {
            this.wrapped.add(str, obj);
            return this;
        }

        public ToStringHelper omitNullValues() {
            this.wrapped.omitNullValues();
            return this;
        }
    }

    private Objects() {
    }

    public static boolean equal(Object obj, Object obj2) {
        return com.google.api.client.repackaged.com.google.common.base.Objects.equal(obj, obj2);
    }

    public static ToStringHelper toStringHelper(Object obj) {
        return new ToStringHelper(com.google.api.client.repackaged.com.google.common.base.Objects.toStringHelper(obj));
    }
}
