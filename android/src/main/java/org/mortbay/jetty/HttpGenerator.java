package org.mortbay.jetty;

import java.io.IOException;
import java.util.Iterator;
import org.mortbay.io.Buffer;
import org.mortbay.io.BufferCache.CachedBuffer;
import org.mortbay.io.BufferUtil;
import org.mortbay.io.Buffers;
import org.mortbay.io.EndPoint;
import org.mortbay.io.Portable;
import org.mortbay.jetty.HttpFields.Field;
import org.mortbay.log.Log;
import org.mortbay.util.QuotedStringTokenizer;

public class HttpGenerator extends AbstractGenerator {
    private static int CHUNK_SPACE = 12;
    private static byte[] CONNECTION_ = Portable.getBytes("Connection: ");
    private static byte[] CONNECTION_CLOSE = Portable.getBytes("Connection: close\r\n");
    private static byte[] CONNECTION_KEEP_ALIVE = Portable.getBytes("Connection: keep-alive\r\n");
    private static byte[] CONTENT_LENGTH_0 = Portable.getBytes("Content-Length: 0\r\n");
    private static byte[] CRLF = Portable.getBytes("\r\n");
    private static byte[] LAST_CHUNK = new byte[]{(byte) 48, (byte) 13, (byte) 10, (byte) 13, (byte) 10};
    private static byte[] SERVER = Portable.getBytes("Server: Jetty(6.0.x)\r\n");
    private static byte[] TRANSFER_ENCODING_CHUNKED = Portable.getBytes("Transfer-Encoding: chunked\r\n");
    private boolean _bufferChunked = false;
    private boolean _bypass = false;
    private boolean _needCRLF = false;
    private boolean _needEOC = false;

    public HttpGenerator(Buffers buffers, EndPoint endPoint, int i, int i2) {
        super(buffers, endPoint, i, i2);
    }

    private void prepareBuffers() {
        if (!this._bufferChunked) {
            if (this._content != null && this._content.length() > 0 && this._buffer != null && this._buffer.space() > 0) {
                this._content.skip(this._buffer.put(this._content));
                if (this._content.length() == 0) {
                    this._content = null;
                }
            }
            if (this._contentLength == -2) {
                int length = this._buffer == null ? 0 : this._buffer.length();
                if (length > 0) {
                    this._bufferChunked = true;
                    if (this._buffer.getIndex() == CHUNK_SPACE) {
                        this._buffer.poke(this._buffer.getIndex() - 2, HttpTokens.CRLF, 0, 2);
                        this._buffer.setGetIndex(this._buffer.getIndex() - 2);
                        BufferUtil.prependHexInt(this._buffer, length);
                        if (this._needCRLF) {
                            this._buffer.poke(this._buffer.getIndex() - 2, HttpTokens.CRLF, 0, 2);
                            this._buffer.setGetIndex(this._buffer.getIndex() - 2);
                            this._needCRLF = false;
                        }
                    } else {
                        if (this._needCRLF) {
                            if (this._header.length() > 0) {
                                throw new IllegalStateException("EOC");
                            }
                            this._header.put(HttpTokens.CRLF);
                            this._needCRLF = false;
                        }
                        BufferUtil.putHexInt(this._header, length);
                        this._header.put(HttpTokens.CRLF);
                    }
                    if (this._buffer.space() >= 2) {
                        this._buffer.put(HttpTokens.CRLF);
                    } else {
                        this._needCRLF = true;
                    }
                }
                if (this._needEOC && (this._content == null || this._content.length() == 0)) {
                    if (this._needCRLF) {
                        if (this._buffer == null && this._header.space() >= 2) {
                            this._header.put(HttpTokens.CRLF);
                            this._needCRLF = false;
                        } else if (this._buffer != null && this._buffer.space() >= 2) {
                            this._buffer.put(HttpTokens.CRLF);
                            this._needCRLF = false;
                        }
                    }
                    if (!this._needCRLF && this._needEOC) {
                        if (this._buffer == null && this._header.space() >= LAST_CHUNK.length) {
                            if (!this._head) {
                                this._header.put(LAST_CHUNK);
                                this._bufferChunked = true;
                            }
                            this._needEOC = false;
                        } else if (this._buffer != null && this._buffer.space() >= LAST_CHUNK.length) {
                            if (!this._head) {
                                this._buffer.put(LAST_CHUNK);
                                this._bufferChunked = true;
                            }
                            this._needEOC = false;
                        }
                    }
                }
            }
        }
        if (this._content != null && this._content.length() == 0) {
            this._content = null;
        }
    }

