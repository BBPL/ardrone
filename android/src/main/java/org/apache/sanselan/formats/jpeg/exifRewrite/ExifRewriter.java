package org.apache.sanselan.formats.jpeg.exifRewrite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.common.byteSources.ByteSourceInputStream;
import org.apache.sanselan.formats.jpeg.JpegConstants;
import org.apache.sanselan.formats.jpeg.JpegUtils;
import org.apache.sanselan.formats.jpeg.JpegUtils.Visitor;
import org.apache.sanselan.formats.tiff.write.TiffImageWriterBase;
import org.apache.sanselan.formats.tiff.write.TiffImageWriterLossless;
import org.apache.sanselan.formats.tiff.write.TiffImageWriterLossy;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.apache.sanselan.util.Debug;

public class ExifRewriter extends BinaryFileParser implements JpegConstants {

    public static class ExifOverflowException extends ImageWriteException {
        public ExifOverflowException(String str) {
            super(str);
        }
    }

    private static abstract class JFIFPiece {
        private JFIFPiece() {
        }

        protected abstract void write(OutputStream outputStream) throws IOException;
    }

    private static class JFIFPieceImageData extends JFIFPiece {
        public final byte[] imageData;
        public final InputStream isImageData;
        public final byte[] markerBytes;

        public JFIFPieceImageData(byte[] bArr, InputStream inputStream) {
            super();
            this.markerBytes = bArr;
            this.imageData = null;
            this.isImageData = inputStream;
        }

        public JFIFPieceImageData(byte[] bArr, byte[] bArr2) {
            super();
            this.markerBytes = bArr;
            this.imageData = bArr2;
            this.isImageData = null;
        }

