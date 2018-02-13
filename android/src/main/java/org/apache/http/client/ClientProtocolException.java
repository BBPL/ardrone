package org.apache.http.client;

import java.io.IOException;
import org.apache.http.annotation.Immutable;

@Immutable
public class ClientProtocolException extends IOException {
    private static final long serialVersionUID = -5596590843227115865L;

    public ClientProtocolException(String str) {
        super(str);
    }

    public ClientProtocolException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public ClientProtocolException(Throwable th) {
        initCause(th);
    }
}
