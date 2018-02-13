package com.google.api.client.googleapis.auth.clientlogin;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Types;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

final class AuthKeyValueParser implements ObjectParser {
    public static final AuthKeyValueParser INSTANCE = new AuthKeyValueParser();

    private AuthKeyValueParser() {
    }

    public String getContentType() {
        return "text/plain";
    }

    public <T> T parse(HttpResponse httpResponse, Class<T> cls) throws IOException {
        httpResponse.setContentLoggingLimit(0);
        InputStream content = httpResponse.getContent();
        try {
            T parse = parse(content, (Class) cls);
            return parse;
        } finally {
            content.close();
        }
    }

    public <T> T parse(InputStream inputStream, Class<T> cls) throws IOException {
        ClassInfo of = ClassInfo.of(cls);
        T newInstance = Types.newInstance(cls);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return newInstance;
            }
            int indexOf = readLine.indexOf(61);
            String substring = readLine.substring(0, indexOf);
            String substring2 = readLine.substring(indexOf + 1);
            Field field = of.getField(substring);
            if (field != null) {
                Object valueOf;
                Class type = field.getType();
                if (type == Boolean.TYPE || type == Boolean.class) {
                    valueOf = Boolean.valueOf(substring2);
                } else {
                    readLine = substring2;
                }
                FieldInfo.setFieldValue(field, newInstance, valueOf);
            } else if (GenericData.class.isAssignableFrom(cls)) {
                ((GenericData) newInstance).set(substring, substring2);
            } else if (Map.class.isAssignableFrom(cls)) {
                ((Map) newInstance).put(substring, substring2);
            }
        }
    }

    public <T> T parseAndClose(InputStream inputStream, Charset charset, Class<T> cls) throws IOException {
        return parseAndClose(new InputStreamReader(inputStream, charset), (Class) cls);
    }

    public Object parseAndClose(InputStream inputStream, Charset charset, Type type) {
        throw new UnsupportedOperationException("Type-based parsing is not yet supported -- use Class<T> instead");
    }

    public <T> T parseAndClose(Reader reader, Class<T> cls) throws IOException {
        try {
            ClassInfo of = ClassInfo.of(cls);
            T newInstance = Types.newInstance(cls);
            BufferedReader bufferedReader = new BufferedReader(reader);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                int indexOf = readLine.indexOf(61);
                String substring = readLine.substring(0, indexOf);
                String substring2 = readLine.substring(indexOf + 1);
                Field field = of.getField(substring);
                if (field != null) {
                    Object valueOf;
                    Class type = field.getType();
                    if (type == Boolean.TYPE || type == Boolean.class) {
                        valueOf = Boolean.valueOf(substring2);
                    } else {
                        readLine = substring2;
                    }
                    FieldInfo.setFieldValue(field, newInstance, valueOf);
                } else if (GenericData.class.isAssignableFrom(cls)) {
                    ((GenericData) newInstance).set(substring, substring2);
                } else if (Map.class.isAssignableFrom(cls)) {
                    ((Map) newInstance).put(substring, substring2);
                }
            }
            return newInstance;
        } finally {
            reader.close();
        }
    }

    public Object parseAndClose(Reader reader, Type type) {
        throw new UnsupportedOperationException("Type-based parsing is not yet supported -- use Class<T> instead");
    }
}
