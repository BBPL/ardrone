package org.apache.commons.logging.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class LogFactoryImpl extends LogFactory {
    public static final String ALLOW_FLAWED_CONTEXT_PROPERTY = "org.apache.commons.logging.Log.allowFlawedContext";
    public static final String ALLOW_FLAWED_DISCOVERY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedDiscovery";
    public static final String ALLOW_FLAWED_HIERARCHY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedHierarchy";
    private static final String LOGGING_IMPL_JDK14_LOGGER = "org.apache.commons.logging.impl.Jdk14Logger";
    private static final String LOGGING_IMPL_LOG4J_LOGGER = "org.apache.commons.logging.impl.Log4JLogger";
    private static final String LOGGING_IMPL_LUMBERJACK_LOGGER = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
    private static final String LOGGING_IMPL_SIMPLE_LOGGER = "org.apache.commons.logging.impl.SimpleLog";
    public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
    protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
    private static final String PKG_IMPL = "org.apache.commons.logging.impl.";
    private static final int PKG_LEN = PKG_IMPL.length();
    static Class class$java$lang$String;
    static Class class$org$apache$commons$logging$Log;
    static Class class$org$apache$commons$logging$LogFactory;
    static Class class$org$apache$commons$logging$impl$LogFactoryImpl;
    private static final String[] classesToDiscover = new String[]{LOGGING_IMPL_LOG4J_LOGGER, LOGGING_IMPL_JDK14_LOGGER, LOGGING_IMPL_LUMBERJACK_LOGGER, LOGGING_IMPL_SIMPLE_LOGGER};
    private boolean allowFlawedContext;
    private boolean allowFlawedDiscovery;
    private boolean allowFlawedHierarchy;
    protected Hashtable attributes = new Hashtable();
    private String diagnosticPrefix;
    protected Hashtable instances = new Hashtable();
    private String logClassName;
    protected Constructor logConstructor = null;
    protected Class[] logConstructorSignature;
    protected Method logMethod;
    protected Class[] logMethodSignature;
    private boolean useTCCL = true;

    class C12881 implements PrivilegedAction {
        C12881() {
        }

        public Object run() {
            return LogFactoryImpl.access$000();
        }
    }

    class C12892 implements PrivilegedAction {
        private final String val$def;
        private final String val$key;

        C12892(String str, String str2) {
            this.val$key = str;
            this.val$def = str2;
        }

        public Object run() {
            return System.getProperty(this.val$key, this.val$def);
        }
    }

    class C12903 implements PrivilegedAction {
        private final LogFactoryImpl this$0;
        private final ClassLoader val$cl;

        C12903(LogFactoryImpl logFactoryImpl, ClassLoader classLoader) {
            this.this$0 = logFactoryImpl;
            this.val$cl = classLoader;
        }

        public Object run() {
            return this.val$cl.getParent();
        }
    }

    public LogFactoryImpl() {
        Class class$;
        if (class$java$lang$String == null) {
            class$ = class$("java.lang.String");
            class$java$lang$String = class$;
        } else {
            class$ = class$java$lang$String;
        }
        this.logConstructorSignature = new Class[]{class$};
        this.logMethod = null;
        if (class$org$apache$commons$logging$LogFactory == null) {
            class$ = class$(LogFactory.FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = class$;
        } else {
            class$ = class$org$apache$commons$logging$LogFactory;
        }
        this.logMethodSignature = new Class[]{class$};
        initDiagnostics();
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Instance created.");
        }
    }

    static ClassLoader access$000() throws LogConfigurationException {
        return LogFactory.directGetContextClassLoader();
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.commons.logging.Log createLogFromClass(java.lang.String r9, java.lang.String r10, boolean r11) throws org.apache.commons.logging.LogConfigurationException {
        /*
        r8 = this;
        r2 = 0;
        r0 = isDiagnosticsEnabled();
        if (r0 == 0) goto L_0x0023;
    L_0x0007:
        r0 = new java.lang.StringBuffer;
        r0.<init>();
        r1 = "Attempting to instantiate '";
        r0 = r0.append(r1);
        r0 = r0.append(r9);
        r1 = "'";
        r0 = r0.append(r1);
        r0 = r0.toString();
        r8.logDiagnostic(r0);
    L_0x0023:
        r0 = r8.getBaseClassLoader();
        r1 = r0;
        r3 = r2;
        r0 = r2;
    L_0x002a:
        r4 = new java.lang.StringBuffer;
        r4.<init>();
        r5 = "Trying to load '";
        r4 = r4.append(r5);
        r4 = r4.append(r9);
        r5 = "' from classloader ";
        r4 = r4.append(r5);
        r5 = org.apache.commons.logging.LogFactory.objectId(r1);
        r4 = r4.append(r5);
        r4 = r4.toString();
        r8.logDiagnostic(r4);
        r4 = isDiagnosticsEnabled();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        if (r4 == 0) goto L_0x009d;
    L_0x0054:
        r4 = new java.lang.StringBuffer;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4.<init>();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = 46;
        r6 = 47;
        r5 = r9.replace(r5, r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.append(r5);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = ".class";
        r4 = r4.append(r5);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = r4.toString();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        if (r1 == 0) goto L_0x0115;
    L_0x0071:
        r4 = r1.getResource(r5);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
    L_0x0075:
        if (r4 != 0) goto L_0x012e;
    L_0x0077:
        r4 = new java.lang.StringBuffer;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4.<init>();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "Class '";
        r4 = r4.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.append(r9);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "' [";
        r4 = r4.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.append(r5);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = "] cannot be found.";
        r4 = r4.append(r5);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.toString();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r8.logDiagnostic(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
    L_0x009d:
        r4 = 1;
        r4 = java.lang.Class.forName(r9, r4, r1);	 Catch:{ ClassNotFoundException -> 0x01a8 }
    L_0x00a2:
        r5 = r8.logConstructorSignature;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = r4.getConstructor(r5);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r0 = 1;
        r0 = new java.lang.Object[r0];	 Catch:{ NoClassDefFoundError -> 0x02e9, ExceptionInInitializerError -> 0x02e2, LogConfigurationException -> 0x0298, Throwable -> 0x02db }
        r6 = 0;
        r0[r6] = r10;	 Catch:{ NoClassDefFoundError -> 0x02e9, ExceptionInInitializerError -> 0x02e2, LogConfigurationException -> 0x0298, Throwable -> 0x02db }
        r0 = r5.newInstance(r0);	 Catch:{ NoClassDefFoundError -> 0x02e9, ExceptionInInitializerError -> 0x02e2, LogConfigurationException -> 0x0298, Throwable -> 0x02db }
        r6 = r0 instanceof org.apache.commons.logging.Log;	 Catch:{ NoClassDefFoundError -> 0x02e9, ExceptionInInitializerError -> 0x02e2, LogConfigurationException -> 0x0298, Throwable -> 0x02db }
        if (r6 == 0) goto L_0x0288;
    L_0x00b6:
        r0 = (org.apache.commons.logging.Log) r0;	 Catch:{ NoClassDefFoundError -> 0x02e5, ExceptionInInitializerError -> 0x0239, LogConfigurationException -> 0x0298, Throwable -> 0x029a }
    L_0x00b8:
        if (r0 == 0) goto L_0x0114;
    L_0x00ba:
        if (r11 == 0) goto L_0x0114;
    L_0x00bc:
        r8.logClassName = r9;
        r8.logConstructor = r5;
        r3 = "setLogFactory";
        r5 = r8.logMethodSignature;	 Catch:{ Throwable -> 0x02a2 }
        r3 = r4.getMethod(r3, r5);	 Catch:{ Throwable -> 0x02a2 }
        r8.logMethod = r3;	 Catch:{ Throwable -> 0x02a2 }
        r3 = new java.lang.StringBuffer;	 Catch:{ Throwable -> 0x02a2 }
        r3.<init>();	 Catch:{ Throwable -> 0x02a2 }
        r5 = "Found method setLogFactory(LogFactory) in '";
        r3 = r3.append(r5);	 Catch:{ Throwable -> 0x02a2 }
        r3 = r3.append(r9);	 Catch:{ Throwable -> 0x02a2 }
        r5 = "'";
        r3 = r3.append(r5);	 Catch:{ Throwable -> 0x02a2 }
        r3 = r3.toString();	 Catch:{ Throwable -> 0x02a2 }
        r8.logDiagnostic(r3);	 Catch:{ Throwable -> 0x02a2 }
    L_0x00e6:
        r1 = new java.lang.StringBuffer;
        r1.<init>();
        r2 = "Log adapter '";
        r1 = r1.append(r2);
        r1 = r1.append(r9);
        r2 = "' from classloader ";
        r1 = r1.append(r2);
        r2 = r4.getClassLoader();
        r2 = org.apache.commons.logging.LogFactory.objectId(r2);
        r1 = r1.append(r2);
        r2 = " has been selected for use.";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r8.logDiagnostic(r1);
    L_0x0114:
        return r0;
    L_0x0115:
        r4 = new java.lang.StringBuffer;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4.<init>();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.append(r5);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = ".class";
        r4 = r4.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.toString();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = java.lang.ClassLoader.getSystemResource(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        goto L_0x0075;
    L_0x012e:
        r5 = new java.lang.StringBuffer;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5.<init>();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "Class '";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = r5.append(r9);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "' was found at '";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r5.append(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = "'";
        r4 = r4.append(r5);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.toString();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r8.logDiagnostic(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        goto L_0x009d;
    L_0x0156:
        r4 = move-exception;
        r7 = r4;
        r4 = r3;
        r3 = r0;
        r0 = r7;
    L_0x015b:
        r5 = new java.lang.StringBuffer;
        r5.<init>();
        r6 = "";
        r5 = r5.append(r6);
        r0 = r0.getMessage();
        r0 = r5.append(r0);
        r0 = r0.toString();
        r5 = new java.lang.StringBuffer;
        r5.<init>();
        r6 = "The log adapter '";
        r5 = r5.append(r6);
        r5 = r5.append(r9);
        r6 = "' is missing dependencies when loaded via classloader ";
        r5 = r5.append(r6);
        r6 = org.apache.commons.logging.LogFactory.objectId(r1);
        r5 = r5.append(r6);
        r6 = ": ";
        r5 = r5.append(r6);
        r0 = r0.trim();
        r0 = r5.append(r0);
        r0 = r0.toString();
        r8.logDiagnostic(r0);
        r0 = r2;
        r5 = r3;
        goto L_0x00b8;
    L_0x01a8:
        r4 = move-exception;
        r5 = new java.lang.StringBuffer;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5.<init>();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.getMessage();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r5.append(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.toString();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = new java.lang.StringBuffer;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5.<init>();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "The log adapter '";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = r5.append(r9);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "' is not available via classloader ";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = org.apache.commons.logging.LogFactory.objectId(r1);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = ": ";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.trim();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r5.append(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.toString();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r8.logDiagnostic(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = java.lang.Class.forName(r9);	 Catch:{ ClassNotFoundException -> 0x01f8 }
        goto L_0x00a2;
    L_0x01f8:
        r4 = move-exception;
        r5 = new java.lang.StringBuffer;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5.<init>();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.getMessage();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r5.append(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.toString();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = new java.lang.StringBuffer;	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5.<init>();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "The log adapter '";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = r5.append(r9);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r6 = "' is not available via the LogFactoryImpl class classloader: ";
        r5 = r5.append(r6);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.trim();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r5.append(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r4 = r4.toString();	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r8.logDiagnostic(r4);	 Catch:{ NoClassDefFoundError -> 0x0156, ExceptionInInitializerError -> 0x02dd, LogConfigurationException -> 0x0298, Throwable -> 0x02d7 }
        r5 = r0;
        r4 = r3;
        r0 = r2;
        goto L_0x00b8;
    L_0x0239:
        r0 = move-exception;
        r3 = r4;
    L_0x023b:
        r4 = new java.lang.StringBuffer;
        r4.<init>();
        r6 = "";
        r4 = r4.append(r6);
        r0 = r0.getMessage();
        r0 = r4.append(r0);
        r0 = r0.toString();
        r4 = new java.lang.StringBuffer;
        r4.<init>();
        r6 = "The log adapter '";
        r4 = r4.append(r6);
        r4 = r4.append(r9);
        r6 = "' is unable to initialize itself when loaded via classloader ";
        r4 = r4.append(r6);
        r6 = org.apache.commons.logging.LogFactory.objectId(r1);
        r4 = r4.append(r6);
        r6 = ": ";
        r4 = r4.append(r6);
        r0 = r0.trim();
        r0 = r4.append(r0);
        r0 = r0.toString();
        r8.logDiagnostic(r0);
        r0 = r2;
        r4 = r3;
        goto L_0x00b8;
    L_0x0288:
        r8.handleFlawedHierarchy(r1, r4);	 Catch:{ NoClassDefFoundError -> 0x02e9, ExceptionInInitializerError -> 0x02e2, LogConfigurationException -> 0x0298, Throwable -> 0x02db }
        r4 = r3;
        r3 = r5;
    L_0x028d:
        if (r1 == 0) goto L_0x02ee;
    L_0x028f:
        r0 = r8.getParentClassLoader(r1);
        r1 = r0;
        r0 = r3;
        r3 = r4;
        goto L_0x002a;
    L_0x0298:
        r0 = move-exception;
        throw r0;
    L_0x029a:
        r0 = move-exception;
        r3 = r4;
    L_0x029c:
        r8.handleFlawedDiscovery(r9, r1, r0);
        r4 = r3;
        r3 = r5;
        goto L_0x028d;
    L_0x02a2:
        r3 = move-exception;
        r8.logMethod = r2;
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r3 = "[INFO] '";
        r2 = r2.append(r3);
        r2 = r2.append(r9);
        r3 = "' from classloader ";
        r2 = r2.append(r3);
        r1 = org.apache.commons.logging.LogFactory.objectId(r1);
        r1 = r2.append(r1);
        r2 = " does not declare optional method ";
        r1 = r1.append(r2);
        r2 = "setLogFactory(LogFactory)";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r8.logDiagnostic(r1);
        goto L_0x00e6;
    L_0x02d7:
        r4 = move-exception;
        r5 = r0;
        r0 = r4;
        goto L_0x029c;
    L_0x02db:
        r0 = move-exception;
        goto L_0x029c;
    L_0x02dd:
        r4 = move-exception;
        r5 = r0;
        r0 = r4;
        goto L_0x023b;
    L_0x02e2:
        r0 = move-exception;
        goto L_0x023b;
    L_0x02e5:
        r0 = move-exception;
        r3 = r5;
        goto L_0x015b;
    L_0x02e9:
        r0 = move-exception;
        r4 = r3;
        r3 = r5;
        goto L_0x015b;
    L_0x02ee:
        r0 = r2;
        r5 = r3;
        goto L_0x00b8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.logging.impl.LogFactoryImpl.createLogFromClass(java.lang.String, java.lang.String, boolean):org.apache.commons.logging.Log");
    }

    private Log discoverLogImplementation(String str) throws LogConfigurationException {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Discovering a Log implementation...");
        }
        initConfiguration();
        Log log = null;
        String findUserSpecifiedLogClassName = findUserSpecifiedLogClassName();
        if (findUserSpecifiedLogClassName != null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Attempting to load user-specified log class '").append(findUserSpecifiedLogClassName).append("'...").toString());
            }
            log = createLogFromClass(findUserSpecifiedLogClassName, str, true);
            if (log == null) {
                StringBuffer stringBuffer = new StringBuffer("User-specified log class '");
                stringBuffer.append(findUserSpecifiedLogClassName);
                stringBuffer.append("' cannot be found or is not useable.");
                if (findUserSpecifiedLogClassName != null) {
                    informUponSimilarName(stringBuffer, findUserSpecifiedLogClassName, LOGGING_IMPL_LOG4J_LOGGER);
                    informUponSimilarName(stringBuffer, findUserSpecifiedLogClassName, LOGGING_IMPL_JDK14_LOGGER);
                    informUponSimilarName(stringBuffer, findUserSpecifiedLogClassName, LOGGING_IMPL_LUMBERJACK_LOGGER);
                    informUponSimilarName(stringBuffer, findUserSpecifiedLogClassName, LOGGING_IMPL_SIMPLE_LOGGER);
                }
                throw new LogConfigurationException(stringBuffer.toString());
            }
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic("No user-specified Log implementation; performing discovery using the standard supported logging implementations...");
        }
        for (int i = 0; i < classesToDiscover.length && log == null; i++) {
            log = createLogFromClass(classesToDiscover[i], str, true);
        }
        if (log == null) {
            throw new LogConfigurationException("No suitable Log implementation");
        }
        return log;
    }

    private String findUserSpecifiedLogClassName() {
        String str;
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.Log'");
        }
        String str2 = (String) getAttribute(LOG_PROPERTY);
        if (str2 == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.log'");
            }
            str = (String) getAttribute(LOG_PROPERTY_OLD);
        } else {
            str = str2;
        }
        if (str == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.Log'");
            }
            try {
                str = getSystemProperty(LOG_PROPERTY, null);
            } catch (Throwable e) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("No access allowed to system property 'org.apache.commons.logging.Log' - ").append(e.getMessage()).toString());
                }
            }
        }
        if (str == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.log'");
            }
            try {
                str2 = getSystemProperty(LOG_PROPERTY_OLD, null);
            } catch (Throwable e2) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("No access allowed to system property 'org.apache.commons.logging.log' - ").append(e2.getMessage()).toString());
                    str2 = str;
                }
            }
            return str2 == null ? str2.trim() : str2;
        }
        str2 = str;
        if (str2 == null) {
        }
    }

    private ClassLoader getBaseClassLoader() throws LogConfigurationException {
        Class class$;
        if (class$org$apache$commons$logging$impl$LogFactoryImpl == null) {
            class$ = class$(LogFactory.FACTORY_DEFAULT);
            class$org$apache$commons$logging$impl$LogFactoryImpl = class$;
        } else {
            class$ = class$org$apache$commons$logging$impl$LogFactoryImpl;
        }
        ClassLoader classLoader = getClassLoader(class$);
        if (!this.useTCCL) {
            return classLoader;
        }
        ClassLoader contextClassLoaderInternal = getContextClassLoaderInternal();
        classLoader = getLowestClassLoader(contextClassLoaderInternal, classLoader);
        if (classLoader == null) {
            if (this.allowFlawedContext) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[WARNING] the context classloader is not part of a parent-child relationship with the classloader that loaded LogFactoryImpl.");
                }
                return contextClassLoaderInternal;
            }
            throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
        } else if (classLoader == contextClassLoaderInternal) {
            return classLoader;
        } else {
            if (!this.allowFlawedContext) {
                throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
            } else if (!isDiagnosticsEnabled()) {
                return classLoader;
            } else {
                logDiagnostic("Warning: the context classloader is an ancestor of the classloader that loaded LogFactoryImpl; it should be the same or a descendant. The application using commons-logging should ensure the context classloader is used correctly.");
                return classLoader;
            }
        }
    }

    private boolean getBooleanConfiguration(String str, boolean z) {
        String configurationValue = getConfigurationValue(str);
        return configurationValue == null ? z : Boolean.valueOf(configurationValue).booleanValue();
    }

    protected static ClassLoader getClassLoader(Class cls) {
        return LogFactory.getClassLoader(cls);
    }

    private String getConfigurationValue(String str) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[ENV] Trying to get configuration for item ").append(str).toString());
        }
        Object attribute = getAttribute(str);
        if (attribute != null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[ENV] Found LogFactory attribute [").append(attribute).append("] for ").append(str).toString());
            }
            return attribute.toString();
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[ENV] No LogFactory attribute found for ").append(str).toString());
        }
        try {
            String systemProperty = getSystemProperty(str, null);
            if (systemProperty == null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[ENV] No system property found for property ").append(str).toString());
                }
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[ENV] No configuration defined for item ").append(str).toString());
                }
                return null;
            } else if (!isDiagnosticsEnabled()) {
                return systemProperty;
            } else {
                logDiagnostic(new StringBuffer().append("[ENV] Found system property [").append(systemProperty).append("] for ").append(str).toString());
                return systemProperty;
            }
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[ENV] Security prevented reading system property ").append(str).toString());
            }
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return LogFactory.getContextClassLoader();
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader) AccessController.doPrivileged(new C12881());
    }

    private ClassLoader getLowestClassLoader(ClassLoader classLoader, ClassLoader classLoader2) {
        if (classLoader != null) {
            if (classLoader2 == null) {
                return classLoader;
            }
            ClassLoader classLoader3;
            for (classLoader3 = classLoader; classLoader3 != null; classLoader3 = classLoader3.getParent()) {
                if (classLoader3 == classLoader2) {
                    return classLoader;
                }
            }
            classLoader3 = classLoader2;
            while (classLoader3 != null) {
                if (classLoader3 != classLoader) {
                    classLoader3 = classLoader3.getParent();
                }
            }
            return null;
        }
        return classLoader2;
    }

    private ClassLoader getParentClassLoader(ClassLoader classLoader) {
        try {
            return (ClassLoader) AccessController.doPrivileged(new C12903(this, classLoader));
        } catch (SecurityException e) {
            logDiagnostic("[SECURITY] Unable to obtain parent classloader");
            return null;
        }
    }

    private static String getSystemProperty(String str, String str2) throws SecurityException {
        return (String) AccessController.doPrivileged(new C12892(str, str2));
    }

    private void handleFlawedDiscovery(String str, ClassLoader classLoader, Throwable th) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Could not instantiate Log '").append(str).append("' -- ").append(th.getClass().getName()).append(": ").append(th.getLocalizedMessage()).toString());
            if (th instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException) th).getTargetException();
                if (targetException != null) {
                    logDiagnostic(new StringBuffer().append("... InvocationTargetException: ").append(targetException.getClass().getName()).append(": ").append(targetException.getLocalizedMessage()).toString());
                    if (targetException instanceof ExceptionInInitializerError) {
                        targetException = ((ExceptionInInitializerError) targetException).getException();
                        if (targetException != null) {
                            logDiagnostic(new StringBuffer().append("... ExceptionInInitializerError: ").append(targetException.getClass().getName()).append(": ").append(targetException.getLocalizedMessage()).toString());
                        }
                    }
                }
            }
        }
        if (!this.allowFlawedDiscovery) {
            throw new LogConfigurationException(th);
        }
    }

    private void handleFlawedHierarchy(ClassLoader classLoader, Class cls) throws LogConfigurationException {
        Class class$;
        Object obj = null;
        if (class$org$apache$commons$logging$Log == null) {
            class$ = class$(LOG_PROPERTY);
            class$org$apache$commons$logging$Log = class$;
        } else {
            class$ = class$org$apache$commons$logging$Log;
        }
        String name = class$.getName();
        Class[] interfaces = cls.getInterfaces();
        for (Class name2 : interfaces) {
            if (name.equals(name2.getName())) {
                obj = 1;
                break;
            }
        }
        if (obj != null) {
            if (isDiagnosticsEnabled()) {
                try {
                    if (class$org$apache$commons$logging$Log == null) {
                        class$ = class$(LOG_PROPERTY);
                        class$org$apache$commons$logging$Log = class$;
                    } else {
                        class$ = class$org$apache$commons$logging$Log;
                    }
                    logDiagnostic(new StringBuffer().append("Class '").append(cls.getName()).append("' was found in classloader ").append(LogFactory.objectId(classLoader)).append(". It is bound to a Log interface which is not").append(" the one loaded from classloader ").append(LogFactory.objectId(getClassLoader(class$))).toString());
                } catch (Throwable th) {
                    logDiagnostic(new StringBuffer().append("Error while trying to output diagnostics about bad class '").append(cls).append("'").toString());
                }
            }
            StringBuffer stringBuffer;
            if (!this.allowFlawedHierarchy) {
                stringBuffer = new StringBuffer();
                stringBuffer.append("Terminating logging for this context ");
                stringBuffer.append("due to bad log hierarchy. ");
                stringBuffer.append("You have more than one version of '");
                if (class$org$apache$commons$logging$Log == null) {
                    class$ = class$(LOG_PROPERTY);
                    class$org$apache$commons$logging$Log = class$;
                } else {
                    class$ = class$org$apache$commons$logging$Log;
                }
                stringBuffer.append(class$.getName());
                stringBuffer.append("' visible.");
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(stringBuffer.toString());
                }
                throw new LogConfigurationException(stringBuffer.toString());
            } else if (isDiagnosticsEnabled()) {
                stringBuffer = new StringBuffer();
                stringBuffer.append("Warning: bad log hierarchy. ");
                stringBuffer.append("You have more than one version of '");
                if (class$org$apache$commons$logging$Log == null) {
                    class$ = class$(LOG_PROPERTY);
                    class$org$apache$commons$logging$Log = class$;
                } else {
                    class$ = class$org$apache$commons$logging$Log;
                }
                stringBuffer.append(class$.getName());
                stringBuffer.append("' visible.");
                logDiagnostic(stringBuffer.toString());
            }
        } else if (!this.allowFlawedDiscovery) {
            r0 = new StringBuffer();
            r0.append("Terminating logging for this context. ");
            r0.append("Log class '");
            r0.append(cls.getName());
            r0.append("' does not implement the Log interface.");
            if (isDiagnosticsEnabled()) {
                logDiagnostic(r0.toString());
            }
            throw new LogConfigurationException(r0.toString());
        } else if (isDiagnosticsEnabled()) {
            r0 = new StringBuffer();
            r0.append("[WARNING] Log class '");
            r0.append(cls.getName());
            r0.append("' does not implement the Log interface.");
            logDiagnostic(r0.toString());
        }
    }

    private void informUponSimilarName(StringBuffer stringBuffer, String str, String str2) {
        if (!str.equals(str2)) {
            if (str.regionMatches(true, 0, str2, 0, PKG_LEN + 5)) {
                stringBuffer.append(" Did you mean '");
                stringBuffer.append(str2);
                stringBuffer.append("'?");
            }
        }
    }

    private void initConfiguration() {
        this.allowFlawedContext = getBooleanConfiguration(ALLOW_FLAWED_CONTEXT_PROPERTY, true);
        this.allowFlawedDiscovery = getBooleanConfiguration(ALLOW_FLAWED_DISCOVERY_PROPERTY, true);
        this.allowFlawedHierarchy = getBooleanConfiguration(ALLOW_FLAWED_HIERARCHY_PROPERTY, true);
    }

    private void initDiagnostics() {
        String str;
        ClassLoader classLoader = getClassLoader(getClass());
        if (classLoader == null) {
            str = "BOOTLOADER";
        } else {
            try {
                str = LogFactory.objectId(classLoader);
            } catch (SecurityException e) {
                str = "UNKNOWN";
            }
        }
        this.diagnosticPrefix = new StringBuffer().append("[LogFactoryImpl@").append(System.identityHashCode(this)).append(" from ").append(str).append("] ").toString();
    }

    protected static boolean isDiagnosticsEnabled() {
        return LogFactory.isDiagnosticsEnabled();
    }

    private boolean isLogLibraryAvailable(String str, String str2) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Checking for '").append(str).append("'.").toString());
        }
        try {
            if (createLogFromClass(str2, getClass().getName(), false) != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("Found '").append(str).append("'.").toString());
                }
                return true;
            } else if (!isDiagnosticsEnabled()) {
                return false;
            } else {
                logDiagnostic(new StringBuffer().append("Did not find '").append(str).append("'.").toString());
                return false;
            }
        } catch (LogConfigurationException e) {
            if (!isDiagnosticsEnabled()) {
                return false;
            }
            logDiagnostic(new StringBuffer().append("Logging system '").append(str).append("' is available but not useable.").toString());
            return false;
        }
    }

    public Object getAttribute(String str) {
        return this.attributes.get(str);
    }

    public String[] getAttributeNames() {
        Vector vector = new Vector();
        Enumeration keys = this.attributes.keys();
        while (keys.hasMoreElements()) {
            vector.addElement((String) keys.nextElement());
        }
        String[] strArr = new String[vector.size()];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = (String) vector.elementAt(i);
        }
        return strArr;
    }

    public Log getInstance(Class cls) throws LogConfigurationException {
        return getInstance(cls.getName());
    }

    public Log getInstance(String str) throws LogConfigurationException {
        Log log = (Log) this.instances.get(str);
        if (log != null) {
            return log;
        }
        log = newInstance(str);
        this.instances.put(str, log);
        return log;
    }

    protected String getLogClassName() {
        if (this.logClassName == null) {
            discoverLogImplementation(getClass().getName());
        }
        return this.logClassName;
    }

    protected Constructor getLogConstructor() throws LogConfigurationException {
        if (this.logConstructor == null) {
            discoverLogImplementation(getClass().getName());
        }
        return this.logConstructor;
    }

    protected boolean isJdk13LumberjackAvailable() {
        return isLogLibraryAvailable("Jdk13Lumberjack", LOGGING_IMPL_LUMBERJACK_LOGGER);
    }

    protected boolean isJdk14Available() {
        return isLogLibraryAvailable("Jdk14", LOGGING_IMPL_JDK14_LOGGER);
    }

    protected boolean isLog4JAvailable() {
        return isLogLibraryAvailable("Log4J", LOGGING_IMPL_LOG4J_LOGGER);
    }

    protected void logDiagnostic(String str) {
        if (isDiagnosticsEnabled()) {
            LogFactory.logRawDiagnostic(new StringBuffer().append(this.diagnosticPrefix).append(str).toString());
        }
    }

    protected Log newInstance(String str) throws LogConfigurationException {
        try {
            Log discoverLogImplementation;
            if (this.logConstructor == null) {
                discoverLogImplementation = discoverLogImplementation(str);
            } else {
                discoverLogImplementation = (Log) this.logConstructor.newInstance(new Object[]{str});
            }
            if (this.logMethod != null) {
                this.logMethod.invoke(discoverLogImplementation, new Object[]{this});
            }
            return discoverLogImplementation;
        } catch (LogConfigurationException e) {
            throw e;
        } catch (Throwable e2) {
            Throwable targetException = e2.getTargetException();
            if (targetException != null) {
                throw new LogConfigurationException(targetException);
            }
            throw new LogConfigurationException(e2);
        } catch (Throwable e22) {
            LogConfigurationException logConfigurationException = new LogConfigurationException(e22);
        }
    }

    public void release() {
        logDiagnostic("Releasing all known loggers");
        this.instances.clear();
    }

    public void removeAttribute(String str) {
        this.attributes.remove(str);
    }

    public void setAttribute(String str, Object obj) {
        if (this.logConstructor != null) {
            logDiagnostic("setAttribute: call too late; configuration already performed.");
        }
        if (obj == null) {
            this.attributes.remove(str);
        } else {
            this.attributes.put(str, obj);
        }
        if (str.equals(LogFactory.TCCL_KEY)) {
            this.useTCCL = Boolean.valueOf(obj.toString()).booleanValue();
        }
    }
}
