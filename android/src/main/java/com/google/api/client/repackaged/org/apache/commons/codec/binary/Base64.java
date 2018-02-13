package com.google.api.client.repackaged.org.apache.commons.codec.binary;

import com.google.common.base.Ascii;
import java.math.BigInteger;
import org.mortbay.jetty.HttpTokens;

public class Base64 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 6;
    private static final int BYTES_PER_ENCODED_BLOCK = 4;
    private static final int BYTES_PER_UNENCODED_BLOCK = 3;
    static final byte[] CHUNK_SEPARATOR = new byte[]{(byte) 13, (byte) 10};
    private static final byte[] DECODE_TABLE = new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 62, (byte) -1, (byte) 62, (byte) -1, (byte) 63, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, HttpTokens.COLON, HttpTokens.SEMI_COLON, (byte) 60, (byte) 61, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, Ascii.VT, Ascii.FF, (byte) 13, Ascii.SO, Ascii.SI, Ascii.DLE, (byte) 17, Ascii.DC2, (byte) 19, Ascii.DC4, Ascii.NAK, Ascii.SYN, Ascii.ETB, Ascii.CAN, Ascii.EM, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 63, (byte) -1, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51};
    private static final int MASK_6BITS = 63;
    private static final byte[] STANDARD_ENCODE_TABLE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
    private int bitWorkArea;
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;

    public Base64() {
        this(0);
    }

    public Base64(int i) {
        this(i, CHUNK_SEPARATOR);
    }

    public Base64(int i, byte[] bArr) {
        this(i, bArr, false);
    }

    public Base64(int i, byte[] bArr, boolean z) {
        super(3, 4, i, bArr == null ? 0 : bArr.length);
        this.decodeTable = DECODE_TABLE;
        if (bArr == null) {
            this.encodeSize = 4;
            this.lineSeparator = null;
        } else if (containsAlphabetOrPad(bArr)) {
            throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + StringUtils.newStringUtf8(bArr) + "]");
        } else if (i > 0) {
            this.encodeSize = bArr.length + 4;
            this.lineSeparator = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.lineSeparator, 0, bArr.length);
        } else {
            this.encodeSize = 4;
            this.lineSeparator = null;
        }
        this.decodeSize = this.encodeSize - 1;
        this.encodeTable = z ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
    }

    public Base64(boolean z) {
        this(76, CHUNK_SEPARATOR, z);
    }

    public static byte[] decodeBase64(String str) {
        return new Base64().decode(str);
    }

    public static byte[] decodeBase64(byte[] bArr) {
        return new Base64().decode(bArr);
    }

    public static BigInteger decodeInteger(byte[] bArr) {
        return new BigInteger(1, decodeBase64(bArr));
    }

    public static byte[] encodeBase64(byte[] bArr) {
        return encodeBase64(bArr, false);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z) {
        return encodeBase64(bArr, z, false);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z, boolean z2) {
        return encodeBase64(bArr, z, z2, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z, boolean z2, int i) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        Base64 base64 = z ? new Base64(z2) : new Base64(0, CHUNK_SEPARATOR, z2);
        long encodedLength = base64.getEncodedLength(bArr);
        if (encodedLength <= ((long) i)) {
            return base64.encode(bArr);
        }
        throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + encodedLength + ") than the specified maximum size of " + i);
    }

    public static byte[] encodeBase64Chunked(byte[] bArr) {
        return encodeBase64(bArr, true);
    }

    public static String encodeBase64String(byte[] bArr) {
        return StringUtils.newStringUtf8(encodeBase64(bArr, false));
    }

    public static byte[] encodeBase64URLSafe(byte[] bArr) {
        return encodeBase64(bArr, false, true);
    }

    public static String encodeBase64URLSafeString(byte[] bArr) {
        return StringUtils.newStringUtf8(encodeBase64(bArr, false, true));
    }

    public static byte[] encodeInteger(BigInteger bigInteger) {
        if (bigInteger != null) {
            return encodeBase64(toIntegerBytes(bigInteger), false);
        }
        throw new NullPointerException("encodeInteger called with null parameter");
    }

    public static boolean isArrayByteBase64(byte[] bArr) {
        return isBase64(bArr);
    }

    public static boolean isBase64(byte b) {
        return b == (byte) 61 || (b >= (byte) 0 && b < DECODE_TABLE.length && DECODE_TABLE[b] != (byte) -1);
    }

    public static boolean isBase64(String str) {
        return isBase64(StringUtils.getBytesUtf8(str));
    }

    public static boolean isBase64(byte[] bArr) {
        int i = 0;
        while (i < bArr.length) {
            if (!isBase64(bArr[i]) && !BaseNCodec.isWhiteSpace(bArr[i])) {
                return false;
            }
            i++;
        }
        return true;
    }

    static byte[] toIntegerBytes(BigInteger bigInteger) {
        int bitLength = ((bigInteger.bitLength() + 7) >> 3) << 3;
        Object toByteArray = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 != 0 && (bigInteger.bitLength() / 8) + 1 == bitLength / 8) {
            return toByteArray;
        }
        int i = 0;
        int length = toByteArray.length;
        if (bigInteger.bitLength() % 8 == 0) {
            i = 1;
            length--;
        }
        int i2 = bitLength / 8;
        Object obj = new byte[(bitLength / 8)];
        System.arraycopy(toByteArray, i, obj, i2 - length, length);
        return obj;
    }

    void decode(byte[] bArr, int i, int i2) {
        if (!this.eof) {
            if (i2 < 0) {
                this.eof = true;
            }
            int i3 = 0;
            while (i3 < i2) {
                ensureBufferSize(this.decodeSize);
                byte b = bArr[i];
                if (b == (byte) 61) {
                    this.eof = true;
                    break;
                }
                if (b >= (byte) 0 && b < DECODE_TABLE.length) {
                    b = DECODE_TABLE[b];
                    if (b >= (byte) 0) {
                        this.modulus = (this.modulus + 1) % 4;
                        this.bitWorkArea = b + (this.bitWorkArea << 6);
                        if (this.modulus == 0) {
                            byte[] bArr2 = this.buffer;
                            int i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) ((this.bitWorkArea >> 16) & 255);
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) ((this.bitWorkArea >> 8) & 255);
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) (this.bitWorkArea & 255);
                        }
                    }
                }
                i3++;
                i++;
            }
            if (this.eof && this.modulus != 0) {
                ensureBufferSize(this.decodeSize);
                byte[] bArr3;
                int i5;
                switch (this.modulus) {
                    case 2:
                        this.bitWorkArea >>= 4;
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) (this.bitWorkArea & 255);
                        return;
                    case 3:
                        this.bitWorkArea >>= 2;
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((this.bitWorkArea >> 8) & 255);
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) (this.bitWorkArea & 255);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    void encode(byte[] bArr, int i, int i2) {
        if (!this.eof) {
            int i3;
            int i4;
            if (i2 < 0) {
                this.eof = true;
                if (this.modulus != 0 || this.lineLength != 0) {
                    ensureBufferSize(this.encodeSize);
                    i3 = this.pos;
                    byte[] bArr2;
                    switch (this.modulus) {
                        case 1:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[(this.bitWorkArea >> 2) & 63];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[(this.bitWorkArea << 4) & 63];
                            if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                                bArr2 = this.buffer;
                                i4 = this.pos;
                                this.pos = i4 + 1;
                                bArr2[i4] = (byte) 61;
                                bArr2 = this.buffer;
                                i4 = this.pos;
                                this.pos = i4 + 1;
                                bArr2[i4] = (byte) 61;
                                break;
                            }
                            break;
                        case 2:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[(this.bitWorkArea >> 10) & 63];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[(this.bitWorkArea >> 4) & 63];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[(this.bitWorkArea << 2) & 63];
                            if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                                bArr2 = this.buffer;
                                i4 = this.pos;
                                this.pos = i4 + 1;
                                bArr2[i4] = (byte) 61;
                                break;
                            }
                            break;
                    }
                    this.currentLinePos = (this.pos - i3) + this.currentLinePos;
                    if (this.lineLength > 0 && this.currentLinePos > 0) {
                        System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
                        this.pos += this.lineSeparator.length;
                        return;
                    }
                    return;
                }
                return;
            }
            for (int i5 = 0; i5 < i2; i5++) {
                ensureBufferSize(this.encodeSize);
                this.modulus = (this.modulus + 1) % 3;
                i3 = bArr[i];
                if (i3 < 0) {
                    i3 += 256;
                }
                this.bitWorkArea = i3 + (this.bitWorkArea << 8);
                if (this.modulus == 0) {
                    byte[] bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[(this.bitWorkArea >> 18) & 63];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[(this.bitWorkArea >> 12) & 63];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[(this.bitWorkArea >> 6) & 63];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[this.bitWorkArea & 63];
                    this.currentLinePos += 4;
                    if (this.lineLength > 0 && this.lineLength <= this.currentLinePos) {
                        System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
                        this.pos += this.lineSeparator.length;
                        this.currentLinePos = 0;
                    }
                }
                i++;
            }
        }
    }

    protected boolean isInAlphabet(byte b) {
        return b >= (byte) 0 && b < this.decodeTable.length && this.decodeTable[b] != (byte) -1;
    }

    public boolean isUrlSafe() {
        return this.encodeTable == URL_SAFE_ENCODE_TABLE;
    }
}
