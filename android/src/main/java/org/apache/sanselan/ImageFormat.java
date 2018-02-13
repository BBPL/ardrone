package org.apache.sanselan;

public class ImageFormat {
    public static final ImageFormat IMAGE_FORMAT_BMP = new ImageFormat("BMP");
    public static final ImageFormat IMAGE_FORMAT_GIF = new ImageFormat("GIF");
    public static final ImageFormat IMAGE_FORMAT_ICO = new ImageFormat("ICO");
    public static final ImageFormat IMAGE_FORMAT_JBIG2 = new ImageFormat("JBig2");
    public static final ImageFormat IMAGE_FORMAT_JPEG = new ImageFormat(ImageInfo.COMPRESSION_ALGORITHM_JPEG);
    public static final ImageFormat IMAGE_FORMAT_PBM = new ImageFormat("PBM");
    public static final ImageFormat IMAGE_FORMAT_PGM = new ImageFormat("PGM");
    public static final ImageFormat IMAGE_FORMAT_PNG = new ImageFormat("PNG");
    public static final ImageFormat IMAGE_FORMAT_PNM = new ImageFormat("PNM");
    public static final ImageFormat IMAGE_FORMAT_PPM = new ImageFormat("PPM");
    public static final ImageFormat IMAGE_FORMAT_PSD = new ImageFormat("PSD");
    public static final ImageFormat IMAGE_FORMAT_TGA = new ImageFormat("TGA");
    public static final ImageFormat IMAGE_FORMAT_TIFF = new ImageFormat("TIFF");
    public static final ImageFormat IMAGE_FORMAT_UNKNOWN = new ImageFormat("UNKNOWN", false);
    public final boolean actual;
    public final String extension;
    public final String name;

    private ImageFormat(String str) {
        this.name = str;
        this.extension = str;
        this.actual = true;
    }

    private ImageFormat(String str, boolean z) {
        this.name = str;
        this.extension = str;
        this.actual = z;
    }

    public static final ImageFormat[] getAllFormats() {
        return new ImageFormat[]{IMAGE_FORMAT_UNKNOWN, IMAGE_FORMAT_PNG, IMAGE_FORMAT_GIF, IMAGE_FORMAT_TIFF, IMAGE_FORMAT_JPEG, IMAGE_FORMAT_BMP, IMAGE_FORMAT_PSD, IMAGE_FORMAT_PBM, IMAGE_FORMAT_PGM, IMAGE_FORMAT_PPM, IMAGE_FORMAT_PNM, IMAGE_FORMAT_TGA, IMAGE_FORMAT_JBIG2};
    }

    public boolean equals(Object obj) {
        return !(obj instanceof ImageFormat) ? false : ((ImageFormat) obj).name.equals(this.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return "{" + this.name + ": " + this.extension + "}";
    }
}
