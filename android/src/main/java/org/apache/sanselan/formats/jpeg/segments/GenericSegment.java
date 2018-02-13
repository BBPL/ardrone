package org.apache.sanselan.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import org.apache.sanselan.ImageReadException;

public abstract class GenericSegment extends Segment {
    public final byte[] bytes;

    public GenericSegment(int i, int i2, InputStream inputStream) throws ImageReadException, IOException {
        super(i, i2);
        this.bytes = readByteArray("Segment Data", i2, inputStream, "Invalid Segment: insufficient data");
    }

    public GenericSegment(int i, byte[] bArr) throws ImageReadException, IOException {
        super(i, bArr.length);
        this.bytes = bArr;
    }

    public void dump(PrintWriter printWriter) {
        dump(printWriter, 0);
    }

    public void dump(PrintWriter printWriter, int i) {
        int i2 = 0;
        while (i2 < 50 && i2 + i < this.bytes.length) {
            debugNumber(printWriter, "\t" + (i2 + i), (int) this.bytes[i2 + i]);
            i2++;
        }
    }
}
