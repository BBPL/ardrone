package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeShort extends FieldType {
    public FieldTypeShort(int i, String str) {
        super(i, 2, str);
    }

    public Object getSimpleValue(TiffField tiffField) throws ImageReadException {
        return tiffField.length == 1 ? new Integer(convertByteArrayToShort(this.name + " (" + tiffField.tagInfo.name + ")", tiffField.valueOffsetBytes, tiffField.byteOrder)) : convertByteArrayToShortArray(this.name + " (" + tiffField.tagInfo.name + ")", getRawBytes(tiffField), 0, tiffField.length, tiffField.byteOrder);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        int i2 = 0;
        if (obj instanceof Integer) {
            return convertShortArrayToByteArray(new int[]{((Integer) obj).intValue()}, i);
        } else if (obj instanceof int[]) {
            return convertShortArrayToByteArray((int[]) obj, i);
        } else {
            if (obj instanceof Integer[]) {
                Integer[] numArr = (Integer[]) obj;
                int[] iArr = new int[numArr.length];
                while (i2 < iArr.length) {
                    iArr[i2] = numArr[i2].intValue();
                    i2++;
                }
                return convertShortArrayToByteArray(iArr, i);
            }
            throw new ImageWriteException("Invalid data: " + obj + " (" + Debug.getType(obj) + ")");
        }
    }
}
