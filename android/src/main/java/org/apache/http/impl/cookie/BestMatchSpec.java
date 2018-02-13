package org.apache.http.impl.cookie;

import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class BestMatchSpec implements CookieSpec {
    private BrowserCompatSpec compat;
    private final String[] datepatterns;
    private RFC2109Spec obsoleteStrict;
    private final boolean oneHeader;
    private RFC2965Spec strict;

    public BestMatchSpec() {
        this(null, false);
    }

    public BestMatchSpec(String[] strArr, boolean z) {
        this.datepatterns = strArr == null ? null : (String[]) strArr.clone();
        this.oneHeader = z;
    }

    private BrowserCompatSpec getCompat() {
        if (this.compat == null) {
            this.compat = new BrowserCompatSpec(this.datepatterns);
        }
        return this.compat;
    }

    private RFC2109Spec getObsoleteStrict() {
        if (this.obsoleteStrict == null) {
            this.obsoleteStrict = new RFC2109Spec(this.datepatterns, this.oneHeader);
        }
        return this.obsoleteStrict;
    }

    private RFC2965Spec getStrict() {
        if (this.strict == null) {
            this.strict = new RFC2965Spec(this.datepatterns, this.oneHeader);
        }
        return this.strict;
    }

    public List<Header> formatCookies(List<Cookie> list) {
        if (list == null) {
            throw new IllegalArgumentException("List of cookies may not be null");
        }
        int i = Integer.MAX_VALUE;
        Object obj = 1;
        for (Cookie cookie : list) {
            if (!(cookie instanceof SetCookie2)) {
                obj = null;
            }
            if (cookie.getVersion() < i) {
                i = cookie.getVersion();
            }
        }
        return i > 0 ? obj != null ? getStrict().formatCookies(list) : getObsoleteStrict().formatCookies(list) : getCompat().formatCookies(list);
    }

    public int getVersion() {
        return getStrict().getVersion();
    }

    public Header getVersionHeader() {
        return getStrict().getVersionHeader();
    }

    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (cookieOrigin != null) {
            return cookie.getVersion() > 0 ? cookie instanceof SetCookie2 ? getStrict().match(cookie, cookieOrigin) : getObsoleteStrict().match(cookie, cookieOrigin) : getCompat().match(cookie, cookieOrigin);
        } else {
            throw new IllegalArgumentException("Cookie origin may not be null");
        }
    }

    public List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException {
        if (header == null) {
            throw new IllegalArgumentException("Header may not be null");
        } else if (cookieOrigin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else {
            HeaderElement[] elements = header.getElements();
            int i = 0;
            int i2 = 0;
            for (HeaderElement headerElement : elements) {
                if (headerElement.getParameterByName(ClientCookie.VERSION_ATTR) != null) {
                    i = 1;
                }
                if (headerElement.getParameterByName(ClientCookie.EXPIRES_ATTR) != null) {
                    i2 = 1;
                }
            }
            if (i2 == 0 && r0 != 0) {
                return "Set-Cookie2".equals(header.getName()) ? getStrict().parse(elements, cookieOrigin) : getObsoleteStrict().parse(elements, cookieOrigin);
            } else {
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
                HeaderElement parseHeader = netscapeDraftHeaderParser.parseHeader(buffer, parserCursor);
                return getCompat().parse(new HeaderElement[]{parseHeader}, cookieOrigin);
            }
        }
    }

    public String toString() {
        return CookiePolicy.BEST_MATCH;
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (cookieOrigin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else if (cookie.getVersion() <= 0) {
            getCompat().validate(cookie, cookieOrigin);
        } else if (cookie instanceof SetCookie2) {
            getStrict().validate(cookie, cookieOrigin);
        } else {
            getObsoleteStrict().validate(cookie, cookieOrigin);
        }
    }
}
