package org.apache.sanselan.formats.jpeg;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.util.Debug;
import org.mortbay.jetty.HttpVersions;

public class JpegUtils extends BinaryFileParser implements JpegConstants {

    public interface Visitor {
        boolean beginSOS();

        void visitSOS(int i, byte[] bArr, byte[] bArr2);

        boolean visitSOS(int i, byte[] bArr, InputStream inputStream);

        boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException;
    }

    class C13141 implements Visitor {
        C13141() {
        }

        public boolean beginSOS() {
            return true;
        }

        public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            Debug.debug("SOS marker.  " + bArr2.length + " bytes of image data.");
            Debug.debug(HttpVersions.HTTP_0_9);
        }

        public boolean visitSOS(int i, byte[] bArr, InputStream inputStream) {
            return false;
        }

        public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) {
            Debug.debug("Segment marker: " + Integer.toHexString(i) + " (" + JpegUtils.getMarkerName(i) + "), " + bArr3.length + " bytes of segment data.");
            return true;
        }
    }

    public JpegUtils() {
        setByteOrder(77);
    }

    public static String getMarkerName(int i) {
        switch (i) {
            case JpegConstants.SOF0Marker /*65472*/:
                return "SOF0Marker";
            case JpegConstants.SOF1Marker /*65473*/:
                return "SOF1Marker";
            case JpegConstants.SOF2Marker /*65474*/:
                return "SOF2Marker";
            case JpegConstants.SOF3Marker /*65475*/:
                return "SOF3Marker";
            case JpegConstants.SOF4Marker /*65476*/:
                return "SOF4Marker";
            case JpegConstants.SOF5Marker /*65477*/:
                return "SOF5Marker";
            case JpegConstants.SOF6Marker /*65478*/:
                return "SOF6Marker";
            case JpegConstants.SOF7Marker /*65479*/:
                return "SOF7Marker";
            case JpegConstants.SOF8Marker /*65480*/:
                return "SOF8Marker";
            case JpegConstants.SOF9Marker /*65481*/:
                return "SOF9Marker";
            case JpegConstants.SOF10Marker /*65482*/:
                return "SOF10Marker";
            case JpegConstants.SOF11Marker /*65483*/:
                return "SOF11Marker";
            case JpegConstants.SOF12Marker /*65484*/:
                return "SOF12Marker";
            case JpegConstants.SOF13Marker /*65485*/:
                return "SOF13Marker";
            case JpegConstants.SOF14Marker /*65486*/:
                return "SOF14Marker";
            case JpegConstants.SOF15Marker /*65487*/:
                return "SOF15Marker";
            case JpegConstants.SOS_Marker /*65498*/:
                return "SOS_Marker";
            case 65504:
                return "JFIFMarker";
            case JpegConstants.JPEG_APP1_Marker /*65505*/:
                return "JPEG_APP1_Marker";
            case JpegConstants.JPEG_APP2_Marker /*65506*/:
                return "JPEG_APP2_Marker";
            case JpegConstants.JPEG_APP13_Marker /*65517*/:
                return "JPEG_APP13_Marker";
            case JpegConstants.JPEG_APP14_Marker /*65518*/:
                return "JPEG_APP14_Marker";
            case JpegConstants.JPEG_APP15_Marker /*65519*/:
                return "JPEG_APP15_Marker";
            default:
                return "Unknown";
        }
    }

    public void dumpJFIF(ByteSource byteSource) throws ImageReadException, IOException, ImageWriteException {
        traverseJFIF(byteSource, new C13141());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void traverseJFIF(org.apache.sanselan.common.byteSources.ByteSource r12, org.apache.sanselan.formats.jpeg.JpegUtils.Visitor r13) throws org.apache.sanselan.ImageReadException, java.io.IOException {
        /*
        r11 = this;
        r7 = 0;
        r1 = 0;
        r8 = r12.getInputStream();	 Catch:{ all -> 0x0080 }
        r0 = SOI;	 Catch:{ all -> 0x008c }
        r1 = "Not a Valid JPEG File: doesn't begin with 0xffd8";
        r11.readAndVerifyBytes(r8, r0, r1);	 Catch:{ all -> 0x008c }
        r9 = r11.getByteOrder();	 Catch:{ all -> 0x008c }
        r6 = r7;
    L_0x0012:
        r0 = "markerBytes";
        r1 = 2;
        r2 = "markerBytes";
        r2 = r11.readByteArray(r0, r1, r8, r2);	 Catch:{ all -> 0x008c }
        r0 = "marker";
        r1 = r11.convertByteArrayToShort(r0, r2, r9);	 Catch:{ all -> 0x008c }
        r0 = 65497; // 0xffd9 float:9.1781E-41 double:3.236E-319;
        if (r1 == r0) goto L_0x002b;
    L_0x0026:
        r0 = 65498; // 0xffda float:9.1782E-41 double:3.23603E-319;
        if (r1 != r0) goto L_0x0051;
    L_0x002b:
        r0 = r13.beginSOS();	 Catch:{ all -> 0x008c }
        if (r0 != 0) goto L_0x003c;
    L_0x0031:
        if (r8 == 0) goto L_0x0036;
    L_0x0033:
        r8.close();	 Catch:{ Exception -> 0x0037 }
    L_0x0036:
        return;
    L_0x0037:
        r0 = move-exception;
        org.apache.sanselan.util.Debug.debug(r0);
        goto L_0x0036;
    L_0x003c:
        r0 = r13.visitSOS(r1, r2, r8);	 Catch:{ all -> 0x008c }
        if (r0 == 0) goto L_0x004f;
    L_0x0042:
        if (r8 == 0) goto L_0x0036;
    L_0x0044:
        if (r7 == 0) goto L_0x0036;
    L_0x0046:
        r8.close();	 Catch:{ Exception -> 0x004a }
        goto L_0x0036;
    L_0x004a:
        r0 = move-exception;
        org.apache.sanselan.util.Debug.debug(r0);
        goto L_0x0036;
    L_0x004f:
        r7 = 1;
        goto L_0x0042;
    L_0x0051:
        r0 = "segmentLengthBytes";
        r3 = 2;
        r4 = "segmentLengthBytes";
        r4 = r11.readByteArray(r0, r3, r8, r4);	 Catch:{ all -> 0x008c }
        r0 = "segmentLength";
        r3 = r11.convertByteArrayToShort(r0, r4, r9);	 Catch:{ all -> 0x008c }
        r0 = "Segment Data";
        r5 = r3 + -2;
        r10 = "Invalid Segment: insufficient data";
        r5 = r11.readByteArray(r0, r5, r8, r10);	 Catch:{ all -> 0x008c }
        r0 = r13;
        r0 = r0.visitSegment(r1, r2, r3, r4, r5);	 Catch:{ all -> 0x008c }
        if (r0 != 0) goto L_0x007c;
    L_0x0071:
        if (r8 == 0) goto L_0x0036;
    L_0x0073:
        r8.close();	 Catch:{ Exception -> 0x0077 }
        goto L_0x0036;
    L_0x0077:
        r0 = move-exception;
        org.apache.sanselan.util.Debug.debug(r0);
        goto L_0x0036;
    L_0x007c:
        r0 = r6 + 1;
        r6 = r0;
        goto L_0x0012;
    L_0x0080:
        r0 = move-exception;
    L_0x0081:
        if (r1 == 0) goto L_0x0086;
    L_0x0083:
        r1.close();	 Catch:{ Exception -> 0x0087 }
    L_0x0086:
        throw r0;
    L_0x0087:
        r1 = move-exception;
        org.apache.sanselan.util.Debug.debug(r1);
        goto L_0x0086;
    L_0x008c:
        r0 = move-exception;
        r1 = r8;
        goto L_0x0081;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.formats.jpeg.JpegUtils.traverseJFIF(org.apache.sanselan.common.byteSources.ByteSource, org.apache.sanselan.formats.jpeg.JpegUtils$Visitor):void");
    }
}
