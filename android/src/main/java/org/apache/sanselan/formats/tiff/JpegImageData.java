package org.apache.sanselan.formats.tiff;

import org.apache.sanselan.formats.tiff.TiffElement.DataElement;

public class JpegImageData extends DataElement {
    public JpegImageData(int i, int i2, byte[] bArr) {
        super(i, i2, bArr);
    }

    public String getElementDescription(boolean z) {
        return "Jpeg image data: " + this.data.length + " bytes";
    }
}
