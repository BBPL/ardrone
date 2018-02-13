package org.apache.http.impl.client.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.cache.Resource;

@ThreadSafe
public class FileResource implements Resource {
    private static final long serialVersionUID = 4132244415919043397L;
    private volatile boolean disposed = false;
    private final File file;

    public FileResource(File file) {
        this.file = file;
    }

    public void dispose() {
        synchronized (this) {
            if (!this.disposed) {
                this.disposed = true;
                this.file.delete();
            }
        }
    }

    File getFile() {
        File file;
        synchronized (this) {
            file = this.file;
        }
        return file;
    }

    public InputStream getInputStream() throws IOException {
        InputStream fileInputStream;
        synchronized (this) {
            fileInputStream = new FileInputStream(this.file);
        }
        return fileInputStream;
    }

    public long length() {
        long length;
        synchronized (this) {
            length = this.file.length();
        }
        return length;
    }
}
