package com.parrot.freeflight.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.util.Log;
import com.google.android.gms.plus.PlusShare;
import com.parrot.freeflight.vo.MediaVO;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.mortbay.jetty.HttpVersions;

public class ARDroneMediaGallery implements MediaScannerConnectionClient {
    private static final String TAG = ARDroneMediaGallery.class.getSimpleName();
    private MediaScannerConnection connection;
    private ContentResolver contentResolver;
    private Uri imageSourceUri;
    private List<String> mediaToScan = new Vector();
    private Uri videoSourceUri;

    public ARDroneMediaGallery(Context context) {
        this.contentResolver = context.getContentResolver();
        this.connection = new MediaScannerConnection(context, this);
        this.imageSourceUri = Media.EXTERNAL_CONTENT_URI;
        this.videoSourceUri = Video.Media.EXTERNAL_CONTENT_URI;
    }

    private void addMedia(ArrayList<MediaVO> arrayList, Uri uri, String str) {
        Cursor mediaCursor = getMediaCursor(uri, str);
        if (mediaCursor == null) {
            try {
                Log.w(TAG, "Unknown error");
            } catch (Throwable th) {
                if (mediaCursor != null) {
                    mediaCursor.close();
                }
            }
        } else if (mediaCursor.moveToFirst()) {
            int columnIndex = mediaCursor.getColumnIndex("_id");
            int columnIndex2 = mediaCursor.getColumnIndex("_data");
            int columnIndex3 = mediaCursor.getColumnIndex("date_added");
            int columnIndex4 = mediaCursor.getColumnIndex("datetaken");
            do {
                int i = mediaCursor.getInt(columnIndex);
                long j = mediaCursor.getLong(columnIndex3);
                long j2 = mediaCursor.getLong(columnIndex4);
                String string = mediaCursor.getString(columnIndex2);
                boolean isVideo = FileUtils.isVideo(string);
                MediaVO mediaVO = new MediaVO();
                mediaVO.setId(i);
                mediaVO.setDateAdded(j);
                mediaVO.setDateTaken(j2);
                mediaVO.setPath(string);
                mediaVO.setUri(Uri.withAppendedPath(uri, Integer.toString(i)));
                mediaVO.setVideo(isVideo);
                arrayList.add(mediaVO);
            } while (mediaCursor.moveToNext());
        } else {
            Log.w(TAG, "No media on the device");
        }
        if (mediaCursor != null) {
            mediaCursor.close();
        }
    }

    private Cursor getMediaCursor(Uri uri, String str) {
        String str2;
        String[] strArr;
        if (str == null) {
            str2 = "bucket_display_name=?";
            strArr = new String[]{FileUtils.MEDIA_PUBLIC_FOLDER_NAME};
        } else {
            str2 = "bucket_display_name=? AND description=?";
            strArr = new String[]{FileUtils.MEDIA_PUBLIC_FOLDER_NAME, str};
        }
        return this.contentResolver.query(uri, new String[]{"_id", "_data", "date_added", "datetaken"}, str2, strArr, "datetaken ASC");
    }

    public int countOfMedia() {
        return countOfVideos() + countOfPhotos();
    }

    public int countOfMedia(String str) {
        return countOfVideos(str) + countOfPhotos(str);
    }

    public int countOfPhotos() {
        return countOfPhotos(null);
    }

