package org.apache.sanselan.formats.tiff.fieldtypes;

import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.common.RationalNumberUtilities;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.util.Debug;

public class FieldTypeRational extends FieldType {
    public FieldTypeRational(int i, String str) {
        super(i, 8, str);
    }

    public Object getSimpleValue(TiffField tiffField) {
        return tiffField.length == 1 ? convertByteArrayToRational(this.name + " (" + tiffField.tagInfo.name + ")", tiffField.oversizeValue, tiffField.byteOrder) : convertByteArrayToRationalArray(this.name + " (" + tiffField.tagInfo.name + ")", getRawBytes(tiffField), 0, tiffField.length, tiffField.byteOrder);
    }

    public byte[] writeData(int i, int i2, int i3) throws ImageWriteException {
        return writeData(new int[]{i}, new int[]{i2}, i3);
    }

    public byte[] writeData(Object obj, int i) throws ImageWriteException {
        int i2 = 0;
        if (obj instanceof RationalNumber) {
            return convertRationalToByteArray((RationalNumber) obj, i);
        }
        if (obj instanceof RationalNumber[]) {
            return convertRationalArrayToByteArray((RationalNumber[]) obj, i);
        }
        if (obj instanceof Number) {
            return convertRationalToByteArray(RationalNumberUtilities.getRationalNumber(((Number) obj).doubleValue()), i);
        }
        RationalNumber[] rationalNumberArr;
        if (obj instanceof Number[]) {
            Number[] numberArr = (Number[]) obj;
            rationalNumberArr = new RationalNumber[numberArr.length];
            while (i2 < numberArr.length) {
                rationalNumberArr[i2] = RationalNumberUtilities.getRationalNumber(numberArr[i2].doubleValue());
                i2++;
            }
            return convertRationalArrayToByteArray(rationalNumberArr, i);
        } else if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            rationalNumberArr = new RationalNumber[dArr.length];
            while (i2 < dArr.length) {
                rationalNumberArr[i2] = RationalNumberUtilities.getRationalNumber(dArr[i2]);
                i2++;
            }
            return convertRationalArrayToByteArray(rationalNumberArr, i);
        } else {
            throw new ImageWriteException("Invalid data: " + obj + " (" + Debug.getType(obj) + ")");
        }
    }

    public byte[] writeData(int[] iArr, int[] iArr2, int i) throws ImageWriteException {
        return convertIntArrayToRationalArray(iArr, iArr2, i);
    }
}
