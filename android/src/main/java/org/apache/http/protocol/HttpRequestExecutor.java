package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.params.CoreProtocolPNames;

@Immutable
public class HttpRequestExecutor {
    private static final void closeConnection(HttpClientConnection httpClientConnection) {
        try {
            httpClientConnection.close();
        } catch (IOException e) {
        }
    }

    protected boolean canResponseHaveBody(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!"HEAD".equalsIgnoreCase(httpRequest.getRequestLine().getMethod())) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (!(statusCode < 200 || statusCode == 204 || statusCode == 304 || statusCode == 205)) {
                return true;
            }
        }
        return false;
    }

    protected HttpResponse doReceiveResponse(HttpRequest httpRequest, HttpClientConnection httpClientConnection, HttpContext httpContext) throws HttpException, IOException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpClientConnection == null) {
            throw new IllegalArgumentException("HTTP connection may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            HttpResponse httpResponse = null;
            int i = 0;
            while (true) {
                if (httpResponse != null && r0 >= 200) {
                    return httpResponse;
                }
                httpResponse = httpClientConnection.receiveResponseHeader();
                if (canResponseHaveBody(httpRequest, httpResponse)) {
                    httpClientConnection.receiveResponseEntity(httpResponse);
                }
                i = httpResponse.getStatusLine().getStatusCode();
            }
        }
    }

    protected HttpResponse doSendRequest(HttpRequest httpRequest, HttpClientConnection httpClientConnection, HttpContext httpContext) throws IOException, HttpException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpClientConnection == null) {
            throw new IllegalArgumentException("HTTP connection may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            HttpResponse receiveResponseHeader;
            httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, httpClientConnection);
            httpContext.setAttribute(ExecutionContext.HTTP_REQ_SENT, Boolean.FALSE);
            httpClientConnection.sendRequestHeader(httpRequest);
            if (httpRequest instanceof HttpEntityEnclosingRequest) {
                Object obj;
                ProtocolVersion protocolVersion = httpRequest.getRequestLine().getProtocolVersion();
                if (((HttpEntityEnclosingRequest) httpRequest).expectContinue() && !protocolVersion.lessEquals(HttpVersion.HTTP_1_0)) {
                    httpClientConnection.flush();
                    if (httpClientConnection.isResponseAvailable(httpRequest.getParams().getIntParameter(CoreProtocolPNames.WAIT_FOR_CONTINUE, 2000))) {
                        receiveResponseHeader = httpClientConnection.receiveResponseHeader();
                        if (canResponseHaveBody(httpRequest, receiveResponseHeader)) {
                            httpClientConnection.receiveResponseEntity(receiveResponseHeader);
                        }
                        int statusCode = receiveResponseHeader.getStatusLine().getStatusCode();
                        if (statusCode >= 200) {
                            obj = null;
                        } else if (statusCode != 100) {
                            throw new ProtocolException("Unexpected response: " + receiveResponseHeader.getStatusLine());
                        } else {
                            receiveResponseHeader = null;
                            obj = 1;
                        }
                        if (obj != null) {
                            httpClientConnection.sendRequestEntity((HttpEntityEnclosingRequest) httpRequest);
                        }
                    }
                }
                receiveResponseHeader = null;
                int i = 1;
                if (obj != null) {
                    httpClientConnection.sendRequestEntity((HttpEntityEnclosingRequest) httpRequest);
                }
            } else {
                receiveResponseHeader = null;
            }
            httpClientConnection.flush();
            httpContext.setAttribute(ExecutionContext.HTTP_REQ_SENT, Boolean.TRUE);
            return receiveResponseHeader;
        }
    }

    public HttpResponse execute(HttpRequest httpRequest, HttpClientConnection httpClientConnection, HttpContext httpContext) throws IOException, HttpException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpClientConnection == null) {
            throw new IllegalArgumentException("Client connection may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            try {
                HttpResponse doSendRequest = doSendRequest(httpRequest, httpClientConnection, httpContext);
                if (doSendRequest == null) {
                    doSendRequest = doReceiveResponse(httpRequest, httpClientConnection, httpContext);
                }
                return doSendRequest;
            } catch (IOException e) {
                closeConnection(httpClientConnection);
                throw e;
            } catch (HttpException e2) {
                closeConnection(httpClientConnection);
                throw e2;
            } catch (RuntimeException e3) {
                closeConnection(httpClientConnection);
                throw e3;
            }
        }
    }

    public void postProcess(HttpResponse httpResponse, HttpProcessor httpProcessor, HttpContext httpContext) throws HttpException, IOException {
        if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        } else if (httpProcessor == null) {
            throw new IllegalArgumentException("HTTP processor may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            httpContext.setAttribute(ExecutionContext.HTTP_RESPONSE, httpResponse);
            httpProcessor.process(httpResponse, httpContext);
        }
    }

    public void preProcess(HttpRequest httpRequest, HttpProcessor httpProcessor, HttpContext httpContext) throws HttpException, IOException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpProcessor == null) {
            throw new IllegalArgumentException("HTTP processor may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, httpRequest);
            httpProcessor.process(httpRequest, httpContext);
        }
    }
}
