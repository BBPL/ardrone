package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpTrace extends HttpRequestBase {
    public static final String METHOD_NAME = "TRACE";

    public HttpTrace(String str) {
        setURI(URI.create(str));
    }

    public HttpTrace(URI uri) {
        setURI(uri);
    }

    public String getMethod() {
        return "TRACE";
    }
}
