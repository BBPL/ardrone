package com.google.api.client.xml;

import com.google.api.client.util.Data;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import org.mortbay.jetty.HttpVersions;
import org.xmlpull.v1.XmlSerializer;

public final class XmlNamespaceDictionary {
    private final HashMap<String, String> namespaceAliasToUriMap = new HashMap();
    private final HashMap<String, String> namespaceUriToAliasMap = new HashMap();

    class ElementSerializer {
        final List<String> attributeNames = new ArrayList();
        final List<Object> attributeValues = new ArrayList();
        private final boolean errorOnUnknown;
        final List<String> subElementNames = new ArrayList();
        final List<Object> subElementValues = new ArrayList();
        Object textValue = null;

        ElementSerializer(Object obj, boolean z) {
            this.errorOnUnknown = z;
            if (!Data.isPrimitive(obj.getClass()) || Data.isNull(obj)) {
                for (Entry entry : Data.mapOf(obj).entrySet()) {
                    Object value = entry.getValue();
                    if (!(value == null || Data.isNull(value))) {
                        String str = (String) entry.getKey();
                        if ("text()".equals(str)) {
                            this.textValue = value;
                        } else if (str.charAt(0) == '@') {
                            this.attributeNames.add(str.substring(1));
                            this.attributeValues.add(value);
                        } else {
                            this.subElementNames.add(str);
                            this.subElementValues.add(value);
                        }
                    }
                }
                return;
            }
            this.textValue = obj;
        }

        void serialize(XmlSerializer xmlSerializer, String str) throws IOException {
            String substring;
            String str2 = null;
            if (str != null) {
                int indexOf = str.indexOf(58);
                substring = str.substring(indexOf + 1);
                str2 = XmlNamespaceDictionary.this.getNamespaceUriForAliasHandlingUnknown(this.errorOnUnknown, indexOf == -1 ? HttpVersions.HTTP_0_9 : str.substring(0, indexOf));
            } else {
                substring = null;
            }
            serialize(xmlSerializer, str2, substring);
        }

        void serialize(XmlSerializer xmlSerializer, String str, String str2) throws IOException {
            int i;
            int i2 = 0;
            boolean z = this.errorOnUnknown;
            if (str2 == null) {
                if (z) {
                    throw new IllegalArgumentException("XML name not specified");
                }
                str2 = "unknownName";
            }
            xmlSerializer.startTag(str, str2);
            int size = this.attributeNames.size();
            for (i = 0; i < size; i++) {
                String str3 = (String) this.attributeNames.get(i);
                int indexOf = str3.indexOf(58);
                xmlSerializer.attribute(indexOf == -1 ? null : XmlNamespaceDictionary.this.getNamespaceUriForAliasHandlingUnknown(z, str3.substring(0, indexOf)), str3.substring(indexOf + 1), XmlNamespaceDictionary.toSerializedValue(this.attributeValues.get(i)));
            }
            if (this.textValue != null) {
                xmlSerializer.text(XmlNamespaceDictionary.toSerializedValue(this.textValue));
            }
            i = this.subElementNames.size();
            while (i2 < i) {
                Object obj = this.subElementValues.get(i2);
                str3 = (String) this.subElementNames.get(i2);
                Class cls = obj.getClass();
                if ((obj instanceof Iterable) || cls.isArray()) {
                    for (Object next : Types.iterableOf(obj)) {
                        if (!(next == null || Data.isNull(next))) {
                            new ElementSerializer(next, z).serialize(xmlSerializer, str3);
                        }
                    }
                } else {
                    new ElementSerializer(obj, z).serialize(xmlSerializer, str3);
                }
                i2++;
            }
            xmlSerializer.endTag(str, str2);
        }
    }

