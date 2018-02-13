package org.apache.sanselan.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ZLibUtils extends BinaryFileFunctions {
    public final byte[] deflate(byte[] bArr) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
        deflaterOutputStream.write(bArr);
        deflaterOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public final byte[] inflate(byte[] bArr) throws IOException {
        return getStreamBytes(new InflaterInputStream(new ByteArrayInputStream(bArr)));
    }
}
