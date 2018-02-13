package com.google.api.client.json.jackson;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonToken;

public final class JacksonFactory extends JsonFactory {
    private final org.codehaus.jackson.JsonFactory factory = new org.codehaus.jackson.JsonFactory();

    static /* synthetic */ class C04551 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];

        static {
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_ARRAY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_OBJECT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NULL.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    public JacksonFactory() {
        this.factory.configure(Feature.AUTO_CLOSE_JSON_CONTENT, false);
    }

    static com.google.api.client.json.JsonToken convert(JsonToken jsonToken) {
        if (jsonToken == null) {
            return null;
        }
        switch (C04551.$SwitchMap$org$codehaus$jackson$JsonToken[jsonToken.ordinal()]) {
            case 1:
                return com.google.api.client.json.JsonToken.END_ARRAY;
            case 2:
                return com.google.api.client.json.JsonToken.START_ARRAY;
            case 3:
                return com.google.api.client.json.JsonToken.END_OBJECT;
            case 4:
                return com.google.api.client.json.JsonToken.START_OBJECT;
            case 5:
                return com.google.api.client.json.JsonToken.VALUE_FALSE;
            case 6:
                return com.google.api.client.json.JsonToken.VALUE_TRUE;
            case 7:
                return com.google.api.client.json.JsonToken.VALUE_NULL;
            case 8:
                return com.google.api.client.json.JsonToken.VALUE_STRING;
            case 9:
                return com.google.api.client.json.JsonToken.VALUE_NUMBER_FLOAT;
            case 10:
                return com.google.api.client.json.JsonToken.VALUE_NUMBER_INT;
            case 11:
                return com.google.api.client.json.JsonToken.FIELD_NAME;
            default:
                return com.google.api.client.json.JsonToken.NOT_AVAILABLE;
        }
    }

    public JsonGenerator createJsonGenerator(OutputStream outputStream, Charset charset) throws IOException {
        return new JacksonGenerator(this, this.factory.createJsonGenerator(outputStream, JsonEncoding.UTF8));
    }

    public JsonGenerator createJsonGenerator(Writer writer) throws IOException {
        return new JacksonGenerator(this, this.factory.createJsonGenerator(writer));
    }

    public JsonParser createJsonParser(InputStream inputStream) throws IOException {
        Preconditions.checkNotNull(inputStream);
        return new JacksonParser(this, this.factory.createJsonParser(inputStream));
    }

    public JsonParser createJsonParser(InputStream inputStream, Charset charset) throws IOException {
        Preconditions.checkNotNull(inputStream);
        return new JacksonParser(this, this.factory.createJsonParser(inputStream));
    }

    public JsonParser createJsonParser(Reader reader) throws IOException {
        Preconditions.checkNotNull(reader);
        return new JacksonParser(this, this.factory.createJsonParser(reader));
    }

    public JsonParser createJsonParser(String str) throws IOException {
        Preconditions.checkNotNull(str);
        return new JacksonParser(this, this.factory.createJsonParser(str));
    }
}
