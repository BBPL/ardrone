package org.mortbay.jetty.security;

import java.io.IOException;
import java.security.Principal;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.log.Log;
import org.mortbay.util.StringUtil;

public class BasicAuthenticator implements Authenticator {
    public Principal authenticate(UserRealm userRealm, String str, Request request, Response response) throws IOException {
        Principal principal = null;
        String header = request.getHeader("Authorization");
        if (header != null) {
            try {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("Credentials: ").append(header).toString());
                }
                header = B64Code.decode(header.substring(header.indexOf(32) + 1), StringUtil.__ISO_8859_1);
                int indexOf = header.indexOf(58);
                String substring = header.substring(0, indexOf);
                principal = userRealm.authenticate(substring, header.substring(indexOf + 1), request);
                if (principal == null) {
                    Log.warn("AUTH FAILURE: user {}", StringUtil.printable(substring));
                } else {
                    request.setAuthType(Constraint.__BASIC_AUTH);
                    request.setUserPrincipal(principal);
                }
            } catch (Throwable e) {
                Log.warn(new StringBuffer().append("AUTH FAILURE: ").append(e.toString()).toString());
                Log.ignore(e);
            }
        }
        if (principal == null && response != null) {
            sendChallenge(userRealm, response);
        }
        return principal;
    }

    public String getAuthMethod() {
        return Constraint.__BASIC_AUTH;
    }

    public void sendChallenge(UserRealm userRealm, Response response) throws IOException {
        response.setHeader("WWW-Authenticate", new StringBuffer().append("Basic realm=\"").append(userRealm.getName()).append('\"').toString());
        response.sendError(401);
    }
}
