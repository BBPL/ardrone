package org.mortbay.jetty.webapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.util.HashSet;
import java.util.StringTokenizer;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;
import org.mortbay.util.IO;
import org.mortbay.util.LazyList;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;

public class WebAppClassLoader extends URLClassLoader {
    static Class class$org$mortbay$jetty$webapp$WebAppClassLoader;
    private WebAppContext _context;
    private HashSet _extensions;
    private String _name;
    private ClassLoader _parent;

    public WebAppClassLoader(ClassLoader classLoader, WebAppContext webAppContext) throws IOException {
        Class class$;
        if (classLoader == null) {
            if (Thread.currentThread().getContextClassLoader() != null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            } else {
                if (class$org$mortbay$jetty$webapp$WebAppClassLoader == null) {
                    class$ = class$("org.mortbay.jetty.webapp.WebAppClassLoader");
                    class$org$mortbay$jetty$webapp$WebAppClassLoader = class$;
                } else {
                    class$ = class$org$mortbay$jetty$webapp$WebAppClassLoader;
                }
                if (class$.getClassLoader() != null) {
                    if (class$org$mortbay$jetty$webapp$WebAppClassLoader == null) {
                        class$ = class$("org.mortbay.jetty.webapp.WebAppClassLoader");
                        class$org$mortbay$jetty$webapp$WebAppClassLoader = class$;
                    } else {
                        class$ = class$org$mortbay$jetty$webapp$WebAppClassLoader;
                    }
                    classLoader = class$.getClassLoader();
                } else {
                    classLoader = ClassLoader.getSystemClassLoader();
                }
            }
        }
        super(new URL[0], classLoader);
        this._parent = getParent();
        this._context = webAppContext;
        if (this._parent == null) {
            throw new IllegalArgumentException("no parent classloader!");
        }
        this._extensions = new HashSet();
        this._extensions.add(".jar");
        this._extensions.add(".zip");
        StringBuffer stringBuffer = new StringBuffer();
        if (class$org$mortbay$jetty$webapp$WebAppClassLoader == null) {
            class$ = class$("org.mortbay.jetty.webapp.WebAppClassLoader");
            class$org$mortbay$jetty$webapp$WebAppClassLoader = class$;
        } else {
            class$ = class$org$mortbay$jetty$webapp$WebAppClassLoader;
        }
        String property = System.getProperty(stringBuffer.append(class$.getName()).append(".extensions").toString());
        if (property != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(property, ",;");
            while (stringTokenizer.hasMoreTokens()) {
                this._extensions.add(stringTokenizer.nextToken().trim());
            }
        }
        if (webAppContext.getExtraClasspath() != null) {
            addClassPath(webAppContext.getExtraClasspath());
        }
    }

