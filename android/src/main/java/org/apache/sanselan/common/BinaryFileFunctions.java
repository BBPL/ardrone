package org.apache.sanselan.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;

public class BinaryFileFunctions implements BinaryConstants {
    protected boolean debug = false;

    public static final int CharsToQuad(char c, char c2, char c3, char c4) {
        return ((((c & 255) << 24) | ((c2 & 255) << 16)) | ((c3 & 255) << 8)) | ((c4 & 255) << 0);
    }

    public static final boolean compareBytes(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr.length < i + i3 || bArr2.length < i2 + i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    public static final boolean compareBytes(byte[] bArr, byte[] bArr2) {
        return bArr.length != bArr2.length ? false : compareBytes(bArr, 0, bArr2, 0, bArr.length);
    }

    public static final byte[] head(byte[] bArr, int i) {
        if (i > bArr.length) {
            i = bArr.length;
        }
        return slice(bArr, 0, i);
    }

    protected static final byte[] int2ToByteArray(int i, int i2) {
        if (i2 == 77) {
            return new byte[]{(byte) (i >> 8), (byte) (i >> 0)};
        }
        return new byte[]{(byte) (i >> 0), (byte) (i >> 8)};
    }

    public static final byte[] slice(byte[] bArr, int i, int i2) {
        if (bArr.length < i + i2) {
            return null;
        }
        Object obj = new byte[i2];
        System.arraycopy(bArr, i, obj, 0, i2);
        return obj;
    }

    public static final byte[] tail(byte[] bArr, int i) {
        if (i > bArr.length) {
            i = bArr.length;
        }
        return slice(bArr, bArr.length - i, i);
    }

    public final boolean compareByteArrays(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr.length < i + i3 || bArr2.length < i2 + i3) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    public final boolean compareByteArrays(byte[] bArr, byte[] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        return compareByteArrays(bArr, 0, bArr2, 0, bArr.length);
    }

    protected final double convertByteArrayToDouble(String str, byte[] bArr, int i) {
        return convertByteArrayToDouble(str, bArr, 0, i);
    }

    protected final double convertByteArrayToDouble(String str, byte[] bArr, int i, int i2) {
        long j;
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        byte b3 = bArr[i + 2];
        byte b4 = bArr[i + 3];
        byte b5 = bArr[i + 4];
        byte b6 = bArr[i + 5];
        byte b7 = bArr[i + 6];
        byte b8 = bArr[i + 7];
        if (i2 == 77) {
            j = (long) (((((((((b & 255) << 56) | ((b2 & 255) << 48)) | ((b3 & 255) << 40)) | ((b4 & 255) << 32)) | ((b5 & 255) << 24)) | ((b6 & 255) << 16)) | ((b7 & 255) << 8)) | ((b8 & 255) << 0));
        } else {
            int i3 = (b6 & 255) << 40;
            int i4 = (b5 & 255) << 32;
            int i5 = (b4 & 255) << 24;
            int i6 = (b3 & 255) << 16;
            int i7 = (b2 & 255) << 8;
            int i8 = (b & 255) << 0;
            j = (long) (i8 | (i7 | (i6 | (i5 | (i4 | (i3 | (((b7 & 255) << 48) | ((b8 & 255) << 56))))))));
        }
        return Double.longBitsToDouble(j);
    }

    protected final double[] convertByteArrayToDoubleArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 8) + i;
        if (bArr.length < i4) {
            System.out.println(new StringBuilder(String.valueOf(str)).append(": expected length: ").append(i4).append(", actual length: ").append(bArr.length).toString());
            return null;
        }
        double[] dArr = new double[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            dArr[i5] = convertByteArrayToDouble(str, bArr, (i5 * 8) + i, i3);
        }
        return dArr;
    }

    protected final float convertByteArrayToFloat(String str, byte[] bArr, int i) {
        return convertByteArrayToFloat(str, bArr, 0, i);
    }

    protected final float convertByteArrayToFloat(String str, byte[] bArr, int i, int i2) {
        int i3;
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        byte b3 = bArr[i + 2];
        byte b4 = bArr[i + 3];
        if (i2 == 77) {
            i3 = ((((b & 255) << 24) | ((b2 & 255) << 16)) | ((b3 & 255) << 8)) | ((b4 & 255) << 0);
        } else {
            int i4 = (b2 & 255) << 8;
            i3 = (b & 255) << 0;
            i3 |= i4 | (((b3 & 255) << 16) | ((b4 & 255) << 24));
        }
        return Float.intBitsToFloat(i3);
    }

    protected final float[] convertByteArrayToFloatArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 4) + i;
        if (bArr.length < i4) {
            System.out.println(new StringBuilder(String.valueOf(str)).append(": expected length: ").append(i4).append(", actual length: ").append(bArr.length).toString());
            return null;
        }
        float[] fArr = new float[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            fArr[i5] = convertByteArrayToFloat(str, bArr, (i5 * 4) + i, i3);
        }
        return fArr;
    }

    protected final int convertByteArrayToInt(String str, byte[] bArr, int i) {
        return convertByteArrayToInt(str, bArr, 0, i);
    }

    protected final int convertByteArrayToInt(String str, byte[] bArr, int i, int i2) {
        int i3;
        byte b = bArr[i + 0];
        byte b2 = bArr[i + 1];
        byte b3 = bArr[i + 2];
        byte b4 = bArr[i + 3];
        if (i2 == 77) {
            i3 = ((((b & 255) << 24) | ((b2 & 255) << 16)) | ((b3 & 255) << 8)) | ((b4 & 255) << 0);
        } else {
            int i4 = (b2 & 255) << 8;
            i3 = (b & 255) << 0;
            i3 |= i4 | (((b3 & 255) << 16) | ((b4 & 255) << 24));
        }
        if (this.debug) {
            debugNumber(str, i3, 4);
        }
        return i3;
    }

    protected final int[] convertByteArrayToIntArray(String str, byte[] bArr, int i, int i2, int i3) {
        int i4 = (i2 * 4) + i;
        if (bArr.length < i4) {
            System.out.println(new StringBuilder(String.valueOf(str)).append(": expected length: ").append(i4).append(", actual length: ").append(bArr.length).toString());
            return null;
        }
        int[] iArr = new int[i2];
        for (int i5 = 0; i5 < i2; i5++) {
            iArr[i5] = convertByteArrayToInt(str, bArr, (i5 * 4) + i, i3);
        }
        return iArr;
    }

    protected final RationalNumber convertByteArrayToRational(String str, byte[] bArr, int i) {
        return convertByteArrayToRational(str, bArr, 0, i);
    }

    protected final RationalNumber convertByteArrayToRational(String str, byte[] bArr, int i, int i2) {
        return new RationalNumber(convertByteArrayToInt(str, bArr, i + 0, i2), convertByteArrayToInt(str, bArr, i + 4, i2));
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

    protected final int convertByteArrayToShort(String str, int i, byte[] bArr, int i2) throws ImageReadException {
        if (i + 1 >= bArr.length) {
            throw new ImageReadException("Index out of bounds. Array size: " + bArr.length + ", index: " + i);
        }
        int i3 = bArr[i + 0] & 255;
        int i4 = bArr[i + 1] & 255;
        i3 = i2 == 77 ? (i3 << 8) | i4 : i3 | (i4 << 8);
        if (this.debug) {
            debugNumber(str, i3, 2);
        }
        return i3;
    }

    protected final int convertByteArrayToShort(String str, byte[] bArr, int i) throws ImageReadException {
        return convertByteArrayToShort(str, 0, bArr, i);
    }

    protected final int[] convertByteArrayToShortArray(String str, byte[] bArr, int i, int i2, int i3) throws ImageReadException {
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

    protected final byte[] convertDoubleArrayToByteArray(double[] dArr, int i) {
        byte[] bArr = new byte[(dArr.length * 8)];
        for (int i2 = 0; i2 < dArr.length; i2++) {
            long doubleToRawLongBits = Double.doubleToRawLongBits(dArr[i2]);
            int i3 = i2 * 8;
            if (i == 77) {
                bArr[i3 + 0] = (byte) ((int) (255 & (doubleToRawLongBits >> null)));
                bArr[i3 + 1] = (byte) ((int) (255 & (doubleToRawLongBits >> 8)));
                bArr[i3 + 2] = (byte) ((int) (255 & (doubleToRawLongBits >> 16)));
                bArr[i3 + 3] = (byte) ((int) (255 & (doubleToRawLongBits >> 24)));
                bArr[i3 + 4] = (byte) ((int) (255 & (doubleToRawLongBits >> 32)));
                bArr[i3 + 5] = (byte) ((int) (255 & (doubleToRawLongBits >> 40)));
                bArr[i3 + 6] = (byte) ((int) (255 & (doubleToRawLongBits >> 48)));
                bArr[i3 + 7] = (byte) ((int) ((doubleToRawLongBits >> 56) & 255));
            } else {
                bArr[i3 + 7] = (byte) ((int) (255 & (doubleToRawLongBits >> null)));
                bArr[i3 + 6] = (byte) ((int) (255 & (doubleToRawLongBits >> 8)));
                bArr[i3 + 5] = (byte) ((int) (255 & (doubleToRawLongBits >> 16)));
                bArr[i3 + 4] = (byte) ((int) (255 & (doubleToRawLongBits >> 24)));
                bArr[i3 + 3] = (byte) ((int) (255 & (doubleToRawLongBits >> 32)));
                bArr[i3 + 2] = (byte) ((int) (255 & (doubleToRawLongBits >> 40)));
                bArr[i3 + 1] = (byte) ((int) (255 & (doubleToRawLongBits >> 48)));
                bArr[i3 + 0] = (byte) ((int) ((doubleToRawLongBits >> 56) & 255));
            }
        }
        return bArr;
    }

    protected final byte[] convertDoubleToByteArray(double d, int i) {
        byte[] bArr = new byte[8];
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        if (i == 77) {
            bArr[0] = (byte) ((int) ((doubleToRawLongBits >> 0) & 255));
            bArr[1] = (byte) ((int) ((doubleToRawLongBits >> 8) & 255));
            bArr[2] = (byte) ((int) ((doubleToRawLongBits >> 16) & 255));
            bArr[3] = (byte) ((int) ((doubleToRawLongBits >> 24) & 255));
            bArr[4] = (byte) ((int) ((doubleToRawLongBits >> 32) & 255));
            bArr[5] = (byte) ((int) ((doubleToRawLongBits >> 40) & 255));
            bArr[6] = (byte) ((int) ((doubleToRawLongBits >> 48) & 255));
            bArr[7] = (byte) ((int) ((doubleToRawLongBits >> 56) & 255));
        } else {
            bArr[7] = (byte) ((int) ((doubleToRawLongBits >> 0) & 255));
            bArr[6] = (byte) ((int) ((doubleToRawLongBits >> 8) & 255));
            bArr[5] = (byte) ((int) ((doubleToRawLongBits >> 16) & 255));
            bArr[4] = (byte) ((int) ((doubleToRawLongBits >> 24) & 255));
            bArr[3] = (byte) ((int) ((doubleToRawLongBits >> 32) & 255));
            bArr[2] = (byte) ((int) ((doubleToRawLongBits >> 40) & 255));
            bArr[1] = (byte) ((int) ((doubleToRawLongBits >> 48) & 255));
            bArr[0] = (byte) ((int) ((doubleToRawLongBits >> 56) & 255));
        }
        return bArr;
    }

    protected final byte[] convertFloatArrayToByteArray(float[] fArr, int i) {
        byte[] bArr = new byte[(fArr.length * 4)];
        for (int i2 = 0; i2 < fArr.length; i2++) {
            int floatToRawIntBits = Float.floatToRawIntBits(fArr[i2]);
            int i3 = i2 * 4;
            if (i == 77) {
                bArr[i3 + 0] = (byte) ((floatToRawIntBits >> 0) & 255);
                bArr[i3 + 1] = (byte) ((floatToRawIntBits >> 8) & 255);
                bArr[i3 + 2] = (byte) ((floatToRawIntBits >> 16) & 255);
                bArr[i3 + 3] = (byte) ((floatToRawIntBits >> 24) & 255);
            } else {
                bArr[i3 + 3] = (byte) ((floatToRawIntBits >> 0) & 255);
                bArr[i3 + 2] = (byte) ((floatToRawIntBits >> 8) & 255);
                bArr[i3 + 1] = (byte) ((floatToRawIntBits >> 16) & 255);
                bArr[i3 + 0] = (byte) ((floatToRawIntBits >> 24) & 255);
            }
        }
        return bArr;
    }

    protected final byte[] convertFloatToByteArray(float f, int i) {
        byte[] bArr = new byte[4];
        int floatToRawIntBits = Float.floatToRawIntBits(f);
        if (i == 77) {
            bArr[0] = (byte) ((floatToRawIntBits >> 0) & 255);
            bArr[1] = (byte) ((floatToRawIntBits >> 8) & 255);
            bArr[2] = (byte) ((floatToRawIntBits >> 16) & 255);
            bArr[3] = (byte) ((floatToRawIntBits >> 24) & 255);
        } else {
            bArr[3] = (byte) ((floatToRawIntBits >> 0) & 255);
            bArr[2] = (byte) ((floatToRawIntBits >> 8) & 255);
            bArr[1] = (byte) ((floatToRawIntBits >> 16) & 255);
            bArr[0] = (byte) ((floatToRawIntBits >> 24) & 255);
        }
        return bArr;
    }

    protected final byte[] convertIntArrayToByteArray(int[] iArr, int i) {
        byte[] bArr = new byte[(iArr.length * 4)];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            writeIntInToByteArray(iArr[i2], bArr, i2 * 4, i);
        }
        return bArr;
    }

    protected final byte[] convertIntArrayToRationalArray(int[] iArr, int[] iArr2, int i) throws ImageWriteException {
        if (iArr.length != iArr2.length) {
            throw new ImageWriteException("numerators.length (" + iArr.length + " != denominators.length (" + iArr2.length + ")");
        }
        byte[] bArr = new byte[(iArr.length * 8)];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            writeIntInToByteArray(iArr[i2], bArr, i2 * 8, i);
            writeIntInToByteArray(iArr2[i2], bArr, (i2 * 8) + 4, i);
        }
        return bArr;
    }

    protected final byte[] convertRationalArrayToByteArray(RationalNumber[] rationalNumberArr, int i) throws ImageWriteException {
        byte[] bArr = new byte[(rationalNumberArr.length * 8)];
        for (int i2 = 0; i2 < rationalNumberArr.length; i2++) {
            writeIntInToByteArray(rationalNumberArr[i2].numerator, bArr, i2 * 8, i);
            writeIntInToByteArray(rationalNumberArr[i2].divisor, bArr, (i2 * 8) + 4, i);
        }
        return bArr;
    }

    protected final byte[] convertRationalToByteArray(RationalNumber rationalNumber, int i) throws ImageWriteException {
        byte[] bArr = new byte[8];
        writeIntInToByteArray(rationalNumber.numerator, bArr, 0, i);
        writeIntInToByteArray(rationalNumber.divisor, bArr, 4, i);
        return bArr;
    }

    protected final byte[] convertShortArrayToByteArray(int[] iArr, int i) {
        byte[] bArr = new byte[(iArr.length * 2)];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = iArr[i2];
            if (i == 77) {
                bArr[(i2 * 2) + 0] = (byte) (i3 >> 8);
                bArr[(i2 * 2) + 1] = (byte) (i3 >> 0);
            } else {
                bArr[(i2 * 2) + 1] = (byte) (i3 >> 8);
                bArr[(i2 * 2) + 0] = (byte) (i3 >> 0);
            }
        }
        return bArr;
    }

    protected final byte[] convertShortToByteArray(int i, int i2) {
        byte[] bArr = new byte[2];
        if (i2 == 77) {
            bArr[0] = (byte) (i >> 8);
            bArr[1] = (byte) (i >> 0);
        } else {
            bArr[1] = (byte) (i >> 8);
            bArr[0] = (byte) (i >> 0);
        }
        return bArr;
    }

    public final void copyStreamToStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read > 0) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public final void debugByteArray(String str, byte[] bArr) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": ").append(bArr.length).toString());
        int i = 0;
        while (i < bArr.length && i < 50) {
            debugNumber("\t (" + i + ")", bArr[i] & 255);
            i++;
        }
    }

    public final void debugNumber(PrintWriter printWriter, String str, int i) {
        debugNumber(printWriter, str, i, 1);
    }

    public final void debugNumber(PrintWriter printWriter, String str, int i, int i2) {
        printWriter.print(new StringBuilder(String.valueOf(str)).append(": ").append(i).append(" (").toString());
        int i3 = i;
        for (int i4 = 0; i4 < i2; i4++) {
            if (i4 > 0) {
                printWriter.print(",");
            }
            int i5 = i3 & 255;
            printWriter.print(((char) i5) + " [" + i5 + "]");
            i3 >>= 8;
        }
        printWriter.println(") [0x" + Integer.toHexString(i) + ", " + Integer.toBinaryString(i) + "]");
        printWriter.flush();
    }

    public final void debugNumber(String str, int i) {
        debugNumber(str, i, 1);
    }

    public final void debugNumber(String str, int i, int i2) {
        PrintWriter printWriter = new PrintWriter(System.out);
        debugNumber(printWriter, str, i, i2);
        printWriter.flush();
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

    protected final byte[] getByteArrayTail(String str, byte[] bArr, int i) throws ImageReadException {
        return readBytearray(str, bArr, i, bArr.length - i);
    }

    protected final byte[] getBytearrayHead(String str, byte[] bArr, int i) throws ImageReadException {
        return readBytearray(str, bArr, 0, bArr.length - i);
    }

    public final boolean getDebug() {
        return this.debug;
    }

    protected final byte[] getRAFBytes(RandomAccessFile randomAccessFile, long j, int i, String str) throws IOException {
        if (this.debug) {
            System.out.println("getRAFBytes pos: " + j);
            System.out.println("getRAFBytes length: " + i);
        }
        byte[] bArr = new byte[i];
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

    public final byte[] getStreamBytes(InputStream inputStream) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStreamToStream(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected final void printByteBits(String str, byte b) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": '").append(Integer.toBinaryString(b & 255)).toString());
    }

    protected final void printCharQuad(PrintWriter printWriter, String str, int i) {
        printWriter.println(new StringBuilder(String.valueOf(str)).append(": '").append((char) ((i >> 24) & 255)).append((char) ((i >> 16) & 255)).append((char) ((i >> 8) & 255)).append((char) ((i >> 0) & 255)).append("'").toString());
    }

    protected final void printCharQuad(String str, int i) {
        System.out.println(new StringBuilder(String.valueOf(str)).append(": '").append((char) ((i >> 24) & 255)).append((char) ((i >> 16) & 255)).append((char) ((i >> 8) & 255)).append((char) ((i >> 0) & 255)).append("'").toString());
    }

    protected final int read2Bytes(String str, InputStream inputStream, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[2];
        int i2 = 0;
        while (i2 < 2) {
            int read = inputStream.read(bArr, i2, 2 - i2);
            if (read < 1) {
                throw new IOException(str2);
            }
            i2 += read;
        }
        return convertByteArrayToShort(str, bArr, i);
    }

    protected final int read3Bytes(String str, InputStream inputStream, String str2, int i) throws ImageReadException, IOException {
        int i2;
        byte read = (byte) inputStream.read();
        byte read2 = (byte) inputStream.read();
        byte read3 = (byte) inputStream.read();
        if (i == 77) {
            i2 = (((read & 255) << 16) | ((read2 & 255) << 8)) | ((read3 & 255) << 0);
        } else {
            i2 = (read & 255) << 0;
            i2 |= ((read2 & 255) << 8) | ((read3 & 255) << 16);
        }
        if (this.debug) {
            debugNumber(str, i2, 3);
        }
        return i2;
    }

    protected final int read4Bytes(String str, InputStream inputStream, String str2, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[4];
        int i2 = 0;
        while (i2 < 4) {
            int read = inputStream.read(bArr, i2, 4 - i2);
            if (read < 1) {
                throw new IOException(str2);
            }
            i2 += read;
        }
        return convertByteArrayToInt(str, bArr, i);
    }

    public final void readAndVerifyBytes(InputStream inputStream, byte[] bArr, String str) throws ImageReadException, IOException {
        int i = 0;
        while (i < bArr.length) {
            int read = inputStream.read();
            byte b = (byte) (read & 255);
            if (read < 0) {
                throw new ImageReadException("Unexpected EOF.");
            } else if (b != bArr[i]) {
                throw new ImageReadException(str);
            } else {
                i++;
            }
        }
    }

    protected final void readAndVerifyBytes(String str, InputStream inputStream, byte[] bArr, String str2) throws ImageReadException, IOException {
        byte[] readByteArray = readByteArray(str, bArr.length, inputStream, str2);
        for (int i = 0; i < bArr.length; i++) {
            if (readByteArray[i] != bArr[i]) {
                throw new ImageReadException(str2);
            }
        }
    }

    public final byte readByte(String str, InputStream inputStream, String str2) throws ImageReadException, IOException {
        int read = inputStream.read();
        if (read < 0) {
            System.out.println(new StringBuilder(String.valueOf(str)).append(": ").append(read).toString());
            throw new IOException(str2);
        }
        if (this.debug) {
            debugNumber(str, read);
        }
        return (byte) (read & 255);
    }

    public final byte[] readByteArray(String str, int i, InputStream inputStream) throws IOException {
        return readByteArray(str, i, inputStream, new StringBuilder(String.valueOf(str)).append(" could not be read.").toString());
    }

    public final byte[] readByteArray(String str, int i, InputStream inputStream, String str2) throws IOException {
        int i2 = 0;
        byte[] bArr = new byte[i];
        int i3 = 0;
        while (i3 < i) {
            int read = inputStream.read(bArr, i3, i - i3);
            if (read < 1) {
                throw new IOException(str2);
            }
            i3 += read;
        }
        if (this.debug) {
            while (i2 < i && i2 < 50) {
                debugNumber(new StringBuilder(String.valueOf(str)).append(" (").append(i2).append(")").toString(), bArr[i2] & 255);
                i2++;
            }
        }
        return bArr;
    }

    public final byte[] readBytearray(String str, byte[] bArr, int i, int i2) throws ImageReadException {
        if (bArr.length < i + i2) {
            throw new ImageReadException("Invalid read. bytes.length: " + bArr.length + ", start: " + i + ", count: " + i2);
        }
        Object obj = new byte[i2];
        System.arraycopy(bArr, i, obj, 0, i2);
        if (this.debug) {
            debugByteArray(str, obj);
        }
        return obj;
    }

    public final byte[] readBytes(InputStream inputStream, int i) throws ImageReadException, IOException {
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr[i2] = (byte) inputStream.read();
        }
        return bArr;
    }

    protected final void readRandomBytes(InputStream inputStream) throws ImageReadException, IOException {
        for (int i = 0; i < 100; i++) {
            readByte(i, inputStream, "Random Data");
        }
    }

    protected final void scanForByte(InputStream inputStream, byte b) throws IOException {
        int i = 0;
        int i2 = 0;
        while (i < 3) {
            int read = inputStream.read();
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

    public final void setDebug(boolean z) {
        this.debug = z;
    }

    protected void skipBytes(InputStream inputStream, int i) throws IOException {
        skipBytes(inputStream, i, "Couldn't skip bytes");
    }

    public final void skipBytes(InputStream inputStream, int i, String str) throws IOException {
        long j = 0;
        while (((long) i) != j) {
            long skip = inputStream.skip(((long) i) - j);
            if (skip < 1) {
                throw new IOException(new StringBuilder(String.valueOf(str)).append(" (").append(skip).append(")").toString());
            }
            j += skip;
        }
    }

    public final boolean startsWith(byte[] bArr, byte[] bArr2) {
        if (bArr2 == null || bArr == null || bArr2.length > bArr.length) {
            return false;
        }
        for (int i = 0; i < bArr2.length; i++) {
            if (bArr2[i] != bArr[i]) {
                return false;
            }
        }
        return true;
    }

    protected final void writeIntInToByteArray(int i, byte[] bArr, int i2, int i3) {
        if (i3 == 77) {
            bArr[i2 + 0] = (byte) (i >> 24);
            bArr[i2 + 1] = (byte) (i >> 16);
            bArr[i2 + 2] = (byte) (i >> 8);
            bArr[i2 + 3] = (byte) (i >> 0);
            return;
        }
        bArr[i2 + 3] = (byte) (i >> 24);
        bArr[i2 + 2] = (byte) (i >> 16);
        bArr[i2 + 1] = (byte) (i >> 8);
        bArr[i2 + 0] = (byte) (i >> 0);
    }
}
