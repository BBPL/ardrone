package org.apache.sanselan.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MyLZWCompressor {
    private final int byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final boolean earlyLimit;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final Map map;

    private static final class ByteArray {
        private final byte[] bytes;
        private final int hash;
        private final int length;
        private final int start;

        public ByteArray(byte[] bArr) {
            this(bArr, 0, bArr.length);
        }

        public ByteArray(byte[] bArr, int i, int i2) {
            this.bytes = bArr;
            this.start = i;
            this.length = i2;
            int i3 = i2;
            for (int i4 = 0; i4 < i2; i4++) {
                i3 = ((i3 + (i3 << 8)) ^ (bArr[i4 + i] & 255)) ^ i4;
            }
            this.hash = i3;
        }

        public final boolean equals(Object obj) {
            ByteArray byteArray = (ByteArray) obj;
            if (byteArray.hash != this.hash || byteArray.length != this.length) {
                return false;
            }
            for (int i = 0; i < this.length; i++) {
                if (byteArray.bytes[byteArray.start + i] != this.bytes[this.start + i]) {
                    return false;
                }
            }
            return true;
        }

        public final int hashCode() {
            return this.hash;
        }
    }

    public interface Listener {
        void clearCode(int i);

        void dataCode(int i);

        void eoiCode(int i);

        void init(int i, int i2);
    }

    public MyLZWCompressor(int i, int i2, boolean z) {
        this(i, i2, z, null);
    }

    public MyLZWCompressor(int i, int i2, boolean z, Listener listener) {
        this.codes = -1;
        this.map = new HashMap();
        this.listener = listener;
        this.byteOrder = i2;
        this.earlyLimit = z;
        this.initialCodeSize = i;
        this.clearCode = 1 << i;
        this.eoiCode = this.clearCode + 1;
        if (listener != null) {
            listener.init(this.clearCode, this.eoiCode);
        }
        InitializeStringTable();
    }

    private final void InitializeStringTable() {
        this.codeSize = this.initialCodeSize;
        int i = this.codeSize;
        this.map.clear();
        this.codes = 0;
        while (this.codes < (1 << i) + 2) {
            if (!(this.codes == this.clearCode || this.codes == this.eoiCode)) {
                this.map.put(arrayToKey((byte) this.codes), new Integer(this.codes));
            }
            this.codes++;
        }
    }

    private final boolean addTableEntry(MyBitOutputStream myBitOutputStream, Object obj) throws IOException {
        boolean z;
        int i = 1 << this.codeSize;
        if (this.earlyLimit) {
            i--;
        }
        if (this.codes != i) {
            z = false;
        } else if (this.codeSize < 12) {
            incrementCodeSize();
            z = false;
        } else {
            writeClearCode(myBitOutputStream);
            clearTable();
            z = true;
        }
        if (!z) {
            this.map.put(obj, new Integer(this.codes));
            this.codes++;
        }
        return z;
    }

    private final boolean addTableEntry(MyBitOutputStream myBitOutputStream, byte[] bArr, int i, int i2) throws IOException {
        return addTableEntry(myBitOutputStream, arrayToKey(bArr, i, i2));
    }

    private final Object arrayToKey(byte b) {
        return arrayToKey(new byte[]{b}, 0, 1);
    }

    private final Object arrayToKey(byte[] bArr, int i, int i2) {
        return new ByteArray(bArr, i, i2);
    }

    private final void clearTable() {
        InitializeStringTable();
        incrementCodeSize();
    }

    private final int codeFromString(byte[] bArr, int i, int i2) throws IOException {
        Object obj = this.map.get(arrayToKey(bArr, i, i2));
        if (obj != null) {
            return ((Integer) obj).intValue();
        }
        throw new IOException("CodeFromString");
    }

    private final void incrementCodeSize() {
        if (this.codeSize != 12) {
            this.codeSize++;
        }
    }

    private final boolean isInTable(byte[] bArr, int i, int i2) {
        return this.map.containsKey(arrayToKey(bArr, i, i2));
    }

    private final void writeClearCode(MyBitOutputStream myBitOutputStream) throws IOException {
        if (this.listener != null) {
            this.listener.dataCode(this.clearCode);
        }
        writeCode(myBitOutputStream, this.clearCode);
    }

    private final void writeCode(MyBitOutputStream myBitOutputStream, int i) throws IOException {
        myBitOutputStream.writeBits(i, this.codeSize);
    }

    private final void writeDataCode(MyBitOutputStream myBitOutputStream, int i) throws IOException {
        if (this.listener != null) {
            this.listener.dataCode(i);
        }
        writeCode(myBitOutputStream, i);
    }

    private final void writeEoiCode(MyBitOutputStream myBitOutputStream) throws IOException {
        if (this.listener != null) {
            this.listener.eoiCode(this.eoiCode);
        }
        writeCode(myBitOutputStream, this.eoiCode);
    }

    public byte[] compress(byte[] bArr) throws IOException {
        int i = 0;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        MyBitOutputStream myBitOutputStream = new MyBitOutputStream(byteArrayOutputStream, this.byteOrder);
        InitializeStringTable();
        clearTable();
        writeClearCode(myBitOutputStream);
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            if (isInTable(bArr, i2, i + 1)) {
                i++;
            } else {
                writeDataCode(myBitOutputStream, codeFromString(bArr, i2, i));
                addTableEntry(myBitOutputStream, bArr, i2, i + 1);
                i = 1;
                i2 = i3;
            }
        }
        writeDataCode(myBitOutputStream, codeFromString(bArr, i2, i));
        writeEoiCode(myBitOutputStream);
        myBitOutputStream.flushCache();
        return byteArrayOutputStream.toByteArray();
    }
}
