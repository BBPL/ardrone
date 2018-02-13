package org.apache.sanselan.common.byteSources;

import com.parrot.ardronetool.ConfigArdroneMask;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteSourceInputStream extends ByteSource {
    private static final int BLOCK_SIZE = 1024;
    private CacheBlock cacheHead = null;
    private final InputStream is;
    private Long length = null;
    private byte[] readBuffer = null;

    private class CacheBlock {
        public final byte[] bytes;
        private CacheBlock next = null;
        private boolean triedNext = false;

        public CacheBlock(byte[] bArr) {
            this.bytes = bArr;
        }

        public CacheBlock getNext() throws IOException {
            if (this.next != null) {
                return this.next;
            }
            if (this.triedNext) {
                return null;
            }
            this.triedNext = true;
            this.next = ByteSourceInputStream.this.readBlock();
            return this.next;
        }
    }

    private class CacheReadingInputStream extends InputStream {
        private CacheBlock block;
        private int blockIndex;
        private boolean readFirst;

        private CacheReadingInputStream() {
            this.block = null;
            this.readFirst = false;
            this.blockIndex = 0;
        }

        public int read() throws IOException {
            if (this.block == null) {
                if (!this.readFirst) {
                    this.block = ByteSourceInputStream.this.getFirstBlock();
                    this.readFirst = true;
                }
                return -1;
            }
            if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                this.block = this.block.getNext();
                this.blockIndex = 0;
            }
            if (this.block != null && this.blockIndex < this.block.bytes.length) {
                byte[] bArr = this.block.bytes;
                int i = this.blockIndex;
                this.blockIndex = i + 1;
                return bArr[i] & 255;
            }
            return -1;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (bArr == null) {
                throw new NullPointerException();
            } else if (i < 0 || i > bArr.length || i2 < 0 || i + i2 > bArr.length || i + i2 < 0) {
                throw new IndexOutOfBoundsException();
            } else if (i2 == 0) {
                return 0;
            } else {
                if (this.block == null) {
                    if (this.readFirst) {
                        return -1;
                    }
                    this.block = ByteSourceInputStream.this.getFirstBlock();
                    this.readFirst = true;
                }
                if (this.block != null && this.blockIndex >= this.block.bytes.length) {
                    this.block = this.block.getNext();
                    this.blockIndex = 0;
                }
                if (this.block == null) {
                    return -1;
                }
                if (this.blockIndex >= this.block.bytes.length) {
                    return -1;
                }
                int min = Math.min(i2, this.block.bytes.length - this.blockIndex);
                System.arraycopy(this.block.bytes, this.blockIndex, bArr, i, min);
                this.blockIndex += min;
                return min;
            }
        }
    }

    public ByteSourceInputStream(InputStream inputStream, String str) {
        super(str);
        this.is = new BufferedInputStream(inputStream);
    }

    private CacheBlock getFirstBlock() throws IOException {
        if (this.cacheHead == null) {
            this.cacheHead = readBlock();
        }
        return this.cacheHead;
    }

    private CacheBlock readBlock() throws IOException {
        if (this.readBuffer == null) {
            this.readBuffer = new byte[1024];
        }
        int read = this.is.read(this.readBuffer);
        if (read < 1) {
            return null;
        }
        if (read < 1024) {
            Object obj = new byte[read];
            System.arraycopy(this.readBuffer, 0, obj, 0, read);
            return new CacheBlock(obj);
        }
        byte[] bArr = this.readBuffer;
        this.readBuffer = null;
        return new CacheBlock(bArr);
    }

    public byte[] getAll() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (CacheBlock firstBlock = getFirstBlock(); firstBlock != null; firstBlock = firstBlock.getNext()) {
            byteArrayOutputStream.write(firstBlock.bytes);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] getBlock(int i, int i2) throws IOException {
        InputStream inputStream = getInputStream();
        inputStream.skip((long) i);
        byte[] bArr = new byte[i2];
        int i3 = 0;
        do {
            int read = inputStream.read(bArr, i3, bArr.length - i3);
            if (read < 1) {
                throw new IOException("Could not read block.");
            }
            i3 += read;
        } while (i3 < i2);
        return bArr;
    }

    public String getDescription() {
        return "Inputstream: '" + this.filename + "'";
    }

    public InputStream getInputStream() throws IOException {
        return new CacheReadingInputStream();
    }

    public long getLength() throws IOException {
        if (this.length != null) {
            return this.length.longValue();
        }
        InputStream inputStream = getInputStream();
        long j = 0;
        while (true) {
            long skip = inputStream.skip(ConfigArdroneMask.ARDRONE_NAVDATA_DEMO_MASK);
            if (skip <= 0) {
                this.length = new Long(j);
                return j;
            }
            j += skip;
        }
    }
}
