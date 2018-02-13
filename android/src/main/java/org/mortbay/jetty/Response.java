package org.mortbay.jetty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.protocol.HTTP;
import org.mortbay.io.Buffer;
import org.mortbay.io.BufferCache.CachedBuffer;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.log.Log;
import org.mortbay.util.ByteArrayISO8859Writer;
import org.mortbay.util.IO;
import org.mortbay.util.QuotedStringTokenizer;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;

public class Response implements HttpServletResponse {
    public static final int DISABLED = -1;
    public static final int NONE = 0;
    public static final int STREAM = 1;
    public static final int WRITER = 2;
    private static PrintWriter __nullPrintWriter;
    private static ServletOutputStream __nullServletOut;
    private CachedBuffer _cachedMimeType;
    private String _characterEncoding;
    private HttpConnection _connection;
    private String _contentType;
    private boolean _explicitEncoding;
    private Locale _locale;
    private String _mimeType;
    private int _outputState;
    private String _reason;
    private int _status = 200;
    private PrintWriter _writer;

    static class C13301 {
    }

    private static class NullOutput extends ServletOutputStream {
        private NullOutput() {
        }

        NullOutput(C13301 c13301) {
            this();
        }

        public void write(int i) throws IOException {
        }
    }

    static {
        try {
            __nullPrintWriter = new PrintWriter(IO.getNullWriter());
            __nullServletOut = new NullOutput(null);
        } catch (Throwable e) {
            Log.warn(e);
        }
    }

    public Response(HttpConnection httpConnection) {
        this._connection = httpConnection;
    }

    public void addCookie(Cookie cookie) {
        this._connection.getResponseFields().addSetCookie(cookie);
    }

    public void addDateHeader(String str, long j) {
        if (!this._connection.isIncluding()) {
            this._connection.getResponseFields().addDateField(str, j);
        }
    }

    public void addHeader(String str, String str2) {
        if (!this._connection.isIncluding()) {
            this._connection.getResponseFields().add(str, str2);
            if ("Content-Length".equalsIgnoreCase(str)) {
                this._connection._generator.setContentLength(Long.parseLong(str2));
            }
        }
    }

    public void addIntHeader(String str, int i) {
        if (!this._connection.isIncluding()) {
            this._connection.getResponseFields().addLongField(str, (long) i);
            if ("Content-Length".equalsIgnoreCase(str)) {
                this._connection._generator.setContentLength((long) i);
            }
        }
    }

    public void complete() throws IOException {
        this._connection.completeResponse();
    }

    public boolean containsHeader(String str) {
        return this._connection.getResponseFields().containsKey(str);
    }

    public String encodeRedirectURL(String str) {
        return encodeURL(str);
    }

    public String encodeRedirectUrl(String str) {
        return encodeURL(str);
    }

    public String encodeURL(String str) {
        Request request = this._connection.getRequest();
        SessionManager sessionManager = request.getSessionManager();
        if (sessionManager == null) {
            return str;
        }
        String sessionURLPrefix = sessionManager.getSessionURLPrefix();
        if (sessionURLPrefix == null) {
            return str;
        }
        int indexOf;
        if (str == null || request == null || request.isRequestedSessionIdFromCookie()) {
            int indexOf2 = str.indexOf(sessionURLPrefix);
            if (indexOf2 == -1) {
                return str;
            }
            indexOf = str.indexOf("?", indexOf2);
            if (indexOf < 0) {
                indexOf = str.indexOf("#", indexOf2);
            }
            return indexOf <= indexOf2 ? str.substring(0, indexOf2) : new StringBuffer().append(str.substring(0, indexOf2)).append(str.substring(indexOf)).toString();
        } else {
            HttpSession session = request.getSession(false);
            if (session == null || !sessionManager.isValid(session)) {
                return str;
            }
            String nodeId = sessionManager.getNodeId(session);
            int indexOf3 = str.indexOf(sessionURLPrefix);
            if (indexOf3 != -1) {
                indexOf = str.indexOf("?", indexOf3);
                if (indexOf < 0) {
                    indexOf = str.indexOf("#", indexOf3);
                }
                return indexOf <= indexOf3 ? new StringBuffer().append(str.substring(0, sessionURLPrefix.length() + indexOf3)).append(nodeId).toString() : new StringBuffer().append(str.substring(0, sessionURLPrefix.length() + indexOf3)).append(nodeId).append(str.substring(indexOf)).toString();
            } else {
                indexOf = str.indexOf(63);
                if (indexOf < 0) {
                    indexOf = str.indexOf(35);
                }
                return indexOf < 0 ? new StringBuffer().append(str).append(sessionURLPrefix).append(nodeId).toString() : new StringBuffer().append(str.substring(0, indexOf)).append(sessionURLPrefix).append(nodeId).append(str.substring(indexOf)).toString();
            }
        }
    }

