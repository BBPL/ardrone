package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeLong extends FieldType {
    public FieldTypeLong(int i, String str) {
        super(i, 4, str);
    }

    public Object getSimpleValue(TiffField tiffField) {
        return tiffField.length == 1 ? new Integer(convertByteArrayToInt(this.name + " (" + tiffField.tagInfo.name + ")", tiffField.valueOffsetBytes, tiffField.byteOrder)) : convertByteArrayToIntArray(this.name + " (" + tiffField.tagInfo.name + ")", getRawBytes(tiffField), 0, tiffField.length, tiffField.byteOrder);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        int i2 = 0;
        if (obj instanceof Integer) {
            return convertIntArrayToByteArray(new int[]{((Integer) obj).intValue()}, i);
        } else if (obj instanceof int[]) {
            return convertIntArrayToByteArray((int[]) obj, i);
        } else {
            if (obj instanceof Integer[]) {
                Integer[] numArr = (Integer[]) obj;
                int[] iArr = new int[numArr.length];
                while (i2 < iArr.length) {
                    iArr[i2] = numArr[i2].intValue();
                    i2++;
                }
                return convertIntArrayToByteArray(iArr, i);
            }
            throw new ImageWriteException("Invalid data: " + obj + " (" + Debug.getType(obj) + ")");
        }
    }
}
