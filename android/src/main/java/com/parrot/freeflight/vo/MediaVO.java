package com.parrot.freeflight.vo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.mortbay.jetty.HttpVersions;

public class MediaVO implements Comparable<MediaVO>, Parcelable, Serializable {
    public static final Creator<MediaVO> CREATOR = new C12541();
    private static final long serialVersionUID = -3053808776882538852L;
    public static final Comparator<MediaVO> sortByDateTakenComparator = new C12552();
    private long dateAdded;
    private long dateTaken;
    private int id;
    private boolean isSelected;
    private boolean isVideo;
    private String key;
    private File path;
    private transient Uri uri;

    static final class C12541 implements Creator<MediaVO> {
        C12541() {
        }

        public MediaVO createFromParcel(Parcel parcel) {
            return new MediaVO(parcel);
        }

        public MediaVO[] newArray(int i) {
            return new MediaVO[i];
        }
    }

    static final class C12552 implements Comparator<MediaVO> {
        C12552() {
        }

        public int compare(MediaVO mediaVO, MediaVO mediaVO2) {
            return (int) (mediaVO.getDateTaken() - mediaVO2.getDateTaken());
        }
    }

    public MediaVO(Parcel parcel) {
        this.id = parcel.readInt();
        this.dateAdded = parcel.readLong();
        this.dateTaken = parcel.readLong();
        this.path = (File) parcel.readSerializable();
        boolean[] zArr = new boolean[2];
        parcel.readBooleanArray(zArr);
        this.isVideo = zArr[0];
        this.isSelected = zArr[1];
        this.key = parcel.readString();
        this.uri = (Uri) parcel.readParcelable(null);
    }

    public int compareTo(MediaVO mediaVO) {
        long j = this.dateTaken == 0 ? this.dateAdded * 1000 : this.dateTaken;
        long j2 = mediaVO.getDateTaken() == 0 ? mediaVO.dateAdded * 1000 : mediaVO.dateTaken;
        return j2 != j ? j < j2 ? -1 : 1 : 0;
    }

    public int describeContents() {
        return 1;
    }

    public long getDateAdded() {
        return this.dateAdded;
    }

    public long getDateTaken() {
        return this.dateTaken;
    }

    public File getFilePath() {
        return this.path;
    }

    public int getId() {
        return this.id;
    }

    public String getKey() {
        if (this.key == null) {
            this.key = (this.isVideo ? "video_" : HttpVersions.HTTP_0_9) + this.id;
        }
        return this.key;
    }

    public String getPath() {
        return this.path.getAbsolutePath();
    }

    public Uri getUri() {
        return this.uri;
    }

    public boolean isRemote() {
        return false;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setDateAdded(long j) {
        this.dateAdded = j;
    }

    public void setDateTaken(long j) {
        this.dateTaken = j;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setPath(String str) {
        this.path = new File(str);
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setVideo(boolean z) {
        this.isVideo = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeLong(this.dateAdded);
        parcel.writeLong(this.dateTaken);
        parcel.writeSerializable(this.path);
        parcel.writeBooleanArray(new boolean[]{this.isVideo, this.isSelected});
        parcel.writeString(this.key);
        parcel.writeParcelable(this.uri, i);
    }
}
