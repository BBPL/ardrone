package com.google.common.primitives;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

final class AndroidInteger {
    private AndroidInteger() {
    }

    @CheckForNull
    static Integer tryParse(String str) {
        return tryParse(str, 10);
    }

    @CheckForNull
    static Integer tryParse(String str, int i) {
        int i2 = 1;
        Preconditions.checkNotNull(str);
        Preconditions.checkArgument(i >= 2, "Invalid radix %s, min radix is %s", Integer.valueOf(i), Integer.valueOf(2));
        Preconditions.checkArgument(i <= 36, "Invalid radix %s, max radix is %s", Integer.valueOf(i), Integer.valueOf(36));
        int length = str.length();
        if (length == 0) {
            return null;
        }
        boolean z = str.charAt(0) == '-';
        if (!z) {
            i2 = 0;
        } else if (1 == length) {
            return null;
        }
        return tryParse(str, i2, i, z);
    }

    @CheckForNull
    private static Integer tryParse(String str, int i, int i2, boolean z) {
        int i3 = Integer.MIN_VALUE / i2;
        int i4 = 0;
        int length = str.length();
        while (i < length) {
            int digit = Character.digit(str.charAt(i), i2);
            if (digit == -1 || i3 > i4) {
                return null;
            }
            digit = (i4 * i2) - digit;
            if (digit > i4) {
                return null;
            }
            i++;
            i4 = digit;
        }
        if (!z) {
            i4 = -i4;
            if (i4 < 0) {
                return null;
            }
        }
        return (i4 > Integer.MAX_VALUE || i4 < Integer.MIN_VALUE) ? null : Integer.valueOf(i4);
    }
}
