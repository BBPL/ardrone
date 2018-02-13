package com.google.api.client.xml.atom;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class AbstractAtomFeedParser<T> {
    private final Class<T> feedClass;
    private boolean feedParsed;
    private final InputStream inputStream;
    private final XmlNamespaceDictionary namespaceDictionary;
    private final XmlPullParser parser;

    protected AbstractAtomFeedParser(XmlNamespaceDictionary xmlNamespaceDictionary, XmlPullParser xmlPullParser, InputStream inputStream, Class<T> cls) {
        this.namespaceDictionary = (XmlNamespaceDictionary) Preconditions.checkNotNull(xmlNamespaceDictionary);
        this.parser = (XmlPullParser) Preconditions.checkNotNull(xmlPullParser);
        this.inputStream = (InputStream) Preconditions.checkNotNull(inputStream);
        this.feedClass = (Class) Preconditions.checkNotNull(cls);
    }

    public void close() throws IOException {
        this.inputStream.close();
    }

    public final Class<T> getFeedClass() {
        return this.feedClass;
    }

    public final InputStream getInputStream() {
        return this.inputStream;
    }

    public final XmlNamespaceDictionary getNamespaceDictionary() {
        return this.namespaceDictionary;
    }

    public final XmlPullParser getParser() {
        return this.parser;
    }

    protected abstract Object parseEntryInternal() throws IOException, XmlPullParserException;

    public T parseFeed() throws IOException, XmlPullParserException {
        try {
            this.feedParsed = true;
            T newInstance = Types.newInstance(this.feedClass);
            Xml.parseElement(this.parser, newInstance, this.namespaceDictionary, StopAtAtomEntry.INSTANCE);
            return newInstance;
        } catch (Throwable th) {
            close();
        }
    }

    public Object parseNextEntry() throws IOException, XmlPullParserException {
        if (!this.feedParsed) {
            this.feedParsed = true;
            Xml.parseElement(this.parser, null, this.namespaceDictionary, StopAtAtomEntry.INSTANCE);
        }
        try {
            if (this.parser.getEventType() != 2) {
                return null;
            }
            Object parseEntryInternal = parseEntryInternal();
            this.parser.next();
            return parseEntryInternal;
        } finally {
            close();
        }
    }
}
