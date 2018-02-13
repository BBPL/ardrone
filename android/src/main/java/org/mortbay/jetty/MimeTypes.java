package org.mortbay.jetty;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import org.mortbay.io.Buffer;
import org.mortbay.io.BufferCache;
import org.mortbay.io.BufferCache.CachedBuffer;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.log.Log;
import org.mortbay.util.StringUtil;

public class MimeTypes {
    public static final BufferCache CACHE = new BufferCache();
    public static final String FORM_ENCODED = "application/x-www-form-urlencoded";
    public static final CachedBuffer FORM_ENCODED_BUFFER = CACHE.add("application/x-www-form-urlencoded", 1);
    private static final int FORM_ENCODED_ORDINAL = 1;
    public static final String MESSAGE_HTTP = "message/http";
    public static final CachedBuffer MESSAGE_HTTP_BUFFER = CACHE.add(MESSAGE_HTTP, 2);
    private static final int MESSAGE_HTTP_ORDINAL = 2;
    public static final String MULTIPART_BYTERANGES = "multipart/byteranges";
    public static final CachedBuffer MULTIPART_BYTERANGES_BUFFER = CACHE.add(MULTIPART_BYTERANGES, 3);
    private static final int MULTIPART_BYTERANGES_ORDINAL = 3;
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_HTML_8859_1 = "text/html; charset=iso-8859-1";
    public static final CachedBuffer TEXT_HTML_8859_1_BUFFER = new CachedBuffer(TEXT_HTML_8859_1, 7);
    private static final int TEXT_HTML_8859_1_ORDINAL = 7;
    public static final CachedBuffer TEXT_HTML_BUFFER = CACHE.add(TEXT_HTML, 4);
    private static final int TEXT_HTML_ORDINAL = 4;
    public static final String TEXT_HTML_UTF_8 = "text/html; charset=utf-8";
    public static final CachedBuffer TEXT_HTML_UTF_8_BUFFER = new CachedBuffer(TEXT_HTML_UTF_8, 10);
    private static final int TEXT_HTML_UTF_8_ORDINAL = 10;
    public static final String TEXT_JSON = "text/json";
    public static final CachedBuffer TEXT_JSON_BUFFER = CACHE.add(TEXT_JSON, 13);
    private static final int TEXT_JSON_ORDINAL = 13;
    public static final String TEXT_JSON_UTF_8 = "text/json;charset=UTF-8";
    public static final CachedBuffer TEXT_JSON_UTF_8_BUFFER = CACHE.add(TEXT_JSON_UTF_8, 14);
    private static final int TEXT_JSON_UTF_8_ORDINAL = 14;
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_PLAIN_8859_1 = "text/plain; charset=iso-8859-1";
    public static final CachedBuffer TEXT_PLAIN_8859_1_BUFFER = new CachedBuffer(TEXT_PLAIN_8859_1, 8);
    private static final int TEXT_PLAIN_8859_1_ORDINAL = 8;
    public static final CachedBuffer TEXT_PLAIN_BUFFER = CACHE.add("text/plain", 5);
    private static final int TEXT_PLAIN_ORDINAL = 5;
    public static final String TEXT_PLAIN_UTF_8 = "text/plain; charset=utf-8";
    public static final CachedBuffer TEXT_PLAIN_UTF_8_BUFFER = new CachedBuffer(TEXT_PLAIN_UTF_8, 11);
    private static final int TEXT_PLAIN_UTF_8_ORDINAL = 11;
    public static final String TEXT_XML = "text/xml";
    public static final String TEXT_XML_8859_1 = "text/xml; charset=iso-8859-1";
    public static final CachedBuffer TEXT_XML_8859_1_BUFFER = new CachedBuffer(TEXT_XML_8859_1, 9);
    private static final int TEXT_XML_8859_1_ORDINAL = 9;
    public static final CachedBuffer TEXT_XML_BUFFER = CACHE.add(TEXT_XML, 6);
    private static final int TEXT_XML_ORDINAL = 6;
    public static final String TEXT_XML_UTF_8 = "text/xml; charset=utf-8";
    public static final CachedBuffer TEXT_XML_UTF_8_BUFFER = new CachedBuffer(TEXT_XML_UTF_8, 12);
    private static final int TEXT_XML_UTF_8_ORDINAL = 12;
    private static final Map __dftMimeMap = new HashMap();
    private static final Map __encodings = new HashMap();
    private static int __index = 15;
    private Map _mimeMap;

