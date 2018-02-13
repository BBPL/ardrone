package org.apache.http.impl.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;

@ThreadSafe
public class BasicCredentialsProvider implements CredentialsProvider {
    private final ConcurrentHashMap<AuthScope, Credentials> credMap = new ConcurrentHashMap();

    private static Credentials matchCredentials(Map<AuthScope, Credentials> map, AuthScope authScope) {
        Credentials credentials = (Credentials) map.get(authScope);
        if (credentials != null) {
            return credentials;
        }
        int i = -1;
        Object obj = null;
        for (AuthScope authScope2 : map.keySet()) {
            int match = authScope.match(authScope2);
            if (match > i) {
                obj = authScope2;
                i = match;
            }
        }
        return obj != null ? (Credentials) map.get(obj) : credentials;
    }

    public void clear() {
        this.credMap.clear();
    }

    public Credentials getCredentials(AuthScope authScope) {
        if (authScope != null) {
            return matchCredentials(this.credMap, authScope);
        }
        throw new IllegalArgumentException("Authentication scope may not be null");
    }

    public void setCredentials(AuthScope authScope, Credentials credentials) {
        if (authScope == null) {
            throw new IllegalArgumentException("Authentication scope may not be null");
        }
        this.credMap.put(authScope, credentials);
    }

    public String toString() {
        return this.credMap.toString();
    }
}
