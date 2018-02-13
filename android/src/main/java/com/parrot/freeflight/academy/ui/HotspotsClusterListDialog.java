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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.activities.AcademyHotspotDetailsActivity;
import com.parrot.freeflight.academy.model.Hotspot;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.academy.utils.JSONParser;
import com.parrot.freeflight.utils.FontUtils;
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

public class HotspotsClusterListDialog extends AlertDialog implements OnClickListener {
    private static final int PAGINATE_BY = 25;
    private HotspotsClusterDetailsAdapter adapter;
    private AlertDialog alertDialog;
    private String clusterIdx;
    private Context context;
    protected View footer = null;
    private ArrayList<Hotspot> hotspots = null;
    protected int hotspotsOffset = 0;
    private ListView list;
    protected ProgressDialog progress;
    private RadioButton radioDate;
    private RadioButton radioRank;
    private SortOrder sortOrder;
    private int zoomRank;

    class C10441 implements OnClickListener {
        C10441() {
        }

        public void onClick(View view) {
            HotspotsClusterListDialog.this.dismiss();
        }
    }

    class C10452 implements OnClickListener {
        C10452() {
        }

        public void onClick(View view) {
            ((ProgressBar) HotspotsClusterListDialog.this.findViewById(C0984R.id.loadingProgressBar)).setVisibility(0);
            new GetHotspotsClustersListAsyncTask().execute(new String[]{HotspotsClusterListDialog.this.clusterIdx});
        }
    }

