package org.apache.sanselan;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.tiff.TiffImageParser;

public abstract class ImageParser extends BinaryFileParser implements SanselanConstants {
    public static final ImageParser[] getAllImageParsers() {
        return new ImageParser[]{new JpegImageParser(), new TiffImageParser()};
    }

    public static final boolean isStrict(Map map) {
        return (map == null || !map.containsKey(SanselanConstants.PARAM_KEY_STRICT)) ? false : ((Boolean) map.get(SanselanConstants.PARAM_KEY_STRICT)).booleanValue();
    }

    protected final boolean canAcceptExtension(File file) {
        return canAcceptExtension(file.getName());
    }

    protected final boolean canAcceptExtension(String str) {
        String[] acceptedExtensions = getAcceptedExtensions();
        if (acceptedExtensions != null) {
            int lastIndexOf = str.lastIndexOf(46);
            if (lastIndexOf < 0) {
                return false;
            }
            String toLowerCase = str.substring(lastIndexOf).toLowerCase();
            lastIndexOf = 0;
            while (lastIndexOf < acceptedExtensions.length) {
                if (!acceptedExtensions[lastIndexOf].toLowerCase().equals(toLowerCase)) {
                    lastIndexOf++;
                }
            }
            return false;
        }
        return true;
    }

    public boolean canAcceptType(ImageFormat imageFormat) {
        ImageFormat[] acceptedTypes = getAcceptedTypes();
        for (ImageFormat equals : acceptedTypes) {
            if (equals.equals(imageFormat)) {
                return true;
            }
        }
        return false;
    }

    public final String dumpImageFile(File file) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        if (this.debug) {
            System.out.println(getName() + ": " + file.getName());
        }
        return dumpImageFile(new ByteSourceFile(file));
    }

    public final String dumpImageFile(ByteSource byteSource) throws ImageReadException, IOException {
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        dumpImageFile(printWriter, byteSource);
        printWriter.flush();
        return stringWriter.toString();
    }

    public final String dumpImageFile(byte[] bArr) throws ImageReadException, IOException {
        return dumpImageFile(new ByteSourceArray(bArr));
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        return false;
    }

    public abstract boolean embedICCProfile(File file, File file2, byte[] bArr);

    protected abstract String[] getAcceptedExtensions();

    protected abstract ImageFormat[] getAcceptedTypes();

    public abstract String getDefaultExtension();

    public final FormatCompliance getFormatCompliance(File file) throws ImageReadException, IOException {
        return !canAcceptExtension(file) ? null : getFormatCompliance(new ByteSourceFile(file));
    }

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        return null;
    }

    public final FormatCompliance getFormatCompliance(byte[] bArr) throws ImageReadException, IOException {
        return getFormatCompliance(new ByteSourceArray(bArr));
    }

    public final byte[] getICCProfileBytes(File file) throws ImageReadException, IOException {
        return getICCProfileBytes(file, null);
    }

    public final byte[] getICCProfileBytes(File file, Map map) throws ImageReadException, IOException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        if (this.debug) {
            System.out.println(getName() + ": " + file.getName());
        }
        return getICCProfileBytes(new ByteSourceFile(file), map);
    }

    public abstract byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public final byte[] getICCProfileBytes(byte[] bArr) throws ImageReadException, IOException {
        return getICCProfileBytes(bArr, null);
    }

    public final byte[] getICCProfileBytes(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getICCProfileBytes(new ByteSourceArray(bArr), map);
    }

    public final ImageInfo getImageInfo(File file, Map map) throws ImageReadException, IOException {
        return !canAcceptExtension(file) ? null : getImageInfo(new ByteSourceFile(file), map);
    }

    public final ImageInfo getImageInfo(ByteSource byteSource) throws ImageReadException, IOException {
        return getImageInfo(byteSource, null);
    }

    public abstract ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public final ImageInfo getImageInfo(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceArray(bArr), map);
    }

    public final IImageMetadata getMetadata(File file) throws ImageReadException, IOException {
        return getMetadata(file, null);
    }

    public final IImageMetadata getMetadata(File file, Map map) throws ImageReadException, IOException {
        if (this.debug) {
            System.out.println(getName() + ".getMetadata" + ": " + file.getName());
        }
        return !canAcceptExtension(file) ? null : getMetadata(new ByteSourceFile(file), map);
    }

    public final IImageMetadata getMetadata(ByteSource byteSource) throws ImageReadException, IOException {
        return getMetadata(byteSource, null);
    }

    public abstract IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException;

    public final IImageMetadata getMetadata(byte[] bArr) throws ImageReadException, IOException {
        return getMetadata(bArr);
    }

    public final IImageMetadata getMetadata(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceArray(bArr), map);
    }

    public abstract String getName();

    public abstract String getXmpXml(ByteSource byteSource, Map map) throws ImageReadException, IOException;
}
