package org.apache.sanselan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.common.byteSources.ByteSourceInputStream;
import org.apache.sanselan.util.Debug;

public abstract class Sanselan implements SanselanConstants {
    private static final ImageParser getImageParser(ByteSource byteSource) throws ImageReadException, IOException {
        int i = 0;
        ImageFormat guessFormat = guessFormat(byteSource);
        if (!guessFormat.equals(ImageFormat.IMAGE_FORMAT_UNKNOWN)) {
            ImageParser[] allImageParsers = ImageParser.getAllImageParsers();
            for (ImageParser imageParser : allImageParsers) {
                if (imageParser.canAcceptType(guessFormat)) {
                    return imageParser;
                }
            }
        }
        String filename = byteSource.getFilename();
        if (filename != null) {
            ImageParser[] allImageParsers2 = ImageParser.getAllImageParsers();
            while (i < allImageParsers2.length) {
                ImageParser imageParser2 = allImageParsers2[i];
                if (imageParser2.canAcceptExtension(filename)) {
                    return imageParser2;
                }
                i++;
            }
        }
        throw new ImageReadException("Can't parse this format.");
    }

    public static IImageMetadata getMetadata(File file) throws ImageReadException, IOException {
        return getMetadata(file, null);
    }

    public static IImageMetadata getMetadata(File file, Map map) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceFile(file), map);
    }

    public static IImageMetadata getMetadata(InputStream inputStream, String str) throws ImageReadException, IOException {
        return getMetadata(inputStream, str, null);
    }

    public static IImageMetadata getMetadata(InputStream inputStream, String str, Map map) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceInputStream(inputStream, str), map);
    }

    private static IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getMetadata(byteSource, map);
    }

    public static IImageMetadata getMetadata(byte[] bArr) throws ImageReadException, IOException {
        return getMetadata(bArr, null);
    }

    public static IImageMetadata getMetadata(byte[] bArr, Map map) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceArray(bArr), map);
    }

    public static ImageFormat guessFormat(ByteSource byteSource) throws ImageReadException, IOException {
        InputStream inputStream = null;
        try {
            inputStream = byteSource.getInputStream();
            int read = inputStream.read();
            int read2 = inputStream.read();
            if (read < 0 || read2 < 0) {
                throw new ImageReadException("Couldn't read magic numbers to guess format.");
            }
            ImageFormat imageFormat;
            read &= 255;
            read2 &= 255;
            if (read == 71 && read2 == 73) {
                imageFormat = ImageFormat.IMAGE_FORMAT_GIF;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e) {
                        Debug.debug(e);
                    }
                }
            } else if (read == 137 && read2 == 80) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PNG;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e2) {
                        Debug.debug(e2);
                    }
                }
            } else if (read == 255 && read2 == 216) {
                imageFormat = ImageFormat.IMAGE_FORMAT_JPEG;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e22) {
                        Debug.debug(e22);
                    }
                }
            } else if (read == 66 && read2 == 77) {
                imageFormat = ImageFormat.IMAGE_FORMAT_BMP;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e222) {
                        Debug.debug(e222);
                    }
                }
            } else if (read == 77 && read2 == 77) {
                imageFormat = ImageFormat.IMAGE_FORMAT_TIFF;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e2222) {
                        Debug.debug(e2222);
                    }
                }
            } else if (read == 73 && read2 == 73) {
                imageFormat = ImageFormat.IMAGE_FORMAT_TIFF;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e22222) {
                        Debug.debug(e22222);
                    }
                }
            } else if (read == 56 && read2 == 66) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PSD;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e222222) {
                        Debug.debug(e222222);
                    }
                }
            } else if (read == 80 && read2 == 49) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PBM;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e2222222) {
                        Debug.debug(e2222222);
                    }
                }
            } else if (read == 80 && read2 == 52) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PBM;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e22222222) {
                        Debug.debug(e22222222);
                    }
                }
            } else if (read == 80 && read2 == 50) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PGM;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e222222222) {
                        Debug.debug(e222222222);
                    }
                }
            } else if (read == 80 && read2 == 53) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PGM;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e2222222222) {
                        Debug.debug(e2222222222);
                    }
                }
            } else if (read == 80 && read2 == 51) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PPM;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e22222222222) {
                        Debug.debug(e22222222222);
                    }
                }
            } else if (read == 80 && read2 == 54) {
                imageFormat = ImageFormat.IMAGE_FORMAT_PPM;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e222222222222) {
                        Debug.debug(e222222222222);
                    }
                }
            } else {
                if (read == 151 && read2 == 74) {
                    read = inputStream.read();
                    read2 = inputStream.read();
                    if (read < 0 || read2 < 0) {
                        throw new ImageReadException("Couldn't read magic numbers to guess format.");
                    } else if ((read & 255) == 66 && (read2 & 255) == 50) {
                        imageFormat = ImageFormat.IMAGE_FORMAT_JBIG2;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable e2222222222222) {
                                Debug.debug(e2222222222222);
                            }
                        }
                    }
                }
                imageFormat = ImageFormat.IMAGE_FORMAT_UNKNOWN;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable e22222222222222) {
                        Debug.debug(e22222222222222);
                    }
                }
            }
            return imageFormat;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable e222222222222222) {
                    Debug.debug(e222222222222222);
                }
            }
        }
    }
}
