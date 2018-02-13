package org.apache.sanselan.formats.tiff.write;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.BinaryConstants;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public abstract class TiffImageWriterBase implements TiffConstants, BinaryConstants {
    protected final int byteOrder;

    public TiffImageWriterBase() {
        this.byteOrder = 73;
    }

    public TiffImageWriterBase(int i) {
        this.byteOrder = i;
    }

    protected static final int imageDataPaddingLength(int i) {
        return (4 - (i % 4)) % 4;
    }

    protected TiffOutputSummary validateDirectories(TiffOutputSet tiffOutputSet) throws ImageWriteException {
        List directories = tiffOutputSet.getDirectories();
        if (1 > directories.size()) {
            throw new ImageWriteException("No directories.");
        }
        ArrayList arrayList = new ArrayList();
        Map hashMap = new HashMap();
        int i = 0;
        TiffOutputItem tiffOutputItem = null;
        TiffOutputItem tiffOutputItem2 = null;
        TiffOutputField tiffOutputField = null;
        TiffOutputField tiffOutputField2 = null;
        TiffOutputField tiffOutputField3 = null;
        TiffOutputItem tiffOutputItem3 = null;
        while (i < directories.size()) {
            TiffOutputItem tiffOutputItem4;
            TiffOutputItem tiffOutputItem5 = (TiffOutputDirectory) directories.get(i);
            int i2 = tiffOutputItem5.type;
            Integer num = new Integer(i2);
            hashMap.put(num, tiffOutputItem5);
            if (i2 < 0) {
                switch (i2) {
                    case -4:
                        if (tiffOutputItem3 == null) {
                            tiffOutputItem4 = tiffOutputItem;
                            tiffOutputItem = tiffOutputItem2;
                            tiffOutputItem2 = tiffOutputItem5;
                            break;
                        }
                        throw new ImageWriteException("More than one Interoperability directory.");
                    case -3:
                        if (tiffOutputItem2 == null) {
                            tiffOutputItem4 = tiffOutputItem;
                            tiffOutputItem2 = tiffOutputItem3;
                            tiffOutputItem = tiffOutputItem5;
                            break;
                        }
                        throw new ImageWriteException("More than one GPS directory.");
                    case -2:
                        if (tiffOutputItem == null) {
                            tiffOutputItem4 = tiffOutputItem5;
                            tiffOutputItem = tiffOutputItem2;
                            tiffOutputItem2 = tiffOutputItem3;
                            break;
                        }
                        throw new ImageWriteException("More than one EXIF directory.");
                    default:
                        throw new ImageWriteException("Unknown directory: " + i2);
                }
            } else if (arrayList.contains(num)) {
                throw new ImageWriteException("More than one directory with index: " + i2 + ".");
            } else {
                arrayList.add(new Integer(i2));
                tiffOutputItem4 = tiffOutputItem;
                tiffOutputItem = tiffOutputItem2;
                tiffOutputItem2 = tiffOutputItem3;
            }
            HashSet hashSet = new HashSet();
            ArrayList fields = tiffOutputItem5.getFields();
            int i3 = 0;
            while (i3 < fields.size()) {
                TiffOutputField tiffOutputField4 = (TiffOutputField) fields.get(i3);
                Integer num2 = new Integer(tiffOutputField4.tag);
                if (hashSet.contains(num2)) {
                    throw new ImageWriteException("Tag (" + tiffOutputField4.tagInfo.getDescription() + ") appears twice in directory.");
                }
                hashSet.add(num2);
                TiffOutputField tiffOutputField5;
                if (tiffOutputField4.tag == EXIF_TAG_EXIF_OFFSET.tag) {
                    if (tiffOutputField2 != null) {
                        throw new ImageWriteException("More than one Exif directory offset field.");
                    }
                    tiffOutputField2 = tiffOutputField3;
                    tiffOutputField5 = tiffOutputField;
                    tiffOutputField = tiffOutputField4;
                    tiffOutputField4 = tiffOutputField5;
                } else if (tiffOutputField4.tag == EXIF_TAG_INTEROP_OFFSET.tag) {
                    if (tiffOutputField != null) {
                        throw new ImageWriteException("More than one Interoperability directory offset field.");
                    }
                    tiffOutputField = tiffOutputField2;
                    tiffOutputField2 = tiffOutputField3;
                } else if (tiffOutputField4.tag != EXIF_TAG_GPSINFO.tag) {
                    tiffOutputField4 = tiffOutputField;
                    tiffOutputField = tiffOutputField2;
                    tiffOutputField2 = tiffOutputField3;
                } else if (tiffOutputField3 != null) {
                    throw new ImageWriteException("More than one GPS directory offset field.");
                } else {
                    tiffOutputField5 = tiffOutputField;
                    tiffOutputField = tiffOutputField2;
                    tiffOutputField2 = tiffOutputField4;
                    tiffOutputField4 = tiffOutputField5;
                }
                i3++;
                tiffOutputField3 = tiffOutputField2;
                tiffOutputField2 = tiffOutputField;
                tiffOutputField = tiffOutputField4;
            }
            tiffOutputItem3 = tiffOutputItem2;
            i++;
            tiffOutputItem2 = tiffOutputItem;
            tiffOutputItem = tiffOutputItem4;
        }
        if (arrayList.size() < 1) {
            throw new ImageWriteException("Missing root directory.");
        }
        TiffOutputDirectory tiffOutputDirectory;
        Collections.sort(arrayList);
        i = 0;
        TiffOutputDirectory tiffOutputDirectory2 = null;
        while (i < arrayList.size()) {
            Integer num3 = (Integer) arrayList.get(i);
            if (num3.intValue() != i) {
                throw new ImageWriteException("Missing directory: " + i + ".");
            }
            tiffOutputDirectory = (TiffOutputDirectory) hashMap.get(num3);
            if (tiffOutputDirectory2 != null) {
                tiffOutputDirectory2.setNextDirectory(tiffOutputDirectory);
            }
            i++;
            tiffOutputDirectory2 = tiffOutputDirectory;
        }
        tiffOutputDirectory = (TiffOutputDirectory) hashMap.get(new Integer(0));
        TiffOutputSummary tiffOutputSummary = new TiffOutputSummary(this.byteOrder, tiffOutputDirectory, hashMap);
        if (tiffOutputItem3 != null || tiffOutputField == null) {
            if (tiffOutputItem3 != null) {
                if (tiffOutputItem == null) {
                    tiffOutputItem = tiffOutputSet.addExifDirectory();
                }
                if (tiffOutputField == null) {
                    tiffOutputField = TiffOutputField.createOffsetField(EXIF_TAG_INTEROP_OFFSET, this.byteOrder);
                    tiffOutputItem.add(tiffOutputField);
                }
                tiffOutputSummary.add(tiffOutputItem3, tiffOutputField);
            }
            if (tiffOutputItem != null || tiffOutputField2 == null) {
                if (tiffOutputItem != null) {
                    if (tiffOutputField2 == null) {
                        tiffOutputField2 = TiffOutputField.createOffsetField(EXIF_TAG_EXIF_OFFSET, this.byteOrder);
                        tiffOutputDirectory.add(tiffOutputField2);
                    }
                    tiffOutputSummary.add(tiffOutputItem, tiffOutputField2);
                }
                if (tiffOutputItem2 != null || tiffOutputField3 == null) {
                    if (tiffOutputItem2 != null) {
                        if (tiffOutputField3 == null) {
                            tiffOutputField3 = TiffOutputField.createOffsetField(EXIF_TAG_GPSINFO, this.byteOrder);
                            tiffOutputDirectory.add(tiffOutputField3);
                        }
                        tiffOutputSummary.add(tiffOutputItem2, tiffOutputField3);
                    }
                    return tiffOutputSummary;
                }
                throw new ImageWriteException("Output set has GPS Directory Offset field, but no GPS Directory");
            }
            throw new ImageWriteException("Output set has Exif Directory Offset field, but no Exif Directory");
        }
        throw new ImageWriteException("Output set has Interoperability Directory Offset field, but no Interoperability Directory");
    }

    public abstract void write(OutputStream outputStream, TiffOutputSet tiffOutputSet) throws IOException, ImageWriteException;

    protected void writeImageFileHeader(BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
        writeImageFileHeader(binaryOutputStream, 8);
    }

    protected void writeImageFileHeader(BinaryOutputStream binaryOutputStream, int i) throws IOException, ImageWriteException {
        binaryOutputStream.write(this.byteOrder);
        binaryOutputStream.write(this.byteOrder);
        binaryOutputStream.write2Bytes(42);
        binaryOutputStream.write4Bytes(i);
    }
}
