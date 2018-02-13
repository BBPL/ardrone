package org.apache.sanselan.formats.tiff.constants;

import org.apache.sanselan.formats.tiff.constants.TiffDirectoryConstants.ExifDirectoryType;

public class TagConstantsUtils implements TiffDirectoryConstants {
    public static ExifDirectoryType getExifDirectoryType(int i) {
        for (int i2 = 0; i2 < EXIF_DIRECTORIES.length; i2++) {
            if (EXIF_DIRECTORIES[i2].directoryType == i) {
                return EXIF_DIRECTORIES[i2];
            }
        }
        return EXIF_DIRECTORY_UNKNOWN;
    }

    public static TagInfo[] mergeTagLists(TagInfo[][] tagInfoArr) {
        int i;
        int i2 = 0;
        for (TagInfo[] length : tagInfoArr) {
            i2 += length.length;
        }
        Object obj = new TagInfo[i2];
        i2 = 0;
        for (i = 0; i < tagInfoArr.length; i++) {
            System.arraycopy(tagInfoArr[i], 0, obj, i2, tagInfoArr[i].length);
            i2 += tagInfoArr[i].length;
        }
        return obj;
    }
}
