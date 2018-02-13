package org.mortbay.jetty.handler;

import java.io.IOException;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.servlet.PathMap;
import org.mortbay.util.URIUtil;

public class RewriteHandler extends HandlerWrapper {
    private String _originalPathAttribute;
    private PathMap _rewrite = new PathMap(true);
    private boolean _rewritePathInfo = true;
    private boolean _rewriteRequestURI = true;

    public void addRewriteRule(String str, String str2) {
        if (str == null || str.length() == 0 || !str.startsWith(URIUtil.SLASH)) {
            throw new IllegalArgumentException();
        }
        if (this._rewrite == null) {
            this._rewrite = new PathMap(true);
        }
        this._rewrite.put(str, str2);
    }

    public String getOriginalPathAttribute() {
        return this._originalPathAttribute;
    }

    public PathMap getRewrite() {
        return this._rewrite;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        if (isStarted() && this._rewrite != null) {
            Entry match = this._rewrite.getMatch(str);
            if (!(match == null || match.getValue() == null)) {
                if (this._originalPathAttribute != null) {
                    httpServletRequest.setAttribute(this._originalPathAttribute, str);
                }
                str = URIUtil.addPaths(match.getValue().toString(), PathMap.pathInfo(match.getKey().toString(), str));
                if (this._rewriteRequestURI) {
                    ((Request) httpServletRequest).setRequestURI(str);
                }
                if (this._rewritePathInfo) {
                    ((Request) httpServletRequest).setPathInfo(str);
                }
            }
        }
        super.handle(str, httpServletRequest, httpServletResponse, i);
    }

    public boolean isRewritePathInfo() {
        return this._rewritePathInfo;
    }

    public boolean isRewriteRequestURI() {
        return this._rewriteRequestURI;
    }

    public void setOriginalPathAttribute(String str) {
        this._originalPathAttribute = str;
    }

    public void setRewrite(PathMap pathMap) {
        this._rewrite = pathMap;
    }

    public void setRewritePathInfo(boolean z) {
        this._rewritePathInfo = z;
    }

    public void setRewriteRequestURI(boolean z) {
        this._rewriteRequestURI = z;
    }
}
