package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.OutputStream;

@Beta
public final class Funnels {

    private enum ByteArrayFunnel implements Funnel<byte[]> {
        INSTANCE;

        public void funnel(byte[] bArr, PrimitiveSink primitiveSink) {
            primitiveSink.putBytes(bArr);
        }

        public String toString() {
            return "Funnels.byteArrayFunnel()";
        }
    }

    private enum IntegerFunnel implements Funnel<Integer> {
        INSTANCE;

        public void funnel(Integer num, PrimitiveSink primitiveSink) {
            primitiveSink.putInt(num.intValue());
        }

        public String toString() {
            return "Funnels.integerFunnel()";
        }
    }

    private enum LongFunnel implements Funnel<Long> {
        INSTANCE;

        public void funnel(Long l, PrimitiveSink primitiveSink) {
            primitiveSink.putLong(l.longValue());
        }

        public String toString() {
            return "Funnels.longFunnel()";
        }
    }

    private static class SinkAsStream extends OutputStream {
        final PrimitiveSink sink;

        SinkAsStream(PrimitiveSink primitiveSink) {
            this.sink = (PrimitiveSink) Preconditions.checkNotNull(primitiveSink);
        }

        public String toString() {
            return "Funnels.asOutputStream(" + this.sink + ")";
        }

        public void write(int i) {
            this.sink.putByte((byte) i);
        }

        public void write(byte[] bArr) {
            this.sink.putBytes(bArr);
        }

        public void write(byte[] bArr, int i, int i2) {
            this.sink.putBytes(bArr, i, i2);
        }
    }

    private enum StringFunnel implements Funnel<CharSequence> {
        INSTANCE;

        public void funnel(CharSequence charSequence, PrimitiveSink primitiveSink) {
            primitiveSink.putString(charSequence);
        }

        public String toString() {
            return "Funnels.stringFunnel()";
        }
    }

    private Funnels() {
    }

    public static OutputStream asOutputStream(PrimitiveSink primitiveSink) {
        return new SinkAsStream(primitiveSink);
    }

    public static Funnel<byte[]> byteArrayFunnel() {
        return ByteArrayFunnel.INSTANCE;
    }

    public static Funnel<Integer> integerFunnel() {
        return IntegerFunnel.INSTANCE;
    }

    public static Funnel<Long> longFunnel() {
        return LongFunnel.INSTANCE;
    }

    public static Funnel<CharSequence> stringFunnel() {
        return StringFunnel.INSTANCE;
    }
}
