package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class JsonObject extends JsonElement {
    private final Map<String, JsonElement> members = new LinkedHashMap();

    private JsonElement createJsonElement(Object obj) {
        return obj == null ? JsonNull.INSTANCE : new JsonPrimitive(obj);
    }

    public void add(String str, JsonElement jsonElement) {
        Object obj;
        if (jsonElement == null) {
            obj = JsonNull.INSTANCE;
        }
        this.members.put(C$Gson$Preconditions.checkNotNull(str), obj);
    }

    public void addProperty(String str, Boolean bool) {
        add(str, createJsonElement(bool));
    }

    public void addProperty(String str, Character ch) {
        add(str, createJsonElement(ch));
    }

    public void addProperty(String str, Number number) {
        add(str, createJsonElement(number));
    }

    public void addProperty(String str, String str2) {
        add(str, createJsonElement(str2));
    }

    public Set<Entry<String, JsonElement>> entrySet() {
        return this.members.entrySet();
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof JsonObject) && ((JsonObject) obj).members.equals(this.members));
    }

    public JsonElement get(String str) {
        if (!this.members.containsKey(str)) {
            return null;
        }
        JsonElement jsonElement = (JsonElement) this.members.get(str);
        return jsonElement == null ? JsonNull.INSTANCE : jsonElement;
    }

    public JsonArray getAsJsonArray(String str) {
        return (JsonArray) this.members.get(str);
    }

    public JsonObject getAsJsonObject(String str) {
        return (JsonObject) this.members.get(str);
    }

    public JsonPrimitive getAsJsonPrimitive(String str) {
        return (JsonPrimitive) this.members.get(str);
    }

    public boolean has(String str) {
        return this.members.containsKey(str);
    }

    public int hashCode() {
        return this.members.hashCode();
    }

    public JsonElement remove(String str) {
        return (JsonElement) this.members.remove(str);
    }
}
