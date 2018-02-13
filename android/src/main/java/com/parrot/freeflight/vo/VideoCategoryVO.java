package com.parrot.freeflight.vo;

public class VideoCategoryVO {
    public String id;
    public String title;

    public VideoCategoryVO(String str, String str2) {
        this.id = str;
        this.title = str2;
    }

    public String toString() {
        return this.title;
    }
}
