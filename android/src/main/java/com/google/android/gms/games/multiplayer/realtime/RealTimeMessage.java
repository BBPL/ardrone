package com.google.android.gms.games.multiplayer.realtime;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.C0242s;

public final class RealTimeMessage implements Parcelable {
    public static final Creator<RealTimeMessage> CREATOR = new C01011();
    public static final int RELIABLE = 1;
    public static final int UNRELIABLE = 0;
    private final String eT;
    private final byte[] eU;
    private final int eV;

    static final class C01011 implements Creator<RealTimeMessage> {
        C01011() {
        }

        public RealTimeMessage[] m229J(int i) {
            return new RealTimeMessage[i];
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return m230r(parcel);
        }

        public /* synthetic */ Object[] newArray(int i) {
            return m229J(i);
        }

        public RealTimeMessage m230r(Parcel parcel) {
            return new RealTimeMessage(parcel);
        }
    }

    private RealTimeMessage(Parcel parcel) {
        this(parcel.readString(), parcel.createByteArray(), parcel.readInt());
    }

    public RealTimeMessage(String str, byte[] bArr, int i) {
        this.eT = (String) C0242s.m1208d(str);
        this.eU = (byte[]) ((byte[]) C0242s.m1208d(bArr)).clone();
        this.eV = i;
    }

    public int describeContents() {
        return 0;
    }

    public byte[] getMessageData() {
        return this.eU;
    }

    public String getSenderParticipantId() {
        return this.eT;
    }

    public boolean isReliable() {
        return this.eV == 1;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.eT);
        parcel.writeByteArray(this.eU);
        parcel.writeInt(this.eV);
    }
}
