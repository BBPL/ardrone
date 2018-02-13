package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;

@Immutable
public class ResponseConnControl implements HttpResponseInterceptor {
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 400 || statusCode == 408 || statusCode == 411 || statusCode == 413 || statusCode == 414 || statusCode == 503 || statusCode == 501) {
                httpResponse.setHeader("Connection", HTTP.CONN_CLOSE);
                return;
            }
            Header firstHeader = httpResponse.getFirstHeader("Connection");
            if (firstHeader == null || !HTTP.CONN_CLOSE.equalsIgnoreCase(firstHeader.getValue())) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    ProtocolVersion protocolVersion = httpResponse.getStatusLine().getProtocolVersion();
                    if (entity.getContentLength() < 0 && (!entity.isChunked() || protocolVersion.lessEquals(HttpVersion.HTTP_1_0))) {
                        httpResponse.setHeader("Connection", HTTP.CONN_CLOSE);
                        return;
                    }
                }
                HttpRequest httpRequest = (HttpRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
                if (httpRequest != null) {
                    Header firstHeader2 = httpRequest.getFirstHeader("Connection");
                    if (firstHeader2 != null) {
                        httpResponse.setHeader("Connection", firstHeader2.getValue());
                    } else if (httpRequest.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                        httpResponse.setHeader("Connection", HTTP.CONN_CLOSE);
                    }
                }
            }
        }
    }
}
