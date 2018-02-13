package com.parrot.freeflight.academy.model;

public class PrivacyItem {
    private String name;
    private int state;

    public PrivacyItem(String str, int i) {
        this.name = str;
        this.state = i;
    }

    public String getName() {
        return this.name;
    }

    public int getState() {
        return this.state;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setState(int i) {
        this.state = i;
    }
}
