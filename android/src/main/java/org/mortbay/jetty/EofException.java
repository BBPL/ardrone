package org.mortbay.jetty;

import java.io.EOFException;

public class EofException extends EOFException {
    public EofException(String str) {
        super(str);
    }

    public EofException(Throwable th) {
        initCause(th);
    }
}
