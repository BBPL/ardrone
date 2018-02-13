package com.parrot.freeflight.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

public class CheckAcademyAvailabilityTask extends AsyncTask<Context, Integer, Boolean> {
    public void cancel() {
        cancel(true);
    }

    protected Boolean doInBackground(Context... contextArr) {
        if (contextArr.length == 0) {
            throw new IllegalStateException("Context should be passed to CheckAcademyAvailability task");
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) contextArr[0].getSystemService("connectivity");
        boolean z = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected();
        return Boolean.valueOf(z);
    }
}
