package com.parrot.freeflight.academy.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity;
import com.parrot.freeflight.academy.activities.AcademyMapActivity.MEDIA_MODE;
import com.parrot.freeflight.academy.activities.AcademyMapActivity.VIEW_MODE;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.utils.AcademyFormatUtils;
import com.parrot.freeflight.academy.utils.JSONParser;
import com.parrot.freeflight.utils.FontUtils;
import com.parrot.freeflight.utils.SimpleDelegate;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.mortbay.jetty.HttpVersions;

public class FlightsClusterListDialog extends AlertDialog implements OnClickListener {
    private static final int PAGINATE_BY = 25;
    private FlightsClusterDetailsAdapter adapter;
    private AlertDialog alertDialog;
    private String clusterIdx;
    private final Context context;
    protected ArrayList<Flight> flights;
    protected int flightsOffset;
    protected View footer;
    private ListView list;
    protected MEDIA_MODE mediaMode;
    private final SimpleDelegate onDismissListener;
    protected ProgressDialog progress;
    private RadioButton radioDate;
    private RadioButton radioRank;
    private SortOrder sortOrder;
    protected VIEW_MODE viewMode;
    private int zoomRank;

    class C10391 implements OnClickListener {
        C10391() {
        }

        public void onClick(View view) {
            FlightsClusterListDialog.this.dismiss();
        }
    }

    class C10402 implements OnClickListener {
        C10402() {
        }

        public void onClick(View view) {
            ((ProgressBar) FlightsClusterListDialog.this.findViewById(C0984R.id.loadingProgressBar)).setVisibility(0);
            new GetFlightsClustersListAsyncTask(false, FlightsClusterListDialog.this.flights.size(), 25, 0, 0).execute(new String[]{FlightsClusterListDialog.this.clusterIdx});
        }
    }

