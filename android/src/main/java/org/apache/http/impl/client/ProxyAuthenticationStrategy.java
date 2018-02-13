package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.params.AuthPNames;

@Immutable
public class ProxyAuthenticationStrategy extends AuthenticationStrategyImpl {
    public ProxyAuthenticationStrategy() {
        super(407, "Proxy-Authenticate", AuthPNames.PROXY_AUTH_PREF);
    }
}
