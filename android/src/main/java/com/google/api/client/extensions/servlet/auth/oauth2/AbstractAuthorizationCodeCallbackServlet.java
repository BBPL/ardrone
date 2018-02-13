package com.google.api.client.extensions.servlet.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractAuthorizationCodeCallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1;
    private AuthorizationCodeFlow flow;
    private final Lock lock = new ReentrantLock();

    protected final void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        if (httpServletRequest.getQueryString() != null) {
            requestURL.append('?').append(httpServletRequest.getQueryString());
        }
        AuthorizationCodeResponseUrl authorizationCodeResponseUrl = new AuthorizationCodeResponseUrl(requestURL.toString());
        String code = authorizationCodeResponseUrl.getCode();
        if (authorizationCodeResponseUrl.getError() != null) {
            onError(httpServletRequest, httpServletResponse, authorizationCodeResponseUrl);
        } else if (code == null) {
            httpServletResponse.setStatus(400);
            httpServletResponse.getWriter().print("Missing authorization code");
        } else {
            this.lock.lock();
            try {
                if (this.flow == null) {
                    this.flow = initializeFlow();
                }
                onSuccess(httpServletRequest, httpServletResponse, this.flow.createAndStoreCredential(this.flow.newTokenRequest(code).setRedirectUri(getRedirectUri(httpServletRequest)).execute(), getUserId(httpServletRequest)));
            } finally {
                this.lock.unlock();
            }
        }
    }

    protected abstract String getRedirectUri(HttpServletRequest httpServletRequest) throws ServletException, IOException;

    protected abstract String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException;

    protected abstract AuthorizationCodeFlow initializeFlow() throws ServletException, IOException;

    protected void onError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthorizationCodeResponseUrl authorizationCodeResponseUrl) throws ServletException, IOException {
    }

    protected void onSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Credential credential) throws ServletException, IOException {
    }
}
