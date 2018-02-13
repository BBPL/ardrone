package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpGet extends HttpRequestBase {
    public static final String METHOD_NAME = "GET";

    public HttpGet(String str) {
        setURI(URI.create(str));
    }

    public HttpGet(URI uri) {
        setURI(uri);
    }

    public String getMethod() {
        return "GET";
    }
}
