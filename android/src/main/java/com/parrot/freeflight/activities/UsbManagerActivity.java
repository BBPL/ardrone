package com.parrot.freeflight.activities;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.parrot.ardronetool.ARDroneEngine.EVideoRecorderCapability;
import com.parrot.ardronetool.utils.DeviceCapabilitiesUtils;
import com.parrot.ardronetool.video.UsbVideoDownloader.Status;
import com.parrot.ardronetool.video.UsbVideoDownloader.UsbVideoDownloadProgressListener;
import com.parrot.ardronetool.video.UsbVideoDownloader.VideoItem;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.dialogs.ProgressDialog;
import com.parrot.freeflight.receivers.DroneUsbStateChangedReceiver;
import com.parrot.freeflight.receivers.DroneUsbStateChangedReceiverDelegate;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.DroneControlService.LocalBinder;
import com.parrot.freeflight.utils.ARDroneMediaGallery;
import com.parrot.freeflight.vo.MediaVO;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.util.URIUtil;

public class UsbManagerActivity extends ParrotActivity implements OnClickListener, OnCheckedChangeListener, ServiceConnection, DroneUsbStateChangedReceiverDelegate {
    private boolean deviceSupportsHD;
    private DroneControlService droneControlService;
    private final ArrayList<VideoItem> itemsAlreadyDownloaded = new ArrayList();
    private final ArrayList<VideoItem> itemsToDelete = new ArrayList();
    private final ArrayList<VideoItem> itemsToDownload = new ArrayList();
    private DroneUsbStateChangedReceiver usbStateChangedReceiver;
    private RemoteFlightsAdapter videoListAdapter;

    private class DownloadItemsTask extends AsyncTask<List<VideoItem>, Integer, Boolean> implements UsbVideoDownloadProgressListener, OnCancelListener {
        private final String TAG;
        private int currentVideoIndex;
        private ProgressDialog progressDialog;
        private int totalDownloadedBytes;
        private int totalVideoCount;
        private int totalVideoSize;
        private Object waitLock;

        private DownloadItemsTask() {
            this.TAG = DownloadItemsTask.class.getSimpleName();
            this.totalVideoSize = 0;
            this.totalVideoCount = 0;
            this.totalDownloadedBytes = 0;
            this.currentVideoIndex = 0;
        }

        private int calculateTotalVideoSize(List<VideoItem> list) {
            int i = 0;
            int i2 = 0;
            while (i2 < list.size()) {
                int i3 = ((VideoItem) list.get(i2)).size + i;
                i2++;
                i = i3;
            }
            return i;
        }

        private void onDownloadFailedForItem(VideoItem videoItem) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
        }

        private void onDownloadFinishedForItem(VideoItem videoItem) {
            this.totalDownloadedBytes += videoItem.size;
            publishProgress(new Integer[]{Integer.valueOf(this.totalDownloadedBytes)});
            UsbManagerActivity.this.videoListAdapter.setItemDownloaded(videoItem, true);
        }

        private void onItemDownloading(VideoItem videoItem, float f) {
            publishProgress(new Integer[]{Integer.valueOf((int) (((float) this.totalDownloadedBytes) + (((float) videoItem.size) * (f / 100.0f))))});
        }

        protected Boolean doInBackground(List<VideoItem>... listArr) {
            List<VideoItem> list = listArr[0];
            this.totalVideoSize = calculateTotalVideoSize(list);
            this.totalVideoCount = list.size();
            this.progressDialog.setMax(this.totalVideoSize);
            this.currentVideoIndex = 0;
            for (VideoItem videoItem : list) {
                this.currentVideoIndex++;
                UsbManagerActivity.this.droneControlService.usbDownloadVideo(videoItem, this);
                synchronized (this.waitLock) {
                    try {
                        this.waitLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return Boolean.valueOf(false);
                    }
                }
                if (isCancelled()) {
                    break;
                }
            }
            return Boolean.valueOf(true);
        }

        public void onCancel(DialogInterface dialogInterface) {
            cancel(false);
            UsbManagerActivity.this.droneControlService.usbCancellAllDownloads();
        }

        protected void onCancelled() {
            this.progressDialog.dismiss();
        }

        protected void onPostExecute(Boolean bool) {
            this.progressDialog.dismiss();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.waitLock = new Object();
            this.progressDialog = new ProgressDialog(UsbManagerActivity.this);
            this.progressDialog.setMessage(UsbManagerActivity.this.getString(C0984R.string.ae_ID000110));
            this.progressDialog.setSubMessage(HttpVersions.HTTP_0_9);
            this.progressDialog.setProgressStyle(1);
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.setCancelable(true);
            this.progressDialog.setOnCancelListener(this);
            this.progressDialog.show();
            this.totalDownloadedBytes = 0;
        }

