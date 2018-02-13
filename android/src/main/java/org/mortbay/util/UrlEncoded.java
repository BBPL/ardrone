package org.mortbay.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.mortbay.jetty.HttpVersions;

public class UrlEncoded extends MultiMap {
    public static final String ENCODING = System.getProperty("org.mortbay.util.UrlEncoding.charset", "UTF-8");

    public UrlEncoded() {
        super(6);
    }

    public UrlEncoded(String str) {
        super(6);
        decode(str, ENCODING);
    }

    public UrlEncoded(String str, String str2) {
        super(6);
        decode(str, str2);
    }

    public UrlEncoded(UrlEncoded urlEncoded) {
        super((Map) urlEncoded);
    }

    public static void decode88591To(InputStream inputStream, MultiMap multiMap, int i) throws IOException {
        synchronized (multiMap) {
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            int i2 = 0;
            while (true) {
                int read = inputStream.read();
                if (read >= 0) {
                    String str2;
                    switch ((char) read) {
                        case '%':
                            read = inputStream.read();
                            int read2 = inputStream.read();
                            if (read >= 0 && read2 >= 0) {
                                stringBuffer.append((char) ((TypeUtil.convertHexDigit((byte) read) << 4) + TypeUtil.convertHexDigit((byte) read2)));
                                str2 = str;
                                break;
                            }
                            str2 = str;
                            break;
                            break;
                        case '&':
                            str2 = stringBuffer.length() == 0 ? HttpVersions.HTTP_0_9 : stringBuffer.toString();
                            stringBuffer.setLength(0);
                            if (str != null) {
                                multiMap.add(str, str2);
                            } else if (str2 != null && str2.length() > 0) {
                                multiMap.add(str2, HttpVersions.HTTP_0_9);
                            }
                            str2 = null;
                            break;
                        case '+':
                            stringBuffer.append(' ');
                            str2 = str;
                            break;
                        case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                            if (str == null) {
                                str2 = stringBuffer.toString();
                                stringBuffer.setLength(0);
                                break;
                            }
                            stringBuffer.append((char) read);
                            str2 = str;
                            break;
                        default:
                            stringBuffer.append((char) read);
                            str2 = str;
                            break;
                    }
                    if (i >= 0) {
                        int i3 = i2 + 1;
                        if (i3 > i) {
                            throw new IllegalStateException("Form too large");
                        }
                        i2 = i3;
                        str = str2;
                    } else {
                        str = str2;
                    }
                } else {
                    if (str != null) {
                        Object stringBuffer2 = stringBuffer.length() == 0 ? HttpVersions.HTTP_0_9 : stringBuffer.toString();
                        stringBuffer.setLength(0);
                        multiMap.add(str, stringBuffer2);
                    } else if (stringBuffer.length() > 0) {
                        multiMap.add(stringBuffer.toString(), HttpVersions.HTTP_0_9);
                    }
                }
            }
        }
    }

