package org.mortbay.jetty.security;

import java.security.Principal;
import org.mortbay.jetty.Request;

public interface UserRealm {
    Principal authenticate(String str, Object obj, Request request);

    void disassociate(Principal principal);

    String getName();

    Principal getPrincipal(String str);

    boolean isUserInRole(Principal principal, String str);

    void logout(Principal principal);

    Principal popRole(Principal principal);

    Principal pushRole(Principal principal, String str);

    boolean reauthenticate(Principal principal);
}
