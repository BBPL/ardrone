package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeFloat extends FieldType {
    public FieldTypeFloat() {
        super(11, 4, "Float");
    }

    public Object getSimpleValue(TiffField tiffField) {
        return tiffField.length == 1 ? new Float(convertByteArrayToFloat(this.name + " (" + tiffField.tagInfo.name + ")", tiffField.valueOffsetBytes, tiffField.byteOrder)) : convertByteArrayToFloatArray(this.name + " (" + tiffField.tagInfo.name + ")", getRawBytes(tiffField), 0, tiffField.length, tiffField.byteOrder);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof Float) {
            return convertFloatToByteArray(((Float) obj).floatValue(), i);
        }
        if (obj instanceof float[]) {
            return convertFloatArrayToByteArray((float[]) obj, i);
        }
        if (obj instanceof Float[]) {
            Float[] fArr = (Float[]) obj;
            float[] fArr2 = new float[fArr.length];
            for (int i2 = 0; i2 < fArr2.length; i2++) {
                fArr2[i2] = fArr[i2].floatValue();
            }
            return convertFloatArrayToByteArray(fArr2, i);
        }
        throw new ImageWriteException("Invalid data: " + obj + " (" + Debug.getType(obj) + ")");
    }
}
