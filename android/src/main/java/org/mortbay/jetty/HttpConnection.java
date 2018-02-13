package org.mortbay.jetty;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import org.mortbay.io.Buffer;
import org.mortbay.io.BufferCache.CachedBuffer;
import org.mortbay.io.Connection;
import org.mortbay.io.EndPoint;
import org.mortbay.io.nio.SelectChannelEndPoint;
import org.mortbay.jetty.HttpParser.EventHandler;
import org.mortbay.jetty.HttpParser.Input;
import org.mortbay.log.Log;
import org.mortbay.util.QuotedStringTokenizer;
import org.mortbay.util.URIUtil;

public class HttpConnection implements Connection {
    private static int UNKNOWN = -2;
    private static ThreadLocal __currentConnection = new ThreadLocal();
    private Object _associatedObject;
    protected final Connector _connector;
    private transient boolean _delayedHandling = false;
    private boolean _destroy;
    protected final EndPoint _endp;
    private transient int _expect = UNKNOWN;
    protected final Generator _generator;
    private boolean _handling;
    private transient boolean _head = false;
    private transient boolean _host = false;
    protected ServletInputStream _in;
    int _include;
    protected Output _out;
    protected final Parser _parser;
    protected PrintWriter _printWriter;
    protected final Request _request;
    protected final HttpFields _requestFields;
    private int _requests;
    protected final Response _response;
    protected final HttpFields _responseFields;
    protected final Server _server;
    private long _timeStamp = System.currentTimeMillis();
    protected final HttpURI _uri;
    private transient int _version = UNKNOWN;
    protected OutputWriter _writer;

    public class Output extends org.mortbay.jetty.AbstractGenerator.Output {
        private final HttpConnection this$0;

        Output(HttpConnection httpConnection) {
            this.this$0 = httpConnection;
            super((AbstractGenerator) httpConnection._generator, (long) httpConnection._connector.getMaxIdleTime());
        }

        public void close() throws IOException {
            if (!this._closed) {
                if (this.this$0.isIncluding() || this._generator.isCommitted()) {
                    this.this$0.flushResponse();
                } else {
                    this.this$0.commitResponse(true);
                }
                super.close();
            }
        }

        public void flush() throws IOException {
            if (!this._generator.isCommitted()) {
                this.this$0.commitResponse(false);
            }
            super.flush();
        }

        public void print(String str) throws IOException {
            if (this._closed) {
                throw new IOException("Closed");
            }
            this.this$0.getPrintWriter(null).print(str);
        }

        public void sendContent(java.lang.Object r11) throws java.io.IOException {
            /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r10 = this;
            r8 = 0;
            r6 = 1;
            r2 = 0;
            r0 = r10._closed;
            if (r0 == 0) goto L_0x0010;
        L_0x0008:
            r0 = new java.io.IOException;
            r1 = "Closed";
            r0.<init>(r1);
            throw r0;
        L_0x0010:
            r0 = r10._generator;
            r0 = r0.getContentWritten();
            r0 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
            if (r0 <= 0) goto L_0x0022;
        L_0x001a:
            r0 = new java.lang.IllegalStateException;
            r1 = "!empty";
            r0.<init>(r1);
            throw r0;
        L_0x0022:
            r0 = r11 instanceof org.mortbay.jetty.HttpContent;
            if (r0 == 0) goto L_0x0113;
        L_0x0026:
            r11 = (org.mortbay.jetty.HttpContent) r11;
            r1 = r11.getContentType();
            if (r1 == 0) goto L_0x004d;
        L_0x002e:
            r0 = r10.this$0;
            r0 = r0._responseFields;
            r3 = org.mortbay.jetty.HttpHeaders.CONTENT_TYPE_BUFFER;
            r0 = r0.containsKey(r3);
            if (r0 != 0) goto L_0x004d;
        L_0x003a:
            r0 = r10.this$0;
            r0 = r0._response;
            r3 = r0.getSetCharacterEncoding();
            if (r3 != 0) goto L_0x0095;
        L_0x0044:
            r0 = r10.this$0;
            r0 = r0._responseFields;
            r3 = org.mortbay.jetty.HttpHeaders.CONTENT_TYPE_BUFFER;
            r0.add(r3, r1);
        L_0x004d:
            r0 = r11.getContentLength();
            r0 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
            if (r0 <= 0) goto L_0x0062;
        L_0x0055:
            r0 = r10.this$0;
            r0 = r0._responseFields;
            r1 = org.mortbay.jetty.HttpHeaders.CONTENT_LENGTH_BUFFER;
            r4 = r11.getContentLength();
            r0.putLongField(r1, r4);
        L_0x0062:
            r0 = r11.getLastModified();
            r1 = r11.getResource();
            r4 = r1.lastModified();
            if (r0 == 0) goto L_0x00fc;
        L_0x0070:
            r1 = r10.this$0;
            r1 = r1._responseFields;
            r3 = org.mortbay.jetty.HttpHeaders.LAST_MODIFIED_BUFFER;
            r1.put(r3, r0, r4);
        L_0x0079:
            r0 = r11.getBuffer();
            if (r0 != 0) goto L_0x0190;
        L_0x007f:
            r0 = r11.getInputStream();
            r11 = r2;
        L_0x0084:
            r1 = r0 instanceof org.mortbay.io.Buffer;
            if (r1 == 0) goto L_0x012c;
        L_0x0088:
            r1 = r10._generator;
            r0 = (org.mortbay.io.Buffer) r0;
            r1.addContent(r0, r6);
            r0 = r10.this$0;
            r0.commitResponse(r6);
        L_0x0094:
            return;
        L_0x0095:
            r0 = r1 instanceof org.mortbay.io.BufferCache.CachedBuffer;
            if (r0 == 0) goto L_0x00d4;
        L_0x0099:
            r0 = r1;
            r0 = (org.mortbay.io.BufferCache.CachedBuffer) r0;
            r0 = r0.getAssociate(r3);
            if (r0 == 0) goto L_0x00ac;
        L_0x00a2:
            r1 = r10.this$0;
            r1 = r1._responseFields;
            r3 = org.mortbay.jetty.HttpHeaders.CONTENT_TYPE_BUFFER;
            r1.put(r3, r0);
            goto L_0x004d;
        L_0x00ac:
            r0 = r10.this$0;
            r0 = r0._responseFields;
            r4 = org.mortbay.jetty.HttpHeaders.CONTENT_TYPE_BUFFER;
            r5 = new java.lang.StringBuffer;
            r5.<init>();
            r1 = r5.append(r1);
            r5 = ";charset=";
            r1 = r1.append(r5);
            r5 = ";= ";
            r3 = org.mortbay.util.QuotedStringTokenizer.quote(r3, r5);
            r1 = r1.append(r3);
            r1 = r1.toString();
            r0.put(r4, r1);
            goto L_0x004d;
        L_0x00d4:
            r0 = r10.this$0;
            r0 = r0._responseFields;
            r4 = org.mortbay.jetty.HttpHeaders.CONTENT_TYPE_BUFFER;
            r5 = new java.lang.StringBuffer;
            r5.<init>();
            r1 = r5.append(r1);
            r5 = ";charset=";
            r1 = r1.append(r5);
            r5 = ";= ";
            r3 = org.mortbay.util.QuotedStringTokenizer.quote(r3, r5);
            r1 = r1.append(r3);
            r1 = r1.toString();
            r0.put(r4, r1);
            goto L_0x004d;
        L_0x00fc:
            r0 = r11.getResource();
            if (r0 == 0) goto L_0x0079;
        L_0x0102:
            r0 = -1;
            r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
            if (r0 == 0) goto L_0x0079;
        L_0x0108:
            r0 = r10.this$0;
            r0 = r0._responseFields;
            r1 = org.mortbay.jetty.HttpHeaders.LAST_MODIFIED_BUFFER;
            r0.putDateField(r1, r4);
            goto L_0x0079;
        L_0x0113:
            r0 = r11 instanceof org.mortbay.resource.Resource;
            if (r0 == 0) goto L_0x018c;
        L_0x0117:
            r11 = (org.mortbay.resource.Resource) r11;
            r0 = r10.this$0;
            r0 = r0._responseFields;
            r1 = org.mortbay.jetty.HttpHeaders.LAST_MODIFIED_BUFFER;
            r2 = r11.lastModified();
            r0.putDateField(r1, r2);
            r0 = r11.getInputStream();
            goto L_0x0084;
        L_0x012c:
            r1 = r0 instanceof java.io.InputStream;
            if (r1 == 0) goto L_0x0184;
        L_0x0130:
            r0 = (java.io.InputStream) r0;
            r1 = r10._generator;	 Catch:{ all -> 0x0179 }
            r1 = r1.prepareUncheckedAddContent();	 Catch:{ all -> 0x0179 }
            r2 = r10._generator;	 Catch:{ all -> 0x0179 }
            r2 = r2.getUncheckedBuffer();	 Catch:{ all -> 0x0179 }
            r1 = r2.readFrom(r0, r1);	 Catch:{ all -> 0x0179 }
        L_0x0142:
            if (r1 < 0) goto L_0x0161;	 Catch:{ all -> 0x0179 }
        L_0x0144:
            r1 = r10._generator;	 Catch:{ all -> 0x0179 }
            r1.completeUncheckedAddContent();	 Catch:{ all -> 0x0179 }
            r1 = r10.this$0;	 Catch:{ all -> 0x0179 }
            r1 = r1._out;	 Catch:{ all -> 0x0179 }
            r1.flush();	 Catch:{ all -> 0x0179 }
            r1 = r10._generator;	 Catch:{ all -> 0x0179 }
            r1 = r1.prepareUncheckedAddContent();	 Catch:{ all -> 0x0179 }
            r2 = r10._generator;	 Catch:{ all -> 0x0179 }
            r2 = r2.getUncheckedBuffer();	 Catch:{ all -> 0x0179 }
            r1 = r2.readFrom(r0, r1);	 Catch:{ all -> 0x0179 }
            goto L_0x0142;	 Catch:{ all -> 0x0179 }
        L_0x0161:
            r1 = r10._generator;	 Catch:{ all -> 0x0179 }
            r1.completeUncheckedAddContent();	 Catch:{ all -> 0x0179 }
            r1 = r10.this$0;	 Catch:{ all -> 0x0179 }
            r1 = r1._out;	 Catch:{ all -> 0x0179 }
            r1.flush();	 Catch:{ all -> 0x0179 }
            if (r11 == 0) goto L_0x0174;
        L_0x016f:
            r11.release();
            goto L_0x0094;
        L_0x0174:
            r0.close();
            goto L_0x0094;
        L_0x0179:
            r1 = move-exception;
            if (r11 == 0) goto L_0x0180;
        L_0x017c:
            r11.release();
        L_0x017f:
            throw r1;
        L_0x0180:
            r0.close();
            goto L_0x017f;
        L_0x0184:
            r0 = new java.lang.IllegalArgumentException;
            r1 = "unknown content type?";
            r0.<init>(r1);
            throw r0;
        L_0x018c:
            r0 = r11;
            r11 = r2;
            goto L_0x0084;
        L_0x0190:
            r11 = r2;
            goto L_0x0084;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.HttpConnection.Output.sendContent(java.lang.Object):void");
        }

