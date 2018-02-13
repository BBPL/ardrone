package org.mortbay.util;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.mortbay.jetty.HttpVersions;

public class QuotedStringTokenizer extends StringTokenizer {
    private static final String __delim = "\t\n\r";
    private String _delim;
    private boolean _double;
    private boolean _hasToken;
    private int _i;
    private int _lastStart;
    private boolean _returnDelimiters;
    private boolean _returnQuotes;
    private boolean _single;
    private String _string;
    private StringBuffer _token;

    public QuotedStringTokenizer(String str) {
        this(str, null, false, false);
    }

    public QuotedStringTokenizer(String str, String str2) {
        this(str, str2, false, false);
    }

    public QuotedStringTokenizer(String str, String str2, boolean z) {
        this(str, str2, z, false);
    }

    public QuotedStringTokenizer(String str, String str2, boolean z, boolean z2) {
        super(HttpVersions.HTTP_0_9);
        this._delim = __delim;
        this._returnQuotes = false;
        this._returnDelimiters = false;
        this._hasToken = false;
        this._i = 0;
        this._lastStart = 0;
        this._double = true;
        this._single = true;
        this._string = str;
        if (str2 != null) {
            this._delim = str2;
        }
        this._returnDelimiters = z;
        this._returnQuotes = z2;
        if (this._delim.indexOf(39) >= 0 || this._delim.indexOf(34) >= 0) {
            throw new Error(new StringBuffer().append("Can't use quotes as delimiters: ").append(this._delim).toString());
        }
        this._token = new StringBuffer(this._string.length() > 1024 ? 512 : this._string.length() / 2);
    }

