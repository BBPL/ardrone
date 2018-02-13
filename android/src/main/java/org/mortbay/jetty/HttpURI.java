package org.mortbay.jetty;

import java.io.UnsupportedEncodingException;
import org.mortbay.util.MultiMap;
import org.mortbay.util.StringUtil;
import org.mortbay.util.TypeUtil;
import org.mortbay.util.UrlEncoded;
import org.mortbay.util.Utf8StringBuffer;

public class HttpURI {
    private static final int ASTERISK = 10;
    private static final int AUTH = 4;
    private static final int AUTH_OR_PATH = 1;
    private static final int IPV6 = 5;
    private static final int PARAM = 8;
    private static final int PATH = 7;
    private static final int PORT = 6;
    private static final int QUERY = 9;
    private static final int SCHEME_OR_PATH = 2;
    private static final int START = 0;
    private static byte[] __empty = new byte[0];
    int _authority;
    int _end;
    int _fragment;
    int _host;
    int _param;
    boolean _partial = false;
    int _path;
    int _port;
    int _query;
    byte[] _raw = __empty;
    String _rawString;
    int _scheme;
    Utf8StringBuffer _utf8b = new Utf8StringBuffer(64);

    public HttpURI(String str) {
        this._rawString = str;
        byte[] bytes = str.getBytes();
        parse(bytes, 0, bytes.length);
    }

    public HttpURI(boolean z) {
        this._partial = z;
    }

