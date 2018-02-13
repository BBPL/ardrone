package com.google.api.client.googleapis.xml.atom;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Types;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.AbstractAtomFeedParser;
import com.google.api.client.xml.atom.Atom;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class MultiKindFeedParser<T> extends AbstractAtomFeedParser<T> {
    private final HashMap<String, Class<?>> kindToEntryClassMap = new HashMap();

    MultiKindFeedParser(XmlNamespaceDictionary xmlNamespaceDictionary, XmlPullParser xmlPullParser, InputStream inputStream, Class<T> cls) {
        super(xmlNamespaceDictionary, xmlPullParser, inputStream, cls);
    }

    public static <T, E> MultiKindFeedParser<T> create(HttpResponse httpResponse, XmlNamespaceDictionary xmlNamespaceDictionary, Class<T> cls, Class<E>... clsArr) throws IOException, XmlPullParserException {
        InputStream content = httpResponse.getContent();
        try {
            Atom.checkContentType(httpResponse.getContentType());
            XmlPullParser createParser = Xml.createParser();
            createParser.setInput(content, null);
            MultiKindFeedParser<T> multiKindFeedParser = new MultiKindFeedParser(xmlNamespaceDictionary, createParser, content, cls);
            multiKindFeedParser.setEntryClasses(clsArr);
            return multiKindFeedParser;
        } finally {
            content.close();
        }
    }

    protected Object parseEntryInternal() throws IOException, XmlPullParserException {
        XmlPullParser parser = getParser();
        String attributeValue = parser.getAttributeValue(GoogleAtom.GD_NAMESPACE, "kind");
        Class cls = (Class) this.kindToEntryClassMap.get(attributeValue);
        if (cls == null) {
            throw new IllegalArgumentException("unrecognized kind: " + attributeValue);
        }
        Object newInstance = Types.newInstance(cls);
        Xml.parseElement(parser, newInstance, getNamespaceDictionary(), null);
        return newInstance;
    }

    public void setEntryClasses(Class<?>... clsArr) {
        HashMap hashMap = this.kindToEntryClassMap;
        for (Class cls : clsArr) {
            Field field = ClassInfo.of(cls).getField("@gd:kind");
            if (field == null) {
                throw new IllegalArgumentException("missing @gd:kind field for " + cls.getName());
            }
            String str = (String) FieldInfo.getFieldValue(field, Types.newInstance(cls));
            if (str == null) {
                throw new IllegalArgumentException("missing value for @gd:kind field in " + cls.getName());
            }
            hashMap.put(str, cls);
        }
    }
}
