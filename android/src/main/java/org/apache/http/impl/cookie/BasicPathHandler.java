package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.mortbay.util.URIUtil;

@Immutable
public class BasicPathHandler implements CookieAttributeHandler {
    public boolean match(Cookie cookie, CookieOrigin cookieOrigin) {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        } else if (cookieOrigin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        } else {
            String path = cookieOrigin.getPath();
            String path2 = cookie.getPath();
            if (path2 == null) {
                path2 = URIUtil.SLASH;
            }
            if (path2.length() > 1 && path2.endsWith(URIUtil.SLASH)) {
                path2 = path2.substring(0, path2.length() - 1);
            }
            boolean startsWith = path.startsWith(path2);
            return (!startsWith || path.length() == path2.length() || path2.endsWith(URIUtil.SLASH)) ? startsWith : path.charAt(path2.length()) == '/';
        }
    }

    public void parse(SetCookie setCookie, String str) throws MalformedCookieException {
        if (setCookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        }
        if (str == null || str.trim().length() == 0) {
            str = URIUtil.SLASH;
        }
        setCookie.setPath(str);
    }

    public void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException {
        if (!match(cookie, cookieOrigin)) {
            throw new CookieRestrictionViolationException("Illegal path attribute \"" + cookie.getPath() + "\". Path of origin: \"" + cookieOrigin.getPath() + "\"");
        }
    }
}
