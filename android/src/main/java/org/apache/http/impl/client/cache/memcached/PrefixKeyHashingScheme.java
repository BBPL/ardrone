package org.apache.http.impl.client.cache.memcached;

public class PrefixKeyHashingScheme implements KeyHashingScheme {
    private KeyHashingScheme backingScheme;
    private String prefix;

    public PrefixKeyHashingScheme(String str, KeyHashingScheme keyHashingScheme) {
        this.prefix = str;
        this.backingScheme = keyHashingScheme;
    }

    public String hash(String str) {
        return this.prefix + this.backingScheme.hash(str);
    }
}
