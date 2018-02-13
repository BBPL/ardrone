package com.parrot.freeflight.academy.model;

public class OnlineInfoItem {
    private String name;
    private String value;

    public OnlineInfoItem(String str, String str2) {
        this.name = str;
        this.value = str2;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setValue(String str) {
        this.value = str;
    }
}