    public int countOfPhotos(String str) {
        String str2;
        String[] strArr;
        Cursor query;
        Throwable th;
        if (str == null) {
            str2 = "bucket_display_name=?";
            strArr = new String[]{FileUtils.MEDIA_PUBLIC_FOLDER_NAME};
        } else {
            str2 = "bucket_display_name=? AND description =?";
            strArr = new String[]{FileUtils.MEDIA_PUBLIC_FOLDER_NAME, str};
        }
        try {
            int columnIndex;
            query = this.contentResolver.query(this.imageSourceUri, new String[]{"count(_id) as result"}, str2, strArr, null);
            if (query == null) {
                try {
                    Log.w(TAG, "Unknown error");
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query.moveToFirst()) {
                columnIndex = query.getColumnIndex("result");
                if (columnIndex != -1) {
                    query.moveToFirst();
                    columnIndex = query.getInt(columnIndex);
                }
            } else {
                Log.w(TAG, "No media on the device");
                columnIndex = 0;
            }
            if (query != null) {
                query.close();
            }
            return columnIndex;
            columnIndex = 0;
            if (query != null) {
                query.close();
            }
            return columnIndex;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public int countOfVideos() {
        return countOfVideos(null);
    }

    public int countOfVideos(String str) {
        String str2;
        String[] strArr;
        Throwable th;
        if (str == null) {
            str2 = "bucket_display_name=?";
            strArr = new String[]{FileUtils.MEDIA_PUBLIC_FOLDER_NAME};
        } else {
            str2 = "bucket_display_name=? AND description =?";
            strArr = new String[]{FileUtils.MEDIA_PUBLIC_FOLDER_NAME, str};
        }
        Cursor query;
        try {
            int columnIndex;
            query = this.contentResolver.query(this.videoSourceUri, new String[]{"count(_id) as result"}, str2, strArr, null);
            if (query == null) {
                try {
                    Log.w(TAG, "Unknown error");
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query.moveToFirst()) {
                columnIndex = query.getColumnIndex("result");
                if (columnIndex != -1) {
                    query.moveToFirst();
                    columnIndex = query.getInt(columnIndex);
                }
            } else {
                Log.w(TAG, "No media on the device");
                columnIndex = 0;
            }
            if (query != null) {
                query.close();
            }
            return columnIndex;
            columnIndex = 0;
            if (query != null) {
                query.close();
            }
            return columnIndex;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public void deleteImages(int[] iArr) {
        String str = HttpVersions.HTTP_0_9;
        int length = iArr.length;
        String[] strArr = new String[length];
        for (int i = 0; i < iArr.length; i++) {
            strArr[i] = String.valueOf(iArr[i]);
            str = str + "_id=?";
            if (i < length - 1) {
                str = str + " OR ";
            }
        }
        this.contentResolver.delete(this.imageSourceUri, str, strArr);
    }

    public void deleteImages(Integer[] numArr) {
        String str = HttpVersions.HTTP_0_9;
        int length = numArr.length;
        String[] strArr = new String[length];
        for (int i = 0; i < numArr.length; i++) {
            strArr[i] = String.valueOf(numArr[i]);
            str = str + "_id=?";
            if (i < length - 1) {
                str = str + " OR ";
            }
        }
        this.contentResolver.delete(this.imageSourceUri, str, strArr);
    }

    public void deleteMedia(int i) {
        this.contentResolver.delete(this.imageSourceUri, "_id=?", new String[]{String.valueOf(i)});
    }

    public void deleteMedia(File file) {
        String name = file.getName();
        String[] strArr = new String[]{name, FileUtils.MEDIA_PUBLIC_FOLDER_NAME};
        if (name.endsWith("jpg")) {
            this.contentResolver.delete(this.imageSourceUri, "_display_name=? and bucket_display_name=?", strArr);
        } else if (name.endsWith("mp4")) {
            this.contentResolver.delete(this.videoSourceUri, "_display_name=? and bucket_display_name=?", strArr);
        }
    }

    public void deleteMedia(String str) {
        deleteMedia(new File(str));
    }

    public void deleteVideos(int[] iArr) {
        String str = HttpVersions.HTTP_0_9;
        int length = iArr.length;
        String[] strArr = new String[length];
        for (int i = 0; i < iArr.length; i++) {
            strArr[i] = String.valueOf(iArr[i]);
            str = str + "_id=?";
            if (i < length - 1) {
                str = str + " OR ";
            }
        }
        this.contentResolver.delete(this.videoSourceUri, str, strArr);
    }

    public void deleteVideos(Integer[] numArr) {
        String str = HttpVersions.HTTP_0_9;
        int length = numArr.length;
        String[] strArr = new String[length];
        for (int i = 0; i < numArr.length; i++) {
            strArr[i] = String.valueOf(numArr[i]);
            str = str + "_id=?";
            if (i < length - 1) {
                str = str + " OR ";
            }
        }
        this.contentResolver.delete(this.videoSourceUri, str, strArr);
    }

    public Cursor getAllMediaCursor() {
        return new MergeCursor(new Cursor[]{getImagesCursor(), getVideosCursor()});
    }

    public Cursor getImagesCursor() {
        return getMediaCursor(this.imageSourceUri, null);
    }

    public ArrayList<MediaVO> getMediaImageList() {
        return getMediaImageList(null);
    }

    public ArrayList<MediaVO> getMediaImageList(String str) {
        ArrayList<MediaVO> arrayList = new ArrayList();
        addMedia(arrayList, this.imageSourceUri, str);
        return arrayList;
    }

    public ArrayList<MediaVO> getMediaVideoList() {
        return getMediaVideoList(null);
    }

    public ArrayList<MediaVO> getMediaVideoList(String str) {
        ArrayList<MediaVO> arrayList = new ArrayList();
        addMedia(arrayList, this.videoSourceUri, str);
        return arrayList;
    }

    public String getPathOf(Uri uri) {
        Throwable th;
        Cursor cursor = null;
        try {
            Cursor query = this.contentResolver.query(uri, new String[]{"_data"}, null, null, null);
            if (query == null) {
                try {
                    Log.w(TAG, "Unknown error");
                } catch (Throwable th2) {
                    th = th2;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } else if (query.moveToFirst()) {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                query.moveToFirst();
                String string = query.getString(columnIndexOrThrow);
                if (query == null) {
                    return string;
                }
                query.close();
                return string;
            } else {
                Log.w(TAG, "No media on the device");
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public String getUserboxByPath(String str) {
        String[] strArr;
        Uri uri;
        String str2;
        Throwable th;
        Cursor cursor = null;
        if (str.endsWith("mp4")) {
            strArr = new String[]{PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION};
            uri = this.videoSourceUri;
            str2 = "_data=?";
        } else if (str.endsWith("jpg")) {
            strArr = new String[]{PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION};
            uri = this.imageSourceUri;
            str2 = "_data=?";
        } else {
            str2 = null;
            strArr = null;
            uri = null;
        }
        try {
            String string;
            Cursor query = this.contentResolver.query(uri, strArr, str2, new String[]{str}, null);
            if (query == null) {
                try {
                    Log.w(TAG, "Unknown error");
                } catch (Throwable th2) {
                    th = th2;
                    cursor = query;
                    cursor.close();
                    throw th;
                }
            } else if (query.moveToFirst()) {
                int columnIndexOrThrow = query.getColumnIndexOrThrow(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION);
                query.moveToFirst();
                string = query.getString(columnIndexOrThrow);
                query.close();
                return string;
            } else {
                Log.w(TAG, "No media on the device");
            }
            query.close();
            return string;
        } catch (Throwable th3) {
            th = th3;
            cursor.close();
            throw th;
        }
    }

    public long getVideoCreationTime(Uri uri) {
        Throwable th;
        String lastPathSegment = uri.getLastPathSegment();
        Cursor query;
        try {
            long j;
            String[] strArr = new String[]{lastPathSegment};
            Uri uri2 = uri;
            query = this.contentResolver.query(uri2, new String[]{"_id", "datetaken"}, "_id=?", strArr, null);
            if (query == null) {
                try {
                    Log.w(TAG, "Unknown error");
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query.moveToFirst()) {
                int columnIndex = query.getColumnIndex("datetaken");
                if (columnIndex != -1) {
                    query.moveToFirst();
                    j = query.getLong(columnIndex);
                }
            } else {
                Log.w(TAG, "No media on the device");
                j = 0;
            }
            if (query != null) {
                query.close();
            }
            return j;
            j = 0;
            if (query != null) {
                query.close();
            }
            return j;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    public Cursor getVideosCursor() {
        return getMediaCursor(this.videoSourceUri, null);
    }

    public void insertMedia(File file) throws RuntimeException {
        insertMedia(file, (String) null);
    }

    @SuppressLint({"NewApi"})
    public void insertMedia(File file, String str) {
        IOException e;
        String name = file.getName();
        ContentValues contentValues;
        if (name.endsWith("jpg")) {
            contentValues = new ContentValues();
            contentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, name);
            contentValues.put("_display_name", name);
            contentValues.put("bucket_display_name", FileUtils.MEDIA_PUBLIC_FOLDER_NAME);
            contentValues.put("_data", file.getAbsolutePath());
            contentValues.put("mime_type", "image/jpg");
            contentValues.put("date_added", Long.valueOf(System.currentTimeMillis() / 1000));
            try {
                contentValues.put("datetaken", Long.valueOf(new SimpleDateFormat("'picture_'yyyyMMdd'_'HHmmss.'jpg'", Locale.ENGLISH).parse(name).getTime()));
            } catch (ParseException e2) {
                contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
                Log.w(TAG, "Unable to parse date from filename " + name + ", using current time as date taken");
                e2.printStackTrace();
            }
            try {
                try {
                    float[] fArr = new float[2];
                    if (new ExifInterface(file.getAbsolutePath()).getLatLong(fArr)) {
                        contentValues.put("latitude", Float.valueOf(fArr[0]));
                        contentValues.put("longitude", Float.valueOf(fArr[1]));
                    }
                } catch (IOException e3) {
                    e = e3;
                    e.printStackTrace();
                    if (str != null) {
                        contentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, str);
                    }
                    this.contentResolver.insert(this.imageSourceUri, contentValues);
                }
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                if (str != null) {
                    contentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, str);
                }
                this.contentResolver.insert(this.imageSourceUri, contentValues);
            }
            if (str != null) {
                contentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, str);
            }
            this.contentResolver.insert(this.imageSourceUri, contentValues);
        } else if (name.endsWith("mp4")) {
            contentValues = new ContentValues();
            contentValues.put("_display_name", name);
            contentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, name);
            contentValues.put("bucket_display_name", FileUtils.MEDIA_PUBLIC_FOLDER_NAME);
            contentValues.put("_data", file.getAbsolutePath());
            try {
                contentValues.put("datetaken", Long.valueOf(new SimpleDateFormat("'video_'yyyyMMdd'_'HHmmss.'mp4'", Locale.ENGLISH).parse(name).getTime()));
            } catch (ParseException e22) {
                contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
                Log.w(TAG, "Unable to parse date from filename " + name + ", using current time as date taken");
                e22.printStackTrace();
            }
            contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
            contentValues.put("mime_type", "video/mp4");
            contentValues.put("isprivate", Integer.valueOf(0));
            contentValues.put("artist", HttpVersions.HTTP_0_9);
            contentValues.put("album", FileUtils.MEDIA_PUBLIC_FOLDER_NAME);
            if (str != null) {
                contentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, str);
            }
            if (VERSION.SDK_INT >= 11) {
                try {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
                    name = mediaMetadataRetriever.extractMetadata(18);
                    String extractMetadata = mediaMetadataRetriever.extractMetadata(19);
                    if (!(name == null || extractMetadata == null)) {
                        contentValues.put("resolution", name + "x" + extractMetadata);
                        contentValues.put("width", name);
                        contentValues.put("height", extractMetadata);
                    }
                    name = mediaMetadataRetriever.extractMetadata(12);
                    if (name != null) {
                        contentValues.put("mime_type", name);
                    }
                    String extractMetadata2 = mediaMetadataRetriever.extractMetadata(9);
                    if (extractMetadata2 != null) {
                        contentValues.put("duration", Integer.valueOf(Integer.parseInt(extractMetadata2)));
                    }
                } catch (RuntimeException e5) {
                    Log.w(TAG, "Can't get metadata from the file. Looks like it is corrupted.");
                }
            }
            this.contentResolver.insert(this.videoSourceUri, contentValues);
        }
    }

    public void onDestroy() {
        Log.d(TAG, "Media scaner: OnDestroyReceived [DISCONNECTING]");
        if (this.connection != null) {
            this.connection.disconnect();
        }
    }

    public void onMediaScannerConnected() {
        Log.d(TAG, "Media scanner [CONNECTED]");
        if (this.mediaToScan.isEmpty()) {
            Log.d(TAG, "Media scaner: No media in queue [DISCONNECTING]");
            this.connection.disconnect();
            return;
        }
        this.connection.scanFile((String) this.mediaToScan.get(0), null);
    }

    public void onScanCompleted(String str, Uri uri) {
        Log.d(TAG, "File " + str + " has been added to media gallery");
        synchronized (this.mediaToScan) {
            this.mediaToScan.remove(str);
            if (this.mediaToScan.isEmpty()) {
                Log.d(TAG, "Media scaner: No media in queue [DISCONNECTING]");
                this.connection.disconnect();
            } else {
                this.connection.scanFile((String) this.mediaToScan.get(0), null);
            }
        }
    }

    @Deprecated
    public void scanMediaFile(File file) {
        scanMediaFile(file.getAbsolutePath());
    }

    @Deprecated
    public void scanMediaFile(String str) {
        if (this.connection.isConnected()) {
            this.connection.scanFile(str, null);
            return;
        }
        synchronized (this.mediaToScan) {
            this.mediaToScan.add(str);
        }
        this.connection.connect();
    }
}
