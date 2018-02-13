package com.parrot.freeflight.vo;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.File;

public abstract class RemoteMediaVO extends MediaVO implements Parcelable {
    private String path;

    public RemoteMediaVO(Parcel parcel) {
        super(parcel);
        this.path = parcel.readString();
    }

    public File getFilePath() {
        return null;
    }

    public String getKey() {
        return this.path;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isRemote() {
        return true;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.path);
    }
}
