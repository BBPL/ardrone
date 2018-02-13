package org.apache.sanselan.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.mortbay.util.URIUtil;

public class RationalNumber extends Number {
    private static final NumberFormat nf = DecimalFormat.getInstance();
    private static final long serialVersionUID = -1;
    public final int divisor;
    public final int numerator;

    public RationalNumber(int i, int i2) {
        this.numerator = i;
        this.divisor = i2;
    }

    public static final RationalNumber factoryMethod(long j, long j2) {
        if (j > 2147483647L || j < -2147483648L || j2 > 2147483647L || j2 < -2147483648L) {
            while (true) {
                if ((j > 2147483647L || j < -2147483648L || j2 > 2147483647L || j2 < -2147483648L) && Math.abs(j) > 1 && Math.abs(j2) > 1) {
                    j >>= 1;
                    j2 >>= 1;
                }
            }
            if (j2 == 0) {
                throw new NumberFormatException("Invalid value, numerator: " + j + ", divisor: " + j2);
            }
        }
        long gcd = gcd(j, j2);
        return new RationalNumber((int) (j / gcd), (int) (j2 / gcd));
    }

    private static long gcd(long j, long j2) {
        return j2 == 0 ? j : gcd(j2, j % j2);
    }

    public double doubleValue() {
        return ((double) this.numerator) / ((double) this.divisor);
    }

    public float floatValue() {
        return ((float) this.numerator) / ((float) this.divisor);
    }

    public int intValue() {
        return this.numerator / this.divisor;
    }

    public boolean isValid() {
        return this.divisor != 0;
    }

    public long longValue() {
        return ((long) this.numerator) / ((long) this.divisor);
    }

    public RationalNumber negate() {
        return new RationalNumber(-this.numerator, this.divisor);
    }

    public String toDisplayString() {
        if (this.numerator % this.divisor == 0) {
            return (this.numerator / this.divisor);
        }
        NumberFormat instance = DecimalFormat.getInstance();
        instance.setMaximumFractionDigits(3);
        return instance.format(((double) this.numerator) / ((double) this.divisor));
    }

    public String toString() {
        return this.divisor == 0 ? "Invalid rational (" + this.numerator + URIUtil.SLASH + this.divisor + ")" : this.numerator % this.divisor == 0 ? nf.format((long) (this.numerator / this.divisor)) : this.numerator + URIUtil.SLASH + this.divisor + " (" + nf.format(((double) this.numerator) / ((double) this.divisor)) + ")";
    }
}
