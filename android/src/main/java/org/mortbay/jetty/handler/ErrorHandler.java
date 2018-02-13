package org.mortbay.jetty.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.mortbay.jetty.AbstractGenerator;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.util.ByteArrayISO8859Writer;

public class ErrorHandler extends AbstractHandler {
    String _cacheControl = "must-revalidate,no-cache,no-store";
    boolean _showStacks = true;

    public String getCacheControl() {
        return this._cacheControl;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException {
        HttpConnection currentConnection = HttpConnection.getCurrentConnection();
        currentConnection.getRequest().setHandled(true);
        String method = httpServletRequest.getMethod();
        if (method.equals("GET") || method.equals("POST") || method.equals("HEAD")) {
            httpServletResponse.setContentType(MimeTypes.TEXT_HTML_8859_1);
            if (this._cacheControl != null) {
                httpServletResponse.setHeader("Cache-Control", this._cacheControl);
            }
            Writer byteArrayISO8859Writer = new ByteArrayISO8859Writer(4096);
            handleErrorPage(httpServletRequest, byteArrayISO8859Writer, currentConnection.getResponse().getStatus(), currentConnection.getResponse().getReason());
            byteArrayISO8859Writer.flush();
            httpServletResponse.setContentLength(byteArrayISO8859Writer.size());
            byteArrayISO8859Writer.writeTo(httpServletResponse.getOutputStream());
            byteArrayISO8859Writer.destroy();
        }
    }

    protected void handleErrorPage(HttpServletRequest httpServletRequest, Writer writer, int i, String str) throws IOException {
        writeErrorPage(httpServletRequest, writer, i, str, this._showStacks);
    }

    public boolean isShowStacks() {
        return this._showStacks;
    }

    public void setCacheControl(String str) {
        this._cacheControl = str;
    }

    public void setShowStacks(boolean z) {
        this._showStacks = z;
    }

    protected void write(Writer writer, String str) throws IOException {
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char charAt = str.charAt(i);
                switch (charAt) {
                    case '&':
                        writer.write("&amp;");
                        break;
                    case CacheConfig.DEFAULT_ASYNCHRONOUS_WORKER_IDLE_LIFETIME_SECS /*60*/:
                        writer.write("&lt;");
                        break;
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF /*62*/:
                        writer.write("&gt;");
                        break;
                    default:
                        if (Character.isISOControl(charAt) && !Character.isWhitespace(charAt)) {
                            writer.write(63);
                            break;
                        } else {
                            writer.write(charAt);
                            break;
                        }
                }
            }
        }
    }

    protected void writeErrorPage(HttpServletRequest httpServletRequest, Writer writer, int i, String str, boolean z) throws IOException {
        String reason = str == null ? AbstractGenerator.getReason(i) : str;
        writer.write("<html>\n<head>\n");
        writeErrorPageHead(httpServletRequest, writer, i, reason);
        writer.write("</head>\n<body>");
        writeErrorPageBody(httpServletRequest, writer, i, reason, z);
        writer.write("\n</body>\n</html>\n");
    }

    protected void writeErrorPageBody(HttpServletRequest httpServletRequest, Writer writer, int i, String str, boolean z) throws IOException {
        writeErrorPageMessage(httpServletRequest, writer, i, str, httpServletRequest.getRequestURI());
        if (z) {
            writeErrorPageStacks(httpServletRequest, writer);
        }
        writer.write("<hr /><i><small>Powered by Jetty://</small></i>");
        for (int i2 = 0; i2 < 20; i2++) {
            writer.write("<br/>                                                \n");
        }
    }

    protected void writeErrorPageHead(HttpServletRequest httpServletRequest, Writer writer, int i, String str) throws IOException {
        writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"/>\n");
        writer.write("<title>Error ");
        writer.write(Integer.toString(i));
        writer.write(32);
        write(writer, str);
        writer.write("</title>\n");
    }

    protected void writeErrorPageMessage(HttpServletRequest httpServletRequest, Writer writer, int i, String str, String str2) throws IOException {
        writer.write("<h2>HTTP ERROR ");
        writer.write(Integer.toString(i));
        writer.write("</h2>\n<p>Problem accessing ");
        write(writer, str2);
        writer.write(". Reason:\n<pre>    ");
        write(writer, str);
        writer.write("</pre></p>");
    }

    protected void writeErrorPageStacks(HttpServletRequest httpServletRequest, Writer writer) throws IOException {
        for (Throwable th = (Throwable) httpServletRequest.getAttribute(ServletHandler.__J_S_ERROR_EXCEPTION); th != null; th = th.getCause()) {
            writer.write("<h3>Caused by:</h3><pre>");
            Writer stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            printWriter.flush();
            write(writer, stringWriter.getBuffer().toString());
            writer.write("</pre>\n");
        }
    }
}