    class C10463 implements OnItemClickListener {
        C10463() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            AcademyHotspotDetailsActivity.start(HotspotsClusterListDialog.this.context, (Hotspot) HotspotsClusterListDialog.this.hotspots.get(i));
            if (((Hotspot) HotspotsClusterListDialog.this.hotspots.get(i)).getUser().equals(AcademyUtils.profile.getUser())) {
                HotspotsClusterListDialog.this.dismiss();
            }
        }
    }

    static class ClusterDetailHolder {
        TextView date;
        TextView photos;
        ImageView photosView;
        RatingBar rating;
        ImageView separator1;
        ImageView separator2;
        ImageView separator3;
        TextView title;
        TextView videos;
        ImageView videosView;

        ClusterDetailHolder() {
        }
    }

    private class GetHotspotsClustersListAsyncTask extends AsyncTask<String, Void, ArrayList<Hotspot>> {
        private GetHotspotsClustersListAsyncTask() {
        }

        protected ArrayList<Hotspot> doInBackground(String... strArr) {
            Collection jsonArrToHotspots;
            Object obj;
            ArrayList arrayList = new ArrayList();
            String str = strArr[0];
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(HotspotsClusterListDialog.this.context.getString(C0984R.string.http).concat(HotspotsClusterListDialog.this.context.getString(C0984R.string.url_server)).concat(String.format(HotspotsClusterListDialog.this.context.getString(C0984R.string.url_hotspots_clusters_details), new Object[]{str, Integer.valueOf(HotspotsClusterListDialog.this.zoomRank)})).concat(getOffsetCount())).openConnection();
                httpURLConnection.connect();
                jsonArrToHotspots = Hotspot.jsonArrToHotspots(new JSONArray(JSONParser.readStream(httpURLConnection.getInputStream())));
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
            return new ArrayList(jsonArrToHotspots);
        }

        protected String getOffsetCount() {
            return "?offset=" + HotspotsClusterListDialog.this.hotspotsOffset + "&count=" + 25;
        }

        protected void onPostExecute(ArrayList<Hotspot> arrayList) {
            super.onPostExecute(arrayList);
            if (HotspotsClusterListDialog.this.hotspotsOffset == 0) {
                if (arrayList != null) {
                    HotspotsClusterListDialog.this.hotspots = new ArrayList(arrayList);
                    if (HotspotsClusterListDialog.this.hotspots.size() > 1) {
                        HotspotsClusterListDialog.this.setContentView(C0984R.layout.academy_map_flights_details);
                        if (arrayList.size() < 25) {
                            HotspotsClusterListDialog.this.initListView(false);
                        } else {
                            HotspotsClusterListDialog.this.initListView(true);
                        }
                        HotspotsClusterListDialog.this.initBottomBar();
                        HotspotsClusterListDialog.this.initCloseButton();
                        HotspotsClusterListDialog hotspotsClusterListDialog = HotspotsClusterListDialog.this;
                        hotspotsClusterListDialog.hotspotsOffset += arrayList.size();
                    } else {
                        AcademyHotspotDetailsActivity.start(HotspotsClusterListDialog.this.context, (Hotspot) HotspotsClusterListDialog.this.hotspots.get(0));
                        HotspotsClusterListDialog.this.dismiss();
                    }
                } else {
                    HotspotsClusterListDialog.this.showAlertDialog(HotspotsClusterListDialog.this.context.getString(C0984R.string.aa_ID000099), HotspotsClusterListDialog.this.context.getString(C0984R.string.aa_ID000082), null);
                }
                HotspotsClusterListDialog.this.progress.dismiss();
                return;
            }
            if (arrayList != null) {
                if (arrayList.size() < 25) {
                    HotspotsClusterListDialog.this.list.removeFooterView(HotspotsClusterListDialog.this.footer);
                }
                HotspotsClusterListDialog.this.adapter.addAll(arrayList);
                hotspotsClusterListDialog = HotspotsClusterListDialog.this;
                hotspotsClusterListDialog.hotspotsOffset += arrayList.size();
            } else {
                HotspotsClusterListDialog.this.showAlertDialog(HotspotsClusterListDialog.this.context.getString(C0984R.string.aa_ID000099), HotspotsClusterListDialog.this.context.getString(C0984R.string.aa_ID000082), null);
            }
            ((ProgressBar) HotspotsClusterListDialog.this.findViewById(C0984R.id.loadingProgressBar)).setVisibility(4);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (HotspotsClusterListDialog.this.hotspotsOffset == 0) {
                HotspotsClusterListDialog.this.progress.show();
                HotspotsClusterListDialog.this.progress.setContentView(C0984R.layout.academy_progress_dialog);
            }
        }
    }

    private static final class HotspotDateAscComparator implements Comparator<Hotspot> {
        private HotspotDateAscComparator() {
        }

        public int compare(Hotspot hotspot, Hotspot hotspot2) {
            Date creation_dateAsDate = hotspot.getCreation_dateAsDate();
            Date creation_dateAsDate2 = hotspot2.getCreation_dateAsDate();
            return creation_dateAsDate.equals(creation_dateAsDate2) ? 0 : creation_dateAsDate.before(creation_dateAsDate2) ? -1 : 1;
        }
    }

    private static final class HotspotDateDescComparator implements Comparator<Hotspot> {
        private HotspotDateDescComparator() {
        }

        public int compare(Hotspot hotspot, Hotspot hotspot2) {
            Date creation_dateAsDate = hotspot.getCreation_dateAsDate();
            Date creation_dateAsDate2 = hotspot2.getCreation_dateAsDate();
            return creation_dateAsDate.equals(creation_dateAsDate2) ? 0 : creation_dateAsDate.before(creation_dateAsDate2) ? 1 : -1;
        }
    }

    private static final class HotspotRankAscComparator implements Comparator<Hotspot> {
        private HotspotRankAscComparator() {
        }

        public int compare(Hotspot hotspot, Hotspot hotspot2) {
            int grade = hotspot.getGrade();
            int grade2 = hotspot2.getGrade();
            return grade == grade2 ? 0 : grade > grade2 ? 1 : -1;
        }
    }

    private static final class HotspotRankDescComparator implements Comparator<Hotspot> {
        private HotspotRankDescComparator() {
        }

        public int compare(Hotspot hotspot, Hotspot hotspot2) {
            int grade = hotspot.getGrade();
            int grade2 = hotspot2.getGrade();
            return grade == grade2 ? 0 : grade > grade2 ? -1 : 1;
        }
    }

    public class HotspotsClusterDetailsAdapter extends ArrayAdapter<Hotspot> {
        Context context;
        private Comparator<Hotspot> currComparator;
        int layoutResourceId;

        public HotspotsClusterDetailsAdapter(Context context, int i, ArrayList<Hotspot> arrayList) {
            super(context, i, arrayList);
            this.layoutResourceId = i;
            this.context = context;
        }

        private void onSortData() {
            Collections.sort(HotspotsClusterListDialog.this.hotspots, this.currComparator);
            notifyDataSetChanged();
        }

        protected void addAll(ArrayList<Hotspot> arrayList) {
            HotspotsClusterListDialog.this.hotspots.addAll(arrayList);
            notifyDataSetChanged();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ClusterDetailHolder clusterDetailHolder;
            if (view == null) {
                view = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, viewGroup, false);
                clusterDetailHolder = new ClusterDetailHolder();
                clusterDetailHolder.title = (TextView) view.findViewById(C0984R.id.title);
                clusterDetailHolder.date = (TextView) view.findViewById(C0984R.id.date);
                clusterDetailHolder.rating = (RatingBar) view.findViewById(C0984R.id.ratingbar_rating);
                view.setTag(clusterDetailHolder);
                clusterDetailHolder.photos = (TextView) view.findViewById(C0984R.id.photos_count);
                clusterDetailHolder.videos = (TextView) view.findViewById(C0984R.id.videos_count);
                clusterDetailHolder.photosView = (ImageView) view.findViewById(C0984R.id.photosView);
                clusterDetailHolder.videosView = (ImageView) view.findViewById(C0984R.id.videosView);
                clusterDetailHolder.separator1 = (ImageView) view.findViewById(C0984R.id.separator1);
                clusterDetailHolder.separator2 = (ImageView) view.findViewById(C0984R.id.separator2);
                clusterDetailHolder.separator3 = (ImageView) view.findViewById(C0984R.id.separator3);
            } else {
                clusterDetailHolder = (ClusterDetailHolder) view.getTag();
            }
            Hotspot hotspot = (Hotspot) HotspotsClusterListDialog.this.hotspots.get(i);
            clusterDetailHolder.title.setText(String.format(this.context.getString(C0984R.string.aa_ID000094), new Object[]{hotspot.getName(), hotspot.getUser().getUsername()}).toUpperCase());
            clusterDetailHolder.date.setText(hotspot.getCreation_date());
            clusterDetailHolder.rating.setProgress(hotspot.getGrade());
            Log.d("Grade", String.valueOf(hotspot.getGrade()));
            clusterDetailHolder.photos.setVisibility(8);
            clusterDetailHolder.videos.setVisibility(8);
            clusterDetailHolder.photosView.setVisibility(8);
            clusterDetailHolder.videosView.setVisibility(8);
            clusterDetailHolder.separator1.setVisibility(8);
            clusterDetailHolder.separator2.setVisibility(8);
            clusterDetailHolder.separator3.setVisibility(8);
            return view;
        }

        public void setSortOrder(SortOrder sortOrder) {
            switch (sortOrder) {
                case DATE_ASC:
                    this.currComparator = new HotspotDateAscComparator();
                    break;
                case DATE_DESC:
                    this.currComparator = new HotspotDateDescComparator();
                    break;
                case RANK_ASC:
                    this.currComparator = new HotspotRankAscComparator();
                    break;
                case RANK_DESC:
                    this.currComparator = new HotspotRankDescComparator();
                    break;
                default:
                    Log.w("sort", "Unknown sort order " + sortOrder.name());
                    break;
            }
            onSortData();
        }
    }

    private enum SortOrder {
        DATE_ASC,
        DATE_DESC,
        RANK_ASC,
        RANK_DESC
    }

    public HotspotsClusterListDialog(Context context, String str, int i) {
        super(context);
        this.context = context;
        this.hotspots = null;
        this.clusterIdx = str;
        this.zoomRank = i;
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
        ((ImageButton) findViewById(C0984R.id.close)).setOnClickListener(new C10441());
    }

    protected void initListView(boolean z) {
        this.list = (ListView) findViewById(C0984R.id.flights_list);
        this.adapter = new HotspotsClusterDetailsAdapter(this.context, C0984R.layout.academy_map_details_row, this.hotspots);
        if (z) {
            this.footer = ((Activity) this.context).getLayoutInflater().inflate(C0984R.layout.academy_map_details_footer, this.list, false);
            ((TextView) this.footer.findViewById(C0984R.id.moreTextView)).setText(this.context.getString(C0984R.string.aa_ID000187));
            this.footer.setOnClickListener(new C10452());
            this.list.addFooterView(this.footer);
        }
        this.list.setAdapter(this.adapter);
        this.list.setOnItemClickListener(new C10463());
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
        new GetHotspotsClustersListAsyncTask().execute(new String[]{this.clusterIdx});
        onPostCreate(bundle);
        getWindow().setLayout(-1, -1);
    }

    protected void onPostCreate(Bundle bundle) {
        FontUtils.applyFont(this.context, (ViewGroup) findViewById(16908290));
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        super.setOnShowListener(onShowListener);
        getWindow().setSoftInputMode(5);
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        this.adapter.setSortOrder(sortOrder);
        if (this.radioDate != null && this.radioRank != null) {
            switch (sortOrder) {
                case DATE_ASC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.map_sort_indicator_asc, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    return;
                case DATE_DESC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.map_sort_indicator_desc, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    return;
                case RANK_ASC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.map_sort_indicator_asc, 0);
                    return;
                case RANK_DESC:
                    this.radioDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.sort_indicator, 0);
                    this.radioRank.setCompoundDrawablesWithIntrinsicBounds(0, 0, C0984R.drawable.map_sort_indicator_desc, 0);
                    return;
                default:
                    Log.w("clusterdialog", "Unknown sort order");
                    return;
            }
        }
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
}
