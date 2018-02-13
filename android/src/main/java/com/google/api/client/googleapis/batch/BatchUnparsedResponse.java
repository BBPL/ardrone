package com.google.api.client.googleapis.batch;

import com.google.api.client.http.BackOffPolicy;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.util.StringUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.mortbay.jetty.HttpVersions;

final class BatchUnparsedResponse {
    boolean backOffRequired;
    private final String boundary;
    private final BufferedReader bufferedReader;
    private int contentId = 0;
    boolean hasNext = true;
    private final List<RequestInfo<?, ?>> requestInfos;
    private final boolean retryAllowed;
    List<RequestInfo<?, ?>> unsuccessfulRequestInfos = new ArrayList();

    @Deprecated
    private static class FakeLowLevelHttpRequest extends LowLevelHttpRequest {
        private List<String> headerNames;
        private List<String> headerValues;
        private String partContent;
        private int statusCode;

        FakeLowLevelHttpRequest(String str, int i, List<String> list, List<String> list2) {
            this.partContent = str;
            this.statusCode = i;
            this.headerNames = list;
            this.headerValues = list2;
        }

        public void addHeader(String str, String str2) {
        }

        public LowLevelHttpResponse execute() {
            return new FakeLowLevelHttpResponse(new ByteArrayInputStream(StringUtils.getBytesUtf8(this.partContent)), this.statusCode, this.headerNames, this.headerValues);
        }

        public void setContent(HttpContent httpContent) {
        }
    }

    @Deprecated
    private static class FakeLowLevelHttpResponse extends LowLevelHttpResponse {
        private List<String> headerNames = new ArrayList();
        private List<String> headerValues = new ArrayList();
        private InputStream partContent;
        private int statusCode;

        FakeLowLevelHttpResponse(InputStream inputStream, int i, List<String> list, List<String> list2) {
            this.partContent = inputStream;
            this.statusCode = i;
            this.headerNames = list;
            this.headerValues = list2;
        }

        public InputStream getContent() {
            return this.partContent;
        }

        public String getContentEncoding() {
            return null;
        }

        public long getContentLength() {
            return 0;
        }

        public String getContentType() {
            return null;
        }

        public int getHeaderCount() {
            return this.headerNames.size();
        }

        public String getHeaderName(int i) {
            return (String) this.headerNames.get(i);
        }

        public String getHeaderValue(int i) {
            return (String) this.headerValues.get(i);
        }

        public String getReasonPhrase() {
            return null;
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        public String getStatusLine() {
            return null;
        }
    }

    @Deprecated
    private static class FakeResponseHttpTransport extends HttpTransport {
        private List<String> headerNames;
        private List<String> headerValues;
        private String partContent;
        private int statusCode;

        FakeResponseHttpTransport(int i, String str, List<String> list, List<String> list2) {
            this.statusCode = i;
            this.partContent = str;
            this.headerNames = list;
            this.headerValues = list2;
        }

        protected LowLevelHttpRequest buildRequest(String str, String str2) {
            return new FakeLowLevelHttpRequest(this.partContent, this.statusCode, this.headerNames, this.headerValues);
        }
    }

    BatchUnparsedResponse(InputStream inputStream, String str, List<RequestInfo<?, ?>> list, boolean z) throws IOException {
        this.boundary = str;
        this.requestInfos = list;
        this.retryAllowed = z;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        checkForFinalBoundary(this.bufferedReader.readLine());
    }

    private void checkForFinalBoundary(String str) throws IOException {
        if (str.equals(this.boundary + "--")) {
            this.hasNext = false;
            this.bufferedReader.close();
        }
    }

    @Deprecated
    private HttpResponse getFakeResponse(int i, String str, List<String> list, List<String> list2) throws IOException {
        HttpRequest buildPostRequest = new FakeResponseHttpTransport(i, str, list, list2).createRequestFactory().buildPostRequest(new GenericUrl(HttpTesting.SIMPLE_URL), null);
        buildPostRequest.setLoggingEnabled(false);
        buildPostRequest.setThrowExceptionOnExecuteError(false);
        return buildPostRequest.execute();
    }

