package org.apache.sanselan.formats.tiff;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public class TiffDirectory extends TiffElement implements TiffConstants {
    public final ArrayList entries;
    private JpegImageData jpegImageData = null;
    public final int nextDirectoryOffset;
    public final int type;

    public final class ImageDataElement extends TiffElement {
        public ImageDataElement(int i, int i2) {
            super(i, i2);
        }

        public String getElementDescription(boolean z) {
            return z ? null : "ImageDataElement";
        }
    }

    public TiffDirectory(int i, ArrayList arrayList, int i2, int i3) {
        super(i2, ((arrayList.size() * 12) + 2) + 4);
        this.type = i;
        this.entries = arrayList;
        this.nextDirectoryOffset = i3;
    }

    public static final String description(int i) {
        switch (i) {
            case -4:
                return "Interoperability";
            case -3:
                return "Gps";
            case -2:
                return "Exif";
            case -1:
                return "Unknown";
            case 0:
                return "Root";
            case 1:
                return "Sub";
            case 2:
                return "Thumbnail";
            default:
                return "Bad Type";
        }
    }

    private ArrayList getRawImageDataElements(TiffField tiffField, TiffField tiffField2) throws ImageReadException {
        int[] intArrayValue = tiffField.getIntArrayValue();
        int[] intArrayValue2 = tiffField2.getIntArrayValue();
        if (intArrayValue.length != intArrayValue2.length) {
            throw new ImageReadException("offsets.length(" + intArrayValue.length + ") != byteCounts.length(" + intArrayValue2.length + ")");
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < intArrayValue.length; i++) {
            arrayList.add(new ImageDataElement(intArrayValue[i], intArrayValue2[i]));
        }
        return arrayList;
    }

    public String description() {
        return description(this.type);
    }

    public void dump() {
        for (int i = 0; i < this.entries.size(); i++) {
            ((TiffField) this.entries.get(i)).dump();
        }
    }

    protected void fillInValues(ByteSource byteSource) throws ImageReadException, IOException {
        for (int i = 0; i < this.entries.size(); i++) {
            ((TiffField) this.entries.get(i)).fillInValue(byteSource);
        }
    }

    public TiffField findField(TagInfo tagInfo) throws ImageReadException {
        return findField(tagInfo, false);
    }

    public TiffField findField(TagInfo tagInfo, boolean z) throws ImageReadException {
        TiffField tiffField;
        if (this.entries == null) {
            tiffField = null;
        } else {
            int i = 0;
            while (i < this.entries.size()) {
                tiffField = (TiffField) this.entries.get(i);
                if (tiffField.tag != tagInfo.tag) {
                    i++;
                }
            }
            if (!z) {
                return null;
            }
            throw new ImageReadException("Missing expected field: " + tagInfo.getDescription());
        }
        return tiffField;
    }

    public ArrayList getDirectoryEntrys() {
        return new ArrayList(this.entries);
    }

    public String getElementDescription(boolean z) {
        if (!z) {
            return "TIFF Directory (" + description() + ")";
        }
        int i = this.offset + 2;
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = 0;
        int i3 = i;
        while (i2 < this.entries.size()) {
            TiffField tiffField = (TiffField) this.entries.get(i2);
            stringBuffer.append("\t");
            stringBuffer.append("[" + i3 + "]: ");
            stringBuffer.append(tiffField.tagInfo.name);
            stringBuffer.append(" (" + tiffField.tag + ", 0x" + Integer.toHexString(tiffField.tag) + ")");
            stringBuffer.append(", " + tiffField.fieldType.name);
            stringBuffer.append(", " + tiffField.fieldType.getRawBytes(tiffField).length);
            stringBuffer.append(": " + tiffField.getValueDescription());
            stringBuffer.append("\n");
            i2++;
            i3 += 12;
        }
        return stringBuffer.toString();
    }

    public JpegImageData getJpegImageData() {
        return this.jpegImageData;
    }

    public ImageDataElement getJpegRawImageDataElement() throws ImageReadException {
        TiffField findField = findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        TiffField findField2 = findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (findField != null && findField2 != null) {
            return new ImageDataElement(findField.getIntArrayValue()[0], findField2.getIntArrayValue()[0]);
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public ArrayList getTiffRawImageDataElements() throws ImageReadException {
        TiffField findField = findField(TIFF_TAG_TILE_OFFSETS);
        TiffField findField2 = findField(TIFF_TAG_TILE_BYTE_COUNTS);
        TiffField findField3 = findField(TIFF_TAG_STRIP_OFFSETS);
        TiffField findField4 = findField(TIFF_TAG_STRIP_BYTE_COUNTS);
        if (findField != null && findField2 != null) {
            return getRawImageDataElements(findField, findField2);
        }
        if (findField3 != null && findField4 != null) {
            return getRawImageDataElements(findField3, findField4);
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public boolean hasJpegImageData() throws ImageReadException {
        return findField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT) != null;
    }

    public boolean hasTiffImageData() throws ImageReadException {
        return (findField(TIFF_TAG_TILE_OFFSETS) == null && findField(TIFF_TAG_STRIP_OFFSETS) == null) ? false : true;
    }

    public boolean imageDataInStrips() throws ImageReadException {
        TiffField findField = findField(TIFF_TAG_TILE_OFFSETS);
        TiffField findField2 = findField(TIFF_TAG_TILE_BYTE_COUNTS);
        TiffField findField3 = findField(TIFF_TAG_STRIP_OFFSETS);
        TiffField findField4 = findField(TIFF_TAG_STRIP_BYTE_COUNTS);
        if (findField != null && findField2 != null) {
            return false;
        }
        if (findField3 != null && findField4 != null) {
            return true;
        }
        if (findField3 != null && findField4 != null) {
            return true;
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public void setJpegImageData(JpegImageData jpegImageData) {
        this.jpegImageData = jpegImageData;
    }
}
