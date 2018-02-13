package org.mortbay.jetty;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import org.mortbay.io.Buffer;
import org.mortbay.io.BufferCache.CachedBuffer;
import org.mortbay.io.Buffers;
import org.mortbay.io.EndPoint;
import org.mortbay.io.View;
import org.mortbay.io.View.CaseInsensitive;
import org.mortbay.log.Log;

public class HttpParser implements Parser {
    public static final int STATE_CHUNK = 6;
    public static final int STATE_CHUNKED_CONTENT = 3;
    public static final int STATE_CHUNK_PARAMS = 5;
    public static final int STATE_CHUNK_SIZE = 4;
    public static final int STATE_CONTENT = 2;
    public static final int STATE_END = 0;
    public static final int STATE_END0 = -8;
    public static final int STATE_END1 = -7;
    public static final int STATE_EOF_CONTENT = 1;
    public static final int STATE_FIELD0 = -12;
    public static final int STATE_FIELD1 = -10;
    public static final int STATE_FIELD2 = -6;
    public static final int STATE_HEADER = -5;
    public static final int STATE_HEADER_IN_NAME = -3;
    public static final int STATE_HEADER_IN_VALUE = -1;
    public static final int STATE_HEADER_NAME = -4;
    public static final int STATE_HEADER_VALUE = -2;
    public static final int STATE_SPACE1 = -11;
    public static final int STATE_SPACE2 = -9;
    public static final int STATE_START = -13;
    private Buffer _body;
    private Buffer _buffer;
    private Buffers _buffers;
    private CachedBuffer _cached;
    protected int _chunkLength;
    protected int _chunkPosition;
    private int _contentBufferSize;
    protected long _contentLength;
    protected long _contentPosition;
    private View _contentView = new View();
    private EndPoint _endp;
    protected byte _eol;
    private boolean _forceContentBuffer;
    private EventHandler _handler;
    private Buffer _header;
    private int _headerBufferSize;
    private Input _input;
    protected int _length;
    private String _multiLineValue;
    private int _responseStatus;
    protected int _state = -13;
    private CaseInsensitive _tok0;
    private CaseInsensitive _tok1;

    public static abstract class EventHandler {
        public abstract void content(Buffer buffer) throws IOException;

        public void headerComplete() throws IOException {
        }

        public void messageComplete(long j) throws IOException {
        }

        public void parsedHeader(Buffer buffer, Buffer buffer2) throws IOException {
        }

        public abstract void startRequest(Buffer buffer, Buffer buffer2, Buffer buffer3) throws IOException;

        public abstract void startResponse(Buffer buffer, int i, Buffer buffer2) throws IOException;
    }

    public static class Input extends ServletInputStream {
        protected Buffer _content = HttpParser.access$100(this._parser);
        protected EndPoint _endp;
        protected long _maxIdleTime;
        protected HttpParser _parser;

        public Input(HttpParser httpParser, long j) {
            this._parser = httpParser;
            this._endp = HttpParser.access$000(httpParser);
            this._maxIdleTime = j;
            HttpParser.access$202(this._parser, this);
        }

        private boolean blockForContent() throws IOException {
            if (this._content.length() <= 0) {
                if (this._parser.getState() <= 0) {
                    return false;
                }
                if (this._endp == null) {
                    this._parser.parseNext();
                } else if (this._endp.isBlocking()) {
                    try {
                        this._parser.parseNext();
                        while (this._content.length() == 0 && !this._parser.isState(0) && this._endp.isOpen()) {
                            this._parser.parseNext();
                        }
                    } catch (IOException e) {
                        this._endp.close();
                        throw e;
                    }
                } else {
                    this._parser.parseNext();
                    while (this._content.length() == 0 && !this._parser.isState(0) && this._endp.isOpen()) {
                        if (!this._endp.isBufferingInput() || this._parser.parseNext() <= 0) {
                            if (this._endp.blockReadable(this._maxIdleTime)) {
                                this._parser.parseNext();
                            } else {
                                this._endp.close();
                                throw new EofException("timeout");
                            }
                        }
                    }
                }
                if (this._content.length() <= 0) {
                    return false;
                }
            }
            return true;
        }

        public int available() throws IOException {
            if (this._content != null && this._content.length() > 0) {
                return this._content.length();
            }
            if (!this._endp.isBlocking()) {
                this._parser.parseNext();
            }
            return this._content == null ? 0 : this._content.length();
        }

        public int read() throws IOException {
            return blockForContent() ? this._content.get() & 255 : -1;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            return blockForContent() ? this._content.get(bArr, i, i2) : -1;
        }
    }

    public HttpParser(Buffer buffer, EventHandler eventHandler) {
        this._header = buffer;
        this._buffer = buffer;
        this._handler = eventHandler;
        if (buffer != null) {
            this._tok0 = new CaseInsensitive(buffer);
            this._tok1 = new CaseInsensitive(buffer);
            this._tok0.setPutIndex(this._tok0.getIndex());
            this._tok1.setPutIndex(this._tok1.getIndex());
        }
    }

    public HttpParser(Buffers buffers, EndPoint endPoint, EventHandler eventHandler, int i, int i2) {
        this._buffers = buffers;
        this._endp = endPoint;
        this._handler = eventHandler;
        this._headerBufferSize = i;
        this._contentBufferSize = i2;
    }

    static EndPoint access$000(HttpParser httpParser) {
        return httpParser._endp;
    }

    static View access$100(HttpParser httpParser) {
        return httpParser._contentView;
    }

    static Input access$202(HttpParser httpParser, Input input) {
        httpParser._input = input;
        return input;
    }