    public static void setServerVersion(String str) {
        SERVER = Portable.getBytes(new StringBuffer().append("Server: Jetty(").append(str).append(")\r\n").toString());
    }

    public void addContent(Buffer buffer, boolean z) throws IOException {
        if (this._noContent) {
            throw new IllegalStateException("NO CONTENT");
        } else if (this._last || this._state == 4) {
            Log.debug("Ignoring extra content {}", buffer);
            buffer.clear();
        } else {
            this._last = z;
            if ((this._content != null && this._content.length() > 0) || this._bufferChunked) {
                if (this._endp.isOpen()) {
                    flush();
                    if ((this._content != null && this._content.length() > 0) || this._bufferChunked) {
                        throw new IllegalStateException("FULL");
                    }
                }
                throw new EofException();
            }
            this._content = buffer;
            this._contentWritten += (long) buffer.length();
            if (this._head) {
                buffer.clear();
                this._content = null;
            } else if (this._endp == null || this._buffer != null || buffer.length() <= 0 || !this._last) {
                if (this._buffer == null) {
                    this._buffer = this._buffers.getBuffer(this._contentBufferSize);
                }
                this._content.skip(this._buffer.put(this._content));
                if (this._content.length() == 0) {
                    this._content = null;
                }
            } else {
                this._bypass = true;
            }
        }
    }

    public boolean addContent(byte b) throws IOException {
        if (this._noContent) {
            throw new IllegalStateException("NO CONTENT");
        } else if (this._last || this._state == 4) {
            Log.debug("Ignoring extra content {}", new Byte(b));
            return false;
        } else {
            if ((this._content != null && this._content.length() > 0) || this._bufferChunked) {
                flush();
                if ((this._content != null && this._content.length() > 0) || this._bufferChunked) {
                    throw new IllegalStateException("FULL");
                }
            }
            this._contentWritten++;
            if (this._head) {
                return false;
            }
            if (this._buffer == null) {
                this._buffer = this._buffers.getBuffer(this._contentBufferSize);
            }
            this._buffer.put(b);
            return this._buffer.space() <= ((this._contentLength > -2 ? 1 : (this._contentLength == -2 ? 0 : -1)) == 0 ? CHUNK_SPACE : 0);
        }
    }

    public void complete() throws IOException {
        if (this._state != 4) {
            super.complete();
            if (this._state < 3) {
                this._state = 3;
                if (this._contentLength == -2) {
                    this._needEOC = true;
                }
            }
            flush();
        }
    }

