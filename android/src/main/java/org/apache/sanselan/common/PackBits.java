package org.apache.sanselan.common;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PackBits {
    private int findNextDuplicate(byte[] bArr, int i) {
        if (i < bArr.length) {
            byte b = bArr[i];
            int i2 = i + 1;
            while (i2 < bArr.length) {
                byte b2 = bArr[i2];
                if (b2 == b) {
                    return i2 - 1;
                }
                i2++;
                b = b2;
            }
        }
        return -1;
    }

    private int findRunLength(byte[] bArr, int i) {
        byte b = bArr[i];
        int i2 = i + 1;
        while (i2 < bArr.length && bArr[i2] == b) {
            i2++;
        }
        return i2 - i;
    }

    public byte[] compress(byte[] bArr) throws IOException {
        MyByteArrayOutputStream myByteArrayOutputStream = new MyByteArrayOutputStream(bArr.length * 2);
        int i = 0;
        int i2 = 0;
        while (i2 < bArr.length) {
            i++;
            int findNextDuplicate = findNextDuplicate(bArr, i2);
            int min;
            if (findNextDuplicate == i2) {
                min = Math.min(findRunLength(bArr, findNextDuplicate), 128);
                myByteArrayOutputStream.write(-(min - 1));
                myByteArrayOutputStream.write(bArr[i2]);
                i2 += min;
            } else {
                int i3 = findNextDuplicate - i2;
                if (findNextDuplicate > 0) {
                    min = findRunLength(bArr, findNextDuplicate);
                    if (min < 3) {
                        int i4 = (i2 + i3) + min;
                        min = findNextDuplicate(bArr, i4);
                        if (min != i4) {
                            int i5 = min;
                            min -= i2;
                            findNextDuplicate = i5;
                            if (findNextDuplicate < 0) {
                                min = bArr.length - i2;
                            }
                            findNextDuplicate = Math.min(min, 128);
                            myByteArrayOutputStream.write(findNextDuplicate - 1);
                            for (min = 0; min < findNextDuplicate; min++) {
                                myByteArrayOutputStream.write(bArr[i2]);
                                i2++;
                            }
                        }
                    }
                }
                min = i3;
                if (findNextDuplicate < 0) {
                    min = bArr.length - i2;
                }
                findNextDuplicate = Math.min(min, 128);
                myByteArrayOutputStream.write(findNextDuplicate - 1);
                for (min = 0; min < findNextDuplicate; min++) {
                    myByteArrayOutputStream.write(bArr[i2]);
                    i2++;
                }
            }
        }
        return myByteArrayOutputStream.toByteArray();
    }

    public byte[] decompress(byte[] bArr, int i) throws ImageReadException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            if (i3 >= bArr.length) {
                throw new ImageReadException("Tiff: Unpack bits source exhausted: " + i3 + ", done + " + i2 + ", expected + " + i);
            }
            int i4 = i3 + 1;
            byte b = bArr[i3];
            int i5;
            if (b >= (byte) 0 && b <= Ascii.DEL) {
                i5 = b + 1;
                i3 = i2 + i5;
                i2 = i4;
                i4 = 0;
                while (i4 < i5) {
                    byteArrayOutputStream.write(bArr[i2]);
                    i4++;
                    i2++;
                }
                int i6 = i3;
                i3 = i2;
                i2 = i6;
            } else if (b >= (byte) -127 && b <= (byte) -1) {
                i3 = i4 + 1;
                byte b2 = bArr[i4];
                i5 = (-b) + 1;
                i2 += i5;
                for (i4 = 0; i4 < i5; i4++) {
                    byteArrayOutputStream.write(b2);
                }
            } else if (b == UnsignedBytes.MAX_POWER_OF_TWO) {
                throw new ImageReadException("Packbits: " + b);
            } else {
                i3 = i4;
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
