package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Deprecated
public final class MultipartRelatedContent extends AbstractHttpContent {
    private static final byte[] CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding: binary".getBytes();
    private static final byte[] CONTENT_TYPE = "Content-Type: ".getBytes();
    private static final byte[] CR_LF = "\r\n".getBytes();
    private static final byte[] TWO_DASHES = "--".getBytes();
    private final Collection<HttpContent> parts;

    public MultipartRelatedContent(HttpContent httpContent, HttpContent... httpContentArr) {
        super(new HttpMediaType("multipart/related").setParameter("boundary", "END_OF_PART"));
        Collection arrayList = new ArrayList(httpContentArr.length + 1);
        arrayList.add(httpContent);
        arrayList.addAll(Arrays.asList(httpContentArr));
        this.parts = arrayList;
    }

    private static boolean isTextBasedContentType(String str) {
        if (str != null) {
            HttpMediaType httpMediaType = new HttpMediaType(str);
            if (httpMediaType.getType().equals("text") || httpMediaType.getType().equals("application")) {
                return true;
            }
        }
        return false;
    }

    public long computeLength() throws IOException {
        byte[] bytes = getBoundary().getBytes();
        long length = (long) ((TWO_DASHES.length * 2) + bytes.length);
        long j = length;
        for (HttpContent httpContent : this.parts) {
            long length2 = httpContent.getLength();
            if (length2 < 0) {
                return -1;
            }
            String type = httpContent.getType();
            if (type != null) {
                int length3 = CR_LF.length + CONTENT_TYPE.length;
                j += (long) (type.getBytes().length + length3);
            }
            if (!isTextBasedContentType(type)) {
                j += (long) (CONTENT_TRANSFER_ENCODING.length + CR_LF.length);
            }
            j = (((((long) (CR_LF.length * 3)) + length2) + ((long) TWO_DASHES.length)) + ((long) bytes.length)) + j;
        }
        return j;
    }

    public void forRequest(HttpRequest httpRequest) {
        httpRequest.setContent(this);
        httpRequest.getHeaders().setMimeVersion("1.0");
    }

    public String getBoundary() {
        return getMediaType().getParameter("boundary");
    }

    public Collection<HttpContent> getParts() {
        return Collections.unmodifiableCollection(this.parts);
    }

    public boolean retrySupported() {
        for (HttpContent retrySupported : this.parts) {
            if (!retrySupported.retrySupported()) {
                return false;
            }
        }
        return true;
    }

    public MultipartRelatedContent setBoundary(String str) {
        getMediaType().setParameter("boundary", (String) Preconditions.checkNotNull(str));
        return this;
    }

    public MultipartRelatedContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        byte[] bytes = getBoundary().getBytes();
        outputStream.write(TWO_DASHES);
        outputStream.write(bytes);
        for (HttpContent httpContent : this.parts) {
            String type = httpContent.getType();
            if (type != null) {
                byte[] bytes2 = type.getBytes();
                outputStream.write(CR_LF);
                outputStream.write(CONTENT_TYPE);
                outputStream.write(bytes2);
            }
            outputStream.write(CR_LF);
            if (!isTextBasedContentType(type)) {
                outputStream.write(CONTENT_TRANSFER_ENCODING);
                outputStream.write(CR_LF);
            }
            outputStream.write(CR_LF);
            httpContent.writeTo(outputStream);
            outputStream.write(CR_LF);
            outputStream.write(TWO_DASHES);
            outputStream.write(bytes);
        }
        outputStream.write(TWO_DASHES);
        outputStream.flush();
    }
}
