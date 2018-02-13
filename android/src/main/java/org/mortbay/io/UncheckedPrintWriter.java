package org.mortbay.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import org.mortbay.log.Log;

public class UncheckedPrintWriter extends PrintWriter {
    private boolean autoFlush;
    private String lineSeparator;

    public UncheckedPrintWriter(OutputStream outputStream) {
        this(outputStream, false);
    }

    public UncheckedPrintWriter(OutputStream outputStream, boolean z) {
        this(new BufferedWriter(new OutputStreamWriter(outputStream)), z);
    }

    public UncheckedPrintWriter(Writer writer) {
        this(writer, false);
    }

    public UncheckedPrintWriter(Writer writer, boolean z) {
        super(writer, z);
        this.autoFlush = false;
        this.autoFlush = z;
        this.lineSeparator = System.getProperty("line.separator");
    }

    private void isOpen() throws IOException {
        if (this.out == null) {
            throw new IOException("Stream closed");
        }
    }

    private void newLine() {
        try {
            synchronized (this.lock) {
                isOpen();
                this.out.write(this.lineSeparator);
                if (this.autoFlush) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
        } catch (Throwable e2) {
            Log.debug(e2);
            setError();
            throw new RuntimeIOException(e2);
        }
    }

    public void close() {
        try {
            synchronized (this.lock) {
                this.out.close();
            }
        } catch (Throwable e) {
            Log.debug(e);
            setError();
            throw new RuntimeIOException(e);
        }
    }

    public void flush() {
        try {
            synchronized (this.lock) {
                isOpen();
                this.out.flush();
            }
        } catch (Throwable e) {
            Log.debug(e);
            setError();
            throw new RuntimeIOException(e);
        }
    }

    public void print(char c) {
        write((int) c);
    }

    public void print(double d) {
        write(String.valueOf(d));
    }

    public void print(float f) {
        write(String.valueOf(f));
    }

    public void print(int i) {
        write(String.valueOf(i));
    }

    public void print(long j) {
        write(String.valueOf(j));
    }

    public void print(Object obj) {
        write(String.valueOf(obj));
    }

    public void print(String str) {
        if (str == null) {
            str = "null";
        }
        write(str);
    }

    public void print(boolean z) {
        write(z ? "true" : "false");
    }

    public void print(char[] cArr) {
        write(cArr);
    }

    public void println() {
        newLine();
    }

    public void println(char c) {
        synchronized (this.lock) {
            print(c);
            println();
        }
    }

    public void println(double d) {
        synchronized (this.lock) {
            print(d);
            println();
        }
    }

    public void println(float f) {
        synchronized (this.lock) {
            print(f);
            println();
        }
    }

    public void println(int i) {
        synchronized (this.lock) {
            print(i);
            println();
        }
    }

    public void println(long j) {
        synchronized (this.lock) {
            print(j);
            println();
        }
    }

    public void println(Object obj) {
        synchronized (this.lock) {
            print(obj);
            println();
        }
    }

    public void println(String str) {
        synchronized (this.lock) {
            print(str);
            println();
        }
    }

    public void println(boolean z) {
        synchronized (this.lock) {
            print(z);
            println();
        }
    }

    public void println(char[] cArr) {
        synchronized (this.lock) {
            print(cArr);
            println();
        }
    }

    public void write(int i) {
        try {
            synchronized (this.lock) {
                isOpen();
                this.out.write(i);
            }
        } catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
        } catch (Throwable e2) {
            Log.debug(e2);
            setError();
            throw new RuntimeIOException(e2);
        }
    }

    public void write(String str) {
        write(str, 0, str.length());
    }

    public void write(String str, int i, int i2) {
        try {
            synchronized (this.lock) {
                isOpen();
                this.out.write(str, i, i2);
            }
        } catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
        } catch (Throwable e2) {
            Log.debug(e2);
            setError();
            throw new RuntimeIOException(e2);
        }
    }

    public void write(char[] cArr) {
        write(cArr, 0, cArr.length);
    }

    public void write(char[] cArr, int i, int i2) {
        try {
            synchronized (this.lock) {
                isOpen();
                this.out.write(cArr, i, i2);
            }
        } catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
        } catch (Throwable e2) {
            Log.debug(e2);
            setError();
            throw new RuntimeIOException(e2);
        }
    }
}
