package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Hashing {
    private Hashing() {
    }

    static int smear(int i) {
        int i2 = ((i >>> 20) ^ (i >>> 12)) ^ i;
        return (i2 >>> 4) ^ ((i2 >>> 7) ^ i2);
    }
}