    public WebAppClassLoader(WebAppContext webAppContext) throws IOException {
        this(null, webAppContext);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private boolean isFileSupported(String str) {
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf != -1 && this._extensions.contains(str.substring(lastIndexOf));
    }

    public void addClassPath(String str) throws IOException {
        Throwable th;
        OutputStream fileOutputStream;
        if (str != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, ",;");
            while (stringTokenizer.hasMoreTokens()) {
                Resource newResource = Resource.newResource(stringTokenizer.nextToken());
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("Path resource=").append(newResource).toString());
                }
                File file = newResource.getFile();
                if (file != null) {
                    addURL(newResource.getURL());
                } else if (newResource.isDirectory() || file != null) {
                    addURL(newResource.getURL());
                } else {
                    InputStream inputStream = newResource.getInputStream();
                    file = this._context.getTempDirectory();
                    if (file == null) {
                        file = File.createTempFile("jetty.cl.lib", null);
                        file.mkdir();
                        file.deleteOnExit();
                    }
                    File file2 = new File(file, "lib");
                    if (!file2.exists()) {
                        file2.mkdir();
                        file2.deleteOnExit();
                    }
                    file = File.createTempFile("Jetty-", ".jar", file2);
                    file.deleteOnExit();
                    if (Log.isDebugEnabled()) {
                        Log.debug(new StringBuffer().append("Extract ").append(newResource).append(" to ").append(file).toString());
                    }
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            IO.copy(inputStream, fileOutputStream);
                            IO.close(fileOutputStream);
                            addURL(file.toURL());
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream = null;
                    }
                }
            }
            return;
        }
        return;
        IO.close(fileOutputStream);
        throw th;
    }

    public void addJars(Resource resource) {
        if (resource.exists() && resource.isDirectory()) {
            String[] list = resource.list();
            int i = 0;
            while (list != null && i < list.length) {
                try {
                    Resource addPath = resource.addPath(list[i]);
                    if (isFileSupported(addPath.getName().toLowerCase())) {
                        addClassPath(StringUtil.replace(StringUtil.replace(addPath.toString(), ",", "%2C"), ";", "%3B"));
                    }
                } catch (Throwable e) {
                    Log.warn(Log.EXCEPTION, e);
                }
                i++;
            }
        }
    }

    public void destroy() {
        this._parent = null;
    }

    public ContextHandler getContext() {
        return this._context;
    }

    public String getName() {
        return this._name;
    }

    public PermissionCollection getPermissions(CodeSource codeSource) {
        PermissionCollection permissions = this._context.getPermissions();
        return permissions == null ? super.getPermissions(codeSource) : permissions;
    }

    public URL getResource(String str) {
        URL url = null;
        int i = 0;
        if (this._context.isParentLoaderPriority() || isSystemPath(str)) {
            if (this._parent != null) {
                url = this._parent.getResource(str);
                i = 1;
            } else {
                i = 1;
            }
        }
        if (url == null) {
            url = findResource(str);
            if (url == null && str.startsWith(URIUtil.SLASH)) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("HACK leading / off ").append(str).toString());
                }
                url = findResource(str.substring(1));
            }
        }
        if (url == null && r1 == 0 && this._parent != null) {
            url = this._parent.getResource(str);
        }
        if (url != null && Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("getResource(").append(str).append(")=").append(url).toString());
        }
        return url;
    }

    public boolean isServerPath(String str) {
        String replace = str.replace('/', '.');
        while (replace.startsWith(".")) {
            replace = replace.substring(1);
        }
        String[] serverClasses = this._context.getServerClasses();
        if (serverClasses != null) {
            for (String str2 : serverClasses) {
                String str22;
                boolean z;
                if (str22.startsWith("-")) {
                    str22 = str22.substring(1);
                    z = false;
                } else {
                    z = true;
                }
                if (str22.endsWith(".")) {
                    if (replace.startsWith(str22)) {
                        return z;
                    }
                } else if (replace.equals(str22)) {
                    return z;
                }
            }
        }
        return false;
    }

    public boolean isSystemPath(String str) {
        String replace = str.replace('/', '.');
        while (replace.startsWith(".")) {
            replace = replace.substring(1);
        }
        String[] systemClasses = this._context.getSystemClasses();
        if (systemClasses != null) {
            for (String str2 : systemClasses) {
                String str22;
                boolean z;
                if (str22.startsWith("-")) {
                    str22 = str22.substring(1);
                    z = false;
                } else {
                    z = true;
                }
                if (str22.endsWith(".")) {
                    if (replace.startsWith(str22)) {
                        return z;
                    }
                } else if (replace.equals(str22)) {
                    return z;
                }
            }
        }
        return false;
    }

    public Class loadClass(String str) throws ClassNotFoundException {
        return loadClass(str, false);
    }

    protected Class loadClass(String str, boolean z) throws ClassNotFoundException {
        ClassNotFoundException classNotFoundException;
        Class findClass;
        synchronized (this) {
            ClassNotFoundException classNotFoundException2;
            Class findLoadedClass = findLoadedClass(str);
            ClassNotFoundException classNotFoundException3 = null;
            Object obj = null;
            if (findLoadedClass == null && this._parent != null && (this._context.isParentLoaderPriority() || isSystemPath(str))) {
                obj = 1;
                try {
                    findLoadedClass = this._parent.loadClass(str);
                    if (Log.isDebugEnabled()) {
                        Log.debug(new StringBuffer().append("loaded ").append(findLoadedClass).toString());
                    }
                } catch (ClassNotFoundException e) {
                    classNotFoundException3 = e;
                }
            }
            if (findLoadedClass == null) {
                try {
                    classNotFoundException = classNotFoundException3;
                    findClass = findClass(str);
                    classNotFoundException2 = classNotFoundException;
                } catch (ClassNotFoundException classNotFoundException32) {
                    classNotFoundException = classNotFoundException32;
                    findClass = findLoadedClass;
                    classNotFoundException2 = classNotFoundException;
                }
            } else {
                classNotFoundException = classNotFoundException32;
                findClass = findLoadedClass;
                classNotFoundException2 = classNotFoundException;
            }
            if (findClass == null) {
                if (!(this._parent == null || r2 != null || isServerPath(str))) {
                    findClass = this._parent.loadClass(str);
                }
            }
            if (findClass == null) {
                throw classNotFoundException2;
            }
            if (z) {
                resolveClass(findClass);
            }
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("loaded ").append(findClass).append(" from ").append(findClass.getClassLoader()).toString());
            }
        }
        return findClass;
    }

    public void setName(String str) {
        this._name = str;
    }

    public String toString() {
        return Log.isDebugEnabled() ? new StringBuffer().append("ContextLoader@").append(this._name).append("(").append(LazyList.array2List(getURLs())).append(") / ").append(this._parent).toString() : new StringBuffer().append("ContextLoader@").append(this._name).toString();
    }
}
