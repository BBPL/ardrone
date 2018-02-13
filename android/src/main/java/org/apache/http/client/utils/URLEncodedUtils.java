package org.apache.http.client.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

@Immutable
public class URLEncodedUtils {
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final char[] DELIM = new char[]{'&'};
    private static final BitSet FRAGMENT = new BitSet(256);
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final BitSet PATHSAFE = new BitSet(256);
    private static final BitSet PUNCT = new BitSet(256);
    private static final int RADIX = 16;
    private static final BitSet RESERVED = new BitSet(256);
    private static final BitSet UNRESERVED = new BitSet(256);
    private static final BitSet URLENCODER = new BitSet(256);
    private static final BitSet USERINFO = new BitSet(256);

    static {
        int i;
        for (i = 97; i <= 122; i++) {
            UNRESERVED.set(i);
        }
        for (i = 65; i <= 90; i++) {
            UNRESERVED.set(i);
        }
        for (i = 48; i <= 57; i++) {
            UNRESERVED.set(i);
        }
        UNRESERVED.set(95);
        UNRESERVED.set(45);
        UNRESERVED.set(46);
        UNRESERVED.set(42);
        URLENCODER.or(UNRESERVED);
        UNRESERVED.set(33);
        UNRESERVED.set(126);
        UNRESERVED.set(39);
        UNRESERVED.set(40);
        UNRESERVED.set(41);
        PUNCT.set(44);
        PUNCT.set(59);
        PUNCT.set(58);
        PUNCT.set(36);
        PUNCT.set(38);
        PUNCT.set(43);
        PUNCT.set(61);
        USERINFO.or(UNRESERVED);
        USERINFO.or(PUNCT);
        PATHSAFE.or(UNRESERVED);
        PATHSAFE.set(47);
        PATHSAFE.set(59);
        PATHSAFE.set(58);
        PATHSAFE.set(64);
        PATHSAFE.set(38);
        PATHSAFE.set(61);
        PATHSAFE.set(43);
        PATHSAFE.set(36);
        PATHSAFE.set(44);
        RESERVED.set(59);
        RESERVED.set(47);
        RESERVED.set(63);
        RESERVED.set(58);
        RESERVED.set(64);
        RESERVED.set(38);
        RESERVED.set(61);
        RESERVED.set(43);
        RESERVED.set(36);
        RESERVED.set(44);
        RESERVED.set(91);
        RESERVED.set(93);
        FRAGMENT.or(RESERVED);
        FRAGMENT.or(UNRESERVED);
    }

    private static String decodeFormFields(String str, String str2) {
        if (str == null) {
            return null;
        }
        return urldecode(str, str2 != null ? Charset.forName(str2) : Consts.UTF_8, true);
    }

    private static String decodeFormFields(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        if (charset == null) {
            charset = Consts.UTF_8;
        }
        return urldecode(str, charset, true);
    }

    static String encFragment(String str, Charset charset) {
        return urlencode(str, charset, FRAGMENT, false);
    }

    static String encPath(String str, Charset charset) {
        return urlencode(str, charset, PATHSAFE, false);
    }

    static String encUserInfo(String str, Charset charset) {
        return urlencode(str, charset, USERINFO, false);
    }

    private static String encodeFormFields(String str, String str2) {
        if (str == null) {
            return null;
        }
        return urlencode(str, str2 != null ? Charset.forName(str2) : Consts.UTF_8, URLENCODER, true);
    }

    private static String encodeFormFields(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        if (charset == null) {
            charset = Consts.UTF_8;
        }
        return urlencode(str, charset, URLENCODER, true);
    }

    public static String format(Iterable<? extends NameValuePair> iterable, Charset charset) {
        StringBuilder stringBuilder = new StringBuilder();
        for (NameValuePair nameValuePair : iterable) {
            String encodeFormFields = encodeFormFields(nameValuePair.getName(), charset);
            String encodeFormFields2 = encodeFormFields(nameValuePair.getValue(), charset);
            if (stringBuilder.length() > 0) {
                stringBuilder.append(PARAMETER_SEPARATOR);
            }
            stringBuilder.append(encodeFormFields);
            if (encodeFormFields2 != null) {
                stringBuilder.append(NAME_VALUE_SEPARATOR);
                stringBuilder.append(encodeFormFields2);
            }
        }
        return stringBuilder.toString();
    }

    public static String format(List<? extends NameValuePair> list, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (NameValuePair nameValuePair : list) {
            String encodeFormFields = encodeFormFields(nameValuePair.getName(), str);
            String encodeFormFields2 = encodeFormFields(nameValuePair.getValue(), str);
            if (stringBuilder.length() > 0) {
                stringBuilder.append(PARAMETER_SEPARATOR);
            }
            stringBuilder.append(encodeFormFields);
            if (encodeFormFields2 != null) {
                stringBuilder.append(NAME_VALUE_SEPARATOR);
                stringBuilder.append(encodeFormFields2);
            }
        }
        return stringBuilder.toString();
    }

