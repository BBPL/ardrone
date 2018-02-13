package org.apache.http.client.fluent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

public class Response {
    private boolean consumed;
    private final HttpResponse response;

    Response(HttpResponse httpResponse) {
        this.response = httpResponse;
    }

    private void assertNotConsumed() {
        if (this.consumed) {
            throw new IllegalStateException("Response content has been already consumed");
        }
    }

    private void dispose() {
        if (!this.consumed) {
            try {
                EntityUtils.consume(this.response.getEntity());
            } catch (Exception e) {
            } finally {
                this.consumed = true;
            }
        }
    }

    public void discardContent() {
        dispose();
    }

    public <T> T handleResponse(ResponseHandler<T> responseHandler) throws ClientProtocolException, IOException {
        assertNotConsumed();
        try {
            T handleResponse = responseHandler.handleResponse(this.response);
            return handleResponse;
        } finally {
            dispose();
        }
    }

    public Content returnContent() throws ClientProtocolException, IOException {
        return (Content) handleResponse(new ContentResponseHandler());
    }

    public HttpResponse returnResponse() throws IOException {
        assertNotConsumed();
        try {
            HttpEntity entity = this.response.getEntity();
            if (entity != null) {
                this.response.setEntity(new ByteArrayEntity(EntityUtils.toByteArray(entity), ContentType.getOrDefault(entity)));
            }
            HttpResponse httpResponse = this.response;
            return httpResponse;
        } finally {
            this.consumed = true;
        }
    }

    public void saveContent(File file) throws IOException {
        assertNotConsumed();
        StatusLine statusLine = this.response.getStatusLine();
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        OutputStream fileOutputStream = new FileOutputStream(file);
        try {
            HttpEntity entity = this.response.getEntity();
            if (entity != null) {
                entity.writeTo(fileOutputStream);
            }
            this.consumed = true;
            fileOutputStream.close();
        } catch (Throwable th) {
            this.consumed = true;
            fileOutputStream.close();
        }
    }
}
