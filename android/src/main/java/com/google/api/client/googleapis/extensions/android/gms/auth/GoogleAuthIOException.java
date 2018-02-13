package com.google.api.client.googleapis.extensions.android.gms.auth;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.util.Preconditions;
import java.io.IOException;

public class GoogleAuthIOException extends IOException {
    private static final long serialVersionUID = 1;

    GoogleAuthIOException(GoogleAuthException googleAuthException) {
        initCause((Throwable) Preconditions.checkNotNull(googleAuthException));
    }

    public GoogleAuthException getCause() {
        return (GoogleAuthException) super.getCause();
    }
}
