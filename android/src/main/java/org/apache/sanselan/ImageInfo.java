package org.apache.sanselan;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import org.mortbay.jetty.HttpVersions;

public class ImageInfo {
    public static final int COLOR_TYPE_BW = 0;
    public static final int COLOR_TYPE_CMYK = 3;
    public static final int COLOR_TYPE_GRAYSCALE = 1;
    public static final int COLOR_TYPE_OTHER = -1;
    public static final int COLOR_TYPE_RGB = 2;
    public static final int COLOR_TYPE_UNKNOWN = -2;
    public static final String COMPRESSION_ALGORITHM_CCITT_1D = "CCITT 1D";
    public static final String COMPRESSION_ALGORITHM_CCITT_GROUP_3 = "CCITT Group 3 1-Dimensional Modified Huffman run-length encoding.";
    public static final String COMPRESSION_ALGORITHM_CCITT_GROUP_4 = "CCITT Group 4";
    public static final String COMPRESSION_ALGORITHM_JPEG = "JPEG";
    public static final String COMPRESSION_ALGORITHM_LZW = "LZW";
    public static final String COMPRESSION_ALGORITHM_NONE = "None";
    public static final String COMPRESSION_ALGORITHM_PACKBITS = "PackBits";
    public static final String COMPRESSION_ALGORITHM_PNG_FILTER = "PNG Filter";
    public static final String COMPRESSION_ALGORITHM_PSD = "Photoshop";
    public static final String COMPRESSION_ALGORITHM_RLE = "RLE: Run-Length Encoding";
    public static final String COMPRESSION_ALGORITHM_UNKNOWN = "Unknown";
    private final int bitsPerPixel;
    private final int colorType;
    private final ArrayList comments;
    private final String compressionAlgorithm;
    private final ImageFormat format;
    private final String formatDetails;
    private final String formatName;
    private final int height;
    private final boolean isProgressive;
    private final boolean isTransparent;
    private final String mimeType;
    private final int numberOfImages;
    private final int physicalHeightDpi;
    private final float physicalHeightInch;
    private final int physicalWidthDpi;
    private final float physicalWidthInch;
    private final boolean usesPalette;
    private final int width;

    public ImageInfo(String str, int i, ArrayList arrayList, ImageFormat imageFormat, String str2, int i2, String str3, int i3, int i4, float f, int i5, float f2, int i6, boolean z, boolean z2, boolean z3, int i7, String str4) {
        this.formatDetails = str;
        this.bitsPerPixel = i;
        this.comments = arrayList;
        this.format = imageFormat;
        this.formatName = str2;
        this.height = i2;
        this.mimeType = str3;
        this.numberOfImages = i3;
        this.physicalHeightDpi = i4;
        this.physicalHeightInch = f;
        this.physicalWidthDpi = i5;
        this.physicalWidthInch = f2;
        this.width = i6;
        this.isProgressive = z;
        this.isTransparent = z2;
        this.usesPalette = z3;
        this.colorType = i7;
        this.compressionAlgorithm = str4;
    }

    public void dump() {
        System.out.print(toString());
    }

    public int getBitsPerPixel() {
        return this.bitsPerPixel;
    }

    public int getColorType() {
        return this.colorType;
    }

    public String getColorTypeDescription() {
        switch (this.colorType) {
            case -2:
                return "Unknown";
            case -1:
                return "Other";
            case 0:
                return "Black and White";
            case 1:
                return "Grayscale";
            case 2:
                return "RGB";
            case 3:
                return "CMYK";
            default:
                return "Unknown";
        }
    }

    public ArrayList getComments() {
        return new ArrayList(this.comments);
    }

    public ImageFormat getFormat() {
        return this.format;
    }

    public String getFormatName() {
        return this.formatName;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean getIsProgressive() {
        return this.isProgressive;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public int getNumberOfImages() {
        return this.numberOfImages;
    }

    public int getPhysicalHeightDpi() {
        return this.physicalHeightDpi;
    }

    public float getPhysicalHeightInch() {
        return this.physicalHeightInch;
    }

    public int getPhysicalWidthDpi() {
        return this.physicalWidthDpi;
    }

    public float getPhysicalWidthInch() {
        return this.physicalWidthInch;
    }

    public int getWidth() {
        return this.width;
    }

    public String toString() {
        try {
            Writer stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            toString(printWriter, HttpVersions.HTTP_0_9);
            printWriter.flush();
            return stringWriter.toString();
        } catch (Exception e) {
            return "Image Data: Error";
        }
    }

    public void toString(PrintWriter printWriter, String str) throws ImageReadException, IOException {
        printWriter.println("Format Details: " + this.formatDetails);
        printWriter.println("Bits Per Pixel: " + this.bitsPerPixel);
        printWriter.println("Comments: " + this.comments.size());
        for (int i = 0; i < this.comments.size(); i++) {
            printWriter.println("\t" + i + ": '" + ((String) this.comments.get(i)) + "'");
        }
        printWriter.println("Format: " + this.format.name);
        printWriter.println("Format Name: " + this.formatName);
        printWriter.println("Compression Algorithm: " + this.compressionAlgorithm);
        printWriter.println("Height: " + this.height);
        printWriter.println("MimeType: " + this.mimeType);
        printWriter.println("Number Of Images: " + this.numberOfImages);
        printWriter.println("Physical Height Dpi: " + this.physicalHeightDpi);
        printWriter.println("Physical Height Inch: " + this.physicalHeightInch);
        printWriter.println("Physical Width Dpi: " + this.physicalWidthDpi);
        printWriter.println("Physical Width Inch: " + this.physicalWidthInch);
        printWriter.println("Width: " + this.width);
        printWriter.println("Is Progressive: " + this.isProgressive);
        printWriter.println("Is Transparent: " + this.isTransparent);
        printWriter.println("Color Type: " + getColorTypeDescription());
        printWriter.println("Uses Palette: " + this.usesPalette);
        printWriter.flush();
    }
}
