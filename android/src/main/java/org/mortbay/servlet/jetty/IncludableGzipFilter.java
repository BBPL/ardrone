package org.mortbay.servlet.jetty;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.io.UncheckedPrintWriter;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpHeaderValues;
import org.mortbay.servlet.GzipFilter;
import org.mortbay.servlet.GzipFilter.GZIPResponseWrapper;
import org.mortbay.servlet.GzipFilter.GzipStream;

public class IncludableGzipFilter extends GzipFilter {
    boolean _uncheckedPrintWriter = false;

    public class IncludableGzipStream extends GzipStream {
        private final IncludableGzipFilter this$0;

        public IncludableGzipStream(IncludableGzipFilter includableGzipFilter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, long j, int i, int i2) throws IOException {
            this.this$0 = includableGzipFilter;
            super(httpServletRequest, httpServletResponse, j, i, i2);
        }

        protected boolean setContentEncodingGzip() {
            HttpConnection.getCurrentConnection().getResponseFields().put("Content-Encoding", HttpHeaderValues.GZIP);
            return true;
        }
    }

    public class IncludableResponseWrapper extends GZIPResponseWrapper {
        private final IncludableGzipFilter this$0;

        public IncludableResponseWrapper(IncludableGzipFilter includableGzipFilter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
            this.this$0 = includableGzipFilter;
            super(includableGzipFilter, httpServletRequest, httpServletResponse);
        }

        protected GzipStream newGzipStream(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, long j, int i, int i2) throws IOException {
            return new IncludableGzipStream(this.this$0, httpServletRequest, httpServletResponse, j, i, i2);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        String initParameter = filterConfig.getInitParameter("uncheckedPrintWriter");
        if (initParameter != null) {
            this._uncheckedPrintWriter = Boolean.valueOf(initParameter).booleanValue();
        }
    }

    protected GZIPResponseWrapper newGZIPResponseWrapper(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new IncludableResponseWrapper(this, httpServletRequest, httpServletResponse);
    }

    protected PrintWriter newWriter(OutputStream outputStream, String str) throws UnsupportedEncodingException {
        return this._uncheckedPrintWriter ? str == null ? new UncheckedPrintWriter(outputStream) : new UncheckedPrintWriter(new OutputStreamWriter(outputStream, str)) : super.newWriter(outputStream, str);
    }
}