    public long fill() throws IOException {
        if (this._buffer == null) {
            Buffer headerBuffer = getHeaderBuffer();
            this._header = headerBuffer;
            this._buffer = headerBuffer;
            this._tok0 = new CaseInsensitive(this._buffer);
            this._tok1 = new CaseInsensitive(this._buffer);
        }
        if (!(this._body == null || this._buffer == this._body)) {
            this._buffer = this._body;
        }
        if (this._buffer == this._body) {
            this._buffer.compact();
        }
        if (this._buffer.space() == 0) {
            throw new HttpException(413, new StringBuffer().append("FULL ").append(this._buffer == this._body ? "body" : "head").toString());
        }
        int i = -1;
        if (this._endp != null) {
            try {
                i = this._endp.fill(this._buffer);
            } catch (Throwable e) {
                Log.debug(e);
                reset(true);
                throw (e instanceof EofException ? e : new EofException(e));
            }
        }
        return (long) i;
    }

    public Buffer getBodyBuffer() {
        return this._body;
    }

    public long getContentLength() {
        return this._contentLength;
    }

    public long getContentRead() {
        return this._contentPosition;
    }

    public Buffer getHeaderBuffer() {
        if (this._header == null) {
            this._header = this._buffers.getBuffer(this._headerBufferSize);
        }
        return this._header;
    }

    public int getState() {
        return this._state;
    }

    public boolean inContentState() {
        return this._state > 0;
    }

    public boolean inHeaderState() {
        return this._state < 0;
    }

    public boolean isChunking() {
        return this._contentLength == -2;
    }

    public boolean isComplete() {
        return isState(0);
    }

    public boolean isIdle() {
        return isState(-13);
    }

    public boolean isMoreInBuffer() throws IOException {
        return (this._header != null && this._header.hasContent()) || (this._body != null && this._body.hasContent());
    }

    public boolean isState(int i) {
        return this._state == i;
    }

    public void parse() throws IOException {
        if (this._state == 0) {
            reset(false);
        }
        if (this._state != -13) {
            throw new IllegalStateException("!START");
        }
        while (this._state != 0) {
            parseNext();
        }
    }

