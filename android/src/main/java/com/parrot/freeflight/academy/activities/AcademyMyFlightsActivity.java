package com.parrot.freeflight.academy.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.api.Academy;
import com.parrot.freeflight.academy.model.AcademyCredentials;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.utils.AcademyFormatUtils;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.ARDroneMediaGallery;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

public class AcademyMyFlightsActivity extends ParrotActivity implements OnItemClickListener, OnClickListener, OnRefreshListener<ListView> {
    private static final int ITEMS_ON_PAGE = 25;
    private static final String PREF_FLIGHTS_DATE_END = "flights.date.end";
    private static final String PREF_FLIGHTS_DATE_START = "flights.date.start";
    private static final String PREF_SORT_ORDER = "flights.sortorder.name";
    private static final int REQUEST_DETAILS = 1;
    private static final String TAG = AcademyMyFlightsActivity.class.getSimpleName();
    private ActionBar actionBar;
    private List<DeleteFlightTask> activeDeleteTasks;
    private Date dateEnd;
    private Date dateStart;
    private List<Flight> flightIdsToDelete;
    private List<Flight> flights;
    private GetFlightsTask getFlightsTask;
    private ListView listView;
    private ARDroneMediaGallery mediaGallery;
    private Mode mode;
    private MyFlightsAdapter myFlightsAdapter;
    private int page = 1;
    private PullToRefreshListView pullToRefreshView;
    private RadioButton radioDate;
    private RadioButton radioRank;
    private SortOrder sortOrder;
    private View viewLoadMore;

    class C10161 implements OnClickListener {
        C10161() {
        }

        public void onClick(View view) {
            ((ProgressBar) AcademyMyFlightsActivity.this.findViewById(C0984R.id.myFlightsProgressBar)).setVisibility(0);
            AcademyMyFlightsActivity.this.onLoadNextPage();
        }
    }

    private class GetFlightsTask extends AsyncTask<Integer, Integer, List<Flight>> {
        private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);

        protected List<Flight> doInBackground(Integer... numArr) {
            try {
                return Academy.getFlights(AcademyMyFlightsActivity.this, new AcademyCredentials(AcademyUtils.login, AcademyUtils.password), AcademyMyFlightsActivity.this.dateStart, AcademyMyFlightsActivity.this.dateEnd, numArr[0].intValue(), numArr[1].intValue());
            } catch (ClientProtocolException e) {
                Log.w(AcademyMyFlightsActivity.TAG, "Unable to get flights. Due to " + e.toString());
                return Collections.emptyList();
            } catch (IOException e2) {
                e2.printStackTrace();
                return Collections.emptyList();
            } catch (JSONException e3) {
                e3.printStackTrace();
                return Collections.emptyList();
            }
        }

        protected void onPostExecute(List<Flight> list) {
            if (list != null) {
                AcademyMyFlightsActivity.this.flights = list;
                AcademyMyFlightsActivity.this.pullToRefreshView.onRefreshComplete();
                AcademyMyFlightsActivity.this.myFlightsAdapter.setData(list);
                AcademyMyFlightsActivity.this.hideLoadingProgress();
                AcademyMyFlightsActivity.this.setFlightListVisible(true);
                AcademyMyFlightsActivity.this.actionBar.showSettingsButton();
                AcademyMyFlightsActivity.this.setMode(Mode.BROWSE);
            }
        }

