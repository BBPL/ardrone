package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ObjectTypeAdapter extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new C09201();
    private final Gson gson;

    static final class C09201 implements TypeAdapterFactory {
        C09201() {
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Object.class ? new ObjectTypeAdapter(gson) : null;
        }
    }

    private ObjectTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    public Object read(JsonReader jsonReader) throws IOException {
        switch (jsonReader.peek()) {
            case BEGIN_ARRAY:
                List arrayList = new ArrayList();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    arrayList.add(read(jsonReader));
                }
                jsonReader.endArray();
                return arrayList;
            case BEGIN_OBJECT:
                Map linkedHashMap = new LinkedHashMap();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    linkedHashMap.put(jsonReader.nextName(), read(jsonReader));
                }
                jsonReader.endObject();
                return linkedHashMap;
            case STRING:
                return jsonReader.nextString();
            case NUMBER:
                return Double.valueOf(jsonReader.nextDouble());
            case BOOLEAN:
                return Boolean.valueOf(jsonReader.nextBoolean());
            case NULL:
                jsonReader.nextNull();
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    public void write(JsonWriter jsonWriter, Object obj) throws IOException {
        if (obj == null) {
            jsonWriter.nullValue();
            return;
        }
        TypeAdapter adapter = this.gson.getAdapter(obj.getClass());
        if (adapter instanceof ObjectTypeAdapter) {
            jsonWriter.beginObject();
            jsonWriter.endObject();
            return;
        }
        adapter.write(jsonWriter, obj);
    }
}
