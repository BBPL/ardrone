package org.apache.http.impl.client.cache;

import java.util.Date;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

@Immutable
class CacheValidityPolicy {
    public static final long MAX_AGE = 2147483648L;

    CacheValidityPolicy() {
    }

    private boolean mayReturnStaleIfError(Header[] headerArr, long j) {
        boolean z = false;
        for (Header elements : headerArr) {
            for (HeaderElement headerElement : elements.getElements()) {
                if (HeaderConstants.STALE_IF_ERROR.equals(headerElement.getName())) {
                    try {
                        if (j <= ((long) Integer.parseInt(headerElement.getValue()))) {
                            z = true;
                            break;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        return z;
    }

    protected boolean contentLengthHeaderMatchesActualLength(HttpCacheEntry httpCacheEntry) {
        return !hasContentLengthHeader(httpCacheEntry) || getContentLengthValue(httpCacheEntry) == httpCacheEntry.getResource().length();
    }

    protected long getAgeValue(HttpCacheEntry httpCacheEntry) {
        Header[] headers = httpCacheEntry.getHeaders("Age");
        int length = headers.length;
        long j = 0;
        int i = 0;
        while (i < length) {
            long parseLong;
            try {
                parseLong = Long.parseLong(headers[i].getValue());
                if (parseLong < 0) {
                    parseLong = 2147483648L;
                }
            } catch (NumberFormatException e) {
                parseLong = 2147483648L;
            }
            if (parseLong <= j) {
                parseLong = j;
            }
            i++;
            j = parseLong;
        }
        return j;
    }

    protected long getApparentAgeSecs(HttpCacheEntry httpCacheEntry) {
        Date dateValue = getDateValue(httpCacheEntry);
        if (dateValue == null) {
            return 2147483648L;
        }
        long time = httpCacheEntry.getResponseDate().getTime() - dateValue.getTime();
        return time >= 0 ? time / 1000 : 0;
    }

    protected long getContentLengthValue(HttpCacheEntry httpCacheEntry) {
        long j = -1;
        Header firstHeader = httpCacheEntry.getFirstHeader("Content-Length");
        if (firstHeader != null) {
            try {
                j = Long.parseLong(firstHeader.getValue());
            } catch (NumberFormatException e) {
            }
        }
        return j;
    }

    protected long getCorrectedInitialAgeSecs(HttpCacheEntry httpCacheEntry) {
        return getCorrectedReceivedAgeSecs(httpCacheEntry) + getResponseDelaySecs(httpCacheEntry);
    }

    protected long getCorrectedReceivedAgeSecs(HttpCacheEntry httpCacheEntry) {
        long apparentAgeSecs = getApparentAgeSecs(httpCacheEntry);
        long ageValue = getAgeValue(httpCacheEntry);
        return apparentAgeSecs > ageValue ? apparentAgeSecs : ageValue;
    }

    public long getCurrentAgeSecs(HttpCacheEntry httpCacheEntry, Date date) {
        return getCorrectedInitialAgeSecs(httpCacheEntry) + getResidentTimeSecs(httpCacheEntry, date);
    }

    protected Date getDateValue(HttpCacheEntry httpCacheEntry) {
        Date date = null;
        Header firstHeader = httpCacheEntry.getFirstHeader("Date");
        if (firstHeader != null) {
            try {
                date = DateUtils.parseDate(firstHeader.getValue());
            } catch (DateParseException e) {
            }
        }
        return date;
    }

    protected Date getExpirationDate(HttpCacheEntry httpCacheEntry) {
        Date date = null;
        Header firstHeader = httpCacheEntry.getFirstHeader("Expires");
        if (firstHeader != null) {
            try {
                date = DateUtils.parseDate(firstHeader.getValue());
            } catch (DateParseException e) {
            }
        }
        return date;
    }

    public long getFreshnessLifetimeSecs(HttpCacheEntry httpCacheEntry) {
        long maxAge = getMaxAge(httpCacheEntry);
        if (maxAge > -1) {
            return maxAge;
        }
        Date dateValue = getDateValue(httpCacheEntry);
        if (dateValue == null) {
            return 0;
        }
        Date expirationDate = getExpirationDate(httpCacheEntry);
        return expirationDate == null ? 0 : (expirationDate.getTime() - dateValue.getTime()) / 1000;
    }

    public long getHeuristicFreshnessLifetimeSecs(HttpCacheEntry httpCacheEntry, float f, long j) {
        Date dateValue = getDateValue(httpCacheEntry);
        Date lastModifiedValue = getLastModifiedValue(httpCacheEntry);
        if (dateValue == null || lastModifiedValue == null) {
            return j;
        }
        long time = dateValue.getTime() - lastModifiedValue.getTime();
        return time < 0 ? 0 : (long) (((float) (time / 1000)) * f);
    }

    protected Date getLastModifiedValue(HttpCacheEntry httpCacheEntry) {
        Date date = null;
        Header firstHeader = httpCacheEntry.getFirstHeader("Last-Modified");
        if (firstHeader != null) {
            try {
                date = DateUtils.parseDate(firstHeader.getValue());
            } catch (DateParseException e) {
            }
        }
        return date;
    }

    protected long getMaxAge(HttpCacheEntry httpCacheEntry) {
        long j = -1;
        for (Header elements : httpCacheEntry.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                if ("max-age".equals(headerElement.getName()) || "s-maxage".equals(headerElement.getName())) {
                    try {
                        long parseLong = Long.parseLong(headerElement.getValue());
                        if (j == -1 || parseLong < j) {
                            j = parseLong;
                        }
                    } catch (NumberFormatException e) {
                        j = 0;
                    }
                }
            }
        }
        return j;
    }

    protected long getResidentTimeSecs(HttpCacheEntry httpCacheEntry, Date date) {
        return (date.getTime() - httpCacheEntry.getResponseDate().getTime()) / 1000;
    }

    protected long getResponseDelaySecs(HttpCacheEntry httpCacheEntry) {
        return (httpCacheEntry.getResponseDate().getTime() - httpCacheEntry.getRequestDate().getTime()) / 1000;
    }

    public long getStalenessSecs(HttpCacheEntry httpCacheEntry, Date date) {
        long currentAgeSecs = getCurrentAgeSecs(httpCacheEntry, date);
        long freshnessLifetimeSecs = getFreshnessLifetimeSecs(httpCacheEntry);
        return currentAgeSecs <= freshnessLifetimeSecs ? 0 : currentAgeSecs - freshnessLifetimeSecs;
    }

    public boolean hasCacheControlDirective(HttpCacheEntry httpCacheEntry, String str) {
        for (Header elements : httpCacheEntry.getHeaders("Cache-Control")) {
            for (HeaderElement name : elements.getElements()) {
                if (str.equalsIgnoreCase(name.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean hasContentLengthHeader(HttpCacheEntry httpCacheEntry) {
        return httpCacheEntry.getFirstHeader("Content-Length") != null;
    }

    public boolean isResponseFresh(HttpCacheEntry httpCacheEntry, Date date) {
        return getCurrentAgeSecs(httpCacheEntry, date) < getFreshnessLifetimeSecs(httpCacheEntry);
    }

    public boolean isResponseHeuristicallyFresh(HttpCacheEntry httpCacheEntry, Date date, float f, long j) {
        return getCurrentAgeSecs(httpCacheEntry, date) < getHeuristicFreshnessLifetimeSecs(httpCacheEntry, f, j);
    }

    public boolean isRevalidatable(HttpCacheEntry httpCacheEntry) {
        return (httpCacheEntry.getFirstHeader("ETag") == null && httpCacheEntry.getFirstHeader("Last-Modified") == null) ? false : true;
    }

    public boolean mayReturnStaleIfError(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, Date date) {
        long stalenessSecs = getStalenessSecs(httpCacheEntry, date);
        return mayReturnStaleIfError(httpRequest.getHeaders("Cache-Control"), stalenessSecs) || mayReturnStaleIfError(httpCacheEntry.getHeaders("Cache-Control"), stalenessSecs);
    }

    public boolean mayReturnStaleWhileRevalidating(HttpCacheEntry httpCacheEntry, Date date) {
        for (Header elements : httpCacheEntry.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                if (HeaderConstants.STALE_WHILE_REVALIDATE.equalsIgnoreCase(headerElement.getName())) {
                    try {
                        if (getStalenessSecs(httpCacheEntry, date) <= ((long) Integer.parseInt(headerElement.getValue()))) {
                            return true;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        return false;
    }

    public boolean mustRevalidate(HttpCacheEntry httpCacheEntry) {
        return hasCacheControlDirective(httpCacheEntry, HeaderConstants.CACHE_CONTROL_MUST_REVALIDATE);
    }

    public boolean proxyRevalidate(HttpCacheEntry httpCacheEntry) {
        return hasCacheControlDirective(httpCacheEntry, HeaderConstants.CACHE_CONTROL_PROXY_REVALIDATE);
    }
}
