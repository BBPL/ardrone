package org.apache.sanselan.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class MyLZWDecompressor {
    private static final int MAX_TABLE_SIZE = 4096;
    private final int byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final byte[][] table;
    private boolean tiffLZWMode;
    private int written;

    public interface Listener {
        void code(int i);

        void init(int i, int i2);
    }

    public MyLZWDecompressor(int i, int i2) {
        this(i, i2, null);
    }

    public MyLZWDecompressor(int i, int i2, Listener listener) {
        this.codes = -1;
        this.written = 0;
        this.tiffLZWMode = false;
        this.listener = listener;
        this.byteOrder = i2;
        this.initialCodeSize = i;
        this.table = new byte[4096][];
        this.clearCode = 1 << i;
        this.eoiCode = this.clearCode + 1;
        if (listener != null) {
            listener.init(this.clearCode, this.eoiCode);
        }
        InitializeTable();
    }

    private final void InitializeTable() {
        this.codeSize = this.initialCodeSize;
        int i = this.codeSize;
        for (int i2 = 0; i2 < (1 << (i + 2)); i2++) {
            this.table[i2] = new byte[]{(byte) i2};
        }
    }

    private final void addStringToTable(byte[] bArr) throws IOException {
        if (this.codes < (1 << this.codeSize)) {
            this.table[this.codes] = bArr;
            this.codes++;
            checkCodeSize();
            return;
        }
        throw new IOException("AddStringToTable: codes: " + this.codes + " code_size: " + this.codeSize);
    }

    private final byte[] appendBytes(byte[] bArr, byte b) {
        Object obj = new byte[(bArr.length + 1)];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        obj[obj.length - 1] = b;
        return obj;
    }

    private final void checkCodeSize() {
        int i = 1 << this.codeSize;
        if (this.tiffLZWMode) {
            i--;
        }
        if (this.codes == i) {
            incrementCodeSize();
        }
    }

    private final void clearTable() {
        this.codes = (1 << this.initialCodeSize) + 2;
        this.codeSize = this.initialCodeSize;
        incrementCodeSize();
    }

    private final byte firstChar(byte[] bArr) {
        return bArr[0];
    }

    private final int getNextCode(MyBitInputStream myBitInputStream) throws IOException {
        int readBits = myBitInputStream.readBits(this.codeSize);
        if (this.listener != null) {
            this.listener.code(readBits);
        }
        return readBits;
    }

    private final void incrementCodeSize() {
        if (this.codeSize != 12) {
            this.codeSize++;
        }
    }

    private final boolean isInTable(int i) {
        return i < this.codes;
    }

    private final byte[] stringFromCode(int i) throws IOException {
        if (i < this.codes && i >= 0) {
            return this.table[i];
        }
        throw new IOException("Bad Code: " + i + " codes: " + this.codes + " code_size: " + this.codeSize + ", table: " + this.table.length);
    }

    private final void writeToResult(OutputStream outputStream, byte[] bArr) throws IOException {
        outputStream.write(bArr);
        this.written += bArr.length;
    }

    public byte[] decompress(InputStream inputStream, int i) throws IOException {
        int i2 = -1;
        MyBitInputStream myBitInputStream = new MyBitInputStream(inputStream, this.byteOrder);
        if (this.tiffLZWMode) {
            myBitInputStream.setTiffLZWMode();
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
        clearTable();
        do {
            int nextCode = getNextCode(myBitInputStream);
            if (nextCode == this.eoiCode) {
                break;
            } else if (nextCode == this.clearCode) {
                clearTable();
                if (this.written >= i) {
                    break;
                }
                i2 = getNextCode(myBitInputStream);
                if (i2 == this.eoiCode) {
                    break;
                }
                writeToResult(byteArrayOutputStream, stringFromCode(i2));
            } else if (isInTable(nextCode)) {
                writeToResult(byteArrayOutputStream, stringFromCode(nextCode));
                addStringToTable(appendBytes(stringFromCode(i2), firstChar(stringFromCode(nextCode))));
                i2 = nextCode;
            } else {
                byte[] appendBytes = appendBytes(stringFromCode(i2), firstChar(stringFromCode(i2)));
                writeToResult(byteArrayOutputStream, appendBytes);
                addStringToTable(appendBytes);
                i2 = nextCode;
            }
        } while (this.written < i);
        return byteArrayOutputStream.toByteArray();
    }

    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }
}
