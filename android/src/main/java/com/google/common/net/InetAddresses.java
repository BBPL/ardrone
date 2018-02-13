package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Ints;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Nullable;
import org.mortbay.io.Portable;

@Beta
public final class InetAddresses {
    private static final Inet4Address ANY4 = ((Inet4Address) forString(Portable.ALL_INTERFACES));
    private static final int IPV4_PART_COUNT = 4;
    private static final int IPV6_PART_COUNT = 8;
    private static final Inet4Address LOOPBACK4 = ((Inet4Address) forString("127.0.0.1"));

    @Beta
    public static final class TeredoInfo {
        private final Inet4Address client;
        private final int flags;
        private final int port;
        private final Inet4Address server;

        public TeredoInfo(@Nullable Inet4Address inet4Address, @Nullable Inet4Address inet4Address2, int i, int i2) {
            boolean z = i >= 0 && i <= 65535;
            Preconditions.checkArgument(z, "port '%s' is out of range (0 <= port <= 0xffff)", Integer.valueOf(i));
            z = i2 >= 0 && i2 <= 65535;
            Preconditions.checkArgument(z, "flags '%s' is out of range (0 <= flags <= 0xffff)", Integer.valueOf(i2));
            this.server = (Inet4Address) Objects.firstNonNull(inet4Address, InetAddresses.ANY4);
            this.client = (Inet4Address) Objects.firstNonNull(inet4Address2, InetAddresses.ANY4);
            this.port = i;
            this.flags = i2;
        }

        public Inet4Address getClient() {
            return this.client;
        }

        public int getFlags() {
            return this.flags;
        }

        public int getPort() {
            return this.port;
        }

        public Inet4Address getServer() {
            return this.server;
        }
    }

    private InetAddresses() {
    }

    private static InetAddress bytesToInetAddress(byte[] bArr) {
        try {
            return InetAddress.getByAddress(bArr);
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
    }

    public static int coerceToInteger(InetAddress inetAddress) {
        return ByteStreams.newDataInput(getCoercedIPv4Address(inetAddress).getAddress()).readInt();
    }

    private static void compressLongestRunOfZeroes(int[] iArr) {
        int i = -1;
        int i2 = -1;
        int i3 = 0;
        int i4 = -1;
        while (i3 < iArr.length + 1) {
            if (i3 >= iArr.length || iArr[i3] != 0) {
                if (i2 >= 0) {
                    int i5 = i3 - i2;
                    if (i5 > i) {
                        i = i5;
                        i4 = i2;
                    }
                    i2 = -1;
                }
            } else if (i2 < 0) {
                i2 = i3;
            }
            i3++;
        }
        if (i >= 2) {
            Arrays.fill(iArr, i4, i + i4, -1);
        }
    }

    private static String convertDottedQuadToHex(String str) {
        int lastIndexOf = str.lastIndexOf(58);
        String substring = str.substring(0, lastIndexOf + 1);
        byte[] textToNumericFormatV4 = textToNumericFormatV4(str.substring(lastIndexOf + 1));
        if (textToNumericFormatV4 == null) {
            return null;
        }
        String toHexString = Integer.toHexString(((textToNumericFormatV4[0] & 255) << 8) | (textToNumericFormatV4[1] & 255));
        return substring + toHexString + ":" + Integer.toHexString((textToNumericFormatV4[3] & 255) | ((textToNumericFormatV4[2] & 255) << 8));
    }

    private static byte[] copyOfRange(byte[] bArr, int i, int i2) {
        Object obj = new byte[(i2 - i)];
        System.arraycopy(bArr, i, obj, 0, i2 - i);
        return obj;
    }

    public static InetAddress forString(String str) {
        byte[] ipStringToBytes = ipStringToBytes(str);
        if (ipStringToBytes != null) {
            return bytesToInetAddress(ipStringToBytes);
        }
        throw new IllegalArgumentException(String.format("'%s' is not an IP string literal.", new Object[]{str}));
    }

    public static InetAddress forUriString(String str) {
        String substring;
        Preconditions.checkNotNull(str);
        int i;
        if (str.startsWith("[") && str.endsWith("]")) {
            substring = str.substring(1, str.length() - 1);
            i = 16;
        } else {
            i = 4;
            substring = str;
        }
        byte[] ipStringToBytes = ipStringToBytes(substring);
        if (ipStringToBytes != null && ipStringToBytes.length == r1) {
            return bytesToInetAddress(ipStringToBytes);
        }
        throw new IllegalArgumentException(String.format("Not a valid URI IP literal: '%s'", new Object[]{str}));
    }

    public static Inet4Address fromInteger(int i) {
        return getInet4Address(Ints.toByteArray(i));
    }

    public static InetAddress fromLittleEndianByteArray(byte[] bArr) throws UnknownHostException {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[(bArr.length - i) - 1];
        }
        return InetAddress.getByAddress(bArr2);
    }

