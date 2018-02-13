package com.parrot.freeflight.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.parrot.freeflight.utils.ARDroneMediaGallery;

public class CheckMediaAvailabilityTask extends AsyncTask<Void, Void, Boolean> {
    private Context context;

    public CheckMediaAvailabilityTask(Context context) {
        this.context = context;
    }

    protected Boolean doInBackground(Void... voidArr) {
        return Boolean.valueOf(new ARDroneMediaGallery(this.context).countOfMedia() > 0);
    }
}
