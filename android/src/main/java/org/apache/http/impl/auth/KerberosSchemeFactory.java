package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.params.HttpParams;

@Immutable
public class KerberosSchemeFactory implements AuthSchemeFactory {
    private final boolean stripPort;

    public KerberosSchemeFactory() {
        this(false);
    }

    public KerberosSchemeFactory(boolean z) {
        this.stripPort = z;
    }

    public boolean isStripPort() {
        return this.stripPort;
    }

    public AuthScheme newInstance(HttpParams httpParams) {
        return new KerberosScheme(this.stripPort);
    }
}