    public long parseAvailable() throws IOException {
        long parseNext = parseNext();
        if (parseNext <= 0) {
            parseNext = 0;
        }
        while (!isComplete() && this._buffer != null && this._buffer.length() > 0) {
            long parseNext2 = parseNext();
            if (parseNext2 > 0) {
                parseNext += parseNext2;
            }
        }
        return parseNext;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long parseNext() throws java.io.IOException {
        /*
        r15 = this;
        r2 = -1;
        r0 = r15._state;
        if (r0 != 0) goto L_0x0009;
    L_0x0006:
        r0 = -1;
    L_0x0008:
        return r0;
    L_0x0009:
        r0 = r15._buffer;
        if (r0 != 0) goto L_0x0047;
    L_0x000d:
        r0 = r15._header;
        if (r0 != 0) goto L_0x001b;
    L_0x0011:
        r0 = r15._buffers;
        r1 = r15._headerBufferSize;
        r0 = r0.getBuffer(r1);
        r15._header = r0;
    L_0x001b:
        r0 = r15._header;
        r15._buffer = r0;
        r0 = new org.mortbay.io.View$CaseInsensitive;
        r1 = r15._header;
        r0.<init>(r1);
        r15._tok0 = r0;
        r0 = new org.mortbay.io.View$CaseInsensitive;
        r1 = r15._header;
        r0.<init>(r1);
        r15._tok1 = r0;
        r0 = r15._tok0;
        r1 = r15._tok0;
        r1 = r1.getIndex();
        r0.setPutIndex(r1);
        r0 = r15._tok1;
        r1 = r15._tok1;
        r1 = r1.getIndex();
        r0.setPutIndex(r1);
    L_0x0047:
        r0 = r15._state;
        r1 = 2;
        if (r0 != r1) goto L_0x0061;
    L_0x004c:
        r0 = r15._contentPosition;
        r4 = r15._contentLength;
        r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r0 != 0) goto L_0x0061;
    L_0x0054:
        r0 = 0;
        r15._state = r0;
        r0 = r15._handler;
        r2 = r15._contentPosition;
        r0.messageComplete(r2);
        r0 = -1;
        goto L_0x0008;
    L_0x0061:
        r0 = r15._buffer;
        r0 = r0.length();
        if (r0 != 0) goto L_0x0841;
    L_0x0069:
        r0 = -1;
        r1 = r15._body;
        if (r1 == 0) goto L_0x007e;
    L_0x006e:
        r1 = r15._buffer;
        r4 = r15._body;
        if (r1 == r4) goto L_0x007e;
    L_0x0074:
        r0 = r15._body;
        r15._buffer = r0;
        r0 = r15._buffer;
        r0 = r0.length();
    L_0x007e:
        r1 = r15._buffer;
        r1 = r1.markIndex();
        if (r1 != 0) goto L_0x009e;
    L_0x0086:
        r1 = r15._buffer;
        r1 = r1.putIndex();
        r4 = r15._buffer;
        r4 = r4.capacity();
        if (r1 != r4) goto L_0x009e;
    L_0x0094:
        r0 = new org.mortbay.jetty.HttpException;
        r1 = 413; // 0x19d float:5.79E-43 double:2.04E-321;
        r2 = "FULL";
        r0.<init>(r1, r2);
        throw r0;
    L_0x009e:
        r1 = 0;
        r4 = r15._endp;
        if (r4 == 0) goto L_0x083a;
    L_0x00a3:
        if (r0 > 0) goto L_0x083a;
    L_0x00a5:
        r0 = r15._buffer;
        r4 = r15._body;
        if (r0 != r4) goto L_0x00b0;
    L_0x00ab:
        r0 = r15._buffer;
        r0.compact();
    L_0x00b0:
        r0 = r15._buffer;
        r0 = r0.space();
        if (r0 != 0) goto L_0x00de;
    L_0x00b8:
        r0 = new java.lang.StringBuffer;
        r0.<init>();
        r1 = "FULL ";
        r1 = r0.append(r1);
        r0 = r15._buffer;
        r2 = r15._body;
        if (r0 != r2) goto L_0x00db;
    L_0x00c9:
        r0 = "body";
    L_0x00cb:
        r2 = new org.mortbay.jetty.HttpException;
        r3 = 413; // 0x19d float:5.79E-43 double:2.04E-321;
        r0 = r1.append(r0);
        r0 = r0.toString();
        r2.<init>(r3, r0);
        throw r2;
    L_0x00db:
        r0 = "head";
        goto L_0x00cb;
    L_0x00de:
        r4 = -1;
        r6 = 0;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 >= 0) goto L_0x00e8;
    L_0x00e6:
        r2 = 0;
    L_0x00e8:
        r0 = r15._endp;	 Catch:{ IOException -> 0x0134 }
        r4 = r15._buffer;	 Catch:{ IOException -> 0x0134 }
        r0 = r0.fill(r4);	 Catch:{ IOException -> 0x0134 }
        if (r0 <= 0) goto L_0x083a;
    L_0x00f2:
        r4 = (long) r0;
        r2 = r2 + r4;
        r12 = r1;
        r13 = r2;
        r2 = r12;
        r3 = r0;
        r0 = r13;
    L_0x00f9:
        if (r3 >= 0) goto L_0x0149;
    L_0x00fb:
        r3 = r15._state;
        r4 = 1;
        if (r3 != r4) goto L_0x013f;
    L_0x0100:
        r2 = r15._buffer;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x0128;
    L_0x0108:
        r2 = r15._buffer;
        r3 = r15._buffer;
        r3 = r3.length();
        r2 = r2.get(r3);
        r4 = r15._contentPosition;
        r3 = r2.length();
        r6 = (long) r3;
        r4 = r4 + r6;
        r15._contentPosition = r4;
        r3 = r15._contentView;
        r3.update(r2);
        r3 = r15._handler;
        r3.content(r2);
    L_0x0128:
        r2 = 0;
        r15._state = r2;
        r2 = r15._handler;
        r4 = r15._contentPosition;
        r2.messageComplete(r4);
        goto L_0x0008;
    L_0x0134:
        r0 = move-exception;
        org.mortbay.log.Log.debug(r0);
        r1 = -1;
        r12 = r0;
        r13 = r2;
        r2 = r12;
        r3 = r1;
        r0 = r13;
        goto L_0x00f9;
    L_0x013f:
        r0 = 1;
        r15.reset(r0);
        r0 = new org.mortbay.jetty.EofException;
        r0.<init>(r2);
        throw r0;
    L_0x0149:
        r2 = r15._buffer;
        r2 = r2.length();
    L_0x014f:
        r3 = r15._buffer;
        r5 = r3.array();
    L_0x0155:
        r3 = r15._state;
        if (r3 >= 0) goto L_0x0684;
    L_0x0159:
        r3 = r2 + -1;
        if (r2 <= 0) goto L_0x0684;
    L_0x015d:
        r2 = r15._buffer;
        r6 = r2.get();
        r2 = r15._eol;
        r4 = 13;
        if (r2 != r4) goto L_0x0173;
    L_0x0169:
        r2 = 10;
        if (r6 != r2) goto L_0x0173;
    L_0x016d:
        r2 = 10;
        r15._eol = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x0173:
        r2 = 0;
        r15._eol = r2;
        r2 = r15._state;
        switch(r2) {
            case -13: goto L_0x017d;
            case -12: goto L_0x0195;
            case -11: goto L_0x01c0;
            case -10: goto L_0x01dd;
            case -9: goto L_0x0226;
            case -8: goto L_0x017b;
            case -7: goto L_0x017b;
            case -6: goto L_0x025c;
            case -5: goto L_0x02d8;
            case -4: goto L_0x04b3;
            case -3: goto L_0x0520;
            case -2: goto L_0x057a;
            case -1: goto L_0x0609;
            default: goto L_0x017b;
        };
    L_0x017b:
        r2 = r3;
        goto L_0x0155;
    L_0x017d:
        r8 = -3;
        r15._contentLength = r8;
        r2 = 0;
        r15._cached = r2;
        r2 = 32;
        if (r6 > r2) goto L_0x018a;
    L_0x0188:
        if (r6 >= 0) goto L_0x017b;
    L_0x018a:
        r2 = r15._buffer;
        r2.mark();
        r2 = -12;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x0195:
        r2 = 32;
        if (r6 != r2) goto L_0x01b2;
    L_0x0199:
        r2 = r15._tok0;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r6 = r15._buffer;
        r6 = r6.getIndex();
        r6 = r6 + -1;
        r2.update(r4, r6);
        r2 = -11;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x01b2:
        r2 = 32;
        if (r6 >= r2) goto L_0x017b;
    L_0x01b6:
        if (r6 < 0) goto L_0x017b;
    L_0x01b8:
        r0 = new org.mortbay.jetty.HttpException;
        r1 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r0.<init>(r1);
        throw r0;
    L_0x01c0:
        r2 = 32;
        if (r6 > r2) goto L_0x01c6;
    L_0x01c4:
        if (r6 >= 0) goto L_0x01d1;
    L_0x01c6:
        r2 = r15._buffer;
        r2.mark();
        r2 = -10;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x01d1:
        r2 = 32;
        if (r6 >= r2) goto L_0x017b;
    L_0x01d5:
        r0 = new org.mortbay.jetty.HttpException;
        r1 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r0.<init>(r1);
        throw r0;
    L_0x01dd:
        r2 = 32;
        if (r6 != r2) goto L_0x01fb;
    L_0x01e1:
        r2 = r15._tok1;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r6 = r15._buffer;
        r6 = r6.getIndex();
        r6 = r6 + -1;
        r2.update(r4, r6);
        r2 = -9;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x01fb:
        r2 = 32;
        if (r6 >= r2) goto L_0x017b;
    L_0x01ff:
        if (r6 < 0) goto L_0x017b;
    L_0x0201:
        r2 = r15._handler;
        r3 = org.mortbay.jetty.HttpMethods.CACHE;
        r4 = r15._tok0;
        r3 = r3.lookup(r4);
        r4 = r15._buffer;
        r4 = r4.sliceFromMark();
        r5 = 0;
        r2.startRequest(r3, r4, r5);
        r2 = 0;
        r15._state = r2;
        r2 = r15._handler;
        r2.headerComplete();
        r2 = r15._handler;
        r4 = r15._contentPosition;
        r2.messageComplete(r4);
        goto L_0x0008;
    L_0x0226:
        r2 = 32;
        if (r6 > r2) goto L_0x022c;
    L_0x022a:
        if (r6 >= 0) goto L_0x0237;
    L_0x022c:
        r2 = r15._buffer;
        r2.mark();
        r2 = -6;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x0237:
        r2 = 32;
        if (r6 >= r2) goto L_0x017b;
    L_0x023b:
        r2 = r15._handler;
        r3 = org.mortbay.jetty.HttpMethods.CACHE;
        r4 = r15._tok0;
        r3 = r3.lookup(r4);
        r4 = r15._tok1;
        r5 = 0;
        r2.startRequest(r3, r4, r5);
        r2 = 0;
        r15._state = r2;
        r2 = r15._handler;
        r2.headerComplete();
        r2 = r15._handler;
        r4 = r15._contentPosition;
        r2.messageComplete(r4);
        goto L_0x0008;
    L_0x025c:
        r2 = 13;
        if (r6 == r2) goto L_0x0264;
    L_0x0260:
        r2 = 10;
        if (r6 != r2) goto L_0x017b;
    L_0x0264:
        r2 = org.mortbay.jetty.HttpMethods.CACHE;
        r4 = r15._tok0;
        r2 = r2.lookup(r4);
        r4 = r15._tok0;
        if (r2 != r4) goto L_0x02c4;
    L_0x0270:
        r4 = r15._tok1;
        r4 = r4.length();
        r7 = 3;
        if (r4 != r7) goto L_0x02c4;
    L_0x0279:
        r4 = r15._tok1;
        r4 = r4.peek();
        r4 = (char) r4;
        r4 = java.lang.Character.isDigit(r4);
        if (r4 == 0) goto L_0x02c4;
    L_0x0286:
        r2 = r15._tok1;
        r2 = org.mortbay.io.BufferUtil.toInt(r2);
        r15._responseStatus = r2;
        r2 = r15._handler;
        r4 = org.mortbay.jetty.HttpVersions.CACHE;
        r7 = r15._tok0;
        r4 = r4.lookup(r7);
        r7 = r15._responseStatus;
        r8 = r15._buffer;
        r8 = r8.sliceFromMark();
        r2.startResponse(r4, r7, r8);
    L_0x02a3:
        r15._eol = r6;
        r2 = -5;
        r15._state = r2;
        r2 = r15._tok0;
        r4 = r15._tok0;
        r4 = r4.getIndex();
        r2.setPutIndex(r4);
        r2 = r15._tok1;
        r4 = r15._tok1;
        r4 = r4.getIndex();
        r2.setPutIndex(r4);
        r2 = 0;
        r15._multiLineValue = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x02c4:
        r4 = r15._handler;
        r7 = r15._tok1;
        r8 = org.mortbay.jetty.HttpVersions.CACHE;
        r9 = r15._buffer;
        r9 = r9.sliceFromMark();
        r8 = r8.lookup(r9);
        r4.startRequest(r2, r7, r8);
        goto L_0x02a3;
    L_0x02d8:
        switch(r6) {
            case 9: goto L_0x03a4;
            case 32: goto L_0x03a4;
            case 58: goto L_0x03a4;
            default: goto L_0x02db;
        };
    L_0x02db:
        r2 = r15._cached;
        if (r2 != 0) goto L_0x02f3;
    L_0x02df:
        r2 = r15._tok0;
        r2 = r2.length();
        if (r2 > 0) goto L_0x02f3;
    L_0x02e7:
        r2 = r15._tok1;
        r2 = r2.length();
        if (r2 > 0) goto L_0x02f3;
    L_0x02ef:
        r2 = r15._multiLineValue;
        if (r2 == 0) goto L_0x032b;
    L_0x02f3:
        r2 = r15._cached;
        if (r2 == 0) goto L_0x03ad;
    L_0x02f7:
        r2 = r15._cached;
    L_0x02f9:
        r4 = 0;
        r15._cached = r4;
        r4 = r15._multiLineValue;
        if (r4 != 0) goto L_0x03b7;
    L_0x0300:
        r4 = r15._tok1;
    L_0x0302:
        r7 = org.mortbay.jetty.HttpHeaders.CACHE;
        r7 = r7.getOrdinal(r2);
        if (r7 < 0) goto L_0x030d;
    L_0x030a:
        switch(r7) {
            case 5: goto L_0x03e8;
            case 12: goto L_0x03c0;
            default: goto L_0x030d;
        };
    L_0x030d:
        r7 = r15._handler;
        r7.parsedHeader(r2, r4);
        r2 = r15._tok0;
        r4 = r15._tok0;
        r4 = r4.getIndex();
        r2.setPutIndex(r4);
        r2 = r15._tok1;
        r4 = r15._tok1;
        r4 = r4.getIndex();
        r2.setPutIndex(r4);
        r2 = 0;
        r15._multiLineValue = r2;
    L_0x032b:
        r2 = 13;
        if (r6 == r2) goto L_0x0333;
    L_0x032f:
        r2 = 10;
        if (r6 != r2) goto L_0x0474;
    L_0x0333:
        r2 = r15._contentLength;
        r4 = -3;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 != 0) goto L_0x0355;
    L_0x033b:
        r2 = r15._responseStatus;
        if (r2 == 0) goto L_0x0351;
    L_0x033f:
        r2 = r15._responseStatus;
        r3 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r2 == r3) goto L_0x0351;
    L_0x0345:
        r2 = r15._responseStatus;
        r3 = 204; // 0xcc float:2.86E-43 double:1.01E-321;
        if (r2 == r3) goto L_0x0351;
    L_0x034b:
        r2 = r15._responseStatus;
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 >= r3) goto L_0x0420;
    L_0x0351:
        r2 = 0;
        r15._contentLength = r2;
    L_0x0355:
        r2 = 0;
        r15._contentPosition = r2;
        r15._eol = r6;
        r2 = r15._contentLength;
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 <= 0) goto L_0x0426;
    L_0x0364:
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
    L_0x0367:
        switch(r2) {
            case -2: goto L_0x0447;
            case -1: goto L_0x042b;
            case 0: goto L_0x0463;
            default: goto L_0x036a;
        };
    L_0x036a:
        r2 = 2;
        r15._state = r2;
        r2 = r15._forceContentBuffer;
        if (r2 != 0) goto L_0x0393;
    L_0x0371:
        r2 = r15._buffers;
        if (r2 == 0) goto L_0x039d;
    L_0x0375:
        r2 = r15._body;
        if (r2 != 0) goto L_0x039d;
    L_0x0379:
        r2 = r15._buffer;
        r3 = r15._header;
        if (r2 != r3) goto L_0x039d;
    L_0x037f:
        r2 = r15._contentLength;
        r4 = r15._header;
        r4 = r4.capacity();
        r5 = r15._header;
        r5 = r5.getIndex();
        r4 = r4 - r5;
        r4 = (long) r4;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 < 0) goto L_0x039d;
    L_0x0393:
        r2 = r15._buffers;
        r3 = r15._contentBufferSize;
        r2 = r2.getBuffer(r3);
        r15._body = r2;
    L_0x039d:
        r2 = r15._handler;
        r2.headerComplete();
        goto L_0x0008;
    L_0x03a4:
        r2 = -1;
        r15._length = r2;
        r2 = -2;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x03ad:
        r2 = org.mortbay.jetty.HttpHeaders.CACHE;
        r4 = r15._tok0;
        r2 = r2.lookup(r4);
        goto L_0x02f9;
    L_0x03b7:
        r4 = new org.mortbay.io.ByteArrayBuffer;
        r7 = r15._multiLineValue;
        r4.<init>(r7);
        goto L_0x0302;
    L_0x03c0:
        r8 = r15._contentLength;
        r10 = -2;
        r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r7 == 0) goto L_0x030d;
    L_0x03c8:
        r8 = org.mortbay.io.BufferUtil.toLong(r4);	 Catch:{ NumberFormatException -> 0x03dc }
        r15._contentLength = r8;	 Catch:{ NumberFormatException -> 0x03dc }
        r8 = r15._contentLength;
        r10 = 0;
        r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r7 > 0) goto L_0x030d;
    L_0x03d6:
        r8 = 0;
        r15._contentLength = r8;
        goto L_0x030d;
    L_0x03dc:
        r0 = move-exception;
        org.mortbay.log.Log.ignore(r0);
        r0 = new org.mortbay.jetty.HttpException;
        r1 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r0.<init>(r1);
        throw r0;
    L_0x03e8:
        r7 = org.mortbay.jetty.HttpHeaderValues.CACHE;
        r4 = r7.lookup(r4);
        r7 = 2;
        r8 = org.mortbay.jetty.HttpHeaderValues.CACHE;
        r8 = r8.getOrdinal(r4);
        if (r7 != r8) goto L_0x03fd;
    L_0x03f7:
        r8 = -2;
        r15._contentLength = r8;
        goto L_0x030d;
    L_0x03fd:
        r7 = r4.toString();
        r8 = "chunked";
        r8 = r7.endsWith(r8);
        if (r8 == 0) goto L_0x040f;
    L_0x0409:
        r8 = -2;
        r15._contentLength = r8;
        goto L_0x030d;
    L_0x040f:
        r8 = "chunked";
        r7 = r7.indexOf(r8);
        if (r7 < 0) goto L_0x030d;
    L_0x0417:
        r0 = new org.mortbay.jetty.HttpException;
        r1 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r2 = 0;
        r0.<init>(r1, r2);
        throw r0;
    L_0x0420:
        r2 = -1;
        r15._contentLength = r2;
        goto L_0x0355;
    L_0x0426:
        r2 = r15._contentLength;
        r2 = (int) r2;
        goto L_0x0367;
    L_0x042b:
        r2 = 1;
        r15._state = r2;
        r2 = r15._body;
        if (r2 != 0) goto L_0x0440;
    L_0x0432:
        r2 = r15._buffers;
        if (r2 == 0) goto L_0x0440;
    L_0x0436:
        r2 = r15._buffers;
        r3 = r15._contentBufferSize;
        r2 = r2.getBuffer(r3);
        r15._body = r2;
    L_0x0440:
        r2 = r15._handler;
        r2.headerComplete();
        goto L_0x0008;
    L_0x0447:
        r2 = 3;
        r15._state = r2;
        r2 = r15._body;
        if (r2 != 0) goto L_0x045c;
    L_0x044e:
        r2 = r15._buffers;
        if (r2 == 0) goto L_0x045c;
    L_0x0452:
        r2 = r15._buffers;
        r3 = r15._contentBufferSize;
        r2 = r2.getBuffer(r3);
        r15._body = r2;
    L_0x045c:
        r2 = r15._handler;
        r2.headerComplete();
        goto L_0x0008;
    L_0x0463:
        r2 = 0;
        r15._state = r2;
        r2 = r15._handler;
        r2.headerComplete();
        r2 = r15._handler;
        r4 = r15._contentPosition;
        r2.messageComplete(r4);
        goto L_0x0008;
    L_0x0474:
        r2 = 1;
        r15._length = r2;
        r2 = r15._buffer;
        r2.mark();
        r2 = -4;
        r15._state = r2;
        if (r5 == 0) goto L_0x017b;
    L_0x0481:
        r2 = org.mortbay.jetty.HttpHeaders.CACHE;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r6 = r3 + 1;
        r2 = r2.getBest(r5, r4, r6);
        r15._cached = r2;
        r2 = r15._cached;
        if (r2 == 0) goto L_0x017b;
    L_0x0495:
        r2 = r15._cached;
        r2 = r2.length();
        r15._length = r2;
        r2 = r15._buffer;
        r3 = r15._buffer;
        r3 = r3.markIndex();
        r4 = r15._length;
        r3 = r3 + r4;
        r2.setGetIndex(r3);
        r2 = r15._buffer;
        r2 = r2.length();
        goto L_0x0155;
    L_0x04b3:
        switch(r6) {
            case 9: goto L_0x051d;
            case 10: goto L_0x04d8;
            case 13: goto L_0x04d8;
            case 32: goto L_0x051d;
            case 58: goto L_0x04f8;
            default: goto L_0x04b6;
        };
    L_0x04b6:
        r2 = 0;
        r15._cached = r2;
        r2 = r15._length;
        r4 = -1;
        if (r2 != r4) goto L_0x04c3;
    L_0x04be:
        r2 = r15._buffer;
        r2.mark();
    L_0x04c3:
        r2 = r15._buffer;
        r2 = r2.getIndex();
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r2 = r2 - r4;
        r15._length = r2;
        r2 = -3;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x04d8:
        r2 = r15._length;
        if (r2 <= 0) goto L_0x04f0;
    L_0x04dc:
        r2 = r15._tok0;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r7 = r15._buffer;
        r7 = r7.markIndex();
        r8 = r15._length;
        r7 = r7 + r8;
        r2.update(r4, r7);
    L_0x04f0:
        r15._eol = r6;
        r2 = -5;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x04f8:
        r2 = r15._length;
        if (r2 <= 0) goto L_0x0514;
    L_0x04fc:
        r2 = r15._cached;
        if (r2 != 0) goto L_0x0514;
    L_0x0500:
        r2 = r15._tok0;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r6 = r15._buffer;
        r6 = r6.markIndex();
        r7 = r15._length;
        r6 = r6 + r7;
        r2.update(r4, r6);
    L_0x0514:
        r2 = -1;
        r15._length = r2;
        r2 = -2;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x051d:
        r2 = r3;
        goto L_0x0155;
    L_0x0520:
        switch(r6) {
            case 9: goto L_0x0574;
            case 10: goto L_0x052f;
            case 13: goto L_0x052f;
            case 32: goto L_0x0574;
            case 58: goto L_0x054f;
            default: goto L_0x0523;
        };
    L_0x0523:
        r2 = 0;
        r15._cached = r2;
        r2 = r15._length;
        r2 = r2 + 1;
        r15._length = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x052f:
        r2 = r15._length;
        if (r2 <= 0) goto L_0x0547;
    L_0x0533:
        r2 = r15._tok0;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r7 = r15._buffer;
        r7 = r7.markIndex();
        r8 = r15._length;
        r7 = r7 + r8;
        r2.update(r4, r7);
    L_0x0547:
        r15._eol = r6;
        r2 = -5;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x054f:
        r2 = r15._length;
        if (r2 <= 0) goto L_0x056b;
    L_0x0553:
        r2 = r15._cached;
        if (r2 != 0) goto L_0x056b;
    L_0x0557:
        r2 = r15._tok0;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r6 = r15._buffer;
        r6 = r6.markIndex();
        r7 = r15._length;
        r6 = r6 + r7;
        r2.update(r4, r6);
    L_0x056b:
        r2 = -1;
        r15._length = r2;
        r2 = -2;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x0574:
        r2 = -4;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x057a:
        switch(r6) {
            case 9: goto L_0x0606;
            case 10: goto L_0x059c;
            case 13: goto L_0x059c;
            case 32: goto L_0x0606;
            default: goto L_0x057d;
        };
    L_0x057d:
        r2 = r15._length;
        r4 = -1;
        if (r2 != r4) goto L_0x0587;
    L_0x0582:
        r2 = r15._buffer;
        r2.mark();
    L_0x0587:
        r2 = r15._buffer;
        r2 = r2.getIndex();
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r2 = r2 - r4;
        r15._length = r2;
        r2 = -1;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x059c:
        r2 = r15._length;
        if (r2 <= 0) goto L_0x05bc;
    L_0x05a0:
        r2 = r15._tok1;
        r2 = r2.length();
        if (r2 != 0) goto L_0x05c4;
    L_0x05a8:
        r2 = r15._tok1;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r7 = r15._buffer;
        r7 = r7.markIndex();
        r8 = r15._length;
        r7 = r7 + r8;
        r2.update(r4, r7);
    L_0x05bc:
        r15._eol = r6;
        r2 = -5;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x05c4:
        r2 = r15._multiLineValue;
        if (r2 != 0) goto L_0x05d0;
    L_0x05c8:
        r2 = r15._tok1;
        r2 = r2.toString();
        r15._multiLineValue = r2;
    L_0x05d0:
        r2 = r15._tok1;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r7 = r15._buffer;
        r7 = r7.markIndex();
        r8 = r15._length;
        r7 = r7 + r8;
        r2.update(r4, r7);
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r4 = r15._multiLineValue;
        r2 = r2.append(r4);
        r4 = " ";
        r2 = r2.append(r4);
        r4 = r15._tok1;
        r4 = r4.toString();
        r2 = r2.append(r4);
        r2 = r2.toString();
        r15._multiLineValue = r2;
        goto L_0x05bc;
    L_0x0606:
        r2 = r3;
        goto L_0x0155;
    L_0x0609:
        switch(r6) {
            case 9: goto L_0x067e;
            case 10: goto L_0x0614;
            case 13: goto L_0x0614;
            case 32: goto L_0x067e;
            default: goto L_0x060c;
        };
    L_0x060c:
        r2 = r15._length;
        r2 = r2 + 1;
        r15._length = r2;
        goto L_0x017b;
    L_0x0614:
        r2 = r15._length;
        if (r2 <= 0) goto L_0x0634;
    L_0x0618:
        r2 = r15._tok1;
        r2 = r2.length();
        if (r2 != 0) goto L_0x063c;
    L_0x0620:
        r2 = r15._tok1;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r7 = r15._buffer;
        r7 = r7.markIndex();
        r8 = r15._length;
        r7 = r7 + r8;
        r2.update(r4, r7);
    L_0x0634:
        r15._eol = r6;
        r2 = -5;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x063c:
        r2 = r15._multiLineValue;
        if (r2 != 0) goto L_0x0648;
    L_0x0640:
        r2 = r15._tok1;
        r2 = r2.toString();
        r15._multiLineValue = r2;
    L_0x0648:
        r2 = r15._tok1;
        r4 = r15._buffer;
        r4 = r4.markIndex();
        r7 = r15._buffer;
        r7 = r7.markIndex();
        r8 = r15._length;
        r7 = r7 + r8;
        r2.update(r4, r7);
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r4 = r15._multiLineValue;
        r2 = r2.append(r4);
        r4 = " ";
        r2 = r2.append(r4);
        r4 = r15._tok1;
        r4 = r4.toString();
        r2 = r2.append(r4);
        r2 = r2.toString();
        r15._multiLineValue = r2;
        goto L_0x0634;
    L_0x067e:
        r2 = -2;
        r15._state = r2;
        r2 = r3;
        goto L_0x0155;
    L_0x0684:
        r2 = r15._buffer;
        r2 = r2.length();
    L_0x068a:
        r3 = r15._state;
        if (r3 <= 0) goto L_0x0008;
    L_0x068e:
        if (r2 <= 0) goto L_0x0008;
    L_0x0690:
        r3 = r15._eol;
        r4 = 13;
        if (r3 != r4) goto L_0x06af;
    L_0x0696:
        r3 = r15._buffer;
        r3 = r3.peek();
        r4 = 10;
        if (r3 != r4) goto L_0x06af;
    L_0x06a0:
        r2 = r15._buffer;
        r2 = r2.get();
        r15._eol = r2;
        r2 = r15._buffer;
        r2 = r2.length();
        goto L_0x068a;
    L_0x06af:
        r3 = 0;
        r15._eol = r3;
        r3 = r15._state;
        switch(r3) {
            case 1: goto L_0x06be;
            case 2: goto L_0x06e0;
            case 3: goto L_0x072b;
            case 4: goto L_0x0759;
            case 5: goto L_0x07e1;
            case 6: goto L_0x0806;
            default: goto L_0x06b7;
        };
    L_0x06b7:
        r2 = r15._buffer;
        r2 = r2.length();
        goto L_0x068a;
    L_0x06be:
        r2 = r15._buffer;
        r3 = r15._buffer;
        r3 = r3.length();
        r2 = r2.get(r3);
        r4 = r15._contentPosition;
        r3 = r2.length();
        r6 = (long) r3;
        r4 = r4 + r6;
        r15._contentPosition = r4;
        r3 = r15._contentView;
        r3.update(r2);
        r3 = r15._handler;
        r3.content(r2);
        goto L_0x0008;
    L_0x06e0:
        r4 = r15._contentLength;
        r6 = r15._contentPosition;
        r4 = r4 - r6;
        r6 = 0;
        r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r3 != 0) goto L_0x06f7;
    L_0x06eb:
        r2 = 0;
        r15._state = r2;
        r2 = r15._handler;
        r4 = r15._contentPosition;
        r2.messageComplete(r4);
        goto L_0x0008;
    L_0x06f7:
        r6 = (long) r2;
        r3 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r3 <= 0) goto L_0x06fd;
    L_0x06fc:
        r2 = (int) r4;
    L_0x06fd:
        r3 = r15._buffer;
        r2 = r3.get(r2);
        r4 = r15._contentPosition;
        r3 = r2.length();
        r6 = (long) r3;
        r4 = r4 + r6;
        r15._contentPosition = r4;
        r3 = r15._contentView;
        r3.update(r2);
        r3 = r15._handler;
        r3.content(r2);
        r2 = r15._contentPosition;
        r4 = r15._contentLength;
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 != 0) goto L_0x0008;
    L_0x071f:
        r2 = 0;
        r15._state = r2;
        r2 = r15._handler;
        r4 = r15._contentPosition;
        r2.messageComplete(r4);
        goto L_0x0008;
    L_0x072b:
        r2 = r15._buffer;
        r2 = r2.peek();
        r3 = 13;
        if (r2 == r3) goto L_0x0739;
    L_0x0735:
        r3 = 10;
        if (r2 != r3) goto L_0x0743;
    L_0x0739:
        r2 = r15._buffer;
        r2 = r2.get();
        r15._eol = r2;
        goto L_0x06b7;
    L_0x0743:
        r3 = 32;
        if (r2 > r3) goto L_0x074e;
    L_0x0747:
        r2 = r15._buffer;
        r2.get();
        goto L_0x06b7;
    L_0x074e:
        r2 = 0;
        r15._chunkLength = r2;
        r2 = 0;
        r15._chunkPosition = r2;
        r2 = 4;
        r15._state = r2;
        goto L_0x06b7;
    L_0x0759:
        r2 = r15._buffer;
        r2 = r2.get();
        r3 = 13;
        if (r2 == r3) goto L_0x0767;
    L_0x0763:
        r3 = 10;
        if (r2 != r3) goto L_0x077e;
    L_0x0767:
        r15._eol = r2;
        r2 = r15._chunkLength;
        if (r2 != 0) goto L_0x0779;
    L_0x076d:
        r2 = 0;
        r15._state = r2;
        r2 = r15._handler;
        r4 = r15._contentPosition;
        r2.messageComplete(r4);
        goto L_0x0008;
    L_0x0779:
        r2 = 6;
        r15._state = r2;
        goto L_0x06b7;
    L_0x077e:
        r3 = 32;
        if (r2 <= r3) goto L_0x0786;
    L_0x0782:
        r3 = 59;
        if (r2 != r3) goto L_0x078b;
    L_0x0786:
        r2 = 5;
        r15._state = r2;
        goto L_0x06b7;
    L_0x078b:
        r3 = 48;
        if (r2 < r3) goto L_0x079e;
    L_0x078f:
        r3 = 57;
        if (r2 > r3) goto L_0x079e;
    L_0x0793:
        r3 = r15._chunkLength;
        r3 = r3 * 16;
        r2 = r2 + -48;
        r2 = r2 + r3;
        r15._chunkLength = r2;
        goto L_0x06b7;
    L_0x079e:
        r3 = 97;
        if (r2 < r3) goto L_0x07b3;
    L_0x07a2:
        r3 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r2 > r3) goto L_0x07b3;
    L_0x07a6:
        r3 = r15._chunkLength;
        r3 = r3 * 16;
        r2 = r2 + 10;
        r2 = r2 + -97;
        r2 = r2 + r3;
        r15._chunkLength = r2;
        goto L_0x06b7;
    L_0x07b3:
        r3 = 65;
        if (r2 < r3) goto L_0x07c8;
    L_0x07b7:
        r3 = 70;
        if (r2 > r3) goto L_0x07c8;
    L_0x07bb:
        r3 = r15._chunkLength;
        r3 = r3 * 16;
        r2 = r2 + 10;
        r2 = r2 + -65;
        r2 = r2 + r3;
        r15._chunkLength = r2;
        goto L_0x06b7;
    L_0x07c8:
        r0 = new java.io.IOException;
        r1 = new java.lang.StringBuffer;
        r1.<init>();
        r3 = "bad chunk char: ";
        r1 = r1.append(r3);
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x07e1:
        r2 = r15._buffer;
        r2 = r2.get();
        r3 = 13;
        if (r2 == r3) goto L_0x07ef;
    L_0x07eb:
        r3 = 10;
        if (r2 != r3) goto L_0x06b7;
    L_0x07ef:
        r15._eol = r2;
        r2 = r15._chunkLength;
        if (r2 != 0) goto L_0x0801;
    L_0x07f5:
        r2 = 0;
        r15._state = r2;
        r2 = r15._handler;
        r4 = r15._contentPosition;
        r2.messageComplete(r4);
        goto L_0x0008;
    L_0x0801:
        r2 = 6;
        r15._state = r2;
        goto L_0x06b7;
    L_0x0806:
        r3 = r15._chunkLength;
        r4 = r15._chunkPosition;
        r3 = r3 - r4;
        if (r3 != 0) goto L_0x0812;
    L_0x080d:
        r2 = 3;
        r15._state = r2;
        goto L_0x06b7;
    L_0x0812:
        if (r2 <= r3) goto L_0x0815;
    L_0x0814:
        r2 = r3;
    L_0x0815:
        r3 = r15._buffer;
        r2 = r3.get(r2);
        r4 = r15._contentPosition;
        r3 = r2.length();
        r6 = (long) r3;
        r4 = r4 + r6;
        r15._contentPosition = r4;
        r3 = r15._chunkPosition;
        r4 = r2.length();
        r3 = r3 + r4;
        r15._chunkPosition = r3;
        r3 = r15._contentView;
        r3.update(r2);
        r3 = r15._handler;
        r3.content(r2);
        goto L_0x0008;
    L_0x083a:
        r12 = r1;
        r13 = r2;
        r2 = r12;
        r3 = r0;
        r0 = r13;
        goto L_0x00f9;
    L_0x0841:
        r12 = r2;
        r2 = r0;
        r0 = r12;
        goto L_0x014f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.HttpParser.parseNext():long");
    }

    public void reset(boolean z) {
        synchronized (this) {
            this._contentView.setGetIndex(this._contentView.putIndex());
            this._state = -13;
            this._contentLength = -3;
            this._contentPosition = 0;
            this._length = 0;
            this._responseStatus = 0;
            if (this._buffer != null && this._buffer.length() > 0 && this._eol == (byte) 13 && this._buffer.peek() == (byte) 10) {
                this._buffer.skip(1);
                this._eol = (byte) 10;
            }
            if (this._body != null) {
                if (this._body.hasContent()) {
                    this._header.setMarkIndex(-1);
                    this._header.compact();
                    int space = this._header.space();
                    if (space > this._body.length()) {
                        space = this._body.length();
                    }
                    this._body.peek(this._body.getIndex(), space);
                    this._body.skip(this._header.put(this._body.peek(this._body.getIndex(), space)));
                }
                if (this._body.length() == 0) {
                    if (this._buffers != null && z) {
                        this._buffers.returnBuffer(this._body);
                    }
                    this._body = null;
                } else {
                    this._body.setMarkIndex(-1);
                    this._body.compact();
                }
            }
            if (this._header != null) {
                this._header.setMarkIndex(-1);
                if (this._header.hasContent() || this._buffers == null || !z) {
                    this._header.compact();
                    this._tok0.update(this._header);
                    this._tok0.update(0, 0);
                    this._tok1.update(this._header);
                    this._tok1.update(0, 0);
                } else {
                    this._buffers.returnBuffer(this._header);
                    this._header = null;
                    this._buffer = null;
                }
            }
            this._buffer = this._header;
        }
    }

    public void setForceContentBuffer(boolean z) {
        this._forceContentBuffer = z;
    }

    public void setState(int i) {
        this._state = i;
        this._contentLength = -3;
    }

    public void skipCRLF() {
        while (this._header != null && this._header.length() > 0) {
            byte peek = this._header.peek();
            if (peek != (byte) 13 && peek != (byte) 10) {
                break;
            }
            this._eol = peek;
            this._header.skip(1);
        }
        while (this._body != null && this._body.length() > 0) {
            peek = this._body.peek();
            if (peek == (byte) 13 || peek == (byte) 10) {
                this._eol = peek;
                this._body.skip(1);
            } else {
                return;
            }
        }
    }

    public String toString() {
        return new StringBuffer().append("state=").append(this._state).append(" length=").append(this._length).append(" len=").append(this._contentLength).toString();
    }

    public String toString(Buffer buffer) {
        return new StringBuffer().append("state=").append(this._state).append(" length=").append(this._length).append(" buf=").append(buffer.hashCode()).toString();
    }
}
