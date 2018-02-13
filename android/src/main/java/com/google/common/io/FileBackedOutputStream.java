package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Beta
public final class FileBackedOutputStream extends OutputStream {
    private File file;
    private final int fileThreshold;
    private MemoryOutput memory;
    private OutputStream out;
    private final boolean resetOnFinalize;
    private final InputSupplier<InputStream> supplier;

    class C08061 implements InputSupplier<InputStream> {
        C08061() {
        }

        protected void finalize() {
            try {
                FileBackedOutputStream.this.reset();
            } catch (Throwable th) {
                th.printStackTrace(System.err);
            }
        }

        public InputStream getInput() throws IOException {
            return FileBackedOutputStream.this.openStream();
        }
    }

    class C08072 implements InputSupplier<InputStream> {
        C08072() {
        }

        public InputStream getInput() throws IOException {
            return FileBackedOutputStream.this.openStream();
        }
    }

    private static class MemoryOutput extends ByteArrayOutputStream {
        private MemoryOutput() {
        }

        byte[] getBuffer() {
            return this.buf;
        }

        int getCount() {
            return this.count;
        }
    }

    public FileBackedOutputStream(int i) {
        this(i, false);
    }

    public FileBackedOutputStream(int i, boolean z) {
        this.fileThreshold = i;
        this.resetOnFinalize = z;
        this.memory = new MemoryOutput();
        this.out = this.memory;
        if (z) {
            this.supplier = new C08061();
        } else {
            this.supplier = new C08072();
        }
    }

    private InputStream openStream() throws IOException {
        InputStream fileInputStream;
        synchronized (this) {
            fileInputStream = this.file != null ? new FileInputStream(this.file) : new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
        }
        return fileInputStream;
    }

    private void update(int i) throws IOException {
        if (this.file == null && this.memory.getCount() + i > this.fileThreshold) {
            File createTempFile = File.createTempFile("FileBackedOutputStream", null);
            if (this.resetOnFinalize) {
                createTempFile.deleteOnExit();
            }
            OutputStream fileOutputStream = new FileOutputStream(createTempFile);
            fileOutputStream.write(this.memory.getBuffer(), 0, this.memory.getCount());
            fileOutputStream.flush();
            this.out = fileOutputStream;
            this.file = createTempFile;
            this.memory = null;
        }
    }

    public void close() throws IOException {
        synchronized (this) {
            this.out.close();
        }
    }

    public void flush() throws IOException {
        synchronized (this) {
            this.out.flush();
        }
    }

    @VisibleForTesting
    File getFile() {
        File file;
        synchronized (this) {
            file = this.file;
        }
        return file;
    }

    public InputSupplier<InputStream> getSupplier() {
        return this.supplier;
    }

    public void reset() throws IOException {
        synchronized (this) {
            try {
                close();
                if (this.memory == null) {
                    this.memory = new MemoryOutput();
                } else {
                    this.memory.reset();
                }
                this.out = this.memory;
                if (this.file != null) {
                    File file = this.file;
                    this.file = null;
                    if (!file.delete()) {
                        throw new IOException("Could not delete: " + file);
                    }
                }
            } catch (Throwable th) {
                if (this.memory == null) {
                    this.memory = new MemoryOutput();
                } else {
                    this.memory.reset();
                }
                this.out = this.memory;
                if (this.file != null) {
                    File file2 = this.file;
                    this.file = null;
                    if (!file2.delete()) {
                        IOException iOException = new IOException("Could not delete: " + file2);
                    }
                }
            }
        }
    }

    public void write(int i) throws IOException {
        synchronized (this) {
            update(1);
            this.out.write(i);
        }
    }

    public void write(byte[] bArr) throws IOException {
        synchronized (this) {
            write(bArr, 0, bArr.length);
        }
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        synchronized (this) {
            update(i2);
            this.out.write(bArr, i, i2);
        }
    }
}
