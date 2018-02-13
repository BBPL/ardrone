package com.parrot.freeflight.academy.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Cluster;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.model.FlightsCluster;
import com.parrot.freeflight.academy.model.HotspotsCluster;
import com.parrot.freeflight.academy.model.Media;
import com.parrot.freeflight.academy.model.MediaCluster;
import com.parrot.freeflight.academy.ui.AnimatedView;
import com.parrot.freeflight.academy.ui.DropDownMenu;
import com.parrot.freeflight.academy.ui.FlightsClusterListDialog;
import com.parrot.freeflight.academy.ui.HotspotsClusterListDialog;
import com.parrot.freeflight.academy.ui.MyCheckedTextView;
import com.parrot.freeflight.academy.ui.MyRadioGroup;
import com.parrot.freeflight.academy.utils.AcademyLocationUtils;
import com.parrot.freeflight.academy.utils.AcademyLocationUtils.ARSexaCoordinate;
import com.parrot.freeflight.academy.utils.AcademySharedPreferences;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.academy.utils.CustomMapFragment;
import com.parrot.freeflight.academy.utils.GeocoderAlt;
import com.parrot.freeflight.academy.utils.JSONParser;
import com.parrot.freeflight.activities.WatchPhotoActivity;
import com.parrot.freeflight.activities.WatchVideoActivity;
import com.parrot.freeflight.activities.WatchYoutubeVideoLegacyActivity;
import com.parrot.freeflight.media.LocalMediaProvider;
import com.parrot.freeflight.media.MediaProvider;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.media.MediaProvider.ThumbSize;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.ui.ActionBar.Background;
import com.parrot.freeflight.utils.ARDroneMediaGallery;
import com.parrot.freeflight.utils.FontUtils;
import com.parrot.freeflight.utils.GPSHelper;
import com.parrot.freeflight.utils.SimpleDelegate;
import com.parrot.freeflight.utils.SystemUtils;
import com.parrot.freeflight.vo.MediaVO;
import com.parrot.freeflight.vo.YouTubeMediaVO;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.mortbay.jetty.HttpVersions;

public class AcademyMapActivity extends FragmentActivity implements OnClickListener, OnEditorActionListener, OnCameraChangeListener, OnMarkerClickListener, LocationListener {
    private static final String ALL = "all";
    @SuppressLint({"SimpleDateFormat"})
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final String EXTRA_POSITION_OBJ_LAT = "position_obj_lat";
    private static final String EXTRA_POSITION_OBJ_LNG = "position_obj_lng";
    private static final String GPS = "gps";
    private static final int MAX_ZOOM_RANK = 16;
    private static final String MEDIAS = "medias";
    private static final String PHOTOS = "photos";
    private static final String TAG = AcademyMapActivity.class.getSimpleName();
    private static final long TIMEOUT_REFRESH = 1000;
    private static final String VIDEOS = "videos";
    private ActionBar actionBar;
    private CheckedTextView add_hotspot;
    private Address address;
    private Button addspot_address;
    private TextView addspot_address_text;
    private Button addspot_cancel;
    private Button addspot_confirm;
    private LinearLayout addspot_confirm_cancel;
    private AnimatedView addspot_details;
    private Button addspot_drop_pin;
    private Button addspot_gps_position;
    private AlertDialog alertDialog;
    private MyCheckedTextView all;
    private ArrayList<Rect> borders = new ArrayList();
    private LatLngBounds bounds;
    private Button cancel;
    private Bitmap cluster_bitmap;
    private ADDSPOT_MODE currentAddSpotMode;
    private MAP_MODE currentMapMode;
    private MEDIA_MODE currentMediaMode;
    private SEARCH_MODE currentSearchMode;
    private THUMBNAILS currentThumbnailsMode;
    private VIEW_MODE currentViewMode;
    private FlightsClusterListDialog dialogFlights;
    private ArrayList<DownloadImageAsyncTask> downloadImageAsyncTask_list = new ArrayList();
    private Runnable executeRequest = new C10145();
    private boolean firstOnResume = true;
    private MyCheckedTextView flights;
    private HashMap<String, FlightsCluster> flightsClusters;
    private MyRadioGroup flights_group;
    private Geocoder geocoder;
    private GetClustersAsyncTask getClustersTask;
    private MyCheckedTextView gps;
    private Bitmap gps_bitmap;
    private Handler handler = new Handler();
    private int height;
    private LatLng hotspotLocation;
    private MyCheckedTextView hotspots;
    private ArrayList<HotspotsCluster> hotspotsClusters;
    private MyCheckedTextView hybrid;
    private Timer lastChange;
    private LatLng lastPosition;
    private Location location;
    private GoogleMap map;
    private CustomMapFragment mapFragment;
    private final OnClickListener mapPlusBtnClickListener = new C10112();
    private MyRadioGroup map_group;
    private final List<Marker> markers = new ArrayList();
    private HashMap<LatLng, Pair<Marker, MarkerOptions>> markers_list = new HashMap();
    private ARDroneMediaGallery mediaGallery;
    private Bitmap media_bitmap;
    private MyRadioGroup media_group;
    private DropDownMenu menu;
    private SparseArray<HashMap<String, FlightsCluster>> myFlightsClusters;
    private Marker myPositionMarker = null;
    private MyCheckedTextView my_flights;
    private LatLng northEast;
    private final SimpleDelegate onFlightsDialogClosed = new C10101();
    private MyCheckedTextView photos;
    private Bitmap photos_bitmap;
    private SparseArray<HashMap<String, FlightsCluster>> pilotFlightsClusters;
    private List<Flight> pilot_flights;
    private LatLng position;
    private ProgressDialog progress;
    private MyCheckedTextView satellite;
    private CheckedTextView search;
    private Button search_address;
    private LinearLayout search_bar;
    private AnimatedView search_details;
    private EditText search_field;
    private Button search_pilot;
    private OnClickListener settingsChangedListener = new C10123();
    private LatLng southWest;
    private MyCheckedTextView standard;
    private ImageView target;
    private MyCheckedTextView thumbnails;
    private HashMap<LatLng, Bitmap> thumbnails_list = new HashMap();
    private List<Flight> user_flights;
    private String username;
    private MyCheckedTextView videos;
    private Bitmap videos_bitmap;
    private int width;
    private float zoom;
    private TextView zoom_level_text;

    class C10101 implements SimpleDelegate {
        C10101() {
        }

        public void call() {
            AcademyMapActivity.this.dialogFlights = null;
        }
    }

    class C10112 implements OnClickListener {
        C10112() {
        }

        public void onClick(View view) {
            AcademyMapActivity.this.showMapSettings();
        }
    }

    class C10123 implements OnClickListener {
        C10123() {
        }

        public void onClick(View view) {
            if (view.getTag() instanceof VIEW_MODE) {
                AcademyMapActivity.this.onViewModeClicked(view);
            } else if (view.getTag() instanceof MEDIA_MODE) {
                AcademyMapActivity.this.onMediaModeClicked(view);
            } else if (view.getTag() instanceof MAP_MODE) {
                AcademyMapActivity.this.onMapModeClicked(view);
            } else {
                AcademyMapActivity.this.onThumbnailsClicked(view);
            }
        }
    }

    class C10145 implements Runnable {
        C10145() {
        }

        public void run() {
            if (!(AcademyMapActivity.this.getClustersTask == null || AcademyMapActivity.this.getClustersTask.getStatus() == Status.RUNNING)) {
                AcademyMapActivity.this.getClustersTask.cancel(true);
            }
            AcademyMapActivity.this.getClustersTask = new GetClustersAsyncTask();
            AcademyMapActivity.this.getClustersTask.execute(new Void[0]);
        }
    }

    private enum ADDSPOT_MODE {
        GPS_POSITION,
        DROP_PIN,
        ADDRESS
    }

    private class DownloadImageAsyncTask extends AsyncTask<FlightsCluster, Void, Void> {
        private Bitmap bitmap;
        private LatLng point;

        public DownloadImageAsyncTask(LatLng latLng) {
            this.point = latLng;
            AcademyMapActivity.this.thumbnails_list.put(latLng, null);
        }

