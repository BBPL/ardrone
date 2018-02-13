package org.mortbay.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

class BadResource extends URLResource {
    private String _message = null;

    BadResource(URL url, String str) {
        super(url, null);
        this._message = str;
    }

    public boolean delete() throws SecurityException {
        throw new SecurityException(this._message);
    }

    public boolean exists() {
        return false;
    }

    public File getFile() {
        return null;
    }

    public InputStream getInputStream() throws IOException {
        throw new FileNotFoundException(this._message);
    }

    public OutputStream getOutputStream() throws IOException, SecurityException {
        throw new FileNotFoundException(this._message);
    }

    public boolean isDirectory() {
        return false;
    }

    public long lastModified() {
        return -1;
    }

    public long length() {
        return -1;
    }

    public String[] list() {
        return null;
    }

    public boolean renameTo(Resource resource) throws SecurityException {
        throw new SecurityException(this._message);
    }

    public String toString() {
        return new StringBuffer().append(super.toString()).append("; BadResource=").append(this._message).toString();
    }
}
