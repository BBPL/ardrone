package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPut extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "PUT";

    public HttpPut(String str) {
        setURI(URI.create(str));
    }

    public HttpPut(URI uri) {
        setURI(uri);
    }

    public String getMethod() {
        return "PUT";
    }
}