    public static String decodeString(String str, int i, int i2, String str2) {
        int i3;
        Utf8StringBuffer utf8StringBuffer;
        int i4;
        Throwable e;
        if (str2 == null || StringUtil.isUTF8(str2)) {
            Utf8StringBuffer utf8StringBuffer2 = null;
            i3 = 0;
            while (i3 < i2) {
                char charAt = str.charAt(i + i3);
                if (charAt < '\u0000' || charAt > 'ÿ') {
                    if (utf8StringBuffer2 == null) {
                        utf8StringBuffer2 = new Utf8StringBuffer(i2);
                        utf8StringBuffer2.getStringBuffer().append(str.substring(i, (i + i3) + 1));
                    } else {
                        utf8StringBuffer2.getStringBuffer().append(charAt);
                    }
                } else if (charAt == '+') {
                    if (utf8StringBuffer2 == null) {
                        utf8StringBuffer2 = new Utf8StringBuffer(i2);
                        utf8StringBuffer2.getStringBuffer().append(str.substring(i, i + i3));
                    }
                    utf8StringBuffer2.getStringBuffer().append(' ');
                } else if (charAt == '%' && i3 + 2 < i2) {
                    if (utf8StringBuffer2 == null) {
                        utf8StringBuffer = new Utf8StringBuffer(i2);
                        utf8StringBuffer.getStringBuffer().append(str.substring(i, i + i3));
                        i4 = i3;
                    } else {
                        utf8StringBuffer = utf8StringBuffer2;
                        i4 = i3;
                    }
                    while (charAt == '%' && i4 + 2 < i2) {
                        try {
                            utf8StringBuffer.append((byte) TypeUtil.parseInt(str, (i + i4) + 1, 2, 16));
                            i3 = i4 + 3;
                        } catch (NumberFormatException e2) {
                            utf8StringBuffer.getStringBuffer().append('%');
                            while (true) {
                                i3 = i4 + 1;
                                char charAt2 = str.charAt(i3 + i);
                                if (charAt2 == '%') {
                                    break;
                                }
                                StringBuffer stringBuffer = utf8StringBuffer.getStringBuffer();
                                if (charAt2 == '+') {
                                    charAt2 = ' ';
                                }
                                stringBuffer.append(charAt2);
                                i4 = i3;
                            }
                        }
                        if (i3 < i2) {
                            charAt = str.charAt(i + i3);
                            i4 = i3;
                        } else {
                            i4 = i3;
                        }
                    }
                    i3 = i4 - 1;
                    utf8StringBuffer2 = utf8StringBuffer;
                } else if (utf8StringBuffer2 != null) {
                    utf8StringBuffer2.getStringBuffer().append(charAt);
                }
                i3++;
            }
            return utf8StringBuffer2 == null ? (i == 0 && str.length() == i2) ? str : str.substring(i, i + i2) : utf8StringBuffer2.toString();
        } else {
            i3 = 0;
            StringBuffer stringBuffer2 = null;
            while (i3 < i2) {
                char charAt3 = str.charAt(i + i3);
                if (charAt3 < '\u0000' || charAt3 > 'ÿ') {
                    if (stringBuffer2 == null) {
                        stringBuffer2 = new StringBuffer(i2);
                        stringBuffer2.append(str.substring(i, (i + i3) + 1));
                    } else {
                        try {
                            stringBuffer2.append(charAt3);
                        } catch (UnsupportedEncodingException e3) {
                            e = e3;
                        }
                    }
                } else if (charAt3 == '+') {
                    if (stringBuffer2 == null) {
                        stringBuffer2 = new StringBuffer(i2);
                        stringBuffer2.append(str.substring(i, i + i3));
                    }
                    stringBuffer2.append(' ');
                } else if (charAt3 == '%' && i3 + 2 < i2) {
                    int i5;
                    if (stringBuffer2 == null) {
                        stringBuffer2 = new StringBuffer(i2);
                        stringBuffer2.append(str.substring(i, i + i3));
                    }
                    byte[] bArr = new byte[i2];
                    int i6 = charAt3;
                    int i7 = i3;
                    i3 = 0;
                    while (i6 >= 0 && i6 <= 255) {
                        int i8;
                        if (i6 == 37) {
                            if (i7 + 2 < i2) {
                                i8 = i3 + 1;
                                try {
                                    bArr[i3] = (byte) TypeUtil.parseInt(str, (i + i7) + 1, 2, 16);
                                    i7 += 3;
                                    i3 = i8;
                                } catch (NumberFormatException e4) {
                                    bArr[i8 - 1] = (byte) 37;
                                    i3 = i8;
                                    while (true) {
                                        i7++;
                                        try {
                                            i8 = str.charAt(i7 + i);
                                            if (i8 == 37) {
                                                break;
                                            }
                                            if (i8 == 43) {
                                                i8 = 32;
                                            }
                                            bArr[i3] = (byte) i8;
                                            i3++;
                                        } catch (UnsupportedEncodingException e5) {
                                            e = e5;
                                        }
                                    }
                                }
                            } else {
                                i8 = i3 + 1;
                                bArr[i3] = (byte) 37;
                                i7++;
                                i3 = i8;
                            }
                        } else if (i6 == 43) {
                            i8 = i3 + 1;
                            bArr[i3] = (byte) 32;
                            i7++;
                            i3 = i8;
                        } else {
                            i8 = i3 + 1;
                            bArr[i3] = (byte) i6;
                            i7++;
                            i3 = i8;
                        }
                        if (i7 >= i2) {
                            i5 = i3;
                            i3 = i7;
                            i7 = i5;
                            break;
                        }
                        char charAt4 = str.charAt(i + i7);
                    }
                    i5 = i3;
                    i3 = i7;
                    i7 = i5;
                    i3--;
                    stringBuffer2.append(new String(bArr, 0, i7, str2));
                } else if (stringBuffer2 != null) {
                    stringBuffer2.append(charAt3);
                } else {
                    continue;
                }
                i3++;
            }
            return stringBuffer2 == null ? (i == 0 && str.length() == i2) ? str : str.substring(i, i + i2) : stringBuffer2.toString();
        }
        throw new RuntimeException(e);
    }

