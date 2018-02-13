package com.google.api.client.http.javanet;

import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.LowLevelHttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

final class NetHttpResponse extends LowLevelHttpResponse {
    private final HttpURLConnection connection;
    private final ArrayList<String> headerNames = new ArrayList();
    private final ArrayList<String> headerValues = new ArrayList();
    private final int responseCode;
    private final String responseMessage;

    NetHttpResponse(HttpURLConnection httpURLConnection) throws IOException {
        this.connection = httpURLConnection;
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == -1) {
            responseCode = 0;
        }
        this.responseCode = responseCode;
        this.responseMessage = httpURLConnection.getResponseMessage();
        List list = this.headerNames;
        List list2 = this.headerValues;
        for (Entry entry : httpURLConnection.getHeaderFields().entrySet()) {
            String str = (String) entry.getKey();
            if (str != null) {
                for (String str2 : (List) entry.getValue()) {
                    if (str2 != null) {
                        list.add(str);
                        list2.add(str2);
                    }
                }
            }
        }
    }

    public void disconnect() {
        this.connection.disconnect();
    }

    public InputStream getContent() throws IOException {
        HttpURLConnection httpURLConnection = this.connection;
        return HttpStatusCodes.isSuccess(this.responseCode) ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
    }

    public String getContentEncoding() {
        return this.connection.getContentEncoding();
    }

    public long getContentLength() {
        String headerField = this.connection.getHeaderField("Content-Length");
        return headerField == null ? -1 : Long.parseLong(headerField);
    }

    public String getContentType() {
        return this.connection.getHeaderField("Content-Type");
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
        return this.responseMessage;
    }

    public int getStatusCode() {
        return this.responseCode;
    }

    public String getStatusLine() {
        String headerField = this.connection.getHeaderField(0);
        return (headerField == null || !headerField.startsWith("HTTP/1.")) ? null : headerField;
    }
}
