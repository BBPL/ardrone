package com.google.api.client.googleapis.xml.atom;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.xml.AbstractXmlHttpContent;
import com.google.api.client.util.Preconditions;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public final class AtomPatchRelativeToOriginalContent extends AbstractXmlHttpContent {
    private final Object originalEntry;
    private final Object patchedEntry;

    public AtomPatchRelativeToOriginalContent(XmlNamespaceDictionary xmlNamespaceDictionary, Object obj, Object obj2) {
        super(xmlNamespaceDictionary);
        this.originalEntry = Preconditions.checkNotNull(obj);
        this.patchedEntry = Preconditions.checkNotNull(obj2);
    }

    public final Object getOriginalEntry() {
        return this.originalEntry;
    }

    public final Object getPatchedEntry() {
        return this.patchedEntry;
    }

    public AtomPatchRelativeToOriginalContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    protected void writeTo(XmlSerializer xmlSerializer) throws IOException {
        getNamespaceDictionary().serialize(xmlSerializer, Atom.ATOM_NAMESPACE, "entry", GoogleAtom.computePatch(this.patchedEntry, this.originalEntry));
    }
}
