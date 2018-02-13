package com.parrot.freeflight.academy.services.restoremedia;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Media;
import android.util.Log;
import com.google.android.gms.plus.PlusShare;
import com.parrot.ardronetool.video.VideoAtomRetriever;
import com.parrot.ardronetool.video.VideoAtomRetriever.AtomType;
import com.parrot.freeflight.media.Exif2Interface;
import com.parrot.freeflight.media.Exif2Interface.Tag;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.utils.FileUtils;
import com.parrot.freeflight.vo.MediaVO;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mortbay.util.URIUtil;

public class RestoreFlightMediaService extends Service {
    private String TAG = RestoreFlightMediaService.class.getSimpleName();
    private ExecutorService executor;

    private class ScanMediaFilesTask extends AsyncTask<Void, Integer, List<MediaVO>> {
        private VideoAtomRetriever atomRetriever = new VideoAtomRetriever();
        private SimpleDateFormat photoDateFormatter = new SimpleDateFormat("'picture_'yyyyMMdd'_'HHmmss", Locale.US);
        private SimpleDateFormat videoDateFormatter = new SimpleDateFormat("'video_'yyyyMMdd'_'HHmmss", Locale.US);

        private void dumpListToLog(List<MediaVO> list) {
            Log.d(RestoreFlightMediaService.this.TAG, "Files without flight id: ");
            for (MediaVO path : list) {
                Log.d(RestoreFlightMediaService.this.TAG, path.getPath());
            }
        }

