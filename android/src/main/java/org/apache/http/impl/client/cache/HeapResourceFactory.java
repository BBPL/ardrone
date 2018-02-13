package org.apache.http.impl.client.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.InputLimit;
import org.apache.http.client.cache.Resource;
import org.apache.http.client.cache.ResourceFactory;

@Immutable
public class HeapResourceFactory implements ResourceFactory {
    public Resource copy(String str, Resource resource) throws IOException {
        byte[] byteArray;
        if (resource instanceof HeapResource) {
            byteArray = ((HeapResource) resource).getByteArray();
        } else {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copyAndClose(resource.getInputStream(), byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        }
        return new HeapResource(byteArray);
    }

    public Resource generate(String str, InputStream inputStream, InputLimit inputLimit) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[2048];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(bArr, 0, read);
            j += (long) read;
            if (inputLimit != null && j > inputLimit.getValue()) {
                break;
            }
        }
        inputLimit.reached();
        return new HeapResource(byteArrayOutputStream.toByteArray());
    }
}
