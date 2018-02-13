package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;

@NotThreadSafe
public abstract class CookieSpecBase extends AbstractCookieSpec {
    protected static String getDefaultDomain(CookieOrigin cookieOrigin) {
        return cookieOrigin.getHost();
    }

    protected static String getDefaultPath(CookieOrigin cookieOrigin) {
        String path = cookieOrigin.getPath();
        int lastIndexOf = path.lastIndexOf(47);
        if (lastIndexOf < 0) {
            return path;
        }
        if (lastIndexOf == 0) {
            lastIndexOf = 1;
        }
        return path.substring(0, lastIndexOf);
    }

    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (cookieOrigin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else {
            for (CookieAttributeHandler match : getAttribHandlers()) {
                if (!match.match(cookie, cookieOrigin)) {
                    return false;
                }
            }
            return true;
        }
    }

    protected List<Cookie> parse(HeaderElement[] headerElementArr, CookieOrigin cookieOrigin) throws MalformedCookieException {
        List<Cookie> arrayList = new ArrayList(headerElementArr.length);
        for (HeaderElement headerElement : headerElementArr) {
            String name = headerElement.getName();
            String value = headerElement.getValue();
            if (name == null || name.length() == 0) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            SetCookie basicClientCookie = new BasicClientCookie(name, value);
            basicClientCookie.setPath(getDefaultPath(cookieOrigin));
            basicClientCookie.setDomain(getDefaultDomain(cookieOrigin));
            NameValuePair[] parameters = headerElement.getParameters();
            for (int length = parameters.length - 1; length >= 0; length--) {
                NameValuePair nameValuePair = parameters[length];
                String toLowerCase = nameValuePair.getName().toLowerCase(Locale.ENGLISH);
                basicClientCookie.setAttribute(toLowerCase, nameValuePair.getValue());
                CookieAttributeHandler findAttribHandler = findAttribHandler(toLowerCase);
                if (findAttribHandler != null) {
                    findAttribHandler.parse(basicClientCookie, nameValuePair.getValue());
                }
            }
            arrayList.add(basicClientCookie);
        }
        return arrayList;
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (cookieOrigin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else {
            for (CookieAttributeHandler validate : getAttribHandlers()) {
                validate.validate(cookie, cookieOrigin);
            }
        }
    }
}
