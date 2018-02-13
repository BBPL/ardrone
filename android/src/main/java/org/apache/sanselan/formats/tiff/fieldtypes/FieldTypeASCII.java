package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;

public class FieldTypeASCII extends FieldType {
    public FieldTypeASCII(int i, String str) {
        super(i, 1, str);
    }

    public Object getSimpleValue(TiffField tiffField) {
        byte[] rawBytes = getRawBytes(tiffField);
        return new String(rawBytes, 0, rawBytes.length - 1);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof byte[]) {
            obj = (byte[]) obj;
        } else if (obj instanceof String) {
            obj = ((String) obj).getBytes();
        } else {
            throw new ImageWriteException("Unknown data type: " + obj);
        }
        Object obj2 = new byte[(obj.length + 1)];
        System.arraycopy(obj, 0, obj2, 0, obj.length);
        return obj2;
    }
}
