package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.math.BigInteger;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
@Beta
public final class UnsignedLong extends Number implements Comparable<UnsignedLong>, Serializable {
    public static final UnsignedLong MAX_VALUE = new UnsignedLong(-1);
    public static final UnsignedLong ONE = new UnsignedLong(1);
    private static final long UNSIGNED_MASK = Long.MAX_VALUE;
    public static final UnsignedLong ZERO = new UnsignedLong(0);
    private final long value;

    private UnsignedLong(long j) {
        this.value = j;
    }

    public static UnsignedLong asUnsigned(long j) {
        return new UnsignedLong(j);
    }

    public static UnsignedLong valueOf(String str) {
        return valueOf(str, 10);
    }

    public static UnsignedLong valueOf(String str, int i) {
        return asUnsigned(UnsignedLongs.parseUnsignedLong(str, i));
    }

    public static UnsignedLong valueOf(BigInteger bigInteger) {
        Preconditions.checkNotNull(bigInteger);
        boolean z = bigInteger.signum() >= 0 && bigInteger.bitLength() <= 64;
        Preconditions.checkArgument(z, "value (%s) is outside the range for an unsigned long value", bigInteger);
        return asUnsigned(bigInteger.longValue());
    }

    public UnsignedLong add(UnsignedLong unsignedLong) {
        Preconditions.checkNotNull(unsignedLong);
        return asUnsigned(this.value + unsignedLong.value);
    }

    public BigInteger bigIntegerValue() {
        BigInteger valueOf = BigInteger.valueOf(this.value & UNSIGNED_MASK);
        return this.value < 0 ? valueOf.setBit(63) : valueOf;
    }

    public int compareTo(UnsignedLong unsignedLong) {
        Preconditions.checkNotNull(unsignedLong);
        return UnsignedLongs.compare(this.value, unsignedLong.value);
    }

    public UnsignedLong divide(UnsignedLong unsignedLong) {
        Preconditions.checkNotNull(unsignedLong);
        return asUnsigned(UnsignedLongs.divide(this.value, unsignedLong.value));
    }

    public double doubleValue() {
        double d = (double) (this.value & UNSIGNED_MASK);
        return this.value < 0 ? d + 9.223372036854776E18d : d;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof UnsignedLong)) {
            return false;
        }
        return this.value == ((UnsignedLong) obj).value;
    }

    public float floatValue() {
        float f = (float) (this.value & UNSIGNED_MASK);
        return this.value < 0 ? f + 9.223372E18f : f;
    }

    public int hashCode() {
        return Longs.hashCode(this.value);
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return this.value;
    }

    public UnsignedLong multiply(UnsignedLong unsignedLong) {
        Preconditions.checkNotNull(unsignedLong);
        return asUnsigned(this.value * unsignedLong.value);
    }

    public UnsignedLong remainder(UnsignedLong unsignedLong) {
        Preconditions.checkNotNull(unsignedLong);
        return asUnsigned(UnsignedLongs.remainder(this.value, unsignedLong.value));
    }

    public UnsignedLong subtract(UnsignedLong unsignedLong) {
        Preconditions.checkNotNull(unsignedLong);
        return asUnsigned(this.value - unsignedLong.value);
    }

    public String toString() {
        return UnsignedLongs.toString(this.value);
    }

    public String toString(int i) {
        return UnsignedLongs.toString(this.value, i);
    }
}
