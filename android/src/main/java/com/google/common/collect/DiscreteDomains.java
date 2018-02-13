package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.math.BigInteger;

@GwtCompatible
@Beta
public final class DiscreteDomains {

    private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
        private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
        private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
        private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
        private static final long serialVersionUID = 0;

        private BigIntegerDomain() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public long distance(BigInteger bigInteger, BigInteger bigInteger2) {
            return bigInteger.subtract(bigInteger2).max(MIN_LONG).min(MAX_LONG).longValue();
        }

        public BigInteger next(BigInteger bigInteger) {
            return bigInteger.add(BigInteger.ONE);
        }

        public BigInteger previous(BigInteger bigInteger) {
            return bigInteger.subtract(BigInteger.ONE);
        }
    }

    private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
        private static final IntegerDomain INSTANCE = new IntegerDomain();
        private static final long serialVersionUID = 0;

        private IntegerDomain() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public long distance(Integer num, Integer num2) {
            return ((long) num2.intValue()) - ((long) num.intValue());
        }

        public Integer maxValue() {
            return Integer.valueOf(Integer.MAX_VALUE);
        }

        public Integer minValue() {
            return Integer.valueOf(Integer.MIN_VALUE);
        }

        public Integer next(Integer num) {
            int intValue = num.intValue();
            return intValue == Integer.MAX_VALUE ? null : Integer.valueOf(intValue + 1);
        }

        public Integer previous(Integer num) {
            int intValue = num.intValue();
            return intValue == Integer.MIN_VALUE ? null : Integer.valueOf(intValue - 1);
        }
    }

    private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
        private static final LongDomain INSTANCE = new LongDomain();
        private static final long serialVersionUID = 0;

        private LongDomain() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public long distance(Long l, Long l2) {
            long longValue = l2.longValue() - l.longValue();
            return (l2.longValue() <= l.longValue() || longValue >= 0) ? (l2.longValue() >= l.longValue() || longValue <= 0) ? longValue : Long.MIN_VALUE : Long.MAX_VALUE;
        }

        public Long maxValue() {
            return Long.valueOf(Long.MAX_VALUE);
        }

        public Long minValue() {
            return Long.valueOf(Long.MIN_VALUE);
        }

        public Long next(Long l) {
            long longValue = l.longValue();
            return longValue == Long.MAX_VALUE ? null : Long.valueOf(longValue + 1);
        }

        public Long previous(Long l) {
            long longValue = l.longValue();
            return longValue == Long.MIN_VALUE ? null : Long.valueOf(longValue - 1);
        }
    }

    private DiscreteDomains() {
    }

    static DiscreteDomain<BigInteger> bigIntegers() {
        return BigIntegerDomain.INSTANCE;
    }

    public static DiscreteDomain<Integer> integers() {
        return IntegerDomain.INSTANCE;
    }

    public static DiscreteDomain<Long> longs() {
        return LongDomain.INSTANCE;
    }
}
