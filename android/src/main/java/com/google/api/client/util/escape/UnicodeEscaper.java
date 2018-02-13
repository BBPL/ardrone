package com.google.api.client.util.escape;

public abstract class UnicodeEscaper extends Escaper {
    private static final int DEST_PAD = 32;

    protected static int codePointAt(CharSequence charSequence, int i, int i2) {
        if (i < i2) {
            int i3 = i + 1;
            char charAt = charSequence.charAt(i);
            if (charAt < '?' || charAt > '?') {
                return charAt;
            }
            if (charAt > '?') {
                throw new IllegalArgumentException("Unexpected low surrogate character '" + charAt + "' with value " + charAt + " at index " + (i3 - 1));
            } else if (i3 == i2) {
                return -charAt;
            } else {
                char charAt2 = charSequence.charAt(i3);
                if (Character.isLowSurrogate(charAt2)) {
                    return Character.toCodePoint(charAt, charAt2);
                }
                throw new IllegalArgumentException("Expected low surrogate but got char '" + charAt2 + "' with value " + charAt2 + " at index " + i3);
            }
        }
        throw new IndexOutOfBoundsException("Index exceeds specified range");
    }

    private static char[] growBuffer(char[] cArr, int i, int i2) {
        Object obj = new char[i2];
        if (i > 0) {
            System.arraycopy(cArr, 0, obj, 0, i);
        }
        return obj;
    }

    public abstract String escape(String str);

    protected abstract char[] escape(int i);

    protected final String escapeSlow(String str, int i) {
        int codePointAt;
        char[] growBuffer;
        int length = str.length();
        Object charBufferFromThreadLocal = Platform.charBufferFromThreadLocal();
        int i2 = 0;
        int i3 = 0;
        while (i < length) {
            codePointAt = codePointAt(str, i, length);
            if (codePointAt < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            Object obj;
            int i4;
            Object obj2;
            Object escape = escape(codePointAt);
            codePointAt = (Character.isSupplementaryCodePoint(codePointAt) ? 2 : 1) + i;
            if (escape != null) {
                int i5 = i - i2;
                int length2 = (i3 + i5) + escape.length;
                if (charBufferFromThreadLocal.length < length2) {
                    charBufferFromThreadLocal = growBuffer(charBufferFromThreadLocal, i3, ((length2 + length) - i) + 32);
                }
                if (i5 > 0) {
                    str.getChars(i2, i, charBufferFromThreadLocal, i3);
                    i3 += i5;
                }
                if (escape.length > 0) {
                    System.arraycopy(escape, 0, charBufferFromThreadLocal, i3, escape.length);
                    i3 += escape.length;
                }
                i2 = codePointAt;
                int i6 = i3;
                obj = charBufferFromThreadLocal;
                i4 = i6;
            } else {
                obj2 = charBufferFromThreadLocal;
                i4 = i3;
                obj = obj2;
            }
            i = nextEscapeIndex(str, codePointAt, length);
            obj2 = obj;
            i3 = i4;
            charBufferFromThreadLocal = obj2;
        }
        codePointAt = length - i2;
        if (codePointAt > 0) {
            codePointAt += i3;
            if (charBufferFromThreadLocal.length < codePointAt) {
                growBuffer = growBuffer(charBufferFromThreadLocal, i3, codePointAt);
            }
            str.getChars(i2, length, growBuffer, i3);
            i3 = codePointAt;
        }
        return new String(growBuffer, 0, i3);
    }

    protected abstract int nextEscapeIndex(CharSequence charSequence, int i, int i2);
}
