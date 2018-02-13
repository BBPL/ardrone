package org.apache.sanselan;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import org.mortbay.jetty.HttpVersions;

public class FormatCompliance {
    private final ArrayList comments = new ArrayList();
    private final String description;
    private final boolean failOnError;

    public FormatCompliance(String str) {
        this.description = str;
        this.failOnError = false;
    }

    public FormatCompliance(String str, boolean z) {
        this.description = str;
        this.failOnError = z;
    }

    public static final FormatCompliance getDefault() {
        return new FormatCompliance("ignore", false);
    }

    private String getValueDescription(int i) {
        return new StringBuilder(String.valueOf(i)).append(" (").append(Integer.toHexString(i)).append(")").toString();
    }

    public void addComment(String str) throws ImageReadException {
        this.comments.add(str);
        if (this.failOnError) {
            throw new ImageReadException(str);
        }
    }

    public void addComment(String str, int i) throws ImageReadException {
        addComment(new StringBuilder(String.valueOf(str)).append(": ").append(getValueDescription(i)).toString());
    }

    public boolean checkBounds(String str, int i, int i2, int i3) throws ImageReadException {
        if (i3 >= i && i3 <= i2) {
            return true;
        }
        addComment(new StringBuilder(String.valueOf(str)).append(": ").append("bounds check: ").append(i).append(" <= ").append(i3).append(" <= ").append(i2).append(": false").toString());
        return false;
    }

    public boolean compare(String str, int i, int i2) throws ImageReadException {
        return compare(str, new int[]{i}, i2);
    }

    public boolean compare(String str, int[] iArr, int i) throws ImageReadException {
        int i2;
        for (int i3 : iArr) {
            if (i == i3) {
                return true;
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new StringBuilder(String.valueOf(str)).append(": ").append("Unexpected value: (valid: ").toString());
        if (iArr.length > 1) {
            stringBuffer.append("{");
        }
        for (i2 = 0; i2 < iArr.length; i2++) {
            if (i2 > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(getValueDescription(iArr[i2]));
        }
        if (iArr.length > 1) {
            stringBuffer.append("}");
        }
        stringBuffer.append(", actual: " + getValueDescription(i) + ")");
        addComment(stringBuffer.toString());
        return false;
    }

    public boolean compare_bytes(String str, byte[] bArr, byte[] bArr2) throws ImageReadException {
        if (bArr.length != bArr2.length) {
            addComment(new StringBuilder(String.valueOf(str)).append(": ").append("Unexpected length: (expected: ").append(bArr.length).append(", actual: ").append(bArr2.length).append(")").toString());
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                addComment(new StringBuilder(String.valueOf(str)).append(": ").append("Unexpected value: (expected: ").append(getValueDescription(bArr[i])).append(", actual: ").append(getValueDescription(bArr2[i])).append(")").toString());
                return false;
            }
        }
        return true;
    }

    public void dump() {
        dump(new PrintWriter(new OutputStreamWriter(System.out)));
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("Format Compliance: " + this.description);
        if (this.comments.size() == 0) {
            printWriter.println("\tNo comments.");
        } else {
            for (int i = 0; i < this.comments.size(); i++) {
                printWriter.println("\t" + (i + 1) + ": " + this.comments.get(i));
            }
        }
        printWriter.println(HttpVersions.HTTP_0_9);
        printWriter.flush();
    }

    public String toString() {
        Writer stringWriter = new StringWriter();
        dump(new PrintWriter(stringWriter));
        return stringWriter.getBuffer().toString();
    }
}
