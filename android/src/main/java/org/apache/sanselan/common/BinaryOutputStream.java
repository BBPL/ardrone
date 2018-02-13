package org.apache.sanselan.common;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.sanselan.ImageWriteException;

public class BinaryOutputStream extends OutputStream implements BinaryConstants {
    private int byteOrder = 77;
    private int count = 0;
    protected boolean debug = false;
    private final OutputStream os;

    public BinaryOutputStream(OutputStream outputStream) {
        this.os = outputStream;
    }

    public BinaryOutputStream(OutputStream outputStream, int i) {
        this.byteOrder = i;
        this.os = outputStream;
    }

    private byte[] convertValueToByteArray(int i, int i2) {
        int i3 = 0;
        byte[] bArr = new byte[i2];
        if (this.byteOrder == 77) {
            while (i3 < i2) {
                bArr[i3] = (byte) ((i >> (((i2 - i3) - 1) * 8)) & 255);
                i3++;
            }
        } else {
            while (i3 < i2) {
                bArr[i3] = (byte) ((i >> (i3 * 8)) & 255);
                i3++;
            }
        }
        return bArr;
    }

    private final void writeNBytes(int i, int i2) throws ImageWriteException, IOException {
        write(convertValueToByteArray(i, i2));
    }

    public int getByteCount() {
        return this.count;
    }

    public int getByteOrder() {
        return this.byteOrder;
    }

    public final boolean getDebug() {
        return this.debug;
    }

    protected void setByteOrder(int i) {
        this.byteOrder = i;
    }

    protected void setByteOrder(int i, int i2) throws ImageWriteException, IOException {
        if (i != i2) {
            throw new ImageWriteException("Byte Order bytes don't match (" + i + ", " + i2 + ").");
        } else if (i == 77) {
            this.byteOrder = i;
        } else if (i == 73) {
            this.byteOrder = i;
        } else {
            throw new ImageWriteException("Unknown Byte Order hint: " + i);
        }
    }

    public final void setDebug(boolean z) {
        this.debug = z;
    }

    public void write(int i) throws IOException {
        this.os.write(i);
        this.count++;
    }

    public final void write2ByteInteger(int i) throws ImageWriteException, IOException {
        if (this.byteOrder == 77) {
            write((i >> 8) & 255);
            write(i & 255);
            return;
        }
        write(i & 255);
        write((i >> 8) & 255);
    }

    public final void write2Bytes(int i) throws ImageWriteException, IOException {
        writeNBytes(i, 2);
    }

    public final void write3Bytes(int i) throws ImageWriteException, IOException {
        writeNBytes(i, 3);
    }

    public final void write4ByteInteger(int i) throws ImageWriteException, IOException {
        if (this.byteOrder == 77) {
            write((i >> 24) & 255);
            write((i >> 16) & 255);
            write((i >> 8) & 255);
            write(i & 255);
            return;
        }
        write(i & 255);
        write((i >> 8) & 255);
        write((i >> 16) & 255);
        write((i >> 24) & 255);
    }

    public final void write4Bytes(int i) throws ImageWriteException, IOException {
        writeNBytes(i, 4);
    }

    public final void writeByteArray(byte[] bArr) throws IOException {
        this.os.write(bArr, 0, bArr.length);
        this.count += bArr.length;
    }
}
