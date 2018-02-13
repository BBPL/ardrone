package org.mortbay.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class AttributesMap implements Attributes {
    Map _map;

    public AttributesMap() {
        this._map = new HashMap();
    }

    public AttributesMap(Map map) {
        this._map = map;
    }

    public static Enumeration getAttributeNamesCopy(Attributes attributes) {
        if (attributes instanceof AttributesMap) {
            return Collections.enumeration(((AttributesMap) attributes)._map.keySet());
        }
        Collection arrayList = new ArrayList();
        Enumeration attributeNames = attributes.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            arrayList.add(attributeNames.nextElement());
        }
        return Collections.enumeration(arrayList);
    }

    public void clearAttributes() {
        this._map.clear();
    }

    public Object getAttribute(String str) {
        return this._map.get(str);
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration(this._map.keySet());
    }

    public void removeAttribute(String str) {
        this._map.remove(str);
    }

    public void setAttribute(String str, Object obj) {
        if (obj == null) {
            this._map.remove(str);
        } else {
            this._map.put(str, obj);
        }
    }

    public String toString() {
        return this._map.toString();
    }
}
