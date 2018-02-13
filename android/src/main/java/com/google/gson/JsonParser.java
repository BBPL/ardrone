package com.google.gson;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.EOFException;
import java.io.Reader;
import java.io.StringReader;

public final class JsonParser {
    public JsonElement parse(JsonReader jsonReader) throws JsonIOException, JsonSyntaxException {
        JsonElement parse;
        boolean isLenient = jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            parse = Streams.parse(jsonReader);
            jsonReader.setLenient(isLenient);
        } catch (Throwable e) {
            throw new JsonParseException("Failed parsing JSON source: " + jsonReader + " to Json", e);
        } catch (Throwable e2) {
            throw new JsonParseException("Failed parsing JSON source: " + jsonReader + " to Json", e2);
        } catch (JsonParseException e3) {
            if (e3.getCause() instanceof EOFException) {
                parse = JsonNull.INSTANCE;
                jsonReader.setLenient(isLenient);
            } else {
                throw e3;
            }
        } catch (Throwable th) {
            jsonReader.setLenient(isLenient);
        }
        return parse;
    }

    public JsonElement parse(Reader reader) throws JsonIOException, JsonSyntaxException {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            JsonElement parse = parse(jsonReader);
            if (parse.isJsonNull() || jsonReader.peek() == JsonToken.END_DOCUMENT) {
                return parse;
            }
            throw new JsonSyntaxException("Did not consume the entire document.");
        } catch (Throwable e) {
            throw new JsonSyntaxException(e);
        } catch (Throwable e2) {
            throw new JsonIOException(e2);
        } catch (Throwable e22) {
            throw new JsonSyntaxException(e22);
        }
    }

    public JsonElement parse(String str) throws JsonSyntaxException {
        return parse(new StringReader(str));
    }
}
