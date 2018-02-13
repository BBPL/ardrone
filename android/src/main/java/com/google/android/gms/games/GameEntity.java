package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.C0083j;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.ao;
import com.google.android.gms.internal.av;

public final class GameEntity extends av implements Game {
    public static final Creator<GameEntity> CREATOR = new C0082a();
    private final int ab;
    private final String cl;
    private final String df;
    private final String dg;
    private final String dh;
    private final String di;
    private final String dj;
    private final Uri dk;
    private final Uri dl;
    private final Uri dm;
    private final boolean dn;
    private final boolean f49do;
    private final String dp;
    private final int dq;
    private final int dr;
    private final int ds;

    static final class C0082a extends C0081a {
        C0082a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return mo364n(parcel);
        }

        public GameEntity mo364n(Parcel parcel) {
            if (av.m185c(C0083j.m183v()) || C0083j.m181h(GameEntity.class.getCanonicalName())) {
                return super.mo364n(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            String readString5 = parcel.readString();
            String readString6 = parcel.readString();
            String readString7 = parcel.readString();
            Uri parse = readString7 == null ? null : Uri.parse(readString7);
            readString7 = parcel.readString();
            Uri parse2 = readString7 == null ? null : Uri.parse(readString7);
            readString7 = parcel.readString();
            return new GameEntity(1, readString, readString2, readString3, readString4, readString5, readString6, parse, parse2, readString7 == null ? null : Uri.parse(readString7), parcel.readInt() > 0, parcel.readInt() > 0, parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt());
        }
    }

    GameEntity(int i, String str, String str2, String str3, String str4, String str5, String str6, Uri uri, Uri uri2, Uri uri3, boolean z, boolean z2, String str7, int i2, int i3, int i4) {
        this.ab = i;
        this.df = str;
        this.cl = str2;
        this.dg = str3;
        this.dh = str4;
        this.di = str5;
        this.dj = str6;
        this.dk = uri;
        this.dl = uri2;
        this.dm = uri3;
        this.dn = z;
        this.f49do = z2;
        this.dp = str7;
        this.dq = i2;
        this.dr = i3;
        this.ds = i4;
    }

    public GameEntity(Game game) {
        this.ab = 1;
        this.df = game.getApplicationId();
        this.dg = game.getPrimaryCategory();
        this.dh = game.getSecondaryCategory();
        this.di = game.getDescription();
        this.dj = game.getDeveloperName();
        this.cl = game.getDisplayName();
        this.dk = game.getIconImageUri();
        this.dl = game.getHiResImageUri();
        this.dm = game.getFeaturedImageUri();
        this.dn = game.isPlayEnabledGame();
        this.f49do = game.isInstanceInstalled();
        this.dp = game.getInstancePackageName();
        this.dq = game.getGameplayAclStatus();
        this.dr = game.getAchievementTotalCount();
        this.ds = game.getLeaderboardCount();
    }

    static int m186a(Game game) {
        return C0241r.hashCode(game.getApplicationId(), game.getDisplayName(), game.getPrimaryCategory(), game.getSecondaryCategory(), game.getDescription(), game.getDeveloperName(), game.getIconImageUri(), game.getHiResImageUri(), game.getFeaturedImageUri(), Boolean.valueOf(game.isPlayEnabledGame()), Boolean.valueOf(game.isInstanceInstalled()), game.getInstancePackageName(), Integer.valueOf(game.getGameplayAclStatus()), Integer.valueOf(game.getAchievementTotalCount()), Integer.valueOf(game.getLeaderboardCount()));
    }

    static boolean m187a(Game game, Object obj) {
        if (!(obj instanceof Game)) {
            return false;
        }
        if (game != obj) {
            Game game2 = (Game) obj;
            if (!(C0241r.m1200a(game2.getApplicationId(), game.getApplicationId()) && C0241r.m1200a(game2.getDisplayName(), game.getDisplayName()) && C0241r.m1200a(game2.getPrimaryCategory(), game.getPrimaryCategory()) && C0241r.m1200a(game2.getSecondaryCategory(), game.getSecondaryCategory()) && C0241r.m1200a(game2.getDescription(), game.getDescription()) && C0241r.m1200a(game2.getDeveloperName(), game.getDeveloperName()) && C0241r.m1200a(game2.getIconImageUri(), game.getIconImageUri()) && C0241r.m1200a(game2.getHiResImageUri(), game.getHiResImageUri()) && C0241r.m1200a(game2.getFeaturedImageUri(), game.getFeaturedImageUri()) && C0241r.m1200a(Boolean.valueOf(game2.isPlayEnabledGame()), Boolean.valueOf(game.isPlayEnabledGame())) && C0241r.m1200a(Boolean.valueOf(game2.isInstanceInstalled()), Boolean.valueOf(game.isInstanceInstalled())) && C0241r.m1200a(game2.getInstancePackageName(), game.getInstancePackageName()) && C0241r.m1200a(Integer.valueOf(game2.getGameplayAclStatus()), Integer.valueOf(game.getGameplayAclStatus())) && C0241r.m1200a(Integer.valueOf(game2.getAchievementTotalCount()), Integer.valueOf(game.getAchievementTotalCount())) && C0241r.m1200a(Integer.valueOf(game2.getLeaderboardCount()), Integer.valueOf(game.getLeaderboardCount())))) {
                return false;
            }
        }
        return true;
    }

    static String m188b(Game game) {
        return C0241r.m1201c(game).m1199a("ApplicationId", game.getApplicationId()).m1199a("DisplayName", game.getDisplayName()).m1199a("PrimaryCategory", game.getPrimaryCategory()).m1199a("SecondaryCategory", game.getSecondaryCategory()).m1199a("Description", game.getDescription()).m1199a("DeveloperName", game.getDeveloperName()).m1199a("IconImageUri", game.getIconImageUri()).m1199a("HiResImageUri", game.getHiResImageUri()).m1199a("FeaturedImageUri", game.getFeaturedImageUri()).m1199a("PlayEnabledGame", Boolean.valueOf(game.isPlayEnabledGame())).m1199a("InstanceInstalled", Boolean.valueOf(game.isInstanceInstalled())).m1199a("InstancePackageName", game.getInstancePackageName()).m1199a("GameplayAclStatus", Integer.valueOf(game.getGameplayAclStatus())).m1199a("AchievementTotalCount", Integer.valueOf(game.getAchievementTotalCount())).m1199a("LeaderboardCount", Integer.valueOf(game.getLeaderboardCount())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m187a(this, obj);
    }

    public Game freeze() {
        return this;
    }

    public int getAchievementTotalCount() {
        return this.dr;
    }

    public String getApplicationId() {
        return this.df;
    }

    public String getDescription() {
        return this.di;
    }

    public void getDescription(CharArrayBuffer charArrayBuffer) {
        ao.m345b(this.di, charArrayBuffer);
    }

    public String getDeveloperName() {
        return this.dj;
    }

    public void getDeveloperName(CharArrayBuffer charArrayBuffer) {
        ao.m345b(this.dj, charArrayBuffer);
    }

    public String getDisplayName() {
        return this.cl;
    }

    public void getDisplayName(CharArrayBuffer charArrayBuffer) {
        ao.m345b(this.cl, charArrayBuffer);
    }

    public Uri getFeaturedImageUri() {
        return this.dm;
    }

    public int getGameplayAclStatus() {
        return this.dq;
    }

    public Uri getHiResImageUri() {
        return this.dl;
    }

    public Uri getIconImageUri() {
        return this.dk;
    }

    public String getInstancePackageName() {
        return this.dp;
    }

    public int getLeaderboardCount() {
        return this.ds;
    }

    public String getPrimaryCategory() {
        return this.dg;
    }

    public String getSecondaryCategory() {
        return this.dh;
    }

    public int hashCode() {
        return m186a(this);
    }

    public int m191i() {
        return this.ab;
    }

    public boolean isDataValid() {
        return true;
    }

    public boolean isInstanceInstalled() {
        return this.f49do;
    }

    public boolean isPlayEnabledGame() {
        return this.dn;
    }

    public String toString() {
        return m188b((Game) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2 = 1;
        String str = null;
        if (m184w()) {
            parcel.writeString(this.df);
            parcel.writeString(this.cl);
            parcel.writeString(this.dg);
            parcel.writeString(this.dh);
            parcel.writeString(this.di);
            parcel.writeString(this.dj);
            parcel.writeString(this.dk == null ? null : this.dk.toString());
            parcel.writeString(this.dl == null ? null : this.dl.toString());
            if (this.dm != null) {
                str = this.dm.toString();
            }
            parcel.writeString(str);
            parcel.writeInt(this.dn ? 1 : 0);
            if (!this.f49do) {
                i2 = 0;
            }
            parcel.writeInt(i2);
            parcel.writeString(this.dp);
            parcel.writeInt(this.dq);
            parcel.writeInt(this.dr);
            parcel.writeInt(this.ds);
            return;
        }
        C0081a.m176a(this, parcel, i);
    }
}
