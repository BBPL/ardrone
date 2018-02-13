package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;

@Immutable
public class BasicRequestLine implements RequestLine, Cloneable, Serializable {
    private static final long serialVersionUID = 2810581718468737193L;
    private final String method;
    private final ProtocolVersion protoversion;
    private final String uri;

    public BasicRequestLine(String str, String str2, ProtocolVersion protocolVersion) {
        if (str == null) {
            throw new IllegalArgumentException("Method must not be null.");
        } else if (str2 == null) {
            throw new IllegalArgumentException("URI must not be null.");
        } else if (protocolVersion == null) {
            throw new IllegalArgumentException("Protocol version must not be null.");
        } else {
            this.method = str;
            this.uri = str2;
            this.protoversion = protocolVersion;
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMethod() {
        return this.method;
    }

    public ProtocolVersion getProtocolVersion() {
        return this.protoversion;
    }

    public String getUri() {
        return this.uri;
    }

    public String toString() {
        return BasicLineFormatter.DEFAULT.formatRequestLine(null, (RequestLine) this).toString();
    }
}
