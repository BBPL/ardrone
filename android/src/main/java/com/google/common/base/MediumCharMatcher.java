package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;

@GwtCompatible
final class MediumCharMatcher extends CharMatcher {
    private static final double DESIRED_LOAD_FACTOR = 0.5d;
    static final int MAX_SIZE = 1023;
    private final boolean containsZero;
    private final long filter;
    private final char[] table;

    private MediumCharMatcher(char[] cArr, long j, boolean z, String str) {
        super(str);
        this.table = cArr;
        this.filter = j;
        this.containsZero = z;
    }

    private boolean checkFilter(int i) {
        return 1 == ((this.filter >> i) & 1);
    }

    @VisibleForTesting
    static int chooseTableSize(int i) {
        if (i == 1) {
            return 2;
        }
        int highestOneBit = Integer.highestOneBit(i - 1) << 1;
        while (((double) highestOneBit) * 0.5d < ((double) i)) {
            highestOneBit <<= 1;
        }
        return highestOneBit;
    }

    static CharMatcher from(char[] cArr, String str) {
        long j = 0;
        int length = cArr.length;
        boolean z = cArr[0] == '\u0000';
        for (char c : cArr) {
            j |= 1 << c;
        }
        char[] cArr2 = new char[chooseTableSize(length)];
        int length2 = cArr2.length - 1;
        for (char c2 : cArr) {
            int i = c2 & length2;
            while (cArr2[i] != '\u0000') {
                i = (i + 1) & length2;
            }
            cArr2[i] = c2;
        }
        return new MediumCharMatcher(cArr2, j, z, str);
    }

    public boolean matches(char c) {
        if (c == '\u0000') {
            return this.containsZero;
        }
        if (!checkFilter(c)) {
            return false;
        }
        int length = this.table.length - 1;
        int i = c & length;
        int i2 = i;
        while (this.table[i2] != '\u0000') {
            if (this.table[i2] == c) {
                return true;
            }
            i2 = (i2 + 1) & length;
            if (i2 == i) {
                return false;
            }
        }
        return false;
    }

    public CharMatcher precomputed() {
        return this;
    }
}
