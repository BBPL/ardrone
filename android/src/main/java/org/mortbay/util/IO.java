package org.mortbay.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import org.mortbay.log.Log;
import org.mortbay.thread.BoundedThreadPool;

public class IO extends BoundedThreadPool {
    public static final String CRLF = "\r\n";
    public static final byte[] CRLF_BYTES = new byte[]{(byte) 13, (byte) 10};
    private static ClosedIS __closedStream = new ClosedIS(null);
    private static NullOS __nullStream = new NullOS(null);
    private static NullWrite __nullWriter = new NullWrite(null);
    public static int bufferSize = 16384;

    static class C13491 {
    }

    private static class ClosedIS extends InputStream {
        private ClosedIS() {
        }

        ClosedIS(C13491 c13491) {
            this();
        }

        public int read() throws IOException {
            return -1;
        }
    }

    static class Job implements Runnable {
        InputStream in;
        OutputStream out;
        Reader read;
        Writer write;

        Job(InputStream inputStream, OutputStream outputStream) {
            this.in = inputStream;
            this.out = outputStream;
            this.read = null;
            this.write = null;
        }

        Job(Reader reader, Writer writer) {
            this.in = null;
            this.out = null;
            this.read = reader;
            this.write = writer;
        }

        public void run() {
            try {
                if (this.in != null) {
                    IO.copy(this.in, this.out, -1);
                } else {
                    IO.copy(this.read, this.write, -1);
                }
            } catch (Throwable e) {
                Log.ignore(e);
                try {
                    if (this.out != null) {
                        this.out.close();
                    }
                    if (this.write != null) {
                        this.write.close();
                    }
                } catch (Throwable e2) {
                    Log.ignore(e2);
                }
            }
        }
    }

    private static class NullOS extends OutputStream {
        private NullOS() {
        }

        NullOS(C13491 c13491) {
            this();
        }

        public void close() {
        }

        public void flush() {
        }

        public void write(int i) {
        }

        public void write(byte[] bArr) {
        }

        public void write(byte[] bArr, int i, int i2) {
        }
    }

    private static class NullWrite extends Writer {
        private NullWrite() {
        }

        NullWrite(C13491 c13491) {
            this();
        }

        public void close() {
        }

        public void flush() {
        }

        public void write(int i) {
        }

        public void write(String str) {
        }

        public void write(String str, int i, int i2) {
        }

        public void write(char[] cArr) {
        }

        public void write(char[] cArr, int i, int i2) {
        }
    }

    private static class Singleton {
        static final IO __instance = new IO();

        static {
            try {
                __instance.start();
            } catch (Throwable e) {
                Log.warn(e);
                System.exit(1);
            }
        }

        private Singleton() {
        }
    }

    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Throwable e) {
                Log.ignore(e);
            }
        }
    }

    public static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Throwable e) {
                Log.ignore(e);
            }
        }
    }

    public static void copy(File file, File file2) throws IOException {
        if (file.isDirectory()) {
            copyDir(file, file2);
        } else {
            copyFile(file, file2);
        }
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, -1);
    }

    public static void copy(InputStream inputStream, OutputStream outputStream, long j) throws IOException {
        byte[] bArr = new byte[bufferSize];
        int i = bufferSize;
        if (j >= 0) {
            while (j > 0) {
                i = j < ((long) bufferSize) ? inputStream.read(bArr, 0, (int) j) : inputStream.read(bArr, 0, bufferSize);
                if (i != -1) {
                    j -= (long) i;
                    outputStream.write(bArr, 0, i);
                } else {
                    return;
                }
            }
            return;
        }
        while (true) {
            i = inputStream.read(bArr, 0, bufferSize);
            if (i >= 0) {
                outputStream.write(bArr, 0, i);
            } else {
                return;
            }
        }
    }

    public static void copy(Reader reader, Writer writer) throws IOException {
        copy(reader, writer, -1);
    }

    public static void copy(Reader reader, Writer writer, long j) throws IOException {
        char[] cArr = new char[bufferSize];
        int i = bufferSize;
        if (j >= 0) {
            while (j > 0) {
                i = j < ((long) bufferSize) ? reader.read(cArr, 0, (int) j) : reader.read(cArr, 0, bufferSize);
                if (i != -1) {
                    j -= (long) i;
                    writer.write(cArr, 0, i);
                } else {
                    return;
                }
            }
        } else if (writer instanceof PrintWriter) {
            PrintWriter printWriter = (PrintWriter) writer;
            while (!printWriter.checkError()) {
                int read = reader.read(cArr, 0, bufferSize);
                if (read != -1) {
                    writer.write(cArr, 0, read);
                } else {
                    return;
                }
            }
        } else {
            while (true) {
                i = reader.read(cArr, 0, bufferSize);
                if (i != -1) {
                    writer.write(cArr, 0, i);
                } else {
                    return;
                }
            }
        }
    }

    public static void copyDir(File file, File file2) throws IOException {
        if (!file2.exists()) {
            file2.mkdirs();
        } else if (!file2.isDirectory()) {
            throw new IllegalArgumentException(file2.toString());
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                String name = listFiles[i].getName();
                if (!(".".equals(name) || "..".equals(name))) {
                    copy(listFiles[i], new File(file2, name));
                }
            }
        }
    }

    public static void copyFile(File file, File file2) throws IOException {
        InputStream fileInputStream = new FileInputStream(file);
        OutputStream fileOutputStream = new FileOutputStream(file2);
        copy(fileInputStream, fileOutputStream);
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void copyThread(InputStream inputStream, OutputStream outputStream) {
        try {
            Object job = new Job(inputStream, outputStream);
            if (!instance().dispatch(job)) {
                job.run();
            }
        } catch (Throwable e) {
            Log.warn(e);
        }
    }

    public static void copyThread(Reader reader, Writer writer) {
        try {
            Object job = new Job(reader, writer);
            if (!instance().dispatch(job)) {
                job.run();
            }
        } catch (Throwable e) {
            Log.warn(e);
        }
    }

    public static boolean delete(File file) {
        int i = 0;
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            while (listFiles != null && i < listFiles.length) {
                delete(listFiles[i]);
                i++;
            }
        }
        return file.delete();
    }

    public static InputStream getClosedStream() {
        return __closedStream;
    }

    public static OutputStream getNullStream() {
        return __nullStream;
    }

    public static Writer getNullWriter() {
        return __nullWriter;
    }

    public static IO instance() {
        return Singleton.__instance;
    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String toString(InputStream inputStream) throws IOException {
        return toString(inputStream, null);
    }

    public static String toString(InputStream inputStream, String str) throws IOException {
        Writer stringWriter = new StringWriter();
        copy(str == null ? new InputStreamReader(inputStream) : new InputStreamReader(inputStream, str), stringWriter);
        return stringWriter.toString();
    }

    public static String toString(Reader reader) throws IOException {
        Writer stringWriter = new StringWriter();
        copy(reader, stringWriter);
        return stringWriter.toString();
    }
}
