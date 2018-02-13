package com.parrot.freeflight.track_3d_viewer.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.AcademyCredentials;
import com.parrot.freeflight.academy.model.FlightDataItem;
import com.parrot.freeflight.academy.utils.JSONParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class LoadFlightDetailsTask extends AsyncTask<Integer, Void, TaskResult> {
    private final Context context;
    private final AcademyCredentials creds;

    public static class TaskResult {
        public final Exception exception;
        public final ArrayList<FlightDataItem> items;

        public TaskResult(ArrayList<FlightDataItem> arrayList, Exception exception) {
            this.items = arrayList;
            this.exception = exception;
        }
    }

    public LoadFlightDetailsTask(Context context, AcademyCredentials academyCredentials) {
        this.context = context;
        this.creds = academyCredentials;
    }

    private ArrayList<FlightDataItem> loadFlightDetails(int i) throws IOException, JSONException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(String.format(this.context.getString(C0984R.string.http) + this.context.getString(C0984R.string.url_server) + this.context.getString(C0984R.string.url_get_track), new Object[]{Integer.valueOf(i)})).openConnection();
        if (this.creds != null) {
            httpURLConnection.setRequestProperty("Authorization", this.creds.getBasicAuthorizationString());
        }
        httpURLConnection.connect();
        return FlightDataItem.jsonArrToGraphData(new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream())));
    }

    protected TaskResult doInBackground(Integer... numArr) {
        try {
            return new TaskResult(loadFlightDetails(numArr[0].intValue()), null);
        } catch (Exception e) {
            return new TaskResult(null, e);
        }
    }
}
