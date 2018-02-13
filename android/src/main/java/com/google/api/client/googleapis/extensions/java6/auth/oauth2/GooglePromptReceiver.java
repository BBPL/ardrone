package com.google.api.client.googleapis.extensions.java6.auth.oauth2;

import com.google.api.client.extensions.java6.auth.oauth2.AbstractPromptReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import java.io.IOException;

public class GooglePromptReceiver extends AbstractPromptReceiver {
    public String getRedirectUri() throws IOException {
        return GoogleOAuthConstants.OOB_REDIRECT_URI;
    }
}
