package org.apache.http.impl.entity;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.io.SessionInputBuffer;

@Immutable
public class EntityDeserializer {
    private final ContentLengthStrategy lenStrategy;

    public EntityDeserializer(ContentLengthStrategy contentLengthStrategy) {
        if (contentLengthStrategy == null) {
            throw new IllegalArgumentException("Content length strategy may not be null");
        }
        this.lenStrategy = contentLengthStrategy;
    }

    public HttpEntity deserialize(SessionInputBuffer sessionInputBuffer, HttpMessage httpMessage) throws HttpException, IOException {
        if (sessionInputBuffer == null) {
            throw new IllegalArgumentException("Session input buffer may not be null");
        } else if (httpMessage != null) {
            return doDeserialize(sessionInputBuffer, httpMessage);
        } else {
            throw new IllegalArgumentException("HTTP message may not be null");
        }
    }

    protected BasicHttpEntity doDeserialize(SessionInputBuffer sessionInputBuffer, HttpMessage httpMessage) throws HttpException, IOException {
        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        long determineLength = this.lenStrategy.determineLength(httpMessage);
        if (determineLength == -2) {
            basicHttpEntity.setChunked(true);
            basicHttpEntity.setContentLength(-1);
            basicHttpEntity.setContent(new ChunkedInputStream(sessionInputBuffer));
        } else if (determineLength == -1) {
            basicHttpEntity.setChunked(false);
            basicHttpEntity.setContentLength(-1);
            basicHttpEntity.setContent(new IdentityInputStream(sessionInputBuffer));
        } else {
            basicHttpEntity.setChunked(false);
            basicHttpEntity.setContentLength(determineLength);
            basicHttpEntity.setContent(new ContentLengthInputStream(sessionInputBuffer, determineLength));
        }
        Header firstHeader = httpMessage.getFirstHeader("Content-Type");
        if (firstHeader != null) {
            basicHttpEntity.setContentType(firstHeader);
        }
        firstHeader = httpMessage.getFirstHeader("Content-Encoding");
        if (firstHeader != null) {
            basicHttpEntity.setContentEncoding(firstHeader);
        }
        return basicHttpEntity;
    }
}
