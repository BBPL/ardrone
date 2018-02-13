package org.apache.sanselan.formats.tiff;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;

public class TiffField implements TiffConstants {
    private static final Map ALL_TAG_MAP = makeTagMap(ALL_TAGS, true, "All");
    public static final String Attribute_Tag = "Tag";
    private static final Map EXIF_TAG_MAP = makeTagMap(ALL_EXIF_TAGS, true, "EXIF");
    private static final Map GPS_TAG_MAP = makeTagMap(ALL_GPS_TAGS, false, "GPS");
    private static final Map TIFF_TAG_MAP = makeTagMap(ALL_TIFF_TAGS, false, "TIFF");
    public final int byteOrder;
    public final int directoryType;
    public final FieldType fieldType;
    public final int length;
    public byte[] oversizeValue = null;
    private int sortHint = -1;
    public final int tag;
    public final TagInfo tagInfo;
    public final int type;
    public final int valueOffset;
    public final byte[] valueOffsetBytes;

    public final class OversizeValueElement extends TiffElement {
        public OversizeValueElement(int i, int i2) {
            super(i, i2);
        }

        public String getElementDescription(boolean z) {
            return z ? null : "OversizeValueElement, tag: " + TiffField.this.tagInfo.name + ", fieldType: " + TiffField.this.fieldType.name;
        }
    }

    public TiffField(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6) {
        this.tag = i;
        this.directoryType = i2;
        this.type = i3;
        this.length = i4;
        this.valueOffset = i5;
        this.valueOffsetBytes = bArr;
        this.byteOrder = i6;
        this.fieldType = getFieldType(i3);
        this.tagInfo = getTag(i2, i);
    }

    private static FieldType getFieldType(int i) {
        for (FieldType fieldType : FIELD_TYPES) {
            if (fieldType.type == i) {
                return fieldType;
            }
        }
        return FIELD_TYPE_UNKNOWN;
    }

    private static TagInfo getTag(int i, int i2) {
        List list = (List) EXIF_TAG_MAP.get(new Integer(i2));
        return list == null ? TIFF_TAG_UNKNOWN : getTag(i, i2, list);
    }

    private static TagInfo getTag(int i, int i2, List list) {
        int i3 = 0;
        if (list.size() < 1) {
            return null;
        }
        int i4;
        for (i4 = 0; i4 < list.size(); i4++) {
            TagInfo tagInfo = (TagInfo) list.get(i4);
            if (tagInfo.directoryType != EXIF_DIRECTORY_UNKNOWN) {
                if (i == -2 && tagInfo.directoryType == EXIF_DIRECTORY_EXIF_IFD) {
                    return tagInfo;
                }
                if (i == -4 && tagInfo.directoryType == EXIF_DIRECTORY_INTEROP_IFD) {
                    return tagInfo;
                }
                if (i == -3 && tagInfo.directoryType == EXIF_DIRECTORY_GPS) {
                    return tagInfo;
                }
                if (i == -5 && tagInfo.directoryType == EXIF_DIRECTORY_MAKER_NOTES) {
                    return tagInfo;
                }
                if (i == 0 && tagInfo.directoryType == TIFF_DIRECTORY_IFD0) {
                    return tagInfo;
                }
                if (i == 1 && tagInfo.directoryType == TIFF_DIRECTORY_IFD1) {
                    return tagInfo;
                }
                if (i == 2 && tagInfo.directoryType == TIFF_DIRECTORY_IFD2) {
                    return tagInfo;
                }
                if (i == 3 && tagInfo.directoryType == TIFF_DIRECTORY_IFD3) {
                    return tagInfo;
                }
            }
        }
        for (i4 = 0; i4 < list.size(); i4++) {
            tagInfo = (TagInfo) list.get(i4);
            if (tagInfo.directoryType != EXIF_DIRECTORY_UNKNOWN) {
                if (i >= 0 && tagInfo.directoryType.isImageDirectory()) {
                    return tagInfo;
                }
                if (i < 0 && !tagInfo.directoryType.isImageDirectory()) {
                    return tagInfo;
                }
            }
        }
        while (i3 < list.size()) {
            tagInfo = (TagInfo) list.get(i3);
            if (tagInfo.directoryType == EXIF_DIRECTORY_UNKNOWN) {
                return tagInfo;
            }
            i3++;
        }
        return TIFF_TAG_UNKNOWN;
    }

