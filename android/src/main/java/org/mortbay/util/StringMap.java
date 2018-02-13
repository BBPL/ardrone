package org.mortbay.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class StringMap extends AbstractMap implements Externalizable {
    public static final boolean CASE_INSENSTIVE = true;
    protected static final int __HASH_WIDTH = 17;
    protected HashSet _entrySet;
    protected boolean _ignoreCase;
    protected NullEntry _nullEntry;
    protected Object _nullValue;
    protected Node _root;
    protected Set _umEntrySet;
    protected int _width;

    static class C13521 {
    }

    private static class Node implements Entry {
        char[] _char;
        Node[] _children;
        String _key;
        Node _next;
        char[] _ochar;
        Object _value;

        Node() {
        }

        Node(boolean z, String str, int i) {
            int length = str.length() - i;
            this._char = new char[length];
            this._ochar = new char[length];
            for (int i2 = 0; i2 < length; i2++) {
                char charAt = str.charAt(i + i2);
                this._char[i2] = charAt;
                if (z) {
                    if (Character.isUpperCase(charAt)) {
                        charAt = Character.toLowerCase(charAt);
                    } else if (Character.isLowerCase(charAt)) {
                        charAt = Character.toUpperCase(charAt);
                    }
                    this._ochar[i2] = charAt;
                }
            }
        }

        private void toString(StringBuffer stringBuffer) {
            int i = 0;
            stringBuffer.append("{[");
            if (this._char == null) {
                stringBuffer.append('-');
            } else {
                for (char append : this._char) {
                    stringBuffer.append(append);
                }
            }
            stringBuffer.append(':');
            stringBuffer.append(this._key);
            stringBuffer.append('=');
            stringBuffer.append(this._value);
            stringBuffer.append(']');
            if (this._children != null) {
                while (i < this._children.length) {
                    stringBuffer.append('|');
                    if (this._children[i] != null) {
                        this._children[i].toString(stringBuffer);
                    } else {
                        stringBuffer.append("-");
                    }
                    i++;
                }
            }
            stringBuffer.append('}');
            if (this._next != null) {
                stringBuffer.append(",\n");
                this._next.toString(stringBuffer);
            }
        }

        public Object getKey() {
            return this._key;
        }

        public Object getValue() {
            return this._value;
        }

        public Object setValue(Object obj) {
            Object obj2 = this._value;
            this._value = obj;
            return obj2;
        }

        Node split(StringMap stringMap, int i) {
            Node node = new Node();
            int length = this._char.length - i;
            Object obj = this._char;
            this._char = new char[i];
            node._char = new char[length];
            System.arraycopy(obj, 0, this._char, 0, i);
            System.arraycopy(obj, i, node._char, 0, length);
            if (this._ochar != null) {
                obj = this._ochar;
                this._ochar = new char[i];
                node._ochar = new char[length];
                System.arraycopy(obj, 0, this._ochar, 0, i);
                System.arraycopy(obj, i, node._ochar, 0, length);
            }
            node._key = this._key;
            node._value = this._value;
            this._key = null;
            this._value = null;
            if (stringMap._entrySet.remove(this)) {
                stringMap._entrySet.add(node);
            }
            node._children = this._children;
            this._children = new Node[stringMap._width];
            this._children[node._char[0] % stringMap._width] = node;
            if (!(node._ochar == null || this._children[node._ochar[0] % stringMap._width] == node)) {
                this._children[node._ochar[0] % stringMap._width] = node;
            }
            return node;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            synchronized (stringBuffer) {
                toString(stringBuffer);
            }
            return stringBuffer.toString();
        }
    }

    private class NullEntry implements Entry {
        private final StringMap this$0;

        private NullEntry(StringMap stringMap) {
            this.this$0 = stringMap;
        }

        NullEntry(StringMap stringMap, C13521 c13521) {
            this(stringMap);
        }

        public Object getKey() {
            return null;
        }

        public Object getValue() {
            return this.this$0._nullValue;
        }

        public Object setValue(Object obj) {
            Object obj2 = this.this$0._nullValue;
            this.this$0._nullValue = obj;
            return obj2;
        }

        public String toString() {
            return new StringBuffer().append("[:null=").append(this.this$0._nullValue).append("]").toString();
        }
    }

    public StringMap() {
        this._width = 17;
        this._root = new Node();
        this._ignoreCase = false;
        this._nullEntry = null;
        this._nullValue = null;
        this._entrySet = new HashSet(3);
        this._umEntrySet = Collections.unmodifiableSet(this._entrySet);
    }

    public StringMap(boolean z) {
        this();
        this._ignoreCase = z;
    }

    public StringMap(boolean z, int i) {
        this();
        this._ignoreCase = z;
        this._width = i;
    }

    public void clear() {
        this._root = new Node();
        this._nullEntry = null;
        this._nullValue = null;
        this._entrySet.clear();
    }

    public boolean containsKey(Object obj) {
        if (obj != null) {
            if (getEntry(obj.toString(), 0, obj == null ? 0 : obj.toString().length()) == null) {
                return false;
            }
        } else if (this._nullEntry == null) {
            return false;
        }
        return true;
    }

    public Set entrySet() {
        return this._umEntrySet;
    }

    public Object get(Object obj) {
        return obj == null ? this._nullValue : obj instanceof String ? get((String) obj) : get(obj.toString());
    }

    public Object get(String str) {
        if (str == null) {
            return this._nullValue;
        }
        Entry entry = getEntry(str, 0, str.length());
        return entry == null ? null : entry.getValue();
    }

    public Entry getBestEntry(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return this._nullEntry;
        }
        Entry entry = this._root;
        int i3 = 0;
        int i4 = -1;
        while (i3 < i2) {
            Node node;
            int i5;
            char c = (char) bArr[i + i3];
            if (i4 == -1) {
                node = entry._children == null ? null : entry._children[c % this._width];
                if (node == null && i3 > 0) {
                    return entry;
                }
                i5 = 0;
            } else {
                Entry entry2 = entry;
                i5 = i4;
                Entry entry3 = entry2;
            }
            while (node != null) {
                if (node._char[i5] == c || (this._ignoreCase && node._ochar[i5] == c)) {
                    i5++;
                    if (i5 == node._char.length) {
                        i5 = -1;
                    }
                    i3++;
                    Node node2 = node;
                    i4 = i5;
                    Object obj = node2;
                } else if (i5 > 0) {
                    return null;
                } else {
                    node = node._next;
                }
            }
            return null;
        }
        return i4 > 0 ? null : (entry == null || entry._key != null) ? entry : null;
    }

    public Entry getEntry(String str, int i, int i2) {
        if (str == null) {
            return this._nullEntry;
        }
        Node node = this._root;
        int i3 = 0;
        int i4 = -1;
        while (i3 < i2) {
            int i5;
            Node node2;
            char charAt = str.charAt(i + i3);
            if (i4 != -1) {
                i5 = i4;
                node2 = node;
            } else if (node._children == null) {
                node2 = null;
                i5 = 0;
            } else {
                node2 = node._children[charAt % this._width];
                i5 = 0;
            }
            while (node2 != null) {
                if (node2._char[i5] == charAt || (this._ignoreCase && node2._ochar[i5] == charAt)) {
                    int i6 = i5 + 1;
                    if (i6 == node2._char.length) {
                        i6 = -1;
                    }
                    i3++;
                    Node node3 = node2;
                    i4 = i6;
                    node = node3;
                } else if (i5 > 0) {
                    return null;
                } else {
                    node2 = node2._next;
                }
            }
            return null;
        }
        return i4 <= 0 ? (node == null || node._key != null) ? node : null : null;
    }

    public Entry getEntry(char[] cArr, int i, int i2) {
        if (cArr == null) {
            return this._nullEntry;
        }
        Node node = this._root;
        int i3 = 0;
        int i4 = -1;
        while (i3 < i2) {
            int i5;
            Node node2;
            char c = cArr[i + i3];
            if (i4 != -1) {
                i5 = i4;
                node2 = node;
            } else if (node._children == null) {
                node2 = null;
                i5 = 0;
            } else {
                node2 = node._children[c % this._width];
                i5 = 0;
            }
            while (node2 != null) {
                if (node2._char[i5] == c || (this._ignoreCase && node2._ochar[i5] == c)) {
                    int i6 = i5 + 1;
                    if (i6 == node2._char.length) {
                        i6 = -1;
                    }
                    i3++;
                    Node node3 = node2;
                    i4 = i6;
                    node = node3;
                } else if (i5 > 0) {
                    return null;
                } else {
                    node2 = node2._next;
                }
            }
            return null;
        }
        return i4 <= 0 ? (node == null || node._key != null) ? node : null : null;
    }

    public int getWidth() {
        return this._width;
    }

    public boolean isEmpty() {
        return this._entrySet.isEmpty();
    }

    public boolean isIgnoreCase() {
        return this._ignoreCase;
    }

    public Object put(Object obj, Object obj2) {
        return obj == null ? put(null, obj2) : put(obj.toString(), obj2);
    }

    public Object put(String str, Object obj) {
        if (str == null) {
            Object obj2 = this._nullValue;
            this._nullValue = obj;
            if (this._nullEntry == null) {
                this._nullEntry = new NullEntry(this, null);
                this._entrySet.add(this._nullEntry);
            }
            return obj2;
        }
        C13521 c13521 = null;
        Node node = this._root;
        int i = 0;
        int i2 = -1;
        C13521 c135212 = null;
        while (i < str.length()) {
            int i3;
            Node node2;
            Node node3;
            char charAt = str.charAt(i);
            if (i2 != -1) {
                i3 = i2;
                node2 = node;
                Object obj3 = c135212;
                obj2 = c13521;
            } else if (node._children == null) {
                node3 = null;
                node2 = null;
                i3 = 0;
            } else {
                node2 = node._children[charAt % this._width];
                node3 = null;
                i3 = 0;
            }
            while (node2 != null) {
                int i4;
                int i5;
                Node node4;
                if (node2._char[i3] == charAt || (this._ignoreCase && node2._ochar[i3] == charAt)) {
                    i4 = i3 + 1;
                    if (i4 == node2._char.length) {
                        node3 = null;
                        i5 = -1;
                    } else {
                        i5 = i4;
                        node3 = null;
                    }
                } else if (i3 == 0) {
                    node4 = node2;
                    node2 = node2._next;
                    node3 = node4;
                } else {
                    node2.split(this, i3);
                    i--;
                    i5 = -1;
                }
                i++;
                node4 = node;
                node = node2;
                i2 = i5;
                Object obj4 = node3;
                obj2 = node4;
            }
            Node node5 = new Node(this._ignoreCase, str, i);
            if (node3 != null) {
                node3._next = node5;
                node = node5;
                i2 = i3;
            } else if (node != null) {
                if (node._children == null) {
                    node._children = new Node[this._width];
                }
                node._children[charAt % this._width] = node5;
                i4 = node5._ochar[0] % this._width;
                if (node5._ochar == null || node5._char[0] % this._width == i4) {
                    node = node5;
                    i2 = i3;
                } else if (node._children[i4] == null) {
                    node._children[i4] = node5;
                    node = node5;
                    i2 = i3;
                } else {
                    node3 = node._children[i4];
                    while (node3._next != null) {
                        node3 = node3._next;
                    }
                    node3._next = node5;
                    node = node5;
                    i2 = i3;
                }
            } else {
                this._root = node5;
                node = node5;
                i2 = i3;
            }
            if (node != null) {
                return null;
            }
            if (i2 > 0) {
                node.split(this, i2);
            }
            Object obj5 = node._value;
            node._key = str;
            node._value = obj;
            this._entrySet.add(node);
            return obj5;
        }
        if (node != null) {
            return null;
        }
        if (i2 > 0) {
            node.split(this, i2);
        }
        Object obj52 = node._value;
        node._key = str;
        node._value = obj;
        this._entrySet.add(node);
        return obj52;
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        HashMap hashMap = (HashMap) objectInput.readObject();
        setIgnoreCase(objectInput.readBoolean());
        putAll(hashMap);
    }

    public Object remove(Object obj) {
        return obj == null ? remove(null) : remove(obj.toString());
    }

    public Object remove(String str) {
        if (str == null) {
            Object obj = this._nullValue;
            if (this._nullEntry == null) {
                return obj;
            }
            this._entrySet.remove(this._nullEntry);
            this._nullEntry = null;
            this._nullValue = null;
            return obj;
        }
        Node node = this._root;
        int i = 0;
        int i2 = -1;
        loop0:
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (i2 == -1) {
                if (node._children == null) {
                    node = null;
                    i2 = 0;
                } else {
                    node = node._children[charAt % this._width];
                    i2 = 0;
                }
            }
            while (node != null) {
                if (node._char[i2] != charAt && (!this._ignoreCase || node._ochar[i2] != charAt)) {
                    if (i2 > 0) {
                        break loop0;
                    }
                    node = node._next;
                } else {
                    i2++;
                    if (i2 == node._char.length) {
                        i2 = -1;
                    }
                    i++;
                }
            }
        }
        if (i2 <= 0 && (node == null || node._key != null)) {
            Object obj2 = node._value;
            this._entrySet.remove(node);
            node._value = null;
            node._key = null;
            return obj2;
        }
        return null;
    }

    public void setIgnoreCase(boolean z) {
        if (this._root._children != null) {
            throw new IllegalStateException("Must be set before first put");
        }
        this._ignoreCase = z;
    }

    public void setWidth(int i) {
        this._width = i;
    }

    public int size() {
        return this._entrySet.size();
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        HashMap hashMap = new HashMap(this);
        objectOutput.writeBoolean(this._ignoreCase);
        objectOutput.writeObject(hashMap);
    }
}
