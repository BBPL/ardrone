package org.mortbay.jetty.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.io.RuntimeIOException;
import org.mortbay.jetty.EofException;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpException;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.RetryRequest;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;
import org.mortbay.util.MultiException;
import org.mortbay.util.MultiMap;
import org.mortbay.util.URIUtil;

public class ServletHandler extends AbstractHandler {
    public static final String __DEFAULT_SERVLET = "default";
    public static final String __J_S_CONTEXT_TEMPDIR = "javax.servlet.context.tempdir";
    public static final String __J_S_ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String __J_S_ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String __J_S_ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String __J_S_ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String __J_S_ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String __J_S_ERROR_STATUS_CODE = "javax.servlet.error.status_code";
    static Class class$org$mortbay$jetty$servlet$FilterHolder;
    static Class class$org$mortbay$jetty$servlet$FilterMapping;
    static Class class$org$mortbay$jetty$servlet$ServletHolder;
    static Class class$org$mortbay$jetty$servlet$ServletMapping;
    protected transient MruCache[] _chainCache;
    private ContextHandler _contextHandler;
    private boolean _filterChainsCached = true;
    private FilterMapping[] _filterMappings;
    private transient Map _filterNameMap = new HashMap();
    private transient MultiMap _filterNameMappings;
    private transient List _filterPathMappings;
    private FilterHolder[] _filters;
    private int _maxFilterChainsCacheSize = 1000;
    private SContext _servletContext;
    private ServletMapping[] _servletMappings;
    private transient Map _servletNameMap = new HashMap();
    private transient PathMap _servletPathMap;
    private ServletHolder[] _servlets;
    private boolean _startWithUnavailable = true;

    private class CachedChain implements FilterChain {
        FilterHolder _filterHolder;
        CachedChain _next;
        ServletHolder _servletHolder;
        private final ServletHandler this$0;

