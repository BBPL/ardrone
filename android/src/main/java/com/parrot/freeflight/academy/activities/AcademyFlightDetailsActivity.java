package com.parrot.freeflight.academy.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.model.User;
import com.parrot.freeflight.academy.utils.AcademyFormatUtils;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.GalleryActivity;
import com.parrot.freeflight.activities.MediaActivity;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.track_3d_viewer.Track3DViewerActivity;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.ARDroneMediaGallery;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.mortbay.util.URIUtil;

public class AcademyFlightDetailsActivity extends ParrotActivity implements OnClickListener, OnRatingBarChangeListener, OnCheckedChangeListener {
    public static final String EXTRA_FLIGHT_ID = "flight.id";
    public static final String EXTRA_FLIGHT_OBJ = "flight.object";
    public static final double INVALID_COORDINATES = 500.0d;
    private ActionBar actionBar;
    private View btnGraphic;
    private View btnMedia;
    private View btnPosition;
    private View btnProfil;
    public Flight flight;
    private GetAddressFromLatLonTask getAddressTask;
    private boolean isFinishing = false;
    private int mediaCount;
    private ARDroneMediaGallery mediaGallery;
    private int pilotId = -1;
    private RadioGroup radioGroupVisibility;
    private int rating;
    private RatingBar ratingBar;
    private int ratingOld;
    private boolean visible;
    private boolean visibleOld;

    class C09911 implements OnClickListener {
        C09911() {
        }

        public void onClick(View view) {
            if (!AcademyFlightDetailsActivity.this.isFinishing) {
                AcademyFlightDetailsActivity.this.updateInformationsAndFinish();
            }
        }
    }

    private final class GetAddressFromLatLonTask extends AsyncTask<Long, Integer, String> {
        private final String TAG;

        private GetAddressFromLatLonTask() {
            this.TAG = GetAddressFromLatLonTask.class.getSimpleName();
        }

