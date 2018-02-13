package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class NetscapeDraftSpec extends CookieSpecBase {
    protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yy HH:mm:ss z";
    private final String[] datepatterns;

    public NetscapeDraftSpec() {
        this(null);
    }

    public NetscapeDraftSpec(String[] strArr) {
        if (strArr != null) {
            this.datepatterns = (String[]) strArr.clone();
        } else {
            this.datepatterns = new String[]{EXPIRES_PATTERN};
        }
        registerAttribHandler(ClientCookie.PATH_ATTR, new BasicPathHandler());
        registerAttribHandler(ClientCookie.DOMAIN_ATTR, new NetscapeDomainHandler());
        registerAttribHandler("max-age", new BasicMaxAgeHandler());
        registerAttribHandler(ClientCookie.SECURE_ATTR, new BasicSecureHandler());
        registerAttribHandler(ClientCookie.COMMENT_ATTR, new BasicCommentHandler());
        registerAttribHandler(ClientCookie.EXPIRES_ATTR, new BasicExpiresHandler(this.datepatterns));
    }

    public List<Header> formatCookies(List<Cookie> list) {
        if (list == null) {
            throw new IllegalArgumentException("List of cookies may not be null");
        } else if (list.isEmpty()) {
            throw new IllegalArgumentException("List of cookies may not be empty");
        } else {
            CharArrayBuffer charArrayBuffer = new CharArrayBuffer(list.size() * 20);
            charArrayBuffer.append("Cookie");
            charArrayBuffer.append(": ");
            for (int i = 0; i < list.size(); i++) {
                Cookie cookie = (Cookie) list.get(i);
                if (i > 0) {
                    charArrayBuffer.append("; ");
                }
                charArrayBuffer.append(cookie.getName());
                String value = cookie.getValue();
                if (value != null) {
                    charArrayBuffer.append("=");
                    charArrayBuffer.append(value);
                }
            }
            List<Header> arrayList = new ArrayList(1);
            arrayList.add(new BufferedHeader(charArrayBuffer));
            return arrayList;
        }
    }

    public int getVersion() {
        return 0;
    }

    public Header getVersionHeader() {
        return null;
    }

    public List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException {
        if (header == null) {
            throw new IllegalArgumentException("Header may not be null");
        } else if (cookieOrigin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else if (header.getName().equalsIgnoreCase("Set-Cookie")) {
            CharArrayBuffer buffer;
            ParserCursor parserCursor;
            NetscapeDraftHeaderParser netscapeDraftHeaderParser = NetscapeDraftHeaderParser.DEFAULT;
            if (header instanceof FormattedHeader) {
                buffer = ((FormattedHeader) header).getBuffer();
                parserCursor = new ParserCursor(((FormattedHeader) header).getValuePos(), buffer.length());
            } else {
                String value = header.getValue();
                if (value == null) {
                    throw new MalformedCookieException("Header value is null");
                }
                buffer = new CharArrayBuffer(value.length());
                buffer.append(value);
                parserCursor = new ParserCursor(0, buffer.length());
            }
            return parse(new HeaderElement[]{netscapeDraftHeaderParser.parseHeader(buffer, parserCursor)}, cookieOrigin);
        } else {
            throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
        }
    }

    public String toString() {
        return CookiePolicy.NETSCAPE;
    }
}
