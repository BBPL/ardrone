package com.google.api.client.util.escape;

public class PercentEscaper extends UnicodeEscaper {
    public static final String SAFECHARS_URLENCODER = "-_.*";
    public static final String SAFEPATHCHARS_URLENCODER = "-_.!~*'()@:$&,;=";
    public static final String SAFEQUERYSTRINGCHARS_URLENCODER = "-_.!~*'()@:$,;/?:";
    private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    private static final char[] URI_ESCAPED_SPACE = new char[]{'+'};
    private final boolean plusForSpace;
    private final boolean[] safeOctets;

    public PercentEscaper(String str, boolean z) {
        if (str.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        } else if (z && str.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        } else if (str.contains("%")) {
            throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
        } else {
            this.plusForSpace = z;
            this.safeOctets = createSafeOctets(str);
        }
    }

    private static boolean[] createSafeOctets(String str) {
        int i = 0;
        char[] toCharArray = str.toCharArray();
        int i2 = 122;
        for (char max : toCharArray) {
            i2 = Math.max(max, i2);
        }
        boolean[] zArr = new boolean[(i2 + 1)];
        for (i2 = 48; i2 <= 57; i2++) {
            zArr[i2] = true;
        }
        for (i2 = 65; i2 <= 90; i2++) {
            zArr[i2] = true;
        }
        for (i2 = 97; i2 <= 122; i2++) {
            zArr[i2] = true;
        }
        i2 = toCharArray.length;
        while (i < i2) {
            zArr[toCharArray[i]] = true;
            i++;
        }
        return zArr;
    }

    public String escape(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt >= this.safeOctets.length || !this.safeOctets[charAt]) {
                return escapeSlow(str, i);
            }
        }
        return str;
    }

    protected char[] escape(int i) {
        if (i < this.safeOctets.length && this.safeOctets[i]) {
            return null;
        }
        if (i == 32 && this.plusForSpace) {
            return URI_ESCAPED_SPACE;
        }
        char c;
        if (i <= 127) {
            c = UPPER_HEX_DIGITS[i & 15];
            return new char[]{'%', UPPER_HEX_DIGITS[i >>> 4], c};
        } else if (i <= 2047) {
            c = UPPER_HEX_DIGITS[i & 15];
            r0 = i >>> 4;
            r2 = UPPER_HEX_DIGITS[(r0 & 3) | 8];
            r4 = UPPER_HEX_DIGITS[(r0 >>> 2) & 15];
            return new char[]{'%', UPPER_HEX_DIGITS[(r3 >>> 4) | 12], r4, '%', r2, c};
        } else if (i <= 65535) {
            c = UPPER_HEX_DIGITS[i & 15];
            r0 = i >>> 4;
            r2 = UPPER_HEX_DIGITS[(r0 & 3) | 8];
            r0 >>>= 2;
            r3 = UPPER_HEX_DIGITS[r0 & 15];
            r5 = UPPER_HEX_DIGITS[((r0 >>> 4) & 3) | 8];
            return new char[]{'%', 'E', UPPER_HEX_DIGITS[r4 >>> 2], '%', r5, r3, '%', r2, c};
        } else if (i <= 1114111) {
            c = UPPER_HEX_DIGITS[i & 15];
            r0 = i >>> 4;
            r2 = UPPER_HEX_DIGITS[(r0 & 3) | 8];
            r0 >>>= 2;
            r3 = UPPER_HEX_DIGITS[r0 & 15];
            r0 >>>= 4;
            r4 = UPPER_HEX_DIGITS[(r0 & 3) | 8];
            r0 >>>= 2;
            r5 = UPPER_HEX_DIGITS[r0 & 15];
            char c2 = UPPER_HEX_DIGITS[((r0 >>> 4) & 3) | 8];
            return new char[]{'%', 'F', UPPER_HEX_DIGITS[(r6 >>> 2) & 7], '%', c2, r5, '%', r4, r3, '%', r2, c};
        } else {
            throw new IllegalArgumentException("Invalid unicode character value " + i);
        }
    }

    protected int nextEscapeIndex(CharSequence charSequence, int i, int i2) {
        while (i < i2) {
            char charAt = charSequence.charAt(i);
            if (charAt >= this.safeOctets.length || !this.safeOctets[charAt]) {
                break;
            }
            i++;
        }
        return i;
    }
}
