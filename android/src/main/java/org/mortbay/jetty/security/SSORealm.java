package org.mortbay.jetty.security;

import java.security.Principal;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;

public interface SSORealm {
    void clearSingleSignOn(String str);

    Credential getSingleSignOn(Request request, Response response);

    void setSingleSignOn(Request request, Response response, Principal principal, Credential credential);
}
