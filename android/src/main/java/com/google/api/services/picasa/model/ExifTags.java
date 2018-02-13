package com.google.api.services.picasa.model;

import com.google.api.client.util.Key;

public class ExifTags {
    @Key("exif:exposure")
    private float exposure;
    @Key("exif:focallength")
    private float folallength;
    @Key("exif:fstop")
    private float fstop;
    @Key("exif:imageUniqueID")
    private String imageUniqueID;
    @Key("exif:iso")
    private int iso;
    @Key("exif:make")
    private String make;
    @Key("exif:model")
    private String model;
    @Key("exif:time")
    private long time;

    public float getExposure() {
        return this.exposure;
    }

    public float getFolallength() {
        return this.folallength;
    }

    public float getFstop() {
        return this.fstop;
    }

    public String getImageUniqueID() {
        return this.imageUniqueID;
    }

    public int getIso() {
        return this.iso;
    }

    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public long getTime() {
        return this.time;
    }

    public void setExposure(float f) {
        this.exposure = f;
    }

    public void setFolallength(float f) {
        this.folallength = f;
    }

    public void setFstop(float f) {
        this.fstop = f;
    }

    public void setImageUniqueID(String str) {
        this.imageUniqueID = str;
    }

    public void setIso(int i) {
        this.iso = i;
    }

    public void setMake(String str) {
        this.make = str;
    }

    public void setModel(String str) {
        this.model = str;
    }

    public void setTime(long j) {
        this.time = j;
    }
}