        protected Void doInBackground(FlightsCluster... flightsClusterArr) {
            InputStream content;
            Throwable th;
            if (AcademyMapActivity.this.currentViewMode == VIEW_MODE.MY_FLIGHTS) {
                System.out.println(AcademyMapActivity.this.getThumbUrl(flightsClusterArr[0]));
                if (flightsClusterArr[0].getFlights(AcademyMapActivity.this.currentMediaMode) != null) {
                    MediaType mediaType;
                    MediaProvider localMediaProvider = new LocalMediaProvider(AcademyMapActivity.this, (Flight) flightsClusterArr[0].getFlights(AcademyMapActivity.this.currentMediaMode).get(0));
                    switch (AcademyMapActivity.this.currentMediaMode) {
                        case ALL:
                            mediaType = MediaType.ALL;
                            break;
                        case PHOTOS:
                            mediaType = MediaType.PHOTOS;
                            break;
                        case VIDEOS:
                            mediaType = MediaType.VIDEOS;
                            break;
                        default:
                            mediaType = null;
                            break;
                    }
                    try {
                        this.bitmap = localMediaProvider.getThumbnail((MediaVO) localMediaProvider.getMediaList(mediaType).get(0), ThumbSize.MICRO).getBitmap();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                String access$2800 = AcademyMapActivity.this.getThumbUrl(flightsClusterArr[0]);
                AndroidHttpClient newInstance = AndroidHttpClient.newInstance("Android");
                Object httpGet = new HttpGet(access$2800);
                httpGet.addHeader("Authorization", "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2)));
                try {
                    HttpResponse execute = newInstance.execute(httpGet);
                    int statusCode = execute.getStatusLine().getStatusCode();
                    if (statusCode != 200) {
                        Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + access$2800);
                        if (newInstance != null) {
                            newInstance.close();
                        }
                    } else {
                        HttpEntity entity = execute.getEntity();
                        if (entity != null) {
                            try {
                                content = entity.getContent();
                                try {
                                    Options options = new Options();
                                    options.inSampleSize = 4;
                                    options.inPurgeable = true;
                                    this.bitmap = BitmapFactory.decodeStream(content, null, options);
                                    if (content != null) {
                                        content.close();
                                    }
                                    entity.consumeContent();
                                } catch (Throwable th2) {
                                    th = th2;
                                    if (content != null) {
                                        content.close();
                                    }
                                    entity.consumeContent();
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                content = null;
                                if (content != null) {
                                    content.close();
                                }
                                entity.consumeContent();
                                throw th;
                            }
                        }
                        if (newInstance != null) {
                            newInstance.close();
                        }
                    }
                } catch (Exception e2) {
                    httpGet.abort();
                    Log.w("ImageDownloader", "Error while retrieving bitmap from " + access$2800);
                    e2.printStackTrace();
                    if (newInstance != null) {
                        newInstance.close();
                    }
                } catch (Throwable th4) {
                    if (newInstance != null) {
                        newInstance.close();
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            AcademyMapActivity.this.thumbnails_list.put(this.point, this.bitmap);
            AcademyMapActivity.this.drawThumbnails();
        }
    }

    private class GetClustersAsyncTask extends AsyncTask<Void, Void, Void> {
        private GetClustersAsyncTask() {
        }

        protected Void doInBackground(Void... voidArr) {
            String string;
            MalformedURLException e;
            IOException e2;
            JSONException e3;
            StringBuilder stringBuilder = new StringBuilder();
            switch (AcademyMapActivity.this.currentViewMode) {
                case ALL_FLIGHTS:
                    string = AcademyMapActivity.this.getString(C0984R.string.url_flights_clusters);
                    break;
                case MY_FLIGHTS:
                    string = AcademyMapActivity.this.getString(C0984R.string.url_flights);
                    break;
                case HOTSPOTS:
                    string = AcademyMapActivity.this.getString(C0984R.string.url_hotspots_clusters);
                    break;
                default:
                    string = AcademyMapActivity.this.getString(C0984R.string.url_flights_clusters);
                    break;
            }
            HttpURLConnection httpURLConnection;
            StringBuilder stringBuilder2;
            URL url;
            JSONArray jSONArray;
            JSONArray jSONArray2;
            if (AcademyMapActivity.this.currentViewMode == VIEW_MODE.MY_FLIGHTS) {
                try {
                    URL url2 = new URL(stringBuilder.append(AcademyMapActivity.this.getString(C0984R.string.http)).append(AcademyMapActivity.this.getString(C0984R.string.url_server)).append(string).toString());
                    String str = "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2));
                    Log.i("URL : 0", url2.toString());
                    if (!isCancelled()) {
                        httpURLConnection = (HttpURLConnection) url2.openConnection();
                        httpURLConnection.setRequestProperty("Authorization", str);
                        httpURLConnection.connect();
                        AcademyMapActivity.this.user_flights = Flight.jsonArrToFlights(new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream())));
                        if (!isCancelled()) {
                            AcademyMapActivity.this.flightsToClusters(AcademyMapActivity.this.user_flights, AcademyMapActivity.this.myFlightsClusters);
                            string = AcademyMapActivity.this.getString(C0984R.string.url_flights_clusters);
                            stringBuilder2 = new StringBuilder();
                            if (AcademyMapActivity.this.username != null) {
                                Log.i("TODAY", AcademyMapActivity.DATE_FORMAT.format(new Date()));
                                string = AcademyMapActivity.this.getString(C0984R.string.url_flights).concat(String.format("?start_date=19700101&end_date=%s&user=%s", new Object[]{string, AcademyMapActivity.this.username}));
                                try {
                                    url = new URL(stringBuilder2.append(AcademyMapActivity.this.getString(C0984R.string.http)).append(AcademyMapActivity.this.getString(C0984R.string.url_server)).append(string).toString());
                                    Log.i("URL : 1", url.toString());
                                    httpURLConnection = (HttpURLConnection) url.openConnection();
                                    if (!isCancelled()) {
                                        httpURLConnection.connect();
                                        jSONArray = new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream()));
                                        Log.d("Array", jSONArray.toString());
                                        AcademyMapActivity.this.pilot_flights = Flight.jsonArrToFlights(jSONArray);
                                        if (!isCancelled()) {
                                            AcademyMapActivity.this.flightsToClusters(AcademyMapActivity.this.pilot_flights, AcademyMapActivity.this.pilotFlightsClusters);
                                            Log.d(AcademyMapActivity.TAG, "Received " + AcademyMapActivity.this.pilotFlightsClusters.size() + " clusters");
                                            string = AcademyMapActivity.this.getString(C0984R.string.url_flights_clusters);
                                            stringBuilder2 = new StringBuilder();
                                        }
                                    }
                                } catch (MalformedURLException e4) {
                                    e4.printStackTrace();
                                } catch (IOException e22) {
                                    e22.printStackTrace();
                                } catch (JSONException e32) {
                                    e32.printStackTrace();
                                }
                            }
                            if (AcademyMapActivity.this.southWest.longitude >= AcademyMapActivity.this.northEast.longitude) {
                                Log.d("Clusters", "borders OK");
                                try {
                                    url = new URL(stringBuilder2.append(AcademyMapActivity.this.getString(C0984R.string.http)).append(AcademyMapActivity.this.getString(C0984R.string.url_server)).append(String.format(string, new Object[]{Float.valueOf((float) AcademyMapActivity.this.northEast.latitude), Float.valueOf((float) AcademyMapActivity.this.southWest.longitude), Float.valueOf((float) AcademyMapActivity.this.southWest.latitude), Float.valueOf((float) AcademyMapActivity.this.northEast.longitude), Integer.valueOf(AcademyMapActivity.this.getZoomRank())})).toString().replace(',', '.'));
                                    Log.i("URL : ", url.toString());
                                    httpURLConnection = (HttpURLConnection) url.openConnection();
                                    if (!isCancelled()) {
                                        httpURLConnection.connect();
                                        jSONArray2 = new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream()));
                                        if (AcademyMapActivity.this.currentViewMode != VIEW_MODE.HOTSPOTS) {
                                            AcademyMapActivity.this.hotspotsClusters = HotspotsCluster.jsonToHotspotsClusters(jSONArray2);
                                        } else {
                                            AcademyMapActivity.this.flightsClusters = FlightsCluster.jsonToFlightsClusters(jSONArray2);
                                        }
                                    }
                                } catch (MalformedURLException e42) {
                                    e42.printStackTrace();
                                } catch (IOException e222) {
                                    e222.printStackTrace();
                                } catch (JSONException e322) {
                                    e322.printStackTrace();
                                }
                            } else {
                                Log.d("Clusters", "borders mixed");
                                try {
                                    url = new URL(stringBuilder2.append(AcademyMapActivity.this.getString(C0984R.string.http)).append(AcademyMapActivity.this.getString(C0984R.string.url_server)).append(String.format(string, new Object[]{Float.valueOf((float) AcademyMapActivity.this.northEast.latitude), Float.valueOf((float) AcademyMapActivity.this.southWest.longitude), Float.valueOf((float) AcademyMapActivity.this.southWest.latitude), Double.valueOf(179.999999d), Integer.valueOf(AcademyMapActivity.this.getZoomRank())})).toString().replace(',', '.'));
                                    Log.i("URL 2: ", url.toString());
                                    httpURLConnection = (HttpURLConnection) url.openConnection();
                                    if (!isCancelled()) {
                                        httpURLConnection.connect();
                                        jSONArray = new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream()));
                                        if (AcademyMapActivity.this.currentViewMode != VIEW_MODE.HOTSPOTS) {
                                            AcademyMapActivity.this.hotspotsClusters = HotspotsCluster.jsonToHotspotsClusters(jSONArray);
                                        } else {
                                            AcademyMapActivity.this.flightsClusters = FlightsCluster.jsonToFlightsClusters(jSONArray);
                                        }
                                        try {
                                            url = new URL((AcademyMapActivity.this.getString(C0984R.string.http) + AcademyMapActivity.this.getString(C0984R.string.url_server) + String.format(string, new Object[]{Float.valueOf((float) AcademyMapActivity.this.northEast.latitude), Double.valueOf(-179.999999d), Float.valueOf((float) AcademyMapActivity.this.southWest.latitude), Float.valueOf((float) AcademyMapActivity.this.northEast.longitude), Integer.valueOf(AcademyMapActivity.this.getZoomRank())})).replace(',', '.'));
                                            Log.i("URL 3: ", url.toString());
                                            httpURLConnection = (HttpURLConnection) url.openConnection();
                                            if (!isCancelled()) {
                                                httpURLConnection.connect();
                                                jSONArray2 = new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream()));
                                                if (AcademyMapActivity.this.currentViewMode != VIEW_MODE.HOTSPOTS) {
                                                    AcademyMapActivity.this.hotspotsClusters.addAll(HotspotsCluster.jsonToHotspotsClusters(jSONArray2));
                                                } else {
                                                    AcademyMapActivity.this.flightsClusters.putAll(FlightsCluster.jsonToFlightsClusters(jSONArray2));
                                                }
                                            }
                                        } catch (MalformedURLException e5) {
                                            e42 = e5;
                                            e42.printStackTrace();
                                            return null;
                                        } catch (IOException e6) {
                                            e222 = e6;
                                            e222.printStackTrace();
                                            AcademyMapActivity.this.showAlertDialog(AcademyMapActivity.this.getString(C0984R.string.aa_ID000099), AcademyMapActivity.this.getString(C0984R.string.aa_ID000082), null);
                                            return null;
                                        } catch (JSONException e7) {
                                            e322 = e7;
                                            e322.printStackTrace();
                                            return null;
                                        }
                                    }
                                } catch (MalformedURLException e8) {
                                    e42 = e8;
                                    e42.printStackTrace();
                                    return null;
                                } catch (IOException e9) {
                                    e222 = e9;
                                    e222.printStackTrace();
                                    AcademyMapActivity.this.showAlertDialog(AcademyMapActivity.this.getString(C0984R.string.aa_ID000099), AcademyMapActivity.this.getString(C0984R.string.aa_ID000082), null);
                                    return null;
                                } catch (JSONException e10) {
                                    e322 = e10;
                                    e322.printStackTrace();
                                    return null;
                                }
                            }
                        }
                    }
                } catch (MalformedURLException e422) {
                    e422.printStackTrace();
                } catch (IOException e2222) {
                    e2222.printStackTrace();
                } catch (JSONException e3222) {
                    e3222.printStackTrace();
                }
            } else {
                stringBuilder2 = stringBuilder;
                if (AcademyMapActivity.this.username != null) {
                    Log.i("TODAY", AcademyMapActivity.DATE_FORMAT.format(new Date()));
                    string = AcademyMapActivity.this.getString(C0984R.string.url_flights).concat(String.format("?start_date=19700101&end_date=%s&user=%s", new Object[]{string, AcademyMapActivity.this.username}));
                    url = new URL(stringBuilder2.append(AcademyMapActivity.this.getString(C0984R.string.http)).append(AcademyMapActivity.this.getString(C0984R.string.url_server)).append(string).toString());
                    Log.i("URL : 1", url.toString());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    if (isCancelled()) {
                        httpURLConnection.connect();
                        jSONArray = new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream()));
                        Log.d("Array", jSONArray.toString());
                        AcademyMapActivity.this.pilot_flights = Flight.jsonArrToFlights(jSONArray);
                        if (isCancelled()) {
                            AcademyMapActivity.this.flightsToClusters(AcademyMapActivity.this.pilot_flights, AcademyMapActivity.this.pilotFlightsClusters);
                            Log.d(AcademyMapActivity.TAG, "Received " + AcademyMapActivity.this.pilotFlightsClusters.size() + " clusters");
                            string = AcademyMapActivity.this.getString(C0984R.string.url_flights_clusters);
                            stringBuilder2 = new StringBuilder();
                        }
                    }
                }
                if (AcademyMapActivity.this.southWest.longitude >= AcademyMapActivity.this.northEast.longitude) {
                    Log.d("Clusters", "borders mixed");
                    url = new URL(stringBuilder2.append(AcademyMapActivity.this.getString(C0984R.string.http)).append(AcademyMapActivity.this.getString(C0984R.string.url_server)).append(String.format(string, new Object[]{Float.valueOf((float) AcademyMapActivity.this.northEast.latitude), Float.valueOf((float) AcademyMapActivity.this.southWest.longitude), Float.valueOf((float) AcademyMapActivity.this.southWest.latitude), Double.valueOf(179.999999d), Integer.valueOf(AcademyMapActivity.this.getZoomRank())})).toString().replace(',', '.'));
                    Log.i("URL 2: ", url.toString());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    if (isCancelled()) {
                        httpURLConnection.connect();
                        jSONArray = new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream()));
                        if (AcademyMapActivity.this.currentViewMode != VIEW_MODE.HOTSPOTS) {
                            AcademyMapActivity.this.flightsClusters = FlightsCluster.jsonToFlightsClusters(jSONArray);
                        } else {
                            AcademyMapActivity.this.hotspotsClusters = HotspotsCluster.jsonToHotspotsClusters(jSONArray);
                        }
                        url = new URL((AcademyMapActivity.this.getString(C0984R.string.http) + AcademyMapActivity.this.getString(C0984R.string.url_server) + String.format(string, new Object[]{Float.valueOf((float) AcademyMapActivity.this.northEast.latitude), Double.valueOf(-179.999999d), Float.valueOf((float) AcademyMapActivity.this.southWest.latitude), Float.valueOf((float) AcademyMapActivity.this.northEast.longitude), Integer.valueOf(AcademyMapActivity.this.getZoomRank())})).replace(',', '.'));
                        Log.i("URL 3: ", url.toString());
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        if (isCancelled()) {
                            httpURLConnection.connect();
                            jSONArray2 = new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream()));
                            if (AcademyMapActivity.this.currentViewMode != VIEW_MODE.HOTSPOTS) {
                                AcademyMapActivity.this.flightsClusters.putAll(FlightsCluster.jsonToFlightsClusters(jSONArray2));
                            } else {
                                AcademyMapActivity.this.hotspotsClusters.addAll(HotspotsCluster.jsonToHotspotsClusters(jSONArray2));
                            }
                        }
                    }
                } else {
                    Log.d("Clusters", "borders OK");
                    url = new URL(stringBuilder2.append(AcademyMapActivity.this.getString(C0984R.string.http)).append(AcademyMapActivity.this.getString(C0984R.string.url_server)).append(String.format(string, new Object[]{Float.valueOf((float) AcademyMapActivity.this.northEast.latitude), Float.valueOf((float) AcademyMapActivity.this.southWest.longitude), Float.valueOf((float) AcademyMapActivity.this.southWest.latitude), Float.valueOf((float) AcademyMapActivity.this.northEast.longitude), Integer.valueOf(AcademyMapActivity.this.getZoomRank())})).toString().replace(',', '.'));
                    Log.i("URL : ", url.toString());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    if (isCancelled()) {
                        httpURLConnection.connect();
                        jSONArray2 = new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream()));
                        if (AcademyMapActivity.this.currentViewMode != VIEW_MODE.HOTSPOTS) {
                            AcademyMapActivity.this.flightsClusters = FlightsCluster.jsonToFlightsClusters(jSONArray2);
                        } else {
                            AcademyMapActivity.this.hotspotsClusters = HotspotsCluster.jsonToHotspotsClusters(jSONArray2);
                        }
                    }
                }
            }
            return null;
        }

        protected String getMediaFilter() {
            switch (AcademyMapActivity.this.currentMediaMode) {
                case PHOTOS:
                    return "&photo";
                case VIDEOS:
                    return "&video";
                case GPS:
                    return "&gps";
                default:
                    return HttpVersions.HTTP_0_9;
            }
        }

        protected void onPostExecute(Void voidR) {
            if (AcademyMapActivity.this.currentViewMode == VIEW_MODE.MY_FLIGHTS) {
                AcademyMapActivity.this.updateUserFlightsClusters(AcademyMapActivity.this.myFlightsClusters);
            }
            if (AcademyMapActivity.this.username != null) {
                AcademyMapActivity.this.updateUserFlightsClusters(AcademyMapActivity.this.pilotFlightsClusters);
            }
            AcademyMapActivity.this.drawClusters();
            if (AcademyMapActivity.this.progress != null) {
                AcademyMapActivity.this.progress.dismiss();
            }
        }

        protected void onPreExecute() {
            if (AcademyMapActivity.this.progress != null) {
                AcademyMapActivity.this.progress.show();
                AcademyMapActivity.this.progress.setContentView(C0984R.layout.academy_progress_dialog);
            }
            AcademyMapActivity.this.hotspotsClusters.clear();
            AcademyMapActivity.this.flightsClusters.clear();
            AcademyMapActivity.this.killAllDownloadingTask();
            AcademyMapActivity.this.updateBorders();
        }
    }

    @SuppressLint({"NewApi"})
    private class GetLocationAsyncTask extends AsyncTask<String, Void, LatLng> {
        private boolean fromSearch;

        protected GetLocationAsyncTask(boolean z) {
            this.fromSearch = z;
        }

        protected LatLng doInBackground(String... strArr) {
            AcademyMapActivity.this.address = null;
            if (!Geocoder.isPresent()) {
                return null;
            }
            List fromLocationName;
            try {
                fromLocationName = AcademyMapActivity.this.geocoder.getFromLocationName(strArr[0], 1);
            } catch (IOException e) {
                try {
                    fromLocationName = GeocoderAlt.getFromLocationNameAlt(strArr[0]);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    fromLocationName = null;
                }
            }
            if (fromLocationName == null || fromLocationName.isEmpty()) {
                return null;
            }
            AcademyMapActivity.this.address = (Address) fromLocationName.get(0);
            return new LatLng(AcademyMapActivity.this.address.getLatitude(), AcademyMapActivity.this.address.getLongitude());
        }

        protected void onPostExecute(LatLng latLng) {
            if (latLng != null) {
                AcademyMapActivity.this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= AcademyMapActivity.this.address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(AcademyMapActivity.this.address.getAddressLine(i));
                    if (i < AcademyMapActivity.this.address.getMaxAddressLineIndex()) {
                        if (this.fromSearch) {
                            stringBuilder.append("\n");
                        } else {
                            stringBuilder.append(", ");
                        }
                    }
                }
                if (this.fromSearch) {
                    Toast.makeText(AcademyMapActivity.this.getBaseContext(), stringBuilder.toString(), 0).show();
                    AcademyMapActivity.this.search_bar.setVisibility(4);
                    return;
                }
                AcademyMapActivity.this.hotspotLocation = latLng;
                AcademyMapActivity.this.addspot_address_text.setText(stringBuilder.toString());
                AcademyMapActivity.this.addspot_address_text.setVisibility(0);
                AcademyMapActivity.this.showAddSpotConfirmCancel();
            }
        }
    }

    private class HotspotAddressDialog extends AlertDialog implements OnClickListener {
        private String address;
        private EditText address_edit;
        private Button cancel;
        private Button confirm;
        private Context context;

        private class myTextWatcher implements TextWatcher {
            int resId;

            protected myTextWatcher(int i) {
                this.resId = i;
            }

            public void afterTextChanged(Editable editable) {
                switch (this.resId) {
                    case C0984R.id.addspot_address_edit /*2131361811*/:
                        HotspotAddressDialog.this.address = editable.toString();
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

        protected HotspotAddressDialog(Context context) {
            super(context);
            this.context = context;
        }

        private void initListeners() {
            this.address_edit.addTextChangedListener(new myTextWatcher(C0984R.id.addspot_address_edit));
            this.confirm.setOnClickListener(this);
            this.cancel.setOnClickListener(this);
        }

        private void initUi() {
            this.address_edit = (EditText) findViewById(C0984R.id.addspot_address_edit);
            this.confirm = (Button) findViewById(C0984R.id.addspot_address_confirm);
            this.cancel = (Button) findViewById(C0984R.id.addspot_address_cancel);
        }

        public void dismiss() {
            AcademyMapActivity.this.hideAddSpotConfirmCancel();
            super.dismiss();
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case C0984R.id.addspot_address_confirm /*2131361812*/:
                    if (this.address != null && this.address != HttpVersions.HTTP_0_9) {
                        new GetLocationAsyncTask(false).execute(new String[]{this.address});
                        dismiss();
                        return;
                    }
                    return;
                case C0984R.id.addspot_address_cancel /*2131361813*/:
                    dismiss();
                    AcademyMapActivity.this.currentAddSpotMode = null;
                    return;
                default:
                    return;
            }
        }

        protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setContentView(C0984R.layout.academy_addspot_address_dialog);
            initUi();
            initListeners();
            onPostCreate(bundle);
        }

        protected void onPostCreate(Bundle bundle) {
            FontUtils.applyFont(this.context, (ViewGroup) findViewById(16908290));
            getWindow().clearFlags(131080);
        }
    }

    private enum MAP_MODE {
        STANDARD,
        SATELLITE,
        HYBRID
    }

    public enum MEDIA_MODE {
        ALL,
        PHOTOS,
        VIDEOS,
        GPS
    }

    private class MyTimerTask extends TimerTask {
        private CameraPosition cameraPosition;

        public MyTimerTask(CameraPosition cameraPosition) {
            this.cameraPosition = cameraPosition;
        }

        public void run() {
            if (!AcademyMapActivity.this.mapFragment.isMapTouched()) {
                if (!AcademyMapActivity.this.bounds.contains(this.cameraPosition.target) || ((int) this.cameraPosition.zoom) != ((int) AcademyMapActivity.this.zoom)) {
                    AcademyMapActivity.this.zoom = this.cameraPosition.zoom;
                    AcademyMapActivity.this.handler.post(AcademyMapActivity.this.executeRequest);
                }
            }
        }
    }

    private enum SEARCH_MODE {
        PILOT,
        ADDRESS
    }

    private enum THUMBNAILS {
        ON,
        OFF
    }

    public enum VIEW_MODE {
        ALL_FLIGHTS,
        MY_FLIGHTS,
        HOTSPOTS
    }

    private LatLng addFlightClusterMarker(String str, FlightsCluster flightsCluster) {
        boolean equals = str.equals(ALL);
        if (!flightsCluster.getMediaCluster().containsKey(str) && !equals) {
            return null;
        }
        String gps_latitude;
        String gps_longitude;
        int count;
        if (equals) {
            Cluster cluster = flightsCluster.getCluster();
            gps_latitude = cluster.getGps_latitude();
            gps_longitude = cluster.getGps_longitude();
            count = cluster.getCount();
        } else {
            MediaCluster mediaCluster = (MediaCluster) flightsCluster.getMediaCluster().get(str);
            gps_latitude = mediaCluster.getGps_latitude();
            String gps_longitude2 = mediaCluster.getGps_longitude();
            count = mediaCluster.getCount();
            gps_longitude = gps_longitude2;
        }
        LatLng latLng = new LatLng(Double.parseDouble(gps_latitude), Double.parseDouble(gps_longitude));
        Bitmap createMarker = createMarker(countToString(count), flightsCluster.getIndex());
        MarkerOptions icon = new MarkerOptions().position(latLng).anchor(0.4f, 0.6f).title(flightsCluster.getIndex()).icon(BitmapDescriptorFactory.fromBitmap(createMarker));
        Marker addMarker = this.map.addMarker(icon);
        this.markers.add(addMarker);
        createMarker.recycle();
        this.markers_list.put(latLng, new Pair(addMarker, icon));
        return latLng;
    }

    private void addHotspotClusterMarker(HotspotsCluster hotspotsCluster) {
        LatLng latLng = new LatLng(Double.parseDouble(hotspotsCluster.getCluster().getGps_latitude()), Double.parseDouble(hotspotsCluster.getCluster().getGps_longitude()));
        Bitmap createMarker = createMarker(countToString(hotspotsCluster.getCluster().getCount()), hotspotsCluster.getIndex());
        MarkerOptions icon = new MarkerOptions().position(latLng).anchor(0.4f, 0.6f).title(hotspotsCluster.getIndex()).icon(BitmapDescriptorFactory.fromBitmap(createMarker));
        Marker addMarker = this.map.addMarker(icon);
        this.markers.add(addMarker);
        createMarker.recycle();
        this.markers_list.put(latLng, new Pair(addMarker, icon));
    }

    private void changeMapMode() {
        switch (this.currentMapMode) {
            case STANDARD:
                this.map.setMapType(1);
                return;
            case SATELLITE:
                this.map.setMapType(2);
                return;
            case HYBRID:
                this.map.setMapType(4);
                return;
            default:
                return;
        }
    }

    private void changeMarker() {
        int i;
        int i2 = 0;
        switch (this.currentViewMode) {
            case ALL_FLIGHTS:
                i = C0984R.drawable.ff2_2_flights;
                break;
            case MY_FLIGHTS:
                i = C0984R.drawable.ff2_2_myflights;
                break;
            case HOTSPOTS:
                i = C0984R.drawable.ff2_2_hotspot;
                break;
            default:
                i = 0;
                break;
        }
        CheckedTextView checkedTextView = this.add_hotspot;
        if (this.currentViewMode != VIEW_MODE.HOTSPOTS) {
            i2 = 4;
        }
        checkedTextView.setVisibility(i2);
        this.cluster_bitmap = BitmapFactory.decodeResource(getResources(), i);
    }

    private void clearMap() {
        for (Marker remove : this.markers) {
            remove.remove();
        }
        this.map.clear();
        this.markers.clear();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Bitmap createMarker(java.lang.String r14, java.lang.String r15) {
        /*
        r13 = this;
        r12 = 1;
        r11 = 0;
        r6 = 4608083138725491507; // 0x3ff3333333333333 float:4.172325E-8 double:1.2;
        r10 = 0;
        r2 = 0;
        r0 = com.parrot.freeflight.academy.activities.AcademyMapActivity.C10156.f321x3c91b21a;
        r1 = r13.currentViewMode;
        r1 = r1.ordinal();
        r0 = r0[r1];
        switch(r0) {
            case 1: goto L_0x009a;
            case 2: goto L_0x00a2;
            default: goto L_0x0016;
        };
    L_0x0016:
        r1 = r2;
    L_0x0017:
        if (r1 == 0) goto L_0x00e6;
    L_0x0019:
        r0 = com.parrot.freeflight.academy.activities.AcademyMapActivity.C10156.f318xef50f5c1;
        r3 = r13.currentMediaMode;
        r3 = r3.ordinal();
        r0 = r0[r3];
        switch(r0) {
            case 1: goto L_0x00b4;
            case 2: goto L_0x00da;
            case 3: goto L_0x00de;
            case 4: goto L_0x00e2;
            default: goto L_0x0026;
        };
    L_0x0026:
        r0 = r2;
    L_0x0027:
        r1 = r13.cluster_bitmap;
        r1 = r1.getWidth();
        r4 = (double) r1;
        r4 = r4 * r6;
        r1 = (int) r4;
        r3 = r13.cluster_bitmap;
        r3 = r3.getHeight();
        r4 = (double) r3;
        r4 = r4 * r6;
        r3 = (int) r4;
        r4 = android.graphics.Bitmap.Config.ARGB_8888;
        r1 = android.graphics.Bitmap.createBitmap(r1, r3, r4);
        r3 = new android.graphics.Canvas;
        r3.<init>(r1);
        r4 = r13.cluster_bitmap;
        r5 = r13.cluster_bitmap;
        r5 = r5.getHeight();
        r6 = (double) r5;
        r8 = 4596373779694328218; // 0x3fc999999999999a float:-1.5881868E-23 double:0.2;
        r6 = r6 * r8;
        r5 = (float) r6;
        r3.drawBitmap(r4, r11, r5, r2);
        r4 = new android.graphics.Paint;
        r4.<init>();
        r5 = android.graphics.Paint.Align.CENTER;
        r4.setTextAlign(r5);
        r5 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r4.setARGB(r5, r10, r10, r10);
        r5 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r4.setTextSize(r5);
        r5 = com.parrot.freeflight.utils.FontUtils.TYPEFACE.Helvetica(r13);
        r4.setTypeface(r5);
        r4.setFakeBoldText(r12);
        r4.setAntiAlias(r12);
        r5 = r1.getWidth();
        r5 = (float) r5;
        r6 = 1054280253; // 0x3ed70a3d float:0.42 double:5.20883654E-315;
        r5 = r5 * r6;
        r6 = r1.getHeight();
        r6 = (float) r6;
        r7 = 1060320051; // 0x3f333333 float:0.7 double:5.23867711E-315;
        r6 = r6 * r7;
        r3.drawText(r14, r5, r6, r4);
        if (r0 == 0) goto L_0x0099;
    L_0x008f:
        r4 = r1.getWidth();
        r4 = r4 / 2;
        r4 = (float) r4;
        r3.drawBitmap(r0, r4, r11, r2);
    L_0x0099:
        return r1;
    L_0x009a:
        r0 = r13.flightsClusters;
        r1 = r0.get(r15);
        goto L_0x0017;
    L_0x00a2:
        r0 = r13.myFlightsClusters;
        r1 = r13.getZoomRank();
        r0 = r0.get(r1);
        r0 = (java.util.HashMap) r0;
        r1 = r0.get(r15);
        goto L_0x0017;
    L_0x00b4:
        r0 = r1;
        r0 = (com.parrot.freeflight.academy.model.FlightsCluster) r0;
        r0 = r0.hasMedias();
        if (r0 == 0) goto L_0x00c1;
    L_0x00bd:
        r0 = r13.media_bitmap;
        goto L_0x0027;
    L_0x00c1:
        r0 = r1;
        r0 = (com.parrot.freeflight.academy.model.FlightsCluster) r0;
        r0 = r0.hasVideos();
        if (r0 == 0) goto L_0x00ce;
    L_0x00ca:
        r0 = r13.videos_bitmap;
        goto L_0x0027;
    L_0x00ce:
        r1 = (com.parrot.freeflight.academy.model.FlightsCluster) r1;
        r0 = r1.hasPhotos();
        if (r0 == 0) goto L_0x00e6;
    L_0x00d6:
        r0 = r13.photos_bitmap;
        goto L_0x0027;
    L_0x00da:
        r0 = r13.photos_bitmap;
        goto L_0x0027;
    L_0x00de:
        r0 = r13.videos_bitmap;
        goto L_0x0027;
    L_0x00e2:
        r0 = r13.gps_bitmap;
        goto L_0x0027;
    L_0x00e6:
        r0 = r2;
        goto L_0x0027;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.academy.activities.AcademyMapActivity.createMarker(java.lang.String, java.lang.String):android.graphics.Bitmap");
    }

    private Bitmap createVideoThumbnail(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), C0984R.drawable.ff2_2_media_watch);
        canvas.drawBitmap(decodeResource, (float) ((bitmap.getWidth() / 2) - (decodeResource.getWidth() / 2)), (float) ((bitmap.getHeight() / 2) - (decodeResource.getHeight() / 2)), null);
        decodeResource.recycle();
        return bitmap;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void drawClusters() {
        /*
        r8 = this;
        r2 = 0;
        r7 = 1;
        r6 = 0;
        r8.clearMap();
        r0 = r8.markers_list;
        r0.clear();
        r0 = r8.thumbnails_list;
        r0.clear();
        r8.killAllDownloadingTask();
        r0 = r8.borders;
        r0.clear();
        r8.initPosition();
        r0 = r8.username;
        if (r0 == 0) goto L_0x0076;
    L_0x001f:
        r0 = r8.pilotFlightsClusters;
        r1 = r8.getZoomRank();
        r0 = r0.get(r1);
        if (r0 == 0) goto L_0x0083;
    L_0x002b:
        r0 = r8.pilotFlightsClusters;
        r1 = r8.getZoomRank();
        r0 = r0.get(r1);
        r0 = (java.util.HashMap) r0;
        r0 = r0.values();
        r1 = r0.iterator();
    L_0x003f:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x0083;
    L_0x0045:
        r0 = r1.next();
        r0 = (com.parrot.freeflight.academy.model.FlightsCluster) r0;
        r2 = com.parrot.freeflight.academy.activities.AcademyMapActivity.C10156.f318xef50f5c1;	 Catch:{ Exception -> 0x005f }
        r3 = r8.currentMediaMode;	 Catch:{ Exception -> 0x005f }
        r3 = r3.ordinal();	 Catch:{ Exception -> 0x005f }
        r2 = r2[r3];	 Catch:{ Exception -> 0x005f }
        switch(r2) {
            case 1: goto L_0x0059;
            case 2: goto L_0x0064;
            case 3: goto L_0x006a;
            case 4: goto L_0x0070;
            default: goto L_0x0058;
        };	 Catch:{ Exception -> 0x005f }
    L_0x0058:
        goto L_0x003f;
    L_0x0059:
        r2 = "all";
        r8.addFlightClusterMarker(r2, r0);	 Catch:{ Exception -> 0x005f }
        goto L_0x003f;
    L_0x005f:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x003f;
    L_0x0064:
        r2 = "photos";
        r8.addFlightClusterMarker(r2, r0);	 Catch:{ Exception -> 0x005f }
        goto L_0x003f;
    L_0x006a:
        r2 = "videos";
        r8.addFlightClusterMarker(r2, r0);	 Catch:{ Exception -> 0x005f }
        goto L_0x003f;
    L_0x0070:
        r2 = "gps";
        r8.addFlightClusterMarker(r2, r0);	 Catch:{ Exception -> 0x005f }
        goto L_0x003f;
    L_0x0076:
        r0 = com.parrot.freeflight.academy.activities.AcademyMapActivity.C10156.f321x3c91b21a;
        r1 = r8.currentViewMode;
        r1 = r1.ordinal();
        r0 = r0[r1];
        switch(r0) {
            case 1: goto L_0x00a3;
            case 2: goto L_0x0109;
            case 3: goto L_0x008d;
            default: goto L_0x0083;
        };
    L_0x0083:
        r0 = r8.currentThumbnailsMode;
        r1 = com.parrot.freeflight.academy.activities.AcademyMapActivity.THUMBNAILS.ON;
        if (r0 != r1) goto L_0x008c;
    L_0x0089:
        r8.drawThumbnails();
    L_0x008c:
        return;
    L_0x008d:
        r0 = r8.hotspotsClusters;
        r1 = r0.iterator();
    L_0x0093:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x0083;
    L_0x0099:
        r0 = r1.next();
        r0 = (com.parrot.freeflight.academy.model.HotspotsCluster) r0;
        r8.addHotspotClusterMarker(r0);
        goto L_0x0093;
    L_0x00a3:
        r0 = r8.flightsClusters;
        r0 = r0.values();
        r3 = r0.iterator();
    L_0x00ad:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x0083;
    L_0x00b3:
        r0 = r3.next();
        r0 = (com.parrot.freeflight.academy.model.FlightsCluster) r0;
        r1 = com.parrot.freeflight.academy.activities.AcademyMapActivity.C10156.f318xef50f5c1;
        r4 = r8.currentMediaMode;
        r4 = r4.ordinal();
        r1 = r1[r4];
        switch(r1) {
            case 1: goto L_0x00ed;
            case 2: goto L_0x00f4;
            case 3: goto L_0x00fb;
            case 4: goto L_0x0102;
            default: goto L_0x00c6;
        };
    L_0x00c6:
        r1 = r2;
    L_0x00c7:
        r4 = r8.currentThumbnailsMode;
        r5 = com.parrot.freeflight.academy.activities.AcademyMapActivity.THUMBNAILS.ON;
        if (r4 != r5) goto L_0x00ad;
    L_0x00cd:
        r4 = r8.getThumbUrl(r0);
        if (r4 == 0) goto L_0x00ad;
    L_0x00d3:
        r4 = r8.thumbnails_list;
        r4 = r4.containsKey(r1);
        if (r4 != 0) goto L_0x00ad;
    L_0x00db:
        r4 = new com.parrot.freeflight.academy.activities.AcademyMapActivity$DownloadImageAsyncTask;
        r4.<init>(r1);
        r1 = r8.downloadImageAsyncTask_list;
        r1.add(r4);
        r1 = new com.parrot.freeflight.academy.model.FlightsCluster[r7];
        r1[r6] = r0;
        r4.execute(r1);
        goto L_0x00ad;
    L_0x00ed:
        r1 = "all";
        r1 = r8.addFlightClusterMarker(r1, r0);
        goto L_0x00c7;
    L_0x00f4:
        r1 = "photos";
        r1 = r8.addFlightClusterMarker(r1, r0);
        goto L_0x00c7;
    L_0x00fb:
        r1 = "videos";
        r1 = r8.addFlightClusterMarker(r1, r0);
        goto L_0x00c7;
    L_0x0102:
        r1 = "gps";
        r1 = r8.addFlightClusterMarker(r1, r0);
        goto L_0x00c7;
    L_0x0109:
        r0 = r8.myFlightsClusters;
        r1 = r8.getZoomRank();
        r0 = r0.get(r1);
        if (r0 == 0) goto L_0x0083;
    L_0x0115:
        r0 = r8.myFlightsClusters;
        r1 = r8.getZoomRank();
        r0 = r0.get(r1);
        r0 = (java.util.HashMap) r0;
        r0 = r0.values();
        r3 = r0.iterator();
    L_0x0129:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x0083;
    L_0x012f:
        r0 = r3.next();
        r0 = (com.parrot.freeflight.academy.model.FlightsCluster) r0;
        r1 = com.parrot.freeflight.academy.activities.AcademyMapActivity.C10156.f318xef50f5c1;
        r4 = r8.currentMediaMode;
        r4 = r4.ordinal();
        r1 = r1[r4];
        switch(r1) {
            case 1: goto L_0x0169;
            case 2: goto L_0x0170;
            case 3: goto L_0x0177;
            case 4: goto L_0x017e;
            default: goto L_0x0142;
        };
    L_0x0142:
        r1 = r2;
    L_0x0143:
        r4 = r8.currentThumbnailsMode;
        r5 = com.parrot.freeflight.academy.activities.AcademyMapActivity.THUMBNAILS.ON;
        if (r4 != r5) goto L_0x0129;
    L_0x0149:
        r4 = r8.getThumbUrl(r0);
        if (r4 == 0) goto L_0x0129;
    L_0x014f:
        r4 = r8.thumbnails_list;
        r4 = r4.containsKey(r1);
        if (r4 != 0) goto L_0x0129;
    L_0x0157:
        r4 = new com.parrot.freeflight.academy.activities.AcademyMapActivity$DownloadImageAsyncTask;
        r4.<init>(r1);
        r1 = r8.downloadImageAsyncTask_list;
        r1.add(r4);
        r1 = new com.parrot.freeflight.academy.model.FlightsCluster[r7];
        r1[r6] = r0;
        r4.execute(r1);
        goto L_0x0129;
    L_0x0169:
        r1 = "all";
        r1 = r8.addFlightClusterMarker(r1, r0);
        goto L_0x0143;
    L_0x0170:
        r1 = "photos";
        r1 = r8.addFlightClusterMarker(r1, r0);
        goto L_0x0143;
    L_0x0177:
        r1 = "videos";
        r1 = r8.addFlightClusterMarker(r1, r0);
        goto L_0x0143;
    L_0x017e:
        r1 = "gps";
        r1 = r8.addFlightClusterMarker(r1, r0);
        goto L_0x0143;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.academy.activities.AcademyMapActivity.drawClusters():void");
    }

    private void drawThumbnails() {
        Point point = new Point();
        for (LatLng latLng : this.thumbnails_list.keySet()) {
            Pair pair = (Pair) this.markers_list.get(latLng);
            Bitmap bitmap = (Bitmap) this.thumbnails_list.get(latLng);
            if (!(pair == null || bitmap == null)) {
                boolean z;
                Object obj;
                String mediaUrl;
                Bitmap createVideoThumbnail;
                Point toScreenLocation = this.map.getProjection().toScreenLocation(latLng);
                Rect rect = new Rect(toScreenLocation.x, toScreenLocation.y, toScreenLocation.x + 100, toScreenLocation.y + 75);
                int i = 0;
                while (i < this.borders.size()) {
                    if (Rect.intersects((Rect) this.borders.get(i), rect)) {
                        z = true;
                        if (!z) {
                            switch (this.currentViewMode) {
                                case ALL_FLIGHTS:
                                    obj = this.flightsClusters.get(((Marker) pair.first).getTitle());
                                    break;
                                case MY_FLIGHTS:
                                    obj = ((HashMap) this.myFlightsClusters.get(getZoomRank())).get(((Marker) pair.first).getTitle());
                                    break;
                                case HOTSPOTS:
                                    obj = null;
                                    break;
                                default:
                                    obj = null;
                                    break;
                            }
                            mediaUrl = obj == null ? getMediaUrl((FlightsCluster) obj) : null;
                            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 75, false);
                            createVideoThumbnail = (mediaUrl != null || mediaUrl.contains("google") || mediaUrl.contains("images")) ? bitmap : createVideoThumbnail(bitmap);
                            ((Marker) pair.first).remove();
                            this.markers.add(this.map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(createVideoThumbnail)).anchor(0.0f, 0.0f).position(latLng).snippet(mediaUrl)));
                            this.markers.add(this.map.addMarker((MarkerOptions) pair.second));
                            this.borders.add(rect);
                            createVideoThumbnail.recycle();
                        }
                    } else {
                        i++;
                    }
                }
                z = false;
                if (!z) {
                    switch (this.currentViewMode) {
                        case ALL_FLIGHTS:
                            obj = this.flightsClusters.get(((Marker) pair.first).getTitle());
                            break;
                        case MY_FLIGHTS:
                            obj = ((HashMap) this.myFlightsClusters.get(getZoomRank())).get(((Marker) pair.first).getTitle());
                            break;
                        case HOTSPOTS:
                            obj = null;
                            break;
                        default:
                            obj = null;
                            break;
                    }
                    if (obj == null) {
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 75, false);
                    if (mediaUrl != null) {
                    }
                    ((Marker) pair.first).remove();
                    this.markers.add(this.map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(createVideoThumbnail)).anchor(0.0f, 0.0f).position(latLng).snippet(mediaUrl)));
                    this.markers.add(this.map.addMarker((MarkerOptions) pair.second));
                    this.borders.add(rect);
                    createVideoThumbnail.recycle();
                }
            }
        }
    }

    private void flightsToClusters(List<Flight> list, SparseArray<HashMap<String, FlightsCluster>> sparseArray) {
        sparseArray.clear();
        System.out.println(list.size());
        for (Flight flight : list) {
            if (!this.getClustersTask.isCancelled()) {
                double gps_latitude = flight.getGps_latitude();
                double gps_longitude = flight.getGps_longitude();
                if (!(gps_latitude == AcademyFlightDetailsActivity.INVALID_COORDINATES || gps_longitude == AcademyFlightDetailsActivity.INVALID_COORDINATES)) {
                    int i;
                    int i2;
                    String uri;
                    String str;
                    String str2;
                    String str3 = null;
                    String str4 = null;
                    int size;
                    int size2;
                    String uri2;
                    if (this.username == null) {
                        this.mediaGallery = new ARDroneMediaGallery(this);
                        List mediaImageList = this.mediaGallery.getMediaImageList(flight.getFlightTag());
                        List mediaVideoList = this.mediaGallery.getMediaVideoList(flight.getFlightTag());
                        flight.setLocalCaptureSet(mediaImageList);
                        flight.setLocalVideoSet(mediaVideoList);
                        size = mediaImageList.size();
                        size2 = mediaVideoList.size();
                        if (size > 0) {
                            str3 = ((MediaVO) mediaImageList.get(size - 1)).getUri().toString();
                            str4 = ((MediaVO) mediaImageList.get(size - 1)).getUri().toString();
                        } else {
                            flight.setFlightCaptureSet(null);
                            str3 = null;
                            str4 = null;
                        }
                        if (size2 > 0) {
                            uri2 = ((MediaVO) mediaVideoList.get(size2 - 1)).getUri().toString();
                            i = size2;
                            i2 = size;
                            uri = ((MediaVO) mediaVideoList.get(size2 - 1)).getUri().toString();
                            str = uri2;
                            str2 = str3;
                        } else {
                            flight.setFlightVideoSet(null);
                            i = size2;
                            i2 = size;
                            uri = str4;
                            str = null;
                            str2 = str3;
                        }
                    } else {
                        ArrayList flightCaptureSet = flight.getFlightCaptureSet();
                        ArrayList flightVideoSet = flight.getFlightVideoSet();
                        size = flightCaptureSet.size();
                        size2 = flightVideoSet.size();
                        if (size > 0) {
                            str3 = ((Media) flightCaptureSet.get(size - 1)).getUrl().toString();
                            str4 = ((Media) flightCaptureSet.get(size - 1)).getUrl().toString();
                        }
                        if (size2 > 0) {
                            uri2 = ((Media) flightVideoSet.get(size2 - 1)).getUrl().toString();
                            i = size2;
                            i2 = size;
                            uri = ((Media) flightVideoSet.get(size2 - 1)).getUrl().toString();
                            str = uri2;
                            str2 = str3;
                        } else {
                            i = size2;
                            i2 = size;
                            uri = str4;
                            str = null;
                            str2 = str3;
                        }
                    }
                    ARSexaCoordinate ARConvertCLLocationDegreesToSexaCoordinate = AcademyLocationUtils.ARConvertCLLocationDegreesToSexaCoordinate(gps_latitude, true);
                    ARSexaCoordinate ARConvertCLLocationDegreesToSexaCoordinate2 = AcademyLocationUtils.ARConvertCLLocationDegreesToSexaCoordinate(gps_longitude, false);
                    long degrees = ARConvertCLLocationDegreesToSexaCoordinate.getDegrees();
                    long minutes = ARConvertCLLocationDegreesToSexaCoordinate.getMinutes();
                    long seconds = ARConvertCLLocationDegreesToSexaCoordinate.getSeconds();
                    long degrees2 = ARConvertCLLocationDegreesToSexaCoordinate2.getDegrees();
                    long minutes2 = ARConvertCLLocationDegreesToSexaCoordinate2.getMinutes();
                    long seconds2 = ARConvertCLLocationDegreesToSexaCoordinate2.getSeconds();
                    for (int i3 = 0; i3 < 17 && !this.getClustersTask.isCancelled(); i3++) {
                        HashMap hashMap;
                        FlightsCluster flightsCluster;
                        FlightsCluster flightsCluster2;
                        int intValue;
                        ARSexaCoordinate aRSexaCoordinate = new ARSexaCoordinate();
                        ARSexaCoordinate aRSexaCoordinate2 = new ARSexaCoordinate();
                        ARSexaCoordinate rankGranularity = rankGranularity(i3);
                        long degrees3 = ((rankGranularity.getDegrees() * 3600) + (rankGranularity.getMinutes() * 60)) + rankGranularity.getSeconds();
                        long j = ((((3600 * degrees) + (60 * minutes)) + seconds) / degrees3) * degrees3;
                        aRSexaCoordinate.setDegrees(j / 3600);
                        j %= 3600;
                        aRSexaCoordinate.setMinutes(j / 60);
                        aRSexaCoordinate.setSeconds(j % 60);
                        aRSexaCoordinate.setDirection(ARConvertCLLocationDegreesToSexaCoordinate.getDirection());
                        degrees3 *= (((3600 * degrees2) + (60 * minutes2)) + seconds2) / degrees3;
                        aRSexaCoordinate2.setDegrees(degrees3 / 3600);
                        degrees3 %= 3600;
                        aRSexaCoordinate2.setMinutes(degrees3 / 60);
                        aRSexaCoordinate2.setSeconds(degrees3 % 60);
                        aRSexaCoordinate2.setDirection(ARConvertCLLocationDegreesToSexaCoordinate2.getDirection());
                        String format = String.format("%02d%02d%02d%c%03d%02d%02d%c", new Object[]{Long.valueOf(aRSexaCoordinate.getDegrees()), Long.valueOf(aRSexaCoordinate.getMinutes()), Long.valueOf(aRSexaCoordinate.getSeconds()), Character.valueOf(aRSexaCoordinate.getDirection()), Long.valueOf(aRSexaCoordinate2.getDegrees()), Long.valueOf(aRSexaCoordinate2.getMinutes()), Long.valueOf(aRSexaCoordinate2.getSeconds()), Character.valueOf(aRSexaCoordinate2.getDirection())});
                        HashMap hashMap2 = (HashMap) sparseArray.get(i3);
                        if (hashMap2 != null) {
                            hashMap = hashMap2;
                            flightsCluster = (FlightsCluster) hashMap2.get(format);
                        } else {
                            hashMap = new HashMap();
                            flightsCluster = null;
                        }
                        if (flightsCluster == null) {
                            flightsCluster = new FlightsCluster();
                            flightsCluster.setIndex(format);
                            flightsCluster.getCluster().setGps_latitude(String.valueOf(gps_latitude));
                            flightsCluster.getCluster().setGps_longitude(String.valueOf(gps_longitude));
                            flightsCluster2 = flightsCluster;
                        } else {
                            flightsCluster2 = flightsCluster;
                        }
                        int i4 = 0;
                        int i5 = 0;
                        MediaCluster mediaCluster = (MediaCluster) flightsCluster2.getMediaCluster().get(PHOTOS);
                        MediaCluster mediaCluster2 = (MediaCluster) flightsCluster2.getMediaCluster().get(VIDEOS);
                        MediaCluster mediaCluster3 = (MediaCluster) flightsCluster2.getMediaCluster().get(MEDIAS);
                        flightsCluster2.getFlights().add(flight);
                        int intValue2 = Integer.valueOf(flightsCluster2.getCluster().getCount()).intValue();
                        if (i2 > 0) {
                            if (mediaCluster != null) {
                                i5 = Integer.valueOf(mediaCluster.getCount()).intValue();
                            }
                            i4 = i5 + 1;
                        }
                        if (i > 0) {
                            intValue = (mediaCluster2 != null ? Integer.valueOf(mediaCluster2.getCount()).intValue() : 0) + 1;
                        } else {
                            intValue = 0;
                        }
                        if (i2 > 0 || i > 0) {
                            i5 = (mediaCluster3 != null ? Integer.valueOf(mediaCluster3.getCount()).intValue() : 0) + 1;
                        } else {
                            i5 = 0;
                        }
                        flightsCluster2.getCluster().setCount(intValue2 + 1);
                        if (i4 > 0) {
                            if (mediaCluster == null) {
                                mediaCluster = new MediaCluster();
                            }
                            mediaCluster.setCount(i4);
                            mediaCluster.getFlights().add(flight);
                            if (mediaCluster.getUrl() == null) {
                                mediaCluster.setUrl(str2);
                            }
                            if (mediaCluster.getThumbUrl() == null) {
                                mediaCluster.setThumbUrl(str2);
                            }
                            flightsCluster2.getMediaCluster().put(PHOTOS, mediaCluster);
                        }
                        if (intValue > 0) {
                            if (mediaCluster2 == null) {
                                mediaCluster2 = new MediaCluster();
                            }
                            mediaCluster2.setCount(intValue);
                            if (i > 0) {
                                mediaCluster2.getFlights().add(flight);
                            }
                            if (mediaCluster2.getUrl() == null) {
                                mediaCluster2.setUrl(str);
                            }
                            if (mediaCluster2.getThumbUrl() == null) {
                                mediaCluster2.setThumbUrl(str);
                            }
                            flightsCluster2.getMediaCluster().put(VIDEOS, mediaCluster2);
                        }
                        if (i5 > 0) {
                            if (mediaCluster3 == null) {
                                mediaCluster3 = new MediaCluster();
                            }
                            mediaCluster3.setCount(i5);
                            mediaCluster3.getFlights().add(flight);
                            if (mediaCluster3.getUrl() == null) {
                                mediaCluster3.setUrl(uri);
                            }
                            if (mediaCluster3.getThumbUrl() == null) {
                                mediaCluster3.setThumbUrl(uri);
                            }
                            flightsCluster2.getMediaCluster().put(MEDIAS, mediaCluster3);
                        }
                        if (!hashMap.containsKey(format)) {
                            hashMap.put(format, flightsCluster2);
                        }
                        sparseArray.put(i3, hashMap);
                    }
                }
            } else {
                return;
            }
        }
    }

    private String getMediaUrl(FlightsCluster flightsCluster) {
        switch (this.currentMediaMode) {
            case ALL:
                if (flightsCluster.hasMedias()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getUrl();
                }
                if (flightsCluster.hasPhotos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(PHOTOS)).getUrl();
                }
                if (flightsCluster.hasVideos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getUrl();
                }
                break;
            case PHOTOS:
                if (flightsCluster.hasPhotos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(PHOTOS)).getUrl();
                }
                break;
            case VIDEOS:
                if (flightsCluster.hasVideos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getUrl();
                }
                break;
            case GPS:
                if (flightsCluster.hasGps()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(GPS)).getUrl();
                }
                break;
        }
        return null;
    }

    private void getPrefs() {
        SharedPreferences academySharedPreferences = new AcademySharedPreferences(this, getSharedPreferences(AcademyUtils.ACADEMY_PREFS_FILE_NAME, 0));
        this.currentMapMode = MAP_MODE.valueOf(academySharedPreferences.getString("map_mode", MAP_MODE.STANDARD.name()));
        this.currentViewMode = VIEW_MODE.valueOf(academySharedPreferences.getString("view_mode", VIEW_MODE.ALL_FLIGHTS.name()));
        this.currentMediaMode = MEDIA_MODE.valueOf(academySharedPreferences.getString("media_mode", MEDIA_MODE.ALL.name()));
        this.currentThumbnailsMode = THUMBNAILS.valueOf(academySharedPreferences.getString("thumbnails_mode", THUMBNAILS.ON.name()));
        this.lastPosition = new LatLng((double) academySharedPreferences.getFloat("last_position_lat", 0.0f), (double) academySharedPreferences.getFloat("last_position_lng", 0.0f));
        initPosition();
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(this.lastPosition, academySharedPreferences.getFloat("zoom_level", 8.0f)));
    }

    private String getThumbUrl(FlightsCluster flightsCluster) {
        switch (this.currentMediaMode) {
            case ALL:
                if (flightsCluster.hasMedias()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getThumbUrl();
                }
                if (flightsCluster.hasPhotos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(PHOTOS)).getThumbUrl();
                }
                if (flightsCluster.hasVideos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getThumbUrl();
                }
                break;
            case PHOTOS:
                if (flightsCluster.hasPhotos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(PHOTOS)).getThumbUrl();
                }
                break;
            case VIDEOS:
                if (flightsCluster.hasVideos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getThumbUrl();
                }
                break;
            case GPS:
                if (flightsCluster.hasGps()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(GPS)).getThumbUrl();
                }
                if (flightsCluster.hasPhotos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(PHOTOS)).getThumbUrl();
                }
                if (flightsCluster.hasVideos()) {
                    return ((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getThumbUrl();
                }
                break;
        }
        return null;
    }

    private int getZoomRank() {
        int i = ((int) this.zoom) - 1;
        return i <= 13 ? i + 2 : 16;
    }

    private void hideAddSpotConfirmCancel() {
        this.add_hotspot.toggle();
        this.actionBar.showSettingsButton();
        this.target.setVisibility(8);
        this.actionBar.showBackButton();
        this.search.setVisibility(0);
        this.add_hotspot.setVisibility(0);
        this.addspot_confirm_cancel.setVisibility(8);
        this.addspot_address_text.setVisibility(8);
    }

    private void hideAllControls() {
        this.actionBar.hideSettingsButton();
        this.actionBar.hideBackButton();
        this.target.setVisibility(8);
        this.addspot_details.setVisibility(8);
        this.add_hotspot.setVisibility(8);
        this.search.setVisibility(8);
    }

    private void initActionBar() {
        this.actionBar = new ActionBar(this, findViewById(C0984R.id.navigation_bar));
        this.actionBar.setTitle(getString(C0984R.string.aa_ID000069));
        this.actionBar.initBackButton();
        if (this.position == null) {
            this.actionBar.initMapSettingsButton(this.mapPlusBtnClickListener);
        }
        this.actionBar.changeBackground(Background.ACCENT_HALF_TRANSP);
    }

    private void initBitmaps() {
        this.media_bitmap = BitmapFactory.decodeResource(getResources(), C0984R.drawable.ff2_2_cluster_with_media);
        this.photos_bitmap = BitmapFactory.decodeResource(getResources(), C0984R.drawable.ff2_2_photos);
        this.videos_bitmap = BitmapFactory.decodeResource(getResources(), C0984R.drawable.ff2_2_videos);
        this.gps_bitmap = BitmapFactory.decodeResource(getResources(), C0984R.drawable.ff2_gps_not_selected);
    }

    private void initClusters() {
        this.hotspotsClusters = new ArrayList();
        this.flightsClusters = new HashMap();
        this.myFlightsClusters = new SparseArray();
        this.pilotFlightsClusters = new SparseArray();
        this.getClustersTask = new GetClustersAsyncTask();
    }

    private void initFlightPosition() {
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(this.position, 18.0f));
        this.markers.add(this.map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(C0984R.drawable.ff2_2_flights)).position(this.position)));
    }

    private void initListeners() {
        this.map.setOnMarkerClickListener(this);
        this.map.setOnCameraChangeListener(this);
        this.search.setOnClickListener(this);
        this.add_hotspot.setOnClickListener(this);
        this.search_pilot.setOnClickListener(this);
        this.cancel.setOnClickListener(this);
        this.search_address.setOnClickListener(this);
        this.search_field.setOnEditorActionListener(this);
        this.addspot_address.setOnClickListener(this);
        this.addspot_drop_pin.setOnClickListener(this);
        this.addspot_gps_position.setOnClickListener(this);
        this.addspot_confirm.setOnClickListener(this);
        this.addspot_cancel.setOnClickListener(this);
    }

    private void initMap() {
        this.map.getUiSettings().setZoomControlsEnabled(false);
        this.map.getUiSettings().setRotateGesturesEnabled(false);
        this.map.getUiSettings().setCompassEnabled(false);
        this.map.getUiSettings().setTiltGesturesEnabled(false);
        this.zoom = this.map.getCameraPosition().zoom;
    }

    private void initMenu() {
        this.flights_group = new MyRadioGroup(this);
        this.flights = new MyCheckedTextView(this, C0984R.drawable.ff2_2_flights_thumb, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000086, true, this.settingsChangedListener);
        this.flights.addDrawable();
        this.my_flights = new MyCheckedTextView(this, C0984R.drawable.ff2_2_myflights_thumb, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000085, true, this.settingsChangedListener);
        this.my_flights.addDrawable();
        this.hotspots = new MyCheckedTextView(this, C0984R.drawable.ff2_2_hotspot_thumb, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000087, true, this.settingsChangedListener);
        this.hotspots.addDrawable();
        this.flights_group.addView(this.flights, false, VIEW_MODE.ALL_FLIGHTS);
        this.flights_group.addView(this.my_flights, false, VIEW_MODE.MY_FLIGHTS);
        this.flights_group.addView(this.hotspots, false, VIEW_MODE.HOTSPOTS);
        this.menu.addView(this.flights_group, false, null);
        this.media_group = new MyRadioGroup(this);
        this.all = new MyCheckedTextView(this, C0984R.drawable.ff2_2_transparent, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000191, true, this.settingsChangedListener);
        this.all.addDrawable();
        this.photos = new MyCheckedTextView(this, C0984R.drawable.ff2_2_photos_thumb, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000184, true, this.settingsChangedListener);
        this.photos.addDrawable();
        this.videos = new MyCheckedTextView(this, C0984R.drawable.ff2_2_videos_thumb, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000185, true, this.settingsChangedListener);
        this.videos.addDrawable();
        this.gps = new MyCheckedTextView(this, C0984R.drawable.ff2_gps_not_selected, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000192, true, this.settingsChangedListener);
        this.gps.addDrawable();
        this.media_group.addView(this.all, false, MEDIA_MODE.ALL);
        this.media_group.addView(this.photos, false, MEDIA_MODE.PHOTOS);
        this.media_group.addView(this.videos, false, MEDIA_MODE.VIDEOS);
        this.media_group.addView(this.gps, false, MEDIA_MODE.GPS);
        this.menu.addView(this.media_group, false, Boolean.valueOf(false));
        this.thumbnails = new MyCheckedTextView(this, C0984R.drawable.ff2_2_thumbnails, C0984R.drawable.rbtn_check, C0984R.string.aa_ID000188, true, this.settingsChangedListener);
        this.thumbnails.addDrawable();
        this.menu.addView(this.thumbnails, false, "thumbnails");
        this.map_group = new MyRadioGroup(this);
        this.standard = new MyCheckedTextView(this, C0984R.drawable.ff2_2_standard, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000175, true, this.settingsChangedListener);
        this.standard.addDrawable();
        this.satellite = new MyCheckedTextView(this, C0984R.drawable.ff2_2_satellite, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000176, true, this.settingsChangedListener);
        this.satellite.addDrawable();
        this.hybrid = new MyCheckedTextView(this, C0984R.drawable.ff2_2_hybrid, C0984R.drawable.rbtn_mapmenu, C0984R.string.aa_ID000186, true, this.settingsChangedListener);
        this.hybrid.addDrawable();
        this.map_group.addView(this.standard, false, MAP_MODE.STANDARD);
        this.map_group.addView(this.satellite, false, MAP_MODE.SATELLITE);
        this.map_group.addView(this.hybrid, false, MAP_MODE.HYBRID);
        this.menu.addView(this.map_group, false, "map_group");
    }

    private void initPosition() {
        LocationManager locationManager = (LocationManager) getSystemService("location");
        String bestProvider = locationManager.getBestProvider(new Criteria(), false);
        GPSHelper instance = GPSHelper.getInstance(this);
        this.location = locationManager.getLastKnownLocation(bestProvider);
        if (GPSHelper.isGpsOn(this)) {
            instance.startListening(this);
            this.location = GPSHelper.getLastKnownLocation(this);
            requestGPSUpdate();
            Log.d(TAG, "GPS [OK]");
            return;
        }
        Log.d(TAG, "GPS [DISABLED].");
        this.addspot_gps_position.setEnabled(this.location != null);
    }

    private void initProgress() {
        this.progress = new ProgressDialog(this);
        this.progress.setCancelable(false);
    }

    private void initSettings() {
        switch (this.currentMapMode) {
            case STANDARD:
                this.standard.setChecked(true);
                this.map.setMapType(1);
                break;
            case SATELLITE:
                this.satellite.setChecked(true);
                this.map.setMapType(2);
                break;
            case HYBRID:
                this.hybrid.setChecked(true);
                this.map.setMapType(4);
                break;
        }
        switch (this.currentViewMode) {
            case ALL_FLIGHTS:
                this.flights.setChecked(true);
                changeMarker();
                break;
            case MY_FLIGHTS:
                this.my_flights.setChecked(true);
                changeMarker();
                break;
            case HOTSPOTS:
                this.hotspots.setChecked(true);
                this.search_pilot.setEnabled(false);
                changeMarker();
                break;
        }
        switch (this.currentMediaMode) {
            case ALL:
                this.all.setChecked(true);
                break;
            case PHOTOS:
                this.photos.setChecked(true);
                break;
            case VIDEOS:
                this.videos.setChecked(true);
                break;
            case GPS:
                this.gps.setChecked(true);
                break;
            default:
                Log.w(TAG, "Unknown media mode: " + this.currentMediaMode.name());
                break;
        }
        switch (this.currentThumbnailsMode) {
            case ON:
                this.thumbnails.setChecked(true);
                return;
            case OFF:
                this.thumbnails.setChecked(false);
                return;
            default:
                return;
        }
    }

    private void initUI() {
        setUpMapIfNeeded();
        this.zoom_level_text = (TextView) findViewById(C0984R.id.zoom_level);
        this.zoom_level_text.setText(((int) this.map.getCameraPosition().zoom) + "x");
        if (this.position == null) {
            this.zoom_level_text.setVisibility(0);
        }
        this.menu = (DropDownMenu) findViewById(C0984R.id.map_menu);
        this.search = (CheckedTextView) findViewById(C0984R.id.search);
        this.search.setVisibility(this.position != null ? 8 : 0);
        this.search_details = (AnimatedView) findViewById(C0984R.id.search_details);
        Animation loadAnimation = AnimationUtils.loadAnimation(this, 2130968580);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(this, 2130968583);
        this.search_details.setInAnimation(loadAnimation);
        this.search_details.setOutAnimation(loadAnimation2);
        this.addspot_details = (AnimatedView) findViewById(C0984R.id.addspot_details);
        this.addspot_details.setInAnimation(loadAnimation);
        this.addspot_details.setOutAnimation(loadAnimation2);
        this.search_bar = (LinearLayout) findViewById(C0984R.id.search_bar);
        this.search_pilot = (Button) findViewById(C0984R.id.pilots_flight);
        this.search_address = (Button) findViewById(C0984R.id.address);
        this.cancel = (Button) findViewById(C0984R.id.cancel);
        this.search_field = (EditText) findViewById(C0984R.id.search_edit);
        this.add_hotspot = (CheckedTextView) findViewById(C0984R.id.add_hotspot);
        this.target = (ImageView) findViewById(C0984R.id.target);
        this.addspot_address = (Button) findViewById(C0984R.id.addspot_address);
        this.addspot_drop_pin = (Button) findViewById(C0984R.id.addspot_drop_pin);
        this.addspot_gps_position = (Button) findViewById(C0984R.id.addspot_gps_position);
        this.addspot_confirm = (Button) findViewById(C0984R.id.addspot_confirm);
        this.addspot_cancel = (Button) findViewById(C0984R.id.addspot_cancel);
        this.addspot_confirm_cancel = (LinearLayout) findViewById(C0984R.id.addspot_confirm_cancel_layout);
        this.addspot_address_text = (TextView) findViewById(C0984R.id.addspot_address_text);
    }

    private void killAllDownloadingTask() {
        for (int i = 0; i < this.downloadImageAsyncTask_list.size(); i++) {
            ((DownloadImageAsyncTask) this.downloadImageAsyncTask_list.get(i)).cancel(true);
        }
        this.downloadImageAsyncTask_list.clear();
    }

    private void onMapModeClicked(View view) {
        this.currentMapMode = (MAP_MODE) view.getTag();
        onRadioButtonClicked(view);
        changeMapMode();
    }

    private void onMediaModeClicked(View view) {
        this.currentMediaMode = (MEDIA_MODE) view.getTag();
        onRadioButtonClicked(view);
        drawClusters();
    }

    private void onRadioButtonClicked(View view) {
        ((MyRadioGroup) view.getParent()).check(view.getId());
        ((MyRadioGroup) view.getParent()).onClick(view);
    }

    private void onThumbnailsClicked(View view) {
        this.currentThumbnailsMode = ((MyCheckedTextView) view).isChecked() ? THUMBNAILS.ON : THUMBNAILS.OFF;
        drawClusters();
    }

    private void onViewModeClicked(View view) {
        this.currentViewMode = (VIEW_MODE) view.getTag();
        changeMarker();
        this.add_hotspot.setVisibility(this.currentViewMode == VIEW_MODE.HOTSPOTS ? 0 : 4);
        this.search_pilot.setEnabled(this.currentViewMode == VIEW_MODE.ALL_FLIGHTS);
        onRadioButtonClicked(view);
        this.getClustersTask = new GetClustersAsyncTask();
        this.getClustersTask.execute(new Void[0]);
    }

    private ARSexaCoordinate rankGranularity(int i) {
        ARSexaCoordinate aRSexaCoordinate = new ARSexaCoordinate();
        switch (i) {
            case 0:
                aRSexaCoordinate.setDegrees(180);
                break;
            case 1:
                aRSexaCoordinate.setDegrees(90);
                break;
            case 2:
                aRSexaCoordinate.setDegrees(60);
                break;
            case 3:
                aRSexaCoordinate.setDegrees(30);
                break;
            case 4:
                aRSexaCoordinate.setDegrees(15);
                break;
            case 5:
                aRSexaCoordinate.setDegrees(8);
                break;
            case 6:
                aRSexaCoordinate.setDegrees(4);
                break;
            case 7:
                aRSexaCoordinate.setDegrees(2);
                break;
            case 8:
                aRSexaCoordinate.setDegrees(1);
                break;
            case 9:
                aRSexaCoordinate.setMinutes(30);
                break;
            case 10:
                aRSexaCoordinate.setMinutes(15);
                break;
            case 11:
                aRSexaCoordinate.setMinutes(8);
                break;
            case 12:
                aRSexaCoordinate.setMinutes(4);
                break;
            case 13:
                aRSexaCoordinate.setMinutes(2);
                break;
            case 14:
                aRSexaCoordinate.setMinutes(1);
                break;
            case 15:
                aRSexaCoordinate.setSeconds(30);
                break;
            case 16:
                aRSexaCoordinate.setSeconds(15);
                break;
            case 17:
                aRSexaCoordinate.setSeconds(4);
                break;
            case 18:
                aRSexaCoordinate.setSeconds(1);
                break;
        }
        return aRSexaCoordinate;
    }

    private void savePrefs() {
        SharedPreferences academySharedPreferences = new AcademySharedPreferences(this, getSharedPreferences(AcademyUtils.ACADEMY_PREFS_FILE_NAME, 0));
        academySharedPreferences.edit().putString("map_mode", this.currentMapMode.name()).commit();
        academySharedPreferences.edit().putString("media_mode", this.currentMediaMode.name()).commit();
        academySharedPreferences.edit().putString("view_mode", this.currentViewMode.name()).commit();
        academySharedPreferences.edit().putString("thumbnails_mode", this.currentThumbnailsMode.name()).commit();
        academySharedPreferences.edit().putFloat("zoom_level", this.map.getCameraPosition().zoom).commit();
        this.lastPosition = this.map.getCameraPosition().target;
        academySharedPreferences.edit().putFloat("last_position_lat", (float) this.lastPosition.latitude).commit();
        academySharedPreferences.edit().putFloat("last_position_lng", (float) this.lastPosition.longitude).commit();
    }

    private void setDisplay() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        this.width = point.x;
        this.height = point.y;
        updateBorders();
    }

    private void setUpMapIfNeeded() {
        if (this.map == null) {
            this.mapFragment = (CustomMapFragment) getSupportFragmentManager().findFragmentById(C0984R.id.mapview);
            this.map = this.mapFragment.getMap();
            if (this.map == null) {
            }
        }
    }

    private void showAddSpotConfirmCancel() {
        this.actionBar.hideSettingsButton();
        this.actionBar.hideBackButton();
        this.search.setVisibility(8);
        if (this.currentAddSpotMode != ADDSPOT_MODE.DROP_PIN) {
            this.target.setVisibility(8);
        }
        this.addspot_details.setVisibility(8);
        this.add_hotspot.setVisibility(8);
        this.addspot_confirm_cancel.setVisibility(0);
    }

    public static void start(Context context, LatLng latLng) {
        Intent intent = new Intent(context, AcademyMapActivity.class);
        intent.putExtra(EXTRA_POSITION_OBJ_LAT, latLng.latitude);
        intent.putExtra(EXTRA_POSITION_OBJ_LNG, latLng.longitude);
        context.startActivity(intent);
    }

    private void updateBorders() {
        if (getZoomRank() > 4) {
            this.northEast = this.map.getProjection().fromScreenLocation(new Point(this.width * 2, -this.height));
            this.southWest = this.map.getProjection().fromScreenLocation(new Point(-this.width, this.height * 2));
        } else {
            this.northEast = new LatLng(89.999999d, 179.999999d);
            this.southWest = new LatLng(-89.999999d, -179.999999d);
        }
        this.bounds = new LatLngBounds(this.southWest, this.northEast);
    }

    private void updateUserFlightsClusters(SparseArray<HashMap<String, FlightsCluster>> sparseArray) {
        int zoomRank = getZoomRank();
        for (FlightsCluster flightsCluster : this.flightsClusters.values()) {
            if (!this.getClustersTask.isCancelled()) {
                String index = flightsCluster.getIndex();
                if (sparseArray.get(zoomRank) != null && ((HashMap) sparseArray.get(zoomRank)).containsKey(index)) {
                    switch (this.currentMediaMode) {
                        case ALL:
                            ((FlightsCluster) ((HashMap) sparseArray.get(zoomRank)).get(index)).switchGeneralLocation(flightsCluster);
                            break;
                        case PHOTOS:
                            ((FlightsCluster) ((HashMap) sparseArray.get(zoomRank)).get(index)).switchPhotoLocation(flightsCluster);
                            break;
                        case VIDEOS:
                            ((FlightsCluster) ((HashMap) sparseArray.get(zoomRank)).get(index)).switchVideoLocation(flightsCluster);
                            break;
                        case GPS:
                            ((FlightsCluster) ((HashMap) sparseArray.get(zoomRank)).get(index)).switchGpsLocation(flightsCluster);
                            break;
                        default:
                            break;
                    }
                }
            }
            return;
        }
    }

    public String countToString(int i) {
        return ((double) i) > 1000000.0d ? String.valueOf((int) (((double) i) / 1000000.0d)) + 'M' : ((double) i) > 1000.0d ? String.valueOf((int) (((double) i) / 1000.0d)) + 'K' : i == 1 ? HttpVersions.HTTP_0_9 : String.valueOf(i);
    }

    public String getUrl(VIEW_MODE view_mode) {
        switch (view_mode) {
            case ALL_FLIGHTS:
                return getString(C0984R.string.url_flights_clusters_details);
            case HOTSPOTS:
                return getString(C0984R.string.url_hotspots_clusters_details);
            default:
                return null;
        }
    }

    public void onCameraChange(CameraPosition cameraPosition) {
        if (this.position == null) {
            if (this.lastChange != null) {
                this.lastChange.cancel();
                this.lastChange.purge();
            }
            this.lastChange = new Timer();
            this.zoom_level_text.setText(((int) cameraPosition.zoom) + "x");
            this.lastChange.schedule(new MyTimerTask(cameraPosition), TIMEOUT_REFRESH);
        }
    }

    public void onClick(View view) {
        int i = 0;
        switch (view.getId()) {
            case C0984R.id.cancel /*2131361845*/:
                SystemUtils.hideKeyboard(this);
                this.search_bar.setVisibility(8);
                this.actionBar.showSettingsButton();
                if (this.username != null) {
                    this.getClustersTask = new GetClustersAsyncTask();
                    this.getClustersTask.execute(new Void[0]);
                    this.username = null;
                    return;
                }
                return;
            case C0984R.id.search /*2131361848*/:
                showSearchDetails(view);
                return;
            case C0984R.id.add_hotspot /*2131361850*/:
                ((CheckedTextView) view).toggle();
                if (this.search.isChecked()) {
                    this.search.toggle();
                    this.search_details.setVisibility(8);
                    this.target.setVisibility(8);
                }
                this.addspot_details.setVisibility(((CheckedTextView) view).isChecked() ? 0 : 8);
                ImageView imageView = this.target;
                if (!((CheckedTextView) view).isChecked()) {
                    i = 8;
                }
                imageView.setVisibility(i);
                if (((CheckedTextView) view).isChecked() && this.menu.getVisibility() == 0) {
                    showMapSettings();
                    return;
                }
                return;
            case C0984R.id.addspot_confirm /*2131361854*/:
                switch (this.currentAddSpotMode) {
                    case DROP_PIN:
                        this.hotspotLocation = this.map.getCameraPosition().target;
                        break;
                    case GPS_POSITION:
                        this.hotspotLocation = new LatLng(this.location.getLatitude(), this.location.getLongitude());
                        break;
                }
                AcademyAddHotspotActivity.start(this, this.hotspotLocation);
                hideAddSpotConfirmCancel();
                return;
            case C0984R.id.addspot_cancel /*2131361855*/:
                hideAddSpotConfirmCancel();
                this.currentAddSpotMode = null;
                return;
            case C0984R.id.addspot_gps_position /*2131361857*/:
                this.currentAddSpotMode = ADDSPOT_MODE.GPS_POSITION;
                showAddSpotConfirmCancel();
                return;
            case C0984R.id.addspot_drop_pin /*2131361858*/:
                this.currentAddSpotMode = ADDSPOT_MODE.DROP_PIN;
                showAddSpotConfirmCancel();
                return;
            case C0984R.id.addspot_address /*2131361859*/:
                this.currentAddSpotMode = ADDSPOT_MODE.ADDRESS;
                hideAllControls();
                new HotspotAddressDialog(this).show();
                return;
            case C0984R.id.address /*2131361915*/:
                Log.i("****", "ADRESS");
                this.search_field.setText(null);
                this.search_bar.setVisibility(0);
                this.search_bar.bringToFront();
                this.search_field.setHint(C0984R.string.aa_ID000096);
                this.search.performClick();
                this.actionBar.showSettingsButton();
                this.currentSearchMode = SEARCH_MODE.ADDRESS;
                return;
            case C0984R.id.pilots_flight /*2131361950*/:
                Log.i("****", "pilots_flight");
                this.search_field.setText(null);
                this.search_bar.setVisibility(0);
                this.search_bar.bringToFront();
                this.search_field.setHint(C0984R.string.aa_ID000095);
                this.search.performClick();
                this.actionBar.hideSettingsButton();
                this.currentSearchMode = SEARCH_MODE.PILOT;
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.academy_map);
        setUpMapIfNeeded();
        double doubleExtra = getIntent().getDoubleExtra(EXTRA_POSITION_OBJ_LAT, 0.0d);
        double doubleExtra2 = getIntent().getDoubleExtra(EXTRA_POSITION_OBJ_LNG, 0.0d);
        if (!(doubleExtra == 0.0d && doubleExtra2 == 0.0d)) {
            this.position = new LatLng(doubleExtra, doubleExtra2);
        }
        initActionBar();
        initUI();
        initMap();
        if (this.position != null) {
            initFlightPosition();
        } else {
            this.geocoder = new Geocoder(this);
            initClusters();
            getPrefs();
            initMenu();
            initSettings();
            initListeners();
            initBitmaps();
            setDisplay();
            initProgress();
        }
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_MAP_ZONE_OPEN);
    }

    protected void onDestroy() {
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_MAP_ZONE_CLOSE);
        if (this.position == null) {
            savePrefs();
        }
        killAllDownloadingTask();
        if (this.getClustersTask != null) {
            this.getClustersTask.cancel(true);
        }
        if (this.progress != null) {
            this.progress.cancel();
            this.progress = null;
        }
        super.onDestroy();
    }

    @SuppressLint({"DefaultLocale"})
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 3 || textView.getText().length() <= 0) {
            return false;
        }
        switch (this.currentSearchMode) {
            case PILOT:
                this.username = textView.getText().toString().toLowerCase();
                SystemUtils.hideKeyboard(this);
                this.getClustersTask = new GetClustersAsyncTask();
                this.getClustersTask.execute(new Void[0]);
                return true;
            case ADDRESS:
                String charSequence = textView.getText().toString();
                SystemUtils.hideKeyboard(this);
                new GetLocationAsyncTask(true).execute(new String[]{charSequence});
                return true;
            default:
                return true;
        }
    }

    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged()");
        if (!location.hasAccuracy() || location.getAccuracy() >= 100.0f) {
            Log.d(TAG, "Skipped location value as it doesn't have desired accuracy. Accuracy: " + location.getAccuracy() + " meters");
            Log.d(TAG, "Skipped location value as it doesn't have desired accuracy. Accuracy:hasAltitude " + location.hasAltitude());
            Log.d(TAG, "Skipped location value as it doesn't have desired accuracy. Accuracy:hasAccuracy " + location.hasAccuracy());
            Log.d(TAG, "Skipped location value as it doesn't have desired accuracy. Accuracy:provider " + location.getProvider());
            return;
        }
        this.location = location;
        GPSHelper.getInstance(this).stopListening(this);
        requestGPSUpdate();
    }

    public boolean onMarkerClick(Marker marker) {
        if (this.position == null) {
            if (marker.getTitle() != null) {
                if (this.username == null) {
                    switch (this.currentViewMode) {
                        case ALL_FLIGHTS:
                            this.dialogFlights = new FlightsClusterListDialog(this, this.onFlightsDialogClosed, this.currentViewMode, marker.getTitle(), getZoomRank(), this.currentMediaMode);
                            this.dialogFlights.show();
                            break;
                        case MY_FLIGHTS:
                            HashMap hashMap = (HashMap) this.myFlightsClusters.get(getZoomRank());
                            if (hashMap != null) {
                                FlightsCluster flightsCluster = (FlightsCluster) hashMap.get(marker.getTitle());
                                if (flightsCluster != null) {
                                    ArrayList flights;
                                    switch (this.currentMediaMode) {
                                        case ALL:
                                            flights = flightsCluster.getFlights();
                                            break;
                                        default:
                                            flights = flightsCluster.getFlights(this.currentMediaMode);
                                            break;
                                    }
                                    if (flights != null) {
                                        if (flights.size() <= 1) {
                                            AcademyFlightDetailsActivity.start(this, (Flight) flights.get(0));
                                            break;
                                        }
                                        this.dialogFlights = new FlightsClusterListDialog(this, this.onFlightsDialogClosed, this.currentViewMode, flights);
                                        this.dialogFlights.show();
                                        break;
                                    }
                                }
                            }
                            break;
                        case HOTSPOTS:
                            new HotspotsClusterListDialog(this, marker.getTitle(), getZoomRank()).show();
                            break;
                        default:
                            break;
                    }
                } else if (((HashMap) this.pilotFlightsClusters.get(getZoomRank())).get(marker.getTitle()) != null) {
                    if (((FlightsCluster) ((HashMap) this.pilotFlightsClusters.get(getZoomRank())).get(marker.getTitle())).getFlights().size() > 1) {
                        this.dialogFlights = new FlightsClusterListDialog(this, this.onFlightsDialogClosed, this.currentViewMode, ((FlightsCluster) ((HashMap) this.pilotFlightsClusters.get(getZoomRank())).get(marker.getTitle())).getFlights());
                        this.dialogFlights.show();
                    } else {
                        AcademyFlightDetailsActivity.start(this, (Flight) ((FlightsCluster) ((HashMap) this.pilotFlightsClusters.get(getZoomRank())).get(marker.getTitle())).getFlights().get(0));
                    }
                }
            } else if (marker.getSnippet() != null) {
                MediaVO mediaVO;
                if (this.currentViewMode == VIEW_MODE.MY_FLIGHTS) {
                    mediaVO = new MediaVO();
                    mediaVO.setPath(marker.getSnippet());
                    mediaVO.setUri(Uri.parse(marker.getSnippet()));
                    if (marker.getSnippet().contains("images")) {
                        mediaVO.setVideo(false);
                    } else {
                        mediaVO.setVideo(true);
                    }
                    if (mediaVO.isVideo()) {
                        WatchVideoActivity.start((Context) this, mediaVO, "video/mp4", getTitle().toString(), false);
                    } else {
                        WatchPhotoActivity.start(this, mediaVO, getTitle().toString(), false);
                    }
                } else {
                    mediaVO = new YouTubeMediaVO();
                    mediaVO.setPath(marker.getSnippet());
                    if (marker.getSnippet().contains("google")) {
                        mediaVO.setVideo(false);
                    } else {
                        mediaVO.setVideo(true);
                    }
                    if (!mediaVO.isVideo()) {
                        WatchPhotoActivity.start(this, mediaVO, getTitle().toString(), true);
                    } else if (mediaVO.isRemote()) {
                        WatchYoutubeVideoLegacyActivity.start(this, mediaVO, getTitle().toString());
                    } else {
                        WatchVideoActivity.start((Context) this, mediaVO, "video/mp4", getTitle().toString(), null);
                    }
                }
            }
        }
        return true;
    }

    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        FontUtils.applyFont((Context) this, (ViewGroup) findViewById(16908290));
    }

    public void onProviderDisabled(String str) {
    }

    public void onProviderEnabled(String str) {
    }

    protected void onResume() {
        if (!this.firstOnResume && this.position == null) {
            this.handler.post(this.executeRequest);
        }
        this.firstOnResume = false;
        super.onResume();
        if (this.dialogFlights != null) {
            this.dialogFlights.updateClasterFlights();
        }
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public void requestGPSUpdate() {
        String bestProvider = ((LocationManager) getSystemService("location")).getBestProvider(new Criteria(), false);
        if (this.location != null) {
            if (this.myPositionMarker != null) {
                this.markers.remove(this.myPositionMarker);
                this.myPositionMarker.remove();
            }
            this.myPositionMarker = this.map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(C0984R.drawable.ff2_2_my_position)).position(new LatLng(this.location.getLatitude(), this.location.getLongitude())));
            this.markers.add(this.myPositionMarker);
            if (this.lastPosition.latitude == 0.0d && this.lastPosition.longitude == 0.0d) {
                this.lastPosition = new LatLng(this.location.getLatitude(), this.location.getLongitude());
            }
        }
        Log.i("MAP GPS", "last location = " + this.location + " with : " + bestProvider);
        this.addspot_gps_position.setEnabled(this.location != null);
    }

    protected void showAlertDialog(String str, String str2, final Runnable runnable) {
        this.alertDialog = new Builder(this).setTitle(str).setMessage(str2).setCancelable(false).setNegativeButton(getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).create();
        this.alertDialog.show();
    }

    public void showMapSettings() {
        int i = 4;
        DropDownMenu dropDownMenu = (DropDownMenu) findViewById(C0984R.id.map_menu);
        if (findViewById(C0984R.id.search_details).getVisibility() == 0 && dropDownMenu.getVisibility() == 4) {
            showSearchDetails(findViewById(C0984R.id.search));
        }
        Animation loadAnimation = AnimationUtils.loadAnimation(dropDownMenu.getContext(), C0984R.anim.slide_left_in);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(dropDownMenu.getContext(), C0984R.anim.slide_right_out);
        dropDownMenu.setInAnimation(loadAnimation);
        dropDownMenu.setOutAnimation(loadAnimation2);
        ImageButton imageButton = (ImageButton) findViewById(C0984R.id.btn_right);
        if (dropDownMenu.getVisibility() == 4) {
            i = 0;
        }
        dropDownMenu.setVisibility(i);
        if (dropDownMenu.getVisibility() == 0) {
            dropDownMenu.bringToFront();
            dropDownMenu.requestFocus();
            imageButton.setImageDrawable(getResources().getDrawable(C0984R.drawable.ff2_2_minus_button));
            return;
        }
        imageButton.setImageDrawable(getResources().getDrawable(C0984R.drawable.ff2_2_plus_button));
    }

    public void showSearchDetails(View view) {
        ((CheckedTextView) view).toggle();
        if (this.add_hotspot.isChecked()) {
            this.add_hotspot.toggle();
            this.addspot_details.setVisibility(8);
            this.target.setVisibility(8);
        }
        ((AnimatedView) findViewById(C0984R.id.search_details)).setVisibility(((CheckedTextView) view).isChecked() ? 0 : 8);
        if (((CheckedTextView) view).isChecked() && this.menu.getVisibility() == 0) {
            showMapSettings();
        }
    }
}
