package com.google.api.client.json;

import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class JsonParser {
    private void parse(ArrayList<Type> arrayList, Object obj, CustomizeJsonParser customizeJsonParser) throws IOException {
        if (obj instanceof GenericJson) {
            ((GenericJson) obj).setFactory(getFactory());
        }
        JsonToken startParsingObjectOrArray = startParsingObjectOrArray();
        Type type = obj.getClass();
        ClassInfo of = ClassInfo.of(type);
        boolean isAssignableFrom = GenericData.class.isAssignableFrom(type);
        if (isAssignableFrom || !Map.class.isAssignableFrom(type)) {
            while (startParsingObjectOrArray == JsonToken.FIELD_NAME) {
                String text = getText();
                nextToken();
                if (customizeJsonParser == null || !customizeJsonParser.stopAt(obj, text)) {
                    FieldInfo fieldInfo = of.getFieldInfo(text);
                    if (fieldInfo != null) {
                        if (!fieldInfo.isFinal() || fieldInfo.isPrimitive()) {
                            Field field = fieldInfo.getField();
                            int size = arrayList.size();
                            arrayList.add(field.getGenericType());
                            Object parseValue = parseValue(field, fieldInfo.getGenericType(), arrayList, obj, customizeJsonParser);
                            arrayList.remove(size);
                            fieldInfo.setValue(obj, parseValue);
                        } else {
                            throw new IllegalArgumentException("final array/object fields are not supported");
                        }
                    } else if (isAssignableFrom) {
                        ((GenericData) obj).set(text, parseValue(null, null, arrayList, obj, customizeJsonParser));
                    } else {
                        if (customizeJsonParser != null) {
                            customizeJsonParser.handleUnrecognizedKey(obj, text);
                        }
                        skipChildren();
                    }
                    startParsingObjectOrArray = nextToken();
                } else {
                    return;
                }
            }
            return;
        }
        parseMap(null, (Map) obj, Types.getMapValueParameter(type), arrayList, customizeJsonParser);
    }

    private <T> void parseArray(Field field, Collection<T> collection, Type type, ArrayList<Type> arrayList, CustomizeJsonParser customizeJsonParser) throws IOException {
        JsonToken startParsingObjectOrArray = startParsingObjectOrArray();
        while (startParsingObjectOrArray != JsonToken.END_ARRAY) {
            collection.add(parseValue(field, type, arrayList, collection, customizeJsonParser));
            startParsingObjectOrArray = nextToken();
        }
    }

    private void parseMap(Field field, Map<String, Object> map, Type type, ArrayList<Type> arrayList, CustomizeJsonParser customizeJsonParser) throws IOException {
        JsonToken startParsingObjectOrArray = startParsingObjectOrArray();
        while (startParsingObjectOrArray == JsonToken.FIELD_NAME) {
            String text = getText();
            nextToken();
            if (customizeJsonParser == null || !customizeJsonParser.stopAt(map, text)) {
                map.put(text, parseValue(field, type, arrayList, map, customizeJsonParser));
                startParsingObjectOrArray = nextToken();
            } else {
                return;
            }
        }
    }

    private final Object parseValue(Field field, Type type, ArrayList<Type> arrayList, Object obj, CustomizeJsonParser customizeJsonParser) throws IOException {
        Type type2 = null;
        int i = 1;
        Type resolveWildcardTypeOrTypeVariable = Data.resolveWildcardTypeOrTypeVariable(arrayList, type);
        Class cls = resolveWildcardTypeOrTypeVariable instanceof Class ? (Class) resolveWildcardTypeOrTypeVariable : null;
        if (resolveWildcardTypeOrTypeVariable instanceof ParameterizedType) {
            cls = Types.getRawClass((ParameterizedType) resolveWildcardTypeOrTypeVariable);
        }
        JsonToken currentToken = getCurrentToken();
        String currentName = getCurrentName();
        StringBuilder stringBuilder = new StringBuilder();
        if (!(currentName == null && field == null)) {
            stringBuilder.append(" [");
            if (currentName != null) {
                stringBuilder.append("key ").append(currentName);
            }
            if (field != null) {
                if (currentName != null) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append("field ").append(field);
            }
            stringBuilder.append("]");
        }
        currentName = stringBuilder.toString();
        boolean z;
        boolean z2;
        switch (currentToken) {
            case START_OBJECT:
            case FIELD_NAME:
            case END_OBJECT:
                Preconditions.checkArgument(!Types.isArray(resolveWildcardTypeOrTypeVariable), "expected object or map type but got %s%s", resolveWildcardTypeOrTypeVariable, currentName);
                Object newInstanceForObject = (cls == null || customizeJsonParser == null) ? null : customizeJsonParser.newInstanceForObject(obj, cls);
                if (cls == null || !Types.isAssignableToOrFrom(cls, Map.class)) {
                    i = 0;
                }
                if (newInstanceForObject == null) {
                    newInstanceForObject = (i != 0 || cls == null) ? Data.newMapInstance(cls) : Types.newInstance(cls);
                }
                int size = arrayList.size();
                if (resolveWildcardTypeOrTypeVariable != null) {
                    arrayList.add(resolveWildcardTypeOrTypeVariable);
                }
                if (!(i == 0 || GenericData.class.isAssignableFrom(cls))) {
                    if (Map.class.isAssignableFrom(cls)) {
                        type2 = Types.getMapValueParameter(resolveWildcardTypeOrTypeVariable);
                    }
                    if (type2 != null) {
                        parseMap(field, (Map) newInstanceForObject, type2, arrayList, customizeJsonParser);
                        return newInstanceForObject;
                    }
                }
                parse((ArrayList) arrayList, newInstanceForObject, customizeJsonParser);
                if (resolveWildcardTypeOrTypeVariable == null) {
                    return newInstanceForObject;
                }
                arrayList.remove(size);
                return newInstanceForObject;
            case START_ARRAY:
            case END_ARRAY:
                boolean isArray = Types.isArray(resolveWildcardTypeOrTypeVariable);
                z = resolveWildcardTypeOrTypeVariable == null || isArray || (cls != null && Types.isAssignableToOrFrom(cls, Collection.class));
                Preconditions.checkArgument(z, "expected collection or array type but got %s%s", resolveWildcardTypeOrTypeVariable, currentName);
                Collection newInstanceForArray = (customizeJsonParser == null || field == null) ? null : customizeJsonParser.newInstanceForArray(obj, field);
                if (newInstanceForArray == null) {
                    newInstanceForArray = Data.newCollectionInstance(resolveWildcardTypeOrTypeVariable);
                }
                if (isArray) {
                    type2 = Types.getArrayComponentType(resolveWildcardTypeOrTypeVariable);
                } else if (cls != null && Iterable.class.isAssignableFrom(cls)) {
                    type2 = Types.getIterableParameter(resolveWildcardTypeOrTypeVariable);
                }
                type2 = Data.resolveWildcardTypeOrTypeVariable(arrayList, type2);
                parseArray(field, newInstanceForArray, type2, arrayList, customizeJsonParser);
                return isArray ? Types.toArray(newInstanceForArray, Types.getRawArrayComponentType(arrayList, type2)) : newInstanceForArray;
            case VALUE_TRUE:
            case VALUE_FALSE:
                z2 = resolveWildcardTypeOrTypeVariable == null || cls == Boolean.TYPE || (cls != null && cls.isAssignableFrom(Boolean.class));
                Preconditions.checkArgument(z2, "expected type Boolean or boolean but got %s%s", resolveWildcardTypeOrTypeVariable, currentName);
                return currentToken == JsonToken.VALUE_TRUE ? Boolean.TRUE : Boolean.FALSE;
            case VALUE_NUMBER_FLOAT:
            case VALUE_NUMBER_INT:
                z = field == null || field.getAnnotation(JsonString.class) == null;
                Preconditions.checkArgument(z, "number type formatted as a JSON number cannot use @JsonString annotation%s", currentName);
                if (cls == null || cls.isAssignableFrom(BigDecimal.class)) {
                    return getDecimalValue();
                }
                if (cls == BigInteger.class) {
                    return getBigIntegerValue();
                }
                if (cls == Double.class || cls == Double.TYPE) {
                    return Double.valueOf(getDoubleValue());
                }
                if (cls == Long.class || cls == Long.TYPE) {
                    return Long.valueOf(getLongValue());
                }
                if (cls == Float.class || cls == Float.TYPE) {
                    return Float.valueOf(getFloatValue());
                }
                if (cls == Integer.class || cls == Integer.TYPE) {
                    return Integer.valueOf(getIntValue());
                }
                if (cls == Short.class || cls == Short.TYPE) {
                    return Short.valueOf(getShortValue());
                }
                if (cls == Byte.class || cls == Byte.TYPE) {
                    return Byte.valueOf(getByteValue());
                }
                throw new IllegalArgumentException("expected numeric type but got " + resolveWildcardTypeOrTypeVariable + currentName);
            case VALUE_STRING:
                z2 = (cls != null && Number.class.isAssignableFrom(cls) && (field == null || field.getAnnotation(JsonString.class) == null)) ? false : true;
                Preconditions.checkArgument(z2, "number field formatted as a JSON string must use the @JsonString annotation%s", currentName);
                try {
                    return Data.parsePrimitiveValue(resolveWildcardTypeOrTypeVariable, getText());
                } catch (Throwable e) {
                    throw new IllegalArgumentException(currentName, e);
                }
            case VALUE_NULL:
                z = cls == null || !cls.isPrimitive();
                Preconditions.checkArgument(z, "primitive number field but found a JSON null%s", currentName);
                if (!(cls == null || (cls.getModifiers() & 1536) == 0)) {
                    if (Types.isAssignableToOrFrom(cls, Collection.class)) {
                        return Data.nullOf(Data.newCollectionInstance(resolveWildcardTypeOrTypeVariable).getClass());
                    }
                    if (Types.isAssignableToOrFrom(cls, Map.class)) {
                        return Data.nullOf(Data.newMapInstance(cls).getClass());
                    }
                }
                return Data.nullOf(Types.getRawArrayComponentType(arrayList, resolveWildcardTypeOrTypeVariable));
            default:
                throw new IllegalArgumentException("unexpected JSON node type: " + currentToken + currentName);
        }
    }

    private JsonToken startParsing() throws IOException {
        JsonToken currentToken = getCurrentToken();
        JsonToken nextToken = currentToken == null ? nextToken() : currentToken;
        Preconditions.checkArgument(nextToken != null, "no JSON input found");
        return nextToken;
    }

    private JsonToken startParsingObjectOrArray() throws IOException {
        JsonToken startParsing = startParsing();
        switch (startParsing) {
            case START_OBJECT:
                JsonToken nextToken = nextToken();
                boolean z = nextToken == JsonToken.FIELD_NAME || nextToken == JsonToken.END_OBJECT;
                Preconditions.checkArgument(z, nextToken);
                return nextToken;
            case START_ARRAY:
                return nextToken();
            default:
                return startParsing;
        }
    }

    public abstract void close() throws IOException;

    public abstract BigInteger getBigIntegerValue() throws IOException;

    public abstract byte getByteValue() throws IOException;

    public abstract String getCurrentName() throws IOException;

    public abstract JsonToken getCurrentToken();

    public abstract BigDecimal getDecimalValue() throws IOException;

    public abstract double getDoubleValue() throws IOException;

    public abstract JsonFactory getFactory();

    public abstract float getFloatValue() throws IOException;

    public abstract int getIntValue() throws IOException;

    public abstract long getLongValue() throws IOException;

    public abstract short getShortValue() throws IOException;

    public abstract String getText() throws IOException;

    public abstract JsonToken nextToken() throws IOException;

    public final <T> T parse(Class<T> cls, CustomizeJsonParser customizeJsonParser) throws IOException {
        startParsing();
        return parse((Type) cls, false, customizeJsonParser);
    }

    public Object parse(Type type, boolean z, CustomizeJsonParser customizeJsonParser) throws IOException {
        try {
            startParsing();
            Object parseValue = parseValue(null, type, new ArrayList(), null, customizeJsonParser);
            return parseValue;
        } finally {
            if (z) {
                close();
            }
        }
    }

    public final void parse(Object obj, CustomizeJsonParser customizeJsonParser) throws IOException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(obj.getClass());
        parse(arrayList, obj, customizeJsonParser);
    }

    public final <T> T parseAndClose(Class<T> cls, CustomizeJsonParser customizeJsonParser) throws IOException {
        try {
            T parse = parse((Class) cls, customizeJsonParser);
            return parse;
        } finally {
            close();
        }
    }

    public final void parseAndClose(Object obj, CustomizeJsonParser customizeJsonParser) throws IOException {
        try {
            parse(obj, customizeJsonParser);
        } finally {
            close();
        }
    }

    public final <T> Collection<T> parseArray(Class<?> cls, Class<T> cls2, CustomizeJsonParser customizeJsonParser) throws IOException {
        Collection newCollectionInstance = Data.newCollectionInstance(cls);
        parseArray(newCollectionInstance, (Class) cls2, customizeJsonParser);
        return newCollectionInstance;
    }

    public final <T> void parseArray(Collection<? super T> collection, Class<T> cls, CustomizeJsonParser customizeJsonParser) throws IOException {
        parseArray(null, collection, cls, new ArrayList(), customizeJsonParser);
    }

    public final <T> Collection<T> parseArrayAndClose(Class<?> cls, Class<T> cls2, CustomizeJsonParser customizeJsonParser) throws IOException {
        try {
            Collection<T> parseArray = parseArray((Class) cls, (Class) cls2, customizeJsonParser);
            return parseArray;
        } finally {
            close();
        }
    }

    public final <T> void parseArrayAndClose(Collection<? super T> collection, Class<T> cls, CustomizeJsonParser customizeJsonParser) throws IOException {
        try {
            parseArray((Collection) collection, (Class) cls, customizeJsonParser);
        } finally {
            close();
        }
    }

    public abstract JsonParser skipChildren() throws IOException;

    public final String skipToKey(Set<String> set) throws IOException {
        JsonToken startParsingObjectOrArray = startParsingObjectOrArray();
        while (startParsingObjectOrArray == JsonToken.FIELD_NAME) {
            String text = getText();
            nextToken();
            if (set.contains(text)) {
                return text;
            }
            skipChildren();
            startParsingObjectOrArray = nextToken();
        }
        return null;
    }

    public final void skipToKey(String str) throws IOException {
        skipToKey(Collections.singleton(str));
    }
}
