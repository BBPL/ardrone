package org.apache.sanselan.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.jpeg.JpegConstants;
import org.mortbay.jetty.HttpVersions;

public class JFIFSegment extends Segment implements JpegConstants {
    public final int densityUnits;
    public final int jfifMajorVersion;
    public final int jfifMinorVersion;
    public final int thumbnailSize;
    public final int xDensity;
    public final int xThumbnail;
    public final int yDensity;
    public final int yThumbnail;

    public JFIFSegment(int i, int i2, InputStream inputStream) throws ImageReadException, IOException {
        super(i, i2);
        byte[] readBytes = readBytes(inputStream, JFIF0_SIGNATURE.length);
        if (compareByteArrays(readBytes, JFIF0_SIGNATURE) || compareByteArrays(readBytes, JFIF0_SIGNATURE_ALTERNATIVE)) {
            this.jfifMajorVersion = readByte("JFIF_major_version", inputStream, "Not a Valid JPEG File");
            this.jfifMinorVersion = readByte("JFIF_minor_version", inputStream, "Not a Valid JPEG File");
            this.densityUnits = readByte("density_units", inputStream, "Not a Valid JPEG File");
            this.xDensity = read2Bytes("x_density", inputStream, "Not a Valid JPEG File");
            this.yDensity = read2Bytes("y_density", inputStream, "Not a Valid JPEG File");
            this.xThumbnail = readByte("x_thumbnail", inputStream, "Not a Valid JPEG File");
            this.yThumbnail = readByte("y_thumbnail", inputStream, "Not a Valid JPEG File");
            this.thumbnailSize = this.xThumbnail * this.yThumbnail;
            if (this.thumbnailSize > 0) {
                skipBytes(inputStream, this.thumbnailSize, "Not a Valid JPEG File: missing thumbnail");
            }
            if (getDebug()) {
                System.out.println(HttpVersions.HTTP_0_9);
                return;
            }
            return;
        }
        throw new ImageReadException("Not a Valid JPEG File: missing JFIF string");
    }

    public JFIFSegment(int i, byte[] bArr) throws ImageReadException, IOException {
        this(i, bArr.length, new ByteArrayInputStream(bArr));
    }

    public String getDescription() {
        return "JFIF (" + getSegmentType() + ")";
    }
}
