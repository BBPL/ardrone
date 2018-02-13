package com.google.protobuf;

import org.apache.sanselan.formats.jpeg.JpegConstants;

public class Internal {

    public interface EnumLiteMap<T extends EnumLite> {
        T findValueByNumber(int i);
    }

    public interface EnumLite {
        int getNumber();
    }

    public static ByteString bytesDefaultValue(String str) {
        try {
            return ByteString.copyFrom(str.getBytes("ISO-8859-1"));
        } catch (Throwable e) {
            throw new IllegalStateException("Java VM does not support a standard character set.", e);
        }
    }

    public static boolean isValidUtf8(ByteString byteString) {
        int size = byteString.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            int byteAt = byteString.byteAt(i) & 255;
            if (byteAt < 128) {
                i = i2;
            } else {
                if (byteAt >= 194 && byteAt <= 244 && i2 < size) {
                    int i3 = i2 + 1;
                    i2 = byteString.byteAt(i2) & 255;
                    if (i2 < 128 || i2 > 191) {
                        return false;
                    }
                    if (byteAt <= 223) {
                        i = i3;
                    } else if (i3 >= size) {
                        return false;
                    } else {
                        i = i3 + 1;
                        i3 = byteString.byteAt(i3) & 255;
                        if (i3 >= 128 && i3 <= 191) {
                            if (byteAt <= 239) {
                                if (byteAt != JpegConstants.JPEG_APP0 || i2 >= 160) {
                                    if (byteAt != 237) {
                                        continue;
                                    } else if (i2 <= 159) {
                                    }
                                }
                            } else if (i < size) {
                                i3 = byteString.byteAt(i) & 255;
                                if (i3 < 128 || i3 > 191) {
                                    return false;
                                }
                                if ((byteAt == 240 && i2 < 144) || (byteAt == 244 && i2 > 143)) {
                                    return false;
                                }
                                i++;
                            }
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static String stringDefaultValue(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (Throwable e) {
            throw new IllegalStateException("Java VM does not support a standard character set.", e);
        }
    }
}