    private String getValueDescription(Object obj) {
        int i = 0;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return obj.toString();
        }
        if (obj instanceof String) {
            return "'" + obj.toString().trim() + "'";
        }
        if (obj instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format((Date) obj);
        }
        StringBuffer stringBuffer;
        if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            stringBuffer = new StringBuffer();
            while (i < objArr.length) {
                Object obj2 = objArr[i];
                if (i > 50) {
                    stringBuffer.append("... (" + objArr.length + ")");
                    break;
                }
                if (i > 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(obj2);
                i++;
            }
            return stringBuffer.toString();
        } else if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            stringBuffer = new StringBuffer();
            while (i < iArr.length) {
                int i2 = iArr[i];
                if (i > 50) {
                    stringBuffer.append("... (" + iArr.length + ")");
                    break;
                }
                if (i > 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(i2);
                i++;
            }
            return stringBuffer.toString();
        } else if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            stringBuffer = new StringBuffer();
            while (i < jArr.length) {
                long j = jArr[i];
                if (i > 50) {
                    stringBuffer.append("... (" + jArr.length + ")");
                    break;
                }
                if (i > 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(j);
                i++;
            }
            return stringBuffer.toString();
        } else if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            stringBuffer = new StringBuffer();
            while (i < dArr.length) {
                double d = dArr[i];
                if (i > 50) {
                    stringBuffer.append("... (" + dArr.length + ")");
                    break;
                }
                if (i > 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(d);
                i++;
            }
            return stringBuffer.toString();
        } else if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            stringBuffer = new StringBuffer();
            while (i < bArr.length) {
                byte b = bArr[i];
                if (i > 50) {
                    stringBuffer.append("... (" + bArr.length + ")");
                    break;
                }
                if (i > 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(b);
                i++;
            }
            return stringBuffer.toString();
        } else if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            stringBuffer = new StringBuffer();
            while (i < cArr.length) {
                char c = cArr[i];
                if (i > 50) {
                    stringBuffer.append("... (" + cArr.length + ")");
                    break;
                }
                if (i > 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(c);
                i++;
            }
            return stringBuffer.toString();
        } else if (!(obj instanceof float[])) {
            return "Unknown: " + obj.getClass().getName();
        } else {
            float[] fArr = (float[]) obj;
            stringBuffer = new StringBuffer();
            while (i < fArr.length) {
                float f = fArr[i];
                if (i > 50) {
                    stringBuffer.append("... (" + fArr.length + ")");
                    break;
                }
                if (i > 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(f);
                i++;
            }
            return stringBuffer.toString();
        }
    }

    private int getValueLengthInBytes() {
        return this.fieldType.length * this.length;
    }

    private static final Map makeTagMap(TagInfo[] tagInfoArr, boolean z, String str) {
        Map hashtable = new Hashtable();
        for (TagInfo tagInfo : tagInfoArr) {
            Integer num = new Integer(tagInfo.tag);
            List list = (List) hashtable.get(num);
            if (list == null) {
                list = new ArrayList();
                hashtable.put(num, list);
            }
            list.add(tagInfo);
        }
        return hashtable;
    }

    public void dump() {
        PrintWriter printWriter = new PrintWriter(System.out);
        dump(printWriter);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter) {
        dump(printWriter, null);
    }

    public void dump(PrintWriter printWriter, String str) {
        if (str != null) {
            printWriter.print(new StringBuilder(String.valueOf(str)).append(": ").toString());
        }
        printWriter.println(toString());
        printWriter.flush();
    }

    public void fillInValue(ByteSource byteSource) throws ImageReadException, IOException {
        if (!this.fieldType.isLocalValue(this)) {
            setOversizeValue(byteSource.getBlock(this.valueOffset, getValueLengthInBytes()));
        }
    }

    public byte[] getByteArrayValue() throws ImageReadException {
        return this.fieldType.getRawBytes(this);
    }

    public int getBytesLength() throws ImageReadException {
        return this.fieldType.getBytesLength(this);
    }

    public String getDescriptionWithoutValue() {
        return this.tag + " (0x" + Integer.toHexString(this.tag) + ": " + this.tagInfo.name + "): ";
    }

