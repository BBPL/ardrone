package com.google.gson.stream;

final class StringPool {
    private final String[] pool = new String[512];

    StringPool() {
    }

    public String get(char[] cArr, int i, int i2) {
        int i3;
        int i4 = 0;
        int i5 = 0;
        for (i3 = i; i3 < i + i2; i3++) {
            i5 = (i5 * 31) + cArr[i3];
        }
        i5 ^= (i5 >>> 20) ^ (i5 >>> 12);
        i3 = (this.pool.length - 1) & (i5 ^ ((i5 >>> 7) ^ (i5 >>> 4)));
        String str = this.pool[i3];
        if (str == null || str.length() != i2) {
            str = new String(cArr, i, i2);
            this.pool[i3] = str;
            return str;
        }
        while (i4 < i2) {
            if (str.charAt(i4) != cArr[i + i4]) {
                str = new String(cArr, i, i2);
                this.pool[i3] = str;
                return str;
            }
            i4++;
        }
        return str;
    }
}
