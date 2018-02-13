package org.mortbay.jetty.servlet;

import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import org.mortbay.log.Log;

public class FilterHolder extends Holder {
    static Class class$javax$servlet$Filter;
    private transient Config _config;
    private transient Filter _filter;

    class Config implements FilterConfig {
        private final FilterHolder this$0;

        Config(FilterHolder filterHolder) {
            this.this$0 = filterHolder;
        }

        public String getFilterName() {
            return this.this$0._name;
        }

        public String getInitParameter(String str) {
            return this.this$0.getInitParameter(str);
        }

        public Enumeration getInitParameterNames() {
            return this.this$0.getInitParameterNames();
        }

        public ServletContext getServletContext() {
            return this.this$0._servletHandler.getServletContext();
        }
    }

    public FilterHolder(Class cls) {
        super(cls);
    }

    public FilterHolder(Filter filter) {
        setFilter(filter);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public static int dispatch(String str) {
        if ("request".equalsIgnoreCase(str)) {
            return 1;
        }
        if ("forward".equalsIgnoreCase(str)) {
            return 2;
        }
        if ("include".equalsIgnoreCase(str)) {
            return 4;
        }
        if ("error".equalsIgnoreCase(str)) {
            return 8;
        }
        throw new IllegalArgumentException(str);
    }

    public void destroyInstance(Object obj) throws Exception {
        if (obj != null) {
            Filter filter = (Filter) obj;
            filter.destroy();
            getServletHandler().customizeFilterDestroy(filter);
        }
    }

    public void doStart() throws Exception {
        Class class$;
        super.doStart();
        if (class$javax$servlet$Filter == null) {
            class$ = class$("javax.servlet.Filter");
            class$javax$servlet$Filter = class$;
        } else {
            class$ = class$javax$servlet$Filter;
        }
        if (class$.isAssignableFrom(this._class)) {
            if (this._filter == null) {
                this._filter = (Filter) newInstance();
            }
            this._filter = getServletHandler().customizeFilter(this._filter);
            this._config = new Config(this);
            this._filter.init(this._config);
            return;
        }
        String stringBuffer = new StringBuffer().append(this._class).append(" is not a javax.servlet.Filter").toString();
        super.stop();
        throw new IllegalStateException(stringBuffer);
    }

    public void doStop() {
        if (this._filter != null) {
            try {
                destroyInstance(this._filter);
            } catch (Throwable e) {
                Log.warn(e);
            }
        }
        if (!this._extInstance) {
            this._filter = null;
        }
        this._config = null;
        super.doStop();
    }

    public Filter getFilter() {
        return this._filter;
    }

    public void setFilter(Filter filter) {
        synchronized (this) {
            this._filter = filter;
            this._extInstance = true;
            setHeldClass(filter.getClass());
            if (getName() == null) {
                setName(filter.getClass().getName());
            }
        }
    }

    public String toString() {
        return getName();
    }
}
