package org.apache.http.entity;

import com.parrot.ardronetool.ConfigArdroneMask;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class InputStreamEntity extends AbstractHttpEntity {
    private static final int BUFFER_SIZE = 2048;
    private final InputStream content;
    private final long length;

    public InputStreamEntity(InputStream inputStream, long j) {
        this(inputStream, j, null);
    }

    public InputStreamEntity(InputStream inputStream, long j, ContentType contentType) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Source input stream may not be null");
        }
        this.content = inputStream;
        this.length = j;
        if (contentType != null) {
            setContentType(contentType.toString());
        }
    }

    @Deprecated
    public void consumeContent() throws IOException {
        this.content.close();
    }

    public InputStream getContent() throws IOException {
        return this.content;
    }

    public long getContentLength() {
        return this.length;
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return true;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream inputStream = this.content;
        try {
            byte[] bArr = new byte[2048];
            if (this.length < 0) {
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(bArr, 0, read);
                }
            } else {
                long j = this.length;
                while (j > 0) {
                    int read2 = inputStream.read(bArr, 0, (int) Math.min(ConfigArdroneMask.ARDRONE_NAVDATA_BOOTSTRAP, j));
                    if (read2 == -1) {
                        break;
                    }
                    outputStream.write(bArr, 0, read2);
                    j -= (long) read2;
                }
            }
            inputStream.close();
        } catch (Throwable th) {
            inputStream.close();
        }
    }
}
