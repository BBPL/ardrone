package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class BasicHttpEntityEnclosingRequest extends BasicHttpRequest implements HttpEntityEnclosingRequest {
    private HttpEntity entity;

    public BasicHttpEntityEnclosingRequest(String str, String str2) {
        super(str, str2);
    }

    public BasicHttpEntityEnclosingRequest(String str, String str2, ProtocolVersion protocolVersion) {
        super(str, str2, protocolVersion);
    }

    public BasicHttpEntityEnclosingRequest(RequestLine requestLine) {
        super(requestLine);
    }

    public boolean expectContinue() {
        Header firstHeader = getFirstHeader("Expect");
        return firstHeader != null && "100-continue".equalsIgnoreCase(firstHeader.getValue());
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public void setEntity(HttpEntity httpEntity) {
        this.entity = httpEntity;
    }
}
