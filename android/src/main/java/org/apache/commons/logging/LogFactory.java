package org.apache.commons.logging;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import org.mortbay.jetty.HttpVersions;

public abstract class LogFactory {
    public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
    public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
    public static final String FACTORY_PROPERTIES = "commons-logging.properties";
    public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
    public static final String PRIORITY_KEY = "priority";
    protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
    public static final String TCCL_KEY = "use_tccl";
    private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
    static Class class$java$lang$Thread;
    static Class class$org$apache$commons$logging$LogFactory;
    private static String diagnosticPrefix;
    private static PrintStream diagnosticsStream = null;
    protected static Hashtable factories;
    protected static LogFactory nullClassLoaderFactory = null;
    private static ClassLoader thisClassLoader;

    class C12821 implements PrivilegedAction {
        C12821() {
        }

        public Object run() {
            return LogFactory.directGetContextClassLoader();
        }
    }

    class C12832 implements PrivilegedAction {
        private final ClassLoader val$classLoader;
        private final String val$factoryClass;

        C12832(String str, ClassLoader classLoader) {
            this.val$factoryClass = str;
            this.val$classLoader = classLoader;
        }

        public Object run() {
            return LogFactory.createFactory(this.val$factoryClass, this.val$classLoader);
        }
    }

    class C12843 implements PrivilegedAction {
        private final ClassLoader val$loader;
        private final String val$name;

        C12843(ClassLoader classLoader, String str) {
            this.val$loader = classLoader;
            this.val$name = str;
        }

        public Object run() {
            return this.val$loader != null ? this.val$loader.getResourceAsStream(this.val$name) : ClassLoader.getSystemResourceAsStream(this.val$name);
        }
    }

    class C12854 implements PrivilegedAction {
        private final ClassLoader val$loader;
        private final String val$name;

        C12854(ClassLoader classLoader, String str) {
            this.val$loader = classLoader;
            this.val$name = str;
        }

        public Object run() {
            try {
                return this.val$loader != null ? this.val$loader.getResources(this.val$name) : ClassLoader.getSystemResources(this.val$name);
            } catch (Throwable e) {
                if (!LogFactory.isDiagnosticsEnabled()) {
                    return null;
                }
                LogFactory.access$000(new StringBuffer().append("Exception while trying to find configuration file ").append(this.val$name).append(":").append(e.getMessage()).toString());
                return null;
            } catch (NoSuchMethodError e2) {
                return null;
            }
        }
    }

    class C12865 implements PrivilegedAction {
        private final URL val$url;

        C12865(URL url) {
            this.val$url = url;
        }

        public Object run() {
            try {
                InputStream openStream = this.val$url.openStream();
                if (openStream != null) {
                    Properties properties = new Properties();
                    properties.load(openStream);
                    openStream.close();
                    return properties;
                }
            } catch (IOException e) {
                if (LogFactory.isDiagnosticsEnabled()) {
                    LogFactory.access$000(new StringBuffer().append("Unable to read URL ").append(this.val$url).toString());
                }
            }
            return null;
        }
    }

    class C12876 implements PrivilegedAction {
        private final String val$def;
        private final String val$key;

        C12876(String str, String str2) {
            this.val$key = str;
            this.val$def = str2;
        }

        public Object run() {
            return System.getProperty(this.val$key, this.val$def);
        }
    }

    static {
        Class class$;
        factories = null;
        if (class$org$apache$commons$logging$LogFactory == null) {
            class$ = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = class$;
        } else {
            class$ = class$org$apache$commons$logging$LogFactory;
        }
        thisClassLoader = getClassLoader(class$);
        initDiagnostics();
        if (class$org$apache$commons$logging$LogFactory == null) {
            class$ = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = class$;
        } else {
            class$ = class$org$apache$commons$logging$LogFactory;
        }
        logClassLoaderEnvironment(class$);
        factories = createFactoryStore();
        if (isDiagnosticsEnabled()) {
            logDiagnostic("BOOTSTRAP COMPLETED");
        }
    }

    protected LogFactory() {
    }

    static void access$000(String str) {
        logDiagnostic(str);
    }

