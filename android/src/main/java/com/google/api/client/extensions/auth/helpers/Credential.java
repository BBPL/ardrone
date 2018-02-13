package com.google.api.client.extensions.auth.helpers;

import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;

public interface Credential extends HttpRequestInitializer, HttpExecuteInterceptor, HttpUnsuccessfulResponseHandler {
    boolean isInvalid();
}
