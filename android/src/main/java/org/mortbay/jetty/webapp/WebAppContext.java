package org.mortbay.jetty.webapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.PermissionCollection;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionListener;
import org.mortbay.io.Portable;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.deployer.WebAppDeployer;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ErrorPageErrorHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.SessionHandler;
import org.mortbay.log.Log;
import org.mortbay.resource.JarResource;
import org.mortbay.resource.Resource;
import org.mortbay.util.IO;
import org.mortbay.util.LazyList;
import org.mortbay.util.Loader;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;

public class WebAppContext extends Context {
    public static final String ERROR_PAGE = "org.mortbay.jetty.error_page";
    public static final String WEB_DEFAULTS_XML = "org/mortbay/jetty/webapp/webdefault.xml";
    private static String[] __dftConfigurationClasses = new String[]{"org.mortbay.jetty.webapp.WebInfConfiguration", "org.mortbay.jetty.webapp.WebXmlConfiguration", "org.mortbay.jetty.webapp.JettyWebXmlConfiguration", "org.mortbay.jetty.webapp.TagLibConfiguration"};
    static Class class$java$util$EventListener;
    static Class class$org$mortbay$jetty$handler$ContextHandlerCollection;
    static Class class$org$mortbay$jetty$handler$HandlerCollection;
    private String[] _configurationClasses;
    private Configuration[] _configurations;
    private boolean _copyDir;
    private String _defaultsDescriptor;
    private String _descriptor;
    private boolean _distributable;
    private String _extraClasspath;
    private boolean _extractWAR;
    private boolean _isExistingTmpDir;
    private boolean _logUrlOnStart;
    private String _overrideDescriptor;
    private transient boolean _ownClassLoader;
    private boolean _parentLoaderPriority;
    private PermissionCollection _permissions;
    private transient Map _resourceAliases;
    private String[] _serverClasses;
    private String[] _systemClasses;
    private File _tmpDir;
    private transient boolean _unavailable;
    private Throwable _unavailableException;
    private String _war;

    public WebAppContext() {
        this(null, null, null, null);
    }

    public WebAppContext(String str, String str2) {
        super(null, str2, 3);
        this._configurationClasses = __dftConfigurationClasses;
        this._defaultsDescriptor = WEB_DEFAULTS_XML;
        this._descriptor = null;
        this._overrideDescriptor = null;
        this._distributable = false;
        this._extractWAR = true;
        this._copyDir = false;
        this._logUrlOnStart = false;
        this._parentLoaderPriority = Boolean.getBoolean("org.mortbay.jetty.webapp.parentLoaderPriority");
        this._systemClasses = new String[]{"java.", "javax.", "org.mortbay.", "org.xml.", "org.w3c.", "org.apache.commons.logging.", "org.apache.log4j."};
        this._serverClasses = new String[]{"-org.mortbay.jetty.plus.annotation.", "-org.mortbay.jetty.plus.jaas.", "-org.mortbay.jetty.plus.naming.", "-org.mortbay.jetty.plus.jaas.", "-org.mortbay.jetty.servlet.DefaultServlet", "org.mortbay.jetty.", "org.slf4j."};
        this._ownClassLoader = false;
        setContextPath(str2);
        setWar(str);
        setErrorHandler(new ErrorPageErrorHandler());
    }

    public WebAppContext(HandlerContainer handlerContainer, String str, String str2) {
        super(handlerContainer, str2, 3);
        this._configurationClasses = __dftConfigurationClasses;
        this._defaultsDescriptor = WEB_DEFAULTS_XML;
        this._descriptor = null;
        this._overrideDescriptor = null;
        this._distributable = false;
        this._extractWAR = true;
        this._copyDir = false;
        this._logUrlOnStart = false;
        this._parentLoaderPriority = Boolean.getBoolean("org.mortbay.jetty.webapp.parentLoaderPriority");
        this._systemClasses = new String[]{"java.", "javax.", "org.mortbay.", "org.xml.", "org.w3c.", "org.apache.commons.logging.", "org.apache.log4j."};
        this._serverClasses = new String[]{"-org.mortbay.jetty.plus.annotation.", "-org.mortbay.jetty.plus.jaas.", "-org.mortbay.jetty.plus.naming.", "-org.mortbay.jetty.plus.jaas.", "-org.mortbay.jetty.servlet.DefaultServlet", "org.mortbay.jetty.", "org.slf4j."};
        this._ownClassLoader = false;
        setWar(str);
        setErrorHandler(new ErrorPageErrorHandler());
    }

