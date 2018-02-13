package org.apache.http.impl.client.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.mortbay.jetty.HttpVersions;

@Immutable
class RequestProtocolCompliance {
    private static final List<String> disallowedWithNoCache = Arrays.asList(new String[]{HeaderConstants.CACHE_CONTROL_MIN_FRESH, HeaderConstants.CACHE_CONTROL_MAX_STALE, "max-age"});

    RequestProtocolCompliance() {
    }

    private void add100ContinueHeaderIfMissing(HttpRequest httpRequest) {
        Object obj = null;
        for (Header elements : httpRequest.getHeaders("Expect")) {
            for (HeaderElement name : elements.getElements()) {
                if ("100-continue".equalsIgnoreCase(name.getName())) {
                    obj = 1;
                }
            }
        }
        if (obj == null) {
            httpRequest.addHeader("Expect", "100-continue");
        }
    }

    private void addContentTypeHeaderIfMissing(HttpEntityEnclosingRequest httpEntityEnclosingRequest) {
        if (httpEntityEnclosingRequest.getEntity().getContentType() == null) {
            ((AbstractHttpEntity) httpEntityEnclosingRequest.getEntity()).setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
        }
    }

    private String buildHeaderFromElements(List<HeaderElement> list) {
        StringBuilder stringBuilder = new StringBuilder(HttpVersions.HTTP_0_9);
        Object obj = 1;
        for (HeaderElement headerElement : list) {
            if (obj == null) {
                stringBuilder.append(",");
            } else {
                obj = null;
            }
            stringBuilder.append(headerElement.toString());
        }
        return stringBuilder.toString();
    }

    private void decrementOPTIONSMaxForwardsIfGreaterThen0(HttpRequest httpRequest) {
        if ("OPTIONS".equals(httpRequest.getRequestLine().getMethod())) {
            Header firstHeader = httpRequest.getFirstHeader("Max-Forwards");
            if (firstHeader != null) {
                httpRequest.removeHeaders("Max-Forwards");
                httpRequest.setHeader("Max-Forwards", Integer.toString(Integer.parseInt(firstHeader.getValue()) - 1));
            }
        }
    }

    private HttpRequest downgradeRequestTo(HttpRequest httpRequest, ProtocolVersion protocolVersion) throws ClientProtocolException {
        try {
            HttpRequest requestWrapper = new RequestWrapper(httpRequest);
            requestWrapper.setProtocolVersion(protocolVersion);
            return requestWrapper;
        } catch (Throwable e) {
            throw new ClientProtocolException(e);
        }
    }

    private void remove100ContinueHeaderIfExists(HttpRequest httpRequest) {
        Header[] headers = httpRequest.getHeaders("Expect");
        ArrayList arrayList = new ArrayList();
        int length = headers.length;
        List list = arrayList;
        int i = 0;
        Object obj = null;
        while (i < length) {
            Header header = headers[i];
            for (HeaderElement headerElement : header.getElements()) {
                if ("100-continue".equalsIgnoreCase(headerElement.getName())) {
                    obj = 1;
                } else {
                    r3.add(headerElement);
                }
            }
            if (obj != null) {
                httpRequest.removeHeader(header);
                for (HeaderElement name : r3) {
                    httpRequest.addHeader(new BasicHeader("Expect", name.getName()));
                }
                return;
            }
            i++;
            Object arrayList2 = new ArrayList();
        }
    }

