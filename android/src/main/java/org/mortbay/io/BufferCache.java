package org.mortbay.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.mortbay.io.Buffer.CaseInsensitve;
import org.mortbay.io.ByteArrayBuffer.CaseInsensitive;
import org.mortbay.util.StringMap;

public class BufferCache {
    private HashMap _bufferMap = new HashMap();
    private ArrayList _index = new ArrayList();
    private StringMap _stringMap = new StringMap(true);

    public static class CachedBuffer extends CaseInsensitive {
        private HashMap _associateMap = null;
        private int _ordinal;

        public CachedBuffer(String str, int i) {
            super(str);
            this._ordinal = i;
        }

        public CachedBuffer getAssociate(Object obj) {
            return this._associateMap == null ? null : (CachedBuffer) this._associateMap.get(obj);
        }

        public int getOrdinal() {
            return this._ordinal;
        }

        public void setAssociate(Object obj, CachedBuffer cachedBuffer) {
            if (this._associateMap == null) {
                this._associateMap = new HashMap();
            }
            this._associateMap.put(obj, cachedBuffer);
        }
    }

    public CachedBuffer add(String str, int i) {
        Object cachedBuffer = new CachedBuffer(str, i);
        this._bufferMap.put(cachedBuffer, cachedBuffer);
        this._stringMap.put(str, cachedBuffer);
        while (i - this._index.size() > 0) {
            this._index.add(null);
        }
        this._index.add(i, cachedBuffer);
        return cachedBuffer;
    }

    public CachedBuffer get(int i) {
        return (i < 0 || i >= this._index.size()) ? null : (CachedBuffer) this._index.get(i);
    }

    public CachedBuffer get(String str) {
        return (CachedBuffer) this._stringMap.get(str);
    }

    public CachedBuffer get(Buffer buffer) {
        return (CachedBuffer) this._bufferMap.get(buffer);
    }

    public CachedBuffer getBest(byte[] bArr, int i, int i2) {
        Entry bestEntry = this._stringMap.getBestEntry(bArr, i, i2);
        return bestEntry != null ? (CachedBuffer) bestEntry.getValue() : null;
    }

    public int getOrdinal(Buffer buffer) {
        if (buffer instanceof CachedBuffer) {
            return ((CachedBuffer) buffer).getOrdinal();
        }
        Buffer lookup = lookup(buffer);
        return (lookup == null || !(lookup instanceof CachedBuffer)) ? -1 : ((CachedBuffer) lookup).getOrdinal();
    }

    public Buffer lookup(String str) {
        Buffer buffer = get(str);
        return buffer == null ? new CachedBuffer(str, -1) : buffer;
    }

    public Buffer lookup(Buffer buffer) {
        CachedBuffer cachedBuffer = get(buffer);
        return cachedBuffer == null ? buffer instanceof CaseInsensitve ? buffer : new View.CaseInsensitive(buffer) : cachedBuffer;
    }

    public String toString() {
        return new StringBuffer().append("CACHE[bufferMap=").append(this._bufferMap).append(",stringMap=").append(this._stringMap).append(",index=").append(this._index).append("]").toString();
    }

    public String toString(Buffer buffer) {
        return lookup(buffer).toString();
    }
}
