package org.mortbay.jetty.servlet;

import java.util.Arrays;

public class FilterMapping {
    private int _dispatches = 1;
    private String _filterName;
    private transient FilterHolder _holder;
    private String[] _pathSpecs;
    private String[] _servletNames;

    boolean appliesTo(int i) {
        return (this._dispatches & i) == 0 ? this._dispatches == 0 && i == 1 : true;
    }

    boolean appliesTo(String str, int i) {
        if (((this._dispatches & i) == 0 && (this._dispatches != 0 || i != 1)) || this._pathSpecs == null) {
            return false;
        }
        int i2 = 0;
        while (i2 < this._pathSpecs.length) {
            if (this._pathSpecs[i2] != null && PathMap.match(this._pathSpecs[i2], str, true)) {
                return true;
            }
            i2++;
        }
        return false;
    }

    public int getDispatches() {
        return this._dispatches;
    }

    FilterHolder getFilterHolder() {
        return this._holder;
    }

    public String getFilterName() {
        return this._filterName;
    }

    public String[] getPathSpecs() {
        return this._pathSpecs;
    }

    public String[] getServletNames() {
        return this._servletNames;
    }

    public void setDispatches(int i) {
        this._dispatches = i;
    }

    void setFilterHolder(FilterHolder filterHolder) {
        this._holder = filterHolder;
    }

    public void setFilterName(String str) {
        this._filterName = str;
    }

    public void setPathSpec(String str) {
        this._pathSpecs = new String[]{str};
    }

    public void setPathSpecs(String[] strArr) {
        this._pathSpecs = strArr;
    }

    public void setServletName(String str) {
        this._servletNames = new String[]{str};
    }

    public void setServletNames(String[] strArr) {
        this._servletNames = strArr;
    }

    public String toString() {
        return new StringBuffer().append("(F=").append(this._filterName).append(",").append(this._pathSpecs == null ? "[]" : Arrays.asList(this._pathSpecs).toString()).append(",").append(this._servletNames == null ? "[]" : Arrays.asList(this._servletNames).toString()).append(",").append(this._dispatches).append(")").toString();
    }
}