        public void sendResponse(Buffer buffer) throws IOException {
            ((HttpGenerator) this._generator).sendResponse(buffer);
        }
    }

    public class OutputWriter extends org.mortbay.jetty.AbstractGenerator.OutputWriter {
        private final HttpConnection this$0;

        OutputWriter(HttpConnection httpConnection) {
            this.this$0 = httpConnection;
            super(httpConnection._out);
        }
    }

    private class RequestHandler extends EventHandler {
        private String _charset;
        private final HttpConnection this$0;

        private RequestHandler(HttpConnection httpConnection) {
            this.this$0 = httpConnection;
        }

        RequestHandler(HttpConnection httpConnection, C13231 c13231) {
            this(httpConnection);
        }

        public void content(Buffer buffer) throws IOException {
            if (this.this$0._endp instanceof SelectChannelEndPoint) {
                ((SelectChannelEndPoint) this.this$0._endp).scheduleIdle();
            }
            if (HttpConnection.access$400(this.this$0)) {
                HttpConnection.access$402(this.this$0, false);
                this.this$0.handleRequest();
            }
        }

        public void headerComplete() throws IOException {
            if (this.this$0._endp instanceof SelectChannelEndPoint) {
                ((SelectChannelEndPoint) this.this$0._endp).scheduleIdle();
            }
            HttpConnection.access$708(this.this$0);
            this.this$0._generator.setVersion(HttpConnection.access$500(this.this$0));
            switch (HttpConnection.access$500(this.this$0)) {
                case 10:
                    this.this$0._generator.setHead(HttpConnection.access$600(this.this$0));
                    break;
                case 11:
                    this.this$0._generator.setHead(HttpConnection.access$600(this.this$0));
                    if (this.this$0._server.getSendDateHeader()) {
                        this.this$0._responseFields.put(HttpHeaders.DATE_BUFFER, this.this$0._request.getTimeStampBuffer(), this.this$0._request.getTimeStamp());
                    }
                    if (!HttpConnection.access$100(this.this$0)) {
                        this.this$0._generator.setResponse(400, null);
                        this.this$0._responseFields.put(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.CLOSE_BUFFER);
                        this.this$0._generator.completeHeader(this.this$0._responseFields, true);
                        this.this$0._generator.complete();
                        return;
                    } else if (HttpConnection.access$200(this.this$0) != HttpConnection.access$300()) {
                        if (HttpConnection.access$200(this.this$0) == 6) {
                            if (((HttpParser) this.this$0._parser).getHeaderBuffer() == null || ((HttpParser) this.this$0._parser).getHeaderBuffer().length() < 2) {
                                this.this$0._generator.setResponse(100, null);
                                this.this$0._generator.completeHeader(null, true);
                                this.this$0._generator.complete();
                                this.this$0._generator.reset(false);
                                break;
                            }
                        } else if (HttpConnection.access$200(this.this$0) != 7) {
                            this.this$0._generator.setResponse(417, null);
                            this.this$0._responseFields.put(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.CLOSE_BUFFER);
                            this.this$0._generator.completeHeader(this.this$0._responseFields, true);
                            this.this$0._generator.complete();
                            return;
                        }
                    }
                    break;
            }
            if (this._charset != null) {
                this.this$0._request.setCharacterEncodingUnchecked(this._charset);
            }
            if (((HttpParser) this.this$0._parser).getContentLength() > 0 || ((HttpParser) this.this$0._parser).isChunking()) {
                HttpConnection.access$402(this.this$0, true);
            } else {
                this.this$0.handleRequest();
            }
        }

