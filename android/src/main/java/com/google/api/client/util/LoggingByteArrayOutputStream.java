package com.google.api.client.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingByteArrayOutputStream extends ByteArrayOutputStream {
    private int bytesWritten;
    private boolean closed;
    private final Logger logger;
    private final Level loggingLevel;
    private final int maximumBytesToLog;

    public LoggingByteArrayOutputStream(Logger logger, Level level, int i) {
        this.logger = (Logger) Preconditions.checkNotNull(logger);
        this.loggingLevel = (Level) Preconditions.checkNotNull(level);
        Preconditions.checkArgument(i >= 0);
        this.maximumBytesToLog = i;
    }

    private static void appendBytes(StringBuilder stringBuilder, int i) {
        if (i == 1) {
            stringBuilder.append("1 byte");
        } else {
            stringBuilder.append(NumberFormat.getInstance().format((long) i)).append(" bytes");
        }
    }

    public void close() throws IOException {
        synchronized (this) {
            if (!this.closed) {
                if (this.bytesWritten != 0) {
                    StringBuilder append = new StringBuilder().append("Total: ");
                    appendBytes(append, this.bytesWritten);
                    if (this.count != 0 && this.count < this.bytesWritten) {
                        append.append(" (logging first ");
                        appendBytes(append, this.count);
                        append.append(")");
                    }
                    this.logger.config(append.toString());
                    if (this.count != 0) {
                        this.logger.log(this.loggingLevel, toString("UTF-8").replaceAll("[\\x00-\\x09\\x0B\\x0C\\x0E-\\x1F\\x7F]", " "));
                    }
                }
                this.closed = true;
            }
        }
    }

    public final int getBytesWritten() {
        int i;
        synchronized (this) {
            i = this.bytesWritten;
        }
        return i;
    }

    public final int getMaximumBytesToLog() {
        return this.maximumBytesToLog;
    }

    public void write(int i) {
        synchronized (this) {
            Preconditions.checkArgument(!this.closed);
            this.bytesWritten++;
            if (this.count < this.maximumBytesToLog) {
                super.write(i);
            }
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        synchronized (this) {
            Preconditions.checkArgument(!this.closed);
            this.bytesWritten += i2;
            if (this.count < this.maximumBytesToLog) {
                int i3 = this.count + i2;
                if (i3 > this.maximumBytesToLog) {
                    i2 += this.maximumBytesToLog - i3;
                }
                super.write(bArr, i, i2);
            }
        }
    }
}
