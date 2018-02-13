package com.google.api.services.picasa.model;

import com.google.api.client.util.Key;
import java.util.List;
import org.mortbay.util.URIUtil;

public class AlbumEntry {
    @Key("gphoto:access")
    private String access;
    @Key("atom:category")
    private Category category = Category.newKind("album");
    @Key("atom:id")
    private String id;
    @Key("atom:rights")
    private String rights;
    @Key("atom:summary")
    private String summary;
    @Key("atom:title")
    private String title;

    public static String getEditLink(List<Link> list) {
        return list != null ? Link.find(list, "edit") : null;
    }

    public String getAccess() {
        return this.access;
    }

    public String getAlbumId() {
        return this.id != null ? this.id.substring(this.id.lastIndexOf(URIUtil.SLASH) + 1) : null;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getId() {
        return this.id;
    }

    public String getRights() {
        return this.rights;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getTitle() {
        return this.title;
    }

    public void setAccess(String str) {
        this.access = str;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setRights(String str) {
        this.rights = str;
    }

    public void setSummary(String str) {
        this.summary = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String toString() {
        return this.title;
    }
}
