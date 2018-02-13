package com.google.api.client.http.xml;

import com.google.api.client.http.AbstractHttpContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.Preconditions;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlNamespaceDictionary;
import java.io.IOException;
import java.io.OutputStream;
import org.xmlpull.v1.XmlSerializer;

public abstract class AbstractXmlHttpContent extends AbstractHttpContent {
    private final XmlNamespaceDictionary namespaceDictionary;

    protected AbstractXmlHttpContent(XmlNamespaceDictionary xmlNamespaceDictionary) {
        super(new HttpMediaType(Xml.MEDIA_TYPE));
        this.namespaceDictionary = (XmlNamespaceDictionary) Preconditions.checkNotNull(xmlNamespaceDictionary);
    }

    public final XmlNamespaceDictionary getNamespaceDictionary() {
        return this.namespaceDictionary;
    }

    public AbstractXmlHttpContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public final void writeTo(OutputStream outputStream) throws IOException {
        XmlSerializer createSerializer = Xml.createSerializer();
        createSerializer.setOutput(outputStream, getCharset().name());
        writeTo(createSerializer);
    }

    protected abstract void writeTo(XmlSerializer xmlSerializer) throws IOException;
}
