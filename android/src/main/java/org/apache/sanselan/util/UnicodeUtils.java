package org.apache.sanselan.util;

import org.apache.sanselan.common.BinaryConstants;

public abstract class UnicodeUtils implements BinaryConstants {
    public static final int CHAR_ENCODING_CODE_AMBIGUOUS = -1;
    public static final int CHAR_ENCODING_CODE_ISO_8859_1 = 0;
    public static final int CHAR_ENCODING_CODE_UTF_16_BIG_ENDIAN_NO_BOM = 3;
    public static final int CHAR_ENCODING_CODE_UTF_16_BIG_ENDIAN_WITH_BOM = 1;
    public static final int CHAR_ENCODING_CODE_UTF_16_LITTLE_ENDIAN_NO_BOM = 4;
    public static final int CHAR_ENCODING_CODE_UTF_16_LITTLE_ENDIAN_WITH_BOM = 2;
    public static final int CHAR_ENCODING_CODE_UTF_8 = 5;

    public static class UnicodeException extends Exception {
        public UnicodeException(String str) {
            super(str);
        }
    }

    private static class UnicodeMetricsASCII extends UnicodeUtils {
        private UnicodeMetricsASCII() {
            super();
        }

        public int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException {
            while (i < bArr.length) {
                if (bArr[i] == (byte) 0) {
                    return z ? i + 1 : i;
                } else {
                    i++;
                }
            }
            return bArr.length;
        }
    }

    private static abstract class UnicodeMetricsUTF16 extends UnicodeUtils {
        protected static final int BYTE_ORDER_BIG_ENDIAN = 0;
        protected static final int BYTE_ORDER_LITTLE_ENDIAN = 1;
        protected int byteOrder = 0;

        public UnicodeMetricsUTF16(int i) {
            super();
            this.byteOrder = i;
        }

        public int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException {
            while (i != bArr.length) {
                if (i > bArr.length - 1) {
                    throw new UnicodeException("Terminator not found.");
                }
                int i2 = i + 1;
                int i3 = bArr[i] & 255;
                i = i2 + 1;
                int i4 = bArr[i2] & 255;
                i2 = this.byteOrder == 0 ? i3 : i4;
                if (i3 == 0 && i4 == 0) {
                    return !z ? i - 2 : i;
                } else {
                    if (i2 >= 216) {
                        if (i > bArr.length - 1) {
                            throw new UnicodeException("Terminator not found.");
                        }
                        i2 = i + 1;
                        byte b = bArr[i];
                        i = i2 + 1;
                        if ((this.byteOrder == 0 ? b & 255 : bArr[i2] & 255) < 220) {
                            throw new UnicodeException("Invalid code point.");
                        }
                    }
                }
            }
            return bArr.length;
        }

        public boolean isValid(byte[] bArr, int i, boolean z, boolean z2) throws UnicodeException {
            while (i != bArr.length) {
                if (i >= bArr.length - 1) {
                    break;
                }
                int i2 = i + 1;
                int i3 = bArr[i] & 255;
                i = i2 + 1;
                int i4 = bArr[i2] & 255;
                i2 = this.byteOrder == 0 ? i3 : i4;
                if (i3 != 0 || i4 != 0) {
                    if (i2 >= 216) {
                        if (i2 >= 220 || i >= bArr.length - 1) {
                            break;
                        }
                        i2 = i + 1;
                        byte b = bArr[i];
                        i = i2 + 1;
                        if ((this.byteOrder == 0 ? b & 255 : bArr[i2] & 255) < 220) {
                            return false;
                        }
                    }
                } else {
                    return z;
                }
            }
            if (!z2) {
                return true;
            }
            return false;
        }
    }

    private static class UnicodeMetricsUTF16NoBOM extends UnicodeMetricsUTF16 {
        public UnicodeMetricsUTF16NoBOM(int i) {
            super(i);
        }
    }

