package org.mortbay.jetty.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.io.Buffer;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpException;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.Server.Graceful;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.log.Log;
import org.mortbay.log.Logger;
import org.mortbay.resource.Resource;
import org.mortbay.util.Attributes;
import org.mortbay.util.AttributesMap;
import org.mortbay.util.LazyList;
import org.mortbay.util.Loader;
import org.mortbay.util.QuotedStringTokenizer;
import org.mortbay.util.URIUtil;

public class ContextHandler extends HandlerWrapper implements Attributes, Graceful {
    public static final String MANAGED_ATTRIBUTES = "org.mortbay.jetty.servlet.ManagedAttributes";
    private static ThreadLocal __context = new ThreadLocal();
    static Class class$java$util$EventListener;
    static Class class$org$mortbay$jetty$handler$ContextHandler;
    static Class class$org$mortbay$jetty$handler$ContextHandlerCollection;
    private boolean _allowNullPathInfo;
    private AttributesMap _attributes;
    private Resource _baseResource;
    private ClassLoader _classLoader;
    private boolean _compactPath;
    private Set _connectors;
    private Object _contextAttributeListeners;
    private AttributesMap _contextAttributes;
    private Object _contextListeners;
    private String _contextPath;
    private String _displayName;
    private ErrorHandler _errorHandler;
    private EventListener[] _eventListeners;
    private Map _initParams;
    private Map _localeEncodingMap;
    private Logger _logger;
    private Set _managedAttributes;
    private int _maxFormContentSize;
    private MimeTypes _mimeTypes;
    private Object _requestAttributeListeners;
    private Object _requestListeners;
    protected SContext _scontext;
    private boolean _shutdown;
    private String[] _vhosts;
    private String[] _welcomeFiles;

    public class SContext implements ServletContext {
        private final ContextHandler this$0;

        protected SContext(ContextHandler contextHandler) {
            this.this$0 = contextHandler;
        }

        public Object getAttribute(String str) {
            Object attribute;
            synchronized (this) {
                attribute = this.this$0.getAttribute(str);
                if (attribute == null && ContextHandler.access$200(this.this$0) != null) {
                    attribute = ContextHandler.access$200(this.this$0).getAttribute(str);
                }
            }
            return attribute;
        }

