package com.google.gson.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;

public class JsonWriter implements Closeable {
    private String deferredName;
    private boolean htmlSafe;
    private String indent;
    private boolean lenient;
    private final Writer out;
    private String separator;
    private boolean serializeNulls;
    private final List<JsonScope> stack = new ArrayList();

    public JsonWriter(Writer writer) {
        this.stack.add(JsonScope.EMPTY_DOCUMENT);
        this.separator = ":";
        this.serializeNulls = true;
        if (writer == null) {
            throw new NullPointerException("out == null");
        }
        this.out = writer;
    }

    private void beforeName() throws IOException {
        JsonScope peek = peek();
        if (peek == JsonScope.NONEMPTY_OBJECT) {
            this.out.write(44);
        } else if (peek != JsonScope.EMPTY_OBJECT) {
            throw new IllegalStateException("Nesting problem: " + this.stack);
        }
        newline();
        replaceTop(JsonScope.DANGLING_NAME);
    }

    private void beforeValue(boolean z) throws IOException {
        switch (peek()) {
            case EMPTY_DOCUMENT:
                if (this.lenient || z) {
                    replaceTop(JsonScope.NONEMPTY_DOCUMENT);
                    return;
                }
                throw new IllegalStateException("JSON must start with an array or an object.");
            case EMPTY_ARRAY:
                replaceTop(JsonScope.NONEMPTY_ARRAY);
                newline();
                return;
            case NONEMPTY_ARRAY:
                this.out.append(',');
                newline();
                return;
            case DANGLING_NAME:
                this.out.append(this.separator);
                replaceTop(JsonScope.NONEMPTY_OBJECT);
                return;
            case NONEMPTY_DOCUMENT:
                throw new IllegalStateException("JSON must have only one top-level value.");
            default:
                throw new IllegalStateException("Nesting problem: " + this.stack);
        }
    }

    private JsonWriter close(JsonScope jsonScope, JsonScope jsonScope2, String str) throws IOException {
        JsonScope peek = peek();
        if (peek != jsonScope2 && peek != jsonScope) {
            throw new IllegalStateException("Nesting problem: " + this.stack);
        } else if (this.deferredName != null) {
            throw new IllegalStateException("Dangling name: " + this.deferredName);
        } else {
            this.stack.remove(this.stack.size() - 1);
            if (peek == jsonScope2) {
                newline();
            }
            this.out.write(str);
            return this;
        }
    }

    private void newline() throws IOException {
        if (this.indent != null) {
            this.out.write("\n");
            for (int i = 1; i < this.stack.size(); i++) {
                this.out.write(this.indent);
            }
        }
    }

    private JsonWriter open(JsonScope jsonScope, String str) throws IOException {
        beforeValue(true);
        this.stack.add(jsonScope);
        this.out.write(str);
        return this;
    }

    private JsonScope peek() {
        return (JsonScope) this.stack.get(this.stack.size() - 1);
    }

    private void replaceTop(JsonScope jsonScope) {
        this.stack.set(this.stack.size() - 1, jsonScope);
    }

    private void string(String str) throws IOException {
        this.out.write("\"");
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case '\b':
                    this.out.write("\\b");
                    break;
                case '\t':
                    this.out.write("\\t");
                    break;
                case '\n':
                    this.out.write("\\n");
                    break;
                case '\f':
                    this.out.write("\\f");
                    break;
                case '\r':
                    this.out.write("\\r");
                    break;
                case '\"':
                case '\\':
                    this.out.write(92);
                    this.out.write(charAt);
                    break;
                case '&':
                case '\'':
                case CacheConfig.DEFAULT_ASYNCHRONOUS_WORKER_IDLE_LIFETIME_SECS /*60*/:
                case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                case ExifTagConstants.PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF /*62*/:
                    if (!this.htmlSafe) {
                        this.out.write(charAt);
                        break;
                    }
                    this.out.write(String.format("\\u%04x", new Object[]{Integer.valueOf(charAt)}));
                    break;
                case ' ':
                case ' ':
                    this.out.write(String.format("\\u%04x", new Object[]{Integer.valueOf(charAt)}));
                    break;
                default:
                    if (charAt > '\u001f') {
                        this.out.write(charAt);
                        break;
                    }
                    this.out.write(String.format("\\u%04x", new Object[]{Integer.valueOf(charAt)}));
                    break;
            }
        }
        this.out.write("\"");
    }

    private void writeDeferredName() throws IOException {
        if (this.deferredName != null) {
            beforeName();
            string(this.deferredName);
            this.deferredName = null;
        }
    }

    public JsonWriter beginArray() throws IOException {
        writeDeferredName();
        return open(JsonScope.EMPTY_ARRAY, "[");
    }

    public JsonWriter beginObject() throws IOException {
        writeDeferredName();
        return open(JsonScope.EMPTY_OBJECT, "{");
    }

    public void close() throws IOException {
        this.out.close();
        if (peek() != JsonScope.NONEMPTY_DOCUMENT) {
            throw new IOException("Incomplete document");
        }
    }

    public JsonWriter endArray() throws IOException {
        return close(JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, "]");
    }

    public JsonWriter endObject() throws IOException {
        return close(JsonScope.EMPTY_OBJECT, JsonScope.NONEMPTY_OBJECT, "}");
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }

    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public JsonWriter name(String str) throws IOException {
        if (str == null) {
            throw new NullPointerException("name == null");
        } else if (this.deferredName != null) {
            throw new IllegalStateException();
        } else {
            this.deferredName = str;
            return this;
        }
    }

    public JsonWriter nullValue() throws IOException {
        if (this.deferredName != null) {
            if (this.serializeNulls) {
                writeDeferredName();
            } else {
                this.deferredName = null;
                return this;
            }
        }
        beforeValue(false);
        this.out.write("null");
        return this;
    }

    public final void setHtmlSafe(boolean z) {
        this.htmlSafe = z;
    }

    public final void setIndent(String str) {
        if (str.length() == 0) {
            this.indent = null;
            this.separator = ":";
            return;
        }
        this.indent = str;
        this.separator = ": ";
    }

    public final void setLenient(boolean z) {
        this.lenient = z;
    }

    public final void setSerializeNulls(boolean z) {
        this.serializeNulls = z;
    }

    public JsonWriter value(double d) throws IOException {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + d);
        }
        writeDeferredName();
        beforeValue(false);
        this.out.append(Double.toString(d));
        return this;
    }

    public JsonWriter value(long j) throws IOException {
        writeDeferredName();
        beforeValue(false);
        this.out.write(Long.toString(j));
        return this;
    }

    public JsonWriter value(Number number) throws IOException {
        if (number == null) {
            return nullValue();
        }
        writeDeferredName();
        CharSequence obj = number.toString();
        if (this.lenient || !(obj.equals("-Infinity") || obj.equals("Infinity") || obj.equals("NaN"))) {
            beforeValue(false);
            this.out.append(obj);
            return this;
        }
        throw new IllegalArgumentException("Numeric values must be finite, but was " + number);
    }

    public JsonWriter value(String str) throws IOException {
        if (str == null) {
            return nullValue();
        }
        writeDeferredName();
        beforeValue(false);
        string(str);
        return this;
    }

    public JsonWriter value(boolean z) throws IOException {
        writeDeferredName();
        beforeValue(false);
        this.out.write(z ? "true" : "false");
        return this;
    }
}
