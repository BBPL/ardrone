package org.mortbay.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.mortbay.log.Log;
import org.mortbay.util.IO;
import org.mortbay.util.Loader;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;

public abstract class Resource implements Serializable {
    public static boolean __defaultUseCaches = true;
    static Class class$org$mortbay$resource$Resource;
    Object _associate;

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private static String deTag(String str) {
        return StringUtil.replace(StringUtil.replace(str, "<", "&lt;"), ">", "&gt;");
    }

    private static String defangURI(String str) {
        StringBuffer stringBuffer = null;
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            switch (str.charAt(i2)) {
                case '\"':
                case '\'':
                case CacheConfig.DEFAULT_ASYNCHRONOUS_WORKER_IDLE_LIFETIME_SECS /*60*/:
                case ExifTagConstants.PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF /*62*/:
                    stringBuffer = new StringBuffer(str.length() << 1);
                    break;
                default:
                    break;
            }
        }
        if (stringBuffer == null) {
            return str;
        }
        while (i < str.length()) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case '\"':
                    stringBuffer.append("%22");
                    break;
                case '\'':
                    stringBuffer.append("%27");
                    break;
                case CacheConfig.DEFAULT_ASYNCHRONOUS_WORKER_IDLE_LIFETIME_SECS /*60*/:
                    stringBuffer.append("%3C");
                    break;
                case ExifTagConstants.PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF /*62*/:
                    stringBuffer.append("%3E");
                    break;
                default:
                    stringBuffer.append(charAt);
                    break;
            }
            i++;
        }
        return stringBuffer.toString();
    }

    public static boolean getDefaultUseCaches() {
        return __defaultUseCaches;
    }

    public static Resource newClassPathResource(String str) {
        return newClassPathResource(str, true, false);
    }

    public static Resource newClassPathResource(String str, boolean z, boolean z2) {
        Class class$;
        if (class$org$mortbay$resource$Resource == null) {
            class$ = class$("org.mortbay.resource.Resource");
            class$org$mortbay$resource$Resource = class$;
        } else {
            class$ = class$org$mortbay$resource$Resource;
        }
        URL resource = class$.getResource(str);
        if (resource == null) {
            try {
                if (class$org$mortbay$resource$Resource == null) {
                    class$ = class$("org.mortbay.resource.Resource");
                    class$org$mortbay$resource$Resource = class$;
                } else {
                    class$ = class$org$mortbay$resource$Resource;
                }
                resource = Loader.getResource(class$, str, z2);
            } catch (ClassNotFoundException e) {
                resource = ClassLoader.getSystemResource(str);
            }
        }
        return resource == null ? null : newResource(resource, z);
    }

    public static Resource newResource(String str) throws MalformedURLException, IOException {
        return newResource(str, __defaultUseCaches);
    }

    public static Resource newResource(String str, boolean z) throws MalformedURLException, IOException {
        Object e;
        try {
            URL url = new URL(str);
            String url2 = url.toString();
            return (url2.length() <= 0 || url2.charAt(url2.length() - 1) == str.charAt(str.length() - 1) || ((url2.charAt(url2.length() - 1) == '/' && url2.charAt(url2.length() - 2) == str.charAt(str.length() - 1)) || (str.charAt(str.length() - 1) == '/' && str.charAt(str.length() - 2) == url2.charAt(url2.length() - 1)))) ? newResource(url) : new BadResource(url, new StringBuffer().append("Trailing special characters stripped by URL in ").append(str).toString());
        } catch (MalformedURLException e2) {
            MalformedURLException malformedURLException = e2;
            if (str.startsWith("ftp:") || str.startsWith("file:") || str.startsWith("jar:")) {
                Log.warn(new StringBuffer().append("Bad Resource: ").append(str).toString());
                throw malformedURLException;
            }
            try {
                if (str.startsWith("./")) {
                    str = str.substring(2);
                }
                File canonicalFile = new File(str).getCanonicalFile();
                URL url3 = new URL(URIUtil.encodePath(canonicalFile.toURL().toString()));
                try {
                    URLConnection openConnection = url3.openConnection();
                    openConnection.setUseCaches(z);
                    return new FileResource(url3, openConnection, canonicalFile);
                } catch (Exception e3) {
                    e = e3;
                    Log.debug(Log.EXCEPTION, e);
                    throw malformedURLException;
                }
            } catch (Exception e4) {
                e = e4;
                Log.debug(Log.EXCEPTION, e);
                throw malformedURLException;
            }
        }
    }

    public static Resource newResource(URL url) throws IOException {
        return newResource(url, __defaultUseCaches);
    }

    public static Resource newResource(URL url, boolean z) {
        if (url == null) {
            return null;
        }
        String toExternalForm = url.toExternalForm();
        if (!toExternalForm.startsWith("file:")) {
            return toExternalForm.startsWith("jar:file:") ? new JarFileResource(url, z) : toExternalForm.startsWith("jar:") ? new JarResource(url, z) : new URLResource(url, null, z);
        } else {
            try {
                return new FileResource(url);
            } catch (Exception e) {
                Exception exception = e;
                Log.debug(Log.EXCEPTION, exception);
                return new BadResource(url, exception.toString());
            }
        }
    }

    public static Resource newSystemResource(String str) throws IOException {
        URL resource;
        ClassLoader classLoader;
        URL resource2;
        ClassLoader classLoader2;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            resource = contextClassLoader.getResource(str);
            if (resource == null && str.startsWith(URIUtil.SLASH)) {
                resource = contextClassLoader.getResource(str.substring(1));
            }
        } else {
            resource = null;
        }
        if (resource == null) {
            Class class$;
            if (class$org$mortbay$resource$Resource == null) {
                class$ = class$("org.mortbay.resource.Resource");
                class$org$mortbay$resource$Resource = class$;
            } else {
                class$ = class$org$mortbay$resource$Resource;
            }
            contextClassLoader = class$.getClassLoader();
            if (contextClassLoader != null) {
                resource = contextClassLoader.getResource(str);
                if (resource == null && str.startsWith(URIUtil.SLASH)) {
                    classLoader = contextClassLoader;
                    resource2 = contextClassLoader.getResource(str.substring(1));
                    classLoader2 = classLoader;
                    if (resource2 == null) {
                        resource2 = ClassLoader.getSystemResource(str);
                        if (resource2 == null && str.startsWith(URIUtil.SLASH)) {
                            resource2 = classLoader2.getResource(str.substring(1));
                        }
                    }
                    return resource2 != null ? null : newResource(resource2);
                }
            }
        }
        classLoader = contextClassLoader;
        resource2 = resource;
        classLoader2 = classLoader;
        if (resource2 == null) {
            resource2 = ClassLoader.getSystemResource(str);
            resource2 = classLoader2.getResource(str.substring(1));
        }
        if (resource2 != null) {
        }
    }

    public static void setDefaultUseCaches(boolean z) {
        __defaultUseCaches = z;
    }

    public abstract Resource addPath(String str) throws IOException, MalformedURLException;

    public abstract boolean delete() throws SecurityException;

    public String encode(String str) {
        return URIUtil.encodePath(str);
    }

    public abstract boolean exists();

    protected void finalize() {
        release();
    }

    public URL getAlias() {
        return null;
    }

    public Object getAssociate() {
        return this._associate;
    }

    public abstract File getFile() throws IOException;

    public abstract InputStream getInputStream() throws IOException;

    public String getListHTML(String str, boolean z) throws IOException {
        String canonicalPath = URIUtil.canonicalPath(str);
        if (canonicalPath != null && isDirectory()) {
            String[] list = list();
            if (list != null) {
                Arrays.sort(list);
                String stringBuffer = new StringBuffer().append("Directory: ").append(deTag(URIUtil.decodePath(canonicalPath))).toString();
                StringBuffer stringBuffer2 = new StringBuffer(4096);
                stringBuffer2.append("<HTML><HEAD><TITLE>");
                stringBuffer2.append(stringBuffer);
                stringBuffer2.append("</TITLE></HEAD><BODY>\n<H1>");
                stringBuffer2.append(stringBuffer);
                stringBuffer2.append("</H1>\n<TABLE BORDER=0>\n");
                if (z) {
                    stringBuffer2.append("<TR><TD><A HREF=\"");
                    stringBuffer2.append(URIUtil.addPaths(canonicalPath, "../"));
                    stringBuffer2.append("\">Parent Directory</A></TD><TD></TD><TD></TD></TR>\n");
                }
                stringBuffer = defangURI(canonicalPath);
                DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(2, 2);
                for (int i = 0; i < list.length; i++) {
                    Resource addPath = addPath(list[i]);
                    stringBuffer2.append("\n<TR><TD><A HREF=\"");
                    String addPaths = URIUtil.addPaths(stringBuffer, URIUtil.encodePath(list[i]));
                    stringBuffer2.append(addPaths);
                    if (addPath.isDirectory() && !addPaths.endsWith(URIUtil.SLASH)) {
                        stringBuffer2.append(URIUtil.SLASH);
                    }
                    stringBuffer2.append("\">");
                    stringBuffer2.append(deTag(list[i]));
                    stringBuffer2.append("&nbsp;");
                    stringBuffer2.append("</TD><TD ALIGN=right>");
                    stringBuffer2.append(addPath.length());
                    stringBuffer2.append(" bytes&nbsp;</TD><TD>");
                    stringBuffer2.append(dateTimeInstance.format(new Date(addPath.lastModified())));
                    stringBuffer2.append("</TD></TR>");
                }
                stringBuffer2.append("</TABLE>\n");
                stringBuffer2.append("</BODY></HTML>\n");
                return stringBuffer2.toString();
            }
        }
        return null;
    }

    public abstract String getName();

    public abstract OutputStream getOutputStream() throws IOException, SecurityException;

    public abstract URL getURL();

    public abstract boolean isDirectory();

    public abstract long lastModified();

    public abstract long length();

    public abstract String[] list();

    public abstract void release();

    public abstract boolean renameTo(Resource resource) throws SecurityException;

    public void setAssociate(Object obj) {
        this._associate = obj;
    }

    public void writeTo(OutputStream outputStream, long j, long j2) throws IOException {
        InputStream inputStream = getInputStream();
        try {
            inputStream.skip(j);
            if (j2 < 0) {
                IO.copy(inputStream, outputStream);
            } else {
                IO.copy(inputStream, outputStream, j2);
            }
            inputStream.close();
        } catch (Throwable th) {
            inputStream.close();
        }
    }
}
