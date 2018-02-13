package org.mortbay.util.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;

public class AjaxFilter implements Filter {
    ServletContext context;

    static class C13531 {
    }

    public static class AjaxResponse {
        private PrintWriter out;
        private HttpServletRequest request;

        private AjaxResponse(HttpServletRequest httpServletRequest, PrintWriter printWriter) {
            this.out = printWriter;
            this.request = httpServletRequest;
        }

        AjaxResponse(HttpServletRequest httpServletRequest, PrintWriter printWriter, C13531 c13531) {
            this(httpServletRequest, printWriter);
        }

        public void elementResponse(String str, String str2) {
            String parameter = str == null ? this.request.getParameter("id") : str;
            if (parameter == null) {
                parameter = "unknown";
            }
            this.out.println(new StringBuffer().append("<response type=\"element\" id=\"").append(parameter).append("\">").append(str2).append("</response>").toString());
        }

        public void objectResponse(String str, String str2) {
            String parameter = str == null ? this.request.getParameter("id") : str;
            if (parameter == null) {
                parameter = "unknown";
            }
            this.out.println(new StringBuffer().append("<response type=\"object\" id=\"").append(parameter).append("\">").append(str2).append("</response>").toString());
        }
    }

    public static String encodeText(String str) {
        StringBuffer stringBuffer = null;
        for (int i = 0; i < str.length(); i++) {
            String str2;
            char charAt = str.charAt(i);
            switch (charAt) {
                case '&':
                    str2 = "&amp;";
                    break;
                case CacheConfig.DEFAULT_ASYNCHRONOUS_WORKER_IDLE_LIFETIME_SECS /*60*/:
                    str2 = "&lt;";
                    break;
                case ExifTagConstants.PIXEL_FORMAT_VALUE_16_BIT_GRAY_HALF /*62*/:
                    str2 = "&gt;";
                    break;
                default:
                    str2 = null;
                    break;
            }
            if (str2 != null) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer(str.length() * 2);
                    stringBuffer.append(str.subSequence(0, i));
                }
                stringBuffer.append(str2);
            } else if (stringBuffer != null) {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer != null ? stringBuffer.toString() : str;
    }

    public void destroy() {
        this.context = null;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String[] parameterValues = servletRequest.getParameterValues("ajax");
        String[] parameterValues2 = servletRequest.getParameterValues("message");
        if (parameterValues == null || parameterValues.length <= 0) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println("<ajax-response>");
        AjaxResponse ajaxResponse = new AjaxResponse(httpServletRequest, printWriter, null);
        for (int i = 0; i < parameterValues.length; i++) {
            handle(parameterValues[i], parameterValues2[i], httpServletRequest, ajaxResponse);
        }
        printWriter.println("</ajax-response>");
        byte[] bytes = stringWriter.toString().getBytes("UTF-8");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.addHeader("Cache-Control", "must-revalidate,no-cache,no-store");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("text/xml; charset=UTF-8");
        httpServletResponse.setContentLength(bytes.length);
        httpServletResponse.getOutputStream().write(bytes);
        httpServletResponse.flushBuffer();
    }

    public ServletContext getContext() {
        return this.context;
    }

    public void handle(String str, String str2, HttpServletRequest httpServletRequest, AjaxResponse ajaxResponse) {
        ajaxResponse.elementResponse(null, new StringBuffer().append("<span class=\"error\">No implementation for ").append(str).append(" ").append(httpServletRequest.getParameter("member")).append("</span>").toString());
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
    }
}