    static {
        ResourceBundle bundle;
        Enumeration keys;
        try {
            bundle = ResourceBundle.getBundle("org/mortbay/jetty/mime");
            keys = bundle.getKeys();
            while (keys.hasMoreElements()) {
                String str = (String) keys.nextElement();
                __dftMimeMap.put(StringUtil.asciiToLowerCase(str), normalizeMimeType(bundle.getString(str)));
            }
        } catch (Throwable e) {
            Log.warn(e.toString());
            Log.debug(e);
        }
        try {
            bundle = ResourceBundle.getBundle("org/mortbay/jetty/encoding");
            keys = bundle.getKeys();
            while (keys.hasMoreElements()) {
                Buffer normalizeMimeType = normalizeMimeType((String) keys.nextElement());
                __encodings.put(normalizeMimeType, bundle.getString(normalizeMimeType.toString()));
            }
        } catch (Throwable e2) {
            Log.warn(e2.toString());
            Log.debug(e2);
        }
        TEXT_HTML_BUFFER.setAssociate("ISO-8859-1", TEXT_HTML_8859_1_BUFFER);
        TEXT_HTML_BUFFER.setAssociate("ISO_8859_1", TEXT_HTML_8859_1_BUFFER);
        TEXT_HTML_BUFFER.setAssociate("iso-8859-1", TEXT_HTML_8859_1_BUFFER);
        TEXT_PLAIN_BUFFER.setAssociate("ISO-8859-1", TEXT_PLAIN_8859_1_BUFFER);
        TEXT_PLAIN_BUFFER.setAssociate("ISO_8859_1", TEXT_PLAIN_8859_1_BUFFER);
        TEXT_PLAIN_BUFFER.setAssociate("iso-8859-1", TEXT_PLAIN_8859_1_BUFFER);
        TEXT_XML_BUFFER.setAssociate("ISO-8859-1", TEXT_XML_8859_1_BUFFER);
        TEXT_XML_BUFFER.setAssociate("ISO_8859_1", TEXT_XML_8859_1_BUFFER);
        TEXT_XML_BUFFER.setAssociate("iso-8859-1", TEXT_XML_8859_1_BUFFER);
        TEXT_HTML_BUFFER.setAssociate("UTF-8", TEXT_HTML_UTF_8_BUFFER);
        TEXT_HTML_BUFFER.setAssociate(StringUtil.__UTF8Alt, TEXT_HTML_UTF_8_BUFFER);
        TEXT_HTML_BUFFER.setAssociate("utf8", TEXT_HTML_UTF_8_BUFFER);
        TEXT_HTML_BUFFER.setAssociate("utf-8", TEXT_HTML_UTF_8_BUFFER);
        TEXT_PLAIN_BUFFER.setAssociate("UTF-8", TEXT_PLAIN_UTF_8_BUFFER);
        TEXT_PLAIN_BUFFER.setAssociate(StringUtil.__UTF8Alt, TEXT_PLAIN_UTF_8_BUFFER);
        TEXT_PLAIN_BUFFER.setAssociate("utf-8", TEXT_PLAIN_UTF_8_BUFFER);
        TEXT_XML_BUFFER.setAssociate("UTF-8", TEXT_XML_UTF_8_BUFFER);
        TEXT_XML_BUFFER.setAssociate("utf8", TEXT_XML_UTF_8_BUFFER);
        TEXT_XML_BUFFER.setAssociate(StringUtil.__UTF8Alt, TEXT_XML_UTF_8_BUFFER);
        TEXT_XML_BUFFER.setAssociate("utf-8", TEXT_XML_UTF_8_BUFFER);
        TEXT_JSON_BUFFER.setAssociate("UTF-8", TEXT_JSON_UTF_8_BUFFER);
        TEXT_JSON_BUFFER.setAssociate("utf8", TEXT_JSON_UTF_8_BUFFER);
        TEXT_JSON_BUFFER.setAssociate(StringUtil.__UTF8Alt, TEXT_JSON_UTF_8_BUFFER);
        TEXT_JSON_BUFFER.setAssociate("utf-8", TEXT_JSON_UTF_8_BUFFER);
    }