        public void onProgress(Status status, final VideoItem videoItem, float f) {
            switch (status) {
                case PROGRESS:
                    onItemDownloading(videoItem, f);
                    return;
                case OK:
                    UsbManagerActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            DownloadItemsTask.this.onDownloadFinishedForItem(videoItem);
                        }
                    });
                    synchronized (this.waitLock) {
                        this.waitLock.notify();
                    }
                    Log.i(this.TAG, "Downloading succeeded");
                    return;
                case FAIL:
                    UsbManagerActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            DownloadItemsTask.this.onDownloadFailedForItem(videoItem);
                        }
                    });
                    synchronized (this.waitLock) {
                        this.waitLock.notify();
                    }
                    Log.i(this.TAG, "Downloading failed");
                    return;
                case CANCEL:
                    synchronized (this.waitLock) {
                        this.waitLock.notify();
                    }
                    Log.i(this.TAG, "Downloading cancelled");
                    return;
                case TIMEOUT:
                    UsbManagerActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            DownloadItemsTask.this.onDownloadFailedForItem(videoItem);
                        }
                    });
                    synchronized (this.waitLock) {
                        this.waitLock.notify();
                    }
                    Log.i(this.TAG, "Downloading timed out");
                    return;
                default:
                    return;
            }
        }

        protected void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            this.progressDialog.setProgress(numArr[0].intValue());
            this.progressDialog.setSubMessage(String.format("%d/%d", new Object[]{Integer.valueOf(this.currentVideoIndex), Integer.valueOf(this.totalVideoCount)}));
        }
    }

    class C11142 extends DownloadItemsTask {
        C11142() {
            super();
        }

        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool == Boolean.TRUE) {
                UsbManagerActivity.this.requestListRefresh();
            }
        }
    }

    private class DeleteItemsTask extends AsyncTask<List<VideoItem>, Void, Boolean> {
        private final String TAG;

        private DeleteItemsTask() {
            this.TAG = DeleteItemsTask.class.getSimpleName();
        }

        protected Boolean doInBackground(List<VideoItem>... listArr) {
            boolean usbDeleteVideo;
            boolean z = true;
            for (VideoItem videoItem : listArr[0]) {
                usbDeleteVideo = UsbManagerActivity.this.droneControlService.usbDeleteVideo(videoItem);
                if (!usbDeleteVideo) {
                    Log.w(this.TAG, "Error deleting media file " + videoItem.userboxName + URIUtil.SLASH + videoItem.videoName);
                    break;
                }
                z = usbDeleteVideo;
            }
            usbDeleteVideo = z;
            return Boolean.valueOf(usbDeleteVideo);
        }
    }

    class C11153 extends DeleteItemsTask {
        C11153() {
            super();
        }

        protected void onPostExecute(Boolean bool) {
            UsbManagerActivity.this.requestListLoad();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            UsbManagerActivity.this.videoListAdapter.notifyDataSetInvalidated();
        }
    }

    private class GetDownloadedVideoListTask extends AsyncTask<List<VideoItem>, Void, List<VideoItem>> {
        private ARDroneMediaGallery mediaGallery;

        public GetDownloadedVideoListTask() {
            this.mediaGallery = new ARDroneMediaGallery(UsbManagerActivity.this);
        }

        protected List<VideoItem> doInBackground(List<VideoItem>... listArr) {
            List<VideoItem> arrayList = new ArrayList();
            List list = listArr[0];
            if (list == null) {
                return Collections.emptyList();
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                VideoItem videoItem = (VideoItem) list.get(i);
                List mediaVideoList = this.mediaGallery.getMediaVideoList(videoItem.userboxName);
                int size2 = mediaVideoList.size();
                if (size2 != 0) {
                    for (int i2 = 0; i2 < size2; i2++) {
                        if (((MediaVO) mediaVideoList.get(i2)).getFilePath().getName().equals(videoItem.videoName)) {
                            arrayList.add(videoItem);
                        }
                    }
                }
            }
            return arrayList;
        }
    }

    class C11164 extends GetDownloadedVideoListTask {
        C11164() {
            super();
        }

        protected void onPostExecute(List<VideoItem> list) {
            UsbManagerActivity.this.itemsAlreadyDownloaded.addAll(list);
            UsbManagerActivity.this.videoListAdapter.setAlreadyDownloadedItems(UsbManagerActivity.this.itemsAlreadyDownloaded);
        }
    }

    private class GetUsbVideoListTask extends AsyncTask<Void, Void, List<VideoItem>> {
        private GetUsbVideoListTask() {
        }

        protected List<VideoItem> doInBackground(Void... voidArr) {
            return UsbManagerActivity.this.droneControlService.getUsbVideoList();
        }

        protected void onPostExecute(List<VideoItem> list) {
            if (list == null) {
                UsbManagerActivity.this.setMessageTextAndVisibility(UsbManagerActivity.this.getString(C0984R.string.ae_ID000116), 0);
            } else if (list.isEmpty()) {
                UsbManagerActivity.this.setMessageTextAndVisibility(UsbManagerActivity.this.getString(C0984R.string.ae_ID000115), 0);
            } else {
                UsbManagerActivity.this.videoListAdapter.setData(list);
                UsbManagerActivity.this.setLoadingVisibility(4);
            }
        }

        protected void onPreExecute() {
            UsbManagerActivity.this.videoListAdapter.setData(new ArrayList());
            UsbManagerActivity.this.setLoadingVisibility(0);
        }
    }

    class C11186 extends GetDownloadedVideoListTask {
        C11186() {
            super();
        }

        protected void onPostExecute(List<VideoItem> list) {
            UsbManagerActivity.this.itemsAlreadyDownloaded.clear();
            UsbManagerActivity.this.itemsAlreadyDownloaded.addAll(list);
            UsbManagerActivity.this.videoListAdapter.setAlreadyDownloadedItems(UsbManagerActivity.this.itemsAlreadyDownloaded);
        }
    }

    private static final class ListItem {
        public boolean delete;
        public boolean download;
        public boolean downloaded;
        public Spanned name;
        public VideoItem videoItem;

        private ListItem() {
        }
    }

    private class RemoteFlightsAdapter extends BaseAdapter {
        private DateFormat itemDateFormat = DateFormat.getDateTimeInstance(3, 2, Locale.getDefault());
        private List<ListItem> listData = Collections.emptyList();
        private Map<VideoItem, ListItem> listDataMap = new HashMap();
        private SimpleDateFormat userboxDateFormat = new SimpleDateFormat("'media_'yyyyMMdd'_'HHmmss", Locale.US);
        private SimpleDateFormat videoDateFormat = new SimpleDateFormat("'video_'yyyyMMdd'_'HHmmss", Locale.US);

        private String generateItemName(VideoItem videoItem) {
            String str = videoItem.videoName;
            if (str.split("_").length == 3) {
                try {
                    return this.itemDateFormat.format(this.videoDateFormat.parse(str));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return videoItem.videoName;
                }
            }
            try {
                return this.itemDateFormat.format(this.userboxDateFormat.parse(videoItem.userboxName));
            } catch (ParseException e2) {
                e2.printStackTrace();
                return videoItem.videoName;
            }
        }

        public int getCount() {
            return this.listData.size();
        }

        public Object getItem(int i) {
            return this.listData.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = UsbManagerActivity.this.inflateView(C0984R.layout.view_usb_manager_list_item, viewGroup, false);
                viewHolder = new ViewHolder();
                view.setTag(viewHolder);
                viewHolder.textName = (TextView) view.findViewById(C0984R.id.view_usb_manager_listitem_textview_date);
                viewHolder.checkDelete = (CheckBox) view.findViewById(C0984R.id.view_usb_manager_listitem_checkbox_delete);
                viewHolder.checkDownload = (CheckBox) view.findViewById(C0984R.id.view_usb_manager_listitem_checkbox_download);
                viewHolder.labelDownloaded = (TextView) view.findViewById(C0984R.id.view_usb_manager_listitem_textview_downloaded);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            ListItem listItem = (ListItem) this.listData.get(i);
            viewHolder.checkDelete.setOnCheckedChangeListener(null);
            viewHolder.checkDownload.setOnCheckedChangeListener(null);
            viewHolder.checkDelete.setChecked(listItem.delete);
            if (listItem.downloaded) {
                viewHolder.checkDownload.setVisibility(8);
                viewHolder.labelDownloaded.setVisibility(0);
            } else {
                viewHolder.labelDownloaded.setVisibility(8);
                viewHolder.checkDownload.setVisibility(0);
                viewHolder.checkDownload.setChecked(listItem.download);
            }
            viewHolder.textName.setText(listItem.name);
            viewHolder.checkDelete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    ListItem listItem = (ListItem) RemoteFlightsAdapter.this.listData.get(i);
                    listItem.delete = z;
                    if (z) {
                        UsbManagerActivity.this.itemsToDelete.add(listItem.videoItem);
                    } else {
                        UsbManagerActivity.this.itemsToDelete.remove(listItem.videoItem);
                    }
                }
            });
            viewHolder.checkDownload.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (!z || UsbManagerActivity.this.deviceSupportsHD) {
                        ListItem listItem = (ListItem) RemoteFlightsAdapter.this.listData.get(i);
                        listItem.download = z;
                        if (z) {
                            UsbManagerActivity.this.itemsToDownload.add(listItem.videoItem);
                            return;
                        } else {
                            UsbManagerActivity.this.itemsToDownload.remove(listItem.videoItem);
                            return;
                        }
                    }
                    new Builder(UsbManagerActivity.this).setTitle("Unable to download").setMessage("Sorry, your device doesn't support playback of HD video.").setNegativeButton("Ok", null).create().show();
                    compoundButton.setChecked(false);
                }
            });
            return view;
        }

        public void setAlreadyDownloadedItems(List<VideoItem> list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ListItem listItem = (ListItem) this.listDataMap.get((VideoItem) list.get(i));
                if (listItem != null) {
                    listItem.downloaded = true;
                }
            }
            notifyDataSetChanged();
        }

        public void setData(List<VideoItem> list) {
            this.listData = new ArrayList(list.size());
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ListItem listItem = new ListItem();
                VideoItem videoItem = (VideoItem) list.get(i);
                listItem.videoItem = videoItem;
                float f = (((float) videoItem.size) / 1024.0f) / 1024.0f;
                String str = "MiB";
                if (1.0f > f) {
                    str = "KiB";
                    f = ((float) videoItem.size) / 1024.0f;
                }
                listItem.name = Html.fromHtml(String.format("%s - <font color=#ff7800>%.1f %s</font>", new Object[]{generateItemName(videoItem), Float.valueOf(f), str}));
                this.listData.add(listItem);
                this.listDataMap.put(videoItem, listItem);
            }
            notifyDataSetChanged();
        }

        public void setDeleteAllChecked(boolean z) {
            int size = this.listData.size();
            UsbManagerActivity.this.itemsToDelete.clear();
            for (int i = 0; i < size; i++) {
                ListItem listItem = (ListItem) this.listData.get(i);
                listItem.delete = z;
                if (z) {
                    UsbManagerActivity.this.itemsToDelete.add(listItem.videoItem);
                }
            }
            notifyDataSetChanged();
        }

        public void setDownloadAllChecked(boolean z) {
            int size = this.listData.size();
            UsbManagerActivity.this.itemsToDownload.clear();
            for (int i = 0; i < size; i++) {
                ListItem listItem = (ListItem) this.listData.get(i);
                listItem.download = z;
                if (z && !listItem.downloaded) {
                    UsbManagerActivity.this.itemsToDownload.add(listItem.videoItem);
                }
            }
            notifyDataSetChanged();
        }

        public void setItemDownloaded(VideoItem videoItem, boolean z) {
            int size = this.listData.size();
            for (int i = 0; i < size; i++) {
                ListItem listItem = (ListItem) this.listData.get(i);
                if (listItem.videoItem.equals(videoItem)) {
                    listItem.downloaded = z;
                    UsbManagerActivity.this.itemsToDownload.remove(videoItem);
                }
            }
            notifyDataSetChanged();
        }
    }

    private enum SelectAllType {
        DOWNLOAD,
        DELETE
    }

    private static final class ViewHolder {
        public CheckBox checkDelete;
        public CheckBox checkDownload;
        public TextView labelDownloaded;
        public TextView textName;

        private ViewHolder() {
        }
    }

    private void initActionBar() {
        getParrotActionBar().initBackButton();
        setTitle((CharSequence) "AR.DRONE USB KEY");
    }

    private void initBottomBar() {
        ((Button) findViewById(C0984R.id.activity_usb_manager_button_apply)).setOnClickListener(this);
        ToggleButton toggleButton = (ToggleButton) findViewById(C0984R.id.activity_usb_manager_toggle_selectall_download);
        toggleButton.setEnabled(this.deviceSupportsHD);
        if (this.deviceSupportsHD) {
            toggleButton.setOnCheckedChangeListener(this);
        }
        ((ToggleButton) findViewById(C0984R.id.activity_usb_manager_toggle_selectall_delete)).setOnCheckedChangeListener(this);
    }

    private void initBroadcastReceivers() {
        this.usbStateChangedReceiver = new DroneUsbStateChangedReceiver(this);
    }

    private void initList() {
        ListView listView = (ListView) findViewById(C0984R.id.activity_usb_manager_flights_listview_list);
        this.videoListAdapter = new RemoteFlightsAdapter();
        listView.setAdapter(this.videoListAdapter);
        setMessageTextAndVisibility("No media on USB key", 0);
    }

    private void onApplyClicked(View view) {
        if (!this.itemsToDownload.isEmpty() && !this.itemsToDelete.isEmpty()) {
            requestDownloadAndDeleteItems((List) this.itemsToDownload.clone(), (List) this.itemsToDelete.clone());
        } else if (!this.itemsToDelete.isEmpty()) {
            requestDeleteItems((List) this.itemsToDelete.clone());
        } else if (!this.itemsToDownload.isEmpty()) {
            requestDownloadItems((List) this.itemsToDownload.clone());
        }
    }

    private void onSelectAllCheckedChanged(SelectAllType selectAllType, boolean z) {
        switch (selectAllType) {
            case DELETE:
                this.videoListAdapter.setDeleteAllChecked(z);
                return;
            case DOWNLOAD:
                this.videoListAdapter.setDownloadAllChecked(z);
                return;
            default:
                Toast.makeText(this, "Sorry, not implemented yet", 0).show();
                return;
        }
    }

    private void requestDeleteItems(List<VideoItem> list) {
        new C11153().execute(new List[]{this.itemsToDelete});
    }

    private void requestDownloadAndDeleteItems(List<VideoItem> list, final List<VideoItem> list2) {
        new DownloadItemsTask() {
            protected void onPostExecute(Boolean bool) {
                super.onPostExecute(bool);
                if (bool == Boolean.TRUE) {
                    UsbManagerActivity.this.requestDeleteItems(list2);
                }
            }
        }.execute(new List[]{new ArrayList(list)});
    }

    private void requestDownloadItems(List<VideoItem> list) {
        new C11142().execute(new List[]{list});
    }

    private void requestListLoad() {
        final GetDownloadedVideoListTask c11164 = new C11164();
        new GetUsbVideoListTask() {
            protected void onPostExecute(List<VideoItem> list) {
                super.onPostExecute((List) list);
                c11164.execute(new List[]{list});
            }
        }.execute(new Void[0]);
    }

    private void requestListRefresh() {
        final GetDownloadedVideoListTask c11186 = new C11186();
        new GetUsbVideoListTask() {
            protected void onPostExecute(List<VideoItem> list) {
                c11186.execute(new List[]{list});
            }

            protected void onPreExecute() {
            }
        }.execute(new Void[0]);
    }

    private void setLoadingVisibility(int i) {
        setMessageTextAndVisibility(getString(C0984R.string.ae_ID000068), i);
    }

    private void setMessageTextAndVisibility(String str, int i) {
        TextView textView = (TextView) findViewById(C0984R.id.activity_usb_manager_textview_loading);
        textView.setText(str);
        textView.setVisibility(i);
    }

    public static final void start(Context context) {
        context.startActivity(new Intent(context, UsbManagerActivity.class));
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        switch (compoundButton.getId()) {
            case C0984R.id.activity_usb_manager_toggle_selectall_download /*2131362045*/:
                onSelectAllCheckedChanged(SelectAllType.DOWNLOAD, z);
                return;
            case C0984R.id.activity_usb_manager_toggle_selectall_delete /*2131362046*/:
                onSelectAllCheckedChanged(SelectAllType.DELETE, z);
                return;
            default:
                Toast.makeText(this, "Sorry, not implemented yet", 0).show();
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.activity_usb_manager_button_apply /*2131362044*/:
                onApplyClicked(view);
                return;
            default:
                Toast.makeText(this, "Sorry, not implemented yet", 0).show();
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_usb_manager);
        bindService(new Intent(this, DroneControlService.class), this, 1);
        this.deviceSupportsHD = DeviceCapabilitiesUtils.getMaxSupportedVideoRes(this).equals(EVideoRecorderCapability.VIDEO_720P);
        initBroadcastReceivers();
        initActionBar();
        initList();
        initBottomBar();
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    public void onDroneUsbStateChanged(boolean z) {
        if (!z) {
            finish();
        }
    }

    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.usbStateChangedReceiver);
        if (this.droneControlService != null) {
            this.droneControlService.pause();
        }
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.usbStateChangedReceiver, DroneUsbStateChangedReceiver.createFilter());
        if (this.droneControlService != null) {
            this.droneControlService.resume();
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.droneControlService = ((LocalBinder) iBinder).getService();
        this.droneControlService.resume();
        requestListLoad();
    }

    public void onServiceDisconnected(ComponentName componentName) {
    }
}
