package org.apache.http.client.utils;

import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;

@Immutable
public class Rfc3492Idn implements Idn {
    private static final String ACE_PREFIX = "xn--";
    private static final int base = 36;
    private static final int damp = 700;
    private static final char delimiter = '-';
    private static final int initial_bias = 72;
    private static final int initial_n = 128;
    private static final int skew = 38;
    private static final int tmax = 26;
    private static final int tmin = 1;

    private int adapt(int i, int i2, boolean z) {
        int i3 = z ? i / damp : i / 2;
        i3 += i3 / i2;
        int i4 = 0;
        while (i3 > 455) {
            i3 /= 35;
            i4 += 36;
        }
        return ((i3 * 36) / (i3 + 38)) + i4;
    }

    private int digit(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 65;
        }
        if (c >= 'a' && c <= 'z') {
            return c - 97;
        }
        if (c >= '0' && c <= '9') {
            return (c - 48) + 26;
        }
        throw new IllegalArgumentException("illegal digit: " + c);
    }

    protected String decode(String str) {
        int i = 128;
        int i2 = initial_bias;
        StringBuilder stringBuilder = new StringBuilder(str.length());
        int lastIndexOf = str.lastIndexOf(45);
        if (lastIndexOf != -1) {
            stringBuilder.append(str.subSequence(0, lastIndexOf));
            str = str.substring(lastIndexOf + 1);
            lastIndexOf = 0;
        } else {
            lastIndexOf = 0;
        }
        while (str.length() > 0) {
            int i3 = 36;
            int i4 = lastIndexOf;
            int i5 = 1;
            while (str.length() != 0) {
                char charAt = str.charAt(0);
                str = str.substring(1);
                int digit = digit(charAt);
                i4 += digit * i5;
                int i6 = i3 <= i2 + 1 ? 1 : i3 >= i2 + 26 ? 26 : i3 - i2;
                if (digit < i6) {
                    break;
                }
                i5 *= 36 - i6;
                i3 += 36;
            }
            i2 = adapt(i4 - lastIndexOf, stringBuilder.length() + 1, lastIndexOf == 0);
            i += i4 / (stringBuilder.length() + 1);
            lastIndexOf = i4 % (stringBuilder.length() + 1);
            stringBuilder.insert(lastIndexOf, (char) i);
            lastIndexOf++;
        }
        return stringBuilder.toString();
    }

    public String toUnicode(String str) {
        StringBuilder stringBuilder = new StringBuilder(str.length());
        StringTokenizer stringTokenizer = new StringTokenizer(str, ".");
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            if (stringBuilder.length() > 0) {
                stringBuilder.append('.');
            }
            if (nextToken.startsWith(ACE_PREFIX)) {
                nextToken = decode(nextToken.substring(4));
            }
            stringBuilder.append(nextToken);
        }
        return stringBuilder.toString();
    }
}
