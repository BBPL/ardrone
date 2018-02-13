package org.apache.http.cookie;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.http.annotation.Immutable;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.util.URIUtil;

@Immutable
public class CookieIdentityComparator implements Serializable, Comparator<Cookie> {
    private static final long serialVersionUID = 4466565437490631532L;

    public int compare(Cookie cookie, Cookie cookie2) {
        String domain;
        String domain2;
        int compareTo = cookie.getName().compareTo(cookie2.getName());
        if (compareTo == 0) {
            domain = cookie.getDomain();
            if (domain == null) {
                domain = HttpVersions.HTTP_0_9;
            } else if (domain.indexOf(46) == -1) {
                domain = domain + ".local";
            }
            domain2 = cookie2.getDomain();
            if (domain2 == null) {
                domain2 = HttpVersions.HTTP_0_9;
            } else if (domain2.indexOf(46) == -1) {
                domain2 = domain2 + ".local";
            }
            compareTo = domain.compareToIgnoreCase(domain2);
        }
        if (compareTo != 0) {
            return compareTo;
        }
        domain = cookie.getPath();
        if (domain == null) {
            domain = URIUtil.SLASH;
        }
        domain2 = cookie2.getPath();
        if (domain2 == null) {
            domain2 = URIUtil.SLASH;
        }
        return domain.compareTo(domain2);
    }
}
