package com.parrot.freeflight.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.parrot.freeflight.academy.api.Academy;
import com.parrot.freeflight.academy.model.AcademyCredentials;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.utils.ARDroneMediaGallery;
import java.io.IOException;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.mortbay.jetty.HttpVersions;

public class RegisterThumbnailUrlTask extends AsyncTask<Void, Void, Boolean> implements Runnable {
    private static final String TAG = RegisterThumbnailUrlTask.class.getSimpleName();
    private Context context;
    private boolean isPublic;
    private String path;
    private String thumbUrl;

    public RegisterThumbnailUrlTask(Context context, String str, String str2, boolean z) {
        this.context = context;
        this.path = str;
        this.thumbUrl = fixThumbnailUrl(str2);
        this.isPublic = z;
    }

    private static String fixThumbnailUrl(String str) {
        return (str == null || str.length() == 0) ? str : str.replace("://", ":/");
    }

    protected Boolean doInBackground(Void... voidArr) {
        AcademyCredentials academyCredentials = new AcademyCredentials(AcademyUtils.login, AcademyUtils.password);
        Boolean bool = Boolean.FALSE;
        try {
            Map keys = Academy.getKeys(this.context, academyCredentials);
            String userboxByPath = new ARDroneMediaGallery(this.context).getUserboxByPath(this.path);
            if (userboxByPath == null || userboxByPath.equalsIgnoreCase("unknown")) {
                Log.i(TAG, "Registration of url skipped as we do not know userbox id for media " + this.path);
            } else {
                userboxByPath = userboxByPath.substring(userboxByPath.indexOf("_")).replaceAll("_", HttpVersions.HTTP_0_9);
                String str = (String) keys.get(userboxByPath);
                if (str != null) {
                    if (this.path.endsWith("jpg")) {
                        Academy.registerPhotoUrl(this.context, academyCredentials, Integer.parseInt(str), this.thumbUrl, this.isPublic);
                    } else if (this.path.endsWith("mp4")) {
                        Academy.registerVideoUrl(this.context, academyCredentials, Integer.parseInt(str), this.thumbUrl, this.isPublic);
                    }
                    Log.i(TAG, "Media " + this.path + "registered on Academy with success. Flight: " + str);
                } else {
                    Log.i(TAG, "Registration of url skipped as flight " + userboxByPath + " is not registered on Academy");
                }
            }
            return Boolean.TRUE;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return bool;
        } catch (IOException e2) {
            e2.printStackTrace();
            return bool;
        } catch (JSONException e3) {
            e3.printStackTrace();
            return bool;
        }
    }

    public void run() {
        onPreExecute();
        onPostExecute(doInBackground(new Void[0]));
    }
}
