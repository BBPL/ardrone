package com.google.android.gms.games.achievement;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.games.C0089d;
import com.google.android.gms.games.Player;
import com.google.android.gms.internal.C0219h;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.C0241r.C0240a;
import com.google.android.gms.plus.PlusShare;
import org.mortbay.util.URIUtil;

public final class C0087a extends C0044b implements Achievement {
    C0087a(C0051d c0051d, int i) {
        super(c0051d, i);
    }

    public String getAchievementId() {
        return getString("external_achievement_id");
    }

    public int getCurrentSteps() {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        C0219h.m1132a(z);
        return getInteger("current_steps");
    }

    public String getDescription() {
        return getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION);
    }

    public void getDescription(CharArrayBuffer charArrayBuffer) {
        m4a(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, charArrayBuffer);
    }

    public String getFormattedCurrentSteps() {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        C0219h.m1132a(z);
        return getString("formatted_current_steps");
    }

    public void getFormattedCurrentSteps(CharArrayBuffer charArrayBuffer) {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        C0219h.m1132a(z);
        m4a("formatted_current_steps", charArrayBuffer);
    }

    public String getFormattedTotalSteps() {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        C0219h.m1132a(z);
        return getString("formatted_total_steps");
    }

    public void getFormattedTotalSteps(CharArrayBuffer charArrayBuffer) {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        C0219h.m1132a(z);
        m4a("formatted_total_steps", charArrayBuffer);
    }

    public long getLastUpdatedTimestamp() {
        return getLong("last_updated_timestamp");
    }

    public String getName() {
        return getString("name");
    }

    public void getName(CharArrayBuffer charArrayBuffer) {
        m4a("name", charArrayBuffer);
    }

    public Player getPlayer() {
        return new C0089d(this.S, this.V);
    }

    public Uri getRevealedImageUri() {
        return m5d("revealed_icon_image_uri");
    }

    public int getState() {
        return getInteger("state");
    }

    public int getTotalSteps() {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        C0219h.m1132a(z);
        return getInteger("total_steps");
    }

    public int getType() {
        return getInteger("type");
    }

    public Uri getUnlockedImageUri() {
        return m5d("unlocked_icon_image_uri");
    }

    public String toString() {
        C0240a a = C0241r.m1201c(this).m1199a("id", getAchievementId()).m1199a("name", getName()).m1199a("state", Integer.valueOf(getState())).m1199a("type", Integer.valueOf(getType()));
        if (getType() == 1) {
            a.m1199a("steps", getCurrentSteps() + URIUtil.SLASH + getTotalSteps());
        }
        return a.toString();
    }
}
