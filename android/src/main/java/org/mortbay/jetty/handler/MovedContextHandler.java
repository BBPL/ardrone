package org.mortbay.jetty.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.util.URIUtil;

public class MovedContextHandler extends ContextHandler {
    boolean _discardPathInfo;
    boolean _discardQuery;
    String _newContextURL;
    boolean _permanent;
    Redirector _redirector = new Redirector(this, null);

    static class C13331 {
    }

    private class Redirector extends AbstractHandler {
        private final MovedContextHandler this$0;

        private Redirector(MovedContextHandler movedContextHandler) {
            this.this$0 = movedContextHandler;
        }

        Redirector(MovedContextHandler movedContextHandler, C13331 c13331) {
            this(movedContextHandler);
        }

        public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
            if (this.this$0._newContextURL != null) {
                Request request = httpServletRequest instanceof Request ? (Request) httpServletRequest : HttpConnection.getCurrentConnection().getRequest();
                String str2 = this.this$0._newContextURL;
                if (!(this.this$0._discardPathInfo || httpServletRequest.getPathInfo() == null)) {
                    str2 = URIUtil.addPaths(str2, httpServletRequest.getPathInfo());
                }
                if (!(this.this$0._discardQuery || httpServletRequest.getQueryString() == null)) {
                    str2 = new StringBuffer().append(str2).append("?").append(httpServletRequest.getQueryString()).toString();
                }
                httpServletResponse.sendRedirect(str2);
                if (this.this$0._permanent) {
                    httpServletResponse.setStatus(301);
                }
                request.setHandled(true);
            }
        }
    }

    public MovedContextHandler() {
        addHandler(this._redirector);
    }

    public MovedContextHandler(HandlerContainer handlerContainer, String str, String str2) {
        super(handlerContainer, str);
        this._newContextURL = str2;
        addHandler(this._redirector);
    }

    public String getNewContextURL() {
        return this._newContextURL;
    }

    public boolean isDiscardPathInfo() {
        return this._discardPathInfo;
    }

    public boolean isDiscardQuery() {
        return this._discardQuery;
    }

    public boolean isPermanent() {
        return this._permanent;
    }

    public void setDiscardPathInfo(boolean z) {
        this._discardPathInfo = z;
    }

    public void setDiscardQuery(boolean z) {
        this._discardQuery = z;
    }

    public void setNewContextURL(String str) {
        this._newContextURL = str;
    }

    public void setPermanent(boolean z) {
        this._permanent = z;
    }
}
