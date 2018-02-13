package com.google.api.client.util.escape;

final class Platform {
    private static final ThreadLocal<char[]> DEST_TL = new C04691();

    static final class C04691 extends ThreadLocal<char[]> {
        C04691() {
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
}
