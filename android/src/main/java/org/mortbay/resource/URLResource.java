package org.mortbay.resource;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import org.mortbay.log.Log;
import org.mortbay.util.URIUtil;

public class URLResource extends Resource {
    protected transient URLConnection _connection;
    protected transient InputStream _in;
    protected URL _url;
    protected String _urlString;
    transient boolean _useCaches;

    protected URLResource(URL url, URLConnection uRLConnection) {
        this._in = null;
        this._useCaches = Resource.__defaultUseCaches;
        this._url = url;
        this._urlString = this._url.toString();
        this._connection = uRLConnection;
    }

    protected URLResource(URL url, URLConnection uRLConnection, boolean z) {
        this(url, uRLConnection);
        this._useCaches = z;
    }

    public Resource addPath(String str) throws IOException, MalformedURLException {
        if (str == null) {
            return null;
        }
        return Resource.newResource(URIUtil.addPaths(this._url.toExternalForm(), URIUtil.canonicalPath(str)));
    }

    protected boolean checkConnection() {
        boolean z;
        synchronized (this) {
            if (this._connection == null) {
                try {
                    this._connection = this._url.openConnection();
                    this._connection.setUseCaches(this._useCaches);
                } catch (Throwable e) {
                    Log.ignore(e);
                }
            }
            z = this._connection != null;
        }
        return z;
    }

    public boolean delete() throws SecurityException {
        throw new SecurityException("Delete not supported");
    }

    public boolean equals(Object obj) {
        return (obj instanceof URLResource) && this._url.equals(((URLResource) obj)._url);
    }

    public boolean exists() {
        try {
            synchronized (this) {
                if (checkConnection() && this._in == null) {
                    this._in = this._connection.getInputStream();
                }
            }
        } catch (Throwable e) {
            Log.ignore(e);
        }
        return this._in != null;
    }

    public File getFile() throws IOException {
        if (checkConnection()) {
            Permission permission = this._connection.getPermission();
            if (permission instanceof FilePermission) {
                return new File(permission.getName());
            }
        }
        try {
            return new File(this._url.getFile());
        } catch (Throwable e) {
            Log.ignore(e);
            return null;
        }
    }

    public InputStream getInputStream() throws IOException {
        InputStream inputStream;
        synchronized (this) {
            if (checkConnection()) {
                try {
                    if (this._in != null) {
                        inputStream = this._in;
                        this._in = null;
                        this._connection = null;
                    } else {
                        inputStream = this._connection.getInputStream();
                        this._connection = null;
                    }
                } catch (Throwable th) {
                    this._connection = null;
                }
            } else {
                throw new IOException("Invalid resource");
            }
        }
        return inputStream;
    }

    public String getName() {
        return this._url.toExternalForm();
    }

    public OutputStream getOutputStream() throws IOException, SecurityException {
        throw new IOException("Output not supported");
    }

    public URL getURL() {
        return this._url;
    }

    public boolean getUseCaches() {
        return this._useCaches;
    }

    public int hashCode() {
        return this._url.hashCode();
    }

    public boolean isDirectory() {
        return exists() && this._url.toString().endsWith(URIUtil.SLASH);
    }

    public long lastModified() {
        return checkConnection() ? this._connection.getLastModified() : -1;
    }

    public long length() {
        return checkConnection() ? (long) this._connection.getContentLength() : -1;
    }

    public String[] list() {
        return null;
    }

    public void release() {
        synchronized (this) {
            if (this._in != null) {
                try {
                    this._in.close();
                } catch (Throwable e) {
                    Log.ignore(e);
                }
                this._in = null;
            }
            if (this._connection != null) {
                this._connection = null;
            }
        }
    }

    public boolean renameTo(Resource resource) throws SecurityException {
        throw new SecurityException("RenameTo not supported");
    }

    public String toString() {
        return this._urlString;
    }
}
