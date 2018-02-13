package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpVersion;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.ProtocolException;
import org.apache.http.UnsupportedHttpVersionException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

@Immutable
public class HttpService {
    private volatile ConnectionReuseStrategy connStrategy;
    private volatile HttpExpectationVerifier expectationVerifier;
    private volatile HttpRequestHandlerResolver handlerResolver;
    private volatile HttpParams params;
    private volatile HttpProcessor processor;
    private volatile HttpResponseFactory responseFactory;

    @Deprecated
    public HttpService(HttpProcessor httpProcessor, ConnectionReuseStrategy connectionReuseStrategy, HttpResponseFactory httpResponseFactory) {
        this.params = null;
        this.processor = null;
        this.handlerResolver = null;
        this.connStrategy = null;
        this.responseFactory = null;
        this.expectationVerifier = null;
        setHttpProcessor(httpProcessor);
        setConnReuseStrategy(connectionReuseStrategy);
        setResponseFactory(httpResponseFactory);
    }

    public HttpService(HttpProcessor httpProcessor, ConnectionReuseStrategy connectionReuseStrategy, HttpResponseFactory httpResponseFactory, HttpRequestHandlerResolver httpRequestHandlerResolver, HttpParams httpParams) {
        this(httpProcessor, connectionReuseStrategy, httpResponseFactory, httpRequestHandlerResolver, null, httpParams);
    }