        protected java.lang.String doInBackground(java.lang.Long... r13) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.GetAddressFromLatLonTask.doInBackground(java.lang.Long[]):java.lang.String. bs: [B:8:0x0046, B:12:0x006b, B:33:0x00cd]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r12 = this;
            r7 = 0;
            r8 = 0;
            r1 = new android.location.Geocoder;
            r0 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;
            r2 = java.util.Locale.getDefault();
            r1.<init>(r0, r2);
            r9 = new java.lang.StringBuilder;
            r9.<init>();
            r0 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;
            r0 = r0.flight;
            r2 = r0.getGps_latitude();
            r0 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;
            r0 = r0.flight;
            r4 = r0.getGps_longitude();
            r10 = -4587338432941916160; // 0xc056800000000000 float:0.0 double:-90.0;
            r0 = (r2 > r10 ? 1 : (r2 == r10 ? 0 : -1));
            if (r0 < 0) goto L_0x0046;
        L_0x002b:
            r10 = 4636033603912859648; // 0x4056800000000000 float:0.0 double:90.0;
            r0 = (r2 > r10 ? 1 : (r2 == r10 ? 0 : -1));
            if (r0 > 0) goto L_0x0046;
        L_0x0034:
            r10 = -4582834833314545664; // 0xc066800000000000 float:0.0 double:-180.0;
            r0 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
            if (r0 < 0) goto L_0x0046;
        L_0x003d:
            r10 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
            r0 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
            if (r0 <= 0) goto L_0x006a;
        L_0x0046:
            r0 = r12.TAG;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1.<init>();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r6 = "Can't request for address as latitude and longitude is not valid. Lat: ";	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r1.append(r6);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r1.append(r2);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = " Lon: ";	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r1.append(r2);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r1.append(r4);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r1.toString();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            android.util.Log.w(r0, r1);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r0 = r7;
        L_0x0069:
            return r0;
        L_0x006a:
            r6 = 1;
            r0 = r1.getFromLocation(r2, r4, r6);	 Catch:{ IllegalArgumentException -> 0x0079, IOException -> 0x00a3, JSONException -> 0x011f }
        L_0x006f:
            if (r0 == 0) goto L_0x0077;
        L_0x0071:
            r1 = r0.size();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            if (r1 != 0) goto L_0x00a9;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
        L_0x0077:
            r0 = r7;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            goto L_0x0069;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
        L_0x0079:
            r0 = move-exception;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r12.TAG;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r6 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r6.<init>();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r8 = "Can't request for address as latitude and longitude is not valid. Lat: ";	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r6 = r6.append(r8);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = r6.append(r2);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r3 = " Lon: ";	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = r2.append(r3);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = r2.append(r4);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = r2.toString();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            android.util.Log.w(r1, r2);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            throw r0;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
        L_0x009d:
            r0 = move-exception;
            r0.printStackTrace();
        L_0x00a1:
            r0 = r7;
            goto L_0x0069;
        L_0x00a3:
            r0 = move-exception;
            r0 = com.parrot.freeflight.academy.utils.GeocoderAlt.getFromLocationAlt(r2, r4);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            goto L_0x006f;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
        L_0x00a9:
            r1 = 0;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r0 = r0.get(r1);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r0 = (android.location.Address) r0;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = r0.getMaxAddressLineIndex();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r8;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
        L_0x00b5:
            if (r1 >= r2) goto L_0x00c6;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
        L_0x00b7:
            r3 = r0.getAddressLine(r1);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r9.append(r3);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r3 = ", ";	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r9.append(r3);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r1 + 1;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            goto L_0x00b5;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
        L_0x00c6:
            r1 = r0.getCountryName();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r9.append(r1);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = 2131165228; // 0x7f07002c float:1.7944667E38 double:1.052935525E-314;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r1 = r1.getString(r2);	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = 3;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = new java.lang.Object[r2];	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r3 = 0;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r4 = r9.toString();	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r2[r3] = r4;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r3 = 1;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r4 = r0.getLatitude();	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r4 = java.lang.Double.valueOf(r4);	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r2[r3] = r4;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r3 = 2;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r4 = r0.getLongitude();	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r0 = java.lang.Double.valueOf(r4);	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r2[r3] = r0;	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            r0 = java.lang.String.format(r1, r2);	 Catch:{ IllegalArgumentException -> 0x00fc, JSONException -> 0x011f, IOException -> 0x0125 }
            goto L_0x0069;
        L_0x00fc:
            r0 = move-exception;
            r1 = r12.TAG;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2.<init>();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r3 = "Unable to format address due to exception. Looks like format string is bad. Format string: ";	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = r2.append(r3);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r3 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r4 = 2131165228; // 0x7f07002c float:1.7944667E38 double:1.052935525E-314;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r3 = r3.getString(r4);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = r2.append(r3);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            r2 = r2.toString();	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            android.util.Log.w(r1, r2);	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
            throw r0;	 Catch:{ IllegalArgumentException -> 0x009d, JSONException -> 0x011f, IOException -> 0x0125 }
        L_0x011f:
            r0 = move-exception;
            r0.printStackTrace();
            goto L_0x00a1;
        L_0x0125:
            r0 = move-exception;
            r1 = r12.TAG;
            r2 = "Can't request for address. Looks like no internet connection available.";
            android.util.Log.w(r1, r2);
            r0.printStackTrace();
            goto L_0x00a1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.GetAddressFromLatLonTask.doInBackground(java.lang.Long[]):java.lang.String");
        }

