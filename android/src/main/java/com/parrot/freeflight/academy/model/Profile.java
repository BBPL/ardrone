package com.parrot.freeflight.academy.model;

import android.annotation.SuppressLint;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.HttpVersions;

public class Profile {
    private static final String ADDRESS_1 = "address_1";
    private static final String ADDRESS_2 = "address_2";
    private static final String ADDRESS_COMMENTS = "address_comments";
    private static final String AVATAR = "avatar";
    private static final String BIOGRAPHY = "biography";
    private static final String BIRTH_DATE = "birth_date";
    private static final String CITY = "city";
    private static final String CIVILITY = "civility";
    private static final String COMMENTS = "comments";
    private static final String COUNTRY = "country";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String EMAIL_ACADEMY = "email_academy";
    private static final String EMAIL_PARTNAIRS = "email_partnairs";
    private static final String FACEBOOK = "facebook";
    private static final String FIRST_NAME = "first_name";
    private static final String FLIGHT_NUMBER = "flight_number";
    private static final String GMAIL = "gmail";
    private static final String GOOGLE_PLUS = "google_plus";
    private static final String ID = "id";
    private static final String LAST_NAME = "last_name";
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private static final String MOBILE = "mobile";
    private static final String MODIFICATION_DATE = "modification_date";
    private static final String MSN = "msn";
    private static final String NULL = "null";
    private static final String PHONE = "phone";
    private static final String POSTAL_CODE = "postal_code";
    private static final String PSN = "psn";
    private static final String SKYPE = "skype";
    private static final String STATUS = "status";
    private static final String STEAM = "steam";
    private static final String TAGS = "tags";
    private static final String TWITTER = "twitter";
    private static final String USER = "user";
    private static final String VISIBLE = "visible";
    private static final String WEBSITE = "website";
    private static final String XBOX_LIVE = "xbox_live";
    private static final String YOUTUBE = "youtube";
    private String address_1 = HttpVersions.HTTP_0_9;
    private String address_2 = HttpVersions.HTTP_0_9;
    private String address_comments = HttpVersions.HTTP_0_9;
    private String avatar = HttpVersions.HTTP_0_9;
    private String biography = HttpVersions.HTTP_0_9;
    private Date birth_date;
    private String city = HttpVersions.HTTP_0_9;
    private int civility;
    private String comments = HttpVersions.HTTP_0_9;
    private Country country;
    private boolean email_academy;
    private boolean email_partnairs;
    private String facebook = HttpVersions.HTTP_0_9;
    private String first_name = HttpVersions.HTTP_0_9;
    private int flight_number;
    private String gmail = HttpVersions.HTTP_0_9;
    private String google_plus = HttpVersions.HTTP_0_9;
    private int id;
    private String last_name = HttpVersions.HTTP_0_9;
    private String lat = HttpVersions.HTTP_0_9;
    private String lng = HttpVersions.HTTP_0_9;
    private String mobile = HttpVersions.HTTP_0_9;
    private Date modification_date;
    private String msn = HttpVersions.HTTP_0_9;
    private String phone = HttpVersions.HTTP_0_9;
    private String postal_code = HttpVersions.HTTP_0_9;
    private Privacy privacy;
    private String psn = HttpVersions.HTTP_0_9;
    private String skype = HttpVersions.HTTP_0_9;
    private String status = HttpVersions.HTTP_0_9;
    private String steam = HttpVersions.HTTP_0_9;
    private String tags = HttpVersions.HTTP_0_9;
    private String twitter = HttpVersions.HTTP_0_9;
    private User user;
    private boolean visible;
    private String website = HttpVersions.HTTP_0_9;
    private String xbox_live = HttpVersions.HTTP_0_9;
    private String youtube = HttpVersions.HTTP_0_9;

    public Profile(Profile profile) {
        updateValues(profile);
    }

