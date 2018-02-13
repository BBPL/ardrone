package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpHead extends HttpRequestBase {
    public static final String METHOD_NAME = "HEAD";

    public HttpHead(String str) {
        setURI(URI.create(str));
    }

    public HttpHead(URI uri) {
        setURI(uri);
    }

    public String getMethod() {
        return "HEAD";
    }
}
