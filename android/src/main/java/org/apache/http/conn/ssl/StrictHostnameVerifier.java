package org.apache.http.conn.ssl;

import javax.net.ssl.SSLException;
import org.apache.http.annotation.Immutable;
import org.apache.sanselan.SanselanConstants;

@Immutable
public class StrictHostnameVerifier extends AbstractVerifier {
    public final String toString() {
        return SanselanConstants.PARAM_KEY_STRICT;
    }

    public final void verify(String str, String[] strArr, String[] strArr2) throws SSLException {
        verify(str, strArr, strArr2, true);
    }
}
