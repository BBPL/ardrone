package com.google.api.client.http;

import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import com.google.api.client.util.escape.CharEscapers;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map.Entry;

public class UrlEncodedContent extends AbstractHttpContent {
    private Object data;

    public UrlEncodedContent(Object obj) {
        super(UrlEncodedParser.MEDIA_TYPE);
        setData(obj);
    }

    private static boolean appendParam(boolean z, Writer writer, String str, Object obj) throws IOException {
        if (!(obj == null || Data.isNull(obj))) {
            if (z) {
                z = false;
            } else {
                writer.write("&");
            }
            writer.write(str);
            String escapeUri = CharEscapers.escapeUri(obj instanceof Enum ? FieldInfo.of((Enum) obj).getName() : obj.toString());
            if (escapeUri.length() != 0) {
                writer.write("=");
                writer.write(escapeUri);
            }
        }
        return z;
    }

    public static UrlEncodedContent getContent(HttpRequest httpRequest) {
        HttpContent content = httpRequest.getContent();
        if (content != null) {
            return (UrlEncodedContent) content;
        }
        Object urlEncodedContent = new UrlEncodedContent(new HashMap());
        httpRequest.setContent(urlEncodedContent);
        return urlEncodedContent;
    }

    public final Object getData() {
        return this.data;
    }

    public UrlEncodedContent setData(Object obj) {
        this.data = Preconditions.checkNotNull(obj);
        return this;
    }

    public UrlEncodedContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, getCharset()));
        boolean z = true;
        for (Entry entry : Data.mapOf(this.data).entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                String escapeUri = CharEscapers.escapeUri((String) entry.getKey());
                Class cls = value.getClass();
                if ((value instanceof Iterable) || cls.isArray()) {
                    boolean z2 = z;
                    for (Object appendParam : Types.iterableOf(value)) {
                        z2 = appendParam(z2, bufferedWriter, escapeUri, appendParam);
                    }
                    z = z2;
                } else {
                    z = appendParam(z, bufferedWriter, escapeUri, value);
                }
            }
        }
        bufferedWriter.flush();
    }
}
