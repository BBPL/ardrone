package com.google.api.client.json;

import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.Data;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

public abstract class JsonGenerator {
    private void serialize(boolean z, Object obj) throws IOException {
        boolean z2 = true;
        if (obj != null) {
            Class cls = obj.getClass();
            if (Data.isNull(obj)) {
                writeNull();
            } else if (obj instanceof String) {
                writeString((String) obj);
            } else if (obj instanceof Number) {
                if (z) {
                    writeString(obj.toString());
                } else if (obj instanceof BigDecimal) {
                    writeNumber((BigDecimal) obj);
                } else if (obj instanceof BigInteger) {
                    writeNumber((BigInteger) obj);
                } else if (obj instanceof Long) {
                    writeNumber(((Long) obj).longValue());
                } else if (obj instanceof Float) {
                    float floatValue = ((Number) obj).floatValue();
                    boolean z3 = (Float.isInfinite(floatValue) || Float.isNaN(floatValue)) ? false : true;
                    Preconditions.checkArgument(z3);
                    writeNumber(floatValue);
                } else if ((obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte)) {
                    writeNumber(((Number) obj).intValue());
                } else {
                    double doubleValue = ((Number) obj).doubleValue();
                    if (Double.isInfinite(doubleValue) || Double.isNaN(doubleValue)) {
                        z2 = false;
                    }
                    Preconditions.checkArgument(z2);
                    writeNumber(doubleValue);
                }
            } else if (obj instanceof Boolean) {
                writeBoolean(((Boolean) obj).booleanValue());
            } else if (obj instanceof DateTime) {
                writeString(((DateTime) obj).toStringRfc3339());
            } else if ((obj instanceof Iterable) || cls.isArray()) {
                writeStartArray();
                for (Object serialize : Types.iterableOf(obj)) {
                    serialize(z, serialize);
                }
                writeEndArray();
            } else if (cls.isEnum()) {
                r0 = FieldInfo.of((Enum) obj).getName();
                if (r0 == null) {
                    writeNull();
                } else {
                    writeString(r0);
                }
            } else {
                writeStartObject();
                boolean z4 = (obj instanceof Map) && !(obj instanceof GenericData);
                ClassInfo of = z4 ? null : ClassInfo.of(cls);
                for (Entry entry : Data.mapOf(obj).entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        boolean z5;
                        r0 = (String) entry.getKey();
                        if (z4) {
                            z5 = z;
                        } else {
                            Field field = of.getField(r0);
                            z5 = (field == null || field.getAnnotation(JsonString.class) == null) ? false : true;
                        }
                        writeFieldName(r0);
                        serialize(z5, value);
                    }
                }
                writeEndObject();
            }
        }
    }

    public abstract void close() throws IOException;

    public void enablePrettyPrint() throws IOException {
    }

    public abstract void flush() throws IOException;

    public abstract JsonFactory getFactory();

    public final void serialize(Object obj) throws IOException {
        serialize(false, obj);
    }

    public abstract void writeBoolean(boolean z) throws IOException;

    public abstract void writeEndArray() throws IOException;

    public abstract void writeEndObject() throws IOException;

    public abstract void writeFieldName(String str) throws IOException;

    public abstract void writeNull() throws IOException;

    public abstract void writeNumber(double d) throws IOException;

    public abstract void writeNumber(float f) throws IOException;

    public abstract void writeNumber(int i) throws IOException;

    public abstract void writeNumber(long j) throws IOException;

    public abstract void writeNumber(String str) throws IOException;

    public abstract void writeNumber(BigDecimal bigDecimal) throws IOException;

    public abstract void writeNumber(BigInteger bigInteger) throws IOException;

    public abstract void writeStartArray() throws IOException;

    public abstract void writeStartObject() throws IOException;

    public abstract void writeString(String str) throws IOException;
}
