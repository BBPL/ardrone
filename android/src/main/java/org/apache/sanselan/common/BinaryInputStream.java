package org.apache.sanselan.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import org.apache.sanselan.ImageReadException;

public class BinaryInputStream extends InputStream implements BinaryConstants {
    private int byteOrder = 77;
    protected boolean debug = false;
    private final InputStream is;

    public BinaryInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    public BinaryInputStream(InputStream inputStream, int i) {
        this.byteOrder = i;
        this.is = inputStream;
    }

    public BinaryInputStream(byte[] bArr, int i) {
        this.byteOrder = i;
        this.is = new ByteArrayInputStream(bArr);
    }

    protected static final int CharsToQuad(char c, char c2, char c3, char c4) {
        return ((((c & 255) << 24) | ((c2 & 255) << 16)) | ((c3 & 255) << 8)) | ((c4 & 255) << 0);
    }

    public final boolean compareByteArrays(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr.length < i + i3 || bArr2.length < i2 + i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                debugNumber("a[" + (i + i4) + "]", bArr[i + i4]);
                debugNumber("b[" + (i2 + i4) + "]", bArr2[i4 + i2]);
                return false;
            }
        }
        return true;
    }

    protected final int convertByteArrayToInt(String str, byte[] bArr) {
        return convertByteArrayToInt(str, bArr, this.byteOrder);
    }

    protected final int convertByteArrayToInt(String str, byte[] bArr, int i) {
        return convertByteArrayToInt(str, bArr, 0, 4, i);
    }

    protected final int convertByteArrayToInt(String str, byte[] bArr, int i, int i2, int i3) {
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        byte b3 = bArr[i + 2];
        int i4 = 0;
        if (i2 == 4) {
            i4 = bArr[i + 3];
        }
        if (i3 == 77) {
            i4 = ((i4 & 255) << 0) + ((((b & 255) << 24) + ((b2 & 255) << 16)) + ((b3 & 255) << 8));
        } else {
            i4 = ((((i4 & 255) << 24) + ((b3 & 255) << 16)) + ((b2 & 255) << 8)) + ((b & 255) << 0);
        }
        if (this.debug) {
            debugNumber(str, i4, 4);
        }
        return i4;
    }

    protected final int[] convertByteArrayToIntArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 4) + i;
        if (bArr.length < i4) {
            System.out.println(new StringBuilder(String.valueOf(str)).append(": expected length: ").append(i4).append(", actual length: ").append(bArr.length).toString());
            return null;
        }
        int[] iArr = new int[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            iArr[i5] = convertByteArrayToInt(str, bArr, i + (i5 * 4), 4, i3);
        }
        return iArr;
    }

    protected final RationalNumber convertByteArrayToRational(String str, byte[] bArr, int i) {
        return convertByteArrayToRational(str, bArr, 0, i);
    }

    protected final RationalNumber convertByteArrayToRational(String str, byte[] bArr, int i, int i2) {
        return new RationalNumber(convertByteArrayToInt(str, bArr, i + 0, 4, i2), convertByteArrayToInt(str, bArr, i + 4, 4, i2));
    }

    protected final RationalNumber[] convertByteArrayToRationalArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 8) + i;
        if (bArr.length < i4) {
            System.out.println(new StringBuilder(String.valueOf(str)).append(": expected length: ").append(i4).append(", actual length: ").append(bArr.length).toString());
            return null;
        }
        RationalNumber[] rationalNumberArr = new RationalNumber[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            rationalNumberArr[i5] = convertByteArrayToRational(str, bArr, (i5 * 8) + i, i3);
        }
        return rationalNumberArr;
    }

    public final int convertByteArrayToShort(String str, int i, byte[] bArr) {
        return convertByteArrayToShort(str, i, bArr, this.byteOrder);
    }

    protected final int convertByteArrayToShort(String str, int i, byte[] bArr, int i2) {
        int i3;
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        if (i2 == 77) {
            i3 = ((b & 255) << 8) + ((b2 & 255) << 0);
        } else {
            i3 = ((b & 255) << 0) + ((b2 & 255) << 8);
        }
        if (this.debug) {
            debugNumber(str, i3, 2);
        }
        return i3;
    }

    public final int convertByteArrayToShort(String str, byte[] bArr) {
        return convertByteArrayToShort(str, bArr, this.byteOrder);
    }

    protected final int convertByteArrayToShort(String str, byte[] bArr, int i) {
        return convertByteArrayToShort(str, 0, bArr, i);
    }

    protected final int[] convertByteArrayToShortArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 2) + i;
        if (bArr.length < i4) {
            System.out.println(new StringBuilder(String.valueOf(str)).append(": expected length: ").append(i4).append(", actual length: ").append(bArr.length).toString());
            return null;
        }
        int[] iArr = new int[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            iArr[i5] = convertByteArrayToShort(str, (i5 * 2) + i, bArr, i3);
        }
        return iArr;
    }

    protected final void debugByteArray(String str, byte[] bArr) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": ").append(bArr.length).toString());
        int i = 0;
        while (i < bArr.length && i < 50) {
            debugNumber(new StringBuilder(String.valueOf(str)).append(" (").append(i).append(")").toString(), bArr[i]);
            i++;
        }
    }

    public final void debugNumber(String str, int i) {
        debugNumber(str, i, 1);
    }

    public final void debugNumber(String str, int i, int i2) {
        System.out.print(new StringBuilder(String.valueOf(str)).append(": ").append(i).append(" (").toString());
        int i3 = i;
        for (int i4 = 0; i4 < i2; i4++) {
            if (i4 > 0) {
                System.out.print(",");
            }
            int i5 = i3 & 255;
            System.out.print(((char) i5) + " [" + i5 + "]");
            i3 >>= 8;
        }
        System.out.println(") [0x" + Integer.toHexString(i) + ", " + Integer.toBinaryString(i) + "]");
    }

    protected final void debugNumberArray(String str, int[] iArr, int i) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": ").append(iArr.length).toString());
        int i2 = 0;
        while (i2 < iArr.length && i2 < 50) {
            debugNumber(new StringBuilder(String.valueOf(str)).append(" (").append(i2).append(")").toString(), iArr[i2], i);
            i2++;
        }
    }

    public final int findNull(byte[] bArr) {
        return findNull(bArr, 0);
    }

    public final int findNull(byte[] bArr, int i) {
        while (i < bArr.length) {
            if (bArr[i] == (byte) 0) {
                return i;
            }
            i++;
        }
        return -1;
    }

    protected int getByteOrder() {
        return this.byteOrder;
    }

    protected final byte[] getBytearrayHead(String str, byte[] bArr, int i) {
        return readBytearray(str, bArr, 0, bArr.length - i);
    }

    protected final byte[] getBytearrayTail(String str, byte[] bArr, int i) {
        return readBytearray(str, bArr, i, bArr.length - i);
    }

    public final boolean getDebug() {
        return this.debug;
    }

    protected final byte[] getRAFBytes(RandomAccessFile randomAccessFile, long j, int i, String str) throws IOException {
        byte[] bArr = new byte[i];
        if (this.debug) {
            System.out.println("getRAFBytes pos: " + j);
            System.out.println("getRAFBytes length: " + i);
        }
        randomAccessFile.seek(j);
        int i2 = 0;
        while (i2 < i) {
            int read = randomAccessFile.read(bArr, i2, i - i2);
            if (read < 1) {
                throw new IOException(str);
            }
            i2 += read;
        }
        return bArr;
    }

    protected final void printByteBits(String str, byte b) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": '").append(Integer.toBinaryString(b & 255)).toString());
    }

    protected final void printCharQuad(String str, int i) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": '").append((char) ((i >> 24) & 255)).append((char) ((i >> 16) & 255)).append((char) ((i >> 8) & 255)).append((char) ((i >> 0) & 255)).append("'").toString());
    }

    public int read() throws IOException {
        return this.is.read();
    }

    public final int read1ByteInteger(String str) throws ImageReadException, IOException {
        int read = this.is.read();
        if (read >= 0) {
            return read & 255;
        }
        throw new ImageReadException(str);
    }

    public final int read2ByteInteger(String str) throws ImageReadException, IOException {
        int read = this.is.read();
        int read2 = this.is.read();
        if (read < 0 || read2 < 0) {
            throw new ImageReadException(str);
        } else if (this.byteOrder == 77) {
            return ((read & 255) << 8) + ((read2 & 255) << 0);
        } else {
            return ((read & 255) << 0) + ((read2 & 255) << 8);
        }
    }

    public final int read2Bytes(String str, String str2) throws ImageReadException, IOException {
        return read2Bytes(str, str2, this.byteOrder);
    }

    protected final int read2Bytes(String str, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[2];
        int i2 = 0;
        while (i2 < 2) {
            int read = this.is.read(bArr, i2, 2 - i2);
            if (read < 1) {
                throw new IOException(str2);
            }
            i2 += read;
        }
        return convertByteArrayToShort(str, bArr, i);
    }

    public final int read3Bytes(String str, String str2) throws ImageReadException, IOException {
        return read3Bytes(str, str2, this.byteOrder);
    }

    protected final int read3Bytes(String str, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[3];
        int i2 = 0;
        while (i2 < 3) {
            int read = this.is.read(bArr, i2, 3 - i2);
            if (read < 1) {
                throw new IOException(str2);
            }
            i2 += read;
        }
        return convertByteArrayToInt(str, bArr, 0, 3, i);
    }

    public final int read4ByteInteger(String str) throws ImageReadException, IOException {
        int read = this.is.read();
        int read2 = this.is.read();
        int read3 = this.is.read();
        int read4 = this.is.read();
        if (read < 0 || read2 < 0 || read3 < 0 || read4 < 0) {
            throw new ImageReadException(str);
        } else if (this.byteOrder == 77) {
            return ((((read & 255) << 24) + ((read2 & 255) << 16)) + ((read3 & 255) << 8)) + ((read4 & 255) << 0);
        } else {
            return ((read & 255) << 0) + (((read2 & 255) << 8) + (((read3 & 255) << 16) + ((read4 & 255) << 24)));
        }
    }

    public final int read4Bytes(String str, String str2) throws ImageReadException, IOException {
        return read4Bytes(str, str2, this.byteOrder);
    }

    protected final int read4Bytes(String str, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[4];
        int i2 = 0;
        while (i2 < 4) {
            int read = this.is.read(bArr, i2, 4 - i2);
            if (read < 1) {
                throw new IOException(str2);
            }
            i2 += read;
        }
        return convertByteArrayToInt(str, bArr, i);
    }

    protected final void readAndVerifyBytes(String str, byte[] bArr, String str2) throws ImageReadException, IOException {
        byte[] readByteArray = readByteArray(str, bArr.length, str2);
        for (int i = 0; i < bArr.length; i++) {
            if (readByteArray[i] != bArr[i]) {
                System.out.println("i: " + i);
                debugNumber("bytes[" + i + "]", readByteArray[i]);
                debugNumber("expected[" + i + "]", bArr[i]);
                throw new ImageReadException(str2);
            }
        }
    }

    public final void readAndVerifyBytes(byte[] bArr, String str) throws ImageReadException, IOException {
        int i = 0;
        while (i < bArr.length) {
            int read = this.is.read();
            byte b = (byte) (read & 255);
            if (read < 0 || b != bArr[i]) {
                System.out.println("i: " + i);
                debugByteArray("expected", bArr);
                debugNumber("data[" + i + "]", b);
                throw new ImageReadException(str);
            }
            i++;
        }
    }

    public final byte readByte(String str, String str2) throws IOException {
        int read = this.is.read();
        if (read < 0) {
            System.out.println(new StringBuilder(String.valueOf(str)).append(": ").append(read).toString());
            throw new IOException(str2);
        }
        if (this.debug) {
            debugNumber(str, read);
        }
        return (byte) (read & 255);
    }

    public final byte[] readByteArray(int i, String str) throws ImageReadException, IOException {
        return readByteArray(i, str, false, true);
    }

    public final byte[] readByteArray(int i, String str, boolean z, boolean z2) throws ImageReadException, IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (true) {
            int read = read(bArr, i2, i - i2);
            if (read <= 0) {
                break;
            }
            i2 += read;
        }
        if (i2 >= i) {
            return bArr;
        }
        if (z2) {
            throw new ImageReadException(str);
        }
        if (z) {
            System.out.println(str);
        }
        return null;
    }

    public final byte[] readByteArray(String str, int i, String str2) throws ImageReadException, IOException {
        int i2 = 0;
        byte[] bArr = new byte[i];
        int i3 = 0;
        while (i3 < i) {
            int read = this.is.read(bArr, i3, i - i3);
            if (read < 1) {
                throw new IOException(str2);
            }
            i3 += read;
        }
        if (this.debug) {
            while (i2 < i && i2 < 150) {
                debugNumber(new StringBuilder(String.valueOf(str)).append(" (").append(i2).append(")").toString(), bArr[i2] & 255);
                i2++;
            }
        }
        return bArr;
    }

    public final byte[] readBytearray(String str, byte[] bArr, int i, int i2) {
        if (bArr.length < i + i2) {
            return null;
        }
        Object obj = new byte[i2];
        System.arraycopy(bArr, i, obj, 0, i2);
        if (!this.debug) {
            return obj;
        }
        debugByteArray(str, obj);
        return obj;
    }

    protected final void readRandomBytes() throws ImageReadException, IOException {
        for (int i = 0; i < 100; i++) {
            readByte(i, "Random Data");
        }
    }

    protected final void scanForByte(byte b) throws IOException {
        int i = 0;
        int i2 = 0;
        while (i < 3) {
            int read = this.is.read();
            if (read >= 0) {
                if ((read & 255) == b) {
                    System.out.println("\t" + i2 + ": match.");
                    i++;
                }
                i2++;
            } else {
                return;
            }
        }
    }

    protected void setByteOrder(int i) {
        this.byteOrder = i;
    }

    protected void setByteOrder(int i, int i2) throws ImageReadException, IOException {
        if (i != i2) {
            throw new ImageReadException("Byte Order bytes don't match (" + i + ", " + i2 + ").");
        } else if (i == 77) {
            this.byteOrder = i;
        } else if (i == 73) {
            this.byteOrder = i;
        } else {
            throw new ImageReadException("Unknown Byte Order hint: " + i);
        }
    }

    public final void setDebug(boolean z) {
        this.debug = z;
    }

    protected void skipBytes(int i) throws IOException {
        skipBytes(i, "Couldn't skip bytes");
    }

    public final void skipBytes(int i, String str) throws IOException {
        long j = 0;
        while (((long) i) != j) {
            long skip = this.is.skip(((long) i) - j);
            if (skip < 1) {
                throw new IOException(new StringBuilder(String.valueOf(str)).append(" (").append(skip).append(")").toString());
            }
            j += skip;
        }
    }
}