    public static boolean isEncoded(HttpEntity httpEntity) {
        Header contentType = httpEntity.getContentType();
        if (contentType == null) {
            return false;
        }
        HeaderElement[] elements = contentType.getElements();
        return elements.length > 0 ? elements[0].getName().equalsIgnoreCase("application/x-www-form-urlencoded") : false;
    }

    public static List<NameValuePair> parse(String str, Charset charset) {
        if (str == null) {
            return Collections.emptyList();
        }
        BasicHeaderValueParser basicHeaderValueParser = BasicHeaderValueParser.DEFAULT;
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(str.length());
        charArrayBuffer.append(str);
        ParserCursor parserCursor = new ParserCursor(0, charArrayBuffer.length());
        List<NameValuePair> arrayList = new ArrayList();
        while (!parserCursor.atEnd()) {
            NameValuePair parseNameValuePair = basicHeaderValueParser.parseNameValuePair(charArrayBuffer, parserCursor, DELIM);
            if (parseNameValuePair.getName().length() > 0) {
                arrayList.add(new BasicNameValuePair(decodeFormFields(parseNameValuePair.getName(), charset), decodeFormFields(parseNameValuePair.getValue(), charset)));
            }
        }
        return arrayList;
    }

    public static List<NameValuePair> parse(URI uri, String str) {
        String rawQuery = uri.getRawQuery();
        if (rawQuery == null || rawQuery.length() <= 0) {
            return Collections.emptyList();
        }
        List<NameValuePair> arrayList = new ArrayList();
        parse(arrayList, new Scanner(rawQuery), str);
        return arrayList;
    }

    public static List<NameValuePair> parse(HttpEntity httpEntity) throws IOException {
        ContentType contentType = ContentType.get(httpEntity);
        if (contentType != null && contentType.getMimeType().equalsIgnoreCase("application/x-www-form-urlencoded")) {
            String entityUtils = EntityUtils.toString(httpEntity, Consts.ASCII);
            if (entityUtils != null && entityUtils.length() > 0) {
                Charset charset = contentType.getCharset();
                if (charset == null) {
                    charset = HTTP.DEF_CONTENT_CHARSET;
                }
                return parse(entityUtils, charset);
            }
        }
        return Collections.emptyList();
    }

    public static void parse(List<NameValuePair> list, Scanner scanner, String str) {
        scanner.useDelimiter(PARAMETER_SEPARATOR);
        while (scanner.hasNext()) {
            String decodeFormFields;
            String str2 = null;
            String next = scanner.next();
            int indexOf = next.indexOf(NAME_VALUE_SEPARATOR);
            if (indexOf != -1) {
                decodeFormFields = decodeFormFields(next.substring(0, indexOf).trim(), str);
                str2 = decodeFormFields(next.substring(indexOf + 1).trim(), str);
            } else {
                decodeFormFields = decodeFormFields(next.trim(), str);
            }
            list.add(new BasicNameValuePair(decodeFormFields, str2));
        }
    }

    private static String urldecode(String str, Charset charset, boolean z) {
        if (str == null) {
            return null;
        }
        ByteBuffer allocate = ByteBuffer.allocate(str.length());
        CharBuffer wrap = CharBuffer.wrap(str);
        while (wrap.hasRemaining()) {
            char c = wrap.get();
            if (c == '%' && wrap.remaining() >= 2) {
                c = wrap.get();
                char c2 = wrap.get();
                int digit = Character.digit(c, 16);
                int digit2 = Character.digit(c2, 16);
                if (digit == -1 || digit2 == -1) {
                    allocate.put((byte) 37);
                    allocate.put((byte) c);
                    allocate.put((byte) c2);
                } else {
                    allocate.put((byte) ((digit << 4) + digit2));
                }
            } else if (z && c == '+') {
                allocate.put((byte) 32);
            } else {
                allocate.put((byte) c);
            }
        }
        allocate.flip();
        return charset.decode(allocate).toString();
    }

    private static String urlencode(String str, Charset charset, BitSet bitSet, boolean z) {
        if (str == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        ByteBuffer encode = charset.encode(str);
        while (encode.hasRemaining()) {
            int i = encode.get() & 255;
            if (bitSet.get(i)) {
                stringBuilder.append((char) i);
            } else if (z && i == 32) {
                stringBuilder.append('+');
            } else {
                stringBuilder.append("%");
                char toUpperCase = Character.toUpperCase(Character.forDigit((i >> 4) & 15, 16));
                char toUpperCase2 = Character.toUpperCase(Character.forDigit(i & 15, 16));
                stringBuilder.append(toUpperCase);
                stringBuilder.append(toUpperCase2);
            }
        }
        return stringBuilder.toString();
    }
}
