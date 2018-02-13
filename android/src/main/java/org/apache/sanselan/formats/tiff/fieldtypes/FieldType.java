package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryFileFunctions;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public abstract class FieldType extends BinaryFileFunctions implements TiffConstants {
    public final int length;
    public final String name;
    public final int type;

    public FieldType(int i, int i2, String str) {
        this.type = i;
        this.length = i2;
        this.name = str;
    }

    public static final byte[] getStubLocalValue() {
        return new byte[4];
    }

    public int getBytesLength(TiffField tiffField) throws ImageReadException {
        if (this.length >= 1) {
            return this.length * tiffField.length;
        }
        throw new ImageReadException("Unknown field type");
    }

    public String getDisplayValue(TiffField tiffField) throws ImageReadException {
        Object simpleValue = getSimpleValue(tiffField);
        return simpleValue == null ? "NULL" : simpleValue.toString();
    }

    public final byte[] getRawBytes(TiffField tiffField) {
        if (!isLocalValue(tiffField)) {
            return tiffField.oversizeValue;
        }
        int i = tiffField.length * this.length;
        Object obj = new byte[i];
        System.arraycopy(tiffField.valueOffsetBytes, 0, obj, 0, i);
        return obj;
    }

    public abstract Object getSimpleValue(TiffField tiffField) throws ImageReadException;

    public final byte[] getStubValue(int i) {
        return new byte[(this.length * i)];
    }

    public boolean isLocalValue(TiffField tiffField) {
        return this.length > 0 && this.length * tiffField.length <= 4;
    }

    public String toString() {
        return "[" + getClass().getName() + ". type: " + this.type + ", name: " + this.name + ", length: " + this.length + "]";
    }

    public abstract byte[] writeData(Object obj, int i) throws ImageWriteException;
}
