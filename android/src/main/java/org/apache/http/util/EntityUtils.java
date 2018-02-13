package org.apache.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;

public final class EntityUtils {
    private EntityUtils() {
    }

    public static void consume(HttpEntity httpEntity) throws IOException {
        if (httpEntity != null && httpEntity.isStreaming()) {
            InputStream content = httpEntity.getContent();
            if (content != null) {
                content.close();
            }
        }
    }

    public static void consumeQuietly(HttpEntity httpEntity) {
        try {
            consume(httpEntity);
        } catch (IOException e) {
        }
    }

    @Deprecated
    public static String getContentCharSet(HttpEntity httpEntity) throws ParseException {
        if (httpEntity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        } else if (httpEntity.getContentType() == null) {
            return null;
        } else {
            HeaderElement[] elements = httpEntity.getContentType().getElements();
            if (elements.length <= 0) {
                return null;
            }
            NameValuePair parameterByName = elements[0].getParameterByName("charset");
            return parameterByName != null ? parameterByName.getValue() : null;
        }
    }

    @Deprecated
    public static String getContentMimeType(HttpEntity httpEntity) throws ParseException {
        if (httpEntity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        } else if (httpEntity.getContentType() == null) {
            return null;
        } else {
            HeaderElement[] elements = httpEntity.getContentType().getElements();
            return elements.length > 0 ? elements[0].getName() : null;
        }
    }

    public static byte[] toByteArray(HttpEntity httpEntity) throws IOException {
        int i = 4096;
        if (httpEntity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream content = httpEntity.getContent();
        if (content == null) {
            return null;
        }
        try {
            if (httpEntity.getContentLength() > 2147483647L) {
                throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
            }
            int contentLength = (int) httpEntity.getContentLength();
            if (contentLength >= 0) {
                i = contentLength;
            }
            ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(i);
            byte[] bArr = new byte[4096];
            while (true) {
                int read = content.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayBuffer.append(bArr, 0, read);
            }
            bArr = byteArrayBuffer.toByteArray();
            return bArr;
        } finally {
            content.close();
        }
    }

    public static String toString(HttpEntity httpEntity) throws IOException, ParseException {
        return toString(httpEntity, (Charset) null);
    }

    public static String toString(HttpEntity httpEntity, String str) throws IOException, ParseException {
        return toString(httpEntity, Charset.forName(str));
    }

    public static String toString(HttpEntity httpEntity, Charset charset) throws IOException, ParseException {
        if (httpEntity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream content = httpEntity.getContent();
        if (content == null) {
            return null;
        }
        try {
            if (httpEntity.getContentLength() > 2147483647L) {
                throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
            }
            int contentLength = (int) httpEntity.getContentLength();
            int i = contentLength < 0 ? 4096 : contentLength;
            Charset charset2 = ContentType.getOrDefault(httpEntity).getCharset();
            if (charset2 == null) {
                charset2 = charset;
            }
            if (charset2 == null) {
                charset2 = HTTP.DEF_CONTENT_CHARSET;
            }
            Reader inputStreamReader = new InputStreamReader(content, charset2);
            CharArrayBuffer charArrayBuffer = new CharArrayBuffer(i);
            char[] cArr = new char[1024];
            while (true) {
                int read = inputStreamReader.read(cArr);
                if (read == -1) {
                    break;
                }
                charArrayBuffer.append(cArr, 0, read);
            }
            String charArrayBuffer2 = charArrayBuffer.toString();
            return charArrayBuffer2;
        } finally {
            content.close();
        }
    }
}
