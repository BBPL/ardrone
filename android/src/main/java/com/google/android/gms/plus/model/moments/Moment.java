package com.google.android.gms.plus.model.moments;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.internal.bx;
import com.google.android.gms.internal.bz;
import java.util.HashSet;
import java.util.Set;

public interface Moment extends Freezable<Moment> {

    public static class Builder {
        private final Set<Integer> iD = new HashSet();
        private bx jB;
        private bx jC;
        private String jh;
        private String js;
        private String jy;

        public Moment build() {
            return new bz(this.iD, this.jh, this.jB, this.js, this.jC, this.jy);
        }

        public Builder setId(String str) {
            this.jh = str;
            this.iD.add(Integer.valueOf(2));
            return this;
        }

        public Builder setResult(ItemScope itemScope) {
            this.jB = (bx) itemScope;
            this.iD.add(Integer.valueOf(4));
            return this;
        }

        public Builder setStartDate(String str) {
            this.js = str;
            this.iD.add(Integer.valueOf(5));
            return this;
        }

        public Builder setTarget(ItemScope itemScope) {
            this.jC = (bx) itemScope;
            this.iD.add(Integer.valueOf(6));
            return this;
        }

        public Builder setType(String str) {
            this.jy = str;
            this.iD.add(Integer.valueOf(7));
            return this;
        }
    }

    String getId();

    ItemScope getResult();

    String getStartDate();

    ItemScope getTarget();

    String getType();

    boolean hasId();

    boolean hasResult();

    boolean hasStartDate();

    boolean hasTarget();

    boolean hasType();
}
