package com.google.api.services.picasa.model;

import com.google.api.client.util.Key;
import com.google.api.client.xml.GenericXml;
import java.util.List;

public class Photo extends GenericXml {
    @Key("atom:category")
    private Category category = Category.newKind("photo");
    @Key("@gd:etag")
    private String etag;
    @Key("exif:tags")
    private ExifTags exifTags;
    @Key("atom:id")
    private String id;
    @Key("atom:link")
    private List<Link> links;
    @Key("media:group")
    private MediaGroup mediaGroup;
    @Key("atom:summary")
    private String summary;
    @Key("atom:title")
    private String title;

    public Category getCategory() {
        return this.category;
    }

    public String getEditUrl() {
        return Link.find(this.links, "edit");
    }

    public String getEtag() {
        return this.etag;
    }

    public String getFeedUrl() {
        return Link.find(this.links, "http://schemas.google.com/g/2005#feed");
    }

    public String getId() {
        return this.id;
    }

    public MediaGroup getMediaGroup() {
        return this.mediaGroup;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setEtag(String str) {
        this.etag = str;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setMediaGroup(MediaGroup mediaGroup) {
        this.mediaGroup = mediaGroup;
    }

    public void setSummary(String str) {
        this.summary = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
