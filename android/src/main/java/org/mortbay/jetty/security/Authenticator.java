package org.mortbay.jetty.security;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;

public interface Authenticator extends Serializable {
    Principal authenticate(UserRealm userRealm, String str, Request request, Response response) throws IOException;

    String getAuthMethod();
}
