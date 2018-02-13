package com.google.api.services.picasa;

import com.google.api.client.googleapis.xml.atom.GoogleAtom;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;
import org.mortbay.jetty.HttpVersions;

public final class PicasaAtom {
    public static final XmlNamespaceDictionary NAMESPACE_DICTIONARY = new XmlNamespaceDictionary();

    static {
        NAMESPACE_DICTIONARY.set(HttpVersions.HTTP_0_9, Atom.ATOM_NAMESPACE);
        NAMESPACE_DICTIONARY.set("atom", Atom.ATOM_NAMESPACE);
        NAMESPACE_DICTIONARY.set("exif", "http://schemas.google.com/photos/exif/2007");
        NAMESPACE_DICTIONARY.set("gd", GoogleAtom.GD_NAMESPACE);
        NAMESPACE_DICTIONARY.set("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        NAMESPACE_DICTIONARY.set("georss", "http://www.georss.org/georss");
        NAMESPACE_DICTIONARY.set("gml", "http://www.opengis.net/gml");
        NAMESPACE_DICTIONARY.set("gphoto", "http://schemas.google.com/photos/2007");
        NAMESPACE_DICTIONARY.set("media", "http://search.yahoo.com/mrss/");
        NAMESPACE_DICTIONARY.set("openSearch", "http://a9.com/-/spec/opensearch/1.1/");
        NAMESPACE_DICTIONARY.set("xml", "http://www.w3.org/XML/1998/namespace");
    }

    private PicasaAtom() {
    }
}
