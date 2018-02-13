package com.google.gson;

import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public abstract class TypeAdapter<T> {

    class C09011 extends TypeAdapter<T> {
        C09011() {
        }

        public T read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() != JsonToken.NULL) {
                return TypeAdapter.this.read(jsonReader);
            }
            jsonReader.nextNull();
            return null;
        }

        public void write(JsonWriter jsonWriter, T t) throws IOException {
            if (t == null) {
                jsonWriter.nullValue();
            } else {
                TypeAdapter.this.write(jsonWriter, t);
            }
        }
    }

    final T fromJson(Reader reader) throws IOException {
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true);
        return read(jsonReader);
    }

    final T fromJson(String str) throws IOException {
        return fromJson(new StringReader(str));
    }

    final T fromJsonTree(JsonElement jsonElement) {
        try {
            JsonReader jsonTreeReader = new JsonTreeReader(jsonElement);
            jsonTreeReader.setLenient(true);
            return read(jsonTreeReader);
        } catch (Throwable e) {
            throw new JsonIOException(e);
        }
    }

    public final TypeAdapter<T> nullSafe() {
        return new C09011();
    }

    public abstract T read(JsonReader jsonReader) throws IOException;

    final String toJson(T t) throws IOException {
        Writer stringWriter = new StringWriter();
        toJson(stringWriter, t);
        return stringWriter.toString();
    }

    final void toJson(Writer writer, T t) throws IOException {
        write(new JsonWriter(writer), t);
    }

    final JsonElement toJsonTree(T t) {
        try {
            JsonWriter jsonTreeWriter = new JsonTreeWriter();
            jsonTreeWriter.setLenient(true);
            write(jsonTreeWriter, t);
            return jsonTreeWriter.get();
        } catch (Throwable e) {
            throw new JsonIOException(e);
        }
    }

    public abstract void write(JsonWriter jsonWriter, T t) throws IOException;
}
