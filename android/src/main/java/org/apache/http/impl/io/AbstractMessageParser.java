package org.apache.http.impl.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class AbstractMessageParser<T extends HttpMessage> implements HttpMessageParser<T> {
    private static final int HEADERS = 1;
    private static final int HEAD_LINE = 0;
    private final List<CharArrayBuffer> headerLines;
    protected final LineParser lineParser;
    private final int maxHeaderCount;
    private final int maxLineLen;
    private T message;
    private final SessionInputBuffer sessionBuffer;
    private int state;

    public AbstractMessageParser(SessionInputBuffer sessionInputBuffer, LineParser lineParser, HttpParams httpParams) {
        if (sessionInputBuffer == null) {
            throw new IllegalArgumentException("Session input buffer may not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.sessionBuffer = sessionInputBuffer;
            this.maxHeaderCount = httpParams.getIntParameter(CoreConnectionPNames.MAX_HEADER_COUNT, -1);
            this.maxLineLen = httpParams.getIntParameter(CoreConnectionPNames.MAX_LINE_LENGTH, -1);
            if (lineParser == null) {
                lineParser = BasicLineParser.DEFAULT;
            }
            this.lineParser = lineParser;
            this.headerLines = new ArrayList();
            this.state = 0;
        }
    }

    public static Header[] parseHeaders(SessionInputBuffer sessionInputBuffer, int i, int i2, LineParser lineParser) throws HttpException, IOException {
        if (lineParser == null) {
            lineParser = BasicLineParser.DEFAULT;
        }
        return parseHeaders(sessionInputBuffer, i, i2, lineParser, new ArrayList());
    }

    public static Header[] parseHeaders(SessionInputBuffer sessionInputBuffer, int i, int i2, LineParser lineParser, List<CharArrayBuffer> list) throws HttpException, IOException {
        int i3 = 0;
        if (sessionInputBuffer == null) {
            throw new IllegalArgumentException("Session input buffer may not be null");
        } else if (lineParser == null) {
            throw new IllegalArgumentException("Line parser may not be null");
        } else if (list == null) {
            throw new IllegalArgumentException("Header line list may not be null");
        } else {
            Header[] headerArr;
            CharArrayBuffer charArrayBuffer = null;
            CharArrayBuffer charArrayBuffer2 = null;
            while (true) {
                if (charArrayBuffer2 == null) {
                    charArrayBuffer2 = new CharArrayBuffer(64);
                } else {
                    charArrayBuffer2.clear();
                }
                if (sessionInputBuffer.readLine(charArrayBuffer2) == -1 || charArrayBuffer2.length() < 1) {
                    headerArr = new Header[list.size()];
                } else {
                    CharArrayBuffer charArrayBuffer3;
                    if ((charArrayBuffer2.charAt(0) == ' ' || charArrayBuffer2.charAt(0) == '\t') && charArrayBuffer != null) {
                        int i4 = 0;
                        while (i4 < charArrayBuffer2.length()) {
                            char charAt = charArrayBuffer2.charAt(i4);
                            if (charAt != ' ' && charAt != '\t') {
                                break;
                            }
                            i4++;
                        }
                        if (i2 <= 0 || ((charArrayBuffer.length() + 1) + charArrayBuffer2.length()) - i4 <= i2) {
                            charArrayBuffer.append(' ');
                            charArrayBuffer.append(charArrayBuffer2, i4, charArrayBuffer2.length() - i4);
                            charArrayBuffer3 = charArrayBuffer2;
                            charArrayBuffer2 = charArrayBuffer;
                        } else {
                            throw new IOException("Maximum line length limit exceeded");
                        }
                    }
                    list.add(charArrayBuffer2);
                    charArrayBuffer3 = null;
                    if (i <= 0 || list.size() < i) {
                        charArrayBuffer = charArrayBuffer2;
                        charArrayBuffer2 = charArrayBuffer3;
                    } else {
                        throw new IOException("Maximum header count exceeded");
                    }
                }
            }
            headerArr = new Header[list.size()];
            while (i3 < list.size()) {
                try {
                    headerArr[i3] = lineParser.parseHeader((CharArrayBuffer) list.get(i3));
                    i3++;
                } catch (ParseException e) {
                    throw new ProtocolException(e.getMessage());
                }
            }
            return headerArr;
        }
    }

    public T parse() throws IOException, HttpException {
        switch (this.state) {
            case 0:
                try {
                    this.message = parseHead(this.sessionBuffer);
                    this.state = 1;
                    break;
                } catch (Throwable e) {
                    throw new ProtocolException(e.getMessage(), e);
                }
            case 1:
                break;
            default:
                throw new IllegalStateException("Inconsistent parser state");
        }
        this.message.setHeaders(parseHeaders(this.sessionBuffer, this.maxHeaderCount, this.maxLineLen, this.lineParser, this.headerLines));
        T t = this.message;
        this.message = null;
        this.headerLines.clear();
        this.state = 0;
        return t;
    }

    protected abstract T parseHead(SessionInputBuffer sessionInputBuffer) throws IOException, HttpException, ParseException;
}
