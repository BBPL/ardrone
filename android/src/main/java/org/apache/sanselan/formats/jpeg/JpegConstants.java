package org.apache.sanselan.formats.jpeg;

import org.mortbay.jetty.HttpTokens;

public interface JpegConstants {
    public static final byte[] CONST_8BIM = new byte[]{(byte) 56, (byte) 66, (byte) 73, (byte) 77};
    public static final byte[] EOI = new byte[]{(byte) -1, (byte) -39};
    public static final byte[] EXIF_IDENTIFIER_CODE = new byte[]{(byte) 69, (byte) 120, (byte) 105, (byte) 102};
    public static final byte[] JFIF0_SIGNATURE;
    public static final byte[] JFIF0_SIGNATURE_ALTERNATIVE = new byte[]{(byte) 74, (byte) 70, (byte) 73, (byte) 70, (byte) 32};
    public static final int JFIFMarker = 65504;
    public static final int JPEG_APP0 = 224;
    public static final int JPEG_APP0_Marker = 65504;
    public static final int JPEG_APP13_Marker = 65517;
    public static final int JPEG_APP14_Marker = 65518;
    public static final int JPEG_APP15_Marker = 65519;
    public static final int JPEG_APP1_Marker = 65505;
    public static final int JPEG_APP2_Marker = 65506;
    public static final int[] MARKERS = new int[]{SOS_Marker, JPEG_APP0, 65504, JPEG_APP1_Marker, JPEG_APP2_Marker, JPEG_APP13_Marker, JPEG_APP14_Marker, JPEG_APP15_Marker, 65504, SOF0Marker, SOF1Marker, SOF2Marker, SOF3Marker, SOF4Marker, SOF5Marker, SOF6Marker, SOF7Marker, SOF8Marker, SOF9Marker, SOF10Marker, SOF11Marker, SOF12Marker, SOF13Marker, SOF14Marker, SOF15Marker};
    public static final int MAX_SEGMENT_SIZE = 65535;
    public static final byte[] PHOTOSHOP_IDENTIFICATION_STRING;
    public static final int SOF0Marker = 65472;
    public static final int SOF10Marker = 65482;
    public static final int SOF11Marker = 65483;
    public static final int SOF12Marker = 65484;
    public static final int SOF13Marker = 65485;
    public static final int SOF14Marker = 65486;
    public static final int SOF15Marker = 65487;
    public static final int SOF1Marker = 65473;
    public static final int SOF2Marker = 65474;
    public static final int SOF3Marker = 65475;
    public static final int SOF4Marker = 65476;
    public static final int SOF5Marker = 65477;
    public static final int SOF6Marker = 65478;
    public static final int SOF7Marker = 65479;
    public static final int SOF8Marker = 65480;
    public static final int SOF9Marker = 65481;
    public static final byte[] SOI = new byte[]{(byte) -1, (byte) -40};
    public static final int SOS_Marker = 65498;
    public static final byte[] XMP_IDENTIFIER;
    public static final byte[] icc_profile_label;

    static {
        byte[] bArr = new byte[5];
        bArr[0] = (byte) 74;
        bArr[1] = (byte) 70;
        bArr[2] = (byte) 73;
        bArr[3] = (byte) 70;
        JFIF0_SIGNATURE = bArr;
        bArr = new byte[29];
        bArr[0] = (byte) 104;
        bArr[1] = (byte) 116;
        bArr[2] = (byte) 116;
        bArr[3] = (byte) 112;
        bArr[4] = HttpTokens.COLON;
        bArr[5] = (byte) 47;
        bArr[6] = (byte) 47;
        bArr[7] = (byte) 110;
        bArr[8] = (byte) 115;
        bArr[9] = (byte) 46;
        bArr[10] = (byte) 97;
        bArr[11] = (byte) 100;
        bArr[12] = (byte) 111;
        bArr[13] = (byte) 98;
        bArr[14] = (byte) 101;
        bArr[15] = (byte) 46;
        bArr[16] = (byte) 99;
        bArr[17] = (byte) 111;
        bArr[18] = (byte) 109;
        bArr[19] = (byte) 47;
        bArr[20] = (byte) 120;
        bArr[21] = (byte) 97;
        bArr[22] = (byte) 112;
        bArr[23] = (byte) 47;
        bArr[24] = (byte) 49;
        bArr[25] = (byte) 46;
        bArr[26] = (byte) 48;
        bArr[27] = (byte) 47;
        XMP_IDENTIFIER = bArr;
        bArr = new byte[12];
        bArr[0] = (byte) 73;
        bArr[1] = (byte) 67;
        bArr[2] = (byte) 67;
        bArr[3] = (byte) 95;
        bArr[4] = (byte) 80;
        bArr[5] = (byte) 82;
        bArr[6] = (byte) 79;
        bArr[7] = (byte) 70;
        bArr[8] = (byte) 73;
        bArr[9] = (byte) 76;
        bArr[10] = (byte) 69;
        icc_profile_label = bArr;
        bArr = new byte[14];
        bArr[0] = (byte) 80;
        bArr[1] = (byte) 104;
        bArr[2] = (byte) 111;
        bArr[3] = (byte) 116;
        bArr[4] = (byte) 111;
        bArr[5] = (byte) 115;
        bArr[6] = (byte) 104;
        bArr[7] = (byte) 111;
        bArr[8] = (byte) 112;
        bArr[9] = (byte) 32;
        bArr[10] = (byte) 51;
        bArr[11] = (byte) 46;
        bArr[12] = (byte) 48;
        PHOTOSHOP_IDENTIFICATION_STRING = bArr;
    }
}
