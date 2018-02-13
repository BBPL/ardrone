package com.google.android.gms.common;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;

public final class AccountPicker {
    private AccountPicker() {
    }

    public static Intent m14a(Account account, ArrayList<Account> arrayList, String[] strArr, boolean z, String str, String str2, String[] strArr2, Bundle bundle, boolean z2) {
        return m15a(account, arrayList, strArr, z, str, str2, strArr2, bundle, z2, 0);
    }

    public static Intent m15a(Account account, ArrayList<Account> arrayList, String[] strArr, boolean z, String str, String str2, String[] strArr2, Bundle bundle, boolean z2, int i) {
        Intent intent = new Intent();
        intent.setAction("com.google.android.gms.common.account.CHOOSE_ACCOUNT");
        intent.putExtra("allowableAccounts", arrayList);
        intent.putExtra("allowableAccountTypes", strArr);
        intent.putExtra("addAccountOptions", bundle);
        intent.putExtra("selectedAccount", account);
        intent.putExtra("alwaysPromptForAccount", z);
        intent.putExtra("descriptionTextOverride", str);
        intent.putExtra("authTokenType", str2);
        intent.putExtra("addAccountRequiredFeatures", strArr2);
        intent.putExtra("setGmsCoreAccount", z2);
        intent.putExtra("overrideTheme", i);
        return intent;
    }

    public static Intent newChooseAccountIntent(Account account, ArrayList<Account> arrayList, String[] strArr, boolean z, String str, String str2, String[] strArr2, Bundle bundle) {
        return m14a(account, arrayList, strArr, z, str, str2, strArr2, bundle, false);
    }
}
