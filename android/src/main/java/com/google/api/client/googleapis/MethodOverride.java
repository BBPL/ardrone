package com.google.api.client.googleapis;

import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.UrlEncodedContent;
import java.io.IOException;

public final class MethodOverride implements HttpExecuteInterceptor, HttpRequestInitializer {
    public static final String HEADER = "X-HTTP-Method-Override";
    static final int MAX_URL_LENGTH = 2048;
    private final boolean overrideAllMethods;

    public static final class Builder {
        private boolean overrideAllMethods;

        public MethodOverride build() {
            return new MethodOverride(this.overrideAllMethods);
        }

        public boolean getOverrideAllMethods() {
            return this.overrideAllMethods;
        }

        public Builder setOverrideAllMethods(boolean z) {
            this.overrideAllMethods = z;
            return this;
        }
    }

    public MethodOverride() {
        this(false);
    }

    MethodOverride(boolean z) {
        this.overrideAllMethods = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean overrideThisMethod(com.google.api.client.http.HttpRequest r6) throws java.io.IOException {
        /*
        r5 = this;
        r1 = 1;
        r0 = 0;
        r2 = r6.getRequestMethod();
        r3 = "POST";
        r3 = r2.equals(r3);
        if (r3 == 0) goto L_0x000f;
    L_0x000e:
        return r0;
    L_0x000f:
        r3 = "GET";
        r3 = r2.equals(r3);
        if (r3 == 0) goto L_0x0033;
    L_0x0017:
        r3 = r6.getUrl();
        r3 = r3.build();
        r3 = r3.length();
        r4 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        if (r3 > r4) goto L_0x0031;
    L_0x0027:
        r3 = r6.getTransport();
        r2 = r3.supportsMethod(r2);
        if (r2 != 0) goto L_0x000e;
    L_0x0031:
        r0 = r1;
        goto L_0x000e;
    L_0x0033:
        r3 = r5.overrideAllMethods;
        if (r3 == 0) goto L_0x0027;
    L_0x0037:
        r0 = r1;
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.googleapis.MethodOverride.overrideThisMethod(com.google.api.client.http.HttpRequest):boolean");
    }

    public void initialize(HttpRequest httpRequest) {
        httpRequest.setInterceptor(this);
    }

    public void intercept(HttpRequest httpRequest) throws IOException {
        if (overrideThisMethod(httpRequest)) {
            Object requestMethod = httpRequest.getRequestMethod();
            httpRequest.setRequestMethod("POST");
            httpRequest.getHeaders().set(HEADER, requestMethod);
            if (requestMethod.equals("GET")) {
                httpRequest.setContent(new UrlEncodedContent(httpRequest.getUrl()));
            } else if (httpRequest.getContent() == null) {
                httpRequest.setContent(new EmptyContent());
            }
        }
    }
}