    private void computeAliases(Object obj, SortedSet<String> sortedSet) {
        for (Entry entry : Data.mapOf(obj).entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                String str = (String) entry.getKey();
                if (!"text()".equals(str)) {
                    Object obj2;
                    int indexOf = str.indexOf(58);
                    int i = str.charAt(0) == '@' ? 1 : 0;
                    if (indexOf != -1 || i == 0) {
                        if (indexOf == -1) {
                            obj2 = HttpVersions.HTTP_0_9;
                        } else {
                            obj2 = str.substring(str.charAt(0) == '@' ? 1 : 0, indexOf);
                        }
                        sortedSet.add(obj2);
                    }
                    obj2 = value.getClass();
                    if (i == 0 && !Data.isPrimitive(obj2)) {
                        if ((value instanceof Iterable) || obj2.isArray()) {
                            for (Object computeAliases : Types.iterableOf(value)) {
                                computeAliases(computeAliases, sortedSet);
                            }
                        } else {
                            computeAliases(value, sortedSet);
                        }
                    }
                }
            }
        }
    }

    private void serialize(XmlSerializer xmlSerializer, String str, Object obj, boolean z) throws IOException {
        String str2 = HttpVersions.HTTP_0_9;
        if (str != null) {
            int indexOf = str.indexOf(58);
            if (indexOf != -1) {
                str2 = str.substring(0, indexOf);
            }
        }
        startDoc(xmlSerializer, obj, z, str2).serialize(xmlSerializer, str);
        xmlSerializer.endDocument();
    }

    private void serialize(XmlSerializer xmlSerializer, String str, String str2, Object obj, boolean z) throws IOException {
        startDoc(xmlSerializer, obj, z, str == null ? null : getAliasForUri(str)).serialize(xmlSerializer, str, str2);
        xmlSerializer.endDocument();
    }

    private ElementSerializer startDoc(XmlSerializer xmlSerializer, Object obj, boolean z, String str) throws IOException {
        xmlSerializer.startDocument(null, null);
        SortedSet<String> treeSet = new TreeSet();
        computeAliases(obj, treeSet);
        if (str != null) {
            treeSet.add(str);
        }
        for (String str2 : treeSet) {
            xmlSerializer.setPrefix(str2, getNamespaceUriForAliasHandlingUnknown(z, str2));
        }
        return new ElementSerializer(obj, z);
    }

    static String toSerializedValue(Object obj) {
        if (obj instanceof Float) {
            Float f = (Float) obj;
            if (f.floatValue() == Float.POSITIVE_INFINITY) {
                return "INF";
            }
            if (f.floatValue() == Float.NEGATIVE_INFINITY) {
                return "-INF";
            }
        }
        if (obj instanceof Double) {
            Double d = (Double) obj;
            if (d.doubleValue() == Double.POSITIVE_INFINITY) {
                return "INF";
            }
            if (d.doubleValue() == Double.NEGATIVE_INFINITY) {
                return "-INF";
            }
        }
        if ((obj instanceof String) || (obj instanceof Number) || (obj instanceof Boolean)) {
            return obj.toString();
        }
        if (obj instanceof DateTime) {
            return ((DateTime) obj).toStringRfc3339();
        }
        if (obj instanceof Enum) {
            return FieldInfo.of((Enum) obj).getName();
        }
        throw new IllegalArgumentException("unrecognized value type: " + obj.getClass());
    }

    public String getAliasForUri(String str) {
        String str2;
        synchronized (this) {
            str2 = (String) this.namespaceUriToAliasMap.get(Preconditions.checkNotNull(str));
        }
        return str2;
    }

    public Map<String, String> getAliasToUriMap() {
        Map<String, String> unmodifiableMap;
        synchronized (this) {
            unmodifiableMap = Collections.unmodifiableMap(this.namespaceAliasToUriMap);
        }
        return unmodifiableMap;
    }

    String getNamespaceAliasForUriErrorOnUnknown(String str) {
        String aliasForUri = getAliasForUri(str);
        Preconditions.checkArgument(aliasForUri != null, "invalid XML: no alias declared for namesapce <%s>; work-around by setting XML namepace directly by calling the set method of %s", str, XmlNamespaceDictionary.class.getName());
        return aliasForUri;
    }

    String getNamespaceUriForAliasHandlingUnknown(boolean z, String str) {
        String uriForAlias = getUriForAlias(str);
        if (uriForAlias != null) {
            return uriForAlias;
        }
        boolean z2 = !z;
        String str2 = str.length() == 0 ? "(default)" : str;
        Preconditions.checkArgument(z2, "unrecognized alias: %s", str2);
        return "http://unknown/" + str;
    }

    public String getUriForAlias(String str) {
        String str2;
        synchronized (this) {
            str2 = (String) this.namespaceAliasToUriMap.get(Preconditions.checkNotNull(str));
        }
        return str2;
    }

    public Map<String, String> getUriToAliasMap() {
        Map<String, String> unmodifiableMap;
        synchronized (this) {
            unmodifiableMap = Collections.unmodifiableMap(this.namespaceUriToAliasMap);
        }
        return unmodifiableMap;
    }

    public void serialize(XmlSerializer xmlSerializer, String str, Object obj) throws IOException {
        serialize(xmlSerializer, str, obj, true);
    }

    public void serialize(XmlSerializer xmlSerializer, String str, String str2, Object obj) throws IOException {
        serialize(xmlSerializer, str, str2, obj, true);
    }

    public XmlNamespaceDictionary set(String str, String str2) {
        Object obj = null;
        synchronized (this) {
            Object obj2;
            if (str2 == null) {
                if (str != null) {
                    obj = (String) this.namespaceAliasToUriMap.remove(str);
                    obj2 = null;
                } else {
                    obj2 = null;
                }
            } else if (str == null) {
                r0 = (String) this.namespaceUriToAliasMap.remove(str2);
            } else {
                r0 = (String) this.namespaceAliasToUriMap.put(Preconditions.checkNotNull(str), Preconditions.checkNotNull(str2));
                if (str2.equals(r0)) {
                    obj2 = null;
                } else {
                    String str3 = r0;
                    r0 = (String) this.namespaceUriToAliasMap.put(str2, str);
                }
            }
            if (obj != null) {
                this.namespaceUriToAliasMap.remove(obj);
            }
            if (obj2 != null) {
                this.namespaceAliasToUriMap.remove(obj2);
            }
        }
        return this;
    }

    public String toStringOf(String str, Object obj) {
        try {
            Writer stringWriter = new StringWriter();
            XmlSerializer createSerializer = Xml.createSerializer();
            createSerializer.setOutput(stringWriter);
            serialize(createSerializer, str, obj, false);
            return stringWriter.toString();
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }
}
