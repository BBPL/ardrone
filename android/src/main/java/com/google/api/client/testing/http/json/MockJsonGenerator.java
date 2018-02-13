package com.google.api.client.testing.http.json;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class MockJsonGenerator extends JsonGenerator {
    private final JsonFactory factory;

    MockJsonGenerator(JsonFactory jsonFactory) {
        this.factory = jsonFactory;
    }

    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }

    public JsonFactory getFactory() {
        return this.factory;
    }

    public void writeBoolean(boolean z) throws IOException {
    }

    public void writeEndArray() throws IOException {
    }

    public void writeEndObject() throws IOException {
    }

    public void writeFieldName(String str) throws IOException {
    }

    public void writeNull() throws IOException {
    }

    public void writeNumber(double d) throws IOException {
    }

    public void writeNumber(float f) throws IOException {
    }

    public void writeNumber(int i) throws IOException {
    }

    public void writeNumber(long j) throws IOException {
    }

    public void writeNumber(String str) throws IOException {
    }

    public void writeNumber(BigDecimal bigDecimal) throws IOException {
    }

    public void writeNumber(BigInteger bigInteger) throws IOException {
    }

    public void writeStartArray() throws IOException {
    }

    public void writeStartObject() throws IOException {
    }

    public void writeString(String str) throws IOException {
    }
}
