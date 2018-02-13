package org.apache.sanselan.formats.tiff.constants;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryFileFunctions;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TiffDirectoryConstants.ExifDirectoryType;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;
import org.apache.sanselan.util.Debug;

public class TagInfo implements TiffDirectoryConstants, TiffFieldTypeConstants {
    protected static final int LENGTH_UNKNOWN = -1;
    public final FieldType[] dataTypes;
    public final ExifDirectoryType directoryType;
    public final int length;
    public final String name;
    public final int tag;

    public static class Date extends TagInfo {
        private static final DateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        private static final DateFormat DATE_FORMAT_2 = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");

        public Date(String str, int i, FieldType fieldType, int i2) {
            super(str, i, fieldType, i2);
        }

        public byte[] encodeValue(FieldType fieldType, Object obj, int i) throws ImageWriteException {
            throw new ImageWriteException("date encode value: " + obj + " (" + Debug.getType(obj) + ")");
        }

        public Object getValue(TiffField tiffField) throws ImageReadException {
            Object simpleValue = tiffField.fieldType.getSimpleValue(tiffField);
            String str = (String) simpleValue;
            try {
                simpleValue = DATE_FORMAT_1.parse(str);
            } catch (Exception e) {
                try {
                    simpleValue = DATE_FORMAT_2.parse(str);
                } catch (Throwable e2) {
                    Debug.debug(e2);
                }
            }
            return simpleValue;
        }

        public boolean isDate() {
            return true;
        }

        public String toString() {
            return "[TagInfo. tag: " + this.tag + ", name: " + this.name + " (data)" + "]";
        }
    }

    public static class Offset extends TagInfo {
        public Offset(String str, int i, FieldType fieldType, int i2) {
            super(str, i, fieldType, i2);
        }

        public Offset(String str, int i, FieldType fieldType, int i2, ExifDirectoryType exifDirectoryType) {
            super(str, i, fieldType, i2, exifDirectoryType);
        }

        public Offset(String str, int i, FieldType[] fieldTypeArr, int i2, ExifDirectoryType exifDirectoryType) {
            super(str, i, fieldTypeArr, i2, exifDirectoryType);
        }

        public boolean isOffset() {
            return true;
        }
    }

    public static final class Text extends TagInfo {
        private static final TextEncoding[] TEXT_ENCODINGS = new TextEncoding[]{TEXT_ENCODING_ASCII, TEXT_ENCODING_JIS, TEXT_ENCODING_UNICODE, TEXT_ENCODING_UNDEFINED};
        private static final TextEncoding TEXT_ENCODING_ASCII;
        private static final TextEncoding TEXT_ENCODING_JIS;
        private static final TextEncoding TEXT_ENCODING_UNDEFINED = new TextEncoding(new byte[8], "ISO-8859-1");
        private static final TextEncoding TEXT_ENCODING_UNICODE;

        private static final class TextEncoding {
            public final String encodingName;
            public final byte[] prefix;

            public TextEncoding(byte[] bArr, String str) {
                this.prefix = bArr;
                this.encodingName = str;
            }
        }

        static {
            r0 = new byte[8];
            TEXT_ENCODING_ASCII = new TextEncoding(r0, "US-ASCII");
            r0 = new byte[8];
            TEXT_ENCODING_JIS = new TextEncoding(r0, "JIS");
            r0 = new byte[8];
            r0[0] = (byte) 85;
            r0[1] = (byte) 78;
            r0[2] = (byte) 73;
            r0[3] = (byte) 67;
            r0[4] = (byte) 79;
            r0[5] = (byte) 68;
            r0[6] = (byte) 69;
            TEXT_ENCODING_UNICODE = new TextEncoding(r0, "UTF-8");
        }

        public Text(String str, int i, FieldType fieldType, int i2, ExifDirectoryType exifDirectoryType) {
            super(str, i, fieldType, i2, exifDirectoryType);
        }

        public Text(String str, int i, FieldType[] fieldTypeArr, int i2, ExifDirectoryType exifDirectoryType) {
            super(str, i, fieldTypeArr, i2, exifDirectoryType);
        }

        public byte[] encodeValue(FieldType fieldType, Object obj, int i) throws ImageWriteException {
            if (obj instanceof String) {
                String str = (String) obj;
                try {
                    Object bytes = str.getBytes(TEXT_ENCODING_ASCII.encodingName);
                    Object obj2;
                    if (new String(bytes, TEXT_ENCODING_ASCII.encodingName).equals(str)) {
                        obj2 = new byte[(bytes.length + TEXT_ENCODING_ASCII.prefix.length)];
                        System.arraycopy(TEXT_ENCODING_ASCII.prefix, 0, obj2, 0, TEXT_ENCODING_ASCII.prefix.length);
                        System.arraycopy(bytes, 0, obj2, TEXT_ENCODING_ASCII.prefix.length, bytes.length);
                        return obj2;
                    }
                    bytes = str.getBytes(TEXT_ENCODING_UNICODE.encodingName);
                    obj2 = new byte[(bytes.length + TEXT_ENCODING_UNICODE.prefix.length)];
                    System.arraycopy(TEXT_ENCODING_UNICODE.prefix, 0, obj2, 0, TEXT_ENCODING_UNICODE.prefix.length);
                    System.arraycopy(bytes, 0, obj2, TEXT_ENCODING_UNICODE.prefix.length, bytes.length);
                    return obj2;
                } catch (Exception e) {
                    throw new ImageWriteException(e.getMessage(), e);
                }
            }
            throw new ImageWriteException("Text value not String: " + obj + " (" + Debug.getType(obj) + ")");
        }

