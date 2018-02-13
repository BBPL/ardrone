package org.apache.http.client.fluent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public class Request {
    public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final Locale DATE_LOCALE = Locale.US;
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");
    private SimpleDateFormat dateFormatter;
    private final HttpParams localParams;
    private final HttpRequestBase request;

    Request(HttpRequestBase httpRequestBase) {
        this.request = httpRequestBase;
        this.localParams = httpRequestBase.getParams();
    }

    public static Request Delete(String str) {
        return new Request(new HttpDelete(str));
    }

    public static Request Delete(URI uri) {
        return new Request(new HttpDelete(uri));
    }

    public static Request Get(String str) {
        return new Request(new HttpGet(str));
    }

    public static Request Get(URI uri) {
        return new Request(new HttpGet(uri));
    }

    public static Request Head(String str) {
        return new Request(new HttpHead(str));
    }

    public static Request Head(URI uri) {
        return new Request(new HttpHead(uri));
    }

    public static Request Options(String str) {
        return new Request(new HttpOptions(str));
    }

    public static Request Options(URI uri) {
        return new Request(new HttpOptions(uri));
    }

    public static Request Post(String str) {
        return new Request(new HttpPost(str));
    }

    public static Request Post(URI uri) {
        return new Request(new HttpPost(uri));
    }

    public static Request Put(String str) {
        return new Request(new HttpPut(str));
    }

    public static Request Put(URI uri) {
        return new Request(new HttpPut(uri));
    }

    public static Request Trace(String str) {
        return new Request(new HttpTrace(str));
    }

    public static Request Trace(URI uri) {
        return new Request(new HttpTrace(uri));
    }

    private SimpleDateFormat getDateFormat() {
        if (this.dateFormatter == null) {
            this.dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", DATE_LOCALE);
            this.dateFormatter.setTimeZone(TIME_ZONE);
        }
        return this.dateFormatter;
    }

    public void abort() throws UnsupportedOperationException {
        this.request.abort();
    }

    public Request addHeader(String str, String str2) {
        this.request.addHeader(str, str2);
        return this;
    }

    public Request addHeader(Header header) {
        this.request.addHeader(header);
        return this;
    }

    public Request body(HttpEntity httpEntity) {
        if (this.request instanceof HttpEntityEnclosingRequest) {
            ((HttpEntityEnclosingRequest) this.request).setEntity(httpEntity);
            return this;
        }
        throw new IllegalStateException(this.request.getMethod() + " request cannot enclose an entity");
    }

    public Request bodyByteArray(byte[] bArr) {
        return body(new ByteArrayEntity(bArr));
    }

    public Request bodyByteArray(byte[] bArr, int i, int i2) {
        return body(new ByteArrayEntity(bArr, i, i2));
    }

    public Request bodyFile(File file, ContentType contentType) {
        return body(new FileEntity(file, contentType));
    }

    public Request bodyForm(Iterable<? extends NameValuePair> iterable) {
        return bodyForm(iterable, HTTP.DEF_CONTENT_CHARSET);
    }

    public Request bodyForm(Iterable<? extends NameValuePair> iterable, Charset charset) {
        return body(new UrlEncodedFormEntity((Iterable) iterable, charset));
    }

    public Request bodyForm(NameValuePair... nameValuePairArr) {
        return bodyForm(Arrays.asList(nameValuePairArr), HTTP.DEF_CONTENT_CHARSET);
    }

    public Request bodyStream(InputStream inputStream) {
        return body(new InputStreamEntity(inputStream, -1));
    }

    public Request bodyStream(InputStream inputStream, ContentType contentType) {
        return body(new InputStreamEntity(inputStream, -1, contentType));
    }

    public Request bodyString(String str, ContentType contentType) {
        return body(new StringEntity(str, contentType));
    }

    public Request config(String str, Object obj) {
        this.localParams.setParameter(str, obj);
        return this;
    }

    public Request connectTimeout(int i) {
        return config(CoreConnectionPNames.CONNECTION_TIMEOUT, Integer.valueOf(i));
    }

    public Request elementCharset(String str) {
        return config(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, str);
    }

    public Response execute() throws ClientProtocolException, IOException {
        return new Response(Executor.CLIENT.execute(this.request));
    }

    HttpRequestBase getHttpRequest() {
        return this.request;
    }

    public Request removeConfig(String str) {
        this.localParams.removeParameter(str);
        return this;
    }

    public Request removeHeader(Header header) {
        this.request.removeHeader(header);
        return this;
    }

    public Request removeHeaders(String str) {
        this.request.removeHeaders(str);
        return this;
    }

    public Request setCacheControl(String str) {
        this.request.setHeader("Cache-Control", str);
        return this;
    }

    public Request setDate(Date date) {
        this.request.setHeader("Date", getDateFormat().format(date));
        return this;
    }

    public Request setHeaders(Header[] headerArr) {
        this.request.setHeaders(headerArr);
        return this;
    }

    public Request setIfModifiedSince(Date date) {
        this.request.setHeader("If-Modified-Since", getDateFormat().format(date));
        return this;
    }

    public Request setIfUnmodifiedSince(Date date) {
        this.request.setHeader("If-Unmodified-Since", getDateFormat().format(date));
        return this;
    }

    public Request socketTimeout(int i) {
        return config(CoreConnectionPNames.SO_TIMEOUT, Integer.valueOf(i));
    }

    public Request staleConnectionCheck(boolean z) {
        return config(CoreConnectionPNames.STALE_CONNECTION_CHECK, Boolean.valueOf(z));
    }

    public String toString() {
        return this.request.getRequestLine().toString();
    }

    public Request useExpectContinue() {
        return config(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.valueOf(true));
    }

    public Request userAgent(String str) {
        return config(CoreProtocolPNames.USER_AGENT, str);
    }

    public Request version(HttpVersion httpVersion) {
        return config(CoreProtocolPNames.PROTOCOL_VERSION, httpVersion);
    }

    public Request viaProxy(HttpHost httpHost) {
        return config(ConnRoutePNames.DEFAULT_PROXY, httpHost);
    }
}
