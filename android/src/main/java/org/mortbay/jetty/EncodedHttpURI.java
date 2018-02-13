package org.mortbay.jetty;

import java.io.UnsupportedEncodingException;
import org.mortbay.util.MultiMap;
import org.mortbay.util.StringUtil;
import org.mortbay.util.TypeUtil;
import org.mortbay.util.URIUtil;
import org.mortbay.util.UrlEncoded;
import org.mortbay.util.Utf8StringBuffer;

public class EncodedHttpURI extends HttpURI {
    private String _encoding;

    public EncodedHttpURI(String str) {
        this._encoding = str;
    }

    public void decodeQueryTo(MultiMap multiMap) {
        if (this._query != this._fragment) {
            UrlEncoded.decodeTo(StringUtil.toString(this._raw, this._query + 1, (this._fragment - this._query) - 1, this._encoding), multiMap, this._encoding);
        }
    }

    public void decodeQueryTo(MultiMap multiMap, String str) throws UnsupportedEncodingException {
        if (this._query != this._fragment) {
            if (str == null) {
                str = this._encoding;
            }
            UrlEncoded.decodeTo(StringUtil.toString(this._raw, this._query + 1, (this._fragment - this._query) - 1, str), multiMap, str);
        }
    }

    public String getAuthority() {
        return this._authority == this._path ? null : StringUtil.toString(this._raw, this._authority, this._path - this._authority, this._encoding);
    }

    public String getCompletePath() {
        return this._path == this._end ? null : StringUtil.toString(this._raw, this._path, this._end - this._path, this._encoding);
    }

    public String getDecodedPath() {
        return this._path == this._param ? null : URIUtil.decodePath(this._raw, this._path, this._param - this._path);
    }

    public String getFragment() {
        return this._fragment == this._end ? null : StringUtil.toString(this._raw, this._fragment + 1, (this._end - this._fragment) - 1, this._encoding);
    }

    public String getHost() {
        return this._host == this._port ? null : StringUtil.toString(this._raw, this._host, this._port - this._host, this._encoding);
    }

    public String getParam() {
        return this._param == this._query ? null : StringUtil.toString(this._raw, this._param + 1, (this._query - this._param) - 1, this._encoding);
    }

    public String getPath() {
        return this._path == this._param ? null : StringUtil.toString(this._raw, this._path, this._param - this._path, this._encoding);
    }

    public String getPathAndParam() {
        return this._path == this._query ? null : StringUtil.toString(this._raw, this._path, this._query - this._path, this._encoding);
    }

    public int getPort() {
        return this._port == this._path ? -1 : TypeUtil.parseInt(this._raw, this._port + 1, (this._path - this._port) - 1, 10);
    }

    public String getQuery() {
        return this._query == this._fragment ? null : StringUtil.toString(this._raw, this._query + 1, (this._fragment - this._query) - 1, this._encoding);
    }

    public String getScheme() {
        if (this._scheme == this._authority) {
            return null;
        }
        int i = this._authority - this._scheme;
        return (i == 5 && this._raw[this._scheme] == (byte) 104 && this._raw[this._scheme + 1] == (byte) 116 && this._raw[this._scheme + 2] == (byte) 116 && this._raw[this._scheme + 3] == (byte) 112) ? "http" : (i == 6 && this._raw[this._scheme] == (byte) 104 && this._raw[this._scheme + 1] == (byte) 116 && this._raw[this._scheme + 2] == (byte) 116 && this._raw[this._scheme + 3] == (byte) 112 && this._raw[this._scheme + 4] == (byte) 115) ? "https" : StringUtil.toString(this._raw, this._scheme, (this._authority - this._scheme) - 1, this._encoding);
    }

    public boolean hasQuery() {
        return this._fragment > this._query;
    }

    public String toString() {
        if (this._rawString == null) {
            this._rawString = StringUtil.toString(this._raw, this._scheme, this._end - this._scheme, this._encoding);
        }
        return this._rawString;
    }

    public void writeTo(Utf8StringBuffer utf8StringBuffer) {
        utf8StringBuffer.getStringBuffer().append(toString());
    }
}