    public static void decodeTo(InputStream inputStream, MultiMap multiMap, String str, int i) throws IOException {
        if (str == null || "UTF-8".equalsIgnoreCase(str)) {
            decodeUtf8To(inputStream, multiMap, i);
        } else if (StringUtil.__ISO_8859_1.equals(str)) {
            decode88591To(inputStream, multiMap, i);
        } else if ("UTF-16".equalsIgnoreCase(str)) {
            decodeUtf16To(inputStream, multiMap, i);
        } else {
            synchronized (multiMap) {
                ByteArrayOutputStream2 byteArrayOutputStream2 = new ByteArrayOutputStream2();
                String str2 = null;
                Object obj = null;
                byte b = (byte) 0;
                int i2 = 0;
                while (true) {
                    int read = inputStream.read();
                    if (read > 0) {
                        String str3;
                        byte b2;
                        Object obj2;
                        switch ((char) read) {
                            case '%':
                                str3 = str2;
                                b2 = b;
                                int i3 = 2;
                                break;
                            case '&':
                                str3 = byteArrayOutputStream2.size() == 0 ? HttpVersions.HTTP_0_9 : byteArrayOutputStream2.toString(str);
                                byteArrayOutputStream2.setCount(0);
                                if (str2 != null) {
                                    multiMap.add(str2, str3);
                                } else if (str3 != null && str3.length() > 0) {
                                    multiMap.add(str3, HttpVersions.HTTP_0_9);
                                }
                                str3 = null;
                                b2 = b;
                                obj2 = obj;
                                break;
                            case '+':
                                byteArrayOutputStream2.write(32);
                                str3 = str2;
                                b2 = b;
                                obj2 = obj;
                                break;
                            case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                                if (str2 == null) {
                                    str3 = byteArrayOutputStream2.size() == 0 ? HttpVersions.HTTP_0_9 : byteArrayOutputStream2.toString(str);
                                    byteArrayOutputStream2.setCount(0);
                                    b2 = b;
                                    obj2 = obj;
                                    break;
                                }
                                byteArrayOutputStream2.write(read);
                                str3 = str2;
                                b2 = b;
                                obj2 = obj;
                                break;
                            default:
                                if (obj != 2) {
                                    if (obj != 1) {
                                        byteArrayOutputStream2.write(read);
                                        str3 = str2;
                                        b2 = b;
                                        obj2 = obj;
                                        break;
                                    }
                                    byteArrayOutputStream2.write(TypeUtil.convertHexDigit((byte) read) + (b << 4));
                                    str3 = str2;
                                    b2 = b;
                                    obj2 = null;
                                    break;
                                }
                                obj2 = 1;
                                byte convertHexDigit = TypeUtil.convertHexDigit((byte) read);
                                str3 = str2;
                                b2 = convertHexDigit;
                                break;
                        }
                        int i4 = i2 + 1;
                        if (i < 0 || i4 <= i) {
                            i2 = i4;
                            obj = obj2;
                            b = b2;
                            str2 = str3;
                        } else {
                            throw new IllegalStateException("Form too large");
                        }
                    }
                    read = byteArrayOutputStream2.size();
                    if (str2 != null) {
                        Object byteArrayOutputStream22 = read == 0 ? HttpVersions.HTTP_0_9 : byteArrayOutputStream2.toString(str);
                        byteArrayOutputStream2.setCount(0);
                        multiMap.add(str2, byteArrayOutputStream22);
                    } else if (read > 0) {
                        multiMap.add(byteArrayOutputStream2.toString(str), HttpVersions.HTTP_0_9);
                    }
                }
            }
        }
    }

