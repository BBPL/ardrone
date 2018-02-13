package com.google.api.client.googleapis.batch;

import com.google.api.client.http.AbstractHttpContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

class HttpRequestContent extends AbstractHttpContent {
    static final String NEWLINE = "\r\n";
    private final HttpRequest request;

    HttpRequestContent(HttpRequest httpRequest) {
        super("application/http");
        this.request = httpRequest;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        Writer outputStreamWriter = new OutputStreamWriter(outputStream, getCharset());
        outputStreamWriter.write(this.request.getRequestMethod());
        outputStreamWriter.write(" ");
        outputStreamWriter.write(this.request.getUrl().build());
        outputStreamWriter.write("\r\n");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.fromHttpHeaders(this.request.getHeaders());
        httpHeaders.setAcceptEncoding(null).setUserAgent(null).setContentEncoding(null).setContentType(null).setContentLength(null);
        HttpContent content = this.request.getContent();
        if (content != null) {
            httpHeaders.setContentType(content.getType());
            long length = content.getLength();
            if (length != -1) {
                httpHeaders.setContentLength(Long.valueOf(length));
            }
        }
        HttpHeaders.serializeHeadersForMultipartRequests(httpHeaders, null, null, outputStreamWriter);
        if (content != null) {
            outputStreamWriter.write("\r\n");
            outputStreamWriter.flush();
            content.writeTo(outputStream);
        }
    }
}
