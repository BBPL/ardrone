package com.google.api.client.json;

import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sets;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JsonObjectParser implements ObjectParser {
    private final JsonFactory jsonFactory;
    private final Set<String> wrapperKeys;

    public static class Builder {
        final JsonFactory jsonFactory;
        Collection<String> wrapperKeys = Sets.newHashSet();

        public Builder(JsonFactory jsonFactory) {
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        }

        public JsonObjectParser build() {
            return new JsonObjectParser(this);
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final Collection<String> getWrapperKeys() {
            return this.wrapperKeys;
        }

        public Builder setWrapperKeys(Collection<String> collection) {
            this.wrapperKeys = collection;
            return this;
        }
    }

    public JsonObjectParser(JsonFactory jsonFactory) {
        this(new Builder(jsonFactory));
    }

    protected JsonObjectParser(Builder builder) {
        this.jsonFactory = builder.jsonFactory;
        this.wrapperKeys = new HashSet(builder.wrapperKeys);
    }

    private void initializeParser(JsonParser jsonParser) throws IOException {
        boolean z = true;
        if (!this.wrapperKeys.isEmpty()) {
            try {
                if (jsonParser.skipToKey(this.wrapperKeys) == null || jsonParser.getCurrentToken() == JsonToken.END_OBJECT) {
                    z = false;
                }
                Preconditions.checkArgument(z, "wrapper key(s) not found: %s", this.wrapperKeys);
            } catch (Throwable th) {
                jsonParser.close();
            }
        }
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public Set<String> getWrapperKeys() {
        return Collections.unmodifiableSet(this.wrapperKeys);
    }

    public <T> T parseAndClose(InputStream inputStream, Charset charset, Class<T> cls) throws IOException {
        return parseAndClose(inputStream, charset, (Type) cls);
    }

    public Object parseAndClose(InputStream inputStream, Charset charset, Type type) throws IOException {
        JsonParser createJsonParser = this.jsonFactory.createJsonParser(inputStream, charset);
        initializeParser(createJsonParser);
        return createJsonParser.parse(type, true, null);
    }

    public <T> T parseAndClose(Reader reader, Class<T> cls) throws IOException {
        return parseAndClose(reader, (Type) cls);
    }

    public Object parseAndClose(Reader reader, Type type) throws IOException {
        JsonParser createJsonParser = this.jsonFactory.createJsonParser(reader);
        initializeParser(createJsonParser);
        return createJsonParser.parse(type, true, null);
    }
}