        private Date getDateTakenFromFile(MediaVO mediaVO) {
            String atomData;
            Date date = null;
            String[] split;
            Object obj;
            if (mediaVO.isVideo()) {
                atomData = this.atomRetriever.getAtomData(mediaVO.getPath(), AtomType.ARDT);
                if (!(atomData == null || atomData.indexOf(47) == -1)) {
                    split = atomData.split("[|/]");
                    if (split.length == 3) {
                        atomData = split[2];
                    }
                }
                obj = date;
            } else {
                try {
                    Exif2Interface exif2Interface = new Exif2Interface(mediaVO.getPath());
                    if (exif2Interface.getAttribute(Tag.MAKE).equals(DroneControlService.EXIF_MAKE_TAG)) {
                        atomData = exif2Interface.getAttribute(Tag.IMAGE_DESCRIPTION);
                        if (atomData != null) {
                            split = atomData.split(URIUtil.SLASH);
                            if (split.length > 1) {
                                atomData = split[1];
                            }
                        }
                    }
                    obj = date;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (atomData == null) {
                String name = new File(mediaVO.getPath()).getName();
                if (name.startsWith("picture_") || name.startsWith("video_")) {
                    atomData = name;
                }
            }
            if (atomData != null) {
                try {
                    date = mediaVO.isVideo() ? this.videoDateFormatter.parse(atomData) : this.photoDateFormatter.parse(atomData);
                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
            }
            return date;
        }

        private String getFlightIdFromPhoto(String str) throws IOException {
            Exif2Interface exif2Interface = new Exif2Interface(str);
            String attribute = exif2Interface.getAttribute(Tag.MAKE);
            if (attribute != null && attribute.equals(DroneControlService.EXIF_MAKE_TAG)) {
                String attribute2 = exif2Interface.getAttribute(Tag.IMAGE_DESCRIPTION);
                Log.d(RestoreFlightMediaService.this.TAG, "Got image description data: " + attribute2);
                if (attribute2 != null) {
                    int indexOf = attribute2.indexOf(47);
                    if (indexOf != -1) {
                        return attribute2.substring(0, indexOf);
                    }
                }
            }
            return null;
        }

        private String getFlightIdFromVideo(String str) {
            String atomData = this.atomRetriever.getAtomData(str, AtomType.ARDT);
            Log.d(RestoreFlightMediaService.this.TAG, "Got atom data: " + atomData);
            if (atomData == null) {
                return null;
            }
            int indexOf = atomData.indexOf(47);
            int indexOf2 = atomData.indexOf(124);
            return (indexOf == -1 || indexOf2 == -1) ? null : atomData.substring(indexOf2 + 1, indexOf);
        }

        private List<MediaVO> getMediaWithoutDateTaken() {
            Throwable th;
            List<MediaVO> emptyList = Collections.emptyList();
            String[] strArr = new String[]{FileUtils.MEDIA_PUBLIC_FOLDER_NAME};
            ContentResolver contentResolver = RestoreFlightMediaService.this.getContentResolver();
            Cursor[] cursorArr = new Cursor[2];
            cursorArr[0] = contentResolver.query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, "bucket_display_name=? AND datetaken IS NULL", strArr, null);
            cursorArr[1] = contentResolver.query(Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, "bucket_display_name=? AND datetaken IS NULL", strArr, null);
            MergeCursor mergeCursor = new MergeCursor(cursorArr);
            try {
                List<MediaVO> arrayList;
                if (mergeCursor.moveToFirst()) {
                    arrayList = new ArrayList(mergeCursor.getCount());
                    try {
                        int columnIndex = mergeCursor.getColumnIndex("_id");
                        int columnIndex2 = mergeCursor.getColumnIndex("_data");
                        do {
                            String string = mergeCursor.getString(columnIndex2);
                            int i = mergeCursor.getInt(columnIndex);
                            MediaVO mediaVO = new MediaVO();
                            mediaVO.setId(i);
                            mediaVO.setPath(string);
                            mediaVO.setVideo(string.endsWith(".mp4"));
                            mediaVO.setUri(Uri.withAppendedPath(mediaVO.isVideo() ? Media.EXTERNAL_CONTENT_URI : Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(i)));
                            arrayList.add(mediaVO);
                        } while (mergeCursor.moveToNext());
                    } catch (Throwable th2) {
                        th = th2;
                        if (mergeCursor != null) {
                            mergeCursor.close();
                        }
                        throw th;
                    }
                }
                Log.d(RestoreFlightMediaService.this.TAG, "No media files");
                arrayList = emptyList;
                if (mergeCursor != null) {
                    mergeCursor.close();
                }
                return arrayList;
            } catch (Throwable th3) {
                th = th3;
                if (mergeCursor != null) {
                    mergeCursor.close();
                }
                throw th;
            }
        }

        private List<MediaVO> getMediaWithoutDescriptionData() {
            Throwable th;
            List<MediaVO> emptyList = Collections.emptyList();
            String[] strArr = new String[]{"_id", "_data"};
            String[] strArr2 = new String[]{FileUtils.MEDIA_PUBLIC_FOLDER_NAME};
            ContentResolver contentResolver = RestoreFlightMediaService.this.getContentResolver();
            MergeCursor mergeCursor = new MergeCursor(new Cursor[]{contentResolver.query(Media.EXTERNAL_CONTENT_URI, strArr, "bucket_display_name=? AND description IS NULL", strArr2, null), contentResolver.query(Images.Media.EXTERNAL_CONTENT_URI, strArr, "bucket_display_name=? AND description IS NULL", strArr2, null)});
            try {
                List<MediaVO> arrayList;
                if (mergeCursor.moveToFirst()) {
                    arrayList = new ArrayList(mergeCursor.getCount());
                    try {
                        int columnIndex = mergeCursor.getColumnIndex("_id");
                        int columnIndex2 = mergeCursor.getColumnIndex("_data");
                        do {
                            String string = mergeCursor.getString(columnIndex2);
                            int i = mergeCursor.getInt(columnIndex);
                            MediaVO mediaVO = new MediaVO();
                            mediaVO.setId(i);
                            mediaVO.setPath(string);
                            mediaVO.setVideo(string.endsWith(".mp4"));
                            mediaVO.setUri(Uri.withAppendedPath(mediaVO.isVideo() ? Media.EXTERNAL_CONTENT_URI : Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(i)));
                            arrayList.add(mediaVO);
                        } while (mergeCursor.moveToNext());
                    } catch (Throwable th2) {
                        th = th2;
                        if (mergeCursor != null) {
                            mergeCursor.close();
                        }
                        throw th;
                    }
                }
                Log.d(RestoreFlightMediaService.this.TAG, "No media files");
                arrayList = emptyList;
                if (mergeCursor != null) {
                    mergeCursor.close();
                }
                return arrayList;
            } catch (Throwable th3) {
                th = th3;
                if (mergeCursor != null) {
                    mergeCursor.close();
                }
                throw th;
            }
        }

        private void updateMediaWithDateTaken(MediaVO mediaVO, Date date) {
            ContentResolver contentResolver = RestoreFlightMediaService.this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            String[] strArr = new String[]{Integer.toString(mediaVO.getId())};
            if (mediaVO.isVideo()) {
                contentValues.put("datetaken", Long.valueOf(date.getTime()));
                contentResolver.update(Media.EXTERNAL_CONTENT_URI, contentValues, "_id=?", strArr);
                return;
            }
            contentValues.put("datetaken", Long.valueOf(date.getTime()));
            contentResolver.update(Images.Media.EXTERNAL_CONTENT_URI, contentValues, "_id=?", strArr);
        }

        private void updateMediaWithFlightId(MediaVO mediaVO, String str) {
            ContentResolver contentResolver = RestoreFlightMediaService.this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            String[] strArr = new String[]{Integer.toString(mediaVO.getId())};
            if (mediaVO.isVideo()) {
                contentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, str);
                contentResolver.update(Media.EXTERNAL_CONTENT_URI, contentValues, "_id=?", strArr);
                return;
            }
            contentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, str);
            contentResolver.update(Images.Media.EXTERNAL_CONTENT_URI, contentValues, "_id=?", strArr);
        }

