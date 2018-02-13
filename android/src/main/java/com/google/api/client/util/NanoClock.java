package com.google.api.client.util;

public interface NanoClock {
    public static final NanoClock SYSTEM = new C04641();

    static final class C04641 implements NanoClock {
        C04641() {
        }

        public long nanoTime() {
            return System.nanoTime();
        }
    }

    long nanoTime();
}
