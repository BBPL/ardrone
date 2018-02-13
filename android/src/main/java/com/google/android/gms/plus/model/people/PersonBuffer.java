package com.google.android.gms.plus.model.people;

import com.google.android.gms.common.data.C0048c;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.internal.cc;
import com.google.android.gms.internal.cn;

public final class PersonBuffer extends DataBuffer<Person> {
    private final C0048c<cc> kp;

    public PersonBuffer(C0051d c0051d) {
        super(c0051d);
        if (c0051d.m54l() == null || !c0051d.m54l().getBoolean("com.google.android.gms.plus.IsSafeParcelable", false)) {
            this.kp = null;
        } else {
            this.kp = new C0048c(c0051d, cc.CREATOR);
        }
    }

    public Person get(int i) {
        return this.kp != null ? (Person) this.kp.m33d(i) : new cn(this.S, i);
    }
}
