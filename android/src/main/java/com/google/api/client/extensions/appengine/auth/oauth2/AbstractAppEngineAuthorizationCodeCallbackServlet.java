package com.google.api.client.extensions.appengine.auth.oauth2;

import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractAppEngineAuthorizationCodeCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {
    private static final long serialVersionUID = 1;

    protected String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException {
        return UserServiceFactory.getUserService().getCurrentUser().getUserId();
    }
}
