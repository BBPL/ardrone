package org.apache.commons.logging.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.mortbay.util.URIUtil;

public class SimpleLog implements Log, Serializable {
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_FATAL = 6;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_OFF = 7;
    public static final int LOG_LEVEL_TRACE = 1;
    public static final int LOG_LEVEL_WARN = 4;
    static Class class$java$lang$Thread = null;
    static Class class$org$apache$commons$logging$impl$SimpleLog = null;
    protected static DateFormat dateFormatter = null;
    protected static String dateTimeFormat = null;
    protected static boolean showDateTime = false;
    protected static boolean showLogName = false;
    protected static boolean showShortName = false;
    protected static final Properties simpleLogProps = new Properties();
    protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
    protected int currentLogLevel;
    protected String logName = null;
    private String shortLogName = null;

    class C12911 implements PrivilegedAction {
        private final String val$name;

        C12911(String str) {
            this.val$name = str;
        }

        public Object run() {
            ClassLoader access$000 = SimpleLog.access$000();
            return access$000 != null ? access$000.getResourceAsStream(this.val$name) : ClassLoader.getSystemResourceAsStream(this.val$name);
        }
    }

    static {
        showLogName = false;
        showShortName = true;
        showDateTime = false;
        dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
        dateFormatter = null;
        InputStream resourceAsStream = getResourceAsStream("simplelog.properties");
        if (resourceAsStream != null) {
            try {
                simpleLogProps.load(resourceAsStream);
                resourceAsStream.close();
            } catch (IOException e) {
            }
        }
        showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
        showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
        showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime);
        if (showDateTime) {
            dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);
            try {
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            } catch (IllegalArgumentException e2) {
                dateTimeFormat = DEFAULT_DATE_TIME_FORMAT;
                dateFormatter = new SimpleDateFormat(dateTimeFormat);
            }
        }
    }

    public SimpleLog(String str) {
        this.logName = str;
        setLevel(3);
        String stringProperty = getStringProperty(new StringBuffer().append("org.apache.commons.logging.simplelog.log.").append(this.logName).toString());
        String str2 = stringProperty;
        int lastIndexOf = String.valueOf(str).lastIndexOf(".");
        while (str2 == null && lastIndexOf > -1) {
            str = str.substring(0, lastIndexOf);
            stringProperty = getStringProperty(new StringBuffer().append("org.apache.commons.logging.simplelog.log.").append(str).toString());
            str2 = stringProperty;
            lastIndexOf = String.valueOf(str).lastIndexOf(".");
        }
        if (str2 == null) {
            str2 = getStringProperty("org.apache.commons.logging.simplelog.defaultlog");
        }
        if ("all".equalsIgnoreCase(str2)) {
            setLevel(0);
        } else if ("trace".equalsIgnoreCase(str2)) {
            setLevel(1);
        } else if ("debug".equalsIgnoreCase(str2)) {
            setLevel(2);
        } else if ("info".equalsIgnoreCase(str2)) {
            setLevel(3);
        } else if ("warn".equalsIgnoreCase(str2)) {
            setLevel(4);
        } else if ("error".equalsIgnoreCase(str2)) {
            setLevel(5);
        } else if ("fatal".equalsIgnoreCase(str2)) {
            setLevel(6);
        } else if ("off".equalsIgnoreCase(str2)) {
            setLevel(7);
        }
    }

    static ClassLoader access$000() {
        return getContextClassLoader();
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static boolean getBooleanProperty(String str, boolean z) {
        String stringProperty = getStringProperty(str);
        return stringProperty == null ? z : "true".equalsIgnoreCase(stringProperty);
    }

    private static ClassLoader getContextClassLoader() {
        ClassLoader classLoader;
        try {
            Class cls;
            if (class$java$lang$Thread == null) {
                Class class$ = class$("java.lang.Thread");
                class$java$lang$Thread = class$;
                cls = class$;
            } else {
                cls = class$java$lang$Thread;
            }
            classLoader = (ClassLoader) cls.getMethod("getContextClassLoader", (Class[]) null).invoke(Thread.currentThread(), (Class[]) null);
        } catch (IllegalAccessException e) {
            classLoader = null;
        } catch (InvocationTargetException e2) {
            if (e2.getTargetException() instanceof SecurityException) {
                classLoader = null;
            } else {
                throw new LogConfigurationException("Unexpected InvocationTargetException", e2.getTargetException());
            }
        } catch (NoSuchMethodException e3) {
            classLoader = null;
        }
        if (classLoader != null) {
            return classLoader;
        }
        if (class$org$apache$commons$logging$impl$SimpleLog == null) {
            class$ = class$("org.apache.commons.logging.impl.SimpleLog");
            class$org$apache$commons$logging$impl$SimpleLog = class$;
        } else {
            class$ = class$org$apache$commons$logging$impl$SimpleLog;
        }
        return class$.getClassLoader();
    }

    private static InputStream getResourceAsStream(String str) {
        return (InputStream) AccessController.doPrivileged(new C12911(str));
    }

    private static String getStringProperty(String str) {
        String str2 = null;
        try {
            str2 = System.getProperty(str);
        } catch (SecurityException e) {
        }
        return str2 == null ? simpleLogProps.getProperty(str) : str2;
    }

    private static String getStringProperty(String str, String str2) {
        String stringProperty = getStringProperty(str);
        return stringProperty == null ? str2 : stringProperty;
    }

    public final void debug(Object obj) {
        if (isLevelEnabled(2)) {
            log(2, obj, null);
        }
    }

    public final void debug(Object obj, Throwable th) {
        if (isLevelEnabled(2)) {
            log(2, obj, th);
        }
    }

    public final void error(Object obj) {
        if (isLevelEnabled(5)) {
            log(5, obj, null);
        }
    }

    public final void error(Object obj, Throwable th) {
        if (isLevelEnabled(5)) {
            log(5, obj, th);
        }
    }

    public final void fatal(Object obj) {
        if (isLevelEnabled(6)) {
            log(6, obj, null);
        }
    }

    public final void fatal(Object obj, Throwable th) {
        if (isLevelEnabled(6)) {
            log(6, obj, th);
        }
    }

    public int getLevel() {
        return this.currentLogLevel;
    }

    public final void info(Object obj) {
        if (isLevelEnabled(3)) {
            log(3, obj, null);
        }
    }

    public final void info(Object obj, Throwable th) {
        if (isLevelEnabled(3)) {
            log(3, obj, th);
        }
    }

    public final boolean isDebugEnabled() {
        return isLevelEnabled(2);
    }

    public final boolean isErrorEnabled() {
        return isLevelEnabled(5);
    }

    public final boolean isFatalEnabled() {
        return isLevelEnabled(6);
    }

    public final boolean isInfoEnabled() {
        return isLevelEnabled(3);
    }

    protected boolean isLevelEnabled(int i) {
        return i >= this.currentLogLevel;
    }

    public final boolean isTraceEnabled() {
        return isLevelEnabled(1);
    }

    public final boolean isWarnEnabled() {
        return isLevelEnabled(4);
    }

    protected void log(int i, Object obj, Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        if (showDateTime) {
            String format;
            Date date = new Date();
            synchronized (dateFormatter) {
                format = dateFormatter.format(date);
            }
            stringBuffer.append(format);
            stringBuffer.append(" ");
        }
        switch (i) {
            case 1:
                stringBuffer.append("[TRACE] ");
                break;
            case 2:
                stringBuffer.append("[DEBUG] ");
                break;
            case 3:
                stringBuffer.append("[INFO] ");
                break;
            case 4:
                stringBuffer.append("[WARN] ");
                break;
            case 5:
                stringBuffer.append("[ERROR] ");
                break;
            case 6:
                stringBuffer.append("[FATAL] ");
                break;
        }
        if (showShortName) {
            if (this.shortLogName == null) {
                this.shortLogName = this.logName.substring(this.logName.lastIndexOf(".") + 1);
                this.shortLogName = this.shortLogName.substring(this.shortLogName.lastIndexOf(URIUtil.SLASH) + 1);
            }
            stringBuffer.append(String.valueOf(this.shortLogName)).append(" - ");
        } else if (showLogName) {
            stringBuffer.append(String.valueOf(this.logName)).append(" - ");
        }
        stringBuffer.append(String.valueOf(obj));
        if (th != null) {
            stringBuffer.append(" <");
            stringBuffer.append(th.toString());
            stringBuffer.append(">");
            Writer stringWriter = new StringWriter(1024);
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            printWriter.close();
            stringBuffer.append(stringWriter.toString());
        }
        write(stringBuffer);
    }

    public void setLevel(int i) {
        this.currentLogLevel = i;
    }

    public final void trace(Object obj) {
        if (isLevelEnabled(1)) {
            log(1, obj, null);
        }
    }

    public final void trace(Object obj, Throwable th) {
        if (isLevelEnabled(1)) {
            log(1, obj, th);
        }
    }

    public final void warn(Object obj) {
        if (isLevelEnabled(4)) {
            log(4, obj, null);
        }
    }

    public final void warn(Object obj, Throwable th) {
        if (isLevelEnabled(4)) {
            log(4, obj, th);
        }
    }

    protected void write(StringBuffer stringBuffer) {
        System.err.println(stringBuffer.toString());
    }
}