        public Enumeration getAttributeNames() {
            Enumeration enumeration;
            synchronized (this) {
                Enumeration attributeNames;
                Collection hashSet = new HashSet();
                if (ContextHandler.access$200(this.this$0) != null) {
                    attributeNames = ContextHandler.access$200(this.this$0).getAttributeNames();
                    while (attributeNames.hasMoreElements()) {
                        hashSet.add(attributeNames.nextElement());
                    }
                }
                attributeNames = ContextHandler.access$300(this.this$0).getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    hashSet.add(attributeNames.nextElement());
                }
                enumeration = Collections.enumeration(hashSet);
            }
            return enumeration;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public javax.servlet.ServletContext getContext(java.lang.String r9) {
            /*
            r8 = this;
            r2 = 0;
            r0 = r8.this$0;
            r1 = r0.getServer();
            r0 = org.mortbay.jetty.handler.ContextHandler.class$org$mortbay$jetty$handler$ContextHandler;
            if (r0 != 0) goto L_0x002f;
        L_0x000b:
            r0 = "org.mortbay.jetty.handler.ContextHandler";
            r0 = org.mortbay.jetty.handler.ContextHandler.class$(r0);
            org.mortbay.jetty.handler.ContextHandler.class$org$mortbay$jetty$handler$ContextHandler = r0;
        L_0x0013:
            r4 = r1.getChildHandlersByClass(r0);
            r0 = 0;
            r1 = r2;
            r3 = r0;
        L_0x001a:
            r0 = r4.length;
            if (r3 >= r0) goto L_0x0064;
        L_0x001d:
            r0 = r4[r3];
            if (r0 == 0) goto L_0x0062;
        L_0x0021:
            r0 = r4[r3];
            r0 = r0.isStarted();
            if (r0 != 0) goto L_0x0032;
        L_0x0029:
            r0 = r1;
        L_0x002a:
            r1 = r3 + 1;
            r3 = r1;
            r1 = r0;
            goto L_0x001a;
        L_0x002f:
            r0 = org.mortbay.jetty.handler.ContextHandler.class$org$mortbay$jetty$handler$ContextHandler;
            goto L_0x0013;
        L_0x0032:
            r0 = r4[r3];
            r0 = (org.mortbay.jetty.handler.ContextHandler) r0;
            r5 = r0.getContextPath();
            r6 = r9.equals(r5);
            if (r6 != 0) goto L_0x0052;
        L_0x0040:
            r6 = r9.startsWith(r5);
            if (r6 == 0) goto L_0x0062;
        L_0x0046:
            r6 = r5.length();
            r6 = r9.charAt(r6);
            r7 = 47;
            if (r6 != r7) goto L_0x0062;
        L_0x0052:
            if (r1 == 0) goto L_0x002a;
        L_0x0054:
            r5 = r5.length();
            r6 = r1.getContextPath();
            r6 = r6.length();
            if (r5 > r6) goto L_0x002a;
        L_0x0062:
            r0 = r1;
            goto L_0x002a;
        L_0x0064:
            if (r1 == 0) goto L_0x0068;
        L_0x0066:
            r2 = r1._scontext;
        L_0x0068:
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.handler.ContextHandler.SContext.getContext(java.lang.String):javax.servlet.ServletContext");
        }

        public ContextHandler getContextHandler() {
            return this.this$0;
        }

        public String getContextPath() {
            return (ContextHandler.access$600(this.this$0) == null || !ContextHandler.access$600(this.this$0).equals(URIUtil.SLASH)) ? ContextHandler.access$600(this.this$0) : HttpVersions.HTTP_0_9;
        }

        public String getInitParameter(String str) {
            return this.this$0.getInitParameter(str);
        }

        public Enumeration getInitParameterNames() {
            return this.this$0.getInitParameterNames();
        }

        public int getMajorVersion() {
            return 2;
        }

        public String getMimeType(String str) {
            if (ContextHandler.access$000(this.this$0) != null) {
                Buffer mimeByExtension = ContextHandler.access$000(this.this$0).getMimeByExtension(str);
                if (mimeByExtension != null) {
                    return mimeByExtension.toString();
                }
            }
            return null;
        }

        public int getMinorVersion() {
            return 5;
        }

        public RequestDispatcher getNamedDispatcher(String str) {
            return null;
        }

        public String getRealPath(String str) {
            String str2 = null;
            if (str != null) {
                if (str.length() == 0) {
                    str = URIUtil.SLASH;
                } else if (str.charAt(0) != '/') {
                    str = new StringBuffer().append(URIUtil.SLASH).append(str).toString();
                }
                try {
                    Resource resource = this.this$0.getResource(str);
                    if (resource != null) {
                        File file = resource.getFile();
                        if (file != null) {
                            str2 = file.getCanonicalPath();
                        }
                    }
                } catch (Throwable e) {
                    Log.ignore(e);
                }
            }
            return str2;
        }

        public RequestDispatcher getRequestDispatcher(String str) {
            return null;
        }

        public URL getResource(String str) throws MalformedURLException {
            Resource resource = this.this$0.getResource(str);
            return (resource == null || !resource.exists()) ? null : resource.getURL();
        }

        public InputStream getResourceAsStream(String str) {
            InputStream inputStream = null;
            try {
                URL resource = getResource(str);
                if (resource != null) {
                    inputStream = resource.openStream();
                }
            } catch (Throwable e) {
                Log.ignore(e);
            }
            return inputStream;
        }

        public Set getResourcePaths(String str) {
            return this.this$0.getResourcePaths(str);
        }

        public String getServerInfo() {
            return new StringBuffer().append("jetty/").append(Server.getVersion()).toString();
        }

        public Servlet getServlet(String str) throws ServletException {
            return null;
        }

        public String getServletContextName() {
            String displayName = this.this$0.getDisplayName();
            return displayName == null ? this.this$0.getContextPath() : displayName;
        }

        public Enumeration getServletNames() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        public Enumeration getServlets() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        public void log(Exception exception, String str) {
            ContextHandler.access$100(this.this$0).warn(str, exception);
        }

        public void log(String str) {
            ContextHandler.access$100(this.this$0).info(str, null, null);
        }

        public void log(String str, Throwable th) {
            ContextHandler.access$100(this.this$0).warn(str, th);
        }

        public void removeAttribute(String str) {
            synchronized (this) {
                ContextHandler.access$400(this.this$0, str, null);
                if (ContextHandler.access$200(this.this$0) == null) {
                    ContextHandler.access$300(this.this$0).removeAttribute(str);
                } else {
                    Object attribute = ContextHandler.access$200(this.this$0).getAttribute(str);
                    ContextHandler.access$200(this.this$0).removeAttribute(str);
                    if (!(attribute == null || ContextHandler.access$500(this.this$0) == null)) {
                        ServletContextAttributeEvent servletContextAttributeEvent = new ServletContextAttributeEvent(this.this$0._scontext, str, attribute);
                        for (int i = 0; i < LazyList.size(ContextHandler.access$500(this.this$0)); i++) {
                            ((ServletContextAttributeListener) LazyList.get(ContextHandler.access$500(this.this$0), i)).attributeRemoved(servletContextAttributeEvent);
                        }
                    }
                }
            }
        }

        public void setAttribute(String str, Object obj) {
            synchronized (this) {
                if (ContextHandler.access$200(this.this$0) == null) {
                    this.this$0.setAttribute(str, obj);
                } else {
                    ContextHandler.access$400(this.this$0, str, obj);
                    Object attribute = ContextHandler.access$200(this.this$0).getAttribute(str);
                    if (obj == null) {
                        ContextHandler.access$200(this.this$0).removeAttribute(str);
                    } else {
                        ContextHandler.access$200(this.this$0).setAttribute(str, obj);
                    }
                    if (ContextHandler.access$500(this.this$0) != null) {
                        ServletContextAttributeEvent servletContextAttributeEvent = new ServletContextAttributeEvent(this.this$0._scontext, str, attribute == null ? obj : attribute);
                        for (int i = 0; i < LazyList.size(ContextHandler.access$500(this.this$0)); i++) {
                            ServletContextAttributeListener servletContextAttributeListener = (ServletContextAttributeListener) LazyList.get(ContextHandler.access$500(this.this$0), i);
                            if (attribute == null) {
                                servletContextAttributeListener.attributeAdded(servletContextAttributeEvent);
                            } else if (obj == null) {
                                servletContextAttributeListener.attributeRemoved(servletContextAttributeEvent);
                            } else {
                                servletContextAttributeListener.attributeReplaced(servletContextAttributeEvent);
                            }
                        }
                    }
                }
            }
        }

        public String toString() {
            return new StringBuffer().append("ServletContext@").append(Integer.toHexString(hashCode())).append("{").append(getContextPath().equals(HttpVersions.HTTP_0_9) ? URIUtil.SLASH : getContextPath()).append(",").append(this.this$0.getBaseResource()).append("}").toString();
        }
    }

    public ContextHandler() {
        this._contextPath = URIUtil.SLASH;
        this._maxFormContentSize = Integer.getInteger("org.mortbay.jetty.Request.maxFormContentSize", 200000).intValue();
        this._compactPath = false;
        this._scontext = new SContext(this);
        this._attributes = new AttributesMap();
        this._initParams = new HashMap();
    }

    public ContextHandler(String str) {
        this();
        setContextPath(str);
    }

    public ContextHandler(HandlerContainer handlerContainer, String str) {
        this();
        setContextPath(str);
        handlerContainer.addHandler(this);
    }

    protected ContextHandler(SContext sContext) {
        this._contextPath = URIUtil.SLASH;
        this._maxFormContentSize = Integer.getInteger("org.mortbay.jetty.Request.maxFormContentSize", 200000).intValue();
        this._compactPath = false;
        this._scontext = sContext;
        this._attributes = new AttributesMap();
        this._initParams = new HashMap();
    }

    static MimeTypes access$000(ContextHandler contextHandler) {
        return contextHandler._mimeTypes;
    }

    static Logger access$100(ContextHandler contextHandler) {
        return contextHandler._logger;
    }

    static AttributesMap access$200(ContextHandler contextHandler) {
        return contextHandler._contextAttributes;
    }

    static AttributesMap access$300(ContextHandler contextHandler) {
        return contextHandler._attributes;
    }

    static void access$400(ContextHandler contextHandler, String str, Object obj) {
        contextHandler.setManagedAttribute(str, obj);
    }

    static Object access$500(ContextHandler contextHandler) {
        return contextHandler._contextAttributeListeners;
    }

    static String access$600(ContextHandler contextHandler) {
        return contextHandler._contextPath;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public static SContext getCurrentContext() {
        return (SContext) __context.get();
    }

    private String normalizeHostname(String str) {
        return str == null ? null : str.endsWith(".") ? str.substring(0, str.length() - 1) : str;
    }

    private void setManagedAttribute(String str, Object obj) {
        if (this._managedAttributes != null && this._managedAttributes.contains(str)) {
            Object attribute = this._scontext.getAttribute(str);
            if (attribute != null) {
                getServer().getContainer().removeBean(attribute);
            }
            if (obj != null) {
                getServer().getContainer().addBean(obj);
            }
        }
    }

    public void addEventListener(EventListener eventListener) {
        Class class$;
        EventListener[] eventListeners = getEventListeners();
        if (class$java$util$EventListener == null) {
            class$ = class$("java.util.EventListener");
            class$java$util$EventListener = class$;
        } else {
            class$ = class$java$util$EventListener;
        }
        setEventListeners((EventListener[]) LazyList.addToArray(eventListeners, eventListener, class$));
    }

    public void addLocaleEncoding(String str, String str2) {
        if (this._localeEncodingMap == null) {
            this._localeEncodingMap = new HashMap();
        }
        this._localeEncodingMap.put(str, str2);
    }

    public void clearAttributes() {
        Enumeration attributeNames = this._attributes.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            setManagedAttribute((String) attributeNames.nextElement(), null);
        }
        this._attributes.clearAttributes();
    }

    protected void doStart() throws Exception {
        Throwable th;
        Thread thread;
        ClassLoader classLoader;
        Throwable th2;
        SContext sContext;
        Object obj = null;
        if (this._contextPath == null) {
            throw new IllegalStateException("Null contextPath");
        }
        this._logger = Log.getLogger(getDisplayName() == null ? getContextPath() : getDisplayName());
        this._contextAttributes = new AttributesMap();
        try {
            if (this._classLoader != null) {
                ClassLoader contextClassLoader;
                Thread currentThread = Thread.currentThread();
                try {
                    contextClassLoader = currentThread.getContextClassLoader();
                } catch (Throwable th3) {
                    th = th3;
                    thread = currentThread;
                    classLoader = null;
                    __context.set(obj);
                    if (this._classLoader != null) {
                        thread.setContextClassLoader(classLoader);
                    }
                    throw th;
                }
                try {
                    currentThread.setContextClassLoader(this._classLoader);
                    thread = currentThread;
                    classLoader = contextClassLoader;
                } catch (Throwable th4) {
                    th2 = th4;
                    thread = currentThread;
                    classLoader = contextClassLoader;
                    th = th2;
                    __context.set(obj);
                    if (this._classLoader != null) {
                        thread.setContextClassLoader(classLoader);
                    }
                    throw th;
                }
            }
            classLoader = null;
            thread = null;
            try {
                if (this._mimeTypes == null) {
                    this._mimeTypes = new MimeTypes();
                }
                sContext = (SContext) __context.get();
            } catch (Throwable th5) {
                th = th5;
                __context.set(obj);
                if (this._classLoader != null) {
                    thread.setContextClassLoader(classLoader);
                }
                throw th;
            }
            try {
                __context.set(this._scontext);
                if (this._errorHandler == null) {
                    setErrorHandler(new ErrorHandler());
                }
                startContext();
                __context.set(sContext);
                if (this._classLoader != null) {
                    thread.setContextClassLoader(classLoader);
                }
            } catch (Throwable th6) {
                th2 = th6;
                SContext sContext2 = sContext;
                th = th2;
                __context.set(obj);
                if (this._classLoader != null) {
                    thread.setContextClassLoader(classLoader);
                }
                throw th;
            }
        } catch (Throwable th7) {
            th = th7;
            classLoader = null;
            thread = null;
            __context.set(obj);
            if (this._classLoader != null) {
                thread.setContextClassLoader(classLoader);
            }
            throw th;
        }
    }

    protected void doStop() throws Exception {
        Thread thread;
        ClassLoader classLoader;
        Throwable th;
        SContext sContext = (SContext) __context.get();
        __context.set(this._scontext);
        try {
            if (this._classLoader != null) {
                Thread currentThread = Thread.currentThread();
                try {
                    ClassLoader contextClassLoader = currentThread.getContextClassLoader();
                    try {
                        currentThread.setContextClassLoader(this._classLoader);
                        thread = currentThread;
                        classLoader = contextClassLoader;
                    } catch (Throwable th2) {
                        thread = currentThread;
                        classLoader = contextClassLoader;
                        th = th2;
                        __context.set(sContext);
                        if (this._classLoader != null) {
                            thread.setContextClassLoader(classLoader);
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    thread = currentThread;
                    classLoader = null;
                    __context.set(sContext);
                    if (this._classLoader != null) {
                        thread.setContextClassLoader(classLoader);
                    }
                    throw th;
                }
            }
            classLoader = null;
            thread = null;
            try {
                super.doStop();
                if (this._contextListeners != null) {
                    ServletContextEvent servletContextEvent = new ServletContextEvent(this._scontext);
                    int size = LazyList.size(this._contextListeners);
                    while (true) {
                        int i = size - 1;
                        if (size <= 0) {
                            break;
                        }
                        ((ServletContextListener) LazyList.get(this._contextListeners, i)).contextDestroyed(servletContextEvent);
                        size = i;
                    }
                }
                if (this._errorHandler != null) {
                    this._errorHandler.stop();
                }
                Enumeration attributeNames = this._scontext.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    setManagedAttribute((String) attributeNames.nextElement(), null);
                }
                __context.set(sContext);
                if (this._classLoader != null) {
                    thread.setContextClassLoader(classLoader);
                }
                if (this._contextAttributes != null) {
                    this._contextAttributes.clearAttributes();
                }
                this._contextAttributes = null;
            } catch (Throwable th4) {
                th = th4;
                __context.set(sContext);
                if (this._classLoader != null) {
                    thread.setContextClassLoader(classLoader);
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            classLoader = null;
            thread = null;
            __context.set(sContext);
            if (this._classLoader != null) {
                thread.setContextClassLoader(classLoader);
            }
            throw th;
        }
    }

    public boolean getAllowNullPathInfo() {
        return this._allowNullPathInfo;
    }

    public Object getAttribute(String str) {
        return this._attributes.getAttribute(str);
    }

    public Enumeration getAttributeNames() {
        return AttributesMap.getAttributeNamesCopy(this._attributes);
    }

    public Attributes getAttributes() {
        return this._attributes;
    }

    public Resource getBaseResource() {
        return this._baseResource == null ? null : this._baseResource;
    }

    public ClassLoader getClassLoader() {
        return this._classLoader;
    }

    public String getClassPath() {
        if (this._classLoader != null && (this._classLoader instanceof URLClassLoader)) {
            URL[] uRLs = ((URLClassLoader) this._classLoader).getURLs();
            StringBuffer stringBuffer = new StringBuffer();
            for (URL newResource : uRLs) {
                try {
                    File file = Resource.newResource(newResource).getFile();
                    if (file.exists()) {
                        if (stringBuffer.length() > 0) {
                            stringBuffer.append(File.pathSeparatorChar);
                        }
                        stringBuffer.append(file.getAbsolutePath());
                    }
                } catch (Throwable e) {
                    Log.debug(e);
                }
            }
            if (stringBuffer.length() != 0) {
                return stringBuffer.toString();
            }
        }
        return null;
    }

    public String[] getConnectorNames() {
        return (this._connectors == null || this._connectors.size() == 0) ? null : (String[]) this._connectors.toArray(new String[this._connectors.size()]);
    }

    public String getContextPath() {
        return this._contextPath;
    }

    public String getDisplayName() {
        return this._displayName;
    }

    public ErrorHandler getErrorHandler() {
        return this._errorHandler;
    }

    public EventListener[] getEventListeners() {
        return this._eventListeners;
    }

    public String[] getHosts() {
        return getConnectorNames();
    }

    public String getInitParameter(String str) {
        return (String) this._initParams.get(str);
    }

    public Enumeration getInitParameterNames() {
        return Collections.enumeration(this._initParams.keySet());
    }

    public Map getInitParams() {
        return this._initParams;
    }

    public String getLocaleEncoding(Locale locale) {
        if (this._localeEncodingMap == null) {
            return null;
        }
        String str = (String) this._localeEncodingMap.get(locale.toString());
        return str == null ? (String) this._localeEncodingMap.get(locale.getLanguage()) : str;
    }

    public int getMaxFormContentSize() {
        return this._maxFormContentSize;
    }

    public MimeTypes getMimeTypes() {
        return this._mimeTypes;
    }

    public Resource getResource(String str) throws MalformedURLException {
        Resource resource = null;
        if (str == null || !str.startsWith(URIUtil.SLASH)) {
            throw new MalformedURLException(str);
        }
        if (this._baseResource != null) {
            try {
                resource = this._baseResource.addPath(URIUtil.canonicalPath(str));
            } catch (Throwable e) {
                Log.ignore(e);
            }
        }
        return resource;
    }

    public String getResourceBase() {
        return this._baseResource == null ? null : this._baseResource.toString();
    }

    public Set getResourcePaths(String str) {
        try {
            String canonicalPath = URIUtil.canonicalPath(str);
            Resource resource = getResource(canonicalPath);
            if (resource != null && resource.exists()) {
                String stringBuffer = !canonicalPath.endsWith(URIUtil.SLASH) ? new StringBuffer().append(canonicalPath).append(URIUtil.SLASH).toString() : canonicalPath;
                String[] list = resource.list();
                if (list != null) {
                    HashSet hashSet = new HashSet();
                    for (String append : list) {
                        hashSet.add(new StringBuffer().append(stringBuffer).append(append).toString());
                    }
                    return hashSet;
                }
            }
        } catch (Throwable e) {
            Log.ignore(e);
        }
        return Collections.EMPTY_SET;
    }

    public SContext getServletContext() {
        return this._scontext;
    }

    public String[] getVirtualHosts() {
        return this._vhosts;
    }

    public String[] getWelcomeFiles() {
        return this._welcomeFiles;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        String normalizeHostname;
        int i2;
        String str2;
        ClassLoader classLoader;
        Throwable th;
        String str3;
        Thread thread;
        String str4;
        int size;
        Object obj = null;
        Request request = httpServletRequest instanceof Request ? (Request) httpServletRequest : HttpConnection.getCurrentConnection().getRequest();
        if (isStarted() && !this._shutdown) {
            if (i != 1 || !request.isHandled()) {
                SContext context = request.getContext();
                if (context != this._scontext) {
                    String str5;
                    if (this._vhosts != null && this._vhosts.length > 0) {
                        normalizeHostname = normalizeHostname(httpServletRequest.getServerName());
                        boolean z = false;
                        int i3 = 0;
                        while (!z && i3 < this._vhosts.length) {
                            str5 = this._vhosts[i3];
                            boolean regionMatches = str5 == null ? z : str5.startsWith("*.") ? str5.regionMatches(true, 2, normalizeHostname, normalizeHostname.indexOf(".") + 1, str5.length() - 2) : str5.equalsIgnoreCase(normalizeHostname);
                            i3++;
                            z = regionMatches;
                        }
                        if (!z) {
                            return;
                        }
                    }
                    if (this._connectors != null && this._connectors.size() > 0) {
                        str5 = HttpConnection.getCurrentConnection().getConnector().getName();
                        if (str5 == null || !this._connectors.contains(str5)) {
                            return;
                        }
                    }
                    if (i == 1) {
                        if (this._compactPath) {
                            str = URIUtil.compactPath(str);
                        }
                        if (str.equals(this._contextPath)) {
                            if (!this._allowNullPathInfo) {
                                if (!str.endsWith(URIUtil.SLASH)) {
                                    request.setHandled(true);
                                    if (httpServletRequest.getQueryString() != null) {
                                        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL(new StringBuffer().append(URIUtil.addPaths(httpServletRequest.getRequestURI(), URIUtil.SLASH)).append("?").append(httpServletRequest.getQueryString()).toString()));
                                        return;
                                    } else {
                                        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL(URIUtil.addPaths(httpServletRequest.getRequestURI(), URIUtil.SLASH)));
                                        return;
                                    }
                                }
                            }
                            if (this._contextPath.length() > 1) {
                                str = URIUtil.SLASH;
                                httpServletRequest.setAttribute("org.mortbay.jetty.nullPathInfo", URIUtil.SLASH);
                                obj = 1;
                            }
                        } else {
                            if (str.startsWith(this._contextPath)) {
                                if (this._contextPath.length() != 1) {
                                    if (str.charAt(this._contextPath.length()) != '/') {
                                        return;
                                    }
                                }
                                if (this._contextPath.length() > 1) {
                                    str = str.substring(this._contextPath.length());
                                    i2 = 1;
                                }
                            } else {
                                return;
                            }
                        }
                    }
                    i2 = 1;
                }
                try {
                    String contextPath = request.getContextPath();
                    try {
                        String servletPath = request.getServletPath();
                        try {
                            String pathInfo = request.getPathInfo();
                            try {
                                Thread thread2;
                                ClassLoader classLoader2;
                                request.setContext(this._scontext);
                                if (i != 4) {
                                    if (str.startsWith(URIUtil.SLASH)) {
                                        if (this._contextPath.length() == 1) {
                                            request.setContextPath(HttpVersions.HTTP_0_9);
                                        } else {
                                            request.setContextPath(this._contextPath);
                                        }
                                        request.setServletPath(null);
                                        request.setPathInfo(str);
                                    }
                                }
                                if (obj != null) {
                                    if (this._classLoader != null) {
                                        Thread currentThread = Thread.currentThread();
                                        try {
                                            ClassLoader contextClassLoader = currentThread.getContextClassLoader();
                                            try {
                                                currentThread.setContextClassLoader(this._classLoader);
                                                thread2 = currentThread;
                                                classLoader2 = contextClassLoader;
                                            } catch (Throwable th2) {
                                                normalizeHostname = contextPath;
                                                str2 = servletPath;
                                                classLoader = contextClassLoader;
                                                th = th2;
                                                str3 = str2;
                                                String str6 = pathInfo;
                                                thread = currentThread;
                                                str4 = str6;
                                                if (context != this._scontext) {
                                                    if (this._classLoader != null) {
                                                        thread.setContextClassLoader(classLoader);
                                                    }
                                                    request.setContext(context);
                                                    request.setContextPath(normalizeHostname);
                                                    request.setServletPath(str3);
                                                    request.setPathInfo(str4);
                                                }
                                                throw th;
                                            }
                                        } catch (Throwable th3) {
                                            th = th3;
                                            str3 = servletPath;
                                            normalizeHostname = contextPath;
                                            classLoader = null;
                                            str2 = pathInfo;
                                            thread = currentThread;
                                            str4 = str2;
                                            if (context != this._scontext) {
                                                if (this._classLoader != null) {
                                                    thread.setContextClassLoader(classLoader);
                                                }
                                                request.setContext(context);
                                                request.setContextPath(normalizeHostname);
                                                request.setServletPath(str3);
                                                request.setPathInfo(str4);
                                            }
                                            throw th;
                                        }
                                    }
                                    classLoader2 = null;
                                    thread2 = null;
                                    try {
                                        request.setRequestListeners(this._requestListeners);
                                        if (this._requestAttributeListeners != null) {
                                            int size2 = LazyList.size(this._requestAttributeListeners);
                                            for (int i4 = 0; i4 < size2; i4++) {
                                                request.addEventListener((EventListener) LazyList.get(this._requestAttributeListeners, i4));
                                            }
                                        }
                                    } catch (Throwable th4) {
                                        Log.debug(th4);
                                        httpServletResponse.sendError(th4.getStatus(), th4.getReason());
                                        if (obj != null) {
                                            request.takeRequestListeners();
                                            if (this._requestAttributeListeners != null) {
                                                size = LazyList.size(this._requestAttributeListeners);
                                                while (true) {
                                                    i2 = size - 1;
                                                    if (size <= 0) {
                                                        break;
                                                    }
                                                    request.removeEventListener((EventListener) LazyList.get(this._requestAttributeListeners, i2));
                                                    size = i2;
                                                }
                                            }
                                        }
                                    } catch (Throwable th5) {
                                        th4 = th5;
                                        str3 = servletPath;
                                        classLoader = classLoader2;
                                        str4 = pathInfo;
                                        thread = thread2;
                                        normalizeHostname = contextPath;
                                        if (context != this._scontext) {
                                            if (this._classLoader != null) {
                                                thread.setContextClassLoader(classLoader);
                                            }
                                            request.setContext(context);
                                            request.setContextPath(normalizeHostname);
                                            request.setServletPath(str3);
                                            request.setPathInfo(str4);
                                        }
                                        throw th4;
                                    }
                                }
                                classLoader2 = null;
                                thread2 = null;
                                if (i == 1) {
                                    if (isProtectedTarget(str)) {
                                        throw new HttpException(404);
                                    }
                                }
                                Handler handler = getHandler();
                                if (handler != null) {
                                    handler.handle(str, httpServletRequest, httpServletResponse, i);
                                }
                                if (obj != null) {
                                    request.takeRequestListeners();
                                    if (this._requestAttributeListeners != null) {
                                        size = LazyList.size(this._requestAttributeListeners);
                                        while (true) {
                                            i2 = size - 1;
                                            if (size <= 0) {
                                                break;
                                            }
                                            request.removeEventListener((EventListener) LazyList.get(this._requestAttributeListeners, i2));
                                            size = i2;
                                        }
                                    }
                                }
                                if (context != this._scontext) {
                                    if (this._classLoader != null) {
                                        thread2.setContextClassLoader(classLoader2);
                                    }
                                    request.setContext(context);
                                    request.setContextPath(contextPath);
                                    request.setServletPath(servletPath);
                                    request.setPathInfo(pathInfo);
                                }
                            } catch (Throwable th6) {
                                th4 = th6;
                                str3 = servletPath;
                                str4 = pathInfo;
                                normalizeHostname = contextPath;
                                classLoader = null;
                                thread = null;
                                if (context != this._scontext) {
                                    if (this._classLoader != null) {
                                        thread.setContextClassLoader(classLoader);
                                    }
                                    request.setContext(context);
                                    request.setContextPath(normalizeHostname);
                                    request.setServletPath(str3);
                                    request.setPathInfo(str4);
                                }
                                throw th4;
                            }
                        } catch (Throwable th7) {
                            th4 = th7;
                            str3 = servletPath;
                            str4 = null;
                            normalizeHostname = contextPath;
                            thread = null;
                            classLoader = null;
                            if (context != this._scontext) {
                                if (this._classLoader != null) {
                                    thread.setContextClassLoader(classLoader);
                                }
                                request.setContext(context);
                                request.setContextPath(normalizeHostname);
                                request.setServletPath(str3);
                                request.setPathInfo(str4);
                            }
                            throw th4;
                        }
                    } catch (Throwable th8) {
                        th4 = th8;
                        str3 = null;
                        str4 = null;
                        normalizeHostname = contextPath;
                        classLoader = null;
                        thread = null;
                        if (context != this._scontext) {
                            if (this._classLoader != null) {
                                thread.setContextClassLoader(classLoader);
                            }
                            request.setContext(context);
                            request.setContextPath(normalizeHostname);
                            request.setServletPath(str3);
                            request.setPathInfo(str4);
                        }
                        throw th4;
                    }
                } catch (Throwable th9) {
                    th4 = th9;
                    str3 = null;
                    str4 = null;
                    normalizeHostname = null;
                    classLoader = null;
                    thread = null;
                    if (context != this._scontext) {
                        if (this._classLoader != null) {
                            thread.setContextClassLoader(classLoader);
                        }
                        request.setContext(context);
                        request.setContextPath(normalizeHostname);
                        request.setServletPath(str3);
                        request.setPathInfo(str4);
                    }
                    throw th4;
                }
            }
        }
    }

    public boolean isCompactPath() {
        return this._compactPath;
    }

    protected boolean isProtectedTarget(String str) {
        return false;
    }

    public boolean isShutdown() {
        return !this._shutdown;
    }

    public Class loadClass(String str) throws ClassNotFoundException {
        Class loadClass;
        synchronized (this) {
            loadClass = str == null ? null : this._classLoader == null ? Loader.loadClass(getClass(), str) : this._classLoader.loadClass(str);
        }
        return loadClass;
    }

    public void removeAttribute(String str) {
        setManagedAttribute(str, null);
        this._attributes.removeAttribute(str);
    }

    public void setAllowNullPathInfo(boolean z) {
        this._allowNullPathInfo = z;
    }

    public void setAttribute(String str, Object obj) {
        setManagedAttribute(str, obj);
        this._attributes.setAttribute(str, obj);
    }

    public void setAttributes(Attributes attributes) {
        Enumeration attributeNames;
        if (attributes instanceof AttributesMap) {
            this._attributes = (AttributesMap) attributes;
            attributeNames = this._attributes.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String str = (String) attributeNames.nextElement();
                setManagedAttribute(str, attributes.getAttribute(str));
            }
            return;
        }
        this._attributes = new AttributesMap();
        attributeNames = attributes.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            str = (String) attributeNames.nextElement();
            Object attribute = attributes.getAttribute(str);
            setManagedAttribute(str, attribute);
            this._attributes.setAttribute(str, attribute);
        }
    }

    public void setBaseResource(Resource resource) {
        this._baseResource = resource;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this._classLoader = classLoader;
    }

    public void setCompactPath(boolean z) {
        this._compactPath = z;
    }

    public void setConnectorNames(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            this._connectors = null;
        } else {
            this._connectors = new HashSet(Arrays.asList(strArr));
        }
    }

    public void setContextPath(String str) {
        if (str == null || str.length() <= 1 || !str.endsWith(URIUtil.SLASH)) {
            this._contextPath = str;
            if (getServer() == null) {
                return;
            }
            if (getServer().isStarting() || getServer().isStarted()) {
                Class class$;
                Server server = getServer();
                if (class$org$mortbay$jetty$handler$ContextHandlerCollection == null) {
                    class$ = class$("org.mortbay.jetty.handler.ContextHandlerCollection");
                    class$org$mortbay$jetty$handler$ContextHandlerCollection = class$;
                } else {
                    class$ = class$org$mortbay$jetty$handler$ContextHandlerCollection;
                }
                Handler[] childHandlersByClass = server.getChildHandlersByClass(class$);
                int i = 0;
                while (childHandlersByClass != null && i < childHandlersByClass.length) {
                    ((ContextHandlerCollection) childHandlersByClass[i]).mapContexts();
                    i++;
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("ends with /");
    }

    public void setDisplayName(String str) {
        this._displayName = str;
        if (this._classLoader != null && (this._classLoader instanceof WebAppClassLoader)) {
            ((WebAppClassLoader) this._classLoader).setName(str);
        }
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        if (errorHandler != null) {
            errorHandler.setServer(getServer());
        }
        if (getServer() != null) {
            getServer().getContainer().update((Object) this, this._errorHandler, (Object) errorHandler, "errorHandler", true);
        }
        this._errorHandler = errorHandler;
    }

    public void setEventListeners(EventListener[] eventListenerArr) {
        this._contextListeners = null;
        this._contextAttributeListeners = null;
        this._requestListeners = null;
        this._requestAttributeListeners = null;
        this._eventListeners = eventListenerArr;
        int i = 0;
        while (eventListenerArr != null && i < eventListenerArr.length) {
            Object obj = this._eventListeners[i];
            if (obj instanceof ServletContextListener) {
                this._contextListeners = LazyList.add(this._contextListeners, obj);
            }
            if (obj instanceof ServletContextAttributeListener) {
                this._contextAttributeListeners = LazyList.add(this._contextAttributeListeners, obj);
            }
            if (obj instanceof ServletRequestListener) {
                this._requestListeners = LazyList.add(this._requestListeners, obj);
            }
            if (obj instanceof ServletRequestAttributeListener) {
                this._requestAttributeListeners = LazyList.add(this._requestAttributeListeners, obj);
            }
            i++;
        }
    }

    public void setHosts(String[] strArr) {
        setConnectorNames(strArr);
    }

    public void setInitParams(Map map) {
        if (map != null) {
            this._initParams = new HashMap(map);
        }
    }

    public void setMaxFormContentSize(int i) {
        this._maxFormContentSize = i;
    }

    public void setMimeTypes(MimeTypes mimeTypes) {
        this._mimeTypes = mimeTypes;
    }

    public void setResourceBase(String str) {
        try {
            setBaseResource(Resource.newResource(str));
        } catch (Throwable e) {
            Log.warn(e.toString());
            Log.debug(e);
            throw new IllegalArgumentException(str);
        }
    }

    public void setServer(Server server) {
        if (this._errorHandler != null) {
            Server server2 = getServer();
            if (!(server2 == null || server2 == server)) {
                server2.getContainer().update((Object) this, this._errorHandler, null, "error", true);
            }
            super.setServer(server);
            if (!(server == null || server == server2)) {
                server.getContainer().update((Object) this, null, this._errorHandler, "error", true);
            }
            this._errorHandler.setServer(server);
            return;
        }
        super.setServer(server);
    }

    public void setShutdown(boolean z) {
        this._shutdown = z;
    }

    public void setVirtualHosts(String[] strArr) {
        if (strArr == null) {
            this._vhosts = strArr;
            return;
        }
        this._vhosts = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            this._vhosts[i] = normalizeHostname(strArr[i]);
        }
    }

    public void setWelcomeFiles(String[] strArr) {
        this._welcomeFiles = strArr;
    }

    protected void startContext() throws Exception {
        super.doStart();
        if (this._errorHandler != null) {
            this._errorHandler.start();
        }
        if (this._contextListeners != null) {
            ServletContextEvent servletContextEvent = new ServletContextEvent(this._scontext);
            for (int i = 0; i < LazyList.size(this._contextListeners); i++) {
                ((ServletContextListener) LazyList.get(this._contextListeners, i)).contextInitialized(servletContextEvent);
            }
        }
        String str = (String) this._initParams.get(MANAGED_ATTRIBUTES);
        if (str != null) {
            this._managedAttributes = new HashSet();
            QuotedStringTokenizer quotedStringTokenizer = new QuotedStringTokenizer(str, ",");
            while (quotedStringTokenizer.hasMoreTokens()) {
                this._managedAttributes.add(quotedStringTokenizer.nextToken().trim());
            }
            Enumeration attributeNames = this._scontext.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                str = (String) attributeNames.nextElement();
                setManagedAttribute(str, this._scontext.getAttribute(str));
            }
        }
    }

    public String toString() {
        return new StringBuffer().append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append("{").append(getContextPath()).append(",").append(getBaseResource()).append("}").toString();
    }
}
