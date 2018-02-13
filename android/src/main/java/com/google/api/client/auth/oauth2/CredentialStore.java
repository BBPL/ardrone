package com.google.api.client.auth.oauth2;

import java.io.IOException;

public interface CredentialStore {
    void delete(String str, Credential credential) throws IOException;

    boolean load(String str, Credential credential) throws IOException;

    void store(String str, Credential credential) throws IOException;
}