    public WebAppContext(SecurityHandler securityHandler, SessionHandler sessionHandler, ServletHandler servletHandler, ErrorHandler errorHandler) {
        super(null, sessionHandler != null ? sessionHandler : new SessionHandler(), securityHandler != null ? securityHandler : new SecurityHandler(), servletHandler != null ? servletHandler : new ServletHandler(), null);
        this._configurationClasses = __dftConfigurationClasses;
        this._defaultsDescriptor = WEB_DEFAULTS_XML;
        this._descriptor = null;
        this._overrideDescriptor = null;
        this._distributable = false;
        this._extractWAR = true;
        this._copyDir = false;
        this._logUrlOnStart = false;
        this._parentLoaderPriority = Boolean.getBoolean("org.mortbay.jetty.webapp.parentLoaderPriority");
        this._systemClasses = new String[]{"java.", "javax.", "org.mortbay.", "org.xml.", "org.w3c.", "org.apache.commons.logging.", "org.apache.log4j."};
        this._serverClasses = new String[]{"-org.mortbay.jetty.plus.annotation.", "-org.mortbay.jetty.plus.jaas.", "-org.mortbay.jetty.plus.naming.", "-org.mortbay.jetty.plus.jaas.", "-org.mortbay.jetty.servlet.DefaultServlet", "org.mortbay.jetty.", "org.slf4j."};
        this._ownClassLoader = false;
        if (errorHandler == null) {
            errorHandler = new ErrorPageErrorHandler();
        }
        setErrorHandler(errorHandler);
    }

    public static void addWebApplications(HandlerContainer handlerContainer, String str, String str2, boolean z, boolean z2) throws IOException {
        addWebApplications(handlerContainer, str, str2, __dftConfigurationClasses, z, z2);
    }

    public static void addWebApplications(HandlerContainer handlerContainer, String str, String str2, String[] strArr, boolean z, boolean z2) throws IOException {
        Log.warn(new StringBuffer().append("Deprecated configuration used for ").append(str).toString());
        WebAppDeployer webAppDeployer = new WebAppDeployer();
        webAppDeployer.setContexts(handlerContainer);
        webAppDeployer.setWebAppDir(str);
        webAppDeployer.setConfigurationClasses(strArr);
        webAppDeployer.setExtract(z);
        webAppDeployer.setParentLoaderPriority(z2);
        try {
            webAppDeployer.start();
        } catch (IOException e) {
            throw e;
        } catch (Throwable e2) {
            throw new RuntimeException(e2);
        }
    }

    public static void addWebApplications(Server server, String str, String str2, boolean z, boolean z2) throws IOException {
        addWebApplications(server, str, str2, __dftConfigurationClasses, z, z2);
    }

    public static void addWebApplications(Server server, String str, String str2, String[] strArr, boolean z, boolean z2) throws IOException {
        Class class$;
        if (class$org$mortbay$jetty$handler$ContextHandlerCollection == null) {
            class$ = class$("org.mortbay.jetty.handler.ContextHandlerCollection");
            class$org$mortbay$jetty$handler$ContextHandlerCollection = class$;
        } else {
            class$ = class$org$mortbay$jetty$handler$ContextHandlerCollection;
        }
        HandlerContainer handlerContainer = (HandlerCollection) server.getChildHandlerByClass(class$);
        if (handlerContainer == null) {
            if (class$org$mortbay$jetty$handler$HandlerCollection == null) {
                class$ = class$("org.mortbay.jetty.handler.HandlerCollection");
                class$org$mortbay$jetty$handler$HandlerCollection = class$;
            } else {
                class$ = class$org$mortbay$jetty$handler$HandlerCollection;
            }
            handlerContainer = (HandlerCollection) server.getChildHandlerByClass(class$);
        }
        addWebApplications(handlerContainer, str, str2, strArr, z, z2);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private String getCanonicalNameForWebAppTmpDir() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Jetty");
        Connector[] connectors = getServer().getConnectors();
        stringBuffer.append("_");
        String host = (connectors == null || connectors[0] == null) ? HttpVersions.HTTP_0_9 : connectors[0].getHost();
        if (host == null) {
            host = Portable.ALL_INTERFACES;
        }
        stringBuffer.append(host.replace('.', '_'));
        stringBuffer.append("_");
        int localPort = (connectors == null || connectors[0] == null) ? 0 : connectors[0].getLocalPort();
        if (localPort < 0) {
            localPort = connectors[0].getPort();
        }
        stringBuffer.append(localPort);
        stringBuffer.append("_");
        try {
            Resource baseResource = super.getBaseResource();
            if (baseResource == null) {
                if (this._war == null || this._war.length() == 0) {
                    Resource.newResource(getResourceBase());
                }
                baseResource = Resource.newResource(this._war);
            }
            host = URIUtil.decodePath(baseResource.getURL().getPath());
            if (host.endsWith(URIUtil.SLASH)) {
                host = host.substring(0, host.length() - 1);
            }
            if (host.endsWith("!")) {
                host = host.substring(0, host.length() - 1);
            }
            stringBuffer.append(host.substring(host.lastIndexOf(URIUtil.SLASH) + 1, host.length()));
        } catch (Throwable e) {
            Log.warn("Can't generate resourceBase as part of webapp tmp dir name", e);
        }
        stringBuffer.append("_");
        stringBuffer.append(getContextPath().replace('/', '_').replace('\\', '_'));
        stringBuffer.append("_");
        String[] virtualHosts = getVirtualHosts();
        host = (virtualHosts == null || virtualHosts[0] == null) ? HttpVersions.HTTP_0_9 : virtualHosts[0];
        stringBuffer.append(host);
        host = Integer.toString(stringBuffer.toString().hashCode(), 36);
        stringBuffer.append("_");
        stringBuffer.append(host);
        for (localPort = 0; localPort < stringBuffer.length(); localPort++) {
            if (!Character.isJavaIdentifierPart(stringBuffer.charAt(localPort))) {
                stringBuffer.setCharAt(localPort, '.');
            }
        }
        return stringBuffer.toString();
    }

