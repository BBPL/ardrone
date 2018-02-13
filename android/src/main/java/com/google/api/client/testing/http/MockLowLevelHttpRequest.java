package com.google.api.client.testing.http;

import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.util.Charsets;
import com.google.api.client.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.mortbay.jetty.HttpHeaderValues;
import org.mortbay.jetty.HttpVersions;

public class MockLowLevelHttpRequest extends LowLevelHttpRequest {
    @Deprecated
    private HttpContent content;
    private final Map<String, List<String>> headersMap = new HashMap();
    private MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
    private String url;

    public MockLowLevelHttpRequest(String str) {
        this.url = str;
    }

    public void addHeader(String str, String str2) throws IOException {
        String toLowerCase = str.toLowerCase();
        List list = (List) this.headersMap.get(toLowerCase);
        if (list == null) {
            list = new ArrayList();
            this.headersMap.put(toLowerCase, list);
        }
        list.add(str2);
    }

    public LowLevelHttpResponse execute() throws IOException {
        return this.response;
    }

    @Deprecated
    public HttpContent getContent() {
        return this.content;
    }

    public String getContentAsString() throws IOException {
        if (getStreamingContent() == null) {
            return HttpVersions.HTTP_0_9;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        getStreamingContent().writeTo(byteArrayOutputStream);
        String contentEncoding = getContentEncoding();
        if (contentEncoding != null && contentEncoding.contains(HttpHeaderValues.GZIP)) {
            InputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(gZIPInputStream, byteArrayOutputStream);
        }
        String contentType = getContentType();
        HttpMediaType httpMediaType = contentType != null ? new HttpMediaType(contentType) : null;
        Charset charsetParameter = (httpMediaType == null || httpMediaType.getCharsetParameter() == null) ? Charsets.ISO_8859_1 : httpMediaType.getCharsetParameter();
        return byteArrayOutputStream.toString(charsetParameter.name());
    }

    public String getFirstHeaderValue(String str) {
        List list = (List) this.headersMap.get(str.toLowerCase());
        return list == null ? null : (String) list.get(0);
    }

    public List<String> getHeaderValues(String str) {
        List list = (List) this.headersMap.get(str.toLowerCase());
        return list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
    }

    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(this.headersMap);
    }

    public MockLowLevelHttpResponse getResponse() {
        return this.response;
    }

    public String getUrl() {
        return this.url;
    }

    @Deprecated
    public void setContent(HttpContent httpContent) throws IOException {
        this.content = httpContent;
    }

    public MockLowLevelHttpRequest setResponse(MockLowLevelHttpResponse mockLowLevelHttpResponse) {
        this.response = mockLowLevelHttpResponse;
        return this;
    }

    public MockLowLevelHttpRequest setUrl(String str) {
        this.url = str;
        return this;
    }
}
