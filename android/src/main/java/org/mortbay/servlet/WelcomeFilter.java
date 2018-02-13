package org.mortbay.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.mortbay.util.URIUtil;

public class WelcomeFilter implements Filter {
    private String welcome;

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String servletPath = ((HttpServletRequest) servletRequest).getServletPath();
        if (this.welcome == null || !servletPath.endsWith(URIUtil.SLASH)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletRequest.getRequestDispatcher(new StringBuffer().append(servletPath).append(this.welcome).toString()).forward(servletRequest, servletResponse);
        }
    }

    public void init(FilterConfig filterConfig) {
        this.welcome = filterConfig.getInitParameter("welcome");
        if (this.welcome == null) {
            this.welcome = "index.html";
        }
    }
}
