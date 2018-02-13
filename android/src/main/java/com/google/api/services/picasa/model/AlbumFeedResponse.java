package com.google.api.services.picasa.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import com.google.api.client.xml.GenericXml;
import java.util.List;

public class AlbumFeedResponse extends GenericXml {
    @Key("atom:entry")
    private List<AlbumEntry> albums;
    @Key("atom:author")
    private Author author;
    @Key("atom:id")
    private String id;
    @Key("atom:link")
    public List<Link> links;
    @Key("atom:title")
    private String title;
    @Key("openSearch:totalResults")
    private int totalResults;

    static {
        Data.nullOf(AlbumEntry.class);
    }

    public Author getAuthor() {
        return this.author;
    }

    public String getId() {
        return this.id;
    }

    public List<AlbumEntry> getItems() {
        return this.albums;
    }

    public String getPostLink() {
        return Link.find(this.links, "http://schemas.google.com/g/2005#post");
    }

    public String getTitle() {
        return this.title;
    }

    public int getTotalResults() {
        return this.totalResults;
    }

    public AlbumFeedResponse setAuthor(Author author) {
        this.author = author;
        return this;
    }

    public AlbumFeedResponse setId(String str) {
        this.id = str;
        return this;
    }

    public AlbumFeedResponse setItems(List<AlbumEntry> list) {
        this.albums = list;
        return this;
    }

    public AlbumFeedResponse setTitle(String str) {
        this.title = str;
        return this;
    }

    public AlbumFeedResponse setTotalResults(int i) {
        this.totalResults = i;
        return this;
    }
}
