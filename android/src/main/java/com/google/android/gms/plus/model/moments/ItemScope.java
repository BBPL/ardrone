package com.google.android.gms.plus.model.moments;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.internal.bx;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ItemScope extends Freezable<ItemScope> {

    public static class Builder {
        private String di;
        private double fy;
        private double fz;
        private final Set<Integer> iD = new HashSet();
        private bx iE;
        private List<String> iF;
        private bx iG;
        private String iH;
        private String iI;
        private String iJ;
        private List<bx> iK;
        private int iL;
        private List<bx> iM;
        private bx iN;
        private List<bx> iO;
        private String iP;
        private String iQ;
        private bx iR;
        private String iS;
        private String iT;
        private String iU;
        private List<bx> iV;
        private String iW;
        private String iX;
        private String iY;
        private String iZ;
        private String ie;
        private String jA;
        private String ja;
        private String jb;
        private String jc;
        private String jd;
        private bx je;
        private String jf;
        private String jg;
        private String jh;
        private String ji;
        private bx jj;
        private bx jk;
        private bx jl;
        private List<bx> jm;
        private String jn;
        private String jo;
        private String jp;
        private String jq;
        private bx jr;
        private String js;
        private String jt;
        private String ju;
        private bx jv;
        private String jw;
        private String jx;
        private String jy;
        private String jz;
        private String mName;

        public ItemScope build() {
            return new bx(this.iD, this.iE, this.iF, this.iG, this.iH, this.iI, this.iJ, this.iK, this.iL, this.iM, this.iN, this.iO, this.iP, this.iQ, this.iR, this.iS, this.iT, this.iU, this.iV, this.iW, this.iX, this.iY, this.di, this.iZ, this.ja, this.jb, this.jc, this.jd, this.je, this.jf, this.jg, this.jh, this.ji, this.jj, this.fy, this.jk, this.fz, this.mName, this.jl, this.jm, this.jn, this.jo, this.jp, this.jq, this.jr, this.js, this.jt, this.ju, this.jv, this.jw, this.jx, this.jy, this.ie, this.jz, this.jA);
        }

        public Builder setAbout(ItemScope itemScope) {
            this.iE = (bx) itemScope;
            this.iD.add(Integer.valueOf(2));
            return this;
        }

        public Builder setAdditionalName(List<String> list) {
            this.iF = list;
            this.iD.add(Integer.valueOf(3));
            return this;
        }

        public Builder setAddress(ItemScope itemScope) {
            this.iG = (bx) itemScope;
            this.iD.add(Integer.valueOf(4));
            return this;
        }

        public Builder setAddressCountry(String str) {
            this.iH = str;
            this.iD.add(Integer.valueOf(5));
            return this;
        }

        public Builder setAddressLocality(String str) {
            this.iI = str;
            this.iD.add(Integer.valueOf(6));
            return this;
        }

        public Builder setAddressRegion(String str) {
            this.iJ = str;
            this.iD.add(Integer.valueOf(7));
            return this;
        }

        public Builder setAssociated_media(List<ItemScope> list) {
            this.iK = list;
            this.iD.add(Integer.valueOf(8));
            return this;
        }

        public Builder setAttendeeCount(int i) {
            this.iL = i;
            this.iD.add(Integer.valueOf(9));
            return this;
        }

        public Builder setAttendees(List<ItemScope> list) {
            this.iM = list;
            this.iD.add(Integer.valueOf(10));
            return this;
        }

        public Builder setAudio(ItemScope itemScope) {
            this.iN = (bx) itemScope;
            this.iD.add(Integer.valueOf(11));
            return this;
        }

        public Builder setAuthor(List<ItemScope> list) {
            this.iO = list;
            this.iD.add(Integer.valueOf(12));
            return this;
        }

        public Builder setBestRating(String str) {
            this.iP = str;
            this.iD.add(Integer.valueOf(13));
            return this;
        }

        public Builder setBirthDate(String str) {
            this.iQ = str;
            this.iD.add(Integer.valueOf(14));
            return this;
        }

        public Builder setByArtist(ItemScope itemScope) {
            this.iR = (bx) itemScope;
            this.iD.add(Integer.valueOf(15));
            return this;
        }

        public Builder setCaption(String str) {
            this.iS = str;
            this.iD.add(Integer.valueOf(16));
            return this;
        }

        public Builder setContentSize(String str) {
            this.iT = str;
            this.iD.add(Integer.valueOf(17));
            return this;
        }

        public Builder setContentUrl(String str) {
            this.iU = str;
            this.iD.add(Integer.valueOf(18));
            return this;
        }

        public Builder setContributor(List<ItemScope> list) {
            this.iV = list;
            this.iD.add(Integer.valueOf(19));
            return this;
        }

        public Builder setDateCreated(String str) {
            this.iW = str;
            this.iD.add(Integer.valueOf(20));
            return this;
        }

        public Builder setDateModified(String str) {
            this.iX = str;
            this.iD.add(Integer.valueOf(21));
            return this;
        }

        public Builder setDatePublished(String str) {
            this.iY = str;
            this.iD.add(Integer.valueOf(22));
            return this;
        }

        public Builder setDescription(String str) {
            this.di = str;
            this.iD.add(Integer.valueOf(23));
            return this;
        }

        public Builder setDuration(String str) {
            this.iZ = str;
            this.iD.add(Integer.valueOf(24));
            return this;
        }

        public Builder setEmbedUrl(String str) {
            this.ja = str;
            this.iD.add(Integer.valueOf(25));
            return this;
        }

        public Builder setEndDate(String str) {
            this.jb = str;
            this.iD.add(Integer.valueOf(26));
            return this;
        }

        public Builder setFamilyName(String str) {
            this.jc = str;
            this.iD.add(Integer.valueOf(27));
            return this;
        }

        public Builder setGender(String str) {
            this.jd = str;
            this.iD.add(Integer.valueOf(28));
            return this;
        }

        public Builder setGeo(ItemScope itemScope) {
            this.je = (bx) itemScope;
            this.iD.add(Integer.valueOf(29));
            return this;
        }

        public Builder setGivenName(String str) {
            this.jf = str;
            this.iD.add(Integer.valueOf(30));
            return this;
        }

        public Builder setHeight(String str) {
            this.jg = str;
            this.iD.add(Integer.valueOf(31));
            return this;
        }

        public Builder setId(String str) {
            this.jh = str;
            this.iD.add(Integer.valueOf(32));
            return this;
        }

        public Builder setImage(String str) {
            this.ji = str;
            this.iD.add(Integer.valueOf(33));
            return this;
        }

        public Builder setInAlbum(ItemScope itemScope) {
            this.jj = (bx) itemScope;
            this.iD.add(Integer.valueOf(34));
            return this;
        }

        public Builder setLatitude(double d) {
            this.fy = d;
            this.iD.add(Integer.valueOf(36));
            return this;
        }

        public Builder setLocation(ItemScope itemScope) {
            this.jk = (bx) itemScope;
            this.iD.add(Integer.valueOf(37));
            return this;
        }

        public Builder setLongitude(double d) {
            this.fz = d;
            this.iD.add(Integer.valueOf(38));
            return this;
        }

        public Builder setName(String str) {
            this.mName = str;
            this.iD.add(Integer.valueOf(39));
            return this;
        }

        public Builder setPartOfTVSeries(ItemScope itemScope) {
            this.jl = (bx) itemScope;
            this.iD.add(Integer.valueOf(40));
            return this;
        }

        public Builder setPerformers(List<ItemScope> list) {
            this.jm = list;
            this.iD.add(Integer.valueOf(41));
            return this;
        }

        public Builder setPlayerType(String str) {
            this.jn = str;
            this.iD.add(Integer.valueOf(42));
            return this;
        }

        public Builder setPostOfficeBoxNumber(String str) {
            this.jo = str;
            this.iD.add(Integer.valueOf(43));
            return this;
        }

        public Builder setPostalCode(String str) {
            this.jp = str;
            this.iD.add(Integer.valueOf(44));
            return this;
        }

        public Builder setRatingValue(String str) {
            this.jq = str;
            this.iD.add(Integer.valueOf(45));
            return this;
        }

        public Builder setReviewRating(ItemScope itemScope) {
            this.jr = (bx) itemScope;
            this.iD.add(Integer.valueOf(46));
            return this;
        }

        public Builder setStartDate(String str) {
            this.js = str;
            this.iD.add(Integer.valueOf(47));
            return this;
        }

        public Builder setStreetAddress(String str) {
            this.jt = str;
            this.iD.add(Integer.valueOf(48));
            return this;
        }

        public Builder setText(String str) {
            this.ju = str;
            this.iD.add(Integer.valueOf(49));
            return this;
        }

        public Builder setThumbnail(ItemScope itemScope) {
            this.jv = (bx) itemScope;
            this.iD.add(Integer.valueOf(50));
            return this;
        }

        public Builder setThumbnailUrl(String str) {
            this.jw = str;
            this.iD.add(Integer.valueOf(51));
            return this;
        }

        public Builder setTickerSymbol(String str) {
            this.jx = str;
            this.iD.add(Integer.valueOf(52));
            return this;
        }

        public Builder setType(String str) {
            this.jy = str;
            this.iD.add(Integer.valueOf(53));
            return this;
        }

        public Builder setUrl(String str) {
            this.ie = str;
            this.iD.add(Integer.valueOf(54));
            return this;
        }

        public Builder setWidth(String str) {
            this.jz = str;
            this.iD.add(Integer.valueOf(55));
            return this;
        }

        public Builder setWorstRating(String str) {
            this.jA = str;
            this.iD.add(Integer.valueOf(56));
            return this;
        }
    }

    ItemScope getAbout();

    List<String> getAdditionalName();

    ItemScope getAddress();

    String getAddressCountry();

    String getAddressLocality();

    String getAddressRegion();

    List<ItemScope> getAssociated_media();

    int getAttendeeCount();

    List<ItemScope> getAttendees();

    ItemScope getAudio();

    List<ItemScope> getAuthor();

    String getBestRating();

    String getBirthDate();

    ItemScope getByArtist();

    String getCaption();

    String getContentSize();

    String getContentUrl();

    List<ItemScope> getContributor();

    String getDateCreated();

    String getDateModified();

    String getDatePublished();

    String getDescription();

    String getDuration();

    String getEmbedUrl();

    String getEndDate();

    String getFamilyName();

    String getGender();

    ItemScope getGeo();

    String getGivenName();

    String getHeight();

    String getId();

    String getImage();

    ItemScope getInAlbum();

    double getLatitude();

    ItemScope getLocation();

    double getLongitude();

    String getName();

    ItemScope getPartOfTVSeries();

    List<ItemScope> getPerformers();

    String getPlayerType();

    String getPostOfficeBoxNumber();

    String getPostalCode();

    String getRatingValue();

    ItemScope getReviewRating();

    String getStartDate();

    String getStreetAddress();

    String getText();

    ItemScope getThumbnail();

    String getThumbnailUrl();

    String getTickerSymbol();

    String getType();

    String getUrl();

    String getWidth();

    String getWorstRating();

    boolean hasAbout();

    boolean hasAdditionalName();

    boolean hasAddress();

    boolean hasAddressCountry();

    boolean hasAddressLocality();

    boolean hasAddressRegion();

    boolean hasAssociated_media();

    boolean hasAttendeeCount();

    boolean hasAttendees();

    boolean hasAudio();

    boolean hasAuthor();

    boolean hasBestRating();

    boolean hasBirthDate();

    boolean hasByArtist();

    boolean hasCaption();

    boolean hasContentSize();

    boolean hasContentUrl();

    boolean hasContributor();

    boolean hasDateCreated();

    boolean hasDateModified();

    boolean hasDatePublished();

    boolean hasDescription();

    boolean hasDuration();

    boolean hasEmbedUrl();

    boolean hasEndDate();

    boolean hasFamilyName();

    boolean hasGender();

    boolean hasGeo();

    boolean hasGivenName();

    boolean hasHeight();

    boolean hasId();

    boolean hasImage();

    boolean hasInAlbum();

    boolean hasLatitude();

    boolean hasLocation();

    boolean hasLongitude();

    boolean hasName();

    boolean hasPartOfTVSeries();

    boolean hasPerformers();

    boolean hasPlayerType();

    boolean hasPostOfficeBoxNumber();

    boolean hasPostalCode();

    boolean hasRatingValue();

    boolean hasReviewRating();

    boolean hasStartDate();

    boolean hasStreetAddress();

    boolean hasText();

    boolean hasThumbnail();

    boolean hasThumbnailUrl();

    boolean hasTickerSymbol();

    boolean hasType();

    boolean hasUrl();

    boolean hasWidth();

    boolean hasWorstRating();
}
