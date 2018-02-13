package org.mortbay.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.mortbay.jetty.HttpHeaderValues;
import org.mortbay.jetty.servlet.Dispatcher;
import org.mortbay.util.ByteArrayOutputStream2;
import org.mortbay.util.StringUtil;

public class GzipFilter extends UserAgentFilter {
    protected int _bufferSize = 8192;
    protected Set _excluded;
    protected Set _mimeTypes;
    protected int _minGzipSize = 0;

    public class GZIPResponseWrapper extends HttpServletResponseWrapper {
        long _contentLength = -1;
        GzipStream _gzStream;
        boolean _noGzip;
        HttpServletRequest _request;
        PrintWriter _writer;
        private final GzipFilter this$0;

        public GZIPResponseWrapper(GzipFilter gzipFilter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
            this.this$0 = gzipFilter;
            super(httpServletResponse);
            this._request = httpServletRequest;
        }

        public void addHeader(String str, String str2) {
            if ("content-length".equalsIgnoreCase(str)) {
                this._contentLength = Long.parseLong(str2);
                if (this._gzStream != null) {
                    this._gzStream.setContentLength(this._contentLength);
                }
            } else if ("content-type".equalsIgnoreCase(str)) {
                setContentType(str2);
            } else if ("content-encoding".equalsIgnoreCase(str)) {
                super.addHeader(str, str2);
                if (!isCommitted()) {
                    noGzip();
                }
            } else {
                super.addHeader(str, str2);
            }
        }

        void finish() throws IOException {
            if (!(this._writer == null || this._gzStream._closed)) {
                this._writer.flush();
            }
            if (this._gzStream != null) {
                this._gzStream.finish();
            }
        }

        public void flushBuffer() throws IOException {
            if (this._writer != null) {
                this._writer.flush();
            }
            if (this._gzStream != null) {
                this._gzStream.finish();
            } else {
                getResponse().flushBuffer();
            }
        }

        public ServletOutputStream getOutputStream() throws IOException {
            if (this._gzStream == null) {
                if (getResponse().isCommitted() || this._noGzip) {
                    return getResponse().getOutputStream();
                }
                this._gzStream = newGzipStream(this._request, (HttpServletResponse) getResponse(), this._contentLength, this.this$0._bufferSize, this.this$0._minGzipSize);
            } else if (this._writer != null) {
                throw new IllegalStateException("getWriter() called");
            }
            return this._gzStream;
        }

        public PrintWriter getWriter() throws IOException {
            if (this._writer == null) {
                if (this._gzStream != null) {
                    throw new IllegalStateException("getOutputStream() called");
                } else if (getResponse().isCommitted() || this._noGzip) {
                    return getResponse().getWriter();
                } else {
                    this._gzStream = newGzipStream(this._request, (HttpServletResponse) getResponse(), this._contentLength, this.this$0._bufferSize, this.this$0._minGzipSize);
                    this._writer = this.this$0.newWriter(this._gzStream, getCharacterEncoding());
                }
            }
            return this._writer;
        }

        protected GzipStream newGzipStream(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, long j, int i, int i2) throws IOException {
            return new GzipStream(httpServletRequest, httpServletResponse, j, i, i2);
        }

        void noGzip() {
            this._noGzip = true;
            if (this._gzStream != null) {
                try {
                    this._gzStream.doNotGzip();
                } catch (IOException e) {
                    throw new IllegalStateException();
                }
            }
        }

        public void reset() {
            super.reset();
            if (this._gzStream != null) {
                this._gzStream.resetBuffer();
            }
            this._writer = null;
            this._gzStream = null;
            this._noGzip = false;
            this._contentLength = -1;
        }

        public void resetBuffer() {
            super.resetBuffer();
            if (this._gzStream != null) {
                this._gzStream.resetBuffer();
            }
            this._writer = null;
            this._gzStream = null;
        }

        public void sendError(int i) throws IOException {
            resetBuffer();
            super.sendError(i);
        }

        public void sendError(int i, String str) throws IOException {
            resetBuffer();
            super.sendError(i, str);
        }

        public void sendRedirect(String str) throws IOException {
            resetBuffer();
            super.sendRedirect(str);
        }

        public void setContentLength(int i) {
            this._contentLength = (long) i;
            if (this._gzStream != null) {
                this._gzStream.setContentLength((long) i);
            }
        }

        public void setContentType(String str) {
            super.setContentType(str);
            if (str != null) {
                int indexOf = str.indexOf(";");
                if (indexOf > 0) {
                    str = str.substring(0, indexOf);
                }
            }
            if (this._gzStream == null || this._gzStream._out == null) {
                if (!(this.this$0._mimeTypes == null && "application/gzip".equalsIgnoreCase(str))) {
                    if (this.this$0._mimeTypes == null) {
                        return;
                    }
                    if (str != null && this.this$0._mimeTypes.contains(StringUtil.asciiToLowerCase(str))) {
                        return;
                    }
                }
                noGzip();
            }
        }