        CachedChain(ServletHandler servletHandler, Object obj, ServletHolder servletHolder) {
            this.this$0 = servletHandler;
            if (LazyList.size(obj) > 0) {
                this._filterHolder = (FilterHolder) LazyList.get(obj, 0);
                this._next = new CachedChain(servletHandler, LazyList.remove(obj, 0), servletHolder);
                return;
            }
            this._servletHolder = servletHolder;
        }

        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
            if (this._filterHolder != null) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("call filter ").append(this._filterHolder).toString());
                }
                this._filterHolder.getFilter().doFilter(servletRequest, servletResponse, this._next);
            } else if (this._servletHolder != null) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("call servlet ").append(this._servletHolder).toString());
                }
                this._servletHolder.handle(servletRequest, servletResponse);
            } else {
                this.this$0.notFound((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
            }
        }

        public String toString() {
            return this._filterHolder != null ? new StringBuffer().append(this._filterHolder).append("->").append(this._next.toString()).toString() : this._servletHolder != null ? this._servletHolder.toString() : "null";
        }
    }

    private class Chain implements FilterChain {
        Object _chain;
        int _filter = 0;
        ServletHolder _servletHolder;
        private final ServletHandler this$0;

        Chain(ServletHandler servletHandler, Object obj, ServletHolder servletHolder) {
            this.this$0 = servletHandler;
            this._chain = obj;
            this._servletHolder = servletHolder;
        }

        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("doFilter ").append(this._filter).toString());
            }
            if (this._filter < LazyList.size(this._chain)) {
                Object obj = this._chain;
                int i = this._filter;
                this._filter = i + 1;
                FilterHolder filterHolder = (FilterHolder) LazyList.get(obj, i);
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("call filter ").append(filterHolder).toString());
                }
                filterHolder.getFilter().doFilter(servletRequest, servletResponse, this);
            } else if (this._servletHolder != null) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("call servlet ").append(this._servletHolder).toString());
                }
                this._servletHolder.handle(servletRequest, servletResponse);
            } else {
                this.this$0.notFound((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
            }
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < LazyList.size(this._chain); i++) {
                stringBuffer.append(LazyList.get(this._chain, i).toString());
                stringBuffer.append("->");
            }
            stringBuffer.append(this._servletHolder);
            return stringBuffer.toString();
        }
    }

    private class MruCache extends LinkedHashMap {
        private int maxEntries = 1000;
        private final ServletHandler this$0;

        public MruCache(ServletHandler servletHandler) {
            this.this$0 = servletHandler;
        }

        public MruCache(ServletHandler servletHandler, int i) {
            this.this$0 = servletHandler;
            setMaxEntries(i);
        }

        protected boolean removeEldestEntry(Entry entry) {
            return size() > this.maxEntries;
        }

        public void setMaxEntries(int i) {
            this.maxEntries = i;
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private FilterChain getFilterChain(int i, String str, ServletHolder servletHolder) {
        Object obj;
        Object obj2;
        int i2 = 0;
        FilterChain filterChain = null;
        if (str == null) {
            Object name = servletHolder.getName();
        } else {
            String str2 = str;
        }
        if (this._filterChainsCached && this._chainCache != null) {
            synchronized (this) {
                if (this._chainCache[i].containsKey(name)) {
                    FilterChain filterChain2 = (FilterChain) this._chainCache[i].get(name);
                    return filterChain2;
                }
            }
        }
        if (str == null || this._filterPathMappings == null) {
            obj = null;
        } else {
            obj2 = null;
            int i3 = 0;
            while (i3 < this._filterPathMappings.size()) {
                FilterMapping filterMapping = (FilterMapping) this._filterPathMappings.get(i3);
                i3++;
                obj2 = filterMapping.appliesTo(str, i) ? LazyList.add(obj2, filterMapping.getFilterHolder()) : obj2;
            }
            obj = obj2;
        }
        if (servletHolder != null && this._filterNameMappings != null && this._filterNameMappings.size() > 0 && this._filterNameMappings.size() > 0) {
            Object obj3 = this._filterNameMappings.get(servletHolder.getName());
            Object obj4 = obj;
            for (int i4 = 0; i4 < LazyList.size(obj3); i4++) {
                filterMapping = (FilterMapping) LazyList.get(obj3, i4);
                if (filterMapping.appliesTo(i)) {
                    obj4 = LazyList.add(obj4, filterMapping.getFilterHolder());
                }
            }
            obj2 = this._filterNameMappings.get(Constraint.ANY_ROLE);
            while (i2 < LazyList.size(obj2)) {
                filterMapping = (FilterMapping) LazyList.get(obj2, i2);
                if (filterMapping.appliesTo(i)) {
                    obj4 = LazyList.add(obj4, filterMapping.getFilterHolder());
                }
                i2++;
            }
            obj = obj4;
        }
        if (obj == null) {
            return null;
        }
        if (!this._filterChainsCached) {
            return LazyList.size(obj) > 0 ? new Chain(this, obj, servletHolder) : null;
        } else {
            if (LazyList.size(obj) > 0) {
                filterChain = new CachedChain(this, obj, servletHolder);
            }
            synchronized (this) {
                this._chainCache[i].put(name, filterChain);
            }
            return filterChain;
        }
    }

    private void invalidateChainsCache() {
        this._chainCache = new MruCache[]{null, new MruCache(this, this._maxFilterChainsCacheSize), new MruCache(this, this._maxFilterChainsCacheSize), null, new MruCache(this, this._maxFilterChainsCacheSize), null, null, null, new MruCache(this, this._maxFilterChainsCacheSize)};
    }

    public FilterHolder addFilter(String str, String str2, int i) {
        return addFilterWithMapping(str, str2, i);
    }

    public void addFilter(FilterHolder filterHolder) {
        if (filterHolder != null) {
            Class class$;
            FilterHolder[] filters = getFilters();
            if (class$org$mortbay$jetty$servlet$FilterHolder == null) {
                class$ = class$("org.mortbay.jetty.servlet.FilterHolder");
                class$org$mortbay$jetty$servlet$FilterHolder = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$FilterHolder;
            }
            setFilters((FilterHolder[]) LazyList.addToArray(filters, filterHolder, class$));
        }
    }

    public void addFilter(FilterHolder filterHolder, FilterMapping filterMapping) {
        Class class$;
        if (filterHolder != null) {
            FilterHolder[] filters = getFilters();
            if (class$org$mortbay$jetty$servlet$FilterHolder == null) {
                class$ = class$("org.mortbay.jetty.servlet.FilterHolder");
                class$org$mortbay$jetty$servlet$FilterHolder = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$FilterHolder;
            }
            setFilters((FilterHolder[]) LazyList.addToArray(filters, filterHolder, class$));
        }
        if (filterMapping != null) {
            FilterMapping[] filterMappings = getFilterMappings();
            if (class$org$mortbay$jetty$servlet$FilterMapping == null) {
                class$ = class$("org.mortbay.jetty.servlet.FilterMapping");
                class$org$mortbay$jetty$servlet$FilterMapping = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$FilterMapping;
            }
            setFilterMappings((FilterMapping[]) LazyList.addToArray(filterMappings, filterMapping, class$));
        }
    }

    public void addFilterMapping(FilterMapping filterMapping) {
        if (filterMapping != null) {
            Class class$;
            FilterMapping[] filterMappings = getFilterMappings();
            if (class$org$mortbay$jetty$servlet$FilterMapping == null) {
                class$ = class$("org.mortbay.jetty.servlet.FilterMapping");
                class$org$mortbay$jetty$servlet$FilterMapping = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$FilterMapping;
            }
            setFilterMappings((FilterMapping[]) LazyList.addToArray(filterMappings, filterMapping, class$));
        }
    }

    public FilterHolder addFilterWithMapping(Class cls, String str, int i) {
        FilterHolder newFilterHolder = newFilterHolder(cls);
        addFilterWithMapping(newFilterHolder, str, i);
        return newFilterHolder;
    }

    public FilterHolder addFilterWithMapping(String str, String str2, int i) {
        FilterHolder newFilterHolder = newFilterHolder(null);
        newFilterHolder.setName(new StringBuffer().append(str).append("-").append(newFilterHolder.hashCode()).toString());
        newFilterHolder.setClassName(str);
        addFilterWithMapping(newFilterHolder, str2, i);
        return newFilterHolder;
    }

    public void addFilterWithMapping(FilterHolder filterHolder, String str, int i) {
        Object filters = getFilters();
        if (filters != null) {
            FilterHolder[] filterHolderArr = (FilterHolder[]) filters.clone();
        } else {
            Object obj = filters;
        }
        try {
            Class class$;
            if (class$org$mortbay$jetty$servlet$FilterHolder == null) {
                class$ = class$("org.mortbay.jetty.servlet.FilterHolder");
                class$org$mortbay$jetty$servlet$FilterHolder = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$FilterHolder;
            }
            setFilters((FilterHolder[]) LazyList.addToArray(filterHolderArr, filterHolder, class$));
            FilterMapping filterMapping = new FilterMapping();
            filterMapping.setFilterName(filterHolder.getName());
            filterMapping.setPathSpec(str);
            filterMapping.setDispatches(i);
            FilterMapping[] filterMappings = getFilterMappings();
            if (class$org$mortbay$jetty$servlet$FilterMapping == null) {
                class$ = class$("org.mortbay.jetty.servlet.FilterMapping");
                class$org$mortbay$jetty$servlet$FilterMapping = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$FilterMapping;
            }
            setFilterMappings((FilterMapping[]) LazyList.addToArray(filterMappings, filterMapping, class$));
        } catch (RuntimeException e) {
            setFilters(filterHolderArr);
            throw e;
        } catch (Error e2) {
            setFilters(filterHolderArr);
            throw e2;
        }
    }

    public ServletHolder addServlet(String str, String str2) {
        return addServletWithMapping(str, str2);
    }

    public void addServlet(ServletHolder servletHolder) {
        Class class$;
        ServletHolder[] servlets = getServlets();
        if (class$org$mortbay$jetty$servlet$ServletHolder == null) {
            class$ = class$("org.mortbay.jetty.servlet.ServletHolder");
            class$org$mortbay$jetty$servlet$ServletHolder = class$;
        } else {
            class$ = class$org$mortbay$jetty$servlet$ServletHolder;
        }
        setServlets((ServletHolder[]) LazyList.addToArray(servlets, servletHolder, class$));
    }

    public void addServletMapping(ServletMapping servletMapping) {
        Class class$;
        ServletMapping[] servletMappings = getServletMappings();
        if (class$org$mortbay$jetty$servlet$ServletMapping == null) {
            class$ = class$("org.mortbay.jetty.servlet.ServletMapping");
            class$org$mortbay$jetty$servlet$ServletMapping = class$;
        } else {
            class$ = class$org$mortbay$jetty$servlet$ServletMapping;
        }
        setServletMappings((ServletMapping[]) LazyList.addToArray(servletMappings, servletMapping, class$));
    }

    public ServletHolder addServletWithMapping(Class cls, String str) {
        Class class$;
        ServletHolder newServletHolder = newServletHolder(cls);
        ServletHolder[] servlets = getServlets();
        if (class$org$mortbay$jetty$servlet$ServletHolder == null) {
            class$ = class$("org.mortbay.jetty.servlet.ServletHolder");
            class$org$mortbay$jetty$servlet$ServletHolder = class$;
        } else {
            class$ = class$org$mortbay$jetty$servlet$ServletHolder;
        }
        setServlets((ServletHolder[]) LazyList.addToArray(servlets, newServletHolder, class$));
        addServletWithMapping(newServletHolder, str);
        return newServletHolder;
    }

    public ServletHolder addServletWithMapping(String str, String str2) {
        ServletHolder newServletHolder = newServletHolder(null);
        newServletHolder.setName(new StringBuffer().append(str).append("-").append(newServletHolder.hashCode()).toString());
        newServletHolder.setClassName(str);
        addServletWithMapping(newServletHolder, str2);
        return newServletHolder;
    }

    public void addServletWithMapping(ServletHolder servletHolder, String str) {
        Object servlets = getServlets();
        if (servlets != null) {
            ServletHolder[] servletHolderArr = (ServletHolder[]) servlets.clone();
        } else {
            Object obj = servlets;
        }
        try {
            Class class$;
            if (class$org$mortbay$jetty$servlet$ServletHolder == null) {
                class$ = class$("org.mortbay.jetty.servlet.ServletHolder");
                class$org$mortbay$jetty$servlet$ServletHolder = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$ServletHolder;
            }
            setServlets((ServletHolder[]) LazyList.addToArray(servletHolderArr, servletHolder, class$));
            ServletMapping servletMapping = new ServletMapping();
            servletMapping.setServletName(servletHolder.getName());
            servletMapping.setPathSpec(str);
            ServletMapping[] servletMappings = getServletMappings();
            if (class$org$mortbay$jetty$servlet$ServletMapping == null) {
                class$ = class$("org.mortbay.jetty.servlet.ServletMapping");
                class$org$mortbay$jetty$servlet$ServletMapping = class$;
            } else {
                class$ = class$org$mortbay$jetty$servlet$ServletMapping;
            }
            setServletMappings((ServletMapping[]) LazyList.addToArray(servletMappings, servletMapping, class$));
        } catch (Throwable e) {
            setServlets(servletHolderArr);
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            }
            throw new RuntimeException(e);
        }
    }

    public Filter customizeFilter(Filter filter) throws Exception {
        return filter;
    }

    public Filter customizeFilterDestroy(Filter filter) throws Exception {
        return filter;
    }

    public Servlet customizeServlet(Servlet servlet) throws Exception {
        return servlet;
    }

    public Servlet customizeServletDestroy(Servlet servlet) throws Exception {
        return servlet;
    }

    protected void doStart() throws Exception {
        ContextHandler contextHandler = null;
        synchronized (this) {
            this._servletContext = ContextHandler.getCurrentContext();
            if (this._servletContext != null) {
                contextHandler = this._servletContext.getContextHandler();
            }
            this._contextHandler = contextHandler;
            updateNameMappings();
            updateMappings();
            if (this._filterChainsCached) {
                this._chainCache = new MruCache[]{null, new MruCache(this, this._maxFilterChainsCacheSize), new MruCache(this, this._maxFilterChainsCacheSize), null, new MruCache(this, this._maxFilterChainsCacheSize), null, null, null, new MruCache(this, this._maxFilterChainsCacheSize)};
            }
            super.doStart();
            if (this._contextHandler == null || !(this._contextHandler instanceof Context)) {
                initialize();
            }
        }
    }

    protected void doStop() throws Exception {
        int i;
        synchronized (this) {
            int length;
            super.doStop();
            if (this._filters != null) {
                length = this._filters.length;
                while (true) {
                    i = length - 1;
                    if (length <= 0) {
                        break;
                    }
                    try {
                        this._filters[i].stop();
                        length = i;
                    } catch (Throwable e) {
                        Log.warn(Log.EXCEPTION, e);
                        length = i;
                    }
                }
            }
            if (this._servlets != null) {
                length = this._servlets.length;
                while (true) {
                    i = length - 1;
                    if (length <= 0) {
                        break;
                    }
                    try {
                        this._servlets[i].stop();
                        length = i;
                    } catch (Throwable e2) {
                        Log.warn(Log.EXCEPTION, e2);
                        length = i;
                    }
                }
            }
            this._filterPathMappings = null;
            this._filterNameMappings = null;
            this._servletPathMap = null;
            this._chainCache = null;
        }
    }

    public Object getContextLog() {
        return null;
    }

    public FilterHolder getFilter(String str) {
        return (FilterHolder) this._filterNameMap.get(str);
    }

    public FilterMapping[] getFilterMappings() {
        return this._filterMappings;
    }

    public FilterHolder[] getFilters() {
        return this._filters;
    }

    public PathMap.Entry getHolderEntry(String str) {
        return this._servletPathMap == null ? null : this._servletPathMap.getMatch(str);
    }

    public int getMaxFilterChainsCacheSize() {
        return this._maxFilterChainsCacheSize;
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
            return new Dispatcher(this._contextHandler, URIUtil.addPaths(this._contextHandler.getContextPath(), substring), canonicalPath, str2);
        } catch (Throwable e) {
            Log.ignore(e);
            return null;
        }
    }

    public ServletHolder getServlet(String str) {
        return (ServletHolder) this._servletNameMap.get(str);
    }

    public ServletContext getServletContext() {
        return this._servletContext;
    }

    public ServletMapping[] getServletMappings() {
        return this._servletMappings;
    }

    public ServletHolder[] getServlets() {
        return this._servlets;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        Object takeRequestListeners;
        ServletRequestEvent servletRequestEvent;
        RetryRequest e;
        Object obj;
        ServletRequestEvent servletRequestEvent2;
        Throwable th;
        Throwable th2;
        Throwable th3;
        int size;
        int i2;
        EofException e2;
        RuntimeIOException e3;
        if (isStarted()) {
            Request request = httpServletRequest instanceof Request ? (Request) httpServletRequest : HttpConnection.getCurrentConnection().getRequest();
            String servletName = request.getServletName();
            String servletPath = request.getServletPath();
            String pathInfo = request.getPathInfo();
            Map roleMap = request.getRoleMap();
            try {
                FilterChain filterChain;
                ServletHolder servletHolder;
                ServletHolder servletHolder2;
                if (str.startsWith(URIUtil.SLASH)) {
                    PathMap.Entry holderEntry = getHolderEntry(str);
                    if (holderEntry != null) {
                        servletHolder2 = (ServletHolder) holderEntry.getValue();
                        request.setServletName(servletHolder2.getName());
                        request.setRoleMap(servletHolder2.getRoleMap());
                        if (Log.isDebugEnabled()) {
                            Log.debug(new StringBuffer().append("servlet=").append(servletHolder2).toString());
                        }
                        String str2 = (String) holderEntry.getKey();
                        String mapped = holderEntry.getMapped() != null ? holderEntry.getMapped() : PathMap.pathMatch(str2, str);
                        str2 = PathMap.pathInfo(str2, str);
                        if (i == 4) {
                            request.setAttribute(Dispatcher.__INCLUDE_SERVLET_PATH, mapped);
                            request.setAttribute(Dispatcher.__INCLUDE_PATH_INFO, str2);
                        } else {
                            request.setServletPath(mapped);
                            request.setPathInfo(str2);
                        }
                        if (!(servletHolder2 == null || this._filterMappings == null || this._filterMappings.length <= 0)) {
                            filterChain = getFilterChain(i, str, servletHolder2);
                            servletHolder = servletHolder2;
                        }
                        servletHolder = servletHolder2;
                        filterChain = null;
                    } else {
                        servletHolder = null;
                        filterChain = null;
                    }
                } else {
                    servletHolder2 = (ServletHolder) this._servletNameMap.get(str);
                    if (!(servletHolder2 == null || this._filterMappings == null || this._filterMappings.length <= 0)) {
                        request.setServletName(servletHolder2.getName());
                        filterChain = getFilterChain(i, null, servletHolder2);
                        servletHolder = servletHolder2;
                    }
                    servletHolder = servletHolder2;
                    filterChain = null;
                }
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("chain=").append(filterChain).toString());
                    Log.debug(new StringBuffer().append("servlet holder=").append(servletHolder).toString());
                }
                takeRequestListeners = request.takeRequestListeners();
                if (takeRequestListeners != null) {
                    try {
                        servletRequestEvent = new ServletRequestEvent(getServletContext(), httpServletRequest);
                    } catch (RetryRequest e4) {
                        e = e4;
                        obj = takeRequestListeners;
                        servletRequestEvent2 = null;
                        try {
                            request.setHandled(false);
                            throw e;
                        } catch (Throwable th22) {
                            takeRequestListeners = obj;
                            servletRequestEvent = servletRequestEvent2;
                            th = th22;
                            th3 = th;
                            if (takeRequestListeners != null) {
                                size = LazyList.size(takeRequestListeners);
                                while (true) {
                                    i2 = size - 1;
                                    if (size <= 0) {
                                        break;
                                    }
                                    ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                    size = i2;
                                }
                            }
                            request.setServletName(servletName);
                            request.setRoleMap(roleMap);
                            if (i != 4) {
                                request.setServletPath(servletPath);
                                request.setPathInfo(pathInfo);
                            }
                            throw th3;
                        }
                    } catch (EofException e5) {
                        e2 = e5;
                        servletRequestEvent = null;
                        try {
                            throw e2;
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    } catch (RuntimeIOException e6) {
                        e3 = e6;
                        servletRequestEvent = null;
                        throw e3;
                    } catch (Exception e7) {
                        th = e7;
                        servletRequestEvent = null;
                        if (i != 1) {
                            if (!(th instanceof IOException)) {
                                throw ((IOException) th);
                            } else if (!(th instanceof RuntimeException)) {
                                throw ((RuntimeException) th);
                            } else if (th instanceof ServletException) {
                                throw ((ServletException) th);
                            }
                        }
                        if (!(th instanceof UnavailableException)) {
                            Log.debug(th);
                        } else if (th instanceof ServletException) {
                            Log.debug(th);
                            th22 = ((ServletException) th).getRootCause();
                            th = th22;
                        }
                        if (!(th instanceof RetryRequest)) {
                            request.setHandled(false);
                            throw ((RetryRequest) th);
                        } else if (!(th instanceof HttpException)) {
                            throw ((HttpException) th);
                        } else if (!(th instanceof RuntimeIOException)) {
                            throw ((RuntimeIOException) th);
                        } else if (th instanceof EofException) {
                            throw ((EofException) th);
                        } else {
                            if (Log.isDebugEnabled()) {
                                Log.warn(httpServletRequest.getRequestURI(), th);
                                Log.debug(httpServletRequest.toString());
                            } else {
                                if (!(th instanceof IOException)) {
                                }
                                Log.warn(new StringBuffer().append(httpServletRequest.getRequestURI()).append(": ").append(th).toString());
                            }
                            if (httpServletResponse.isCommitted()) {
                                httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION_TYPE, th.getClass());
                                httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION, th);
                                if (!(th instanceof UnavailableException)) {
                                    httpServletResponse.sendError(500, th.getMessage());
                                } else if (((UnavailableException) th).isPermanent()) {
                                    httpServletResponse.sendError(404, th.getMessage());
                                } else {
                                    httpServletResponse.sendError(503, th.getMessage());
                                }
                            } else if (Log.isDebugEnabled()) {
                                Log.debug(new StringBuffer().append("Response already committed for handling ").append(th).toString());
                            }
                            if (takeRequestListeners != null) {
                                size = LazyList.size(takeRequestListeners);
                                while (true) {
                                    i2 = size - 1;
                                    if (size <= 0) {
                                        break;
                                    }
                                    ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                    size = i2;
                                }
                            }
                            request.setServletName(servletName);
                            request.setRoleMap(roleMap);
                            if (i == 4) {
                                request.setServletPath(servletPath);
                                request.setPathInfo(pathInfo);
                            }
                        }
                    } catch (Error e8) {
                        th = e8;
                        servletRequestEvent = null;
                        if (i == 1) {
                            Log.warn(new StringBuffer().append("Error for ").append(httpServletRequest.getRequestURI()).toString(), th);
                            if (Log.isDebugEnabled()) {
                                Log.debug(httpServletRequest.toString());
                            }
                            if (httpServletResponse.isCommitted()) {
                                httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION_TYPE, th.getClass());
                                httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION, th);
                                httpServletResponse.sendError(500, th.getMessage());
                            } else if (Log.isDebugEnabled()) {
                                Log.debug("Response already committed for handling ", th);
                            }
                            if (takeRequestListeners != null) {
                                size = LazyList.size(takeRequestListeners);
                                while (true) {
                                    i2 = size - 1;
                                    if (size <= 0) {
                                        break;
                                    }
                                    ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                    size = i2;
                                }
                            }
                            request.setServletName(servletName);
                            request.setRoleMap(roleMap);
                            if (i == 4) {
                                request.setServletPath(servletPath);
                                request.setPathInfo(pathInfo);
                                return;
                            }
                            return;
                        }
                        throw th;
                    } catch (Throwable th5) {
                        th = th5;
                        servletRequestEvent = null;
                        th3 = th;
                        if (takeRequestListeners != null) {
                            size = LazyList.size(takeRequestListeners);
                            while (true) {
                                i2 = size - 1;
                                if (size <= 0) {
                                    break;
                                }
                                ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                size = i2;
                            }
                        }
                        request.setServletName(servletName);
                        request.setRoleMap(roleMap);
                        if (i != 4) {
                            request.setServletPath(servletPath);
                            request.setPathInfo(pathInfo);
                        }
                        throw th3;
                    }
                    try {
                        int size2 = LazyList.size(takeRequestListeners);
                        for (int i3 = 0; i3 < size2; i3++) {
                            ((ServletRequestListener) LazyList.get(takeRequestListeners, i3)).requestInitialized(servletRequestEvent);
                        }
                    } catch (RetryRequest e9) {
                        e = e9;
                        obj = takeRequestListeners;
                        servletRequestEvent2 = servletRequestEvent;
                        request.setHandled(false);
                        throw e;
                    } catch (EofException e10) {
                        e2 = e10;
                        throw e2;
                    } catch (RuntimeIOException e11) {
                        e3 = e11;
                        throw e3;
                    } catch (Exception e12) {
                        th = e12;
                        if (i != 1) {
                            if (!(th instanceof IOException)) {
                                throw ((IOException) th);
                            } else if (!(th instanceof RuntimeException)) {
                                throw ((RuntimeException) th);
                            } else if (th instanceof ServletException) {
                                throw ((ServletException) th);
                            }
                        }
                        if (!(th instanceof UnavailableException)) {
                            Log.debug(th);
                        } else if (th instanceof ServletException) {
                            Log.debug(th);
                            th22 = ((ServletException) th).getRootCause();
                            th = th22;
                        }
                        if (!(th instanceof RetryRequest)) {
                            request.setHandled(false);
                            throw ((RetryRequest) th);
                        } else if (!(th instanceof HttpException)) {
                            throw ((HttpException) th);
                        } else if (!(th instanceof RuntimeIOException)) {
                            throw ((RuntimeIOException) th);
                        } else if (th instanceof EofException) {
                            throw ((EofException) th);
                        } else {
                            if (Log.isDebugEnabled()) {
                                Log.warn(httpServletRequest.getRequestURI(), th);
                                Log.debug(httpServletRequest.toString());
                            } else {
                                if (th instanceof IOException) {
                                }
                                Log.warn(new StringBuffer().append(httpServletRequest.getRequestURI()).append(": ").append(th).toString());
                            }
                            if (httpServletResponse.isCommitted()) {
                                httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION_TYPE, th.getClass());
                                httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION, th);
                                if (!(th instanceof UnavailableException)) {
                                    httpServletResponse.sendError(500, th.getMessage());
                                } else if (((UnavailableException) th).isPermanent()) {
                                    httpServletResponse.sendError(404, th.getMessage());
                                } else {
                                    httpServletResponse.sendError(503, th.getMessage());
                                }
                            } else if (Log.isDebugEnabled()) {
                                Log.debug(new StringBuffer().append("Response already committed for handling ").append(th).toString());
                            }
                            if (takeRequestListeners != null) {
                                size = LazyList.size(takeRequestListeners);
                                while (true) {
                                    i2 = size - 1;
                                    if (size <= 0) {
                                        break;
                                    }
                                    ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                    size = i2;
                                }
                            }
                            request.setServletName(servletName);
                            request.setRoleMap(roleMap);
                            if (i == 4) {
                                request.setServletPath(servletPath);
                                request.setPathInfo(pathInfo);
                            }
                        }
                    } catch (Error e13) {
                        th = e13;
                        if (i == 1) {
                            throw th;
                        }
                        Log.warn(new StringBuffer().append("Error for ").append(httpServletRequest.getRequestURI()).toString(), th);
                        if (Log.isDebugEnabled()) {
                            Log.debug(httpServletRequest.toString());
                        }
                        if (httpServletResponse.isCommitted()) {
                            httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION_TYPE, th.getClass());
                            httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION, th);
                            httpServletResponse.sendError(500, th.getMessage());
                        } else if (Log.isDebugEnabled()) {
                            Log.debug("Response already committed for handling ", th);
                        }
                        if (takeRequestListeners != null) {
                            size = LazyList.size(takeRequestListeners);
                            while (true) {
                                i2 = size - 1;
                                if (size <= 0) {
                                    break;
                                }
                                ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                size = i2;
                            }
                        }
                        request.setServletName(servletName);
                        request.setRoleMap(roleMap);
                        if (i == 4) {
                            request.setServletPath(servletPath);
                            request.setPathInfo(pathInfo);
                            return;
                        }
                        return;
                    } catch (Throwable th6) {
                        th3 = th6;
                        if (takeRequestListeners != null) {
                            size = LazyList.size(takeRequestListeners);
                            while (true) {
                                i2 = size - 1;
                                if (size <= 0) {
                                    break;
                                }
                                ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                size = i2;
                            }
                        }
                        request.setServletName(servletName);
                        request.setRoleMap(roleMap);
                        if (i != 4) {
                            request.setServletPath(servletPath);
                            request.setPathInfo(pathInfo);
                        }
                        throw th3;
                    }
                }
                servletRequestEvent = null;
                if (servletHolder != null) {
                    try {
                        request.setHandled(true);
                        if (filterChain != null) {
                            filterChain.doFilter(httpServletRequest, httpServletResponse);
                        } else {
                            servletHolder.handle(httpServletRequest, httpServletResponse);
                        }
                    } catch (RetryRequest e14) {
                        e = e14;
                        obj = takeRequestListeners;
                        servletRequestEvent2 = servletRequestEvent;
                        request.setHandled(false);
                        throw e;
                    } catch (EofException e15) {
                        e2 = e15;
                        throw e2;
                    } catch (RuntimeIOException e16) {
                        e3 = e16;
                        throw e3;
                    } catch (Exception e17) {
                        th6 = e17;
                        if (i != 1) {
                            if (!(th6 instanceof IOException)) {
                                throw ((IOException) th6);
                            } else if (!(th6 instanceof RuntimeException)) {
                                throw ((RuntimeException) th6);
                            } else if (th6 instanceof ServletException) {
                                throw ((ServletException) th6);
                            }
                        }
                        if (!(th6 instanceof UnavailableException)) {
                            Log.debug(th6);
                        } else if (th6 instanceof ServletException) {
                            Log.debug(th6);
                            th22 = ((ServletException) th6).getRootCause();
                            if (!(th22 == th6 || th22 == null)) {
                                th6 = th22;
                            }
                        }
                        if (!(th6 instanceof RetryRequest)) {
                            request.setHandled(false);
                            throw ((RetryRequest) th6);
                        } else if (!(th6 instanceof HttpException)) {
                            throw ((HttpException) th6);
                        } else if (!(th6 instanceof RuntimeIOException)) {
                            throw ((RuntimeIOException) th6);
                        } else if (th6 instanceof EofException) {
                            throw ((EofException) th6);
                        } else {
                            if (Log.isDebugEnabled()) {
                                Log.warn(httpServletRequest.getRequestURI(), th6);
                                Log.debug(httpServletRequest.toString());
                            } else if ((th6 instanceof IOException) || (th6 instanceof UnavailableException)) {
                                Log.warn(new StringBuffer().append(httpServletRequest.getRequestURI()).append(": ").append(th6).toString());
                            } else {
                                Log.warn(httpServletRequest.getRequestURI(), th6);
                            }
                            if (httpServletResponse.isCommitted()) {
                                httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION_TYPE, th6.getClass());
                                httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION, th6);
                                if (!(th6 instanceof UnavailableException)) {
                                    httpServletResponse.sendError(500, th6.getMessage());
                                } else if (((UnavailableException) th6).isPermanent()) {
                                    httpServletResponse.sendError(404, th6.getMessage());
                                } else {
                                    httpServletResponse.sendError(503, th6.getMessage());
                                }
                            } else if (Log.isDebugEnabled()) {
                                Log.debug(new StringBuffer().append("Response already committed for handling ").append(th6).toString());
                            }
                            if (takeRequestListeners != null) {
                                size = LazyList.size(takeRequestListeners);
                                while (true) {
                                    i2 = size - 1;
                                    if (size <= 0) {
                                        break;
                                    }
                                    ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                    size = i2;
                                }
                            }
                            request.setServletName(servletName);
                            request.setRoleMap(roleMap);
                            if (i == 4) {
                                request.setServletPath(servletPath);
                                request.setPathInfo(pathInfo);
                            }
                        }
                    } catch (Error e18) {
                        th6 = e18;
                        if (i == 1) {
                            throw th6;
                        }
                        Log.warn(new StringBuffer().append("Error for ").append(httpServletRequest.getRequestURI()).toString(), th6);
                        if (Log.isDebugEnabled()) {
                            Log.debug(httpServletRequest.toString());
                        }
                        if (httpServletResponse.isCommitted()) {
                            httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION_TYPE, th6.getClass());
                            httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION, th6);
                            httpServletResponse.sendError(500, th6.getMessage());
                        } else if (Log.isDebugEnabled()) {
                            Log.debug("Response already committed for handling ", th6);
                        }
                        if (takeRequestListeners != null) {
                            size = LazyList.size(takeRequestListeners);
                            while (true) {
                                i2 = size - 1;
                                if (size <= 0) {
                                    break;
                                }
                                ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                                size = i2;
                            }
                        }
                        request.setServletName(servletName);
                        request.setRoleMap(roleMap);
                        if (i == 4) {
                            request.setServletPath(servletPath);
                            request.setPathInfo(pathInfo);
                            return;
                        }
                        return;
                    }
                }
                notFound(httpServletRequest, httpServletResponse);
                if (takeRequestListeners != null) {
                    size = LazyList.size(takeRequestListeners);
                    while (true) {
                        i2 = size - 1;
                        if (size <= 0) {
                            break;
                        }
                        ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                        size = i2;
                    }
                }
                request.setServletName(servletName);
                request.setRoleMap(roleMap);
                if (i != 4) {
                    request.setServletPath(servletPath);
                    request.setPathInfo(pathInfo);
                }
            } catch (RetryRequest e19) {
                e = e19;
                obj = null;
                servletRequestEvent2 = null;
                request.setHandled(false);
                throw e;
            } catch (EofException e20) {
                e2 = e20;
                takeRequestListeners = null;
                servletRequestEvent = null;
                throw e2;
            } catch (RuntimeIOException e21) {
                e3 = e21;
                takeRequestListeners = null;
                servletRequestEvent = null;
                throw e3;
            } catch (Exception e22) {
                th6 = e22;
                takeRequestListeners = null;
                servletRequestEvent = null;
                if (i != 1) {
                    if (!(th6 instanceof IOException)) {
                        throw ((IOException) th6);
                    } else if (!(th6 instanceof RuntimeException)) {
                        throw ((RuntimeException) th6);
                    } else if (th6 instanceof ServletException) {
                        throw ((ServletException) th6);
                    }
                }
                if (!(th6 instanceof UnavailableException)) {
                    Log.debug(th6);
                } else if (th6 instanceof ServletException) {
                    Log.debug(th6);
                    th22 = ((ServletException) th6).getRootCause();
                    th6 = th22;
                }
                if (!(th6 instanceof RetryRequest)) {
                    request.setHandled(false);
                    throw ((RetryRequest) th6);
                } else if (!(th6 instanceof HttpException)) {
                    throw ((HttpException) th6);
                } else if (!(th6 instanceof RuntimeIOException)) {
                    throw ((RuntimeIOException) th6);
                } else if (th6 instanceof EofException) {
                    if (Log.isDebugEnabled()) {
                        if (th6 instanceof IOException) {
                        }
                        Log.warn(new StringBuffer().append(httpServletRequest.getRequestURI()).append(": ").append(th6).toString());
                    } else {
                        Log.warn(httpServletRequest.getRequestURI(), th6);
                        Log.debug(httpServletRequest.toString());
                    }
                    if (httpServletResponse.isCommitted()) {
                        httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION_TYPE, th6.getClass());
                        httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION, th6);
                        if (!(th6 instanceof UnavailableException)) {
                            httpServletResponse.sendError(500, th6.getMessage());
                        } else if (((UnavailableException) th6).isPermanent()) {
                            httpServletResponse.sendError(503, th6.getMessage());
                        } else {
                            httpServletResponse.sendError(404, th6.getMessage());
                        }
                    } else if (Log.isDebugEnabled()) {
                        Log.debug(new StringBuffer().append("Response already committed for handling ").append(th6).toString());
                    }
                    if (takeRequestListeners != null) {
                        size = LazyList.size(takeRequestListeners);
                        while (true) {
                            i2 = size - 1;
                            if (size <= 0) {
                                break;
                            }
                            ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                            size = i2;
                        }
                    }
                    request.setServletName(servletName);
                    request.setRoleMap(roleMap);
                    if (i == 4) {
                        request.setServletPath(servletPath);
                        request.setPathInfo(pathInfo);
                    }
                } else {
                    throw ((EofException) th6);
                }
            } catch (Error e23) {
                th6 = e23;
                takeRequestListeners = null;
                servletRequestEvent = null;
                if (i == 1) {
                    Log.warn(new StringBuffer().append("Error for ").append(httpServletRequest.getRequestURI()).toString(), th6);
                    if (Log.isDebugEnabled()) {
                        Log.debug(httpServletRequest.toString());
                    }
                    if (httpServletResponse.isCommitted()) {
                        httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION_TYPE, th6.getClass());
                        httpServletRequest.setAttribute(__J_S_ERROR_EXCEPTION, th6);
                        httpServletResponse.sendError(500, th6.getMessage());
                    } else if (Log.isDebugEnabled()) {
                        Log.debug("Response already committed for handling ", th6);
                    }
                    if (takeRequestListeners != null) {
                        size = LazyList.size(takeRequestListeners);
                        while (true) {
                            i2 = size - 1;
                            if (size <= 0) {
                                break;
                            }
                            ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                            size = i2;
                        }
                    }
                    request.setServletName(servletName);
                    request.setRoleMap(roleMap);
                    if (i == 4) {
                        request.setServletPath(servletPath);
                        request.setPathInfo(pathInfo);
                        return;
                    }
                    return;
                }
                throw th6;
            } catch (Throwable th7) {
                th6 = th7;
                takeRequestListeners = null;
                servletRequestEvent = null;
                th3 = th6;
                if (takeRequestListeners != null) {
                    size = LazyList.size(takeRequestListeners);
                    while (true) {
                        i2 = size - 1;
                        if (size <= 0) {
                            break;
                        }
                        ((ServletRequestListener) LazyList.get(takeRequestListeners, i2)).requestDestroyed(servletRequestEvent);
                        size = i2;
                    }
                }
                request.setServletName(servletName);
                request.setRoleMap(roleMap);
                if (i != 4) {
                    request.setServletPath(servletPath);
                    request.setPathInfo(pathInfo);
                }
                throw th3;
            }
        }
    }

    public void initialize() throws Exception {
        MultiException multiException = new MultiException();
        if (this._filters != null) {
            for (FilterHolder start : this._filters) {
                start.start();
            }
        }
        if (this._servlets != null) {
            ServletHolder[] servletHolderArr = (ServletHolder[]) this._servlets.clone();
            Arrays.sort(servletHolderArr);
            int i = 0;
            while (i < servletHolderArr.length) {
                try {
                    if (servletHolderArr[i].getClassName() == null && servletHolderArr[i].getForcedPath() != null) {
                        ServletHolder servletHolder = (ServletHolder) this._servletPathMap.match(servletHolderArr[i].getForcedPath());
                        if (servletHolder == null || servletHolder.getClassName() == null) {
                            multiException.add(new IllegalStateException(new StringBuffer().append("No forced path servlet for ").append(servletHolderArr[i].getForcedPath()).toString()));
                            i++;
                        } else {
                            servletHolderArr[i].setClassName(servletHolder.getClassName());
                        }
                    }
                    servletHolderArr[i].start();
                } catch (Throwable th) {
                    Log.debug(Log.EXCEPTION, th);
                    multiException.add(th);
                }
                i++;
            }
            multiException.ifExceptionThrow();
        }
    }

    public boolean isAvailable() {
        if (!isStarted()) {
            return false;
        }
        ServletHolder[] servlets = getServlets();
        for (ServletHolder servletHolder : servlets) {
            if (servletHolder != null && !servletHolder.isAvailable()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFilterChainsCached() {
        return this._filterChainsCached;
    }

    public boolean isInitializeAtStart() {
        return false;
    }

    public boolean isStartWithUnavailable() {
        return this._startWithUnavailable;
    }

    public boolean matchesPath(String str) {
        return this._servletPathMap.containsMatch(str);
    }

    public FilterHolder newFilterHolder() {
        return new FilterHolder();
    }

    public FilterHolder newFilterHolder(Class cls) {
        return new FilterHolder(cls);
    }

    public ServletHolder newServletHolder() {
        return new ServletHolder();
    }

    public ServletHolder newServletHolder(Class cls) {
        return new ServletHolder(cls);
    }

    protected void notFound(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("Not Found ").append(httpServletRequest.getRequestURI()).toString());
        }
        httpServletResponse.sendError(404);
    }

    public void setFilterChainsCached(boolean z) {
        this._filterChainsCached = z;
    }

    public void setFilterMappings(FilterMapping[] filterMappingArr) {
        if (getServer() != null) {
            getServer().getContainer().update((Object) this, this._filterMappings, (Object[]) filterMappingArr, "filterMapping", true);
        }
        this._filterMappings = filterMappingArr;
        updateMappings();
        invalidateChainsCache();
    }

    public void setFilters(FilterHolder[] filterHolderArr) {
        synchronized (this) {
            if (getServer() != null) {
                getServer().getContainer().update((Object) this, this._filters, (Object[]) filterHolderArr, "filter", true);
            }
            this._filters = filterHolderArr;
            updateNameMappings();
            invalidateChainsCache();
        }
    }

    public void setInitializeAtStart(boolean z) {
    }

    public void setMaxFilterChainsCacheSize(int i) {
        this._maxFilterChainsCacheSize = i;
        int i2 = 0;
        while (i2 < this._chainCache.length) {
            if (this._chainCache[i2] != null && (this._chainCache[i2] instanceof MruCache)) {
                this._chainCache[i2].setMaxEntries(i);
            }
            i2++;
        }
    }

    public void setServer(Server server) {
        if (!(getServer() == null || getServer() == server)) {
            getServer().getContainer().update((Object) this, this._filters, null, "filter", true);
            getServer().getContainer().update((Object) this, this._filterMappings, null, "filterMapping", true);
            getServer().getContainer().update((Object) this, this._servlets, null, "servlet", true);
            getServer().getContainer().update((Object) this, this._servletMappings, null, "servletMapping", true);
        }
        if (!(server == null || getServer() == server)) {
            server.getContainer().update((Object) this, null, this._filters, "filter", true);
            server.getContainer().update((Object) this, null, this._filterMappings, "filterMapping", true);
            server.getContainer().update((Object) this, null, this._servlets, "servlet", true);
            server.getContainer().update((Object) this, null, this._servletMappings, "servletMapping", true);
        }
        super.setServer(server);
        invalidateChainsCache();
    }

    public void setServletMappings(ServletMapping[] servletMappingArr) {
        if (getServer() != null) {
            getServer().getContainer().update((Object) this, this._servletMappings, (Object[]) servletMappingArr, "servletMapping", true);
        }
        this._servletMappings = servletMappingArr;
        updateMappings();
        invalidateChainsCache();
    }

    public void setServlets(ServletHolder[] servletHolderArr) {
        synchronized (this) {
            if (getServer() != null) {
                getServer().getContainer().update((Object) this, this._servlets, (Object[]) servletHolderArr, "servlet", true);
            }
            this._servlets = servletHolderArr;
            updateNameMappings();
            invalidateChainsCache();
        }
    }

    public void setStartWithUnavailable(boolean z) {
        this._startWithUnavailable = z;
    }

    protected void updateMappings() {
        synchronized (this) {
            int i;
            if (this._filterMappings == null) {
                this._filterPathMappings = null;
                this._filterNameMappings = null;
            } else {
                this._filterPathMappings = new ArrayList();
                this._filterNameMappings = new MultiMap();
                for (i = 0; i < this._filterMappings.length; i++) {
                    FilterHolder filterHolder = (FilterHolder) this._filterNameMap.get(this._filterMappings[i].getFilterName());
                    if (filterHolder == null) {
                        throw new IllegalStateException(new StringBuffer().append("No filter named ").append(this._filterMappings[i].getFilterName()).toString());
                    }
                    this._filterMappings[i].setFilterHolder(filterHolder);
                    if (this._filterMappings[i].getPathSpecs() != null) {
                        this._filterPathMappings.add(this._filterMappings[i]);
                    }
                    if (this._filterMappings[i].getServletNames() != null) {
                        String[] servletNames = this._filterMappings[i].getServletNames();
                        for (int i2 = 0; i2 < servletNames.length; i2++) {
                            if (servletNames[i2] != null) {
                                this._filterNameMappings.add(servletNames[i2], this._filterMappings[i]);
                            }
                        }
                        continue;
                    }
                }
            }
            if (this._servletMappings == null || this._servletNameMap == null) {
                this._servletPathMap = null;
            } else {
                PathMap pathMap = new PathMap();
                for (int i3 = 0; i3 < this._servletMappings.length; i3++) {
                    ServletHolder servletHolder = (ServletHolder) this._servletNameMap.get(this._servletMappings[i3].getServletName());
                    if (servletHolder == null) {
                        throw new IllegalStateException(new StringBuffer().append("No such servlet: ").append(this._servletMappings[i3].getServletName()).toString());
                    }
                    if (this._servletMappings[i3].getPathSpecs() != null) {
                        String[] pathSpecs = this._servletMappings[i3].getPathSpecs();
                        for (i = 0; i < pathSpecs.length; i++) {
                            if (pathSpecs[i] != null) {
                                pathMap.put(pathSpecs[i], servletHolder);
                            }
                        }
                        continue;
                    }
                }
                this._servletPathMap = pathMap;
            }
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("filterNameMap=").append(this._filterNameMap).toString());
                Log.debug(new StringBuffer().append("pathFilters=").append(this._filterPathMappings).toString());
                Log.debug(new StringBuffer().append("servletFilterMap=").append(this._filterNameMappings).toString());
                Log.debug(new StringBuffer().append("servletPathMap=").append(this._servletPathMap).toString());
                Log.debug(new StringBuffer().append("servletNameMap=").append(this._servletNameMap).toString());
            }
            try {
                if (isStarted()) {
                    initialize();
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void updateNameMappings() {
        int i = 0;
        synchronized (this) {
            this._filterNameMap.clear();
            if (this._filters != null) {
                for (int i2 = 0; i2 < this._filters.length; i2++) {
                    this._filterNameMap.put(this._filters[i2].getName(), this._filters[i2]);
                    this._filters[i2].setServletHandler(this);
                }
            }
            this._servletNameMap.clear();
            if (this._servlets != null) {
                while (i < this._servlets.length) {
                    this._servletNameMap.put(this._servlets[i].getName(), this._servlets[i]);
                    this._servlets[i].setServletHandler(this);
                    i++;
                }
            }
        }
    }
}
