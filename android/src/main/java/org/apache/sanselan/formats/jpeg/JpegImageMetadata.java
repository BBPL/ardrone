package org.apache.sanselan.formats.jpeg;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata.Item;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.util.Debug;
import org.mortbay.jetty.HttpVersions;

public class JpegImageMetadata implements IImageMetadata {
    private static final String newline = System.getProperty("line.separator");
    private final TiffImageMetadata exif;

    public JpegImageMetadata(Object obj, TiffImageMetadata tiffImageMetadata) {
        this.exif = tiffImageMetadata;
    }

    public void dump() {
        Debug.debug(toString());
    }

    public TiffField findEXIFValue(TagInfo tagInfo) {
        ArrayList items = getItems();
        for (int i = 0; i < items.size(); i++) {
            Object obj = items.get(i);
            if (obj instanceof Item) {
                TiffField tiffField = ((Item) obj).getTiffField();
                if (tiffField.tag == tagInfo.tag) {
                    return tiffField;
                }
            }
        }
        return null;
    }

    public Object getEXIFThumbnail() throws ImageReadException, IOException {
        return null;
    }

    public TiffImageMetadata getExif() {
        return this.exif;
    }

    public ArrayList getItems() {
        ArrayList arrayList = new ArrayList();
        if (this.exif != null) {
            arrayList.addAll(this.exif.getItems());
        }
        return arrayList;
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String str) {
        if (str == null) {
            str = HttpVersions.HTTP_0_9;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        if (this.exif == null) {
            stringBuffer.append("No Exif metadata.");
        } else {
            stringBuffer.append("Exif metadata:");
            stringBuffer.append(newline);
            stringBuffer.append(this.exif.toString("\t"));
        }
        stringBuffer.append(newline);
        stringBuffer.append(str);
        stringBuffer.append("No Photoshop (IPTC) metadata.");
        return stringBuffer.toString();
    }
}
