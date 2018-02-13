package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;

@Immutable
public class StrictContentLengthStrategy implements ContentLengthStrategy {
    private final int implicitLen;

    public StrictContentLengthStrategy() {
        this(-1);
    }

    public StrictContentLengthStrategy(int i) {
        this.implicitLen = i;
    }

    public long determineLength(HttpMessage httpMessage) throws HttpException {
        if (httpMessage == null) {
            throw new IllegalArgumentException("HTTP message may not be null");
        }
        Header firstHeader = httpMessage.getFirstHeader("Transfer-Encoding");
        if (firstHeader != null) {
            String value = firstHeader.getValue();
            if ("chunked".equalsIgnoreCase(value)) {
                if (!httpMessage.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                    return -2;
                }
                throw new ProtocolException("Chunked transfer encoding not allowed for " + httpMessage.getProtocolVersion());
            } else if ("identity".equalsIgnoreCase(value)) {
                return -1;
            } else {
                throw new ProtocolException("Unsupported transfer encoding: " + value);
            }
        }
        firstHeader = httpMessage.getFirstHeader("Content-Length");
        if (firstHeader == null) {
            return (long) this.implicitLen;
        }
        String value2 = firstHeader.getValue();
        try {
            long parseLong = Long.parseLong(value2);
            if (parseLong >= 0) {
                return parseLong;
            }
            throw new ProtocolException("Negative content length: " + value2);
        } catch (NumberFormatException e) {
            throw new ProtocolException("Invalid content length: " + value2);
        }
    }
}
