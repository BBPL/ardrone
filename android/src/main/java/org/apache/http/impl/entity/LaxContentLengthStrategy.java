package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.params.CoreProtocolPNames;

@Immutable
public class LaxContentLengthStrategy implements ContentLengthStrategy {
    private final int implicitLen;

    public LaxContentLengthStrategy() {
        this(-1);
    }

    public LaxContentLengthStrategy(int i) {
        this.implicitLen = i;
    }

    public long determineLength(HttpMessage httpMessage) throws HttpException {
        int i;
        if (httpMessage == null) {
            throw new IllegalArgumentException("HTTP message may not be null");
        }
        long j;
        boolean isParameterTrue = httpMessage.getParams().isParameterTrue(CoreProtocolPNames.STRICT_TRANSFER_ENCODING);
        Header firstHeader = httpMessage.getFirstHeader("Transfer-Encoding");
        if (firstHeader != null) {
            try {
                HeaderElement[] elements = firstHeader.getElements();
                if (isParameterTrue) {
                    i = 0;
                    while (i < elements.length) {
                        String name = elements[i].getName();
                        if (name == null || name.length() <= 0 || name.equalsIgnoreCase("chunked") || name.equalsIgnoreCase("identity")) {
                            i++;
                        } else {
                            throw new ProtocolException("Unsupported transfer encoding: " + name);
                        }
                    }
                }
                i = elements.length;
                if ("identity".equalsIgnoreCase(firstHeader.getValue())) {
                    j = -1;
                } else if (i > 0 && "chunked".equalsIgnoreCase(elements[i - 1].getName())) {
                    return -2;
                } else {
                    if (!isParameterTrue) {
                        return -1;
                    }
                    throw new ProtocolException("Chunk-encoding must be the last one applied");
                }
            } catch (Throwable e) {
                throw new ProtocolException("Invalid Transfer-Encoding header value: " + firstHeader, e);
            }
        } else if (httpMessage.getFirstHeader("Content-Length") == null) {
            return (long) this.implicitLen;
        } else {
            Header[] headers = httpMessage.getHeaders("Content-Length");
            if (!isParameterTrue || headers.length <= 1) {
                i = headers.length - 1;
                while (i >= 0) {
                    Header header = headers[i];
                    try {
                        j = Long.parseLong(header.getValue());
                        break;
                    } catch (NumberFormatException e2) {
                        if (isParameterTrue) {
                            throw new ProtocolException("Invalid content length: " + header.getValue());
                        }
                        i--;
                    }
                }
                j = -1;
                if (j < 0) {
                    return -1;
                }
            }
            throw new ProtocolException("Multiple content length headers");
        }
        return j;
    }
}
