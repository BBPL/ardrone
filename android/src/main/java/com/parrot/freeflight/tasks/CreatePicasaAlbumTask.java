package com.parrot.freeflight.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.picasa.Picasa;
import com.google.api.services.picasa.model.AlbumEntry;
import java.io.IOException;

public class CreatePicasaAlbumTask extends AsyncTask<Object, Double, AlbumEntry> {
    public static final String PRIVATE_ACCESS = "private";
    public static final String PUBLIC_ACCESS = "public";
    public static final int REQUEST_AUTHORIZATION = 1;

    protected AlbumEntry doInBackground(Object... objArr) {
        if (objArr.length < 3) {
            return null;
        }
        Activity activity = (Activity) objArr[0];
        Picasa picasa = (Picasa) objArr[1];
        String str = (String) objArr[2];
        Boolean bool = (Boolean) objArr[3];
        AlbumEntry albumEntry = new AlbumEntry();
        albumEntry.setTitle(str);
        albumEntry.setRights(bool.booleanValue() ? "private" : "public");
        albumEntry.setAccess(bool.booleanValue() ? "private" : "public");
        try {
            return (AlbumEntry) picasa.albums().insert(albumEntry).execute();
        } catch (UserRecoverableAuthIOException e) {
            if (activity != null) {
                activity.startActivityForResult(e.getIntent(), 1);
                return null;
            }
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return null;
        }
    }
}