    @SuppressLint({"SimpleDateFormat"})
    public Profile(JSONObject jSONObject) throws JSONException, MalformedURLException, ParseException {
        if (!jSONObject.isNull(ADDRESS_1)) {
            setAddress_1(jSONObject.getString(ADDRESS_1));
        }
        if (!jSONObject.isNull(ADDRESS_2)) {
            setAddress_2(jSONObject.getString(ADDRESS_2));
        }
        if (!jSONObject.isNull(ADDRESS_COMMENTS)) {
            setAddress_comments(jSONObject.getString(ADDRESS_COMMENTS));
        }
        if (!jSONObject.isNull(AVATAR)) {
            setAvatar(jSONObject.getString(AVATAR));
        }
        if (!jSONObject.isNull(BIOGRAPHY)) {
            setBiography(jSONObject.getString(BIOGRAPHY));
        }
        if (!jSONObject.isNull(BIRTH_DATE)) {
            String string = jSONObject.getString(BIRTH_DATE);
            if (!(string == null || string.equals(NULL))) {
                setBirth_date(new SimpleDateFormat(DATE_FORMAT).parse(string));
            }
        }
        if (!jSONObject.isNull(CITY)) {
            setCity(jSONObject.getString(CITY));
        }
        if (!jSONObject.isNull(CIVILITY)) {
            setCivility(jSONObject.getInt(CIVILITY));
        }
        if (!jSONObject.isNull(COMMENTS)) {
            setComments(jSONObject.getString(COMMENTS));
        }
        if (jSONObject.isNull(COUNTRY)) {
            setCountry(null);
        } else if (!(jSONObject.get(COUNTRY).equals(null) || jSONObject.get(COUNTRY).equals(NULL))) {
            setCountry(new Country(jSONObject.getJSONObject(COUNTRY)));
        }
        if (!jSONObject.isNull(EMAIL_ACADEMY)) {
            setEmail_academy(jSONObject.getBoolean(EMAIL_ACADEMY));
        }
        if (!jSONObject.isNull(EMAIL_PARTNAIRS)) {
            setEmail_partnairs(jSONObject.getBoolean(EMAIL_PARTNAIRS));
        }
        if (!jSONObject.isNull(FACEBOOK)) {
            setFacebook(jSONObject.getString(FACEBOOK));
        }
        if (!jSONObject.isNull(FIRST_NAME)) {
            setFirst_name(jSONObject.getString(FIRST_NAME));
        }
        if (!jSONObject.isNull(FLIGHT_NUMBER)) {
            setFlight_number(jSONObject.getInt(FLIGHT_NUMBER));
        }
        if (!jSONObject.isNull(GMAIL)) {
            setGmail(jSONObject.getString(GMAIL));
        }
        if (!jSONObject.isNull(GOOGLE_PLUS)) {
            setGoogle_plus(jSONObject.getString(GOOGLE_PLUS));
        }
        if (!jSONObject.isNull(ID)) {
            setId(jSONObject.getInt(ID));
        }
        if (!jSONObject.isNull(LAST_NAME)) {
            setLast_name(jSONObject.getString(LAST_NAME));
        }
        if (!jSONObject.isNull(LAT)) {
            setLat(jSONObject.getString(LAT));
        }
        if (!jSONObject.isNull(LNG)) {
            setLng(jSONObject.getString(LNG));
        }
        if (!jSONObject.isNull(MOBILE)) {
            setMobile(jSONObject.getString(MOBILE));
        }
        if (jSONObject.isNull(MODIFICATION_DATE)) {
            setModification_date(null);
        } else {
            setModification_date(new SimpleDateFormat(DATE_FORMAT).parse(jSONObject.getString(MODIFICATION_DATE)));
        }
        if (!jSONObject.isNull(MSN)) {
            setMsn(jSONObject.getString(MSN));
        }
        if (!jSONObject.isNull(PHONE)) {
            setPhone(jSONObject.getString(PHONE));
        }
        if (!jSONObject.isNull(POSTAL_CODE)) {
            setPostal_code(jSONObject.getString(POSTAL_CODE));
        }
        if (!jSONObject.isNull(PSN)) {
            setPsn(jSONObject.getString(PSN));
        }
        if (!jSONObject.isNull(SKYPE)) {
            setSkype(jSONObject.getString(SKYPE));
        }
        if (!jSONObject.isNull(STATUS)) {
            setStatus(jSONObject.getString(STATUS));
        }
        if (!jSONObject.isNull(STEAM)) {
            setSteam(jSONObject.getString(STEAM));
        }
        if (!jSONObject.isNull(TAGS)) {
            setTags(jSONObject.getString(TAGS));
        }
        if (!jSONObject.isNull(TWITTER)) {
            setTwitter(jSONObject.getString(TWITTER));
        }
        if (jSONObject.isNull(USER)) {
            setUser(null);
        } else {
            setUser(new User(jSONObject.getJSONObject(USER)));
        }
        if (!jSONObject.isNull("visible")) {
            setVisible(jSONObject.getBoolean("visible"));
        }
        if (!jSONObject.isNull(WEBSITE)) {
            setWebsite(jSONObject.getString(WEBSITE));
        }
        if (!jSONObject.isNull(XBOX_LIVE)) {
            setXbox_live(jSONObject.getString(XBOX_LIVE));
        }
        if (!jSONObject.isNull(YOUTUBE)) {
            setYoutube(jSONObject.getString(YOUTUBE));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
        r3 = this;
        r0 = 1;
        if (r3 != r4) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r4 = (com.parrot.freeflight.academy.model.Profile) r4;
        r1 = r3.id;
        r2 = r4.id;
        if (r1 != r2) goto L_0x01ea;
    L_0x000c:
        r1 = r3.user;
        if (r1 == 0) goto L_0x01ed;
    L_0x0010:
        r1 = r3.user;
        r2 = r4.user;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x001a:
        r1 = r3.avatar;
        if (r1 == 0) goto L_0x01f5;
    L_0x001e:
        r1 = r3.avatar;
        r2 = r4.avatar;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0028:
        r1 = r3.status;
        if (r1 == 0) goto L_0x01fd;
    L_0x002c:
        r1 = r3.status;
        r2 = r4.status;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0036:
        r1 = r3.civility;
        r2 = r4.civility;
        if (r1 != r2) goto L_0x01ea;
    L_0x003c:
        r1 = r3.first_name;
        if (r1 == 0) goto L_0x0205;
    L_0x0040:
        r1 = r3.first_name;
        r2 = r4.first_name;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x004a:
        r1 = r3.last_name;
        if (r1 == 0) goto L_0x020d;
    L_0x004e:
        r1 = r3.last_name;
        r2 = r4.last_name;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0058:
        r1 = r3.birth_date;
        if (r1 == 0) goto L_0x0215;
    L_0x005c:
        r1 = r3.birth_date;
        r2 = r4.birth_date;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0066:
        r1 = r3.phone;
        if (r1 == 0) goto L_0x021d;
    L_0x006a:
        r1 = r3.phone;
        r2 = r4.phone;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0074:
        r1 = r3.mobile;
        if (r1 == 0) goto L_0x0225;
    L_0x0078:
        r1 = r3.mobile;
        r2 = r4.mobile;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0082:
        r1 = r3.address_1;
        if (r1 == 0) goto L_0x022d;
    L_0x0086:
        r1 = r3.address_1;
        r2 = r4.address_1;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0090:
        r1 = r3.address_2;
        if (r1 == 0) goto L_0x0235;
    L_0x0094:
        r1 = r3.address_2;
        r2 = r4.address_2;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x009e:
        r1 = r3.address_comments;
        if (r1 == 0) goto L_0x023d;
    L_0x00a2:
        r1 = r3.address_comments;
        r2 = r4.address_comments;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x00ac:
        r1 = r3.postal_code;
        if (r1 == 0) goto L_0x0245;
    L_0x00b0:
        r1 = r3.postal_code;
        r2 = r4.postal_code;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x00ba:
        r1 = r3.city;
        if (r1 == 0) goto L_0x024d;
    L_0x00be:
        r1 = r3.city;
        r2 = r4.city;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x00c8:
        r1 = r3.country;
        if (r1 == 0) goto L_0x0255;
    L_0x00cc:
        r1 = r3.country;
        r2 = r4.country;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x00d6:
        r1 = r3.website;
        if (r1 == 0) goto L_0x025d;
    L_0x00da:
        r1 = r3.website;
        r2 = r4.website;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x00e4:
        r1 = r3.facebook;
        if (r1 == 0) goto L_0x0265;
    L_0x00e8:
        r1 = r3.facebook;
        r2 = r4.facebook;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x00f2:
        r1 = r3.twitter;
        if (r1 == 0) goto L_0x026d;
    L_0x00f6:
        r1 = r3.twitter;
        r2 = r4.facebook;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0100:
        r1 = r3.google_plus;
        if (r1 == 0) goto L_0x0275;
    L_0x0104:
        r1 = r3.google_plus;
        r2 = r4.google_plus;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x010e:
        r1 = r3.youtube;
        if (r1 == 0) goto L_0x027d;
    L_0x0112:
        r1 = r3.youtube;
        r2 = r4.youtube;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x011c:
        r1 = r3.psn;
        if (r1 == 0) goto L_0x0285;
    L_0x0120:
        r1 = r3.psn;
        r2 = r4.psn;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x012a:
        r1 = r3.xbox_live;
        if (r1 == 0) goto L_0x028d;
    L_0x012e:
        r1 = r3.xbox_live;
        r2 = r4.xbox_live;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0138:
        r1 = r3.gmail;
        if (r1 == 0) goto L_0x0295;
    L_0x013c:
        r1 = r3.gmail;
        r2 = r4.gmail;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0146:
        r1 = r3.email_academy;
        r2 = r4.email_academy;
        if (r1 != r2) goto L_0x01ea;
    L_0x014c:
        r1 = r3.msn;
        if (r1 == 0) goto L_0x029d;
    L_0x0150:
        r1 = r3.msn;
        r2 = r4.msn;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x015a:
        r1 = r3.steam;
        if (r1 == 0) goto L_0x02a5;
    L_0x015e:
        r1 = r3.steam;
        r2 = r4.steam;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0168:
        r1 = r3.skype;
        if (r1 == 0) goto L_0x02ad;
    L_0x016c:
        r1 = r3.skype;
        r2 = r4.skype;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x0176:
        r1 = r3.visible;
        r2 = r4.visible;
        if (r1 != r2) goto L_0x01ea;
    L_0x017c:
        r1 = r3.modification_date;
        if (r1 == 0) goto L_0x02b5;
    L_0x0180:
        r1 = r3.modification_date;
        r2 = r4.modification_date;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x018a:
        r1 = r3.email_partnairs;
        r2 = r4.email_partnairs;
        if (r1 != r2) goto L_0x01ea;
    L_0x0190:
        r1 = r3.lat;
        if (r1 == 0) goto L_0x02bd;
    L_0x0194:
        r1 = r3.lat;
        r2 = r4.lat;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x019e:
        r1 = r3.lng;
        if (r1 == 0) goto L_0x02c5;
    L_0x01a2:
        r1 = r3.lng;
        r2 = r4.lng;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x01ac:
        r1 = r3.tags;
        if (r1 == 0) goto L_0x02cd;
    L_0x01b0:
        r1 = r3.tags;
        r2 = r4.tags;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x01ba:
        r1 = r3.flight_number;
        r2 = r4.flight_number;
        if (r1 != r2) goto L_0x01ea;
    L_0x01c0:
        r1 = r3.biography;
        if (r1 == 0) goto L_0x02d5;
    L_0x01c4:
        r1 = r3.biography;
        r2 = r4.biography;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x01ce:
        r1 = r3.comments;
        if (r1 == 0) goto L_0x02dd;
    L_0x01d2:
        r1 = r3.comments;
        r2 = r4.comments;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x01ea;
    L_0x01dc:
        r1 = r3.privacy;
        if (r1 == 0) goto L_0x02e5;
    L_0x01e0:
        r1 = r3.privacy;
        r2 = r4.privacy;
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x0003;
    L_0x01ea:
        r0 = 0;
        goto L_0x0003;
    L_0x01ed:
        r1 = r3.user;
        r2 = r4.user;
        if (r1 != r2) goto L_0x01ea;
    L_0x01f3:
        goto L_0x001a;
    L_0x01f5:
        r1 = r3.avatar;
        r2 = r4.avatar;
        if (r1 != r2) goto L_0x01ea;
    L_0x01fb:
        goto L_0x0028;
    L_0x01fd:
        r1 = r3.status;
        r2 = r4.status;
        if (r1 != r2) goto L_0x01ea;
    L_0x0203:
        goto L_0x0036;
    L_0x0205:
        r1 = r3.first_name;
        r2 = r4.first_name;
        if (r1 != r2) goto L_0x01ea;
    L_0x020b:
        goto L_0x004a;
    L_0x020d:
        r1 = r3.last_name;
        r2 = r4.last_name;
        if (r1 != r2) goto L_0x01ea;
    L_0x0213:
        goto L_0x0058;
    L_0x0215:
        r1 = r3.birth_date;
        r2 = r4.birth_date;
        if (r1 != r2) goto L_0x01ea;
    L_0x021b:
        goto L_0x0066;
    L_0x021d:
        r1 = r3.phone;
        r2 = r4.phone;
        if (r1 != r2) goto L_0x01ea;
    L_0x0223:
        goto L_0x0074;
    L_0x0225:
        r1 = r3.mobile;
        r2 = r4.mobile;
        if (r1 != r2) goto L_0x01ea;
    L_0x022b:
        goto L_0x0082;
    L_0x022d:
        r1 = r3.address_1;
        r2 = r4.address_1;
        if (r1 != r2) goto L_0x01ea;
    L_0x0233:
        goto L_0x0090;
    L_0x0235:
        r1 = r3.address_2;
        r2 = r4.address_2;
        if (r1 != r2) goto L_0x01ea;
    L_0x023b:
        goto L_0x009e;
    L_0x023d:
        r1 = r3.address_comments;
        r2 = r4.address_comments;
        if (r1 != r2) goto L_0x01ea;
    L_0x0243:
        goto L_0x00ac;
    L_0x0245:
        r1 = r3.postal_code;
        r2 = r4.postal_code;
        if (r1 != r2) goto L_0x01ea;
    L_0x024b:
        goto L_0x00ba;
    L_0x024d:
        r1 = r3.city;
        r2 = r4.city;
        if (r1 != r2) goto L_0x01ea;
    L_0x0253:
        goto L_0x00c8;
    L_0x0255:
        r1 = r3.country;
        r2 = r4.country;
        if (r1 != r2) goto L_0x01ea;
    L_0x025b:
        goto L_0x00d6;
    L_0x025d:
        r1 = r3.website;
        r2 = r4.website;
        if (r1 != r2) goto L_0x01ea;
    L_0x0263:
        goto L_0x00e4;
    L_0x0265:
        r1 = r3.facebook;
        r2 = r4.facebook;
        if (r1 != r2) goto L_0x01ea;
    L_0x026b:
        goto L_0x00f2;
    L_0x026d:
        r1 = r3.twitter;
        r2 = r4.twitter;
        if (r1 != r2) goto L_0x01ea;
    L_0x0273:
        goto L_0x0100;
    L_0x0275:
        r1 = r3.google_plus;
        r2 = r4.google_plus;
        if (r1 != r2) goto L_0x01ea;
    L_0x027b:
        goto L_0x010e;
    L_0x027d:
        r1 = r3.youtube;
        r2 = r4.youtube;
        if (r1 != r2) goto L_0x01ea;
    L_0x0283:
        goto L_0x011c;
    L_0x0285:
        r1 = r3.psn;
        r2 = r4.psn;
        if (r1 != r2) goto L_0x01ea;
    L_0x028b:
        goto L_0x012a;
    L_0x028d:
        r1 = r3.xbox_live;
        r2 = r4.xbox_live;
        if (r1 != r2) goto L_0x01ea;
    L_0x0293:
        goto L_0x0138;
    L_0x0295:
        r1 = r3.gmail;
        r2 = r4.gmail;
        if (r1 != r2) goto L_0x01ea;
    L_0x029b:
        goto L_0x0146;
    L_0x029d:
        r1 = r3.msn;
        r2 = r4.msn;
        if (r1 != r2) goto L_0x01ea;
    L_0x02a3:
        goto L_0x015a;
    L_0x02a5:
        r1 = r3.steam;
        r2 = r4.steam;
        if (r1 != r2) goto L_0x01ea;
    L_0x02ab:
        goto L_0x0168;
    L_0x02ad:
        r1 = r3.skype;
        r2 = r4.skype;
        if (r1 != r2) goto L_0x01ea;
    L_0x02b3:
        goto L_0x0176;
    L_0x02b5:
        r1 = r3.modification_date;
        r2 = r4.modification_date;
        if (r1 != r2) goto L_0x01ea;
    L_0x02bb:
        goto L_0x018a;
    L_0x02bd:
        r1 = r3.lat;
        r2 = r4.lat;
        if (r1 != r2) goto L_0x01ea;
    L_0x02c3:
        goto L_0x019e;
    L_0x02c5:
        r1 = r3.lng;
        r2 = r4.lng;
        if (r1 != r2) goto L_0x01ea;
    L_0x02cb:
        goto L_0x01ac;
    L_0x02cd:
        r1 = r3.tags;
        r2 = r4.tags;
        if (r1 != r2) goto L_0x01ea;
    L_0x02d3:
        goto L_0x01ba;
    L_0x02d5:
        r1 = r3.biography;
        r2 = r4.biography;
        if (r1 != r2) goto L_0x01ea;
    L_0x02db:
        goto L_0x01ce;
    L_0x02dd:
        r1 = r3.comments;
        r2 = r4.comments;
        if (r1 != r2) goto L_0x01ea;
    L_0x02e3:
        goto L_0x01dc;
    L_0x02e5:
        r1 = r3.privacy;
        r2 = r4.privacy;
        if (r1 != r2) goto L_0x01ea;
    L_0x02eb:
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.academy.model.Profile.equals(java.lang.Object):boolean");
    }

    public String getAddress_1() {
        return !this.address_1.equals(HttpVersions.HTTP_0_9) ? this.address_1 : null;
    }

    public String getAddress_2() {
        return !this.address_2.equals(HttpVersions.HTTP_0_9) ? this.address_2 : null;
    }

    public String getAddress_comments() {
        return !this.address_comments.equals(HttpVersions.HTTP_0_9) ? this.address_comments : null;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getBiography() {
        return this.biography;
    }

    public Date getBirth_date() {
        return this.birth_date;
    }

    public String getCity() {
        return !this.city.equals(HttpVersions.HTTP_0_9) ? this.city : null;
    }

    public int getCivility() {
        return this.civility;
    }

    public String getComments() {
        return this.comments;
    }

    public Country getCountry() {
        return this.country;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public String getFirst_name() {
        return !this.first_name.equals(HttpVersions.HTTP_0_9) ? this.first_name : null;
    }

    public int getFlight_number() {
        return this.flight_number;
    }

    public String getGmail() {
        return this.gmail;
    }

    public String getGoogle_plus() {
        return this.google_plus;
    }

    public int getId() {
        return this.id;
    }

    public String getLast_name() {
        return !this.last_name.equals(HttpVersions.HTTP_0_9) ? this.last_name : null;
    }

    public String getLat() {
        return this.lat;
    }

    public String getLng() {
        return this.lng;
    }

    public String getMobile() {
        return !this.mobile.equals(HttpVersions.HTTP_0_9) ? this.mobile : null;
    }

    public Date getModification_date() {
        return this.modification_date;
    }

    public String getMsn() {
        return this.msn;
    }

    public String getPhone() {
        return !this.phone.equals(HttpVersions.HTTP_0_9) ? this.phone : null;
    }

    public String getPostal_code() {
        return !this.postal_code.equals(HttpVersions.HTTP_0_9) ? this.postal_code : null;
    }

    public Privacy getPrivacy() {
        return this.privacy;
    }

    public String getPsn() {
        return this.psn;
    }

    public String getSkype() {
        return this.skype;
    }

    public String getStatus() {
        return this.status;
    }

    public String getSteam() {
        return this.steam;
    }

    public String getTags() {
        return this.tags;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public User getUser() {
        return this.user;
    }

    public String getWebsite() {
        return this.website;
    }

    public String getXbox_live() {
        return this.xbox_live;
    }

    public String getYoutube() {
        return this.youtube;
    }

    public boolean isEmail_academy() {
        return this.email_academy;
    }

    public boolean isEmail_partnairs() {
        return this.email_partnairs;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setAddress_1(String str) {
        this.address_1 = str;
    }

    public void setAddress_2(String str) {
        this.address_2 = str;
    }

    public void setAddress_comments(String str) {
        this.address_comments = str;
    }

    public void setAvatar(String str) {
        this.avatar = str;
    }

    public void setBiography(String str) {
        this.biography = str;
    }

    public void setBirth_date(Date date) {
        this.birth_date = date;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setCivility(int i) {
        this.civility = i;
    }

    public void setComments(String str) {
        this.comments = str;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setEmail_academy(boolean z) {
        this.email_academy = z;
    }

    public void setEmail_partnairs(boolean z) {
        this.email_partnairs = z;
    }

    public void setFacebook(String str) {
        this.facebook = str;
    }

    public void setFirst_name(String str) {
        this.first_name = str;
    }

    public void setFlight_number(int i) {
        this.flight_number = i;
    }

    public void setGmail(String str) {
        this.gmail = str;
    }

    public void setGoogle_plus(String str) {
        this.google_plus = str;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setLast_name(String str) {
        this.last_name = str;
    }

    public void setLat(String str) {
        this.lat = str;
    }

    public void setLng(String str) {
        this.lng = str;
    }

    public void setMobile(String str) {
        this.mobile = str;
    }

    public void setModification_date(Date date) {
        this.modification_date = date;
    }

    public void setMsn(String str) {
        this.msn = str;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public void setPostal_code(String str) {
        this.postal_code = str;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public void setPsn(String str) {
        this.psn = str;
    }

    public void setSkype(String str) {
        this.skype = str;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public void setSteam(String str) {
        this.steam = str;
    }

    public void setTags(String str) {
        this.tags = str;
    }

    public void setTwitter(String str) {
        this.twitter = str;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }

    public void setWebsite(String str) {
        this.website = str;
    }

    public void setXbox_live(String str) {
        this.xbox_live = str;
    }

    public void setYoutube(String str) {
        this.youtube = str;
    }

    public String toString() {
        return this.user.getUsername() + "\n" + this.first_name + "\n" + this.last_name + "\n" + this.phone + "\n" + this.country + "\n" + this.user.getEmail();
    }

    public void updateValues(Profile profile) {
        this.id = Integer.valueOf(profile.id).intValue();
        this.user = profile.user;
        this.avatar = profile.avatar;
        this.status = new String(profile.status);
        this.civility = profile.civility;
        this.first_name = profile.first_name;
        this.last_name = profile.last_name;
        this.birth_date = profile.birth_date;
        this.phone = profile.phone;
        this.mobile = profile.mobile;
        this.address_1 = profile.address_1;
        this.address_2 = profile.address_2;
        this.address_comments = profile.address_comments;
        this.postal_code = profile.postal_code;
        this.city = profile.city;
        this.country = profile.country;
        this.website = profile.website;
        this.facebook = profile.facebook;
        this.twitter = profile.twitter;
        this.google_plus = profile.google_plus;
        this.youtube = profile.youtube;
        this.psn = profile.psn;
        this.xbox_live = profile.xbox_live;
        this.gmail = profile.gmail;
        this.email_academy = profile.email_academy;
        this.msn = profile.msn;
        this.steam = profile.steam;
        this.skype = profile.skype;
        this.visible = profile.visible;
        this.modification_date = profile.modification_date;
        this.email_partnairs = profile.email_partnairs;
        this.lat = profile.lat;
        this.lng = profile.lng;
        this.tags = profile.tags;
        this.flight_number = profile.flight_number;
        this.biography = profile.biography;
        this.comments = profile.comments;
        this.privacy = profile.privacy;
    }
}