        protected void onPreExecute() {
            if (AcademyMyFlightsActivity.this.flights == null || AcademyMyFlightsActivity.this.flights.size() == 0) {
                AcademyMyFlightsActivity.this.hideErrorMessage();
                AcademyMyFlightsActivity.this.showLoadingProgress();
            }
        }
    }

    class C10172 extends GetFlightsTask {
        C10172() {
            super();
        }

        protected void onPostExecute(List<Flight> list) {
            super.onPostExecute((List) list);
            if (list == null || list.size() != AcademyMyFlightsActivity.this.page * 25) {
                AcademyMyFlightsActivity.this.viewLoadMore.setVisibility(8);
            } else {
                AcademyMyFlightsActivity.this.viewLoadMore.setVisibility(0);
            }
        }
    }

    class C10183 extends GetFlightsTask {
        C10183() {
            super();
        }

        protected void onPostExecute(List<Flight> list) {
            if (list != null) {
                ((ProgressBar) AcademyMyFlightsActivity.this.findViewById(C0984R.id.myFlightsProgressBar)).setVisibility(4);
                if (list.size() == 25) {
                    AcademyMyFlightsActivity.this.viewLoadMore.setVisibility(0);
                } else {
                    AcademyMyFlightsActivity.this.viewLoadMore.setVisibility(8);
                }
                AcademyMyFlightsActivity.this.flights.addAll(list);
                AcademyMyFlightsActivity.this.myFlightsAdapter.appendData(list);
                AcademyMyFlightsActivity.this.hideLoadingProgress();
                AcademyMyFlightsActivity.this.setFlightListVisible(true);
                return;
            }
            AcademyMyFlightsActivity.this.viewLoadMore.setVisibility(8);
        }
    }

    private class DeleteFlightTask extends AsyncTask<Integer, Integer, Integer> {
        private DeleteFlightTask() {
        }

        protected Integer doInBackground(Integer... numArr) {
            Object obj = numArr[0];
            HttpClient defaultHttpClient = new DefaultHttpClient();
            StringBuilder stringBuilder = new StringBuilder();
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(0, stringBuilder.length());
            }
            String stringBuilder2 = stringBuilder.append(AcademyMyFlightsActivity.this.getString(C0984R.string.http)).append(AcademyMyFlightsActivity.this.getString(C0984R.string.url_server)).append(AcademyMyFlightsActivity.this.getString(C0984R.string.url_flights)).append(obj).append('/').toString();
            Log.d(AcademyMyFlightsActivity.TAG, "Delete Flight URL: " + stringBuilder2);
            try {
                HttpUriRequest httpDelete = new HttpDelete(stringBuilder2);
                httpDelete.setHeader("Authorization", "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2)));
                HttpResponse execute = defaultHttpClient.execute(httpDelete);
                HttpEntity entity = execute.getEntity();
                int statusCode = execute.getStatusLine().getStatusCode();
                if (entity != null) {
                    Log.d(AcademyMyFlightsActivity.TAG, "Response code: " + statusCode + ", Body: " + EntityUtils.toString(entity));
                } else {
                    Log.d(AcademyMyFlightsActivity.TAG, "Delete flight " + obj + ": " + statusCode);
                }
                return Integer.valueOf(statusCode);
            } catch (ClientProtocolException e) {
                Log.w(AcademyMyFlightsActivity.TAG, "Unable to delete flight " + obj + " due to " + e.toString());
                return Integer.valueOf(-1);
            } catch (IOException e2) {
                e2.printStackTrace();
                return Integer.valueOf(-1);
            }
        }

        protected void onPostExecute(Integer num) {
            AcademyMyFlightsActivity.this.myFlightsAdapter.setData(AcademyMyFlightsActivity.this.flights);
            AcademyMyFlightsActivity.this.setFlightListVisible(true);
            AcademyMyFlightsActivity.this.hideLoadingProgress();
            AcademyMyFlightsActivity.this.actionBar.showSettingsButton();
            AcademyMyFlightsActivity.this.setMode(Mode.BROWSE);
        }
    }

    private static final class FlightDateAscComparator implements Comparator<ListItemVO> {
        private FlightDateAscComparator() {
        }

        public int compare(ListItemVO listItemVO, ListItemVO listItemVO2) {
            Date dateTimeAsDate = listItemVO.flight.getDateTimeAsDate();
            Date dateTimeAsDate2 = listItemVO2.flight.getDateTimeAsDate();
            return dateTimeAsDate.equals(dateTimeAsDate2) ? 0 : dateTimeAsDate.before(dateTimeAsDate2) ? -1 : 1;
        }
    }

    private static final class FlightDateDescComparator implements Comparator<ListItemVO> {
        private FlightDateDescComparator() {
        }

        public int compare(ListItemVO listItemVO, ListItemVO listItemVO2) {
            Date dateTimeAsDate = listItemVO.flight.getDateTimeAsDate();
            Date dateTimeAsDate2 = listItemVO2.flight.getDateTimeAsDate();
            return dateTimeAsDate.equals(dateTimeAsDate2) ? 0 : dateTimeAsDate.before(dateTimeAsDate2) ? 1 : -1;
        }
    }

    private static final class FlightRankAscComparator implements Comparator<ListItemVO> {
        private FlightRankAscComparator() {
        }

        public int compare(ListItemVO listItemVO, ListItemVO listItemVO2) {
            int grade = listItemVO.flight.getGrade();
            int grade2 = listItemVO2.flight.getGrade();
            return grade == grade2 ? 0 : grade > grade2 ? 1 : -1;
        }
    }

    private static final class FlightRankDescComparator implements Comparator<ListItemVO> {
        private FlightRankDescComparator() {
        }

        public int compare(ListItemVO listItemVO, ListItemVO listItemVO2) {
            int grade = listItemVO.flight.getGrade();
            int grade2 = listItemVO2.flight.getGrade();
            return grade == grade2 ? 0 : grade > grade2 ? -1 : 1;
        }
    }

    final class ListItemVO {
        public Flight flight;
        public String flightCrashesCache;
        public String flightTimeCache;
        public String flightTitleCache;
        public String photosCountCache;
        public String videosCountCache;

        ListItemVO() {
        }
    }

    private enum Mode {
        BROWSE,
        EDIT
    }

    private final class MyFlightsAdapter extends BaseAdapter {
        private Comparator<ListItemVO> currComparator;
        private List<ListItemVO> data = Collections.emptyList();
        private DecimalFormat decimalFormat;

        public MyFlightsAdapter() {
            setSortOrder(SortOrder.DATE_DESC);
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setGroupingSeparator(' ');
            this.decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
            this.decimalFormat.setGroupingUsed(true);
            this.decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        }

        private void cacheListDisplayData(ListItemVO listItemVO) {
            Flight flight = listItemVO.flight;
            listItemVO.flightTitleCache = String.format(AcademyMyFlightsActivity.this.getString(C0984R.string.aa_ID000040), new Object[]{this.decimalFormat.format((long) flight.getId())});
            listItemVO.flightTimeCache = DateFormat.getDateTimeInstance(3, 2).format(flight.getDateTimeAsDate());
            listItemVO.flightCrashesCache = String.format(AcademyMyFlightsActivity.this.getString(C0984R.string.aa_ID000041), new Object[]{AcademyFormatUtils.timeFromSeconds(flight.getFlightTime()), Integer.valueOf(flight.getCrash())});
            String flightTag = flight.getFlightTag();
            int countOfPhotos = AcademyMyFlightsActivity.this.mediaGallery.countOfPhotos(flightTag);
            int countOfVideos = AcademyMyFlightsActivity.this.mediaGallery.countOfVideos(flightTag);
            listItemVO.photosCountCache = Integer.toString(countOfPhotos);
            listItemVO.videosCountCache = Integer.toString(countOfVideos);
        }

        private void onSortData() {
            Collections.sort(this.data, this.currComparator);
        }

        public void appendData(List<Flight> list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ListItemVO listItemVO = new ListItemVO();
                listItemVO.flight = (Flight) list.get(i);
                this.data.add(listItemVO);
                cacheListDisplayData(listItemVO);
            }
            onSortData();
            notifyDataSetChanged();
        }

        public int getCount() {
            return this.data.size();
        }

        public Object getItem(int i) {
            return this.data.get(i);
        }

        public long getItemId(int i) {
            return (long) ((ListItemVO) this.data.get(i)).flight.getId();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = AcademyMyFlightsActivity.this.inflateView(C0984R.layout.view_my_flights_item, viewGroup, false);
            }
            ViewHolder viewHolder2 = (ViewHolder) view.getTag();
            if (viewHolder2 == null) {
                viewHolder = new ViewHolder();
                viewHolder.flightView = (TextView) view.findViewById(C0984R.id.view_my_flights_label_flight);
                viewHolder.timeCrashesView = (TextView) view.findViewById(C0984R.id.view_my_flights_label_time_crashes);
                viewHolder.photosView = (TextView) view.findViewById(C0984R.id.view_my_flights_label_photos);
                viewHolder.videoView = (TextView) view.findViewById(C0984R.id.view_my_flights_label_videos);
                viewHolder.dateView = (TextView) view.findViewById(C0984R.id.view_my_flights_label_date);
                viewHolder.ratingBar = (RatingBar) view.findViewById(C0984R.id.view_my_flights_ratingbar_rating);
                viewHolder.checkDelete = (CheckBox) view.findViewById(C0984R.id.view_my_flights_checkbox_delete);
                viewHolder.gpsAvailable = (ImageView) view.findViewById(C0984R.id.view_my_flights_imageview_gps);
            } else {
                viewHolder = viewHolder2;
            }
            ListItemVO listItemVO = (ListItemVO) this.data.get(i);
            Flight flight = listItemVO.flight;
            viewHolder.flightView.setText(listItemVO.flightTitleCache);
            viewHolder.timeCrashesView.setText(listItemVO.flightCrashesCache);
            viewHolder.dateView.setText(listItemVO.flightTimeCache);
            viewHolder.photosView.setText(listItemVO.photosCountCache);
            viewHolder.videoView.setText(listItemVO.videosCountCache);
            viewHolder.ratingBar.setProgress(flight.getGrade());
            viewHolder.gpsAvailable.setImageResource(flight.isGpsAvailable() ? C0984R.drawable.ff2_gps_selected : C0984R.drawable.ff2_gps_selected_grey);
            CheckBox checkBox = viewHolder.checkDelete;
            if (AcademyMyFlightsActivity.this.mode == Mode.EDIT) {
                if (checkBox.getVisibility() != 0) {
                    viewHolder.checkDelete.setVisibility(0);
                }
                boolean isItemChecked = AcademyMyFlightsActivity.this.listView.isItemChecked(i + 1);
                checkBox.setTag(Integer.valueOf(i));
                checkBox.setChecked(isItemChecked);
                if (isItemChecked) {
                    view.setBackgroundColor(AcademyMyFlightsActivity.this.getResources().getColor(C0984R.color.list_item_checked_bg));
                } else {
                    view.setBackgroundResource(0);
                }
            } else {
                if (checkBox.getVisibility() != 8) {
                    viewHolder.checkDelete.setVisibility(8);
                }
                view.setBackgroundResource(0);
            }
            view.setTag(viewHolder);
            return view;
        }

        public void setData(List<Flight> list) {
            int size = list.size();
            if (size == 0) {
                AcademyMyFlightsActivity.this.showErrorMessage(C0984R.string.aa_ID000173);
            } else {
                AcademyMyFlightsActivity.this.hideErrorMessage();
            }
            this.data = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                ListItemVO listItemVO = new ListItemVO();
                listItemVO.flight = (Flight) list.get(i);
                this.data.add(listItemVO);
                cacheListDisplayData(listItemVO);
            }
            onSortData();
            notifyDataSetChanged();
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
                    Log.w(AcademyMyFlightsActivity.TAG, "Unknown sort order " + sortOrder.name());
                    break;
            }
            onSortData();
            notifyDataSetChanged();
        }

        public void updateFlight(Flight flight) {
            int size = this.data.size();
            for (int i = 0; i < size; i++) {
                ListItemVO listItemVO = (ListItemVO) this.data.get(i);
                if (listItemVO.flight.getId() == flight.getId()) {
                    listItemVO.flight = flight;
                    break;
                }
            }
            notifyDataSetChanged();
        }
    }

    private enum SortOrder {
        DATE_ASC,
        DATE_DESC,
        RANK_ASC,
        RANK_DESC
    }

    private static final class ViewHolder {
        CheckBox checkDelete;
        TextView dateView;
        TextView flightView;
        ImageView gpsAvailable;
        TextView photosView;
        RatingBar ratingBar;
        TextView timeCrashesView;
        TextView videoView;

        private ViewHolder() {
        }
    }

    private void cancelAllFlightDeleteTasks() {
        int size = this.activeDeleteTasks.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                DeleteFlightTask deleteFlightTask = (DeleteFlightTask) this.activeDeleteTasks.get(i);
                if (!(deleteFlightTask == null || deleteFlightTask.isCancelled())) {
                    deleteFlightTask.cancel(true);
                }
            }
        }
    }

    private void cancelGetFlightsTask() {
        if (this.getFlightsTask != null) {
            this.getFlightsTask.cancel(true);
        }
    }

    @SuppressLint({"NewApi"})
    private void deleteFlights() {
        int size = this.flightIdsToDelete.size();
        if (size != 0) {
            setFlightListVisible(false);
            showLoadingProgress();
            hideSortBar();
            this.actionBar.hideSettingsButton();
            hideDeleteBar();
            for (int i = 0; i < size; i++) {
                DeleteFlightTask deleteFlightTask = new DeleteFlightTask();
                this.activeDeleteTasks.add(deleteFlightTask);
                Flight flight = (Flight) this.flightIdsToDelete.get(i);
                if (VERSION.SDK_INT >= 11) {
                    deleteFlightTask.executeOnExecutor(DeleteFlightTask.THREAD_POOL_EXECUTOR, new Integer[]{Integer.valueOf(flight.getId())});
                } else {
                    deleteFlightTask.execute(new Integer[]{Integer.valueOf(flight.getId())});
                }
            }
            if (this.flights != null) {
                this.flights.removeAll(this.flightIdsToDelete);
            }
            this.flightIdsToDelete.clear();
        }
    }

    private void hideLoadingProgress() {
        findViewById(C0984R.id.activity_academy_my_flights_progress).setVisibility(8);
    }

    private void initBottomBar() {
        this.radioDate = (RadioButton) findViewById(C0984R.id.activity_academy_my_flights_radio_date);
        this.radioRank = (RadioButton) findViewById(C0984R.id.activity_academy_my_flights_radio_rank);
        this.radioDate.setOnClickListener(this);
        this.radioRank.setOnClickListener(this);
        if (this.sortOrder.compareTo(SortOrder.RANK_ASC) == 0 || this.sortOrder.compareTo(SortOrder.RANK_DESC) == 0) {
            this.radioRank.setChecked(true);
        }
        setSortOrder(this.sortOrder);
        findViewById(C0984R.id.activity_academy_my_flights_button_delete).setOnClickListener(this);
        findViewById(C0984R.id.activity_academy_my_flights_button_cancel).setOnClickListener(this);
        findViewById(C0984R.id.activity_academy_my_flights_button_select_clear).setOnClickListener(this);
        findViewById(C0984R.id.activity_academy_my_flights_button_confirm_delete).setOnClickListener(this);
        hideSortBar();
        hideDeleteBar();
    }

    private void initDateRange() {
        SharedPreferences preferences = getPreferences(0);
        long j = preferences.getLong(PREF_FLIGHTS_DATE_START, 0);
        long j2 = preferences.getLong(PREF_FLIGHTS_DATE_END, 0);
        if (j == 0 || j2 == 0) {
            Calendar instance = Calendar.getInstance();
            j2 = instance.getTimeInMillis();
            instance.add(1, -1);
            j = instance.getTimeInMillis();
        }
        this.dateStart = new Date(j);
        this.dateEnd = new Date(j2);
    }

    private void initFlightList() {
        this.mode = Mode.BROWSE;
        this.mediaGallery = new ARDroneMediaGallery(this);
        this.myFlightsAdapter = new MyFlightsAdapter();
        setSortOrder(SortOrder.valueOf(getPreferences(0).getString(PREF_SORT_ORDER, SortOrder.DATE_DESC.name())));
        this.pullToRefreshView = (PullToRefreshListView) findViewById(C0984R.id.activity_academy_my_flights_list_flights);
        this.pullToRefreshView.setOnRefreshListener((OnRefreshListener) this);
        this.listView = (ListView) this.pullToRefreshView.getRefreshableView();
        this.viewLoadMore = inflateView(C0984R.layout.view_my_flights_list_footer, this.listView, false);
        this.viewLoadMore.setClickable(true);
        this.viewLoadMore.setVisibility(8);
        this.viewLoadMore.setOnClickListener(new C10161());
        this.listView.addFooterView(this.viewLoadMore);
        this.listView.setAdapter(this.myFlightsAdapter);
        this.listView.setOnItemClickListener(this);
    }

    private void initNavigationBar() {
        setTitle((int) C0984R.string.aa_ID000027);
        this.actionBar = getParrotActionBar();
        this.actionBar.initBackButton();
        this.actionBar.initSettingsButton(this);
        this.actionBar.hideSettingsButton();
    }

    private void onCancelClicked(View view) {
        setMode(Mode.BROWSE);
    }

    private void onDeleteClicked(View view) {
        setMode(Mode.EDIT);
    }

    private void onDeleteConfirmClicked(final View view) {
        Builder builder = new Builder(this);
        DialogInterface.OnClickListener c10194 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case -2:
                        AcademyMyFlightsActivity.this.onCancelClicked(view);
                        return;
                    case -1:
                        AcademyMyFlightsActivity.this.deleteFlights();
                        return;
                    default:
                        return;
                }
            }
        };
        builder.setMessage(C0984R.string.aa_ID000044).setPositiveButton(17039379, c10194).setNegativeButton(17039369, c10194).create().show();
    }

    private void onRefreshData() {
        this.getFlightsTask = new C10172();
        this.getFlightsTask.execute(new Integer[]{Integer.valueOf(1), Integer.valueOf(this.page * 25)});
    }

    private void onSaveDateInterval() {
        Editor edit = getPreferences(0).edit();
        edit.putLong(PREF_FLIGHTS_DATE_START, this.dateStart.getTime());
        edit.putLong(PREF_FLIGHTS_DATE_END, this.dateEnd.getTime());
        edit.commit();
    }

    private void onSelectAllClearClicked(Button button) {
        if (button.getText().equals(getString(C0984R.string.aa_ID000032))) {
            setListItemsChecked(true);
            this.flightIdsToDelete.addAll(this.flights);
            button.setText(C0984R.string.aa_ID000042);
            return;
        }
        setListItemsChecked(false);
        this.flightIdsToDelete.clear();
        button.setText(C0984R.string.aa_ID000032);
    }

    private void onSettingsClicked(View view) {
        final DateRangeDialog dateRangeDialog = new DateRangeDialog();
        dateRangeDialog.setDateStart(this.dateStart);
        dateRangeDialog.setDateEnd(this.dateEnd);
        dateRangeDialog.setOnOkClickedListener(new OnClickListener() {
            public void onClick(View view) {
                Date dateStart = dateRangeDialog.getDateStart();
                Date dateEnd = dateRangeDialog.getDateEnd();
                if (!AcademyMyFlightsActivity.this.dateStart.equals(dateStart) || !AcademyMyFlightsActivity.this.dateEnd.equals(dateEnd)) {
                    AcademyMyFlightsActivity.this.dateStart = dateRangeDialog.getDateStart();
                    AcademyMyFlightsActivity.this.dateEnd = dateRangeDialog.getDateEnd();
                    if (!AcademyMyFlightsActivity.this.dateStart.before(AcademyMyFlightsActivity.this.dateEnd)) {
                        AcademyMyFlightsActivity.this.dateEnd = AcademyMyFlightsActivity.this.dateStart;
                        AcademyMyFlightsActivity.this.dateStart = dateRangeDialog.getDateEnd();
                    }
                    AcademyMyFlightsActivity.this.onDateRangeChanged();
                }
            }
        });
        dateRangeDialog.show(getSupportFragmentManager(), "daterange");
    }

    private void setListItemsChecked(boolean z) {
        for (int i = 1; i <= this.flights.size(); i++) {
            this.listView.setItemChecked(i, z);
        }
    }

    private void showLoadingProgress() {
        findViewById(C0984R.id.activity_academy_my_flights_progress).setVisibility(0);
    }

    public void hideDeleteBar() {
        findViewById(C0984R.id.activity_academy_my_flights_bar_delete).setVisibility(8);
    }

    public void hideErrorMessage() {
        ((TextView) findViewById(C0984R.id.activity_academy_my_flights_label_error_msg)).setVisibility(4);
    }

    public void hideSortBar() {
        findViewById(C0984R.id.activity_academy_my_flights_bar_sort).setVisibility(8);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        switch (i) {
            case 1:
                if (intent != null) {
                    this.myFlightsAdapter.updateFlight((Flight) intent.getSerializableExtra(AcademyFlightDetailsActivity.EXTRA_FLIGHT_OBJ));
                    return;
                }
                return;
            default:
                Log.w(TAG, "Unknown activity result.");
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.activity_academy_my_flights_radio_date /*2131362013*/:
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
            case C0984R.id.activity_academy_my_flights_radio_rank /*2131362014*/:
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
            case C0984R.id.activity_academy_my_flights_button_delete /*2131362015*/:
                onDeleteClicked(view);
                return;
            case C0984R.id.activity_academy_my_flights_button_select_clear /*2131362017*/:
                onSelectAllClearClicked((Button) view);
                return;
            case C0984R.id.activity_academy_my_flights_button_confirm_delete /*2131362018*/:
                onDeleteConfirmClicked(view);
                return;
            case C0984R.id.activity_academy_my_flights_button_cancel /*2131362019*/:
                onCancelClicked(view);
                return;
            case C0984R.id.btn_right /*2131362321*/:
                onSettingsClicked(view);
                return;
            default:
                Toast.makeText(this, "Not yet implemented", 0).show();
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_academy_my_flights);
        if (TextUtils.isEmpty(AcademyUtils.login)) {
            AcademyUtils.getCredentials(this);
        }
        this.flightIdsToDelete = new ArrayList();
        this.activeDeleteTasks = new ArrayList();
        initNavigationBar();
        initFlightList();
        initBottomBar();
        initDateRange();
        hideErrorMessage();
        showLoadingProgress();
    }

    protected void onDateRangeChanged() {
        onSaveDateInterval();
        onRefreshData();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        ListItemVO listItemVO = (ListItemVO) this.myFlightsAdapter.getItem(i - 1);
        if (this.mode == Mode.BROWSE) {
            AcademyFlightDetailsActivity.startForResult(this, 1, listItemVO.flight);
        } else if (this.mode == Mode.EDIT) {
            boolean isItemChecked = this.listView.isItemChecked(i);
            Flight flight = listItemVO.flight;
            if (isItemChecked) {
                this.flightIdsToDelete.add(flight);
            } else {
                this.flightIdsToDelete.remove(flight);
            }
            this.myFlightsAdapter.notifyDataSetChanged();
        }
    }

    protected void onLoadNextPage() {
        this.page++;
        this.getFlightsTask = new C10183();
        this.getFlightsTask.execute(new Integer[]{Integer.valueOf(this.page), Integer.valueOf(25)});
    }

    public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        this.pullToRefreshView.setRefreshing();
        onRefreshData();
    }

    protected void onStart() {
        super.onStart();
        onRefreshData();
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_MY_FLIGHTS_ZONE_OPEN);
    }

    protected void onStop() {
        super.onStop();
        cancelGetFlightsTask();
        cancelAllFlightDeleteTasks();
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_MY_FLIGHTS_ZONE_CLOSE);
    }

    public void setFlightListVisible(boolean z) {
        findViewById(C0984R.id.activity_academy_my_flights_list_flights).setVisibility(z ? 0 : 4);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        switch (mode) {
            case EDIT:
                findViewById(C0984R.id.activity_academy_my_flights_bar_delete).setVisibility(0);
                findViewById(C0984R.id.activity_academy_my_flights_bar_sort).setVisibility(8);
                ((Button) findViewById(C0984R.id.activity_academy_my_flights_button_select_clear)).setText(C0984R.string.aa_ID000032);
                this.listView.setChoiceMode(2);
                break;
            case BROWSE:
                findViewById(C0984R.id.activity_academy_my_flights_bar_sort).setVisibility(0);
                findViewById(C0984R.id.activity_academy_my_flights_bar_delete).setVisibility(8);
                this.listView.setChoiceMode(0);
                this.listView.clearChoices();
                break;
        }
        this.myFlightsAdapter.notifyDataSetChanged();
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        this.myFlightsAdapter.setSortOrder(sortOrder);
        if (!(this.radioDate == null || this.radioRank == null)) {
            switch (sortOrder) {
                case DATE_ASC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator_asc, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    break;
                case DATE_DESC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator_desc, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    break;
                case RANK_ASC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator_asc, 0);
                    break;
                case RANK_DESC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator_desc, 0);
                    break;
                default:
                    Log.w(TAG, "Unknown sort order");
                    break;
            }
        }
        Editor edit = getPreferences(0).edit();
        edit.putString(PREF_SORT_ORDER, sortOrder.name());
        edit.commit();
    }

    public void showDeleteBar() {
        findViewById(C0984R.id.activity_academy_my_flights_bar_delete).setVisibility(0);
    }

    public void showErrorMessage(int i) {
        TextView textView = (TextView) findViewById(C0984R.id.activity_academy_my_flights_label_error_msg);
        textView.setVisibility(0);
        textView.setText(C0984R.string.aa_ID000173);
    }

    public void showSortBar() {
        findViewById(C0984R.id.activity_academy_my_flights_bar_sort).setVisibility(0);
    }
}
