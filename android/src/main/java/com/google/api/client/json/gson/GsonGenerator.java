package com.google.api.client.json.gson;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

class GsonGenerator extends JsonGenerator {
    private final GsonFactory factory;
    private final JsonWriter writer;

    static final class StringNumber extends Number {
        private static final long serialVersionUID = 1;
        private final String encodedValue;

        StringNumber(String str) {
            this.encodedValue = str;
        }

        public double doubleValue() {
            return 0.0d;
        }

        public float floatValue() {
            return 0.0f;
        }

        public int intValue() {
            return 0;
        }

        public long longValue() {
            return 0;
        }

        public String toString() {
            return this.encodedValue;
        }
    }

    GsonGenerator(GsonFactory gsonFactory, JsonWriter jsonWriter) {
        this.factory = gsonFactory;
        this.writer = jsonWriter;
        jsonWriter.setLenient(true);
    }

    public void close() throws IOException {
        this.writer.close();
    }

    public void enablePrettyPrint() throws IOException {
        this.writer.setIndent("  ");
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public JsonFactory getFactory() {
        return this.factory;
    }

    public void writeBoolean(boolean z) throws IOException {
        this.writer.value(z);
    }

    public void writeEndArray() throws IOException {
        this.writer.endArray();
    }

    public void writeEndObject() throws IOException {
        this.writer.endObject();
    }

    public void writeFieldName(String str) throws IOException {
        this.writer.name(str);
    }

    public void writeNull() throws IOException {
        this.writer.nullValue();
    }

    public void writeNumber(double d) throws IOException {
        this.writer.value(d);
    }

    public void writeNumber(float f) throws IOException {
        this.writer.value((double) f);
    }

    public void writeNumber(int i) throws IOException {
        this.writer.value((long) i);
    }

    public void writeNumber(long j) throws IOException {
        this.writer.value(j);
    }

    public void writeNumber(String str) throws IOException {
        this.writer.value(new StringNumber(str));
    }

    public void writeNumber(BigDecimal bigDecimal) throws IOException {
        this.writer.value((Number) bigDecimal);
    }

    public void writeNumber(BigInteger bigInteger) throws IOException {
        this.writer.value((Number) bigInteger);
    }

    public void writeStartArray() throws IOException {
        this.writer.beginArray();
    }

    public void writeStartObject() throws IOException {
        this.writer.beginObject();
    }

    public void writeString(String str) throws IOException {
        this.writer.value(str);
    }
}
