package org.mortbay.util;

import com.google.common.base.Ascii;
import java.io.UnsupportedEncodingException;
import org.mortbay.jetty.HttpVersions;

public class StringUtil {
    public static final String CRLF = "\r\n";
    public static final String __ISO_8859_1;
    public static final String __LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    public static final String __UTF16 = "UTF-16";
    public static final String __UTF8 = "UTF-8";
    public static final String __UTF8Alt = "UTF8";
    private static char[] lowercases = new char[]{'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001a', '\u001b', '\u001c', '\u001d', '\u001e', '\u001f', ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', Ascii.MAX};

    static {
        String property = System.getProperty("ISO_8859_1");
        if (property == null) {
            try {
                property = new String(new byte[]{Ascii.DC4}, "ISO-8859-1");
                property = "ISO-8859-1";
            } catch (UnsupportedEncodingException e) {
                property = "ISO8859_1";
            }
        }
        __ISO_8859_1 = property;
    }

    public static void append(StringBuffer stringBuffer, byte b, int i) {
        int i2 = b & 255;
        int i3 = ((i2 / i) % i) + 48;
        if (i3 > 57) {
            i3 = ((i3 - 48) - 10) + 97;
        }
        stringBuffer.append((char) i3);
        i3 = (i2 % i) + 48;
        if (i3 > 57) {
            i3 = ((i3 - 48) - 10) + 97;
        }
        stringBuffer.append((char) i3);
    }

    public static void append(StringBuffer stringBuffer, String str, int i, int i2) {
        synchronized (stringBuffer) {
            for (int i3 = i; i3 < i + i2; i3++) {
                if (i3 >= str.length()) {
                    break;
                }
                stringBuffer.append(str.charAt(i3));
            }
        }
    }

    public static void append2digits(StringBuffer stringBuffer, int i) {
        if (i < 100) {
            stringBuffer.append((char) ((i / 10) + 48));
            stringBuffer.append((char) ((i % 10) + 48));
        }
    }

    public static java.lang.String asciiToLowerCase(java.lang.String r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r4 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r0 = 0;
        r1 = r5.length();
        r2 = r1;
    L_0x0008:
        r1 = r2 + -1;
        if (r2 <= 0) goto L_0x003c;
    L_0x000c:
        r2 = r5.charAt(r1);
        if (r2 > r4) goto L_0x0031;
    L_0x0012:
        r3 = lowercases;
        r3 = r3[r2];
        if (r2 == r3) goto L_0x0031;
    L_0x0018:
        r0 = r5.toCharArray();
        r0[r1] = r3;
        r2 = r1;
    L_0x001f:
        r1 = r2 + -1;
        if (r2 <= 0) goto L_0x0033;
    L_0x0023:
        r2 = r0[r1];
        if (r2 > r4) goto L_0x003c;
    L_0x0027:
        r2 = lowercases;
        r3 = r0[r1];
        r2 = r2[r3];
        r0[r1] = r2;
        r2 = r1;
        goto L_0x001f;
    L_0x0031:
        r2 = r1;
        goto L_0x0008;
    L_0x0033:
        if (r0 != 0) goto L_0x0036;
    L_0x0035:
        return r5;
    L_0x0036:
        r5 = new java.lang.String;
        r5.<init>(r0);
        goto L_0x0035;
    L_0x003c:
        r2 = r1;
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.util.StringUtil.asciiToLowerCase(java.lang.String):java.lang.String");
    }

    public static boolean endsWithIgnoreCase(String str, String str2) {
        if (str2 != null) {
            if (str == null) {
                return false;
            }
            int length = str.length();
            int length2 = str2.length();
            if (length >= length2) {
                while (true) {
                    int i = length2 - 1;
                    if (length2 <= 0) {
                        break;
                    }
                    int i2 = length - 1;
                    char charAt = str.charAt(i2);
                    char charAt2 = str2.charAt(i);
                    if (charAt != charAt2) {
                        if (charAt <= Ascii.MAX) {
                            charAt = lowercases[charAt];
                        }
                        if (charAt2 <= Ascii.MAX) {
                            charAt2 = lowercases[charAt2];
                        }
                        if (charAt != charAt2) {
                            return false;
                        }
                    }
                    length = i2;
                    length2 = i;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(String str, char[] cArr, int i, int i2) {
        if (str.length() != i2) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (cArr[i + i3] != str.charAt(i3)) {
                return false;
            }
        }
        return true;
    }

    public static int indexFrom(String str, String str2) {
        for (int i = 0; i < str.length(); i++) {
            if (str2.indexOf(str.charAt(i)) >= 0) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isUTF8(String str) {
        return str == "UTF-8" || "UTF-8".equalsIgnoreCase(str) || __UTF8Alt.equalsIgnoreCase(str);
    }

    public static String nonNull(String str) {
        return str == null ? HttpVersions.HTTP_0_9 : str;
    }

    public static String printable(String str) {
        if (str == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (!Character.isISOControl(charAt)) {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer.toString();
    }

    public static String replace(String str, String str2, String str3) {
        int i = 0;
        int indexOf = str.indexOf(str2, 0);
        if (indexOf != -1) {
            StringBuffer stringBuffer = new StringBuffer(str.length() + str3.length());
            synchronized (stringBuffer) {
                do {
                    stringBuffer.append(str.substring(i, indexOf));
                    stringBuffer.append(str3);
                    i = str2.length() + indexOf;
                    indexOf = str.indexOf(str2, i);
                } while (indexOf != -1);
                if (i < str.length()) {
                    stringBuffer.append(str.substring(i, str.length()));
                }
                str = stringBuffer.toString();
            }
        }
        return str;
    }

    public static boolean startsWithIgnoreCase(String str, String str2) {
        if (str2 != null) {
            if (str == null || str.length() < str2.length()) {
                return false;
            }
            for (int i = 0; i < str2.length(); i++) {
                char charAt = str.charAt(i);
                char charAt2 = str2.charAt(i);
                if (charAt != charAt2) {
                    if (charAt <= Ascii.MAX) {
                        charAt = lowercases[charAt];
                    }
                    if (charAt2 <= Ascii.MAX) {
                        charAt2 = lowercases[charAt2];
                    }
                    if (charAt != charAt2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static String toString(byte[] bArr, int i, int i2, String str) {
        if (str == null || isUTF8(str)) {
            return toUTF8String(bArr, i, i2);
        }
        try {
            return new String(bArr, i, i2, str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toUTF8String(byte[] bArr, int i, int i2) {
        if (i2 >= 32) {
            return new String(bArr, i, i2, "UTF-8");
        }
        try {
            Utf8StringBuffer utf8StringBuffer = new Utf8StringBuffer(i2);
            utf8StringBuffer.append(bArr, i, i2);
            return utf8StringBuffer.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String unquote(String str) {
        return QuotedStringTokenizer.unquote(str);
    }
}
