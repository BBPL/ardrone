package org.mortbay.jetty.servlet;

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.UnavailableException;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.log.Log;
import org.mortbay.util.Loader;

public class Holder extends AbstractLifeCycle implements Serializable {
    static Class class$org$mortbay$jetty$servlet$Holder;
    protected transient Class _class;
    protected String _className;
    protected String _displayName;
    protected boolean _extInstance;
    protected Map _initParams;
    protected String _name;
    protected ServletHandler _servletHandler;

    protected Holder() {
    }

    protected Holder(Class cls) {
        this._class = cls;
        if (cls != null) {
            this._className = cls.getName();
            this._name = new StringBuffer().append(cls.getName()).append("-").append(hashCode()).toString();
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public void destroyInstance(Object obj) throws Exception {
    }

    public void doStart() throws Exception {
        if (this._class == null && (this._className == null || this._className.equals(HttpVersions.HTTP_0_9))) {
            throw new UnavailableException("No class for Servlet or Filter", -1);
        } else if (this._class == null) {
            try {
                Class class$;
                if (class$org$mortbay$jetty$servlet$Holder == null) {
                    class$ = class$("org.mortbay.jetty.servlet.Holder");
                    class$org$mortbay$jetty$servlet$Holder = class$;
                } else {
                    class$ = class$org$mortbay$jetty$servlet$Holder;
                }
                this._class = Loader.loadClass(class$, this._className);
                if (Log.isDebugEnabled()) {
                    Log.debug("Holding {}", this._class);
                }
            } catch (Throwable e) {
                Log.warn(e);
                throw new UnavailableException(e.getMessage(), -1);
            }
        }
    }

    public void doStop() {
        if (!this._extInstance) {
            this._class = null;
        }
    }

    public String getClassName() {
        return this._className;
    }

    public String getDisplayName() {
        return this._displayName;
    }

    public Class getHeldClass() {
        return this._class;
    }

    public String getInitParameter(String str) {
        return this._initParams == null ? null : (String) this._initParams.get(str);
    }

    public Enumeration getInitParameterNames() {
        return this._initParams == null ? Collections.enumeration(Collections.EMPTY_LIST) : Collections.enumeration(this._initParams.keySet());
    }

    public Map getInitParameters() {
        return this._initParams;
    }

    public String getName() {
        return this._name;
    }

    public ServletHandler getServletHandler() {
        return this._servletHandler;
    }

    public Object newInstance() throws InstantiationException, IllegalAccessException {
        Object newInstance;
        synchronized (this) {
            if (this._class == null) {
                throw new InstantiationException(new StringBuffer().append("!").append(this._className).toString());
            }
            newInstance = this._class.newInstance();
        }
        return newInstance;
    }

    public void setClassName(String str) {
        this._className = str;
        this._class = null;
    }

    public void setDisplayName(String str) {
        this._displayName = str;
    }

    public void setHeldClass(Class cls) {
        this._class = cls;
        this._className = cls != null ? cls.getName() : null;
    }

    public void setInitParameter(String str, String str2) {
        if (this._initParams == null) {
            this._initParams = new HashMap(3);
        }
        this._initParams.put(str, str2);
    }

    public void setInitParameters(Map map) {
        this._initParams = map;
    }

    public void setName(String str) {
        this._name = str;
    }

    public void setServletHandler(ServletHandler servletHandler) {
        this._servletHandler = servletHandler;
    }

    public String toString() {
        return this._name;
    }
}
