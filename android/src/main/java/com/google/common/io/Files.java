package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Checksum;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.util.URIUtil;

@Beta
public final class Files {
    private static final int TEMP_DIR_ATTEMPTS = 10000;

    private Files() {
    }

    public static void append(CharSequence charSequence, File file, Charset charset) throws IOException {
        write(charSequence, file, charset, true);
    }

    public static void copy(InputSupplier<? extends InputStream> inputSupplier, File file) throws IOException {
        ByteStreams.copy((InputSupplier) inputSupplier, newOutputStreamSupplier(file));
    }

    public static <R extends Readable & Closeable> void copy(InputSupplier<R> inputSupplier, File file, Charset charset) throws IOException {
        CharStreams.copy((InputSupplier) inputSupplier, newWriterSupplier(file, charset));
    }

    public static void copy(File file, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
        ByteStreams.copy(newInputStreamSupplier(file), (OutputSupplier) outputSupplier);
    }

    public static void copy(File file, File file2) throws IOException {
        Preconditions.checkArgument(!file.equals(file2), "Source %s and destination %s must be different", file, file2);
        copy(newInputStreamSupplier(file), file2);
    }

    public static void copy(File file, OutputStream outputStream) throws IOException {
        ByteStreams.copy(newInputStreamSupplier(file), outputStream);
    }

    public static <W extends Appendable & Closeable> void copy(File file, Charset charset, OutputSupplier<W> outputSupplier) throws IOException {
        CharStreams.copy(newReaderSupplier(file, charset), (OutputSupplier) outputSupplier);
    }

    public static void copy(File file, Charset charset, Appendable appendable) throws IOException {
        CharStreams.copy(newReaderSupplier(file, charset), appendable);
    }

