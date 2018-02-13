package com.google.api.client.auth.oauth2;

import com.google.api.client.util.Preconditions;
import java.io.IOException;

public final class CredentialStoreRefreshListener implements CredentialRefreshListener {
    private final CredentialStore credentialStore;
    private final String userId;

    public CredentialStoreRefreshListener(String str, CredentialStore credentialStore) {
        this.userId = (String) Preconditions.checkNotNull(str);
        this.credentialStore = (CredentialStore) Preconditions.checkNotNull(credentialStore);
    }

    public CredentialStore getCredentialStore() {
        return this.credentialStore;
    }

    public void makePersistent(Credential credential) throws IOException {
        this.credentialStore.store(this.userId, credential);
    }

    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
        makePersistent(credential);
    }

    public void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException {
        makePersistent(credential);
    }
}
