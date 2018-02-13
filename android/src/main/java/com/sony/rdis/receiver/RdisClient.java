package com.sony.rdis.receiver;

import android.hardware.Sensor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RdisClient {
    private boolean mEnableGpCommunication = false;
    private List<HumanInterfaceDeviceInfomation> mHidList = new ArrayList();
    private int mMobileId = -1;
    private String mProtocolName = null;
    private ArrayList<Sensor> mSensorList = new ArrayList();

    public RdisClient(int i) {
        this.mMobileId = i;
    }

    private int getDeviceId(int i) {
        for (HumanInterfaceDeviceInfomation humanInterfaceDeviceInfomation : this.mHidList) {
            if (humanInterfaceDeviceInfomation.getDeviceType() == i) {
                return humanInterfaceDeviceInfomation.getDeviceId();
            }
        }
        return -1;
    }

    public void addHid(HumanInterfaceDeviceInfomation humanInterfaceDeviceInfomation) {
        this.mHidList.add(humanInterfaceDeviceInfomation);
    }

    public void addSensor(Sensor sensor) {
        this.mSensorList.add(sensor);
    }

    public int getKeyDeviceId() {
        return getDeviceId(1);
    }

    public int getMobileId() {
        return this.mMobileId;
    }

    public int getMouseDeviceId() {
        return getDeviceId(3);
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

    public int getTouchPanelDeviceId() {
        return getDeviceId(2);
    }

    public void setGpCommunication(boolean z, String str) {
        this.mEnableGpCommunication = z;
        this.mProtocolName = str;
    }
}
