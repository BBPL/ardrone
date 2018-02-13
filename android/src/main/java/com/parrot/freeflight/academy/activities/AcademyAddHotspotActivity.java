package com.parrot.freeflight.academy.activities;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.PlusShare;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.dialogs.ProgressDialog;
import com.parrot.freeflight.ui.ActionBar;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class AcademyAddHotspotActivity extends ParrotActivity implements OnClickListener {
    public static final String EXTRA_LATLNG_ID = "latlng.id";
    public static final String EXTRA_LATLNG_OBJ = "latlng.object";
    private OnTouchListener OnTouchOutOfFocusListener = new C09904();
    private Button add;
    private LinearLayout add_cancel_layout;
    private Button cancel;
    private EditText description_edit;
    private ProgressDialog dialog;
    private Point firstPointTouch = new Point();
    private boolean movedEnough = false;
    private int movementPrecision = 10;
    private EditText name_edit;
    private LatLng position;
    private RatingBar rating;

    class C09871 implements Runnable {
        C09871() {
        }

        public void run() {
            AcademyAddHotspotActivity.this.finish();
        }
    }

    class C09882 implements Runnable {
        C09882() {
        }

        public void run() {
            AcademyAddHotspotActivity.this.finish();
        }
    }

    class C09904 implements OnTouchListener {
        C09904() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionIndex() == 0) {
                switch (motionEvent.getAction()) {
                    case 0:
                        AcademyAddHotspotActivity.this.firstPointTouch.x = (int) motionEvent.getX(0);
                        AcademyAddHotspotActivity.this.firstPointTouch.y = (int) motionEvent.getY(0);
                        break;
                    case 1:
                        if (!(AcademyAddHotspotActivity.this.getCurrentFocus() == null || AcademyAddHotspotActivity.this.getCurrentFocus() == view || AcademyAddHotspotActivity.this.movedEnough)) {
                            ((InputMethodManager) AcademyAddHotspotActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(AcademyAddHotspotActivity.this.getCurrentFocus().getWindowToken(), 2);
                        }
                        AcademyAddHotspotActivity.this.movedEnough = false;
                        break;
                    case 2:
                        if (Math.pow((double) (motionEvent.getX(0) - ((float) AcademyAddHotspotActivity.this.firstPointTouch.x)), 2.0d) + Math.pow((double) (motionEvent.getY(0) - ((float) AcademyAddHotspotActivity.this.firstPointTouch.y)), 2.0d) > Math.pow((double) AcademyAddHotspotActivity.this.movementPrecision, 2.0d)) {
                            AcademyAddHotspotActivity.this.movedEnough = true;
                            break;
                        }
                        break;
                }
            }
            return true;
        }
    }

    private class AddHotspotAsyncTask extends AsyncTask<Void, Void, Integer> {
        public final String description;
        public final String name;

        private AddHotspotAsyncTask(String str, String str2) {
            this.name = str;
            this.description = str2;
        }

        protected Integer doInBackground(Void... voidArr) {
            int statusCode;
            UnsupportedEncodingException e;
            ClientProtocolException e2;
            IOException e3;
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpPost = new HttpPost(AcademyAddHotspotActivity.this.getString(C0984R.string.http).concat(AcademyAddHotspotActivity.this.getString(C0984R.string.url_server)).concat(AcademyAddHotspotActivity.this.getString(C0984R.string.url_hotspots)));
                String str = "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2));
                List arrayList = new ArrayList();
                arrayList.add(new BasicNameValuePair("type", "0"));
                arrayList.add(new BasicNameValuePair("name", this.name));
                arrayList.add(new BasicNameValuePair(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, this.description));
                arrayList.add(new BasicNameValuePair("gps_latitude", String.format("%.6f", new Object[]{Double.valueOf(AcademyAddHotspotActivity.this.position.latitude)}).replace(",", ".")));
                arrayList.add(new BasicNameValuePair("gps_longitude", String.format("%.6f", new Object[]{Double.valueOf(AcademyAddHotspotActivity.this.position.longitude)}).replace(",", ".")));
                arrayList.add(new BasicNameValuePair(UpdateFlightTask.KEY_GRADE, String.valueOf(AcademyAddHotspotActivity.this.rating.getProgress())));
                httpPost.setHeader("Authorization", str);
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
                HttpResponse execute = defaultHttpClient.execute(httpPost);
                HttpEntity entity = execute.getEntity();
                statusCode = execute.getStatusLine().getStatusCode();
                try {
                    EntityUtils.toString(entity);
                } catch (UnsupportedEncodingException e4) {
                    e = e4;
                    e.printStackTrace();
                    return Integer.valueOf(statusCode);
                } catch (ClientProtocolException e5) {
                    e2 = e5;
                    e2.printStackTrace();
                    return Integer.valueOf(statusCode);
                } catch (IOException e6) {
                    e3 = e6;
                    e3.printStackTrace();
                    return Integer.valueOf(statusCode);
                }
            } catch (UnsupportedEncodingException e7) {
                UnsupportedEncodingException unsupportedEncodingException = e7;
                statusCode = 0;
                e = unsupportedEncodingException;
                e.printStackTrace();
                return Integer.valueOf(statusCode);
            } catch (ClientProtocolException e8) {
                ClientProtocolException clientProtocolException = e8;
                statusCode = 0;
                e2 = clientProtocolException;
                e2.printStackTrace();
                return Integer.valueOf(statusCode);
            } catch (IOException e9) {
                IOException iOException = e9;
                statusCode = 0;
                e3 = iOException;
                e3.printStackTrace();
                return Integer.valueOf(statusCode);
            }
            return Integer.valueOf(statusCode);
        }

        protected void onPostExecute(Integer num) {
            super.onPostExecute(num);
            AcademyAddHotspotActivity.this.dialog.dismiss();
            AcademyAddHotspotActivity.this.showMessage(num.intValue());
        }

        protected void onPreExecute() {
            super.onPreExecute();
            AcademyAddHotspotActivity.this.dialog.setIndeterminate(true);
            AcademyAddHotspotActivity.this.dialog.show();
            AcademyAddHotspotActivity.this.dialog.setCancelable(false);
        }
    }

    private void enableDisableControls() {
        this.name_edit.setEnabled(true);
        this.name_edit.setClickable(true);
        this.name_edit.setFocusable(true);
        this.name_edit.setFocusableInTouchMode(true);
        this.name_edit.setCursorVisible(true);
        this.description_edit.setEnabled(true);
        this.description_edit.setClickable(true);
        this.description_edit.setFocusable(true);
        this.description_edit.setFocusableInTouchMode(true);
        this.description_edit.setCursorVisible(true);
        this.add_cancel_layout.setVisibility(0);
        this.rating.setIsIndicator(false);
    }

    private void initActionBar() {
        ActionBar parrotActionBar = getParrotActionBar();
        parrotActionBar.initBackButton();
        parrotActionBar.setTitle(getString(C0984R.string.aa_ID000062));
    }

    private void initDialog() {
        this.dialog = new ProgressDialog(this, C0984R.style.FreeFlightDialog_Hotspot);
        this.dialog.setMessage(getString(C0984R.string.aa_ID000063).toUpperCase());
        this.dialog.setSubMessage(" ");
    }

    private void initListeners() {
        this.add.setOnClickListener(this);
        this.cancel.setOnClickListener(this);
    }

    private void initUi() {
        this.name_edit = (EditText) findViewById(C0984R.id.name_edit);
        this.description_edit = (EditText) findViewById(C0984R.id.description_edit);
        this.add_cancel_layout = (LinearLayout) findViewById(C0984R.id.add_cancel_layout);
        this.add = (Button) findViewById(C0984R.id.addspot_add);
        this.cancel = (Button) findViewById(C0984R.id.addspot_cancel);
        this.rating = (RatingBar) findViewById(C0984R.id.rating_bar);
    }

    private void showMessage(int i) {
        switch (i) {
            case 200:
                showAlertDialog(getString(C0984R.string.aa_ID000063), getString(C0984R.string.aa_ID000067), new C09871());
                return;
            default:
                showAlertDialog(getString(C0984R.string.aa_ID000063), getString(C0984R.string.aa_ID000065), new C09882());
                return;
        }
    }

    public static void start(Context context, LatLng latLng) {
        Intent intent = new Intent(context, AcademyAddHotspotActivity.class);
        intent.putExtra(EXTRA_LATLNG_OBJ, latLng);
        context.startActivity(intent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.addspot_cancel /*2131361855*/:
                super.onBackPressed();
                return;
            case C0984R.id.addspot_add /*2131362007*/:
                new AddHotspotAsyncTask(this.name_edit.getText().toString(), this.description_edit.getText().toString()).execute(new Void[0]);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_academy_hotspot_details);
        findViewById(16908290).setOnTouchListener(this.OnTouchOutOfFocusListener);
        this.position = (LatLng) getIntent().getExtras().get(EXTRA_LATLNG_OBJ);
        initActionBar();
        initUi();
        initListeners();
        initDialog();
        enableDisableControls();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void showAlertDialog(String str, String str2, final Runnable runnable) {
        new Builder(this).setTitle(str).setMessage(str2).setCancelable(false).setNegativeButton(getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).create().show();
    }
}
