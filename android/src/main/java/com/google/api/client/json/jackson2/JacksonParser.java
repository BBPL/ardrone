package com.google.api.client.json.jackson2;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

final class JacksonParser extends JsonParser {
    private final JacksonFactory factory;
    private final com.fasterxml.jackson.core.JsonParser parser;

    JacksonParser(JacksonFactory jacksonFactory, com.fasterxml.jackson.core.JsonParser jsonParser) {
        this.factory = jacksonFactory;
        this.parser = jsonParser;
    }

    public void close() throws IOException {
        this.parser.close();
    }

    public BigInteger getBigIntegerValue() throws IOException {
        return this.parser.getBigIntegerValue();
    }

    public byte getByteValue() throws IOException {
        return this.parser.getByteValue();
    }

    public String getCurrentName() throws IOException {
        return this.parser.getCurrentName();
    }

    public JsonToken getCurrentToken() {
        return JacksonFactory.convert(this.parser.getCurrentToken());
    }

    public BigDecimal getDecimalValue() throws IOException {
        return this.parser.getDecimalValue();
    }

    public double getDoubleValue() throws IOException {
        return this.parser.getDoubleValue();
    }

    public JacksonFactory getFactory() {
        return this.factory;
    }

    public float getFloatValue() throws IOException {
        return this.parser.getFloatValue();
    }

    public int getIntValue() throws IOException {
        return this.parser.getIntValue();
    }

    public long getLongValue() throws IOException {
        return this.parser.getLongValue();
    }

    public short getShortValue() throws IOException {
        return this.parser.getShortValue();
    }

    public String getText() throws IOException {
        return this.parser.getText();
    }

    public JsonToken nextToken() throws IOException {
        return JacksonFactory.convert(this.parser.nextToken());
    }

    public JsonParser skipChildren() throws IOException {
        this.parser.skipChildren();
        return this;
    }
}
