package com.google.api.client.http.xml.atom;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.xml.AbstractXmlHttpContent;
import com.google.api.client.util.Preconditions;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public class AtomContent extends AbstractXmlHttpContent {
    private final Object entry;
    private final boolean isEntry;

    protected AtomContent(XmlNamespaceDictionary xmlNamespaceDictionary, Object obj, boolean z) {
        super(xmlNamespaceDictionary);
        setMediaType(new HttpMediaType(Atom.MEDIA_TYPE));
        this.entry = Preconditions.checkNotNull(obj);
        this.isEntry = z;
    }

    public static AtomContent forEntry(XmlNamespaceDictionary xmlNamespaceDictionary, Object obj) {
        return new AtomContent(xmlNamespaceDictionary, obj, true);
    }

    public static AtomContent forFeed(XmlNamespaceDictionary xmlNamespaceDictionary, Object obj) {
        return new AtomContent(xmlNamespaceDictionary, obj, false);
    }

    public final Object getData() {
        return this.entry;
    }

    public final boolean isEntry() {
        return this.isEntry;
    }

    public AtomContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public final void writeTo(XmlSerializer xmlSerializer) throws IOException {
        getNamespaceDictionary().serialize(xmlSerializer, Atom.ATOM_NAMESPACE, this.isEntry ? "entry" : "feed", this.entry);
    }
}