    public static String getCharsetFromContentType(Buffer buffer) {
        if (buffer instanceof CachedBuffer) {
            switch (((CachedBuffer) buffer).getOrdinal()) {
                case 7:
                case 8:
                case 9:
                    return StringUtil.__ISO_8859_1;
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    return "UTF-8";
            }
        }
        int index = buffer.getIndex();
        int putIndex = buffer.putIndex();
        Object obj = null;
        Object obj2 = null;
        int i = 0;
        while (index < putIndex) {
            byte peek = buffer.peek(index);
            if (obj2 == null || obj == 10) {
                int i2;
                int i3;
                switch (obj) {
                    case null:
                        if ((byte) 34 != peek) {
                            if (HttpTokens.SEMI_COLON != peek) {
                                break;
                            }
                            i2 = 1;
                            break;
                        }
                        i3 = 1;
                        break;
                    case 1:
                        if ((byte) 99 != peek) {
                            if ((byte) 32 == peek) {
                                break;
                            }
                            obj = null;
                            break;
                        }
                        obj = 2;
                        break;
                    case 2:
                        if ((byte) 104 != peek) {
                            obj = null;
                            break;
                        }
                        obj = 3;
                        break;
                    case 3:
                        if ((byte) 97 != peek) {
                            obj = null;
                            break;
                        }
                        obj = 4;
                        break;
                    case 4:
                        if ((byte) 114 != peek) {
                            obj = null;
                            break;
                        }
                        obj = 5;
                        break;
                    case 5:
                        if ((byte) 115 != peek) {
                            obj = null;
                            break;
                        }
                        obj = 6;
                        break;
                    case 6:
                        if ((byte) 101 != peek) {
                            obj = null;
                            break;
                        }
                        obj = 7;
                        break;
                    case 7:
                        if ((byte) 116 != peek) {
                            obj = null;
                            break;
                        }
                        obj = 8;
                        break;
                    case 8:
                        if ((byte) 61 != peek) {
                            if ((byte) 32 == peek) {
                                break;
                            }
                            obj = null;
                            break;
                        }
                        obj = 9;
                        break;
                    case 9:
                        if ((byte) 32 != peek) {
                            if ((byte) 34 != peek) {
                                i = index;
                                i2 = 10;
                                break;
                            }
                            i = index + 1;
                            i3 = 1;
                            i2 = 10;
                            break;
                        }
                        break;
                    case 10:
                        if ((obj2 == null && (HttpTokens.SEMI_COLON == peek || (byte) 32 == peek)) || (obj2 != null && (byte) 34 == peek)) {
                            return CACHE.lookup(buffer.peek(i, index - i)).toString();
                        }
                    default:
                        break;
                }
            } else if ((byte) 34 == peek) {
                obj2 = null;
            }
            index++;
        }
        return obj == 10 ? CACHE.lookup(buffer.peek(i, index - i)).toString() : null;
    }

    private static Buffer normalizeMimeType(String str) {
        Buffer buffer;
        synchronized (MimeTypes.class) {
            try {
                buffer = CACHE.get(str);
                if (buffer == null) {
                    BufferCache bufferCache = CACHE;
                    int i = __index;
                    __index = i + 1;
                    buffer = bufferCache.add(str, i);
                }
            } catch (Throwable th) {
                Class cls = MimeTypes.class;
            }
        }
        return buffer;
    }

    public void addMimeMapping(String str, String str2) {
        if (this._mimeMap == null) {
            this._mimeMap = new HashMap();
        }
        this._mimeMap.put(StringUtil.asciiToLowerCase(str), normalizeMimeType(str2));
    }

    public Buffer getMimeByExtension(String str) {
        Buffer buffer = null;
        if (str != null) {
            int i = -1;
            while (buffer == null) {
                i = str.indexOf(".", i + 1);
                if (i < 0 || i >= str.length()) {
                    break;
                }
                String asciiToLowerCase = StringUtil.asciiToLowerCase(str.substring(i + 1));
                if (this._mimeMap != null) {
                    buffer = (Buffer) this._mimeMap.get(asciiToLowerCase);
                }
                if (buffer == null) {
                    buffer = (Buffer) __dftMimeMap.get(asciiToLowerCase);
                }
            }
        }
        if (buffer != null) {
            return buffer;
        }
        if (this._mimeMap != null) {
            buffer = (Buffer) this._mimeMap.get(Constraint.ANY_ROLE);
        }
        return buffer == null ? (Buffer) __dftMimeMap.get(Constraint.ANY_ROLE) : buffer;
    }

    public Map getMimeMap() {
        Map map;
        synchronized (this) {
            map = this._mimeMap;
        }
        return map;
    }

    public void setMimeMap(Map map) {
        if (map == null) {
            this._mimeMap = null;
            return;
        }
        Map hashMap = new HashMap();
        for (Entry entry : map.entrySet()) {
            hashMap.put(entry.getKey(), normalizeMimeType(entry.getValue().toString()));
        }
        this._mimeMap = hashMap;
    }
}