    public static Inet4Address get6to4IPv4Address(Inet6Address inet6Address) {
        Preconditions.checkArgument(is6to4Address(inet6Address), "Address '%s' is not a 6to4 address.", toAddrString(inet6Address));
        return getInet4Address(copyOfRange(inet6Address.getAddress(), 2, 6));
    }

    public static Inet4Address getCoercedIPv4Address(InetAddress inetAddress) {
        if (inetAddress instanceof Inet4Address) {
            return (Inet4Address) inetAddress;
        }
        int i;
        byte[] address = inetAddress.getAddress();
        for (i = 0; i < 15; i++) {
            if (address[i] != (byte) 0) {
                i = 0;
                break;
            }
        }
        i = 1;
        if (i != 0 && address[15] == (byte) 1) {
            return LOOPBACK4;
        }
        if (i != 0 && address[15] == (byte) 0) {
            return ANY4;
        }
        Inet6Address inet6Address = (Inet6Address) inetAddress;
        i = Hashing.murmur3_32().hashLong(hasEmbeddedIPv4ClientAddress(inet6Address) ? (long) getEmbeddedIPv4ClientAddress(inet6Address).hashCode() : ByteBuffer.wrap(inet6Address.getAddress(), 0, 8).getLong()).asInt() | -536870912;
        if (i == -1) {
            i = -2;
        }
        return getInet4Address(Ints.toByteArray(i));
    }

    public static Inet4Address getCompatIPv4Address(Inet6Address inet6Address) {
        Preconditions.checkArgument(isCompatIPv4Address(inet6Address), "Address '%s' is not IPv4-compatible.", toAddrString(inet6Address));
        return getInet4Address(copyOfRange(inet6Address.getAddress(), 12, 16));
    }