    class C10413 implements OnItemClickListener {
        C10413() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            AcademyFlightDetailsActivity.start(FlightsClusterListDialog.this.context, (Flight) adapterView.getItemAtPosition(i));
        }
    }

    static class ClusterDetailHolder {
        TextView date;
        ImageView gps;
        TextView photos_count;
        RatingBar rating;
        TextView title;
        TextView videos_count;

        ClusterDetailHolder() {
        }
    }

    private static final class FlightDateAscComparator implements Comparator<Flight> {
        private FlightDateAscComparator() {
        }

        public int compare(Flight flight, Flight flight2) {
            Date dateTimeAsDate = flight.getDateTimeAsDate();
            Date dateTimeAsDate2 = flight2.getDateTimeAsDate();
            return dateTimeAsDate.equals(dateTimeAsDate2) ? 0 : dateTimeAsDate.before(dateTimeAsDate2) ? -1 : 1;
        }
    }

    private static final class FlightDateDescComparator implements Comparator<Flight> {
        private FlightDateDescComparator() {
        }

        public int compare(Flight flight, Flight flight2) {
            Date dateTimeAsDate = flight.getDateTimeAsDate();
            Date dateTimeAsDate2 = flight2.getDateTimeAsDate();
            return dateTimeAsDate.equals(dateTimeAsDate2) ? 0 : dateTimeAsDate.before(dateTimeAsDate2) ? 1 : -1;
        }
    }

    private static final class FlightRankAscComparator implements Comparator<Flight> {
        private FlightRankAscComparator() {
        }

        public int compare(Flight flight, Flight flight2) {
            int grade = flight.getGrade();
            int grade2 = flight2.getGrade();
            return grade == grade2 ? 0 : grade > grade2 ? 1 : -1;
        }
    }

    private static final class FlightRankDescComparator implements Comparator<Flight> {
        private FlightRankDescComparator() {
        }

        public int compare(Flight flight, Flight flight2) {
            int grade = flight.getGrade();
            int grade2 = flight2.getGrade();
            return grade == grade2 ? 0 : grade > grade2 ? -1 : 1;
        }
    }

    public class FlightsClusterDetailsAdapter extends BaseAdapter {
        Context context;
        private Comparator<Flight> currComparator;
        int layoutResourceId;

        public FlightsClusterDetailsAdapter(Context context, int i, ArrayList<Flight> arrayList) {
            this.layoutResourceId = i;
            this.context = context;
        }

        private void onSortData() {
            Collections.sort(FlightsClusterListDialog.this.flights, this.currComparator);
            notifyDataSetChanged();
        }

        protected void addAll(ArrayList<Flight> arrayList) {
            FlightsClusterListDialog.this.flights.addAll(arrayList);
            notifyDataSetChanged();
        }

        public int getCount() {
            return FlightsClusterListDialog.this.flights.size();
        }

        public Object getItem(int i) {
            return FlightsClusterListDialog.this.flights.get(i);
        }

        public long getItemId(int i) {
            return (long) ((Flight) FlightsClusterListDialog.this.flights.get(i)).getId();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ClusterDetailHolder clusterDetailHolder;
            int size;
            int i2 = 0;
            if (view == null) {
                view = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, viewGroup, false);
                FontUtils.applyFont(FlightsClusterListDialog.this.getContext(), view);
                clusterDetailHolder = new ClusterDetailHolder();
                clusterDetailHolder.title = (TextView) view.findViewById(C0984R.id.title);
                clusterDetailHolder.date = (TextView) view.findViewById(C0984R.id.date);
                clusterDetailHolder.rating = (RatingBar) view.findViewById(C0984R.id.ratingbar_rating);
                view.setTag(clusterDetailHolder);
                clusterDetailHolder.photos_count = (TextView) view.findViewById(C0984R.id.photos_count);
                clusterDetailHolder.videos_count = (TextView) view.findViewById(C0984R.id.videos_count);
                clusterDetailHolder.gps = (ImageView) view.findViewById(C0984R.id.academy_map_details_row_imageview_gps);
            } else {
                clusterDetailHolder = (ClusterDetailHolder) view.getTag();
            }
            Flight flight = (Flight) FlightsClusterListDialog.this.flights.get(i);
            clusterDetailHolder.title.setText(String.format(this.context.getString(C0984R.string.aa_ID000093), new Object[]{Integer.valueOf(flight.getId()), flight.getUser().getUsername()}).toUpperCase());
            clusterDetailHolder.date.setText(flight.getDateTimeAsString() + " (" + AcademyFormatUtils.timeFromSeconds(flight.getTotalFlightTime()) + ")");
            clusterDetailHolder.rating.setProgress(flight.getGrade());
            clusterDetailHolder.gps.setImageResource(flight.isGpsAvailable() ? C0984R.drawable.ff2_gps_selected : C0984R.drawable.ff2_gps_not_selected);
            switch (FlightsClusterListDialog.this.viewMode) {
                case ALL_FLIGHTS:
                    i2 = flight.getFlightCaptureSet().size();
                    size = flight.getFlightVideoSet().size();
                    break;
                case MY_FLIGHTS:
                    i2 = flight.getLocalCaptureSet().size();
                    size = flight.getLocalVideoSet().size();
                    break;
                default:
                    size = 0;
                    break;
            }
            clusterDetailHolder.photos_count.setText(Integer.toString(i2));
            clusterDetailHolder.videos_count.setText(Integer.toString(size));
            return view;
        }

        public void setSortOrder(SortOrder sortOrder) {
            switch (sortOrder) {
                case DATE_ASC:
                    this.currComparator = new FlightDateAscComparator();
                    break;
                case DATE_DESC:
                    this.currComparator = new FlightDateDescComparator();
                    break;
                case RANK_ASC:
                    this.currComparator = new FlightRankAscComparator();
                    break;
                case RANK_DESC:
                    this.currComparator = new FlightRankDescComparator();
                    break;
                default:
                    Log.w("sort", "Unknown sort order " + sortOrder.name());
                    break;
            }
            onSortData();
        }
    }

    private class GetFlightsClustersListAsyncTask extends AsyncTask<String, Void, ArrayList<Flight>> {
        private final int count;
        private final boolean fullUpdate;
        private final int offset;
        private final int selectionIndex;
        private final int selectionTop;

        public GetFlightsClustersListAsyncTask(boolean z, int i, int i2, int i3, int i4) {
            this.fullUpdate = z;
            this.offset = i;
            this.count = i2;
            this.selectionIndex = i3;
            this.selectionTop = i4;
        }

        protected ArrayList<Flight> doInBackground(String... strArr) {
            Collection jsonArrToFlights;
            Object obj;
            ArrayList arrayList = new ArrayList();
            String str = strArr[0];
            try {
                URL url = new URL(FlightsClusterListDialog.this.context.getString(C0984R.string.http).concat(FlightsClusterListDialog.this.context.getString(C0984R.string.url_server)).concat(String.format(FlightsClusterListDialog.this.context.getString(C0984R.string.url_flights_clusters_details), new Object[]{str, Integer.valueOf(FlightsClusterListDialog.this.zoomRank)})).concat(getOffsetCount()).concat(getMediaFilter()));
                Log.d("URL : ", url.toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                jsonArrToFlights = Flight.jsonArrToFlights(new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream())));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                obj = arrayList;
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            } catch (JSONException e3) {
                e3.printStackTrace();
                obj = arrayList;
            }
            return new ArrayList(jsonArrToFlights);
        }

        protected String getMediaFilter() {
            switch (FlightsClusterListDialog.this.mediaMode) {
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

        protected String getOffsetCount() {
            return "?offset=" + this.offset + "&count=" + this.count;
        }

        protected void onPostExecute(ArrayList<Flight> arrayList) {
            super.onPostExecute(arrayList);
            if (this.fullUpdate) {
                if (arrayList != null) {
                    FlightsClusterListDialog.this.flights = new ArrayList(arrayList);
                    if (FlightsClusterListDialog.this.flights.size() > 1) {
                        FlightsClusterListDialog.this.setContentView(C0984R.layout.academy_map_flights_details);
                        if (arrayList.size() < 25) {
                            FlightsClusterListDialog.this.initListView(false);
                        } else {
                            FlightsClusterListDialog.this.initListView(true);
                        }
                        FlightsClusterListDialog.this.initBottomBar();
                        FlightsClusterListDialog.this.initCloseButton();
                        FlightsClusterListDialog.this.list.setSelectionFromTop(this.selectionIndex, this.selectionTop);
                    } else if (FlightsClusterListDialog.this.flights.size() == 1) {
                        AcademyFlightDetailsActivity.start(FlightsClusterListDialog.this.context, (Flight) FlightsClusterListDialog.this.flights.get(0));
                        FlightsClusterListDialog.this.dismiss();
                    } else {
                        Log.e("GetFlightsClustersListAsyncTask", "The flight array is empty");
                    }
                } else {
                    FlightsClusterListDialog.this.showAlertDialog(FlightsClusterListDialog.this.context.getString(C0984R.string.aa_ID000099), FlightsClusterListDialog.this.context.getString(C0984R.string.aa_ID000082), null);
                }
                FlightsClusterListDialog.this.progress.dismiss();
                return;
            }
            if (arrayList != null) {
                if (arrayList.size() < 25) {
                    FlightsClusterListDialog.this.list.removeFooterView(FlightsClusterListDialog.this.footer);
                }
                FlightsClusterListDialog.this.adapter.addAll(arrayList);
                FlightsClusterListDialog flightsClusterListDialog = FlightsClusterListDialog.this;
                flightsClusterListDialog.flightsOffset += arrayList.size();
            } else {
                FlightsClusterListDialog.this.showAlertDialog(FlightsClusterListDialog.this.context.getString(C0984R.string.aa_ID000099), FlightsClusterListDialog.this.context.getString(C0984R.string.aa_ID000082), null);
            }
            ((ProgressBar) FlightsClusterListDialog.this.findViewById(C0984R.id.loadingProgressBar)).setVisibility(4);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (this.fullUpdate) {
                FlightsClusterListDialog.this.progress.show();
                FlightsClusterListDialog.this.progress.setContentView(C0984R.layout.academy_progress_dialog);
            }
        }
    }

    private enum SortOrder {
        DATE_ASC,
        DATE_DESC,
        RANK_ASC,
        RANK_DESC
    }

    public FlightsClusterListDialog(Context context, SimpleDelegate simpleDelegate, VIEW_MODE view_mode, String str, int i, MEDIA_MODE media_mode) {
        this(context, simpleDelegate, view_mode, null);
        this.flights = null;
        this.clusterIdx = str;
        this.zoomRank = i;
        this.mediaMode = media_mode;
        setCanceledOnTouchOutside(true);
    }

    public FlightsClusterListDialog(Context context, SimpleDelegate simpleDelegate, VIEW_MODE view_mode, ArrayList<Flight> arrayList) {
        super(context, 16973841);
        this.footer = null;
        this.flightsOffset = 0;
        this.context = context;
        this.onDismissListener = simpleDelegate;
        this.viewMode = view_mode;
        this.flights = arrayList;
        setCanceledOnTouchOutside(true);
    }

    protected void initBottomBar() {
        this.radioDate = (RadioButton) findViewById(C0984R.id.academy_map_radio_date);
        this.radioRank = (RadioButton) findViewById(C0984R.id.academy_map_radio_rank);
        this.radioDate.setOnClickListener(this);
        this.radioRank.setOnClickListener(this);
        setSortOrder(SortOrder.DATE_DESC);
        if (this.sortOrder.compareTo(SortOrder.RANK_ASC) == 0 || this.sortOrder.compareTo(SortOrder.RANK_DESC) == 0) {
            this.radioRank.setChecked(true);
        }
        setSortOrder(this.sortOrder);
    }

    protected void initCloseButton() {
        ((ImageButton) findViewById(C0984R.id.close)).setOnClickListener(new C10391());
    }

    protected void initListView(boolean z) {
        this.list = (ListView) findViewById(C0984R.id.flights_list);
        this.adapter = new FlightsClusterDetailsAdapter(this.context, C0984R.layout.academy_map_details_row, this.flights);
        if (z) {
            this.footer = ((Activity) this.context).getLayoutInflater().inflate(C0984R.layout.academy_map_details_footer, this.list, false);
            ((TextView) this.footer.findViewById(C0984R.id.moreTextView)).setText(this.context.getString(C0984R.string.aa_ID000045));
            this.footer.setOnClickListener(new C10402());
            this.list.addFooterView(this.footer);
        }
        this.list.setAdapter(this.adapter);
        this.list.setOnItemClickListener(new C10413());
        FontUtils.applyFont(getContext(), this.list);
        this.list.invalidate();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.academy_map_radio_date /*2131361878*/:
                if (this.sortOrder.compareTo(SortOrder.DATE_ASC) == 0) {
                    setSortOrder(SortOrder.DATE_DESC);
                    return;
                } else if (this.sortOrder.compareTo(SortOrder.DATE_DESC) == 0) {
                    setSortOrder(SortOrder.DATE_ASC);
                    return;
                } else {
                    setSortOrder(SortOrder.DATE_DESC);
                    return;
                }
            case C0984R.id.academy_map_radio_rank /*2131361879*/:
                if (this.sortOrder.compareTo(SortOrder.RANK_ASC) == 0) {
                    setSortOrder(SortOrder.RANK_DESC);
                    return;
                } else if (this.sortOrder.compareTo(SortOrder.RANK_DESC) == 0) {
                    setSortOrder(SortOrder.RANK_ASC);
                    return;
                } else {
                    setSortOrder(SortOrder.RANK_DESC);
                    return;
                }
            default:
                Toast.makeText(this.context, "Not yet implemented", 0).show();
                return;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.progress = new ProgressDialog(this.context);
        this.progress.setCancelable(false);
        if (this.flights == null) {
            new GetFlightsClustersListAsyncTask(true, 0, 25, 0, 0).execute(new String[]{this.clusterIdx});
        } else if (this.flights.size() > 1) {
            setContentView(C0984R.layout.academy_map_flights_details);
            initListView(false);
            initBottomBar();
            initCloseButton();
            FontUtils.applyFont(getContext(), findViewById(16908290));
        } else {
            AcademyFlightDetailsActivity.start(this.context, (Flight) this.flights.get(0));
        }
        getWindow().setLayout(-1, -1);
    }

    protected void onStop() {
        super.onStop();
        if (this.onDismissListener != null) {
            this.onDismissListener.call();
        }
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        super.setOnShowListener(onShowListener);
        getWindow().setSoftInputMode(5);
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        this.adapter.setSortOrder(sortOrder);
        if (!(this.radioDate == null || this.radioRank == null)) {
            switch (sortOrder) {
                case DATE_ASC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.map_sort_indicator_asc, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    break;
                case DATE_DESC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.map_sort_indicator_desc, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    break;
                case RANK_ASC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.map_sort_indicator_asc, 0);
                    break;
                case RANK_DESC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.map_sort_indicator_desc, 0);
                    break;
                default:
                    Log.w("clusterdialog", "Unknown sort order");
                    break;
            }
        }
        this.list.invalidate();
    }

    protected void showAlertDialog(String str, String str2, final Runnable runnable) {
        this.alertDialog = new Builder(this.context).setTitle(str).setMessage(str2).setCancelable(false).setNegativeButton(this.context.getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).create();
        this.alertDialog.show();
    }

    public void updateClasterFlights() {
        if (this.flights == null) {
            int firstVisiblePosition = this.list.getFirstVisiblePosition();
            View childAt = this.list.getChildAt(0);
            new GetFlightsClustersListAsyncTask(true, 0, this.flights.size(), firstVisiblePosition, childAt == null ? 0 : childAt.getTop()).execute(new String[]{this.clusterIdx});
        }
    }
}
