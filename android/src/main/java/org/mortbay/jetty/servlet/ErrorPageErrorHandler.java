package org.mortbay.jetty.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.log.Log;
import org.mortbay.util.TypeUtil;

public class ErrorPageErrorHandler extends ErrorHandler {
    static Class class$javax$servlet$ServletException;
    protected List _errorPageList;
    protected Map _errorPages;
    protected ServletContext _servletContext;

    private class ErrorCodeRange {
        private int _from;
        private int _to;
        private String _uri;
        private final ErrorPageErrorHandler this$0;

        ErrorCodeRange(ErrorPageErrorHandler errorPageErrorHandler, int i, int i2, String str) throws IllegalArgumentException {
            this.this$0 = errorPageErrorHandler;
            if (i > i2) {
                throw new IllegalArgumentException("from>to");
            }
            this._from = i;
            this._to = i2;
            this._uri = str;
        }

        String getUri() {
            return this._uri;
        }

        boolean isInRange(int i) {
            return i >= this._from && i <= this._to;
        }

        public String toString() {
            return new StringBuffer().append("from: ").append(this._from).append(",to: ").append(this._to).append(",uri: ").append(this._uri).toString();
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public void addErrorPage(int i, int i2, String str) {
        if (this._errorPageList == null) {
            this._errorPageList = new ArrayList();
        }
        this._errorPageList.add(new ErrorCodeRange(this, i, i2, str));
    }

    public void addErrorPage(int i, String str) {
        if (this._errorPages == null) {
            this._errorPages = new HashMap();
        }
        this._errorPages.put(TypeUtil.toString(i), str);
    }

    public void addErrorPage(Class cls, String str) {
        if (this._errorPages == null) {
            this._errorPages = new HashMap();
        }
        this._errorPages.put(cls.getName(), str);
    }

    protected void doStart() throws Exception {
        super.doStart();
        this._servletContext = ContextHandler.getCurrentContext();
    }

    protected void doStop() throws Exception {
        super.doStop();
    }

    public Map getErrorPages() {
        return this._errorPages;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException {
        String method = httpServletRequest.getMethod();
        if (method.equals("GET") || method.equals("POST") || method.equals("HEAD")) {
            if (this._errorPages != null) {
                Object class$;
                String str2;
                Class cls;
                Class cls2 = (Class) httpServletRequest.getAttribute(ServletHandler.__J_S_ERROR_EXCEPTION_TYPE);
                if (class$javax$servlet$ServletException == null) {
                    class$ = class$("javax.servlet.ServletException");
                    class$javax$servlet$ServletException = class$;
                } else {
                    class$ = class$javax$servlet$ServletException;
                }
                if (class$.equals(cls2)) {
                    str2 = (String) this._errorPages.get(cls2.getName());
                    if (str2 == null) {
                        Object obj = (Throwable) httpServletRequest.getAttribute(ServletHandler.__J_S_ERROR_EXCEPTION);
                        while (obj instanceof ServletException) {
                            obj = ((ServletException) obj).getRootCause();
                        }
                        if (obj != null) {
                            cls = obj.getClass();
                        }
                    }
                    cls = cls2;
                } else {
                    str2 = null;
                    cls = cls2;
                }
                while (str2 == null && cls != null) {
                    method = (String) this._errorPages.get(cls.getName());
                    cls = cls.getSuperclass();
                    str2 = method;
                }
                if (str2 == null) {
                    Integer num = (Integer) httpServletRequest.getAttribute(ServletHandler.__J_S_ERROR_STATUS_CODE);
                    if (num != null) {
                        str2 = (String) this._errorPages.get(TypeUtil.toString(num.intValue()));
                        if (str2 == null && this._errorPageList != null) {
                            for (int i2 = 0; i2 < this._errorPageList.size(); i2++) {
                                ErrorCodeRange errorCodeRange = (ErrorCodeRange) this._errorPageList.get(i2);
                                if (errorCodeRange.isInRange(num.intValue())) {
                                    str2 = errorCodeRange.getUri();
                                    break;
                                }
                            }
                        }
                    }
                }
                if (str2 != null) {
                    method = (String) httpServletRequest.getAttribute(WebAppContext.ERROR_PAGE);
                    if (method == null || !method.equals(str2)) {
                        httpServletRequest.setAttribute(WebAppContext.ERROR_PAGE, str2);
                        Dispatcher dispatcher = (Dispatcher) this._servletContext.getRequestDispatcher(str2);
                        if (dispatcher != null) {
                            try {
                                dispatcher.error(httpServletRequest, httpServletResponse);
                                return;
                            } catch (Throwable e) {
                                Log.warn(Log.EXCEPTION, e);
                                return;
                            }
                        }
                        Log.warn(new StringBuffer().append("No error page ").append(str2).toString());
                    }
                }
            }
            super.handle(str, httpServletRequest, httpServletResponse, i);
            return;
        }
        HttpConnection.getCurrentConnection().getRequest().setHandled(true);
    }

    public void setErrorPages(Map map) {
        this._errorPages = map;
    }
}
