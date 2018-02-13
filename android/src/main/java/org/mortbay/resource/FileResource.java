package org.mortbay.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import org.mortbay.log.Log;
import org.mortbay.util.URIUtil;

public class FileResource extends URLResource {
    private static boolean __checkAliases = "true".equalsIgnoreCase(System.getProperty("org.mortbay.util.FileResource.checkAliases", "true"));
    private transient URL _alias = null;
    private transient boolean _aliasChecked = false;
    private File _file;

    static {
        if (__checkAliases) {
            Log.debug("Checking Resource aliases");
        } else {
            Log.warn("Resource alias checking is disabled");
        }
    }

    public FileResource(URL url) throws IOException, URISyntaxException {
        super(url, null);
        try {
            this._file = new File(new URI(url.toString()));
        } catch (Throwable e) {
            Log.ignore(e);
            try {
                URI uri = new URI(new StringBuffer().append("file:").append(URIUtil.encodePath(url.toString().substring(5))).toString());
                if (uri.getAuthority() == null) {
                    this._file = new File(uri);
                } else {
                    this._file = new File(new StringBuffer().append("//").append(uri.getAuthority()).append(URIUtil.decodePath(url.getFile())).toString());
                }
            } catch (Throwable e2) {
                Log.ignore(e2);
                checkConnection();
                Permission permission = this._connection.getPermission();
                this._file = new File(permission == null ? url.getFile() : permission.getName());
            }
        }
        if (this._file.isDirectory()) {
            if (!this._urlString.endsWith(URIUtil.SLASH)) {
                this._urlString = new StringBuffer().append(this._urlString).append(URIUtil.SLASH).toString();
            }
        } else if (this._urlString.endsWith(URIUtil.SLASH)) {
            this._urlString = this._urlString.substring(0, this._urlString.length() - 1);
        }
    }

    FileResource(URL url, URLConnection uRLConnection, File file) {
        super(url, uRLConnection);
        this._file = file;
        if (this._file.isDirectory() && !this._urlString.endsWith(URIUtil.SLASH)) {
            this._urlString = new StringBuffer().append(this._urlString).append(URIUtil.SLASH).toString();
        }
    }

    public static boolean getCheckAliases() {
        return __checkAliases;
    }

    public static void setCheckAliases(boolean z) {
        __checkAliases = z;
    }

    public Resource addPath(String str) throws IOException, MalformedURLException {
        String str2;
        Resource resource;
        String canonicalPath = URIUtil.canonicalPath(str);
        if (!isDirectory()) {
            Resource resource2 = (FileResource) super.addPath(canonicalPath);
            str2 = resource2._urlString;
            resource = resource2;
        } else if (canonicalPath == null) {
            throw new MalformedURLException();
        } else {
            String addPaths = URIUtil.addPaths(this._urlString, URIUtil.encodePath(canonicalPath.startsWith(URIUtil.SLASH) ? canonicalPath.substring(1) : canonicalPath));
            str2 = addPaths;
            resource = (URLResource) Resource.newResource(addPaths);
        }
        String encodePath = URIUtil.encodePath(canonicalPath);
        int length = resource.toString().length() - encodePath.length();
        int lastIndexOf = resource._urlString.lastIndexOf(encodePath, length);
        if (!(length == lastIndexOf || ((length - 1 == lastIndexOf && !canonicalPath.endsWith(URIUtil.SLASH) && resource.isDirectory()) || (resource instanceof BadResource)))) {
            ((FileResource) resource)._alias = new URL(str2);
            ((FileResource) resource)._aliasChecked = true;
        }
        return resource;
    }

    public boolean delete() throws SecurityException {
        return this._file.delete();
    }

    public String encode(String str) {
        return str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof FileResource)) {
            FileResource fileResource = (FileResource) obj;
            if (fileResource._file == this._file) {
                return true;
            }
            if (this._file != null && this._file.equals(fileResource._file)) {
                return true;
            }
        }
        return false;
    }

    public boolean exists() {
        return this._file.exists();
    }

    public URL getAlias() {
        if (__checkAliases && !this._aliasChecked) {
            try {
                String absolutePath = this._file.getAbsolutePath();
                String canonicalPath = this._file.getCanonicalPath();
                if (!(absolutePath.length() == canonicalPath.length() && absolutePath.equals(canonicalPath))) {
                    this._alias = new File(canonicalPath).toURI().toURL();
                }
                this._aliasChecked = true;
                if (this._alias != null && Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("ALIAS abs=").append(absolutePath).toString());
                    Log.debug(new StringBuffer().append("ALIAS can=").append(canonicalPath).toString());
                }
            } catch (Throwable e) {
                Log.warn(Log.EXCEPTION, e);
                return getURL();
            }
        }
        return this._alias;
    }

    public File getFile() {
        return this._file;
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this._file);
    }

    public String getName() {
        return this._file.getAbsolutePath();
    }

    public OutputStream getOutputStream() throws IOException, SecurityException {
        return new FileOutputStream(this._file);
    }

    public int hashCode() {
        return this._file == null ? super.hashCode() : this._file.hashCode();
    }

    public boolean isDirectory() {
        return this._file.isDirectory();
    }

    public long lastModified() {
        return this._file.lastModified();
    }

    public long length() {
        return this._file.length();
    }

    public String[] list() {
        String[] list = this._file.list();
        if (list == null) {
            return null;
        }
        int length = list.length;
        while (true) {
            int i = length - 1;
            if (length <= 0) {
                return list;
            }
            if (!new File(this._file, list[i]).isDirectory() || list[i].endsWith(URIUtil.SLASH)) {
                length = i;
            } else {
                list[i] = new StringBuffer().append(list[i]).append(URIUtil.SLASH).toString();
                length = i;
            }
        }
    }

    public boolean renameTo(Resource resource) throws SecurityException {
        return resource instanceof FileResource ? this._file.renameTo(((FileResource) resource)._file) : false;
    }
}
