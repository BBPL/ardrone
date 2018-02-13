package com.google.api.services.picasa;

import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.xml.atom.AtomContent;
import com.google.api.client.util.Key;
import com.google.api.client.xml.XmlObjectParser;
import com.google.api.services.picasa.model.AlbumEntry;
import com.google.api.services.picasa.model.AlbumFeedResponse;
import com.google.api.services.picasa.model.Photo;
import java.io.IOException;
import org.mortbay.jetty.security.Constraint;

public class Picasa extends AbstractGoogleClient {

    public static class Builder extends com.google.api.client.googleapis.services.AbstractGoogleClient.Builder {
        public Builder(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer) {
            HttpTransport httpTransport2 = httpTransport;
            super(httpTransport2, "https://picasaweb.google.com/", "data/", new XmlObjectParser(PicasaAtom.NAMESPACE_DICTIONARY), httpRequestInitializer);
        }

        public Picasa build() {
            return new Picasa(this);
        }

        public Builder setApplicationName(String str) {
            return (Builder) super.setApplicationName(str);
        }
    }

    public class Feed {

        public class Insert extends PicasaRequest<AlbumEntry> {
            private static final String REST_PATH = "feed/api/user/default";

            protected Insert(AlbumEntry albumEntry) {
                super(Picasa.this, "POST", REST_PATH, AtomContent.forEntry(PicasaAtom.NAMESPACE_DICTIONARY, albumEntry), AlbumEntry.class);
            }
        }

        public class List extends PicasaRequest<AlbumFeedResponse> {
            private static final String REST_PATH = "feed/api/user/default";
            @Key
            private String access;
            @Key
            private String kind;

            protected List() {
                super(Picasa.this, "GET", REST_PATH, null, AlbumFeedResponse.class);
            }

            public String getAccess() {
                return this.access;
            }

            public String getKind() {
                return this.kind;
            }

            public void setAccess(String str) {
                this.access = str;
            }

            public void setKind(String str) {
                this.kind = str;
            }
        }

        public Insert insert(AlbumEntry albumEntry) {
            return new Insert(albumEntry);
        }

        public List list() throws IOException {
            AbstractGoogleClientRequest list = new List();
            Picasa.this.initialize(list);
            return list;
        }
    }

    public class Photos {
        private static final String REST_PATH = "feed/api/user/default/albumid/";

        public class Insert extends PicasaRequest<Photo> {
            protected Insert(Photo photo, String str, AbstractInputStreamContent abstractInputStreamContent) {
                super(Picasa.this, "POST", Photos.REST_PATH + str, photo != null ? AtomContent.forEntry(PicasaAtom.NAMESPACE_DICTIONARY, photo) : null, Photo.class);
                initializeMediaUpload(abstractInputStreamContent);
            }
        }

        public class Update extends PicasaRequest<Photo> {
            protected Update(Photo photo) {
                super(Picasa.this, "PUT", photo.getEditUrl(), AtomContent.forEntry(PicasaAtom.NAMESPACE_DICTIONARY, photo), Photo.class);
                getRequestHeaders().setIfMatch(Constraint.ANY_ROLE);
            }
        }

        public Insert insert(Photo photo, String str, AbstractInputStreamContent abstractInputStreamContent) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(photo, str, abstractInputStreamContent);
            Picasa.this.initialize(insert);
            return insert;
        }

        public Insert insert(String str, AbstractInputStreamContent abstractInputStreamContent) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(null, str, abstractInputStreamContent);
            Picasa.this.initialize(insert);
            return insert;
        }

        public Update update(Photo photo) throws IOException {
            AbstractGoogleClientRequest update = new Update(photo);
            Picasa.this.initialize(update);
            return update;
        }
    }

    Picasa(Builder builder) {
        super(builder);
    }

    public Feed albums() {
        return new Feed();
    }

    protected void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
        super.initialize(abstractGoogleClientRequest);
        abstractGoogleClientRequest.getRequestHeaders().put("GData-Version", (Object) "2.0");
    }

    public Photos photos() {
        return new Photos();
    }
}
