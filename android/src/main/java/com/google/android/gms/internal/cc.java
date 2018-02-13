package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ae.C0113a;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.AgeRange;
import com.google.android.gms.plus.model.people.Person.Cover;
import com.google.android.gms.plus.model.people.Person.Cover.CoverInfo;
import com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto;
import com.google.android.gms.plus.model.people.Person.Emails;
import com.google.android.gms.plus.model.people.Person.Image;
import com.google.android.gms.plus.model.people.Person.Name;
import com.google.android.gms.plus.model.people.Person.Organizations;
import com.google.android.gms.plus.model.people.Person.PlacesLived;
import com.google.android.gms.plus.model.people.Person.Urls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class cc extends ae implements SafeParcelable, Person {
    public static final cd CREATOR = new cd();
    private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
    private final int ab;
    private String cl;
    private final Set<Integer> iD;
    private String ie;
    private String jE;
    private C0199a jF;
    private String jG;
    private String jH;
    private int jI;
    private C0202b jJ;
    private String jK;
    private int jL;
    private C0203c jM;
    private boolean jN;
    private String jO;
    private C0204d jP;
    private String jQ;
    private int jR;
    private List<C0206f> jS;
    private List<C0207g> jT;
    private int jU;
    private int jV;
    private String jW;
    private List<C0208h> jX;
    private boolean jY;
    private String jh;

    public static final class C0199a extends ae implements SafeParcelable, AgeRange {
        public static final ce CREATOR = new ce();
        private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
        private final int ab;
        private final Set<Integer> iD;
        private int jZ;
        private int ka;

        static {
            iC.put("max", C0113a.m272c("max", 2));
            iC.put("min", C0113a.m272c("min", 3));
        }

        public C0199a() {
            this.ab = 1;
            this.iD = new HashSet();
        }

        C0199a(Set<Integer> set, int i, int i2, int i3) {
            this.iD = set;
            this.ab = i;
            this.jZ = i2;
            this.ka = i3;
        }

        public HashMap<String, C0113a<?, ?>> mo477T() {
            return iC;
        }

        protected boolean mo717a(C0113a c0113a) {
            return this.iD.contains(Integer.valueOf(c0113a.aa()));
        }

        protected Object mo718b(C0113a c0113a) {
            switch (c0113a.aa()) {
                case 2:
                    return Integer.valueOf(this.jZ);
                case 3:
                    return Integer.valueOf(this.ka);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
            }
        }

        Set<Integer> bH() {
            return this.iD;
        }

        public C0199a ck() {
            return this;
        }

        public int describeContents() {
            ce ceVar = CREATOR;
            return 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0199a)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0199a c0199a = (C0199a) obj;
            for (C0113a c0113a : iC.values()) {
                if (mo717a(c0113a)) {
                    if (!c0199a.mo717a(c0113a)) {
                        return false;
                    }
                    if (!mo718b(c0113a).equals(c0199a.mo718b(c0113a))) {
                        return false;
                    }
                } else if (c0199a.mo717a(c0113a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return ck();
        }

        public int getMax() {
            return this.jZ;
        }

        public int getMin() {
            return this.ka;
        }

        public boolean hasMax() {
            return this.iD.contains(Integer.valueOf(2));
        }

        public boolean hasMin() {
            return this.iD.contains(Integer.valueOf(3));
        }

        public int hashCode() {
            int i = 0;
            for (C0113a c0113a : iC.values()) {
                int hashCode;
                if (mo717a(c0113a)) {
                    hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                } else {
                    hashCode = i;
                }
                i = hashCode;
            }
            return i;
        }

        int m1022i() {
            return this.ab;
        }

        public boolean isDataValid() {
            return true;
        }

        protected Object mo478m(String str) {
            return null;
        }

        protected boolean mo479n(String str) {
            return false;
        }

        public void writeToParcel(Parcel parcel, int i) {
            ce ceVar = CREATOR;
            ce.m1084a(this, parcel, i);
        }
    }

    public static final class C0202b extends ae implements SafeParcelable, Cover {
        public static final cf CREATOR = new cf();
        private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
        private final int ab;
        private final Set<Integer> iD;
        private C0200a kb;
        private C0201b kc;
        private int kd;

        public static final class C0200a extends ae implements SafeParcelable, CoverInfo {
            public static final cg CREATOR = new cg();
            private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
            private final int ab;
            private final Set<Integer> iD;
            private int ke;
            private int kf;

            static {
                iC.put("leftImageOffset", C0113a.m272c("leftImageOffset", 2));
                iC.put("topImageOffset", C0113a.m272c("topImageOffset", 3));
            }

            public C0200a() {
                this.ab = 1;
                this.iD = new HashSet();
            }

            C0200a(Set<Integer> set, int i, int i2, int i3) {
                this.iD = set;
                this.ab = i;
                this.ke = i2;
                this.kf = i3;
            }

            public HashMap<String, C0113a<?, ?>> mo477T() {
                return iC;
            }

            protected boolean mo717a(C0113a c0113a) {
                return this.iD.contains(Integer.valueOf(c0113a.aa()));
            }

            protected Object mo718b(C0113a c0113a) {
                switch (c0113a.aa()) {
                    case 2:
                        return Integer.valueOf(this.ke);
                    case 3:
                        return Integer.valueOf(this.kf);
                    default:
                        throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
                }
            }

            Set<Integer> bH() {
                return this.iD;
            }

            public C0200a co() {
                return this;
            }

            public int describeContents() {
                cg cgVar = CREATOR;
                return 0;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof C0200a)) {
                    return false;
                }
                if (this == obj) {
                    return true;
                }
                C0200a c0200a = (C0200a) obj;
                for (C0113a c0113a : iC.values()) {
                    if (mo717a(c0113a)) {
                        if (!c0200a.mo717a(c0113a)) {
                            return false;
                        }
                        if (!mo718b(c0113a).equals(c0200a.mo718b(c0113a))) {
                            return false;
                        }
                    } else if (c0200a.mo717a(c0113a)) {
                        return false;
                    }
                }
                return true;
            }

            public /* synthetic */ Object freeze() {
                return co();
            }

            public int getLeftImageOffset() {
                return this.ke;
            }

            public int getTopImageOffset() {
                return this.kf;
            }

            public boolean hasLeftImageOffset() {
                return this.iD.contains(Integer.valueOf(2));
            }

            public boolean hasTopImageOffset() {
                return this.iD.contains(Integer.valueOf(3));
            }

            public int hashCode() {
                int i = 0;
                for (C0113a c0113a : iC.values()) {
                    int hashCode;
                    if (mo717a(c0113a)) {
                        hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                    } else {
                        hashCode = i;
                    }
                    i = hashCode;
                }
                return i;
            }

            int m1028i() {
                return this.ab;
            }

            public boolean isDataValid() {
                return true;
            }

            protected Object mo478m(String str) {
                return null;
            }

            protected boolean mo479n(String str) {
                return false;
            }

            public void writeToParcel(Parcel parcel, int i) {
                cg cgVar = CREATOR;
                cg.m1089a(this, parcel, i);
            }
        }

        public static final class C0201b extends ae implements SafeParcelable, CoverPhoto {
            public static final ch CREATOR = new ch();
            private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
            private final int ab;
            private int hL;
            private int hM;
            private final Set<Integer> iD;
            private String ie;

            static {
                iC.put("height", C0113a.m272c("height", 2));
                iC.put("url", C0113a.m276f("url", 3));
                iC.put("width", C0113a.m272c("width", 4));
            }

            public C0201b() {
                this.ab = 1;
                this.iD = new HashSet();
            }

            C0201b(Set<Integer> set, int i, int i2, String str, int i3) {
                this.iD = set;
                this.ab = i;
                this.hM = i2;
                this.ie = str;
                this.hL = i3;
            }

            public HashMap<String, C0113a<?, ?>> mo477T() {
                return iC;
            }

            protected boolean mo717a(C0113a c0113a) {
                return this.iD.contains(Integer.valueOf(c0113a.aa()));
            }

            protected Object mo718b(C0113a c0113a) {
                switch (c0113a.aa()) {
                    case 2:
                        return Integer.valueOf(this.hM);
                    case 3:
                        return this.ie;
                    case 4:
                        return Integer.valueOf(this.hL);
                    default:
                        throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
                }
            }

            Set<Integer> bH() {
                return this.iD;
            }

            public C0201b cp() {
                return this;
            }

            public int describeContents() {
                ch chVar = CREATOR;
                return 0;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof C0201b)) {
                    return false;
                }
                if (this == obj) {
                    return true;
                }
                C0201b c0201b = (C0201b) obj;
                for (C0113a c0113a : iC.values()) {
                    if (mo717a(c0113a)) {
                        if (!c0201b.mo717a(c0113a)) {
                            return false;
                        }
                        if (!mo718b(c0113a).equals(c0201b.mo718b(c0113a))) {
                            return false;
                        }
                    } else if (c0201b.mo717a(c0113a)) {
                        return false;
                    }
                }
                return true;
            }

            public /* synthetic */ Object freeze() {
                return cp();
            }

            public int getHeight() {
                return this.hM;
            }

            public String getUrl() {
                return this.ie;
            }

            public int getWidth() {
                return this.hL;
            }

            public boolean hasHeight() {
                return this.iD.contains(Integer.valueOf(2));
            }

            public boolean hasUrl() {
                return this.iD.contains(Integer.valueOf(3));
            }

            public boolean hasWidth() {
                return this.iD.contains(Integer.valueOf(4));
            }

            public int hashCode() {
                int i = 0;
                for (C0113a c0113a : iC.values()) {
                    int hashCode;
                    if (mo717a(c0113a)) {
                        hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                    } else {
                        hashCode = i;
                    }
                    i = hashCode;
                }
                return i;
            }

            int m1034i() {
                return this.ab;
            }

            public boolean isDataValid() {
                return true;
            }

            protected Object mo478m(String str) {
                return null;
            }

            protected boolean mo479n(String str) {
                return false;
            }

            public void writeToParcel(Parcel parcel, int i) {
                ch chVar = CREATOR;
                ch.m1091a(this, parcel, i);
            }
        }

        static {
            iC.put("coverInfo", C0113a.m270a("coverInfo", 2, C0200a.class));
            iC.put("coverPhoto", C0113a.m270a("coverPhoto", 3, C0201b.class));
            iC.put("layout", C0113a.m269a("layout", 4, new ab().m260b("banner", 0), false));
        }

        public C0202b() {
            this.ab = 1;
            this.iD = new HashSet();
        }

        C0202b(Set<Integer> set, int i, C0200a c0200a, C0201b c0201b, int i2) {
            this.iD = set;
            this.ab = i;
            this.kb = c0200a;
            this.kc = c0201b;
            this.kd = i2;
        }

        public HashMap<String, C0113a<?, ?>> mo477T() {
            return iC;
        }

        protected boolean mo717a(C0113a c0113a) {
            return this.iD.contains(Integer.valueOf(c0113a.aa()));
        }

        protected Object mo718b(C0113a c0113a) {
            switch (c0113a.aa()) {
                case 2:
                    return this.kb;
                case 3:
                    return this.kc;
                case 4:
                    return Integer.valueOf(this.kd);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
            }
        }

        Set<Integer> bH() {
            return this.iD;
        }

        C0200a cl() {
            return this.kb;
        }

        C0201b cm() {
            return this.kc;
        }

        public C0202b cn() {
            return this;
        }

        public int describeContents() {
            cf cfVar = CREATOR;
            return 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0202b)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0202b c0202b = (C0202b) obj;
            for (C0113a c0113a : iC.values()) {
                if (mo717a(c0113a)) {
                    if (!c0202b.mo717a(c0113a)) {
                        return false;
                    }
                    if (!mo718b(c0113a).equals(c0202b.mo718b(c0113a))) {
                        return false;
                    }
                } else if (c0202b.mo717a(c0113a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return cn();
        }

        public CoverInfo getCoverInfo() {
            return this.kb;
        }

        public CoverPhoto getCoverPhoto() {
            return this.kc;
        }

        public int getLayout() {
            return this.kd;
        }

        public boolean hasCoverInfo() {
            return this.iD.contains(Integer.valueOf(2));
        }

        public boolean hasCoverPhoto() {
            return this.iD.contains(Integer.valueOf(3));
        }

        public boolean hasLayout() {
            return this.iD.contains(Integer.valueOf(4));
        }

        public int hashCode() {
            int i = 0;
            for (C0113a c0113a : iC.values()) {
                int hashCode;
                if (mo717a(c0113a)) {
                    hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                } else {
                    hashCode = i;
                }
                i = hashCode;
            }
            return i;
        }

        int m1040i() {
            return this.ab;
        }

        public boolean isDataValid() {
            return true;
        }

        protected Object mo478m(String str) {
            return null;
        }

        protected boolean mo479n(String str) {
            return false;
        }

        public void writeToParcel(Parcel parcel, int i) {
            cf cfVar = CREATOR;
            cf.m1087a(this, parcel, i);
        }
    }

    public static final class C0203c extends ae implements SafeParcelable, Image {
        public static final ci CREATOR = new ci();
        private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
        private final int ab;
        private final Set<Integer> iD;
        private String ie;

        static {
            iC.put("url", C0113a.m276f("url", 2));
        }

        public C0203c() {
            this.ab = 1;
            this.iD = new HashSet();
        }

        public C0203c(String str) {
            this.iD = new HashSet();
            this.ab = 1;
            this.ie = str;
            this.iD.add(Integer.valueOf(2));
        }

        C0203c(Set<Integer> set, int i, String str) {
            this.iD = set;
            this.ab = i;
            this.ie = str;
        }

        public HashMap<String, C0113a<?, ?>> mo477T() {
            return iC;
        }

        protected boolean mo717a(C0113a c0113a) {
            return this.iD.contains(Integer.valueOf(c0113a.aa()));
        }

        protected Object mo718b(C0113a c0113a) {
            switch (c0113a.aa()) {
                case 2:
                    return this.ie;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
            }
        }

        Set<Integer> bH() {
            return this.iD;
        }

        public C0203c cq() {
            return this;
        }

        public int describeContents() {
            ci ciVar = CREATOR;
            return 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0203c)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0203c c0203c = (C0203c) obj;
            for (C0113a c0113a : iC.values()) {
                if (mo717a(c0113a)) {
                    if (!c0203c.mo717a(c0113a)) {
                        return false;
                    }
                    if (!mo718b(c0113a).equals(c0203c.mo718b(c0113a))) {
                        return false;
                    }
                } else if (c0203c.mo717a(c0113a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return cq();
        }

        public String getUrl() {
            return this.ie;
        }

        public boolean hasUrl() {
            return this.iD.contains(Integer.valueOf(2));
        }

        public int hashCode() {
            int i = 0;
            for (C0113a c0113a : iC.values()) {
                int hashCode;
                if (mo717a(c0113a)) {
                    hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                } else {
                    hashCode = i;
                }
                i = hashCode;
            }
            return i;
        }

        int m1046i() {
            return this.ab;
        }

        public boolean isDataValid() {
            return true;
        }

        protected Object mo478m(String str) {
            return null;
        }

        protected boolean mo479n(String str) {
            return false;
        }

        public void writeToParcel(Parcel parcel, int i) {
            ci ciVar = CREATOR;
            ci.m1093a(this, parcel, i);
        }
    }

    public static final class C0204d extends ae implements SafeParcelable, Name {
        public static final cj CREATOR = new cj();
        private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
        private final int ab;
        private final Set<Integer> iD;
        private String jc;
        private String jf;
        private String kg;
        private String kh;
        private String ki;
        private String kj;

        static {
            iC.put("familyName", C0113a.m276f("familyName", 2));
            iC.put("formatted", C0113a.m276f("formatted", 3));
            iC.put("givenName", C0113a.m276f("givenName", 4));
            iC.put("honorificPrefix", C0113a.m276f("honorificPrefix", 5));
            iC.put("honorificSuffix", C0113a.m276f("honorificSuffix", 6));
            iC.put("middleName", C0113a.m276f("middleName", 7));
        }

        public C0204d() {
            this.ab = 1;
            this.iD = new HashSet();
        }

        C0204d(Set<Integer> set, int i, String str, String str2, String str3, String str4, String str5, String str6) {
            this.iD = set;
            this.ab = i;
            this.jc = str;
            this.kg = str2;
            this.jf = str3;
            this.kh = str4;
            this.ki = str5;
            this.kj = str6;
        }

        public HashMap<String, C0113a<?, ?>> mo477T() {
            return iC;
        }

        protected boolean mo717a(C0113a c0113a) {
            return this.iD.contains(Integer.valueOf(c0113a.aa()));
        }

        protected Object mo718b(C0113a c0113a) {
            switch (c0113a.aa()) {
                case 2:
                    return this.jc;
                case 3:
                    return this.kg;
                case 4:
                    return this.jf;
                case 5:
                    return this.kh;
                case 6:
                    return this.ki;
                case 7:
                    return this.kj;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
            }
        }

        Set<Integer> bH() {
            return this.iD;
        }

        public C0204d cr() {
            return this;
        }

        public int describeContents() {
            cj cjVar = CREATOR;
            return 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0204d)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0204d c0204d = (C0204d) obj;
            for (C0113a c0113a : iC.values()) {
                if (mo717a(c0113a)) {
                    if (!c0204d.mo717a(c0113a)) {
                        return false;
                    }
                    if (!mo718b(c0113a).equals(c0204d.mo718b(c0113a))) {
                        return false;
                    }
                } else if (c0204d.mo717a(c0113a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return cr();
        }

        public String getFamilyName() {
            return this.jc;
        }

        public String getFormatted() {
            return this.kg;
        }

        public String getGivenName() {
            return this.jf;
        }

        public String getHonorificPrefix() {
            return this.kh;
        }

        public String getHonorificSuffix() {
            return this.ki;
        }

        public String getMiddleName() {
            return this.kj;
        }

        public boolean hasFamilyName() {
            return this.iD.contains(Integer.valueOf(2));
        }

        public boolean hasFormatted() {
            return this.iD.contains(Integer.valueOf(3));
        }

        public boolean hasGivenName() {
            return this.iD.contains(Integer.valueOf(4));
        }

        public boolean hasHonorificPrefix() {
            return this.iD.contains(Integer.valueOf(5));
        }

        public boolean hasHonorificSuffix() {
            return this.iD.contains(Integer.valueOf(6));
        }

        public boolean hasMiddleName() {
            return this.iD.contains(Integer.valueOf(7));
        }

        public int hashCode() {
            int i = 0;
            for (C0113a c0113a : iC.values()) {
                int hashCode;
                if (mo717a(c0113a)) {
                    hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                } else {
                    hashCode = i;
                }
                i = hashCode;
            }
            return i;
        }

        int m1052i() {
            return this.ab;
        }

        public boolean isDataValid() {
            return true;
        }

        protected Object mo478m(String str) {
            return null;
        }

        protected boolean mo479n(String str) {
            return false;
        }

        public void writeToParcel(Parcel parcel, int i) {
            cj cjVar = CREATOR;
            cj.m1095a(this, parcel, i);
        }
    }

    public static class C0205e {
        public static int m1055G(String str) {
            if (str.equals("person")) {
                return 0;
            }
            if (str.equals("page")) {
                return 1;
            }
            throw new IllegalArgumentException("Unknown objectType string: " + str);
        }
    }

    public static final class C0206f extends ae implements SafeParcelable, Organizations {
        public static final ck CREATOR = new ck();
        private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
        private int aJ;
        private final int ab;
        private String di;
        private String hs;
        private final Set<Integer> iD;
        private String jb;
        private String js;
        private String kk;
        private String kl;
        private boolean km;
        private String mName;

        static {
            iC.put("department", C0113a.m276f("department", 2));
            iC.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, C0113a.m276f(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, 3));
            iC.put("endDate", C0113a.m276f("endDate", 4));
            iC.put("location", C0113a.m276f("location", 5));
            iC.put("name", C0113a.m276f("name", 6));
            iC.put("primary", C0113a.m275e("primary", 7));
            iC.put("startDate", C0113a.m276f("startDate", 8));
            iC.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, C0113a.m276f(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, 9));
            iC.put("type", C0113a.m269a("type", 10, new ab().m260b("work", 0).m260b("school", 1), false));
        }

        public C0206f() {
            this.ab = 1;
            this.iD = new HashSet();
        }

        C0206f(Set<Integer> set, int i, String str, String str2, String str3, String str4, String str5, boolean z, String str6, String str7, int i2) {
            this.iD = set;
            this.ab = i;
            this.kk = str;
            this.di = str2;
            this.jb = str3;
            this.kl = str4;
            this.mName = str5;
            this.km = z;
            this.js = str6;
            this.hs = str7;
            this.aJ = i2;
        }

        public HashMap<String, C0113a<?, ?>> mo477T() {
            return iC;
        }

        protected boolean mo717a(C0113a c0113a) {
            return this.iD.contains(Integer.valueOf(c0113a.aa()));
        }

        protected Object mo718b(C0113a c0113a) {
            switch (c0113a.aa()) {
                case 2:
                    return this.kk;
                case 3:
                    return this.di;
                case 4:
                    return this.jb;
                case 5:
                    return this.kl;
                case 6:
                    return this.mName;
                case 7:
                    return Boolean.valueOf(this.km);
                case 8:
                    return this.js;
                case 9:
                    return this.hs;
                case 10:
                    return Integer.valueOf(this.aJ);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
            }
        }

        Set<Integer> bH() {
            return this.iD;
        }

        public C0206f cs() {
            return this;
        }

        public int describeContents() {
            ck ckVar = CREATOR;
            return 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0206f)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0206f c0206f = (C0206f) obj;
            for (C0113a c0113a : iC.values()) {
                if (mo717a(c0113a)) {
                    if (!c0206f.mo717a(c0113a)) {
                        return false;
                    }
                    if (!mo718b(c0113a).equals(c0206f.mo718b(c0113a))) {
                        return false;
                    }
                } else if (c0206f.mo717a(c0113a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return cs();
        }

        public String getDepartment() {
            return this.kk;
        }

        public String getDescription() {
            return this.di;
        }

        public String getEndDate() {
            return this.jb;
        }

        public String getLocation() {
            return this.kl;
        }

        public String getName() {
            return this.mName;
        }

        public String getStartDate() {
            return this.js;
        }

        public String getTitle() {
            return this.hs;
        }

        public int getType() {
            return this.aJ;
        }

        public boolean hasDepartment() {
            return this.iD.contains(Integer.valueOf(2));
        }

        public boolean hasDescription() {
            return this.iD.contains(Integer.valueOf(3));
        }

        public boolean hasEndDate() {
            return this.iD.contains(Integer.valueOf(4));
        }

        public boolean hasLocation() {
            return this.iD.contains(Integer.valueOf(5));
        }

        public boolean hasName() {
            return this.iD.contains(Integer.valueOf(6));
        }

        public boolean hasPrimary() {
            return this.iD.contains(Integer.valueOf(7));
        }

        public boolean hasStartDate() {
            return this.iD.contains(Integer.valueOf(8));
        }

        public boolean hasTitle() {
            return this.iD.contains(Integer.valueOf(9));
        }

        public boolean hasType() {
            return this.iD.contains(Integer.valueOf(10));
        }

        public int hashCode() {
            int i = 0;
            for (C0113a c0113a : iC.values()) {
                int hashCode;
                if (mo717a(c0113a)) {
                    hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                } else {
                    hashCode = i;
                }
                i = hashCode;
            }
            return i;
        }

        int m1059i() {
            return this.ab;
        }

        public boolean isDataValid() {
            return true;
        }

        public boolean isPrimary() {
            return this.km;
        }

        protected Object mo478m(String str) {
            return null;
        }

        protected boolean mo479n(String str) {
            return false;
        }

        public void writeToParcel(Parcel parcel, int i) {
            ck ckVar = CREATOR;
            ck.m1097a(this, parcel, i);
        }
    }

    public static final class C0207g extends ae implements SafeParcelable, PlacesLived {
        public static final cl CREATOR = new cl();
        private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
        private final int ab;
        private final Set<Integer> iD;
        private boolean km;
        private String mValue;

        static {
            iC.put("primary", C0113a.m275e("primary", 2));
            iC.put("value", C0113a.m276f("value", 3));
        }

        public C0207g() {
            this.ab = 1;
            this.iD = new HashSet();
        }

        C0207g(Set<Integer> set, int i, boolean z, String str) {
            this.iD = set;
            this.ab = i;
            this.km = z;
            this.mValue = str;
        }

        public HashMap<String, C0113a<?, ?>> mo477T() {
            return iC;
        }

        protected boolean mo717a(C0113a c0113a) {
            return this.iD.contains(Integer.valueOf(c0113a.aa()));
        }

        protected Object mo718b(C0113a c0113a) {
            switch (c0113a.aa()) {
                case 2:
                    return Boolean.valueOf(this.km);
                case 3:
                    return this.mValue;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
            }
        }

        Set<Integer> bH() {
            return this.iD;
        }

        public C0207g ct() {
            return this;
        }

        public int describeContents() {
            cl clVar = CREATOR;
            return 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0207g)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0207g c0207g = (C0207g) obj;
            for (C0113a c0113a : iC.values()) {
                if (mo717a(c0113a)) {
                    if (!c0207g.mo717a(c0113a)) {
                        return false;
                    }
                    if (!mo718b(c0113a).equals(c0207g.mo718b(c0113a))) {
                        return false;
                    }
                } else if (c0207g.mo717a(c0113a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return ct();
        }

        public String getValue() {
            return this.mValue;
        }

        public boolean hasPrimary() {
            return this.iD.contains(Integer.valueOf(2));
        }

        public boolean hasValue() {
            return this.iD.contains(Integer.valueOf(3));
        }

        public int hashCode() {
            int i = 0;
            for (C0113a c0113a : iC.values()) {
                int hashCode;
                if (mo717a(c0113a)) {
                    hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                } else {
                    hashCode = i;
                }
                i = hashCode;
            }
            return i;
        }

        int m1065i() {
            return this.ab;
        }

        public boolean isDataValid() {
            return true;
        }

        public boolean isPrimary() {
            return this.km;
        }

        protected Object mo478m(String str) {
            return null;
        }

        protected boolean mo479n(String str) {
            return false;
        }

        public void writeToParcel(Parcel parcel, int i) {
            cl clVar = CREATOR;
            cl.m1099a(this, parcel, i);
        }
    }

    public static final class C0208h extends ae implements SafeParcelable, Urls {
        public static final cm CREATOR = new cm();
        private static final HashMap<String, C0113a<?, ?>> iC = new HashMap();
        private int aJ;
        private final int ab;
        private final Set<Integer> iD;
        private String kn;
        private final int ko;
        private String mValue;

        static {
            iC.put(PlusShare.KEY_CALL_TO_ACTION_LABEL, C0113a.m276f(PlusShare.KEY_CALL_TO_ACTION_LABEL, 5));
            iC.put("type", C0113a.m269a("type", 6, new ab().m260b("home", 0).m260b("work", 1).m260b("blog", 2).m260b("profile", 3).m260b("other", 4).m260b("otherProfile", 5).m260b("contributor", 6).m260b("website", 7), false));
            iC.put("value", C0113a.m276f("value", 4));
        }

        public C0208h() {
            this.ko = 4;
            this.ab = 2;
            this.iD = new HashSet();
        }

        C0208h(Set<Integer> set, int i, String str, int i2, String str2, int i3) {
            this.ko = 4;
            this.iD = set;
            this.ab = i;
            this.kn = str;
            this.aJ = i2;
            this.mValue = str2;
        }

        public HashMap<String, C0113a<?, ?>> mo477T() {
            return iC;
        }

        protected boolean mo717a(C0113a c0113a) {
            return this.iD.contains(Integer.valueOf(c0113a.aa()));
        }

        protected Object mo718b(C0113a c0113a) {
            switch (c0113a.aa()) {
                case 4:
                    return this.mValue;
                case 5:
                    return this.kn;
                case 6:
                    return Integer.valueOf(this.aJ);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
            }
        }

        Set<Integer> bH() {
            return this.iD;
        }

        @Deprecated
        public int cu() {
            return 4;
        }

        public C0208h cv() {
            return this;
        }

        public int describeContents() {
            cm cmVar = CREATOR;
            return 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0208h)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0208h c0208h = (C0208h) obj;
            for (C0113a c0113a : iC.values()) {
                if (mo717a(c0113a)) {
                    if (!c0208h.mo717a(c0113a)) {
                        return false;
                    }
                    if (!mo718b(c0113a).equals(c0208h.mo718b(c0113a))) {
                        return false;
                    }
                } else if (c0208h.mo717a(c0113a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return cv();
        }

        public String getLabel() {
            return this.kn;
        }

        public int getType() {
            return this.aJ;
        }

        public String getValue() {
            return this.mValue;
        }

        public boolean hasLabel() {
            return this.iD.contains(Integer.valueOf(5));
        }

        public boolean hasType() {
            return this.iD.contains(Integer.valueOf(6));
        }

        public boolean hasValue() {
            return this.iD.contains(Integer.valueOf(4));
        }

        public int hashCode() {
            int i = 0;
            for (C0113a c0113a : iC.values()) {
                int hashCode;
                if (mo717a(c0113a)) {
                    hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
                } else {
                    hashCode = i;
                }
                i = hashCode;
            }
            return i;
        }

        int m1071i() {
            return this.ab;
        }

        public boolean isDataValid() {
            return true;
        }

        protected Object mo478m(String str) {
            return null;
        }

        protected boolean mo479n(String str) {
            return false;
        }

        public void writeToParcel(Parcel parcel, int i) {
            cm cmVar = CREATOR;
            cm.m1101a(this, parcel, i);
        }
    }

    static {
        iC.put("aboutMe", C0113a.m276f("aboutMe", 2));
        iC.put("ageRange", C0113a.m270a("ageRange", 3, C0199a.class));
        iC.put("birthday", C0113a.m276f("birthday", 4));
        iC.put("braggingRights", C0113a.m276f("braggingRights", 5));
        iC.put("circledByCount", C0113a.m272c("circledByCount", 6));
        iC.put("cover", C0113a.m270a("cover", 7, C0202b.class));
        iC.put("currentLocation", C0113a.m276f("currentLocation", 8));
        iC.put("displayName", C0113a.m276f("displayName", 9));
        iC.put("gender", C0113a.m269a("gender", 12, new ab().m260b("male", 0).m260b("female", 1).m260b("other", 2), false));
        iC.put("id", C0113a.m276f("id", 14));
        iC.put("image", C0113a.m270a("image", 15, C0203c.class));
        iC.put("isPlusUser", C0113a.m275e("isPlusUser", 16));
        iC.put("language", C0113a.m276f("language", 18));
        iC.put("name", C0113a.m270a("name", 19, C0204d.class));
        iC.put("nickname", C0113a.m276f("nickname", 20));
        iC.put("objectType", C0113a.m269a("objectType", 21, new ab().m260b("person", 0).m260b("page", 1), false));
        iC.put("organizations", C0113a.m271b("organizations", 22, C0206f.class));
        iC.put("placesLived", C0113a.m271b("placesLived", 23, C0207g.class));
        iC.put("plusOneCount", C0113a.m272c("plusOneCount", 24));
        iC.put("relationshipStatus", C0113a.m269a("relationshipStatus", 25, new ab().m260b("single", 0).m260b("in_a_relationship", 1).m260b("engaged", 2).m260b("married", 3).m260b("its_complicated", 4).m260b("open_relationship", 5).m260b("widowed", 6).m260b("in_domestic_partnership", 7).m260b("in_civil_union", 8), false));
        iC.put("tagline", C0113a.m276f("tagline", 26));
        iC.put("url", C0113a.m276f("url", 27));
        iC.put("urls", C0113a.m271b("urls", 28, C0208h.class));
        iC.put("verified", C0113a.m275e("verified", 29));
    }

    public cc() {
        this.ab = 2;
        this.iD = new HashSet();
    }

    public cc(String str, String str2, C0203c c0203c, int i, String str3) {
        this.ab = 2;
        this.iD = new HashSet();
        this.cl = str;
        this.iD.add(Integer.valueOf(9));
        this.jh = str2;
        this.iD.add(Integer.valueOf(14));
        this.jM = c0203c;
        this.iD.add(Integer.valueOf(15));
        this.jR = i;
        this.iD.add(Integer.valueOf(21));
        this.ie = str3;
        this.iD.add(Integer.valueOf(27));
    }

    cc(Set<Integer> set, int i, String str, C0199a c0199a, String str2, String str3, int i2, C0202b c0202b, String str4, String str5, int i3, String str6, C0203c c0203c, boolean z, String str7, C0204d c0204d, String str8, int i4, List<C0206f> list, List<C0207g> list2, int i5, int i6, String str9, String str10, List<C0208h> list3, boolean z2) {
        this.iD = set;
        this.ab = i;
        this.jE = str;
        this.jF = c0199a;
        this.jG = str2;
        this.jH = str3;
        this.jI = i2;
        this.jJ = c0202b;
        this.jK = str4;
        this.cl = str5;
        this.jL = i3;
        this.jh = str6;
        this.jM = c0203c;
        this.jN = z;
        this.jO = str7;
        this.jP = c0204d;
        this.jQ = str8;
        this.jR = i4;
        this.jS = list;
        this.jT = list2;
        this.jU = i5;
        this.jV = i6;
        this.jW = str9;
        this.ie = str10;
        this.jX = list3;
        this.jY = z2;
    }

    public static cc m1074d(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        cc y = CREATOR.m1083y(obtain);
        obtain.recycle();
        return y;
    }

    public HashMap<String, C0113a<?, ?>> mo477T() {
        return iC;
    }

    protected boolean mo717a(C0113a c0113a) {
        return this.iD.contains(Integer.valueOf(c0113a.aa()));
    }

    protected Object mo718b(C0113a c0113a) {
        switch (c0113a.aa()) {
            case 2:
                return this.jE;
            case 3:
                return this.jF;
            case 4:
                return this.jG;
            case 5:
                return this.jH;
            case 6:
                return Integer.valueOf(this.jI);
            case 7:
                return this.jJ;
            case 8:
                return this.jK;
            case 9:
                return this.cl;
            case 12:
                return Integer.valueOf(this.jL);
            case 14:
                return this.jh;
            case 15:
                return this.jM;
            case 16:
                return Boolean.valueOf(this.jN);
            case 18:
                return this.jO;
            case 19:
                return this.jP;
            case 20:
                return this.jQ;
            case 21:
                return Integer.valueOf(this.jR);
            case 22:
                return this.jS;
            case 23:
                return this.jT;
            case 24:
                return Integer.valueOf(this.jU);
            case 25:
                return Integer.valueOf(this.jV);
            case 26:
                return this.jW;
            case 27:
                return this.ie;
            case 28:
                return this.jX;
            case 29:
                return Boolean.valueOf(this.jY);
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + c0113a.aa());
        }
    }

    Set<Integer> bH() {
        return this.iD;
    }

    C0199a cc() {
        return this.jF;
    }

    C0202b cd() {
        return this.jJ;
    }

    C0203c ce() {
        return this.jM;
    }

    C0204d cf() {
        return this.jP;
    }

    List<C0206f> cg() {
        return this.jS;
    }

    List<C0207g> ch() {
        return this.jT;
    }

    List<C0208h> ci() {
        return this.jX;
    }

    public cc cj() {
        return this;
    }

    public int describeContents() {
        cd cdVar = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof cc)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        cc ccVar = (cc) obj;
        for (C0113a c0113a : iC.values()) {
            if (mo717a(c0113a)) {
                if (!ccVar.mo717a(c0113a)) {
                    return false;
                }
                if (!mo718b(c0113a).equals(ccVar.mo718b(c0113a))) {
                    return false;
                }
            } else if (ccVar.mo717a(c0113a)) {
                return false;
            }
        }
        return true;
    }

    public /* synthetic */ Object freeze() {
        return cj();
    }

    public String getAboutMe() {
        return this.jE;
    }

    public AgeRange getAgeRange() {
        return this.jF;
    }

    public String getBirthday() {
        return this.jG;
    }

    public String getBraggingRights() {
        return this.jH;
    }

    public int getCircledByCount() {
        return this.jI;
    }

    public Cover getCover() {
        return this.jJ;
    }

    public String getCurrentLocation() {
        return this.jK;
    }

    public String getDisplayName() {
        return this.cl;
    }

    @Deprecated
    public List<Emails> getEmails() {
        return null;
    }

    public int getGender() {
        return this.jL;
    }

    public String getId() {
        return this.jh;
    }

    public Image getImage() {
        return this.jM;
    }

    public String getLanguage() {
        return this.jO;
    }

    public Name getName() {
        return this.jP;
    }

    public String getNickname() {
        return this.jQ;
    }

    public int getObjectType() {
        return this.jR;
    }

    public List<Organizations> getOrganizations() {
        return (ArrayList) this.jS;
    }

    public List<PlacesLived> getPlacesLived() {
        return (ArrayList) this.jT;
    }

    public int getPlusOneCount() {
        return this.jU;
    }

    public int getRelationshipStatus() {
        return this.jV;
    }

    public String getTagline() {
        return this.jW;
    }

    public String getUrl() {
        return this.ie;
    }

    public List<Urls> getUrls() {
        return (ArrayList) this.jX;
    }

    public boolean hasAboutMe() {
        return this.iD.contains(Integer.valueOf(2));
    }

    public boolean hasAgeRange() {
        return this.iD.contains(Integer.valueOf(3));
    }

    public boolean hasBirthday() {
        return this.iD.contains(Integer.valueOf(4));
    }

    public boolean hasBraggingRights() {
        return this.iD.contains(Integer.valueOf(5));
    }

    public boolean hasCircledByCount() {
        return this.iD.contains(Integer.valueOf(6));
    }

    public boolean hasCover() {
        return this.iD.contains(Integer.valueOf(7));
    }

    public boolean hasCurrentLocation() {
        return this.iD.contains(Integer.valueOf(8));
    }

    public boolean hasDisplayName() {
        return this.iD.contains(Integer.valueOf(9));
    }

    @Deprecated
    public boolean hasEmails() {
        return false;
    }

    public boolean hasGender() {
        return this.iD.contains(Integer.valueOf(12));
    }

    public boolean hasId() {
        return this.iD.contains(Integer.valueOf(14));
    }

    public boolean hasImage() {
        return this.iD.contains(Integer.valueOf(15));
    }

    public boolean hasIsPlusUser() {
        return this.iD.contains(Integer.valueOf(16));
    }

    public boolean hasLanguage() {
        return this.iD.contains(Integer.valueOf(18));
    }

    public boolean hasName() {
        return this.iD.contains(Integer.valueOf(19));
    }

    public boolean hasNickname() {
        return this.iD.contains(Integer.valueOf(20));
    }

    public boolean hasObjectType() {
        return this.iD.contains(Integer.valueOf(21));
    }

    public boolean hasOrganizations() {
        return this.iD.contains(Integer.valueOf(22));
    }

    public boolean hasPlacesLived() {
        return this.iD.contains(Integer.valueOf(23));
    }

    public boolean hasPlusOneCount() {
        return this.iD.contains(Integer.valueOf(24));
    }

    public boolean hasRelationshipStatus() {
        return this.iD.contains(Integer.valueOf(25));
    }

    public boolean hasTagline() {
        return this.iD.contains(Integer.valueOf(26));
    }

    public boolean hasUrl() {
        return this.iD.contains(Integer.valueOf(27));
    }

    public boolean hasUrls() {
        return this.iD.contains(Integer.valueOf(28));
    }

    public boolean hasVerified() {
        return this.iD.contains(Integer.valueOf(29));
    }

    public int hashCode() {
        int i = 0;
        for (C0113a c0113a : iC.values()) {
            int hashCode;
            if (mo717a(c0113a)) {
                hashCode = mo718b(c0113a).hashCode() + (i + c0113a.aa());
            } else {
                hashCode = i;
            }
            i = hashCode;
        }
        return i;
    }

    int m1078i() {
        return this.ab;
    }

    public boolean isDataValid() {
        return true;
    }

    public boolean isPlusUser() {
        return this.jN;
    }

    public boolean isVerified() {
        return this.jY;
    }

    protected Object mo478m(String str) {
        return null;
    }

    protected boolean mo479n(String str) {
        return false;
    }

    public void writeToParcel(Parcel parcel, int i) {
        cd cdVar = CREATOR;
        cd.m1081a(this, parcel, i);
    }
}
