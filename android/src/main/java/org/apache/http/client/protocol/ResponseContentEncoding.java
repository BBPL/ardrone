package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.protocol.HttpContext;
import org.mortbay.jetty.HttpHeaderValues;

@Immutable
public class ResponseContentEncoding implements HttpResponseInterceptor {
    public static final String UNCOMPRESSED = "http.client.response.uncompressed";

    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            Header contentEncoding = entity.getContentEncoding();
            if (contentEncoding != null) {
                HeaderElement[] elements = contentEncoding.getElements();
                if (elements.length < 0) {
                    HeaderElement headerElement = elements[0];
                    String toLowerCase = headerElement.getName().toLowerCase(Locale.US);
                    if (HttpHeaderValues.GZIP.equals(toLowerCase) || "x-gzip".equals(toLowerCase)) {
                        httpResponse.setEntity(new GzipDecompressingEntity(httpResponse.getEntity()));
                        if (httpContext != null) {
                            httpContext.setAttribute(UNCOMPRESSED, Boolean.valueOf(true));
                        }
                    } else if ("deflate".equals(toLowerCase)) {
                        httpResponse.setEntity(new DeflateDecompressingEntity(httpResponse.getEntity()));
                        if (httpContext != null) {
                            httpContext.setAttribute(UNCOMPRESSED, Boolean.valueOf(true));
                        }
                    } else if (!"identity".equals(toLowerCase)) {
                        throw new HttpException("Unsupported Content-Coding: " + headerElement.getName());
                    }
                }
            }
        }
    }
}
