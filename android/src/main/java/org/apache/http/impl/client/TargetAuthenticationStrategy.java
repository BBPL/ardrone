package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.params.AuthPNames;

@Immutable
public class TargetAuthenticationStrategy extends AuthenticationStrategyImpl {
    public TargetAuthenticationStrategy() {
        super(401, "WWW-Authenticate", AuthPNames.TARGET_AUTH_PREF);
    }
}
