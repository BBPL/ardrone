package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeUnknown extends FieldType {
    public FieldTypeUnknown() {
        super(-1, 1, "Unknown");
    }

    public Object getSimpleValue(TiffField tiffField) {
        return tiffField.length == 1 ? new Byte(tiffField.valueOffsetBytes[0]) : getRawBytes(tiffField);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof Byte) {
            return new byte[]{((Byte) obj).byteValue()};
        } else if (obj instanceof byte[]) {
            return (byte[]) obj;
        } else {
            throw new ImageWriteException("Invalid data: " + obj + " (" + Debug.getType(obj) + ")");
        }
    }
}
