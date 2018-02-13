package org.apache.sanselan.formats.tiff.write;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.formats.tiff.JpegImageData;
import org.apache.sanselan.formats.tiff.TiffDirectory;
import org.apache.sanselan.formats.tiff.constants.TagConstantsUtils;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeLong;
import org.apache.sanselan.formats.tiff.write.TiffOutputItem.Value;

public final class TiffOutputDirectory extends TiffOutputItem implements TiffConstants {
    private final ArrayList fields = new ArrayList();
    private JpegImageData jpegImageData = null;
    private TiffOutputDirectory nextDirectory = null;
    public final int type;

    class C13191 implements Comparator {
        C13191() {
        }

        public int compare(Object obj, Object obj2) {
            TiffOutputField tiffOutputField = (TiffOutputField) obj;
            TiffOutputField tiffOutputField2 = (TiffOutputField) obj2;
            return tiffOutputField.tag != tiffOutputField2.tag ? tiffOutputField.tag - tiffOutputField2.tag : tiffOutputField.getSortHint() - tiffOutputField2.getSortHint();
        }
    }

    public TiffOutputDirectory(int i) {
        this.type = i;
    }

    private void removeFieldIfPresent(TagInfo tagInfo) {
        TiffOutputField findField = findField(tagInfo);
        if (findField != null) {
            this.fields.remove(findField);
        }
    }

    public void add(TiffOutputField tiffOutputField) {
        this.fields.add(tiffOutputField);
    }

    public String description() {
        return TiffDirectory.description(this.type);
    }

    public TiffOutputField findField(int i) {
        for (int i2 = 0; i2 < this.fields.size(); i2++) {
            TiffOutputField tiffOutputField = (TiffOutputField) this.fields.get(i2);
            if (tiffOutputField.tag == i) {
                return tiffOutputField;
            }
        }
        return null;
    }

    public TiffOutputField findField(TagInfo tagInfo) {
        return findField(tagInfo.tag);
    }

    public ArrayList getFields() {
        return new ArrayList(this.fields);
    }

    public String getItemDescription() {
        return "Directory: " + TagConstantsUtils.getExifDirectoryType(this.type).name + " (" + this.type + ")";
    }

    public int getItemLength() {
        return ((this.fields.size() * 12) + 2) + 4;
    }

    protected List getOutputItems(TiffOutputSummary tiffOutputSummary) throws ImageWriteException {
        TiffOutputField tiffOutputField;
        int i = 0;
        removeFieldIfPresent(TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        removeFieldIfPresent(TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (this.jpegImageData != null) {
            TiffOutputField tiffOutputField2 = new TiffOutputField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT, FIELD_TYPE_LONG, 1, FieldType.getStubLocalValue());
            add(tiffOutputField2);
            FieldTypeLong fieldTypeLong = FIELD_TYPE_LONG;
            int i2 = this.jpegImageData.length;
            Object obj = new int[]{i2};
            add(new TiffOutputField(TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, FIELD_TYPE_LONG, 1, fieldTypeLong.writeData(obj, tiffOutputSummary.byteOrder)));
            tiffOutputField = tiffOutputField2;
        } else {
            tiffOutputField = null;
        }
        removeFieldIfPresent(TIFF_TAG_STRIP_OFFSETS);
        removeFieldIfPresent(TIFF_TAG_STRIP_BYTE_COUNTS);
        removeFieldIfPresent(TIFF_TAG_TILE_OFFSETS);
        removeFieldIfPresent(TIFF_TAG_TILE_BYTE_COUNTS);
        List arrayList = new ArrayList();
        arrayList.add(this);
        sortFields();
        while (i < this.fields.size()) {
            tiffOutputField2 = (TiffOutputField) this.fields.get(i);
            if (!tiffOutputField2.isLocalValue()) {
                arrayList.add(tiffOutputField2.getSeperateValue());
            }
            i++;
        }
        if (this.jpegImageData != null) {
            TiffOutputItem value = new Value("JPEG image data", this.jpegImageData.data);
            arrayList.add(value);
            tiffOutputSummary.add(value, tiffOutputField);
        }
        return arrayList;
    }

    public JpegImageData getRawJpegImageData() {
        return this.jpegImageData;
    }

    public void removeField(int i) {
        Collection arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.fields.size(); i2++) {
            TiffOutputField tiffOutputField = (TiffOutputField) this.fields.get(i2);
            if (tiffOutputField.tag == i) {
                arrayList.add(tiffOutputField);
            }
        }
        this.fields.removeAll(arrayList);
    }

    public void removeField(TagInfo tagInfo) {
        removeField(tagInfo.tag);
    }

    public void setJpegImageData(JpegImageData jpegImageData) {
        this.jpegImageData = jpegImageData;
    }

    public void setNextDirectory(TiffOutputDirectory tiffOutputDirectory) {
        this.nextDirectory = tiffOutputDirectory;
    }

    public void sortFields() {
        Collections.sort(this.fields, new C13191());
    }

    public void writeItem(BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
        binaryOutputStream.write2Bytes(this.fields.size());
        for (int i = 0; i < this.fields.size(); i++) {
            ((TiffOutputField) this.fields.get(i)).writeField(binaryOutputStream);
        }
        int offset = this.nextDirectory != null ? this.nextDirectory.getOffset() : 0;
        if (offset == -1) {
            binaryOutputStream.write4Bytes(0);
        } else {
            binaryOutputStream.write4Bytes(offset);
        }
    }
}
