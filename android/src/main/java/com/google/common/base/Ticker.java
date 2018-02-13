package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
public abstract class Ticker {
    private static final Ticker SYSTEM_TICKER = new C05061();

    static final class C05061 extends Ticker {
        C05061() {
        }

        public long read() {
            return Platform.systemNanoTime();
        }
    }

    protected Ticker() {
    }

    public static Ticker systemTicker() {
        return SYSTEM_TICKER;
    }

    public abstract long read();
}