    public void completeHeader(HttpFields httpFields, boolean z) throws IOException {
        if (this._state == 0) {
            if (this._method == null && this._status == 0) {
                throw new EofException();
            } else if (!this._last || z) {
                Object obj;
                StringBuffer stringBuffer;
                Object obj2;
                Field field;
                Field field2;
                this._last |= z;
                if (this._header == null) {
                    this._header = this._buffers.getBuffer(this._headerBufferSize);
                }
                if (this._method != null) {
                    this._close = false;
                    if (this._version == 9) {
                        this._contentLength = 0;
                        this._header.put(this._method);
                        this._header.put((byte) 32);
                        this._header.put(this._uri.getBytes("utf-8"));
                        this._header.put(HttpTokens.CRLF);
                        this._state = 3;
                        this._noContent = true;
                        return;
                    }
                    this._header.put(this._method);
                    this._header.put((byte) 32);
                    this._header.put(this._uri.getBytes("utf-8"));
                    this._header.put((byte) 32);
                    this._header.put(this._version == 10 ? HttpVersions.HTTP_1_0_BUFFER : HttpVersions.HTTP_1_1_BUFFER);
                    this._header.put(HttpTokens.CRLF);
                } else if (this._version == 9) {
                    this._close = true;
                    this._contentLength = -1;
                    this._state = 2;
                    return;
                } else {
                    if (this._version == 10) {
                        this._close = true;
                    }
                    Buffer responseLine = HttpStatus.getResponseLine(this._status);
                    if (responseLine == null) {
                        if (this._reason == null) {
                            this._reason = AbstractGenerator.getReasonBuffer(this._status);
                        }
                        this._header.put(HttpVersions.HTTP_1_1_BUFFER);
                        this._header.put((byte) 32);
                        this._header.put((byte) ((this._status / 100) + 48));
                        this._header.put((byte) (((this._status % 100) / 10) + 48));
                        this._header.put((byte) ((this._status % 10) + 48));
                        this._header.put((byte) 32);
                        if (this._reason == null) {
                            this._header.put((byte) ((this._status / 100) + 48));
                            this._header.put((byte) (((this._status % 100) / 10) + 48));
                            this._header.put((byte) ((this._status % 10) + 48));
                        } else {
                            this._header.put(this._reason);
                        }
                        this._header.put(HttpTokens.CRLF);
                    } else if (this._reason == null) {
                        this._header.put(responseLine);
                    } else {
                        this._header.put(responseLine.array(), 0, HttpVersions.HTTP_1_1_BUFFER.length() + 5);
                        this._header.put(this._reason);
                        this._header.put(HttpTokens.CRLF);
                    }
                    if (this._status < 200 && this._status >= 100) {
                        this._noContent = true;
                        this._content = null;
                        if (this._buffer != null) {
                            this._buffer.clear();
                        }
                        this._header.put(HttpTokens.CRLF);
                        this._state = 2;
                        return;
                    } else if (this._status == 204 || this._status == 304) {
                        this._noContent = true;
                        this._content = null;
                        if (this._buffer != null) {
                            this._buffer.clear();
                        }
                    }
                }
                Object obj3;
                Object obj4;
                if (httpFields != null) {
                    Iterator fields = httpFields.getFields();
                    obj3 = null;
                    obj = null;
                    obj4 = null;
                    stringBuffer = null;
                    obj2 = null;
                    field = null;
                    field2 = null;
                    while (fields.hasNext()) {
                        Field field3 = (Field) fields.next();
                        switch (field3.getNameOrdinal()) {
                            case 1:
                                if (this._method != null) {
                                    field3.put(this._header);
                                }
                                switch (field3.getValueOrdinal()) {
                                    case -1:
                                        QuotedStringTokenizer quotedStringTokenizer = new QuotedStringTokenizer(field3.getValue(), ",");
                                        StringBuffer stringBuffer2 = stringBuffer;
                                        Object obj5 = obj2;
                                        Object obj6 = obj4;
                                        while (quotedStringTokenizer.hasMoreTokens()) {
                                            String trim = quotedStringTokenizer.nextToken().trim();
                                            CachedBuffer cachedBuffer = HttpHeaderValues.CACHE.get(trim);
                                            if (cachedBuffer != null) {
                                                switch (cachedBuffer.getOrdinal()) {
                                                    case 1:
                                                        obj6 = 1;
                                                        if (this._method == null) {
                                                            this._close = true;
                                                        }
                                                        obj5 = null;
                                                        if (this._close && this._method == null && this._contentLength == -3) {
                                                            this._contentLength = -1;
                                                            break;
                                                        }
                                                    case 5:
                                                        if (this._version != 10) {
                                                            break;
                                                        }
                                                        obj5 = 1;
                                                        if (this._method != null) {
                                                            break;
                                                        }
                                                        this._close = false;
                                                        break;
                                                    default:
                                                        if (stringBuffer2 == null) {
                                                            stringBuffer2 = new StringBuffer();
                                                        } else {
                                                            stringBuffer2.append(',');
                                                        }
                                                        stringBuffer2.append(trim);
                                                        break;
                                                }
                                            }
                                            if (stringBuffer2 == null) {
                                                stringBuffer2 = new StringBuffer();
                                            } else {
                                                stringBuffer2.append(',');
                                            }
                                            stringBuffer2.append(trim);
                                        }
                                        obj4 = obj6;
                                        obj2 = obj5;
                                        stringBuffer = stringBuffer2;
                                        break;
                                    case 1:
                                        obj4 = 1;
                                        if (this._method == null) {
                                            this._close = true;
                                        }
                                        if (this._close && this._method == null && this._contentLength == -3) {
                                            this._contentLength = -1;
                                            break;
                                        }
                                    case 5:
                                        if (this._version != 10) {
                                            break;
                                        }
                                        obj2 = 1;
                                        if (this._method != null) {
                                            break;
                                        }
                                        this._close = false;
                                        break;
                                    default:
                                        if (stringBuffer == null) {
                                            stringBuffer = new StringBuffer();
                                        } else {
                                            stringBuffer.append(',');
                                        }
                                        stringBuffer.append(field3.getValue());
                                        break;
                                }
                            case 5:
                                if (this._version != 11) {
                                    break;
                                }
                                field = field3;
                                break;
                            case 12:
                                this._contentLength = field3.getLongValue();
                                Field field4 = (this._contentLength < this._contentWritten || (this._last && this._contentLength != this._contentWritten)) ? null : field3;
                                field3.put(this._header);
                                field2 = field4;
                                break;
                            case 16:
                                if (BufferUtil.isPrefix(MimeTypes.MULTIPART_BYTERANGES_BUFFER, field3.getValueBuffer())) {
                                    this._contentLength = -4;
                                }
                                field3.put(this._header);
                                obj3 = 1;
                                break;
                            case 48:
                                if (!getSendServerVersion()) {
                                    break;
                                }
                                field3.put(this._header);
                                obj = 1;
                                break;
                            default:
                                field3.put(this._header);
                                break;
                        }
                    }
                }
                field = null;
                field2 = null;
                stringBuffer = null;
                obj4 = null;
                obj2 = null;
                obj3 = null;
                obj = null;
                switch ((int) this._contentLength) {
                    case -3:
                        if (this._contentWritten != 0 || this._method != null || (this._status >= 200 && this._status != 204 && this._status != 304)) {
                            if (!this._last) {
                                long j = (this._close || this._version < 11) ? -1 : -2;
                                this._contentLength = j;
                                if (this._method != null && this._contentLength == -1) {
                                    this._contentLength = 0;
                                    this._noContent = true;
                                    break;
                                }
                            }
                            this._contentLength = this._contentWritten;
                            if (field2 == null && (this._method == null || r9 != null || this._contentLength > 0)) {
                                this._header.put(HttpHeaders.CONTENT_LENGTH_BUFFER);
                                this._header.put((byte) HttpTokens.COLON);
                                this._header.put((byte) 32);
                                BufferUtil.putDecLong(this._header, this._contentLength);
                                this._header.put(HttpTokens.CRLF);
                                break;
                            }
                        }
                        this._contentLength = 0;
                        break;
                        break;
                    case -1:
                        this._close = this._method == null;
                        break;
                    case 0:
                        if (field2 == null && this._method == null && this._status >= 200 && this._status != 204 && this._status != 304) {
                            this._header.put(CONTENT_LENGTH_0);
                            break;
                        }
                }
                if (this._contentLength == -2) {
                    if (field == null || 2 == field.getValueOrdinal()) {
                        this._header.put(TRANSFER_ENCODING_CHUNKED);
                    } else if (field.getValue().endsWith("chunked")) {
                        field.put(this._header);
                    } else {
                        throw new IllegalArgumentException("BAD TE");
                    }
                }
                if (this._contentLength == -1) {
                    obj2 = null;
                    this._close = true;
                }
                if (this._method == null) {
                    if (this._close && (r5 != null || this._version > 10)) {
                        this._header.put(CONNECTION_CLOSE);
                        if (stringBuffer != null) {
                            this._header.setPutIndex(this._header.putIndex() - 2);
                            this._header.put((byte) 44);
                            this._header.put(stringBuffer.toString().getBytes());
                            this._header.put(CRLF);
                        }
                    } else if (obj2 != null) {
                        this._header.put(CONNECTION_KEEP_ALIVE);
                        if (stringBuffer != null) {
                            this._header.setPutIndex(this._header.putIndex() - 2);
                            this._header.put((byte) 44);
                            this._header.put(stringBuffer.toString().getBytes());
                            this._header.put(CRLF);
                        }
                    } else if (stringBuffer != null) {
                        this._header.put(CONNECTION_);
                        this._header.put(stringBuffer.toString().getBytes());
                        this._header.put(CRLF);
                    }
                }
                if (obj == null && this._status > 100 && getSendServerVersion()) {
                    this._header.put(SERVER);
                }
                this._header.put(HttpTokens.CRLF);
                this._state = 2;
            } else {
                throw new IllegalStateException("last?");
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long flush() throws java.io.IOException {
        /*
        r10 = this;
        r7 = 3;
        r6 = 4;
        r1 = 0;
        r0 = r10._state;	 Catch:{ IOException -> 0x000f }
        if (r0 != 0) goto L_0x0019;
    L_0x0007:
        r0 = new java.lang.IllegalStateException;	 Catch:{ IOException -> 0x000f }
        r1 = "State==HEADER";
        r0.<init>(r1);	 Catch:{ IOException -> 0x000f }
        throw r0;	 Catch:{ IOException -> 0x000f }
    L_0x000f:
        r1 = move-exception;
        org.mortbay.log.Log.ignore(r1);
        r0 = r1 instanceof org.mortbay.jetty.EofException;
        if (r0 == 0) goto L_0x014c;
    L_0x0017:
        r0 = r1;
    L_0x0018:
        throw r0;
    L_0x0019:
        r10.prepareBuffers();	 Catch:{ IOException -> 0x000f }
        r0 = r10._endp;	 Catch:{ IOException -> 0x000f }
        if (r0 != 0) goto L_0x004b;
    L_0x0020:
        r0 = r10._needCRLF;	 Catch:{ IOException -> 0x000f }
        if (r0 == 0) goto L_0x002f;
    L_0x0024:
        r0 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        if (r0 == 0) goto L_0x002f;
    L_0x0028:
        r0 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r1 = org.mortbay.jetty.HttpTokens.CRLF;	 Catch:{ IOException -> 0x000f }
        r0.put(r1);	 Catch:{ IOException -> 0x000f }
    L_0x002f:
        r0 = r10._needEOC;	 Catch:{ IOException -> 0x000f }
        if (r0 == 0) goto L_0x0042;
    L_0x0033:
        r0 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        if (r0 == 0) goto L_0x0042;
    L_0x0037:
        r0 = r10._head;	 Catch:{ IOException -> 0x000f }
        if (r0 != 0) goto L_0x0042;
    L_0x003b:
        r0 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r1 = LAST_CHUNK;	 Catch:{ IOException -> 0x000f }
        r0.put(r1);	 Catch:{ IOException -> 0x000f }
    L_0x0042:
        r0 = 0;
        r10._needCRLF = r0;	 Catch:{ IOException -> 0x000f }
        r0 = 0;
        r10._needEOC = r0;	 Catch:{ IOException -> 0x000f }
        r0 = 0;
    L_0x004a:
        return r0;
    L_0x004b:
        r0 = r1;
    L_0x004c:
        r2 = -1;
        r3 = r10._header;	 Catch:{ IOException -> 0x000f }
        if (r3 == 0) goto L_0x0153;
    L_0x0051:
        r3 = r10._header;	 Catch:{ IOException -> 0x000f }
        r3 = r3.length();	 Catch:{ IOException -> 0x000f }
        if (r3 <= 0) goto L_0x0153;
    L_0x0059:
        r5 = r6;
    L_0x005a:
        r3 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        if (r3 == 0) goto L_0x0156;
    L_0x005e:
        r3 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r3 = r3.length();	 Catch:{ IOException -> 0x000f }
        if (r3 <= 0) goto L_0x0156;
    L_0x0066:
        r3 = 2;
        r4 = r3;
    L_0x0068:
        r3 = r10._bypass;	 Catch:{ IOException -> 0x000f }
        if (r3 == 0) goto L_0x0159;
    L_0x006c:
        r3 = r10._content;	 Catch:{ IOException -> 0x000f }
        if (r3 == 0) goto L_0x0159;
    L_0x0070:
        r3 = r10._content;	 Catch:{ IOException -> 0x000f }
        r3 = r3.length();	 Catch:{ IOException -> 0x000f }
        if (r3 <= 0) goto L_0x0159;
    L_0x0078:
        r3 = 1;
    L_0x0079:
        r4 = r4 | r5;
        r3 = r3 | r4;
        switch(r3) {
            case 0: goto L_0x00c2;
            case 1: goto L_0x00b9;
            case 2: goto L_0x00b0;
            case 3: goto L_0x00aa;
            case 4: goto L_0x00a1;
            case 5: goto L_0x0095;
            case 6: goto L_0x0089;
            case 7: goto L_0x0083;
            default: goto L_0x007e;
        };	 Catch:{ IOException -> 0x000f }
    L_0x007e:
        if (r2 <= 0) goto L_0x0115;
    L_0x0080:
        r0 = r0 + r2;
        r2 = (long) r2;	 Catch:{ IOException -> 0x000f }
        goto L_0x004c;
    L_0x0083:
        r0 = new java.lang.IllegalStateException;	 Catch:{ IOException -> 0x000f }
        r0.<init>();	 Catch:{ IOException -> 0x000f }
        throw r0;	 Catch:{ IOException -> 0x000f }
    L_0x0089:
        r2 = r10._endp;	 Catch:{ IOException -> 0x000f }
        r3 = r10._header;	 Catch:{ IOException -> 0x000f }
        r4 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r5 = 0;
        r2 = r2.flush(r3, r4, r5);	 Catch:{ IOException -> 0x000f }
        goto L_0x007e;
    L_0x0095:
        r2 = r10._endp;	 Catch:{ IOException -> 0x000f }
        r3 = r10._header;	 Catch:{ IOException -> 0x000f }
        r4 = r10._content;	 Catch:{ IOException -> 0x000f }
        r5 = 0;
        r2 = r2.flush(r3, r4, r5);	 Catch:{ IOException -> 0x000f }
        goto L_0x007e;
    L_0x00a1:
        r2 = r10._endp;	 Catch:{ IOException -> 0x000f }
        r3 = r10._header;	 Catch:{ IOException -> 0x000f }
        r2 = r2.flush(r3);	 Catch:{ IOException -> 0x000f }
        goto L_0x007e;
    L_0x00aa:
        r0 = new java.lang.IllegalStateException;	 Catch:{ IOException -> 0x000f }
        r0.<init>();	 Catch:{ IOException -> 0x000f }
        throw r0;	 Catch:{ IOException -> 0x000f }
    L_0x00b0:
        r2 = r10._endp;	 Catch:{ IOException -> 0x000f }
        r3 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r2 = r2.flush(r3);	 Catch:{ IOException -> 0x000f }
        goto L_0x007e;
    L_0x00b9:
        r2 = r10._endp;	 Catch:{ IOException -> 0x000f }
        r3 = r10._content;	 Catch:{ IOException -> 0x000f }
        r2 = r2.flush(r3);	 Catch:{ IOException -> 0x000f }
        goto L_0x007e;
    L_0x00c2:
        r3 = r10._header;	 Catch:{ IOException -> 0x000f }
        if (r3 == 0) goto L_0x00cb;
    L_0x00c6:
        r3 = r10._header;	 Catch:{ IOException -> 0x000f }
        r3.clear();	 Catch:{ IOException -> 0x000f }
    L_0x00cb:
        r3 = 0;
        r10._bypass = r3;	 Catch:{ IOException -> 0x000f }
        r3 = 0;
        r10._bufferChunked = r3;	 Catch:{ IOException -> 0x000f }
        r3 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        if (r3 == 0) goto L_0x0118;
    L_0x00d5:
        r3 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r3.clear();	 Catch:{ IOException -> 0x000f }
        r4 = r10._contentLength;	 Catch:{ IOException -> 0x000f }
        r8 = -2;
        r3 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r3 != 0) goto L_0x0118;
    L_0x00e2:
        r3 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r4 = CHUNK_SPACE;	 Catch:{ IOException -> 0x000f }
        r3.setPutIndex(r4);	 Catch:{ IOException -> 0x000f }
        r3 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r4 = CHUNK_SPACE;	 Catch:{ IOException -> 0x000f }
        r3.setGetIndex(r4);	 Catch:{ IOException -> 0x000f }
        r3 = r10._content;	 Catch:{ IOException -> 0x000f }
        if (r3 == 0) goto L_0x0118;
    L_0x00f4:
        r3 = r10._content;	 Catch:{ IOException -> 0x000f }
        r3 = r3.length();	 Catch:{ IOException -> 0x000f }
        r4 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r4 = r4.space();	 Catch:{ IOException -> 0x000f }
        if (r3 >= r4) goto L_0x0118;
    L_0x0102:
        r3 = r10._state;	 Catch:{ IOException -> 0x000f }
        if (r3 == r7) goto L_0x0118;
    L_0x0106:
        r1 = r10._buffer;	 Catch:{ IOException -> 0x000f }
        r2 = r10._content;	 Catch:{ IOException -> 0x000f }
        r1.put(r2);	 Catch:{ IOException -> 0x000f }
        r1 = r10._content;	 Catch:{ IOException -> 0x000f }
        r1.clear();	 Catch:{ IOException -> 0x000f }
        r1 = 0;
        r10._content = r1;	 Catch:{ IOException -> 0x000f }
    L_0x0115:
        r0 = (long) r0;	 Catch:{ IOException -> 0x000f }
        goto L_0x004a;
    L_0x0118:
        r3 = r10._needCRLF;	 Catch:{ IOException -> 0x000f }
        if (r3 != 0) goto L_0x0147;
    L_0x011c:
        r3 = r10._needEOC;	 Catch:{ IOException -> 0x000f }
        if (r3 != 0) goto L_0x0147;
    L_0x0120:
        r3 = r10._content;	 Catch:{ IOException -> 0x000f }
        if (r3 == 0) goto L_0x012c;
    L_0x0124:
        r3 = r10._content;	 Catch:{ IOException -> 0x000f }
        r3 = r3.length();	 Catch:{ IOException -> 0x000f }
        if (r3 != 0) goto L_0x0147;
    L_0x012c:
        r1 = r10._state;	 Catch:{ IOException -> 0x000f }
        if (r1 != r7) goto L_0x0133;
    L_0x0130:
        r1 = 4;
        r10._state = r1;	 Catch:{ IOException -> 0x000f }
    L_0x0133:
        r1 = r10._state;	 Catch:{ IOException -> 0x000f }
        if (r1 != r6) goto L_0x0115;
    L_0x0137:
        r1 = r10._close;	 Catch:{ IOException -> 0x000f }
        if (r1 == 0) goto L_0x0115;
    L_0x013b:
        r1 = r10._status;	 Catch:{ IOException -> 0x000f }
        r2 = 100;
        if (r1 == r2) goto L_0x0115;
    L_0x0141:
        r1 = r10._endp;	 Catch:{ IOException -> 0x000f }
        r1.shutdownOutput();	 Catch:{ IOException -> 0x000f }
        goto L_0x0115;
    L_0x0147:
        r10.prepareBuffers();	 Catch:{ IOException -> 0x000f }
        goto L_0x007e;
    L_0x014c:
        r0 = new org.mortbay.jetty.EofException;
        r0.<init>(r1);
        goto L_0x0018;
    L_0x0153:
        r5 = r1;
        goto L_0x005a;
    L_0x0156:
        r4 = r1;
        goto L_0x0068;
    L_0x0159:
        r3 = r1;
        goto L_0x0079;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.HttpGenerator.flush():long");
    }

    public int getBytesBuffered() {
        int i = 0;
        int length = this._header == null ? 0 : this._header.length();
        int length2 = this._buffer == null ? 0 : this._buffer.length();
        if (this._content != null) {
            i = this._content.length();
        }
        return (length + length2) + i;
    }

    public boolean isBufferFull() {
        return super.isBufferFull() || this._bufferChunked || this._bypass || (this._contentLength == -2 && this._buffer != null && this._buffer.space() < CHUNK_SPACE);
    }

    public boolean isEmpty() {
        return (this._header == null || this._header.length() == 0) && ((this._buffer == null || this._buffer.length() == 0) && (this._content == null || this._content.length() == 0));
    }

    protected int prepareUncheckedAddContent() throws IOException {
        if (this._noContent || this._last || this._state == 4) {
            return -1;
        }
        Buffer buffer = this._content;
        if ((buffer != null && buffer.length() > 0) || this._bufferChunked) {
            flush();
            if ((buffer != null && buffer.length() > 0) || this._bufferChunked) {
                throw new IllegalStateException("FULL");
            }
        }
        if (this._buffer == null) {
            this._buffer = this._buffers.getBuffer(this._contentBufferSize);
        }
        this._contentWritten -= (long) this._buffer.length();
        if (this._head) {
            return Integer.MAX_VALUE;
        }
        return this._buffer.space() - (this._contentLength == -2 ? CHUNK_SPACE : 0);
    }

    public void reset(boolean z) {
        super.reset(z);
        this._bypass = false;
        this._needCRLF = false;
        this._needEOC = false;
        this._bufferChunked = false;
        this._method = null;
        this._uri = null;
        this._noContent = false;
    }

    public void sendResponse(Buffer buffer) throws IOException {
        if (this._noContent || this._state != 0 || ((this._content != null && this._content.length() > 0) || this._bufferChunked || this._head)) {
            throw new IllegalStateException();
        }
        this._last = true;
        this._content = buffer;
        this._bypass = true;
        this._state = 3;
        long length = (long) buffer.length();
        this._contentWritten = length;
        this._contentLength = length;
    }

    public String toString() {
        return new StringBuffer().append("HttpGenerator s=").append(this._state).append(" h=").append(this._header == null ? "null" : new StringBuffer().append(HttpVersions.HTTP_0_9).append(this._header.length()).toString()).append(" b=").append(this._buffer == null ? "null" : new StringBuffer().append(HttpVersions.HTTP_0_9).append(this._buffer.length()).toString()).append(" c=").append(this._content == null ? "null" : new StringBuffer().append(HttpVersions.HTTP_0_9).append(this._content.length()).toString()).toString();
    }
}
