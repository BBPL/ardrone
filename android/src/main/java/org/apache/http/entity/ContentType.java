package org.apache.http.entity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.protocol.HTTP;
import org.mortbay.jetty.MimeTypes;

@Immutable
public final class ContentType implements Serializable {
    public static final ContentType APPLICATION_ATOM_XML = create("application/atom+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_JSON = create("application/json", Consts.UTF_8);
    public static final ContentType APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset) null);
    public static final ContentType APPLICATION_SVG_XML = create("application/svg+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_XHTML_XML = create("application/xhtml+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_XML = create("application/xml", Consts.ISO_8859_1);
    public static final ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
    public static final ContentType DEFAULT_TEXT = TEXT_PLAIN;
    public static final ContentType MULTIPART_FORM_DATA = create("multipart/form-data", Consts.ISO_8859_1);
    public static final ContentType TEXT_HTML = create(MimeTypes.TEXT_HTML, Consts.ISO_8859_1);
    public static final ContentType TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
    public static final ContentType TEXT_XML = create(MimeTypes.TEXT_XML, Consts.ISO_8859_1);
    public static final ContentType WILDCARD = create("*/*", (Charset) null);
    private static final long serialVersionUID = -7768694718232371896L;
    private final Charset charset;
    private final String mimeType;

    ContentType(String str, Charset charset) {
        this.mimeType = str;
        this.charset = charset;
    }

    public static ContentType create(String str) {
        return new ContentType(str, (Charset) null);
    }

    public static ContentType create(String str, String str2) throws UnsupportedCharsetException {
        return create(str, str2 != null ? Charset.forName(str2) : null);
    }

    public static ContentType create(String str, Charset charset) {
        if (str == null) {
            throw new IllegalArgumentException("MIME type may not be null");
        }
        String toLowerCase = str.trim().toLowerCase(Locale.US);
        if (toLowerCase.length() == 0) {
            throw new IllegalArgumentException("MIME type may not be empty");
        } else if (valid(toLowerCase)) {
            return new ContentType(toLowerCase, charset);
        } else {
            throw new IllegalArgumentException("MIME type may not contain reserved characters");
        }
    }

    private static ContentType create(HeaderElement headerElement) {
        String name = headerElement.getName();
        String str = null;
        NameValuePair parameterByName = headerElement.getParameterByName("charset");
        if (parameterByName != null) {
            str = parameterByName.getValue();
        }
        return create(name, str);
    }

    public static ContentType get(HttpEntity httpEntity) throws ParseException, UnsupportedCharsetException {
        if (httpEntity != null) {
            Header contentType = httpEntity.getContentType();
            if (contentType != null) {
                HeaderElement[] elements = contentType.getElements();
                if (elements.length > 0) {
                    return create(elements[0]);
                }
            }
        }
        return null;
    }

    public static ContentType getOrDefault(HttpEntity httpEntity) throws ParseException {
        ContentType contentType = get(httpEntity);
        return contentType != null ? contentType : DEFAULT_TEXT;
    }

    public static ContentType parse(String str) throws ParseException, UnsupportedCharsetException {
        if (str == null) {
            throw new IllegalArgumentException("Content type may not be null");
        }
        HeaderElement[] parseElements = BasicHeaderValueParser.parseElements(str, null);
        if (parseElements.length > 0) {
            return create(parseElements[0]);
        }
        throw new ParseException("Invalid content type: " + str);
    }

    private static boolean valid(String str) {
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '\"' || charAt == ',' || charAt == ';') {
                return false;
            }
        }
        return true;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mimeType);
        if (this.charset != null) {
            stringBuilder.append(HTTP.CHARSET_PARAM);
            stringBuilder.append(this.charset.name());
        }
        return stringBuilder.toString();
    }
}
