package com.sony.rdis.receiver;

import android.hardware.Sensor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RdisRemoteController {
    private int mMobileId = 0;
    private ArrayList<Sensor> mSensorList = new ArrayList();

    public RdisRemoteController(int i) {
        this.mMobileId = i;
    }

    public void addSensor(Sensor sensor) {
        this.mSensorList.add(sensor);
    }

    public int getMobileId() {
        return this.mMobileId;
    }

    public List<Sensor> getSensorList(int i) {
        List<Sensor> unmodifiableList;
        ArrayList arrayList = this.mSensorList;
        synchronized (arrayList) {
            List list;
            if (i == -1) {
                list = arrayList;
            } else {
                List arrayList2 = new ArrayList();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Sensor sensor = (Sensor) it.next();
                    if (sensor.getType() == i) {
                        arrayList2.add(sensor);
                    }
                }
                list = arrayList2;
            }
            unmodifiableList = Collections.unmodifiableList(list);
        }
        return unmodifiableList;
    }
}
