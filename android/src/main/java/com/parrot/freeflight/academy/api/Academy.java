package com.parrot.freeflight.academy.api;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.AcademyCredentials;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.model.Keys;
import com.parrot.freeflight.academy.model.Profile;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.academy.utils.JSONParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Academy {
    private static final String TAG = Academy.class.getSimpleName();

    private static void applyBasicAuthorization(HttpURLConnection httpURLConnection, AcademyCredentials academyCredentials) {
        httpURLConnection.setRequestProperty("Authorization", academyCredentials.getBasicAuthorizationString());
    }

    private static void applyBasicAuthorization(HttpRequestBase httpRequestBase, AcademyCredentials academyCredentials) {
        httpRequestBase.setHeader("Authorization", academyCredentials.getBasicAuthorizationString());
    }

    public static List<Flight> getFlights(Context context, AcademyCredentials academyCredentials, Date date, Date date2, int i, int i2) throws ClientProtocolException, IOException, JSONException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        HttpClient defaultHttpClient = new DefaultHttpClient();
        String str = context.getString(C0984R.string.http).concat(context.getString(C0984R.string.url_server)).concat(context.getString(C0984R.string.url_flights)) + "?";
        Log.d(TAG, "GetFlights URL: " + str);
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("start_date", simpleDateFormat.format(date)));
        arrayList.add(new BasicNameValuePair("end_date", simpleDateFormat.format(date2)));
        arrayList.add(new BasicNameValuePair("page", Integer.toString(i)));
        arrayList.add(new BasicNameValuePair("paginate_by", Integer.toString(i2)));
        HttpRequestBase httpGet = new HttpGet(str + URLEncodedUtils.format(arrayList, "UTF-8"));
        applyBasicAuthorization(httpGet, academyCredentials);
        HttpResponse execute = defaultHttpClient.execute(httpGet);
        HttpEntity entity = execute.getEntity();
        int statusCode = execute.getStatusLine().getStatusCode();
        String entityUtils = EntityUtils.toString(entity);
        Log.d(TAG, "Response code: " + statusCode);
        return Flight.jsonArrToFlights(new JSONArray(entityUtils));
    }

    public static Map<String, String> getKeys(Context context, AcademyCredentials academyCredentials) throws ClientProtocolException, IOException, JSONException {
        String str = context.getString(C0984R.string.http) + context.getString(C0984R.string.url_server) + context.getString(C0984R.string.url_keys);
        Log.d(TAG, "Get keys request: " + str);
        HttpRequestBase httpGet = new HttpGet(str);
        applyBasicAuthorization(httpGet, academyCredentials);
        AndroidHttpClient androidHttpClient = null;
        try {
            androidHttpClient = AndroidHttpClient.newInstance("Android");
            HttpResponse execute = androidHttpClient.execute(httpGet);
            HttpEntity entity = execute.getEntity();
            int statusCode = execute.getStatusLine().getStatusCode();
            String entityUtils = EntityUtils.toString(entity);
            if (succeeded(statusCode)) {
                Map<String, String> jsonObjectToKeys = Keys.jsonObjectToKeys(new JSONObject(entityUtils));
                return jsonObjectToKeys;
            }
            throw new HttpResponseException(statusCode, execute.getStatusLine().getReasonPhrase());
        } finally {
            androidHttpClient.close();
        }
    }

    public static Profile getProfile(Context context, AcademyCredentials academyCredentials) throws IOException, JSONException, ParseException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(context.getString(C0984R.string.http).concat(context.getString(C0984R.string.url_server).concat(context.getString(C0984R.string.url_profile)))).openConnection();
        applyBasicAuthorization(httpURLConnection, academyCredentials);
        httpURLConnection.connect();
        AcademyUtils.responseCode = httpURLConnection.getResponseCode();
        switch (AcademyUtils.responseCode) {
            case 200:
                Profile profile = new Profile(new JSONObject(JSONParser.readStream(httpURLConnection.getInputStream())));
                Log.w("responseCode", "200");
                return profile;
            case 401:
                Log.w("responseCode", "401");
                return null;
            default:
                return null;
        }
    }

    private static void registerMediaUrl(Context context, String str, AcademyCredentials academyCredentials, int i, String str2, boolean z) throws ClientProtocolException, IOException {
        HttpRequestBase httpPost = new HttpPost(str);
        applyBasicAuthorization(httpPost, academyCredentials);
        String str3 = z ? "1" : "0";
        httpPost.setEntity(new StringEntity(String.format("url=%s&visible=%s", new Object[]{str2, str3}), "UTF-8"));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        AndroidHttpClient androidHttpClient = null;
        try {
            androidHttpClient = AndroidHttpClient.newInstance("Android");
            HttpResponse execute = androidHttpClient.execute(httpPost);
            execute.getEntity();
            int statusCode = execute.getStatusLine().getStatusCode();
            if (succeeded(statusCode)) {
                Log.i(TAG, "Register URl. Response Code: " + statusCode);
            } else {
                Log.w(TAG, "Register URl. Response Code: " + statusCode);
                throw new HttpResponseException(statusCode, execute.getStatusLine().getReasonPhrase());
            }
        } finally {
            androidHttpClient.close();
        }
    }

    public static void registerPhotoUrl(Context context, AcademyCredentials academyCredentials, int i, String str, boolean z) throws ClientProtocolException, IOException {
        registerMediaUrl(context, context.getString(C0984R.string.http) + context.getString(C0984R.string.url_server) + String.format(context.getString(C0984R.string.url_register_url_video), new Object[]{Integer.valueOf(i)}), academyCredentials, i, str, z);
    }

    public static void registerVideoUrl(Context context, AcademyCredentials academyCredentials, int i, String str, boolean z) throws ClientProtocolException, IOException {
        registerMediaUrl(context, context.getString(C0984R.string.http) + context.getString(C0984R.string.url_server) + String.format(context.getString(C0984R.string.url_register_url_video), new Object[]{Integer.valueOf(i)}), academyCredentials, i, str, z);
    }

    private static boolean succeeded(int i) {
        return i >= 200 && i < 300;
    }
}
