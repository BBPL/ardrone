package com.google.api.client.http.xml.atom;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.AbstractAtomFeedParser;
import com.google.api.client.xml.atom.Atom;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class AtomFeedParser<T, E> extends AbstractAtomFeedParser<T> {
    private final Class<E> entryClass;

    public AtomFeedParser(XmlNamespaceDictionary xmlNamespaceDictionary, XmlPullParser xmlPullParser, InputStream inputStream, Class<T> cls, Class<E> cls2) {
        super(xmlNamespaceDictionary, xmlPullParser, inputStream, cls);
        this.entryClass = (Class) Preconditions.checkNotNull(cls2);
    }

    public static <T, E> AtomFeedParser<T, E> create(HttpResponse httpResponse, XmlNamespaceDictionary xmlNamespaceDictionary, Class<T> cls, Class<E> cls2) throws IOException, XmlPullParserException {
        InputStream content = httpResponse.getContent();
        try {
            Atom.checkContentType(httpResponse.getContentType());
            XmlPullParser createParser = Xml.createParser();
            createParser.setInput(content, null);
            return new AtomFeedParser(xmlNamespaceDictionary, createParser, content, cls, cls2);
        } catch (Throwable th) {
            if (content != null) {
                content.close();
            }
        }
    }

    public final Class<E> getEntryClass() {
        return this.entryClass;
    }

    protected Object parseEntryInternal() throws IOException, XmlPullParserException {
        Object newInstance = Types.newInstance(this.entryClass);
        Xml.parseElement(getParser(), newInstance, getNamespaceDictionary(), null);
        return newInstance;
    }

    public E parseNextEntry() throws IOException, XmlPullParserException {
        return super.parseNextEntry();
    }
}