    public static ContextHandler getCurrentWebAppContext() {
        SContext currentContext = ContextHandler.getCurrentContext();
        if (currentContext != null) {
            ContextHandler contextHandler = currentContext.getContextHandler();
            if (contextHandler instanceof WebAppContext) {
                return contextHandler;
            }
        }
        return null;
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

    protected void doStart() throws Exception {
        int i = 0;
        try {
            loadConfigurations();
            for (Configuration webAppContext : this._configurations) {
                webAppContext.setWebAppContext(this);
            }
            this._ownClassLoader = false;
            if (getClassLoader() == null) {
                setClassLoader(new WebAppClassLoader(this));
                this._ownClassLoader = true;
            }
            if (Log.isDebugEnabled()) {
                ClassLoader classLoader = getClassLoader();
                Log.debug(new StringBuffer().append("Thread Context class loader is: ").append(classLoader).toString());
                for (classLoader = classLoader.getParent(); classLoader != null; classLoader = classLoader.getParent()) {
                    Log.debug(new StringBuffer().append("Parent class loader is: ").append(classLoader).toString());
                }
            }
            while (i < this._configurations.length) {
                this._configurations[i].configureClassLoader();
                i++;
            }
            getTempDirectory();
            if (!(this._tmpDir == null || this._isExistingTmpDir || isTempWorkDirectory())) {
                File file = new File(this._tmpDir, ".active");
                if (!file.exists()) {
                    file.mkdir();
                }
            }
            super.doStart();
            if (isLogUrlOnStart()) {
                dumpUrl();
            }
        } catch (Throwable e) {
            Log.warn(new StringBuffer().append("Failed startup of context ").append(this).toString(), e);
            this._unavailableException = e;
            this._unavailable = true;
        }
    }

    protected void doStop() throws Exception {
        super.doStop();
        try {
            int length = this._configurations.length;
            while (true) {
                int i = length - 1;
                if (length <= 0) {
                    break;
                }
                this._configurations[i].deconfigureWebApp();
                length = i;
            }
            this._configurations = null;
            if (this._securityHandler.getHandler() == null) {
                this._sessionHandler.setHandler(this._securityHandler);
                this._securityHandler.setHandler(this._servletHandler);
            }
            if (!(this._tmpDir == null || this._isExistingTmpDir || isTempWorkDirectory())) {
                IO.delete(this._tmpDir);
                this._tmpDir = null;
            }
            if (this._ownClassLoader) {
                setClassLoader(null);
            }
            this._unavailable = false;
            this._unavailableException = null;
        } catch (Throwable th) {
            if (this._ownClassLoader) {
                setClassLoader(null);
            }
            this._unavailable = false;
            this._unavailableException = null;
        }
    }

    public void dumpUrl() {
        Object connectors = getServer().getConnectors();
        for (Connector name : connectors) {
            String name2 = name.getName();
            String displayName = getDisplayName();
            if (displayName == null) {
                displayName = new StringBuffer().append("WebApp@").append(connectors.hashCode()).toString();
            }
            Log.info(new StringBuffer().append(displayName).append(" at http://").append(name2).append(getContextPath()).toString());
        }
    }

    public String[] getConfigurationClasses() {
        return this._configurationClasses;
    }

    public Configuration[] getConfigurations() {
        return this._configurations;
    }

    public String getDefaultsDescriptor() {
        return this._defaultsDescriptor;
    }

    public String getDescriptor() {
        return this._descriptor;
    }

    public String getExtraClasspath() {
        return this._extraClasspath;
    }

    public String getOverrideDescriptor() {
        return this._overrideDescriptor;
    }

    public PermissionCollection getPermissions() {
        return this._permissions;
    }

    public Resource getResource(String str) throws MalformedURLException {
        Throwable th;
        Throwable th2 = null;
        int i = 0;
        Resource resource = null;
        while (str != null) {
            int i2 = i + 1;
            if (i >= 100) {
                break;
            }
            try {
                resource = super.getResource(str);
                if (resource != null && resource.exists()) {
                    return resource;
                }
                str = getResourceAlias(str);
                i = i2;
            } catch (Throwable e) {
                Throwable th3 = e;
                Resource resource2 = resource;
                th = th3;
                Log.ignore(th);
                if (th2 == null) {
                    th2 = th;
                }
                resource = resource2;
                i = i2;
            }
        }
        if (th2 == null || !(th2 instanceof MalformedURLException)) {
            return resource;
        }
        throw ((MalformedURLException) th2);
    }

    public String getResourceAlias(String str) {
        return this._resourceAliases == null ? null : (String) this._resourceAliases.get(str);
    }

    public Map getResourceAliases() {
        return this._resourceAliases == null ? null : this._resourceAliases;
    }

    public String[] getServerClasses() {
        return this._serverClasses;
    }

    public String[] getSystemClasses() {
        return this._systemClasses;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.File getTempDirectory() {
        /*
        r4 = this;
        r2 = 0;
        r0 = r4._tmpDir;
        if (r0 == 0) goto L_0x0018;
    L_0x0005:
        r0 = r4._tmpDir;
        r0 = r0.isDirectory();
        if (r0 == 0) goto L_0x0018;
    L_0x000d:
        r0 = r4._tmpDir;
        r0 = r0.canWrite();
        if (r0 == 0) goto L_0x0018;
    L_0x0015:
        r0 = r4._tmpDir;
    L_0x0017:
        return r0;
    L_0x0018:
        r0 = "javax.servlet.context.tempdir";
        r1 = r4.getAttribute(r0);
        if (r1 == 0) goto L_0x003c;
    L_0x0020:
        r0 = r1 instanceof java.io.File;
        if (r0 == 0) goto L_0x003c;
    L_0x0024:
        r0 = r1;
        r0 = (java.io.File) r0;
        r4._tmpDir = r0;
        r0 = r4._tmpDir;
        r0 = r0.isDirectory();
        if (r0 == 0) goto L_0x003c;
    L_0x0031:
        r0 = r4._tmpDir;
        r0 = r0.canWrite();
        if (r0 == 0) goto L_0x003c;
    L_0x0039:
        r0 = r4._tmpDir;
        goto L_0x0017;
    L_0x003c:
        if (r1 == 0) goto L_0x0093;
    L_0x003e:
        r0 = r1 instanceof java.lang.String;
        if (r0 == 0) goto L_0x0093;
    L_0x0042:
        r0 = new java.io.File;	 Catch:{ Exception -> 0x008d }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x008d }
        r0.<init>(r1);	 Catch:{ Exception -> 0x008d }
        r4._tmpDir = r0;	 Catch:{ Exception -> 0x008d }
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x008d }
        r0 = r0.isDirectory();	 Catch:{ Exception -> 0x008d }
        if (r0 == 0) goto L_0x0093;
    L_0x0053:
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x008d }
        r0 = r0.canWrite();	 Catch:{ Exception -> 0x008d }
        if (r0 == 0) goto L_0x0093;
    L_0x005b:
        r0 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ Exception -> 0x008d }
        if (r0 == 0) goto L_0x0083;
    L_0x0061:
        r0 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x008d }
        r0.<init>();	 Catch:{ Exception -> 0x008d }
        r1 = "Converted to File ";
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x008d }
        r1 = r4._tmpDir;	 Catch:{ Exception -> 0x008d }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x008d }
        r1 = " for ";
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x008d }
        r0 = r0.append(r4);	 Catch:{ Exception -> 0x008d }
        r0 = r0.toString();	 Catch:{ Exception -> 0x008d }
        org.mortbay.log.Log.debug(r0);	 Catch:{ Exception -> 0x008d }
    L_0x0083:
        r0 = "javax.servlet.context.tempdir";
        r1 = r4._tmpDir;	 Catch:{ Exception -> 0x008d }
        r4.setAttribute(r0, r1);	 Catch:{ Exception -> 0x008d }
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x008d }
        goto L_0x0017;
    L_0x008d:
        r0 = move-exception;
        r1 = "EXCEPTION ";
        org.mortbay.log.Log.warn(r1, r0);
    L_0x0093:
        r0 = new java.io.File;	 Catch:{ Exception -> 0x0189 }
        r1 = "jetty.home";
        r1 = java.lang.System.getProperty(r1);	 Catch:{ Exception -> 0x0189 }
        r3 = "work";
        r0.<init>(r1, r3);	 Catch:{ Exception -> 0x0189 }
        r1 = r0.exists();	 Catch:{ Exception -> 0x0189 }
        if (r1 == 0) goto L_0x0157;
    L_0x00a6:
        r1 = r0.canWrite();	 Catch:{ Exception -> 0x0189 }
        if (r1 == 0) goto L_0x0157;
    L_0x00ac:
        r1 = r0.isDirectory();	 Catch:{ Exception -> 0x0189 }
        if (r1 == 0) goto L_0x0157;
    L_0x00b2:
        r1 = r4.getCanonicalNameForWebAppTmpDir();	 Catch:{ Exception -> 0x024d }
        if (r0 == 0) goto L_0x0190;
    L_0x00b8:
        r3 = new java.io.File;	 Catch:{ Exception -> 0x024d }
        r3.<init>(r0, r1);	 Catch:{ Exception -> 0x024d }
        r4._tmpDir = r3;	 Catch:{ Exception -> 0x024d }
    L_0x00bf:
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = r0.exists();	 Catch:{ Exception -> 0x024d }
        if (r0 != 0) goto L_0x00cc;
    L_0x00c7:
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0.mkdir();	 Catch:{ Exception -> 0x024d }
    L_0x00cc:
        r0 = r4.isTempWorkDirectory();	 Catch:{ Exception -> 0x024d }
        if (r0 != 0) goto L_0x00d7;
    L_0x00d2:
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0.deleteOnExit();	 Catch:{ Exception -> 0x024d }
    L_0x00d7:
        r0 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ Exception -> 0x024d }
        if (r0 == 0) goto L_0x00ff;
    L_0x00dd:
        r0 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x024d }
        r0.<init>();	 Catch:{ Exception -> 0x024d }
        r1 = "Created temp dir ";
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x024d }
        r1 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x024d }
        r1 = " for ";
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x024d }
        r0 = r0.append(r4);	 Catch:{ Exception -> 0x024d }
        r0 = r0.toString();	 Catch:{ Exception -> 0x024d }
        org.mortbay.log.Log.debug(r0);	 Catch:{ Exception -> 0x024d }
    L_0x00ff:
        r0 = r4._tmpDir;
        if (r0 != 0) goto L_0x014c;
    L_0x0103:
        r0 = "JettyContext";
        r1 = "";
        r0 = java.io.File.createTempFile(r0, r1);	 Catch:{ IOException -> 0x0255 }
        r4._tmpDir = r0;	 Catch:{ IOException -> 0x0255 }
        r0 = r4._tmpDir;	 Catch:{ IOException -> 0x0255 }
        r0 = r0.exists();	 Catch:{ IOException -> 0x0255 }
        if (r0 == 0) goto L_0x011a;
    L_0x0115:
        r0 = r4._tmpDir;	 Catch:{ IOException -> 0x0255 }
        r0.delete();	 Catch:{ IOException -> 0x0255 }
    L_0x011a:
        r0 = r4._tmpDir;	 Catch:{ IOException -> 0x0255 }
        r0.mkdir();	 Catch:{ IOException -> 0x0255 }
        r0 = r4._tmpDir;	 Catch:{ IOException -> 0x0255 }
        r0.deleteOnExit();	 Catch:{ IOException -> 0x0255 }
        r0 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ IOException -> 0x0255 }
        if (r0 == 0) goto L_0x014c;
    L_0x012a:
        r0 = new java.lang.StringBuffer;	 Catch:{ IOException -> 0x0255 }
        r0.<init>();	 Catch:{ IOException -> 0x0255 }
        r1 = "Created temp dir ";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x0255 }
        r1 = r4._tmpDir;	 Catch:{ IOException -> 0x0255 }
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x0255 }
        r1 = " for ";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x0255 }
        r0 = r0.append(r4);	 Catch:{ IOException -> 0x0255 }
        r0 = r0.toString();	 Catch:{ IOException -> 0x0255 }
        org.mortbay.log.Log.debug(r0);	 Catch:{ IOException -> 0x0255 }
    L_0x014c:
        r0 = "javax.servlet.context.tempdir";
        r1 = r4._tmpDir;
        r4.setAttribute(r0, r1);
        r0 = r4._tmpDir;
        goto L_0x0017;
    L_0x0157:
        r0 = r4.getBaseResource();	 Catch:{ Exception -> 0x0189 }
        if (r0 == 0) goto L_0x0186;
    L_0x015d:
        r1 = r4.getWebInf();	 Catch:{ Exception -> 0x0189 }
        if (r1 == 0) goto L_0x0186;
    L_0x0163:
        r0 = r1.exists();	 Catch:{ Exception -> 0x0189 }
        if (r0 == 0) goto L_0x0186;
    L_0x0169:
        r0 = new java.io.File;	 Catch:{ Exception -> 0x0189 }
        r1 = r1.getFile();	 Catch:{ Exception -> 0x0189 }
        r3 = "work";
        r0.<init>(r1, r3);	 Catch:{ Exception -> 0x0189 }
        r1 = r0.exists();	 Catch:{ Exception -> 0x0189 }
        if (r1 == 0) goto L_0x0186;
    L_0x017a:
        r1 = r0.canWrite();	 Catch:{ Exception -> 0x0189 }
        if (r1 == 0) goto L_0x0186;
    L_0x0180:
        r1 = r0.isDirectory();	 Catch:{ Exception -> 0x0189 }
        if (r1 != 0) goto L_0x00b2;
    L_0x0186:
        r0 = r2;
        goto L_0x00b2;
    L_0x0189:
        r0 = move-exception;
        org.mortbay.log.Log.ignore(r0);
        r0 = r2;
        goto L_0x00b2;
    L_0x0190:
        r0 = new java.io.File;	 Catch:{ Exception -> 0x024d }
        r3 = "java.io.tmpdir";
        r3 = java.lang.System.getProperty(r3);	 Catch:{ Exception -> 0x024d }
        r0.<init>(r3, r1);	 Catch:{ Exception -> 0x024d }
        r4._tmpDir = r0;	 Catch:{ Exception -> 0x024d }
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = r0.exists();	 Catch:{ Exception -> 0x024d }
        if (r0 == 0) goto L_0x00bf;
    L_0x01a5:
        r0 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ Exception -> 0x024d }
        if (r0 == 0) goto L_0x01cd;
    L_0x01ab:
        r0 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x024d }
        r0.<init>();	 Catch:{ Exception -> 0x024d }
        r3 = "Delete existing temp dir ";
        r0 = r0.append(r3);	 Catch:{ Exception -> 0x024d }
        r3 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = r0.append(r3);	 Catch:{ Exception -> 0x024d }
        r3 = " for ";
        r0 = r0.append(r3);	 Catch:{ Exception -> 0x024d }
        r0 = r0.append(r4);	 Catch:{ Exception -> 0x024d }
        r0 = r0.toString();	 Catch:{ Exception -> 0x024d }
        org.mortbay.log.Log.debug(r0);	 Catch:{ Exception -> 0x024d }
    L_0x01cd:
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = org.mortbay.util.IO.delete(r0);	 Catch:{ Exception -> 0x024d }
        if (r0 != 0) goto L_0x01f3;
    L_0x01d5:
        r0 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ Exception -> 0x024d }
        if (r0 == 0) goto L_0x01f3;
    L_0x01db:
        r0 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x024d }
        r0.<init>();	 Catch:{ Exception -> 0x024d }
        r3 = "Failed to delete temp dir ";
        r0 = r0.append(r3);	 Catch:{ Exception -> 0x024d }
        r3 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = r0.append(r3);	 Catch:{ Exception -> 0x024d }
        r0 = r0.toString();	 Catch:{ Exception -> 0x024d }
        org.mortbay.log.Log.debug(r0);	 Catch:{ Exception -> 0x024d }
    L_0x01f3:
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = r0.exists();	 Catch:{ Exception -> 0x024d }
        if (r0 == 0) goto L_0x00bf;
    L_0x01fb:
        r0 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = r0.toString();	 Catch:{ Exception -> 0x024d }
        r3 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x024d }
        r3.<init>();	 Catch:{ Exception -> 0x024d }
        r1 = r3.append(r1);	 Catch:{ Exception -> 0x024d }
        r3 = "_";
        r1 = r1.append(r3);	 Catch:{ Exception -> 0x024d }
        r1 = r1.toString();	 Catch:{ Exception -> 0x024d }
        r3 = "";
        r1 = java.io.File.createTempFile(r1, r3);	 Catch:{ Exception -> 0x024d }
        r4._tmpDir = r1;	 Catch:{ Exception -> 0x024d }
        r1 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r1 = r1.exists();	 Catch:{ Exception -> 0x024d }
        if (r1 == 0) goto L_0x0229;
    L_0x0224:
        r1 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r1.delete();	 Catch:{ Exception -> 0x024d }
    L_0x0229:
        r1 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x024d }
        r1.<init>();	 Catch:{ Exception -> 0x024d }
        r3 = "Can't reuse ";
        r1 = r1.append(r3);	 Catch:{ Exception -> 0x024d }
        r0 = r1.append(r0);	 Catch:{ Exception -> 0x024d }
        r1 = ", using ";
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x024d }
        r1 = r4._tmpDir;	 Catch:{ Exception -> 0x024d }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x024d }
        r0 = r0.toString();	 Catch:{ Exception -> 0x024d }
        org.mortbay.log.Log.warn(r0);	 Catch:{ Exception -> 0x024d }
        goto L_0x00bf;
    L_0x024d:
        r0 = move-exception;
        r4._tmpDir = r2;
        org.mortbay.log.Log.ignore(r0);
        goto L_0x00ff;
    L_0x0255:
        r0 = move-exception;
        r1 = "tmpdir";
        org.mortbay.log.Log.warn(r1, r0);
        r0 = 1;
        java.lang.System.exit(r0);
        goto L_0x014c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.webapp.WebAppContext.getTempDirectory():java.io.File");
    }

    public Throwable getUnavailableException() {
        return this._unavailableException;
    }

    public String getWar() {
        if (this._war == null) {
            this._war = getResourceBase();
        }
        return this._war;
    }

    public Resource getWebInf() throws IOException {
        resolveWebApp();
        Resource addPath = super.getBaseResource().addPath("WEB-INF/");
        return (addPath.exists() && addPath.isDirectory()) ? addPath : null;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        if (this._unavailable) {
            (httpServletRequest instanceof Request ? (Request) httpServletRequest : HttpConnection.getCurrentConnection().getRequest()).setHandled(true);
            httpServletResponse.sendError(503);
            return;
        }
        super.handle(str, httpServletRequest, httpServletResponse, i);
    }

    public boolean isCopyWebDir() {
        return this._copyDir;
    }

    public boolean isDistributable() {
        return this._distributable;
    }

    public boolean isExtractWAR() {
        return this._extractWAR;
    }

    public boolean isLogUrlOnStart() {
        return this._logUrlOnStart;
    }

    public boolean isParentLoaderPriority() {
        return this._parentLoaderPriority;
    }

    protected boolean isProtectedTarget(String str) {
        while (str.startsWith("//")) {
            str = URIUtil.compactPath(str);
        }
        return StringUtil.startsWithIgnoreCase(str, "/web-inf") || StringUtil.startsWithIgnoreCase(str, "/meta-inf");
    }

    public boolean isTempWorkDirectory() {
        if (this._tmpDir != null) {
            if (this._tmpDir.getName().equalsIgnoreCase("work")) {
                return true;
            }
            File parentFile = this._tmpDir.getParentFile();
            if (parentFile != null) {
                return parentFile.getName().equalsIgnoreCase("work");
            }
        }
        return false;
    }

    protected void loadConfigurations() throws Exception {
        if (this._configurations == null) {
            if (this._configurationClasses == null) {
                this._configurationClasses = __dftConfigurationClasses;
            }
            this._configurations = new Configuration[this._configurationClasses.length];
            for (int i = 0; i < this._configurations.length; i++) {
                this._configurations[i] = (Configuration) Loader.loadClass(getClass(), this._configurationClasses[i]).newInstance();
            }
        }
    }

    public String removeResourceAlias(String str) {
        return this._resourceAliases == null ? null : (String) this._resourceAliases.remove(str);
    }

    protected void resolveWebApp() throws IOException {
        if (super.getBaseResource() == null) {
            if (this._war == null || this._war.length() == 0) {
                this._war = getResourceBase();
            }
            Resource newResource = Resource.newResource(this._war);
            if (newResource.getAlias() != null) {
                Log.debug(new StringBuffer().append(newResource).append(" anti-aliased to ").append(newResource.getAlias()).toString());
                newResource = Resource.newResource(newResource.getAlias());
            }
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("Try webapp=").append(newResource).append(", exists=").append(newResource.exists()).append(", directory=").append(newResource.isDirectory()).toString());
            }
            if (!(!newResource.exists() || newResource.isDirectory() || newResource.toString().startsWith("jar:"))) {
                Resource newResource2 = Resource.newResource(new StringBuffer().append("jar:").append(newResource).append("!/").toString());
                if (newResource2.exists() && newResource2.isDirectory()) {
                    newResource = newResource2;
                }
            }
            if (newResource.exists() && ((this._copyDir && newResource.getFile() != null && newResource.getFile().isDirectory()) || (!(!this._extractWAR || newResource.getFile() == null || newResource.getFile().isDirectory()) || ((this._extractWAR && newResource.getFile() == null) || !newResource.isDirectory())))) {
                File file = new File(getTempDirectory(), "webapp");
                if (newResource.getFile() != null && newResource.getFile().isDirectory()) {
                    Log.info(new StringBuffer().append("Copy ").append(newResource.getFile()).append(" to ").append(file).toString());
                    IO.copyDir(newResource.getFile(), file);
                } else if (!file.exists()) {
                    file.mkdir();
                    Log.info(new StringBuffer().append("Extract ").append(this._war).append(" to ").append(file).toString());
                    JarResource.extract(newResource, file, false);
                } else if (newResource.lastModified() > file.lastModified()) {
                    IO.delete(file);
                    file.mkdir();
                    Log.info(new StringBuffer().append("Extract ").append(this._war).append(" to ").append(file).toString());
                    JarResource.extract(newResource, file, false);
                }
                newResource = Resource.newResource(file.getCanonicalPath());
            }
            if (newResource.exists() && newResource.isDirectory()) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("webapp=").append(newResource).toString());
                }
                super.setBaseResource(newResource);
                return;
            }
            Log.warn(new StringBuffer().append("Web application not found ").append(this._war).toString());
            throw new FileNotFoundException(this._war);
        }
    }

    public void setClassLoader(ClassLoader classLoader) {
        super.setClassLoader(classLoader);
        if (classLoader != null && (classLoader instanceof WebAppClassLoader)) {
            ((WebAppClassLoader) classLoader).setName(getDisplayName());
        }
    }

    public void setConfigurationClasses(String[] strArr) {
        if (isRunning()) {
            throw new IllegalStateException("Running");
        }
        this._configurationClasses = strArr == null ? null : (String[]) strArr.clone();
    }

    public void setConfigurations(Configuration[] configurationArr) {
        if (isRunning()) {
            throw new IllegalStateException("Running");
        }
        this._configurations = configurationArr == null ? null : (Configuration[]) configurationArr.clone();
    }

    public void setCopyWebDir(boolean z) {
        this._copyDir = z;
    }

    public void setDefaultsDescriptor(String str) {
        if (isRunning()) {
            throw new IllegalStateException("Running");
        }
        this._defaultsDescriptor = str;
    }

    public void setDescriptor(String str) {
        if (isRunning()) {
            throw new IllegalStateException("Running");
        }
        this._descriptor = str;
    }

    public void setDistributable(boolean z) {
        this._distributable = z;
    }

    public void setEventListeners(EventListener[] eventListenerArr) {
        if (this._sessionHandler != null) {
            this._sessionHandler.clearEventListeners();
        }
        super.setEventListeners(eventListenerArr);
        int i = 0;
        while (eventListenerArr != null && i < eventListenerArr.length) {
            EventListener eventListener = eventListenerArr[i];
            if (((eventListener instanceof HttpSessionActivationListener) || (eventListener instanceof HttpSessionAttributeListener) || (eventListener instanceof HttpSessionBindingListener) || (eventListener instanceof HttpSessionListener)) && this._sessionHandler != null) {
                this._sessionHandler.addEventListener(eventListener);
            }
            i++;
        }
    }

    public void setExtraClasspath(String str) {
        this._extraClasspath = str;
    }

    public void setExtractWAR(boolean z) {
        this._extractWAR = z;
    }

    public void setLogUrlOnStart(boolean z) {
        this._logUrlOnStart = z;
    }

    public void setOverrideDescriptor(String str) {
        if (isRunning()) {
            throw new IllegalStateException("Running");
        }
        this._overrideDescriptor = str;
    }

    public void setParentLoaderPriority(boolean z) {
        this._parentLoaderPriority = z;
    }

    public void setPermissions(PermissionCollection permissionCollection) {
        this._permissions = permissionCollection;
    }

    public void setResourceAlias(String str, String str2) {
        if (this._resourceAliases == null) {
            this._resourceAliases = new HashMap(5);
        }
        this._resourceAliases.put(str, str2);
    }

    public void setResourceAliases(Map map) {
        this._resourceAliases = map;
    }

    public void setServerClasses(String[] strArr) {
        this._serverClasses = strArr == null ? null : (String[]) strArr.clone();
    }

    public void setSystemClasses(String[] strArr) {
        this._systemClasses = strArr == null ? null : (String[]) strArr.clone();
    }

    public void setTempDirectory(File file) {
        if (isStarted()) {
            throw new IllegalStateException("Started");
        }
        if (file != null) {
            try {
                file = new File(file.getCanonicalPath());
            } catch (Throwable e) {
                Log.warn(Log.EXCEPTION, e);
            }
        }
        if (file != null && !file.exists()) {
            file.mkdir();
            file.deleteOnExit();
        } else if (file != null) {
            this._isExistingTmpDir = true;
        }
        if (file == null || (file.exists() && file.isDirectory() && file.canWrite())) {
            this._tmpDir = file;
            setAttribute(ServletHandler.__J_S_CONTEXT_TEMPDIR, this._tmpDir);
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Bad temp directory: ").append(file).toString());
    }

    public void setWar(String str) {
        this._war = str;
    }

    protected void startContext() throws Exception {
        int i = 0;
        for (Configuration configureDefaults : this._configurations) {
            configureDefaults.configureDefaults();
        }
        Resource webInf = getWebInf();
        if (webInf != null) {
            webInf = webInf.addPath("work");
            if (webInf.exists() && webInf.isDirectory() && webInf.getFile() != null && webInf.getFile().canWrite() && getAttribute(ServletHandler.__J_S_CONTEXT_TEMPDIR) == null) {
                setAttribute(ServletHandler.__J_S_CONTEXT_TEMPDIR, webInf.getFile());
            }
        }
        while (i < this._configurations.length) {
            this._configurations[i].configureWebApp();
            i++;
        }
        super.startContext();
    }

    public String toString() {
        return new StringBuffer().append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append("{").append(getContextPath()).append(",").append(this._war == null ? getResourceBase() : this._war).append("}").toString();
    }
}