        protected List<MediaVO> doInBackground(Void... voidArr) {
            List<MediaVO> mediaWithoutDescriptionData = getMediaWithoutDescriptionData();
            dumpListToLog(mediaWithoutDescriptionData);
            for (MediaVO mediaVO : mediaWithoutDescriptionData) {
                String path = mediaVO.getPath();
                String str = null;
                if (mediaVO.isVideo()) {
                    str = getFlightIdFromVideo(path);
                    Log.d(RestoreFlightMediaService.this.TAG, "Flight string: " + str);
                } else {
                    try {
                        str = getFlightIdFromPhoto(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (str != null) {
                    Log.d(RestoreFlightMediaService.this.TAG, "Flight id for media " + path + " is " + str);
                    updateMediaWithFlightId(mediaVO, str);
                } else {
                    updateMediaWithFlightId(mediaVO, "unknown");
                }
                Date dateTakenFromFile = getDateTakenFromFile(mediaVO);
                if (dateTakenFromFile != null) {
                    updateMediaWithDateTaken(mediaVO, dateTakenFromFile);
                }
            }
            for (MediaVO mediaVO2 : getMediaWithoutDateTaken()) {
                Date dateTakenFromFile2 = getDateTakenFromFile(mediaVO2);
                if (dateTakenFromFile2 != null) {
                    updateMediaWithDateTaken(mediaVO2, dateTakenFromFile2);
                }
            }
            return mediaWithoutDescriptionData;
        }
    }

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onCreate() {
        super.onCreate();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void onDestroy() {
        this.executor.shutdownNow();
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            stopSelf(i2);
        } else {
            ScanMediaFilesTask scanMediaFilesTask = new ScanMediaFilesTask();
            if (VERSION.SDK_INT < 11) {
                scanMediaFilesTask.execute(new Void[0]);
            } else {
                scanMediaFilesTask.executeOnExecutor(this.executor, new Void[0]);
            }
        }
        return 1;
    }
}
