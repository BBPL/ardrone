package org.mortbay.jetty.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.jetty.handler.HandlerWrapper;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;
import org.mortbay.util.URIUtil;

public class Invoker extends HttpServlet {
    static Class class$org$mortbay$jetty$servlet$ServletMapping;
    private ContextHandler _contextHandler;
    private Entry _invokerEntry;
    private boolean _nonContextServlets;
    private Map _parameters;
    private ServletHandler _servletHandler;
    private boolean _verbose;

    class Request extends HttpServletRequestWrapper {
        boolean _included;
        String _pathInfo;
        String _servletPath;
        private final Invoker this$0;

        Request(Invoker invoker, HttpServletRequest httpServletRequest, boolean z, String str, String str2, String str3) {
            this.this$0 = invoker;
            super(httpServletRequest);
            this._included = z;
            this._servletPath = URIUtil.addPaths(str2, str);
            this._pathInfo = str3.substring(str.length() + 1);
            if (this._pathInfo.length() == 0) {
                this._pathInfo = null;
            }
        }

        public Object getAttribute(String str) {
            if (this._included) {
                if (str.equals(Dispatcher.__INCLUDE_REQUEST_URI)) {
                    return URIUtil.addPaths(URIUtil.addPaths(getContextPath(), this._servletPath), this._pathInfo);
                }
                if (str.equals(Dispatcher.__INCLUDE_PATH_INFO)) {
                    return this._pathInfo;
                }
                if (str.equals(Dispatcher.__INCLUDE_SERVLET_PATH)) {
                    return this._servletPath;
                }
            }
            return super.getAttribute(str);
        }

        public String getPathInfo() {
            return this._included ? super.getPathInfo() : this._pathInfo;
        }

        public String getServletPath() {
            return this._included ? super.getServletPath() : this._servletPath;
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private ServletHolder getHolder(ServletHolder[] servletHolderArr, String str) {
        ServletHolder servletHolder = null;
        if (servletHolderArr != null) {
            int i = 0;
            while (servletHolder == null && i < servletHolderArr.length) {
                if (servletHolderArr[i].getName().equals(str)) {
                    servletHolder = servletHolderArr[i];
                }
                i++;
            }
        }
        return servletHolder;
    }

    public void init() {
        this._contextHandler = ((SContext) getServletContext()).getContextHandler();
        Handler handler = this._contextHandler.getHandler();
        while (handler != null && !(handler instanceof ServletHandler) && (handler instanceof HandlerWrapper)) {
            handler = ((HandlerWrapper) handler).getHandler();
        }
        this._servletHandler = (ServletHandler) handler;
        Enumeration initParameterNames = getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String str = (String) initParameterNames.nextElement();
            String initParameter = getInitParameter(str);
            String toLowerCase = initParameter.toLowerCase();
            if ("nonContextServlets".equals(str)) {
                boolean z = initParameter.length() > 0 && toLowerCase.startsWith("t");
                this._nonContextServlets = z;
            }
            if ("verbose".equals(str)) {
                boolean z2 = initParameter.length() > 0 && toLowerCase.startsWith("t");
                this._verbose = z2;
            } else {
                if (this._parameters == null) {
                    this._parameters = new HashMap();
                }
                this._parameters.put(str, initParameter);
            }
        }
    }

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String servletPath;
        boolean z;
        int i = 1;
        String str = (String) httpServletRequest.getAttribute(Dispatcher.__INCLUDE_SERVLET_PATH);
        if (str == null) {
            servletPath = httpServletRequest.getServletPath();
            z = false;
        } else {
            servletPath = str;
            z = true;
        }
        str = (String) httpServletRequest.getAttribute(Dispatcher.__INCLUDE_PATH_INFO);
        String pathInfo = str == null ? httpServletRequest.getPathInfo() : str;
        if (pathInfo == null || pathInfo.length() <= 1) {
            httpServletResponse.sendError(404);
            return;
        }
        ServletHolder servletHolder;
        String str2;
        if (pathInfo.charAt(0) != '/') {
            i = 0;
        }
        int indexOf = pathInfo.indexOf(47, i);
        String substring = indexOf < 0 ? pathInfo.substring(i) : pathInfo.substring(i, indexOf);
        ServletHolder holder = getHolder(this._servletHandler.getServlets(), substring);
        if (holder != null) {
            Class class$;
            Log.debug(new StringBuffer().append("Adding servlet mapping for named servlet:").append(substring).append(":").append(URIUtil.addPaths(servletPath, substring)).append("/*").toString());
            ServletMapping servletMapping = new ServletMapping();
            servletMapping.setServletName(substring);
            servletMapping.setPathSpec(new StringBuffer().append(URIUtil.addPaths(servletPath, substring)).append("/*").toString());
            ServletHandler servletHandler = this._servletHandler;
            ServletMapping[] servletMappings = this._servletHandler.getServletMappings();
            if (class$org$mortbay$jetty$servlet$ServletMapping == null) {
                class$ = class$("org.mortbay.jetty.servlet.ServletMapping");
                class$org$mortbay$jetty$servlet$ServletMapping = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$ServletMapping;
            }
            servletHandler.setServletMappings((ServletMapping[]) LazyList.addToArray(servletMappings, servletMapping, class$));
            servletHolder = holder;
            str2 = substring;
        } else {
            str2 = substring.endsWith(".class") ? substring.substring(0, substring.length() - 6) : substring;
            if (str2 == null || str2.length() == 0) {
                httpServletResponse.sendError(404);
                return;
            }
            ServletHolder addServletWithMapping;
            synchronized (this._servletHandler) {
                this._invokerEntry = this._servletHandler.getHolderEntry(servletPath);
                substring = URIUtil.addPaths(servletPath, str2);
                Entry holderEntry = this._servletHandler.getHolderEntry(substring);
                if (holderEntry == null || holderEntry.equals(this._invokerEntry)) {
                    Log.debug(new StringBuffer().append("Making new servlet=").append(str2).append(" with path=").append(substring).append("/*").toString());
                    addServletWithMapping = this._servletHandler.addServletWithMapping(str2, new StringBuffer().append(substring).append("/*").toString());
                    if (this._parameters != null) {
                        addServletWithMapping.setInitParameters(this._parameters);
                    }
                    try {
                        addServletWithMapping.start();
                        if (!this._nonContextServlets) {
                            Servlet servlet = addServletWithMapping.getServlet();
                            if (this._contextHandler.getClassLoader() != servlet.getClass().getClassLoader()) {
                                try {
                                    addServletWithMapping.stop();
                                } catch (Throwable e) {
                                    Log.ignore(e);
                                }
                                Log.warn(new StringBuffer().append("Dynamic servlet ").append(servlet).append(" not loaded from context ").append(httpServletRequest.getContextPath()).toString());
                                throw new UnavailableException("Not in context");
                            }
                        }
                        if (this._verbose) {
                            Log.debug(new StringBuffer().append("Dynamic load '").append(str2).append("' at ").append(substring).toString());
                        }
                    } catch (Throwable e2) {
                        Log.debug(e2);
                        throw new UnavailableException(e2.toString());
                    }
                }
                addServletWithMapping = (ServletHolder) holderEntry.getValue();
            }
            servletHolder = addServletWithMapping;
        }
        if (servletHolder != null) {
            servletHolder.handle(new Request(this, httpServletRequest, z, str2, servletPath, pathInfo), httpServletResponse);
            return;
        }
        Log.info(new StringBuffer().append("Can't find holder for servlet: ").append(str2).toString());
        httpServletResponse.sendError(404);
    }
}