    public static void decodeTo(String str, MultiMap multiMap, String str2) {
        if (str2 == null) {
            str2 = ENCODING;
        }
        synchronized (multiMap) {
            int i = -1;
            Object obj = null;
            Object obj2 = null;
            int i2 = 0;
            while (i2 < str.length()) {
                int i3;
                String decodeString;
                switch (str.charAt(i2)) {
                    case '%':
                        i3 = 1;
                        break;
                    case '&':
                        int i4 = (i2 - i) - 1;
                        decodeString = i4 == 0 ? HttpVersions.HTTP_0_9 : obj2 != null ? decodeString(str, i + 1, i4, str2) : str.substring(i + 1, i2);
                        if (obj != null) {
                            multiMap.add(obj, decodeString);
                        } else if (decodeString != null && decodeString.length() > 0) {
                            multiMap.add(decodeString, HttpVersions.HTTP_0_9);
                        }
                        i = i2;
                        obj2 = null;
                        obj = null;
                        break;
                    case '+':
                        i3 = 1;
                        break;
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                        if (obj == null) {
                            decodeString = obj2 != null ? decodeString(str, i + 1, (i2 - i) - 1, str2) : str.substring(i + 1, i2);
                            obj2 = null;
                            String str3 = decodeString;
                            i = i2;
                            break;
                        }
                        continue;
                    default:
                        break;
                }
                i2++;
            }
            if (obj != null) {
                i2 = (str.length() - i) - 1;
                Object decodeString2 = i2 == 0 ? HttpVersions.HTTP_0_9 : obj2 != null ? decodeString(str, i + 1, i2, str2) : str.substring(i + 1);
                multiMap.add(obj, decodeString2);
            } else if (i < str.length()) {
                multiMap.add(obj2 != null ? decodeString(str, i + 1, (str.length() - i) - 1, str2) : str.substring(i + 1), HttpVersions.HTTP_0_9);
            }
        }
    }

