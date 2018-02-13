package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class FileContent extends AbstractInputStreamContent {
    private final File file;

    public FileContent(String str, File file) {
        super(str);
        this.file = (File) Preconditions.checkNotNull(file);
    }

    public File getFile() {
        return this.file;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(this.file);
    }

    public long getLength() {
        return this.file.length();
    }

    public boolean retrySupported() {
        return true;
    }

    public FileContent setCloseInputStream(boolean z) {
        return (FileContent) super.setCloseInputStream(z);
    }

    @Deprecated
    public FileContent setEncoding(String str) {
        return (FileContent) super.setEncoding(str);
    }

    public FileContent setType(String str) {
        return (FileContent) super.setType(str);
    }
}
