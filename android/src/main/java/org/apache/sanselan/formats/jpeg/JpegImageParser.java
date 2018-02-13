package org.apache.sanselan.formats.jpeg;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.jpeg.JpegUtils.Visitor;
import org.apache.sanselan.formats.jpeg.segments.App2Segment;
import org.apache.sanselan.formats.jpeg.segments.GenericSegment;
import org.apache.sanselan.formats.jpeg.segments.JFIFSegment;
import org.apache.sanselan.formats.jpeg.segments.SOFNSegment;
import org.apache.sanselan.formats.jpeg.segments.Segment;
import org.apache.sanselan.formats.jpeg.segments.UnknownSegment;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageParser;
import org.apache.sanselan.formats.tiff.constants.TiffTagConstants;
import org.apache.sanselan.util.Debug;
import org.mortbay.jetty.HttpVersions;

public class JpegImageParser extends ImageParser implements JpegConstants, TiffTagConstants {
    public static final String[] AcceptedExtensions = new String[]{DEFAULT_EXTENSION, ".jpeg"};
    private static final String DEFAULT_EXTENSION = ".jpg";
    public static final boolean permissive = true;

    public JpegImageParser() {
        setByteOrder(77);
    }

    private byte[] assembleSegments(ArrayList arrayList) throws ImageReadException, IOException {
        try {
            return assembleSegments(arrayList, false);
        } catch (ImageReadException e) {
            return assembleSegments(arrayList, true);
        }
    }

    private byte[] assembleSegments(ArrayList arrayList, boolean z) throws ImageReadException, IOException {
        int i = 1;
        if (arrayList.size() < 1) {
            throw new ImageReadException("No App2 Segments Found.");
        }
        int i2 = ((App2Segment) arrayList.get(0)).num_markers;
        if (arrayList.size() != i2) {
            throw new ImageReadException("App2 Segments Missing.  Found: " + arrayList.size() + ", Expected: " + i2 + ".");
        }
        Collections.sort(arrayList);
        if (z) {
            i = 0;
        }
        int i3 = 0;
        int i4 = 0;
        while (i4 < arrayList.size()) {
            App2Segment app2Segment = (App2Segment) arrayList.get(i4);
            if (i4 + i != app2Segment.cur_marker) {
                dumpSegments(arrayList);
                throw new ImageReadException("Incoherent App2 Segment Ordering.  i: " + i4 + ", segment[" + i4 + "].cur_marker: " + app2Segment.cur_marker + ".");
            } else if (i2 != app2Segment.num_markers) {
                dumpSegments(arrayList);
                throw new ImageReadException("Inconsistent App2 Segment Count info.  markerCount: " + i2 + ", segment[" + i4 + "].num_markers: " + app2Segment.num_markers + ".");
            } else {
                i4++;
                i3 = app2Segment.icc_bytes.length + i3;
            }
        }
        Object obj = new byte[i3];
        i = 0;
        i3 = 0;
        while (i3 < arrayList.size()) {
            app2Segment = (App2Segment) arrayList.get(i3);
            System.arraycopy(app2Segment.icc_bytes, 0, obj, i, app2Segment.icc_bytes.length);
            i3++;
            i = app2Segment.icc_bytes.length + i;
        }
        return obj;
    }

    private void dumpSegments(ArrayList arrayList) {
        Debug.debug();
        Debug.debug("dumpSegments", arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            App2Segment app2Segment = (App2Segment) arrayList.get(i);
            Debug.debug(new StringBuilder(String.valueOf(i)).append(": ").append(app2Segment.cur_marker).append(" / ").append(app2Segment.num_markers).toString());
        }
        Debug.debug();
    }

