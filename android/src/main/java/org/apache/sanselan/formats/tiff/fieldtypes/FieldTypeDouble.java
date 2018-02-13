package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeDouble extends FieldType {
    public FieldTypeDouble() {
        super(12, 8, "Double");
    }

    public Object getSimpleValue(TiffField tiffField) {
        return "?";
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        if (obj instanceof Double) {
            return convertDoubleToByteArray(((Double) obj).doubleValue(), i);
        }
        if (obj instanceof double[]) {
            return convertDoubleArrayToByteArray((double[]) obj, i);
        }
        if (obj instanceof Double[]) {
            Double[] dArr = (Double[]) obj;
            double[] dArr2 = new double[dArr.length];
            for (int i2 = 0; i2 < dArr2.length; i2++) {
                dArr2[i2] = dArr[i2].doubleValue();
            }
            return convertDoubleArrayToByteArray(dArr2, i);
        }
        throw new ImageWriteException("Invalid data: " + obj + " (" + Debug.getType(obj) + ")");
    }
}
