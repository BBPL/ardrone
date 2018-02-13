package com.google.api.client.http;

import com.google.api.client.util.IOUtils;
import com.google.api.client.util.LoggingStreamingContent;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StreamingContent;
import com.google.api.client.util.StringUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HttpRequest {
    public static final String USER_AGENT_SUFFIX = "Google-HTTP-Java-Client/1.14.1-beta (gzip)";
    public static final String VERSION = "1.14.1-beta";
    private BackOffPolicy backOffPolicy;
    private int connectTimeout = 20000;
    private HttpContent content;
    private int contentLoggingLimit = 16384;
    private boolean curlLoggingEnabled = true;
    @Deprecated
    private boolean enableGZipContent;
    private HttpEncoding encoding;
    private HttpExecuteInterceptor executeInterceptor;
    private boolean followRedirects = true;
    private HttpHeaders headers = new HttpHeaders();
    private boolean loggingEnabled = true;
    private int numRetries = 10;
    private ObjectParser objectParser;
    private int readTimeout = 20000;
    private String requestMethod;
    private HttpHeaders responseHeaders = new HttpHeaders();
    private HttpResponseInterceptor responseInterceptor;
    private boolean retryOnExecuteIOException = false;
    private boolean suppressUserAgentSuffix;
    private boolean throwExceptionOnExecuteError = true;
    private final HttpTransport transport;
    private HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler;
    private GenericUrl url;

    class C04522 implements Callable<HttpResponse> {
        C04522() {
        }

        public HttpResponse call() throws Exception {
            return HttpRequest.this.execute();
        }
    }

    HttpRequest(HttpTransport httpTransport, String str) {
        this.transport = httpTransport;
        setRequestMethod(str);
    }

    private void sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
        }
    }

    public HttpResponse execute() throws IOException {
        Throwable th;
        Preconditions.checkArgument(this.numRetries >= 0);
        int i = this.numRetries;
        if (this.backOffPolicy != null) {
            this.backOffPolicy.reset();
        }
        HttpResponse httpResponse = null;
        Preconditions.checkNotNull(this.requestMethod);
        Preconditions.checkNotNull(this.url);
        int i2 = i;
        while (true) {
            Object obj;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            StringBuilder stringBuilder3;
            StringBuilder stringBuilder4;
            String userAgent;
            StreamingContent streamingContent;
            final boolean z;
            final String type;
            StreamingContent loggingStreamingContent;
            String encoding;
            boolean z2;
            String name;
            StreamingContent httpEncodingStreamingContent;
            long computeLength;
            boolean z3;
            HttpResponse httpResponse2;
            boolean z4;
            Object obj2;
            Object obj3;
            long nextBackOffMillis;
            int i3;
            if (httpResponse != null) {
                httpResponse.ignore();
            }
            if (this.executeInterceptor != null) {
                this.executeInterceptor.intercept(this);
            }
            String build = this.url.build();
            LowLevelHttpRequest buildRequest = this.transport.buildRequest(this.requestMethod, build);
            Logger logger = HttpTransport.LOGGER;
            if (this.loggingEnabled) {
                if (logger.isLoggable(Level.CONFIG)) {
                    obj = 1;
                    if (obj == null) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("-------------- REQUEST  --------------").append(StringUtils.LINE_SEPARATOR);
                        stringBuilder.append(this.requestMethod).append(' ').append(build).append(StringUtils.LINE_SEPARATOR);
                        if (this.curlLoggingEnabled) {
                            stringBuilder2 = stringBuilder;
                            stringBuilder3 = null;
                        } else {
                            stringBuilder4 = new StringBuilder("curl -v --compressed");
                            if (this.requestMethod.equals("GET")) {
                                stringBuilder4.append(" -X ").append(this.requestMethod);
                                stringBuilder2 = stringBuilder;
                                stringBuilder3 = stringBuilder4;
                            } else {
                                stringBuilder2 = stringBuilder;
                                stringBuilder3 = stringBuilder4;
                            }
                        }
                    } else {
                        stringBuilder2 = null;
                        stringBuilder3 = null;
                    }
                    userAgent = this.headers.getUserAgent();
                    if (!this.suppressUserAgentSuffix) {
                        if (userAgent != null) {
                            this.headers.setUserAgent(USER_AGENT_SUFFIX);
                        } else {
                            this.headers.setUserAgent(userAgent + " " + USER_AGENT_SUFFIX);
                        }
                    }
                    HttpHeaders.serializeHeaders(this.headers, stringBuilder2, stringBuilder3, logger, buildRequest);
                    if (!this.suppressUserAgentSuffix) {
                        this.headers.setUserAgent(userAgent);
                    }
                    streamingContent = this.content;
                    z = streamingContent != null || this.content.retrySupported();
                    if (streamingContent == null) {
                        type = this.content.getType();
                        loggingStreamingContent = obj == null ? new LoggingStreamingContent(streamingContent, HttpTransport.LOGGER, Level.CONFIG, this.contentLoggingLimit) : streamingContent;
                        encoding = this.content.getEncoding();
                        if (this.enableGZipContent) {
                            z2 = this.encoding != null || (this.encoding instanceof GZipEncoding);
                            Preconditions.checkArgument(z2);
                            setEncoding(new GZipEncoding());
                        }
                        if (this.encoding == null && encoding == null) {
                            name = this.encoding.getName();
                            httpEncodingStreamingContent = new HttpEncodingStreamingContent(loggingStreamingContent, this.encoding);
                            computeLength = z ? IOUtils.computeLength(httpEncodingStreamingContent) : -1;
                        } else {
                            z2 = this.encoding != null || encoding == null;
                            Preconditions.checkArgument(z2);
                            computeLength = this.content.getLength();
                            name = encoding;
                            httpEncodingStreamingContent = loggingStreamingContent;
                        }
                        if (obj != null) {
                            if (type != null) {
                                userAgent = "Content-Type: " + type;
                                stringBuilder2.append(userAgent).append(StringUtils.LINE_SEPARATOR);
                                if (stringBuilder3 != null) {
                                    stringBuilder3.append(" -H '" + userAgent + "'");
                                }
                            }
                            if (encoding != null) {
                                userAgent = "Content-Encoding: " + encoding;
                                stringBuilder2.append(userAgent).append(StringUtils.LINE_SEPARATOR);
                                if (stringBuilder3 != null) {
                                    stringBuilder3.append(" -H '" + userAgent + "'");
                                }
                            }
                            if (computeLength >= 0) {
                                stringBuilder2.append("Content-Length: " + computeLength).append(StringUtils.LINE_SEPARATOR);
                            }
                        }
                        if (stringBuilder3 != null) {
                            stringBuilder3.append(" -d '@-'");
                        }
                        buildRequest.setContent(new HttpContent() {
                            @Deprecated
                            public String getEncoding() {
                                return name;
                            }

                            public long getLength() throws IOException {
                                return computeLength;
                            }

                            public String getType() {
                                return type;
                            }

                            public boolean retrySupported() {
                                return z;
                            }

                            public void writeTo(OutputStream outputStream) throws IOException {
                                httpEncodingStreamingContent.writeTo(outputStream);
                            }
                        });
                        buildRequest.setContentType(type);
                        buildRequest.setContentEncoding(name);
                        buildRequest.setContentLength(computeLength);
                        buildRequest.setStreamingContent(httpEncodingStreamingContent);
                    } else {
                        httpEncodingStreamingContent = streamingContent;
                    }
                    if (obj != null) {
                        logger.config(stringBuilder2.toString());
                        if (stringBuilder3 != null) {
                            stringBuilder3.append(" -- ");
                            stringBuilder3.append(build);
                            if (httpEncodingStreamingContent != null) {
                                stringBuilder3.append(" << $$$");
                            }
                            logger.config(stringBuilder3.toString());
                        }
                    }
                    z3 = z && i2 > 0;
                    buildRequest.setTimeout(this.connectTimeout, this.readTimeout);
                    LowLevelHttpResponse execute = buildRequest.execute();
                    th = null;
                    httpResponse2 = new HttpResponse(this, execute);
                    if (httpResponse2 != null) {
                        try {
                            if (!httpResponse2.isSuccessStatusCode()) {
                                z4 = false;
                                obj2 = null;
                                obj3 = null;
                                if (this.unsuccessfulResponseHandler != null) {
                                    z4 = this.unsuccessfulResponseHandler.handleResponse(this, httpResponse2, z3);
                                }
                                if (!z4) {
                                    if (handleRedirect(httpResponse2.getStatusCode(), httpResponse2.getHeaders())) {
                                        obj2 = 1;
                                    } else if (z3) {
                                        if (this.backOffPolicy != null && this.backOffPolicy.isBackOffRequired(httpResponse2.getStatusCode())) {
                                            nextBackOffMillis = this.backOffPolicy.getNextBackOffMillis();
                                            if (nextBackOffMillis != -1) {
                                                sleep(nextBackOffMillis);
                                                obj3 = 1;
                                            }
                                        }
                                    }
                                }
                                i = (!z4 && obj2 == null && obj3 == null) ? 0 : 1;
                                i3 = z3 & i;
                                if (i3 == 0) {
                                    httpResponse2.ignore();
                                    i = i3;
                                } else {
                                    i = i3;
                                }
                                i3 = i2 - 1;
                                if (httpResponse2 == null) {
                                    if (i != 0) {
                                        break;
                                    }
                                    i2 = i3;
                                    httpResponse = httpResponse2;
                                } else if (i != 0) {
                                    break;
                                } else {
                                    i2 = i3;
                                    httpResponse = httpResponse2;
                                }
                            }
                        } catch (Throwable th2) {
                            if (httpResponse2 != null) {
                                httpResponse2.disconnect();
                            }
                        }
                    }
                    i = z3 & (httpResponse2 != null ? 1 : 0);
                    i3 = i2 - 1;
                    if (httpResponse2 == null) {
                        if (i != 0) {
                            break;
                        }
                        i2 = i3;
                        httpResponse = httpResponse2;
                    } else if (i != 0) {
                        break;
                    } else {
                        i2 = i3;
                        httpResponse = httpResponse2;
                    }
                }
            }
            obj = null;
            if (obj == null) {
                stringBuilder2 = null;
                stringBuilder3 = null;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("-------------- REQUEST  --------------").append(StringUtils.LINE_SEPARATOR);
                stringBuilder.append(this.requestMethod).append(' ').append(build).append(StringUtils.LINE_SEPARATOR);
                if (this.curlLoggingEnabled) {
                    stringBuilder2 = stringBuilder;
                    stringBuilder3 = null;
                } else {
                    stringBuilder4 = new StringBuilder("curl -v --compressed");
                    if (this.requestMethod.equals("GET")) {
                        stringBuilder2 = stringBuilder;
                        stringBuilder3 = stringBuilder4;
                    } else {
                        stringBuilder4.append(" -X ").append(this.requestMethod);
                        stringBuilder2 = stringBuilder;
                        stringBuilder3 = stringBuilder4;
                    }
                }
            }
            userAgent = this.headers.getUserAgent();
            if (this.suppressUserAgentSuffix) {
                if (userAgent != null) {
                    this.headers.setUserAgent(userAgent + " " + USER_AGENT_SUFFIX);
                } else {
                    this.headers.setUserAgent(USER_AGENT_SUFFIX);
                }
            }
            HttpHeaders.serializeHeaders(this.headers, stringBuilder2, stringBuilder3, logger, buildRequest);
            if (this.suppressUserAgentSuffix) {
                this.headers.setUserAgent(userAgent);
            }
            streamingContent = this.content;
            if (streamingContent != null) {
            }
            if (streamingContent == null) {
                httpEncodingStreamingContent = streamingContent;
            } else {
                type = this.content.getType();
                if (obj == null) {
                }
                encoding = this.content.getEncoding();
                if (this.enableGZipContent) {
                    if (this.encoding != null) {
                    }
                    Preconditions.checkArgument(z2);
                    setEncoding(new GZipEncoding());
                }
                if (this.encoding == null) {
                }
                if (this.encoding != null) {
                }
                Preconditions.checkArgument(z2);
                computeLength = this.content.getLength();
                name = encoding;
                httpEncodingStreamingContent = loggingStreamingContent;
                if (obj != null) {
                    if (type != null) {
                        userAgent = "Content-Type: " + type;
                        stringBuilder2.append(userAgent).append(StringUtils.LINE_SEPARATOR);
                        if (stringBuilder3 != null) {
                            stringBuilder3.append(" -H '" + userAgent + "'");
                        }
                    }
                    if (encoding != null) {
                        userAgent = "Content-Encoding: " + encoding;
                        stringBuilder2.append(userAgent).append(StringUtils.LINE_SEPARATOR);
                        if (stringBuilder3 != null) {
                            stringBuilder3.append(" -H '" + userAgent + "'");
                        }
                    }
                    if (computeLength >= 0) {
                        stringBuilder2.append("Content-Length: " + computeLength).append(StringUtils.LINE_SEPARATOR);
                    }
                }
                if (stringBuilder3 != null) {
                    stringBuilder3.append(" -d '@-'");
                }
                buildRequest.setContent(/* anonymous class already generated */);
                buildRequest.setContentType(type);
                buildRequest.setContentEncoding(name);
                buildRequest.setContentLength(computeLength);
                buildRequest.setStreamingContent(httpEncodingStreamingContent);
            }
            if (obj != null) {
                logger.config(stringBuilder2.toString());
                if (stringBuilder3 != null) {
                    stringBuilder3.append(" -- ");
                    stringBuilder3.append(build);
                    if (httpEncodingStreamingContent != null) {
                        stringBuilder3.append(" << $$$");
                    }
                    logger.config(stringBuilder3.toString());
                }
            }
            if (!z) {
            }
            buildRequest.setTimeout(this.connectTimeout, this.readTimeout);
            try {
                LowLevelHttpResponse execute2 = buildRequest.execute();
                th = null;
                httpResponse2 = new HttpResponse(this, execute2);
            } catch (Throwable e) {
                r4 = null;
                if (this.retryOnExecuteIOException) {
                    HttpResponse httpResponse3;
                    logger.log(Level.WARNING, "exception thrown while executing request", e);
                    th = e;
                    httpResponse2 = httpResponse3;
                } else {
                    throw e;
                }
            } catch (Throwable th3) {
                execute2.getContent().close();
            }
            if (httpResponse2 != null) {
                if (httpResponse2.isSuccessStatusCode()) {
                    z4 = false;
                    obj2 = null;
                    obj3 = null;
                    if (this.unsuccessfulResponseHandler != null) {
                        z4 = this.unsuccessfulResponseHandler.handleResponse(this, httpResponse2, z3);
                    }
                    if (z4) {
                        if (handleRedirect(httpResponse2.getStatusCode(), httpResponse2.getHeaders())) {
                            obj2 = 1;
                        } else if (z3) {
                            nextBackOffMillis = this.backOffPolicy.getNextBackOffMillis();
                            if (nextBackOffMillis != -1) {
                                sleep(nextBackOffMillis);
                                obj3 = 1;
                            }
                        }
                    }
                    if (!!z4) {
                    }
                    i3 = z3 & i;
                    if (i3 == 0) {
                        i = i3;
                    } else {
                        httpResponse2.ignore();
                        i = i3;
                    }
                    i3 = i2 - 1;
                    if (httpResponse2 == null) {
                        if (i != 0) {
                            break;
                        }
                        i2 = i3;
                        httpResponse = httpResponse2;
                    } else if (i != 0) {
                        break;
                    } else {
                        i2 = i3;
                        httpResponse = httpResponse2;
                    }
                }
            }
            if (httpResponse2 != null) {
            }
            i = z3 & (httpResponse2 != null ? 1 : 0);
            i3 = i2 - 1;
            if (httpResponse2 == null) {
                if (i != 0) {
                    break;
                }
                i2 = i3;
                httpResponse = httpResponse2;
            } else if (i != 0) {
                break;
            } else {
                i2 = i3;
                httpResponse = httpResponse2;
            }
        }
        if (httpResponse2 == null) {
            throw th;
        }
        if (this.responseInterceptor != null) {
            this.responseInterceptor.interceptResponse(httpResponse2);
        }
        if (!this.throwExceptionOnExecuteError || httpResponse2.isSuccessStatusCode()) {
            return httpResponse2;
        }
        try {
            throw new HttpResponseException(httpResponse2);
        } catch (Throwable th4) {
            httpResponse2.disconnect();
        }
    }

    public Future<HttpResponse> executeAsync() {
        return executeAsync(Executors.newSingleThreadExecutor());
    }

    public Future<HttpResponse> executeAsync(Executor executor) {
        Object futureTask = new FutureTask(new C04522());
        executor.execute(futureTask);
        return futureTask;
    }

    public BackOffPolicy getBackOffPolicy() {
        return this.backOffPolicy;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public HttpContent getContent() {
        return this.content;
    }

    public int getContentLoggingLimit() {
        return this.contentLoggingLimit;
    }

    @Deprecated
    public boolean getEnableGZipContent() {
        return this.enableGZipContent;
    }

    public HttpEncoding getEncoding() {
        return this.encoding;
    }

    public boolean getFollowRedirects() {
        return this.followRedirects;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public HttpExecuteInterceptor getInterceptor() {
        return this.executeInterceptor;
    }

    public int getNumberOfRetries() {
        return this.numRetries;
    }

    public final ObjectParser getParser() {
        return this.objectParser;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public HttpHeaders getResponseHeaders() {
        return this.responseHeaders;
    }

    public HttpResponseInterceptor getResponseInterceptor() {
        return this.responseInterceptor;
    }

    public boolean getRetryOnExecuteIOException() {
        return this.retryOnExecuteIOException;
    }

    public boolean getSuppressUserAgentSuffix() {
        return this.suppressUserAgentSuffix;
    }

    public boolean getThrowExceptionOnExecuteError() {
        return this.throwExceptionOnExecuteError;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public HttpUnsuccessfulResponseHandler getUnsuccessfulResponseHandler() {
        return this.unsuccessfulResponseHandler;
    }

    public GenericUrl getUrl() {
        return this.url;
    }

    public boolean handleRedirect(int i, HttpHeaders httpHeaders) {
        String location = httpHeaders.getLocation();
        if (!getFollowRedirects() || !HttpStatusCodes.isRedirect(i) || location == null) {
            return false;
        }
        setUrl(new GenericUrl(this.url.toURL(location)));
        if (i == 303) {
            setRequestMethod("GET");
        }
        this.headers.setAuthorization((String) null);
        this.headers.setIfMatch((String) null);
        this.headers.setIfNoneMatch((String) null);
        this.headers.setIfModifiedSince((String) null);
        this.headers.setIfUnmodifiedSince((String) null);
        this.headers.setIfRange((String) null);
        return true;
    }

    public boolean isCurlLoggingEnabled() {
        return this.curlLoggingEnabled;
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public HttpRequest setBackOffPolicy(BackOffPolicy backOffPolicy) {
        this.backOffPolicy = backOffPolicy;
        return this;
    }

    public HttpRequest setConnectTimeout(int i) {
        Preconditions.checkArgument(i >= 0);
        this.connectTimeout = i;
        return this;
    }

    public HttpRequest setContent(HttpContent httpContent) {
        this.content = httpContent;
        return this;
    }

    public HttpRequest setContentLoggingLimit(int i) {
        Preconditions.checkArgument(i >= 0, "The content logging limit must be non-negative.");
        this.contentLoggingLimit = i;
        return this;
    }

    public HttpRequest setCurlLoggingEnabled(boolean z) {
        this.curlLoggingEnabled = z;
        return this;
    }

    @Deprecated
    public HttpRequest setEnableGZipContent(boolean z) {
        this.enableGZipContent = z;
        return this;
    }

    public HttpRequest setEncoding(HttpEncoding httpEncoding) {
        this.encoding = httpEncoding;
        return this;
    }

    public HttpRequest setFollowRedirects(boolean z) {
        this.followRedirects = z;
        return this;
    }

    public HttpRequest setHeaders(HttpHeaders httpHeaders) {
        this.headers = (HttpHeaders) Preconditions.checkNotNull(httpHeaders);
        return this;
    }

    public HttpRequest setInterceptor(HttpExecuteInterceptor httpExecuteInterceptor) {
        this.executeInterceptor = httpExecuteInterceptor;
        return this;
    }

    public HttpRequest setLoggingEnabled(boolean z) {
        this.loggingEnabled = z;
        return this;
    }

    public HttpRequest setNumberOfRetries(int i) {
        Preconditions.checkArgument(i >= 0);
        this.numRetries = i;
        return this;
    }

    public HttpRequest setParser(ObjectParser objectParser) {
        this.objectParser = objectParser;
        return this;
    }

    public HttpRequest setReadTimeout(int i) {
        Preconditions.checkArgument(i >= 0);
        this.readTimeout = i;
        return this;
    }

    public HttpRequest setRequestMethod(String str) {
        boolean z = str == null || HttpMediaType.matchesToken(str);
        Preconditions.checkArgument(z);
        this.requestMethod = str;
        return this;
    }

    public HttpRequest setResponseHeaders(HttpHeaders httpHeaders) {
        this.responseHeaders = (HttpHeaders) Preconditions.checkNotNull(httpHeaders);
        return this;
    }

    public HttpRequest setResponseInterceptor(HttpResponseInterceptor httpResponseInterceptor) {
        this.responseInterceptor = httpResponseInterceptor;
        return this;
    }

    public HttpRequest setRetryOnExecuteIOException(boolean z) {
        this.retryOnExecuteIOException = z;
        return this;
    }

    public HttpRequest setSuppressUserAgentSuffix(boolean z) {
        this.suppressUserAgentSuffix = z;
        return this;
    }

    public HttpRequest setThrowExceptionOnExecuteError(boolean z) {
        this.throwExceptionOnExecuteError = z;
        return this;
    }

    public HttpRequest setUnsuccessfulResponseHandler(HttpUnsuccessfulResponseHandler httpUnsuccessfulResponseHandler) {
        this.unsuccessfulResponseHandler = httpUnsuccessfulResponseHandler;
        return this;
    }

    public HttpRequest setUrl(GenericUrl genericUrl) {
        this.url = (GenericUrl) Preconditions.checkNotNull(genericUrl);
        return this;
    }
}