    public double[] getDoubleArrayValue() throws ImageReadException {
        double[] dArr;
        int i = 0;
        Object value = getValue();
        if (value instanceof Number) {
            dArr = new double[]{((Number) value).doubleValue()};
        } else if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            dArr = new double[numberArr.length];
            while (i < numberArr.length) {
                dArr[i] = numberArr[i].doubleValue();
                i++;
            }
        } else if (value instanceof int[]) {
            int[] iArr = (int[]) value;
            dArr = new double[iArr.length];
            while (i < iArr.length) {
                dArr[i] = (double) iArr[i];
                i++;
            }
        } else if (value instanceof float[]) {
            float[] fArr = (float[]) value;
            dArr = new double[fArr.length];
            while (i < fArr.length) {
                dArr[i] = (double) fArr[i];
                i++;
            }
        } else if (value instanceof double[]) {
            double[] dArr2 = (double[]) value;
            dArr = new double[dArr2.length];
            while (i < dArr2.length) {
                dArr[i] = dArr2[i];
                i++;
            }
        } else {
            throw new ImageReadException("Unknown value: " + value + " for: " + this.tagInfo.getDescription());
        }
        return dArr;
    }

    public double getDoubleValue() throws ImageReadException {
        Object value = getValue();
        if (value != null) {
            return ((Number) value).doubleValue();
        }
        throw new ImageReadException("Missing value: " + this.tagInfo.getDescription());
    }

    public String getFieldTypeName() {
        return this.fieldType.name;
    }

    public int[] getIntArrayValue() throws ImageReadException {
        int[] iArr;
        int i = 0;
        Object value = getValue();
        if (value instanceof Number) {
            iArr = new int[]{((Number) value).intValue()};
        } else if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            iArr = new int[numberArr.length];
            while (i < numberArr.length) {
                iArr[i] = numberArr[i].intValue();
                i++;
            }
        } else if (value instanceof int[]) {
            int[] iArr2 = (int[]) value;
            iArr = new int[iArr2.length];
            while (i < iArr2.length) {
                iArr[i] = iArr2[i];
                i++;
            }
        } else {
            throw new ImageReadException("Unknown value: " + value + " for: " + this.tagInfo.getDescription());
        }
        return iArr;
    }

    public int getIntValue() throws ImageReadException {
        Object value = getValue();
        if (value != null) {
            return ((Number) value).intValue();
        }
        throw new ImageReadException("Missing value: " + this.tagInfo.getDescription());
    }

    public int getIntValueOrArraySum() throws ImageReadException {
        int i = 0;
        Object value = getValue();
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        int i2;
        int intValue;
        if (value instanceof Number[]) {
            Number[] numberArr = (Number[]) value;
            i2 = 0;
            while (i2 < numberArr.length) {
                intValue = numberArr[i2].intValue() + i;
                i2++;
                i = intValue;
            }
        } else if (value instanceof int[]) {
            int[] iArr = (int[]) value;
            i2 = 0;
            while (i2 < iArr.length) {
                intValue = iArr[i2] + i;
                i2++;
                i = intValue;
            }
        } else {
            throw new ImageReadException("Unknown value: " + value + " for: " + this.tagInfo.getDescription());
        }
        return i;
    }

    public TiffElement getOversizeValueElement() {
        return this.fieldType.isLocalValue(this) ? null : new OversizeValueElement(this.valueOffset, this.oversizeValue.length);
    }

    public int getSortHint() {
        return this.sortHint;
    }

    public String getStringValue() throws ImageReadException {
        Object value = getValue();
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        throw new ImageReadException("Expected String value(" + this.tagInfo.getDescription() + "): " + value);
    }

    public String getTagName() {
        return this.tagInfo == TIFF_TAG_UNKNOWN ? new StringBuilder(String.valueOf(this.tagInfo.name)).append(" (0x").append(Integer.toHexString(this.tag)).append(")").toString() : this.tagInfo.name;
    }

    public Object getValue() throws ImageReadException {
        return this.tagInfo.getValue(this);
    }

    public String getValueDescription() {
        try {
            return getValueDescription(getValue());
        } catch (ImageReadException e) {
            return "Invalid value: " + e.getMessage();
        }
    }

    public boolean isLocalValue() {
        return this.fieldType.isLocalValue(this);
    }

    public void setOversizeValue(byte[] bArr) {
        this.oversizeValue = bArr;
    }

    public void setSortHint(int i) {
        this.sortHint = i;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.tag + " (0x" + Integer.toHexString(this.tag) + ": " + this.tagInfo.name + "): ");
        stringBuffer.append(getValueDescription() + " (" + this.length + " " + this.fieldType.name + ")");
        return stringBuffer.toString();
    }
}
