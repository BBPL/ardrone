package org.apache.sanselan.formats.tiff.write;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.sanselan.FormatCompliance;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryFileFunctions;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.formats.tiff.JpegImageData;
import org.apache.sanselan.formats.tiff.TiffContents;
import org.apache.sanselan.formats.tiff.TiffDirectory;
import org.apache.sanselan.formats.tiff.TiffElement;
import org.apache.sanselan.formats.tiff.TiffElement.Stub;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffReader;
import org.apache.sanselan.util.Debug;

public class TiffImageWriterLossless extends TiffImageWriterBase {
    private static final Comparator ELEMENT_SIZE_COMPARATOR = new C13171();
    private static final Comparator ITEM_SIZE_COMPARATOR = new C13182();
    private final byte[] exifBytes;

    class C13171 implements Comparator {
        C13171() {
        }

        public int compare(Object obj, Object obj2) {
            return ((TiffElement) obj).length - ((TiffElement) obj2).length;
        }
    }

    class C13182 implements Comparator {
        C13182() {
        }

        public int compare(Object obj, Object obj2) {
            return ((TiffOutputItem) obj).getItemLength() - ((TiffOutputItem) obj2).getItemLength();
        }
    }

    private static class BufferOutputStream extends OutputStream {
        private final byte[] buffer;
        private int index;

        public BufferOutputStream(byte[] bArr, int i) {
            this.buffer = bArr;
            this.index = i;
        }

        public void write(int i) throws IOException {
            if (this.index >= this.buffer.length) {
                throw new IOException("Buffer overflow.");
            }
            byte[] bArr = this.buffer;
            int i2 = this.index;
            this.index = i2 + 1;
            bArr[i2] = (byte) i;
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            if (this.index + i2 > this.buffer.length) {
                throw new IOException("Buffer overflow.");
            }
            System.arraycopy(bArr, i, this.buffer, this.index, i2);
            this.index += i2;
        }
    }

    public TiffImageWriterLossless(int i, byte[] bArr) {
        super(i);
        this.exifBytes = bArr;
    }

    public TiffImageWriterLossless(byte[] bArr) {
        this.exifBytes = bArr;
    }

    private List analyzeOldTiff() throws ImageWriteException, IOException {
        try {
            int i;
            TiffElement oversizeValueElement;
            TiffContents readContents = new TiffReader(false).readContents(new ByteSourceArray(this.exifBytes), null, FormatCompliance.getDefault());
            ArrayList arrayList = new ArrayList();
            List list = readContents.directories;
            for (int i2 = 0; i2 < list.size(); i2++) {
                TiffDirectory tiffDirectory = (TiffDirectory) list.get(i2);
                arrayList.add(tiffDirectory);
                List directoryEntrys = tiffDirectory.getDirectoryEntrys();
                for (i = 0; i < directoryEntrys.size(); i++) {
                    oversizeValueElement = ((TiffField) directoryEntrys.get(i)).getOversizeValueElement();
                    if (oversizeValueElement != null) {
                        arrayList.add(oversizeValueElement);
                    }
                }
                JpegImageData jpegImageData = tiffDirectory.getJpegImageData();
                if (jpegImageData != null) {
                    arrayList.add(jpegImageData);
                }
            }
            Collections.sort(arrayList, TiffElement.COMPARATOR);
            List arrayList2 = new ArrayList();
            TiffElement tiffElement = null;
            i = 0;
            int i3 = -1;
            while (i < arrayList.size()) {
                oversizeValueElement = (TiffElement) arrayList.get(i);
                int i4 = oversizeValueElement.offset + oversizeValueElement.length;
                if (tiffElement != null) {
                    if (oversizeValueElement.offset - i3 > 3) {
                        arrayList2.add(new Stub(tiffElement.offset, i3 - tiffElement.offset));
                    } else {
                        oversizeValueElement = tiffElement;
                    }
                }
                i3 = i4;
                i++;
                tiffElement = oversizeValueElement;
            }
            if (tiffElement == null) {
                return arrayList2;
            }
            arrayList2.add(new Stub(tiffElement.offset, i3 - tiffElement.offset));
            return arrayList2;
        } catch (Exception e) {
            throw new ImageWriteException(e.getMessage(), e);
        }
    }

    private void dumpElements(List list) throws IOException {
        dumpElements(new ByteSourceArray(this.exifBytes), list);
    }

    private void dumpElements(ByteSource byteSource, List list) throws IOException {
        int i = 0;
        int i2 = 8;
        while (i < list.size()) {
            TiffElement tiffElement = (TiffElement) list.get(i);
            if (tiffElement.offset > i2) {
                int i3 = tiffElement.offset - i2;
                Debug.debug("gap of " + i3 + " bytes.");
                byte[] block = byteSource.getBlock(i2, i3);
                if (block.length > 64) {
                    Debug.debug("\thead", BinaryFileFunctions.head(block, 32));
                    Debug.debug("\ttail", BinaryFileFunctions.tail(block, 32));
                } else {
                    Debug.debug("\tbytes", block);
                }
            }
            Debug.debug("element[" + i + "]:" + tiffElement.getElementDescription() + " (" + tiffElement.offset + " + " + tiffElement.length + " = " + (tiffElement.offset + tiffElement.length) + ")");
            if (tiffElement instanceof TiffDirectory) {
                Debug.debug("\tnext Directory Offset: " + ((TiffDirectory) tiffElement).nextDirectoryOffset);
            }
            i++;
            i2 = tiffElement.length + tiffElement.offset;
        }
        Debug.debug();
    }

