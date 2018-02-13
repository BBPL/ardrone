package org.apache.sanselan.formats.tiff.constants;

import org.apache.sanselan.SanselanConstants;

public interface AllTagConstants extends SanselanConstants, TiffTagConstants, ExifTagConstants, GPSTagConstants {
    public static final TagInfo[] ALL_TAGS = TagConstantsUtils.mergeTagLists(new TagInfo[][]{ALL_TIFF_TAGS, ALL_EXIF_TAGS, ALL_GPS_TAGS});
}
