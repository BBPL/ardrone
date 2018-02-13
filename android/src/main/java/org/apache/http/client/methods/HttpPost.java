package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPost extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "POST";

    public HttpPost(String str) {
        setURI(URI.create(str));
    }

    public HttpPost(URI uri) {
        setURI(uri);
    }

    public String getMethod() {
        return "POST";
    }
}