        public void setHeader(String str, String str2) {
            if ("content-length".equalsIgnoreCase(str)) {
                this._contentLength = Long.parseLong(str2);
                if (this._gzStream != null) {
                    this._gzStream.setContentLength(this._contentLength);
                }
            } else if ("content-type".equalsIgnoreCase(str)) {
                setContentType(str2);
            } else if ("content-encoding".equalsIgnoreCase(str)) {
                super.setHeader(str, str2);
                if (!isCommitted()) {
                    noGzip();
                }
            } else {
                super.setHeader(str, str2);
            }
        }

        public void setIntHeader(String str, int i) {
            if ("content-length".equalsIgnoreCase(str)) {
                this._contentLength = (long) i;
                if (this._gzStream != null) {
                    this._gzStream.setContentLength(this._contentLength);
                    return;
                }
                return;
            }
            super.setIntHeader(str, i);
        }

        public void setStatus(int i) {
            super.setStatus(i);
            if (i < 200 || i >= 300) {
                noGzip();
            }
        }

        public void setStatus(int i, String str) {
            super.setStatus(i, str);
            if (i < 200 || i >= 300) {
                noGzip();
            }
        }
    }

    public static class GzipStream extends ServletOutputStream {
        protected ByteArrayOutputStream2 _bOut;
        protected int _bufferSize;
        protected boolean _closed;
        protected long _contentLength;
        protected GZIPOutputStream _gzOut;
        protected int _minGzipSize;
        protected OutputStream _out;
        protected HttpServletRequest _request;
        protected HttpServletResponse _response;

        public GzipStream(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, long j, int i, int i2) throws IOException {
            this._request = httpServletRequest;
            this._response = httpServletResponse;
            this._contentLength = j;
            this._bufferSize = i;
            this._minGzipSize = i2;
            if (i2 == 0) {
                doGzip();
            }
        }

        private void checkOut(int i) throws IOException {
            if (this._closed) {
                throw new IOException("CLOSED");
            } else if (this._out == null) {
                if (this._response.isCommitted() || (this._contentLength >= 0 && this._contentLength < ((long) this._minGzipSize))) {
                    doNotGzip();
                } else if (i > this._minGzipSize) {
                    doGzip();
                } else {
                    OutputStream byteArrayOutputStream2 = new ByteArrayOutputStream2(this._bufferSize);
                    this._bOut = byteArrayOutputStream2;
                    this._out = byteArrayOutputStream2;
                }
            } else if (this._bOut == null) {
            } else {
                if (this._response.isCommitted() || (this._contentLength >= 0 && this._contentLength < ((long) this._minGzipSize))) {
                    doNotGzip();
                } else if (i >= this._bOut.getBuf().length - this._bOut.getCount()) {
                    doGzip();
                }
            }
        }

        public void close() throws IOException {
            if (this._request.getAttribute(Dispatcher.__INCLUDE_REQUEST_URI) != null) {
                flush();
                return;
            }
            if (this._bOut != null) {
                if (this._contentLength < 0) {
                    this._contentLength = (long) this._bOut.getCount();
                }
                if (this._contentLength < ((long) this._minGzipSize)) {
                    doNotGzip();
                } else {
                    doGzip();
                }
            } else if (this._out == null) {
                doNotGzip();
            }
            if (this._gzOut != null) {
                this._gzOut.close();
            } else {
                this._out.close();
            }
            this._closed = true;
        }

        public void doGzip() throws IOException {
            if (this._gzOut != null) {
                return;
            }
            if (this._response.isCommitted()) {
                throw new IllegalStateException();
            } else if (setContentEncodingGzip()) {
                OutputStream gZIPOutputStream = new GZIPOutputStream(this._response.getOutputStream(), this._bufferSize);
                this._gzOut = gZIPOutputStream;
                this._out = gZIPOutputStream;
                if (this._bOut != null) {
                    this._out.write(this._bOut.getBuf(), 0, this._bOut.getCount());
                    this._bOut = null;
                }
            } else {
                doNotGzip();
            }
        }

        public void doNotGzip() throws IOException {
            if (this._gzOut != null) {
                throw new IllegalStateException();
            } else if (this._out == null || this._bOut != null) {
                this._out = this._response.getOutputStream();
                if (this._contentLength >= 0) {
                    if (this._contentLength < 2147483647L) {
                        this._response.setContentLength((int) this._contentLength);
                    } else {
                        this._response.setHeader("Content-Length", Long.toString(this._contentLength));
                    }
                }
                if (this._bOut != null) {
                    this._out.write(this._bOut.getBuf(), 0, this._bOut.getCount());
                }
                this._bOut = null;
            }
        }

