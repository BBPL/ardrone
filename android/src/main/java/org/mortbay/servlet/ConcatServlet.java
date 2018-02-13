package org.mortbay.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConcatServlet extends HttpServlet {
    ServletContext _context;
    boolean _development;
    long _lastModified;

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        int i = 0;
        String queryString = httpServletRequest.getQueryString();
        if (queryString == null) {
            httpServletResponse.sendError(204);
            return;
        }
        String[] split = queryString.split("\\&");
        queryString = null;
        for (String mimeType : split) {
            String mimeType2 = this._context.getMimeType(mimeType);
            if (mimeType2 != null) {
                if (queryString == null) {
                    queryString = mimeType2;
                } else if (!queryString.equals(mimeType2)) {
                    httpServletResponse.sendError(415);
                    return;
                }
            }
        }
        if (queryString != null) {
            httpServletResponse.setContentType(queryString);
        }
        while (i < split.length) {
            RequestDispatcher requestDispatcher = this._context.getRequestDispatcher(split[i]);
            if (requestDispatcher != null) {
                requestDispatcher.include(httpServletRequest, httpServletResponse);
            }
            i++;
        }
    }

    protected long getLastModified(HttpServletRequest httpServletRequest) {
        return this._development ? -1 : this._lastModified;
    }

    public void init() throws ServletException {
        this._lastModified = System.currentTimeMillis();
        this._context = getServletContext();
        this._development = "true".equals(getInitParameter("development"));
    }
}
