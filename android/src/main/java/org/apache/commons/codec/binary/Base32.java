package org.apache.commons.codec.binary;

import com.google.common.base.Ascii;

public class Base32 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 5;
    private static final int BYTES_PER_ENCODED_BLOCK = 8;
    private static final int BYTES_PER_UNENCODED_BLOCK = 5;
    private static final byte[] CHUNK_SEPARATOR = new byte[]{(byte) 13, (byte) 10};
    private static final byte[] DECODE_TABLE = new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 63, (byte) -1, (byte) -1, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, Ascii.VT, Ascii.FF, (byte) 13, Ascii.SO, Ascii.SI, Ascii.DLE, (byte) 17, Ascii.DC2, (byte) 19, Ascii.DC4, Ascii.NAK, Ascii.SYN, Ascii.ETB, Ascii.CAN, Ascii.EM};
    private static final byte[] ENCODE_TABLE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55};
    private static final byte[] HEX_DECODE_TABLE = new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 63, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 10, Ascii.VT, Ascii.FF, (byte) 13, Ascii.SO, Ascii.SI, Ascii.DLE, (byte) 17, Ascii.DC2, (byte) 19, Ascii.DC4, Ascii.NAK, Ascii.SYN, Ascii.ETB, Ascii.CAN, Ascii.EM, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, (byte) 32};
    private static final byte[] HEX_ENCODE_TABLE = new byte[]{(byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86};
    private static final int MASK_5BITS = 31;
    private long bitWorkArea;
    private final int decodeSize;
    private final byte[] decodeTable;
    private final int encodeSize;
    private final byte[] encodeTable;
    private final byte[] lineSeparator;

    public Base32() {
        this(false);
    }

    public Base32(int i) {
        this(i, CHUNK_SEPARATOR);
    }

    public Base32(int i, byte[] bArr) {
        this(i, bArr, false);
    }

    public Base32(int i, byte[] bArr, boolean z) {
        super(5, 8, i, bArr == null ? 0 : bArr.length);
        if (z) {
            this.encodeTable = HEX_ENCODE_TABLE;
            this.decodeTable = HEX_DECODE_TABLE;
        } else {
            this.encodeTable = ENCODE_TABLE;
            this.decodeTable = DECODE_TABLE;
        }
        if (i <= 0) {
            this.encodeSize = 8;
            this.lineSeparator = null;
        } else if (bArr == null) {
            throw new IllegalArgumentException("lineLength " + i + " > 0, but lineSeparator is null");
        } else if (containsAlphabetOrPad(bArr)) {
            throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + StringUtils.newStringUtf8(bArr) + "]");
        } else {
            this.encodeSize = bArr.length + 8;
            this.lineSeparator = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.lineSeparator, 0, bArr.length);
        }
        this.decodeSize = this.encodeSize - 1;
    }

    public Base32(boolean z) {
        this(0, null, z);
    }

    void decode(byte[] bArr, int i, int i2) {
        if (!this.eof) {
            if (i2 < 0) {
                this.eof = true;
            }
            int i3 = 0;
            while (i3 < i2) {
                byte b = bArr[i];
                if (b == (byte) 61) {
                    this.eof = true;
                    break;
                }
                ensureBufferSize(this.decodeSize);
                if (b >= (byte) 0 && b < this.decodeTable.length) {
                    b = this.decodeTable[b];
                    if (b >= (byte) 0) {
                        this.modulus = (this.modulus + 1) % 8;
                        this.bitWorkArea = (this.bitWorkArea << 5) + ((long) b);
                        if (this.modulus == 0) {
                            byte[] bArr2 = this.buffer;
                            int i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) ((int) ((this.bitWorkArea >> 32) & 255));
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) ((int) ((this.bitWorkArea >> 24) & 255));
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) ((int) ((this.bitWorkArea >> 16) & 255));
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) ((int) (this.bitWorkArea & 255));
                        }
                    }
                }
                i3++;
                i++;
            }
            if (this.eof && this.modulus >= 2) {
                ensureBufferSize(this.decodeSize);
                byte[] bArr3;
                int i5;
                switch (this.modulus) {
                    case 2:
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 2) & 255));
                        return;
                    case 3:
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 7) & 255));
                        return;
                    case 4:
                        this.bitWorkArea >>= 4;
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) (this.bitWorkArea & 255));
                        return;
                    case 5:
                        this.bitWorkArea >>= 1;
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 16) & 255));
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) (this.bitWorkArea & 255));
                        return;
                    case 6:
                        this.bitWorkArea >>= 6;
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 16) & 255));
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) (this.bitWorkArea & 255));
                        return;
                    case 7:
                        this.bitWorkArea >>= 3;
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 24) & 255));
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 16) & 255));
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) ((this.bitWorkArea >> 8) & 255));
                        bArr3 = this.buffer;
                        i5 = this.pos;
                        this.pos = i5 + 1;
                        bArr3[i5] = (byte) ((int) (this.bitWorkArea & 255));
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
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 3)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea << 2)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            break;
                        case 2:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 11)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 6)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 1)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea << 4)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            break;
                        case 3:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 19)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 14)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 9)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 4)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea << 1)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
                            break;
                        case 4:
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 27)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 22)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 17)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 12)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 7)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea >> 2)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = this.encodeTable[((int) (this.bitWorkArea << 3)) & 31];
                            bArr2 = this.buffer;
                            i4 = this.pos;
                            this.pos = i4 + 1;
                            bArr2[i4] = (byte) 61;
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
                this.modulus = (this.modulus + 1) % 5;
                i3 = bArr[i];
                if (i3 < 0) {
                    i3 += 256;
                }
                this.bitWorkArea = (this.bitWorkArea << 8) + ((long) i3);
                if (this.modulus == 0) {
                    byte[] bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[((int) (this.bitWorkArea >> 35)) & 31];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[((int) (this.bitWorkArea >> 30)) & 31];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[((int) (this.bitWorkArea >> 25)) & 31];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[((int) (this.bitWorkArea >> 20)) & 31];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[((int) (this.bitWorkArea >> 15)) & 31];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[((int) (this.bitWorkArea >> 10)) & 31];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[((int) (this.bitWorkArea >> 5)) & 31];
                    bArr3 = this.buffer;
                    i4 = this.pos;
                    this.pos = i4 + 1;
                    bArr3[i4] = this.encodeTable[((int) this.bitWorkArea) & 31];
                    this.currentLinePos += 8;
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

    public boolean isInAlphabet(byte b) {
        return b >= (byte) 0 && b < this.decodeTable.length && this.decodeTable[b] != (byte) -1;
    }
}