    public static String quote(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "\"\"";
        }
        StringBuffer stringBuffer = new StringBuffer(str.length() + 8);
        quote(stringBuffer, str);
        return stringBuffer.toString();
    }

    public static String quote(String str, String str2) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "\"\"";
        }
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '\\' || charAt == '\"' || charAt == '\'' || Character.isWhitespace(charAt) || str2.indexOf(charAt) >= 0) {
                StringBuffer stringBuffer = new StringBuffer(str.length() + 8);
                quote(stringBuffer, str);
                return stringBuffer.toString();
            }
        }
        return str;
    }

    public static void quote(StringBuffer stringBuffer, String str) {
        int i = 0;
        synchronized (stringBuffer) {
            char charAt;
            stringBuffer.append('\"');
            char[] cArr = null;
            while (i < str.length()) {
                switch (str.charAt(i)) {
                    case '\b':
                        cArr = str.toCharArray();
                        stringBuffer.append(cArr, 0, i);
                        stringBuffer.append("\\b");
                        break;
                    case '\t':
                        cArr = str.toCharArray();
                        stringBuffer.append(cArr, 0, i);
                        stringBuffer.append("\\t");
                        break;
                    case '\n':
                        cArr = str.toCharArray();
                        stringBuffer.append(cArr, 0, i);
                        stringBuffer.append("\\n");
                        break;
                    case '\f':
                        cArr = str.toCharArray();
                        stringBuffer.append(cArr, 0, i);
                        stringBuffer.append("\\f");
                        break;
                    case '\r':
                        cArr = str.toCharArray();
                        stringBuffer.append(cArr, 0, i);
                        stringBuffer.append("\\r");
                        break;
                    case '\"':
                        cArr = str.toCharArray();
                        stringBuffer.append(cArr, 0, i);
                        stringBuffer.append("\\\"");
                        break;
                    case '\\':
                        cArr = str.toCharArray();
                        stringBuffer.append(cArr, 0, i);
                        stringBuffer.append("\\\\");
                        break;
                    default:
                        i++;
                }
                if (cArr != null) {
                    stringBuffer.append(str);
                } else {
                    for (i++; i < str.length(); i++) {
                        charAt = str.charAt(i);
                        switch (charAt) {
                            case '\b':
                                stringBuffer.append("\\b");
                                break;
                            case '\t':
                                stringBuffer.append("\\t");
                                break;
                            case '\n':
                                stringBuffer.append("\\n");
                                break;
                            case '\f':
                                stringBuffer.append("\\f");
                                break;
                            case '\r':
                                stringBuffer.append("\\r");
                                break;
                            case '\"':
                                stringBuffer.append("\\\"");
                                break;
                            case '\\':
                                stringBuffer.append("\\\\");
                                break;
                            default:
                                stringBuffer.append(charAt);
                                break;
                        }
                    }
                }
                stringBuffer.append('\"');
            }
            if (cArr != null) {
                for (i++; i < str.length(); i++) {
                    charAt = str.charAt(i);
                    switch (charAt) {
                        case '\b':
                            stringBuffer.append("\\b");
                            break;
                        case '\t':
                            stringBuffer.append("\\t");
                            break;
                        case '\n':
                            stringBuffer.append("\\n");
                            break;
                        case '\f':
                            stringBuffer.append("\\f");
                            break;
                        case '\r':
                            stringBuffer.append("\\r");
                            break;
                        case '\"':
                            stringBuffer.append("\\\"");
                            break;
                        case '\\':
                            stringBuffer.append("\\\\");
                            break;
                        default:
                            stringBuffer.append(charAt);
                            break;
                    }
                }
            } else {
                stringBuffer.append(str);
            }
            stringBuffer.append('\"');
        }
    }

    public static void quoteIfNeeded(StringBuffer stringBuffer, String str) {
        int i = 0;
        synchronized (stringBuffer) {
            int i2 = 0;
            while (i2 < str.length()) {
                switch (str.charAt(i2)) {
                    case '\b':
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                    case '\"':
                    case '%':
                    case '+':
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_48_BIT_RGB_HALF /*59*/:
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                    case '\\':
                        stringBuffer.append('\"');
                        while (i < i2) {
                            stringBuffer.append(str.charAt(i));
                            i++;
                        }
                        break;
                    default:
                        i2++;
                }
                if (i2 >= 0) {
                    stringBuffer.append(str);
                }
                while (i2 < str.length()) {
                    char charAt = str.charAt(i2);
                    switch (charAt) {
                        case '\b':
                            stringBuffer.append("\\b");
                            break;
                        case '\t':
                            stringBuffer.append("\\t");
                            break;
                        case '\n':
                            stringBuffer.append("\\n");
                            break;
                        case '\f':
                            stringBuffer.append("\\f");
                            break;
                        case '\r':
                            stringBuffer.append("\\r");
                            break;
                        case '\"':
                            stringBuffer.append("\\\"");
                            break;
                        case '\\':
                            stringBuffer.append("\\\\");
                            break;
                        default:
                            stringBuffer.append(charAt);
                            break;
                    }
                    i2++;
                }
                stringBuffer.append('\"');
                return;
            }
            i2 = -1;
            if (i2 >= 0) {
                while (i2 < str.length()) {
                    char charAt2 = str.charAt(i2);
                    switch (charAt2) {
                        case '\b':
                            stringBuffer.append("\\b");
                            break;
                        case '\t':
                            stringBuffer.append("\\t");
                            break;
                        case '\n':
                            stringBuffer.append("\\n");
                            break;
                        case '\f':
                            stringBuffer.append("\\f");
                            break;
                        case '\r':
                            stringBuffer.append("\\r");
                            break;
                        case '\"':
                            stringBuffer.append("\\\"");
                            break;
                        case '\\':
                            stringBuffer.append("\\\\");
                            break;
                        default:
                            stringBuffer.append(charAt2);
                            break;
                    }
                    i2++;
                }
                stringBuffer.append('\"');
                return;
            }
            stringBuffer.append(str);
        }
    }

    public static String unquote(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() < 2) {
            return str;
        }
        char charAt = str.charAt(0);
        if (charAt != str.charAt(str.length() - 1)) {
            return str;
        }
        if (charAt != '\"' && charAt != '\'') {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length() - 2);
        synchronized (stringBuffer) {
            int i = 0;
            int i2 = 1;
            while (i2 < str.length() - 1) {
                try {
                    char charAt2 = str.charAt(i2);
                    if (i != 0) {
                        switch (charAt2) {
                            case 'b':
                                stringBuffer.append('\b');
                                i = 0;
                                break;
                            case 'f':
                                stringBuffer.append('\f');
                                i = 0;
                                break;
                            case 'n':
                                stringBuffer.append('\n');
                                i = 0;
                                break;
                            case 'r':
                                stringBuffer.append('\r');
                                i = 0;
                                break;
                            case 't':
                                stringBuffer.append('\t');
                                i = 0;
                                break;
                            case 'u':
                                i = i2 + 1;
                                try {
                                    byte convertHexDigit = TypeUtil.convertHexDigit((byte) str.charAt(i2));
                                    i2 = i + 1;
                                    byte convertHexDigit2 = TypeUtil.convertHexDigit((byte) str.charAt(i));
                                    int i3 = i2 + 1;
                                    byte convertHexDigit3 = TypeUtil.convertHexDigit((byte) str.charAt(i2));
                                    i2 = i3 + 1;
                                    stringBuffer.append((char) ((((convertHexDigit2 << 16) + (convertHexDigit << 24)) + (convertHexDigit3 << 8)) + TypeUtil.convertHexDigit((byte) str.charAt(i3))));
                                    i = 0;
                                    break;
                                } catch (Throwable th) {
                                    Throwable th2 = th;
                                    throw th2;
                                }
                            default:
                                stringBuffer.append(charAt2);
                                i = 0;
                                break;
                        }
                    } else if (charAt2 == '\\') {
                        i = 1;
                    } else {
                        stringBuffer.append(charAt2);
                    }
                    i2++;
                } catch (Throwable th3) {
                    th2 = th3;
                }
            }
            str = stringBuffer.toString();
            return str;
        }
    }

    public int countTokens() {
        return -1;
    }

    public boolean getDouble() {
        return this._double;
    }

    public boolean getSingle() {
        return this._single;
    }

    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    public boolean hasMoreTokens() {
        if (this._hasToken) {
            return true;
        }
        this._lastStart = this._i;
        boolean z = false;
        boolean z2 = false;
        while (this._i < this._string.length()) {
            String str = this._string;
            int i = this._i;
            this._i = i + 1;
            char charAt = str.charAt(i);
            switch (z2) {
                case false:
                    if (this._delim.indexOf(charAt) < 0) {
                        if (charAt != '\'' || !this._single) {
                            if (charAt != '\"' || !this._double) {
                                this._token.append(charAt);
                                this._hasToken = true;
                                z2 = true;
                                break;
                            }
                            if (this._returnQuotes) {
                                this._token.append(charAt);
                            }
                            z2 = true;
                            break;
                        }
                        if (this._returnQuotes) {
                            this._token.append(charAt);
                        }
                        z2 = true;
                        break;
                    } else if (!this._returnDelimiters) {
                        break;
                    } else {
                        this._token.append(charAt);
                        this._hasToken = true;
                        return true;
                    }
                    break;
                case true:
                    this._hasToken = true;
                    if (this._delim.indexOf(charAt) < 0) {
                        if (charAt != '\'' || !this._single) {
                            if (charAt != '\"' || !this._double) {
                                this._token.append(charAt);
                                break;
                            }
                            if (this._returnQuotes) {
                                this._token.append(charAt);
                            }
                            z2 = true;
                            break;
                        }
                        if (this._returnQuotes) {
                            this._token.append(charAt);
                        }
                        z2 = true;
                        break;
                    }
                    if (this._returnDelimiters) {
                        this._i--;
                    }
                    return this._hasToken;
                    break;
                case true:
                    this._hasToken = true;
                    if (!z) {
                        if (charAt != '\'') {
                            if (charAt != '\\') {
                                this._token.append(charAt);
                                break;
                            }
                            if (this._returnQuotes) {
                                this._token.append(charAt);
                            }
                            z = true;
                            break;
                        }
                        if (this._returnQuotes) {
                            this._token.append(charAt);
                        }
                        z2 = true;
                        break;
                    }
                    this._token.append(charAt);
                    z = false;
                    break;
                case true:
                    this._hasToken = true;
                    if (!z) {
                        if (charAt != '\"') {
                            if (charAt != '\\') {
                                this._token.append(charAt);
                                break;
                            }
                            if (this._returnQuotes) {
                                this._token.append(charAt);
                            }
                            z = true;
                            break;
                        }
                        if (this._returnQuotes) {
                            this._token.append(charAt);
                        }
                        z2 = true;
                        break;
                    }
                    this._token.append(charAt);
                    z = false;
                    break;
                default:
                    break;
            }
        }
        return this._hasToken;
    }

    public Object nextElement() throws NoSuchElementException {
        return nextToken();
    }

    public String nextToken() throws NoSuchElementException {
        if (!hasMoreTokens() || this._token == null) {
            throw new NoSuchElementException();
        }
        String stringBuffer = this._token.toString();
        this._token.setLength(0);
        this._hasToken = false;
        return stringBuffer;
    }

    public String nextToken(String str) throws NoSuchElementException {
        this._delim = str;
        this._i = this._lastStart;
        this._token.setLength(0);
        this._hasToken = false;
        return nextToken();
    }

    public void setDouble(boolean z) {
        this._double = z;
    }

    public void setSingle(boolean z) {
        this._single = z;
    }
}
