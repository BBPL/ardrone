package org.apache.http.impl.client.cache.memcached;

import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SHA256KeyHashingScheme implements KeyHashingScheme {
    private static final Log log = LogFactory.getLog(SHA256KeyHashingScheme.class);

    private MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (Throwable e) {
            log.error("can't find SHA-256 implementation for cache key hashing");
            throw new MemcachedKeyHashingException(e);
        }
    }

    public String hash(String str) {
        MessageDigest digest = getDigest();
        digest.update(str.getBytes());
        return Hex.encodeHexString(digest.digest());
    }
}
