package com.google.api.client.http.xml;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.Preconditions;
import com.google.api.client.xml.XmlNamespaceDictionary;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public class XmlHttpContent extends AbstractXmlHttpContent {
    private final Object data;
    private final String elementName;

    public XmlHttpContent(XmlNamespaceDictionary xmlNamespaceDictionary, String str, Object obj) {
        super(xmlNamespaceDictionary);
        this.elementName = (String) Preconditions.checkNotNull(str);
        this.data = Preconditions.checkNotNull(obj);
    }

    public final Object getData() {
        return this.data;
    }

    public final String getElementName() {
        return this.elementName;
    }

    public XmlHttpContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public final void writeTo(XmlSerializer xmlSerializer) throws IOException {
        getNamespaceDictionary().serialize(xmlSerializer, this.elementName, this.data);
    }
}
