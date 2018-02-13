package org.apache.http.protocol;

@Deprecated
public class SyncBasicHttpContext extends BasicHttpContext {
    public SyncBasicHttpContext(HttpContext httpContext) {
        super(httpContext);
    }

    public void clear() {
        synchronized (this) {
            super.clear();
        }
    }

    public Object getAttribute(String str) {
        Object attribute;
        synchronized (this) {
            attribute = super.getAttribute(str);
        }
        return attribute;
    }

    public Object removeAttribute(String str) {
        Object removeAttribute;
        synchronized (this) {
            removeAttribute = super.removeAttribute(str);
        }
        return removeAttribute;
    }

    public void setAttribute(String str, Object obj) {
        synchronized (this) {
            super.setAttribute(str, obj);
        }
    }
}