        public void messageComplete(long j) throws IOException {
            if (HttpConnection.access$400(this.this$0)) {
                HttpConnection.access$402(this.this$0, false);
                this.this$0.handleRequest();
            }
        }

        public void parsedHeader(Buffer buffer, Buffer buffer2) {
            switch (HttpHeaders.CACHE.getOrdinal(buffer)) {
                case 1:
                    switch (HttpHeaderValues.CACHE.getOrdinal(buffer2)) {
                        case -1:
                            QuotedStringTokenizer quotedStringTokenizer = new QuotedStringTokenizer(buffer2.toString(), ",");
                            while (quotedStringTokenizer.hasMoreTokens()) {
                                CachedBuffer cachedBuffer = HttpHeaderValues.CACHE.get(quotedStringTokenizer.nextToken().trim());
                                if (cachedBuffer != null) {
                                    switch (cachedBuffer.getOrdinal()) {
                                        case 1:
                                            this.this$0._responseFields.add(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.CLOSE_BUFFER);
                                            this.this$0._generator.setPersistent(false);
                                            break;
                                        case 5:
                                            if (HttpConnection.access$500(this.this$0) != 10) {
                                                break;
                                            }
                                            this.this$0._responseFields.add(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.KEEP_ALIVE_BUFFER);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                            break;
                        case 1:
                            this.this$0._responseFields.put(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.CLOSE_BUFFER);
                            this.this$0._generator.setPersistent(false);
                            break;
                        case 5:
                            if (HttpConnection.access$500(this.this$0) == 10) {
                                this.this$0._responseFields.put(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.KEEP_ALIVE_BUFFER);
                                break;
                            }
                            break;
                        default:
                            break;
                    }
                case 16:
                    buffer2 = MimeTypes.CACHE.lookup(buffer2);
                    this._charset = MimeTypes.getCharsetFromContentType(buffer2);
                    break;
                case 21:
                case 40:
                    buffer2 = HttpHeaderValues.CACHE.lookup(buffer2);
                    break;
                case 24:
                    buffer2 = HttpHeaderValues.CACHE.lookup(buffer2);
                    HttpConnection.access$202(this.this$0, HttpHeaderValues.CACHE.getOrdinal(buffer2));
                    break;
                case 27:
                    HttpConnection.access$102(this.this$0, true);
                    break;
            }
            this.this$0._requestFields.add(buffer, buffer2);
        }

        public void startRequest(Buffer buffer, Buffer buffer2, Buffer buffer3) throws IOException {
            boolean z = false;
            HttpConnection.access$102(this.this$0, false);
            HttpConnection.access$202(this.this$0, HttpConnection.access$300());
            HttpConnection.access$402(this.this$0, false);
            this._charset = null;
            if (this.this$0._request.getTimeStamp() == 0) {
                this.this$0._request.setTimeStamp(System.currentTimeMillis());
            }
            this.this$0._request.setMethod(buffer.toString());
            try {
                this.this$0._uri.parse(buffer2.array(), buffer2.getIndex(), buffer2.length());
                this.this$0._request.setUri(this.this$0._uri);
                if (buffer3 == null) {
                    this.this$0._request.setProtocol(HttpVersions.HTTP_0_9);
                    HttpConnection.access$502(this.this$0, 9);
                } else {
                    Buffer buffer4 = HttpVersions.CACHE.get(buffer3);
                    HttpConnection.access$502(this.this$0, HttpVersions.CACHE.getOrdinal(buffer4));
                    if (HttpConnection.access$500(this.this$0) <= 0) {
                        HttpConnection.access$502(this.this$0, 10);
                    }
                    this.this$0._request.setProtocol(buffer4.toString());
                }
                HttpConnection httpConnection = this.this$0;
                if (buffer == HttpMethods.HEAD_BUFFER) {
                    z = true;
                }
                HttpConnection.access$602(httpConnection, z);
            } catch (Throwable e) {
                Log.debug(e);
                throw new HttpException(400, null, e);
            }
        }

        public void startResponse(Buffer buffer, int i, Buffer buffer2) {
            Log.debug(new StringBuffer().append("Bad request!: ").append(buffer).append(" ").append(i).append(" ").append(buffer2).toString());
        }
    }

    public HttpConnection(Connector connector, EndPoint endPoint, Server server) {
        this._uri = URIUtil.__CHARSET == "UTF-8" ? new HttpURI() : new EncodedHttpURI(URIUtil.__CHARSET);
        this._connector = connector;
        this._endp = endPoint;
        this._parser = new HttpParser(this._connector, endPoint, new RequestHandler(this, null), this._connector.getHeaderBufferSize(), this._connector.getRequestBufferSize());
        this._requestFields = new HttpFields();
        this._responseFields = new HttpFields();
        this._request = new Request(this);
        this._response = new Response(this);
        this._generator = new HttpGenerator(this._connector, this._endp, this._connector.getHeaderBufferSize(), this._connector.getResponseBufferSize());
        this._generator.setSendServerVersion(server.getSendServerVersion());
        this._server = server;
    }

    protected HttpConnection(Connector connector, EndPoint endPoint, Server server, Parser parser, Generator generator, Request request) {
        this._uri = URIUtil.__CHARSET == "UTF-8" ? new HttpURI() : new EncodedHttpURI(URIUtil.__CHARSET);
        this._connector = connector;
        this._endp = endPoint;
        this._parser = parser;
        this._requestFields = new HttpFields();
        this._responseFields = new HttpFields();
        this._request = request;
        this._response = new Response(this);
        this._generator = generator;
        this._generator.setSendServerVersion(server.getSendServerVersion());
        this._server = server;
    }

    static boolean access$100(HttpConnection httpConnection) {
        return httpConnection._host;
    }

    static boolean access$102(HttpConnection httpConnection, boolean z) {
        httpConnection._host = z;
        return z;
    }

    static int access$200(HttpConnection httpConnection) {
        return httpConnection._expect;
    }

    static int access$202(HttpConnection httpConnection, int i) {
        httpConnection._expect = i;
        return i;
    }

    static int access$300() {
        return UNKNOWN;
    }

    static boolean access$400(HttpConnection httpConnection) {
        return httpConnection._delayedHandling;
    }

    static boolean access$402(HttpConnection httpConnection, boolean z) {
        httpConnection._delayedHandling = z;
        return z;
    }

    static int access$500(HttpConnection httpConnection) {
        return httpConnection._version;
    }

    static int access$502(HttpConnection httpConnection, int i) {
        httpConnection._version = i;
        return i;
    }

    static boolean access$600(HttpConnection httpConnection) {
        return httpConnection._head;
    }

    static boolean access$602(HttpConnection httpConnection, boolean z) {
        httpConnection._head = z;
        return z;
    }

    static int access$708(HttpConnection httpConnection) {
        int i = httpConnection._requests;
        httpConnection._requests = i + 1;
        return i;
    }

    public static HttpConnection getCurrentConnection() {
        return (HttpConnection) __currentConnection.get();
    }

    protected static void setCurrentConnection(HttpConnection httpConnection) {
        __currentConnection.set(httpConnection);
    }

    public void commitResponse(boolean z) throws IOException {
        if (!this._generator.isCommitted()) {
            this._generator.setResponse(this._response.getStatus(), this._response.getReason());
            try {
                this._generator.completeHeader(this._responseFields, z);
            } catch (IOException e) {
                throw e;
            } catch (RuntimeException e2) {
                RuntimeException runtimeException = e2;
                Log.warn(new StringBuffer().append("header full: ").append(runtimeException).toString());
                if (Log.isDebugEnabled() && (this._generator instanceof HttpGenerator)) {
                    Log.debug(((HttpGenerator) this._generator)._header.toDetailString(), runtimeException);
                }
                this._response.reset();
                this._generator.reset(true);
                this._generator.setResponse(500, null);
                this._generator.completeHeader(this._responseFields, true);
                this._generator.complete();
                throw runtimeException;
            }
        }
        if (z) {
            this._generator.complete();
        }
    }

    public void completeResponse() throws IOException {
        if (!this._generator.isCommitted()) {
            this._generator.setResponse(this._response.getStatus(), this._response.getReason());
            try {
                this._generator.completeHeader(this._responseFields, true);
            } catch (IOException e) {
                throw e;
            } catch (Throwable e2) {
                Log.warn(new StringBuffer().append("header full: ").append(e2).toString());
                Log.debug(e2);
                this._response.reset();
                this._generator.reset(true);
                this._generator.setResponse(500, null);
                this._generator.completeHeader(this._responseFields, true);
                this._generator.complete();
                throw e2;
            }
        }
        this._generator.complete();
    }

    public void destroy() {
        synchronized (this) {
            this._destroy = true;
            if (!this._handling) {
                if (this._parser != null) {
                    this._parser.reset(true);
                }
                if (this._generator != null) {
                    this._generator.reset(true);
                }
                if (this._requestFields != null) {
                    this._requestFields.destroy();
                }
                if (this._responseFields != null) {
                    this._responseFields.destroy();
                }
            }
        }
    }

    public void flushResponse() throws IOException {
        try {
            commitResponse(false);
            this._generator.flush();
        } catch (Throwable e) {
            throw (e instanceof EofException ? e : new EofException(e));
        }
    }

    public Object getAssociatedObject() {
        return this._associatedObject;
    }

    public Connector getConnector() {
        return this._connector;
    }

    public EndPoint getEndPoint() {
        return this._endp;
    }

    public Generator getGenerator() {
        return this._generator;
    }

    public ServletInputStream getInputStream() {
        if (this._in == null) {
            this._in = new Input((HttpParser) this._parser, (long) this._connector.getMaxIdleTime());
        }
        return this._in;
    }

    public ServletOutputStream getOutputStream() {
        if (this._out == null) {
            this._out = new Output(this);
        }
        return this._out;
    }

    public Parser getParser() {
        return this._parser;
    }

    public PrintWriter getPrintWriter(String str) {
        getOutputStream();
        if (this._writer == null) {
            this._writer = new OutputWriter(this);
            this._printWriter = new PrintWriter(this, this._writer) {
                private final HttpConnection this$0;

                public void close() {
                    try {
                        this.out.close();
                    } catch (Throwable e) {
                        Log.debug(e);
                        setError();
                    }
                }
            };
        }
        this._writer.setCharacterEncoding(str);
        return this._printWriter;
    }

    public Request getRequest() {
        return this._request;
    }

    public HttpFields getRequestFields() {
        return this._requestFields;
    }

    public int getRequests() {
        return this._requests;
    }

    public boolean getResolveNames() {
        return this._connector.getResolveNames();
    }

    public Response getResponse() {
        return this._response;
    }

    public HttpFields getResponseFields() {
        return this._responseFields;
    }

    public long getTimeStamp() {
        return this._timeStamp;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handle() throws java.io.IOException {
        /*
        r11 = this;
        r6 = 0;
        r10 = 0;
        r2 = 1;
        r1 = 0;
        r3 = r2;
        r0 = r1;
    L_0x0007:
        if (r3 == 0) goto L_0x008e;
    L_0x0009:
        monitor-enter(r11);	 Catch:{ HttpException -> 0x0017 }
        r3 = r11._handling;	 Catch:{ all -> 0x0014 }
        if (r3 == 0) goto L_0x008f;
    L_0x000e:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0014 }
        r0.<init>();	 Catch:{ all -> 0x0014 }
        throw r0;	 Catch:{ all -> 0x0014 }
    L_0x0014:
        r0 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0014 }
        throw r0;	 Catch:{ HttpException -> 0x0017 }
    L_0x0017:
        r0 = move-exception;
        r3 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ all -> 0x006c }
        if (r3 == 0) goto L_0x0051;
    L_0x001e:
        r3 = new java.lang.StringBuffer;	 Catch:{ all -> 0x006c }
        r3.<init>();	 Catch:{ all -> 0x006c }
        r4 = "uri=";
        r3 = r3.append(r4);	 Catch:{ all -> 0x006c }
        r4 = r11._uri;	 Catch:{ all -> 0x006c }
        r3 = r3.append(r4);	 Catch:{ all -> 0x006c }
        r3 = r3.toString();	 Catch:{ all -> 0x006c }
        org.mortbay.log.Log.debug(r3);	 Catch:{ all -> 0x006c }
        r3 = new java.lang.StringBuffer;	 Catch:{ all -> 0x006c }
        r3.<init>();	 Catch:{ all -> 0x006c }
        r4 = "fields=";
        r3 = r3.append(r4);	 Catch:{ all -> 0x006c }
        r4 = r11._requestFields;	 Catch:{ all -> 0x006c }
        r3 = r3.append(r4);	 Catch:{ all -> 0x006c }
        r3 = r3.toString();	 Catch:{ all -> 0x006c }
        org.mortbay.log.Log.debug(r3);	 Catch:{ all -> 0x006c }
        org.mortbay.log.Log.debug(r0);	 Catch:{ all -> 0x006c }
    L_0x0051:
        r3 = r11._generator;	 Catch:{ all -> 0x006c }
        r4 = r0.getStatus();	 Catch:{ all -> 0x006c }
        r5 = r0.getReason();	 Catch:{ all -> 0x006c }
        r6 = 0;
        r7 = 1;
        r3.sendError(r4, r5, r6, r7);	 Catch:{ all -> 0x006c }
        r3 = r11._parser;	 Catch:{ all -> 0x006c }
        r4 = 1;
        r3.reset(r4);	 Catch:{ all -> 0x006c }
        r3 = r11._endp;	 Catch:{ all -> 0x006c }
        r3.close();	 Catch:{ all -> 0x006c }
        throw r0;	 Catch:{ all -> 0x006c }
    L_0x006c:
        r0 = move-exception;
        r3 = r0;
        setCurrentConnection(r10);
        r0 = r11._parser;
        r0 = r0.isMoreInBuffer();
        if (r0 != 0) goto L_0x0081;
    L_0x0079:
        r0 = r11._endp;
        r0 = r0.isBufferingInput();
        if (r0 == 0) goto L_0x017c;
    L_0x0081:
        r0 = r2;
    L_0x0082:
        monitor-enter(r11);
        r4 = 0;
        r11._handling = r4;	 Catch:{ all -> 0x0179 }
        r4 = r11._destroy;	 Catch:{ all -> 0x0179 }
        if (r4 == 0) goto L_0x017f;
    L_0x008a:
        r11.destroy();	 Catch:{ all -> 0x0179 }
        monitor-exit(r11);	 Catch:{ all -> 0x0179 }
    L_0x008e:
        return;
    L_0x008f:
        r3 = 1;
        r11._handling = r3;	 Catch:{ all -> 0x0014 }
        monitor-exit(r11);	 Catch:{ all -> 0x0014 }
        setCurrentConnection(r11);	 Catch:{ HttpException -> 0x0017 }
        r3 = r11._request;	 Catch:{ HttpException -> 0x0017 }
        r3 = r3.getContinuation();	 Catch:{ HttpException -> 0x0017 }
        if (r3 == 0) goto L_0x00de;
    L_0x009e:
        r4 = r3.isPending();	 Catch:{ HttpException -> 0x0017 }
        if (r4 == 0) goto L_0x00de;
    L_0x00a4:
        r4 = "resume continuation {}";
        org.mortbay.log.Log.debug(r4, r3);	 Catch:{ HttpException -> 0x0017 }
        r3 = r11._request;	 Catch:{ HttpException -> 0x0017 }
        r3 = r3.getMethod();	 Catch:{ HttpException -> 0x0017 }
        if (r3 != 0) goto L_0x00b7;
    L_0x00b1:
        r0 = new java.lang.IllegalStateException;	 Catch:{ HttpException -> 0x0017 }
        r0.<init>();	 Catch:{ HttpException -> 0x0017 }
        throw r0;	 Catch:{ HttpException -> 0x0017 }
    L_0x00b7:
        r11.handleRequest();	 Catch:{ HttpException -> 0x0017 }
    L_0x00ba:
        setCurrentConnection(r10);
        r3 = r11._parser;
        r3 = r3.isMoreInBuffer();
        if (r3 != 0) goto L_0x00cd;
    L_0x00c5:
        r3 = r11._endp;
        r3 = r3.isBufferingInput();
        if (r3 == 0) goto L_0x01cf;
    L_0x00cd:
        r3 = r2;
    L_0x00ce:
        monitor-enter(r11);
        r4 = 0;
        r11._handling = r4;	 Catch:{ all -> 0x00db }
        r4 = r11._destroy;	 Catch:{ all -> 0x00db }
        if (r4 == 0) goto L_0x01d2;
    L_0x00d6:
        r11.destroy();	 Catch:{ all -> 0x00db }
        monitor-exit(r11);	 Catch:{ all -> 0x00db }
        goto L_0x008e;
    L_0x00db:
        r0 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x00db }
        throw r0;
    L_0x00de:
        r3 = r11._parser;	 Catch:{ HttpException -> 0x0017 }
        r3 = r3.isComplete();	 Catch:{ HttpException -> 0x0017 }
        if (r3 != 0) goto L_0x02c1;
    L_0x00e6:
        r3 = r11._parser;	 Catch:{ HttpException -> 0x0017 }
        r4 = r3.parseAvailable();	 Catch:{ HttpException -> 0x0017 }
    L_0x00ec:
        r3 = r11._generator;	 Catch:{ HttpException -> 0x0017 }
        r3 = r3.isCommitted();	 Catch:{ HttpException -> 0x0017 }
        if (r3 == 0) goto L_0x0107;
    L_0x00f4:
        r3 = r11._generator;	 Catch:{ HttpException -> 0x0017 }
        r3 = r3.isComplete();	 Catch:{ HttpException -> 0x0017 }
        if (r3 != 0) goto L_0x0107;
    L_0x00fc:
        r3 = r11._generator;	 Catch:{ HttpException -> 0x0017 }
        r8 = r3.flush();	 Catch:{ HttpException -> 0x0017 }
        r4 = r4 + r8;
        r3 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
        if (r3 > 0) goto L_0x0123;
    L_0x0107:
        r3 = r11._endp;	 Catch:{ HttpException -> 0x0017 }
        r3 = r3.isBufferingOutput();	 Catch:{ HttpException -> 0x0017 }
        if (r3 == 0) goto L_0x02b5;
    L_0x010f:
        r3 = r11._endp;	 Catch:{ HttpException -> 0x0017 }
        r3.flush();	 Catch:{ HttpException -> 0x0017 }
        r3 = r11._endp;	 Catch:{ HttpException -> 0x0017 }
        r3 = r3.isBufferingOutput();	 Catch:{ HttpException -> 0x0017 }
        if (r3 != 0) goto L_0x02b5;
    L_0x011c:
        r3 = r1;
    L_0x011d:
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 <= 0) goto L_0x0131;
    L_0x0121:
        r0 = r1;
        goto L_0x00ba;
    L_0x0123:
        r3 = r11._endp;	 Catch:{ HttpException -> 0x0017 }
        r3 = r3.isBufferingOutput();	 Catch:{ HttpException -> 0x0017 }
        if (r3 == 0) goto L_0x00ec;
    L_0x012b:
        r3 = r11._endp;	 Catch:{ HttpException -> 0x0017 }
        r3.flush();	 Catch:{ HttpException -> 0x0017 }
        goto L_0x00ec;
    L_0x0131:
        r0 = r3 + 1;
        r4 = 2;
        if (r3 < r4) goto L_0x00ba;
    L_0x0136:
        setCurrentConnection(r10);
        r0 = r11._parser;
        r0 = r0.isMoreInBuffer();
        if (r0 != 0) goto L_0x0149;
    L_0x0141:
        r0 = r11._endp;
        r0 = r0.isBufferingInput();
        if (r0 == 0) goto L_0x0245;
    L_0x0149:
        r0 = r2;
    L_0x014a:
        monitor-enter(r11);
        r3 = 0;
        r11._handling = r3;	 Catch:{ all -> 0x0158 }
        r3 = r11._destroy;	 Catch:{ all -> 0x0158 }
        if (r3 == 0) goto L_0x0248;
    L_0x0152:
        r11.destroy();	 Catch:{ all -> 0x0158 }
        monitor-exit(r11);	 Catch:{ all -> 0x0158 }
        goto L_0x008e;
    L_0x0158:
        r0 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0158 }
        throw r0;
    L_0x015b:
        r0 = r11._generator;
        r0 = r0.isCommitted();
        if (r0 == 0) goto L_0x0178;
    L_0x0163:
        r0 = r11._generator;
        r0 = r0.isComplete();
        if (r0 != 0) goto L_0x0178;
    L_0x016b:
        r0 = r11._endp;
        r0 = r0 instanceof org.mortbay.io.nio.SelectChannelEndPoint;
        if (r0 == 0) goto L_0x0178;
    L_0x0171:
        r0 = r11._endp;
        r0 = (org.mortbay.io.nio.SelectChannelEndPoint) r0;
        r0.setWritable(r1);
    L_0x0178:
        throw r3;
    L_0x0179:
        r0 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0179 }
        throw r0;
    L_0x017c:
        r0 = r1;
        goto L_0x0082;
    L_0x017f:
        monitor-exit(r11);	 Catch:{ all -> 0x0179 }
        r4 = r11._parser;
        r4 = r4.isComplete();
        if (r4 == 0) goto L_0x01bb;
    L_0x0188:
        r4 = r11._generator;
        r4 = r4.isComplete();
        if (r4 == 0) goto L_0x01bb;
    L_0x0190:
        r4 = r11._endp;
        r4 = r4.isBufferingOutput();
        if (r4 != 0) goto L_0x01bb;
    L_0x0198:
        r4 = r11._generator;
        r4 = r4.isPersistent();
        if (r4 != 0) goto L_0x01a6;
    L_0x01a0:
        r0 = r11._parser;
        r0.reset(r2);
        r0 = r1;
    L_0x01a6:
        if (r0 == 0) goto L_0x01cb;
    L_0x01a8:
        r11.reset(r1);
        r0 = r11._parser;
        r0 = r0.isMoreInBuffer();
        if (r0 != 0) goto L_0x01bb;
    L_0x01b3:
        r0 = r11._endp;
        r0 = r0.isBufferingInput();
        if (r0 == 0) goto L_0x01bb;
    L_0x01bb:
        r0 = r11._request;
        r0 = r0.getContinuation();
        if (r0 == 0) goto L_0x015b;
    L_0x01c3:
        r0 = r0.isPending();
        if (r0 == 0) goto L_0x015b;
    L_0x01c9:
        goto L_0x008e;
    L_0x01cb:
        r11.reset(r2);
        goto L_0x01bb;
    L_0x01cf:
        r3 = r1;
        goto L_0x00ce;
    L_0x01d2:
        monitor-exit(r11);	 Catch:{ all -> 0x00db }
        r4 = r11._parser;
        r4 = r4.isComplete();
        if (r4 == 0) goto L_0x02be;
    L_0x01db:
        r4 = r11._generator;
        r4 = r4.isComplete();
        if (r4 == 0) goto L_0x02be;
    L_0x01e3:
        r4 = r11._endp;
        r4 = r4.isBufferingOutput();
        if (r4 != 0) goto L_0x02be;
    L_0x01eb:
        r0 = r11._generator;
        r0 = r0.isPersistent();
        if (r0 != 0) goto L_0x02bb;
    L_0x01f3:
        r0 = r11._parser;
        r0.reset(r2);
        r0 = r1;
    L_0x01f9:
        if (r0 == 0) goto L_0x023f;
    L_0x01fb:
        r11.reset(r1);
        r0 = r11._parser;
        r0 = r0.isMoreInBuffer();
        if (r0 != 0) goto L_0x020e;
    L_0x0206:
        r0 = r11._endp;
        r0 = r0.isBufferingInput();
        if (r0 == 0) goto L_0x0243;
    L_0x020e:
        r0 = r2;
    L_0x020f:
        r3 = r0;
        r4 = r1;
    L_0x0211:
        r0 = r11._request;
        r0 = r0.getContinuation();
        if (r0 == 0) goto L_0x021f;
    L_0x0219:
        r0 = r0.isPending();
        if (r0 != 0) goto L_0x008e;
    L_0x021f:
        r0 = r11._generator;
        r0 = r0.isCommitted();
        if (r0 == 0) goto L_0x02b8;
    L_0x0227:
        r0 = r11._generator;
        r0 = r0.isComplete();
        if (r0 != 0) goto L_0x02b8;
    L_0x022f:
        r0 = r11._endp;
        r0 = r0 instanceof org.mortbay.io.nio.SelectChannelEndPoint;
        if (r0 == 0) goto L_0x02b8;
    L_0x0235:
        r0 = r11._endp;
        r0 = (org.mortbay.io.nio.SelectChannelEndPoint) r0;
        r0.setWritable(r1);
        r0 = r4;
        goto L_0x0007;
    L_0x023f:
        r11.reset(r2);
        goto L_0x020f;
    L_0x0243:
        r0 = r1;
        goto L_0x020f;
    L_0x0245:
        r0 = r1;
        goto L_0x014a;
    L_0x0248:
        monitor-exit(r11);	 Catch:{ all -> 0x0158 }
        r3 = r11._parser;
        r3 = r3.isComplete();
        if (r3 == 0) goto L_0x0284;
    L_0x0251:
        r3 = r11._generator;
        r3 = r3.isComplete();
        if (r3 == 0) goto L_0x0284;
    L_0x0259:
        r3 = r11._endp;
        r3 = r3.isBufferingOutput();
        if (r3 != 0) goto L_0x0284;
    L_0x0261:
        r3 = r11._generator;
        r3 = r3.isPersistent();
        if (r3 != 0) goto L_0x026f;
    L_0x0269:
        r0 = r11._parser;
        r0.reset(r2);
        r0 = r1;
    L_0x026f:
        if (r0 == 0) goto L_0x02b1;
    L_0x0271:
        r11.reset(r1);
        r0 = r11._parser;
        r0 = r0.isMoreInBuffer();
        if (r0 != 0) goto L_0x0284;
    L_0x027c:
        r0 = r11._endp;
        r0 = r0.isBufferingInput();
        if (r0 == 0) goto L_0x0284;
    L_0x0284:
        r0 = r11._request;
        r0 = r0.getContinuation();
        if (r0 == 0) goto L_0x0292;
    L_0x028c:
        r0 = r0.isPending();
        if (r0 != 0) goto L_0x008e;
    L_0x0292:
        r0 = r11._generator;
        r0 = r0.isCommitted();
        if (r0 == 0) goto L_0x008e;
    L_0x029a:
        r0 = r11._generator;
        r0 = r0.isComplete();
        if (r0 != 0) goto L_0x008e;
    L_0x02a2:
        r0 = r11._endp;
        r0 = r0 instanceof org.mortbay.io.nio.SelectChannelEndPoint;
        if (r0 == 0) goto L_0x008e;
    L_0x02a8:
        r0 = r11._endp;
        r0 = (org.mortbay.io.nio.SelectChannelEndPoint) r0;
        r0.setWritable(r1);
        goto L_0x008e;
    L_0x02b1:
        r11.reset(r2);
        goto L_0x0284;
    L_0x02b5:
        r3 = r0;
        goto L_0x011d;
    L_0x02b8:
        r0 = r4;
        goto L_0x0007;
    L_0x02bb:
        r0 = r3;
        goto L_0x01f9;
    L_0x02be:
        r4 = r0;
        goto L_0x0211;
    L_0x02c1:
        r4 = r6;
        goto L_0x00ec;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.HttpConnection.handle():void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void handleRequest() throws java.io.IOException {
        /*
        r8 = this;
        r6 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
        r0 = 0;
        r1 = r8._server;
        r1 = r1.isRunning();
        if (r1 == 0) goto L_0x0035;
    L_0x000b:
        r1 = r8._uri;	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x01b0, all -> 0x02bc }
        r1 = r1.getDecodedPath();	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x01b0, all -> 0x02bc }
        r2 = org.mortbay.util.URIUtil.canonicalPath(r1);	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x01b0, all -> 0x02bc }
        if (r2 != 0) goto L_0x0079;
    L_0x0017:
        r1 = new org.mortbay.jetty.HttpException;	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        r3 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r1.<init>(r3);	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        throw r1;	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
    L_0x001f:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
    L_0x0023:
        r2 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ all -> 0x02c6 }
        if (r2 == 0) goto L_0x002c;
    L_0x0029:
        org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x02c6 }
    L_0x002c:
        if (r1 == 0) goto L_0x0035;
    L_0x002e:
        r0 = java.lang.Thread.currentThread();
        r0.setName(r1);
    L_0x0035:
        return;
    L_0x0036:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
    L_0x003a:
        org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x02c6 }
        if (r1 == 0) goto L_0x0046;
    L_0x003f:
        r0 = java.lang.Thread.currentThread();
        r0.setName(r1);
    L_0x0046:
        r0 = r8._request;
        r0 = r0.getContinuation();
        if (r0 == 0) goto L_0x005c;
    L_0x004e:
        r0 = "continuation still pending {}";
        org.mortbay.log.Log.debug(r0);
        r0 = r8._request;
        r0 = r0.getContinuation();
        r0.reset();
    L_0x005c:
        r0 = r8._endp;
        r0 = r0.isOpen();
        if (r0 == 0) goto L_0x02a9;
    L_0x0064:
        r0 = r8._generator;
        r0 = r0.isPersistent();
        if (r0 == 0) goto L_0x0073;
    L_0x006c:
        r0 = r8._connector;
        r1 = r8._endp;
        r0.persist(r1);
    L_0x0073:
        r0 = r8._endp;
    L_0x0075:
        r0.close();
        goto L_0x0035;
    L_0x0079:
        r1 = r8._request;	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        r1.setPathInfo(r2);	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        r1 = r8._out;	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        if (r1 == 0) goto L_0x0087;
    L_0x0082:
        r1 = r8._out;	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        r1.reopen();	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
    L_0x0087:
        r1 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        if (r1 == 0) goto L_0x00b5;
    L_0x008d:
        r1 = java.lang.Thread.currentThread();	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        r0 = r1.getName();	 Catch:{ RetryRequest -> 0x001f, EofException -> 0x0036, HttpException -> 0x0115, RuntimeIOException -> 0x0169, Throwable -> 0x02ca, all -> 0x02bc }
        r1 = java.lang.Thread.currentThread();	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r3 = new java.lang.StringBuffer;	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r3.<init>();	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r3 = r3.append(r0);	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r4 = " - ";
        r3 = r3.append(r4);	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r4 = r8._uri;	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r3 = r3.append(r4);	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r3 = r3.toString();	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r1.setName(r3);	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
    L_0x00b5:
        r1 = r8._connector;	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r3 = r8._endp;	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r4 = r8._request;	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r1.customize(r3, r4);	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r1 = r8._server;	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        r1.handle(r8);	 Catch:{ RetryRequest -> 0x02ea, EofException -> 0x02e4, HttpException -> 0x02de, RuntimeIOException -> 0x02d8, Throwable -> 0x02d1, all -> 0x02c1 }
        if (r0 == 0) goto L_0x00cc;
    L_0x00c5:
        r1 = java.lang.Thread.currentThread();
        r1.setName(r0);
    L_0x00cc:
        r0 = r8._request;
        r0 = r0.getContinuation();
        if (r0 == 0) goto L_0x00e2;
    L_0x00d4:
        r0 = "continuation still pending {}";
        org.mortbay.log.Log.debug(r0);
        r0 = r8._request;
        r0 = r0.getContinuation();
        r0.reset();
    L_0x00e2:
        r0 = r8._endp;
        r0 = r0.isOpen();
        if (r0 == 0) goto L_0x02b9;
    L_0x00ea:
        r0 = r8._generator;
        r0 = r0.isPersistent();
        if (r0 == 0) goto L_0x00f9;
    L_0x00f2:
        r0 = r8._connector;
        r1 = r8._endp;
        r0.persist(r1);
    L_0x00f9:
        r0 = r8._response;
        r0 = r0.isCommitted();
        if (r0 != 0) goto L_0x010e;
    L_0x0101:
        r0 = r8._request;
        r0 = r0.isHandled();
        if (r0 != 0) goto L_0x010e;
    L_0x0109:
        r0 = r8._response;
        r0.sendError(r6);
    L_0x010e:
        r0 = r8._response;
        r0.complete();
        goto L_0x0035;
    L_0x0115:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
    L_0x0119:
        org.mortbay.log.Log.debug(r0);	 Catch:{ all -> 0x02c6 }
        r2 = r8._request;	 Catch:{ all -> 0x02c6 }
        r3 = 1;
        r2.setHandled(r3);	 Catch:{ all -> 0x02c6 }
        r2 = r8._response;	 Catch:{ all -> 0x02c6 }
        r3 = r0.getStatus();	 Catch:{ all -> 0x02c6 }
        r0 = r0.getReason();	 Catch:{ all -> 0x02c6 }
        r2.sendError(r3, r0);	 Catch:{ all -> 0x02c6 }
        if (r1 == 0) goto L_0x0138;
    L_0x0131:
        r0 = java.lang.Thread.currentThread();
        r0.setName(r1);
    L_0x0138:
        r0 = r8._request;
        r0 = r0.getContinuation();
        if (r0 == 0) goto L_0x014e;
    L_0x0140:
        r0 = "continuation still pending {}";
        org.mortbay.log.Log.debug(r0);
        r0 = r8._request;
        r0 = r0.getContinuation();
        r0.reset();
    L_0x014e:
        r0 = r8._endp;
        r0 = r0.isOpen();
        if (r0 == 0) goto L_0x02b0;
    L_0x0156:
        r0 = r8._generator;
        r0 = r0.isPersistent();
        if (r0 == 0) goto L_0x0165;
    L_0x015e:
        r0 = r8._connector;
        r1 = r8._endp;
        r0.persist(r1);
    L_0x0165:
        r0 = r8._endp;
        goto L_0x0075;
    L_0x0169:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
    L_0x016d:
        org.mortbay.log.Log.debug(r0);	 Catch:{ all -> 0x02c6 }
        r0 = r8._request;	 Catch:{ all -> 0x02c6 }
        r2 = 1;
        r0.setHandled(r2);	 Catch:{ all -> 0x02c6 }
        if (r1 == 0) goto L_0x017f;
    L_0x0178:
        r0 = java.lang.Thread.currentThread();
        r0.setName(r1);
    L_0x017f:
        r0 = r8._request;
        r0 = r0.getContinuation();
        if (r0 == 0) goto L_0x0195;
    L_0x0187:
        r0 = "continuation still pending {}";
        org.mortbay.log.Log.debug(r0);
        r0 = r8._request;
        r0 = r0.getContinuation();
        r0.reset();
    L_0x0195:
        r0 = r8._endp;
        r0 = r0.isOpen();
        if (r0 == 0) goto L_0x02b3;
    L_0x019d:
        r0 = r8._generator;
        r0 = r0.isPersistent();
        if (r0 == 0) goto L_0x01ac;
    L_0x01a5:
        r0 = r8._connector;
        r1 = r8._endp;
        r0.persist(r1);
    L_0x01ac:
        r0 = r8._endp;
        goto L_0x0075;
    L_0x01b0:
        r1 = move-exception;
        r2 = r0;
        r7 = r0;
        r0 = r1;
        r1 = r7;
    L_0x01b5:
        r3 = r0 instanceof java.lang.ThreadDeath;	 Catch:{ all -> 0x01bc }
        if (r3 == 0) goto L_0x020e;
    L_0x01b9:
        r0 = (java.lang.ThreadDeath) r0;	 Catch:{ all -> 0x01bc }
        throw r0;	 Catch:{ all -> 0x01bc }
    L_0x01bc:
        r0 = move-exception;
    L_0x01bd:
        if (r2 == 0) goto L_0x01c6;
    L_0x01bf:
        r1 = java.lang.Thread.currentThread();
        r1.setName(r2);
    L_0x01c6:
        r1 = r8._request;
        r1 = r1.getContinuation();
        if (r1 == 0) goto L_0x01dc;
    L_0x01ce:
        r1 = "continuation still pending {}";
        org.mortbay.log.Log.debug(r1);
        r1 = r8._request;
        r1 = r1.getContinuation();
        r1.reset();
    L_0x01dc:
        r1 = r8._endp;
        r1 = r1.isOpen();
        if (r1 == 0) goto L_0x02a2;
    L_0x01e4:
        r1 = r8._generator;
        r1 = r1.isPersistent();
        if (r1 == 0) goto L_0x01f3;
    L_0x01ec:
        r1 = r8._connector;
        r2 = r8._endp;
        r1.persist(r2);
    L_0x01f3:
        r1 = r8._response;
        r1 = r1.isCommitted();
        if (r1 != 0) goto L_0x0208;
    L_0x01fb:
        r1 = r8._request;
        r1 = r1.isHandled();
        if (r1 != 0) goto L_0x0208;
    L_0x0203:
        r1 = r8._response;
        r1.sendError(r6);
    L_0x0208:
        r1 = r8._response;
        r1.complete();
    L_0x020d:
        throw r0;
    L_0x020e:
        if (r1 != 0) goto L_0x0279;
    L_0x0210:
        r1 = new java.lang.StringBuffer;	 Catch:{ all -> 0x01bc }
        r1.<init>();	 Catch:{ all -> 0x01bc }
        r3 = r8._uri;	 Catch:{ all -> 0x01bc }
        r1 = r1.append(r3);	 Catch:{ all -> 0x01bc }
        r3 = ": ";
        r1 = r1.append(r3);	 Catch:{ all -> 0x01bc }
        r1 = r1.append(r0);	 Catch:{ all -> 0x01bc }
        r1 = r1.toString();	 Catch:{ all -> 0x01bc }
        org.mortbay.log.Log.warn(r1);	 Catch:{ all -> 0x01bc }
        org.mortbay.log.Log.debug(r0);	 Catch:{ all -> 0x01bc }
        r0 = r8._request;	 Catch:{ all -> 0x01bc }
        r1 = 1;
        r0.setHandled(r1);	 Catch:{ all -> 0x01bc }
        r0 = r8._generator;	 Catch:{ all -> 0x01bc }
        r1 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r3 = 0;
        r4 = 0;
        r5 = 1;
        r0.sendError(r1, r3, r4, r5);	 Catch:{ all -> 0x01bc }
    L_0x023f:
        if (r2 == 0) goto L_0x0248;
    L_0x0241:
        r0 = java.lang.Thread.currentThread();
        r0.setName(r2);
    L_0x0248:
        r0 = r8._request;
        r0 = r0.getContinuation();
        if (r0 == 0) goto L_0x025e;
    L_0x0250:
        r0 = "continuation still pending {}";
        org.mortbay.log.Log.debug(r0);
        r0 = r8._request;
        r0 = r0.getContinuation();
        r0.reset();
    L_0x025e:
        r0 = r8._endp;
        r0 = r0.isOpen();
        if (r0 == 0) goto L_0x02b6;
    L_0x0266:
        r0 = r8._generator;
        r0 = r0.isPersistent();
        if (r0 == 0) goto L_0x0275;
    L_0x026e:
        r0 = r8._connector;
        r1 = r8._endp;
        r0.persist(r1);
    L_0x0275:
        r0 = r8._endp;
        goto L_0x0075;
    L_0x0279:
        r1 = new java.lang.StringBuffer;	 Catch:{ all -> 0x01bc }
        r1.<init>();	 Catch:{ all -> 0x01bc }
        r3 = "";
        r1 = r1.append(r3);	 Catch:{ all -> 0x01bc }
        r3 = r8._uri;	 Catch:{ all -> 0x01bc }
        r1 = r1.append(r3);	 Catch:{ all -> 0x01bc }
        r1 = r1.toString();	 Catch:{ all -> 0x01bc }
        org.mortbay.log.Log.warn(r1, r0);	 Catch:{ all -> 0x01bc }
        r0 = r8._request;	 Catch:{ all -> 0x01bc }
        r1 = 1;
        r0.setHandled(r1);	 Catch:{ all -> 0x01bc }
        r0 = r8._generator;	 Catch:{ all -> 0x01bc }
        r1 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r3 = 0;
        r4 = 0;
        r5 = 1;
        r0.sendError(r1, r3, r4, r5);	 Catch:{ all -> 0x01bc }
        goto L_0x023f;
    L_0x02a2:
        r1 = r8._response;
        r1.complete();
        goto L_0x020d;
    L_0x02a9:
        r0 = r8._response;
    L_0x02ab:
        r0.complete();
        goto L_0x0035;
    L_0x02b0:
        r0 = r8._response;
        goto L_0x02ab;
    L_0x02b3:
        r0 = r8._response;
        goto L_0x02ab;
    L_0x02b6:
        r0 = r8._response;
        goto L_0x02ab;
    L_0x02b9:
        r0 = r8._response;
        goto L_0x02ab;
    L_0x02bc:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
        goto L_0x01bd;
    L_0x02c1:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
        goto L_0x01bd;
    L_0x02c6:
        r0 = move-exception;
        r2 = r1;
        goto L_0x01bd;
    L_0x02ca:
        r1 = move-exception;
        r7 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r7;
        goto L_0x01b5;
    L_0x02d1:
        r1 = move-exception;
        r7 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r7;
        goto L_0x01b5;
    L_0x02d8:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
        goto L_0x016d;
    L_0x02de:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
        goto L_0x0119;
    L_0x02e4:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
        goto L_0x003a;
    L_0x02ea:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.HttpConnection.handleRequest():void");
    }

    public void include() {
        this._include++;
    }

    public void included() {
        this._include--;
        if (this._out != null) {
            this._out.reopen();
        }
    }

    public boolean isConfidential(Request request) {
        return this._connector != null ? this._connector.isConfidential(request) : false;
    }

    public boolean isIdle() {
        return this._generator.isIdle() && (this._parser.isIdle() || this._delayedHandling);
    }

    public boolean isIncluding() {
        return this._include > 0;
    }

    public boolean isIntegral(Request request) {
        return this._connector != null ? this._connector.isIntegral(request) : false;
    }

    public boolean isResponseCommitted() {
        return this._generator.isCommitted();
    }

    public void reset(boolean z) {
        this._parser.reset(z);
        this._requestFields.clear();
        this._request.recycle();
        this._generator.reset(z);
        this._responseFields.clear();
        this._response.recycle();
        this._uri.clear();
    }

    public void setAssociatedObject(Object obj) {
        this._associatedObject = obj;
    }
}
