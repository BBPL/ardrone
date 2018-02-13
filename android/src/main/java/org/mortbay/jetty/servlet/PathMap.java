package org.mortbay.jetty.servlet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.mortbay.util.LazyList;
import org.mortbay.util.SingletonList;
import org.mortbay.util.StringMap;
import org.mortbay.util.URIUtil;

public class PathMap extends HashMap implements Externalizable {
    private static String __pathSpecSeparators = System.getProperty("org.mortbay.http.PathMap.separators", ":,");
    Entry _default;
    List _defaultSingletonList;
    Set _entrySet;
    StringMap _exactMap;
    boolean _nodefault;
    Entry _prefixDefault;
    StringMap _prefixMap;
    StringMap _suffixMap;

    public static class Entry implements java.util.Map.Entry {
        private Object key;
        private String mapped;
        private transient String string;
        private Object value;

        Entry(Object obj, Object obj2) {
            this.key = obj;
            this.value = obj2;
        }

        public Object getKey() {
            return this.key;
        }

        public String getMapped() {
            return this.mapped;
        }

        public Object getValue() {
            return this.value;
        }

        void setMapped(String str) {
            this.mapped = str;
        }

        public Object setValue(Object obj) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            if (this.string == null) {
                this.string = new StringBuffer().append(this.key).append("=").append(this.value).toString();
            }
            return this.string;
        }
    }

    public PathMap() {
        super(11);
        this._prefixMap = new StringMap();
        this._suffixMap = new StringMap();
        this._exactMap = new StringMap();
        this._defaultSingletonList = null;
        this._prefixDefault = null;
        this._default = null;
        this._nodefault = false;
        this._entrySet = entrySet();
    }

    public PathMap(int i) {
        super(i);
        this._prefixMap = new StringMap();
        this._suffixMap = new StringMap();
        this._exactMap = new StringMap();
        this._defaultSingletonList = null;
        this._prefixDefault = null;
        this._default = null;
        this._nodefault = false;
        this._entrySet = entrySet();
    }

    public PathMap(Map map) {
        this._prefixMap = new StringMap();
        this._suffixMap = new StringMap();
        this._exactMap = new StringMap();
        this._defaultSingletonList = null;
        this._prefixDefault = null;
        this._default = null;
        this._nodefault = false;
        putAll(map);
        this._entrySet = entrySet();
    }

    public PathMap(boolean z) {
        super(11);
        this._prefixMap = new StringMap();
        this._suffixMap = new StringMap();
        this._exactMap = new StringMap();
        this._defaultSingletonList = null;
        this._prefixDefault = null;
        this._default = null;
        this._nodefault = false;
        this._entrySet = entrySet();
        this._nodefault = z;
    }

    private static boolean isPathWildcardMatch(String str, String str2) {
        int length = str.length() - 2;
        return (str.endsWith("/*") && str2.regionMatches(0, str, 0, length)) ? str2.length() == length || '/' == str2.charAt(length) : false;
    }

    public static boolean match(String str, String str2) throws IllegalArgumentException {
        return match(str, str2, false);
    }

    public static boolean match(String str, String str2, boolean z) throws IllegalArgumentException {
        char charAt = str.charAt(0);
        if (charAt == '/') {
            if ((!z && str.length() == 1) || str.equals(str2) || isPathWildcardMatch(str, str2)) {
                return true;
            }
        } else if (charAt == '*') {
            return str2.regionMatches((str2.length() - str.length()) + 1, str, 1, str.length() - 1);
        }
        return false;
    }

    public static String pathInfo(String str, String str2) {
        if (str.charAt(0) == '/' && str.length() != 1) {
            boolean isPathWildcardMatch = isPathWildcardMatch(str, str2);
            if ((!str.equals(str2) || isPathWildcardMatch) && isPathWildcardMatch && str2.length() != str.length() - 2) {
                return str2.substring(str.length() - 2);
            }
        }
        return null;
    }

    public static String pathMatch(String str, String str2) {
        char charAt = str.charAt(0);
        if (charAt == '/') {
            if (str.length() == 1 || str.equals(str2)) {
                return str2;
            }
            if (isPathWildcardMatch(str, str2)) {
                return str2.substring(0, str.length() - 2);
            }
        } else if (charAt == '*' && str2.regionMatches(str2.length() - (str.length() - 1), str, 1, str.length() - 1)) {
            return str2;
        }
        return null;
    }

    public static String relativePath(String str, String str2, String str3) {
        String pathInfo = pathInfo(str2, str3);
        if (pathInfo != null) {
            str3 = pathInfo;
        }
        if (str3.startsWith("./")) {
            str3 = str3.substring(2);
        }
        return str.endsWith(URIUtil.SLASH) ? str3.startsWith(URIUtil.SLASH) ? new StringBuffer().append(str).append(str3.substring(1)).toString() : new StringBuffer().append(str).append(str3).toString() : str3.startsWith(URIUtil.SLASH) ? new StringBuffer().append(str).append(str3).toString() : new StringBuffer().append(str).append(URIUtil.SLASH).append(str3).toString();
    }

    public static void setPathSpecSeparators(String str) {
        __pathSpecSeparators = str;
    }

    public void clear() {
        this._exactMap = new StringMap();
        this._prefixMap = new StringMap();
        this._suffixMap = new StringMap();
        this._default = null;
        this._defaultSingletonList = null;
        super.clear();
    }

    public boolean containsMatch(String str) {
        Entry match = getMatch(str);
        return (match == null || match.equals(this._default)) ? false : true;
    }

    public Object getLazyMatches(String str) {
        Object obj = null;
        if (str == null) {
            return LazyList.getList(null);
        }
        int length = str.length();
        java.util.Map.Entry entry = this._exactMap.getEntry(str, 0, length);
        if (entry != null) {
            obj = LazyList.add(null, entry.getValue());
        }
        int i = length - 1;
        while (true) {
            i = str.lastIndexOf(47, i - 1);
            if (i < 0) {
                break;
            }
            java.util.Map.Entry entry2 = this._prefixMap.getEntry(str, 0, i);
            if (entry2 != null) {
                obj = LazyList.add(obj, entry2.getValue());
            }
        }
        if (this._prefixDefault != null) {
            obj = LazyList.add(obj, this._prefixDefault);
        }
        i = 0;
        while (true) {
            i = str.indexOf(46, i + 1);
            if (i <= 0) {
                break;
            }
            java.util.Map.Entry entry3 = this._suffixMap.getEntry(str, i + 1, (length - i) - 1);
            if (entry3 != null) {
                obj = LazyList.add(obj, entry3.getValue());
            }
        }
        return this._default != null ? obj == null ? this._defaultSingletonList : LazyList.add(obj, this._default) : obj;
    }

    public Entry getMatch(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        java.util.Map.Entry entry = this._exactMap.getEntry(str, 0, length);
        if (entry != null) {
            return (Entry) entry.getValue();
        }
        java.util.Map.Entry entry2;
        int i = length;
        do {
            i = str.lastIndexOf(47, i - 1);
            if (i >= 0) {
                entry2 = this._prefixMap.getEntry(str, 0, i);
            } else if (this._prefixDefault != null) {
                return this._prefixDefault;
            } else {
                java.util.Map.Entry entry3;
                i = 0;
                do {
                    i = str.indexOf(46, i + 1);
                    if (i <= 0) {
                        return this._default;
                    }
                    entry3 = this._suffixMap.getEntry(str, i + 1, (length - i) - 1);
                } while (entry3 == null);
                return (Entry) entry3.getValue();
            }
        } while (entry2 == null);
        return (Entry) entry2.getValue();
    }

    public List getMatches(String str) {
        return LazyList.getList(getLazyMatches(str));
    }

    public Object match(String str) {
        java.util.Map.Entry match = getMatch(str);
        return match != null ? match.getValue() : null;
    }

    public Object put(Object obj, Object obj2) {
        Object obj3;
        synchronized (this) {
            StringTokenizer stringTokenizer = new StringTokenizer(obj.toString(), __pathSpecSeparators);
            obj3 = null;
            while (stringTokenizer.hasMoreTokens()) {
                String nextToken = stringTokenizer.nextToken();
                if (nextToken.startsWith(URIUtil.SLASH) || nextToken.startsWith("*.")) {
                    obj3 = super.put(nextToken, obj2);
                    Object entry = new Entry(nextToken, obj2);
                    if (entry.getKey().equals(nextToken)) {
                        if (nextToken.equals("/*")) {
                            this._prefixDefault = entry;
                        } else if (nextToken.endsWith("/*")) {
                            String substring = nextToken.substring(0, nextToken.length() - 2);
                            entry.setMapped(substring);
                            this._prefixMap.put(substring, entry);
                            this._exactMap.put(substring, entry);
                            this._exactMap.put(nextToken.substring(0, nextToken.length() - 1), entry);
                        } else if (nextToken.startsWith("*.")) {
                            this._suffixMap.put(nextToken.substring(2), entry);
                        } else if (!nextToken.equals(URIUtil.SLASH)) {
                            entry.setMapped(nextToken);
                            this._exactMap.put(nextToken, entry);
                        } else if (this._nodefault) {
                            this._exactMap.put(nextToken, entry);
                        } else {
                            this._default = entry;
                            this._defaultSingletonList = SingletonList.newSingletonList(this._default);
                        }
                    }
                } else {
                    throw new IllegalArgumentException(new StringBuffer().append("PathSpec ").append(nextToken).append(". must start with '/' or '*.'").toString());
                }
            }
        }
        return obj3;
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        putAll((HashMap) objectInput.readObject());
    }

    public Object remove(Object obj) {
        Object remove;
        synchronized (this) {
            if (obj != null) {
                String str = (String) obj;
                if (str.equals("/*")) {
                    this._prefixDefault = null;
                } else if (str.endsWith("/*")) {
                    this._prefixMap.remove(str.substring(0, str.length() - 2));
                    this._exactMap.remove(str.substring(0, str.length() - 1));
                    this._exactMap.remove(str.substring(0, str.length() - 2));
                } else if (str.startsWith("*.")) {
                    this._suffixMap.remove(str.substring(2));
                } else if (str.equals(URIUtil.SLASH)) {
                    this._default = null;
                    this._defaultSingletonList = null;
                } else {
                    this._exactMap.remove(str);
                }
            }
            remove = super.remove(obj);
        }
        return remove;
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(new HashMap(this));
    }
}
