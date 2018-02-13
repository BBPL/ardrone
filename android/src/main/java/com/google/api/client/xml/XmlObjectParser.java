package com.google.api.client.xml;

import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Types;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlObjectParser implements ObjectParser {
    private final XmlNamespaceDictionary namespaceDictionary;

    public XmlObjectParser(XmlNamespaceDictionary xmlNamespaceDictionary) {
        this.namespaceDictionary = (XmlNamespaceDictionary) Preconditions.checkNotNull(xmlNamespaceDictionary);
    }

    private Object readObject(XmlPullParser xmlPullParser, Type type) throws XmlPullParserException, IOException {
        Preconditions.checkArgument(type instanceof Class, "dataType has to be of Class<?>");
        Object newInstance = Types.newInstance((Class) type);
        Xml.parseElement(xmlPullParser, newInstance, this.namespaceDictionary, null);
        return newInstance;
    }

    public final XmlNamespaceDictionary getNamespaceDictionary() {
        return this.namespaceDictionary;
    }

    public <T> T parseAndClose(InputStream inputStream, Charset charset, Class<T> cls) throws IOException {
        return parseAndClose(inputStream, charset, (Type) cls);
    }

    public Object parseAndClose(InputStream inputStream, Charset charset, Type type) throws IOException {
        try {
            XmlPullParser createParser = Xml.createParser();
            createParser.setInput(inputStream, charset.name());
            Object readObject = readObject(createParser, type);
            inputStream.close();
            return readObject;
        } catch (Throwable e) {
            IOException iOException = new IOException();
            iOException.initCause(e);
            throw iOException;
        } catch (Throwable th) {
            inputStream.close();
        }
    }

    public <T> T parseAndClose(Reader reader, Class<T> cls) throws IOException {
        return parseAndClose(reader, (Type) cls);
    }

    public Object parseAndClose(Reader reader, Type type) throws IOException {
        try {
            XmlPullParser createParser = Xml.createParser();
            createParser.setInput(reader);
            Object readObject = readObject(createParser, type);
            reader.close();
            return readObject;
        } catch (Throwable e) {
            IOException iOException = new IOException();
            iOException.initCause(e);
            throw iOException;
        } catch (Throwable th) {
            reader.close();
        }
    }
}
