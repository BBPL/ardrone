package com.parrot.freeflight.settings;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class AccountChecker {
    public static String getGoogleAccountName(ApplicationSettings applicationSettings, Context context) {
        String googleAccountName = applicationSettings.getGoogleAccountName();
        if (googleAccountName != null) {
            for (Account account : AccountManager.get(context).getAccounts()) {
                if (googleAccountName.equalsIgnoreCase(account.name)) {
                    return googleAccountName;
                }
            }
        }
        return null;
    }
}
