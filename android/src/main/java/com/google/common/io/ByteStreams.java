package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.zip.Checksum;

@Beta
public final class ByteStreams {
    private static final int BUF_SIZE = 4096;

    private static class ByteArrayDataInputStream implements ByteArrayDataInput {
        final DataInput input;

        ByteArrayDataInputStream(byte[] bArr) {
            this.input = new DataInputStream(new ByteArrayInputStream(bArr));
        }

        ByteArrayDataInputStream(byte[] bArr, int i) {
            this.input = new DataInputStream(new ByteArrayInputStream(bArr, i, bArr.length - i));
        }

        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public byte readByte() {
            try {
                return this.input.readByte();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            } catch (IOException e2) {
                throw new AssertionError(e2);
            }
        }

        public char readChar() {
            try {
                return this.input.readChar();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public double readDouble() {
            try {
                return this.input.readDouble();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public float readFloat() {
            try {
                return this.input.readFloat();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public void readFully(byte[] bArr) {
            try {
                this.input.readFully(bArr);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public void readFully(byte[] bArr, int i, int i2) {
            try {
                this.input.readFully(bArr, i, i2);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public int readInt() {
            try {
                return this.input.readInt();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public String readLine() {
            try {
                return this.input.readLine();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public long readLong() {
            try {
                return this.input.readLong();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public short readShort() {
            try {
                return this.input.readShort();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public String readUTF() {
            try {
                return this.input.readUTF();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public int readUnsignedByte() {
            try {
                return this.input.readUnsignedByte();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }

        public int skipBytes(int i) {
            try {
                return this.input.skipBytes(i);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
        final ByteArrayOutputStream byteArrayOutputSteam;
        final DataOutput output;

        ByteArrayDataOutputStream() {
            this(new ByteArrayOutputStream());
        }

        ByteArrayDataOutputStream(int i) {
            this(new ByteArrayOutputStream(i));
        }

        ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
            this.byteArrayOutputSteam = byteArrayOutputStream;
            this.output = new DataOutputStream(byteArrayOutputStream);
        }

        public byte[] toByteArray() {
            return this.byteArrayOutputSteam.toByteArray();
        }

        public void write(int i) {
            try {
                this.output.write(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void write(byte[] bArr) {
            try {
                this.output.write(bArr);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void write(byte[] bArr, int i, int i2) {
            try {
                this.output.write(bArr, i, i2);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeBoolean(boolean z) {
            try {
                this.output.writeBoolean(z);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeByte(int i) {
            try {
                this.output.writeByte(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeBytes(String str) {
            try {
                this.output.writeBytes(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeChar(int i) {
            try {
                this.output.writeChar(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeChars(String str) {
            try {
                this.output.writeChars(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeDouble(double d) {
            try {
                this.output.writeDouble(d);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeFloat(float f) {
            try {
                this.output.writeFloat(f);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeInt(int i) {
            try {
                this.output.writeInt(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeLong(long j) {
            try {
                this.output.writeLong(j);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeShort(int i) {
            try {
                this.output.writeShort(i);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public void writeUTF(String str) {
            try {
                this.output.writeUTF(str);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
    }

    private ByteStreams() {
    }

    public static long copy(InputSupplier<? extends InputStream> inputSupplier, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
        Closeable closeable;
        long copy;
        boolean z;
        boolean z2 = false;
        boolean z3 = true;
        InputStream inputStream = (InputStream) inputSupplier.getInput();
        try {
            closeable = (OutputStream) outputSupplier.getOutput();
            copy = copy(inputStream, (OutputStream) closeable);
        } catch (Throwable th) {
            closeable = th;
            if (z < true) {
                z2 = true;
            }
            Closeables.close(inputStream, z2);
            throw closeable;
        } finally {
            z2 = 
/*
Method generation error in method: com.google.common.io.ByteStreams.copy(com.google.common.io.InputSupplier, com.google.common.io.OutputSupplier):long
jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r3_3 'z2' boolean) = (r3_2 'z2' boolean), (r4_0 'z3' boolean) in method: com.google.common.io.ByteStreams.copy(com.google.common.io.InputSupplier, com.google.common.io.OutputSupplier):long
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:203)
	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:100)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:50)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:297)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:328)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:265)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:228)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:118)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:83)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:19)
	at jadx.core.ProcessClass.process(ProcessClass.java:43)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
Caused by: jadx.core.utils.exceptions.CodegenException: MERGE can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:530)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:514)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 20 more

*/

            public static long copy(InputSupplier<? extends InputStream> inputSupplier, OutputStream outputStream) throws IOException {
                InputStream inputStream = (InputStream) inputSupplier.getInput();
                long copy;
                try {
                    copy = copy(inputStream, outputStream);
                    return copy;
                } finally {
                    copy = 1;
                    Closeables.close(inputStream, true);
                }
            }

            public static long copy(InputStream inputStream, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
                OutputStream outputStream = (OutputStream) outputSupplier.getOutput();
                long copy;
                try {
                    copy = copy(inputStream, outputStream);
                    return copy;
                } finally {
                    copy = 1;
                    Closeables.close(outputStream, true);
                }
            }

            public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
                byte[] bArr = new byte[4096];
                long j = 0;
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        return j;
                    }
                    outputStream.write(bArr, 0, read);
                    j += (long) read;
                }
            }

            public static long copy(ReadableByteChannel readableByteChannel, WritableByteChannel writableByteChannel) throws IOException {
                ByteBuffer allocate = ByteBuffer.allocate(4096);
                long j = 0;
                while (readableByteChannel.read(allocate) != -1) {
                    allocate.flip();
                    while (allocate.hasRemaining()) {
                        j += (long) writableByteChannel.write(allocate);
                    }
                    allocate.clear();
                }
                return j;
            }

            public static boolean equal(InputSupplier<? extends InputStream> inputSupplier, InputSupplier<? extends InputStream> inputSupplier2) throws IOException {
                Throwable th;
                boolean z = true;
                byte[] bArr = new byte[4096];
                byte[] bArr2 = new byte[4096];
                InputStream inputStream = (InputStream) inputSupplier.getInput();
                InputStream inputStream2;
                try {
                    inputStream2 = (InputStream) inputSupplier2.getInput();
                    int read;
                    do {
                        read = read(inputStream, bArr, 0, 4096);
                        if (!(read == read(inputStream2, bArr2, 0, 4096) && Arrays.equals(bArr, bArr2))) {
                            try {
                                Closeables.close(inputStream2, false);
                                Closeables.close(inputStream, false);
                                return false;
                            } catch (Throwable th2) {
                                th = th2;
                                z = false;
                                Closeables.close(inputStream, z);
                                throw th;
                            }
                        }
                    } while (read == 4096);
                    Closeables.close(inputStream2, false);
                    Closeables.close(inputStream, false);
                    return true;
                } catch (Throwable th3) {
                    th = th3;
                    Closeables.close(inputStream, z);
                    throw th;
                }
            }

            public static long getChecksum(InputSupplier<? extends InputStream> inputSupplier, final Checksum checksum) throws IOException {
                return ((Long) readBytes(inputSupplier, new ByteProcessor<Long>() {
                    public Long getResult() {
                        long value = checksum.getValue();
                        checksum.reset();
                        return Long.valueOf(value);
                    }

                    public boolean processBytes(byte[] bArr, int i, int i2) {
                        checksum.update(bArr, i, i2);
                        return true;
                    }
                })).longValue();
            }

            public static HashCode hash(InputSupplier<? extends InputStream> inputSupplier, HashFunction hashFunction) throws IOException {
                Object newHasher = hashFunction.newHasher();
                copy((InputSupplier) inputSupplier, Funnels.asOutputStream(newHasher));
                return newHasher.hash();
            }

            public static InputSupplier<InputStream> join(final Iterable<? extends InputSupplier<? extends InputStream>> iterable) {
                return new InputSupplier<InputStream>() {
                    public InputStream getInput() throws IOException {
                        return new MultiInputStream(iterable.iterator());
                    }
                };
            }

            public static InputSupplier<InputStream> join(InputSupplier<? extends InputStream>... inputSupplierArr) {
                return join(Arrays.asList(inputSupplierArr));
            }

            public static long length(InputSupplier<? extends InputStream> inputSupplier) throws IOException {
                InputStream inputStream = (InputStream) inputSupplier.getInput();
                long j = 0;
                while (true) {
                    try {
                        long skip = inputStream.skip(2147483647L);
                        if (skip != 0) {
                            j += skip;
                        } else if (inputStream.read() == -1) {
                            break;
                        } else {
                            j++;
                        }
                    } finally {
                        j = 1;
                        Closeables.close(inputStream, true);
                    }
                }
                return j;
            }

            public static ByteArrayDataInput newDataInput(byte[] bArr) {
                return new ByteArrayDataInputStream(bArr);
            }

            public static ByteArrayDataInput newDataInput(byte[] bArr, int i) {
                Preconditions.checkPositionIndex(i, bArr.length);
                return new ByteArrayDataInputStream(bArr, i);
            }

            public static ByteArrayDataOutput newDataOutput() {
                return new ByteArrayDataOutputStream();
            }

            public static ByteArrayDataOutput newDataOutput(int i) {
                Preconditions.checkArgument(i >= 0, "Invalid size: %s", Integer.valueOf(i));
                return new ByteArrayDataOutputStream(i);
            }

            public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] bArr) {
                return newInputStreamSupplier(bArr, 0, bArr.length);
            }

            public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(final byte[] bArr, final int i, final int i2) {
                return new InputSupplier<ByteArrayInputStream>() {
                    public ByteArrayInputStream getInput() {
                        return new ByteArrayInputStream(bArr, i, i2);
                    }
                };
            }

            public static int read(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
                if (i2 < 0) {
                    throw new IndexOutOfBoundsException("len is negative");
                }
                int i3 = 0;
                while (i3 < i2) {
                    int read = inputStream.read(bArr, i + i3, i2 - i3);
                    if (read == -1) {
                        break;
                    }
                    i3 += read;
                }
                return i3;
            }

            public static <T> T readBytes(InputSupplier<? extends InputStream> inputSupplier, ByteProcessor<T> byteProcessor) throws IOException {
                boolean z = true;
                boolean z2 = false;
                byte[] bArr = new byte[4096];
                InputStream inputStream = (InputStream) inputSupplier.getInput();
                int read;
                do {
                    try {
                        read = inputStream.read(bArr);
                        if (read != -1) {
                        }
                        break;
                    } catch (Throwable th) {
                        Throwable th2 = th;
                    }
                } while (byteProcessor.processBytes(bArr, 0, read));
                z2 = true;
                try {
                    T result = byteProcessor.getResult();
                    Closeables.close(inputStream, z2);
                    return result;
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    z = z2;
                    th2 = th4;
                    Closeables.close(inputStream, z);
                    throw th2;
                }
            }

            public static void readFully(InputStream inputStream, byte[] bArr) throws IOException {
                readFully(inputStream, bArr, 0, bArr.length);
            }

            public static void readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
                if (read(inputStream, bArr, i, i2) != i2) {
                    throw new EOFException();
                }
            }

            public static void skipFully(InputStream inputStream, long j) throws IOException {
                while (j > 0) {
                    long skip = inputStream.skip(j);
                    if (skip != 0) {
                        j -= skip;
                    } else if (inputStream.read() == -1) {
                        throw new EOFException();
                    } else {
                        j--;
                    }
                }
            }

            public static InputSupplier<InputStream> slice(InputSupplier<? extends InputStream> inputSupplier, long j, long j2) {
                boolean z = true;
                Preconditions.checkNotNull(inputSupplier);
                Preconditions.checkArgument(j >= 0, "offset is negative");
                if (j2 < 0) {
                    z = false;
                }
                Preconditions.checkArgument(z, "length is negative");
                final InputSupplier<? extends InputStream> inputSupplier2 = inputSupplier;
                final long j3 = j;
                final long j4 = j2;
                return new InputSupplier<InputStream>() {
                    public InputStream getInput() throws IOException {
                        InputStream inputStream = (InputStream) inputSupplier2.getInput();
                        if (j3 > 0) {
                            try {
                                ByteStreams.skipFully(inputStream, j3);
                            } catch (IOException e) {
                                Closeables.closeQuietly(inputStream);
                                throw e;
                            }
                        }
                        return new LimitInputStream(inputStream, j4);
                    }
                };
            }

            public static byte[] toByteArray(InputSupplier<? extends InputStream> inputSupplier) throws IOException {
                InputStream inputStream = (InputStream) inputSupplier.getInput();
                try {
                    byte[] toByteArray = toByteArray(inputStream);
                    return toByteArray;
                } finally {
                    Closeables.close(inputStream, true);
                }
            }

            public static byte[] toByteArray(InputStream inputStream) throws IOException {
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                copy(inputStream, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }

            public static void write(byte[] bArr, OutputSupplier<? extends OutputStream> outputSupplier) throws IOException {
                Preconditions.checkNotNull(bArr);
                OutputStream outputStream = (OutputStream) outputSupplier.getOutput();
                try {
                    outputStream.write(bArr);
                } finally {
                    Closeables.close(outputStream, true);
                }
            }
        }