    public static void decodeUtf16To(InputStream inputStream, MultiMap multiMap, int i) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-16");
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = 0;
        if (i < 0) {
            i = Integer.MAX_VALUE;
        }
        while (true) {
            int read = inputStreamReader.read();
            if (read <= 0 || i2 >= r6) {
                decodeTo(stringBuffer.toString(), multiMap, ENCODING);
            } else {
                stringBuffer.append((char) read);
                i2++;
            }
        }
        decodeTo(stringBuffer.toString(), multiMap, ENCODING);
    }

    public static void decodeUtf8To(InputStream inputStream, MultiMap multiMap, int i) throws IOException {
        synchronized (multiMap) {
            Utf8StringBuffer utf8StringBuffer = new Utf8StringBuffer();
            String str = null;
            int i2 = 0;
            while (true) {
                int read = inputStream.read();
                if (read >= 0) {
                    String str2;
                    switch ((char) read) {
                        case '%':
                            read = inputStream.read();
                            int read2 = inputStream.read();
                            if (read >= 0 && read2 >= 0) {
                                utf8StringBuffer.append((byte) ((TypeUtil.convertHexDigit((byte) read) << 4) + TypeUtil.convertHexDigit((byte) read2)));
                                str2 = str;
                                break;
                            }
                            str2 = str;
                            break;
                        case '&':
                            str2 = utf8StringBuffer.length() == 0 ? HttpVersions.HTTP_0_9 : utf8StringBuffer.toString();
                            utf8StringBuffer.reset();
                            if (str != null) {
                                multiMap.add(str, str2);
                            } else if (str2 != null && str2.length() > 0) {
                                multiMap.add(str2, HttpVersions.HTTP_0_9);
                            }
                            str2 = null;
                            break;
                        case '+':
                            utf8StringBuffer.append((byte) 32);
                            str2 = str;
                            break;
                        case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                            if (str == null) {
                                str2 = utf8StringBuffer.toString();
                                utf8StringBuffer.reset();
                                break;
                            }
                            utf8StringBuffer.append((byte) read);
                            str2 = str;
                            break;
                        default:
                            utf8StringBuffer.append((byte) read);
                            str2 = str;
                            break;
                    }
                    if (i >= 0) {
                        int i3 = i2 + 1;
                        if (i3 > i) {
                            throw new IllegalStateException("Form too large");
                        }
                        i2 = i3;
                        str = str2;
                    } else {
                        str = str2;
                    }
                } else {
                    if (str != null) {
                        Object utf8StringBuffer2 = utf8StringBuffer.length() == 0 ? HttpVersions.HTTP_0_9 : utf8StringBuffer.toString();
                        utf8StringBuffer.reset();
                        multiMap.add(str, utf8StringBuffer2);
                    } else if (utf8StringBuffer.length() > 0) {
                        multiMap.add(utf8StringBuffer.toString(), HttpVersions.HTTP_0_9);
                    }
                }
            }
        }
    }

    public static void decodeUtf8To(byte[] bArr, int i, int i2, MultiMap multiMap) {
        decodeUtf8To(bArr, i, i2, multiMap, new Utf8StringBuffer());
    }

    public static void decodeUtf8To(byte[] bArr, int i, int i2, MultiMap multiMap, Utf8StringBuffer utf8StringBuffer) {
        synchronized (multiMap) {
            int i3 = i + i2;
            Object obj = null;
            int i4 = i;
            while (i4 < i3) {
                byte b = bArr[i4];
                switch ((char) (b & 255)) {
                    case '%':
                        if (i4 + 2 >= i3) {
                            break;
                        }
                        i4++;
                        i4++;
                        utf8StringBuffer.append((byte) ((TypeUtil.convertHexDigit(bArr[i4]) << 4) + TypeUtil.convertHexDigit(bArr[i4])));
                        break;
                    case '&':
                        String utf8StringBuffer2 = utf8StringBuffer.length() == 0 ? HttpVersions.HTTP_0_9 : utf8StringBuffer.toString();
                        utf8StringBuffer.reset();
                        if (obj != null) {
                            multiMap.add(obj, utf8StringBuffer2);
                        } else if (utf8StringBuffer2 != null && utf8StringBuffer2.length() > 0) {
                            multiMap.add(utf8StringBuffer2, HttpVersions.HTTP_0_9);
                        }
                        obj = null;
                        break;
                    case '+':
                        utf8StringBuffer.append((byte) 32);
                        break;
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                        if (obj == null) {
                            obj = utf8StringBuffer.toString();
                            utf8StringBuffer.reset();
                            break;
                        }
                        utf8StringBuffer.append(b);
                        break;
                    default:
                        utf8StringBuffer.append(b);
                        break;
                }
                i4++;
            }
            if (obj != null) {
                Object utf8StringBuffer3 = utf8StringBuffer.length() == 0 ? HttpVersions.HTTP_0_9 : utf8StringBuffer.toString();
                utf8StringBuffer.reset();
                multiMap.add(obj, utf8StringBuffer3);
            } else if (utf8StringBuffer.length() > 0) {
                multiMap.add(utf8StringBuffer.toString(), HttpVersions.HTTP_0_9);
            }
        }
    }

    public static String encode(MultiMap multiMap, String str, boolean z) {
        String stringBuffer;
        if (str == null) {
            str = ENCODING;
        }
        StringBuffer stringBuffer2 = new StringBuffer(128);
        synchronized (stringBuffer2) {
            Iterator it = multiMap.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                String obj = entry.getKey().toString();
                Object value = entry.getValue();
                int size = LazyList.size(value);
                if (size == 0) {
                    stringBuffer2.append(encodeString(obj, str));
                    if (z) {
                        stringBuffer2.append('=');
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        if (i > 0) {
                            stringBuffer2.append('&');
                        }
                        Object obj2 = LazyList.get(value, i);
                        stringBuffer2.append(encodeString(obj, str));
                        if (obj2 != null) {
                            String obj3 = obj2.toString();
                            if (obj3.length() > 0) {
                                stringBuffer2.append('=');
                                stringBuffer2.append(encodeString(obj3, str));
                            } else if (z) {
                                stringBuffer2.append('=');
                            }
                        } else if (z) {
                            stringBuffer2.append('=');
                        }
                    }
                }
                if (it.hasNext()) {
                    stringBuffer2.append('&');
                }
            }
            stringBuffer = stringBuffer2.toString();
        }
        return stringBuffer;
    }

    public static String encodeString(String str) {
        return encodeString(str, ENCODING);
    }

    public static String encodeString(String str, String str2) {
        byte[] bytes;
        if (str2 == null) {
            str2 = ENCODING;
        }
        try {
            bytes = str.getBytes(str2);
        } catch (UnsupportedEncodingException e) {
            bytes = str.getBytes();
        }
        int length = bytes.length;
        byte[] bArr = new byte[(bytes.length * 3)];
        int i = 1;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            int i4;
            byte b = bytes[i2];
            if (b == (byte) 32) {
                i = i3 + 1;
                bArr[i3] = (byte) 43;
                i4 = i;
                i = 0;
            } else if ((b < (byte) 97 || b > (byte) 122) && ((b < (byte) 65 || b > (byte) 90) && (b < (byte) 48 || b > (byte) 57))) {
                i4 = i3 + 1;
                bArr[i3] = (byte) 37;
                byte b2 = (byte) ((b & 240) >> 4);
                if (b2 >= (byte) 10) {
                    i = i4 + 1;
                    bArr[i4] = (byte) ((b2 + 65) - 10);
                    i4 = i;
                } else {
                    i = i4 + 1;
                    bArr[i4] = (byte) (b2 + 48);
                    i4 = i;
                }
                b2 = (byte) (b & 15);
                if (b2 >= (byte) 10) {
                    i = i4 + 1;
                    bArr[i4] = (byte) ((b2 + 65) - 10);
                    i4 = i;
                    i = 0;
                } else {
                    i = i4 + 1;
                    bArr[i4] = (byte) (b2 + 48);
                    i4 = i;
                    i = 0;
                }
            } else {
                i4 = i3 + 1;
                bArr[i3] = b;
            }
            i2++;
            i3 = i4;
        }
        if (i != 0) {
            return str;
        }
        try {
            return new String(bArr, 0, i3, str2);
        } catch (UnsupportedEncodingException e2) {
            return new String(bArr, 0, i3);
        }
    }

    public Object clone() {
        return new UrlEncoded(this);
    }

    public void decode(String str) {
        decodeTo(str, this, ENCODING);
    }

    public void decode(String str, String str2) {
        decodeTo(str, this, str2);
    }

    public String encode() {
        return encode(ENCODING, false);
    }

    public String encode(String str) {
        return encode(str, false);
    }

    public String encode(String str, boolean z) {
        String encode;
        synchronized (this) {
            encode = encode(this, str, z);
        }
        return encode;
    }
}
