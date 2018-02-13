package com.google.api.client.googleapis.batch;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class BatchRequest {
    private GenericUrl batchUrl = new GenericUrl("https://www.googleapis.com/batch");
    private final HttpRequestFactory requestFactory;
    List<RequestInfo<?, ?>> requestInfos = new ArrayList();

    class BatchInterceptor implements HttpExecuteInterceptor {
        private HttpExecuteInterceptor originalInterceptor;

        BatchInterceptor(HttpExecuteInterceptor httpExecuteInterceptor) {
            this.originalInterceptor = httpExecuteInterceptor;
        }

        public void intercept(HttpRequest httpRequest) throws IOException {
            if (this.originalInterceptor != null) {
                this.originalInterceptor.intercept(httpRequest);
            }
            for (RequestInfo requestInfo : BatchRequest.this.requestInfos) {
                HttpExecuteInterceptor interceptor = requestInfo.request.getInterceptor();
                if (interceptor != null) {
                    interceptor.intercept(requestInfo.request);
                }
            }
        }
    }

    static class RequestInfo<T, E> {
        final BatchCallback<T, E> callback;
        final Class<T> dataClass;
        final Class<E> errorClass;
        final HttpRequest request;

        RequestInfo(BatchCallback<T, E> batchCallback, Class<T> cls, Class<E> cls2, HttpRequest httpRequest) {
            this.callback = batchCallback;
            this.dataClass = cls;
            this.errorClass = cls2;
            this.request = httpRequest;
        }
    }

    public BatchRequest(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer) {
        this.requestFactory = httpRequestInitializer == null ? httpTransport.createRequestFactory() : httpTransport.createRequestFactory(httpRequestInitializer);
    }

    private void sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void execute() throws java.io.IOException {
        /*
        r15 = this;
        r14 = 0;
        r2 = 0;
        r1 = 1;
        r0 = r15.requestInfos;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x007b;
    L_0x000b:
        r0 = r1;
    L_0x000c:
        com.google.api.client.util.Preconditions.checkState(r0);
        r0 = r15.requestFactory;
        r3 = r15.batchUrl;
        r6 = r0.buildPostRequest(r3, r14);
        r0 = new com.google.api.client.googleapis.batch.BatchRequest$BatchInterceptor;
        r3 = r6.getInterceptor();
        r0.<init>(r3);
        r6.setInterceptor(r0);
        r0 = r6.getNumberOfRetries();
        r7 = r6.getBackOffPolicy();
        if (r7 == 0) goto L_0x00e5;
    L_0x002d:
        r7.reset();
        r5 = r0;
    L_0x0031:
        if (r5 <= 0) goto L_0x007d;
    L_0x0033:
        r3 = r1;
    L_0x0034:
        r8 = new com.google.api.client.http.MultipartContent;
        r8.<init>();
        r0 = r8.getMediaType();
        r4 = "mixed";
        r0.setSubType(r4);
        r0 = r15.requestInfos;
        r9 = r0.iterator();
        r4 = r1;
    L_0x0049:
        r0 = r9.hasNext();
        if (r0 == 0) goto L_0x007f;
    L_0x004f:
        r0 = r9.next();
        r0 = (com.google.api.client.googleapis.batch.BatchRequest.RequestInfo) r0;
        r10 = new com.google.api.client.http.MultipartContent$Part;
        r11 = new com.google.api.client.http.HttpHeaders;
        r11.<init>();
        r11 = r11.setAcceptEncoding(r14);
        r12 = "Content-ID";
        r13 = java.lang.Integer.valueOf(r4);
        r11 = r11.set(r12, r13);
        r12 = new com.google.api.client.googleapis.batch.HttpRequestContent;
        r0 = r0.request;
        r12.<init>(r0);
        r10.<init>(r11, r12);
        r8.addPart(r10);
        r0 = r4 + 1;
        r4 = r0;
        goto L_0x0049;
    L_0x007b:
        r0 = r2;
        goto L_0x000c;
    L_0x007d:
        r3 = r2;
        goto L_0x0034;
    L_0x007f:
        r6.setContent(r8);
        r4 = r6.execute();
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b6 }
        r0.<init>();	 Catch:{ all -> 0x00b6 }
        r8 = "--";
        r0 = r0.append(r8);	 Catch:{ all -> 0x00b6 }
        r8 = r4.getMediaType();	 Catch:{ all -> 0x00b6 }
        r9 = "boundary";
        r8 = r8.getParameter(r9);	 Catch:{ all -> 0x00b6 }
        r0 = r0.append(r8);	 Catch:{ all -> 0x00b6 }
        r0 = r0.toString();	 Catch:{ all -> 0x00b6 }
        r8 = new com.google.api.client.googleapis.batch.BatchUnparsedResponse;	 Catch:{ all -> 0x00b6 }
        r9 = r4.getContent();	 Catch:{ all -> 0x00b6 }
        r10 = r15.requestInfos;	 Catch:{ all -> 0x00b6 }
        r8.<init>(r9, r0, r10, r3);	 Catch:{ all -> 0x00b6 }
    L_0x00ae:
        r0 = r8.hasNext;	 Catch:{ all -> 0x00b6 }
        if (r0 == 0) goto L_0x00bb;
    L_0x00b2:
        r8.parseNextResponse();	 Catch:{ all -> 0x00b6 }
        goto L_0x00ae;
    L_0x00b6:
        r0 = move-exception;
        r4.disconnect();
        throw r0;
    L_0x00bb:
        r4.disconnect();
        r0 = r8.unsuccessfulRequestInfos;
        r4 = r0.isEmpty();
        if (r4 != 0) goto L_0x00df;
    L_0x00c6:
        r15.requestInfos = r0;
        r0 = r8.backOffRequired;
        if (r0 == 0) goto L_0x00db;
    L_0x00cc:
        if (r7 == 0) goto L_0x00db;
    L_0x00ce:
        r8 = r7.getNextBackOffMillis();
        r10 = -1;
        r0 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r0 == 0) goto L_0x00db;
    L_0x00d8:
        r15.sleep(r8);
    L_0x00db:
        r0 = r5 + -1;
        if (r3 != 0) goto L_0x00e5;
    L_0x00df:
        r0 = r15.requestInfos;
        r0.clear();
        return;
    L_0x00e5:
        r5 = r0;
        goto L_0x0031;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.googleapis.batch.BatchRequest.execute():void");
    }

    public GenericUrl getBatchUrl() {
        return this.batchUrl;
    }

    public <T, E> BatchRequest queue(HttpRequest httpRequest, Class<T> cls, Class<E> cls2, BatchCallback<T, E> batchCallback) throws IOException {
        Preconditions.checkNotNull(httpRequest);
        Preconditions.checkNotNull(batchCallback);
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(cls2);
        this.requestInfos.add(new RequestInfo(batchCallback, cls, cls2, httpRequest));
        return this;
    }

    public BatchRequest setBatchUrl(GenericUrl genericUrl) {
        this.batchUrl = genericUrl;
        return this;
    }

    public int size() {
        return this.requestInfos.size();
    }
}
