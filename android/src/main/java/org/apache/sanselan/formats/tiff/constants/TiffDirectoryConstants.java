package org.apache.sanselan.formats.tiff.constants;

public interface TiffDirectoryConstants {
    public static final int DIRECTORY_TYPE_DIR_0 = 0;
    public static final int DIRECTORY_TYPE_DIR_1 = 1;
    public static final int DIRECTORY_TYPE_DIR_2 = 2;
    public static final int DIRECTORY_TYPE_DIR_3 = 3;
    public static final int DIRECTORY_TYPE_DIR_4 = 4;
    public static final int DIRECTORY_TYPE_EXIF = -2;
    public static final int DIRECTORY_TYPE_GPS = -3;
    public static final int DIRECTORY_TYPE_INTEROPERABILITY = -4;
    public static final int DIRECTORY_TYPE_MAKER_NOTES = -5;
    public static final int DIRECTORY_TYPE_ROOT = 0;
    public static final int DIRECTORY_TYPE_SUB = 1;
    public static final int DIRECTORY_TYPE_SUB0 = 1;
    public static final int DIRECTORY_TYPE_SUB1 = 2;
    public static final int DIRECTORY_TYPE_SUB2 = 3;
    public static final int DIRECTORY_TYPE_THUMBNAIL = 2;
    public static final int DIRECTORY_TYPE_UNKNOWN = -1;
    public static final ExifDirectoryType[] EXIF_DIRECTORIES = new ExifDirectoryType[]{TIFF_DIRECTORY_ROOT, EXIF_DIRECTORY_EXIF_IFD, TIFF_DIRECTORY_IFD0, EXIF_DIRECTORY_IFD0, TIFF_DIRECTORY_IFD1, EXIF_DIRECTORY_IFD1, TIFF_DIRECTORY_IFD2, EXIF_DIRECTORY_IFD2, EXIF_DIRECTORY_INTEROP_IFD, EXIF_DIRECTORY_MAKER_NOTES, EXIF_DIRECTORY_SUB_IFD, EXIF_DIRECTORY_SUB_IFD1, EXIF_DIRECTORY_SUB_IFD2, EXIF_DIRECTORY_GPS};
    public static final ExifDirectoryType EXIF_DIRECTORY_EXIF_IFD = new Special(-2, "Exif IFD");
    public static final ExifDirectoryType EXIF_DIRECTORY_GPS = new Special(-3, "GPS IFD");
    public static final ExifDirectoryType EXIF_DIRECTORY_IFD0 = TIFF_DIRECTORY_IFD0;
    public static final ExifDirectoryType EXIF_DIRECTORY_IFD1 = TIFF_DIRECTORY_IFD1;
    public static final ExifDirectoryType EXIF_DIRECTORY_IFD2 = TIFF_DIRECTORY_IFD2;
    public static final ExifDirectoryType EXIF_DIRECTORY_IFD3 = TIFF_DIRECTORY_IFD3;
    public static final ExifDirectoryType EXIF_DIRECTORY_INTEROP_IFD = new Special(-4, "Interop IFD");
    public static final ExifDirectoryType EXIF_DIRECTORY_MAKER_NOTES = new Special(-5, "Maker Notes");
    public static final ExifDirectoryType EXIF_DIRECTORY_SUB_IFD = TIFF_DIRECTORY_IFD1;
    public static final ExifDirectoryType EXIF_DIRECTORY_SUB_IFD1 = TIFF_DIRECTORY_IFD2;
    public static final ExifDirectoryType EXIF_DIRECTORY_SUB_IFD2 = TIFF_DIRECTORY_IFD3;
    public static final ExifDirectoryType EXIF_DIRECTORY_UNKNOWN = null;
    public static final ExifDirectoryType TIFF_DIRECTORY_IFD0 = new Image(0, "IFD0");
    public static final ExifDirectoryType TIFF_DIRECTORY_IFD1 = new Image(1, "IFD1");
    public static final ExifDirectoryType TIFF_DIRECTORY_IFD2 = new Image(2, "IFD2");
    public static final ExifDirectoryType TIFF_DIRECTORY_IFD3 = new Image(3, "IFD3");
    public static final ExifDirectoryType TIFF_DIRECTORY_ROOT = TIFF_DIRECTORY_IFD0;

    public static abstract class ExifDirectoryType {
        public final int directoryType;
        public final String name;

        public static class Image extends ExifDirectoryType {
            public Image(int i, String str) {
                super(i, str);
            }

            public boolean isImageDirectory() {
                return true;
            }
        }

        public static class Special extends ExifDirectoryType {
            public Special(int i, String str) {
                super(i, str);
            }

            public boolean isImageDirectory() {
                return false;
            }
        }

        public ExifDirectoryType(int i, String str) {
            this.directoryType = i;
            this.name = str;
        }

        public abstract boolean isImageDirectory();
    }
}
