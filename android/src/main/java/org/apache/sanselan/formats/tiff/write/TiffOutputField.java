package org.apache.sanselan.formats.tiff.write;

import java.io.IOException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;
import org.apache.sanselan.formats.tiff.write.TiffOutputItem.Value;
import org.mortbay.jetty.HttpVersions;

public class TiffOutputField implements TiffConstants {
    private static final String newline = System.getProperty("line.separator");
    private byte[] bytes;
    public final int count;
    public final FieldType fieldType;
    private final Value separateValueItem;
    private int sortHint;
    public final int tag;
    public final TagInfo tagInfo;

    public TiffOutputField(int i, TagInfo tagInfo, FieldType fieldType, int i2, byte[] bArr) {
        this.sortHint = -1;
        this.tag = i;
        this.tagInfo = tagInfo;
        this.fieldType = fieldType;
        this.count = i2;
        this.bytes = bArr;
        if (isLocalValue()) {
            this.separateValueItem = null;
        } else {
            this.separateValueItem = new Value("Field Seperate value (" + tagInfo.getDescription() + ")", bArr);
        }
    }

    public TiffOutputField(TagInfo tagInfo, FieldType fieldType, int i, byte[] bArr) {
        this(tagInfo.tag, tagInfo, fieldType, i, bArr);
    }

    public static TiffOutputField create(TagInfo tagInfo, int i, Number number) throws ImageWriteException {
        if (tagInfo.dataTypes == null || tagInfo.dataTypes.length < 1) {
            throw new ImageWriteException("Tag has no default data type.");
        }
        FieldType fieldType = tagInfo.dataTypes[0];
        if (tagInfo.length != 1) {
            throw new ImageWriteException("Tag does not expect a single value.");
        }
        return new TiffOutputField(tagInfo.tag, tagInfo, fieldType, 1, fieldType.writeData(number, i));
    }

    public static TiffOutputField create(TagInfo tagInfo, int i, String str) throws ImageWriteException {
        FieldType fieldType;
        if (tagInfo.dataTypes == null) {
            fieldType = FIELD_TYPE_ASCII;
        } else if (tagInfo.dataTypes == FIELD_TYPE_DESCRIPTION_ASCII) {
            fieldType = FIELD_TYPE_ASCII;
        } else if (tagInfo.dataTypes[0] == FIELD_TYPE_ASCII) {
            fieldType = FIELD_TYPE_ASCII;
        } else {
            throw new ImageWriteException("Tag has unexpected data type.");
        }
        byte[] writeData = fieldType.writeData(str, i);
        return new TiffOutputField(tagInfo.tag, tagInfo, fieldType, writeData.length, writeData);
    }

    public static TiffOutputField create(TagInfo tagInfo, int i, Number[] numberArr) throws ImageWriteException {
        if (tagInfo.dataTypes == null || tagInfo.dataTypes.length < 1) {
            throw new ImageWriteException("Tag has no default data type.");
        }
        FieldType fieldType = tagInfo.dataTypes[0];
        if (tagInfo.length != numberArr.length) {
            throw new ImageWriteException("Tag does not expect a single value.");
        }
        return new TiffOutputField(tagInfo.tag, tagInfo, fieldType, numberArr.length, fieldType.writeData(numberArr, i));
    }

    protected static final TiffOutputField createOffsetField(TagInfo tagInfo, int i) throws ImageWriteException {
        return new TiffOutputField(tagInfo, FIELD_TYPE_LONG, 1, FIELD_TYPE_LONG.writeData(new int[1], i));
    }

    protected TiffOutputItem getSeperateValue() {
        return this.separateValueItem;
    }

    public int getSortHint() {
        return this.sortHint;
    }

    protected boolean isLocalValue() {
        return this.bytes.length <= 4;
    }

    protected void setData(byte[] bArr) throws ImageWriteException {
        if (this.bytes.length != bArr.length) {
            throw new ImageWriteException("Cannot change size of value.");
        }
        this.bytes = bArr;
        if (this.separateValueItem != null) {
            this.separateValueItem.updateValue(bArr);
        }
    }

    public void setSortHint(int i) {
        this.sortHint = i;
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String str) {
        if (str == null) {
            str = HttpVersions.HTTP_0_9;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(this.tagInfo);
        stringBuffer.append(newline);
        stringBuffer.append(str);
        stringBuffer.append("count: " + this.count);
        stringBuffer.append(newline);
        stringBuffer.append(str);
        stringBuffer.append(this.fieldType);
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }

    protected void writeField(BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
        binaryOutputStream.write2Bytes(this.tag);
        binaryOutputStream.write2Bytes(this.fieldType.type);
        binaryOutputStream.write4Bytes(this.count);
        if (isLocalValue()) {
            if (this.separateValueItem != null) {
                throw new ImageWriteException("Unexpected separate value item.");
            } else if (this.bytes.length > 4) {
                throw new ImageWriteException("Local value has invalid length: " + this.bytes.length);
            } else {
                binaryOutputStream.writeByteArray(this.bytes);
                int length = this.bytes.length;
                for (int i = 0; i < 4 - length; i++) {
                    binaryOutputStream.write(0);
                }
            }
        } else if (this.separateValueItem == null) {
            throw new ImageWriteException("Missing separate value item.");
        } else {
            binaryOutputStream.write4Bytes(this.separateValueItem.getOffset());
        }
    }
}
