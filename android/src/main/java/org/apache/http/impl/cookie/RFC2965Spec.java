package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.cookie.SetCookie;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class RFC2965Spec extends RFC2109Spec {
    public RFC2965Spec() {
        this(null, false);
    }

    public RFC2965Spec(String[] strArr, boolean z) {
        super(strArr, z);
        registerAttribHandler(ClientCookie.DOMAIN_ATTR, new RFC2965DomainAttributeHandler());
        registerAttribHandler(ClientCookie.PORT_ATTR, new RFC2965PortAttributeHandler());
        registerAttribHandler(ClientCookie.COMMENTURL_ATTR, new RFC2965CommentUrlAttributeHandler());
        registerAttribHandler(ClientCookie.DISCARD_ATTR, new RFC2965DiscardAttributeHandler());
        registerAttribHandler(ClientCookie.VERSION_ATTR, new RFC2965VersionAttributeHandler());
    }

    private static CookieOrigin adjustEffectiveHost(CookieOrigin cookieOrigin) {
        Object obj = null;
        String host = cookieOrigin.getHost();
        for (int i = 0; i < host.length(); i++) {
            char charAt = host.charAt(i);
            if (charAt == '.' || charAt == ':') {
                break;
            }
        }
        int i2 = 1;
        return obj != null ? new CookieOrigin(host + ".local", cookieOrigin.getPort(), cookieOrigin.getPath(), cookieOrigin.isSecure()) : cookieOrigin;
    }

    private List<Cookie> createCookies(HeaderElement[] headerElementArr, CookieOrigin cookieOrigin) throws MalformedCookieException {
        List<Cookie> arrayList = new ArrayList(headerElementArr.length);
        for (HeaderElement headerElement : headerElementArr) {
            String name = headerElement.getName();
            String value = headerElement.getValue();
            if (name == null || name.length() == 0) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            SetCookie basicClientCookie2 = new BasicClientCookie2(name, value);
            basicClientCookie2.setPath(CookieSpecBase.getDefaultPath(cookieOrigin));
            basicClientCookie2.setDomain(CookieSpecBase.getDefaultDomain(cookieOrigin));
            basicClientCookie2.setPorts(new int[]{cookieOrigin.getPort()});
            NameValuePair[] parameters = headerElement.getParameters();
            Map hashMap = new HashMap(parameters.length);
            for (int length = parameters.length - 1; length >= 0; length--) {
                NameValuePair nameValuePair = parameters[length];
                hashMap.put(nameValuePair.getName().toLowerCase(Locale.ENGLISH), nameValuePair);
            }
            for (Entry value2 : hashMap.entrySet()) {
                NameValuePair nameValuePair2 = (NameValuePair) value2.getValue();
                value = nameValuePair2.getName().toLowerCase(Locale.ENGLISH);
                basicClientCookie2.setAttribute(value, nameValuePair2.getValue());
                CookieAttributeHandler findAttribHandler = findAttribHandler(value);
                if (findAttribHandler != null) {
                    findAttribHandler.parse(basicClientCookie2, nameValuePair2.getValue());
                }
            }
            arrayList.add(basicClientCookie2);
        }
        return arrayList;
    }

    protected void formatCookieAsVer(CharArrayBuffer charArrayBuffer, Cookie cookie, int i) {
        super.formatCookieAsVer(charArrayBuffer, cookie, i);
        if (cookie instanceof ClientCookie) {
            String attribute = ((ClientCookie) cookie).getAttribute(ClientCookie.PORT_ATTR);
            if (attribute != null) {
                charArrayBuffer.append("; $Port");
                charArrayBuffer.append("=\"");
                if (attribute.trim().length() > 0) {
                    int[] ports = cookie.getPorts();
                    if (ports != null) {
                        int length = ports.length;
                        for (int i2 = 0; i2 < length; i2++) {
                            if (i2 > 0) {
                                charArrayBuffer.append(",");
                            }
                            charArrayBuffer.append(Integer.toString(ports[i2]));
                        }
                    }
                }
                charArrayBuffer.append("\"");
            }
        }
    }

    public int getVersion() {
        return 1;
    }

    public Header getVersionHeader() {
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(40);
        charArrayBuffer.append(SM.COOKIE2);
        charArrayBuffer.append(": ");
        charArrayBuffer.append("$Version=");
        charArrayBuffer.append(Integer.toString(getVersion()));
        return new BufferedHeader(charArrayBuffer);
    }

    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (cookieOrigin != null) {
            return super.match(cookie, adjustEffectiveHost(cookieOrigin));
        } else {
            throw new IllegalArgumentException("Cookie origin may not be null");
        }
    }

    public List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException {
        if (header == null) {
            throw new IllegalArgumentException("Header may not be null");
        } else if (cookieOrigin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else if (header.getName().equalsIgnoreCase("Set-Cookie2")) {
            return createCookies(header.getElements(), adjustEffectiveHost(cookieOrigin));
        } else {
            throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
        }
    }

    protected List<Cookie> parse(HeaderElement[] headerElementArr, CookieOrigin cookieOrigin) throws MalformedCookieException {
        return createCookies(headerElementArr, adjustEffectiveHost(cookieOrigin));
    }

    public String toString() {
        return CookiePolicy.RFC_2965;
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (cookieOrigin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else {
            super.validate(cookie, adjustEffectiveHost(cookieOrigin));
        }
    }
}
