package org.mortbay.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.log.Log;
import org.mortbay.util.IO;
import org.mortbay.util.URIUtil;

public class JarResource extends URLResource {
    protected transient JarURLConnection _jarConnection;

    JarResource(URL url) {
        super(url, null);
    }

    JarResource(URL url, boolean z) {
        super(url, null, z);
    }

    public static void extract(Resource resource, File file, boolean z) throws IOException {
        OutputStream fileOutputStream;
        Throwable th;
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("Extract ").append(resource).append(" to ").append(file).toString());
        }
        String trim = resource.getURL().toExternalForm().trim();
        int indexOf = trim.indexOf("!/");
        int i = indexOf >= 0 ? 4 : 0;
        if (indexOf < 0) {
            throw new IOException(new StringBuffer().append("Not a valid jar url: ").append(trim).toString());
        }
        URL url = new URL(trim.substring(i, indexOf));
        String substring = indexOf + 2 < trim.length() ? trim.substring(indexOf + 2) : null;
        Object obj = (substring == null || !substring.endsWith(URIUtil.SLASH)) ? null : 1;
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("Extracting entry = ").append(substring).append(" from jar ").append(url).toString());
        }
        InputStream jarInputStream = new JarInputStream(url.openConnection().getInputStream());
        new StringBuffer().append(file.getCanonicalPath()).append(URIUtil.SLASH).toString();
        while (true) {
            JarEntry nextJarEntry = jarInputStream.getNextJarEntry();
            if (nextJarEntry == null) {
                break;
            }
            Object obj2;
            File file2;
            trim = nextJarEntry.getName();
            if (substring == null || !trim.startsWith(substring)) {
                obj2 = (substring == null || trim.startsWith(substring)) ? 1 : null;
            } else if (obj != null) {
                trim = trim.substring(substring.length());
                obj2 = !trim.equals(HttpVersions.HTTP_0_9) ? 1 : null;
            } else {
                obj2 = 1;
            }
            if (obj2 == null) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("Skipping entry: ").append(trim).toString());
                }
            } else if (URIUtil.canonicalPath(trim.replace('\\', '/')) != null) {
                File file3 = new File(file, trim);
                if (!nextJarEntry.isDirectory()) {
                    file2 = new File(file3.getParent());
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    try {
                        fileOutputStream = new FileOutputStream(file3);
                        try {
                            IO.copy(jarInputStream, fileOutputStream);
                            IO.close(fileOutputStream);
                            if (nextJarEntry.getTime() >= 0) {
                                file3.setLastModified(nextJarEntry.getTime());
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream = null;
                    }
                } else if (!file3.exists()) {
                    file3.mkdirs();
                }
                if (z) {
                    file3.deleteOnExit();
                }
            } else if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("Invalid entry: ").append(trim).toString());
            }
        }
        if (substring == null || (substring != null && substring.equalsIgnoreCase("META-INF/MANIFEST.MF"))) {
            Manifest manifest = jarInputStream.getManifest();
            if (manifest != null) {
                file2 = new File(file, "META-INF");
                file2.mkdir();
                OutputStream fileOutputStream2 = new FileOutputStream(new File(file2, "MANIFEST.MF"));
                manifest.write(fileOutputStream2);
                fileOutputStream2.close();
            }
        }
        IO.close(jarInputStream);
        return;
        IO.close(fileOutputStream);
        throw th;
    }

    protected boolean checkConnection() {
        super.checkConnection();
        try {
            if (this._jarConnection != this._connection) {
                newConnection();
            }
        } catch (Throwable e) {
            Log.ignore(e);
            this._jarConnection = null;
        }
        return this._jarConnection != null;
    }

    public boolean exists() {
        return this._urlString.endsWith("!/") ? checkConnection() : super.exists();
    }

    public void extract(File file, boolean z) throws IOException {
        extract(this, file, z);
    }

    public File getFile() throws IOException {
        return null;
    }

    public InputStream getInputStream() throws IOException {
        checkConnection();
        return !this._urlString.endsWith("!/") ? new FilterInputStream(this, super.getInputStream()) {
            private final JarResource this$0;

            public void close() throws IOException {
                this.in = IO.getClosedStream();
            }
        } : new URL(this._urlString.substring(4, this._urlString.length() - 2)).openStream();
    }

    protected void newConnection() throws IOException {
        this._jarConnection = (JarURLConnection) this._connection;
    }

    public void release() {
        synchronized (this) {
            this._jarConnection = null;
            super.release();
        }
    }
}
