package com.google.common.io;

import java.io.IOException;
import org.mortbay.jetty.HttpVersions;

abstract class LineBuffer {
    private StringBuilder line = new StringBuilder();
    private boolean sawReturn;

    LineBuffer() {
    }

    private boolean finishLine(boolean z) throws IOException {
        String stringBuilder = this.line.toString();
        String str = this.sawReturn ? z ? "\r\n" : "\r" : z ? "\n" : HttpVersions.HTTP_0_9;
        handleLine(stringBuilder, str);
        this.line = new StringBuilder();
        this.sawReturn = false;
        return z;
    }

    protected void add(char[] cArr, int i, int i2) throws IOException {
        int i3;
        int i4;
        int i5;
        if (this.sawReturn && i2 > 0) {
            if (finishLine(cArr[i] == '\n')) {
                i3 = i + 1;
                i4 = i + i2;
                i5 = i3;
                while (i3 < i4) {
                    switch (cArr[i3]) {
                        case '\n':
                            this.line.append(cArr, i5, i3 - i5);
                            finishLine(true);
                            i5 = i3 + 1;
                            break;
                        case '\r':
                            this.line.append(cArr, i5, i3 - i5);
                            this.sawReturn = true;
                            if (i3 + 1 < i4) {
                                if (finishLine(cArr[i3 + 1] != '\n')) {
                                    i3++;
                                }
                            }
                            i5 = i3 + 1;
                            break;
                        default:
                            break;
                    }
                    i3++;
                }
                this.line.append(cArr, i5, (i + i2) - i5);
            }
        }
        i3 = i;
        i4 = i + i2;
        i5 = i3;
        while (i3 < i4) {
            switch (cArr[i3]) {
                case '\n':
                    this.line.append(cArr, i5, i3 - i5);
                    finishLine(true);
                    i5 = i3 + 1;
                    break;
                case '\r':
                    this.line.append(cArr, i5, i3 - i5);
                    this.sawReturn = true;
                    if (i3 + 1 < i4) {
                        if (cArr[i3 + 1] != '\n') {
                        }
                        if (finishLine(cArr[i3 + 1] != '\n')) {
                            i3++;
                        }
                    }
                    i5 = i3 + 1;
                    break;
                default:
                    break;
            }
            i3++;
        }
        this.line.append(cArr, i5, (i + i2) - i5);
    }

    protected void finish() throws IOException {
        if (this.sawReturn || this.line.length() > 0) {
            finishLine(false);
        }
    }

    protected abstract void handleLine(String str, String str2) throws IOException;
}
