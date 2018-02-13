package com.google.api.client.extensions.servlet.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpResponseException;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractAuthorizationCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1;
    private Credential credential;
    private AuthorizationCodeFlow flow;
    private final Lock lock = new ReentrantLock();

    protected final Credential getCredential() {
        return this.credential;
    }

    protected abstract String getRedirectUri(HttpServletRequest httpServletRequest) throws ServletException, IOException;

    protected abstract String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException;

    protected abstract AuthorizationCodeFlow initializeFlow() throws ServletException, IOException;

    protected void onAuthorization(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthorizationCodeRequestUrl authorizationCodeRequestUrl) throws ServletException, IOException {
        httpServletResponse.sendRedirect(authorizationCodeRequestUrl.build());
    }

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        this.lock.lock();
        try {
            String userId = getUserId(httpServletRequest);
            if (this.flow == null) {
                this.flow = initializeFlow();
            }
            this.credential = this.flow.loadCredential(userId);
            if (!(this.credential == null || this.credential.getAccessToken() == null)) {
                super.service(httpServletRequest, httpServletResponse);
                this.lock.unlock();
                return;
            }
        } catch (HttpResponseException e) {
            if (this.credential.getAccessToken() != null) {
                throw e;
            }
        } catch (Throwable th) {
            this.lock.unlock();
        }
        AuthorizationCodeRequestUrl newAuthorizationUrl = this.flow.newAuthorizationUrl();
        newAuthorizationUrl.setRedirectUri(getRedirectUri(httpServletRequest));
        onAuthorization(httpServletRequest, httpServletResponse, newAuthorizationUrl);
        this.credential = null;
        this.lock.unlock();
    }
}
