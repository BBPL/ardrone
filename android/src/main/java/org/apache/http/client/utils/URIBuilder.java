package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;

@NotThreadSafe
public class URIBuilder {
    private String encodedAuthority;
    private String encodedFragment;
    private String encodedPath;
    private String encodedQuery;
    private String encodedSchemeSpecificPart;
    private String encodedUserInfo;
    private String fragment;
    private String host;
    private String path;
    private int port;
    private List<NameValuePair> queryParams;
    private String scheme;
    private String userInfo;

    public URIBuilder() {
        this.port = -1;
    }

    public URIBuilder(String str) throws URISyntaxException {
        digestURI(new URI(str));
    }

    public URIBuilder(URI uri) {
        digestURI(uri);
    }

    private String buildString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.scheme != null) {
            stringBuilder.append(this.scheme).append(':');
        }
        if (this.encodedSchemeSpecificPart != null) {
            stringBuilder.append(this.encodedSchemeSpecificPart);
        } else {
            if (this.encodedAuthority != null) {
                stringBuilder.append("//").append(this.encodedAuthority);
            } else if (this.host != null) {
                stringBuilder.append("//");
                if (this.encodedUserInfo != null) {
                    stringBuilder.append(this.encodedUserInfo).append("@");
                } else if (this.userInfo != null) {
                    stringBuilder.append(encodeUserInfo(this.userInfo)).append("@");
                }
                if (InetAddressUtils.isIPv6Address(this.host)) {
                    stringBuilder.append("[").append(this.host).append("]");
                } else {
                    stringBuilder.append(this.host);
                }
                if (this.port >= 0) {
                    stringBuilder.append(":").append(this.port);
                }
            }
            if (this.encodedPath != null) {
                stringBuilder.append(normalizePath(this.encodedPath));
            } else if (this.path != null) {
                stringBuilder.append(encodePath(normalizePath(this.path)));
            }
            if (this.encodedQuery != null) {
                stringBuilder.append("?").append(this.encodedQuery);
            } else if (this.queryParams != null) {
                stringBuilder.append("?").append(encodeQuery(this.queryParams));
            }
        }
        if (this.encodedFragment != null) {
            stringBuilder.append("#").append(this.encodedFragment);
        } else if (this.fragment != null) {
            stringBuilder.append("#").append(encodeFragment(this.fragment));
        }
        return stringBuilder.toString();
    }

    private void digestURI(URI uri) {
        this.scheme = uri.getScheme();
        this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
        this.encodedAuthority = uri.getRawAuthority();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.encodedUserInfo = uri.getRawUserInfo();
        this.userInfo = uri.getUserInfo();
        this.encodedPath = uri.getRawPath();
        this.path = uri.getPath();
        this.encodedQuery = uri.getRawQuery();
        this.queryParams = parseQuery(uri.getRawQuery(), Consts.UTF_8);
        this.encodedFragment = uri.getRawFragment();
        this.fragment = uri.getFragment();
    }

    private String encodeFragment(String str) {
        return URLEncodedUtils.encFragment(str, Consts.UTF_8);
    }

    private String encodePath(String str) {
        return URLEncodedUtils.encPath(str, Consts.UTF_8);
    }

    private String encodeQuery(List<NameValuePair> list) {
        return URLEncodedUtils.format((Iterable) list, Consts.UTF_8);
    }

    private String encodeUserInfo(String str) {
        return URLEncodedUtils.encUserInfo(str, Consts.UTF_8);
    }

    private static String normalizePath(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        while (i < str.length() && str.charAt(i) == '/') {
            i++;
        }
        return i > 1 ? str.substring(i - 1) : str;
    }

    private List<NameValuePair> parseQuery(String str, Charset charset) {
        return (str == null || str.length() <= 0) ? null : URLEncodedUtils.parse(str, charset);
    }

    public URIBuilder addParameter(String str, String str2) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList();
        }
        this.queryParams.add(new BasicNameValuePair(str, str2));
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    public URI build() throws URISyntaxException {
        return new URI(buildString());
    }

    public String getFragment() {
        return this.fragment;
    }

    public String getHost() {
        return this.host;
    }

    public String getPath() {
        return this.path;
    }

    public int getPort() {
        return this.port;
    }

    public List<NameValuePair> getQueryParams() {
        return this.queryParams != null ? new ArrayList(this.queryParams) : new ArrayList();
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getUserInfo() {
        return this.userInfo;
    }

    public URIBuilder removeQuery() {
        this.queryParams = null;
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    public URIBuilder setFragment(String str) {
        this.fragment = str;
        this.encodedFragment = null;
        return this;
    }

    public URIBuilder setHost(String str) {
        this.host = str;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    public URIBuilder setParameter(String str, String str2) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList();
        }
        if (!this.queryParams.isEmpty()) {
            Iterator it = this.queryParams.iterator();
            while (it.hasNext()) {
                if (((NameValuePair) it.next()).getName().equals(str)) {
                    it.remove();
                }
            }
        }
        this.queryParams.add(new BasicNameValuePair(str, str2));
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    public URIBuilder setPath(String str) {
        this.path = str;
        this.encodedSchemeSpecificPart = null;
        this.encodedPath = null;
        return this;
    }

    public URIBuilder setPort(int i) {
        if (i < 0) {
            i = -1;
        }
        this.port = i;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    public URIBuilder setQuery(String str) {
        this.queryParams = parseQuery(str, Consts.UTF_8);
        this.encodedQuery = null;
        this.encodedSchemeSpecificPart = null;
        return this;
    }

    public URIBuilder setScheme(String str) {
        this.scheme = str;
        return this;
    }

    public URIBuilder setUserInfo(String str) {
        this.userInfo = str;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        this.encodedUserInfo = null;
        return this;
    }

    public URIBuilder setUserInfo(String str, String str2) {
        return setUserInfo(str + ':' + str2);
    }

    public String toString() {
        return buildString();
    }
}
