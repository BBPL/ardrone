package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class ImmutableHttpProcessor implements HttpProcessor {
    private final HttpRequestInterceptor[] requestInterceptors;
    private final HttpResponseInterceptor[] responseInterceptors;

    public ImmutableHttpProcessor(HttpRequestInterceptorList httpRequestInterceptorList, HttpResponseInterceptorList httpResponseInterceptorList) {
        int i;
        int i2 = 0;
        if (httpRequestInterceptorList != null) {
            int requestInterceptorCount = httpRequestInterceptorList.getRequestInterceptorCount();
            this.requestInterceptors = new HttpRequestInterceptor[requestInterceptorCount];
            for (i = 0; i < requestInterceptorCount; i++) {
                this.requestInterceptors[i] = httpRequestInterceptorList.getRequestInterceptor(i);
            }
        } else {
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (httpResponseInterceptorList != null) {
            i = httpResponseInterceptorList.getResponseInterceptorCount();
            this.responseInterceptors = new HttpResponseInterceptor[i];
            while (i2 < i) {
                this.responseInterceptors[i2] = httpResponseInterceptorList.getResponseInterceptor(i2);
                i2++;
            }
            return;
        }
        this.responseInterceptors = new HttpResponseInterceptor[0];
    }

    public ImmutableHttpProcessor(HttpRequestInterceptor[] httpRequestInterceptorArr) {
        this(httpRequestInterceptorArr, null);
    }

    public ImmutableHttpProcessor(HttpRequestInterceptor[] httpRequestInterceptorArr, HttpResponseInterceptor[] httpResponseInterceptorArr) {
        int i;
        int i2 = 0;
        if (httpRequestInterceptorArr != null) {
            int length = httpRequestInterceptorArr.length;
            this.requestInterceptors = new HttpRequestInterceptor[length];
            for (i = 0; i < length; i++) {
                this.requestInterceptors[i] = httpRequestInterceptorArr[i];
            }
        } else {
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (httpResponseInterceptorArr != null) {
            i = httpResponseInterceptorArr.length;
            this.responseInterceptors = new HttpResponseInterceptor[i];
            while (i2 < i) {
                this.responseInterceptors[i2] = httpResponseInterceptorArr[i2];
                i2++;
            }
            return;
        }
        this.responseInterceptors = new HttpResponseInterceptor[0];
    }

    public ImmutableHttpProcessor(HttpResponseInterceptor[] httpResponseInterceptorArr) {
        this(null, httpResponseInterceptorArr);
    }

    public void process(HttpRequest httpRequest, HttpContext httpContext) throws IOException, HttpException {
        for (HttpRequestInterceptor process : this.requestInterceptors) {
            process.process(httpRequest, httpContext);
        }
    }

    public void process(HttpResponse httpResponse, HttpContext httpContext) throws IOException, HttpException {
        for (HttpResponseInterceptor process : this.responseInterceptors) {
            process.process(httpResponse, httpContext);
        }
    }
}