        protected void onPostExecute(String str) {
            if (str != null) {
                ((TextView) AcademyFlightDetailsActivity.this.findViewById(C0984R.id.activity_academy_flight_details_label_flightposition)).setText(str);
            }
        }
    }

    private class GetProfileIdWithUserNameAsyncTask extends AsyncTask<Void, Void, Void> {
        private GetProfileIdWithUserNameAsyncTask() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.Void doInBackground(java.lang.Void... r6) {
            /*
            r5 = this;
            r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ec }
            r0.<init>();	 Catch:{ Exception -> 0x00ec }
            r1 = "Basic ";
            r0 = r0.append(r1);	 Catch:{ Exception -> 0x00ec }
            r1 = new java.lang.String;	 Catch:{ Exception -> 0x00ec }
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ec }
            r2.<init>();	 Catch:{ Exception -> 0x00ec }
            r3 = com.parrot.freeflight.academy.utils.AcademyUtils.login;	 Catch:{ Exception -> 0x00ec }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x00ec }
            r3 = ":";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x00ec }
            r3 = com.parrot.freeflight.academy.utils.AcademyUtils.password;	 Catch:{ Exception -> 0x00ec }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x00ec }
            r2 = r2.toString();	 Catch:{ Exception -> 0x00ec }
            r2 = r2.getBytes();	 Catch:{ Exception -> 0x00ec }
            r3 = 2;
            r2 = android.util.Base64.encode(r2, r3);	 Catch:{ Exception -> 0x00ec }
            r1.<init>(r2);	 Catch:{ Exception -> 0x00ec }
            r0 = r0.append(r1);	 Catch:{ Exception -> 0x00ec }
            r1 = r0.toString();	 Catch:{ Exception -> 0x00ec }
            r0 = new java.net.URL;	 Catch:{ Exception -> 0x00ec }
            r2 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ Exception -> 0x00ec }
            r3 = 2131165488; // 0x7f070130 float:1.7945195E38 double:1.052935653E-314;
            r2 = r2.getString(r3);	 Catch:{ Exception -> 0x00ec }
            r3 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ Exception -> 0x00ec }
            r4 = 2131165489; // 0x7f070131 float:1.7945197E38 double:1.0529356537E-314;
            r3 = r3.getString(r4);	 Catch:{ Exception -> 0x00ec }
            r2 = r2.concat(r3);	 Catch:{ Exception -> 0x00ec }
            r3 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ Exception -> 0x00ec }
            r4 = 2131165508; // 0x7f070144 float:1.7945235E38 double:1.052935663E-314;
            r3 = r3.getString(r4);	 Catch:{ Exception -> 0x00ec }
            r2 = r2.concat(r3);	 Catch:{ Exception -> 0x00ec }
            r3 = "?q=";
            r2 = r2.concat(r3);	 Catch:{ Exception -> 0x00ec }
            r3 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ Exception -> 0x00ec }
            r3 = r3.flight;	 Catch:{ Exception -> 0x00ec }
            r3 = r3.getUser();	 Catch:{ Exception -> 0x00ec }
            r3 = r3.getUsername();	 Catch:{ Exception -> 0x00ec }
            r2 = r2.concat(r3);	 Catch:{ Exception -> 0x00ec }
            r0.<init>(r2);	 Catch:{ Exception -> 0x00ec }
            r0 = r0.openConnection();	 Catch:{ Exception -> 0x00ec }
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00ec }
            r2 = "Authorization";
            r0.setRequestProperty(r2, r1);	 Catch:{ Exception -> 0x00ec }
            r0.connect();	 Catch:{ Exception -> 0x00ec }
            r1 = r0.getResponseCode();	 Catch:{ Exception -> 0x00ec }
            com.parrot.freeflight.academy.utils.AcademyUtils.responseCode = r1;	 Catch:{ Exception -> 0x00ec }
            r1 = com.parrot.freeflight.academy.utils.AcademyUtils.responseCode;	 Catch:{ Exception -> 0x00ec }
            switch(r1) {
                case 200: goto L_0x0095;
                case 401: goto L_0x00f8;
                default: goto L_0x0093;
            };	 Catch:{ Exception -> 0x00ec }
        L_0x0093:
            r0 = 0;
            return r0;
        L_0x0095:
            r1 = new org.json.JSONArray;	 Catch:{ Exception -> 0x00ec }
            r0 = r0.getInputStream();	 Catch:{ Exception -> 0x00ec }
            r0 = com.parrot.freeflight.academy.utils.JSONParser.readStream(r0);	 Catch:{ Exception -> 0x00ec }
            r1.<init>(r0);	 Catch:{ Exception -> 0x00ec }
            r0 = 0;
        L_0x00a3:
            r2 = r1.length();	 Catch:{ Exception -> 0x00ec }
            if (r0 >= r2) goto L_0x00e4;
        L_0x00a9:
            r2 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ Exception -> 0x00ec }
            r2 = r2.pilotId;	 Catch:{ Exception -> 0x00ec }
            r3 = -1;
            if (r2 != r3) goto L_0x00e4;
        L_0x00b2:
            r2 = r1.getJSONObject(r0);	 Catch:{ Exception -> 0x00ec }
            r3 = "user__username";
            r2 = r2.getString(r3);	 Catch:{ Exception -> 0x00ec }
            r3 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ Exception -> 0x00ec }
            r3 = r3.flight;	 Catch:{ Exception -> 0x00ec }
            r3 = r3.getUser();	 Catch:{ Exception -> 0x00ec }
            r3 = r3.getUsername();	 Catch:{ Exception -> 0x00ec }
            r2 = r2.equals(r3);	 Catch:{ Exception -> 0x00ec }
            if (r2 == 0) goto L_0x00e1;
        L_0x00ce:
            r2 = com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.this;	 Catch:{ Exception -> 0x00ec }
            r3 = r1.getJSONObject(r0);	 Catch:{ Exception -> 0x00ec }
            r4 = "id";
            r3 = r3.getString(r4);	 Catch:{ Exception -> 0x00ec }
            r3 = java.lang.Integer.parseInt(r3);	 Catch:{ Exception -> 0x00ec }
            r2.pilotId = r3;	 Catch:{ Exception -> 0x00ec }
        L_0x00e1:
            r0 = r0 + 1;
            goto L_0x00a3;
        L_0x00e4:
            r0 = "responseCode";
            r1 = "200";
            android.util.Log.w(r0, r1);	 Catch:{ Exception -> 0x00ec }
            goto L_0x0093;
        L_0x00ec:
            r0 = move-exception;
            r1 = "connection";
            r2 = "connection fail";
            android.util.Log.e(r1, r2);
            r0.printStackTrace();
            goto L_0x0093;
        L_0x00f8:
            r0 = "responseCode";
            r1 = "401";
            android.util.Log.w(r0, r1);	 Catch:{ Exception -> 0x00ec }
            goto L_0x0093;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity.GetProfileIdWithUserNameAsyncTask.doInBackground(java.lang.Void[]):java.lang.Void");
        }

        protected void onPostExecute(Void voidR) {
            AcademyFlightDetailsActivity.this.enableEdition();
            AcademyFlightDetailsActivity.this.btnProfil.setEnabled(AcademyFlightDetailsActivity.this.pilotId != -1);
        }
    }

    private final class UpdateFlightTask extends AsyncTask<Map<String, Object>, Integer, Integer> {
        public static final String KEY_GRADE = "grade";
        public static final String KEY_VISIBLE = "visible";
        private final String TAG;

        private UpdateFlightTask() {
            this.TAG = UpdateFlightTask.class.getSimpleName();
        }

        protected Integer doInBackground(Map<String, Object>... mapArr) {
            HttpClient defaultHttpClient = new DefaultHttpClient();
            String str = AcademyFlightDetailsActivity.this.getString(C0984R.string.http).concat(AcademyFlightDetailsActivity.this.getString(C0984R.string.url_server)).concat(AcademyFlightDetailsActivity.this.getString(C0984R.string.url_flights)) + AcademyFlightDetailsActivity.this.flight.getId() + URIUtil.SLASH;
            if (mapArr[0].isEmpty()) {
                return null;
            }
            List arrayList = new ArrayList();
            if (mapArr[0].containsKey(KEY_GRADE)) {
                arrayList.add(new BasicNameValuePair(KEY_GRADE, ((Integer) mapArr[0].get(KEY_GRADE)).toString()));
            }
            if (mapArr[0].containsKey(KEY_VISIBLE)) {
                arrayList.add(new BasicNameValuePair(KEY_VISIBLE, ((Boolean) mapArr[0].get(KEY_VISIBLE)).booleanValue() ? "1" : "0"));
            }
            String str2 = str + "?" + URLEncodedUtils.format(arrayList, "UTF-8");
            Log.d(this.TAG, "Rate Flight URL: " + str2);
            HttpUriRequest httpPut = new HttpPut(str2);
            User user = AcademyFlightDetailsActivity.this.flight.getUser();
            if (!(user == null || AcademyUtils.login == null || AcademyUtils.password == null || !user.getUsername().equals(AcademyUtils.login))) {
                httpPut.setHeader("Authorization", "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2)));
            }
            try {
                httpPut.setEntity(new UrlEncodedFormEntity(arrayList));
                final int statusCode = defaultHttpClient.execute(httpPut).getStatusLine().getStatusCode();
                switch (statusCode) {
                    case 200:
                        Log.i(this.TAG, "Response code: " + statusCode);
                        break;
                    default:
                        Log.w(this.TAG, "Response code: " + statusCode);
                        break;
                }
                if (statusCode >= 400 && statusCode < 500) {
                    AcademyFlightDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AcademyFlightDetailsActivity.this, "Client error. Code " + statusCode, 0).show();
                        }
                    });
                } else if (statusCode >= 500 && statusCode < 600) {
                    AcademyFlightDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AcademyFlightDetailsActivity.this, "Server error. Code " + statusCode, 0).show();
                        }
                    });
                }
                return Integer.valueOf(statusCode);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Integer num) {
            AcademyFlightDetailsActivity.this.finish();
        }

        protected void onPreExecute() {
            AcademyFlightDetailsActivity.this.findViewById(C0984R.id.mainLayout).setVisibility(8);
            AcademyFlightDetailsActivity.this.findViewById(C0984R.id.progressBarLayout).setVisibility(0);
            AcademyFlightDetailsActivity.this.disableAllControls();
        }
    }

    private void disableAllControls() {
        this.btnMedia.setEnabled(false);
        this.btnGraphic.setEnabled(false);
        this.btnPosition.setEnabled(false);
        this.btnProfil.setEnabled(false);
        this.isFinishing = true;
        this.btnProfil.setEnabled(false);
        this.actionBar.hideBackButton();
    }

    private void enableDisableControls() {
        boolean z = true;
        this.btnMedia.setEnabled(this.mediaCount > 0);
        this.btnGraphic.setEnabled(this.flight.getFlightTime() > 0);
        View view = this.btnPosition;
        boolean z2 = (this.flight.getGps_latitude() == INVALID_COORDINATES || this.flight.getGps_longitude() == INVALID_COORDINATES) ? false : true;
        view.setEnabled(z2);
        View view2 = this.btnProfil;
        if (this.pilotId == -1) {
            z = false;
        }
        view2.setEnabled(z);
    }

    private void enableEdition() {
        if (this.pilotId == AcademyUtils.profile.getId()) {
            this.ratingBar.setEnabled(true);
            findViewById(C0984R.id.rating_bar_label).setVisibility(0);
            ((RadioButton) findViewById(C0984R.id.activity_academy_flight_details_radiobutton_private)).setEnabled(true);
            ((RadioButton) findViewById(C0984R.id.activity_academy_flight_details_radiobutton_public)).setEnabled(true);
            this.mediaCount = this.mediaGallery.countOfMedia(this.flight.getFlightTag());
        } else {
            this.mediaCount = this.flight.getFlightVideoSet().size() + this.flight.getFlightCaptureSet().size();
        }
        enableDisableControls();
    }

    private void initActionBar() {
        this.actionBar = getParrotActionBar();
        this.actionBar.initBackButton(new C09911());
        this.actionBar.initUserProfileButton(this.flight.getUser().getUsername(), this);
    }

    private void initListeners() {
        this.btnMedia.setOnClickListener(this);
        this.btnGraphic.setOnClickListener(this);
        this.btnPosition.setOnClickListener(this);
        this.ratingBar.setOnRatingBarChangeListener(this);
    }

    private void initUi() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(' ');
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        setTitle(String.format(getString(C0984R.string.aa_ID000026), new Object[]{decimalFormat.format((long) this.flight.getId())}));
        ((TextView) findViewById(C0984R.id.activity_academy_flight_details_label_datetime)).setText(DateFormat.getDateTimeInstance().format(this.flight.getDateTimeAsDate()));
        this.ratingBar = (RatingBar) findViewById(C0984R.id.activity_academy_flight_details_ratingbar_grade);
        this.ratingBar.setRating((float) this.flight.getGrade());
        this.ratingBar.setEnabled(false);
        findViewById(C0984R.id.rating_bar_label).setVisibility(8);
        ((TextView) findViewById(C0984R.id.activity_academy_flight_details_label_crashes)).setText(Integer.toString(this.flight.getCrash()));
        ((TextView) findViewById(C0984R.id.activity_academy_flight_details_label_flightduration)).setText(AcademyFormatUtils.timeFromSeconds(this.flight.getTotalFlightTime()));
        ((TextView) findViewById(C0984R.id.activity_academy_flight_details_label_steeringdevice)).setText(this.flight.getPhone_model());
        TextView textView = (TextView) findViewById(C0984R.id.activity_academy_flight_details_label_flightposition);
        if (this.flight.getGps_latitude() == INVALID_COORDINATES || this.flight.getGps_longitude() == INVALID_COORDINATES) {
            textView.setText(getString(C0984R.string.aa_ID000011));
        } else {
            textView.setText(this.flight.getGps_latitude() + ", " + this.flight.getGps_longitude());
        }
        this.radioGroupVisibility = (RadioGroup) findViewById(C0984R.id.activity_academy_flight_details_radiogroup_visibility);
        this.radioGroupVisibility.setOnCheckedChangeListener(this);
        if (this.flight.isVisible()) {
            ((RadioButton) findViewById(C0984R.id.activity_academy_flight_details_radiobutton_public)).setChecked(true);
        } else {
            ((RadioButton) findViewById(C0984R.id.activity_academy_flight_details_radiobutton_private)).setChecked(true);
        }
        ((RadioButton) findViewById(C0984R.id.activity_academy_flight_details_radiobutton_private)).setEnabled(false);
        ((RadioButton) findViewById(C0984R.id.activity_academy_flight_details_radiobutton_public)).setEnabled(false);
        this.btnMedia = findViewById(C0984R.id.activity_academy_flight_details_button_media);
        this.btnGraphic = findViewById(C0984R.id.activity_academy_flight_details_button_graphic);
        this.btnPosition = findViewById(C0984R.id.activity_academy_flight_details_button_position);
        this.btnProfil = findViewById(C0984R.id.action_bar_button_profile);
        if (this.flight.isGpsAvailable()) {
            ((TextView) this.btnPosition.findViewById(C0984R.id.textView10)).setText(C0984R.string.aa_ID000192);
            ((ImageView) this.btnPosition.findViewById(C0984R.id.imageView3)).setImageResource(C0984R.drawable.gps_icon);
        }
    }

    private void onGraphicButtonClicked(View view) {
        AcademyGraphActivity.start(this, this.flight);
    }

    private void onMediaButtonClicked(View view) {
        if (this.mediaCount == 1) {
            GalleryActivity.start(this, 0, MediaType.ALL, this.flight);
        } else if (this.mediaCount > 1) {
            MediaActivity.start(this, this.flight);
        }
    }

    private void onPositionButtonClicked(View view) {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isGooglePlayServicesAvailable == 0) {
            if (this.flight.isGpsAvailable()) {
                Track3DViewerActivity.startActivity(this, this.flight.getId(), this.flight.getDateTimeAsDate().getTime(), this.flight);
            } else {
                AcademyMapActivity.start(this, new LatLng(this.flight.getGps_latitude(), this.flight.getGps_longitude()));
            }
        } else if (isGooglePlayServicesAvailable == 2) {
            GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, this, 102).show();
        } else if (isGooglePlayServicesAvailable == 1) {
            GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, this, 102).show();
        }
    }

    private void onProfileButtonClicked(View view) {
        if (this.pilotId != -1) {
            AcademyProfileActivity.start(this, this.pilotId);
        }
    }

    private void resolveReadableAddress(Flight flight) {
        this.getAddressTask = new GetAddressFromLatLonTask();
        this.getAddressTask.execute(new Long[0]);
    }

    public static void start(Context context, Flight flight) {
        Intent intent = new Intent(context, AcademyFlightDetailsActivity.class);
        intent.putExtra(EXTRA_FLIGHT_OBJ, flight);
        context.startActivity(intent);
    }

    public static void startForResult(Activity activity, int i, Flight flight) {
        Intent intent = new Intent(activity, AcademyFlightDetailsActivity.class);
        intent.putExtra(EXTRA_FLIGHT_OBJ, flight);
        activity.startActivityForResult(intent, i);
    }

    @SuppressLint({"NewApi"})
    private void updateInformationsAndFinish() {
        UpdateFlightTask updateFlightTask = new UpdateFlightTask();
        Map hashMap = new HashMap();
        hashMap.put(UpdateFlightTask.KEY_GRADE, Integer.valueOf(this.rating));
        hashMap.put(UpdateFlightTask.KEY_VISIBLE, Boolean.valueOf(this.visible));
        if (this.rating == this.ratingOld && this.visible == this.visibleOld) {
            finish();
        } else if (VERSION.SDK_INT >= 11) {
            updateFlightTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Map[]{hashMap});
        } else {
            updateFlightTask.execute(new Map[]{hashMap});
        }
    }

    public void finish() {
        super.finish();
    }

    public void onBackPressed() {
        if (!this.isFinishing) {
            updateInformationsAndFinish();
        }
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case C0984R.id.activity_academy_flight_details_radiobutton_private /*2131361975*/:
                this.visible = false;
                return;
            case C0984R.id.activity_academy_flight_details_radiobutton_public /*2131361976*/:
                this.visible = true;
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.activity_academy_flight_details_button_media /*2131361978*/:
                onMediaButtonClicked(view);
                return;
            case C0984R.id.activity_academy_flight_details_button_graphic /*2131361981*/:
                onGraphicButtonClicked(view);
                return;
            case C0984R.id.activity_academy_flight_details_button_position /*2131361984*/:
                onPositionButtonClicked(view);
                return;
            case C0984R.id.action_bar_button_profile /*2131362328*/:
                onProfileButtonClicked(view);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_academy_flight_details);
        this.mediaGallery = new ARDroneMediaGallery(this);
        this.flight = (Flight) getIntent().getSerializableExtra(EXTRA_FLIGHT_OBJ);
        this.visible = this.flight.isVisible();
        this.visibleOld = this.visible;
        this.rating = this.flight.getGrade();
        this.ratingOld = this.rating;
        initActionBar();
        initUi();
        initListeners();
        enableDisableControls();
        resolveReadableAddress(this.flight);
        new GetProfileIdWithUserNameAsyncTask().execute(new Void[0]);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.getAddressTask != null) {
            this.getAddressTask.cancel(true);
        }
    }

    protected void onPause() {
        super.onPause();
    }

    public void onRatingChanged(RatingBar ratingBar, float f, boolean z) {
        if (z) {
            this.rating = (int) f;
            this.flight.setGrade(this.rating);
        }
    }

    protected void onResume() {
        super.onResume();
    }
}
