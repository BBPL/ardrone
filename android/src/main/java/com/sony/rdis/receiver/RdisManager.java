package com.sony.rdis.receiver;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import com.sony.rdis.common.Dbg;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RdisManager implements DaemonCommnunicatorListener, ServiceCommunicatorListener, RdisSensorManagerListener {
    public static final int ADDED = 1;
    private static final int DEFAULT_SENSOR_MOBILE_HAS_NOT_BEEN_NOTIFIED = -100;
    private static final int FIRST_REMOCON_ID = -1;
    public static final int REMOVED = 2;
    public static final int REMOVED_UNEXPECTEDLY = -1;
    private static final String logTag = "RDIS_LIB";
    private static Activity mActiviy = null;
    private static ArrayList<RdisClient> mClientList = null;
    private static DaemonCommnunicator mDaemonCommunicatior = null;
    private static RdisDefaultSensorListener mDefaultSensorListener = null;
    private static int mDefaultSensorMobile = DEFAULT_SENSOR_MOBILE_HAS_NOT_BEEN_NOTIFIED;
    private static RdisRemoteController mGeneralRemocon = null;
    private static RdisClientListener mRdisClientListener = null;
    private static RdisGeneralCommunicationListener mRdisGeneralCommunicationListener = null;
    public static RdisHIDManager mRdisHidManager = null;
    private static RdisManager mRdisManager = new RdisManager();
    private static ArrayList<RdisRemoteController> mRemoconList = null;
    private static RdisRemoteControllerListener mRemoteControllerListener = null;
    private static ServiceCommunicator mServiceCommunicator = null;
    private static ServiceCommunicatorListener mServiceCommunicatorListener = null;
    public static RdisSensorManager sensor = null;

    class C12591 implements ServiceCommunicatorListener {
        C12591() {
        }

        public void recvData(int i, String str, String str2) {
            if (RdisManager.mRdisGeneralCommunicationListener != null) {
                RdisManager.mRdisGeneralCommunicationListener.onRecvData(i, str, str2);
            }
        }
    }

    private RdisManager() {
        Dbg.m1744i(logTag, "RdisManager __2011_09_13_A__");
    }

    public static RdisManager getRdisManager() {
        Dbg.m1744i(logTag, "RdisManger::getRdisManger");
        return mRdisManager;
    }

    public static RdisManager getRdisManager(Activity activity) {
        Dbg.m1744i(logTag, "RdisManger::getRdisManger");
        mActiviy = activity;
        mClientList = new ArrayList();
        mRemoconList = new ArrayList();
        if (mDaemonCommunicatior == null) {
            mDaemonCommunicatior = new DaemonCommnunicator(mRdisManager);
        }
        if (sensor == null) {
            sensor = new RdisSensorManager(activity, mClientList, mRemoconList, mRdisManager);
        }
        if (mRdisHidManager == null) {
            mRdisHidManager = new RdisHIDManager(activity);
        }
        return mRdisManager;
    }

    public void daemonConnectionFail() {
        mGeneralRemocon = new RdisRemoteController(-1);
        if (mActiviy != null) {
            for (Sensor addSensor : ((SensorManager) mActiviy.getSystemService("sensor")).getSensorList(-1)) {
                mGeneralRemocon.addSensor(addSensor);
            }
        }
        if (mRemoteControllerListener != null) {
            mRemoteControllerListener.onConnected(mGeneralRemocon);
        }
        defaultSensorChange(-1);
    }

    public void defaultSensorChange(int i) {
        mDefaultSensorMobile = i;
        if (mDefaultSensorListener != null) {
            mDefaultSensorListener.onDefaultSensorChanged(i);
        }
    }

    public void destroy() {
        mActiviy = null;
        if (mDaemonCommunicatior != null) {
            mDaemonCommunicatior.stopThread();
        }
        mDaemonCommunicatior = null;
        mRdisClientListener = null;
        mClientList = null;
        sensor = null;
        mRdisHidManager = null;
    }

    public void destroyGeneralCommunication() {
        mServiceCommunicator.destroy();
        mServiceCommunicatorListener = null;
    }

    public void fatalError(String str) {
        Dbg.m1742e(logTag, "fatalError: " + str);
    }

    public void initGeneralCommunication(Activity activity, String str, RdisGeneralCommunicationListener rdisGeneralCommunicationListener) {
        mRdisGeneralCommunicationListener = rdisGeneralCommunicationListener;
        mServiceCommunicatorListener = new C12591();
        mServiceCommunicator = new ServiceCommunicator(activity, mServiceCommunicatorListener, str);
    }

    public void mobileConnection(int i, RdisClient rdisClient) {
        mClientList.add(rdisClient);
        List arrayList = new ArrayList();
        arrayList.add(rdisClient);
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] " + "mRdisClientListener=" + mRdisClientListener);
        if (mRdisClientListener != null) {
            mRdisClientListener.onStatusChanged(1, arrayList);
        }
    }

    public void mobileDisconnection(int i) {
        Iterator it = mClientList.iterator();
        while (it.hasNext()) {
            RdisClient rdisClient = (RdisClient) it.next();
            if (rdisClient.getMobileId() == i) {
                it.remove();
                List arrayList = new ArrayList();
                arrayList.add(rdisClient);
                if (mRdisClientListener != null) {
                    mRdisClientListener.onStatusChanged(2, arrayList);
                }
            }
        }
    }

    public void recvData(int i, String str, String str2) {
    }

    public boolean registerDefaultSensorListener(RdisDefaultSensorListener rdisDefaultSensorListener) {
        if (rdisDefaultSensorListener == null) {
            Dbg.m1742e(logTag, "registerDefaultSensorListener() argument:listener is invalid.");
            return false;
        } else if (mDefaultSensorListener != null) {
            Dbg.m1742e(logTag, "registerDefaultSensorListener(): already registered.");
            return false;
        } else {
            mDefaultSensorListener = rdisDefaultSensorListener;
            if (mDefaultSensorMobile != DEFAULT_SENSOR_MOBILE_HAS_NOT_BEEN_NOTIFIED) {
                mDefaultSensorListener.onDefaultSensorChanged(mDefaultSensorMobile);
            }
            return true;
        }
    }

    public boolean registerListener(RdisClientListener rdisClientListener) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + " start] " + "listener=" + rdisClientListener);
        if (rdisClientListener == null) {
            Dbg.m1742e(logTag, "registerListener() argument:listener is illegal.");
            return false;
        } else if (mRdisClientListener != null) {
            Dbg.m1744i(logTag, "already registered!");
            return false;
        } else {
            mRdisClientListener = rdisClientListener;
            if (!mClientList.isEmpty()) {
                Dbg.m1746v(logTag, "onStatusChanged(ADDED) from registerListener()");
                mRdisClientListener.onStatusChanged(1, mClientList);
            }
            Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + " end] ");
            return true;
        }
    }

    public boolean registerRemoteControllerListener(RdisRemoteControllerListener rdisRemoteControllerListener) {
        if (rdisRemoteControllerListener == null) {
            Dbg.m1742e(logTag, "registerRemoteControllerListener() argument:listener is invalid.");
            return false;
        } else if (mRemoteControllerListener != null) {
            Dbg.m1742e(logTag, "registerRemoteControllerListener(): already registered.");
            return false;
        } else {
            mRemoteControllerListener = rdisRemoteControllerListener;
            Iterator it = mRemoconList.iterator();
            while (it.hasNext()) {
                mRemoteControllerListener.onConnected((RdisRemoteController) it.next());
            }
            if (mGeneralRemocon != null) {
                mRemoteControllerListener.onConnected(mGeneralRemocon);
            }
            return true;
        }
    }

    public void remoconConnection(RdisRemoteController rdisRemoteController) {
        mRemoconList.add(rdisRemoteController);
        if (mRemoteControllerListener != null) {
            mRemoteControllerListener.onConnected(rdisRemoteController);
        }
    }

    public void sendGeneralCommunication(int i, String str, String str2) {
        if (mServiceCommunicator != null) {
            mServiceCommunicator.sendData(i, str, str2);
        }
    }

    public void sensorEvent(int i, int i2, float[] fArr, int i3) {
        sensor.recvSensorEvent(i, i2, fArr, i3);
    }

    public void startSensorRequest(int i, int i2, int i3) {
        mDaemonCommunicatior.startSensor(i, i2, i3);
    }

    public void stopSensorRequest(int i, int i2) {
        mDaemonCommunicatior.stopSensor(i, i2);
    }

    public void touchEvent(int i, MotionEvent motionEvent) {
        mRdisHidManager.recvTouchEvent(i, motionEvent);
    }

    public void unregisterDefaultSensorListener() {
        mDefaultSensorListener = null;
    }

    public void unregisterListener(RdisClientListener rdisClientListener) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + " start] ");
        mRdisClientListener = null;
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + " end] ");
    }

    public void unregisterRemoteControllerListener() {
        mRemoteControllerListener = null;
    }

    public void vibrate(int i, long j) {
        mDaemonCommunicatior.vibrate(i, j);
    }
}
