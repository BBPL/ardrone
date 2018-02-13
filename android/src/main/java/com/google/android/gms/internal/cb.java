package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.google.android.gms.plus.model.moments.Moment;

public final class cb extends C0044b implements Moment {
    private bz jD;

    public cb(C0051d c0051d, int i) {
        super(c0051d, i);
    }

    private bz cb() {
        synchronized (this) {
            if (this.jD == null) {
                byte[] byteArray = getByteArray("momentImpl");
                Parcel obtain = Parcel.obtain();
                obtain.unmarshall(byteArray, 0, byteArray.length);
                obtain.setDataPosition(0);
                this.jD = bz.CREATOR.m1018x(obtain);
                obtain.recycle();
            }
        }
        return this.jD;
    }

    public bz ca() {
        return cb();
    }

    public /* synthetic */ Object freeze() {
        return ca();
    }

    public String getId() {
        return cb().getId();
    }

    public ItemScope getResult() {
        return cb().getResult();
    }

    public String getStartDate() {
        return cb().getStartDate();
    }

    public ItemScope getTarget() {
        return cb().getTarget();
    }

    public String getType() {
        return cb().getType();
    }

    public boolean hasId() {
        return cb().hasId();
    }

    public boolean hasResult() {
        return cb().hasId();
    }

    public boolean hasStartDate() {
        return cb().hasStartDate();
    }

    public boolean hasTarget() {
        return cb().hasTarget();
    }

    public boolean hasType() {
        return cb().hasType();
    }
}
