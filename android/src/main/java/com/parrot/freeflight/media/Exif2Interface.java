package com.parrot.freeflight.media;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffFieldTypeConstants;
import org.apache.sanselan.formats.tiff.fieldtypes.FieldType;

public class Exif2Interface {
    private static final String TAG = Exif2Interface.class.getSimpleName();
    private Map<Tag, String> mAttributes = new HashMap();
    private String mFilename;

    public enum Tag {
        IMAGE_DESCRIPTION("ImageDescription", ExifTagConstants.EXIF_TAG_IMAGE_DESCRIPTION, TiffFieldTypeConstants.FIELD_TYPE_ASCII),
        MAKE("Make", ExifTagConstants.EXIF_TAG_MAKE, TiffFieldTypeConstants.FIELD_TYPE_ASCII);
        
        private final FieldType mFieldType;
        private final TagInfo mTagInfo;
        private final String mTagName;

        private Tag(String str, TagInfo tagInfo, FieldType fieldType) {
            this.mTagName = str;
            this.mTagInfo = tagInfo;
            this.mFieldType = fieldType;
        }

        public String toString() {
            return this.mTagName;
        }
    }

    public Exif2Interface(String str) throws IOException {
        this.mFilename = str;
        loadAttributes();
    }

    private void loadAttributes() throws IOException {
        InputStream inputStream = null;
        synchronized (this) {
            try {
                inputStream = openInputStream();
                JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) Sanselan.getMetadata(inputStream, null);
                if (inputStream != null) {
                    inputStream.close();
                }
                if (jpegImageMetadata != null) {
                    for (Tag tag : Tag.values()) {
                        TiffField findEXIFValue = jpegImageMetadata.findEXIFValue(tag.mTagInfo);
                        if (findEXIFValue != null) {
                            try {
                                this.mAttributes.put(tag, findEXIFValue.getStringValue());
                            } catch (ImageReadException e) {
                                Log.w(TAG, "Error extracting Exif tag " + tag);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (ImageReadException e2) {
                throw new IOException(e2.getMessage());
            } catch (Throwable th) {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
    }

    private InputStream openInputStream() throws IOException {
        return onOpenStream();
    }

    public String getAttribute(Tag tag) {
        return (String) this.mAttributes.get(tag);
    }

    protected InputStream onOpenStream() throws IOException {
        return this.mFilename.startsWith("http") ? new BufferedInputStream(new URL(this.mFilename).openConnection().getInputStream()) : new BufferedInputStream(new FileInputStream(new File(this.mFilename)));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveAttributes() throws java.io.IOException {
        /*
        r12 = this;
        r3 = 0;
        monitor-enter(r12);
        r5 = new java.io.File;	 Catch:{ all -> 0x00ac }
        r1 = r12.mFilename;	 Catch:{ all -> 0x00ac }
        r5.<init>(r1);	 Catch:{ all -> 0x00ac }
        r1 = org.apache.sanselan.Sanselan.getMetadata(r5);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1 = (org.apache.sanselan.formats.jpeg.JpegImageMetadata) r1;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        if (r1 == 0) goto L_0x00e0;
    L_0x0011:
        r1 = r1.getExif();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        if (r1 == 0) goto L_0x00e0;
    L_0x0017:
        r1 = r1.getOutputSet();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
    L_0x001b:
        if (r1 != 0) goto L_0x00dd;
    L_0x001d:
        r1 = new org.apache.sanselan.formats.tiff.write.TiffOutputSet;	 Catch:{ SanselanException -> 0x00da, all -> 0x00e7 }
        r1.<init>();	 Catch:{ SanselanException -> 0x00da, all -> 0x00e7 }
        r4 = r1;
    L_0x0023:
        r1 = r12.mAttributes;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1 = r1.size();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        if (r1 <= 0) goto L_0x00d8;
    L_0x002b:
        r1 = r12.mAttributes;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1 = r1.keySet();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r6 = r1.iterator();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
    L_0x0035:
        r1 = r6.hasNext();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        if (r1 == 0) goto L_0x00af;
    L_0x003b:
        r1 = r6.next();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r0 = r1;
        r0 = (com.parrot.freeflight.media.Exif2Interface.Tag) r0;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r2 = r0;
        r1 = r12.mAttributes;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1 = r1.get(r2);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1 = (java.lang.String) r1;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r7 = r2.mTagInfo;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r8 = r2.mFieldType;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r9 = r4.byteOrder;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r7 = r7.encodeValue(r8, r1, r9);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r8 = new org.apache.sanselan.formats.tiff.write.TiffOutputField;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r9 = r2.mTagInfo;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r10 = r2.mFieldType;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r11 = r7.length;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r8.<init>(r9, r10, r11, r7);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r7 = r4.getOrCreateExifDirectory();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r9 = r2.mTagInfo;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r7.removeField(r9);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r7.add(r8);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r7 = TAG;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r8 = new java.lang.StringBuilder;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r8.<init>();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r9 = "Saving tag ";
        r8 = r8.append(r9);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r2 = r8.append(r2);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r8 = " with value ";
        r2 = r2.append(r8);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1 = r2.append(r1);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1 = r1.toString();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        android.util.Log.v(r7, r1);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        goto L_0x0035;
    L_0x0098:
        r1 = move-exception;
        r2 = r3;
    L_0x009a:
        r1.printStackTrace();	 Catch:{ all -> 0x00a3 }
        r1 = new java.io.IOException;	 Catch:{ all -> 0x00a3 }
        r1.<init>();	 Catch:{ all -> 0x00a3 }
        throw r1;	 Catch:{ all -> 0x00a3 }
    L_0x00a3:
        r1 = move-exception;
        r3 = r2;
    L_0x00a5:
        r2 = r3;
    L_0x00a6:
        if (r2 == 0) goto L_0x00ab;
    L_0x00a8:
        r2.close();	 Catch:{ all -> 0x00ac }
    L_0x00ab:
        throw r1;	 Catch:{ all -> 0x00ac }
    L_0x00ac:
        r1 = move-exception;
        monitor-exit(r12);
        throw r1;
    L_0x00af:
        r1 = "exif";
        r2 = 0;
        r6 = r5.getParentFile();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1 = java.io.File.createTempFile(r1, r2, r6);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1.createNewFile();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r2 = new java.io.BufferedOutputStream;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r6 = new java.io.FileOutputStream;	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r6.<init>(r1);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r2.<init>(r6);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r6 = new org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;	 Catch:{ SanselanException -> 0x00e3, all -> 0x00e5 }
        r6.<init>();	 Catch:{ SanselanException -> 0x00e3, all -> 0x00e5 }
        r6.updateExifMetadataLossless(r5, r2, r4);	 Catch:{ SanselanException -> 0x00e3, all -> 0x00e5 }
        r2.close();	 Catch:{ SanselanException -> 0x00e3, all -> 0x00e5 }
        r5.delete();	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
        r1.renameTo(r5);	 Catch:{ SanselanException -> 0x0098, all -> 0x00e7 }
    L_0x00d8:
        monitor-exit(r12);
        return;
    L_0x00da:
        r1 = move-exception;
        r2 = r3;
        goto L_0x009a;
    L_0x00dd:
        r4 = r1;
        goto L_0x0023;
    L_0x00e0:
        r1 = r3;
        goto L_0x001b;
    L_0x00e3:
        r1 = move-exception;
        goto L_0x009a;
    L_0x00e5:
        r1 = move-exception;
        goto L_0x00a6;
    L_0x00e7:
        r1 = move-exception;
        goto L_0x00a5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.media.Exif2Interface.saveAttributes():void");
    }

    public void setAttribute(Tag tag, String str) {
        this.mAttributes.put(tag, str);
    }
}
