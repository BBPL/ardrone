package org.mortbay.resource;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.mortbay.log.Log;
import org.mortbay.util.URIUtil;

class JarFileResource extends JarResource {
    transient boolean _directory;
    transient JarEntry _entry;
    transient boolean _exists;
    transient File _file;
    transient JarFile _jarFile;
    transient String _jarUrl;
    transient String[] _list;
    transient String _path;

    JarFileResource(URL url) {
        super(url);
    }

    JarFileResource(URL url, boolean z) {
        super(url, z);
    }

    public static Resource getNonCachingResource(Resource resource) {
        return !(resource instanceof JarFileResource) ? resource : new JarFileResource(((JarFileResource) resource).getURL(), false);
    }

    protected boolean checkConnection() {
        try {
            super.checkConnection();
            return this._jarFile != null;
        } finally {
            if (this._jarConnection == null) {
                this._entry = null;
                this._file = null;
                this._jarFile = null;
                this._list = null;
            }
        }
    }

    public String encode(String str) {
        return str;
    }

    public boolean exists() {
        if (this._exists) {
            return true;
        }
        if (this._urlString.endsWith("!/")) {
            try {
                return Resource.newResource(this._urlString.substring(4, this._urlString.length() - 2)).exists();
            } catch (Throwable e) {
                Log.ignore(e);
                return false;
            }
        }
        boolean checkConnection = checkConnection();
        if (this._jarUrl == null || this._path != null) {
            JarFile jarFile;
            if (checkConnection) {
                jarFile = this._jarFile;
            } else {
                try {
                    JarURLConnection jarURLConnection = (JarURLConnection) new URL(this._jarUrl).openConnection();
                    jarURLConnection.setUseCaches(getUseCaches());
                    jarFile = jarURLConnection.getJarFile();
                } catch (Throwable e2) {
                    Log.ignore(e2);
                    jarFile = null;
                }
            }
            if (jarFile != null && this._entry == null && !this._directory) {
                Enumeration entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    String replace = jarEntry.getName().replace('\\', '/');
                    if (!replace.equals(this._path)) {
                        if (!this._path.endsWith(URIUtil.SLASH)) {
                            if (replace.startsWith(this._path) && replace.length() > this._path.length() && replace.charAt(this._path.length()) == '/') {
                                this._directory = true;
                                break;
                            }
                        } else if (replace.startsWith(this._path)) {
                            this._directory = true;
                            break;
                        }
                    } else {
                        this._entry = jarEntry;
                        this._directory = this._path.endsWith(URIUtil.SLASH);
                        break;
                    }
                }
            }
            checkConnection = this._directory || this._entry != null;
            this._exists = checkConnection;
            return this._exists;
        }
        this._directory = checkConnection;
        return true;
    }

    public boolean isDirectory() {
        return this._urlString.endsWith(URIUtil.SLASH) || (exists() && this._directory);
    }

    public long lastModified() {
        return (!checkConnection() || this._file == null) ? -1 : this._file.lastModified();
    }

    public long length() {
        return (isDirectory() || this._entry == null) ? -1 : this._entry.getSize();
    }

    public String[] list() {
        String[] strArr;
        synchronized (this) {
            if (isDirectory() && this._list == null) {
                JarFile jarFile;
                ArrayList arrayList = new ArrayList(32);
                checkConnection();
                JarFile jarFile2 = this._jarFile;
                if (jarFile2 == null) {
                    try {
                        JarURLConnection jarURLConnection = (JarURLConnection) new URL(this._jarUrl).openConnection();
                        jarURLConnection.setUseCaches(getUseCaches());
                        jarFile = jarURLConnection.getJarFile();
                    } catch (Throwable e) {
                        Log.ignore(e);
                        jarFile = jarFile2;
                    }
                } else {
                    jarFile = jarFile2;
                }
                Enumeration entries = jarFile.entries();
                String substring = this._urlString.substring(this._urlString.indexOf("!/") + 2);
                while (entries.hasMoreElements()) {
                    String replace = ((JarEntry) entries.nextElement()).getName().replace('\\', '/');
                    if (replace.startsWith(substring) && replace.length() != substring.length()) {
                        Object substring2 = replace.substring(substring.length());
                        int indexOf = substring2.indexOf(47);
                        if (indexOf >= 0) {
                            if (indexOf != 0 || substring2.length() != 1) {
                                substring2 = indexOf == 0 ? substring2.substring(indexOf + 1, substring2.length()) : substring2.substring(0, indexOf + 1);
                                if (arrayList.contains(substring2)) {
                                }
                            }
                        }
                        arrayList.add(substring2);
                    }
                }
                this._list = new String[arrayList.size()];
                arrayList.toArray(this._list);
            }
            strArr = this._list;
        }
        return strArr;
    }

    protected void newConnection() throws IOException {
        super.newConnection();
        this._entry = null;
        this._file = null;
        this._jarFile = null;
        this._list = null;
        int indexOf = this._urlString.indexOf("!/");
        this._jarUrl = this._urlString.substring(0, indexOf + 2);
        this._path = this._urlString.substring(indexOf + 2);
        if (this._path.length() == 0) {
            this._path = null;
        }
        this._jarFile = this._jarConnection.getJarFile();
        this._file = new File(this._jarFile.getName());
    }

    public void release() {
        synchronized (this) {
            this._list = null;
            this._entry = null;
            this._file = null;
            this._jarFile = null;
            super.release();
        }
    }
}
