package org.mortbay.util;

import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Loader {
    public static URL getResource(Class cls, String str, boolean z) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        while (url == null && contextClassLoader != null) {
            URL resource = contextClassLoader.getResource(str);
            ClassLoader parent = (resource == null && z) ? contextClassLoader.getParent() : null;
            contextClassLoader = parent;
            url = resource;
        }
        ClassLoader classLoader = cls == null ? null : cls.getClassLoader();
        while (url == null && classLoader != null) {
            URL resource2 = classLoader.getResource(str);
            parent = (resource2 == null && z) ? classLoader.getParent() : null;
            classLoader = parent;
            url = resource2;
        }
        return url == null ? ClassLoader.getSystemResource(str) : url;
    }

    public static ResourceBundle getResourceBundle(Class cls, String str, boolean z, Locale locale) throws MissingResourceException {
        MissingResourceException e;
        ResourceBundle resourceBundle;
        ResourceBundle bundle;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ResourceBundle resourceBundle2 = null;
        MissingResourceException missingResourceException = null;
        while (resourceBundle2 == null && contextClassLoader != null) {
            try {
                resourceBundle2 = ResourceBundle.getBundle(str, locale, contextClassLoader);
            } catch (MissingResourceException e2) {
                if (missingResourceException == null) {
                    missingResourceException = e2;
                }
            }
            ClassLoader parent = (resourceBundle2 == null && z) ? contextClassLoader.getParent() : null;
            contextClassLoader = parent;
        }
        if (cls == null) {
            contextClassLoader = null;
            e2 = missingResourceException;
            resourceBundle = resourceBundle2;
        } else {
            contextClassLoader = cls.getClassLoader();
            e2 = missingResourceException;
            resourceBundle = resourceBundle2;
        }
        while (resourceBundle == null && contextClassLoader != null) {
            try {
                resourceBundle2 = ResourceBundle.getBundle(str, locale, contextClassLoader);
                missingResourceException = e2;
            } catch (MissingResourceException e3) {
                if (e2 == null) {
                    MissingResourceException missingResourceException2 = e3;
                    resourceBundle2 = resourceBundle;
                    missingResourceException = missingResourceException2;
                } else {
                    resourceBundle2 = resourceBundle;
                    missingResourceException = e2;
                }
            }
            parent = (resourceBundle2 == null && z) ? contextClassLoader.getParent() : null;
            contextClassLoader = parent;
            e2 = missingResourceException;
            resourceBundle = resourceBundle2;
        }
        if (resourceBundle == null) {
            try {
                bundle = ResourceBundle.getBundle(str, locale);
            } catch (MissingResourceException e4) {
                if (e2 == null) {
                    e2 = e4;
                    bundle = resourceBundle;
                }
            }
            if (bundle != null) {
                return bundle;
            }
            throw e2;
        }
        bundle = resourceBundle;
        if (bundle != null) {
            return bundle;
        }
        throw e2;
    }

    public static Class loadClass(Class cls, String str) throws ClassNotFoundException {
        return loadClass(cls, str, false);
    }

    public static Class loadClass(Class cls, String str, boolean z) throws ClassNotFoundException {
        Class cls2;
        Class cls3;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Class cls4 = null;
        ClassNotFoundException classNotFoundException = null;
        while (cls4 == null && contextClassLoader != null) {
            try {
                cls4 = contextClassLoader.loadClass(str);
            } catch (ClassNotFoundException e) {
                if (classNotFoundException == null) {
                    ClassNotFoundException e2;
                    classNotFoundException = e2;
                }
            }
            ClassLoader parent = (cls4 == null && z) ? contextClassLoader.getParent() : null;
            contextClassLoader = parent;
        }
        if (cls == null) {
            contextClassLoader = null;
            e2 = classNotFoundException;
            cls2 = cls4;
        } else {
            contextClassLoader = cls.getClassLoader();
            e2 = classNotFoundException;
            cls2 = cls4;
        }
        while (cls2 == null && contextClassLoader != null) {
            try {
                cls4 = contextClassLoader.loadClass(str);
                classNotFoundException = e2;
            } catch (ClassNotFoundException e3) {
                if (e2 == null) {
                    ClassNotFoundException classNotFoundException2 = e3;
                    cls4 = cls2;
                    classNotFoundException = classNotFoundException2;
                } else {
                    cls4 = cls2;
                    classNotFoundException = e2;
                }
            }
            parent = (cls4 == null && z) ? contextClassLoader.getParent() : null;
            contextClassLoader = parent;
            e2 = classNotFoundException;
            cls2 = cls4;
        }
        if (cls2 == null) {
            try {
                cls3 = Class.forName(str);
            } catch (ClassNotFoundException e4) {
                if (e2 == null) {
                    e2 = e4;
                    cls3 = cls2;
                }
            }
            if (cls3 != null) {
                return cls3;
            }
            throw e2;
        }
        cls3 = cls2;
        if (cls3 != null) {
            return cls3;
        }
        throw e2;
    }
}
