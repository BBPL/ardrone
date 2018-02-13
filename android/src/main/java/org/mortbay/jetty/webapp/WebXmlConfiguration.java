package org.mortbay.jetty.webapp;

import com.google.android.gms.plus.PlusShare;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.security.Authenticator;
import org.mortbay.jetty.security.BasicAuthenticator;
import org.mortbay.jetty.security.ClientCertAuthenticator;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.DigestAuthenticator;
import org.mortbay.jetty.security.FormAuthenticator;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.servlet.Dispatcher;
import org.mortbay.jetty.servlet.ErrorPageErrorHandler;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.FilterMapping;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.ServletMapping;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;
import org.mortbay.util.LazyList;
import org.mortbay.util.Loader;
import org.mortbay.util.URIUtil;
import org.mortbay.xml.XmlParser;
import org.mortbay.xml.XmlParser.Node;

public class WebXmlConfiguration implements Configuration {
    static Class class$java$lang$String;
    static Class class$java$util$EventListener;
    static Class class$org$mortbay$jetty$security$ConstraintMapping;
    static Class class$org$mortbay$jetty$servlet$FilterHolder;
    static Class class$org$mortbay$jetty$servlet$FilterMapping;
    static Class class$org$mortbay$jetty$servlet$ServletHolder;
    static Class class$org$mortbay$jetty$servlet$ServletMapping;
    static Class class$org$mortbay$jetty$webapp$WebAppContext;
    protected Object _constraintMappings;
    protected WebAppContext _context;
    protected boolean _defaultWelcomeFileList;
    protected Map _errorPages;
    protected Object _filterMappings;
    protected Object _filters;
    protected boolean _hasJSP;
    protected String _jspServletClass;
    protected String _jspServletName;
    protected Object _listeners;
    protected ServletHandler _servletHandler;
    protected Object _servletMappings;
    protected Object _servlets;
    protected int _version;
    protected Object _welcomeFiles;
    protected XmlParser _xmlParser = webXmlParser();

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public static XmlParser webXmlParser() {
        Class class$;
        XmlParser xmlParser = new XmlParser();
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource = class$.getResource("/javax/servlet/resources/web-app_2_2.dtd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource2 = class$.getResource("/javax/servlet/resources/web-app_2_3.dtd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource3 = class$.getResource("/javax/servlet/resources/jsp_2_0.xsd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource4 = class$.getResource("/javax/servlet/resources/jsp_2_1.xsd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource5 = class$.getResource("/javax/servlet/resources/j2ee_1_4.xsd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource6 = class$.getResource("/javax/servlet/resources/web-app_2_4.xsd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource7 = class$.getResource("/javax/servlet/resources/web-app_2_5.xsd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource8 = class$.getResource("/javax/servlet/resources/XMLSchema.dtd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource9 = class$.getResource("/javax/servlet/resources/xml.xsd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource10 = class$.getResource("/javax/servlet/resources/j2ee_web_services_client_1_1.xsd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource11 = class$.getResource("/javax/servlet/resources/javaee_web_services_client_1_2.xsd");
        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
            class$org$mortbay$jetty$webapp$WebAppContext = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppContext;
        }
        URL resource12 = class$.getResource("/javax/servlet/resources/datatypes.dtd");
        xmlParser.redirectEntity("web-app_2_2.dtd", resource);
        xmlParser.redirectEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", resource);
        xmlParser.redirectEntity("web.dtd", resource2);
        xmlParser.redirectEntity("web-app_2_3.dtd", resource2);
        xmlParser.redirectEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", resource2);
        xmlParser.redirectEntity("XMLSchema.dtd", resource8);
        xmlParser.redirectEntity("http://www.w3.org/2001/XMLSchema.dtd", resource8);
        xmlParser.redirectEntity("-//W3C//DTD XMLSCHEMA 200102//EN", resource8);
        xmlParser.redirectEntity("jsp_2_0.xsd", resource3);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/j2ee/jsp_2_0.xsd", resource3);
        xmlParser.redirectEntity("jsp_2_1.xsd", resource4);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/javaee/jsp_2_1.xsd", resource4);
        xmlParser.redirectEntity("j2ee_1_4.xsd", resource5);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/j2ee/j2ee_1_4.xsd", resource5);
        xmlParser.redirectEntity("web-app_2_4.xsd", resource6);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd", resource6);
        xmlParser.redirectEntity("web-app_2_5.xsd", resource7);
        xmlParser.redirectEntity("http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd", resource7);
        xmlParser.redirectEntity("xml.xsd", resource9);
        xmlParser.redirectEntity("http://www.w3.org/2001/xml.xsd", resource9);
        xmlParser.redirectEntity("datatypes.dtd", resource12);
        xmlParser.redirectEntity("http://www.w3.org/2001/datatypes.dtd", resource12);
        xmlParser.redirectEntity("j2ee_web_services_client_1_1.xsd", resource10);
        xmlParser.redirectEntity("http://www.ibm.com/webservices/xsd/j2ee_web_services_client_1_1.xsd", resource10);
        xmlParser.redirectEntity("javaee_web_services_client_1_2.xsd", resource11);
        xmlParser.redirectEntity("http://www.ibm.com/webservices/xsd/javaee_web_services_client_1_2.xsd", resource11);
        return xmlParser;
    }

    public void configure(String str) throws Exception {
        initialize(this._xmlParser.parse(str));
    }

    public void configureClassLoader() throws Exception {
    }

    public void configureDefaults() throws Exception {
        if (!this._context.isStarted()) {
            String defaultsDescriptor = getWebAppContext().getDefaultsDescriptor();
            if (defaultsDescriptor != null && defaultsDescriptor.length() > 0) {
                Resource newSystemResource = Resource.newSystemResource(defaultsDescriptor);
                if (newSystemResource == null) {
                    newSystemResource = Resource.newResource(defaultsDescriptor);
                }
                configure(newSystemResource.getURL().toString());
                this._defaultWelcomeFileList = this._welcomeFiles != null;
            }
        } else if (Log.isDebugEnabled()) {
            Log.debug("Cannot configure webapp after it is started");
        }
    }

    public void configureWebApp() throws Exception {
        if (!this._context.isStarted()) {
            URL findWebXml = findWebXml();
            if (findWebXml != null) {
                configure(findWebXml.toString());
            }
            String overrideDescriptor = getWebAppContext().getOverrideDescriptor();
            if (overrideDescriptor != null && overrideDescriptor.length() > 0) {
                Resource newSystemResource = Resource.newSystemResource(overrideDescriptor);
                if (newSystemResource == null) {
                    newSystemResource = Resource.newResource(overrideDescriptor);
                }
                this._xmlParser.setValidating(false);
                configure(newSystemResource.getURL().toString());
            }
        } else if (Log.isDebugEnabled()) {
            Log.debug("Cannot configure webapp after it is started");
        }
    }

    public void deconfigureWebApp() throws Exception {
        this._servletHandler = getWebAppContext().getServletHandler();
        this._servletHandler.setFilters(null);
        this._servletHandler.setFilterMappings(null);
        this._servletHandler.setServlets(null);
        this._servletHandler.setServletMappings(null);
        getWebAppContext().setEventListeners(null);
        getWebAppContext().setWelcomeFiles(null);
        if (getWebAppContext().getSecurityHandler() != null) {
            getWebAppContext().getSecurityHandler().setConstraintMappings(null);
        }
        if (getWebAppContext().getErrorHandler() instanceof ErrorPageErrorHandler) {
            ((ErrorPageErrorHandler) getWebAppContext().getErrorHandler()).setErrorPages(null);
        }
    }

    protected URL findWebXml() throws IOException, MalformedURLException {
        Resource newResource;
        String descriptor = getWebAppContext().getDescriptor();
        if (descriptor != null) {
            newResource = Resource.newResource(descriptor);
            if (newResource.exists() && !newResource.isDirectory()) {
                return newResource.getURL();
            }
        }
        newResource = getWebAppContext().getWebInf();
        if (newResource != null && newResource.isDirectory()) {
            newResource = newResource.addPath("web.xml");
            if (newResource.exists()) {
                return newResource.getURL();
            }
            Log.debug(new StringBuffer().append("No WEB-INF/web.xml in ").append(getWebAppContext().getWar()).append(". Serving files and default/dynamic servlets only").toString());
        }
        return null;
    }

    protected String getJSPServletName() {
        if (this._jspServletName == null) {
            Entry holderEntry = this._context.getServletHandler().getHolderEntry("test.jsp");
            if (holderEntry != null) {
                this._jspServletName = ((ServletHolder) holderEntry.getValue()).getName();
            }
        }
        return this._jspServletName;
    }

    public WebAppContext getWebAppContext() {
        return this._context;
    }

    protected void initContextParam(Node node) {
        String string = node.getString("param-name", false, true);
        String string2 = node.getString("param-value", false, true);
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("ContextParam: ").append(string).append("=").append(string2).toString());
        }
        getWebAppContext().getInitParams().put(string, string2);
    }

    protected void initDisplayName(Node node) {
        getWebAppContext().setDisplayName(node.toString(false, true));
    }

    protected void initDistributable(Node node) {
        WebAppContext webAppContext = getWebAppContext();
        if (!webAppContext.isDistributable()) {
            webAppContext.setDistributable(true);
        }
    }

    protected void initErrorPage(Node node) {
        Object string = node.getString("error-code", false, true);
        if (string == null || string.length() == 0) {
            string = node.getString("exception-type", false, true);
        }
        String string2 = node.getString("location", false, true);
        if (this._errorPages == null) {
            this._errorPages = new HashMap();
        }
        this._errorPages.put(string, string2);
    }

    protected void initFilter(Node node) {
        String string = node.getString("filter-name", false, true);
        FilterHolder filter = this._servletHandler.getFilter(string);
        if (filter == null) {
            filter = this._servletHandler.newFilterHolder();
            filter.setName(string);
            this._filters = LazyList.add(this._filters, filter);
        }
        FilterHolder filterHolder = filter;
        String string2 = node.getString("filter-class", false, true);
        if (string2 != null) {
            filterHolder.setClassName(string2);
        }
        Iterator it = node.iterator("init-param");
        while (it.hasNext()) {
            Node node2 = (Node) it.next();
            filterHolder.setInitParameter(node2.getString("param-name", false, true), node2.getString("param-value", false, true));
        }
    }

    protected void initFilterMapping(Node node) {
        String string = node.getString("filter-name", false, true);
        FilterMapping filterMapping = new FilterMapping();
        filterMapping.setFilterName(string);
        ArrayList arrayList = new ArrayList();
        Iterator it = node.iterator("url-pattern");
        while (it.hasNext()) {
            arrayList.add(normalizePattern(((Node) it.next()).toString(false, true)));
        }
        filterMapping.setPathSpecs((String[]) arrayList.toArray(new String[arrayList.size()]));
        arrayList = new ArrayList();
        it = node.iterator("servlet-name");
        while (it.hasNext()) {
            arrayList.add(((Node) it.next()).toString(false, true));
        }
        filterMapping.setServletNames((String[]) arrayList.toArray(new String[arrayList.size()]));
        it = node.iterator("dispatcher");
        int i = 0;
        while (it.hasNext()) {
            i = Dispatcher.type(((Node) it.next()).toString(false, true)) | i;
        }
        filterMapping.setDispatches(i);
        this._filterMappings = LazyList.add(this._filterMappings, filterMapping);
    }

    protected void initJspConfig(Node node) {
        Object obj;
        for (int i = 0; i < node.size(); i++) {
            obj = node.get(i);
            if ((obj instanceof Node) && "taglib".equals(((Node) obj).getTag())) {
                initTagLib((Node) obj);
            }
        }
        Iterator it = node.iterator("jsp-property-group");
        obj = null;
        while (it.hasNext()) {
            Iterator it2 = ((Node) it.next()).iterator("url-pattern");
            while (it2.hasNext()) {
                obj = LazyList.add(obj, normalizePattern(((Node) it2.next()).toString(false, true)));
            }
        }
        if (LazyList.size(obj) > 0) {
            String jSPServletName = getJSPServletName();
            if (jSPServletName != null) {
                ServletMapping servletMapping = new ServletMapping();
                servletMapping.setServletName(jSPServletName);
                servletMapping.setPathSpecs(LazyList.toStringArray(obj));
                this._servletMappings = LazyList.add(this._servletMappings, servletMapping);
            }
        }
    }

    protected void initListener(Node node) {
        String string = node.getString("listener-class", false, true);
        try {
            Object newListenerInstance = newListenerInstance(getWebAppContext().loadClass(string));
            if (newListenerInstance instanceof EventListener) {
                this._listeners = LazyList.add(this._listeners, newListenerInstance);
            } else {
                Log.warn(new StringBuffer().append("Not an EventListener: ").append(newListenerInstance).toString());
            }
        } catch (Throwable e) {
            Log.warn(new StringBuffer().append("Could not instantiate listener ").append(string).toString(), e);
        }
    }

    protected void initLocaleEncodingList(Node node) {
        Iterator it = node.iterator("locale-encoding-mapping");
        while (it.hasNext()) {
            Node node2 = (Node) it.next();
            getWebAppContext().addLocaleEncoding(node2.getString("locale", false, true), node2.getString("encoding", false, true));
        }
    }

    protected void initLoginConfig(Node node) {
        String node2;
        FormAuthenticator formAuthenticator = null;
        Node node3 = node.get("auth-method");
        if (node3 != null) {
            Authenticator authenticator;
            node2 = node3.toString(false, true);
            if (Constraint.__FORM_AUTH.equals(node2)) {
                formAuthenticator = new FormAuthenticator();
                authenticator = formAuthenticator;
            } else if (Constraint.__BASIC_AUTH.equals(node2)) {
                authenticator = new BasicAuthenticator();
            } else if (Constraint.__DIGEST_AUTH.equals(node2)) {
                authenticator = new DigestAuthenticator();
            } else if (Constraint.__CERT_AUTH.equals(node2)) {
                authenticator = new ClientCertAuthenticator();
            } else if (Constraint.__CERT_AUTH2.equals(node2)) {
                authenticator = new ClientCertAuthenticator();
            } else {
                Log.warn(new StringBuffer().append("UNKNOWN AUTH METHOD: ").append(node2).toString());
                authenticator = null;
            }
            getWebAppContext().getSecurityHandler().setAuthenticator(authenticator);
        }
        node3 = node.get("realm-name");
        UserRealm[] userRealms = ContextHandler.getCurrentContext().getContextHandler().getServer().getUserRealms();
        node2 = node3 == null ? ServletHandler.__DEFAULT_SERVLET : node3.toString(false, true);
        UserRealm userRealm = getWebAppContext().getSecurityHandler().getUserRealm();
        int i = 0;
        while (userRealm == null && userRealms != null && i < userRealms.length) {
            if (userRealms[i] != null && node2.equals(userRealms[i].getName())) {
                userRealm = userRealms[i];
            }
            i++;
        }
        if (userRealm == null) {
            Log.warn(new StringBuffer().append("Unknown realm: ").append(node2).toString());
        } else {
            getWebAppContext().getSecurityHandler().setUserRealm(userRealm);
        }
        node3 = node.get("form-login-config");
        if (node3 == null) {
            return;
        }
        if (formAuthenticator == null) {
            Log.warn("FORM Authentication miss-configured");
            return;
        }
        Node node4 = node3.get("form-login-page");
        if (node4 != null) {
            formAuthenticator.setLoginPage(node4.toString(false, true));
        }
        node3 = node3.get("form-error-page");
        if (node3 != null) {
            formAuthenticator.setErrorPage(node3.toString(false, true));
        }
    }

    protected void initMimeConfig(Node node) {
        String string = node.getString("extension", false, true);
        if (string != null && string.startsWith(".")) {
            string = string.substring(1);
        }
        getWebAppContext().getMimeTypes().addMimeMapping(string, node.getString("mime-type", false, true));
    }

    protected void initSecurityConstraint(Node node) {
        Constraint constraint = new Constraint();
        try {
            Node node2 = node.get("auth-constraint");
            if (node2 != null) {
                constraint.setAuthenticate(true);
                Iterator it = node2.iterator("role-name");
                Object obj = null;
                while (it.hasNext()) {
                    obj = LazyList.add(obj, ((Node) it.next()).toString(false, true));
                }
                constraint.setRoles(LazyList.toStringArray(obj));
            }
            node2 = node.get("user-data-constraint");
            if (node2 != null) {
                String toUpperCase = node2.get("transport-guarantee").toString(false, true).toUpperCase();
                if (toUpperCase == null || toUpperCase.length() == 0 || Constraint.NONE.equals(toUpperCase)) {
                    constraint.setDataConstraint(0);
                } else if ("INTEGRAL".equals(toUpperCase)) {
                    constraint.setDataConstraint(1);
                } else if ("CONFIDENTIAL".equals(toUpperCase)) {
                    constraint.setDataConstraint(2);
                } else {
                    Log.warn(new StringBuffer().append("Unknown user-data-constraint:").append(toUpperCase).toString());
                    constraint.setDataConstraint(2);
                }
            }
            Iterator it2 = node.iterator("web-resource-collection");
            while (it2.hasNext()) {
                node2 = (Node) it2.next();
                Constraint constraint2 = (Constraint) constraint.clone();
                constraint2.setName(node2.getString("web-resource-name", false, true));
                Iterator it3 = node2.iterator("url-pattern");
                while (it3.hasNext()) {
                    String normalizePattern = normalizePattern(((Node) it3.next()).toString(false, true));
                    Iterator it4 = node2.iterator("http-method");
                    if (it4.hasNext()) {
                        while (it4.hasNext()) {
                            String node3 = ((Node) it4.next()).toString(false, true);
                            ConstraintMapping constraintMapping = new ConstraintMapping();
                            constraintMapping.setMethod(node3);
                            constraintMapping.setPathSpec(normalizePattern);
                            constraintMapping.setConstraint(constraint2);
                            this._constraintMappings = LazyList.add(this._constraintMappings, constraintMapping);
                        }
                    } else {
                        ConstraintMapping constraintMapping2 = new ConstraintMapping();
                        constraintMapping2.setPathSpec(normalizePattern);
                        constraintMapping2.setConstraint(constraint2);
                        this._constraintMappings = LazyList.add(this._constraintMappings, constraintMapping2);
                    }
                }
            }
        } catch (Throwable e) {
            Log.warn(e);
        }
    }

    protected void initSecurityRole(Node node) {
    }

    protected void initServlet(Node node) {
        Node node2;
        String attribute = node.getAttribute("id");
        String string = node.getString("servlet-name", false, true);
        ServletHolder servlet = this._servletHandler.getServlet(string);
        if (servlet == null) {
            servlet = this._servletHandler.newServletHolder();
            servlet.setName(string);
            this._servlets = LazyList.add(this._servlets, servlet);
        }
        ServletHolder servletHolder = servlet;
        Iterator it = node.iterator("init-param");
        while (it.hasNext()) {
            node2 = (Node) it.next();
            servletHolder.setInitParameter(node2.getString("param-name", false, true), node2.getString("param-value", false, true));
        }
        String string2 = node.getString("servlet-class", false, true);
        if (attribute != null && attribute.equals("jsp")) {
            this._jspServletName = string;
            this._jspServletClass = string2;
            try {
                Loader.loadClass(getClass(), string2);
                this._hasJSP = true;
            } catch (ClassNotFoundException e) {
                Log.info("NO JSP Support for {}, did not find {}", this._context.getContextPath(), string2);
                this._hasJSP = false;
                string2 = "org.mortbay.servlet.NoJspServlet";
                this._jspServletClass = "org.mortbay.servlet.NoJspServlet";
            }
            if (servletHolder.getInitParameter("scratchdir") == null) {
                File file = new File(getWebAppContext().getTempDirectory(), "jsp");
                if (!file.exists()) {
                    file.mkdir();
                }
                servletHolder.setInitParameter("scratchdir", file.getAbsolutePath());
                if ("?".equals(servletHolder.getInitParameter("classpath"))) {
                    attribute = getWebAppContext().getClassPath();
                    Log.debug(new StringBuffer().append("classpath=").append(attribute).toString());
                    if (attribute != null) {
                        servletHolder.setInitParameter("classpath", attribute);
                    }
                }
            }
        }
        if (string2 != null) {
            servletHolder.setClassName(string2);
        }
        string2 = node.getString("jsp-file", false, true);
        if (string2 != null) {
            servletHolder.setForcedPath(string2);
            servletHolder.setClassName(this._jspServletClass);
        }
        node2 = node.get("load-on-startup");
        if (node2 != null) {
            attribute = node2.toString(false, true).toLowerCase();
            if (attribute.startsWith("t")) {
                Log.warn("Deprecated boolean load-on-startup.  Please use integer");
                servletHolder.setInitOrder(1);
            } else {
                int parseInt;
                if (attribute != null) {
                    try {
                        if (attribute.trim().length() > 0) {
                            parseInt = Integer.parseInt(attribute);
                            servletHolder.setInitOrder(parseInt);
                        }
                    } catch (Throwable e2) {
                        Log.warn(new StringBuffer().append("Cannot parse load-on-startup ").append(attribute).append(". Please use integer").toString());
                        Log.ignore(e2);
                        parseInt = 0;
                    }
                }
                parseInt = 0;
                servletHolder.setInitOrder(parseInt);
            }
        }
        Iterator it2 = node.iterator("security-role-ref");
        while (it2.hasNext()) {
            node2 = (Node) it2.next();
            string = node2.getString("role-name", false, true);
            String string3 = node2.getString("role-link", false, true);
            if (string == null || string.length() <= 0 || string3 == null || string3.length() <= 0) {
                Log.warn(new StringBuffer().append("Ignored invalid security-role-ref element: servlet-name=").append(servletHolder.getName()).append(", ").append(node2).toString());
            } else {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("link role ").append(string).append(" to ").append(string3).append(" for ").append(this).toString());
                }
                servletHolder.setUserRoleLink(string, string3);
            }
        }
        node2 = node.get("run-as");
        if (node2 != null) {
            string2 = node2.getString("role-name", false, true);
            if (string2 != null) {
                servletHolder.setRunAs(string2);
            }
        }
    }

