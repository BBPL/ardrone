package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.apache.http.annotation.Immutable;

@Immutable
public class UsernamePasswordCredentials implements Credentials, Serializable {
    private static final long serialVersionUID = 243343858802739403L;
    private final String password;
    private final BasicUserPrincipal principal;

    public UsernamePasswordCredentials(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Username:password string may not be null");
        }
        int indexOf = str.indexOf(58);
        if (indexOf >= 0) {
            this.principal = new BasicUserPrincipal(str.substring(0, indexOf));
            this.password = str.substring(indexOf + 1);
            return;
        }
        this.principal = new BasicUserPrincipal(str);
        this.password = null;
    }

    public UsernamePasswordCredentials(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Username may not be null");
        }
        this.principal = new BasicUserPrincipal(str);
        this.password = str2;
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
        r0 = r3 instanceof org.apache.http.auth.UsernamePasswordCredentials;
        if (r0 == 0) goto L_0x0014;
    L_0x0008:
        r3 = (org.apache.http.auth.UsernamePasswordCredentials) r3;
        r0 = r2.principal;
        r1 = r3.principal;
        r0 = org.apache.http.util.LangUtils.equals(r0, r1);
        if (r0 != 0) goto L_0x0002;
    L_0x0014:
        r0 = 0;
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.auth.UsernamePasswordCredentials.equals(java.lang.Object):boolean");
    }

    public String getPassword() {
        return this.password;
    }

    public String getUserName() {
        return this.principal.getName();
    }

    public Principal getUserPrincipal() {
        return this.principal;
    }

    public int hashCode() {
        return this.principal.hashCode();
    }

    public String toString() {
        return this.principal.toString();
    }
}
