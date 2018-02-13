package org.apache.sanselan.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;

public class APPNSegment extends GenericSegment {
    public APPNSegment(int i, int i2, InputStream inputStream) throws ImageReadException, IOException {
        super(i, i2, inputStream);
    }

    public String getDescription() {
        return "APPN (APP" + (this.marker - 65504) + ") (" + getSegmentType() + ")";
    }
}
