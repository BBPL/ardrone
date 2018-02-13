package com.google.api.client.testing.http.json;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class MockJsonParser extends JsonParser {
    private final JsonFactory factory;

    MockJsonParser(JsonFactory jsonFactory) {
        this.factory = jsonFactory;
    }

    public void close() throws IOException {
    }

    public BigInteger getBigIntegerValue() throws IOException {
        return null;
    }

    public byte getByteValue() throws IOException {
        return (byte) 0;
    }

    public String getCurrentName() throws IOException {
        return null;
    }

    public JsonToken getCurrentToken() {
        return null;
    }

    public BigDecimal getDecimalValue() throws IOException {
        return null;
    }

    public double getDoubleValue() throws IOException {
        return 0.0d;
    }

    public JsonFactory getFactory() {
        return this.factory;
    }

    public float getFloatValue() throws IOException {
        return 0.0f;
    }

    public int getIntValue() throws IOException {
        return 0;
    }

    public long getLongValue() throws IOException {
        return 0;
    }

    public short getShortValue() throws IOException {
        return (short) 0;
    }

    public String getText() throws IOException {
        return null;
    }

    public JsonToken nextToken() throws IOException {
        return null;
    }

    public JsonParser skipChildren() throws IOException {
        return null;
    }
}
