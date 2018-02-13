package com.sony.rdis.receiver;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import com.sony.rdis.common.Dbg;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RdisSensorManager {
    private static final boolean DEBUG_SENSOR = true;
    protected static final String LOG_TAG = "RdisSensorManager";
    private static final int SENSOR_DISABLE = -1;
    protected static final String logTag = "RdisSensorManager";
    private static ArrayList<RdisClient> mClientList;
    private static ArrayList<RdisRemoteController> mRemoconList;
    private static ArrayList<ListenerDelegate> sListeners = new ArrayList();
    private int listenerId = 0;
    private Context mContext;
    private RdisSensorManagerListener mListener = null;

    private class ListenerDelegate {
        private final Handler mHandler;
        private int mId;
        final SensorEventListener mSensorEventListener;
        private final ArrayList<Sensor> mSensorList = new ArrayList();
        private SensorEvent mValuesPool;

        ListenerDelegate(SensorEventListener sensorEventListener, Sensor sensor) {
            Dbg.m1740d("RdisSensorManager", "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
            this.mId = RdisSensorManager.this.listenerId = RdisSensorManager.this.listenerId + 1;
            this.mSensorEventListener = sensorEventListener;
            this.mHandler = new Handler(RdisSensorManager.this.mContext.getMainLooper(), RdisSensorManager.this) {
                public void handleMessage(Message message) {
                    SensorEvent sensorEvent = (SensorEvent) message.obj;
                    if (sensorEvent.accuracy >= 0) {
                        ListenerDelegate.this.mSensorEventListener.onAccuracyChanged(sensorEvent.sensor, sensorEvent.accuracy);
                    }
                    ListenerDelegate.this.mSensorEventListener.onSensorChanged(sensorEvent);
                    ListenerDelegate.this.returnToPool(sensorEvent);
                }
            };
            addSensor(sensor);
            Dbg.m1740d("RdisSensorManager", "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
        }

        void addSensor(Sensor sensor) {
            this.mSensorList.add(sensor);
        }

        protected SensorEvent createSensorEvent() {
            try {
                Constructor[] declaredConstructors = Class.forName("android.hardware.SensorEvent").getDeclaredConstructors();
                declaredConstructors[0].setAccessible(true);
                return (SensorEvent) declaredConstructors[0].newInstance(new Object[]{Integer.valueOf(3)});
            } catch (Exception e) {
                Dbg.printStackTrace(e);
            } catch (Exception e2) {
                Dbg.printStackTrace(e2);
            } catch (Exception e22) {
                Dbg.printStackTrace(e22);
            } catch (Exception e222) {
                Dbg.printStackTrace(e222);
            } catch (Exception e2222) {
                Dbg.printStackTrace(e2222);
            } catch (Exception e22222) {
                Dbg.printStackTrace(e22222);
            }
            return null;
        }

        protected SensorEvent getFromPool() {
            SensorEvent sensorEvent;
            synchronized (this) {
                sensorEvent = this.mValuesPool;
                this.mValuesPool = null;
            }
            return sensorEvent == null ? createSensorEvent() : sensorEvent;
        }

        int getId() {
            return this.mId;
        }

        Object getListener() {
            return this.mSensorEventListener;
        }

        List<Sensor> getSensors() {
            return this.mSensorList;
        }

        boolean hasSensor(Sensor sensor) {
            return this.mSensorList.contains(sensor);
        }

        void onSensorChangedLocked(Sensor sensor, float[] fArr, long[] jArr, int i) {
            SensorEvent fromPool = getFromPool();
            float[] fArr2 = fromPool.values;
            fArr2[0] = fArr[0];
            fArr2[1] = fArr[1];
            fArr2[2] = fArr[2];
            fromPool.timestamp = jArr[0];
            fromPool.accuracy = i;
            fromPool.sensor = sensor;
            Message obtain = Message.obtain();
            obtain.what = 0;
            obtain.obj = fromPool;
            this.mHandler.sendMessage(obtain);
        }

        void removeSensor(Sensor sensor) {
            this.mSensorList.remove(sensor);
        }

        protected void returnToPool(SensorEvent sensorEvent) {
            synchronized (this) {
                if (this.mValuesPool == null) {
                    this.mValuesPool = sensorEvent;
                }
            }
        }
    }

    public RdisSensorManager(Context context, ArrayList<RdisClient> arrayList, ArrayList<RdisRemoteController> arrayList2, RdisSensorManagerListener rdisSensorManagerListener) {
        Dbg.m1744i("RdisSensorManager", "RdisSensorManager, clientList: " + arrayList);
        this.mContext = context;
        mClientList = arrayList;
        mRemoconList = arrayList2;
        this.mListener = rdisSensorManagerListener;
    }

    private boolean enableSensor(ListenerDelegate listenerDelegate, Sensor sensor, int i) {
        int mobileId;
        int mobileId2;
        Dbg.m1740d("RdisSensorManager", "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Iterator it = mClientList.iterator();
        while (it.hasNext()) {
            RdisClient rdisClient = (RdisClient) it.next();
            if (rdisClient.getSensorList(sensor.getType()).contains(sensor)) {
                mobileId = rdisClient.getMobileId();
                break;
            }
        }
        mobileId = -100;
        if (mobileId == -100) {
            Iterator it2 = mRemoconList.iterator();
            while (it2.hasNext()) {
                RdisRemoteController rdisRemoteController = (RdisRemoteController) it2.next();
                if (rdisRemoteController.getSensorList(sensor.getType()).contains(sensor)) {
                    mobileId2 = rdisRemoteController.getMobileId();
                    break;
                }
            }
        }
        mobileId2 = mobileId;
        if (mobileId2 == -100) {
            Dbg.m1748w("RdisSensorManager", "enableSensor could not find the sensor, clientId: " + mobileId2 + ", type: " + sensor.getType());
            return false;
        }
        if (this.mListener != null) {
            if (i == -1) {
                this.mListener.stopSensorRequest(mobileId2, sensor.getType());
            } else {
                this.mListener.startSensorRequest(mobileId2, sensor.getType(), i);
            }
        }
        Dbg.m1740d("RdisSensorManager", "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
        return true;
    }

    public int getClientId(Sensor sensor) {
        Iterator it = mClientList.iterator();
        while (it.hasNext()) {
            RdisClient rdisClient = (RdisClient) it.next();
            if (rdisClient.getSensorList(sensor.getType()).contains(sensor)) {
                return rdisClient.getMobileId();
            }
        }
        it = mRemoconList.iterator();
        while (it.hasNext()) {
            RdisRemoteController rdisRemoteController = (RdisRemoteController) it.next();
            if (rdisRemoteController.getSensorList(sensor.getType()).contains(sensor)) {
                return rdisRemoteController.getMobileId();
            }
        }
        return -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void recvSensorEvent(int r10, int r11, float[] r12, int r13) {
        /*
        r9 = this;
        r1 = 0;
        r4 = java.lang.System.currentTimeMillis();
        r3 = sListeners;
        monitor-enter(r3);
        r0 = sListeners;	 Catch:{ all -> 0x0090 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0090 }
        if (r0 == 0) goto L_0x0012;
    L_0x0010:
        monitor-exit(r3);	 Catch:{ all -> 0x0090 }
    L_0x0011:
        return;
    L_0x0012:
        r2 = 0;
        r0 = mClientList;	 Catch:{ all -> 0x0090 }
        r6 = r0.iterator();	 Catch:{ all -> 0x0090 }
    L_0x0019:
        r0 = r6.hasNext();	 Catch:{ all -> 0x0090 }
        if (r0 == 0) goto L_0x003d;
    L_0x001f:
        r0 = r6.next();	 Catch:{ all -> 0x0090 }
        r0 = (com.sony.rdis.receiver.RdisClient) r0;	 Catch:{ all -> 0x0090 }
        r7 = r0.getMobileId();	 Catch:{ all -> 0x0090 }
        if (r7 != r10) goto L_0x0019;
    L_0x002b:
        r0 = r0.getSensorList(r11);	 Catch:{ all -> 0x0090 }
        r6 = r0.isEmpty();	 Catch:{ all -> 0x0090 }
        if (r6 != 0) goto L_0x003d;
    L_0x0035:
        r2 = 0;
        r0 = r0.get(r2);	 Catch:{ all -> 0x0090 }
        r0 = (android.hardware.Sensor) r0;	 Catch:{ all -> 0x0090 }
        r2 = r0;
    L_0x003d:
        if (r2 != 0) goto L_0x0069;
    L_0x003f:
        r0 = mRemoconList;	 Catch:{ all -> 0x0090 }
        r6 = r0.iterator();	 Catch:{ all -> 0x0090 }
    L_0x0045:
        r0 = r6.hasNext();	 Catch:{ all -> 0x0090 }
        if (r0 == 0) goto L_0x0069;
    L_0x004b:
        r0 = r6.next();	 Catch:{ all -> 0x0090 }
        r0 = (com.sony.rdis.receiver.RdisRemoteController) r0;	 Catch:{ all -> 0x0090 }
        r7 = r0.getMobileId();	 Catch:{ all -> 0x0090 }
        if (r7 != r10) goto L_0x0045;
    L_0x0057:
        r0 = r0.getSensorList(r11);	 Catch:{ all -> 0x0090 }
        r6 = r0.isEmpty();	 Catch:{ all -> 0x0090 }
        if (r6 != 0) goto L_0x0069;
    L_0x0061:
        r2 = 0;
        r0 = r0.get(r2);	 Catch:{ all -> 0x0090 }
        r0 = (android.hardware.Sensor) r0;	 Catch:{ all -> 0x0090 }
        r2 = r0;
    L_0x0069:
        if (r2 == 0) goto L_0x008e;
    L_0x006b:
        r0 = sListeners;	 Catch:{ all -> 0x0090 }
        r6 = r0.size();	 Catch:{ all -> 0x0090 }
    L_0x0071:
        if (r1 >= r6) goto L_0x008e;
    L_0x0073:
        r0 = sListeners;	 Catch:{ all -> 0x0090 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0090 }
        r0 = (com.sony.rdis.receiver.RdisSensorManager.ListenerDelegate) r0;	 Catch:{ all -> 0x0090 }
        r7 = r0.hasSensor(r2);	 Catch:{ all -> 0x0090 }
        if (r7 == 0) goto L_0x008a;
    L_0x0081:
        r7 = 1;
        r7 = new long[r7];	 Catch:{ all -> 0x0090 }
        r8 = 0;
        r7[r8] = r4;	 Catch:{ all -> 0x0090 }
        r0.onSensorChangedLocked(r2, r12, r7, r13);	 Catch:{ all -> 0x0090 }
    L_0x008a:
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0071;
    L_0x008e:
        monitor-exit(r3);	 Catch:{ all -> 0x0090 }
        goto L_0x0011;
    L_0x0090:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0090 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sony.rdis.receiver.RdisSensorManager.recvSensorEvent(int, int, float[], int):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean registerListener(android.hardware.SensorEventListener r6, android.hardware.Sensor r7, int r8) {
        /*
        r5 = this;
        r1 = 0;
        r0 = "RdisSensorManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "[";
        r2 = r2.append(r3);
        r3 = new java.lang.Throwable;
        r3.<init>();
        r3 = r3.getStackTrace();
        r3 = r3[r1];
        r3 = r3.getFileName();
        r2 = r2.append(r3);
        r3 = ":";
        r2 = r2.append(r3);
        r3 = new java.lang.Throwable;
        r3.<init>();
        r3 = r3.getStackTrace();
        r3 = r3[r1];
        r3 = r3.getClassName();
        r2 = r2.append(r3);
        r3 = ":";
        r2 = r2.append(r3);
        r3 = new java.lang.Throwable;
        r3.<init>();
        r3 = r3.getStackTrace();
        r3 = r3[r1];
        r3 = r3.getMethodName();
        r2 = r2.append(r3);
        r3 = ":";
        r2 = r2.append(r3);
        r3 = new java.lang.Throwable;
        r3.<init>();
        r3 = r3.getStackTrace();
        r3 = r3[r1];
        r3 = r3.getLineNumber();
        r2 = r2.append(r3);
        r3 = "] start";
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.sony.rdis.common.Dbg.m1740d(r0, r2);
        r0 = "RdisSensorManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "registerListener() listener: ";
        r2 = r2.append(r3);
        r2 = r2.append(r6);
        r3 = ", sensor: ";
        r2 = r2.append(r3);
        r2 = r2.append(r7);
        r3 = ", rate: ";
        r2 = r2.append(r3);
        r2 = r2.append(r8);
        r2 = r2.toString();
        com.sony.rdis.common.Dbg.m1746v(r0, r2);
        if (r6 == 0) goto L_0x00a9;
    L_0x00a7:
        if (r7 != 0) goto L_0x00aa;
    L_0x00a9:
        return r1;
    L_0x00aa:
        switch(r8) {
            case 0: goto L_0x015d;
            case 1: goto L_0x0160;
            case 2: goto L_0x0164;
            case 3: goto L_0x0169;
            default: goto L_0x00ad;
        };
    L_0x00ad:
        r2 = sListeners;
        monitor-enter(r2);
        r0 = sListeners;	 Catch:{ all -> 0x0181 }
        r3 = r0.iterator();	 Catch:{ all -> 0x0181 }
    L_0x00b6:
        r0 = r3.hasNext();	 Catch:{ all -> 0x0181 }
        if (r0 == 0) goto L_0x017e;
    L_0x00bc:
        r0 = r3.next();	 Catch:{ all -> 0x0181 }
        r0 = (com.sony.rdis.receiver.RdisSensorManager.ListenerDelegate) r0;	 Catch:{ all -> 0x0181 }
        r4 = r0.getListener();	 Catch:{ all -> 0x0181 }
        if (r4 != r6) goto L_0x00b6;
    L_0x00c8:
        if (r0 != 0) goto L_0x016e;
    L_0x00ca:
        r0 = new com.sony.rdis.receiver.RdisSensorManager$ListenerDelegate;	 Catch:{ all -> 0x017b }
        r0.<init>(r6, r7);	 Catch:{ all -> 0x017b }
        r3 = r8 / 1000;
        r3 = r5.enableSensor(r0, r7, r3);	 Catch:{ all -> 0x0181 }
        if (r3 == 0) goto L_0x00e1;
    L_0x00d7:
        r3 = sListeners;	 Catch:{ all -> 0x0181 }
        r3.add(r0);	 Catch:{ all -> 0x0181 }
        r0 = sListeners;	 Catch:{ all -> 0x0181 }
        r0.notify();	 Catch:{ all -> 0x0181 }
    L_0x00e1:
        monitor-exit(r2);	 Catch:{ all -> 0x0181 }
        r0 = "RdisSensorManager";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "[";
        r2 = r2.append(r3);
        r3 = new java.lang.Throwable;
        r3.<init>();
        r3 = r3.getStackTrace();
        r3 = r3[r1];
        r3 = r3.getFileName();
        r2 = r2.append(r3);
        r3 = ":";
        r2 = r2.append(r3);
        r3 = new java.lang.Throwable;
        r3.<init>();
        r3 = r3.getStackTrace();
        r3 = r3[r1];
        r3 = r3.getClassName();
        r2 = r2.append(r3);
        r3 = ":";
        r2 = r2.append(r3);
        r3 = new java.lang.Throwable;
        r3.<init>();
        r3 = r3.getStackTrace();
        r3 = r3[r1];
        r3 = r3.getMethodName();
        r2 = r2.append(r3);
        r3 = ":";
        r2 = r2.append(r3);
        r3 = new java.lang.Throwable;
        r3.<init>();
        r3 = r3.getStackTrace();
        r1 = r3[r1];
        r1 = r1.getLineNumber();
        r1 = r2.append(r1);
        r2 = "] end";
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.sony.rdis.common.Dbg.m1740d(r0, r1);
        r1 = 1;
        goto L_0x00a9;
    L_0x015d:
        r8 = r1;
        goto L_0x00ad;
    L_0x0160:
        r8 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
        goto L_0x00ad;
    L_0x0164:
        r8 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
        goto L_0x00ad;
    L_0x0169:
        r8 = 200000; // 0x30d40 float:2.8026E-40 double:9.8813E-319;
        goto L_0x00ad;
    L_0x016e:
        r3 = r8 / 1000;
        r3 = r5.enableSensor(r0, r7, r3);	 Catch:{ all -> 0x017b }
        if (r3 == 0) goto L_0x00e1;
    L_0x0176:
        r0.addSensor(r7);	 Catch:{ all -> 0x017b }
        goto L_0x00e1;
    L_0x017b:
        r0 = move-exception;
    L_0x017c:
        monitor-exit(r2);	 Catch:{ all -> 0x0181 }
        throw r0;
    L_0x017e:
        r0 = 0;
        goto L_0x00c8;
    L_0x0181:
        r0 = move-exception;
        goto L_0x017c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sony.rdis.receiver.RdisSensorManager.registerListener(android.hardware.SensorEventListener, android.hardware.Sensor, int):boolean");
    }

    public void setClientList(ArrayList<RdisClient> arrayList) {
        Dbg.m1744i("RdisSensorManager", "setClientList: " + arrayList);
        mClientList = arrayList;
    }

    public void unregisterListener(SensorEventListener sensorEventListener, Sensor sensor) {
        Dbg.m1740d("RdisSensorManager", "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Dbg.m1746v("RdisSensorManager", "unregisterListener() listener: " + sensorEventListener + ", sensor: " + sensor);
        if (sensorEventListener != null && sensor != null) {
            synchronized (sListeners) {
                int size = sListeners.size();
                int i = 0;
                while (i < size) {
                    ListenerDelegate listenerDelegate = (ListenerDelegate) sListeners.get(i);
                    if (listenerDelegate.getListener() == sensorEventListener) {
                        enableSensor(listenerDelegate, sensor, -1);
                        listenerDelegate.removeSensor(sensor);
                        if (listenerDelegate.getSensors().isEmpty()) {
                            sListeners.remove(i);
                        }
                    } else {
                        i++;
                    }
                }
            }
            Dbg.m1740d("RdisSensorManager", "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
        }
    }
}
