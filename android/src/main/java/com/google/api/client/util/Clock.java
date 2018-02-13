package com.google.api.client.util;

public interface Clock {
    public static final Clock SYSTEM = new C04631();

    static final class C04631 implements Clock {
        C04631() {
        }

        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    long currentTimeMillis();
}
