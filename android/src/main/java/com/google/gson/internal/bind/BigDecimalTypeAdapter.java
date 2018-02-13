package com.google.gson.internal.bind;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;

public final class BigDecimalTypeAdapter extends TypeAdapter<BigDecimal> {
    public BigDecimal read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        try {
            return new BigDecimal(jsonReader.nextString());
        } catch (Throwable e) {
            throw new JsonSyntaxException(e);
        }
    }

    public void write(JsonWriter jsonWriter, BigDecimal bigDecimal) throws IOException {
        jsonWriter.value((Number) bigDecimal);
    }
}