    private static class UnicodeMetricsUTF16WithBOM extends UnicodeMetricsUTF16 {
        public UnicodeMetricsUTF16WithBOM() {
            super(0);
        }

        public int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException {
            if (i >= bArr.length - 1) {
                throw new UnicodeException("Missing BOM.");
            }
            int i2 = i + 1;
            int i3 = bArr[i] & 255;
            int i4 = bArr[i2] & 255;
            if (i3 == 255 && i4 == 254) {
                this.byteOrder = 1;
            } else if (i3 == 254 && i4 == 255) {
                this.byteOrder = 0;
            } else {
                throw new UnicodeException("Invalid byte order mark.");
            }
            return super.findEnd(bArr, i2 + 1, z);
        }
    }

    private static class UnicodeMetricsUTF8 extends UnicodeUtils {
        private UnicodeMetricsUTF8() {
            super();
        }

        public int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException {
            while (i != bArr.length) {
                if (i > bArr.length) {
                    throw new UnicodeException("Terminator not found.");
                }
                int i2 = i + 1;
                int i3 = bArr[i] & 255;
                if (i3 == 0) {
                    return !z ? i2 - 1 : i2;
                } else {
                    if (i3 <= 127) {
                        i = i2;
                    } else if (i3 <= 223) {
                        if (i2 >= bArr.length) {
                            throw new UnicodeException("Invalid unicode.");
                        }
                        i = i2 + 1;
                        i2 = bArr[i2] & 255;
                        if (i2 < 128 || i2 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                    } else if (i3 <= 239) {
                        if (i2 >= bArr.length - 1) {
                            throw new UnicodeException("Invalid unicode.");
                        }
                        i3 = i2 + 1;
                        i2 = bArr[i2] & 255;
                        if (i2 < 128 || i2 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                        i2 = bArr[i3] & 255;
                        if (i2 < 128 || i2 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                        i = i3 + 1;
                    } else if (i3 > 244) {
                        throw new UnicodeException("Invalid code point.");
                    } else if (i2 >= bArr.length - 2) {
                        throw new UnicodeException("Invalid unicode.");
                    } else {
                        i3 = i2 + 1;
                        i2 = bArr[i2] & 255;
                        if (i2 < 128 || i2 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                        i2 = i3 + 1;
                        i3 = bArr[i3] & 255;
                        if (i3 < 128 || i3 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                        i = i2 + 1;
                        i2 = bArr[i2] & 255;
                        if (i2 < 128 || i2 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                    }
                }
            }
            return bArr.length;
        }
    }

    private UnicodeUtils() {
    }

    private static int findFirstDoubleByteTerminator(byte[] bArr, int i) {
        for (int i2 = i; i2 < bArr.length - 1; i2 += 2) {
            byte b = bArr[i];
            byte b2 = bArr[i + 1];
            if ((b & 255) == 0 && (b2 & 255) == 0) {
                return i2;
            }
        }
        return -1;
    }

    public static UnicodeUtils getInstance(int i) throws UnicodeException {
        switch (i) {
            case 0:
                return new UnicodeMetricsASCII();
            case 1:
            case 2:
                return new UnicodeMetricsUTF16WithBOM();
            case 3:
                return new UnicodeMetricsUTF16NoBOM(77);
            case 4:
                return new UnicodeMetricsUTF16NoBOM(73);
            case 5:
                return new UnicodeMetricsUTF8();
            default:
                throw new UnicodeException("Unknown char encoding code: " + i);
        }
    }

    public static final boolean isValidISO_8859_1(String str) {
        try {
            return str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"));
        } catch (Throwable e) {
            throw new RuntimeException("Error parsing string.", e);
        }
    }

    protected abstract int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException;

    public final int findEndWithTerminator(byte[] bArr, int i) throws UnicodeException {
        return findEnd(bArr, i, true);
    }

    public final int findEndWithoutTerminator(byte[] bArr, int i) throws UnicodeException {
        return findEnd(bArr, i, false);
    }
}
