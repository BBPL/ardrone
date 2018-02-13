package org.apache.sanselan.formats.tiff.write;

import java.io.IOException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.formats.tiff.constants.AllTagConstants;

abstract class TiffOutputItem implements AllTagConstants {
    public static final int UNDEFINED_VALUE = -1;
    private int offset = -1;

    public static class Value extends TiffOutputItem {
        private final byte[] bytes;
        private final String name;

        public Value(String str, byte[] bArr) {
            this.name = str;
            this.bytes = bArr;
        }

        public String getItemDescription() {
            return this.name;
        }

        public int getItemLength() {
            return this.bytes.length;
        }

        public void updateValue(byte[] bArr) throws ImageWriteException {
            if (this.bytes.length != bArr.length) {
                throw new ImageWriteException("Updated data size mismatch: " + this.bytes.length + " vs. " + bArr.length);
            }
            System.arraycopy(bArr, 0, this.bytes, 0, bArr.length);
        }

        public void writeItem(BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
            binaryOutputStream.write(this.bytes);
        }
    }

    TiffOutputItem() {
    }

    public abstract String getItemDescription();

    public abstract int getItemLength();

    protected int getOffset() {
        return this.offset;
    }

    protected void setOffset(int i) {
        this.offset = i;
    }

    public abstract void writeItem(BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException;
}
