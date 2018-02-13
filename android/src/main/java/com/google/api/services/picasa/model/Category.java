package com.google.api.services.picasa.model;

import com.google.api.client.util.Key;

public class Category {
    @Key("@scheme")
    private String scheme;
    @Key("@term")
    private String term;

    public static Category newKind(String str) {
        Category category = new Category();
        category.scheme = "http://schemas.google.com/g/2005#kind";
        category.term = "http://schemas.google.com/photos/2007#" + str;
        return category;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getTerm() {
        return this.term;
    }

    public void setScheme(String str) {
        this.scheme = str;
    }

    public void setTerm(String str) {
        this.term = str;
    }
}
