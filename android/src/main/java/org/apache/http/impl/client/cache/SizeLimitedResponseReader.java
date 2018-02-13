package org.apache.http.impl.client.cache;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.cache.InputLimit;
import org.apache.http.client.cache.Resource;
import org.apache.http.client.cache.ResourceFactory;
import org.apache.http.message.BasicHttpResponse;

@NotThreadSafe
class SizeLimitedResponseReader {
    private boolean consumed;
    private InputStream instream;
    private InputLimit limit;
    private final long maxResponseSizeBytes;
    private final HttpRequest request;
    private Resource resource;
    private final ResourceFactory resourceFactory;
    private final HttpResponse response;

    public SizeLimitedResponseReader(ResourceFactory resourceFactory, long j, HttpRequest httpRequest, HttpResponse httpResponse) {
        this.resourceFactory = resourceFactory;
        this.maxResponseSizeBytes = j;
        this.request = httpRequest;
        this.response = httpResponse;
    }

    private void doConsume() throws IOException {
        ensureNotConsumed();
        this.consumed = true;
        this.limit = new InputLimit(this.maxResponseSizeBytes);
        HttpEntity entity = this.response.getEntity();
        if (entity != null) {
            String uri = this.request.getRequestLine().getUri();
            this.instream = entity.getContent();
            this.resource = this.resourceFactory.generate(uri, this.instream, this.limit);
        }
    }

    private void ensureConsumed() {
        if (!this.consumed) {
            throw new IllegalStateException("Response has not been consumed");
        }
    }

    private void ensureNotConsumed() {
        if (this.consumed) {
            throw new IllegalStateException("Response has already been consumed");
        }
    }

    HttpResponse getReconstructedResponse() throws IOException {
        ensureConsumed();
        HttpResponse basicHttpResponse = new BasicHttpResponse(this.response.getStatusLine());
        basicHttpResponse.setHeaders(this.response.getAllHeaders());
        HttpEntity combinedEntity = new CombinedEntity(this.resource, this.instream);
        HttpEntity entity = this.response.getEntity();
        if (entity != null) {
            combinedEntity.setContentType(entity.getContentType());
            combinedEntity.setContentEncoding(entity.getContentEncoding());
            combinedEntity.setChunked(entity.isChunked());
        }
        basicHttpResponse.setEntity(combinedEntity);
        return basicHttpResponse;
    }

    Resource getResource() {
        ensureConsumed();
        return this.resource;
    }

    boolean isLimitReached() {
        ensureConsumed();
        return this.limit.isReached();
    }

    protected void readResponse() throws IOException {
        if (!this.consumed) {
            doConsume();
        }
    }
}