    private static void cacheFactory(ClassLoader classLoader, LogFactory logFactory) {
        if (logFactory == null) {
            return;
        }
        if (classLoader == null) {
            nullClassLoaderFactory = logFactory;
        } else {
            factories.put(classLoader, logFactory);
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    protected static Object createFactory(String str, ClassLoader classLoader) {
        ClassNotFoundException classNotFoundException;
        Throwable e;
        Class cls;
        NoClassDefFoundError noClassDefFoundError;
        boolean implementsLogFactory;
        StringBuffer append;
        String stringBuffer;
        Class cls2 = null;
        if (classLoader != null) {
            Class loadClass;
            try {
                loadClass = classLoader.loadClass(str);
                try {
                    if (class$org$apache$commons$logging$LogFactory == null) {
                        cls2 = class$(FACTORY_PROPERTY);
                        class$org$apache$commons$logging$LogFactory = cls2;
                    } else {
                        cls2 = class$org$apache$commons$logging$LogFactory;
                    }
                    if (cls2.isAssignableFrom(loadClass)) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("Loaded class ").append(loadClass.getName()).append(" from classloader ").append(objectId(classLoader)).toString());
                        }
                    } else if (isDiagnosticsEnabled()) {
                        StringBuffer append2 = new StringBuffer().append("Factory class ").append(loadClass.getName()).append(" loaded from classloader ").append(objectId(loadClass.getClassLoader())).append(" does not extend '");
                        if (class$org$apache$commons$logging$LogFactory == null) {
                            cls2 = class$(FACTORY_PROPERTY);
                            class$org$apache$commons$logging$LogFactory = cls2;
                        } else {
                            cls2 = class$org$apache$commons$logging$LogFactory;
                        }
                        logDiagnostic(append2.append(cls2.getName()).append("' as loaded by this classloader.").toString());
                        logHierarchy("[BAD CL TREE] ", classLoader);
                    }
                    return (LogFactory) loadClass.newInstance();
                } catch (ClassNotFoundException e2) {
                    ClassNotFoundException classNotFoundException2 = e2;
                    cls2 = loadClass;
                    classNotFoundException = classNotFoundException2;
                    try {
                        if (classLoader == thisClassLoader) {
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer().append("Unable to locate any class called '").append(str).append("' via classloader ").append(objectId(classLoader)).toString());
                            }
                            throw classNotFoundException;
                        }
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
                        }
                        return (LogFactory) Class.forName(str).newInstance();
                    } catch (Exception e3) {
                        e = e3;
                        cls = cls2;
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic("Unable to create LogFactory instance.");
                        }
                        if (cls != null) {
                            if (class$org$apache$commons$logging$LogFactory == null) {
                                cls2 = class$(FACTORY_PROPERTY);
                                class$org$apache$commons$logging$LogFactory = cls2;
                            } else {
                                cls2 = class$org$apache$commons$logging$LogFactory;
                            }
                            if (!cls2.isAssignableFrom(cls)) {
                                return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e);
                            }
                        }
                        return new LogConfigurationException(e);
                    }
                } catch (NoClassDefFoundError e4) {
                    NoClassDefFoundError noClassDefFoundError2 = e4;
                    cls2 = loadClass;
                    noClassDefFoundError = noClassDefFoundError2;
                    if (classLoader == thisClassLoader) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("Class '").append(str).append("' cannot be loaded").append(" via classloader ").append(objectId(classLoader)).append(" - it depends on some other class that cannot").append(" be found.").toString());
                        }
                        throw noClassDefFoundError;
                    }
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
                    }
                    return (LogFactory) Class.forName(str).newInstance();
                } catch (ClassCastException e5) {
                    try {
                        if (classLoader != thisClassLoader) {
                            cls2 = loadClass;
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
                            }
                            return (LogFactory) Class.forName(str).newInstance();
                        }
                        implementsLogFactory = implementsLogFactory(loadClass);
                        append = new StringBuffer().append("The application has specified that a custom LogFactory implementation should be used but Class '").append(str).append("' cannot be converted to '");
                        if (class$org$apache$commons$logging$LogFactory != null) {
                            cls2 = class$org$apache$commons$logging$LogFactory;
                        } else {
                            cls2 = class$(FACTORY_PROPERTY);
                            class$org$apache$commons$logging$LogFactory = cls2;
                        }
                        stringBuffer = append.append(cls2.getName()).append("'. ").toString();
                        stringBuffer = new StringBuffer().append(implementsLogFactory ? new StringBuffer().append(stringBuffer).append("Please check the custom implementation. ").toString() : new StringBuffer().append(stringBuffer).append("The conflict is caused by the presence of multiple LogFactory classes in incompatible classloaders. ").append("Background can be found in http://commons.apache.org/logging/tech.html. ").append("If you have not explicitly specified a custom LogFactory then it is likely that ").append("the container has set one without your knowledge. ").append("In this case, consider using the commons-logging-adapters.jar file or ").append("specifying the standard LogFactory from the command line. ").toString()).append("Help can be found @http://commons.apache.org/logging/troubleshooting.html.").toString();
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(stringBuffer);
                        }
                        throw new ClassCastException(stringBuffer);
                    } catch (Throwable e6) {
                        cls = loadClass;
                        e = e6;
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic("Unable to create LogFactory instance.");
                        }
                        if (cls != null) {
                            if (class$org$apache$commons$logging$LogFactory == null) {
                                cls2 = class$org$apache$commons$logging$LogFactory;
                            } else {
                                cls2 = class$(FACTORY_PROPERTY);
                                class$org$apache$commons$logging$LogFactory = cls2;
                            }
                            if (cls2.isAssignableFrom(cls)) {
                                return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e);
                            }
                        }
                        return new LogConfigurationException(e);
                    }
                }
            } catch (ClassNotFoundException e7) {
                classNotFoundException = e7;
                if (classLoader == thisClassLoader) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("Unable to locate any class called '").append(str).append("' via classloader ").append(objectId(classLoader)).toString());
                    }
                    throw classNotFoundException;
                }
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
                }
                return (LogFactory) Class.forName(str).newInstance();
            } catch (NoClassDefFoundError e8) {
                noClassDefFoundError = e8;
                if (classLoader == thisClassLoader) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("Class '").append(str).append("' cannot be loaded").append(" via classloader ").append(objectId(classLoader)).append(" - it depends on some other class that cannot").append(" be found.").toString());
                    }
                    throw noClassDefFoundError;
                }
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
                }
                return (LogFactory) Class.forName(str).newInstance();
            } catch (ClassCastException e9) {
                loadClass = null;
                if (classLoader != thisClassLoader) {
                    implementsLogFactory = implementsLogFactory(loadClass);
                    append = new StringBuffer().append("The application has specified that a custom LogFactory implementation should be used but Class '").append(str).append("' cannot be converted to '");
                    if (class$org$apache$commons$logging$LogFactory != null) {
                        cls2 = class$(FACTORY_PROPERTY);
                        class$org$apache$commons$logging$LogFactory = cls2;
                    } else {
                        cls2 = class$org$apache$commons$logging$LogFactory;
                    }
                    stringBuffer = append.append(cls2.getName()).append("'. ").toString();
                    if (implementsLogFactory) {
                    }
                    stringBuffer = new StringBuffer().append(implementsLogFactory ? new StringBuffer().append(stringBuffer).append("Please check the custom implementation. ").toString() : new StringBuffer().append(stringBuffer).append("The conflict is caused by the presence of multiple LogFactory classes in incompatible classloaders. ").append("Background can be found in http://commons.apache.org/logging/tech.html. ").append("If you have not explicitly specified a custom LogFactory then it is likely that ").append("the container has set one without your knowledge. ").append("In this case, consider using the commons-logging-adapters.jar file or ").append("specifying the standard LogFactory from the command line. ").toString()).append("Help can be found @http://commons.apache.org/logging/troubleshooting.html.").toString();
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(stringBuffer);
                    }
                    throw new ClassCastException(stringBuffer);
                }
                cls2 = loadClass;
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
                }
                return (LogFactory) Class.forName(str).newInstance();
            } catch (Exception e10) {
                e = e10;
                cls = null;
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("Unable to create LogFactory instance.");
                }
                if (cls != null) {
                    if (class$org$apache$commons$logging$LogFactory == null) {
                        cls2 = class$(FACTORY_PROPERTY);
                        class$org$apache$commons$logging$LogFactory = cls2;
                    } else {
                        cls2 = class$org$apache$commons$logging$LogFactory;
                    }
                    if (cls2.isAssignableFrom(cls)) {
                        return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e);
                    }
                }
                return new LogConfigurationException(e);
            }
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
        }
        return (LogFactory) Class.forName(str).newInstance();
    }

    private static final Hashtable createFactoryStore() {
        String systemProperty;
        Hashtable hashtable;
        try {
            systemProperty = getSystemProperty(HASHTABLE_IMPLEMENTATION_PROPERTY, null);
        } catch (SecurityException e) {
            systemProperty = null;
        }
        String str = systemProperty == null ? WEAK_HASHTABLE_CLASSNAME : systemProperty;
        try {
            hashtable = (Hashtable) Class.forName(str).newInstance();
        } catch (Throwable th) {
            if (WEAK_HASHTABLE_CLASSNAME.equals(str)) {
                hashtable = null;
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
                hashtable = null;
            } else {
                System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
                hashtable = null;
            }
        }
        return hashtable == null ? new Hashtable() : hashtable;
    }

    protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
        Class class$;
        try {
            Class cls;
            if (class$java$lang$Thread == null) {
                class$ = class$("java.lang.Thread");
                class$java$lang$Thread = class$;
                cls = class$;
            } else {
                cls = class$java$lang$Thread;
            }
            return (ClassLoader) cls.getMethod("getContextClassLoader", (Class[]) null).invoke(Thread.currentThread(), (Object[]) null);
        } catch (Throwable e) {
            throw new LogConfigurationException("Unexpected IllegalAccessException", e);
        } catch (InvocationTargetException e2) {
            if (e2.getTargetException() instanceof SecurityException) {
                return null;
            }
            throw new LogConfigurationException("Unexpected InvocationTargetException", e2.getTargetException());
        } catch (NoSuchMethodException e3) {
            if (class$org$apache$commons$logging$LogFactory == null) {
                class$ = class$(FACTORY_PROPERTY);
                class$org$apache$commons$logging$LogFactory = class$;
            } else {
                class$ = class$org$apache$commons$logging$LogFactory;
            }
            return getClassLoader(class$);
        }
    }

    private static LogFactory getCachedFactory(ClassLoader classLoader) {
        return classLoader == null ? nullClassLoaderFactory : (LogFactory) factories.get(classLoader);
    }

    protected static ClassLoader getClassLoader(Class cls) {
        try {
            return cls.getClassLoader();
        } catch (Throwable e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Unable to get classloader for class '").append(cls).append("' due to security restrictions - ").append(e.getMessage()).toString());
            }
            throw e;
        }
    }

    private static final Properties getConfigurationFile(ClassLoader classLoader, String str) {
        Object obj;
        Properties properties;
        try {
            Enumeration resources = getResources(classLoader, str);
            if (resources == null) {
                return null;
            }
            obj = null;
            double d = 0.0d;
            properties = null;
            while (resources.hasMoreElements()) {
                try {
                    URL url = (URL) resources.nextElement();
                    Properties properties2 = getProperties(url);
                    if (properties2 != null) {
                        if (properties == null) {
                            try {
                                String property = properties2.getProperty(PRIORITY_KEY);
                                d = property != null ? Double.parseDouble(property) : 0.0d;
                                if (isDiagnosticsEnabled()) {
                                    logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file found at '").append(url).append("'").append(" with priority ").append(d).toString());
                                    obj = url;
                                    properties = properties2;
                                } else {
                                    obj = url;
                                    properties = properties2;
                                }
                            } catch (SecurityException e) {
                                URL url2 = url;
                                properties = properties2;
                            }
                        } else {
                            String property2 = properties2.getProperty(PRIORITY_KEY);
                            double parseDouble = property2 != null ? Double.parseDouble(property2) : 0.0d;
                            if (parseDouble > d) {
                                if (isDiagnosticsEnabled()) {
                                    logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file at '").append(url).append("'").append(" with priority ").append(parseDouble).append(" overrides file at '").append(obj).append("'").append(" with priority ").append(d).toString());
                                }
                                obj = url;
                                d = parseDouble;
                                properties = properties2;
                            } else if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file at '").append(url).append("'").append(" with priority ").append(parseDouble).append(" does not override file at '").append(obj).append("'").append(" with priority ").append(d).toString());
                            }
                        }
                    }
                } catch (SecurityException e2) {
                }
            }
            if (isDiagnosticsEnabled()) {
                if (properties != null) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] No properties file of name '").append(str).append("' found.").toString());
                } else {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file of name '").append(str).append("' found at '").append(obj).append('\"').toString());
                }
            }
            return properties;
        } catch (SecurityException e3) {
            obj = null;
            properties = null;
            if (isDiagnosticsEnabled()) {
                logDiagnostic("SecurityException thrown while trying to find/read config files.");
            }
            if (isDiagnosticsEnabled()) {
                if (properties != null) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file of name '").append(str).append("' found at '").append(obj).append('\"').toString());
                } else {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] No properties file of name '").append(str).append("' found.").toString());
                }
            }
            return properties;
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return directGetContextClassLoader();
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader) AccessController.doPrivileged(new C12821());
    }

    public static LogFactory getFactory() throws LogConfigurationException {
        LogFactory newFactory;
        ClassLoader contextClassLoaderInternal = getContextClassLoaderInternal();
        if (contextClassLoaderInternal == null && isDiagnosticsEnabled()) {
            logDiagnostic("Context classloader is null.");
        }
        LogFactory cachedFactory = getCachedFactory(contextClassLoaderInternal);
        if (cachedFactory != null) {
            return cachedFactory;
        }
        String property;
        ClassLoader classLoader;
        String systemProperty;
        InputStream resourceAsStream;
        BufferedReader bufferedReader;
        String readLine;
        Enumeration propertyNames;
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[LOOKUP] LogFactory implementation requested for the first time for context classloader ").append(objectId(contextClassLoaderInternal)).toString());
            logHierarchy("[LOOKUP] ", contextClassLoaderInternal);
        }
        Properties configurationFile = getConfigurationFile(contextClassLoaderInternal, FACTORY_PROPERTIES);
        if (configurationFile != null) {
            property = configurationFile.getProperty(TCCL_KEY);
            if (!(property == null || Boolean.valueOf(property).booleanValue())) {
                classLoader = thisClassLoader;
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
                }
                systemProperty = getSystemProperty(FACTORY_PROPERTY, null);
                if (systemProperty == null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("[LOOKUP] Creating an instance of LogFactory class '").append(systemProperty).append("' as specified by system property ").append(FACTORY_PROPERTY).toString());
                    }
                    newFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
                } else {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic("[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined.");
                        newFactory = cachedFactory;
                    }
                    newFactory = cachedFactory;
                }
                if (newFactory == null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic("[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
                    }
                    try {
                        resourceAsStream = getResourceAsStream(contextClassLoaderInternal, SERVICE_ID);
                        if (resourceAsStream != null) {
                            try {
                                bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
                            }
                            readLine = bufferedReader.readLine();
                            bufferedReader.close();
                            if (!(readLine == null || HttpVersions.HTTP_0_9.equals(readLine))) {
                                if (isDiagnosticsEnabled()) {
                                    logDiagnostic(new StringBuffer().append("[LOOKUP]  Creating an instance of LogFactory class ").append(readLine).append(" as specified by file '").append(SERVICE_ID).append("' which was present in the path of the context").append(" classloader.").toString());
                                }
                                cachedFactory = newFactory(readLine, classLoader, contextClassLoaderInternal);
                                if (cachedFactory == null) {
                                    if (configurationFile != null) {
                                        if (isDiagnosticsEnabled()) {
                                            logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                                        }
                                        systemProperty = configurationFile.getProperty(FACTORY_PROPERTY);
                                        if (systemProperty != null) {
                                            if (isDiagnosticsEnabled()) {
                                                logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file specifies LogFactory subclass '").append(systemProperty).append("'").toString());
                                            }
                                            cachedFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
                                        } else if (isDiagnosticsEnabled()) {
                                            logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                                        }
                                    } else if (isDiagnosticsEnabled()) {
                                        logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
                                    }
                                }
                                if (cachedFactory == null) {
                                    if (isDiagnosticsEnabled()) {
                                        logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
                                    }
                                    cachedFactory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoaderInternal);
                                }
                                if (cachedFactory != null) {
                                    cacheFactory(contextClassLoaderInternal, cachedFactory);
                                    if (configurationFile != null) {
                                        propertyNames = configurationFile.propertyNames();
                                        while (propertyNames.hasMoreElements()) {
                                            property = (String) propertyNames.nextElement();
                                            cachedFactory.setAttribute(property, configurationFile.getProperty(property));
                                        }
                                    }
                                }
                                return cachedFactory;
                            }
                        } else if (isDiagnosticsEnabled()) {
                            logDiagnostic("[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found.");
                            cachedFactory = newFactory;
                            if (cachedFactory == null) {
                                if (configurationFile != null) {
                                    if (isDiagnosticsEnabled()) {
                                        logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                                    }
                                    systemProperty = configurationFile.getProperty(FACTORY_PROPERTY);
                                    if (systemProperty != null) {
                                        if (isDiagnosticsEnabled()) {
                                            logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file specifies LogFactory subclass '").append(systemProperty).append("'").toString());
                                        }
                                        cachedFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
                                    } else if (isDiagnosticsEnabled()) {
                                        logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                                    }
                                } else if (isDiagnosticsEnabled()) {
                                    logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
                                }
                            }
                            if (cachedFactory == null) {
                                if (isDiagnosticsEnabled()) {
                                    logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
                                }
                                cachedFactory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoaderInternal);
                            }
                            if (cachedFactory != null) {
                                cacheFactory(contextClassLoaderInternal, cachedFactory);
                                if (configurationFile != null) {
                                    propertyNames = configurationFile.propertyNames();
                                    while (propertyNames.hasMoreElements()) {
                                        property = (String) propertyNames.nextElement();
                                        cachedFactory.setAttribute(property, configurationFile.getProperty(property));
                                    }
                                }
                            }
                            return cachedFactory;
                        }
                    } catch (Throwable e2) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [").append(trim(e2.getMessage())).append("]. Trying alternative implementations...").toString());
                            cachedFactory = newFactory;
                        }
                    }
                }
                cachedFactory = newFactory;
                if (cachedFactory == null) {
                    if (configurationFile != null) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                        }
                        systemProperty = configurationFile.getProperty(FACTORY_PROPERTY);
                        if (systemProperty != null) {
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file specifies LogFactory subclass '").append(systemProperty).append("'").toString());
                            }
                            cachedFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
                        } else if (isDiagnosticsEnabled()) {
                            logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                        }
                    } else if (isDiagnosticsEnabled()) {
                        logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
                    }
                }
                if (cachedFactory == null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
                    }
                    cachedFactory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoaderInternal);
                }
                if (cachedFactory != null) {
                    cacheFactory(contextClassLoaderInternal, cachedFactory);
                    if (configurationFile != null) {
                        propertyNames = configurationFile.propertyNames();
                        while (propertyNames.hasMoreElements()) {
                            property = (String) propertyNames.nextElement();
                            cachedFactory.setAttribute(property, configurationFile.getProperty(property));
                        }
                    }
                }
                return cachedFactory;
            }
        }
        classLoader = contextClassLoaderInternal;
        if (isDiagnosticsEnabled()) {
            logDiagnostic("[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
        }
        try {
            systemProperty = getSystemProperty(FACTORY_PROPERTY, null);
            if (systemProperty == null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined.");
                    newFactory = cachedFactory;
                }
                newFactory = cachedFactory;
            } else {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] Creating an instance of LogFactory class '").append(systemProperty).append("' as specified by system property ").append(FACTORY_PROPERTY).toString());
                }
                newFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
            }
        } catch (Throwable e3) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [").append(trim(e3.getMessage())).append("]. Trying alternative implementations...").toString());
                newFactory = cachedFactory;
            }
        } catch (Throwable e4) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[LOOKUP] An exception occurred while trying to create an instance of the custom factory class: [").append(trim(e4.getMessage())).append("] as specified by a system property.").toString());
            }
            throw e4;
        }
        if (newFactory == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
            }
            resourceAsStream = getResourceAsStream(contextClassLoaderInternal, SERVICE_ID);
            if (resourceAsStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
                readLine = bufferedReader.readLine();
                bufferedReader.close();
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP]  Creating an instance of LogFactory class ").append(readLine).append(" as specified by file '").append(SERVICE_ID).append("' which was present in the path of the context").append(" classloader.").toString());
                }
                cachedFactory = newFactory(readLine, classLoader, contextClassLoaderInternal);
                if (cachedFactory == null) {
                    if (configurationFile != null) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                        }
                        systemProperty = configurationFile.getProperty(FACTORY_PROPERTY);
                        if (systemProperty != null) {
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file specifies LogFactory subclass '").append(systemProperty).append("'").toString());
                            }
                            cachedFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
                        } else if (isDiagnosticsEnabled()) {
                            logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                        }
                    } else if (isDiagnosticsEnabled()) {
                        logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
                    }
                }
                if (cachedFactory == null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
                    }
                    cachedFactory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoaderInternal);
                }
                if (cachedFactory != null) {
                    cacheFactory(contextClassLoaderInternal, cachedFactory);
                    if (configurationFile != null) {
                        propertyNames = configurationFile.propertyNames();
                        while (propertyNames.hasMoreElements()) {
                            property = (String) propertyNames.nextElement();
                            cachedFactory.setAttribute(property, configurationFile.getProperty(property));
                        }
                    }
                }
                return cachedFactory;
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found.");
                cachedFactory = newFactory;
                if (cachedFactory == null) {
                    if (configurationFile != null) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                        }
                        systemProperty = configurationFile.getProperty(FACTORY_PROPERTY);
                        if (systemProperty != null) {
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file specifies LogFactory subclass '").append(systemProperty).append("'").toString());
                            }
                            cachedFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
                        } else if (isDiagnosticsEnabled()) {
                            logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                        }
                    } else if (isDiagnosticsEnabled()) {
                        logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
                    }
                }
                if (cachedFactory == null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
                    }
                    cachedFactory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoaderInternal);
                }
                if (cachedFactory != null) {
                    cacheFactory(contextClassLoaderInternal, cachedFactory);
                    if (configurationFile != null) {
                        propertyNames = configurationFile.propertyNames();
                        while (propertyNames.hasMoreElements()) {
                            property = (String) propertyNames.nextElement();
                            cachedFactory.setAttribute(property, configurationFile.getProperty(property));
                        }
                    }
                }
                return cachedFactory;
            }
        }
        cachedFactory = newFactory;
        if (cachedFactory == null) {
            if (configurationFile != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                }
                systemProperty = configurationFile.getProperty(FACTORY_PROPERTY);
                if (systemProperty != null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file specifies LogFactory subclass '").append(systemProperty).append("'").toString());
                    }
                    cachedFactory = newFactory(systemProperty, classLoader, contextClassLoaderInternal);
                } else if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                }
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
            }
        }
        if (cachedFactory == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
            }
            cachedFactory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoaderInternal);
        }
        if (cachedFactory != null) {
            cacheFactory(contextClassLoaderInternal, cachedFactory);
            if (configurationFile != null) {
                propertyNames = configurationFile.propertyNames();
                while (propertyNames.hasMoreElements()) {
                    property = (String) propertyNames.nextElement();
                    cachedFactory.setAttribute(property, configurationFile.getProperty(property));
                }
            }
        }
        return cachedFactory;
    }

    public static Log getLog(Class cls) throws LogConfigurationException {
        return getFactory().getInstance(cls);
    }

    public static Log getLog(String str) throws LogConfigurationException {
        return getFactory().getInstance(str);
    }

    private static Properties getProperties(URL url) {
        return (Properties) AccessController.doPrivileged(new C12865(url));
    }

    private static InputStream getResourceAsStream(ClassLoader classLoader, String str) {
        return (InputStream) AccessController.doPrivileged(new C12843(classLoader, str));
    }

    private static Enumeration getResources(ClassLoader classLoader, String str) {
        return (Enumeration) AccessController.doPrivileged(new C12854(classLoader, str));
    }

    private static String getSystemProperty(String str, String str2) throws SecurityException {
        return (String) AccessController.doPrivileged(new C12876(str, str2));
    }

    private static boolean implementsLogFactory(Class cls) {
        boolean z = false;
        if (cls != null) {
            try {
                ClassLoader classLoader = cls.getClassLoader();
                if (classLoader == null) {
                    logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
                } else {
                    logHierarchy("[CUSTOM LOG FACTORY] ", classLoader);
                    z = Class.forName(FACTORY_PROPERTY, false, classLoader).isAssignableFrom(cls);
                    if (z) {
                        logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] ").append(cls.getName()).append(" implements LogFactory but was loaded by an incompatible classloader.").toString());
                    } else {
                        logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] ").append(cls.getName()).append(" does not implement LogFactory.").toString());
                    }
                }
            } catch (Throwable e) {
                logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ").append(e.getMessage()).toString());
            } catch (Throwable e2) {
                logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ").append(e2.getMessage()).toString());
            } catch (ClassNotFoundException e3) {
                logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
            }
        }
        return z;
    }

    private static void initDiagnostics() {
        try {
            String systemProperty = getSystemProperty(DIAGNOSTICS_DEST_PROPERTY, null);
            if (systemProperty != null) {
                if (systemProperty.equals("STDOUT")) {
                    diagnosticsStream = System.out;
                } else if (systemProperty.equals("STDERR")) {
                    diagnosticsStream = System.err;
                } else {
                    try {
                        diagnosticsStream = new PrintStream(new FileOutputStream(systemProperty, true));
                    } catch (IOException e) {
                        return;
                    }
                }
                try {
                    systemProperty = thisClassLoader == null ? "BOOTLOADER" : objectId(thisClassLoader);
                } catch (SecurityException e2) {
                    systemProperty = "UNKNOWN";
                }
                diagnosticPrefix = new StringBuffer().append("[LogFactory from ").append(systemProperty).append("] ").toString();
            }
        } catch (SecurityException e3) {
        }
    }

    protected static boolean isDiagnosticsEnabled() {
        return diagnosticsStream != null;
    }

    private static void logClassLoaderEnvironment(Class cls) {
        if (isDiagnosticsEnabled()) {
            try {
                logDiagnostic(new StringBuffer().append("[ENV] Extension directories (java.ext.dir): ").append(System.getProperty("java.ext.dir")).toString());
                logDiagnostic(new StringBuffer().append("[ENV] Application classpath (java.class.path): ").append(System.getProperty("java.class.path")).toString());
            } catch (SecurityException e) {
                logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
            }
            String name = cls.getName();
            try {
                ClassLoader classLoader = getClassLoader(cls);
                logDiagnostic(new StringBuffer().append("[ENV] Class ").append(name).append(" was loaded via classloader ").append(objectId(classLoader)).toString());
                logHierarchy(new StringBuffer().append("[ENV] Ancestry of classloader which loaded ").append(name).append(" is ").toString(), classLoader);
            } catch (SecurityException e2) {
                logDiagnostic(new StringBuffer().append("[ENV] Security forbids determining the classloader for ").append(name).toString());
            }
        }
    }

    private static final void logDiagnostic(String str) {
        if (diagnosticsStream != null) {
            diagnosticsStream.print(diagnosticPrefix);
            diagnosticsStream.println(str);
            diagnosticsStream.flush();
        }
    }

    private static void logHierarchy(String str, ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            if (classLoader != null) {
                logDiagnostic(new StringBuffer().append(str).append(objectId(classLoader)).append(" == '").append(classLoader.toString()).append("'").toString());
            }
            try {
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                if (classLoader != null) {
                    StringBuffer stringBuffer = new StringBuffer(new StringBuffer().append(str).append("ClassLoader tree:").toString());
                    do {
                        stringBuffer.append(objectId(classLoader));
                        if (classLoader == systemClassLoader) {
                            stringBuffer.append(" (SYSTEM) ");
                        }
                        try {
                            classLoader = classLoader.getParent();
                            stringBuffer.append(" --> ");
                        } catch (SecurityException e) {
                            stringBuffer.append(" --> SECRET");
                        }
                    } while (classLoader != null);
                    stringBuffer.append("BOOT");
                    logDiagnostic(stringBuffer.toString());
                }
            } catch (SecurityException e2) {
                logDiagnostic(new StringBuffer().append(str).append("Security forbids determining the system classloader.").toString());
            }
        }
    }

    protected static final void logRawDiagnostic(String str) {
        if (diagnosticsStream != null) {
            diagnosticsStream.println(str);
            diagnosticsStream.flush();
        }
    }

    protected static LogFactory newFactory(String str, ClassLoader classLoader) {
        return newFactory(str, classLoader, null);
    }

    protected static LogFactory newFactory(String str, ClassLoader classLoader, ClassLoader classLoader2) throws LogConfigurationException {
        Object doPrivileged = AccessController.doPrivileged(new C12832(str, classLoader));
        if (doPrivileged instanceof LogConfigurationException) {
            LogConfigurationException logConfigurationException = (LogConfigurationException) doPrivileged;
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("An error occurred while loading the factory class:").append(logConfigurationException.getMessage()).toString());
            }
            throw logConfigurationException;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Created object ").append(objectId(doPrivileged)).append(" to manage classloader ").append(objectId(classLoader2)).toString());
        }
        return (LogFactory) doPrivileged;
    }

    public static String objectId(Object obj) {
        return obj == null ? "null" : new StringBuffer().append(obj.getClass().getName()).append("@").append(System.identityHashCode(obj)).toString();
    }

    public static void release(ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Releasing factory for classloader ").append(objectId(classLoader)).toString());
        }
        synchronized (factories) {
            if (classLoader != null) {
                LogFactory logFactory = (LogFactory) factories.get(classLoader);
                if (logFactory != null) {
                    logFactory.release();
                    factories.remove(classLoader);
                }
            } else if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    public static void releaseAll() {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Releasing factory for all classloaders.");
        }
        synchronized (factories) {
            Enumeration elements = factories.elements();
            while (elements.hasMoreElements()) {
                ((LogFactory) elements.nextElement()).release();
            }
            factories.clear();
            if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    private static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public abstract Object getAttribute(String str);

    public abstract String[] getAttributeNames();

    public abstract Log getInstance(Class cls) throws LogConfigurationException;

    public abstract Log getInstance(String str) throws LogConfigurationException;

    public abstract void release();

    public abstract void removeAttribute(String str);

    public abstract void setAttribute(String str, Object obj);
}