    public HttpService(HttpProcessor httpProcessor, ConnectionReuseStrategy connectionReuseStrategy, HttpResponseFactory httpResponseFactory, HttpRequestHandlerResolver httpRequestHandlerResolver, HttpExpectationVerifier httpExpectationVerifier, HttpParams httpParams) {
        this.params = null;
        this.processor = null;
        this.handlerResolver = null;
        this.connStrategy = null;
        this.responseFactory = null;
        this.expectationVerifier = null;
        if (httpProcessor == null) {
            throw new IllegalArgumentException("HTTP processor may not be null");
        } else if (connectionReuseStrategy == null) {
            throw new IllegalArgumentException("Connection reuse strategy may not be null");
        } else if (httpResponseFactory == null) {
            throw new IllegalArgumentException("Response factory may not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.processor = httpProcessor;
            this.connStrategy = connectionReuseStrategy;
            this.responseFactory = httpResponseFactory;
            this.handlerResolver = httpRequestHandlerResolver;
            this.expectationVerifier = httpExpectationVerifier;
            this.params = httpParams;
        }
    }

    protected void doService(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        HttpRequestHandler httpRequestHandler = null;
        if (this.handlerResolver != null) {
            httpRequestHandler = this.handlerResolver.lookup(httpRequest.getRequestLine().getUri());
        }
        if (httpRequestHandler != null) {
            httpRequestHandler.handle(httpRequest, httpResponse, httpContext);
        } else {
            httpResponse.setStatusCode(501);
        }
    }

    public HttpParams getParams() {
        return this.params;
    }

    protected void handleException(HttpException httpException, HttpResponse httpResponse) {
        if (httpException instanceof MethodNotSupportedException) {
            httpResponse.setStatusCode(501);
        } else if (httpException instanceof UnsupportedHttpVersionException) {
            httpResponse.setStatusCode(505);
        } else if (httpException instanceof ProtocolException) {
            httpResponse.setStatusCode(400);
        } else {
            httpResponse.setStatusCode(500);
        }
        String message = httpException.getMessage();
        if (message == null) {
            message = httpException.toString();
        }
        HttpEntity byteArrayEntity = new ByteArrayEntity(EncodingUtils.getAsciiBytes(message));
        byteArrayEntity.setContentType("text/plain; charset=US-ASCII");
        httpResponse.setEntity(byteArrayEntity);
    }

    public void handleRequest(HttpServerConnection httpServerConnection, HttpContext httpContext) throws IOException, HttpException {
        HttpResponse newHttpResponse;
        httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, httpServerConnection);
        try {
            HttpRequest receiveRequestHeader = httpServerConnection.receiveRequestHeader();
            receiveRequestHeader.setParams(new DefaultedHttpParams(receiveRequestHeader.getParams(), this.params));
            if (receiveRequestHeader instanceof HttpEntityEnclosingRequest) {
                if (((HttpEntityEnclosingRequest) receiveRequestHeader).expectContinue()) {
                    newHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 100, httpContext);
                    newHttpResponse.setParams(new DefaultedHttpParams(newHttpResponse.getParams(), this.params));
                    if (this.expectationVerifier != null) {
                        try {
                            this.expectationVerifier.verify(receiveRequestHeader, newHttpResponse, httpContext);
                        } catch (HttpException e) {
                            HttpException httpException = e;
                            newHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0, 500, httpContext);
                            newHttpResponse.setParams(new DefaultedHttpParams(newHttpResponse.getParams(), this.params));
                            handleException(httpException, newHttpResponse);
                        }
                    }
                    if (newHttpResponse.getStatusLine().getStatusCode() < 200) {
                        httpServerConnection.sendResponseHeader(newHttpResponse);
                        httpServerConnection.flush();
                        httpServerConnection.receiveRequestEntity((HttpEntityEnclosingRequest) receiveRequestHeader);
                    }
                    httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, receiveRequestHeader);
                    if (newHttpResponse == null) {
                        newHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 200, httpContext);
                        newHttpResponse.setParams(new DefaultedHttpParams(newHttpResponse.getParams(), this.params));
                        this.processor.process(receiveRequestHeader, httpContext);
                        doService(receiveRequestHeader, newHttpResponse, httpContext);
                    }
                    if (receiveRequestHeader instanceof HttpEntityEnclosingRequest) {
                        EntityUtils.consume(((HttpEntityEnclosingRequest) receiveRequestHeader).getEntity());
                    }
                    httpContext.setAttribute(ExecutionContext.HTTP_RESPONSE, newHttpResponse);
                    this.processor.process(newHttpResponse, httpContext);
                    httpServerConnection.sendResponseHeader(newHttpResponse);
                    httpServerConnection.sendResponseEntity(newHttpResponse);
                    httpServerConnection.flush();
                    if (this.connStrategy.keepAlive(newHttpResponse, httpContext)) {
                        httpServerConnection.close();
                    }
                }
                httpServerConnection.receiveRequestEntity((HttpEntityEnclosingRequest) receiveRequestHeader);
                newHttpResponse = null;
                httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, receiveRequestHeader);
                if (newHttpResponse == null) {
                    newHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 200, httpContext);
                    newHttpResponse.setParams(new DefaultedHttpParams(newHttpResponse.getParams(), this.params));
                    this.processor.process(receiveRequestHeader, httpContext);
                    doService(receiveRequestHeader, newHttpResponse, httpContext);
                }
                if (receiveRequestHeader instanceof HttpEntityEnclosingRequest) {
                    EntityUtils.consume(((HttpEntityEnclosingRequest) receiveRequestHeader).getEntity());
                }
                httpContext.setAttribute(ExecutionContext.HTTP_RESPONSE, newHttpResponse);
                this.processor.process(newHttpResponse, httpContext);
                httpServerConnection.sendResponseHeader(newHttpResponse);
                httpServerConnection.sendResponseEntity(newHttpResponse);
                httpServerConnection.flush();
                if (this.connStrategy.keepAlive(newHttpResponse, httpContext)) {
                    httpServerConnection.close();
                }
            }
            newHttpResponse = null;
            httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, receiveRequestHeader);
            if (newHttpResponse == null) {
                newHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_1, 200, httpContext);
                newHttpResponse.setParams(new DefaultedHttpParams(newHttpResponse.getParams(), this.params));
                this.processor.process(receiveRequestHeader, httpContext);
                doService(receiveRequestHeader, newHttpResponse, httpContext);
            }
            if (receiveRequestHeader instanceof HttpEntityEnclosingRequest) {
                EntityUtils.consume(((HttpEntityEnclosingRequest) receiveRequestHeader).getEntity());
            }
        } catch (HttpException e2) {
            HttpException httpException2 = e2;
            newHttpResponse = this.responseFactory.newHttpResponse(HttpVersion.HTTP_1_0, 500, httpContext);
            newHttpResponse.setParams(new DefaultedHttpParams(newHttpResponse.getParams(), this.params));
            handleException(httpException2, newHttpResponse);
        }
        httpContext.setAttribute(ExecutionContext.HTTP_RESPONSE, newHttpResponse);
        this.processor.process(newHttpResponse, httpContext);
        httpServerConnection.sendResponseHeader(newHttpResponse);
        httpServerConnection.sendResponseEntity(newHttpResponse);
        httpServerConnection.flush();
        if (this.connStrategy.keepAlive(newHttpResponse, httpContext)) {
            httpServerConnection.close();
        }
    }

    @Deprecated
    public void setConnReuseStrategy(ConnectionReuseStrategy connectionReuseStrategy) {
        if (connectionReuseStrategy == null) {
            throw new IllegalArgumentException("Connection reuse strategy may not be null");
        }
        this.connStrategy = connectionReuseStrategy;
    }

    @Deprecated
    public void setExpectationVerifier(HttpExpectationVerifier httpExpectationVerifier) {
        this.expectationVerifier = httpExpectationVerifier;
    }

    @Deprecated
    public void setHandlerResolver(HttpRequestHandlerResolver httpRequestHandlerResolver) {
        this.handlerResolver = httpRequestHandlerResolver;
    }

    @Deprecated
    public void setHttpProcessor(HttpProcessor httpProcessor) {
        if (httpProcessor == null) {
            throw new IllegalArgumentException("HTTP processor may not be null");
        }
        this.processor = httpProcessor;
    }

    @Deprecated
    public void setParams(HttpParams httpParams) {
        this.params = httpParams;
    }

    @Deprecated
    public void setResponseFactory(HttpResponseFactory httpResponseFactory) {
        if (httpResponseFactory == null) {
            throw new IllegalArgumentException("Response factory may not be null");
        }
        this.responseFactory = httpResponseFactory;
    }
}
