package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;

@GwtCompatible
final class SmallCharMatcher extends CharMatcher {
    static final int MAX_SIZE = 63;
    static final int MAX_TABLE_SIZE = 128;
    private final boolean containsZero;
    final long filter;
    private final boolean reprobe;
    private final char[] table;

    private SmallCharMatcher(char[] cArr, long j, boolean z, boolean z2, String str) {
        super(str);
        this.table = cArr;
        this.filter = j;
        this.containsZero = z;
        this.reprobe = z2;
    }

    @VisibleForTesting
    static char[] buildTable(int i, char[] cArr, boolean z) {
        char[] cArr2 = new char[i];
        for (char c : cArr) {
            int i2 = c % i;
            if (i2 < 0) {
                i2 += i;
            }
            if (cArr2[i2] != '\u0000' && !z) {
                return null;
            }
            if (z) {
                while (cArr2[i2] != '\u0000') {
                    i2 = (i2 + 1) % i;
                }
            }
            cArr2[i2] = c;
        }
        return cArr2;
    }

    private boolean checkFilter(int i) {
        return 1 == ((this.filter >> i) & 1);
    }

    static CharMatcher from(char[] cArr, String str) {
        char[] buildTable;
        boolean z;
        long j = 0;
        boolean z2 = cArr[0] == '\u0000';
        for (char c : cArr) {
            j |= 1 << c;
        }
        char[] cArr2 = null;
        for (int length = cArr.length; length < 128; length++) {
            cArr2 = buildTable(length, cArr, false);
            if (cArr2 != null) {
                break;
            }
        }
        if (cArr2 == null) {
            buildTable = buildTable(128, cArr, true);
            z = true;
        } else {
            char[] cArr3 = cArr2;
            z = false;
            buildTable = cArr3;
        }
        return new SmallCharMatcher(buildTable, j, z2, z, str);
    }

    public boolean matches(char c) {
        if (c == '\u0000') {
            return this.containsZero;
        }
        if (!checkFilter(c)) {
            return false;
        }
        int length = c % this.table.length;
        if (length < 0) {
            length += this.table.length;
        }
        while (this.table[length] != '\u0000') {
            if (this.table[length] == c) {
                return true;
            }
            if (!this.reprobe) {
                return false;
            }
            length = (length + 1) % this.table.length;
        }
        return false;
    }

    public CharMatcher precomputed() {
        return this;
    }
}
