package org.apache.http.impl.client.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.ResourceFactory;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

@Immutable
class CacheEntryUpdater {
    private final ResourceFactory resourceFactory;

    CacheEntryUpdater() {
        this(new HeapResourceFactory());
    }

    CacheEntryUpdater(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    private boolean entryAndResponseHaveDateHeader(HttpCacheEntry httpCacheEntry, HttpResponse httpResponse) {
        return (httpCacheEntry.getFirstHeader("Date") == null || httpResponse.getFirstHeader("Date") == null) ? false : true;
    }

    private boolean entryDateHeaderNewerThenResponse(HttpCacheEntry httpCacheEntry, HttpResponse httpResponse) {
        try {
            return DateUtils.parseDate(httpCacheEntry.getFirstHeader("Date").getValue()).after(DateUtils.parseDate(httpResponse.getFirstHeader("Date").getValue()));
        } catch (DateParseException e) {
            return false;
        }
    }

    private void removeCacheEntry1xxWarnings(List<Header> list, HttpCacheEntry httpCacheEntry) {
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if ("Warning".equals(((Header) listIterator.next()).getName())) {
                for (Header value : httpCacheEntry.getHeaders("Warning")) {
                    if (value.getValue().startsWith("1")) {
                        listIterator.remove();
                    }
                }
            }
        }
    }

    private void removeCacheHeadersThatMatchResponse(List<Header> list, HttpResponse httpResponse) {
        for (Header header : httpResponse.getAllHeaders()) {
            ListIterator listIterator = list.listIterator();
            while (listIterator.hasNext()) {
                if (((Header) listIterator.next()).getName().equals(header.getName())) {
                    listIterator.remove();
                }
            }
        }
    }

    protected Header[] mergeHeaders(HttpCacheEntry httpCacheEntry, HttpResponse httpResponse) {
        if (entryAndResponseHaveDateHeader(httpCacheEntry, httpResponse) && entryDateHeaderNewerThenResponse(httpCacheEntry, httpResponse)) {
            return httpCacheEntry.getAllHeaders();
        }
        List arrayList = new ArrayList(Arrays.asList(httpCacheEntry.getAllHeaders()));
        removeCacheHeadersThatMatchResponse(arrayList, httpResponse);
        removeCacheEntry1xxWarnings(arrayList, httpCacheEntry);
        arrayList.addAll(Arrays.asList(httpResponse.getAllHeaders()));
        return (Header[]) arrayList.toArray(new Header[arrayList.size()]);
    }

    public HttpCacheEntry updateCacheEntry(String str, HttpCacheEntry httpCacheEntry, Date date, Date date2, HttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusLine().getStatusCode() != 304) {
            throw new IllegalArgumentException("Response must have 304 status code");
        }
        return new HttpCacheEntry(date, date2, httpCacheEntry.getStatusLine(), mergeHeaders(httpCacheEntry, httpResponse), this.resourceFactory.copy(str, httpCacheEntry.getResource()));
    }
}
