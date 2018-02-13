package org.apache.http.impl.client.cache;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.util.URIUtil;

@Immutable
class ResponseCachingPolicy {
    private static final Set<Integer> cacheableStatuses = new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(200), Integer.valueOf(203), Integer.valueOf(300), Integer.valueOf(301), Integer.valueOf(410)}));
    private static final Set<Integer> uncacheableStatuses = new HashSet(Arrays.asList(new Integer[]{Integer.valueOf(206), Integer.valueOf(303)}));
    private final Log log = LogFactory.getLog(getClass());
    private final long maxObjectSizeBytes;
    private final boolean sharedCache;

    public ResponseCachingPolicy(long j, boolean z) {
        this.maxObjectSizeBytes = j;
        this.sharedCache = z;
    }

    private boolean expiresHeaderLessOrEqualToDateHeaderAndNoCacheControl(HttpResponse httpResponse) {
        if (httpResponse.getFirstHeader("Cache-Control") != null) {
            return false;
        }
        Header firstHeader = httpResponse.getFirstHeader("Expires");
        Header firstHeader2 = httpResponse.getFirstHeader("Date");
        if (firstHeader == null || firstHeader2 == null) {
            return false;
        }
        try {
            Date parseDate = DateUtils.parseDate(firstHeader.getValue());
            Date parseDate2 = DateUtils.parseDate(firstHeader2.getValue());
            return parseDate.equals(parseDate2) || parseDate.before(parseDate2);
        } catch (DateParseException e) {
            return false;
        }
    }

    private boolean from1_0Origin(HttpResponse httpResponse) {
        Header firstHeader = httpResponse.getFirstHeader("Via");
        if (firstHeader != null) {
            HeaderElement[] elements = firstHeader.getElements();
            if (elements.length < 0) {
                String str = elements[0].toString().split("\\s")[0];
                return str.contains(URIUtil.SLASH) ? str.equals(HttpVersions.HTTP_1_0) : str.equals("1.0");
            }
        }
        return HttpVersion.HTTP_1_0.equals(httpResponse.getProtocolVersion());
    }

    private boolean requestProtocolGreaterThanAccepted(HttpRequest httpRequest) {
        return httpRequest.getProtocolVersion().compareToVersion(HttpVersion.HTTP_1_1) > 0;
    }

    private boolean unknownStatusCode(int i) {
        return (i < 100 || i > 101) && ((i < 200 || i > 206) && ((i < 300 || i > 307) && ((i < 400 || i > 417) && (i < 500 || i > 505))));
    }

    protected boolean hasCacheControlParameterFrom(HttpMessage httpMessage, String[] strArr) {
        for (Header elements : httpMessage.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                for (String equalsIgnoreCase : strArr) {
                    if (equalsIgnoreCase.equalsIgnoreCase(headerElement.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isExplicitlyCacheable(HttpResponse httpResponse) {
        if (httpResponse.getFirstHeader("Expires") != null) {
            return true;
        }
        return hasCacheControlParameterFrom(httpResponse, new String[]{"max-age", "s-maxage", HeaderConstants.CACHE_CONTROL_MUST_REVALIDATE, HeaderConstants.CACHE_CONTROL_PROXY_REVALIDATE, "public"});
    }

    protected boolean isExplicitlyNonCacheable(HttpResponse httpResponse) {
        for (Header elements : httpResponse.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                if (HeaderConstants.CACHE_CONTROL_NO_STORE.equals(headerElement.getName()) || "no-cache".equals(headerElement.getName()) || (this.sharedCache && "private".equals(headerElement.getName()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isResponseCacheable(String str, HttpResponse httpResponse) {
        if ("GET".equals(str)) {
            boolean z;
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (cacheableStatuses.contains(Integer.valueOf(statusCode))) {
                z = true;
            } else if (uncacheableStatuses.contains(Integer.valueOf(statusCode)) || unknownStatusCode(statusCode)) {
                return false;
            } else {
                z = false;
            }
            Header firstHeader = httpResponse.getFirstHeader("Content-Length");
            if ((firstHeader != null && ((long) Integer.parseInt(firstHeader.getValue())) > this.maxObjectSizeBytes) || httpResponse.getHeaders("Age").length > 1 || httpResponse.getHeaders("Expires").length > 1) {
                return false;
            }
            Header[] headers = httpResponse.getHeaders("Date");
            if (headers.length != 1) {
                return false;
            }
            try {
                DateUtils.parseDate(headers[0].getValue());
                for (Header elements : httpResponse.getHeaders("Vary")) {
                    for (HeaderElement name : elements.getElements()) {
                        if (Constraint.ANY_ROLE.equals(name.getName())) {
                            return false;
                        }
                    }
                }
                return !isExplicitlyNonCacheable(httpResponse) ? z || isExplicitlyCacheable(httpResponse) : false;
            } catch (DateParseException e) {
                return false;
            }
        }
        this.log.debug("Response was not cacheable.");
        return false;
    }

    public boolean isResponseCacheable(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (requestProtocolGreaterThanAccepted(httpRequest)) {
            this.log.debug("Response was not cacheable.");
            return false;
        }
        if (hasCacheControlParameterFrom(httpRequest, new String[]{HeaderConstants.CACHE_CONTROL_NO_STORE})) {
            return false;
        }
        if (httpRequest.getRequestLine().getUri().contains("?") && (!isExplicitlyCacheable(httpResponse) || from1_0Origin(httpResponse))) {
            this.log.debug("Response was not cacheable.");
            return false;
        } else if (expiresHeaderLessOrEqualToDateHeaderAndNoCacheControl(httpResponse)) {
            return false;
        } else {
            if (this.sharedCache) {
                Header[] headers = httpRequest.getHeaders("Authorization");
                if (headers != null && headers.length > 0) {
                    return hasCacheControlParameterFrom(httpResponse, new String[]{"s-maxage", HeaderConstants.CACHE_CONTROL_MUST_REVALIDATE, "public"});
                }
            }
            return isResponseCacheable(httpRequest.getRequestLine().getMethod(), httpResponse);
        }
    }
}
