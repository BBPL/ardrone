package com.google.api.client.testing.util;

import com.google.api.client.util.Lists;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogRecordingHandler extends Handler {
    private final List<LogRecord> records = Lists.newArrayList();

    public void close() throws SecurityException {
    }

    public void flush() {
    }

    public List<String> messages() {
        List<String> newArrayList = Lists.newArrayList();
        for (LogRecord message : this.records) {
            newArrayList.add(message.getMessage());
        }
        return newArrayList;
    }

    public void publish(LogRecord logRecord) {
        this.records.add(logRecord);
    }
}