    public String encodeUrl(String str) {
        return encodeURL(str);
    }

    public void flushBuffer() throws IOException {
        this._connection.flushResponse();
    }

    public int getBufferSize() {
        return this._connection.getGenerator().getContentBufferSize();
    }

    public String getCharacterEncoding() {
        if (this._characterEncoding == null) {
            this._characterEncoding = StringUtil.__ISO_8859_1;
        }
        return this._characterEncoding;
    }

    public long getContentCount() {
        return (this._connection == null || this._connection.getGenerator() == null) ? -1 : this._connection.getGenerator().getContentWritten();
    }

    public String getContentType() {
        return this._contentType;
    }

    public String getHeader(String str) {
        return this._connection.getResponseFields().getStringField(str);
    }

    public Enumeration getHeaders(String str) {
        Enumeration values = this._connection.getResponseFields().getValues(str);
        return values == null ? Collections.enumeration(Collections.EMPTY_LIST) : values;
    }

    public HttpFields getHttpFields() {
        return this._connection.getResponseFields();
    }

    public Locale getLocale() {
        return this._locale == null ? Locale.getDefault() : this._locale;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (this._outputState == -1) {
            return __nullServletOut;
        }
        if (this._outputState == 0 || this._outputState == 1) {
            this._outputState = 1;
            return this._connection.getOutputStream();
        }
        throw new IllegalStateException("WRITER");
    }

    public String getReason() {
        return this._reason;
    }

    String getSetCharacterEncoding() {
        return this._characterEncoding;
    }

    public int getStatus() {
        return this._status;
    }

    public PrintWriter getWriter() throws IOException {
        if (this._outputState == -1) {
            return __nullPrintWriter;
        }
        if (this._outputState == 0 || this._outputState == 2) {
            if (this._writer == null) {
                String str = this._characterEncoding;
                if (str == null) {
                    if (this._mimeType != null) {
                        str = null;
                    }
                    if (str == null) {
                        str = StringUtil.__ISO_8859_1;
                    }
                    setCharacterEncoding(str);
                }
                this._writer = this._connection.getPrintWriter(str);
            }
            this._outputState = 2;
            return this._writer;
        }
        throw new IllegalStateException("STREAM");
    }

    public boolean isCommitted() {
        return this._connection.isResponseCommitted();
    }

    public boolean isWriting() {
        return this._outputState == 2;
    }

    protected void recycle() {
        this._status = 200;
        this._reason = null;
        this._locale = null;
        this._mimeType = null;
        this._cachedMimeType = null;
        this._characterEncoding = null;
        this._explicitEncoding = false;
        this._contentType = null;
        this._outputState = 0;
        this._writer = null;
    }

