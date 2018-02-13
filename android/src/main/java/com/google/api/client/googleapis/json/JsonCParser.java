package com.google.api.client.googleapis.json;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.util.Preconditions;
import com.sony.rdis.receiver.ServiceComuncationProtocol;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;

@Deprecated
public final class JsonCParser extends JsonObjectParser {
    private final JsonFactory jsonFactory;

    public JsonCParser(JsonFactory jsonFactory) {
        super(jsonFactory);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
    }

    public static JsonParser initializeParser(JsonParser jsonParser) throws IOException {
        try {
            if (jsonParser.skipToKey(new HashSet(Arrays.asList(new String[]{ServiceComuncationProtocol.DATA, "error"}))) != null && jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
                return jsonParser;
            }
            throw new IllegalArgumentException("data key not found");
        } catch (Throwable th) {
            jsonParser.close();
        }
    }

    public final JsonFactory getFactory() {
        return this.jsonFactory;
    }

    public Object parseAndClose(InputStream inputStream, Charset charset, Type type) throws IOException {
        return initializeParser(this.jsonFactory.createJsonParser(inputStream, charset)).parse(type, true, null);
    }

    public Object parseAndClose(Reader reader, Type type) throws IOException {
        return initializeParser(this.jsonFactory.createJsonParser(reader)).parse(type, true, null);
    }
}
