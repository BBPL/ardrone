package com.google.api.client.xml;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.ArrayValueMap;
import com.google.api.client.util.Charsets;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.mortbay.jetty.HttpVersions;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class Xml {
    public static final String MEDIA_TYPE = new HttpMediaType("application/xml").setCharsetParameter(Charsets.UTF_8).build();
    static final String TEXT_CONTENT = "text()";
    private static XmlPullParserFactory factory;

    public static class CustomizeParser {
        public boolean stopAfterEndTag(String str, String str2) {
            return false;
        }

        public boolean stopBeforeStartTag(String str, String str2) {
            return false;
        }
    }

    private Xml() {
    }

    public static XmlPullParser createParser() throws XmlPullParserException {
        return getParserFactory().newPullParser();
    }

    public static XmlSerializer createSerializer() {
        try {
            return getParserFactory().newSerializer();
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String getFieldName(boolean z, String str, String str2, String str3) {
        if (!z && str.length() == 0) {
            return str3;
        }
        StringBuilder stringBuilder = new StringBuilder((str.length() + 2) + str3.length());
        if (z) {
            stringBuilder.append('@');
        }
        if (str.length() != 0) {
            stringBuilder.append(str).append(':');
        }
        return stringBuilder.append(str3).toString();
    }

    private static XmlPullParserFactory getParserFactory() throws XmlPullParserException {
        synchronized (Xml.class) {
            Class cls;
            try {
                if (factory == null) {
                    factory = XmlPullParserFactory.newInstance(System.getProperty("org.xmlpull.v1.XmlPullParserFactory"), null);
                    cls = true;
                    factory.setNamespaceAware(true);
                }
                XmlPullParserFactory xmlPullParserFactory = factory;
                return xmlPullParserFactory;
            } finally {
                cls = Xml.class;
            }
        }
    }

    private static void parseAttributeOrTextContent(String str, Field field, Type type, List<Type> list, Object obj, GenericXml genericXml, Map<String, Object> map, String str2) {
        if (field != null || genericXml != null || map != null) {
            if (field != null) {
                type = field.getGenericType();
            }
            setValue(parseValue(type, list, str), field, obj, genericXml, map, str2);
        }
    }

    public static void parseElement(XmlPullParser xmlPullParser, Object obj, XmlNamespaceDictionary xmlNamespaceDictionary, CustomizeParser customizeParser) throws IOException, XmlPullParserException {
        ArrayList arrayList = new ArrayList();
        if (obj != null) {
            arrayList.add(obj.getClass());
        }
        parseElementInternal(xmlPullParser, arrayList, obj, null, xmlNamespaceDictionary, customizeParser);
    }

    private static boolean parseElementInternal(XmlPullParser xmlPullParser, ArrayList<Type> arrayList, Object obj, Type type, XmlNamespaceDictionary xmlNamespaceDictionary, CustomizeParser customizeParser) throws IOException, XmlPullParserException {
        String name;
        String namespaceAliasForUriErrorOnUnknown;
        int attributeCount;
        GenericXml genericXml = obj instanceof GenericXml ? (GenericXml) obj : null;
        Map map = (genericXml == null && (obj instanceof Map)) ? (Map) Map.class.cast(obj) : null;
        ClassInfo of = (map != null || obj == null) ? null : ClassInfo.of(obj.getClass());
        if (xmlPullParser.getEventType() == 0) {
            xmlPullParser.next();
        }
        parseNamespacesForElement(xmlPullParser, xmlNamespaceDictionary);
        if (genericXml != null) {
            genericXml.namespaceDictionary = xmlNamespaceDictionary;
            name = xmlPullParser.getName();
            namespaceAliasForUriErrorOnUnknown = xmlNamespaceDictionary.getNamespaceAliasForUriErrorOnUnknown(xmlPullParser.getNamespace());
            if (namespaceAliasForUriErrorOnUnknown.length() != 0) {
                name = namespaceAliasForUriErrorOnUnknown + ":" + name;
            }
            genericXml.name = name;
        }
        if (obj != null) {
            attributeCount = xmlPullParser.getAttributeCount();
            for (int i = 0; i < attributeCount; i++) {
                namespaceAliasForUriErrorOnUnknown = xmlPullParser.getAttributeName(i);
                String attributeNamespace = xmlPullParser.getAttributeNamespace(i);
                String fieldName = getFieldName(true, attributeNamespace.length() == 0 ? HttpVersions.HTTP_0_9 : xmlNamespaceDictionary.getNamespaceAliasForUriErrorOnUnknown(attributeNamespace), attributeNamespace, namespaceAliasForUriErrorOnUnknown);
                parseAttributeOrTextContent(xmlPullParser.getAttributeValue(i), of == null ? null : of.getField(fieldName), type, arrayList, obj, genericXml, map, fieldName);
            }
        }
        ArrayValueMap arrayValueMap = new ArrayValueMap(obj);
        boolean z = false;
        while (true) {
            boolean z2;
            switch (xmlPullParser.next()) {
                case 1:
                    z2 = true;
                    break;
                case 2:
                    if (customizeParser != null) {
                        if (customizeParser.stopBeforeStartTag(xmlPullParser.getNamespace(), xmlPullParser.getName())) {
                            z2 = true;
                            break;
                        }
                    }
                    if (obj == null) {
                        parseTextContentForElement(xmlPullParser, arrayList, true, null);
                        z2 = z;
                    } else {
                        parseNamespacesForElement(xmlPullParser, xmlNamespaceDictionary);
                        name = xmlPullParser.getNamespace();
                        fieldName = getFieldName(false, xmlNamespaceDictionary.getNamespaceAliasForUriErrorOnUnknown(name), name, xmlPullParser.getName());
                        Field field = of == null ? null : of.getField(fieldName);
                        Type resolveWildcardTypeOrTypeVariable = Data.resolveWildcardTypeOrTypeVariable(arrayList, field == null ? type : field.getGenericType());
                        Class cls = resolveWildcardTypeOrTypeVariable instanceof Class ? (Class) resolveWildcardTypeOrTypeVariable : null;
                        if (resolveWildcardTypeOrTypeVariable instanceof ParameterizedType) {
                            cls = Types.getRawClass((ParameterizedType) resolveWildcardTypeOrTypeVariable);
                        }
                        boolean isArray = Types.isArray(resolveWildcardTypeOrTypeVariable);
                        Object obj2 = (field == null && map == null && genericXml == null) ? 1 : null;
                        if (obj2 != null || Data.isPrimitive(resolveWildcardTypeOrTypeVariable)) {
                            attributeCount = 1;
                            while (attributeCount != 0) {
                                switch (xmlPullParser.next()) {
                                    case 1:
                                        z2 = true;
                                        break;
                                    case 2:
                                        attributeCount++;
                                        break;
                                    case 3:
                                        attributeCount--;
                                        break;
                                    case 4:
                                        if (obj2 == null && attributeCount == 1) {
                                            parseAttributeOrTextContent(xmlPullParser.getText(), field, type, arrayList, obj, genericXml, map, fieldName);
                                            break;
                                        }
                                    default:
                                        break;
                                }
                            }
                            z2 = z;
                        } else if (resolveWildcardTypeOrTypeVariable == null || (cls != null && Types.isAssignableToOrFrom(cls, Map.class))) {
                            Map newMapInstance = Data.newMapInstance(cls);
                            int size = arrayList.size();
                            if (resolveWildcardTypeOrTypeVariable != null) {
                                arrayList.add(resolveWildcardTypeOrTypeVariable);
                            }
                            r5 = (resolveWildcardTypeOrTypeVariable == null || !Map.class.isAssignableFrom(cls)) ? null : Types.getMapValueParameter(resolveWildcardTypeOrTypeVariable);
                            z = parseElementInternal(xmlPullParser, arrayList, newMapInstance, Data.resolveWildcardTypeOrTypeVariable(arrayList, r5), xmlNamespaceDictionary, customizeParser);
                            if (resolveWildcardTypeOrTypeVariable != null) {
                                arrayList.remove(size);
                            }
                            if (map != null) {
                                r2 = (Collection) map.get(fieldName);
                                if (r2 == null) {
                                    r2 = new ArrayList(1);
                                    map.put(fieldName, r2);
                                }
                                r2.add(newMapInstance);
                                z2 = z;
                            } else if (field != null) {
                                FieldInfo of2 = FieldInfo.of(field);
                                if (cls == Object.class) {
                                    r2 = (Collection) of2.getValue(obj);
                                    if (r2 == null) {
                                        r2 = new ArrayList(1);
                                        of2.setValue(obj, r2);
                                    }
                                    r2.add(newMapInstance);
                                    z2 = z;
                                } else {
                                    of2.setValue(obj, newMapInstance);
                                    z2 = z;
                                }
                            } else {
                                GenericXml genericXml2 = (GenericXml) obj;
                                Collection collection = (Collection) genericXml2.get(fieldName);
                                if (collection == null) {
                                    collection = new ArrayList(1);
                                    genericXml2.set(fieldName, (Object) collection);
                                }
                                collection.add(newMapInstance);
                                z2 = z;
                            }
                        } else if (isArray || Types.isAssignableToOrFrom(cls, Collection.class)) {
                            FieldInfo of3 = FieldInfo.of(field);
                            Type arrayComponentType = isArray ? Types.getArrayComponentType(resolveWildcardTypeOrTypeVariable) : Types.getIterableParameter(resolveWildcardTypeOrTypeVariable);
                            Class rawArrayComponentType = Types.getRawArrayComponentType(arrayList, arrayComponentType);
                            r5 = Data.resolveWildcardTypeOrTypeVariable(arrayList, arrayComponentType);
                            cls = r5 instanceof Class ? (Class) r5 : null;
                            if (r5 instanceof ParameterizedType) {
                                cls = Types.getRawClass((ParameterizedType) r5);
                            }
                            if (Data.isPrimitive(r5)) {
                                obj2 = parseTextContentForElement(xmlPullParser, arrayList, false, r5);
                            } else if (r5 == null || (cls != null && Types.isAssignableToOrFrom(cls, Map.class))) {
                                obj2 = Data.newMapInstance(cls);
                                int size2 = arrayList.size();
                                if (r5 != null) {
                                    arrayList.add(r5);
                                }
                                arrayComponentType = (r5 == null || !Map.class.isAssignableFrom(cls)) ? null : Types.getMapValueParameter(r5);
                                z = parseElementInternal(xmlPullParser, arrayList, obj2, Data.resolveWildcardTypeOrTypeVariable(arrayList, arrayComponentType), xmlNamespaceDictionary, customizeParser);
                                if (r5 != null) {
                                    arrayList.remove(size2);
                                }
                            } else {
                                obj2 = Types.newInstance(rawArrayComponentType);
                                r2 = arrayList.size();
                                arrayList.add(resolveWildcardTypeOrTypeVariable);
                                z = parseElementInternal(xmlPullParser, arrayList, obj2, null, xmlNamespaceDictionary, customizeParser);
                                arrayList.remove(r2);
                            }
                            if (!isArray) {
                                Collection newCollectionInstance;
                                r2 = (Collection) (field == null ? map.get(fieldName) : of3.getValue(obj));
                                if (r2 == null) {
                                    newCollectionInstance = Data.newCollectionInstance(resolveWildcardTypeOrTypeVariable);
                                    setValue(newCollectionInstance, field, obj, genericXml, map, fieldName);
                                } else {
                                    newCollectionInstance = r2;
                                }
                                newCollectionInstance.add(obj2);
                                z2 = z;
                            } else if (field == null) {
                                arrayValueMap.put(fieldName, rawArrayComponentType, obj2);
                                z2 = z;
                            } else {
                                arrayValueMap.put(field, rawArrayComponentType, obj2);
                                z2 = z;
                            }
                        } else {
                            obj2 = Types.newInstance(cls);
                            r2 = arrayList.size();
                            arrayList.add(resolveWildcardTypeOrTypeVariable);
                            z = parseElementInternal(xmlPullParser, arrayList, obj2, null, xmlNamespaceDictionary, customizeParser);
                            arrayList.remove(r2);
                            setValue(obj2, field, obj, genericXml, map, fieldName);
                            z2 = z;
                        }
                    }
                    if (z2 || xmlPullParser.getEventType() == 1) {
                        z2 = true;
                        break;
                    }
                    z = z2;
                    continue;
                    break;
                case 3:
                    if (customizeParser != null) {
                        if (customizeParser.stopAfterEndTag(xmlPullParser.getNamespace(), xmlPullParser.getName())) {
                            z2 = true;
                            break;
                        }
                    }
                    z2 = false;
                    break;
                case 4:
                    if (obj != null) {
                        parseAttributeOrTextContent(xmlPullParser.getText(), of == null ? null : of.getField(TEXT_CONTENT), type, arrayList, obj, genericXml, map, TEXT_CONTENT);
                        break;
                    }
                    continue;
                default:
                    continue;
            }
            arrayValueMap.setValues();
            return z2;
        }
    }

    private static void parseNamespacesForElement(XmlPullParser xmlPullParser, XmlNamespaceDictionary xmlNamespaceDictionary) throws XmlPullParserException {
        Preconditions.checkState(xmlPullParser.getEventType() == 2, "expected start of XML element, but got something else (event type %s)", Integer.valueOf(xmlPullParser.getEventType()));
        int depth = xmlPullParser.getDepth();
        int namespaceCount = xmlPullParser.getNamespaceCount(depth - 1);
        int namespaceCount2 = xmlPullParser.getNamespaceCount(depth);
        for (int i = namespaceCount; i < namespaceCount2; i++) {
            String namespaceUri = xmlPullParser.getNamespaceUri(i);
            if (xmlNamespaceDictionary.getAliasForUri(namespaceUri) == null) {
                String namespacePrefix = xmlPullParser.getNamespacePrefix(i);
                if (namespacePrefix == null) {
                    namespacePrefix = HttpVersions.HTTP_0_9;
                }
                String str = namespacePrefix;
                int i2 = 1;
                while (xmlNamespaceDictionary.getUriForAlias(str) != null) {
                    i2++;
                    str = namespacePrefix + i2;
                }
                xmlNamespaceDictionary.set(str, namespaceUri);
            }
        }
    }

    private static Object parseTextContentForElement(XmlPullParser xmlPullParser, List<Type> list, boolean z, Type type) throws XmlPullParserException, IOException {
        Object obj = null;
        int i = 1;
        while (i != 0) {
            switch (xmlPullParser.next()) {
                case 1:
                    i = 0;
                    break;
                case 2:
                    i++;
                    break;
                case 3:
                    i--;
                    break;
                case 4:
                    if (!z && i == 1) {
                        obj = parseValue(type, list, xmlPullParser.getText());
                        break;
                    }
                default:
                    break;
            }
        }
        return obj;
    }

    private static Object parseValue(Type type, List<Type> list, String str) {
        Type resolveWildcardTypeOrTypeVariable = Data.resolveWildcardTypeOrTypeVariable(list, type);
        if (resolveWildcardTypeOrTypeVariable == Double.class || resolveWildcardTypeOrTypeVariable == Double.TYPE) {
            if (str.equals("INF")) {
                return new Double(Double.POSITIVE_INFINITY);
            }
            if (str.equals("-INF")) {
                return new Double(Double.NEGATIVE_INFINITY);
            }
        }
        if (resolveWildcardTypeOrTypeVariable == Float.class || resolveWildcardTypeOrTypeVariable == Float.TYPE) {
            if (str.equals("INF")) {
                return Float.valueOf(Float.POSITIVE_INFINITY);
            }
            if (str.equals("-INF")) {
                return Float.valueOf(Float.NEGATIVE_INFINITY);
            }
        }
        return Data.parsePrimitiveValue(resolveWildcardTypeOrTypeVariable, str);
    }

    private static void setValue(Object obj, Field field, Object obj2, GenericXml genericXml, Map<String, Object> map, String str) {
        if (field != null) {
            FieldInfo.setFieldValue(field, obj2, obj);
        } else if (genericXml != null) {
            genericXml.set(str, obj);
        } else {
            map.put(str, obj);
        }
    }

    public static String toStringOf(Object obj) {
        return new XmlNamespaceDictionary().toStringOf(null, obj);
    }
}