    public void reset() {
        resetBuffer();
        HttpFields responseFields = this._connection.getResponseFields();
        responseFields.clear();
        String stringField = this._connection.getRequestFields().getStringField(HttpHeaders.CONNECTION_BUFFER);
        if (stringField != null) {
            QuotedStringTokenizer quotedStringTokenizer = new QuotedStringTokenizer(stringField, ",");
            while (quotedStringTokenizer.hasMoreTokens()) {
                CachedBuffer cachedBuffer = HttpHeaderValues.CACHE.get(quotedStringTokenizer.nextToken().trim());
                if (cachedBuffer != null) {
                    switch (cachedBuffer.getOrdinal()) {
                        case 1:
                            responseFields.put(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.CLOSE_BUFFER);
                            break;
                        case 5:
                            if (!HttpVersions.HTTP_1_0.equalsIgnoreCase(this._connection.getRequest().getProtocol())) {
                                break;
                            }
                            responseFields.put(HttpHeaders.CONNECTION_BUFFER, HttpHeaderValues.KEEP_ALIVE);
                            break;
                        case 8:
                            responseFields.put(HttpHeaders.CONNECTION_BUFFER, "TE");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if (this._connection.getConnector().getServer().getSendDateHeader()) {
            Request request = this._connection.getRequest();
            responseFields.put(HttpHeaders.DATE_BUFFER, request.getTimeStampBuffer(), request.getTimeStamp());
        }
        this._status = 200;
        this._reason = null;
        this._mimeType = null;
        this._cachedMimeType = null;
        this._contentType = null;
        this._characterEncoding = null;
        this._explicitEncoding = false;
        this._locale = null;
        this._outputState = 0;
        this._writer = null;
    }

    public void resetBuffer() {
        if (isCommitted()) {
            throw new IllegalStateException("Committed");
        }
        this._connection.getGenerator().resetBuffer();
    }

    public void sendError(int i) throws IOException {
        if (i == 102) {
            sendProcessing();
        } else {
            sendError(i, null);
        }
    }

    public void sendError(int i, String str) throws IOException {
        if (!this._connection.isIncluding()) {
            if (isCommitted()) {
                Log.warn(new StringBuffer().append("Committed before ").append(i).append(" ").append(str).toString());
            }
            resetBuffer();
            this._characterEncoding = null;
            setHeader("Expires", null);
            setHeader("Last-Modified", null);
            setHeader("Cache-Control", null);
            setHeader("Content-Type", null);
            setHeader("Content-Length", null);
            this._outputState = 0;
            setStatus(i, str);
            String reason = str == null ? AbstractGenerator.getReason(i) : str;
            if (i != 204 && i != 304 && i != 206 && i >= 200) {
                Request request = this._connection.getRequest();
                SContext context = request.getContext();
                ErrorHandler errorHandler = context != null ? context.getContextHandler().getErrorHandler() : null;
                if (errorHandler != null) {
                    request.setAttribute(ServletHandler.__J_S_ERROR_STATUS_CODE, new Integer(i));
                    request.setAttribute(ServletHandler.__J_S_ERROR_MESSAGE, reason);
                    request.setAttribute(ServletHandler.__J_S_ERROR_REQUEST_URI, request.getRequestURI());
                    request.setAttribute(ServletHandler.__J_S_ERROR_SERVLET_NAME, request.getServletName());
                    errorHandler.handle(null, this._connection.getRequest(), this, 8);
                } else {
                    setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
                    setContentType(MimeTypes.TEXT_HTML_8859_1);
                    ByteArrayISO8859Writer byteArrayISO8859Writer = new ByteArrayISO8859Writer(2048);
                    if (reason != null) {
                        reason = StringUtil.replace(StringUtil.replace(StringUtil.replace(reason, "&", "&amp;"), "<", "&lt;"), ">", "&gt;");
                    }
                    String requestURI = request.getRequestURI();
                    if (requestURI != null) {
                        requestURI = StringUtil.replace(StringUtil.replace(StringUtil.replace(requestURI, "&", "&amp;"), "<", "&lt;"), ">", "&gt;");
                    }
                    byteArrayISO8859Writer.write("<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>\n");
                    byteArrayISO8859Writer.write("<title>Error ");
                    byteArrayISO8859Writer.write(Integer.toString(i));
                    byteArrayISO8859Writer.write(' ');
                    if (reason == null) {
                        reason = AbstractGenerator.getReason(i);
                    }
                    byteArrayISO8859Writer.write(reason);
                    byteArrayISO8859Writer.write("</title>\n</head>\n<body>\n<h2>HTTP ERROR: ");
                    byteArrayISO8859Writer.write(Integer.toString(i));
                    byteArrayISO8859Writer.write("</h2>\n<p>Problem accessing ");
                    byteArrayISO8859Writer.write(requestURI);
                    byteArrayISO8859Writer.write(". Reason:\n<pre>    ");
                    byteArrayISO8859Writer.write(reason);
                    byteArrayISO8859Writer.write("</pre>");
                    byteArrayISO8859Writer.write("</p>\n<hr /><i><small>Powered by Jetty://</small></i>");
                    for (int i2 = 0; i2 < 20; i2++) {
                        byteArrayISO8859Writer.write("\n                                                ");
                    }
                    byteArrayISO8859Writer.write("\n</body>\n</html>\n");
                    byteArrayISO8859Writer.flush();
                    setContentLength(byteArrayISO8859Writer.size());
                    byteArrayISO8859Writer.writeTo(getOutputStream());
                    byteArrayISO8859Writer.destroy();
                }
            } else if (i != 206) {
                this._connection.getRequestFields().remove(HttpHeaders.CONTENT_TYPE_BUFFER);
                this._connection.getRequestFields().remove(HttpHeaders.CONTENT_LENGTH_BUFFER);
                this._characterEncoding = null;
                this._mimeType = null;
                this._cachedMimeType = null;
            }
            complete();
        }
    }

    public void sendProcessing() throws IOException {
        Generator generator = this._connection.getGenerator();
        if (generator instanceof HttpGenerator) {
            HttpGenerator httpGenerator = (HttpGenerator) generator;
            String header = this._connection.getRequest().getHeader("Expect");
            if (header != null && header.startsWith("102") && httpGenerator.getVersion() >= 11) {
                boolean isPersistent = httpGenerator.isPersistent();
                httpGenerator.setResponse(102, null);
                httpGenerator.completeHeader(null, true);
                httpGenerator.setPersistent(true);
                httpGenerator.complete();
                httpGenerator.flush();
                httpGenerator.reset(false);
                httpGenerator.setPersistent(isPersistent);
            }
        }
    }

    public void sendRedirect(String str) throws IOException {
        if (!this._connection.isIncluding()) {
            if (str == null) {
                throw new IllegalArgumentException();
            }
            if (!URIUtil.hasScheme(str)) {
                StringBuffer rootURL = this._connection.getRequest().getRootURL();
                if (str.startsWith(URIUtil.SLASH)) {
                    rootURL.append(str);
                } else {
                    String requestURI = this._connection.getRequest().getRequestURI();
                    if (!requestURI.endsWith(URIUtil.SLASH)) {
                        requestURI = URIUtil.parentPath(requestURI);
                    }
                    requestURI = URIUtil.addPaths(requestURI, str);
                    if (requestURI == null) {
                        throw new IllegalStateException("path cannot be above root");
                    }
                    if (!requestURI.startsWith(URIUtil.SLASH)) {
                        rootURL.append('/');
                    }
                    rootURL.append(requestURI);
                }
                str = rootURL.toString();
                HttpURI httpURI = new HttpURI(str);
                String decodedPath = httpURI.getDecodedPath();
                String canonicalPath = URIUtil.canonicalPath(decodedPath);
                if (canonicalPath == null) {
                    throw new IllegalArgumentException();
                } else if (!canonicalPath.equals(decodedPath)) {
                    rootURL = this._connection.getRequest().getRootURL();
                    rootURL.append(canonicalPath);
                    if (httpURI.getQuery() != null) {
                        rootURL.append('?');
                        rootURL.append(httpURI.getQuery());
                    }
                    if (httpURI.getFragment() != null) {
                        rootURL.append('#');
                        rootURL.append(httpURI.getFragment());
                    }
                    str = rootURL.toString();
                }
            }
            resetBuffer();
            setHeader("Location", str);
            setStatus(302);
            complete();
        }
    }

    public void setBufferSize(int i) {
        if (isCommitted() || getContentCount() > 0) {
            throw new IllegalStateException("Committed or content written");
        }
        this._connection.getGenerator().increaseContentBufferSize(i);
    }

    public void setCharacterEncoding(String str) {
        if (!this._connection.isIncluding() && this._outputState == 0 && !isCommitted()) {
            this._explicitEncoding = true;
            if (str != null) {
                this._characterEncoding = str;
                if (this._contentType != null) {
                    int indexOf = this._contentType.indexOf(59);
                    if (indexOf < 0) {
                        this._contentType = null;
                        if (this._cachedMimeType != null) {
                            Buffer associate = this._cachedMimeType.getAssociate(this._characterEncoding);
                            if (associate != null) {
                                this._contentType = associate.toString();
                                this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, associate);
                            }
                        }
                        if (this._contentType == null) {
                            this._contentType = new StringBuffer().append(this._mimeType).append(HTTP.CHARSET_PARAM).append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).toString();
                            this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                            return;
                        }
                        return;
                    }
                    indexOf = this._contentType.indexOf("charset=", indexOf);
                    if (indexOf < 0) {
                        this._contentType = new StringBuffer().append(this._contentType).append(HTTP.CHARSET_PARAM).append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).toString();
                    } else {
                        indexOf += 8;
                        int indexOf2 = this._contentType.indexOf(" ", indexOf);
                        if (indexOf2 < 0) {
                            this._contentType = new StringBuffer().append(this._contentType.substring(0, indexOf)).append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).toString();
                        } else {
                            this._contentType = new StringBuffer().append(this._contentType.substring(0, indexOf)).append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).append(this._contentType.substring(indexOf2)).toString();
                        }
                    }
                    this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                }
            } else if (this._characterEncoding != null) {
                this._characterEncoding = null;
                if (this._cachedMimeType != null) {
                    this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._cachedMimeType);
                } else {
                    this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._mimeType);
                }
            }
        }
    }

    public void setContentLength(int i) {
        if (!isCommitted() && !this._connection.isIncluding()) {
            this._connection._generator.setContentLength((long) i);
            if (i >= 0) {
                this._connection.getResponseFields().putLongField("Content-Length", (long) i);
                if (!this._connection._generator.isContentWritten()) {
                    return;
                }
                if (this._outputState == 2) {
                    this._writer.close();
                } else if (this._outputState == 1) {
                    try {
                        getOutputStream().close();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void setContentType(String str) {
        if (!isCommitted() && !this._connection.isIncluding()) {
            if (str == null) {
                if (this._locale == null) {
                    this._characterEncoding = null;
                }
                this._mimeType = null;
                this._cachedMimeType = null;
                this._contentType = null;
                this._connection.getResponseFields().remove(HttpHeaders.CONTENT_TYPE_BUFFER);
                return;
            }
            int indexOf = str.indexOf(59);
            Buffer associate;
            if (indexOf > 0) {
                this._mimeType = str.substring(0, indexOf).trim();
                this._cachedMimeType = MimeTypes.CACHE.get(this._mimeType);
                int indexOf2 = str.indexOf("charset=", indexOf + 1);
                if (indexOf2 >= 0) {
                    this._explicitEncoding = true;
                    int i = indexOf2 + 8;
                    int indexOf3 = str.indexOf(32, i);
                    if (this._outputState == 2) {
                        if ((indexOf2 != indexOf + 1 || indexOf3 >= 0) && !(indexOf2 == indexOf + 2 && indexOf3 < 0 && str.charAt(indexOf + 1) == ' ')) {
                            if (indexOf3 < 0) {
                                this._contentType = new StringBuffer().append(str.substring(0, indexOf2)).append(" charset=").append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).toString();
                                this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                                return;
                            }
                            this._contentType = new StringBuffer().append(str.substring(0, indexOf2)).append(str.substring(indexOf3)).append(" charset=").append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).toString();
                            this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                            return;
                        } else if (this._cachedMimeType != null) {
                            associate = this._cachedMimeType.getAssociate(this._characterEncoding);
                            if (associate != null) {
                                this._contentType = associate.toString();
                                this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, associate);
                                return;
                            }
                            this._contentType = new StringBuffer().append(this._mimeType).append(HTTP.CHARSET_PARAM).append(this._characterEncoding).toString();
                            this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                            return;
                        } else {
                            this._contentType = new StringBuffer().append(this._mimeType).append(HTTP.CHARSET_PARAM).append(this._characterEncoding).toString();
                            this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                            return;
                        }
                    } else if ((indexOf2 == indexOf + 1 && indexOf3 < 0) || (indexOf2 == indexOf + 2 && indexOf3 < 0 && str.charAt(indexOf + 1) == ' ')) {
                        this._cachedMimeType = MimeTypes.CACHE.get(this._mimeType);
                        this._characterEncoding = QuotedStringTokenizer.unquote(str.substring(i));
                        if (this._cachedMimeType != null) {
                            associate = this._cachedMimeType.getAssociate(this._characterEncoding);
                            if (associate != null) {
                                this._contentType = associate.toString();
                                this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, associate);
                                return;
                            }
                            this._contentType = str;
                            this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                            return;
                        }
                        this._contentType = str;
                        this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                        return;
                    } else if (indexOf3 > 0) {
                        this._characterEncoding = QuotedStringTokenizer.unquote(str.substring(i, indexOf3));
                        this._contentType = str;
                        this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                        return;
                    } else {
                        this._characterEncoding = QuotedStringTokenizer.unquote(str.substring(i));
                        this._contentType = str;
                        this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                        return;
                    }
                }
                this._cachedMimeType = null;
                if (this._characterEncoding != null) {
                    str = new StringBuffer().append(str).append(HTTP.CHARSET_PARAM).append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).toString();
                }
                this._contentType = str;
                this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                return;
            }
            this._mimeType = str;
            this._cachedMimeType = MimeTypes.CACHE.get(this._mimeType);
            if (this._characterEncoding != null) {
                if (this._cachedMimeType != null) {
                    associate = this._cachedMimeType.getAssociate(this._characterEncoding);
                    if (associate != null) {
                        this._contentType = associate.toString();
                        this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, associate);
                        return;
                    }
                    this._contentType = new StringBuffer().append(this._mimeType).append(HTTP.CHARSET_PARAM).append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).toString();
                    this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                    return;
                }
                this._contentType = new StringBuffer().append(str).append(HTTP.CHARSET_PARAM).append(QuotedStringTokenizer.quote(this._characterEncoding, ";= ")).toString();
                this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
            } else if (this._cachedMimeType != null) {
                this._contentType = this._cachedMimeType.toString();
                this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._cachedMimeType);
            } else {
                this._contentType = str;
                this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
            }
        }
    }

    public void setDateHeader(String str, long j) {
        if (!this._connection.isIncluding()) {
            this._connection.getResponseFields().putDateField(str, j);
        }
    }

    public void setHeader(String str, String str2) {
        if (!this._connection.isIncluding()) {
            this._connection.getResponseFields().put(str, str2);
            if (!"Content-Length".equalsIgnoreCase(str)) {
                return;
            }
            if (str2 == null) {
                this._connection._generator.setContentLength(-1);
            } else {
                this._connection._generator.setContentLength(Long.parseLong(str2));
            }
        }
    }

    public void setIntHeader(String str, int i) {
        if (!this._connection.isIncluding()) {
            this._connection.getResponseFields().putLongField(str, (long) i);
            if ("Content-Length".equalsIgnoreCase(str)) {
                this._connection._generator.setContentLength((long) i);
            }
        }
    }

    public void setLocale(Locale locale) {
        if (locale != null && !isCommitted() && !this._connection.isIncluding()) {
            this._locale = locale;
            this._connection.getResponseFields().put(HttpHeaders.CONTENT_LANGUAGE_BUFFER, locale.toString().replace('_', '-'));
            if (!this._explicitEncoding && this._outputState == 0 && this._connection.getRequest().getContext() != null) {
                String localeEncoding = this._connection.getRequest().getContext().getContextHandler().getLocaleEncoding(locale);
                if (localeEncoding != null && localeEncoding.length() > 0) {
                    this._characterEncoding = localeEncoding;
                    String contentType = getContentType();
                    if (contentType != null) {
                        this._characterEncoding = localeEncoding;
                        int indexOf = contentType.indexOf(59);
                        if (indexOf < 0) {
                            this._mimeType = contentType;
                            this._contentType = new StringBuffer().append(contentType).append(HTTP.CHARSET_PARAM).append(localeEncoding).toString();
                        } else {
                            this._mimeType = contentType.substring(0, indexOf);
                            localeEncoding = new StringBuffer().append(this._mimeType).append(HTTP.CHARSET_PARAM).append(localeEncoding).toString();
                            this._mimeType = localeEncoding;
                            this._contentType = localeEncoding;
                        }
                        this._cachedMimeType = MimeTypes.CACHE.get(this._mimeType);
                        this._connection.getResponseFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, this._contentType);
                    }
                }
            }
        }
    }

    public void setLongContentLength(long j) {
        if (!isCommitted() && !this._connection.isIncluding()) {
            this._connection._generator.setContentLength(j);
            this._connection.getResponseFields().putLongField("Content-Length", j);
        }
    }

    public void setStatus(int i) {
        setStatus(i, null);
    }

    public void setStatus(int i, String str) {
        if (!this._connection.isIncluding()) {
            this._status = i;
            this._reason = str;
        }
    }

    public String toString() {
        return new StringBuffer().append("HTTP/1.1 ").append(this._status).append(" ").append(this._reason == null ? HttpVersions.HTTP_0_9 : this._reason).append(System.getProperty("line.separator")).append(this._connection.getResponseFields().toString()).toString();
    }
}