    private ArrayList filterAPP1Segments(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            GenericSegment genericSegment = (GenericSegment) arrayList.get(i);
            if (isExifAPP1Segment(genericSegment)) {
                arrayList2.add(genericSegment);
            }
        }
        return arrayList2;
    }

    private ArrayList filterSegments(ArrayList arrayList, List list) {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            Segment segment = (Segment) arrayList.get(i);
            if (list.contains(new Integer(segment.marker))) {
                arrayList2.add(segment);
            }
        }
        return arrayList2;
    }

    public static boolean isExifAPP1Segment(GenericSegment genericSegment) {
        return BinaryFileParser.byteArrayHasPrefix(genericSegment.bytes, EXIF_IDENTIFIER_CODE);
    }

    private boolean keepMarker(int i, int[] iArr) {
        if (iArr != null) {
            int i2 = 0;
            while (i2 < iArr.length) {
                if (iArr[i2] != i) {
                    i2++;
                }
            }
            return false;
        }
        return true;
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("tiff.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, HttpVersions.HTTP_0_9);
        printWriter.println(HttpVersions.HTTP_0_9);
        ArrayList readSegments = readSegments(byteSource, null, false);
        if (readSegments == null) {
            throw new ImageReadException("No Segments Found.");
        }
        for (int i = 0; i < readSegments.size(); i++) {
            Segment segment = (Segment) readSegments.get(i);
            printWriter.println(new StringBuilder(String.valueOf(i)).append(": marker: ").append(Integer.toHexString(segment.marker)).append(", ").append(segment.getDescription()).append(" (length: ").append(NumberFormat.getIntegerInstance().format((long) segment.length)).append(")").toString());
            segment.dump(printWriter);
        }
        printWriter.println(HttpVersions.HTTP_0_9);
        return true;
    }

    public boolean embedICCProfile(File file, File file2, byte[] bArr) {
        return false;
    }

    public byte[] embedICCProfile(byte[] bArr, byte[] bArr2) {
        return null;
    }

    protected String[] getAcceptedExtensions() {
        return AcceptedExtensions;
    }

    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_JPEG};
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public TiffImageMetadata getExifMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        byte[] exifRawData = getExifRawData(byteSource);
        if (exifRawData == null) {
            return null;
        }
        if (map == null) {
            map = new HashMap();
        }
        if (!map.containsKey(SanselanConstants.PARAM_KEY_READ_THUMBNAILS)) {
            map.put(SanselanConstants.PARAM_KEY_READ_THUMBNAILS, Boolean.TRUE);
        }
        return (TiffImageMetadata) new TiffImageParser().getMetadata(exifRawData, map);
    }

    public byte[] getExifRawData(ByteSource byteSource) throws ImageReadException, IOException {
        ArrayList readSegments = readSegments(byteSource, new int[]{JpegConstants.JPEG_APP1_Marker}, false);
        if (readSegments != null && readSegments.size() >= 1) {
            readSegments = filterAPP1Segments(readSegments);
            if (this.debug) {
                System.out.println("exif_segments.size: " + readSegments.size());
            }
            if (readSegments.size() >= 1) {
                if (readSegments.size() <= 1) {
                    return getByteArrayTail("trimmed exif bytes", ((GenericSegment) readSegments.get(0)).bytes, 6);
                }
                throw new ImageReadException("Sanselan currently can't parse EXIF metadata split across multiple APP1 segments.  Please send this image to the Sanselan project.");
            }
        }
        return null;
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList arrayList;
        String str = null;
        ArrayList readSegments = readSegments(byteSource, new int[]{JpegConstants.JPEG_APP2_Marker}, false);
        if (readSegments != null) {
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < readSegments.size(); i++) {
                App2Segment app2Segment = (App2Segment) readSegments.get(i);
                if (app2Segment.icc_bytes != null) {
                    arrayList2.add(app2Segment);
                }
            }
            arrayList = arrayList2;
        } else {
            arrayList = readSegments;
        }
        if (arrayList == null || arrayList.size() < 1) {
            return null;
        }
        byte[] assembleSegments = assembleSegments(arrayList);
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder("bytes: ");
            if (assembleSegments != null) {
                str = assembleSegments.length;
            }
            printStream.println(stringBuilder.append(str).toString());
        }
        if (!this.debug) {
            return assembleSegments;
        }
        System.out.println(HttpVersions.HTTP_0_9);
        return assembleSegments;
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList readSegments = readSegments(byteSource, new int[]{JpegConstants.SOF0Marker, JpegConstants.SOF1Marker, JpegConstants.SOF2Marker, JpegConstants.SOF3Marker, JpegConstants.SOF5Marker, JpegConstants.SOF6Marker, JpegConstants.SOF7Marker, JpegConstants.SOF9Marker, JpegConstants.SOF10Marker, JpegConstants.SOF11Marker, JpegConstants.SOF13Marker, JpegConstants.SOF14Marker, JpegConstants.SOF15Marker}, false);
        if (readSegments == null) {
            throw new ImageReadException("No SOFN Data Found.");
        }
        ArrayList readSegments2 = readSegments(byteSource, new int[]{65504}, true);
        SOFNSegment sOFNSegment = (SOFNSegment) readSegments.get(0);
        if (sOFNSegment == null) {
            throw new ImageReadException("No SOFN Data Found.");
        }
        String str;
        double d;
        int i = sOFNSegment.width;
        int i2 = sOFNSegment.height;
        JFIFSegment jFIFSegment = null;
        if (readSegments2 != null && readSegments2.size() > 0) {
            jFIFSegment = (JFIFSegment) readSegments2.get(0);
        }
        if (jFIFSegment != null) {
            double d2 = (double) jFIFSegment.xDensity;
            double d3 = (double) jFIFSegment.yDensity;
            int i3 = jFIFSegment.densityUnits;
            str = "Jpeg/JFIF v." + jFIFSegment.jfifMajorVersion + "." + jFIFSegment.jfifMinorVersion;
            switch (i3) {
                case 0:
                    d = -1.0d;
                    break;
                case 1:
                    d = 1.0d;
                    break;
                case 2:
                    d = 2.54d;
                    break;
                default:
                    d = -1.0d;
                    break;
            }
        }
        double d4;
        JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) getMetadata(byteSource, map);
        if (jpegImageMetadata != null) {
            TiffField findEXIFValue = jpegImageMetadata.findEXIFValue(TIFF_TAG_XRESOLUTION);
            d2 = findEXIFValue != null ? ((Number) findEXIFValue.getValue()).doubleValue() : -1.0d;
            TiffField findEXIFValue2 = jpegImageMetadata.findEXIFValue(TIFF_TAG_YRESOLUTION);
            d3 = findEXIFValue2 != null ? ((Number) findEXIFValue2.getValue()).doubleValue() : -1.0d;
            TiffField findEXIFValue3 = jpegImageMetadata.findEXIFValue(TIFF_TAG_RESOLUTION_UNIT);
            if (findEXIFValue3 != null) {
                switch (((Number) findEXIFValue3.getValue()).intValue()) {
                    case 1:
                        break;
                    case 2:
                        d4 = 1.0d;
                        break;
                    case 3:
                        d4 = 2.54d;
                        break;
                    default:
                        d4 = -1.0d;
                        break;
                }
            }
            d4 = -1.0d;
        } else {
            d2 = -1.0d;
            d3 = -1.0d;
            d4 = -1.0d;
        }
        str = "Jpeg/DCM";
        d = d4;
        int i4 = -1;
        float f = GroundOverlayOptions.NO_DIMENSION;
        int i5 = -1;
        float f2 = GroundOverlayOptions.NO_DIMENSION;
        if (d > 0.0d) {
            i5 = (int) Math.round(d2 / d);
            f2 = (float) (((double) i) / (d2 * d));
            i4 = (int) Math.round(d3 * d);
            f = (float) (((double) i2) / (d3 * d));
        }
        ArrayList arrayList = new ArrayList();
        int i6 = sOFNSegment.numberOfComponents;
        int i7 = sOFNSegment.precision;
        ImageFormat imageFormat = ImageFormat.IMAGE_FORMAT_JPEG;
        boolean z = sOFNSegment.marker == JpegConstants.SOF2Marker;
        int i8 = i6 == 1 ? 0 : i6 == 3 ? 2 : i6 == 4 ? 3 : -2;
        return new ImageInfo(str, i6 * i7, arrayList, imageFormat, "JPEG (Joint Photographic Experts Group) Format", i2, "image/jpeg", 1, i4, f, i5, f2, i, z, false, false, i8, ImageInfo.COMPRESSION_ALGORITHM_JPEG);
    }

    public int[] getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ArrayList readSegments = readSegments(byteSource, new int[]{JpegConstants.SOF0Marker, JpegConstants.SOF1Marker, JpegConstants.SOF2Marker, JpegConstants.SOF3Marker, JpegConstants.SOF5Marker, JpegConstants.SOF6Marker, JpegConstants.SOF7Marker, JpegConstants.SOF9Marker, JpegConstants.SOF10Marker, JpegConstants.SOF11Marker, JpegConstants.SOF13Marker, JpegConstants.SOF14Marker, JpegConstants.SOF15Marker}, true);
        if (readSegments == null || readSegments.size() < 1) {
            throw new ImageReadException("No JFIF Data Found.");
        } else if (readSegments.size() > 1) {
            throw new ImageReadException("Redundant JFIF Data Found.");
        } else {
            SOFNSegment sOFNSegment = (SOFNSegment) readSegments.get(0);
            return new int[]{sOFNSegment.width, sOFNSegment.height};
        }
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffImageMetadata exifMetadata = getExifMetadata(byteSource, map);
        return exifMetadata == null ? null : new JpegImageMetadata(null, exifMetadata);
    }

    public String getName() {
        return "Jpeg-Custom";
    }

    public Object getPhotoshopMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public boolean hasExifSegment(ByteSource byteSource) throws ImageReadException, IOException {
        final boolean[] zArr = new boolean[1];
        new JpegUtils().traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            public boolean visitSOS(int i, byte[] bArr, InputStream inputStream) {
                return false;
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i == 65497) {
                    return false;
                }
                if (i != JpegConstants.JPEG_APP1_Marker || !BinaryFileParser.byteArrayHasPrefix(bArr3, JpegImageParser.EXIF_IDENTIFIER_CODE)) {
                    return true;
                }
                zArr[0] = true;
                return false;
            }
        });
        return zArr[0];
    }

    public boolean hasIptcSegment(ByteSource byteSource) throws ImageReadException, IOException {
        return new boolean[1][0];
    }

    public boolean hasXmpSegment(ByteSource byteSource) throws ImageReadException, IOException {
        return new boolean[1][0];
    }

    public ArrayList readSegments(ByteSource byteSource, int[] iArr, boolean z) throws ImageReadException, IOException {
        return readSegments(byteSource, iArr, z, false);
    }

    public ArrayList readSegments(ByteSource byteSource, final int[] iArr, final boolean z, boolean z2) throws ImageReadException, IOException {
        final ArrayList arrayList = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return false;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            public boolean visitSOS(int i, byte[] bArr, InputStream inputStream) {
                return false;
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i != 65497) {
                    if (!JpegImageParser.this.keepMarker(i, iArr)) {
                        return true;
                    }
                    if (i != JpegConstants.JPEG_APP13_Marker) {
                        if (i == JpegConstants.JPEG_APP2_Marker) {
                            arrayList.add(new App2Segment(i, bArr3));
                        } else if (i == 65504) {
                            arrayList.add(new JFIFSegment(i, bArr3));
                        } else if (i >= JpegConstants.SOF0Marker && i <= JpegConstants.SOF15Marker) {
                            arrayList.add(new SOFNSegment(i, bArr3));
                        } else if (i >= JpegConstants.JPEG_APP1_Marker && i <= JpegConstants.JPEG_APP15_Marker) {
                            arrayList.add(new UnknownSegment(i, bArr3));
                        }
                    }
                    if (!z) {
                        return true;
                    }
                }
                return false;
            }
        });
        return arrayList;
    }
}
