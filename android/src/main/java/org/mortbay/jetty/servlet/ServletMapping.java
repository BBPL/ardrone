package org.mortbay.jetty.servlet;

import java.util.Arrays;

public class ServletMapping {
    private String[] _pathSpecs;
    private String _servletName;

    public String[] getPathSpecs() {
        return this._pathSpecs;
    }

    public String getServletName() {
        return this._servletName;
    }

    public void setPathSpec(String str) {
        this._pathSpecs = new String[]{str};
    }

    public void setPathSpecs(String[] strArr) {
        this._pathSpecs = strArr;
    }

    public void setServletName(String str) {
        this._servletName = str;
    }

    public String toString() {
        return new StringBuffer().append("(S=").append(this._servletName).append(",").append(this._pathSpecs == null ? "[]" : Arrays.asList(this._pathSpecs).toString()).append(")").toString();
    }
}
