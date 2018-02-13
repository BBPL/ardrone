package com.google.api.client.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoggingStreamingContent implements StreamingContent {
    private final StreamingContent content;
    private final int contentLoggingLimit;
    private final Logger logger;
    private final Level loggingLevel;

    public LoggingStreamingContent(StreamingContent streamingContent, Logger logger, Level level, int i) {
        this.content = streamingContent;
        this.logger = logger;
        this.loggingLevel = level;
        this.contentLoggingLimit = i;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        OutputStream loggingOutputStream = new LoggingOutputStream(outputStream, this.logger, this.loggingLevel, this.contentLoggingLimit);
        try {
            this.content.writeTo(loggingOutputStream);
            outputStream.flush();
        } finally {
            loggingOutputStream.getLogStream().close();
        }
    }
}
