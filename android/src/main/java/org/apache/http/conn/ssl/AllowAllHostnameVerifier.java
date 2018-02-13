package org.apache.http.conn.ssl;

import org.apache.http.annotation.Immutable;

@Immutable
public class AllowAllHostnameVerifier extends AbstractVerifier {
    public final String toString() {
        return "ALLOW_ALL";
    }

    public final void verify(String str, String[] strArr, String[] strArr2) {
    }
}
