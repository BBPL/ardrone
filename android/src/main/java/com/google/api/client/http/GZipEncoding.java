package com.google.api.client.http;

import com.google.api.client.util.StreamingContent;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import org.mortbay.jetty.HttpHeaderValues;

public class GZipEncoding implements HttpEncoding {
    public void encode(StreamingContent streamingContent, OutputStream outputStream) throws IOException {
        OutputStream gZIPOutputStream = new GZIPOutputStream(new FilterOutputStream(outputStream) {
            public void close() throws IOException {
                try {
                    flush();
                } catch (IOException e) {
                }
            }
        });
        streamingContent.writeTo(gZIPOutputStream);
        gZIPOutputStream.close();
    }

    public String getName() {
        return HttpHeaderValues.GZIP;
    }
}
