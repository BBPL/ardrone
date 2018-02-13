package com.parrot.freeflight.academy.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Media implements Serializable, Parcelable {
    private static final String ID = "id";
    private static final String MODIFICATION_DATE = "modification_date";
    private static final String URL = "url";
    private static final String VISIBLE = "visible";
    private static final long serialVersionUID = -5446058774284115561L;
    private int id;
    private String modification_date;
    private String url;
    private boolean visible;

    public Media(JSONObject jSONObject) throws JSONException {
        setUrl(jSONObject.getString("url"));
        setVisible(jSONObject.getBoolean("visible"));
        setId(jSONObject.getInt(ID));
        setModification_date(jSONObject.getString(MODIFICATION_DATE));
    }

    public static ArrayList<Media> jsonArrayToMedias(JSONArray jSONArray) throws JSONException {
        int length = jSONArray.length();
        ArrayList<Media> arrayList = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            try {
                Media media = new Media(jSONArray.getJSONObject(i));
                if (media != null) {
                    arrayList.add(media);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public int describeContents() {
        return 1;
    }

    public int getId() {
        return this.id;
    }

    public String getModification_date() {
        return this.modification_date;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setModification_date(String str) {
        this.modification_date = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeBooleanArray(new boolean[]{this.visible});
        parcel.writeInt(this.id);
        parcel.writeString(this.modification_date);
    }
}
