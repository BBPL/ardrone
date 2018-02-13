package com.parrot.freeflight.academy.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Country;
import com.parrot.freeflight.academy.model.OnlineInfoItem;
import com.parrot.freeflight.academy.model.Profile;
import com.parrot.freeflight.academy.model.User;
import com.parrot.freeflight.academy.ui.MyAutoCompleteTextView;
import com.parrot.freeflight.academy.ui.ProfileMenu;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.academy.utils.JSONParser;
import com.parrot.freeflight.activities.DashboardActivity;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.AsyncTaskResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.HttpVersions;

@TargetApi(14)
public class AcademyProfileActivity extends ParrotActivity implements OnClickListener {
    private static final int DATE_DIALOG_ID = 999;
    public static final String EXTRA_PILOTID_OBJ = "pilot_id.object";
    private static int RESULT_LOAD_IMAGE = 1;
    private OnTouchListener OnTouchOutOfFocusListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionIndex() == 0) {
                switch (motionEvent.getAction()) {
                    case 0:
                        AcademyProfileActivity.this.firstPointTouch.x = (int) motionEvent.getX(0);
                        AcademyProfileActivity.this.firstPointTouch.y = (int) motionEvent.getY(0);
                        break;
                    case 1:
                        if (!(AcademyProfileActivity.this.getCurrentFocus() == null || AcademyProfileActivity.this.getCurrentFocus() == view || AcademyProfileActivity.this.movedEnough)) {
                            ((InputMethodManager) AcademyProfileActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(AcademyProfileActivity.this.getCurrentFocus().getWindowToken(), 0);
                        }
                        AcademyProfileActivity.this.movedEnough = false;
                        break;
                    case 2:
                        if (Math.pow((double) (motionEvent.getX(0) - ((float) AcademyProfileActivity.this.firstPointTouch.x)), 2.0d) + Math.pow((double) (motionEvent.getY(0) - ((float) AcademyProfileActivity.this.firstPointTouch.y)), 2.0d) > Math.pow((double) AcademyProfileActivity.this.movementPrecision, 2.0d)) {
                            AcademyProfileActivity.this.movedEnough = true;
                            break;
                        }
                        break;
                }
            }
            return false;
        }
    };
    private ActionBar actionBar;
    private EditText address;
    private EditText address2;
    private LinearLayout address2_layout;
    private TextView address_text;
    private boolean avatarHasChanged = false;
    private boolean birthDateHasChanged = false;
    private TextView birth_date;
    private View birthdateStar;
    private EditText city;
    private TextView city_text;
    private ScrollView content;
    private ArrayList<Country> countries;
    private MyAutoCompleteTextView country;
    private boolean countryHasChanged = false;
    private View countryStar;
    private TextView country_text;
    private OnClickListener editClickListener = new C10242();
    private Button edit_cancel;
    private Button edit_delete;
    private Button edit_save;
    private boolean emailHasChanged = false;
    private Point firstPointTouch = new Point();
    private EditText first_name;
    private TextView first_name_text;
    private Profile guest_profile = new Profile();
    private EditText last_name;
    private TextView last_name_text;
    private OnDateSetListener mDateSetListener = new C10297();
    private int mDay;
    private int mMonth;
    private int mYear;
    private boolean movedEnough = false;
    private int movementPrecision = 10;
    private ArrayList<OnlineInfoItem> online_infos;
    private ListView online_infos_list;
    private EditText phone;
    private int pilotId;
    private String pilotUrl;
    private EditText postal_code;
    private TextView postal_code_text;
    private Profile prevProfile;
    private OnClickListener privacyClickListener = new C10275();
    private ProfileMenu profileMenu;
    private TextView profile_complement;
    private ImageView profile_image;
    private Bitmap profile_image_bitmap;
    private String profile_image_path;
    private Country selected_country;
    private EditText status;
    private Profile temporary_profile = new Profile();
    private User user;
    private EditText username;

    class C10231 implements OnItemClickListener {
        C10231() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            AcademyProfileActivity.this.temporary_profile.setCountry((Country) adapterView.getItemAtPosition(i));
            AcademyProfileActivity.this.countryHasChanged = true;
        }
    }

    class C10242 implements OnClickListener {
        C10242() {
        }

        public void onClick(View view) {
            AcademyProfileActivity.this.findViewById(C0984R.id.profile_menu).setVisibility(4);
            AcademyProfileActivity.this.actionBar.hideSettingsButton();
            AcademyProfileActivity.this.enableTexts();
            AcademyProfileActivity.this.prevProfile = new Profile(AcademyProfileActivity.this.temporary_profile);
            AcademyProfileActivity.this.status.requestFocus();
        }
    }

    class C10253 implements Runnable {
        C10253() {
        }

        public void run() {
            super.onBackPressed();
        }
    }

    class C10264 implements Runnable {
        C10264() {
        }

        public void run() {
        }
    }

    class C10275 implements OnClickListener {
        C10275() {
        }

        public void onClick(View view) {
            AcademyProfileActivity.this.findViewById(C0984R.id.profile_menu).setVisibility(4);
            AcademyProfileActivity.this.startActivity(new Intent(AcademyProfileActivity.this, AcademyPrivacyActivity.class));
        }
    }

    class C10297 implements OnDateSetListener {
        C10297() {
        }

        @SuppressLint({"NewApi"})
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            AcademyProfileActivity.this.mYear = i;
            AcademyProfileActivity.this.mMonth = i2;
            AcademyProfileActivity.this.mDay = i3;
            AcademyProfileActivity.this.updateDisplay();
        }
    }

    private class DeleteProfileAsyncTask extends AsyncTask<Void, Void, Boolean> {

        class C10321 implements Runnable {
            C10321() {
            }

            public void run() {
                AcademyProfileActivity.this.refreshData();
            }
        }

        private DeleteProfileAsyncTask() {
        }

        protected Boolean doInBackground(Void... voidArr) {
            Boolean valueOf = Boolean.valueOf(false);
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpDelete = new HttpDelete(AcademyProfileActivity.this.getString(C0984R.string.http).concat(AcademyProfileActivity.this.getString(C0984R.string.url_server).concat(AcademyProfileActivity.this.getString(C0984R.string.url_profile))));
                httpDelete.setHeader("Authorization", "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2)));
                int statusCode = defaultHttpClient.execute(httpDelete).getStatusLine().getStatusCode();
                System.out.println(statusCode);
                switch (statusCode) {
                    case 200:
                    case 204:
                        return Boolean.valueOf(true);
                    default:
                        return valueOf;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return valueOf;
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
                return valueOf;
            } catch (IOException e3) {
                e3.printStackTrace();
                return valueOf;
            }
        }

        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool.booleanValue()) {
                AcademyUtils.clearCredentials(AcademyProfileActivity.this);
                Intent intent = new Intent(AcademyProfileActivity.this, DashboardActivity.class);
                intent.addFlags(268468224);
                AcademyProfileActivity.this.startActivity(intent);
                AcademyProfileActivity.this.startActivity(new Intent(AcademyProfileActivity.this, AcademyLoginActivity.class));
                AcademyProfileActivity.this.finish();
                return;
            }
            AcademyProfileActivity.this.showAlertDialog(AcademyProfileActivity.this.getString(C0984R.string.aa_ID000190), AcademyProfileActivity.this.getString(C0984R.string.aa_ID000183), new C10321(), null);
        }
    }

    private class GetCountriesAsyncTask extends AsyncTask<Void, Void, AsyncTaskResult<String>> {
        private GetCountriesAsyncTask() {
        }

        protected AsyncTaskResult<String> doInBackground(Void... voidArr) {
            Exception e;
            HttpURLConnection httpURLConnection;
            AsyncTaskResult<String> asyncTaskResult;
            Throwable th;
            HttpURLConnection httpURLConnection2 = null;
            try {
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(AcademyProfileActivity.this.getString(C0984R.string.http).concat(AcademyProfileActivity.this.getString(C0984R.string.url_server).concat(AcademyProfileActivity.this.getString(C0984R.string.url_countries)))).openConnection();
                try {
                    httpURLConnection3.setRequestProperty("Accept-Language", Locale.getDefault().getLanguage());
                    httpURLConnection3.connect();
                    AcademyUtils.responseCode = httpURLConnection3.getResponseCode();
                    AsyncTaskResult<String> asyncTaskResult2;
                    if (AcademyUtils.responseCode == 200) {
                        Log.w("responseCode", "200");
                        asyncTaskResult2 = new AsyncTaskResult(JSONParser.readStream(httpURLConnection3.getInputStream()), null);
                        if (httpURLConnection3 == null) {
                            return asyncTaskResult2;
                        }
                        httpURLConnection3.disconnect();
                        return asyncTaskResult2;
                    }
                    asyncTaskResult2 = new AsyncTaskResult(null, new Exception("Unexpected response code: " + AcademyUtils.responseCode));
                    if (httpURLConnection3 == null) {
                        return asyncTaskResult2;
                    }
                    httpURLConnection3.disconnect();
                    return asyncTaskResult2;
                } catch (Exception e2) {
                    e = e2;
                    httpURLConnection = httpURLConnection3;
                    try {
                        Log.e("connection", "fail");
                        e.printStackTrace();
                        asyncTaskResult = new AsyncTaskResult(null, e);
                        if (httpURLConnection != null) {
                            return asyncTaskResult;
                        }
                        httpURLConnection.disconnect();
                        return asyncTaskResult;
                    } catch (Throwable th2) {
                        th = th2;
                        httpURLConnection2 = httpURLConnection;
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    httpURLConnection2 = httpURLConnection3;
                    th = th4;
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                httpURLConnection = null;
                e = e3;
                Log.e("connection", "fail");
                e.printStackTrace();
                asyncTaskResult = new AsyncTaskResult(null, e);
                if (httpURLConnection != null) {
                    return asyncTaskResult;
                }
                httpURLConnection.disconnect();
                return asyncTaskResult;
            } catch (Throwable th5) {
                th = th5;
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                throw th;
            }
        }

        protected void onPostExecute(AsyncTaskResult<String> asyncTaskResult) {
            Throwable th;
            Throwable th2 = asyncTaskResult.exception;
            if (asyncTaskResult.succeeded()) {
                try {
                    AcademyProfileActivity.this.countries = Country.jsonToCountries(new JSONArray((String) asyncTaskResult.result));
                    if (AcademyProfileActivity.this.getCurrentProfile().getAvatar() == null || AcademyProfileActivity.this.getCurrentProfile().getAvatar().length() <= 5) {
                        AcademyProfileActivity.this.refreshData();
                        th = th2;
                    } else {
                        new GetProfileImageAsyncTask().execute(new Void[0]);
                        th = th2;
                    }
                } catch (Exception e) {
                    th = e;
                }
            } else {
                th = th2;
            }
            if (th != null) {
                AcademyProfileActivity.this.onAsyncTaskError(new Exception("Failed to get countries", th));
            }
        }
    }

    private class GetCountryInfoAsyncTask extends AsyncTask<Void, Void, Void> {
        private GetCountryInfoAsyncTask() {
        }

        protected Void doInBackground(Void... voidArr) {
            HttpURLConnection httpURLConnection;
            MalformedURLException malformedURLException;
            Throwable th;
            IOException iOException;
            JSONException jSONException;
            HttpURLConnection httpURLConnection2 = null;
            try {
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(AcademyProfileActivity.this.getString(C0984R.string.http).concat(AcademyProfileActivity.this.getString(C0984R.string.url_server).concat(AcademyProfileActivity.this.getString(C0984R.string.url_countries)).concat(AcademyProfileActivity.this.temporary_profile.getCountry().getIso()))).openConnection();
                try {
                    httpURLConnection3.connect();
                    AcademyProfileActivity.this.selected_country = new Country(new JSONObject(JSONParser.readStream(httpURLConnection3.getInputStream())));
                    if (httpURLConnection3 != null) {
                        httpURLConnection3.disconnect();
                    }
                } catch (MalformedURLException e) {
                    MalformedURLException malformedURLException2 = e;
                    httpURLConnection = httpURLConnection3;
                    malformedURLException = malformedURLException2;
                    try {
                        malformedURLException.printStackTrace();
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        httpURLConnection2 = httpURLConnection;
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    IOException iOException2 = e2;
                    httpURLConnection = httpURLConnection3;
                    iOException = iOException2;
                    iOException.printStackTrace();
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return null;
                } catch (JSONException e3) {
                    JSONException jSONException2 = e3;
                    httpURLConnection = httpURLConnection3;
                    jSONException = jSONException2;
                    jSONException.printStackTrace();
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    return null;
                } catch (Throwable th3) {
                    httpURLConnection2 = httpURLConnection3;
                    th = th3;
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                    throw th;
                }
            } catch (MalformedURLException e4) {
                malformedURLException = e4;
                httpURLConnection = null;
                malformedURLException.printStackTrace();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return null;
            } catch (IOException e5) {
                iOException = e5;
                httpURLConnection = null;
                iOException.printStackTrace();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return null;
            } catch (JSONException e6) {
                jSONException = e6;
                httpURLConnection = null;
                jSONException.printStackTrace();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return null;
            } catch (Throwable th4) {
                th = th4;
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                throw th;
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            AcademyProfileActivity.this.refreshCountryInfo();
        }
    }

    private class GetProfileAsyncTask extends AsyncTask<User, Void, AsyncTaskResult<String>> {
        private GetProfileAsyncTask() {
        }

        protected AsyncTaskResult<String> doInBackground(User... userArr) {
            Exception e;
            HttpURLConnection httpURLConnection;
            AsyncTaskResult<String> asyncTaskResult;
            Throwable th;
            HttpURLConnection httpURLConnection2 = null;
            try {
                String str = "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2));
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(AcademyProfileActivity.this.pilotUrl).openConnection();
                try {
                    httpURLConnection3.setRequestProperty("Authorization", str);
                    httpURLConnection3.connect();
                    AcademyUtils.responseCode = httpURLConnection3.getResponseCode();
                    AsyncTaskResult<String> asyncTaskResult2;
                    if (AcademyUtils.responseCode == 200) {
                        Log.w("responseCode", "200");
                        asyncTaskResult2 = new AsyncTaskResult(JSONParser.readStream(httpURLConnection3.getInputStream()), null);
                        if (httpURLConnection3 == null) {
                            return asyncTaskResult2;
                        }
                        httpURLConnection3.disconnect();
                        return asyncTaskResult2;
                    }
                    asyncTaskResult2 = new AsyncTaskResult(null, new Exception("Unexpected response code: " + AcademyUtils.responseCode));
                    if (httpURLConnection3 == null) {
                        return asyncTaskResult2;
                    }
                    httpURLConnection3.disconnect();
                    return asyncTaskResult2;
                } catch (Exception e2) {
                    e = e2;
                    httpURLConnection = httpURLConnection3;
                    try {
                        Log.e("connection", "fail");
                        e.printStackTrace();
                        asyncTaskResult = new AsyncTaskResult(null, e);
                        if (httpURLConnection != null) {
                            return asyncTaskResult;
                        }
                        httpURLConnection.disconnect();
                        return asyncTaskResult;
                    } catch (Throwable th2) {
                        th = th2;
                        httpURLConnection2 = httpURLConnection;
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    httpURLConnection2 = httpURLConnection3;
                    th = th4;
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                httpURLConnection = null;
                e = e3;
                Log.e("connection", "fail");
                e.printStackTrace();
                asyncTaskResult = new AsyncTaskResult(null, e);
                if (httpURLConnection != null) {
                    return asyncTaskResult;
                }
                httpURLConnection.disconnect();
                return asyncTaskResult;
            } catch (Throwable th5) {
                th = th5;
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                throw th;
            }
        }

        protected void onPostExecute(AsyncTaskResult<String> asyncTaskResult) {
            Throwable th;
            Throwable th2 = asyncTaskResult.exception;
            if (asyncTaskResult.succeeded()) {
                try {
                    JSONObject jSONObject;
                    JSONObject jSONObject2 = new JSONObject((String) asyncTaskResult.result);
                    if (AcademyProfileActivity.this.isMe()) {
                        AcademyUtils.profile = new Profile(jSONObject2);
                        AcademyProfileActivity.this.initBirth_date(AcademyUtils.profile);
                        jSONObject = jSONObject2;
                    } else {
                        jSONObject = jSONObject2.getJSONObject("profile");
                        AcademyProfileActivity.this.guest_profile = new Profile(jSONObject);
                        AcademyProfileActivity.this.initBirth_date(AcademyProfileActivity.this.guest_profile);
                    }
                    AcademyProfileActivity.this.temporary_profile = new Profile(jSONObject);
                    new GetCountriesAsyncTask().execute(new Void[0]);
                    th = th2;
                } catch (Exception e) {
                    th = e;
                }
            } else {
                th = th2;
            }
            if (th != null) {
                AcademyProfileActivity.this.onAsyncTaskError(new Exception("Failed to get profile", th));
            }
        }
    }

    private class GetProfileImageAsyncTask extends AsyncTask<Void, Void, AsyncTaskResult<Void>> {
        private GetProfileImageAsyncTask() {
        }

        protected AsyncTaskResult<Void> doInBackground(Void... voidArr) {
            Exception e;
            HttpURLConnection httpURLConnection;
            AsyncTaskResult<Void> asyncTaskResult;
            Throwable th;
            HttpURLConnection httpURLConnection2 = null;
            try {
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(AcademyProfileActivity.this.getString(C0984R.string.http).concat(AcademyProfileActivity.this.getString(C0984R.string.url_server)).concat(AcademyProfileActivity.this.getString(C0984R.string.url_media)).concat(AcademyProfileActivity.this.getCurrentProfile().getAvatar())).openConnection();
                try {
                    httpURLConnection3.setDoInput(true);
                    httpURLConnection3.connect();
                    InputStream inputStream = httpURLConnection3.getInputStream();
                    Options options = new Options();
                    options.inSampleSize = 2;
                    AcademyProfileActivity.this.profile_image_bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    AsyncTaskResult<Void> asyncTaskResult2 = new AsyncTaskResult(null, null);
                    if (httpURLConnection3 == null) {
                        return asyncTaskResult2;
                    }
                    httpURLConnection3.disconnect();
                    return asyncTaskResult2;
                } catch (IOException e2) {
                    e = e2;
                    httpURLConnection = httpURLConnection3;
                    try {
                        e.printStackTrace();
                        asyncTaskResult = new AsyncTaskResult(null, e);
                        if (httpURLConnection != null) {
                            return asyncTaskResult;
                        }
                        httpURLConnection.disconnect();
                        return asyncTaskResult;
                    } catch (Throwable th2) {
                        th = th2;
                        httpURLConnection2 = httpURLConnection;
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    httpURLConnection2 = httpURLConnection3;
                    th = th4;
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                httpURLConnection = null;
                e = e3;
                e.printStackTrace();
                asyncTaskResult = new AsyncTaskResult(null, e);
                if (httpURLConnection != null) {
                    return asyncTaskResult;
                }
                httpURLConnection.disconnect();
                return asyncTaskResult;
            } catch (Throwable th5) {
                th = th5;
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                throw th;
            }
        }

        protected void onPostExecute(AsyncTaskResult<Void> asyncTaskResult) {
            if (asyncTaskResult.succeeded()) {
                AcademyProfileActivity.this.refreshData();
            } else {
                AcademyProfileActivity.this.onAsyncTaskError(new Exception("Failed to get profile image", asyncTaskResult.exception));
            }
        }
    }

    private class PutProfileImageAsyncTask extends AsyncTask<Void, Void, Void> {
        private PutProfileImageAsyncTask() {
        }

        protected Void doInBackground(Void... voidArr) {
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpPut = new HttpPut(AcademyProfileActivity.this.getString(C0984R.string.http).concat(AcademyProfileActivity.this.getString(C0984R.string.url_server).concat(AcademyProfileActivity.this.getString(C0984R.string.url_profile_avatar))));
                String str = "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2));
                HttpEntity multipartEntity = new MultipartEntity();
                multipartEntity.addPart("avatar", new FileBody(new File(AcademyProfileActivity.this.profile_image_path), "image/jpeg"));
                httpPut.setHeader("Authorization", str);
                httpPut.setEntity(multipartEntity);
                HttpResponse execute = defaultHttpClient.execute(httpPut);
                HttpEntity entity = execute.getEntity();
                execute.getStatusLine().getStatusCode();
                EntityUtils.toString(entity);
                AcademyProfileActivity.this.avatarHasChanged = false;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            new GetProfileAsyncTask().execute(new User[]{AcademyProfileActivity.this.user});
        }
    }

    private class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseString;
        boolean validResponse;

        private UpdateAsyncTask() {
            this.validResponse = true;
        }

        protected Void doInBackground(Void... voidArr) {
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpPut = new HttpPut(AcademyProfileActivity.this.getString(C0984R.string.http).concat(AcademyProfileActivity.this.getString(C0984R.string.url_server).concat(AcademyProfileActivity.this.getString(C0984R.string.url_profile))));
                String str = "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2));
                List arrayList = new ArrayList();
                Profile profile = AcademyUtils.profile;
                Log.i("PROFIL BEFORE", HttpVersions.HTTP_0_9 + profile);
                Log.i("PROFIL NOW", HttpVersions.HTTP_0_9 + AcademyProfileActivity.this.temporary_profile);
                if (profile.getStatus() != AcademyProfileActivity.this.temporary_profile.getStatus()) {
                    arrayList.add(new BasicNameValuePair("status", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getStatus() != null ? AcademyProfileActivity.this.temporary_profile.getStatus() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getAddress_1() != AcademyProfileActivity.this.temporary_profile.getAddress_1()) {
                    arrayList.add(new BasicNameValuePair("address_1", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getAddress_1() != null ? AcademyProfileActivity.this.temporary_profile.getAddress_1() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getAddress_2() != AcademyProfileActivity.this.temporary_profile.getAddress_2()) {
                    arrayList.add(new BasicNameValuePair("address_2", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getAddress_2() != null ? AcademyProfileActivity.this.temporary_profile.getAddress_2() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getFirst_name() != AcademyProfileActivity.this.temporary_profile.getFirst_name()) {
                    arrayList.add(new BasicNameValuePair("first_name", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getFirst_name() != null ? AcademyProfileActivity.this.temporary_profile.getFirst_name() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getLast_name() != AcademyProfileActivity.this.temporary_profile.getLast_name()) {
                    arrayList.add(new BasicNameValuePair("last_name", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getLast_name() != null ? AcademyProfileActivity.this.temporary_profile.getLast_name() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getBirth_date() != AcademyProfileActivity.this.temporary_profile.getBirth_date()) {
                    arrayList.add(new BasicNameValuePair("birth_date", TextUtils.htmlEncode(new SimpleDateFormat("yyyy-MM-dd").format(AcademyProfileActivity.this.temporary_profile.getBirth_date()))));
                }
                if (profile.getMobile() != AcademyProfileActivity.this.temporary_profile.getMobile()) {
                    arrayList.add(new BasicNameValuePair("mobile", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getMobile() != null ? AcademyProfileActivity.this.temporary_profile.getMobile() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getPostal_code() != AcademyProfileActivity.this.temporary_profile.getPostal_code()) {
                    arrayList.add(new BasicNameValuePair("postal_code", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getPostal_code() != null ? AcademyProfileActivity.this.temporary_profile.getPostal_code() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getCity() != AcademyProfileActivity.this.temporary_profile.getCity()) {
                    arrayList.add(new BasicNameValuePair("city", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getCity() != null ? AcademyProfileActivity.this.temporary_profile.getCity() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getCountry() != AcademyProfileActivity.this.temporary_profile.getCountry()) {
                    arrayList.add(new BasicNameValuePair("country", String.valueOf(AcademyProfileActivity.this.temporary_profile.getCountry().getIso() != null ? AcademyProfileActivity.this.temporary_profile.getCountry().getIso() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getWebsite() != AcademyProfileActivity.this.temporary_profile.getWebsite()) {
                    arrayList.add(new BasicNameValuePair("website", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getWebsite() != null ? AcademyProfileActivity.this.temporary_profile.getWebsite() : HttpVersions.HTTP_0_9)));
                    Log.i("Profiles", "update website : " + AcademyProfileActivity.this.temporary_profile.getWebsite());
                }
                if (profile.getYoutube() != AcademyProfileActivity.this.temporary_profile.getYoutube()) {
                    arrayList.add(new BasicNameValuePair("youtube", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getYoutube() != null ? AcademyProfileActivity.this.temporary_profile.getYoutube() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getFacebook() != AcademyProfileActivity.this.temporary_profile.getFacebook()) {
                    arrayList.add(new BasicNameValuePair("facebook", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getFacebook() != null ? AcademyProfileActivity.this.temporary_profile.getFacebook() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getTwitter() != AcademyProfileActivity.this.temporary_profile.getTwitter()) {
                    arrayList.add(new BasicNameValuePair("twitter", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getTwitter() != null ? AcademyProfileActivity.this.temporary_profile.getTwitter() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getGoogle_plus() != AcademyProfileActivity.this.temporary_profile.getGoogle_plus()) {
                    arrayList.add(new BasicNameValuePair("google_plus", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getGoogle_plus() != null ? AcademyProfileActivity.this.temporary_profile.getGoogle_plus() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getPsn() != AcademyProfileActivity.this.temporary_profile.getGoogle_plus()) {
                    arrayList.add(new BasicNameValuePair("psn", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getPsn() != null ? AcademyProfileActivity.this.temporary_profile.getPsn() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getXbox_live() != AcademyProfileActivity.this.temporary_profile.getXbox_live()) {
                    arrayList.add(new BasicNameValuePair("xbox_live", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getXbox_live() != null ? AcademyProfileActivity.this.temporary_profile.getXbox_live() : HttpVersions.HTTP_0_9)));
                }
                if (profile.getSteam() != AcademyProfileActivity.this.temporary_profile.getSteam()) {
                    arrayList.add(new BasicNameValuePair("steam", TextUtils.htmlEncode(AcademyProfileActivity.this.temporary_profile.getSteam() != null ? AcademyProfileActivity.this.temporary_profile.getSteam() : HttpVersions.HTTP_0_9)));
                }
                httpPut.setHeader("Authorization", str);
                httpPut.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
                HttpResponse execute = defaultHttpClient.execute(httpPut);
                HttpEntity entity = execute.getEntity();
                execute.getStatusLine().getStatusCode();
                this.responseString = EntityUtils.toString(entity);
                Log.i("PROFILE", "responseString : " + this.responseString);
                if (!this.responseString.equals(HttpStatus.OK)) {
                    this.validResponse = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            if (this.validResponse) {
                if (AcademyProfileActivity.this.emailHasChanged) {
                    new UpdateEmailAsyncTask().execute(new Void[0]);
                }
                if (AcademyProfileActivity.this.avatarHasChanged) {
                    new PutProfileImageAsyncTask().execute(new Void[0]);
                    return;
                }
                new GetProfileAsyncTask().execute(new User[]{AcademyProfileActivity.this.user});
                return;
            }
            this.responseString = this.responseString.replace("\r", HttpVersions.HTTP_0_9).replace("\t", HttpVersions.HTTP_0_9).replace("\n", HttpVersions.HTTP_0_9).replace("{", HttpVersions.HTTP_0_9).replace("}", HttpVersions.HTTP_0_9).replace("[", HttpVersions.HTTP_0_9).replace("]", HttpVersions.HTTP_0_9).replace("\"", HttpVersions.HTTP_0_9);
            this.responseString.trim().replaceAll("\\s+", " ");
            AcademyProfileActivity.this.showErrorAlertDialog(AcademyProfileActivity.this.getString(C0984R.string.aa_ID000121), this.responseString);
            AcademyProfileActivity.this.refreshData();
        }
    }

    private class UpdateEmailAsyncTask extends AsyncTask<Void, Void, Void> {
        private String responseString;
        private boolean validResponse;

        private UpdateEmailAsyncTask() {
            this.validResponse = true;
        }

        protected Void doInBackground(Void... voidArr) {
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpPut = new HttpPut(AcademyProfileActivity.this.getString(C0984R.string.http).concat(AcademyProfileActivity.this.getString(C0984R.string.url_server).concat(AcademyProfileActivity.this.getString(C0984R.string.url_profile_email))));
                String str = "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2));
                List arrayList = new ArrayList();
                arrayList.add(new BasicNameValuePair("email", AcademyProfileActivity.this.temporary_profile.getUser().getEmail()));
                Log.w("Profiles", "email request " + AcademyProfileActivity.this.temporary_profile.getUser().getEmail());
                httpPut.setHeader("Authorization", str);
                httpPut.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
                HttpResponse execute = defaultHttpClient.execute(httpPut);
                HttpEntity entity = execute.getEntity();
                execute.getStatusLine().getStatusCode();
                this.responseString = EntityUtils.toString(entity);
                Log.i("PROFILE", "responseString : " + this.responseString);
                if (!this.responseString.equals(HttpStatus.OK)) {
                    this.validResponse = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            if (this.validResponse) {
                new GetProfileAsyncTask().execute(new User[]{AcademyProfileActivity.this.user});
                return;
            }
            this.responseString = this.responseString.replace("\r", HttpVersions.HTTP_0_9).replace("\t", HttpVersions.HTTP_0_9).replace("\n", HttpVersions.HTTP_0_9).replace("{", HttpVersions.HTTP_0_9).replace("}", HttpVersions.HTTP_0_9).replace("[", HttpVersions.HTTP_0_9).replace("]", HttpVersions.HTTP_0_9).replace("\"", HttpVersions.HTTP_0_9);
            this.responseString.trim().replaceAll("\\s+", " ");
            AcademyProfileActivity.this.showErrorAlertDialog(AcademyProfileActivity.this.getString(C0984R.string.aa_ID000121), this.responseString);
            AcademyProfileActivity.this.refreshData();
        }
    }

    protected class myProfileTextWatcher implements TextWatcher {
        private int id;

        public myProfileTextWatcher(int i) {
            this.id = i;
        }

        public void afterTextChanged(Editable editable) {
            switch (this.id) {
                case C0984R.id.status /*2131361896*/:
                    AcademyProfileActivity.this.temporary_profile.setStatus(editable.toString());
                    return;
                case C0984R.id.first_name /*2131361902*/:
                    AcademyProfileActivity.this.temporary_profile.setFirst_name(editable.toString());
                    return;
                case C0984R.id.last_name /*2131361905*/:
                    AcademyProfileActivity.this.temporary_profile.setLast_name(editable.toString());
                    return;
                case C0984R.id.phone /*2131361912*/:
                    AcademyProfileActivity.this.temporary_profile.setMobile(editable.toString());
                    return;
                case C0984R.id.address /*2131361915*/:
                    AcademyProfileActivity.this.temporary_profile.setAddress_1(editable.toString());
                    return;
                case C0984R.id.address2 /*2131361917*/:
                    AcademyProfileActivity.this.temporary_profile.setAddress_2(editable.toString());
                    return;
                case C0984R.id.postal_code /*2131361920*/:
                    AcademyProfileActivity.this.temporary_profile.setPostal_code(editable.toString());
                    return;
                case C0984R.id.city /*2131361922*/:
                    AcademyProfileActivity.this.temporary_profile.setCity(editable.toString());
                    return;
                case C0984R.id.country /*2131361926*/:
                    if (editable.length() == 0) {
                        AcademyProfileActivity.this.temporary_profile.setCountry(new Country());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
    }

    private boolean checkFieldsValidity() {
        boolean z = true;
        if (this.country.getText().toString().equals(HttpVersions.HTTP_0_9) || this.birth_date.getText().toString().equals("N/A")) {
            z = false;
        }
        Log.i("PROFILE", "Validity: " + z);
        return z;
    }

    private void deleteAccount() {
        showAlertDialog(getString(C0984R.string.aa_ID000190), getString(C0984R.string.aa_ID000189), new Runnable() {
            public void run() {
                AcademyProfileActivity.this.switchToLayout(C0984R.layout.academy_authentication_progress);
                ((TextView) AcademyProfileActivity.this.findViewById(C0984R.id.loading_text)).setText(AcademyProfileActivity.this.getString(C0984R.string.aa_ID000000));
                new DeleteProfileAsyncTask().execute(new Void[0]);
            }
        }, new Runnable() {
            public void run() {
            }
        });
    }

    private void disableTexts() {
        int i = 8;
        this.profile_image.setEnabled(false);
        this.username.setEnabled(false);
        this.first_name.setEnabled(false);
        this.last_name.setEnabled(false);
        this.birth_date.setEnabled(false);
        this.birthdateStar.setVisibility(8);
        this.phone.setEnabled(false);
        this.address.setEnabled(false);
        this.address2.setEnabled(false);
        this.postal_code.setEnabled(false);
        this.city.setEnabled(false);
        this.country.setEnabled(false);
        this.status.setEnabled(false);
        this.first_name_text.setVisibility(getCurrentProfile().getFirst_name() == null ? 0 : 8);
        this.last_name_text.setVisibility(getCurrentProfile().getLast_name() == null ? 0 : 8);
        this.address_text.setVisibility(getCurrentProfile().getAddress_1() == null ? 0 : 8);
        this.postal_code_text.setVisibility(getCurrentProfile().getPostal_code() == null ? 0 : 8);
        this.city_text.setVisibility(getCurrentProfile().getCity() == null ? 0 : 8);
        this.country_text.setVisibility(getCurrentProfile().getCountry() == null ? 0 : 8);
        this.countryStar.setVisibility(8);
        findViewById(C0984R.id.edit_layout).setVisibility(8);
        onlineInfoLayoutAdapter(this.online_infos, false);
        LinearLayout linearLayout = this.address2_layout;
        if (getCurrentProfile().getAddress_2() != null) {
            i = 0;
        }
        linearLayout.setVisibility(i);
        removeListeners();
    }

    private void enableTexts() {
        this.profile_image.setEnabled(true);
        this.first_name.setEnabled(true);
        this.last_name.setEnabled(true);
        this.birth_date.setEnabled(true);
        this.birthdateStar.setVisibility(0);
        this.phone.setEnabled(true);
        this.address.setEnabled(true);
        this.address2.setEnabled(true);
        this.postal_code.setEnabled(true);
        this.city.setEnabled(true);
        this.country.setEnabled(true);
        this.country.setAdapter(new ArrayAdapter(this, C0984R.layout.academy_dropdown_item_line, this.countries));
        this.country.setOnItemClickListener(new C10231());
        this.status.setEnabled(true);
        this.first_name.setText(getCurrentProfile().getFirst_name());
        this.last_name.setText(getCurrentProfile().getLast_name());
        this.birth_date.setText(getCurrentProfile().getBirth_date() != null ? DateFormat.getDateInstance().format(getCurrentProfile().getBirth_date()) : getString(C0984R.string.aa_ID000011));
        this.phone.setText(getCurrentProfile().getMobile());
        this.address.setText(getCurrentProfile().getAddress_1());
        this.address2.setText(getCurrentProfile().getAddress_2());
        this.postal_code.setText(getCurrentProfile().getPostal_code());
        this.city.setText(getCurrentProfile().getCity());
        this.country.setText(this.selected_country != null ? getCurrentProfile().getCountry().getName() : null);
        this.first_name_text.setVisibility(8);
        this.last_name_text.setVisibility(8);
        this.address_text.setVisibility(8);
        this.postal_code_text.setVisibility(8);
        this.city_text.setVisibility(8);
        this.country_text.setVisibility(8);
        this.countryStar.setVisibility(0);
        findViewById(C0984R.id.edit_layout).setVisibility(0);
        onlineInfoLayoutAdapter(this.online_infos, true);
        this.address2_layout.setVisibility(0);
        initListeners();
        if (!this.content.fullScroll(33)) {
            this.content.smoothScrollTo(0, 0);
        }
    }

    private Profile getCurrentProfile() {
        return isMe() ? AcademyUtils.profile : this.guest_profile;
    }

    private void hideKeyboard() {
        try {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    private void initActionBar() {
        this.actionBar = getParrotActionBar();
        if (isMe()) {
            this.actionBar.setTitle(getString(C0984R.string.aa_ID000104));
        } else {
            this.actionBar.setTitle(getString(C0984R.string.aa_ID000159));
        }
        this.actionBar.initBackButton();
    }

    private void initBirth_date(Profile profile) {
        if (profile.getBirth_date() == null) {
            Calendar instance = Calendar.getInstance();
            this.mYear = instance.get(1) - 5;
            this.mMonth = instance.get(2);
            this.mDay = instance.get(5);
        } else {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(profile.getBirth_date());
            this.mYear = gregorianCalendar.get(1);
            this.mMonth = gregorianCalendar.get(2);
            this.mDay = gregorianCalendar.get(5);
        }
        updateDisplay();
    }

    private void initListeners() {
        this.edit_save.setOnClickListener(this);
        this.edit_cancel.setOnClickListener(this);
        this.edit_delete.setOnClickListener(this);
        this.profile_image.setOnClickListener(this);
        this.username.addTextChangedListener(new myProfileTextWatcher(C0984R.id.username));
        this.first_name.addTextChangedListener(new myProfileTextWatcher(C0984R.id.first_name));
        this.last_name.addTextChangedListener(new myProfileTextWatcher(C0984R.id.last_name));
        this.birth_date.setOnClickListener(this);
        this.country.setOnClickListener(this);
        this.country.addTextChangedListener(new myProfileTextWatcher(C0984R.id.country));
        this.phone.addTextChangedListener(new myProfileTextWatcher(C0984R.id.phone));
        this.address.addTextChangedListener(new myProfileTextWatcher(C0984R.id.address));
        this.address2.addTextChangedListener(new myProfileTextWatcher(C0984R.id.address2));
        this.postal_code.addTextChangedListener(new myProfileTextWatcher(C0984R.id.postal_code));
        this.city.addTextChangedListener(new myProfileTextWatcher(C0984R.id.city));
        this.status.addTextChangedListener(new myProfileTextWatcher(C0984R.id.status));
    }

    private void initProfileMenu() {
        if (this.profileMenu == null) {
            this.profileMenu = new ProfileMenu(this, findViewById(C0984R.id.profile_menu));
            this.profileMenu.initEditButton(this.editClickListener);
            this.profileMenu.initPrivacyButton(this.privacyClickListener);
        }
    }

    private void initUI() {
        this.username = (EditText) findViewById(C0984R.id.username);
        this.first_name = (EditText) findViewById(C0984R.id.first_name);
        this.last_name = (EditText) findViewById(C0984R.id.last_name);
        this.birth_date = (TextView) findViewById(C0984R.id.birth_date);
        this.birthdateStar = findViewById(C0984R.id.birthdate_star);
        this.phone = (EditText) findViewById(C0984R.id.phone);
        this.address = (EditText) findViewById(C0984R.id.address);
        this.address2 = (EditText) findViewById(C0984R.id.address2);
        this.postal_code = (EditText) findViewById(C0984R.id.postal_code);
        this.city = (EditText) findViewById(C0984R.id.city);
        this.country = (MyAutoCompleteTextView) findViewById(C0984R.id.country);
        this.status = (EditText) findViewById(C0984R.id.status);
        this.profile_image = (ImageView) findViewById(C0984R.id.profile_image);
        this.first_name_text = (TextView) findViewById(C0984R.id.first_name_text);
        this.last_name_text = (TextView) findViewById(C0984R.id.last_name_text);
        this.address_text = (TextView) findViewById(C0984R.id.address_text);
        this.postal_code_text = (TextView) findViewById(C0984R.id.postal_code_text);
        this.city_text = (TextView) findViewById(C0984R.id.city_text);
        this.country_text = (TextView) findViewById(C0984R.id.country_text);
        this.countryStar = findViewById(C0984R.id.country_star);
        this.profile_complement = (TextView) findViewById(C0984R.id.profile_complement);
        this.content = (ScrollView) findViewById(C0984R.id.scroll);
        if (!this.content.fullScroll(33)) {
            this.content.smoothScrollTo(0, 0);
        }
        this.edit_save = (Button) findViewById(C0984R.id.edit_save);
        this.edit_cancel = (Button) findViewById(C0984R.id.edit_cancel);
        this.edit_delete = (Button) findViewById(C0984R.id.edit_delete);
        this.address2_layout = (LinearLayout) findViewById(C0984R.id.profile_address_2);
        setTextViews();
    }

    private boolean isMe() {
        return this.pilotId == AcademyUtils.profile.getId();
    }

    @SuppressLint({"DefaultLocale"})
    private void loadInfos(Profile profile) {
        int i = -16777216;
        Log.d("Profile", profile.toString());
        this.username.setText(profile.getUser().getUsername() != null ? profile.getUser().getUsername().toUpperCase() : getString(C0984R.string.aa_ID000011));
        this.first_name.setText(profile.getFirst_name() != null ? profile.getFirst_name() : getString(C0984R.string.aa_ID000011));
        this.first_name.setTextColor(profile.getFirst_name() != null ? -16777216 : -12303292);
        this.last_name.setText(profile.getLast_name() != null ? profile.getLast_name() : getString(C0984R.string.aa_ID000011));
        this.last_name.setTextColor(profile.getLast_name() != null ? -16777216 : -12303292);
        this.birth_date.setText(profile.getBirth_date() != null ? DateFormat.getDateInstance().format(profile.getBirth_date()) : getString(C0984R.string.aa_ID000011));
        this.birth_date.setTextColor(profile.getBirth_date() != null ? -16777216 : -12303292);
        this.phone.setText(profile.getMobile() != null ? profile.getMobile() : getString(C0984R.string.aa_ID000011));
        this.phone.setTextColor(profile.getMobile() != null ? -16777216 : -12303292);
        this.address.setText(profile.getAddress_1() != null ? profile.getAddress_1() : getString(C0984R.string.aa_ID000011));
        this.address.setTextColor(profile.getAddress_1() != null ? -16777216 : -12303292);
        this.address2.setText(profile.getAddress_2() != null ? profile.getAddress_2() : getString(C0984R.string.aa_ID000011));
        this.address2.setTextColor(profile.getAddress_2() != null ? -16777216 : -12303292);
        this.postal_code.setText(profile.getPostal_code() != null ? profile.getPostal_code() : "...");
        this.postal_code.setTextColor(profile.getPostal_code() != null ? -16777216 : -12303292);
        this.city.setText(profile.getCity() != null ? profile.getCity() : getString(C0984R.string.aa_ID000011));
        this.city.setTextColor(profile.getCity() != null ? -16777216 : -12303292);
        this.country.setText(profile.getCountry() != null ? profile.getCountry().getName() : getString(C0984R.string.aa_ID000011));
        this.country.setTextColor(profile.getCountry() != null ? -16777216 : -12303292);
        EditText editText = this.city;
        if (profile.getCity() == null) {
            i = -12303292;
        }
        editText.setTextColor(i);
        this.status.setText(profile.getStatus());
        this.selected_country = profile.getCountry();
        if (this.selected_country != null) {
            CharSequence name = profile.getCountry().getName();
            if (profile.getBirth_date() != null && getAge(this.mYear, this.mMonth, this.mDay) >= 0) {
                name = name + ", " + String.format(getString(C0984R.string.aa_ID000149), new Object[]{Integer.valueOf(getAge(this.mYear, this.mMonth, this.mDay))});
            }
            this.profile_complement.setText(name);
        }
        initOnlineInfos(getCurrentProfile());
    }

    private void onAsyncTaskError(Exception exception) {
        Toast.makeText(this, "Error occurred: " + exception.getLocalizedMessage(), 1).show();
        finish();
    }

    private void onlineInfoLayoutAdapter(final ArrayList<OnlineInfoItem> arrayList, boolean z) {
        LinearLayout linearLayout = (LinearLayout) findViewById(C0984R.id.online_infos_list);
        linearLayout.removeAllViews();
        for (int i = 0; i < arrayList.size(); i++) {
            View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C0984R.layout.academy_profile_online_infos_item, null, false);
            EditText editText = (EditText) inflate.findViewById(C0984R.id.online_info_item_edit);
            ((TextView) inflate.findViewById(C0984R.id.online_info_item_text)).setText(((OnlineInfoItem) arrayList.get(i)).getName());
            editText.setText(((OnlineInfoItem) arrayList.get(i)).getValue());
            editText.setEnabled(z);
            if (z) {
                editText.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable editable) {
                        if (!editable.toString().equals(((OnlineInfoItem) arrayList.get(i)).getValue())) {
                            ((OnlineInfoItem) arrayList.get(i)).setValue(editable.toString());
                            switch (i) {
                                case 0:
                                    AcademyProfileActivity.this.onEmailChanged();
                                    AcademyProfileActivity.this.temporary_profile.getUser().setEmail(editable.toString());
                                    return;
                                case 1:
                                    AcademyProfileActivity.this.temporary_profile.setWebsite(editable.toString());
                                    return;
                                case 2:
                                    AcademyProfileActivity.this.temporary_profile.setYoutube(editable.toString());
                                    return;
                                case 3:
                                    AcademyProfileActivity.this.temporary_profile.setFacebook(editable.toString());
                                    return;
                                case 4:
                                    AcademyProfileActivity.this.temporary_profile.setTwitter(editable.toString());
                                    return;
                                case 5:
                                    AcademyProfileActivity.this.temporary_profile.setGoogle_plus(editable.toString());
                                    return;
                                case 6:
                                    AcademyProfileActivity.this.temporary_profile.setPsn(editable.toString());
                                    return;
                                case 7:
                                    AcademyProfileActivity.this.temporary_profile.setXbox_live(editable.toString());
                                    return;
                                case 8:
                                    AcademyProfileActivity.this.temporary_profile.setSteam(editable.toString());
                                    return;
                                default:
                                    return;
                            }
                        }
                    }

                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }
                });
            }
            linearLayout.addView(inflate);
        }
    }

    private void refreshCountryInfo() {
        this.country.setText(this.selected_country.getName());
        this.temporary_profile.setCountry(this.selected_country);
    }

    private void refreshData() {
        switchToLayout(C0984R.layout.academy_profile_view);
        initUI();
        initActionBar();
        if (isMe()) {
            initProfileMenu();
            this.actionBar.initProfileSettingsButton();
        }
        loadInfos(getCurrentProfile());
        disableTexts();
        if (getCurrentProfile().getAvatar().length() > 5) {
            this.profile_image.setImageBitmap(this.profile_image_bitmap);
        }
    }

    private void removeListeners() {
        this.username.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.username));
        this.first_name.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.first_name));
        this.last_name.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.last_name));
        this.birth_date.setOnClickListener(this);
        this.country.setOnClickListener(this);
        this.phone.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.phone));
        this.address.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.address));
        this.address2.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.address2));
        this.postal_code.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.postal_code));
        this.city.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.city));
        this.status.removeTextChangedListener(new myProfileTextWatcher(C0984R.id.status));
    }

    private void saveBirthdate() {
        this.temporary_profile.setBirth_date(new GregorianCalendar(this.mYear, this.mMonth, this.mDay).getTime());
    }

    private void setTextViews() {
        this.first_name_text.setText(getString(C0984R.string.aa_ID000151) + " :");
        this.last_name_text.setText(getString(C0984R.string.aa_ID000152) + " :");
        this.address_text.setText(getString(C0984R.string.aa_ID000170) + " :");
        this.city_text.setText(getString(C0984R.string.aa_ID000117) + " :");
        this.country_text.setText(getString(C0984R.string.aa_ID000118) + " :");
        this.birth_date.setText(new StringBuilder().append(this.mMonth + 1).append("-").append(this.mDay).append("-").append(this.mYear).append(" "));
    }

    public static void start(Context context, int i) {
        Intent intent = new Intent(context, AcademyProfileActivity.class);
        intent.putExtra(EXTRA_PILOTID_OBJ, i);
        context.startActivity(intent);
    }

    private void switchToLayout(int i) {
        View inflateView = inflateView(i, null, false);
        ViewGroup viewGroup = (ViewGroup) findViewById(C0984R.id.content);
        viewGroup.removeAllViewsInLayout();
        viewGroup.addView(inflateView, new LayoutParams(-1, -1));
        inflateView.setOnTouchListener(this.OnTouchOutOfFocusListener);
    }

    private void updateDisplay() {
        if (this.birth_date != null) {
            this.birth_date.setText(new StringBuilder().append(this.mMonth + 1).append("-").append(this.mDay).append("-").append(this.mYear).append(" "));
        }
    }

    private void updateInfos() {
        if (checkFieldsValidity()) {
            if (this.birthDateHasChanged) {
                saveBirthdate();
            }
            if (!(!this.countryHasChanged || this.temporary_profile == null || this.temporary_profile.getCountry().getIso() == null)) {
                new GetCountryInfoAsyncTask().execute(new Void[0]);
            }
            updateInfosToServer();
            return;
        }
        showErrorAlertDialog(getString(C0984R.string.aa_ID000159), getString(C0984R.string.aa_ID000226));
    }

    private void updateInfosToServer() {
        hideKeyboard();
        switchToLayout(C0984R.layout.academy_authentication_progress);
        ((TextView) findViewById(C0984R.id.loading_text)).setText(getString(C0984R.string.aa_ID000000));
        new UpdateAsyncTask().execute(new Void[0]);
    }

    public int getAge(int i, int i2, int i3) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int i4 = gregorianCalendar.get(1);
        int i5 = gregorianCalendar.get(2);
        int i6 = gregorianCalendar.get(5);
        gregorianCalendar.set(i, i2, i3);
        i4 -= gregorianCalendar.get(1);
        return (i5 < gregorianCalendar.get(2) || (i5 == gregorianCalendar.get(2) && i6 < gregorianCalendar.get(5))) ? i4 - 1 : i4;
    }

    public void initOnlineInfos(Profile profile) {
        OnlineInfoItem onlineInfoItem = new OnlineInfoItem("EMAIL", profile.getUser().getEmail());
        OnlineInfoItem onlineInfoItem2 = new OnlineInfoItem("WEBSITE", profile.getWebsite());
        Log.i("Profiles", "init website : " + profile.getWebsite());
        OnlineInfoItem onlineInfoItem3 = new OnlineInfoItem("YOUTUBE", profile.getYoutube());
        OnlineInfoItem onlineInfoItem4 = new OnlineInfoItem("FACEBOOK", profile.getFacebook());
        OnlineInfoItem onlineInfoItem5 = new OnlineInfoItem("TWITTER", profile.getTwitter());
        OnlineInfoItem onlineInfoItem6 = new OnlineInfoItem("GOOGLE PLUS", profile.getGoogle_plus());
        OnlineInfoItem onlineInfoItem7 = new OnlineInfoItem("PSN", profile.getPsn());
        OnlineInfoItem onlineInfoItem8 = new OnlineInfoItem("XBOX LIVE", profile.getXbox_live());
        OnlineInfoItem onlineInfoItem9 = new OnlineInfoItem("STEAM", profile.getSteam());
        this.online_infos = new ArrayList();
        this.online_infos.add(onlineInfoItem);
        this.online_infos.add(onlineInfoItem2);
        this.online_infos.add(onlineInfoItem3);
        this.online_infos.add(onlineInfoItem4);
        this.online_infos.add(onlineInfoItem5);
        this.online_infos.add(onlineInfoItem6);
        this.online_infos.add(onlineInfoItem7);
        this.online_infos.add(onlineInfoItem8);
        this.online_infos.add(onlineInfoItem9);
        onlineInfoLayoutAdapter(this.online_infos, false);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == RESULT_LOAD_IMAGE && i2 == -1 && intent != null) {
            String[] strArr = new String[]{"_data"};
            Cursor query = getContentResolver().query(intent.getData(), strArr, null, null, null);
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex(strArr[0]));
            query.close();
            Options options = new Options();
            options.inSampleSize = 2;
            this.profile_image_bitmap = BitmapFactory.decodeFile(string, options);
            this.profile_image.setImageBitmap(this.profile_image_bitmap);
            this.profile_image_path = string;
            this.avatarHasChanged = true;
        }
    }

    public void onBackPressed() {
        hideKeyboard();
        if (this.prevProfile == null || this.prevProfile.equals(this.temporary_profile)) {
            Log.d("Profile", "Profile has not been changed");
            super.onBackPressed();
            return;
        }
        showAlertDialog((int) C0984R.string.aa_ID000154, (int) C0984R.string.aa_ID000155, new C10253(), new C10264());
        Log.d("Profile", "Profile has changed, changes lost");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.profile_image /*2131361893*/:
                startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), RESULT_LOAD_IMAGE);
                return;
            case C0984R.id.birth_date /*2131361909*/:
                showDialog(999);
                return;
            case C0984R.id.country /*2131361926*/:
                ((EditText) view).setText(null);
                return;
            case C0984R.id.edit_save /*2131361931*/:
                updateInfos();
                return;
            case C0984R.id.edit_cancel /*2131361932*/:
                disableTexts();
                this.prevProfile = null;
                loadInfos(getCurrentProfile());
                this.actionBar.initProfileSettingsButton();
                return;
            case C0984R.id.edit_delete /*2131361933*/:
                deleteAccount();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.academy_profile);
        findViewById(16908290).setOnTouchListener(this.OnTouchOutOfFocusListener);
        this.pilotId = getIntent().getIntExtra(EXTRA_PILOTID_OBJ, AcademyUtils.profile.getId());
        if (isMe()) {
            this.pilotUrl = getString(C0984R.string.http) + getString(C0984R.string.url_server) + getString(C0984R.string.url_profile);
        } else {
            this.pilotUrl = getString(C0984R.string.http) + getString(C0984R.string.url_server) + getString(C0984R.string.url_pilots) + Integer.toString(this.pilotId);
        }
        initActionBar();
        ((TextView) findViewById(C0984R.id.loading_text)).setText(getString(C0984R.string.aa_ID000000));
        new GetProfileAsyncTask().execute(new User[]{this.user});
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_PROFILE_ZONE_OPEN);
    }

    @SuppressLint({"NewApi"})
    @TargetApi(11)
    protected Dialog onCreateDialog(int i) {
        switch (i) {
            case 999:
                this.birthDateHasChanged = true;
                Dialog c10286 = new DatePickerDialog(this, this.mDateSetListener, this.mYear, this.mMonth, this.mDay) {
                    public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                        super.onDateChanged(datePicker, i, i2, i3);
                        setTitle(AcademyProfileActivity.this.getString(C0984R.string.aa_ID000111));
                    }
                };
                c10286.setTitle(getString(C0984R.string.aa_ID000111));
                c10286.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() - 157788000000L);
                return c10286;
            default:
                return null;
        }
    }

    protected void onDestroy() {
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_PROFILE_ZONE_CLOSE);
        hideKeyboard();
        super.onDestroy();
    }

    public void onEmailChanged() {
        this.emailHasChanged = true;
    }

    protected void onPause() {
        hideKeyboard();
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter != null) {
            int i = 0;
            for (int i2 = 0; i2 < adapter.getCount(); i2++) {
                View view = adapter.getView(i2, null, listView);
                if (view instanceof ViewGroup) {
                    view.setLayoutParams(new LayoutParams(-2, -2));
                }
                view.measure(0, 0);
                i += view.getMeasuredHeight();
            }
            LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = i + (listView.getDividerHeight() * (adapter.getCount() - 1));
            listView.setLayoutParams(layoutParams);
        }
    }

    protected void showAlertDialog(int i, int i2, Runnable runnable, Runnable runnable2) {
        showAlertDialog(getString(i), getString(i2), runnable, runnable2);
    }

    protected void showAlertDialog(String str, String str2, final Runnable runnable, final Runnable runnable2) {
        Builder builder = new Builder(this);
        View textView = new TextView(this);
        textView.setGravity(17);
        textView.setText(str);
        textView.setTextAppearance(this, 16974264);
        if ((super.getResources().getConfiguration().screenLayout & 15) >= 3) {
            textView.setTextSize(22.0f);
        } else {
            textView.setTextSize(18.0f);
        }
        textView.setHeight(((int) textView.getTextSize()) * 4);
        builder.setCustomTitle(textView);
        AlertDialog create = runnable2 != null ? builder.setTitle(str).setMessage(str2).setCancelable(false).setPositiveButton(getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).setNegativeButton(getString(17039360), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        }).create() : builder.setTitle(str).setMessage(str2).setCancelable(false).setPositiveButton(getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).create();
        create.show();
        ((TextView) create.findViewById(16908299)).setGravity(17);
    }

    protected void showErrorAlertDialog(String str, String str2) {
        Builder builder = new Builder(this);
        View textView = new TextView(this);
        textView.setGravity(17);
        textView.setText(str);
        textView.setTextAppearance(this, 16974264);
        if ((super.getResources().getConfiguration().screenLayout & 15) >= 3) {
            textView.setTextSize(22.0f);
        } else {
            textView.setTextSize(18.0f);
        }
        textView.setHeight(((int) textView.getTextSize()) * 4);
        builder.setCustomTitle(textView);
        AlertDialog create = builder.setMessage(str2).setCancelable(false).setPositiveButton(getString(C0984R.string.aa_ID000034), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.show();
        ((TextView) create.findViewById(16908299)).setGravity(17);
    }
}
