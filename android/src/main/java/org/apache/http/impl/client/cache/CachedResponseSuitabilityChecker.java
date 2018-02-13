package org.apache.http.impl.client.cache;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.security.Constraint;

@Immutable
class CachedResponseSuitabilityChecker {
    private final float heuristicCoefficient;
    private final long heuristicDefaultLifetime;
    private final Log log;
    private final boolean sharedCache;
    private final boolean useHeuristicCaching;
    private final CacheValidityPolicy validityStrategy;

    CachedResponseSuitabilityChecker(CacheConfig cacheConfig) {
        this(new CacheValidityPolicy(), cacheConfig);
    }

    CachedResponseSuitabilityChecker(CacheValidityPolicy cacheValidityPolicy, CacheConfig cacheConfig) {
        this.log = LogFactory.getLog(getClass());
        this.validityStrategy = cacheValidityPolicy;
        this.sharedCache = cacheConfig.isSharedCache();
        this.useHeuristicCaching = cacheConfig.isHeuristicCachingEnabled();
        this.heuristicCoefficient = cacheConfig.getHeuristicCoefficient();
        this.heuristicDefaultLifetime = cacheConfig.getHeuristicDefaultLifetime();
    }

    private boolean etagValidatorMatches(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry) {
        Header firstHeader = httpCacheEntry.getFirstHeader("ETag");
        Object value = firstHeader != null ? firstHeader.getValue() : null;
        Header[] headers = httpRequest.getHeaders("If-None-Match");
        if (headers != null) {
            for (Header elements : headers) {
                for (Object obj : elements.getElements()) {
                    String obj2 = obj.toString();
                    if ((Constraint.ANY_ROLE.equals(obj2) && value != null) || obj2.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private long getMaxStale(HttpRequest httpRequest) {
        long j = -1;
        for (Header elements : httpRequest.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                if (HeaderConstants.CACHE_CONTROL_MAX_STALE.equals(headerElement.getName())) {
                    if ((headerElement.getValue() == null || HttpVersions.HTTP_0_9.equals(headerElement.getValue().trim())) && j == -1) {
                        j = Long.MAX_VALUE;
                    } else {
                        try {
                            long parseLong = Long.parseLong(headerElement.getValue());
                            if (parseLong < 0) {
                                parseLong = 0;
                            }
                            if (j == -1 || parseLong < j) {
                                j = parseLong;
                            }
                        } catch (NumberFormatException e) {
                            j = 0;
                        }
                    }
                }
            }
        }
        return j;
    }

    private boolean hasSupportedEtagValidator(HttpRequest httpRequest) {
        return httpRequest.containsHeader("If-None-Match");
    }

    private boolean hasSupportedLastModifiedValidator(HttpRequest httpRequest) {
        return hasValidDateField(httpRequest, "If-Modified-Since");
    }

    private boolean hasUnsupportedConditionalHeaders(HttpRequest httpRequest) {
        return (httpRequest.getFirstHeader("If-Range") == null && httpRequest.getFirstHeader("If-Match") == null && !hasValidDateField(httpRequest, "If-Unmodified-Since")) ? false : true;
    }

    private boolean hasValidDateField(HttpRequest httpRequest, String str) {
        Header[] headers = httpRequest.getHeaders(str);
        int length = headers.length;
        int i = 0;
        while (i < length) {
            try {
                DateUtils.parseDate(headers[i].getValue());
                return true;
            } catch (DateParseException e) {
                i++;
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isFreshEnough(org.apache.http.client.cache.HttpCacheEntry r9, org.apache.http.HttpRequest r10, java.util.Date r11) {
        /*
        r8 = this;
        r6 = 1;
        r7 = 0;
        r0 = r8.validityStrategy;
        r0 = r0.isResponseFresh(r9, r11);
        if (r0 == 0) goto L_0x000c;
    L_0x000a:
        r7 = r6;
    L_0x000b:
        return r7;
    L_0x000c:
        r0 = r8.useHeuristicCaching;
        if (r0 == 0) goto L_0x001e;
    L_0x0010:
        r0 = r8.validityStrategy;
        r3 = r8.heuristicCoefficient;
        r4 = r8.heuristicDefaultLifetime;
        r1 = r9;
        r2 = r11;
        r0 = r0.isResponseHeuristicallyFresh(r1, r2, r3, r4);
        if (r0 != 0) goto L_0x000a;
    L_0x001e:
        r0 = r8.originInsistsOnFreshness(r9);
        if (r0 != 0) goto L_0x000b;
    L_0x0024:
        r0 = r8.getMaxStale(r10);
        r2 = -1;
        r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x000b;
    L_0x002e:
        r2 = r8.validityStrategy;
        r2 = r2.getStalenessSecs(r9, r11);
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 <= 0) goto L_0x003b;
    L_0x0038:
        r0 = r6;
    L_0x0039:
        r7 = r0;
        goto L_0x000b;
    L_0x003b:
        r0 = r7;
        goto L_0x0039;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.client.cache.CachedResponseSuitabilityChecker.isFreshEnough(org.apache.http.client.cache.HttpCacheEntry, org.apache.http.HttpRequest, java.util.Date):boolean");
    }

    private boolean lastModifiedValidatorMatches(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, Date date) {
        Date parseDate;
        Header firstHeader = httpCacheEntry.getFirstHeader("Last-Modified");
        Date date2 = null;
        if (firstHeader != null) {
            try {
                parseDate = DateUtils.parseDate(firstHeader.getValue());
            } catch (DateParseException e) {
                parseDate = date2;
            }
        } else {
            parseDate = date2;
        }
        if (parseDate == null) {
            return false;
        }
        Header[] headers = httpRequest.getHeaders("If-Modified-Since");
        int length = headers.length;
        int i = 0;
        while (i < length) {
            try {
                Date parseDate2 = DateUtils.parseDate(headers[i].getValue());
                if (parseDate2.after(date) || parseDate.after(parseDate2)) {
                    return false;
                }
                i++;
            } catch (DateParseException e2) {
            }
        }
        return true;
    }

    private boolean originInsistsOnFreshness(HttpCacheEntry httpCacheEntry) {
        return this.validityStrategy.mustRevalidate(httpCacheEntry) ? true : this.sharedCache && (this.validityStrategy.proxyRevalidate(httpCacheEntry) || this.validityStrategy.hasCacheControlDirective(httpCacheEntry, "s-maxage"));
    }

    public boolean allConditionalsMatch(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, Date date) {
        boolean hasSupportedEtagValidator = hasSupportedEtagValidator(httpRequest);
        boolean hasSupportedLastModifiedValidator = hasSupportedLastModifiedValidator(httpRequest);
        boolean z = hasSupportedEtagValidator && etagValidatorMatches(httpRequest, httpCacheEntry);
        boolean z2 = hasSupportedLastModifiedValidator && lastModifiedValidatorMatches(httpRequest, httpCacheEntry, date);
        return (hasSupportedEtagValidator && hasSupportedLastModifiedValidator && (!z || !z2)) ? false : (!hasSupportedEtagValidator || z) ? !hasSupportedLastModifiedValidator || z2 : false;
    }

    public boolean canCachedResponseBeUsed(HttpHost httpHost, HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, Date date) {
        if (!isFreshEnough(httpCacheEntry, httpRequest, date)) {
            this.log.trace("Cache entry was not fresh enough");
            return false;
        } else if (!this.validityStrategy.contentLengthHeaderMatchesActualLength(httpCacheEntry)) {
            this.log.debug("Cache entry Content-Length and header information do not match");
            return false;
        } else if (hasUnsupportedConditionalHeaders(httpRequest)) {
            this.log.debug("Request contained conditional headers we don't handle");
            return false;
        } else if (isConditional(httpRequest) && !allConditionalsMatch(httpRequest, httpCacheEntry, date)) {
            return false;
        } else {
            for (Header elements : httpRequest.getHeaders("Cache-Control")) {
                HeaderElement[] elements2 = elements.getElements();
                int length = elements2.length;
                int i = 0;
                while (i < length) {
                    HeaderElement headerElement = elements2[i];
                    if ("no-cache".equals(headerElement.getName())) {
                        this.log.trace("Response contained NO CACHE directive, cache was not suitable");
                        return false;
                    } else if (HeaderConstants.CACHE_CONTROL_NO_STORE.equals(headerElement.getName())) {
                        this.log.trace("Response contained NO STORE directive, cache was not suitable");
                        return false;
                    } else {
                        if ("max-age".equals(headerElement.getName())) {
                            try {
                                if (this.validityStrategy.getCurrentAgeSecs(httpCacheEntry, date) > ((long) Integer.parseInt(headerElement.getValue()))) {
                                    this.log.trace("Response from cache was NOT suitable due to max age");
                                    return false;
                                }
                            } catch (NumberFormatException e) {
                                this.log.debug("Response from cache was malformed" + e.getMessage());
                                return false;
                            }
                        }
                        if (HeaderConstants.CACHE_CONTROL_MAX_STALE.equals(headerElement.getName())) {
                            try {
                                if (this.validityStrategy.getFreshnessLifetimeSecs(httpCacheEntry) > ((long) Integer.parseInt(headerElement.getValue()))) {
                                    this.log.trace("Response from cache was not suitable due to Max stale freshness");
                                    return false;
                                }
                            } catch (NumberFormatException e2) {
                                this.log.debug("Response from cache was malformed: " + e2.getMessage());
                                return false;
                            }
                        }
                        if (HeaderConstants.CACHE_CONTROL_MIN_FRESH.equals(headerElement.getName())) {
                            try {
                                long parseLong = Long.parseLong(headerElement.getValue());
                                if (parseLong < 0) {
                                    return false;
                                }
                                if (this.validityStrategy.getFreshnessLifetimeSecs(httpCacheEntry) - this.validityStrategy.getCurrentAgeSecs(httpCacheEntry, date) < parseLong) {
                                    this.log.trace("Response from cache was not suitable due to min fresh freshness requirement");
                                    return false;
                                }
                            } catch (NumberFormatException e22) {
                                this.log.debug("Response from cache was malformed: " + e22.getMessage());
                                return false;
                            }
                        }
                        i++;
                    }
                }
            }
            this.log.trace("Response from cache was suitable");
            return true;
        }
    }

    public boolean isConditional(HttpRequest httpRequest) {
        return hasSupportedEtagValidator(httpRequest) || hasSupportedLastModifiedValidator(httpRequest);
    }
}
