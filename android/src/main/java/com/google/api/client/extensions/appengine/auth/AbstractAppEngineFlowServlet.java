package com.google.api.client.extensions.appengine.auth;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.extensions.servlet.auth.AbstractFlowUserServlet;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public abstract class AbstractAppEngineFlowServlet extends AbstractFlowUserServlet {
    private static final long serialVersionUID = 1;

    protected String getUserId() {
        return AppEngineServletUtils.getUserId();
    }

    protected HttpTransport newHttpTransportInstance() {
        return new UrlFetchTransport();
    }

    protected JsonFactory newJsonFactoryInstance() {
        return new JacksonFactory();
    }
}
