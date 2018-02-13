package org.apache.sanselan.common.byteSources;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import org.apache.sanselan.util.Debug;

public class ByteSourceFile extends ByteSource {
    private final File file;

    public ByteSourceFile(File file) {
        super(file.getName());
        this.file = file;
    }

    public byte[] getAll() throws IOException {
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        try {
            InputStream fileInputStream = new FileInputStream(this.file);
            try {
                inputStream = new BufferedInputStream(fileInputStream);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
                return toByteArray;
            } catch (Throwable th2) {
                th = th2;
                inputStream = fileInputStream;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public byte[] getBlock(int i, int i2) throws IOException {
        Throwable th;
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(this.file, "r");
            try {
                byte[] rAFBytes = getRAFBytes(randomAccessFile, (long) i, i2, "Could not read value from file");
                try {
                    randomAccessFile.close();
                } catch (Throwable e) {
                    Debug.debug(e);
                }
                return rAFBytes;
            } catch (Throwable th2) {
                th = th2;
                try {
                    randomAccessFile.close();
                } catch (Throwable e2) {
                    Debug.debug(e2);
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            randomAccessFile = null;
            randomAccessFile.close();
            throw th;
        }
    }

    public String getDescription() {
        return "File: '" + this.file.getAbsolutePath() + "'";
    }

    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(this.file));
    }

    public long getLength() {
        return this.file.length();
    }
}