        protected void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            if (this.imageData != null) {
                outputStream.write(this.imageData);
                return;
            }
            byte[] bArr = new byte[1024];
            while (true) {
                int read = this.isImageData.read(bArr);
                if (read <= 0) {
                    try {
                        this.isImageData.close();
                        return;
                    } catch (Exception e) {
                        return;
                    }
                }
                outputStream.write(bArr, 0, read);
            }
        }
    }

    private static class JFIFPieceSegment extends JFIFPiece {
        public final int marker;
        public final byte[] markerBytes;
        public final byte[] markerLengthBytes;
        public final byte[] segmentData;

        public JFIFPieceSegment(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            super();
            this.marker = i;
            this.markerBytes = bArr;
            this.markerLengthBytes = bArr2;
            this.segmentData = bArr3;
        }

        protected void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.markerLengthBytes);
            outputStream.write(this.segmentData);
        }
    }

    private static class JFIFPieceSegmentExif extends JFIFPieceSegment {
        public JFIFPieceSegmentExif(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            super(i, bArr, bArr2, bArr3);
        }
    }

    private static class JFIFPieces {
        public final List exifPieces;
        public final List pieces;

        public JFIFPieces(List list, List list2) {
            this.pieces = list;
            this.exifPieces = list2;
        }
    }

    public ExifRewriter() {
        setByteOrder(77);
    }

    public ExifRewriter(int i) {
        setByteOrder(i);
    }

    private JFIFPieces analyzeJFIF(ByteSource byteSource) throws ImageReadException, IOException {
        final List arrayList = new ArrayList();
        final List arrayList2 = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, new Visitor() {
            public boolean beginSOS() {
                return true;
            }

            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
                arrayList.add(new JFIFPieceImageData(bArr, bArr2));
            }

            public boolean visitSOS(int i, byte[] bArr, InputStream inputStream) {
                arrayList.add(new JFIFPieceImageData(bArr, inputStream));
                return true;
            }

            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws ImageReadException, IOException {
                if (i != JpegConstants.JPEG_APP1_Marker) {
                    arrayList.add(new JFIFPieceSegment(i, bArr, bArr2, bArr3));
                } else if (BinaryFileParser.byteArrayHasPrefix(bArr3, ExifRewriter.EXIF_IDENTIFIER_CODE)) {
                    JFIFPieceSegmentExif jFIFPieceSegmentExif = new JFIFPieceSegmentExif(i, bArr, bArr2, bArr3);
                    arrayList.add(jFIFPieceSegmentExif);
                    arrayList2.add(jFIFPieceSegmentExif);
                } else {
                    arrayList.add(new JFIFPieceSegment(i, bArr, bArr2, bArr3));
                }
                return true;
            }
        });
        return new JFIFPieces(arrayList, arrayList2);
    }

    private byte[] writeExifSegment(TiffImageWriterBase tiffImageWriterBase, TiffOutputSet tiffOutputSet, boolean z) throws IOException, ImageWriteException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (z) {
            byteArrayOutputStream.write(EXIF_IDENTIFIER_CODE);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
        }
        tiffImageWriterBase.write(byteArrayOutputStream, tiffOutputSet);
        return byteArrayOutputStream.toByteArray();
    }

    private void writeSegmentsReplacingExif(OutputStream outputStream, List list, byte[] bArr) throws ImageWriteException, IOException {
        byte[] convertShortToByteArray;
        int i = 0;
        int byteOrder = getByteOrder();
        outputStream.write(SOI);
        int i2 = 0;
        int i3 = 0;
        while (i3 < list.size()) {
            try {
                int i4 = ((JFIFPiece) list.get(i3)) instanceof JFIFPieceSegmentExif ? 1 : i2;
                i3++;
                i2 = i4;
            } finally {
                try {
                    outputStream.close();
                } catch (Throwable e) {
                    Debug.debug(e);
                }
            }
        }
        if (i2 == 0 && bArr != null) {
            convertShortToByteArray = convertShortToByteArray(JpegConstants.JPEG_APP1_Marker, byteOrder);
            if (bArr.length > 65535) {
                throw new ExifOverflowException("APP1 Segment is too long: " + bArr.length);
            }
            byte[] convertShortToByteArray2 = convertShortToByteArray(bArr.length + 2, byteOrder);
            if (((JFIFPieceSegment) list.get(0)).marker == 65504) {
                list.add(0, new JFIFPieceSegmentExif(JpegConstants.JPEG_APP1_Marker, convertShortToByteArray, convertShortToByteArray2, bArr));
            } else {
                list.add(0, new JFIFPieceSegmentExif(JpegConstants.JPEG_APP1_Marker, convertShortToByteArray, convertShortToByteArray2, bArr));
            }
        }
        i2 = 0;
        while (i < list.size()) {
            JFIFPiece jFIFPiece = (JFIFPiece) list.get(i);
            if (!(jFIFPiece instanceof JFIFPieceSegmentExif)) {
                jFIFPiece.write(outputStream);
                i4 = i2;
            } else if (i2 != 0) {
                i4 = i2;
            } else if (bArr != null) {
                byte[] convertShortToByteArray3 = convertShortToByteArray(JpegConstants.JPEG_APP1_Marker, byteOrder);
                if (bArr.length > 65535) {
                    throw new ExifOverflowException("APP1 Segment is too long: " + bArr.length);
                }
                convertShortToByteArray = convertShortToByteArray(bArr.length + 2, byteOrder);
                outputStream.write(convertShortToByteArray3);
                outputStream.write(convertShortToByteArray);
                outputStream.write(bArr);
                i4 = 1;
            } else {
                i4 = 1;
            }
            i++;
            i2 = i4;
        }
    }

    public void removeExifMetadata(File file, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeExifMetadata(new ByteSourceFile(file), outputStream);
    }

    public void removeExifMetadata(InputStream inputStream, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeExifMetadata(new ByteSourceInputStream(inputStream, null), outputStream);
    }

    public void removeExifMetadata(ByteSource byteSource, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        writeSegmentsReplacingExif(outputStream, analyzeJFIF(byteSource).pieces, null);
    }

    public void removeExifMetadata(byte[] bArr, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeExifMetadata(new ByteSourceArray(bArr), outputStream);
    }

    public void updateExifMetadataLossless(File file, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossless(new ByteSourceFile(file), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossless(InputStream inputStream, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossless(new ByteSourceInputStream(inputStream, null), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossless(ByteSource byteSource, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        TiffImageWriterBase tiffImageWriterLossless;
        JFIFPieces analyzeJFIF = analyzeJFIF(byteSource);
        List list = analyzeJFIF.pieces;
        if (analyzeJFIF.exifPieces.size() > 0) {
            tiffImageWriterLossless = new TiffImageWriterLossless(tiffOutputSet.byteOrder, getByteArrayTail("trimmed exif bytes", ((JFIFPieceSegment) analyzeJFIF.exifPieces.get(0)).segmentData, 6));
        } else {
            tiffImageWriterLossless = new TiffImageWriterLossy(tiffOutputSet.byteOrder);
        }
        writeSegmentsReplacingExif(outputStream, list, writeExifSegment(tiffImageWriterLossless, tiffOutputSet, true));
    }

    public void updateExifMetadataLossless(byte[] bArr, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossless(new ByteSourceArray(bArr), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(File file, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossy(new ByteSourceFile(file), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(InputStream inputStream, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossy(new ByteSourceInputStream(inputStream, null), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(ByteSource byteSource, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        writeSegmentsReplacingExif(outputStream, analyzeJFIF(byteSource).pieces, writeExifSegment(new TiffImageWriterLossy(tiffOutputSet.byteOrder), tiffOutputSet, true));
    }

    public void updateExifMetadataLossy(byte[] bArr, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageReadException, IOException, ImageWriteException {
        updateExifMetadataLossy(new ByteSourceArray(bArr), outputStream, tiffOutputSet);
    }
}
