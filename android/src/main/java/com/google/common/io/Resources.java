package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

@Beta
public final class Resources {
    private Resources() {
    }

    public static void copy(URL url, OutputStream outputStream) throws IOException {
        ByteStreams.copy(newInputStreamSupplier(url), outputStream);
    }

    public static URL getResource(Class<?> cls, String str) {
        URL resource = cls.getResource(str);
        Preconditions.checkArgument(resource != null, "resource %s relative to %s not found.", str, cls.getName());
        return resource;
    }

    public static URL getResource(String str) {
        URL resource = Resources.class.getClassLoader().getResource(str);
        Preconditions.checkArgument(resource != null, "resource %s not found.", str);
        return resource;
    }

    public static InputSupplier<InputStream> newInputStreamSupplier(final URL url) {
        Preconditions.checkNotNull(url);
        return new InputSupplier<InputStream>() {
            public InputStream getInput() throws IOException {
                return url.openStream();
            }
        };
    }

    public static InputSupplier<InputStreamReader> newReaderSupplier(URL url, Charset charset) {
        return CharStreams.newReaderSupplier(newInputStreamSupplier(url), charset);
    }

    public static <T> T readLines(URL url, Charset charset, LineProcessor<T> lineProcessor) throws IOException {
        return CharStreams.readLines(newReaderSupplier(url, charset), lineProcessor);
    }

    public static List<String> readLines(URL url, Charset charset) throws IOException {
        return CharStreams.readLines(newReaderSupplier(url, charset));
    }

    public static byte[] toByteArray(URL url) throws IOException {
        return ByteStreams.toByteArray(newInputStreamSupplier(url));
    }

    public static String toString(URL url, Charset charset) throws IOException {
        return CharStreams.toString(newReaderSupplier(url, charset));
    }
}