        public Object getValue(TiffField tiffField) throws ImageReadException {
            if (tiffField.type == FIELD_TYPE_ASCII.type) {
                return FIELD_TYPE_ASCII.getSimpleValue(tiffField);
            }
            if (tiffField.type == FIELD_TYPE_UNDEFINED.type || tiffField.type == FIELD_TYPE_BYTE.type) {
                byte[] rawBytes = tiffField.fieldType.getRawBytes(tiffField);
                if (rawBytes.length < 8) {
                    try {
                        return new String(rawBytes, "US-ASCII");
                    } catch (UnsupportedEncodingException e) {
                        throw new ImageReadException("Text field missing encoding prefix.");
                    }
                }
                for (TextEncoding textEncoding : TEXT_ENCODINGS) {
                    if (BinaryFileFunctions.compareBytes(rawBytes, 0, textEncoding.prefix, 0, textEncoding.prefix.length)) {
                        try {
                            return new String(rawBytes, textEncoding.prefix.length, rawBytes.length - textEncoding.prefix.length, textEncoding.encodingName);
                        } catch (Exception e2) {
                            throw new ImageReadException(e2.getMessage(), e2);
                        }
                    }
                }
                try {
                    return new String(rawBytes, "US-ASCII");
                } catch (UnsupportedEncodingException e3) {
                    throw new ImageReadException("Unknown text encoding prefix.");
                }
            }
            Debug.debug("entry.type", tiffField.type);
            Debug.debug("entry.directoryType", tiffField.directoryType);
            Debug.debug("entry.type", tiffField.getDescriptionWithoutValue());
            Debug.debug("entry.type", tiffField.fieldType);
            throw new ImageReadException("Text field not encoded as bytes.");
        }

        public boolean isText() {
            return true;
        }
    }

    public static final class Unknown extends TagInfo {
        public Unknown(String str, int i, FieldType[] fieldTypeArr, int i2, ExifDirectoryType exifDirectoryType) {
            super(str, i, fieldTypeArr, i2, exifDirectoryType);
        }

        public byte[] encodeValue(FieldType fieldType, Object obj, int i) throws ImageWriteException {
            return super.encodeValue(fieldType, obj, i);
        }

        public Object getValue(TiffField tiffField) throws ImageReadException {
            return super.getValue(tiffField);
        }

        public boolean isUnknown() {
            return true;
        }
    }

    public TagInfo(String str, int i, FieldType fieldType) {
        this(str, i, fieldType, -1, EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String str, int i, FieldType fieldType, int i2) {
        FieldType[] fieldTypeArr = new FieldType[]{fieldType};
        this(str, i, fieldTypeArr, i2, EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String str, int i, FieldType fieldType, int i2, ExifDirectoryType exifDirectoryType) {
        this(str, i, new FieldType[]{fieldType}, i2, exifDirectoryType);
    }

    public TagInfo(String str, int i, FieldType fieldType, String str2) {
        FieldType[] fieldTypeArr = new FieldType[]{fieldType};
        this(str, i, fieldTypeArr, -1, EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String str, int i, FieldType[] fieldTypeArr, int i2, String str2) {
        this(str, i, fieldTypeArr, i2, EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String str, int i, FieldType[] fieldTypeArr, int i2, ExifDirectoryType exifDirectoryType) {
        this.name = str;
        this.tag = i;
        this.dataTypes = fieldTypeArr;
        this.length = i2;
        this.directoryType = exifDirectoryType;
    }

    public TagInfo(String str, int i, FieldType[] fieldTypeArr, String str2) {
        this(str, i, fieldTypeArr, -1, EXIF_DIRECTORY_UNKNOWN);
    }

    public byte[] encodeValue(FieldType fieldType, Object obj, int i) throws ImageWriteException {
        return fieldType.writeData(obj, i);
    }

    public String getDescription() {
        return this.tag + " (0x" + Integer.toHexString(this.tag) + ": " + this.name + "): ";
    }

    public Object getValue(TiffField tiffField) throws ImageReadException {
        return tiffField.fieldType.getSimpleValue(tiffField);
    }

    public boolean isDate() {
        return false;
    }

    public boolean isOffset() {
        return false;
    }

    public boolean isText() {
        return false;
    }

    public boolean isUnknown() {
        return false;
    }

    public String toString() {
        return "[TagInfo. tag: " + this.tag + " (0x" + Integer.toHexString(this.tag) + ", name: " + this.name + "]";
    }
}
