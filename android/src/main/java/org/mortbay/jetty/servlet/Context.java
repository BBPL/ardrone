package org.mortbay.jetty.servlet;

import javax.servlet.RequestDispatcher;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.log.Log;
import org.mortbay.util.URIUtil;

public class Context extends ContextHandler {
    public static final int NO_SECURITY = 0;
    public static final int NO_SESSIONS = 0;
    public static final int SECURITY = 2;
    public static final int SESSIONS = 1;
    protected SecurityHandler _securityHandler;
    protected ServletHandler _servletHandler;
    protected SessionHandler _sessionHandler;

    public class SContext extends org.mortbay.jetty.handler.ContextHandler.SContext {
        private final Context this$0;

        public SContext(Context context) {
            this.this$0 = context;
            super(context);
        }

        public RequestDispatcher getNamedDispatcher(String str) {
            return (this.this$0._servletHandler == null || this.this$0._servletHandler.getServlet(str) == null) ? null : new Dispatcher(this.this$0, str);
        }

        public RequestDispatcher getRequestDispatcher(String str) {
            if (str == null || !str.startsWith(URIUtil.SLASH)) {
                return null;
            }
            try {
                String substring;
                String str2;
                int indexOf = str.indexOf(63);
                if (indexOf > 0) {
                    substring = str.substring(indexOf + 1);
                    str = str.substring(0, indexOf);
                    str2 = substring;
                    substring = str;
                } else {
                    str2 = null;
                    substring = str;
                }
                int indexOf2 = substring.indexOf(59);
                if (indexOf2 > 0) {
                    substring = substring.substring(0, indexOf2);
                }
                String canonicalPath = URIUtil.canonicalPath(URIUtil.decodePath(substring));
                return new Dispatcher(this.this$0, URIUtil.addPaths(getContextPath(), substring), canonicalPath, str2);
            } catch (Throwable e) {
                Log.ignore(e);
                return null;
            }
        }
    }

    public Context() {
        this(null, null, null, null, null);
    }

    public Context(int i) {
        this(null, null, i);
    }

    public Context(HandlerContainer handlerContainer, String str) {
        this(handlerContainer, str, null, null, null, null);
    }

    public Context(HandlerContainer handlerContainer, String str, int i) {
        this(handlerContainer, str, (i & 1) != 0 ? new SessionHandler() : null, (i & 2) != 0 ? new SecurityHandler() : null, null, null);
    }

    public Context(HandlerContainer handlerContainer, String str, SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler) {
        super((org.mortbay.jetty.handler.ContextHandler.SContext) null);
        this._scontext = new SContext(this);
        this._sessionHandler = sessionHandler;
        this._securityHandler = securityHandler;
        if (servletHandler == null) {
            servletHandler = new ServletHandler();
        }
        this._servletHandler = servletHandler;
        if (this._sessionHandler != null) {
            setHandler(this._sessionHandler);
            if (securityHandler != null) {
                this._sessionHandler.setHandler(this._securityHandler);
                this._securityHandler.setHandler(this._servletHandler);
            } else {
                this._sessionHandler.setHandler(this._servletHandler);
            }
        } else if (this._securityHandler != null) {
            setHandler(this._securityHandler);
            this._securityHandler.setHandler(this._servletHandler);
        } else {
            setHandler(this._servletHandler);
        }
        if (errorHandler != null) {
            setErrorHandler(errorHandler);
        }
        if (str != null) {
            setContextPath(str);
        }
        if (handlerContainer != null) {
            handlerContainer.addHandler(this);
        }
    }

    public Context(HandlerContainer handlerContainer, String str, boolean z, boolean z2) {
        int i = 0;
        int i2 = z ? 1 : 0;
        if (z2) {
            i = 2;
        }
        this(handlerContainer, str, i | i2);
    }

    public Context(HandlerContainer handlerContainer, SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler) {
        this(handlerContainer, null, sessionHandler, securityHandler, servletHandler, errorHandler);
    }

    public FilterHolder addFilter(Class cls, String str, int i) {
        return this._servletHandler.addFilterWithMapping(cls, str, i);
    }

    public FilterHolder addFilter(String str, String str2, int i) {
        return this._servletHandler.addFilterWithMapping(str, str2, i);
    }

    public void addFilter(FilterHolder filterHolder, String str, int i) {
        this._servletHandler.addFilterWithMapping(filterHolder, str, i);
    }

    public ServletHolder addServlet(Class cls, String str) {
        return this._servletHandler.addServletWithMapping(cls.getName(), str);
    }

    public ServletHolder addServlet(String str, String str2) {
        return this._servletHandler.addServletWithMapping(str, str2);
    }

    public void addServlet(ServletHolder servletHolder, String str) {
        this._servletHandler.addServletWithMapping(servletHolder, str);
    }

    public SecurityHandler getSecurityHandler() {
        return this._securityHandler;
    }

    public ServletHandler getServletHandler() {
        return this._servletHandler;
    }

    public SessionHandler getSessionHandler() {
        return this._sessionHandler;
    }

    public void setSecurityHandler(SecurityHandler securityHandler) {
        if (this._securityHandler != securityHandler) {
            if (this._securityHandler != null) {
                this._securityHandler.setHandler(null);
            }
            this._securityHandler = securityHandler;
            if (this._securityHandler != null) {
                if (this._sessionHandler != null) {
                    this._sessionHandler.setHandler(this._securityHandler);
                } else {
                    setHandler(this._securityHandler);
                }
                if (this._servletHandler != null) {
                    this._securityHandler.setHandler(this._servletHandler);
                }
            } else if (this._sessionHandler != null) {
                this._sessionHandler.setHandler(this._servletHandler);
            } else {
                setHandler(this._servletHandler);
            }
        }
    }

    public void setServletHandler(ServletHandler servletHandler) {
        if (this._servletHandler != servletHandler) {
            this._servletHandler = servletHandler;
            if (this._securityHandler != null) {
                this._securityHandler.setHandler(this._servletHandler);
            } else if (this._sessionHandler != null) {
                this._sessionHandler.setHandler(this._servletHandler);
            } else {
                setHandler(this._servletHandler);
            }
        }
    }

    public void setSessionHandler(SessionHandler sessionHandler) {
        if (this._sessionHandler != sessionHandler) {
            if (this._sessionHandler != null) {
                this._sessionHandler.setHandler(null);
            }
            this._sessionHandler = sessionHandler;
            setHandler(this._sessionHandler);
            if (this._securityHandler != null) {
                this._sessionHandler.setHandler(this._securityHandler);
            } else if (this._servletHandler != null) {
                this._sessionHandler.setHandler(this._servletHandler);
            }
        }
    }

    protected void startContext() throws Exception {
        super.startContext();
        if (this._servletHandler != null && this._servletHandler.isStarted()) {
            this._servletHandler.initialize();
        }
    }
}
