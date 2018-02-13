package com.google.api.client.googleapis.extensions.android.gms.auth;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.google.api.client.http.BackOffPolicy;
import com.google.api.client.http.ExponentialBackOffPolicy;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.util.Preconditions;
import java.io.IOException;

public class GoogleAccountCredential implements HttpRequestInitializer {
    private final GoogleAccountManager accountManager;
    private String accountName;
    final Context context;
    final String scope;
    private Account selectedAccount;

    class RequestHandler implements HttpExecuteInterceptor, HttpUnsuccessfulResponseHandler {
        boolean received401;
        String token;

        RequestHandler() {
        }

        public boolean handleResponse(HttpRequest httpRequest, HttpResponse httpResponse, boolean z) {
            if (httpResponse.getStatusCode() != 401 || this.received401) {
                return false;
            }
            this.received401 = true;
            GoogleAuthUtil.invalidateToken(GoogleAccountCredential.this.context, this.token);
            return true;
        }

        public void intercept(HttpRequest httpRequest) throws IOException {
            try {
                this.token = GoogleAccountCredential.this.getToken();
                httpRequest.getHeaders().setAuthorization("Bearer " + this.token);
            } catch (GooglePlayServicesAvailabilityException e) {
                throw new GooglePlayServicesAvailabilityIOException(e);
            } catch (UserRecoverableAuthException e2) {
                throw new UserRecoverableAuthIOException(e2);
            } catch (GoogleAuthException e3) {
                throw new GoogleAuthIOException(e3);
            }
        }
    }

    public GoogleAccountCredential(Context context, String str) {
        this.accountManager = new GoogleAccountManager(context);
        this.context = context;
        this.scope = str;
    }

    public static GoogleAccountCredential usingAudience(Context context, String str) {
        Preconditions.checkArgument(str.length() != 0);
        return new GoogleAccountCredential(context, "audience:" + str);
    }

    public static GoogleAccountCredential usingOAuth2(Context context, String str, String... strArr) {
        StringBuilder append = new StringBuilder("oauth2:").append(str);
        for (String append2 : strArr) {
            append.append(' ').append(append2);
        }
        return new GoogleAccountCredential(context, append.toString());
    }

    public final Account[] getAllAccounts() {
        return this.accountManager.getAccounts();
    }

    public final Context getContext() {
        return this.context;
    }

    public final GoogleAccountManager getGoogleAccountManager() {
        return this.accountManager;
    }

    public final String getScope() {
        return this.scope;
    }

    public final Account getSelectedAccount() {
        return this.selectedAccount;
    }

    public final String getSelectedAccountName() {
        return this.accountName;
    }

    public final String getToken() throws IOException, GoogleAuthException {
        BackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        while (true) {
            try {
                break;
            } catch (IOException e) {
                long nextBackOffMillis = exponentialBackOffPolicy.getNextBackOffMillis();
                if (nextBackOffMillis == -1) {
                    throw e;
                }
                try {
                    Thread.sleep(nextBackOffMillis);
                } catch (InterruptedException e2) {
                }
            }
        }
        return GoogleAuthUtil.getToken(this.context, this.accountName, this.scope);
    }

    public void initialize(HttpRequest httpRequest) {
        Object requestHandler = new RequestHandler();
        httpRequest.setInterceptor(requestHandler);
        httpRequest.setUnsuccessfulResponseHandler(requestHandler);
    }

    public final Intent newChooseAccountIntent() {
        return AccountPicker.newChooseAccountIntent(this.selectedAccount, null, new String[]{"com.google"}, true, null, null, null, null);
    }

    public final GoogleAccountCredential setSelectedAccountName(String str) {
        this.selectedAccount = this.accountManager.getAccountByName(str);
        if (this.selectedAccount == null) {
            str = null;
        }
        this.accountName = str;
        return this;
    }
}
