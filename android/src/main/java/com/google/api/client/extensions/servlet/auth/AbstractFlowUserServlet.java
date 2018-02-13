package com.google.api.client.extensions.servlet.auth;

import com.google.api.client.extensions.auth.helpers.Credential;
import com.google.api.client.extensions.auth.helpers.ThreeLeggedFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractFlowUserServlet extends HttpServlet {
    private static final String AUTH_CREDENTIAL = "com.google.api.client.extensions.servlet.auth.credential";
    private static final long serialVersionUID = 1;
    private final HttpTransport httpTransport = newHttpTransportInstance();
    private final JsonFactory jsonFactory = newJsonFactoryInstance();

    private void startAuthFlow(HttpServletResponse httpServletResponse, PersistenceManager persistenceManager, ThreeLeggedFlow threeLeggedFlow) throws IOException {
        persistenceManager.makePersistent(threeLeggedFlow);
        httpServletResponse.sendRedirect(threeLeggedFlow.getAuthorizationUrl());
    }

    protected Credential getCredential(HttpServletRequest httpServletRequest) {
        return (Credential) httpServletRequest.getAttribute(AUTH_CREDENTIAL);
    }

    protected final HttpTransport getHttpTransport() {
        return this.httpTransport;
    }

    protected final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    protected abstract PersistenceManagerFactory getPersistenceManagerFactory();

    protected abstract String getUserId();

    protected abstract ThreeLeggedFlow newFlow(String str) throws IOException;

    protected abstract HttpTransport newHttpTransportInstance();

    protected abstract JsonFactory newJsonFactoryInstance();

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        PersistenceManager persistenceManager = getPersistenceManagerFactory().getPersistenceManager();
        ThreeLeggedFlow newFlow = newFlow(getUserId());
        newFlow.setJsonFactory(getJsonFactory());
        newFlow.setHttpTransport(getHttpTransport());
        Credential loadCredential;
        try {
            loadCredential = newFlow.loadCredential(persistenceManager);
            if (loadCredential != null && loadCredential.isInvalid()) {
                persistenceManager.deletePersistent(loadCredential);
                loadCredential = null;
            }
            if (loadCredential != null) {
                httpServletRequest.setAttribute(AUTH_CREDENTIAL, loadCredential);
                super.service(httpServletRequest, httpServletResponse);
            } else {
                startAuthFlow(httpServletResponse, persistenceManager, newFlow);
            }
        } catch (IOException e) {
            if (loadCredential.isInvalid()) {
                persistenceManager.deletePersistent(loadCredential);
                startAuthFlow(httpServletResponse, persistenceManager, newFlow);
            } else {
                throw e;
            }
        } catch (Throwable th) {
            persistenceManager.close();
        }
        persistenceManager.close();
    }
}
