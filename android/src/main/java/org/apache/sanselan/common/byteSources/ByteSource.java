package org.apache.sanselan.common.byteSources;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.common.BinaryFileFunctions;

public abstract class ByteSource extends BinaryFileFunctions {
    protected final String filename;

    public ByteSource(String str) {
        this.filename = str;
    }

    public abstract byte[] getAll() throws IOException;

    public abstract byte[] getBlock(int i, int i2) throws IOException;

    public abstract String getDescription();

    public final String getFilename() {
        return this.filename;
    }

    public abstract InputStream getInputStream() throws IOException;

    public final InputStream getInputStream(int i) throws IOException {
        InputStream inputStream = getInputStream();
        skipBytes(inputStream, i);
        return inputStream;
    }

    public abstract long getLength() throws IOException;
}
