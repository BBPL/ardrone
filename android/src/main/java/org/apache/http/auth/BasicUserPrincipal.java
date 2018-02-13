package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.LangUtils;

@Immutable
public final class BasicUserPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -2266305184969850467L;
    private final String username;

    public BasicUserPrincipal(String str) {
        if (str == null) {
            throw new IllegalArgumentException("User name may not be null");
        }
        this.username = str;
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
        r0 = r3 instanceof org.apache.http.auth.BasicUserPrincipal;
        if (r0 == 0) goto L_0x0014;
    L_0x0008:
        r3 = (org.apache.http.auth.BasicUserPrincipal) r3;
        r0 = r2.username;
        r1 = r3.username;
        r0 = org.apache.http.util.LangUtils.equals(r0, r1);
        if (r0 != 0) goto L_0x0002;
    L_0x0014:
        r0 = 0;
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.auth.BasicUserPrincipal.equals(java.lang.Object):boolean");
    }

    public String getName() {
        return this.username;
    }

    public int hashCode() {
        return LangUtils.hashCode(17, this.username);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[principal: ");
        stringBuilder.append(this.username);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
