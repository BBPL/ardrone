package org.mortbay.util;

import java.io.UnsupportedEncodingException;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;

public class URIUtil implements Cloneable {
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String HTTPS_COLON = "https:";
    public static final String HTTP_COLON = "http:";
    public static final String SLASH = "/";
    public static final String __CHARSET = System.getProperty("org.mortbay.util.URI.charset", "UTF-8");

    private URIUtil() {
    }

    public static String addPaths(String str, String str2) {
        if (str == null || str.length() == 0) {
            return (str == null || str2 != null) ? str2 : str;
        } else {
            if (str2 == null || str2.length() == 0) {
                return str;
            }
            int indexOf = str.indexOf(59);
            if (indexOf < 0) {
                indexOf = str.indexOf(63);
            }
            if (indexOf == 0) {
                return new StringBuffer().append(str2).append(str).toString();
            }
            if (indexOf < 0) {
                indexOf = str.length();
            }
            StringBuffer stringBuffer = new StringBuffer((str.length() + str2.length()) + 2);
            stringBuffer.append(str);
            if (stringBuffer.charAt(indexOf - 1) == '/') {
                if (str2.startsWith(SLASH)) {
                    stringBuffer.deleteCharAt(indexOf - 1);
                    stringBuffer.insert(indexOf - 1, str2);
                } else {
                    stringBuffer.insert(indexOf, str2);
                }
            } else if (str2.startsWith(SLASH)) {
                stringBuffer.insert(indexOf, str2);
            } else {
                stringBuffer.insert(indexOf, '/');
                stringBuffer.insert(indexOf + 1, str2);
            }
            return stringBuffer.toString();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String canonicalPath(java.lang.String r14) {
        /*
        r11 = 2;
        r2 = 0;
        r3 = -1;
        r10 = 47;
        r9 = 46;
        if (r14 == 0) goto L_0x000f;
    L_0x0009:
        r0 = r14.length();
        if (r0 != 0) goto L_0x0010;
    L_0x000f:
        return r14;
    L_0x0010:
        r4 = r14.length();
        r1 = r14.lastIndexOf(r10, r4);
    L_0x0018:
        if (r4 <= 0) goto L_0x0030;
    L_0x001a:
        r0 = r4 - r1;
        switch(r0) {
            case 2: goto L_0x0028;
            case 3: goto L_0x0073;
            default: goto L_0x001f;
        };
    L_0x001f:
        r0 = r1 + -1;
        r0 = r14.lastIndexOf(r10, r0);
        r4 = r1;
        r1 = r0;
        goto L_0x0018;
    L_0x0028:
        r0 = r1 + 1;
        r0 = r14.charAt(r0);
        if (r0 != r9) goto L_0x001f;
    L_0x0030:
        if (r1 >= r4) goto L_0x000f;
    L_0x0032:
        r7 = new java.lang.StringBuffer;
        r7.<init>(r14);
        r6 = r2;
        r0 = r3;
        r5 = r3;
    L_0x003a:
        if (r4 <= 0) goto L_0x0136;
    L_0x003c:
        r8 = r4 - r1;
        switch(r8) {
            case 2: goto L_0x0084;
            case 3: goto L_0x00f3;
            default: goto L_0x0041;
        };
    L_0x0041:
        if (r6 <= 0) goto L_0x005a;
    L_0x0043:
        r6 = r6 + -1;
        if (r6 != 0) goto L_0x005a;
    L_0x0047:
        if (r1 < 0) goto L_0x0133;
    L_0x0049:
        r0 = r1;
    L_0x004a:
        r8 = r7.length();
        if (r5 != r8) goto L_0x005a;
    L_0x0050:
        r8 = r5 + -1;
        r8 = r7.charAt(r8);
        if (r8 != r9) goto L_0x005a;
    L_0x0058:
        r0 = r0 + 1;
    L_0x005a:
        if (r6 > 0) goto L_0x0163;
    L_0x005c:
        if (r0 < 0) goto L_0x0163;
    L_0x005e:
        if (r5 < r0) goto L_0x0163;
    L_0x0060:
        r7.delete(r0, r5);
        if (r6 <= 0) goto L_0x015f;
    L_0x0065:
        r0 = r3;
    L_0x0066:
        r5 = r1 + -1;
    L_0x0068:
        if (r5 < 0) goto L_0x015b;
    L_0x006a:
        r8 = r7.charAt(r5);
        if (r8 == r10) goto L_0x015b;
    L_0x0070:
        r5 = r5 + -1;
        goto L_0x0068;
    L_0x0073:
        r0 = r1 + 1;
        r0 = r14.charAt(r0);
        if (r0 != r9) goto L_0x001f;
    L_0x007b:
        r0 = r1 + 2;
        r0 = r14.charAt(r0);
        if (r0 == r9) goto L_0x0030;
    L_0x0083:
        goto L_0x001f;
    L_0x0084:
        r8 = r1 + 1;
        r8 = r7.charAt(r8);
        if (r8 == r9) goto L_0x00aa;
    L_0x008c:
        if (r6 <= 0) goto L_0x005a;
    L_0x008e:
        r6 = r6 + -1;
        if (r6 != 0) goto L_0x005a;
    L_0x0092:
        if (r1 < 0) goto L_0x00a8;
    L_0x0094:
        r0 = r1;
    L_0x0095:
        if (r0 <= 0) goto L_0x005a;
    L_0x0097:
        r8 = r7.length();
        if (r5 != r8) goto L_0x005a;
    L_0x009d:
        r8 = r5 + -1;
        r8 = r7.charAt(r8);
        if (r8 != r9) goto L_0x005a;
    L_0x00a5:
        r0 = r0 + 1;
        goto L_0x005a;
    L_0x00a8:
        r0 = r2;
        goto L_0x0095;
    L_0x00aa:
        if (r1 >= 0) goto L_0x00bf;
    L_0x00ac:
        r8 = r7.length();
        if (r8 <= r11) goto L_0x00bf;
    L_0x00b2:
        r8 = 1;
        r8 = r7.charAt(r8);
        if (r8 != r10) goto L_0x00bf;
    L_0x00b9:
        r8 = r7.charAt(r11);
        if (r8 == r10) goto L_0x005a;
    L_0x00bf:
        if (r5 >= 0) goto L_0x00c2;
    L_0x00c1:
        r5 = r4;
    L_0x00c2:
        if (r1 < 0) goto L_0x00cc;
    L_0x00c4:
        if (r1 != 0) goto L_0x00de;
    L_0x00c6:
        r0 = r7.charAt(r1);
        if (r0 != r10) goto L_0x00de;
    L_0x00cc:
        r0 = r1 + 1;
        r8 = r7.length();
        if (r5 >= r8) goto L_0x005a;
    L_0x00d4:
        r8 = r7.charAt(r5);
        if (r8 != r10) goto L_0x005a;
    L_0x00da:
        r5 = r5 + 1;
        goto L_0x005a;
    L_0x00de:
        r0 = r7.length();
        if (r4 != r0) goto L_0x0159;
    L_0x00e4:
        r0 = r1 + 1;
    L_0x00e6:
        r4 = r1 + -1;
    L_0x00e8:
        if (r4 < 0) goto L_0x0152;
    L_0x00ea:
        r8 = r7.charAt(r4);
        if (r8 == r10) goto L_0x0152;
    L_0x00f0:
        r4 = r4 + -1;
        goto L_0x00e8;
    L_0x00f3:
        r8 = r1 + 1;
        r8 = r7.charAt(r8);
        if (r8 != r9) goto L_0x0103;
    L_0x00fb:
        r8 = r1 + 2;
        r8 = r7.charAt(r8);
        if (r8 == r9) goto L_0x0122;
    L_0x0103:
        if (r6 <= 0) goto L_0x005a;
    L_0x0105:
        r6 = r6 + -1;
        if (r6 != 0) goto L_0x005a;
    L_0x0109:
        if (r1 < 0) goto L_0x0120;
    L_0x010b:
        r0 = r1;
    L_0x010c:
        if (r0 <= 0) goto L_0x005a;
    L_0x010e:
        r8 = r7.length();
        if (r5 != r8) goto L_0x005a;
    L_0x0114:
        r8 = r5 + -1;
        r8 = r7.charAt(r8);
        if (r8 != r9) goto L_0x005a;
    L_0x011c:
        r0 = r0 + 1;
        goto L_0x005a;
    L_0x0120:
        r0 = r2;
        goto L_0x010c;
    L_0x0122:
        if (r5 >= 0) goto L_0x0150;
    L_0x0124:
        r0 = r6 + 1;
        r5 = r1 + -1;
    L_0x0128:
        if (r5 < 0) goto L_0x0146;
    L_0x012a:
        r6 = r7.charAt(r5);
        if (r6 == r10) goto L_0x0146;
    L_0x0130:
        r5 = r5 + -1;
        goto L_0x0128;
    L_0x0133:
        r0 = r2;
        goto L_0x004a;
    L_0x0136:
        if (r6 <= 0) goto L_0x013b;
    L_0x0138:
        r14 = 0;
        goto L_0x000f;
    L_0x013b:
        if (r5 < 0) goto L_0x0140;
    L_0x013d:
        r7.delete(r0, r5);
    L_0x0140:
        r14 = r7.toString();
        goto L_0x000f;
    L_0x0146:
        r6 = r1;
    L_0x0147:
        r12 = r0;
        r0 = r6;
        r6 = r12;
        r13 = r4;
        r4 = r1;
        r1 = r5;
        r5 = r13;
        goto L_0x003a;
    L_0x0150:
        r4 = r5;
        goto L_0x0124;
    L_0x0152:
        r12 = r6;
        r6 = r0;
        r0 = r12;
        r13 = r4;
        r4 = r5;
        r5 = r13;
        goto L_0x0147;
    L_0x0159:
        r0 = r1;
        goto L_0x00e6;
    L_0x015b:
        r12 = r6;
        r6 = r0;
        r0 = r12;
        goto L_0x0147;
    L_0x015f:
        r0 = r3;
        r4 = r3;
        goto L_0x0066;
    L_0x0163:
        r4 = r5;
        goto L_0x0066;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.util.URIUtil.canonicalPath(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String compactPath(java.lang.String r8) {
        /*
        r5 = 2;
        r1 = 0;
        if (r8 == 0) goto L_0x000a;
    L_0x0004:
        r0 = r8.length();
        if (r0 != 0) goto L_0x000b;
    L_0x000a:
        return r8;
    L_0x000b:
        r4 = r8.length();
        r0 = r1;
        r2 = r1;
    L_0x0011:
        if (r2 >= r4) goto L_0x0022;
    L_0x0013:
        r3 = r8.charAt(r2);
        switch(r3) {
            case 47: goto L_0x001e;
            case 63: goto L_0x000a;
            default: goto L_0x001a;
        };
    L_0x001a:
        r0 = r1;
    L_0x001b:
        r2 = r2 + 1;
        goto L_0x0011;
    L_0x001e:
        r0 = r0 + 1;
        if (r0 != r5) goto L_0x001b;
    L_0x0022:
        if (r0 < r5) goto L_0x000a;
    L_0x0024:
        r5 = new java.lang.StringBuffer;
        r3 = r8.length();
        r5.<init>(r3);
        r6 = r8.toCharArray();
        r5.append(r6, r1, r2);
        r3 = r2;
        r2 = r0;
    L_0x0036:
        if (r3 >= r4) goto L_0x004d;
    L_0x0038:
        r7 = r8.charAt(r3);
        switch(r7) {
            case 47: goto L_0x0052;
            case 63: goto L_0x0048;
            default: goto L_0x003f;
        };
    L_0x003f:
        r5.append(r7);
        r0 = r1;
    L_0x0043:
        r2 = r3 + 1;
        r3 = r2;
        r2 = r0;
        goto L_0x0036;
    L_0x0048:
        r0 = r4 - r3;
        r5.append(r6, r3, r0);
    L_0x004d:
        r8 = r5.toString();
        goto L_0x000a;
    L_0x0052:
        r0 = r2 + 1;
        if (r2 != 0) goto L_0x0043;
    L_0x0056:
        r5.append(r7);
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.util.URIUtil.compactPath(java.lang.String):java.lang.String");
    }

    public static String decodePath(String str) {
        char[] cArr = null;
        if (str == null) {
            return null;
        }
        int length = str.length();
        byte[] bArr = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < length) {
            int i4;
            char charAt = str.charAt(i);
            if (charAt == '%' && i + 2 < length) {
                if (cArr == null) {
                    cArr = new char[length];
                    bArr = new byte[length];
                    str.getChars(0, i, cArr, 0);
                }
                i4 = i2 + 1;
                bArr[i2] = (byte) (TypeUtil.parseInt(str, i + 1, 2, 16) & 255);
                i += 2;
            } else if (bArr == null) {
                i3++;
                i4 = i2;
            } else {
                if (i2 > 0) {
                    String str2;
                    try {
                        str2 = new String(bArr, 0, i2, __CHARSET);
                    } catch (UnsupportedEncodingException e) {
                        str2 = new String(bArr, 0, i2);
                    }
                    str2.getChars(0, str2.length(), cArr, i3);
                    i3 += str2.length();
                    i4 = 0;
                } else {
                    i4 = i2;
                }
                cArr[i3] = charAt;
                i3++;
            }
            i++;
            i2 = i4;
        }
        if (cArr == null) {
            return str;
        }
        if (i2 > 0) {
            String str3;
            try {
                str3 = new String(bArr, 0, i2, __CHARSET);
            } catch (UnsupportedEncodingException e2) {
                str3 = new String(bArr, 0, i2);
            }
            str3.getChars(0, str3.length(), cArr, i3);
            i3 += str3.length();
        }
        return new String(cArr, 0, i3);
    }

    public static String decodePath(byte[] bArr, int i, int i2) {
        byte[] bArr2 = null;
        int i3 = 0;
        int i4 = 0;
        while (i4 < i2) {
            byte parseInt;
            int i5;
            byte b = bArr[i4 + i];
            if (b == (byte) 37 && i4 + 2 < i2) {
                i4 += 2;
                parseInt = (byte) (TypeUtil.parseInt(bArr, (i4 + i) + 1, 2, 16) & 255);
            } else if (bArr2 == null) {
                i5 = i3 + 1;
                i4++;
                i3 = i5;
            } else {
                parseInt = b;
            }
            if (bArr2 == null) {
                bArr2 = new byte[i2];
                for (i5 = 0; i5 < i3; i5++) {
                    bArr2[i5] = bArr[i5 + i];
                }
            }
            i5 = i3 + 1;
            bArr2[i3] = parseInt;
            i4++;
            i3 = i5;
        }
        return bArr2 == null ? StringUtil.toString(bArr, i, i2, __CHARSET) : StringUtil.toString(bArr2, 0, i3, __CHARSET);
    }

    public static String encodePath(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuffer encodePath = encodePath(null, str);
        return encodePath != null ? encodePath.toString() : str;
    }

    public static StringBuffer encodePath(StringBuffer stringBuffer, String str) {
        int i = 0;
        if (stringBuffer == null) {
            int i2 = 0;
            while (i2 < str.length()) {
                switch (str.charAt(i2)) {
                    case ' ':
                    case '\"':
                    case '#':
                    case '%':
                    case '\'':
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_48_BIT_RGB_HALF /*59*/:
                    case CacheConfig.DEFAULT_ASYNCHRONOUS_WORKER_IDLE_LIFETIME_SECS /*60*/:
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF /*62*/:
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_GRAY_FIXED_POINT /*63*/:
                        stringBuffer = new StringBuffer(str.length() << 1);
                        break;
                    default:
                        i2++;
                }
                if (stringBuffer == null) {
                    return null;
                }
            }
            if (stringBuffer == null) {
                return null;
            }
        }
        synchronized (stringBuffer) {
            while (i < str.length()) {
                char charAt = str.charAt(i);
                switch (charAt) {
                    case ' ':
                        stringBuffer.append("%20");
                        break;
                    case '\"':
                        stringBuffer.append("%22");
                        break;
                    case '#':
                        stringBuffer.append("%23");
                        break;
                    case '%':
                        stringBuffer.append("%25");
                        break;
                    case '\'':
                        stringBuffer.append("%27");
                        break;
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_48_BIT_RGB_HALF /*59*/:
                        stringBuffer.append("%3B");
                        break;
                    case CacheConfig.DEFAULT_ASYNCHRONOUS_WORKER_IDLE_LIFETIME_SECS /*60*/:
                        stringBuffer.append("%3C");
                        break;
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF /*62*/:
                        stringBuffer.append("%3E");
                        break;
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_GRAY_FIXED_POINT /*63*/:
                        stringBuffer.append("%3F");
                        break;
                    default:
                        stringBuffer.append(charAt);
                        break;
                }
                i++;
            }
        }
        return stringBuffer;
    }

    public static StringBuffer encodeString(StringBuffer stringBuffer, String str, String str2) {
        int i = 0;
        if (stringBuffer == null) {
            for (int i2 = 0; i2 < str.length(); i2++) {
                char charAt = str.charAt(i2);
                if (charAt == '%' || str2.indexOf(charAt) >= 0) {
                    stringBuffer = new StringBuffer(str.length() << 1);
                    break;
                }
            }
            if (stringBuffer == null) {
                return null;
            }
        }
        synchronized (stringBuffer) {
            while (i < str.length()) {
                char charAt2 = str.charAt(i);
                if (charAt2 == '%' || str2.indexOf(charAt2) >= 0) {
                    stringBuffer.append('%');
                    StringUtil.append(stringBuffer, (byte) (charAt2 & 255), 16);
                } else {
                    stringBuffer.append(charAt2);
                }
                i++;
            }
        }
        return stringBuffer;
    }

    public static boolean hasScheme(String str) {
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == ':') {
                return true;
            }
            if ((charAt < 'a' || charAt > 'z') && (charAt < 'A' || charAt > 'Z')) {
                if (i <= 0) {
                    return false;
                }
                if (!((charAt >= '0' && charAt <= '9') || charAt == '.' || charAt == '+' || charAt == '-')) {
                    return false;
                }
            }
        }
        return false;
    }

    public static String parentPath(String str) {
        if (!(str == null || SLASH.equals(str))) {
            int lastIndexOf = str.lastIndexOf(47, str.length() - 2);
            if (lastIndexOf >= 0) {
                return str.substring(0, lastIndexOf + 1);
            }
        }
        return null;
    }

    public static String stripPath(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(59);
        return indexOf >= 0 ? str.substring(0, indexOf) : str;
    }
}
