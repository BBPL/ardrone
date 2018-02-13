package org.apache.sanselan.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;

public class UnknownSegment extends GenericSegment {
    public UnknownSegment(int i, int i2, InputStream inputStream) throws ImageReadException, IOException {
        super(i, i2, inputStream);
    }

    public UnknownSegment(int i, byte[] bArr) throws ImageReadException, IOException {
        super(i, bArr);
    }

    public String getDescription() {
        return "Unknown (" + getSegmentType() + ")";
    }
}