        public void finish() throws IOException {
            if (!this._closed) {
                if (this._out == null || this._bOut != null) {
                    if (this._contentLength <= 0 || this._contentLength >= ((long) this._minGzipSize)) {
                        doGzip();
                    } else {
                        doNotGzip();
                    }
                }
                if (this._gzOut != null && !this._closed) {
                    this._closed = true;
                    this._gzOut.close();
                }
            }
        }

        public void flush() throws IOException {
            if (this._out == null || this._bOut != null) {
                if (this._contentLength <= 0 || this._contentLength >= ((long) this._minGzipSize)) {
                    doGzip();
                } else {
                    doNotGzip();
                }
            }
            this._out.flush();
        }

        public void resetBuffer() {
            this._closed = false;
            this._out = null;
            this._bOut = null;
            if (!(this._gzOut == null || this._response.isCommitted())) {
                this._response.setHeader("Content-Encoding", null);
            }
            this._gzOut = null;
        }

        protected boolean setContentEncodingGzip() {
            this._response.setHeader("Content-Encoding", HttpHeaderValues.GZIP);
            return this._response.containsHeader("Content-Encoding");
        }

        public void setContentLength(long j) {
            this._contentLength = j;
        }

        public void write(int i) throws IOException {
            checkOut(1);
            this._out.write(i);
        }

        public void write(byte[] bArr) throws IOException {
            checkOut(bArr.length);
            this._out.write(bArr);
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            checkOut(i2);
            this._out.write(bArr, i, i2);
        }
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String header = httpServletRequest.getHeader("accept-encoding");
        Boolean bool = (Boolean) httpServletRequest.getAttribute("GzipFilter");
        if (header == null || header.indexOf(HttpHeaderValues.GZIP) < 0 || httpServletResponse.containsHeader("Content-Encoding") || (!(bool == null || bool.booleanValue()) || "HEAD".equalsIgnoreCase(httpServletRequest.getMethod()))) {
            super.doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }
        if (this._excluded != null) {
            if (this._excluded.contains(getUserAgent((ServletRequest) httpServletRequest))) {
                super.doFilter(httpServletRequest, httpServletResponse, filterChain);
                return;
            }
        }
        GZIPResponseWrapper newGZIPResponseWrapper = newGZIPResponseWrapper(httpServletRequest, httpServletResponse);
        try {
            super.doFilter(httpServletRequest, newGZIPResponseWrapper, filterChain);
            newGZIPResponseWrapper.finish();
        } catch (RuntimeException e) {
            httpServletRequest.setAttribute("GzipFilter", Boolean.FALSE);
            if (!httpServletResponse.isCommitted()) {
                httpServletResponse.reset();
            }
            throw e;
        } catch (Throwable th) {
            if (httpServletResponse.isCommitted()) {
                newGZIPResponseWrapper.finish();
            } else {
                newGZIPResponseWrapper.resetBuffer();
                newGZIPResponseWrapper.noGzip();
            }
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        StringTokenizer stringTokenizer;
        super.init(filterConfig);
        String initParameter = filterConfig.getInitParameter("bufferSize");
        if (initParameter != null) {
            this._bufferSize = Integer.parseInt(initParameter);
        }
        initParameter = filterConfig.getInitParameter("minGzipSize");
        if (initParameter != null) {
            this._minGzipSize = Integer.parseInt(initParameter);
        }
        initParameter = filterConfig.getInitParameter("mimeTypes");
        if (initParameter != null) {
            this._mimeTypes = new HashSet();
            stringTokenizer = new StringTokenizer(initParameter, ",", false);
            while (stringTokenizer.hasMoreTokens()) {
                this._mimeTypes.add(stringTokenizer.nextToken());
            }
        }
        initParameter = filterConfig.getInitParameter("excludedAgents");
        if (initParameter != null) {
            this._excluded = new HashSet();
            stringTokenizer = new StringTokenizer(initParameter, ",", false);
            while (stringTokenizer.hasMoreTokens()) {
                this._excluded.add(stringTokenizer.nextToken());
            }
        }
    }

    protected GZIPResponseWrapper newGZIPResponseWrapper(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new GZIPResponseWrapper(this, httpServletRequest, httpServletResponse);
    }

    protected PrintWriter newWriter(OutputStream outputStream, String str) throws UnsupportedEncodingException {
        return str == null ? new PrintWriter(outputStream) : new PrintWriter(new OutputStreamWriter(outputStream, str));
    }
}