    public HttpURI(byte[] bArr, int i, int i2) {
        parse2(bArr, i, i2);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parse2(byte[] r9, int r10, int r11) {
        /*
        r8 = this;
        r8._raw = r9;
        r0 = 0;
        r1 = r10 + r11;
        r8._end = r1;
        r8._scheme = r10;
        r8._authority = r10;
        r8._host = r10;
        r8._port = r10;
        r8._path = r10;
        r1 = r8._end;
        r8._param = r1;
        r1 = r8._end;
        r8._query = r1;
        r1 = r8._end;
        r8._fragment = r1;
        r1 = r0;
        r2 = r10;
        r0 = r10;
    L_0x0020:
        r3 = r2;
    L_0x0021:
        r2 = r10 + r11;
        if (r3 >= r2) goto L_0x01b3;
    L_0x0025:
        r2 = r8._raw;
        r2 = r2[r3];
        r2 = r2 & 255;
        r4 = (char) r2;
        r2 = r3 + 1;
        switch(r1) {
            case 0: goto L_0x0033;
            case 1: goto L_0x0072;
            case 2: goto L_0x00a6;
            case 3: goto L_0x0031;
            case 4: goto L_0x0122;
            case 5: goto L_0x013a;
            case 6: goto L_0x0163;
            case 7: goto L_0x0178;
            case 8: goto L_0x0192;
            case 9: goto L_0x01a3;
            case 10: goto L_0x01ab;
            default: goto L_0x0031;
        };
    L_0x0031:
        r3 = r2;
        goto L_0x0021;
    L_0x0033:
        switch(r4) {
            case 35: goto L_0x0054;
            case 42: goto L_0x005d;
            case 47: goto L_0x0040;
            case 59: goto L_0x0044;
            case 63: goto L_0x004b;
            default: goto L_0x0036;
        };
    L_0x0036:
        r0 = java.lang.Character.isLetterOrDigit(r4);
        if (r0 == 0) goto L_0x0064;
    L_0x003c:
        r1 = 2;
        r0 = r3;
        r3 = r2;
        goto L_0x0021;
    L_0x0040:
        r1 = 1;
        r0 = r3;
        r3 = r2;
        goto L_0x0021;
    L_0x0044:
        r8._param = r3;
        r1 = 8;
        r0 = r3;
        r3 = r2;
        goto L_0x0021;
    L_0x004b:
        r8._param = r3;
        r8._query = r3;
        r1 = 9;
        r0 = r3;
        r3 = r2;
        goto L_0x0021;
    L_0x0054:
        r8._param = r3;
        r8._query = r3;
        r8._fragment = r3;
        r0 = r3;
        r3 = r2;
        goto L_0x0021;
    L_0x005d:
        r8._path = r3;
        r1 = 10;
        r0 = r3;
        r3 = r2;
        goto L_0x0021;
    L_0x0064:
        r0 = new java.lang.IllegalArgumentException;
        r1 = r8._raw;
        r2 = org.mortbay.util.URIUtil.__CHARSET;
        r1 = org.mortbay.util.StringUtil.toString(r1, r10, r11, r2);
        r0.<init>(r1);
        throw r0;
    L_0x0072:
        r1 = r8._partial;
        if (r1 != 0) goto L_0x007c;
    L_0x0076:
        r1 = r8._scheme;
        r3 = r8._authority;
        if (r1 == r3) goto L_0x008d;
    L_0x007c:
        r1 = 47;
        if (r4 != r1) goto L_0x008d;
    L_0x0080:
        r8._host = r2;
        r1 = r8._end;
        r8._port = r1;
        r1 = r8._end;
        r8._path = r1;
        r1 = 4;
        r3 = r2;
        goto L_0x0021;
    L_0x008d:
        r1 = 59;
        if (r4 == r1) goto L_0x0099;
    L_0x0091:
        r1 = 63;
        if (r4 == r1) goto L_0x0099;
    L_0x0095:
        r1 = 35;
        if (r4 != r1) goto L_0x009e;
    L_0x0099:
        r1 = 7;
        r2 = r2 + -1;
        r3 = r2;
        goto L_0x0021;
    L_0x009e:
        r8._host = r0;
        r8._port = r0;
        r1 = 7;
        r3 = r2;
        goto L_0x0021;
    L_0x00a6:
        r5 = 6;
        if (r11 <= r5) goto L_0x00c0;
    L_0x00a9:
        r5 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        if (r4 != r5) goto L_0x00c0;
    L_0x00ad:
        r5 = r8._raw;
        r6 = r10 + 3;
        r5 = r5[r6];
        r6 = 58;
        if (r5 != r6) goto L_0x00c6;
    L_0x00b7:
        r3 = r10 + 3;
        r2 = 58;
        r4 = r10 + 4;
        r7 = r2;
        r2 = r4;
        r4 = r7;
    L_0x00c0:
        switch(r4) {
            case 35: goto L_0x011b;
            case 47: goto L_0x010d;
            case 58: goto L_0x00ee;
            case 59: goto L_0x010f;
            case 63: goto L_0x0114;
            default: goto L_0x00c3;
        };
    L_0x00c3:
        r3 = r2;
        goto L_0x0021;
    L_0x00c6:
        r5 = r8._raw;
        r6 = r10 + 4;
        r5 = r5[r6];
        r6 = 58;
        if (r5 != r6) goto L_0x00da;
    L_0x00d0:
        r3 = r10 + 4;
        r2 = 58;
        r4 = r10 + 5;
        r7 = r2;
        r2 = r4;
        r4 = r7;
        goto L_0x00c0;
    L_0x00da:
        r5 = r8._raw;
        r6 = r10 + 5;
        r5 = r5[r6];
        r6 = 58;
        if (r5 != r6) goto L_0x00c0;
    L_0x00e4:
        r3 = r10 + 5;
        r2 = 58;
        r4 = r10 + 6;
        r7 = r2;
        r2 = r4;
        r4 = r7;
        goto L_0x00c0;
    L_0x00ee:
        r0 = r2 + 1;
        r8._authority = r2;
        r8._path = r2;
        r1 = r8._raw;
        r1 = r1[r0];
        r1 = r1 & 255;
        r1 = (char) r1;
        r3 = 47;
        if (r1 != r3) goto L_0x0104;
    L_0x00ff:
        r1 = 1;
        r7 = r2;
        r2 = r0;
        r0 = r7;
        goto L_0x00c3;
    L_0x0104:
        r8._host = r2;
        r8._port = r2;
        r1 = 7;
        r7 = r2;
        r2 = r0;
        r0 = r7;
        goto L_0x00c3;
    L_0x010d:
        r1 = 7;
        goto L_0x00c3;
    L_0x010f:
        r8._param = r3;
        r1 = 8;
        goto L_0x00c3;
    L_0x0114:
        r8._param = r3;
        r8._query = r3;
        r1 = 9;
        goto L_0x00c3;
    L_0x011b:
        r8._param = r3;
        r8._query = r3;
        r8._fragment = r3;
        goto L_0x00c3;
    L_0x0122:
        switch(r4) {
            case 47: goto L_0x0128;
            case 58: goto L_0x0134;
            case 64: goto L_0x0131;
            case 91: goto L_0x0138;
            default: goto L_0x0125;
        };
    L_0x0125:
        r3 = r2;
        goto L_0x0021;
    L_0x0128:
        r8._path = r3;
        r0 = r8._path;
        r8._port = r0;
        r1 = 7;
        r0 = r3;
        goto L_0x0125;
    L_0x0131:
        r8._host = r2;
        goto L_0x0125;
    L_0x0134:
        r8._port = r3;
        r1 = 6;
        goto L_0x0125;
    L_0x0138:
        r1 = 5;
        goto L_0x0125;
    L_0x013a:
        switch(r4) {
            case 47: goto L_0x0140;
            case 93: goto L_0x0161;
            default: goto L_0x013d;
        };
    L_0x013d:
        r3 = r2;
        goto L_0x0021;
    L_0x0140:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuffer;
        r1.<init>();
        r2 = "No closing ']' for ";
        r1 = r1.append(r2);
        r2 = r8._raw;
        r3 = org.mortbay.util.URIUtil.__CHARSET;
        r2 = org.mortbay.util.StringUtil.toString(r2, r10, r11, r3);
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0161:
        r1 = 4;
        goto L_0x013d;
    L_0x0163:
        r5 = 47;
        if (r4 != r5) goto L_0x0020;
    L_0x0167:
        r8._path = r3;
        r0 = r8._port;
        r1 = r8._authority;
        if (r0 > r1) goto L_0x0173;
    L_0x016f:
        r0 = r8._path;
        r8._port = r0;
    L_0x0173:
        r1 = 7;
        r0 = r3;
        r3 = r2;
        goto L_0x0021;
    L_0x0178:
        switch(r4) {
            case 35: goto L_0x018a;
            case 59: goto L_0x017e;
            case 63: goto L_0x0183;
            default: goto L_0x017b;
        };
    L_0x017b:
        r3 = r2;
        goto L_0x0021;
    L_0x017e:
        r8._param = r3;
        r1 = 8;
        goto L_0x017b;
    L_0x0183:
        r8._param = r3;
        r8._query = r3;
        r1 = 9;
        goto L_0x017b;
    L_0x018a:
        r8._param = r3;
        r8._query = r3;
        r8._fragment = r3;
        goto L_0x0031;
    L_0x0192:
        switch(r4) {
            case 35: goto L_0x019d;
            case 63: goto L_0x0198;
            default: goto L_0x0195;
        };
    L_0x0195:
        r3 = r2;
        goto L_0x0021;
    L_0x0198:
        r8._query = r3;
        r1 = 9;
        goto L_0x0195;
    L_0x019d:
        r8._query = r3;
        r8._fragment = r3;
        goto L_0x0031;
    L_0x01a3:
        r5 = 35;
        if (r4 != r5) goto L_0x0020;
    L_0x01a7:
        r8._fragment = r3;
        goto L_0x0031;
    L_0x01ab:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "only '*'";
        r0.<init>(r1);
        throw r0;
    L_0x01b3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.HttpURI.parse2(byte[], int, int):void");
    }

    private String toUtf8String(int i, int i2) {
        this._utf8b.reset();
        this._utf8b.append(this._raw, i, i2);
        return this._utf8b.toString();
    }

    public void clear() {
        this._end = 0;
        this._fragment = 0;
        this._query = 0;
        this._param = 0;
        this._path = 0;
        this._port = 0;
        this._host = 0;
        this._authority = 0;
        this._scheme = 0;
        this._raw = __empty;
        this._rawString = HttpVersions.HTTP_0_9;
    }

    public void decodeQueryTo(MultiMap multiMap) {
        if (this._query != this._fragment) {
            this._utf8b.reset();
            UrlEncoded.decodeUtf8To(this._raw, this._query + 1, (this._fragment - this._query) - 1, multiMap, this._utf8b);
        }
    }

    public void decodeQueryTo(MultiMap multiMap, String str) throws UnsupportedEncodingException {
        if (this._query != this._fragment) {
            if (str == null || StringUtil.isUTF8(str)) {
                UrlEncoded.decodeUtf8To(this._raw, this._query + 1, (this._fragment - this._query) - 1, multiMap);
            } else {
                UrlEncoded.decodeTo(toUtf8String(this._query + 1, (this._fragment - this._query) - 1), multiMap, str);
            }
        }
    }

    public String getAuthority() {
        return this._authority == this._path ? null : toUtf8String(this._authority, this._path - this._authority);
    }

    public String getCompletePath() {
        return this._path == this._end ? null : toUtf8String(this._path, this._end - this._path);
    }

    public String getDecodedPath() {
        byte[] bArr = null;
        if (this._path == this._param) {
            return null;
        }
        int i = this._param - this._path;
        int i2 = this._path;
        int i3 = 0;
        while (i2 < this._param) {
            byte b = this._raw[i2];
            if (b == (byte) 37 && i2 + 2 < this._param) {
                b = (byte) (TypeUtil.parseInt(this._raw, i2 + 1, 2, 16) & 255);
                i2 += 2;
            } else if (bArr == null) {
                i3++;
                i2++;
            }
            if (bArr == null) {
                bArr = new byte[i];
                for (int i4 = 0; i4 < i3; i4++) {
                    bArr[i4] = this._raw[this._path + i4];
                }
            }
            bArr[i3] = b;
            i3++;
            i2++;
        }
        if (bArr == null) {
            return toUtf8String(this._path, i);
        }
        this._utf8b.reset();
        this._utf8b.append(bArr, 0, i3);
        return this._utf8b.toString();
    }

    public String getFragment() {
        return this._fragment == this._end ? null : toUtf8String(this._fragment + 1, (this._end - this._fragment) - 1);
    }

    public String getHost() {
        return this._host == this._port ? null : toUtf8String(this._host, this._port - this._host);
    }

    public String getParam() {
        return this._param == this._query ? null : toUtf8String(this._param + 1, (this._query - this._param) - 1);
    }

    public String getPath() {
        return this._path == this._param ? null : toUtf8String(this._path, this._param - this._path);
    }

    public String getPathAndParam() {
        return this._path == this._query ? null : toUtf8String(this._path, this._query - this._path);
    }

    public int getPort() {
        return this._port == this._path ? -1 : TypeUtil.parseInt(this._raw, this._port + 1, (this._path - this._port) - 1, 10);
    }

    public String getQuery() {
        return this._query == this._fragment ? null : toUtf8String(this._query + 1, (this._fragment - this._query) - 1);
    }

    public String getQuery(String str) {
        return this._query == this._fragment ? null : StringUtil.toString(this._raw, this._query + 1, (this._fragment - this._query) - 1, str);
    }

    public String getScheme() {
        if (this._scheme == this._authority) {
            return null;
        }
        int i = this._authority - this._scheme;
        return (i == 5 && this._raw[this._scheme] == (byte) 104 && this._raw[this._scheme + 1] == (byte) 116 && this._raw[this._scheme + 2] == (byte) 116 && this._raw[this._scheme + 3] == (byte) 112) ? "http" : (i == 6 && this._raw[this._scheme] == (byte) 104 && this._raw[this._scheme + 1] == (byte) 116 && this._raw[this._scheme + 2] == (byte) 116 && this._raw[this._scheme + 3] == (byte) 112 && this._raw[this._scheme + 4] == (byte) 115) ? "https" : toUtf8String(this._scheme, (this._authority - this._scheme) - 1);
    }

    public boolean hasQuery() {
        return this._fragment > this._query;
    }

    public void parse(String str) {
        byte[] bytes = str.getBytes();
        parse2(bytes, 0, bytes.length);
        this._rawString = str;
    }

    public void parse(byte[] bArr, int i, int i2) {
        this._rawString = null;
        parse2(bArr, i, i2);
    }

    public String toString() {
        if (this._rawString == null) {
            this._rawString = toUtf8String(this._scheme, this._end - this._scheme);
        }
        return this._rawString;
    }

    public void writeTo(Utf8StringBuffer utf8StringBuffer) {
        utf8StringBuffer.append(this._raw, this._scheme, this._end - this._scheme);
    }
}
