package org.apache.http.impl.client.cache.memcached;

public interface KeyHashingScheme {
    String hash(String str);
}