    public static void createParentDirs(File file) throws IOException {
        File parentFile = file.getCanonicalFile().getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
            if (!parentFile.isDirectory()) {
                throw new IOException("Unable to create parent directories of " + file);
            }
        }
    }

    public static File createTempDir() {
        File file = new File(System.getProperty("java.io.tmpdir"));
        String str = System.currentTimeMillis() + "-";
        for (int i = 0; i < TEMP_DIR_ATTEMPTS; i++) {
            File file2 = new File(file, str + i);
            if (file2.mkdir()) {
                return file2;
            }
        }
        throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + str + "0 to " + str + 9999 + ')');
    }

    public static boolean equal(File file, File file2) throws IOException {
        if (file == file2 || file.equals(file2)) {
            return true;
        }
        long length = file.length();
        long length2 = file2.length();
        return (length == 0 || length2 == 0 || length == length2) ? ByteStreams.equal(newInputStreamSupplier(file), newInputStreamSupplier(file2)) : false;
    }

    public static long getChecksum(File file, Checksum checksum) throws IOException {
        return ByteStreams.getChecksum(newInputStreamSupplier(file), checksum);
    }

    public static String getFileExtension(String str) {
        Preconditions.checkNotNull(str);
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf == -1 ? HttpVersions.HTTP_0_9 : str.substring(lastIndexOf + 1);
    }

    public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
        return ByteStreams.hash(newInputStreamSupplier(file), hashFunction);
    }

    public static MappedByteBuffer map(File file) throws IOException {
        return map(file, MapMode.READ_ONLY);
    }

    public static MappedByteBuffer map(File file, MapMode mapMode) throws IOException {
        if (file.exists()) {
            return map(file, mapMode, file.length());
        }
        throw new FileNotFoundException(file.toString());
    }

    public static MappedByteBuffer map(File file, MapMode mapMode, long j) throws FileNotFoundException, IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, mapMode == MapMode.READ_ONLY ? "r" : "rw");
        try {
            MappedByteBuffer map = map(randomAccessFile, mapMode, j);
            return map;
        } finally {
            Closeables.close(randomAccessFile, true);
        }
    }

    private static MappedByteBuffer map(RandomAccessFile randomAccessFile, MapMode mapMode, long j) throws IOException {
        Closeable channel = randomAccessFile.getChannel();
        try {
            MappedByteBuffer map = channel.map(mapMode, 0, j);
            return map;
        } finally {
            Closeables.close(channel, true);
        }
    }

    public static void move(File file, File file2) throws IOException {
        Preconditions.checkNotNull(file2);
        Preconditions.checkArgument(!file.equals(file2), "Source %s and destination %s must be different", file, file2);
        if (!file.renameTo(file2)) {
            copy(file, file2);
            if (!file.delete()) {
                if (file2.delete()) {
                    throw new IOException("Unable to delete " + file);
                }
                throw new IOException("Unable to delete " + file2);
            }
        }
    }

    public static InputSupplier<FileInputStream> newInputStreamSupplier(final File file) {
        Preconditions.checkNotNull(file);
        return new InputSupplier<FileInputStream>() {
            public FileInputStream getInput() throws IOException {
                return new FileInputStream(file);
            }
        };
    }

    public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file) {
        return newOutputStreamSupplier(file, false);
    }

    public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(final File file, final boolean z) {
        Preconditions.checkNotNull(file);
        return new OutputSupplier<FileOutputStream>() {
            public FileOutputStream getOutput() throws IOException {
                return new FileOutputStream(file, z);
            }
        };
    }

    public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static InputSupplier<InputStreamReader> newReaderSupplier(File file, Charset charset) {
        return CharStreams.newReaderSupplier(newInputStreamSupplier(file), charset);
    }

    public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset) {
        return newWriterSupplier(file, charset, false);
    }

    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset, boolean z) {
        return CharStreams.newWriterSupplier(newOutputStreamSupplier(file, z), charset);
    }

    public static <T> T readBytes(File file, ByteProcessor<T> byteProcessor) throws IOException {
        return ByteStreams.readBytes(newInputStreamSupplier(file), byteProcessor);
    }

    public static String readFirstLine(File file, Charset charset) throws IOException {
        return CharStreams.readFirstLine(newReaderSupplier(file, charset));
    }

    public static <T> T readLines(File file, Charset charset, LineProcessor<T> lineProcessor) throws IOException {
        return CharStreams.readLines(newReaderSupplier(file, charset), lineProcessor);
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return CharStreams.readLines(newReaderSupplier(file, charset));
    }

    public static String simplifyPath(String str) {
        if (str.length() == 0) {
            return ".";
        }
        Iterable<String> split = Splitter.on('/').omitEmptyStrings().split(str);
        Iterable arrayList = new ArrayList();
        for (String str2 : split) {
            if (!str2.equals(".")) {
                if (!str2.equals("..")) {
                    arrayList.add(str2);
                } else if (arrayList.size() <= 0 || ((String) arrayList.get(arrayList.size() - 1)).equals("..")) {
                    arrayList.add("..");
                } else {
                    arrayList.remove(arrayList.size() - 1);
                }
            }
        }
        String str22 = Joiner.on('/').join(arrayList);
        if (str.charAt(0) == '/') {
            str22 = URIUtil.SLASH + str22;
        }
        while (str22.startsWith("/../")) {
            str22 = str22.substring(3);
        }
        return str22.equals("/..") ? URIUtil.SLASH : HttpVersions.HTTP_0_9.equals(str22) ? "." : str22;
    }

    public static byte[] toByteArray(File file) throws IOException {
        boolean z = true;
        z = false;
        Preconditions.checkArgument(file.length() <= 2147483647L);
        if (file.length() == 0) {
            return ByteStreams.toByteArray(newInputStreamSupplier(file));
        }
        byte[] bArr = new byte[((int) file.length())];
        Closeable fileInputStream = new FileInputStream(file);
        try {
            ByteStreams.readFully(fileInputStream, bArr);
        } finally {
            Closeables.close(fileInputStream, z);
        }
        return bArr;
    }

    public static String toString(File file, Charset charset) throws IOException {
        return new String(toByteArray(file), charset.name());
    }

    public static void touch(File file) throws IOException {
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            throw new IOException("Unable to update modification time of " + file);
        }
    }

    public static void write(CharSequence charSequence, File file, Charset charset) throws IOException {
        write(charSequence, file, charset, false);
    }

    private static void write(CharSequence charSequence, File file, Charset charset, boolean z) throws IOException {
        CharStreams.write(charSequence, newWriterSupplier(file, charset, z));
    }

    public static void write(byte[] bArr, File file) throws IOException {
        ByteStreams.write(bArr, newOutputStreamSupplier(file));
    }
}
