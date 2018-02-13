package org.mortbay.jetty.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.SingleThreadModel;
import javax.servlet.UnavailableException;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.log.Log;

public class ServletHolder extends Holder implements Comparable {
    static Class class$javax$servlet$Servlet;
    static Class class$javax$servlet$SingleThreadModel;
    static Class class$org$mortbay$jetty$security$SecurityHandler;
    private transient Config _config;
    private String _forcedPath;
    private boolean _initOnStartup = false;
    private int _initOrder;
    private UserRealm _realm;
    private Map _roleMap;
    private String _runAs;
    private transient Servlet _servlet;
    private transient long _unavailable;
    private transient UnavailableException _unavailableEx;

    static class C13441 {
    }

    class Config implements ServletConfig {
        private final ServletHolder this$0;

        Config(ServletHolder servletHolder) {
            this.this$0 = servletHolder;
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

        public String getServletName() {
            return this.this$0.getName();
        }
    }

    private class SingleThreadedWrapper implements Servlet {
        Stack _stack;
        private final ServletHolder this$0;

        private SingleThreadedWrapper(ServletHolder servletHolder) {
            this.this$0 = servletHolder;
            this._stack = new Stack();
        }

        SingleThreadedWrapper(ServletHolder servletHolder, C13441 c13441) {
            this(servletHolder);
        }

        public void destroy() {
            synchronized (this) {
                while (this._stack.size() > 0) {
                    try {
                        ((Servlet) this._stack.pop()).destroy();
                    } catch (Throwable e) {
                        Log.warn(e);
                    }
                }
            }
        }

        public ServletConfig getServletConfig() {
            return ServletHolder.access$100(this.this$0);
        }

        public String getServletInfo() {
            return null;
        }

        public void init(ServletConfig servletConfig) throws ServletException {
            synchronized (this) {
                if (this._stack.size() == 0) {
                    try {
                        Servlet customizeServlet = this.this$0.getServletHandler().customizeServlet((Servlet) this.this$0.newInstance());
                        customizeServlet.init(servletConfig);
                        this._stack.push(customizeServlet);
                    } catch (ServletException e) {
                        throw e;
                    } catch (Throwable e2) {
                        throw new ServletException(e2);
                    }
                }
            }
        }

        public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
            synchronized (this) {
                Servlet servlet;
                if (this._stack.size() > 0) {
                    servlet = (Servlet) this._stack.pop();
                } else {
                    try {
                        servlet = this.this$0.getServletHandler().customizeServlet((Servlet) this.this$0.newInstance());
                        servlet.init(ServletHolder.access$100(this.this$0));
                    } catch (ServletException e) {
                        throw e;
                    } catch (IOException e2) {
                        throw e2;
                    } catch (Throwable e3) {
                        throw new ServletException(e3);
                    }
                }
            }
            try {
                servlet.service(servletRequest, servletResponse);
                synchronized (this) {
                    this._stack.push(servlet);
                }
            } catch (Throwable th) {
                synchronized (this) {
                    this._stack.push(servlet);
                }
            }
        }
    }

    public ServletHolder(Class cls) {
        super(cls);
    }

    public ServletHolder(Servlet servlet) {
        setServlet(servlet);
    }