    private RequestProtocolError requestContainsNoCacheDirectiveWithFieldName(HttpRequest httpRequest) {
        for (Header elements : httpRequest.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                if ("no-cache".equalsIgnoreCase(headerElement.getName()) && headerElement.getValue() != null) {
                    return RequestProtocolError.NO_CACHE_DIRECTIVE_WITH_FIELD_NAME;
                }
            }
        }
        return null;
    }

    private RequestProtocolError requestHasWeakETagAndRange(HttpRequest httpRequest) {
        if ("GET".equals(httpRequest.getRequestLine().getMethod()) && httpRequest.getFirstHeader("Range") != null) {
            Header firstHeader = httpRequest.getFirstHeader("If-Range");
            if (firstHeader != null && firstHeader.getValue().startsWith("W/")) {
                return RequestProtocolError.WEAK_ETAG_AND_RANGE_ERROR;
            }
        }
        return null;
    }

    private RequestProtocolError requestHasWeekETagForPUTOrDELETEIfMatch(HttpRequest httpRequest) {
        String method = httpRequest.getRequestLine().getMethod();
        if ("PUT".equals(method) || "DELETE".equals(method)) {
            Header firstHeader = httpRequest.getFirstHeader("If-Match");
            if (firstHeader == null) {
                firstHeader = httpRequest.getFirstHeader("If-None-Match");
                if (firstHeader != null && firstHeader.getValue().startsWith("W/")) {
                    return RequestProtocolError.WEAK_ETAG_ON_PUTDELETE_METHOD_ERROR;
                }
            } else if (firstHeader.getValue().startsWith("W/")) {
                return RequestProtocolError.WEAK_ETAG_ON_PUTDELETE_METHOD_ERROR;
            }
        }
        return null;
    }

    private boolean requestMustNotHaveEntity(HttpRequest httpRequest) {
        return "TRACE".equals(httpRequest.getRequestLine().getMethod()) && (httpRequest instanceof HttpEntityEnclosingRequest);
    }

    private void stripOtherFreshnessDirectivesWithNoCache(HttpRequest httpRequest) {
        List arrayList = new ArrayList();
        Object obj = null;
        for (Header elements : httpRequest.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                if (!disallowedWithNoCache.contains(headerElement.getName())) {
                    arrayList.add(headerElement);
                }
                if ("no-cache".equals(headerElement.getName())) {
                    obj = 1;
                }
            }
        }
        if (obj != null) {
            httpRequest.removeHeaders("Cache-Control");
            httpRequest.setHeader("Cache-Control", buildHeaderFromElements(arrayList));
        }
    }

    private HttpRequest upgradeRequestTo(HttpRequest httpRequest, ProtocolVersion protocolVersion) throws ClientProtocolException {
        try {
            HttpRequest requestWrapper = new RequestWrapper(httpRequest);
            requestWrapper.setProtocolVersion(protocolVersion);
            return requestWrapper;
        } catch (Throwable e) {
            throw new ClientProtocolException(e);
        }
    }

    private void verifyOPTIONSRequestWithBodyHasContentType(HttpRequest httpRequest) {
        if ("OPTIONS".equals(httpRequest.getRequestLine().getMethod()) && (httpRequest instanceof HttpEntityEnclosingRequest)) {
            addContentTypeHeaderIfMissing((HttpEntityEnclosingRequest) httpRequest);
        }
    }

    private void verifyRequestWithExpectContinueFlagHas100continueHeader(HttpRequest httpRequest) {
        if (!(httpRequest instanceof HttpEntityEnclosingRequest)) {
            remove100ContinueHeaderIfExists(httpRequest);
        } else if (!((HttpEntityEnclosingRequest) httpRequest).expectContinue() || ((HttpEntityEnclosingRequest) httpRequest).getEntity() == null) {
            remove100ContinueHeaderIfExists(httpRequest);
        } else {
            add100ContinueHeaderIfMissing(httpRequest);
        }
    }

    public HttpResponse getErrorForRequest(RequestProtocolError requestProtocolError) {
        switch (requestProtocolError) {
            case BODY_BUT_NO_LENGTH_ERROR:
                return new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 411, HttpVersions.HTTP_0_9));
            case WEAK_ETAG_AND_RANGE_ERROR:
                return new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "Weak eTag not compatible with byte range"));
            case WEAK_ETAG_ON_PUTDELETE_METHOD_ERROR:
                return new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "Weak eTag not compatible with PUT or DELETE requests"));
            case NO_CACHE_DIRECTIVE_WITH_FIELD_NAME:
                return new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "No-Cache directive MUST NOT include a field name"));
            default:
                throw new IllegalStateException("The request was compliant, therefore no error can be generated for it.");
        }
    }

    public HttpRequest makeRequestCompliant(HttpRequest httpRequest) throws ClientProtocolException {
        if (requestMustNotHaveEntity(httpRequest)) {
            ((HttpEntityEnclosingRequest) httpRequest).setEntity(null);
        }
        verifyRequestWithExpectContinueFlagHas100continueHeader(httpRequest);
        verifyOPTIONSRequestWithBodyHasContentType(httpRequest);
        decrementOPTIONSMaxForwardsIfGreaterThen0(httpRequest);
        stripOtherFreshnessDirectivesWithNoCache(httpRequest);
        return requestVersionIsTooLow(httpRequest) ? upgradeRequestTo(httpRequest, HttpVersion.HTTP_1_1) : requestMinorVersionIsTooHighMajorVersionsMatch(httpRequest) ? downgradeRequestTo(httpRequest, HttpVersion.HTTP_1_1) : httpRequest;
    }

    public List<RequestProtocolError> requestIsFatallyNonCompliant(HttpRequest httpRequest) {
        List<RequestProtocolError> arrayList = new ArrayList();
        RequestProtocolError requestHasWeakETagAndRange = requestHasWeakETagAndRange(httpRequest);
        if (requestHasWeakETagAndRange != null) {
            arrayList.add(requestHasWeakETagAndRange);
        }
        requestHasWeakETagAndRange = requestHasWeekETagForPUTOrDELETEIfMatch(httpRequest);
        if (requestHasWeakETagAndRange != null) {
            arrayList.add(requestHasWeakETagAndRange);
        }
        requestHasWeakETagAndRange = requestContainsNoCacheDirectiveWithFieldName(httpRequest);
        if (requestHasWeakETagAndRange != null) {
            arrayList.add(requestHasWeakETagAndRange);
        }
        return arrayList;
    }

    protected boolean requestMinorVersionIsTooHighMajorVersionsMatch(HttpRequest httpRequest) {
        ProtocolVersion protocolVersion = httpRequest.getProtocolVersion();
        return protocolVersion.getMajor() == HttpVersion.HTTP_1_1.getMajor() && protocolVersion.getMinor() > HttpVersion.HTTP_1_1.getMinor();
    }

    protected boolean requestVersionIsTooLow(HttpRequest httpRequest) {
        return httpRequest.getProtocolVersion().compareToVersion(HttpVersion.HTTP_1_1) < 0;
    }
}
