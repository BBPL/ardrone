package org.apache.http.params;

import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public class SyncBasicHttpParams extends BasicHttpParams {
    private static final long serialVersionUID = 5387834869062660642L;

    public void clear() {
        synchronized (this) {
            super.clear();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        Object clone;
        synchronized (this) {
            clone = super.clone();
        }
        return clone;
    }

    public Object getParameter(String str) {
        Object parameter;
        synchronized (this) {
            parameter = super.getParameter(str);
        }
        return parameter;
    }

    public boolean isParameterSet(String str) {
        boolean isParameterSet;
        synchronized (this) {
            isParameterSet = super.isParameterSet(str);
        }
        return isParameterSet;
    }

    public boolean isParameterSetLocally(String str) {
        boolean isParameterSetLocally;
        synchronized (this) {
            isParameterSetLocally = super.isParameterSetLocally(str);
        }
        return isParameterSetLocally;
    }

    public boolean removeParameter(String str) {
        boolean removeParameter;
        synchronized (this) {
            removeParameter = super.removeParameter(str);
        }
        return removeParameter;
    }

    public HttpParams setParameter(String str, Object obj) {
        HttpParams parameter;
        synchronized (this) {
            parameter = super.setParameter(str, obj);
        }
        return parameter;
    }

    public void setParameters(String[] strArr, Object obj) {
        synchronized (this) {
            super.setParameters(strArr, obj);
        }
    }
}
