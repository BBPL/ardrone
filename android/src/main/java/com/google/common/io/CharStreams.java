package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Beta
public final class CharStreams {
    private static final int BUF_SIZE = 2048;

    private CharStreams() {
    }

    public static Writer asWriter(Appendable appendable) {
        return appendable instanceof Writer ? (Writer) appendable : new AppendableWriter(appendable);
    }

    public static <R extends Readable & Closeable, W extends Appendable & Closeable> long copy(InputSupplier<R> inputSupplier, OutputSupplier<W> outputSupplier) throws IOException {
        Appendable appendable;
        Throwable th;
        boolean z;
        boolean z2 = false;
        Readable readable = (Readable) inputSupplier.getInput();
        try {
            appendable = (Appendable) outputSupplier.getOutput();
            long copy = copy(readable, appendable);
            Closeables.close((Closeable) appendable, false);
            Closeables.close((Closeable) readable, false);
            return copy;
        } catch (Throwable th2) {
            th = th2;
            z = true;
        }
        Closeable closeable = (Closeable) readable;
        if (z < true) {
            z2 = true;
        }
        Closeables.close(closeable, z2);
        throw th;
    }

    public static <R extends Readable & Closeable> long copy(InputSupplier<R> inputSupplier, Appendable appendable) throws IOException {
        Readable readable = (Readable) inputSupplier.getInput();
        long copy;
        try {
            copy = copy(readable, appendable);
            return copy;
        } finally {
            copy = 1;
            Closeables.close((Closeable) readable, true);
        }
    }

    public static long copy(Readable readable, Appendable appendable) throws IOException {
        CharBuffer allocate = CharBuffer.allocate(2048);
        long j = 0;
        while (readable.read(allocate) != -1) {
            allocate.flip();
            appendable.append(allocate);
            j += (long) allocate.remaining();
            allocate.clear();
        }
        return j;
    }

    public static InputSupplier<Reader> join(final Iterable<? extends InputSupplier<? extends Reader>> iterable) {
        return new InputSupplier<Reader>() {
            public Reader getInput() throws IOException {
                return new MultiReader(iterable.iterator());
            }
        };
    }

    public static InputSupplier<Reader> join(InputSupplier<? extends Reader>... inputSupplierArr) {
        return join(Arrays.asList(inputSupplierArr));
    }

    public static InputSupplier<InputStreamReader> newReaderSupplier(final InputSupplier<? extends InputStream> inputSupplier, final Charset charset) {
        Preconditions.checkNotNull(inputSupplier);
        Preconditions.checkNotNull(charset);
        return new InputSupplier<InputStreamReader>() {
            public InputStreamReader getInput() throws IOException {
                return new InputStreamReader((InputStream) inputSupplier.getInput(), charset);
            }
        };
    }

    public static InputSupplier<StringReader> newReaderSupplier(final String str) {
        Preconditions.checkNotNull(str);
        return new InputSupplier<StringReader>() {
            public StringReader getInput() {
                return new StringReader(str);
            }
        };
    }

    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(final OutputSupplier<? extends OutputStream> outputSupplier, final Charset charset) {
        Preconditions.checkNotNull(outputSupplier);
        Preconditions.checkNotNull(charset);
        return new OutputSupplier<OutputStreamWriter>() {
            public OutputStreamWriter getOutput() throws IOException {
                return new OutputStreamWriter((OutputStream) outputSupplier.getOutput(), charset);
            }
        };
    }

    public static <R extends Readable & Closeable> String readFirstLine(InputSupplier<R> inputSupplier) throws IOException {
        Readable readable = (Readable) inputSupplier.getInput();
        try {
            String readLine = new LineReader(readable).readLine();
            return readLine;
        } finally {
            Closeables.close((Closeable) readable, true);
        }
    }

    public static <R extends Readable & Closeable, T> T readLines(InputSupplier<R> inputSupplier, LineProcessor<T> lineProcessor) throws IOException {
        Readable readable = (Readable) inputSupplier.getInput();
        try {
            LineReader lineReader = new LineReader(readable);
            String readLine;
            do {
                readLine = lineReader.readLine();
                if (readLine == null) {
                    break;
                }
            } while (lineProcessor.processLine(readLine));
            Closeables.close((Closeable) readable, false);
            return lineProcessor.getResult();
        } catch (Throwable th) {
            Closeables.close((Closeable) readable, true);
        }
    }

    public static <R extends Readable & Closeable> List<String> readLines(InputSupplier<R> inputSupplier) throws IOException {
        Readable readable = (Readable) inputSupplier.getInput();
        try {
            List<String> readLines = readLines(readable);
            return readLines;
        } finally {
            Closeables.close((Closeable) readable, true);
        }
    }

    public static List<String> readLines(Readable readable) throws IOException {
        List<String> arrayList = new ArrayList();
        LineReader lineReader = new LineReader(readable);
        while (true) {
            String readLine = lineReader.readLine();
            if (readLine == null) {
                return arrayList;
            }
            arrayList.add(readLine);
        }
    }

    public static void skipFully(Reader reader, long j) throws IOException {
        while (j > 0) {
            long skip = reader.skip(j);
            if (skip != 0) {
                j -= skip;
            } else if (reader.read() == -1) {
                throw new EOFException();
            } else {
                j--;
            }
        }
    }

    public static <R extends Readable & Closeable> String toString(InputSupplier<R> inputSupplier) throws IOException {
        return toStringBuilder((InputSupplier) inputSupplier).toString();
    }

    public static String toString(Readable readable) throws IOException {
        return toStringBuilder(readable).toString();
    }

    private static <R extends Readable & Closeable> StringBuilder toStringBuilder(InputSupplier<R> inputSupplier) throws IOException {
        Readable readable = (Readable) inputSupplier.getInput();
        try {
            StringBuilder toStringBuilder = toStringBuilder(readable);
            return toStringBuilder;
        } finally {
            Closeables.close((Closeable) readable, true);
        }
    }

    private static StringBuilder toStringBuilder(Readable readable) throws IOException {
        Appendable stringBuilder = new StringBuilder();
        copy(readable, stringBuilder);
        return stringBuilder;
    }

    public static <W extends Appendable & Closeable> void write(CharSequence charSequence, OutputSupplier<W> outputSupplier) throws IOException {
        Preconditions.checkNotNull(charSequence);
        Appendable appendable = (Appendable) outputSupplier.getOutput();
        try {
            appendable.append(charSequence);
        } finally {
            Closeables.close((Closeable) appendable, true);
        }
    }
}
