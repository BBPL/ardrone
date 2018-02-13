package org.mortbay.jetty.security;

import com.google.common.base.Ascii;
import com.google.common.primitives.SignedBytes;
import java.lang.reflect.Array;
import org.mortbay.jetty.HttpTokens;

public class UnixCrypt {
    private static byte[] A64TOI = new byte[128];
    private static long[][] CF6464 = ((long[][]) Array.newInstance(Long.TYPE, new int[]{16, 16}));
    private static final byte[] CIFP = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 17, Ascii.DC2, (byte) 19, Ascii.DC4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, Ascii.NAK, Ascii.SYN, Ascii.ETB, Ascii.CAN, (byte) 9, (byte) 10, Ascii.VT, Ascii.FF, Ascii.EM, Ascii.SUB, Ascii.ESC, Ascii.FS, (byte) 13, Ascii.SO, Ascii.SI, Ascii.DLE, Ascii.GS, Ascii.RS, Ascii.US, (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 41, (byte) 42, (byte) 43, (byte) 44, (byte) 57, HttpTokens.COLON, HttpTokens.SEMI_COLON, (byte) 60, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 61, (byte) 62, (byte) 63, SignedBytes.MAX_POWER_OF_TWO};
    private static final byte[] ExpandTr = new byte[]{(byte) 32, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 8, (byte) 9, (byte) 10, Ascii.VT, Ascii.FF, (byte) 13, Ascii.FF, (byte) 13, Ascii.SO, Ascii.SI, Ascii.DLE, (byte) 17, Ascii.DLE, (byte) 17, Ascii.DC2, (byte) 19, Ascii.DC4, Ascii.NAK, Ascii.DC4, Ascii.NAK, Ascii.SYN, Ascii.ETB, Ascii.CAN, Ascii.EM, Ascii.CAN, Ascii.EM, Ascii.SUB, Ascii.ESC, Ascii.FS, Ascii.GS, Ascii.FS, Ascii.GS, Ascii.RS, Ascii.US, (byte) 32, (byte) 1};
    private static long[][] IE3264 = ((long[][]) Array.newInstance(Long.TYPE, new int[]{8, 16}));
    private static final byte[] IP = new byte[]{HttpTokens.COLON, (byte) 50, (byte) 42, (byte) 34, Ascii.SUB, Ascii.DC2, (byte) 10, (byte) 2, (byte) 60, (byte) 52, (byte) 44, (byte) 36, Ascii.FS, Ascii.DC4, Ascii.FF, (byte) 4, (byte) 62, (byte) 54, (byte) 46, (byte) 38, Ascii.RS, Ascii.SYN, Ascii.SO, (byte) 6, SignedBytes.MAX_POWER_OF_TWO, (byte) 56, (byte) 48, (byte) 40, (byte) 32, Ascii.CAN, Ascii.DLE, (byte) 8, (byte) 57, (byte) 49, (byte) 41, (byte) 33, Ascii.EM, (byte) 17, (byte) 9, (byte) 1, HttpTokens.SEMI_COLON, (byte) 51, (byte) 43, (byte) 35, Ascii.ESC, (byte) 19, Ascii.VT, (byte) 3, (byte) 61, (byte) 53, (byte) 45, (byte) 37, Ascii.GS, Ascii.NAK, (byte) 13, (byte) 5, (byte) 63, (byte) 55, (byte) 47, (byte) 39, Ascii.US, Ascii.ETB, Ascii.SI, (byte) 7};
    private static final byte[] ITOA64 = new byte[]{(byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122};
    private static final byte[] P32Tr = new byte[]{Ascii.DLE, (byte) 7, Ascii.DC4, Ascii.NAK, Ascii.GS, Ascii.FF, Ascii.FS, (byte) 17, (byte) 1, Ascii.SI, Ascii.ETB, Ascii.SUB, (byte) 5, Ascii.DC2, Ascii.US, (byte) 10, (byte) 2, (byte) 8, Ascii.CAN, Ascii.SO, (byte) 32, Ascii.ESC, (byte) 3, (byte) 9, (byte) 19, (byte) 13, Ascii.RS, (byte) 6, Ascii.SYN, Ascii.VT, (byte) 4, Ascii.EM};
    private static final byte[] PC1 = new byte[]{(byte) 57, (byte) 49, (byte) 41, (byte) 33, Ascii.EM, (byte) 17, (byte) 9, (byte) 1, HttpTokens.COLON, (byte) 50, (byte) 42, (byte) 34, Ascii.SUB, Ascii.DC2, (byte) 10, (byte) 2, HttpTokens.SEMI_COLON, (byte) 51, (byte) 43, (byte) 35, Ascii.ESC, (byte) 19, Ascii.VT, (byte) 3, (byte) 60, (byte) 52, (byte) 44, (byte) 36, (byte) 63, (byte) 55, (byte) 47, (byte) 39, Ascii.US, Ascii.ETB, Ascii.SI, (byte) 7, (byte) 62, (byte) 54, (byte) 46, (byte) 38, Ascii.RS, Ascii.SYN, Ascii.SO, (byte) 6, (byte) 61, (byte) 53, (byte) 45, (byte) 37, Ascii.GS, Ascii.NAK, (byte) 13, (byte) 5, Ascii.FS, Ascii.DC4, Ascii.FF, (byte) 4};
    private static long[][] PC1ROT = ((long[][]) Array.newInstance(Long.TYPE, new int[]{16, 16}));
    private static final byte[] PC2 = new byte[]{(byte) 9, Ascii.DC2, Ascii.SO, (byte) 17, Ascii.VT, Ascii.CAN, (byte) 1, (byte) 5, Ascii.SYN, Ascii.EM, (byte) 3, Ascii.FS, Ascii.SI, (byte) 6, Ascii.NAK, (byte) 10, (byte) 35, (byte) 38, Ascii.ETB, (byte) 19, Ascii.FF, (byte) 4, Ascii.SUB, (byte) 8, (byte) 43, (byte) 54, Ascii.DLE, (byte) 7, Ascii.ESC, Ascii.DC4, (byte) 13, (byte) 2, (byte) 0, (byte) 0, (byte) 41, (byte) 52, Ascii.US, (byte) 37, (byte) 47, (byte) 55, (byte) 0, (byte) 0, Ascii.RS, (byte) 40, (byte) 51, (byte) 45, (byte) 33, (byte) 48, (byte) 0, (byte) 0, (byte) 44, (byte) 49, (byte) 39, (byte) 56, (byte) 34, (byte) 53, (byte) 0, (byte) 0, (byte) 46, (byte) 42, (byte) 50, (byte) 36, Ascii.GS, (byte) 32};
    private static long[][][] PC2ROT = ((long[][][]) Array.newInstance(Long.TYPE, new int[]{2, 16, 16}));
    private static final byte[] Rotates = new byte[]{(byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 1};
    private static final byte[][] f369S;
    private static long[][] SPE = ((long[][]) Array.newInstance(Long.TYPE, new int[]{8, 64}));

    static {
        int i;
        int i2;
        int i3;
        byte[] bArr = new byte[]{(byte) 13, (byte) 2, (byte) 8, (byte) 4, (byte) 6, Ascii.SI, Ascii.VT, (byte) 1, (byte) 10, (byte) 9, (byte) 3, Ascii.SO, (byte) 5, (byte) 0, Ascii.FF, (byte) 7, (byte) 1, Ascii.SI, (byte) 13, (byte) 8, (byte) 10, (byte) 3, (byte) 7, (byte) 4, Ascii.FF, (byte) 5, (byte) 6, Ascii.VT, (byte) 0, Ascii.SO, (byte) 9, (byte) 2, (byte) 7, Ascii.VT, (byte) 4, (byte) 1, (byte) 9, Ascii.FF, Ascii.SO, (byte) 2, (byte) 0, (byte) 6, (byte) 10, (byte) 13, Ascii.SI, (byte) 3, (byte) 5, (byte) 8, (byte) 2, (byte) 1, Ascii.SO, (byte) 7, (byte) 4, (byte) 10, (byte) 8, (byte) 13, Ascii.SI, Ascii.FF, (byte) 9, (byte) 0, (byte) 3, (byte) 5, (byte) 6, Ascii.VT};
        f369S = new byte[][]{new byte[]{Ascii.SO, (byte) 4, (byte) 13, (byte) 1, (byte) 2, Ascii.SI, Ascii.VT, (byte) 8, (byte) 3, (byte) 10, (byte) 6, Ascii.FF, (byte) 5, (byte) 9, (byte) 0, (byte) 7, (byte) 0, Ascii.SI, (byte) 7, (byte) 4, Ascii.SO, (byte) 2, (byte) 13, (byte) 1, (byte) 10, (byte) 6, Ascii.FF, Ascii.VT, (byte) 9, (byte) 5, (byte) 3, (byte) 8, (byte) 4, (byte) 1, Ascii.SO, (byte) 8, (byte) 13, (byte) 6, (byte) 2, Ascii.VT, Ascii.SI, Ascii.FF, (byte) 9, (byte) 7, (byte) 3, (byte) 10, (byte) 5, (byte) 0, Ascii.SI, Ascii.FF, (byte) 8, (byte) 2, (byte) 4, (byte) 9, (byte) 1, (byte) 7, (byte) 5, Ascii.VT, (byte) 3, Ascii.SO, (byte) 10, (byte) 0, (byte) 6, (byte) 13}, new byte[]{Ascii.SI, (byte) 1, (byte) 8, Ascii.SO, (byte) 6, Ascii.VT, (byte) 3, (byte) 4, (byte) 9, (byte) 7, (byte) 2, (byte) 13, Ascii.FF, (byte) 0, (byte) 5, (byte) 10, (byte) 3, (byte) 13, (byte) 4, (byte) 7, Ascii.SI, (byte) 2, (byte) 8, Ascii.SO, Ascii.FF, (byte) 0, (byte) 1, (byte) 10, (byte) 6, (byte) 9, Ascii.VT, (byte) 5, (byte) 0, Ascii.SO, (byte) 7, Ascii.VT, (byte) 10, (byte) 4, (byte) 13, (byte) 1, (byte) 5, (byte) 8, Ascii.FF, (byte) 6, (byte) 9, (byte) 3, (byte) 2, Ascii.SI, (byte) 13, (byte) 8, (byte) 10, (byte) 1, (byte) 3, Ascii.SI, (byte) 4, (byte) 2, Ascii.VT, (byte) 6, (byte) 7, Ascii.FF, (byte) 0, (byte) 5, Ascii.SO, (byte) 9}, new byte[]{(byte) 10, (byte) 0, (byte) 9, Ascii.SO, (byte) 6, (byte) 3, Ascii.SI, (byte) 5, (byte) 1, (byte) 13, Ascii.FF, (byte) 7, Ascii.VT, (byte) 4, (byte) 2, (byte) 8, (byte) 13, (byte) 7, (byte) 0, (byte) 9, (byte) 3, (byte) 4, (byte) 6, (byte) 10, (byte) 2, (byte) 8, (byte) 5, Ascii.SO, Ascii.FF, Ascii.VT, Ascii.SI, (byte) 1, (byte) 13, (byte) 6, (byte) 4, (byte) 9, (byte) 8, Ascii.SI, (byte) 3, (byte) 0, Ascii.VT, (byte) 1, (byte) 2, Ascii.FF, (byte) 5, (byte) 10, Ascii.SO, (byte) 7, (byte) 1, (byte) 10, (byte) 13, (byte) 0, (byte) 6, (byte) 9, (byte) 8, (byte) 7, (byte) 4, Ascii.SI, Ascii.SO, (byte) 3, Ascii.VT, (byte) 5, (byte) 2, Ascii.FF}, new byte[]{(byte) 7, (byte) 13, Ascii.SO, (byte) 3, (byte) 0, (byte) 6, (byte) 9, (byte) 10, (byte) 1, (byte) 2, (byte) 8, (byte) 5, Ascii.VT, Ascii.FF, (byte) 4, Ascii.SI, (byte) 13, (byte) 8, Ascii.VT, (byte) 5, (byte) 6, Ascii.SI, (byte) 0, (byte) 3, (byte) 4, (byte) 7, (byte) 2, Ascii.FF, (byte) 1, (byte) 10, Ascii.SO, (byte) 9, (byte) 10, (byte) 6, (byte) 9, (byte) 0, Ascii.FF, Ascii.VT, (byte) 7, (byte) 13, Ascii.SI, (byte) 1, (byte) 3, Ascii.SO, (byte) 5, (byte) 2, (byte) 8, (byte) 4, (byte) 3, Ascii.SI, (byte) 0, (byte) 6, (byte) 10, (byte) 1, (byte) 13, (byte) 8, (byte) 9, (byte) 4, (byte) 5, Ascii.VT, Ascii.FF, (byte) 7, (byte) 2, Ascii.SO}, new byte[]{(byte) 2, Ascii.FF, (byte) 4, (byte) 1, (byte) 7, (byte) 10, Ascii.VT, (byte) 6, (byte) 8, (byte) 5, (byte) 3, Ascii.SI, (byte) 13, (byte) 0, Ascii.SO, (byte) 9, Ascii.SO, Ascii.VT, (byte) 2, Ascii.FF, (byte) 4, (byte) 7, (byte) 13, (byte) 1, (byte) 5, (byte) 0, Ascii.SI, (byte) 10, (byte) 3, (byte) 9, (byte) 8, (byte) 6, (byte) 4, (byte) 2, (byte) 1, Ascii.VT, (byte) 10, (byte) 13, (byte) 7, (byte) 8, Ascii.SI, (byte) 9, Ascii.FF, (byte) 5, (byte) 6, (byte) 3, (byte) 0, Ascii.SO, Ascii.VT, (byte) 8, Ascii.FF, (byte) 7, (byte) 1, Ascii.SO, (byte) 2, (byte) 13, (byte) 6, Ascii.SI, (byte) 0, (byte) 9, (byte) 10, (byte) 4, (byte) 5, (byte) 3}, new byte[]{Ascii.FF, (byte) 1, (byte) 10, Ascii.SI, (byte) 9, (byte) 2, (byte) 6, (byte) 8, (byte) 0, (byte) 13, (byte) 3, (byte) 4, Ascii.SO, (byte) 7, (byte) 5, Ascii.VT, (byte) 10, Ascii.SI, (byte) 4, (byte) 2, (byte) 7, Ascii.FF, (byte) 9, (byte) 5, (byte) 6, (byte) 1, (byte) 13, Ascii.SO, (byte) 0, Ascii.VT, (byte) 3, (byte) 8, (byte) 9, Ascii.SO, Ascii.SI, (byte) 5, (byte) 2, (byte) 8, Ascii.FF, (byte) 3, (byte) 7, (byte) 0, (byte) 4, (byte) 10, (byte) 1, (byte) 13, Ascii.VT, (byte) 6, (byte) 4, (byte) 3, (byte) 2, Ascii.FF, (byte) 9, (byte) 5, Ascii.SI, (byte) 10, Ascii.VT, Ascii.SO, (byte) 1, (byte) 7, (byte) 6, (byte) 0, (byte) 8, (byte) 13}, new byte[]{(byte) 4, Ascii.VT, (byte) 2, Ascii.SO, Ascii.SI, (byte) 0, (byte) 8, (byte) 13, (byte) 3, Ascii.FF, (byte) 9, (byte) 7, (byte) 5, (byte) 10, (byte) 6, (byte) 1, (byte) 13, (byte) 0, Ascii.VT, (byte) 7, (byte) 4, (byte) 9, (byte) 1, (byte) 10, Ascii.SO, (byte) 3, (byte) 5, Ascii.FF, (byte) 2, Ascii.SI, (byte) 8, (byte) 6, (byte) 1, (byte) 4, Ascii.VT, (byte) 13, Ascii.FF, (byte) 3, (byte) 7, Ascii.SO, (byte) 10, Ascii.SI, (byte) 6, (byte) 8, (byte) 0, (byte) 5, (byte) 9, (byte) 2, (byte) 6, Ascii.VT, (byte) 13, (byte) 8, (byte) 1, (byte) 4, (byte) 10, (byte) 7, (byte) 9, (byte) 5, (byte) 0, Ascii.SI, Ascii.SO, (byte) 2, (byte) 3, Ascii.FF}, bArr};
        byte[] bArr2 = new byte[64];
        byte[] bArr3 = new byte[64];
        for (i = 0; i < 64; i++) {
            A64TOI[ITOA64[i]] = (byte) i;
        }
        for (i = 0; i < 64; i++) {
            bArr2[i] = (byte) 0;
        }
        for (i2 = 0; i2 < 64; i2++) {
            byte b = PC2[i2];
            if (b != (byte) 0) {
                i = b + (Rotates[0] - 1);
                if (i % 28 < Rotates[0]) {
                    i -= 28;
                }
                i = PC1[i];
                if (i > 0) {
                    i--;
                    i = ((i | 7) - (i & 7)) + 1;
                }
                bArr2[i2] = (byte) i;
            }
        }
        init_perm(PC1ROT, bArr2, 8);
        for (i3 = 0; i3 < 2; i3++) {
            for (i = 0; i < 64; i++) {
                bArr3[i] = (byte) 0;
                bArr2[i] = (byte) 0;
            }
            for (i = 0; i < 64; i++) {
                byte b2 = PC2[i];
                if (b2 != (byte) 0) {
                    bArr3[b2 - 1] = (byte) (i + 1);
                }
            }
            for (i2 = 0; i2 < 64; i2++) {
                b = PC2[i2];
                if (b != (byte) 0) {
                    i = b + i3;
                    if (i % 28 <= i3) {
                        i -= 28;
                    }
                    bArr2[i2] = bArr3[i];
                }
            }
            init_perm(PC2ROT[i3], bArr2, 8);
        }
        for (i3 = 0; i3 < 8; i3++) {
            i2 = 0;
            while (i2 < 8) {
                i = i2 < 2 ? 0 : IP[ExpandTr[((i3 * 6) + i2) - 2] - 1];
                if (i > 32) {
                    i -= 32;
                } else if (i > 0) {
                    i--;
                }
                if (i > 0) {
                    i--;
                    i = ((i | 7) - (i & 7)) + 1;
                }
                bArr2[(i3 * 8) + i2] = (byte) i;
                i2++;
            }
        }
        init_perm(IE3264, bArr2, 8);
        for (i2 = 0; i2 < 64; i2++) {
            i = IP[CIFP[i2] - 1];
            if (i > 0) {
                i--;
                i = ((i | 7) - (i & 7)) + 1;
            }
            bArr2[i - 1] = (byte) (i2 + 1);
        }
        init_perm(CF6464, bArr2, 8);
        for (i = 0; i < 48; i++) {
            bArr2[i] = P32Tr[ExpandTr[i] - 1];
        }
        for (int i4 = 0; i4 < 8; i4++) {
            for (int i5 = 0; i5 < 64; i5++) {
                b2 = f369S[i4][(((((((i5 >> 0) & 1) << 5) | (((i5 >> 1) & 1) << 3)) | (((i5 >> 2) & 1) << 2)) | (((i5 >> 3) & 1) << 1)) | (((i5 >> 4) & 1) << 0)) | (((i5 >> 5) & 1) << 4)];
                for (i = 0; i < 32; i++) {
                    bArr3[i] = (byte) 0;
                }
                for (i = 0; i < 4; i++) {
                    bArr3[(i4 * 4) + i] = (byte) ((((((((b2 >> 3) & 1) << 0) | (((b2 >> 2) & 1) << 1)) | (((b2 >> 1) & 1) << 2)) | (((b2 >> 0) & 1) << 3)) >> i) & 1);
                }
                long j = 0;
                i3 = 24;
                while (true) {
                    i3--;
                    if (i3 < 0) {
                        break;
                    }
                    j = ((j << 1) | (((long) bArr3[bArr2[i3] - 1]) << 32)) | ((long) bArr3[bArr2[i3 + 24] - 1]);
                }
                SPE[i4][i5] = to_six_bit(j);
            }
        }
    }

    private UnixCrypt() {
    }

    public static String crypt(String str, String str2) {
        byte[] bArr = new byte[13];
        long j = 0;
        if (str == null || str2 == null) {
            return Constraint.ANY_ROLE;
        }
        int length = str.length();
        int i = 0;
        while (i < 8) {
            j = (j << 8) | ((long) (i < length ? str.charAt(i) * 2 : 0));
            i++;
        }
        long[] des_setkey = des_setkey(j);
        int i2 = 2;
        int i3 = 0;
        while (true) {
            int i4 = i2 - 1;
            if (i4 < 0) {
                break;
            }
            i2 = i4 < str2.length() ? str2.charAt(i4) : 46;
            bArr[i4] = (byte) i2;
            i3 = (A64TOI[i2] & 255) | (i3 << 6);
            i2 = i4;
        }
        j = des_cipher(0, i3, 25, des_setkey);
        bArr[12] = ITOA64[(((int) j) << 2) & 63];
        j >>= 4;
        i2 = 12;
        while (true) {
            i2--;
            if (i2 < 2) {
                return new String(bArr, 0, 0, 13);
            }
            bArr[i2] = ITOA64[((int) j) & 63];
            j >>= 6;
        }
    }

    private static long des_cipher(long j, int i, int i2, long[] jArr) {
        int to_six_bit = to_six_bit(i);
        long j2 = 6148914691236517205L & j;
        long j3 = (-6148914694099828736L & j) | ((j >> 1) & 1431655765);
        j3 = perm3264((int) (((((j2 << 32) | (j2 << 1)) & -4294967296L) | ((j3 | (j3 >> 32)) & 4294967295L)) >> 32), IE3264);
        j2 = perm3264((int) (-1 & j3), IE3264);
        while (true) {
            i2--;
            if (i2 >= 0) {
                for (int i3 = 0; i3 < 8; i3++) {
                    long j4 = (((j2 >> 32) ^ j2) & ((long) to_six_bit)) & 4294967295L;
                    long j5 = jArr[i3 << 1] ^ ((j4 | (j4 << 32)) ^ j2);
                    j3 ^= SPE[7][(int) ((j5 >> 2) & 63)] ^ ((((((SPE[0][(int) ((j5 >> 58) & 63)] ^ SPE[1][(int) ((j5 >> 50) & 63)]) ^ SPE[2][(int) ((j5 >> 42) & 63)]) ^ SPE[3][(int) ((j5 >> 34) & 63)]) ^ SPE[4][(int) ((j5 >> 26) & 63)]) ^ SPE[5][(int) ((j5 >> 18) & 63)]) ^ SPE[6][(int) ((j5 >> 10) & 63)]);
                    j4 = (((j3 >> 32) ^ j3) & ((long) to_six_bit)) & 4294967295L;
                    j5 = jArr[(i3 << 1) + 1] ^ ((j4 | (j4 << 32)) ^ j3);
                    j2 ^= SPE[7][(int) ((j5 >> 2) & 63)] ^ ((((((SPE[0][(int) ((j5 >> 58) & 63)] ^ SPE[1][(int) ((j5 >> 50) & 63)]) ^ SPE[2][(int) ((j5 >> 42) & 63)]) ^ SPE[3][(int) ((j5 >> 34) & 63)]) ^ SPE[4][(int) ((j5 >> 26) & 63)]) ^ SPE[5][(int) ((j5 >> 18) & 63)]) ^ SPE[6][(int) ((j5 >> 10) & 63)]);
                }
                j3 ^= j2;
                j2 ^= j3;
                j3 ^= j2;
            } else {
                return perm6464(((((j2 & -1) << 1) & 4042322160L) | ((j2 >> 35) & 252645135)) | ((((j3 >> 35) & 252645135) | (((j3 & -1) << 1) & 4042322160L)) << 32), CF6464);
            }
        }
    }

    private static long[] des_setkey(long j) {
        long perm6464 = perm6464(j, PC1ROT);
        long[] jArr = new long[16];
        jArr[0] = perm6464 & -217020518463700993L;
        for (int i = 1; i < 16; i++) {
            jArr[i] = perm6464;
            perm6464 = perm6464(perm6464, PC2ROT[Rotates[i] - 1]);
            jArr[i] = perm6464 & -217020518463700993L;
        }
        return jArr;
    }

    private static void init_perm(long[][] jArr, byte[] bArr, int i) {
        for (int i2 = 0; i2 < i * 8; i2++) {
            int i3 = bArr[i2] - 1;
            if (i3 >= 0) {
                for (int i4 = 0; i4 < 16; i4++) {
                    if (((1 << (i3 & 3)) & i4) != 0) {
                        long[] jArr2 = jArr[i3 >> 2];
                        jArr2[i4] = jArr2[i4] | (1 << ((i2 & 7) + ((7 - (i2 >> 3)) << 3)));
                    }
                }
            }
        }
    }

    public static void main(String[] strArr) {
        if (strArr.length != 2) {
            System.err.println("Usage - java org.mortbay.util.UnixCrypt <key> <salt>");
            System.exit(1);
        }
        System.err.println(new StringBuffer().append("Crypt=").append(crypt(strArr[0], strArr[1])).toString());
    }

    private static long perm3264(int i, long[][] jArr) {
        long j = 0;
        int i2 = 4;
        while (true) {
            i2--;
            if (i2 < 0) {
                return j;
            }
            int i3 = i & 255;
            i >>= 8;
            j = (j | jArr[i2 << 1][i3 & 15]) | jArr[(i2 << 1) + 1][i3 >> 4];
        }
    }

    private static long perm6464(long j, long[][] jArr) {
        long j2 = 0;
        int i = 8;
        while (true) {
            i--;
            if (i < 0) {
                return j2;
            }
            int i2 = (int) (255 & j);
            j >>= 8;
            j2 = (j2 | jArr[i << 1][i2 & 15]) | jArr[(i << 1) + 1][i2 >> 4];
        }
    }

    private static int to_six_bit(int i) {
        return ((((i << 26) & -67108864) | ((i << 12) & 16515072)) | ((i >> 2) & 64512)) | ((i >> 16) & 252);
    }

    private static long to_six_bit(long j) {
        return ((((j << 26) & -288230371923853312L) | ((j << 12) & 70931694147600384L)) | ((j >> 2) & 277076930264064L)) | ((j >> 16) & 1082331758844L);
    }
}
