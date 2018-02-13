package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.math.BigInteger;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
@Beta
public final class UnsignedInteger extends Number implements Comparable<UnsignedInteger> {
    public static final UnsignedInteger MAX_VALUE = asUnsigned(-1);
    public static final UnsignedInteger ONE = asUnsigned(1);
    public static final UnsignedInteger ZERO = asUnsigned(0);
    private final int value;

    private UnsignedInteger(int i) {
        this.value = i & -1;
    }

    public static UnsignedInteger asUnsigned(int i) {
        return new UnsignedInteger(i);
    }

    public static UnsignedInteger valueOf(long j) {
        Preconditions.checkArgument((4294967295L & j) == j, "value (%s) is outside the range for an unsigned integer value", Long.valueOf(j));
        return asUnsigned((int) j);
    }

    public static UnsignedInteger valueOf(String str) {
        return valueOf(str, 10);
    }

    public static UnsignedInteger valueOf(String str, int i) {
        return asUnsigned(UnsignedInts.parseUnsignedInt(str, i));
    }

    public static UnsignedInteger valueOf(BigInteger bigInteger) {
        Preconditions.checkNotNull(bigInteger);
        boolean z = bigInteger.signum() >= 0 && bigInteger.bitLength() <= 32;
        Preconditions.checkArgument(z, "value (%s) is outside the range for an unsigned integer value", bigInteger);
        return asUnsigned(bigInteger.intValue());
    }

    public UnsignedInteger add(UnsignedInteger unsignedInteger) {
        Preconditions.checkNotNull(unsignedInteger);
        return asUnsigned(this.value + unsignedInteger.value);
    }

    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(longValue());
    }

    public int compareTo(UnsignedInteger unsignedInteger) {
        Preconditions.checkNotNull(unsignedInteger);
        return UnsignedInts.compare(this.value, unsignedInteger.value);
    }

    public UnsignedInteger divide(UnsignedInteger unsignedInteger) {
        Preconditions.checkNotNull(unsignedInteger);
        return asUnsigned(UnsignedInts.divide(this.value, unsignedInteger.value));
    }

    public double doubleValue() {
        return (double) longValue();
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof UnsignedInteger)) {
            return false;
        }
        return this.value == ((UnsignedInteger) obj).value;
    }

    public float floatValue() {
        return (float) longValue();
    }

    public int hashCode() {
        return this.value;
    }

    public int intValue() {
        return this.value;
    }

    public long longValue() {
        return UnsignedInts.toLong(this.value);
    }

    @GwtIncompatible("Does not truncate correctly")
    public UnsignedInteger multiply(UnsignedInteger unsignedInteger) {
        Preconditions.checkNotNull(unsignedInteger);
        return asUnsigned(this.value * unsignedInteger.value);
    }

    public UnsignedInteger remainder(UnsignedInteger unsignedInteger) {
        Preconditions.checkNotNull(unsignedInteger);
        return asUnsigned(UnsignedInts.remainder(this.value, unsignedInteger.value));
    }

    public UnsignedInteger subtract(UnsignedInteger unsignedInteger) {
        Preconditions.checkNotNull(unsignedInteger);
        return asUnsigned(this.value - unsignedInteger.value);
    }

    public String toString() {
        return toString(10);
    }

    public String toString(int i) {
        return UnsignedInts.toString(this.value, i);
    }
}
