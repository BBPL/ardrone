package org.apache.sanselan.formats.tiff;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.FormatCompliance;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.IImageMetadata.IImageMetadataItem;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.tiff.TiffDirectory.ImageDataElement;
import org.apache.sanselan.formats.tiff.TiffImageMetadata.Directory;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.mortbay.jetty.HttpVersions;

public class TiffImageParser extends ImageParser implements TiffConstants {
    private static final String[] ACCEPTED_EXTENSIONS = new String[]{DEFAULT_EXTENSION, ".tiff"};
    private static final String DEFAULT_EXTENSION = ".tif";

    public List collectRawImageData(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffContents readDirectories = new TiffReader(ImageParser.isStrict(map)).readDirectories(byteSource, true, FormatCompliance.getDefault());
        List arrayList = new ArrayList();
        for (int i = 0; i < readDirectories.directories.size(); i++) {
            List tiffRawImageDataElements = ((TiffDirectory) readDirectories.directories.get(i)).getTiffRawImageDataElements();
            for (int i2 = 0; i2 < tiffRawImageDataElements.size(); i2++) {
                ImageDataElement imageDataElement = (ImageDataElement) tiffRawImageDataElements.get(i2);
                arrayList.add(byteSource.getBlock(imageDataElement.offset, imageDataElement.length));
            }
        }
        return arrayList;
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        String str = null;
        printWriter.println("tiff.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return str;
        }
        imageInfo.toString(printWriter, HttpVersions.HTTP_0_9);
        printWriter.println(HttpVersions.HTTP_0_9);
        ArrayList arrayList = new TiffReader(true).readContents(byteSource, null, FormatCompliance.getDefault()).directories;
        if (arrayList == null) {
            printWriter.println(HttpVersions.HTTP_0_9);
            return false;
        }
        int i = 0;
        while (i < arrayList.size()) {
            try {
                ArrayList arrayList2 = ((TiffDirectory) arrayList.get(i)).entries;
                if (arrayList2 == null) {
                    printWriter.println(HttpVersions.HTTP_0_9);
                    return false;
                }
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    ((TiffField) arrayList2.get(i2)).dump(printWriter, new StringBuilder(String.valueOf(i)).toString());
                }
                i++;
            } finally {
                str = HttpVersions.HTTP_0_9;
                printWriter.println(str);
            }
        }
        printWriter.println(HttpVersions.HTTP_0_9);
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
        return ACCEPTED_EXTENSIONS;
    }

    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_TIFF};
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        FormatCompliance formatCompliance = FormatCompliance.getDefault();
        new TiffReader(ImageParser.isStrict(null)).readContents(byteSource, null, formatCompliance);
        return formatCompliance;
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffField findField = ((TiffDirectory) new TiffReader(ImageParser.isStrict(map)).readFirstDirectory(byteSource, map, false, FormatCompliance.getDefault()).directories.get(0)).findField(EXIF_TAG_ICC_PROFILE);
        return findField == null ? null : findField.oversizeValue;
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffContents readDirectories = new TiffReader(ImageParser.isStrict(map)).readDirectories(byteSource, false, FormatCompliance.getDefault());
        TiffDirectory tiffDirectory = (TiffDirectory) readDirectories.directories.get(0);
        TiffField findField = tiffDirectory.findField(TIFF_TAG_IMAGE_WIDTH, true);
        TiffField findField2 = tiffDirectory.findField(TIFF_TAG_IMAGE_LENGTH, true);
        if (findField == null || findField2 == null) {
            throw new ImageReadException("TIFF image missing size info.");
        }
        String str;
        int intValue = findField2.getIntValue();
        int intValue2 = findField.getIntValue();
        findField2 = tiffDirectory.findField(TIFF_TAG_RESOLUTION_UNIT);
        int i = 2;
        if (!(findField2 == null || findField2.getValue() == null)) {
            i = findField2.getIntValue();
        }
        double d = -1.0d;
        switch (i) {
            case 2:
                d = 1.0d;
                break;
            case 3:
                d = 0.0254d;
                break;
        }
        findField = tiffDirectory.findField(TIFF_TAG_XRESOLUTION);
        TiffField findField3 = tiffDirectory.findField(TIFF_TAG_YRESOLUTION);
        int i2 = -1;
        float f = GroundOverlayOptions.NO_DIMENSION;
        int i3 = -1;
        float f2 = GroundOverlayOptions.NO_DIMENSION;
        if (d > 0.0d) {
            double doubleValue;
            if (!(findField == null || findField.getValue() == null)) {
                doubleValue = findField.getDoubleValue();
                i2 = (int) (doubleValue / d);
                f = (float) (((double) intValue2) / (doubleValue * d));
            }
            if (!(findField3 == null || findField3.getValue() == null)) {
                doubleValue = findField3.getDoubleValue();
                i3 = (int) (doubleValue / d);
                f2 = (float) (((double) intValue) / (d * doubleValue));
            }
        }
        findField = tiffDirectory.findField(TIFF_TAG_BITS_PER_SAMPLE);
        int i4 = -1;
        if (!(findField == null || findField.getValue() == null)) {
            i4 = findField.getIntValueOrArraySum();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = tiffDirectory.entries;
        for (int i5 = 0; i5 < arrayList2.size(); i5++) {
            arrayList.add(((TiffField) arrayList2.get(i5)).toString());
        }
        ImageFormat imageFormat = ImageFormat.IMAGE_FORMAT_TIFF;
        int size = readDirectories.directories.size();
        String str2 = "Tiff v." + readDirectories.header.tiffVersion;
        boolean z = false;
        if (tiffDirectory.findField(TIFF_TAG_COLOR_MAP) != null) {
            z = true;
        }
        switch (tiffDirectory.findField(TIFF_TAG_COMPRESSION).getIntValue()) {
            case 1:
                str = ImageInfo.COMPRESSION_ALGORITHM_NONE;
                break;
            case 2:
                str = ImageInfo.COMPRESSION_ALGORITHM_CCITT_1D;
                break;
            case 3:
                str = ImageInfo.COMPRESSION_ALGORITHM_CCITT_GROUP_3;
                break;
            case 4:
                str = ImageInfo.COMPRESSION_ALGORITHM_CCITT_GROUP_4;
                break;
            case 5:
                str = ImageInfo.COMPRESSION_ALGORITHM_LZW;
                break;
            case 6:
                str = ImageInfo.COMPRESSION_ALGORITHM_JPEG;
                break;
            case 32771:
                str = ImageInfo.COMPRESSION_ALGORITHM_NONE;
                break;
            case 32773:
                str = ImageInfo.COMPRESSION_ALGORITHM_PACKBITS;
                break;
            default:
                str = "Unknown";
                break;
        }
        return new ImageInfo(str2, i4, arrayList, imageFormat, "TIFF Tag-based Image File Format", intValue, "image/tiff", size, i3, f2, i2, f, intValue2, false, false, z, 2, str);
    }

    public int[] getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffDirectory tiffDirectory = (TiffDirectory) new TiffReader(ImageParser.isStrict(map)).readFirstDirectory(byteSource, map, false, FormatCompliance.getDefault()).directories.get(0);
        return new int[]{tiffDirectory.findField(TIFF_TAG_IMAGE_WIDTH).getIntValue(), tiffDirectory.findField(TIFF_TAG_IMAGE_LENGTH).getIntValue()};
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffContents readContents = new TiffReader(ImageParser.isStrict(map)).readContents(byteSource, map, FormatCompliance.getDefault());
        ArrayList arrayList = readContents.directories;
        IImageMetadata tiffImageMetadata = new TiffImageMetadata(readContents);
        for (int i = 0; i < arrayList.size(); i++) {
            TiffDirectory tiffDirectory = (TiffDirectory) arrayList.get(i);
            IImageMetadataItem directory = new Directory(tiffDirectory);
            ArrayList directoryEntrys = tiffDirectory.getDirectoryEntrys();
            for (int i2 = 0; i2 < directoryEntrys.size(); i2++) {
                directory.add((TiffField) directoryEntrys.get(i2));
            }
            tiffImageMetadata.add(directory);
        }
        return tiffImageMetadata;
    }

    public String getName() {
        return "Tiff-Custom";
    }

    public String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        TiffField findField = ((TiffDirectory) new TiffReader(ImageParser.isStrict(map)).readDirectories(byteSource, false, FormatCompliance.getDefault()).directories.get(0)).findField(TIFF_TAG_XMP, false);
        if (findField == null) {
            return null;
        }
        try {
            return new String(findField.getByteArrayValue(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ImageReadException("Invalid JPEG XMP Segment.");
        }
    }
}
