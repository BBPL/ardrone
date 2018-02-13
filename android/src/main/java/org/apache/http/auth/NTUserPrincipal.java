package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.LangUtils;

@Immutable
public class NTUserPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -6870169797924406894L;
    private final String domain;
    private final String ntname;
    private final String username;

    public NTUserPrincipal(String str, String str2) {
        if (str2 == null) {
            throw new IllegalArgumentException("User name may not be null");
        }
        this.username = str2;
        if (str != null) {
            this.domain = str.toUpperCase(Locale.ENGLISH);
        } else {
            this.domain = null;
        }
        if (this.domain == null || this.domain.length() <= 0) {
            this.ntname = this.username;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.domain);
        stringBuilder.append('/');
        stringBuilder.append(this.username);
        this.ntname = stringBuilder.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        if (r2 != r3) goto L_0x0004;
    L_0x0002:
        r0 = 1;
    L_0x0003:
        return r0;
    L_0x0004:
        r0 = r3 instanceof org.apache.http.auth.NTUserPrincipal;
        if (r0 == 0) goto L_0x001e;
    L_0x0008:
        r3 = (org.apache.http.auth.NTUserPrincipal) r3;
        r0 = r2.username;
        r1 = r3.username;
        r0 = org.apache.http.util.LangUtils.equals(r0, r1);
        if (r0 == 0) goto L_0x001e;
    L_0x0014:
        r0 = r2.domain;
        r1 = r3.domain;
        r0 = org.apache.http.util.LangUtils.equals(r0, r1);
        if (r0 != 0) goto L_0x0002;
    L_0x001e:
        r0 = 0;
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.auth.NTUserPrincipal.equals(java.lang.Object):boolean");
    }

    public String getDomain() {
        return this.domain;
    }

    public String getName() {
        return this.ntname;
    }

    public String getUsername() {
        return this.username;
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(17, this.username), this.domain);
    }

    public String toString() {
        return this.ntname;
    }
}
