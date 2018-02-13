package org.apache.http.impl.client.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.mortbay.jetty.HttpVersions;

@Immutable
class ResponseProtocolCompliance {
    private static final String UNEXPECTED_100_CONTINUE = "The incoming request did not contain a 100-continue header, but the response was a Status 100, continue.";
    private static final String UNEXPECTED_PARTIAL_CONTENT = "partial content was returned for a request that did not ask for it";

    ResponseProtocolCompliance() {
    }

    private boolean backendResponseMustNotHaveBody(HttpRequest httpRequest, HttpResponse httpResponse) {
        return "HEAD".equals(httpRequest.getRequestLine().getMethod()) || httpResponse.getStatusLine().getStatusCode() == 204 || httpResponse.getStatusLine().getStatusCode() == 205 || httpResponse.getStatusLine().getStatusCode() == 304;
    }

    private void consumeBody(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            EntityUtils.consume(entity);
        }
    }

    private void ensure200ForOPTIONSRequestWithNoBodyHasContentLengthZero(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getRequestLine().getMethod().equalsIgnoreCase("OPTIONS") && httpResponse.getStatusLine().getStatusCode() == 200 && httpResponse.getFirstHeader("Content-Length") == null) {
            httpResponse.addHeader("Content-Length", "0");
        }
    }

    private void ensure206ContainsDateHeader(HttpResponse httpResponse) {
        if (httpResponse.getFirstHeader("Date") == null) {
            httpResponse.addHeader("Date", DateUtils.formatDate(new Date()));
        }
    }

    private void ensure304DoesNotContainExtraEntityHeaders(HttpResponse httpResponse) {
        int i = 0;
        String[] strArr = new String[]{"Allow", "Content-Encoding", "Content-Language", "Content-Length", "Content-MD5", "Content-Range", "Content-Type", "Last-Modified"};
        if (httpResponse.getStatusLine().getStatusCode() == 304) {
            int length = strArr.length;
            while (i < length) {
                httpResponse.removeHeaders(strArr[i]);
                i++;
            }
        }
    }

    private void ensurePartialContentIsNotSentToAClientThatDidNotRequestIt(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (httpRequest.getFirstHeader("Range") == null && httpResponse.getStatusLine().getStatusCode() == 206) {
            consumeBody(httpResponse);
            throw new ClientProtocolException(UNEXPECTED_PARTIAL_CONTENT);
        }
    }

    private ProtocolVersion getOriginalRequestProtocol(RequestWrapper requestWrapper) {
        return requestWrapper.getOriginal().getProtocolVersion();
    }

    private void identityIsNotUsedInContentEncoding(HttpResponse httpResponse) {
        Header[] headers = httpResponse.getHeaders("Content-Encoding");
        if (headers != null && headers.length != 0) {
            List<Header> arrayList = new ArrayList();
            Object obj = null;
            for (Header header : headers) {
                StringBuilder stringBuilder = new StringBuilder();
                Object obj2 = 1;
                for (HeaderElement headerElement : header.getElements()) {
                    if ("identity".equalsIgnoreCase(headerElement.getName())) {
                        obj = 1;
                    } else {
                        if (obj2 == null) {
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(headerElement.toString());
                        obj2 = null;
                    }
                }
                String stringBuilder2 = stringBuilder.toString();
                if (!HttpVersions.HTTP_0_9.equals(stringBuilder2)) {
                    arrayList.add(new BasicHeader("Content-Encoding", stringBuilder2));
                }
            }
            if (obj != null) {
                httpResponse.removeHeaders("Content-Encoding");
                for (Header addHeader : arrayList) {
                    httpResponse.addHeader(addHeader);
                }
            }
        }
    }

    private void removeResponseTransferEncoding(HttpResponse httpResponse) {
        httpResponse.removeHeaders("TE");
        httpResponse.removeHeaders("Transfer-Encoding");
    }

    private void requestDidNotExpect100ContinueButResponseIsOne(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusLine().getStatusCode() == 100) {
            HttpRequest original = requestWasWrapped(httpRequest) ? ((RequestWrapper) httpRequest).getOriginal() : httpRequest;
            if (!(original instanceof HttpEntityEnclosingRequest) || !((HttpEntityEnclosingRequest) original).expectContinue()) {
                consumeBody(httpResponse);
                throw new ClientProtocolException(UNEXPECTED_100_CONTINUE);
            }
        }
    }

    private boolean requestWasWrapped(HttpRequest httpRequest) {
        return httpRequest instanceof RequestWrapper;
    }

    private void transferEncodingIsNotReturnedTo1_0Client(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (requestWasWrapped(httpRequest) && getOriginalRequestProtocol((RequestWrapper) httpRequest).compareToVersion(HttpVersion.HTTP_1_1) < 0) {
            removeResponseTransferEncoding(httpResponse);
        }
    }

    private void warningsWithNonMatchingWarnDatesAreRemoved(HttpResponse httpResponse) {
        Object parseDate;
        Object obj = null;
        try {
            parseDate = DateUtils.parseDate(httpResponse.getFirstHeader("Date").getValue());
        } catch (DateParseException e) {
            parseDate = obj;
        }
        if (parseDate != null) {
            Header[] headers = httpResponse.getHeaders("Warning");
            if (headers != null && headers.length != 0) {
                List<Header> arrayList = new ArrayList();
                obj = null;
                for (Header warningValues : headers) {
                    for (WarningValue warningValue : WarningValue.getWarningValues(warningValues)) {
                        Date warnDate = warningValue.getWarnDate();
                        if (warnDate == null || warnDate.equals(parseDate)) {
                            arrayList.add(new BasicHeader("Warning", warningValue.toString()));
                        } else {
                            obj = 1;
                        }
                    }
                }
                if (obj != null) {
                    httpResponse.removeHeaders("Warning");
                    for (Header addHeader : arrayList) {
                        httpResponse.addHeader(addHeader);
                    }
                }
            }
        }
    }

    public void ensureProtocolCompliance(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (backendResponseMustNotHaveBody(httpRequest, httpResponse)) {
            consumeBody(httpResponse);
            httpResponse.setEntity(null);
        }
        requestDidNotExpect100ContinueButResponseIsOne(httpRequest, httpResponse);
        transferEncodingIsNotReturnedTo1_0Client(httpRequest, httpResponse);
        ensurePartialContentIsNotSentToAClientThatDidNotRequestIt(httpRequest, httpResponse);
        ensure200ForOPTIONSRequestWithNoBodyHasContentLengthZero(httpRequest, httpResponse);
        ensure206ContainsDateHeader(httpResponse);
        ensure304DoesNotContainExtraEntityHeaders(httpResponse);
        identityIsNotUsedInContentEncoding(httpResponse);
        warningsWithNonMatchingWarnDatesAreRemoved(httpResponse);
    }
}
