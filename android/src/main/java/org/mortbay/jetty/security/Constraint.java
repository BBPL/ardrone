package org.mortbay.jetty.security;

import java.io.Serializable;
import java.util.Arrays;

public class Constraint implements Cloneable, Serializable {
    public static final String ANY_ROLE = "*";
    public static final int DC_CONFIDENTIAL = 2;
    public static final int DC_INTEGRAL = 1;
    public static final int DC_NONE = 0;
    public static final int DC_UNSET = -1;
    public static final String NONE = "NONE";
    public static final String __BASIC_AUTH = "BASIC";
    public static final String __CERT_AUTH = "CLIENT_CERT";
    public static final String __CERT_AUTH2 = "CLIENT-CERT";
    public static final String __DIGEST_AUTH = "DIGEST";
    public static final String __FORM_AUTH = "FORM";
    private boolean _anyRole = false;
    private boolean _authenticate = false;
    private int _dataConstraint = -1;
    private String _name;
    private String[] _roles;

    public Constraint(String str, String str2) {
        setName(str);
        setRoles(new String[]{str2});
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean getAuthenticate() {
        return this._authenticate;
    }

    public int getDataConstraint() {
        return this._dataConstraint;
    }

    public String[] getRoles() {
        return this._roles;
    }

    public boolean hasDataConstraint() {
        return this._dataConstraint >= 0;
    }

    public boolean hasRole(String str) {
        if (!this._anyRole) {
            if (this._roles != null) {
                int length = this._roles.length;
                while (true) {
                    int i = length - 1;
                    if (length > 0) {
                        if (str.equals(this._roles[i])) {
                            break;
                        }
                        length = i;
                    } else {
                        break;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public boolean isAnyRole() {
        return this._anyRole;
    }

    public boolean isForbidden() {
        return this._authenticate && !this._anyRole && (this._roles == null || this._roles.length == 0);
    }

    public void setAuthenticate(boolean z) {
        this._authenticate = z;
    }

    public void setDataConstraint(int i) {
        if (i < 0 || i > 2) {
            throw new IllegalArgumentException("Constraint out of range");
        }
        this._dataConstraint = i;
    }

    public void setName(String str) {
        this._name = str;
    }

    public void setRoles(String[] strArr) {
        this._roles = strArr;
        this._anyRole = false;
        if (strArr != null) {
            int length = strArr.length;
            while (!this._anyRole) {
                int i = length - 1;
                if (length > 0) {
                    this._anyRole = ANY_ROLE.equals(strArr[i]);
                    length = i;
                } else {
                    return;
                }
            }
        }
    }

    public String toString() {
        StringBuffer append = new StringBuffer().append("SC{").append(this._name).append(",");
        String obj = this._anyRole ? ANY_ROLE : this._roles == null ? "-" : Arrays.asList(this._roles).toString();
        append = append.append(obj).append(",");
        obj = this._dataConstraint == -1 ? "DC_UNSET}" : this._dataConstraint == 0 ? "NONE}" : this._dataConstraint == 1 ? "INTEGRAL}" : "CONFIDENTIAL}";
        return append.append(obj).toString();
    }
}
