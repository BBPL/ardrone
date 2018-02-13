package com.google.api.client.http;

import com.google.api.client.util.Charsets;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mortbay.jetty.HttpVersions;

public final class HttpResponse {
    private InputStream content;
    private final String contentEncoding;
    private int contentLoggingLimit;
    private boolean contentRead;
    private final String contentType;
    private boolean loggingEnabled;
    private final HttpMediaType mediaType;
    private final HttpRequest request;
    LowLevelHttpResponse response;
    private final int statusCode;
    private final String statusMessage;

    HttpResponse(HttpRequest httpRequest, LowLevelHttpResponse lowLevelHttpResponse) throws IOException {
        StringBuilder stringBuilder;
        HttpMediaType httpMediaType = null;
        this.request = httpRequest;
        this.contentLoggingLimit = httpRequest.getContentLoggingLimit();
        this.loggingEnabled = httpRequest.isLoggingEnabled();
        this.response = lowLevelHttpResponse;
        this.contentEncoding = lowLevelHttpResponse.getContentEncoding();
        int statusCode = lowLevelHttpResponse.getStatusCode();
        this.statusCode = statusCode;
        String reasonPhrase = lowLevelHttpResponse.getReasonPhrase();
        this.statusMessage = reasonPhrase;
        Logger logger = HttpTransport.LOGGER;
        Object obj = (this.loggingEnabled && logger.isLoggable(Level.CONFIG)) ? 1 : null;
        if (obj != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("-------------- RESPONSE --------------").append(StringUtils.LINE_SEPARATOR);
            String statusLine = lowLevelHttpResponse.getStatusLine();
            if (statusLine != null) {
                stringBuilder.append(statusLine);
            } else {
                stringBuilder.append(statusCode);
                if (reasonPhrase != null) {
                    stringBuilder.append(' ').append(reasonPhrase);
                }
            }
            stringBuilder.append(StringUtils.LINE_SEPARATOR);
        } else {
            stringBuilder = null;
        }
        httpRequest.getResponseHeaders().fromHttpResponse(lowLevelHttpResponse, obj != null ? stringBuilder : null);
        String contentType = lowLevelHttpResponse.getContentType();
        if (contentType == null) {
            contentType = httpRequest.getResponseHeaders().getContentType();
        }
        this.contentType = contentType;
        if (contentType != null) {
            httpMediaType = new HttpMediaType(contentType);
        }
        this.mediaType = httpMediaType;
        if (obj != null) {
            logger.config(stringBuilder.toString());
        }
    }

    private boolean hasMessageBody() throws IOException {
        int statusCode = getStatusCode();
        if (!getRequest().getRequestMethod().equals("HEAD") && statusCode / 100 != 1 && statusCode != 204 && statusCode != 304) {
            return true;
        }
        ignore();
        return false;
    }

    public void disconnect() throws IOException {
        ignore();
        this.response.disconnect();
    }