    private <A, T, E> A getParsedDataClass(Class<A> cls, HttpResponse httpResponse, RequestInfo<T, E> requestInfo, String str) throws IOException {
        return cls == Void.class ? null : requestInfo.request.getParser().parseAndClose(httpResponse.getContent(), httpResponse.getContentCharset(), (Class) cls);
    }

    private <T, E> void parseAndCallback(RequestInfo<T, E> requestInfo, int i, int i2, HttpResponse httpResponse) throws IOException {
        boolean z = true;
        BatchCallback batchCallback = requestInfo.callback;
        HttpHeaders headers = httpResponse.getHeaders();
        HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler = requestInfo.request.getUnsuccessfulResponseHandler();
        BackOffPolicy backOffPolicy = requestInfo.request.getBackOffPolicy();
        this.backOffRequired = false;
        if (!HttpStatusCodes.isSuccess(i)) {
            HttpContent content = requestInfo.request.getContent();
            boolean z2 = this.retryAllowed && (content == null || content.retrySupported());
            boolean handleResponse = unsuccessfulResponseHandler != null ? unsuccessfulResponseHandler.handleResponse(requestInfo.request, httpResponse, z2) : false;
            if (!handleResponse) {
                if (!requestInfo.request.handleRedirect(httpResponse.getStatusCode(), httpResponse.getHeaders())) {
                    if (z2 && backOffPolicy != null && backOffPolicy.isBackOffRequired(httpResponse.getStatusCode())) {
                        this.backOffRequired = true;
                        z = false;
                    }
                }
                if (!z2 && (handleResponse || this.backOffRequired || r0)) {
                    this.unsuccessfulRequestInfos.add(requestInfo);
                    return;
                } else if (batchCallback != null) {
                    batchCallback.onFailure(getParsedDataClass(requestInfo.errorClass, httpResponse, requestInfo, headers.getContentType()), headers);
                }
            }
            z = false;
            if (!z2) {
            }
            if (batchCallback != null) {
                batchCallback.onFailure(getParsedDataClass(requestInfo.errorClass, httpResponse, requestInfo, headers.getContentType()), headers);
            }
        } else if (batchCallback != null) {
            batchCallback.onSuccess(getParsedDataClass(requestInfo.dataClass, httpResponse, requestInfo, headers.getContentType()), headers);
        }
    }

    void parseNextResponse() throws IOException {
        StringBuilder stringBuilder;
        String readLine;
        this.contentId++;
        String readLine2;
        do {
            readLine2 = this.bufferedReader.readLine();
            if (readLine2 == null) {
                break;
            }
        } while (!readLine2.equals(HttpVersions.HTTP_0_9));
        int parseInt = Integer.parseInt(this.bufferedReader.readLine().split(" ")[1]);
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        while (true) {
            String readLine3 = this.bufferedReader.readLine();
            if (readLine3 == null || readLine3.equals(HttpVersions.HTTP_0_9)) {
                stringBuilder = new StringBuilder();
            } else {
                String[] split = readLine3.split(": ", 2);
                arrayList.add(split[0]);
                arrayList2.add(split[1]);
            }
        }
        stringBuilder = new StringBuilder();
        while (true) {
            readLine = this.bufferedReader.readLine();
            if (readLine == null || readLine.startsWith(this.boundary)) {
                parseAndCallback((RequestInfo) this.requestInfos.get(this.contentId - 1), parseInt, this.contentId, getFakeResponse(parseInt, stringBuilder.toString(), arrayList, arrayList2));
                checkForFinalBoundary(readLine);
            } else {
                stringBuilder.append(readLine);
            }
        }
        parseAndCallback((RequestInfo) this.requestInfos.get(this.contentId - 1), parseInt, this.contentId, getFakeResponse(parseInt, stringBuilder.toString(), arrayList, arrayList2));
        checkForFinalBoundary(readLine);
    }
}
