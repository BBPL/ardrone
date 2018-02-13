package org.mortbay.jetty.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;

public class HandlerList extends HandlerCollection {
    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        Handler[] handlers = getHandlers();
        if (handlers != null && isStarted()) {
            Request request = HttpConnection.getCurrentConnection().getRequest();
            int i2 = 0;
            while (i2 < handlers.length) {
                handlers[i2].handle(str, httpServletRequest, httpServletResponse, i);
                if (!request.isHandled()) {
                    i2++;
                } else {
                    return;
                }
            }
        }
    }
}