    public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address inet6Address) {
        if (isCompatIPv4Address(inet6Address)) {
            return getCompatIPv4Address(inet6Address);
        }
        if (is6to4Address(inet6Address)) {
            return get6to4IPv4Address(inet6Address);
        }
        if (isTeredoAddress(inet6Address)) {
            return getTeredoInfo(inet6Address).getClient();
        }
        throw new IllegalArgumentException(String.format("'%s' has no embedded IPv4 address.", new Object[]{toAddrString(inet6Address)}));
    }

    private static Inet4Address getInet4Address(byte[] bArr) {
        Preconditions.checkArgument(bArr.length == 4, "Byte array has invalid length for an IPv4 address: %s != 4.", Integer.valueOf(bArr.length));
        return (Inet4Address) bytesToInetAddress(bArr);
    }

    public static Inet4Address getIsatapIPv4Address(Inet6Address inet6Address) {
        Preconditions.checkArgument(isIsatapAddress(inet6Address), "Address '%s' is not an ISATAP address.", toAddrString(inet6Address));
        return getInet4Address(copyOfRange(inet6Address.getAddress(), 12, 16));
    }

    public static TeredoInfo getTeredoInfo(Inet6Address inet6Address) {
        int i = 0;
        Preconditions.checkArgument(isTeredoAddress(inet6Address), "Address '%s' is not a Teredo address.", toAddrString(inet6Address));
        byte[] address = inet6Address.getAddress();
        Inet4Address inet4Address = getInet4Address(copyOfRange(address, 4, 8));
        short readShort = ByteStreams.newDataInput(address, 8).readShort();
        short readShort2 = ByteStreams.newDataInput(address, 10).readShort();
        address = copyOfRange(address, 12, 16);
        while (i < address.length) {
            address[i] = (byte) (address[i] ^ -1);
            i++;
        }
        return new TeredoInfo(inet4Address, getInet4Address(address), (readShort2 ^ -1) & 65535, readShort & 65535);
    }

    public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address inet6Address) {
        return isCompatIPv4Address(inet6Address) || is6to4Address(inet6Address) || isTeredoAddress(inet6Address);
    }

    private static String hextetsToIPv6String(int[] iArr) {
        StringBuilder stringBuilder = new StringBuilder(39);
        Object obj = null;
        int i = 0;
        while (i < iArr.length) {
            Object obj2 = iArr[i] >= 0 ? 1 : null;
            if (obj2 != null) {
                if (obj != null) {
                    stringBuilder.append(':');
                }
                stringBuilder.append(Integer.toHexString(iArr[i]));
            } else if (i == 0 || obj != null) {
                stringBuilder.append("::");
            }
            i++;
            obj = obj2;
        }
        return stringBuilder.toString();
    }

    public static InetAddress increment(InetAddress inetAddress) {
        byte[] address = inetAddress.getAddress();
        int length = address.length - 1;
        while (length >= 0 && address[length] == (byte) -1) {
            address[length] = (byte) 0;
            length--;
        }
        Preconditions.checkArgument(length >= 0, "Incrementing %s would wrap.", inetAddress);
        address[length] = (byte) (address[length] + 1);
        return bytesToInetAddress(address);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] ipStringToBytes(java.lang.String r7) {
        /*
        r4 = 0;
        r3 = 1;
        r0 = 0;
        r1 = r0;
        r2 = r0;
    L_0x0005:
        r5 = r7.length();
        if (r2 >= r5) goto L_0x002c;
    L_0x000b:
        r5 = r7.charAt(r2);
        r6 = 46;
        if (r5 != r6) goto L_0x0017;
    L_0x0013:
        r1 = r3;
    L_0x0014:
        r2 = r2 + 1;
        goto L_0x0005;
    L_0x0017:
        r6 = 58;
        if (r5 != r6) goto L_0x0021;
    L_0x001b:
        if (r1 == 0) goto L_0x001f;
    L_0x001d:
        r0 = r4;
    L_0x001e:
        return r0;
    L_0x001f:
        r0 = r3;
        goto L_0x0014;
    L_0x0021:
        r6 = 16;
        r5 = java.lang.Character.digit(r5, r6);
        r6 = -1;
        if (r5 != r6) goto L_0x0014;
    L_0x002a:
        r0 = r4;
        goto L_0x001e;
    L_0x002c:
        if (r0 == 0) goto L_0x003b;
    L_0x002e:
        if (r1 == 0) goto L_0x0036;
    L_0x0030:
        r7 = convertDottedQuadToHex(r7);
        if (r7 == 0) goto L_0x001d;
    L_0x0036:
        r0 = textToNumericFormatV6(r7);
        goto L_0x001e;
    L_0x003b:
        if (r1 == 0) goto L_0x001d;
    L_0x003d:
        r0 = textToNumericFormatV4(r7);
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.InetAddresses.ipStringToBytes(java.lang.String):byte[]");
    }

    public static boolean is6to4Address(Inet6Address inet6Address) {
        byte[] address = inet6Address.getAddress();
        return address[0] == (byte) 32 && address[1] == (byte) 2;
    }

    public static boolean isCompatIPv4Address(Inet6Address inet6Address) {
        if (inet6Address.isIPv4CompatibleAddress()) {
            byte[] address = inet6Address.getAddress();
            if (address[12] != (byte) 0 || address[13] != (byte) 0 || address[14] != (byte) 0) {
                return true;
            }
            if (!(address[15] == (byte) 0 || address[15] == (byte) 1)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInetAddress(String str) {
        return ipStringToBytes(str) != null;
    }

    public static boolean isIsatapAddress(Inet6Address inet6Address) {
        if (!isTeredoAddress(inet6Address)) {
            byte[] address = inet6Address.getAddress();
            if ((address[8] | 3) == 3 && address[9] == (byte) 0 && address[10] == (byte) 94 && address[11] == (byte) -2) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMappedIPv4Address(String str) {
        int i = 10;
        byte[] ipStringToBytes = ipStringToBytes(str);
        if (ipStringToBytes == null || ipStringToBytes.length != 16) {
            return false;
        }
        for (int i2 = 0; i2 < 10; i2++) {
            if (ipStringToBytes[i2] != (byte) 0) {
                return false;
            }
        }
        while (i < 12) {
            if (ipStringToBytes[i] != (byte) -1) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean isMaximum(InetAddress inetAddress) {
        byte[] address = inetAddress.getAddress();
        for (byte b : address) {
            if (b != (byte) -1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isTeredoAddress(Inet6Address inet6Address) {
        byte[] address = inet6Address.getAddress();
        return address[0] == (byte) 32 && address[1] == (byte) 1 && address[2] == (byte) 0 && address[3] == (byte) 0;
    }

    public static boolean isUriInetAddress(String str) {
        try {
            forUriString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static short parseHextet(String str) {
        int parseInt = Integer.parseInt(str, 16);
        if (parseInt <= 65535) {
            return (short) parseInt;
        }
        throw new NumberFormatException();
    }

    private static byte parseOctet(String str) {
        int parseInt = Integer.parseInt(str);
        if (parseInt <= 255 && (!str.startsWith("0") || str.length() <= 1)) {
            return (byte) parseInt;
        }
        throw new NumberFormatException();
    }

    private static byte[] textToNumericFormatV4(String str) {
        String[] split = str.split("\\.", 5);
        if (split.length != 4) {
            return null;
        }
        byte[] bArr = new byte[4];
        int i = 0;
        while (i < bArr.length) {
            try {
                bArr[i] = parseOctet(split[i]);
                i++;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return bArr;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] textToNumericFormatV6(java.lang.String r10) {
        /*
        r2 = 1;
        r3 = 0;
        r4 = 0;
        r0 = ":";
        r1 = 10;
        r6 = r10.split(r0, r1);
        r0 = r6.length;
        r1 = 3;
        if (r0 < r1) goto L_0x0014;
    L_0x000f:
        r0 = r6.length;
        r1 = 9;
        if (r0 <= r1) goto L_0x0016;
    L_0x0014:
        r0 = r3;
    L_0x0015:
        return r0;
    L_0x0016:
        r0 = -1;
        r1 = r2;
    L_0x0018:
        r5 = r6.length;
        r5 = r5 + -1;
        if (r1 >= r5) goto L_0x002b;
    L_0x001d:
        r5 = r6[r1];
        r5 = r5.length();
        if (r5 != 0) goto L_0x0028;
    L_0x0025:
        if (r0 >= 0) goto L_0x0014;
    L_0x0027:
        r0 = r1;
    L_0x0028:
        r1 = r1 + 1;
        goto L_0x0018;
    L_0x002b:
        if (r0 < 0) goto L_0x006c;
    L_0x002d:
        r1 = r6.length;
        r1 = r1 - r0;
        r5 = r1 + -1;
        r1 = r6[r4];
        r1 = r1.length();
        if (r1 != 0) goto L_0x009c;
    L_0x0039:
        r1 = r0 + -1;
        if (r1 != 0) goto L_0x0014;
    L_0x003d:
        r7 = r6.length;
        r7 = r7 + -1;
        r7 = r6[r7];
        r7 = r7.length();
        if (r7 != 0) goto L_0x0098;
    L_0x0048:
        r5 = r5 + -1;
        if (r5 != 0) goto L_0x0014;
    L_0x004c:
        r9 = r1;
        r1 = r5;
        r5 = r9;
    L_0x004f:
        r7 = r5 + r1;
        r7 = 8 - r7;
        if (r0 < 0) goto L_0x0070;
    L_0x0055:
        if (r7 < r2) goto L_0x0014;
    L_0x0057:
        r0 = 16;
        r2 = java.nio.ByteBuffer.allocate(r0);
        r0 = r4;
    L_0x005e:
        if (r0 >= r5) goto L_0x0074;
    L_0x0060:
        r8 = r6[r0];	 Catch:{ NumberFormatException -> 0x008e }
        r8 = parseHextet(r8);	 Catch:{ NumberFormatException -> 0x008e }
        r2.putShort(r8);	 Catch:{ NumberFormatException -> 0x008e }
        r0 = r0 + 1;
        goto L_0x005e;
    L_0x006c:
        r1 = r6.length;
        r5 = r1;
        r1 = r4;
        goto L_0x004f;
    L_0x0070:
        if (r7 == 0) goto L_0x0057;
    L_0x0072:
        r0 = r3;
        goto L_0x0015;
    L_0x0074:
        r0 = r4;
    L_0x0075:
        if (r0 >= r7) goto L_0x0096;
    L_0x0077:
        r4 = 0;
        r2.putShort(r4);	 Catch:{ NumberFormatException -> 0x008e }
        r0 = r0 + 1;
        goto L_0x0075;
    L_0x007e:
        if (r0 <= 0) goto L_0x0091;
    L_0x0080:
        r1 = r6.length;	 Catch:{ NumberFormatException -> 0x008e }
        r1 = r1 - r0;
        r1 = r6[r1];	 Catch:{ NumberFormatException -> 0x008e }
        r1 = parseHextet(r1);	 Catch:{ NumberFormatException -> 0x008e }
        r2.putShort(r1);	 Catch:{ NumberFormatException -> 0x008e }
        r0 = r0 + -1;
        goto L_0x007e;
    L_0x008e:
        r0 = move-exception;
        r0 = r3;
        goto L_0x0015;
    L_0x0091:
        r0 = r2.array();
        goto L_0x0015;
    L_0x0096:
        r0 = r1;
        goto L_0x007e;
    L_0x0098:
        r9 = r1;
        r1 = r5;
        r5 = r9;
        goto L_0x004f;
    L_0x009c:
        r1 = r0;
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.net.InetAddresses.textToNumericFormatV6(java.lang.String):byte[]");
    }

    public static String toAddrString(InetAddress inetAddress) {
        Preconditions.checkNotNull(inetAddress);
        if (inetAddress instanceof Inet4Address) {
            return inetAddress.getHostAddress();
        }
        Preconditions.checkArgument(inetAddress instanceof Inet6Address);
        byte[] address = inetAddress.getAddress();
        int[] iArr = new int[8];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = Ints.fromBytes((byte) 0, (byte) 0, address[i * 2], address[(i * 2) + 1]);
        }
        compressLongestRunOfZeroes(iArr);
        return hextetsToIPv6String(iArr);
    }

    public static String toUriString(InetAddress inetAddress) {
        return inetAddress instanceof Inet6Address ? "[" + toAddrString(inetAddress) + "]" : toAddrString(inetAddress);
    }
}
