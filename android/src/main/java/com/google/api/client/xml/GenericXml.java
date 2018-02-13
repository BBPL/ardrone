package com.google.api.client.xml;

import com.google.api.client.util.GenericData;

public class GenericXml extends GenericData implements Cloneable {
    public String name;
    public XmlNamespaceDictionary namespaceDictionary;

    public GenericXml clone() {
        return (GenericXml) super.clone();
    }

    public GenericXml set(String str, Object obj) {
        return (GenericXml) super.set(str, obj);
    }

    public String toString() {
        XmlNamespaceDictionary xmlNamespaceDictionary = this.namespaceDictionary;
        if (xmlNamespaceDictionary == null) {
            xmlNamespaceDictionary = new XmlNamespaceDictionary();
        }
        return xmlNamespaceDictionary.toStringOf(this.name, this);
    }
}