    public void download(OutputStream outputStream) throws IOException {
        IOUtils.copy(getContent(), outputStream);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.InputStream getContent() throws java.io.IOException {
        /*
        r6 = this;
        r0 = r6.contentRead;
        if (r0 != 0) goto L_0x0039;
    L_0x0004:
        r0 = r6.response;
        r0 = r0.getContent();
        if (r0 == 0) goto L_0x0036;
    L_0x000c:
        r1 = r6.contentEncoding;	 Catch:{ EOFException -> 0x003c, all -> 0x004d }
        if (r1 == 0) goto L_0x0049;
    L_0x0010:
        r2 = "gzip";
        r1 = r1.contains(r2);	 Catch:{ EOFException -> 0x003c, all -> 0x004d }
        if (r1 == 0) goto L_0x0049;
    L_0x0018:
        r1 = new java.util.zip.GZIPInputStream;	 Catch:{ EOFException -> 0x003c, all -> 0x004d }
        r1.<init>(r0);	 Catch:{ EOFException -> 0x003c, all -> 0x004d }
    L_0x001d:
        r2 = com.google.api.client.http.HttpTransport.LOGGER;	 Catch:{ EOFException -> 0x004b, all -> 0x0042 }
        r0 = r6.loggingEnabled;	 Catch:{ EOFException -> 0x004b, all -> 0x0042 }
        if (r0 == 0) goto L_0x0047;
    L_0x0023:
        r0 = java.util.logging.Level.CONFIG;	 Catch:{ EOFException -> 0x004b, all -> 0x0042 }
        r0 = r2.isLoggable(r0);	 Catch:{ EOFException -> 0x004b, all -> 0x0042 }
        if (r0 == 0) goto L_0x0047;
    L_0x002b:
        r0 = new com.google.api.client.util.LoggingInputStream;	 Catch:{ EOFException -> 0x004b, all -> 0x0042 }
        r3 = java.util.logging.Level.CONFIG;	 Catch:{ EOFException -> 0x004b, all -> 0x0042 }
        r4 = r6.contentLoggingLimit;	 Catch:{ EOFException -> 0x004b, all -> 0x0042 }
        r0.<init>(r1, r2, r3, r4);	 Catch:{ EOFException -> 0x004b, all -> 0x0042 }
    L_0x0034:
        r6.content = r0;	 Catch:{ EOFException -> 0x003c, all -> 0x0052 }
    L_0x0036:
        r0 = 1;
        r6.contentRead = r0;
    L_0x0039:
        r0 = r6.content;
        return r0;
    L_0x003c:
        r1 = move-exception;
        r1 = r0;
    L_0x003e:
        r1.close();
        goto L_0x0036;
    L_0x0042:
        r0 = move-exception;
    L_0x0043:
        r1.close();
        throw r0;
    L_0x0047:
        r0 = r1;
        goto L_0x0034;
    L_0x0049:
        r1 = r0;
        goto L_0x001d;
    L_0x004b:
        r0 = move-exception;
        goto L_0x003e;
    L_0x004d:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x0043;
    L_0x0052:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.http.HttpResponse.getContent():java.io.InputStream");
    }

    public Charset getContentCharset() {
        return (this.mediaType == null || this.mediaType.getCharsetParameter() == null) ? Charsets.ISO_8859_1 : this.mediaType.getCharsetParameter();
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public int getContentLoggingLimit() {
        return this.contentLoggingLimit;
    }

    public String getContentType() {
        return this.contentType;
    }

    public HttpHeaders getHeaders() {
        return this.request.getResponseHeaders();
    }

    public HttpMediaType getMediaType() {
        return this.mediaType;
    }

    public HttpRequest getRequest() {
        return this.request;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public HttpTransport getTransport() {
        return this.request.getTransport();
    }

    public void ignore() throws IOException {
        InputStream content = getContent();
        if (content != null) {
            content.close();
        }
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public boolean isSuccessStatusCode() {
        return HttpStatusCodes.isSuccess(this.statusCode);
    }

    public <T> T parseAs(Class<T> cls) throws IOException {
        return !hasMessageBody() ? null : this.request.getParser().parseAndClose(getContent(), getContentCharset(), (Class) cls);
    }

    public Object parseAs(Type type) throws IOException {
        return !hasMessageBody() ? null : this.request.getParser().parseAndClose(getContent(), getContentCharset(), type);
    }

    public String parseAsString() throws IOException {
        InputStream content = getContent();
        if (content == null) {
            return HttpVersions.HTTP_0_9;
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(content, byteArrayOutputStream);
        return byteArrayOutputStream.toString(getContentCharset().name());
    }

    public HttpResponse setContentLoggingLimit(int i) {
        Preconditions.checkArgument(i >= 0, "The content logging limit must be non-negative.");
        this.contentLoggingLimit = i;
        return this;
    }

    public HttpResponse setLoggingEnabled(boolean z) {
        this.loggingEnabled = z;
        return this;
    }
}
