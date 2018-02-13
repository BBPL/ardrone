package com.google.android.gms.common.data;

import java.util.ArrayList;

public abstract class C0053f<T> extends DataBuffer<T> {
    private boolean ao = false;
    private ArrayList<Integer> ap;

    protected C0053f(C0051d c0051d) {
        super(c0051d);
    }

    private int m58h(int i) {
        if (i >= 0 && i < this.ap.size()) {
            return ((Integer) this.ap.get(i)).intValue();
        }
        throw new IllegalArgumentException("Position " + i + " is out of bounds for this buffer");
    }

    private int m59i(int i) {
        return (i < 0 || i == this.ap.size()) ? 0 : i == this.ap.size() + -1 ? this.S.getCount() - ((Integer) this.ap.get(i)).intValue() : ((Integer) this.ap.get(i + 1)).intValue() - ((Integer) this.ap.get(i)).intValue();
    }

    private void m60m() {
        synchronized (this) {
            if (!this.ao) {
                int count = this.S.getCount();
                this.ap = new ArrayList();
                if (count > 0) {
                    this.ap.add(Integer.valueOf(0));
                    String primaryDataMarkerColumn = getPrimaryDataMarkerColumn();
                    String c = this.S.m44c(primaryDataMarkerColumn, 0, this.S.m46e(0));
                    int i = 1;
                    while (i < count) {
                        String c2 = this.S.m44c(primaryDataMarkerColumn, i, this.S.m46e(i));
                        if (c2.equals(c)) {
                            c2 = c;
                        } else {
                            this.ap.add(Integer.valueOf(i));
                        }
                        i++;
                        c = c2;
                    }
                }
                this.ao = true;
            }
        }
    }

    protected abstract T mo410a(int i, int i2);

    public final T get(int i) {
        m60m();
        return mo410a(m58h(i), m59i(i));
    }

    public int getCount() {
        m60m();
        return this.ap.size();
    }

    protected abstract String getPrimaryDataMarkerColumn();
}
