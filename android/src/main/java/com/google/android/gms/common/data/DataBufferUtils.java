package com.google.android.gms.common.data;

import java.util.ArrayList;
import java.util.Iterator;

public final class DataBufferUtils {
    private DataBufferUtils() {
    }

    public static <T, E extends Freezable<T>> ArrayList<T> freezeAndClose(DataBuffer<E> dataBuffer) {
        ArrayList<T> arrayList = new ArrayList(dataBuffer.getCount());
        try {
            Iterator it = dataBuffer.iterator();
            while (it.hasNext()) {
                arrayList.add(((Freezable) it.next()).freeze());
            }
            return arrayList;
        } finally {
            dataBuffer.close();
        }
    }
}
