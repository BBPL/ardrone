package org.mortbay.jetty.security;

import com.google.common.primitives.SignedBytes;
import java.io.UnsupportedEncodingException;
import org.mortbay.util.StringUtil;

public class B64Code {
    static byte[] code2nibble = null;
    static final char[] nibble2code = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    static final char pad = '=';

    static {
        code2nibble = null;
        code2nibble = new byte[256];
        for (int i = 0; i < 256; i++) {
            code2nibble[i] = (byte) -1;
        }
        for (byte b = (byte) 0; b < SignedBytes.MAX_POWER_OF_TWO; b = (byte) (b + 1)) {
            code2nibble[(byte) nibble2code[b]] = b;
        }
        code2nibble[61] = (byte) 0;
    }

    public static String decode(String str) {
        try {
            return decode(str, StringUtil.__ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public static String decode(String str, String str2) throws UnsupportedEncodingException {
        byte[] decode = decode(str.toCharArray());
        return str2 == null ? new String(decode) : new String(decode, str2);
    }

    public static byte[] decode(char[] cArr) {
        int i = 0;
        if (cArr == null) {
            return null;
        }
        int length = cArr.length;
        if (length % 4 != 0) {
            throw new IllegalArgumentException("Input block size is not 4");
        }
        length--;
        while (length >= 0 && cArr[length] == pad) {
            length--;
        }
        if (length < 0) {
            return new byte[0];
        }
        byte b;
        int i2 = ((length + 1) * 3) / 4;
        byte[] bArr = new byte[i2];
        int i3 = i2 / 3;
        int i4 = 0;
        while (i4 < i3 * 3) {
            int i5 = i + 1;
            byte b2 = code2nibble[cArr[i]];
            i = i5 + 1;
            byte b3 = code2nibble[cArr[i5]];
            try {
                i5 = i + 1;
                byte b4 = code2nibble[cArr[i]];
            } catch (IndexOutOfBoundsException e) {
                length = i;
            }
            try {
                i = i5 + 1;
                b = code2nibble[cArr[i5]];
                if (b2 < (byte) 0 || b3 < (byte) 0 || b4 < (byte) 0 || b < (byte) 0) {
                    throw new IllegalArgumentException("Not B64 encoded");
                }
                int i6 = i4 + 1;
                bArr[i4] = (byte) ((b2 << 2) | (b3 >>> 4));
                i4 = i6 + 1;
                bArr[i6] = (byte) ((b3 << 4) | (b4 >>> 2));
                bArr[i4] = (byte) (b | (b4 << 6));
                i4++;
            } catch (IndexOutOfBoundsException e2) {
                length = i5;
            }
        }
        if (i2 == i4) {
            return bArr;
        }
        byte b5;
        switch (i2 % 3) {
            case 1:
                i5 = i + 1;
                b5 = code2nibble[cArr[i]];
                i = i5 + 1;
                b = code2nibble[cArr[i5]];
                if (b5 < (byte) 0 || b < (byte) 0) {
                    throw new IllegalArgumentException("Not B64 encoded");
                }
                bArr[i4] = (byte) ((b5 << 2) | (b >>> 4));
                return bArr;
            case 2:
                i5 = i + 1;
                b5 = code2nibble[cArr[i]];
                i = i5 + 1;
                byte b6 = code2nibble[cArr[i5]];
                i5 = i + 1;
                byte b7 = code2nibble[cArr[i]];
                if (b5 < (byte) 0 || b6 < (byte) 0 || b7 < (byte) 0) {
                    throw new IllegalArgumentException("Not B64 encoded");
                }
                i5 = i4 + 1;
                bArr[i4] = (byte) ((b5 << 2) | (b6 >>> 4));
                bArr[i5] = (byte) ((b7 >>> 2) | (b6 << 4));
                return bArr;
            default:
                return bArr;
        }
        throw new IllegalArgumentException(new StringBuffer().append("char ").append(length).append(" was not B64 encoded").toString());
    }

    public static String encode(String str) {
        try {
            return encode(str, null);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public static String encode(String str, String str2) throws UnsupportedEncodingException {
        return new String(encode(str2 == null ? str.getBytes(StringUtil.__ISO_8859_1) : str.getBytes(str2)));
    }

    public static char[] encode(byte[] bArr) {
        int i = 0;
        if (bArr == null) {
            return null;
        }
        int length = bArr.length;
        char[] cArr = new char[(((length + 2) / 3) * 4)];
        int i2 = length / 3;
        int i3 = 0;
        while (i < i2 * 3) {
            int i4 = i + 1;
            byte b = bArr[i];
            int i5 = i4 + 1;
            byte b2 = bArr[i4];
            byte b3 = bArr[i5];
            int i6 = i3 + 1;
            cArr[i3] = nibble2code[(b >>> 2) & 63];
            i3 = i6 + 1;
            cArr[i6] = nibble2code[((b << 4) & 63) | ((b2 >>> 4) & 15)];
            i = i3 + 1;
            cArr[i3] = nibble2code[((b2 << 2) & 63) | ((b3 >>> 6) & 3)];
            i3 = i + 1;
            cArr[i] = nibble2code[b3 & 63];
            i = i5 + 1;
        }
        if (length == i) {
            return cArr;
        }
        switch (length % 3) {
            case 1:
                b = bArr[i];
                length = i3 + 1;
                cArr[i3] = nibble2code[(b >>> 2) & 63];
                i3 = length + 1;
                cArr[length] = nibble2code[(b << 4) & 63];
                i = i3 + 1;
                cArr[i3] = pad;
                cArr[i] = pad;
                return cArr;
            case 2:
                length = i + 1;
                b = bArr[i];
                byte b4 = bArr[length];
                i2 = i3 + 1;
                cArr[i3] = nibble2code[(b >>> 2) & 63];
                i3 = i2 + 1;
                cArr[i2] = nibble2code[((b << 4) & 63) | ((b4 >>> 4) & 15)];
                i = i3 + 1;
                cArr[i3] = nibble2code[(b4 << 2) & 63];
                cArr[i] = pad;
                return cArr;
            default:
                return cArr;
        }
    }
}
