package org.apache.http.impl.entity;

import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;

@Immutable
public class DisallowIdentityContentLengthStrategy implements ContentLengthStrategy {
    private final ContentLengthStrategy contentLengthStrategy;

    public DisallowIdentityContentLengthStrategy(ContentLengthStrategy contentLengthStrategy) {
        this.contentLengthStrategy = contentLengthStrategy;
    }

    public long determineLength(HttpMessage httpMessage) throws HttpException {
        long determineLength = this.contentLengthStrategy.determineLength(httpMessage);
        if (determineLength != -1) {
            return determineLength;
        }
        throw new ProtocolException("Identity transfer encoding cannot be used");
    }
}
