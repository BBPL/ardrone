package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.LangUtils;

@Immutable
public class NTCredentials implements Credentials, Serializable {
    private static final long serialVersionUID = -7385699315228907265L;
    private final String password;
    private final NTUserPrincipal principal;
    private final String workstation;

    public NTCredentials(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Username:password string may not be null");
        }
        int indexOf = str.indexOf(58);
        if (indexOf >= 0) {
            String substring = str.substring(0, indexOf);
            this.password = str.substring(indexOf + 1);
            str = substring;
        } else {
            this.password = null;
        }
        int indexOf2 = str.indexOf(47);
        if (indexOf2 >= 0) {
            this.principal = new NTUserPrincipal(str.substring(0, indexOf2).toUpperCase(Locale.ENGLISH), str.substring(indexOf2 + 1));
        } else {
            this.principal = new NTUserPrincipal(null, str.substring(indexOf2 + 1));
        }
        this.workstation = null;
    }

    public NTCredentials(String str, String str2, String str3, String str4) {
        if (str == null) {
            throw new IllegalArgumentException("User name may not be null");
        }
        this.principal = new NTUserPrincipal(str4, str);
        this.password = str2;
        if (str3 != null) {
            this.workstation = str3.toUpperCase(Locale.ENGLISH);
        } else {
            this.workstation = null;
        }
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
        r0 = r3 instanceof org.apache.http.auth.NTCredentials;
        if (r0 == 0) goto L_0x001e;
    L_0x0008:
        r3 = (org.apache.http.auth.NTCredentials) r3;
        r0 = r2.principal;
        r1 = r3.principal;
        r0 = org.apache.http.util.LangUtils.equals(r0, r1);
        if (r0 == 0) goto L_0x001e;
    L_0x0014:
        r0 = r2.workstation;
        r1 = r3.workstation;
        r0 = org.apache.http.util.LangUtils.equals(r0, r1);
        if (r0 != 0) goto L_0x0002;
    L_0x001e:
        r0 = 0;
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.auth.NTCredentials.equals(java.lang.Object):boolean");
    }

    public String getDomain() {
        return this.principal.getDomain();
    }

    public String getPassword() {
        return this.password;
    }

    public String getUserName() {
        return this.principal.getUsername();
    }

    public Principal getUserPrincipal() {
        return this.principal;
    }

    public String getWorkstation() {
        return this.workstation;
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(17, this.principal), this.workstation);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[principal: ");
        stringBuilder.append(this.principal);
        stringBuilder.append("][workstation: ");
        stringBuilder.append(this.workstation);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
