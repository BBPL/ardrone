package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;

public class ServletContextCleaner implements ServletContextListener {
    static Class class$java$lang$ClassLoader;
    private Class[] RELEASE_SIGNATURE;

    public ServletContextCleaner() {
        Class class$;
        if (class$java$lang$ClassLoader == null) {
            class$ = class$("java.lang.ClassLoader");
            class$java$lang$ClassLoader = class$;
        } else {
            class$ = class$java$lang$ClassLoader;
        }
        this.RELEASE_SIGNATURE = new Class[]{class$};
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = contextClassLoader;
        while (classLoader != null) {
            try {
                Class loadClass = classLoader.loadClass(LogFactory.FACTORY_PROPERTY);
                loadClass.getMethod("release", this.RELEASE_SIGNATURE).invoke(null, new Object[]{contextClassLoader});
                classLoader = loadClass.getClassLoader().getParent();
            } catch (ClassNotFoundException e) {
                classLoader = null;
            } catch (NoSuchMethodException e2) {
                System.err.println("LogFactory instance found which does not support release method!");
                classLoader = null;
            } catch (IllegalAccessException e3) {
                System.err.println("LogFactory instance found which is not accessable!");
                classLoader = null;
            } catch (InvocationTargetException e4) {
                System.err.println("LogFactory instance release method failed!");
                classLoader = null;
            }
        }
        LogFactory.release(contextClassLoader);
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }
}
