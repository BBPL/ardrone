package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform {
    private static final ThreadLocal<char[]> DEST_TL = new C04901();

    static final class C04901 extends ThreadLocal<char[]> {
        C04901() {
        }

        protected char[] initialValue() {
            return new char[1024];
        }
    }

    private Platform() {
    }

    static char[] charBufferFromThreadLocal() {
        return (char[]) DEST_TL.get();
    }

    static CharMatcher precomputeCharMatcher(CharMatcher charMatcher) {
        return charMatcher.precomputedInternal();
    }

    static long systemNanoTime() {
        return System.nanoTime();
    }
}
