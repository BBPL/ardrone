package org.apache.sanselan.formats.tiff;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.sanselan.FormatCompliance;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.formats.tiff.TiffDirectory.ImageDataElement;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.util.Debug;
import org.mortbay.jetty.HttpVersions;

public class TiffReader extends BinaryFileParser implements TiffConstants {
    private final boolean strict;

    public interface Listener {
        boolean addDirectory(TiffDirectory tiffDirectory);

        boolean addField(TiffField tiffField);

        boolean readImageData();

        boolean readOffsetDirectories();

        boolean setTiffHeader(TiffHeader tiffHeader);
    }

    private static class Collector implements Listener {
        private ArrayList directories;
        private ArrayList fields;
        private final boolean readThumbnails;
        private TiffHeader tiffHeader;

        public Collector() {
            this(null);
        }

        public Collector(Map map) {
            this.tiffHeader = null;
            this.directories = new ArrayList();
            this.fields = new ArrayList();
            boolean z = true;
            if (map != null && map.containsKey(SanselanConstants.PARAM_KEY_READ_THUMBNAILS)) {
                z = Boolean.TRUE.equals(map.get(SanselanConstants.PARAM_KEY_READ_THUMBNAILS));
            }
            this.readThumbnails = z;
        }

        public boolean addDirectory(TiffDirectory tiffDirectory) {
            this.directories.add(tiffDirectory);
            return true;
        }

        public boolean addField(TiffField tiffField) {
            this.fields.add(tiffField);
            return true;
        }

        public TiffContents getContents() {
            return new TiffContents(this.tiffHeader, this.directories);
        }

        public boolean readImageData() {
            return this.readThumbnails;
        }

        public boolean readOffsetDirectories() {
            return true;
        }

        public boolean setTiffHeader(TiffHeader tiffHeader) {
            this.tiffHeader = tiffHeader;
            return true;
        }
    }

    private static class DirectoryCollector extends Collector {
        private final boolean readImageData;

        public DirectoryCollector(boolean z) {
            this.readImageData = z;
        }

        public boolean addDirectory(TiffDirectory tiffDirectory) {
            super.addDirectory(tiffDirectory);
            return false;
        }

        public boolean readImageData() {
            return this.readImageData;
        }
    }

    private static class FirstDirectoryCollector extends Collector {
        private final boolean readImageData;

        public FirstDirectoryCollector(boolean z) {
            this.readImageData = z;
        }

        public boolean addDirectory(TiffDirectory tiffDirectory) {
            super.addDirectory(tiffDirectory);
            return false;
        }

        public boolean readImageData() {
            return this.readImageData;
        }
    }

    public TiffReader(boolean z) {
        this.strict = z;
    }

    private JpegImageData getJpegRawImageData(ByteSource byteSource, TiffDirectory tiffDirectory) throws ImageReadException, IOException {
        ImageDataElement jpegRawImageDataElement = tiffDirectory.getJpegRawImageDataElement();
        int i = jpegRawImageDataElement.offset;
        int i2 = jpegRawImageDataElement.length;
        if (((long) (i + i2)) == byteSource.getLength() + 1) {
            i2--;
        }
        return new JpegImageData(i, i2, byteSource.getBlock(i, i2));
    }

    private void readDirectories(ByteSource byteSource, FormatCompliance formatCompliance, Listener listener) throws ImageReadException, IOException {
        TiffHeader readTiffHeader = readTiffHeader(byteSource, formatCompliance);
        if (listener.setTiffHeader(readTiffHeader)) {
            readDirectory(byteSource, readTiffHeader.offsetToFirstIFD, 0, formatCompliance, listener, new ArrayList());
        }
    }

    private boolean readDirectory(ByteSource byteSource, int i, int i2, FormatCompliance formatCompliance, Listener listener, List list) throws ImageReadException, IOException {
        return readDirectory(byteSource, i, i2, formatCompliance, listener, false, list);
    }

