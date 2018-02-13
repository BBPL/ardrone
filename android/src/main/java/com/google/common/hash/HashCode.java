package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.security.MessageDigest;

@Beta
public abstract class HashCode {
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    HashCode() {
    }

    public abstract byte[] asBytes();

    public abstract int asInt();

    public abstract long asLong();

    public abstract int bits();

    public boolean equals(Object obj) {
        if (!(obj instanceof HashCode)) {
            return false;
        }
        return MessageDigest.isEqual(asBytes(), ((HashCode) obj).asBytes());
    }

    public int hashCode() {
        return asInt();
    }

    public String toString() {
        byte[] asBytes = asBytes();
        StringBuilder stringBuilder = new StringBuilder(asBytes.length * 2);
        for (byte b : asBytes) {
            stringBuilder.append(hexDigits[(b >> 4) & 15]).append(hexDigits[b & 15]);
        }
        return stringBuilder.toString();
    }

    public int writeBytesTo(byte[] bArr, int i, int i2) {
        int min = Ints.min(i2, asBytes().length);
        Preconditions.checkPositionIndexes(i, i + min, bArr.length);
        System.arraycopy(r0, 0, bArr, i, min);
        return min;
    }
}