    protected void initServletMapping(Node node) {
        String string = node.getString("servlet-name", false, true);
        ServletMapping servletMapping = new ServletMapping();
        servletMapping.setServletName(string);
        ArrayList arrayList = new ArrayList();
        Iterator it = node.iterator("url-pattern");
        while (it.hasNext()) {
            arrayList.add(normalizePattern(((Node) it.next()).toString(false, true)));
        }
        servletMapping.setPathSpecs((String[]) arrayList.toArray(new String[arrayList.size()]));
        this._servletMappings = LazyList.add(this._servletMappings, servletMapping);
    }

    protected void initSessionConfig(Node node) {
        Node node2 = node.get("session-timeout");
        if (node2 != null) {
            getWebAppContext().getSessionHandler().getSessionManager().setMaxInactiveInterval(Integer.parseInt(node2.toString(false, true)) * 60);
        }
    }

    protected void initTagLib(Node node) {
        getWebAppContext().setResourceAlias(node.getString("taglib-uri", false, true), node.getString("taglib-location", false, true));
    }

    protected void initWebXmlElement(String str, Node node) throws Exception {
        if ("display-name".equals(str)) {
            initDisplayName(node);
        } else if (!PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION.equals(str)) {
            if ("context-param".equals(str)) {
                initContextParam(node);
            } else if ("servlet".equals(str)) {
                initServlet(node);
            } else if ("servlet-mapping".equals(str)) {
                initServletMapping(node);
            } else if ("session-config".equals(str)) {
                initSessionConfig(node);
            } else if ("mime-mapping".equals(str)) {
                initMimeConfig(node);
            } else if ("welcome-file-list".equals(str)) {
                initWelcomeFileList(node);
            } else if ("locale-encoding-mapping-list".equals(str)) {
                initLocaleEncodingList(node);
            } else if ("error-page".equals(str)) {
                initErrorPage(node);
            } else if ("taglib".equals(str)) {
                initTagLib(node);
            } else if ("jsp-config".equals(str)) {
                initJspConfig(node);
            } else if ("resource-ref".equals(str)) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("No implementation: ").append(node).toString());
                }
            } else if ("security-constraint".equals(str)) {
                initSecurityConstraint(node);
            } else if ("login-config".equals(str)) {
                initLoginConfig(node);
            } else if ("security-role".equals(str)) {
                initSecurityRole(node);
            } else if ("filter".equals(str)) {
                initFilter(node);
            } else if ("filter-mapping".equals(str)) {
                initFilterMapping(node);
            } else if ("listener".equals(str)) {
                initListener(node);
            } else if ("distributable".equals(str)) {
                initDistributable(node);
            } else if (Log.isDebugEnabled()) {
                Log.debug("Element {} not handled in {}", str, this);
                Log.debug(node.toString());
            }
        }
    }

    protected void initWelcomeFileList(Node node) {
        if (this._defaultWelcomeFileList) {
            this._welcomeFiles = null;
        }
        this._defaultWelcomeFileList = false;
        Iterator it = node.iterator("welcome-file");
        while (it.hasNext()) {
            this._welcomeFiles = LazyList.add(this._welcomeFiles, ((Node) it.next()).toString(false, true));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void initialize(org.mortbay.xml.XmlParser.Node r6) throws java.lang.ClassNotFoundException, javax.servlet.UnavailableException {
        /*
        r5 = this;
        r1 = 0;
        r0 = r5.getWebAppContext();
        r0 = r0.getServletHandler();
        r5._servletHandler = r0;
        r0 = r5._servletHandler;
        r0 = r0.getFilters();
        r0 = org.mortbay.util.LazyList.array2List(r0);
        r5._filters = r0;
        r0 = r5._servletHandler;
        r0 = r0.getFilterMappings();
        r0 = org.mortbay.util.LazyList.array2List(r0);
        r5._filterMappings = r0;
        r0 = r5._servletHandler;
        r0 = r0.getServlets();
        r0 = org.mortbay.util.LazyList.array2List(r0);
        r5._servlets = r0;
        r0 = r5._servletHandler;
        r0 = r0.getServletMappings();
        r0 = org.mortbay.util.LazyList.array2List(r0);
        r5._servletMappings = r0;
        r0 = r5.getWebAppContext();
        r0 = r0.getEventListeners();
        r0 = org.mortbay.util.LazyList.array2List(r0);
        r5._listeners = r0;
        r0 = r5.getWebAppContext();
        r0 = r0.getWelcomeFiles();
        r0 = org.mortbay.util.LazyList.array2List(r0);
        r5._welcomeFiles = r0;
        r0 = r5.getWebAppContext();
        r0 = r0.getSecurityHandler();
        r0 = r0.getConstraintMappings();
        r0 = org.mortbay.util.LazyList.array2List(r0);
        r5._constraintMappings = r0;
        r0 = r5.getWebAppContext();
        r0 = r0.getErrorHandler();
        r0 = r0 instanceof org.mortbay.jetty.servlet.ErrorPageErrorHandler;
        if (r0 == 0) goto L_0x00b8;
    L_0x0075:
        r0 = r5.getWebAppContext();
        r0 = r0.getErrorHandler();
        r0 = (org.mortbay.jetty.servlet.ErrorPageErrorHandler) r0;
        r0 = r0.getErrorPages();
    L_0x0083:
        r5._errorPages = r0;
        r0 = "version";
        r2 = "DTD";
        r0 = r6.getAttribute(r0, r2);
        r2 = "2.5";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x00ba;
    L_0x0095:
        r0 = 25;
        r5._version = r0;
    L_0x0099:
        r2 = r6.iterator();
    L_0x009d:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x0117;
    L_0x00a3:
        r0 = r2.next();	 Catch:{ ClassNotFoundException -> 0x00b6, Exception -> 0x0217 }
        r3 = r0 instanceof org.mortbay.xml.XmlParser.Node;	 Catch:{ ClassNotFoundException -> 0x00b6, Exception -> 0x0217 }
        if (r3 == 0) goto L_0x009d;
    L_0x00ab:
        r0 = (org.mortbay.xml.XmlParser.Node) r0;	 Catch:{ ClassNotFoundException -> 0x00b6, Exception -> 0x0217 }
        r1 = r0.getTag();	 Catch:{ ClassNotFoundException -> 0x00b6, Exception -> 0x00e8 }
        r5.initWebXmlElement(r1, r0);	 Catch:{ ClassNotFoundException -> 0x00b6, Exception -> 0x00e8 }
        r1 = r0;
        goto L_0x009d;
    L_0x00b6:
        r0 = move-exception;
        throw r0;
    L_0x00b8:
        r0 = r1;
        goto L_0x0083;
    L_0x00ba:
        r2 = "2.4";
        r2 = r2.equals(r0);
        if (r2 == 0) goto L_0x00c7;
    L_0x00c2:
        r0 = 24;
        r5._version = r0;
        goto L_0x0099;
    L_0x00c7:
        r2 = "DTD";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x0099;
    L_0x00cf:
        r0 = 23;
        r5._version = r0;
        r0 = r5._xmlParser;
        r0 = r0.getDTD();
        if (r0 == 0) goto L_0x0099;
    L_0x00db:
        r2 = "web-app_2_2";
        r0 = r0.indexOf(r2);
        if (r0 < 0) goto L_0x0099;
    L_0x00e3:
        r0 = 22;
        r5._version = r0;
        goto L_0x0099;
    L_0x00e8:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x00ec:
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r3 = "Configuration problem at ";
        r2 = r2.append(r3);
        r1 = r2.append(r1);
        r2 = ": ";
        r1 = r1.append(r2);
        r1 = r1.append(r0);
        r1 = r1.toString();
        org.mortbay.log.Log.warn(r1);
        org.mortbay.log.Log.debug(r0);
        r0 = new javax.servlet.UnavailableException;
        r1 = "Configuration problem";
        r0.<init>(r1);
        throw r0;
    L_0x0117:
        r1 = r5._servletHandler;
        r2 = r5._filters;
        r0 = class$org$mortbay$jetty$servlet$FilterHolder;
        if (r0 != 0) goto L_0x01fe;
    L_0x011f:
        r0 = "org.mortbay.jetty.servlet.FilterHolder";
        r0 = class$(r0);
        class$org$mortbay$jetty$servlet$FilterHolder = r0;
    L_0x0127:
        r0 = org.mortbay.util.LazyList.toArray(r2, r0);
        r0 = (org.mortbay.jetty.servlet.FilterHolder[]) r0;
        r0 = (org.mortbay.jetty.servlet.FilterHolder[]) r0;
        r1.setFilters(r0);
        r1 = r5._servletHandler;
        r2 = r5._filterMappings;
        r0 = class$org$mortbay$jetty$servlet$FilterMapping;
        if (r0 != 0) goto L_0x0202;
    L_0x013a:
        r0 = "org.mortbay.jetty.servlet.FilterMapping";
        r0 = class$(r0);
        class$org$mortbay$jetty$servlet$FilterMapping = r0;
    L_0x0142:
        r0 = org.mortbay.util.LazyList.toArray(r2, r0);
        r0 = (org.mortbay.jetty.servlet.FilterMapping[]) r0;
        r0 = (org.mortbay.jetty.servlet.FilterMapping[]) r0;
        r1.setFilterMappings(r0);
        r1 = r5._servletHandler;
        r2 = r5._servlets;
        r0 = class$org$mortbay$jetty$servlet$ServletHolder;
        if (r0 != 0) goto L_0x0206;
    L_0x0155:
        r0 = "org.mortbay.jetty.servlet.ServletHolder";
        r0 = class$(r0);
        class$org$mortbay$jetty$servlet$ServletHolder = r0;
    L_0x015d:
        r0 = org.mortbay.util.LazyList.toArray(r2, r0);
        r0 = (org.mortbay.jetty.servlet.ServletHolder[]) r0;
        r0 = (org.mortbay.jetty.servlet.ServletHolder[]) r0;
        r1.setServlets(r0);
        r1 = r5._servletHandler;
        r2 = r5._servletMappings;
        r0 = class$org$mortbay$jetty$servlet$ServletMapping;
        if (r0 != 0) goto L_0x020a;
    L_0x0170:
        r0 = "org.mortbay.jetty.servlet.ServletMapping";
        r0 = class$(r0);
        class$org$mortbay$jetty$servlet$ServletMapping = r0;
    L_0x0178:
        r0 = org.mortbay.util.LazyList.toArray(r2, r0);
        r0 = (org.mortbay.jetty.servlet.ServletMapping[]) r0;
        r0 = (org.mortbay.jetty.servlet.ServletMapping[]) r0;
        r1.setServletMappings(r0);
        r1 = r5.getWebAppContext();
        r2 = r5._listeners;
        r0 = class$java$util$EventListener;
        if (r0 != 0) goto L_0x020e;
    L_0x018d:
        r0 = "java.util.EventListener";
        r0 = class$(r0);
        class$java$util$EventListener = r0;
    L_0x0195:
        r0 = org.mortbay.util.LazyList.toArray(r2, r0);
        r0 = (java.util.EventListener[]) r0;
        r0 = (java.util.EventListener[]) r0;
        r1.setEventListeners(r0);
        r1 = r5.getWebAppContext();
        r2 = r5._welcomeFiles;
        r0 = class$java$lang$String;
        if (r0 != 0) goto L_0x0211;
    L_0x01aa:
        r0 = "java.lang.String";
        r0 = class$(r0);
        class$java$lang$String = r0;
    L_0x01b2:
        r0 = org.mortbay.util.LazyList.toArray(r2, r0);
        r0 = (java.lang.String[]) r0;
        r0 = (java.lang.String[]) r0;
        r1.setWelcomeFiles(r0);
        r0 = r5.getWebAppContext();
        r1 = r0.getSecurityHandler();
        r2 = r5._constraintMappings;
        r0 = class$org$mortbay$jetty$security$ConstraintMapping;
        if (r0 != 0) goto L_0x0214;
    L_0x01cb:
        r0 = "org.mortbay.jetty.security.ConstraintMapping";
        r0 = class$(r0);
        class$org$mortbay$jetty$security$ConstraintMapping = r0;
    L_0x01d3:
        r0 = org.mortbay.util.LazyList.toArray(r2, r0);
        r0 = (org.mortbay.jetty.security.ConstraintMapping[]) r0;
        r0 = (org.mortbay.jetty.security.ConstraintMapping[]) r0;
        r1.setConstraintMappings(r0);
        r0 = r5._errorPages;
        if (r0 == 0) goto L_0x01fd;
    L_0x01e2:
        r0 = r5.getWebAppContext();
        r0 = r0.getErrorHandler();
        r0 = r0 instanceof org.mortbay.jetty.servlet.ErrorPageErrorHandler;
        if (r0 == 0) goto L_0x01fd;
    L_0x01ee:
        r0 = r5.getWebAppContext();
        r0 = r0.getErrorHandler();
        r0 = (org.mortbay.jetty.servlet.ErrorPageErrorHandler) r0;
        r1 = r5._errorPages;
        r0.setErrorPages(r1);
    L_0x01fd:
        return;
    L_0x01fe:
        r0 = class$org$mortbay$jetty$servlet$FilterHolder;
        goto L_0x0127;
    L_0x0202:
        r0 = class$org$mortbay$jetty$servlet$FilterMapping;
        goto L_0x0142;
    L_0x0206:
        r0 = class$org$mortbay$jetty$servlet$ServletHolder;
        goto L_0x015d;
    L_0x020a:
        r0 = class$org$mortbay$jetty$servlet$ServletMapping;
        goto L_0x0178;
    L_0x020e:
        r0 = class$java$util$EventListener;
        goto L_0x0195;
    L_0x0211:
        r0 = class$java$lang$String;
        goto L_0x01b2;
    L_0x0214:
        r0 = class$org$mortbay$jetty$security$ConstraintMapping;
        goto L_0x01d3;
    L_0x0217:
        r0 = move-exception;
        goto L_0x00ec;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.webapp.WebXmlConfiguration.initialize(org.mortbay.xml.XmlParser$Node):void");
    }

    protected Object newListenerInstance(Class cls) throws InstantiationException, IllegalAccessException {
        return cls.newInstance();
    }

    protected String normalizePattern(String str) {
        return (str == null || str.length() <= 0 || str.startsWith(URIUtil.SLASH) || str.startsWith(Constraint.ANY_ROLE)) ? str : new StringBuffer().append(URIUtil.SLASH).append(str).toString();
    }

    public void setWebAppContext(WebAppContext webAppContext) {
        this._context = webAppContext;
    }
}