    static Config access$100(ServletHolder servletHolder) {
        return servletHolder._config;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private void initServlet() throws ServletException {
        UnavailableException unavailableException;
        Throwable th;
        ServletException e;
        Principal principal;
        Throwable th2;
        Principal principal2 = null;
        try {
            if (this._servlet == null) {
                this._servlet = (Servlet) newInstance();
            }
            if (this._config == null) {
                this._config = new Config(this);
            }
            if (!(this._servlet instanceof SingleThreadedWrapper)) {
                this._servlet = getServletHandler().customizeServlet(this._servlet);
            }
            Principal pushRole = (this._runAs == null || this._realm == null) ? null : this._realm.pushRole(null, this._runAs);
            try {
                this._servlet.init(this._config);
                if (this._runAs != null && this._realm != null && pushRole != null) {
                    this._realm.popRole(pushRole);
                }
            } catch (UnavailableException e2) {
                UnavailableException unavailableException2 = e2;
                principal2 = pushRole;
                unavailableException = unavailableException2;
                try {
                    makeUnavailable(unavailableException);
                    this._servlet = null;
                    this._config = null;
                    throw unavailableException;
                } catch (Throwable th3) {
                    th = th3;
                    this._realm.popRole(principal2);
                    throw th;
                }
            } catch (ServletException e3) {
                e = e3;
                principal = pushRole;
                try {
                    makeUnavailable(e.getCause() != null ? e.getCause() : e);
                    this._servlet = null;
                    this._config = null;
                    throw e;
                } catch (Throwable th4) {
                    th = th4;
                    principal2 = principal;
                    if (!(this._runAs == null || this._realm == null || principal2 == null)) {
                        this._realm.popRole(principal2);
                    }
                    throw th;
                }
            } catch (Throwable e4) {
                th2 = e4;
                principal2 = pushRole;
                th = th2;
                makeUnavailable(th);
                this._servlet = null;
                this._config = null;
                throw new ServletException(th);
            } catch (Throwable e42) {
                th2 = e42;
                principal2 = pushRole;
                th = th2;
                this._realm.popRole(principal2);
                throw th;
            }
        } catch (UnavailableException e5) {
            unavailableException = e5;
            makeUnavailable(unavailableException);
            this._servlet = null;
            this._config = null;
            throw unavailableException;
        } catch (ServletException e6) {
            principal = null;
            e = e6;
            if (e.getCause() != null) {
            }
            makeUnavailable(e.getCause() != null ? e.getCause() : e);
            this._servlet = null;
            this._config = null;
            throw e;
        } catch (Exception e7) {
            th = e7;
            makeUnavailable(th);
            this._servlet = null;
            this._config = null;
            throw new ServletException(th);
        }
    }

    private void makeUnavailable(Throwable th) {
        if (th instanceof UnavailableException) {
            makeUnavailable((UnavailableException) th);
            return;
        }
        this._servletHandler.getServletContext().log("unavailable", th);
        this._unavailableEx = new UnavailableException(th.toString(), -1);
        this._unavailable = -1;
    }

    private void makeUnavailable(UnavailableException unavailableException) {
        if (this._unavailableEx != unavailableException || this._unavailable == 0) {
            this._servletHandler.getServletContext().log(new StringBuffer().append("Unavailable ").append(unavailableException).toString());
            this._unavailableEx = unavailableException;
            this._unavailable = -1;
            if (unavailableException.isPermanent()) {
                this._unavailable = -1;
            } else if (this._unavailableEx.getUnavailableSeconds() > 0) {
                this._unavailable = System.currentTimeMillis() + ((long) (this._unavailableEx.getUnavailableSeconds() * 1000));
            } else {
                this._unavailable = System.currentTimeMillis() + 5000;
            }
        }
    }

    public void checkServletType() throws UnavailableException {
        Class class$;
        if (class$javax$servlet$Servlet == null) {
            class$ = class$("javax.servlet.Servlet");
            class$javax$servlet$Servlet = class$;
        } else {
            class$ = class$javax$servlet$Servlet;
        }
        if (!class$.isAssignableFrom(this._class)) {
            throw new UnavailableException(new StringBuffer().append("Servlet ").append(this._class).append(" is not a javax.servlet.Servlet").toString());
        }
    }

    public int compareTo(Object obj) {
        int i;
        int i2 = 1;
        int i3 = 0;
        if (obj instanceof ServletHolder) {
            ServletHolder servletHolder = (ServletHolder) obj;
            if (servletHolder == this) {
                i = 0;
                return i;
            } else if (servletHolder._initOrder >= this._initOrder) {
                if (servletHolder._initOrder > this._initOrder) {
                    return -1;
                }
                if (!(this._className == null || servletHolder._className == null)) {
                    i3 = this._className.compareTo(servletHolder._className);
                }
                i = i3 == 0 ? this._name.compareTo(servletHolder._name) : i3;
                if (i != 0) {
                    i2 = i;
                } else if (hashCode() <= obj.hashCode()) {
                    i2 = -1;
                }
                return i2;
            }
        }
        i = 1;
        return i;
    }

    public void destroyInstance(Object obj) throws Exception {
        if (obj != null) {
            Servlet servlet = (Servlet) obj;
            servlet.destroy();
            getServletHandler().customizeServletDestroy(servlet);
        }
    }

    public void doStart() throws Exception {
        Class class$;
        this._unavailable = 0;
        try {
            super.doStart();
            checkServletType();
        } catch (UnavailableException e) {
            makeUnavailable(e);
        }
        this._config = new Config(this);
        if (this._runAs != null) {
            ContextHandler contextHandler = ContextHandler.getCurrentContext().getContextHandler();
            if (class$org$mortbay$jetty$security$SecurityHandler == null) {
                class$ = class$("org.mortbay.jetty.security.SecurityHandler");
                class$org$mortbay$jetty$security$SecurityHandler = class$;
            } else {
                class$ = class$org$mortbay$jetty$security$SecurityHandler;
            }
            this._realm = ((SecurityHandler) contextHandler.getChildHandlerByClass(class$)).getUserRealm();
        }
        if (class$javax$servlet$SingleThreadModel == null) {
            class$ = class$("javax.servlet.SingleThreadModel");
            class$javax$servlet$SingleThreadModel = class$;
        } else {
            class$ = class$javax$servlet$SingleThreadModel;
        }
        if (class$.isAssignableFrom(this._class)) {
            this._servlet = new SingleThreadedWrapper(this, null);
        }
        if (this._extInstance || this._initOnStartup) {
            try {
                initServlet();
            } catch (Throwable e2) {
                if (this._servletHandler.isStartWithUnavailable()) {
                    Log.ignore(e2);
                    return;
                }
                throw e2;
            }
        }
    }

    public void doStop() {
        Throwable th;
        Principal principal;
        Throwable th2;
        Principal principal2 = null;
        try {
            if (!(this._runAs == null || this._realm == null)) {
                principal2 = this._realm.pushRole(null, this._runAs);
            }
            try {
                if (this._servlet != null) {
                    destroyInstance(this._servlet);
                }
            } catch (Throwable e) {
                Log.warn(e);
            } catch (Throwable e2) {
                th = e2;
                principal = principal2;
                th2 = th;
                super.doStop();
                if (!(this._runAs == null || this._realm == null || principal == null)) {
                    this._realm.popRole(principal);
                }
                throw th2;
            }
            if (!this._extInstance) {
                this._servlet = null;
            }
            this._config = null;
            super.doStop();
            if (this._runAs != null && this._realm != null && principal2 != null) {
                this._realm.popRole(principal2);
            }
        } catch (Throwable e22) {
            th = e22;
            principal = null;
            th2 = th;
            super.doStop();
            this._realm.popRole(principal);
            throw th2;
        }
    }

    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }

    public String getForcedPath() {
        return this._forcedPath;
    }

    public int getInitOrder() {
        return this._initOrder;
    }

    public Map getRoleMap() {
        return this._roleMap;
    }

    public String getRunAs() {
        return this._runAs;
    }

    public Servlet getServlet() throws ServletException {
        Servlet servlet;
        synchronized (this) {
            if (this._unavailable != 0) {
                if (this._unavailable < 0 || (this._unavailable > 0 && System.currentTimeMillis() < this._unavailable)) {
                    throw this._unavailableEx;
                }
                this._unavailable = 0;
                this._unavailableEx = null;
            }
            if (this._servlet == null) {
                initServlet();
            }
            servlet = this._servlet;
        }
        return servlet;
    }

    public Servlet getServletInstance() {
        return this._servlet;
    }

    public UnavailableException getUnavailableException() {
        return this._unavailableEx;
    }

    public String getUserRoleLink(String str) {
        if (this._roleMap == null) {
            return str;
        }
        String str2 = (String) this._roleMap.get(str);
        return str2 != null ? str2 : str;
    }

    public void handle(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, UnavailableException, IOException {
        Request request;
        Request request2;
        UnavailableException unavailableException;
        Throwable th;
        Principal principal = null;
        if (this._class == null) {
            throw new UnavailableException("Servlet Not Initialized");
        }
        Servlet servlet = this._servlet;
        synchronized (this) {
            Servlet servlet2 = (this._unavailable == 0 && this._initOnStartup) ? servlet : getServlet();
            if (servlet2 == null) {
                throw new UnavailableException(new StringBuffer().append("Could not instantiate ").append(this._class).toString());
            }
        }
        try {
            if (this._forcedPath != null) {
                servletRequest.setAttribute(Dispatcher.__JSP_FILE, this._forcedPath);
            }
            if (this._runAs == null || this._realm == null) {
                request = null;
            } else {
                request = HttpConnection.getCurrentConnection().getRequest();
                try {
                    principal = this._realm.pushRole(request.getUserPrincipal(), this._runAs);
                    request.setUserPrincipal(principal);
                } catch (UnavailableException e) {
                    UnavailableException unavailableException2 = e;
                    request2 = request;
                    unavailableException = unavailableException2;
                    try {
                        makeUnavailable(unavailableException);
                        throw this._unavailableEx;
                    } catch (Throwable th2) {
                        th = th2;
                        request2.setUserPrincipal(this._realm.popRole(null));
                        servletRequest.setAttribute(ServletHandler.__J_S_ERROR_SERVLET_NAME, getName());
                        throw th;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    request2 = request;
                    th = th4;
                    if (!(this._runAs == null || this._realm == null || null == null || request2 == null)) {
                        request2.setUserPrincipal(this._realm.popRole(null));
                    }
                    servletRequest.setAttribute(ServletHandler.__J_S_ERROR_SERVLET_NAME, getName());
                    throw th;
                }
            }
            servlet2.service(servletRequest, servletResponse);
            if (this._runAs != null && this._realm != null && principal != null && request != null) {
                request.setUserPrincipal(this._realm.popRole(principal));
            }
        } catch (UnavailableException e2) {
            unavailableException = e2;
            request2 = null;
            makeUnavailable(unavailableException);
            throw this._unavailableEx;
        } catch (Throwable th5) {
            th = th5;
            request2 = null;
            request2.setUserPrincipal(this._realm.popRole(null));
            servletRequest.setAttribute(ServletHandler.__J_S_ERROR_SERVLET_NAME, getName());
            throw th;
        }
    }

    public int hashCode() {
        return this._name == null ? System.identityHashCode(this) : this._name.hashCode();
    }

    public boolean isAvailable() {
        if (!(isStarted() && this._unavailable == 0)) {
            try {
                getServlet();
            } catch (Throwable e) {
                Log.ignore(e);
            }
            if (!(isStarted() && this._unavailable == 0)) {
                return false;
            }
        }
        return true;
    }

    public void setForcedPath(String str) {
        this._forcedPath = str;
    }

    public void setInitOrder(int i) {
        this._initOnStartup = true;
        this._initOrder = i;
    }

    public void setRunAs(String str) {
        this._runAs = str;
    }

    public void setServlet(Servlet servlet) {
        synchronized (this) {
            if (servlet != null) {
                if (!(servlet instanceof SingleThreadModel)) {
                    this._extInstance = true;
                    this._servlet = servlet;
                    setHeldClass(servlet.getClass());
                    if (getName() == null) {
                        setName(new StringBuffer().append(servlet.getClass().getName()).append("-").append(super.hashCode()).toString());
                    }
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public void setUserRoleLink(String str, String str2) {
        synchronized (this) {
            if (this._roleMap == null) {
                this._roleMap = new HashMap();
            }
            this._roleMap.put(str, str2);
        }
    }
}
