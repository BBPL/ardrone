package org.apache.sanselan.formats.tiff.constants;

import org.apache.http.protocol.HTTP;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeASCII;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeByte;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeDouble;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeFloat;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeLong;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeRational;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeShort;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldTypeUnknown;

public interface TiffFieldTypeConstants extends SanselanConstants {
    public static final FieldType[] FIELD_TYPES = new FieldType[]{FIELD_TYPE_BYTE, FIELD_TYPE_ASCII, FIELD_TYPE_SHORT, FIELD_TYPE_LONG, FIELD_TYPE_RATIONAL, FIELD_TYPE_SBYTE, FIELD_TYPE_UNDEFINED, FIELD_TYPE_SSHORT, FIELD_TYPE_SLONG, FIELD_TYPE_SRATIONAL, FIELD_TYPE_FLOAT, FIELD_TYPE_DOUBLE};
    public static final FieldType[] FIELD_TYPE_ANY = FIELD_TYPES;
    public static final FieldTypeASCII FIELD_TYPE_ASCII = new FieldTypeASCII(2, HTTP.ASCII);
    public static final FieldTypeByte FIELD_TYPE_BYTE = new FieldTypeByte(1, "Byte");
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_ANY = FIELD_TYPE_ANY;
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_ASCII = new FieldType[]{FIELD_TYPE_ASCII};
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_BYTE = new FieldType[]{FIELD_TYPE_BYTE};
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_BYTE_OR_SHORT = new FieldType[]{FIELD_TYPE_SHORT, FIELD_TYPE_BYTE};
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_LONG = new FieldType[]{FIELD_TYPE_LONG};
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_LONG_OR_SHORT = new FieldType[]{FIELD_TYPE_SHORT, FIELD_TYPE_LONG};
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_RATIONAL = new FieldType[]{FIELD_TYPE_RATIONAL};
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_SHORT = new FieldType[]{FIELD_TYPE_SHORT};
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_SHORT_OR_LONG = new FieldType[]{FIELD_TYPE_SHORT, FIELD_TYPE_LONG};
    public static final FieldType[] FIELD_TYPE_DESCRIPTION_UNKNOWN = null;
    public static final FieldType FIELD_TYPE_DOUBLE = new FieldTypeDouble();
    public static final FieldType FIELD_TYPE_FLOAT = new FieldTypeFloat();
    public static final FieldTypeLong FIELD_TYPE_LONG = new FieldTypeLong(4, "Long");
    public static final FieldTypeRational FIELD_TYPE_RATIONAL = new FieldTypeRational(5, "Rational");
    public static final FieldType FIELD_TYPE_SBYTE = new FieldTypeByte(6, "SByte");
    public static final FieldTypeShort FIELD_TYPE_SHORT = new FieldTypeShort(3, "Short");
    public static final FieldType FIELD_TYPE_SLONG = new FieldTypeLong(9, "SLong");
    public static final FieldType FIELD_TYPE_SRATIONAL = new FieldTypeRational(10, "SRational");
    public static final FieldType FIELD_TYPE_SSHORT = new FieldTypeShort(8, "SShort");
    public static final FieldType FIELD_TYPE_UNDEFINED = new FieldTypeByte(7, "Undefined");
    public static final FieldType FIELD_TYPE_UNKNOWN = new FieldTypeUnknown();
}
