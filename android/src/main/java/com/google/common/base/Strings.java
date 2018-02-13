package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import org.mortbay.jetty.HttpVersions;

@GwtCompatible
public final class Strings {
    private Strings() {
    }

    public static String commonPrefix(CharSequence charSequence, CharSequence charSequence2) {
        Preconditions.checkNotNull(charSequence);
        Preconditions.checkNotNull(charSequence2);
        int min = Math.min(charSequence.length(), charSequence2.length());
        int i = 0;
        while (i < min && charSequence.charAt(i) == charSequence2.charAt(i)) {
            i++;
        }
        if (validSurrogatePairAt(charSequence, i - 1) || validSurrogatePairAt(charSequence2, i - 1)) {
            i--;
        }
        return charSequence.subSequence(0, i).toString();
    }

    public static String commonSuffix(CharSequence charSequence, CharSequence charSequence2) {
        Preconditions.checkNotNull(charSequence);
        Preconditions.checkNotNull(charSequence2);
        int min = Math.min(charSequence.length(), charSequence2.length());
        int i = 0;
        while (i < min && charSequence.charAt((charSequence.length() - i) - 1) == charSequence2.charAt((charSequence2.length() - i) - 1)) {
            i++;
        }
        if (validSurrogatePairAt(charSequence, (charSequence.length() - i) - 1) || validSurrogatePairAt(charSequence2, (charSequence2.length() - i) - 1)) {
            i--;
        }
        return charSequence.subSequence(charSequence.length() - i, charSequence.length()).toString();
    }

    @Nullable
    public static String emptyToNull(@Nullable String str) {
        return isNullOrEmpty(str) ? null : str;
    }

    public static boolean isNullOrEmpty(@Nullable String str) {
        return str == null || str.length() == 0;
    }

    public static String nullToEmpty(@Nullable String str) {
        return str == null ? HttpVersions.HTTP_0_9 : str;
    }

    public static String padEnd(String str, int i, char c) {
        Preconditions.checkNotNull(str);
        if (str.length() >= i) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(i);
        stringBuilder.append(str);
        for (int length = str.length(); length < i; length++) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static String padStart(String str, int i, char c) {
        Preconditions.checkNotNull(str);
        if (str.length() >= i) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(i);
        for (int length = str.length(); length < i; length++) {
            stringBuilder.append(c);
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public static String repeat(String str, int i) {
        Preconditions.checkNotNull(str);
        if (i <= 1) {
            Preconditions.checkArgument(i >= 0, "invalid count: %s", Integer.valueOf(i));
            return i == 0 ? HttpVersions.HTTP_0_9 : str;
        } else {
            int length = str.length();
            long j = ((long) length) * ((long) i);
            int i2 = (int) j;
            if (((long) i2) != j) {
                throw new ArrayIndexOutOfBoundsException("Required array size too large: " + String.valueOf(j));
            }
            Object obj = new char[i2];
            str.getChars(0, length, obj, 0);
            while (length < i2 - length) {
                System.arraycopy(obj, 0, obj, length, length);
                length <<= 1;
            }
            System.arraycopy(obj, 0, obj, length, i2 - length);
            return new String(obj);
        }
    }

    @VisibleForTesting
    static boolean validSurrogatePairAt(CharSequence charSequence, int i) {
        return i >= 0 && i <= charSequence.length() - 2 && Character.isHighSurrogate(charSequence.charAt(i)) && Character.isLowSurrogate(charSequence.charAt(i + 1));
    }
}
