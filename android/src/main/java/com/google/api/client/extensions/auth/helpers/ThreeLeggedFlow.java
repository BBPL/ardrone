package com.google.api.client.extensions.auth.helpers;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.IOException;
import javax.jdo.PersistenceManager;

public interface ThreeLeggedFlow {
    Credential complete(String str) throws IOException;

    String getAuthorizationUrl();

    Credential loadCredential(PersistenceManager persistenceManager);

    void setHttpTransport(HttpTransport httpTransport);

    void setJsonFactory(JsonFactory jsonFactory);
}