    private int updateOffsetsStep(List list, List list2) throws IOException, ImageWriteException {
        int length = this.exifBytes.length;
        List arrayList = new ArrayList(list);
        Collections.sort(arrayList, TiffElement.COMPARATOR);
        Collections.reverse(arrayList);
        int i = length;
        while (arrayList.size() > 0) {
            TiffElement tiffElement = (TiffElement) arrayList.get(0);
            if (tiffElement.offset + tiffElement.length != i) {
                break;
            }
            length = i - tiffElement.length;
            arrayList.remove(0);
            i = length;
        }
        Collections.sort(arrayList, ELEMENT_SIZE_COMPARATOR);
        Collections.reverse(arrayList);
        List arrayList2 = new ArrayList(list2);
        Collections.sort(arrayList2, ITEM_SIZE_COMPARATOR);
        Collections.reverse(arrayList2);
        int i2 = i;
        while (arrayList2.size() > 0) {
            TiffOutputItem tiffOutputItem = (TiffOutputItem) arrayList2.remove(0);
            int itemLength = tiffOutputItem.getItemLength();
            int i3 = 0;
            TiffElement tiffElement2 = null;
            while (i3 < arrayList.size()) {
                TiffElement tiffElement3 = (TiffElement) arrayList.get(i3);
                if (tiffElement3.length < itemLength) {
                    break;
                }
                i3++;
                tiffElement2 = tiffElement3;
            }
            if (tiffElement2 == null) {
                tiffOutputItem.setOffset(i2);
                i2 += itemLength;
            } else {
                tiffOutputItem.setOffset(tiffElement2.offset);
                arrayList.remove(tiffElement2);
                if (tiffElement2.length > itemLength) {
                    arrayList.add(new Stub(tiffElement2.offset + itemLength, tiffElement2.length - itemLength));
                    Collections.sort(arrayList, ELEMENT_SIZE_COMPARATOR);
                    Collections.reverse(arrayList);
                }
            }
        }
        return i2;
    }

    private void writeStep(OutputStream outputStream, TiffOutputSet tiffOutputSet, List list, List list2, int i) throws IOException, ImageWriteException {
        int i2 = 0;
        TiffOutputDirectory rootDirectory = tiffOutputSet.getRootDirectory();
        Object obj = new byte[i];
        System.arraycopy(this.exifBytes, 0, obj, 0, Math.min(this.exifBytes.length, obj.length));
        writeImageFileHeader(new BinaryOutputStream(new BufferOutputStream(obj, 0), this.byteOrder), rootDirectory.getOffset());
        for (int i3 = 0; i3 < list.size(); i3++) {
            TiffElement tiffElement = (TiffElement) list.get(i3);
            for (int i4 = 0; i4 < tiffElement.length; i4++) {
                int i5 = tiffElement.offset + i4;
                if (i5 < obj.length) {
                    obj[i5] = null;
                }
            }
        }
        while (i2 < list2.size()) {
            TiffOutputItem tiffOutputItem = (TiffOutputItem) list2.get(i2);
            tiffOutputItem.writeItem(new BinaryOutputStream(new BufferOutputStream(obj, tiffOutputItem.getOffset()), this.byteOrder));
            i2++;
        }
        outputStream.write(obj);
    }

    public void write(OutputStream outputStream, TiffOutputSet tiffOutputSet) throws IOException, ImageWriteException {
        List analyzeOldTiff = analyzeOldTiff();
        int length = this.exifBytes.length;
        if (analyzeOldTiff.size() < 1) {
            throw new ImageWriteException("Couldn't analyze old tiff data.");
        }
        if (analyzeOldTiff.size() == 1) {
            TiffElement tiffElement = (TiffElement) analyzeOldTiff.get(0);
            if (tiffElement.offset == 8) {
                if ((tiffElement.length + tiffElement.offset) + 8 == length) {
                    new TiffImageWriterLossy(this.byteOrder).write(outputStream, tiffOutputSet);
                    return;
                }
            }
        }
        TiffOutputSummary validateDirectories = validateDirectories(tiffOutputSet);
        List outputItems = tiffOutputSet.getOutputItems(validateDirectories);
        int updateOffsetsStep = updateOffsetsStep(analyzeOldTiff, outputItems);
        validateDirectories.updateOffsets(this.byteOrder);
        writeStep(outputStream, tiffOutputSet, analyzeOldTiff, outputItems, updateOffsetsStep);
    }
}
