package org.apache.http.client.methods;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpOptions extends HttpRequestBase {
    public static final String METHOD_NAME = "OPTIONS";

    public HttpOptions(String str) {
        setURI(URI.create(str));
    }

    public HttpOptions(URI uri) {
        setURI(uri);
    }

    public Set<String> getAllowedMethods(HttpResponse httpResponse) {
        if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        HeaderIterator headerIterator = httpResponse.headerIterator("Allow");
        Set<String> hashSet = new HashSet();
        while (headerIterator.hasNext()) {
            for (HeaderElement name : headerIterator.nextHeader().getElements()) {
                hashSet.add(name.getName());
            }
        }
        return hashSet;
    }

    public String getMethod() {
        return "OPTIONS";
    }
}
