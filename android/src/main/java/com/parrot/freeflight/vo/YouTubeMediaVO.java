package com.parrot.freeflight.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.mortbay.util.URIUtil;

public class YouTubeMediaVO extends RemoteMediaVO implements Parcelable {
    public static final Creator<YouTubeMediaVO> CREATOR = new C12561();
    private String videoId;

    static final class C12561 implements Creator<YouTubeMediaVO> {
        C12561() {
        }

        public YouTubeMediaVO createFromParcel(Parcel parcel) {
            return new YouTubeMediaVO(parcel);
        }

        public YouTubeMediaVO[] newArray(int i) {
            return new YouTubeMediaVO[i];
        }
    }

    public YouTubeMediaVO(Parcel parcel) {
        super(parcel);
        this.videoId = parcel.readString();
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setPath(String str) {
        super.setPath(str);
        this.videoId = str.substring(str.lastIndexOf(URIUtil.SLASH) + 1);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.videoId);
    }
}
