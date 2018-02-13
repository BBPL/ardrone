package com.parrot.freeflight.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.picasa.Picasa;
import com.google.api.services.picasa.Picasa.Feed;
import com.google.api.services.picasa.model.AlbumEntry;
import com.google.api.services.picasa.model.AlbumFeedResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GetPicasaAlbumsTask extends AsyncTask<Object, Void, List<AlbumEntry>> {
    public static final int REQUEST_AUTHORIZATION = 1;

    protected List<AlbumEntry> doInBackground(Object... objArr) {
        List<AlbumEntry> items;
        UserRecoverableAuthIOException e;
        List<AlbumEntry> list;
        IOException iOException;
        IllegalArgumentException illegalArgumentException;
        if (objArr.length < 2) {
            return null;
        }
        Activity activity = (Activity) objArr[0];
        try {
            Feed.List list2 = ((Picasa) objArr[1]).albums().list();
            list2.setFields("id,title,link,author(name,uri),openSearch:totalResults,entry(id,title,rights)");
            list2.setKind("album");
            items = ((AlbumFeedResponse) list2.execute()).getItems();
            if (items != null) {
                return items;
            }
            try {
                return Collections.emptyList();
            } catch (UserRecoverableAuthIOException e2) {
                e = e2;
                if (activity == null) {
                    e.printStackTrace();
                    return items;
                }
                activity.startActivityForResult(e.getIntent(), 1);
                return items;
            } catch (IOException e3) {
                IOException iOException2 = e3;
                list = items;
                iOException = iOException2;
                iOException.printStackTrace();
                return list;
            } catch (IllegalArgumentException e4) {
                IllegalArgumentException illegalArgumentException2 = e4;
                list = items;
                illegalArgumentException = illegalArgumentException2;
                illegalArgumentException.printStackTrace();
                return list;
            }
        } catch (UserRecoverableAuthIOException e5) {
            UserRecoverableAuthIOException userRecoverableAuthIOException = e5;
            items = null;
            e = userRecoverableAuthIOException;
            if (activity == null) {
                activity.startActivityForResult(e.getIntent(), 1);
                return items;
            }
            e.printStackTrace();
            return items;
        } catch (IOException e32) {
            iOException = e32;
            list = null;
            iOException.printStackTrace();
            return list;
        } catch (IllegalArgumentException e42) {
            illegalArgumentException = e42;
            list = null;
            illegalArgumentException.printStackTrace();
            return list;
        }
    }
}
