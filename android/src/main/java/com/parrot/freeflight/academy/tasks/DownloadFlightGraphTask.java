package com.parrot.freeflight.academy.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.model.FlightDataItem;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.utils.CacheUtils;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

public class DownloadFlightGraphTask extends AsyncTask<Object, Integer, List<FlightDataItem>> {
    private static final String TAG = DownloadFlightGraphTask.class.getSimpleName();
    private String login;
    private String password;

    public DownloadFlightGraphTask(String str, String str2) {
        this.login = str;
        this.password = str2;
    }

    private List<FlightDataItem> parseJson(String str) throws JSONException {
        List<FlightDataItem> jsonArrToGraphData = FlightDataItem.jsonArrToGraphData(new JSONArray(str));
        Log.d(TAG, "Got " + jsonArrToGraphData.size() + " points");
        return jsonArrToGraphData;
    }

    protected List<FlightDataItem> doInBackground(Object... objArr) {
        Context context = (Context) objArr[0];
        Flight flight = (Flight) objArr[1];
        try {
            String cachedData = CacheUtils.getCachedData(context, "flight_details_" + flight.getId() + ".json");
            if (cachedData != null) {
                return parseJson(cachedData);
            }
            HttpClient defaultHttpClient = new DefaultHttpClient();
            String str = context.getString(C0984R.string.http).concat(context.getString(C0984R.string.url_server)).concat(context.getString(C0984R.string.url_flights)) + flight.getId() + context.getString(C0984R.string.url_details);
            Log.d(TAG, "Get Flight Graph URL: " + str);
            HttpUriRequest httpGet = new HttpGet(str);
            if (!(!flight.getUser().getUsername().equals(AcademyUtils.login) || AcademyUtils.login == null || AcademyUtils.password == null)) {
                httpGet.setHeader("Authorization", "Basic " + new String(Base64.encode((this.login + ":" + this.password).getBytes(), 2)));
            }
            HttpResponse execute = defaultHttpClient.execute(httpGet);
            HttpEntity entity = execute.getEntity();
            int statusCode = execute.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 200:
                    str = EntityUtils.toString(entity);
                    CacheUtils.cacheData(context, "flight_details_" + flight.getId() + ".json", str);
                    Log.d(TAG, "Response code: " + statusCode);
                    return parseJson(str);
                case 404:
                    Log.w(TAG, "404: Url not working");
                    break;
            }
            return Collections.emptyList();
        } catch (ClientProtocolException e) {
            Log.w(TAG, "Unable to get flights. Due to " + e.toString());
        } catch (IOException e2) {
            Log.w(TAG, "Unable to get flights. Due to " + e2.toString());
            e2.printStackTrace();
        } catch (JSONException e3) {
            Log.w(TAG, "Unable to get flights. Due to " + e3.toString());
            e3.printStackTrace();
        }
    }
}
