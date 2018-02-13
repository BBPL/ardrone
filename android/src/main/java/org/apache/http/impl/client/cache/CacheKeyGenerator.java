package org.apache.http.impl.client.cache;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HttpCacheEntry;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.util.URIUtil;

@Immutable
class CacheKeyGenerator {
    CacheKeyGenerator() {
    }

    private String canonicalizePath(String str) {
        try {
            str = new URI(URLDecoder.decode(str, "UTF-8")).getPath();
        } catch (UnsupportedEncodingException e) {
        } catch (URISyntaxException e2) {
        }
        return str;
    }

    private int canonicalizePort(int i, String str) {
        return (i == -1 && "http".equalsIgnoreCase(str)) ? 80 : (i == -1 && "https".equalsIgnoreCase(str)) ? 443 : i;
    }

    private boolean isRelativeRequest(HttpRequest httpRequest) {
        String uri = httpRequest.getRequestLine().getUri();
        return Constraint.ANY_ROLE.equals(uri) || uri.startsWith(URIUtil.SLASH);
    }

    public String canonicalizeUri(String str) {
        try {
            URL url = new URL(str);
            String toLowerCase = url.getProtocol().toLowerCase();
            String toLowerCase2 = url.getHost().toLowerCase();
            int canonicalizePort = canonicalizePort(url.getPort(), toLowerCase);
            String canonicalizePath = canonicalizePath(url.getPath());
            if (HttpVersions.HTTP_0_9.equals(canonicalizePath)) {
                canonicalizePath = URIUtil.SLASH;
            }
            String query = url.getQuery();
            if (query != null) {
                canonicalizePath = canonicalizePath + "?" + query;
            }
            str = new URL(toLowerCase, toLowerCase2, canonicalizePort, canonicalizePath).toString();
        } catch (MalformedURLException e) {
        }
        return str;
    }

    protected String getFullHeaderValue(Header[] headerArr) {
        if (headerArr == null) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder(HttpVersions.HTTP_0_9);
        Object obj = 1;
        int length = headerArr.length;
        int i = 0;
        while (i < length) {
            Header header = headerArr[i];
            if (obj == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(header.getValue().trim());
            i++;
            obj = null;
        }
        return stringBuilder.toString();
    }

    public String getURI(HttpHost httpHost, HttpRequest httpRequest) {
        if (!isRelativeRequest(httpRequest)) {
            return canonicalizeUri(httpRequest.getRequestLine().getUri());
        }
        return canonicalizeUri(String.format("%s%s", new Object[]{httpHost.toString(), httpRequest.getRequestLine().getUri()}));
    }

    public String getVariantKey(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry) {
        List<String> arrayList = new ArrayList();
        for (Header elements : httpCacheEntry.getHeaders("Vary")) {
            for (HeaderElement name : elements.getElements()) {
                arrayList.add(name.getName());
            }
        }
        Collections.sort(arrayList);
        try {
            StringBuilder stringBuilder = new StringBuilder("{");
            Object obj = 1;
            for (String str : arrayList) {
                if (obj == null) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(URLEncoder.encode(str, Consts.UTF_8.name()));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(getFullHeaderValue(httpRequest.getHeaders(str)), Consts.UTF_8.name()));
                obj = null;
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        } catch (Throwable e) {
            throw new RuntimeException("couldn't encode to UTF-8", e);
        }
    }

    public String getVariantURI(HttpHost httpHost, HttpRequest httpRequest, HttpCacheEntry httpCacheEntry) {
        return !httpCacheEntry.hasVariants() ? getURI(httpHost, httpRequest) : getVariantKey(httpRequest, httpCacheEntry) + getURI(httpHost, httpRequest);
    }
}
