package org.mortbay.io;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.UnsupportedEncodingException;
import org.mortbay.util.StringUtil;

public class BufferUtil {
    static final byte[] DIGIT = new byte[]{(byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70};
    static final byte MINUS = (byte) 45;
    static final byte SPACE = (byte) 32;
    private static int[] decDivisors = new int[]{1000000000, 100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1};
    private static final long[] decDivisorsL = new long[]{1000000000000000000L, 100000000000000000L, 10000000000000000L, 1000000000000000L, 100000000000000L, 10000000000000L, 1000000000000L, 100000000000L, 10000000000L, 1000000000, 100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1};
    private static int[] hexDivisors = new int[]{268435456, 16777216, AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED, 4096, 256, 16, 1};

    public static boolean isPrefix(Buffer buffer, Buffer buffer2) {
        if (buffer.length() <= buffer2.length()) {
            int index = buffer2.getIndex();
            int index2 = buffer.getIndex();
            while (index2 < buffer.putIndex()) {
                if (buffer.peek(index2) == buffer2.peek(index)) {
                    index2++;
                    index++;
                }
            }
            return true;
        }
        return false;
    }

    public static void prependHexInt(Buffer buffer, int i) {
        if (i == 0) {
            int index = buffer.getIndex() - 1;
            buffer.poke(index, (byte) 48);
            buffer.setGetIndex(index);
            return;
        }
        Object obj = null;
        if (i < 0) {
            obj = 1;
            i = -i;
        }
        int index2 = buffer.getIndex();
        while (i > 0) {
            int i2 = i >> 4;
            index2--;
            buffer.poke(index2, DIGIT[i & 15]);
            i = i2;
        }
        if (obj != null) {
            index = index2 - 1;
            buffer.poke(index, (byte) MINUS);
        } else {
            index = index2;
        }
        buffer.setGetIndex(index);
    }

    public static void putCRLF(Buffer buffer) {
        buffer.put((byte) 13);
        buffer.put((byte) 10);
    }

    public static void putDecInt(Buffer buffer, int i) {
        int i2;
        Object obj = null;
        if (i < 0) {
            buffer.put((byte) MINUS);
            if (i == Integer.MIN_VALUE) {
                buffer.put((byte) 50);
                i2 = 147483648;
            } else {
                i2 = -i;
            }
        } else {
            i2 = i;
        }
        if (i2 < 10) {
            buffer.put(DIGIT[i2]);
            return;
        }
        for (int i3 = 0; i3 < decDivisors.length; i3++) {
            if (i2 >= decDivisors[i3]) {
                obj = 1;
                int i4 = i2 / decDivisors[i3];
                buffer.put(DIGIT[i4]);
                i2 -= i4 * decDivisors[i3];
            } else if (obj != null) {
                buffer.put((byte) 48);
            }
        }
    }

    public static void putDecLong(Buffer buffer, long j) {
        long j2;
        Object obj = null;
        if (j < 0) {
            buffer.put((byte) MINUS);
            if (j == Long.MIN_VALUE) {
                buffer.put((byte) 57);
                j2 = 223372036854775808L;
            } else {
                j2 = -j;
            }
        } else {
            j2 = j;
        }
        if (j2 < 10) {
            buffer.put(DIGIT[(int) j2]);
            return;
        }
        for (int i = 0; i < decDivisorsL.length; i++) {
            if (j2 >= decDivisorsL[i]) {
                obj = 1;
                long j3 = j2 / decDivisorsL[i];
                buffer.put(DIGIT[(int) j3]);
                j2 -= j3 * decDivisorsL[i];
            } else if (obj != null) {
                buffer.put((byte) 48);
            }
        }
    }

    public static void putHexInt(Buffer buffer, int i) {
        int i2;
        Object obj = null;
        if (i < 0) {
            buffer.put((byte) MINUS);
            if (i == Integer.MIN_VALUE) {
                buffer.put((byte) 56);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                buffer.put((byte) 48);
                return;
            }
            i2 = -i;
        } else {
            i2 = i;
        }
        if (i2 < 16) {
            buffer.put(DIGIT[i2]);
            return;
        }
        for (int i3 = 0; i3 < hexDivisors.length; i3++) {
            if (i2 >= hexDivisors[i3]) {
                obj = 1;
                int i4 = i2 / hexDivisors[i3];
                buffer.put(DIGIT[i4]);
                i2 -= i4 * hexDivisors[i3];
            } else if (obj != null) {
                buffer.put((byte) 48);
            }
        }
    }

    public static String to8859_1_String(Buffer buffer) {
        if (buffer.isImmutable()) {
            return buffer.toString();
        }
        try {
            byte[] array = buffer.array();
            if (array != null) {
                return new String(array, buffer.getIndex(), buffer.length(), StringUtil.__ISO_8859_1);
            }
            StringBuffer stringBuffer = new StringBuffer(buffer.length());
            int index = buffer.getIndex();
            for (int i = 0; i < buffer.length(); i++) {
                stringBuffer.append((char) (buffer.peek(index) & 127));
                index++;
            }
            return stringBuffer.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return buffer.toString();
        }
    }

    public static Buffer toBuffer(long j) {
        Buffer byteArrayBuffer = new ByteArrayBuffer(16);
        putDecLong(byteArrayBuffer, j);
        return byteArrayBuffer;
    }

    public static int toInt(Buffer buffer) {
        Object obj = null;
        Object obj2 = null;
        int i = 0;
        for (int index = buffer.getIndex(); index < buffer.putIndex(); index++) {
            byte peek = buffer.peek(index);
            if (peek <= (byte) 32) {
                if (obj != null) {
                    break;
                }
            } else if (peek < (byte) 48 || peek > (byte) 57) {
                if (peek != MINUS || obj != null) {
                    break;
                }
                int i2 = 1;
            } else {
                i = (peek - 48) + (i * 10);
                obj = 1;
            }
        }
        if (obj != null) {
            return obj2 != null ? -i : i;
        } else {
            throw new NumberFormatException(buffer.toString());
        }
    }

    public static long toLong(Buffer buffer) {
        Object obj = null;
        long j = 0;
        Object obj2 = null;
        for (int index = buffer.getIndex(); index < buffer.putIndex(); index++) {
            byte peek = buffer.peek(index);
            if (peek <= (byte) 32) {
                if (obj2 != null) {
                    break;
                }
            } else if (peek < (byte) 48 || peek > (byte) 57) {
                if (peek != MINUS || obj2 != null) {
                    break;
                }
                int i = 1;
            } else {
                j = (j * 10) + ((long) (peek - 48));
                obj2 = 1;
            }
        }
        if (obj2 != null) {
            return obj != null ? -j : j;
        } else {
            throw new NumberFormatException(buffer.toString());
        }
    }
}