    private boolean readDirectory(ByteSource byteSource, int i, int i2, FormatCompliance formatCompliance, Listener listener, boolean z, List list) throws ImageReadException, IOException {
        Throwable e;
        Integer num = new Integer(i);
        if (list.contains(num)) {
            return false;
        }
        list.add(num);
        InputStream inputStream = null;
        try {
            InputStream inputStream2 = byteSource.getInputStream();
            if (i > 0) {
                inputStream2.skip((long) i);
            }
            ArrayList arrayList = new ArrayList();
            if (((long) i) >= byteSource.getLength()) {
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Throwable e2) {
                        Debug.debug(e2);
                    }
                }
                return true;
            }
            try {
                int read2Bytes;
                int read2Bytes2 = read2Bytes("DirectoryEntryCount", inputStream2, "Not a Valid TIFF File");
                for (int i3 = 0; i3 < read2Bytes2; i3++) {
                    int read2Bytes3 = read2Bytes(TiffField.Attribute_Tag, inputStream2, "Not a Valid TIFF File");
                    read2Bytes = read2Bytes("Type", inputStream2, "Not a Valid TIFF File");
                    int read4Bytes = read4Bytes("Length", inputStream2, "Not a Valid TIFF File");
                    byte[] readByteArray = readByteArray("ValueOffset", 4, inputStream2, "Not a Valid TIFF File");
                    int convertByteArrayToInt = convertByteArrayToInt("ValueOffset", readByteArray);
                    if (read2Bytes3 != 0) {
                        TiffField tiffField = new TiffField(read2Bytes3, i2, read2Bytes, read4Bytes, convertByteArrayToInt, readByteArray, getByteOrder());
                        tiffField.setSortHint(i3);
                        tiffField.fillInValue(byteSource);
                        arrayList.add(tiffField);
                        if (!listener.addField(tiffField)) {
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                } catch (Throwable e22) {
                                    Debug.debug(e22);
                                }
                            }
                            return true;
                        }
                    }
                }
                TiffDirectory tiffDirectory = new TiffDirectory(i2, arrayList, i, read4Bytes("nextDirectoryOffset", inputStream2, "Not a Valid TIFF File"));
                if (listener.readImageData() && tiffDirectory.hasJpegImageData()) {
                    tiffDirectory.setJpegImageData(getJpegRawImageData(byteSource, tiffDirectory));
                }
                if (listener.addDirectory(tiffDirectory)) {
                    if (listener.readOffsetDirectories()) {
                        Collection arrayList2 = new ArrayList();
                        for (read2Bytes2 = 0; read2Bytes2 < arrayList.size(); read2Bytes2++) {
                            TiffField tiffField2 = (TiffField) arrayList.get(read2Bytes2);
                            if (tiffField2.tag == TiffConstants.EXIF_TAG_EXIF_OFFSET.tag || tiffField2.tag == TiffConstants.EXIF_TAG_GPSINFO.tag || tiffField2.tag == TiffConstants.EXIF_TAG_INTEROP_OFFSET.tag) {
                                int intValue = ((Number) tiffField2.getValue()).intValue();
                                if (tiffField2.tag == TiffConstants.EXIF_TAG_EXIF_OFFSET.tag) {
                                    read2Bytes = -2;
                                } else if (tiffField2.tag == TiffConstants.EXIF_TAG_GPSINFO.tag) {
                                    read2Bytes = -3;
                                } else if (tiffField2.tag == TiffConstants.EXIF_TAG_INTEROP_OFFSET.tag) {
                                    read2Bytes = -4;
                                } else {
                                    throw new ImageReadException("Unknown subdirectory type.");
                                }
                                if (!readDirectory(byteSource, intValue, read2Bytes, formatCompliance, listener, true, list)) {
                                    arrayList2.add(tiffField2);
                                }
                            }
                        }
                        arrayList.removeAll(arrayList2);
                    }
                    if (!z && tiffDirectory.nextDirectoryOffset > 0) {
                        readDirectory(byteSource, tiffDirectory.nextDirectoryOffset, i2 + 1, formatCompliance, listener, list);
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (Throwable e222) {
                            Debug.debug(e222);
                        }
                    }
                    return true;
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Throwable e2222) {
                        Debug.debug(e2222);
                    }
                }
                return true;
            } catch (IOException e3) {
                if (this.strict) {
                    throw e3;
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Throwable e22222) {
                        Debug.debug(e22222);
                    }
                }
                return true;
            } catch (Throwable th) {
                e22222 = th;
                inputStream = inputStream2;
            }
        } catch (Throwable th2) {
            e22222 = th2;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable e4) {
                    Debug.debug(e4);
                }
            }
            throw e22222;
        }
    }

    private TiffHeader readTiffHeader(InputStream inputStream, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        byte readByte = readByte("BYTE_ORDER_1", inputStream, "Not a Valid TIFF File");
        setByteOrder(readByte, readByte("BYTE_ORDER_2", inputStream, "Not a Valid TIFF File"));
        int read2Bytes = read2Bytes("tiffVersion", inputStream, "Not a Valid TIFF File");
        if (read2Bytes != 42) {
            throw new ImageReadException("Unknown Tiff Version: " + read2Bytes);
        }
        int read4Bytes = read4Bytes("offsetToFirstIFD", inputStream, "Not a Valid TIFF File");
        skipBytes(inputStream, read4Bytes - 8, "Not a Valid TIFF File: couldn't find IFDs");
        if (this.debug) {
            System.out.println(HttpVersions.HTTP_0_9);
        }
        return new TiffHeader(readByte, read2Bytes, read4Bytes);
    }

    private TiffHeader readTiffHeader(ByteSource byteSource, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        InputStream inputStream = null;
        try {
            inputStream = byteSource.getInputStream();
            TiffHeader readTiffHeader = readTiffHeader(inputStream, formatCompliance);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable e) {
                    Debug.debug(e);
                }
            }
            return readTiffHeader;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable e2) {
                    Debug.debug(e2);
                }
            }
        }
    }

    public void read(ByteSource byteSource, Map map, FormatCompliance formatCompliance, Listener listener) throws ImageReadException, IOException {
        readDirectories(byteSource, formatCompliance, listener);
    }

    public TiffContents readContents(ByteSource byteSource, Map map, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        Object collector = new Collector(map);
        read(byteSource, map, formatCompliance, collector);
        return collector.getContents();
    }

    public TiffContents readDirectories(ByteSource byteSource, boolean z, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        Listener firstDirectoryCollector = new FirstDirectoryCollector(z);
        readDirectories(byteSource, formatCompliance, firstDirectoryCollector);
        TiffContents contents = firstDirectoryCollector.getContents();
        if (contents.directories.size() >= 1) {
            return contents;
        }
        throw new ImageReadException("Image did not contain any directories.");
    }

    public TiffContents readFirstDirectory(ByteSource byteSource, Map map, boolean z, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        Object firstDirectoryCollector = new FirstDirectoryCollector(z);
        read(byteSource, map, formatCompliance, firstDirectoryCollector);
        TiffContents contents = firstDirectoryCollector.getContents();
        if (contents.directories.size() >= 1) {
            return contents;
        }
        throw new ImageReadException("Image did not contain any directories.");
    }
}
