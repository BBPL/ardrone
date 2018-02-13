package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.OutputStream;

@Beta
public final class NullOutputStream extends OutputStream {
    public void write(int i) {
    }

    public void write(byte[] bArr, int i, int i2) {
    }
}
