package com.sony.rdis.receiver.utility;

import android.app.Activity;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.sony.btv.remotesensor.RemoteSensor;
import com.sony.btv.remotesensor.RemoteSensorManager;
import com.sony.rdis.common.Dbg;
import com.sony.rdis.receiver.RdisClient;
import com.sony.rdis.receiver.RdisClientListener;
import com.sony.rdis.receiver.RdisDefaultSensorListener;
import com.sony.rdis.receiver.RdisGeneralCommunicationListener;
import com.sony.rdis.receiver.RdisManager;
import com.sony.rdis.receiver.RdisRemoteController;
import com.sony.rdis.receiver.RdisRemoteControllerListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.HttpVersions;

public class RdisUtility {
    private static final boolean DEBUG_HID = false;
    private static final String logTag = "RDIS_UTIL";
    private static final String mRemoteControlString = "Remote Control";
    private final int DEFAULT_GAME_PAD_ID = -1;
    private final int DEFAULT_SENSOR_RATE = 2;
    private Activity mActivity = null;
    private boolean mActivityIsResumed = false;
    private RdisUtilityGamePadInfo mDefaultSensorGamePadInfo = null;
    private int mDefaultSensorMobileId = -100;
    private List<RdisUtilityGamePadInfo> mGamePadInfoList = null;
    private String mKeyConfiguration = null;
    private RdisUtilityConnectionListener mListener = null;
    private RdisClientListener mRdisClientListener = null;
    private RdisDefaultSensorListener mRdisDefaultSensorListener = null;
    private RdisGeneralCommunicationListener mRdisGeneralCommunicationListener = null;
    private RdisManager mRdisManager = null;
    private RdisRemoteControllerListener mRdisRemoteControllerListener = null;
    private RdisUtilityGamePadInfo mRemoconGamePadInfo = null;
    private RemoteSensorManager mSensorManager;

    class C12611 implements RdisClientListener {
        C12611() {
        }

        public void onStatusChanged(int i, List<RdisClient> list) {
            RdisUtility.this.rdisLibStatusChanged(i, list);
        }
    }

    class C12622 implements RdisDefaultSensorListener {
        C12622() {
        }

        public void onDefaultSensorChanged(int i) {
            RdisUtility.this.changeDefaultSensorMobile(i);
        }
    }

    class C12633 implements RdisRemoteControllerListener {
        C12633() {
        }

        public void onConnected(RdisRemoteController rdisRemoteController) {
            RdisUtility.this.initRemoconGamePad(rdisRemoteController);
        }
    }

    class C12644 implements RdisGeneralCommunicationListener {
        C12644() {
        }

        public void onRecvData(int i, String str, String str2) {
            RdisUtility.this.recvedGeneralCommunicationData(i, str, str2);
        }
    }

    class C12655 extends Handler {
        C12655() {
        }

        public void handleMessage(Message message) {
            RdisUtility.this.initPostProcess();
        }
    }

    public RdisUtility(Activity activity, RdisUtilityConnectionListener rdisUtilityConnectionListener, RdisUtilityKeyConfiguration rdisUtilityKeyConfiguration) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Dbg.m1744i(logTag, "RdisUtility: initialzie. __2011_09_13_A__");
        if (activity == null) {
            Dbg.m1742e(logTag, "activity is null!");
            throw new NullPointerException();
        } else if (rdisUtilityConnectionListener == null) {
            Dbg.m1742e(logTag, "listener is null!");
            throw new NullPointerException();
        } else {
            this.mActivity = activity;
            this.mListener = rdisUtilityConnectionListener;
            this.mRdisClientListener = new C12611();
            this.mRdisDefaultSensorListener = new C12622();
            this.mRdisRemoteControllerListener = new C12633();
            this.mGamePadInfoList = new ArrayList();
            this.mSensorManager = RemoteSensorManager.getInstance(activity);
            this.mRdisManager = RdisManager.getRdisManager(activity);
            this.mRdisGeneralCommunicationListener = new C12644();
            this.mRdisManager.initGeneralCommunication(activity, "com.sony.rdis.gamepad.GamePad", this.mRdisGeneralCommunicationListener);
            if (rdisUtilityKeyConfiguration != null) {
                createKeyConfigMessage(rdisUtilityKeyConfiguration);
            }
            new C12655().sendEmptyMessage(0);
            Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
        }
    }

    private void changeDefaultSensorGamePadInfo(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        RdisUtilityGamePadInfo rdisUtilityGamePadInfo2 = this.mDefaultSensorGamePadInfo;
        this.mDefaultSensorGamePadInfo = rdisUtilityGamePadInfo;
        updateDefaultSensors(rdisUtilityGamePadInfo2);
        Dbg.m1746v(logTag, "changeDefaultSensorMobile(): newMobileId:" + rdisUtilityGamePadInfo.getMobileId());
    }

    private void changeDefaultSensorMobile(int i) {
        this.mDefaultSensorMobileId = i;
        for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
            if (rdisUtilityGamePadInfo.getMobileId() == i) {
                changeDefaultSensorGamePadInfo(rdisUtilityGamePadInfo);
                return;
            }
        }
        if (this.mRemoconGamePadInfo == null || this.mRemoconGamePadInfo.getMobileId() != i) {
            Dbg.m1744i(logTag, "changeDefaultSensorMobile(): mobileId:" + i + " has not been connected yet");
        } else {
            changeDefaultSensorGamePadInfo(this.mRemoconGamePadInfo);
        }
    }

    private void createKeyConfigMessage(RdisUtilityKeyConfiguration rdisUtilityKeyConfiguration) {
        JSONObject jSONObject = new JSONObject();
        try {
            if (rdisUtilityKeyConfiguration.useAnalogStick) {
                jSONObject.put("DIRECTIONAL", "ANALOG");
            } else {
                jSONObject.put("DIRECTIONAL", "DIGITAL");
            }
            jSONObject.put("DIRECTIONAL_KEY_UP", rdisUtilityKeyConfiguration.keyUp);
            jSONObject.put("DIRECTIONAL_KEY_DOWN", rdisUtilityKeyConfiguration.keyDown);
            jSONObject.put("DIRECTIONAL_KEY_LEFT", rdisUtilityKeyConfiguration.keyLeft);
            jSONObject.put("DIRECTIONAL_KEY_RIGHT", rdisUtilityKeyConfiguration.keyRight);
            jSONObject.put("KEY_A", rdisUtilityKeyConfiguration.keyA);
            jSONObject.put("KEY_B", rdisUtilityKeyConfiguration.keyB);
            jSONObject.put("KEY_C", rdisUtilityKeyConfiguration.keyC);
            jSONObject.put("KEY_D", rdisUtilityKeyConfiguration.keyD);
            this.mKeyConfiguration = jSONObject.toString();
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
        }
    }

    private int[] getRdisSensorType(RdisClient rdisClient) {
        return sensorListToSensorTypes(rdisClient.getSensorList(-1));
    }

    private List<Sensor> getSensorList(RdisUtilityGamePadInfo rdisUtilityGamePadInfo, int i) {
        return rdisUtilityGamePadInfo.mClient != null ? rdisUtilityGamePadInfo.mClient.getSensorList(i) : rdisUtilityGamePadInfo.mRemoteController != null ? rdisUtilityGamePadInfo.mRemoteController.getSensorList(i) : null;
    }

    private int[] getSensorType(RdisRemoteController rdisRemoteController) {
        if (Build.DEVICE.equalsIgnoreCase("asura") || Build.DEVICE.equalsIgnoreCase("eagle")) {
            return new int[0];
        }
        List<RemoteSensor> sensorList = this.mSensorManager.getSensorList(-1);
        List arrayList = new ArrayList();
        for (RemoteSensor remoteSensor : sensorList) {
            try {
                Constructor[] declaredConstructors = Class.forName("android.hardware.Sensor").getDeclaredConstructors();
                declaredConstructors[0].setAccessible(true);
                Sensor sensor = (Sensor) declaredConstructors[0].newInstance(new Object[0]);
                Field declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mType");
                declaredField.setAccessible(true);
                declaredField.set(sensor, Integer.valueOf(remoteSensor.getType()));
                declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mName");
                declaredField.setAccessible(true);
                declaredField.set(sensor, remoteSensor.getName());
                declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mVendor");
                declaredField.setAccessible(true);
                declaredField.set(sensor, remoteSensor.getVendor());
                declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mVersion");
                declaredField.setAccessible(true);
                declaredField.set(sensor, Integer.valueOf(remoteSensor.getVersion()));
                declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mMaxRange");
                declaredField.setAccessible(true);
                declaredField.set(sensor, Float.valueOf(remoteSensor.getMaximumRange()));
                declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mResolution");
                declaredField.setAccessible(true);
                declaredField.set(sensor, Float.valueOf(remoteSensor.getResolution()));
                declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mPower");
                declaredField.setAccessible(true);
                declaredField.set(sensor, Float.valueOf(remoteSensor.getPower()));
                declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mMinDelay");
                declaredField.setAccessible(true);
                declaredField.set(sensor, Integer.valueOf(remoteSensor.getMinDelay()));
                arrayList.add(sensor);
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
            } catch (Exception e222222) {
                Dbg.printStackTrace(e222222);
            }
        }
        return sensorListToSensorTypes(arrayList);
    }

    private void initPostProcess() {
        Dbg.m1744i(logTag, "initPostProcess()");
        this.mRdisManager.registerListener(this.mRdisClientListener);
        this.mRdisManager.registerDefaultSensorListener(this.mRdisDefaultSensorListener);
        this.mRdisManager.registerRemoteControllerListener(this.mRdisRemoteControllerListener);
    }

    private void initRemoconGamePad(RdisRemoteController rdisRemoteController) {
        Dbg.m1744i(logTag, "initRemoconGamePad()");
        int mobileId = rdisRemoteController.getMobileId();
        int[] sensorType = getSensorType(rdisRemoteController);
        this.mRemoconGamePadInfo = new RdisUtilityGamePadInfo(mobileId);
        this.mRemoconGamePadInfo.mMacAddress = mRemoteControlString;
        this.mRemoconGamePadInfo.mNickname = mRemoteControlString;
        this.mRemoconGamePadInfo.mGamePadInfoFlag = true;
        this.mRemoconGamePadInfo.mGamePad = new RdisUtilityGamePad(true, mobileId, sensorType, this.mRemoconGamePadInfo);
        this.mRemoconGamePadInfo.mRemoteController = rdisRemoteController;
        if (mobileId == this.mDefaultSensorMobileId) {
            changeDefaultSensorMobile(mobileId);
        }
        if (this.mListener != null) {
            this.mListener.onConnected(this.mRemoconGamePadInfo.mGamePad);
        }
    }

    private boolean isSensorTypeValid(int[] iArr, int[] iArr2) {
        for (int i : iArr) {
            boolean z;
            for (int i2 : iArr2) {
                if (i == i2) {
                    z = true;
                    break;
                }
            }
            z = false;
            if (!z) {
                return false;
            }
        }
        return true;
    }

    private void rdisLibStatusChanged(int i, List<RdisClient> list) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Dbg.m1744i(logTag, "rdisLibStatusChanged: status=" + i + " (ADDED= " + 1 + ")");
        int mobileId;
        RdisUtilityGamePad rdisUtilityGamePad;
        switch (i) {
            case -1:
            case 2:
                for (RdisClient mobileId2 : list) {
                    mobileId = mobileId2.getMobileId();
                    Iterator it = this.mGamePadInfoList.iterator();
                    while (it.hasNext()) {
                        RdisUtilityGamePadInfo rdisUtilityGamePadInfo = (RdisUtilityGamePadInfo) it.next();
                        if (rdisUtilityGamePadInfo.getMobileId() == mobileId) {
                            rdisUtilityGamePad = rdisUtilityGamePadInfo.mGamePad;
                            if (this.mListener != null) {
                                this.mListener.onDisconnected(rdisUtilityGamePad);
                            }
                            unregisterGamePadInfo(rdisUtilityGamePadInfo);
                            it.remove();
                        }
                    }
                }
                break;
            case 1:
                for (RdisClient mobileId22 : list) {
                    mobileId = mobileId22.getMobileId();
                    RdisUtilityGamePadInfo rdisUtilityGamePadInfo2 = new RdisUtilityGamePadInfo(mobileId);
                    rdisUtilityGamePad = new RdisUtilityGamePad(false, mobileId, getRdisSensorType(mobileId22), rdisUtilityGamePadInfo2);
                    rdisUtilityGamePadInfo2.mClient = mobileId22;
                    rdisUtilityGamePadInfo2.mGamePad = rdisUtilityGamePad;
                    rdisUtilityGamePadInfo2.mGamePadInfoFlag = false;
                    this.mGamePadInfoList.add(rdisUtilityGamePadInfo2);
                    this.mRdisManager.sendGeneralCommunication(rdisUtilityGamePadInfo2.getMobileId(), "REQUEST_NICKNAME", HttpVersions.HTTP_0_9);
                    this.mRdisManager.sendGeneralCommunication(rdisUtilityGamePadInfo2.getMobileId(), "REQUEST_MAC_ADDRESS", HttpVersions.HTTP_0_9);
                    if (mobileId == this.mDefaultSensorMobileId) {
                        changeDefaultSensorMobile(mobileId);
                    }
                }
                break;
            default:
                Dbg.m1742e(logTag, "unknown status!");
                break;
        }
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
    }

    private void recvedGeneralCommunicationData(int i, String str, String str2) {
        for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
            if (rdisUtilityGamePadInfo.getMobileId() == i) {
                if (rdisUtilityGamePadInfo.mGamePadInfoFlag) {
                    Dbg.m1742e(logTag, "ERROR: RECV DATA in UNKNOWN TIMING: " + str);
                } else {
                    if (str.equals("SEND_NICKNAME")) {
                        try {
                            rdisUtilityGamePadInfo.mNickname = new JSONObject(str2).getString("NICKNAME");
                        } catch (JSONException e) {
                            Dbg.m1742e(logTag, "ERROR nickname : " + e);
                        }
                    } else if (str.equals("SEND_MAC_ADDRESS")) {
                        try {
                            rdisUtilityGamePadInfo.mMacAddress = new JSONObject(str2).getString("MAC_ADDRESS");
                        } catch (JSONException e2) {
                            Dbg.m1742e(logTag, "ERROR nickname : " + e2);
                        }
                    } else {
                        Dbg.m1742e(logTag, "ERROR: RECV UNKNOWN DATA: " + str);
                    }
                    if (!(rdisUtilityGamePadInfo.mMacAddress == null || rdisUtilityGamePadInfo.mNickname == null)) {
                        rdisUtilityGamePadInfo.mGamePadInfoFlag = true;
                        if (this.mListener != null) {
                            this.mListener.onConnected(rdisUtilityGamePadInfo.mGamePad);
                        }
                    }
                }
            }
        }
    }

    private boolean registerGamePadInfo(RdisUtilityGamePadInfo rdisUtilityGamePadInfo, RdisUtilityEventListener rdisUtilityEventListener, int[] iArr, int i) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Dbg.m1740d(logTag, "gamePadInfo.mListener=" + rdisUtilityGamePadInfo.mListener);
        if (rdisUtilityGamePadInfo.mListener == null) {
            rdisUtilityGamePadInfo.mListener = rdisUtilityEventListener;
            if (iArr != null) {
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    Dbg.m1740d(logTag, "gamePadInfo.mRegisterdSensor.add() sensorType[" + i2 + "] = " + iArr[i2]);
                    rdisUtilityGamePadInfo.mRegisterdSensor.add(Integer.valueOf(iArr[i2]));
                }
            }
            if (i != 0) {
                rdisUtilityGamePadInfo.mUserColor = i;
            }
            if (this.mActivityIsResumed) {
                startSensor(rdisUtilityGamePadInfo);
                settingGamePad(rdisUtilityGamePadInfo);
                startHIDEvent(rdisUtilityGamePadInfo);
            }
            rdisUtilityGamePadInfo.mRegisteredFlag = true;
            Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
            return true;
        }
        Dbg.m1742e(logTag, "EventListener was already registered, so this request is fail.");
        return false;
    }

    private int[] sensorListToSensorTypes(List<Sensor> list) {
        int[] iArr = new int[list.size()];
        int i = 0;
        for (Sensor type : list) {
            iArr[i] = type.getType();
            i++;
        }
        return iArr;
    }

    private void settingGamePad(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        if (this.mKeyConfiguration != null) {
            this.mRdisManager.sendGeneralCommunication(rdisUtilityGamePadInfo.getMobileId(), "SET_KEY_CONFIG", this.mKeyConfiguration);
        }
        if (rdisUtilityGamePadInfo.mUserColor != 0) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("USER_COLOR", "0x" + Integer.toHexString(rdisUtilityGamePadInfo.mUserColor));
                this.mRdisManager.sendGeneralCommunication(rdisUtilityGamePadInfo.getMobileId(), "SET_USER_COLOR", jSONObject.toString());
            } catch (JSONException e) {
                Dbg.m1742e(logTag, "ERROR :" + e);
            }
        }
    }

    private void startHIDEvent(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        RdisManager rdisManager = this.mRdisManager;
        RdisManager.mRdisHidManager.registerOnTouchListener(rdisUtilityGamePadInfo.getMobileId(), rdisUtilityGamePadInfo);
    }

    private void startSensor(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        synchronized (rdisUtilityGamePadInfo) {
            Iterator it = rdisUtilityGamePadInfo.mRegisterdSensor.iterator();
            while (it.hasNext()) {
                Integer num = (Integer) it.next();
                List sensorList = getSensorList(rdisUtilityGamePadInfo, num.intValue());
                if (sensorList == null) {
                    Dbg.m1742e(logTag, "startSensor(): invalid GamePadInfo!");
                    return;
                }
                if (sensorList.size() != 0) {
                    Sensor sensor = (Sensor) sensorList.get(0);
                    RdisManager rdisManager = this.mRdisManager;
                    RdisManager.sensor.registerListener(rdisUtilityGamePadInfo, sensor, 2);
                }
                rdisUtilityGamePadInfo.mStartedSensor.add(num);
                it.remove();
                if (!(rdisUtilityGamePadInfo != this.mDefaultSensorGamePadInfo || Build.DEVICE.equalsIgnoreCase("asura") || Build.DEVICE.equalsIgnoreCase("eagle"))) {
                    List sensorList2 = this.mSensorManager.getSensorList(-1);
                    if (sensorList == null) {
                        Dbg.m1742e(logTag, "stopSensor(): invalid GamePadInfo!");
                        return;
                    }
                    this.mSensorManager.registerListener(rdisUtilityGamePadInfo.mRemoteSensorEventListener, (RemoteSensor) sensorList2.get(0), 2);
                }
            }
            Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
        }
    }

    private void stopHIDEvent(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        RdisManager rdisManager = this.mRdisManager;
        RdisManager.mRdisHidManager.unregisterOnTouchListener(rdisUtilityGamePadInfo.getMobileId(), rdisUtilityGamePadInfo);
    }

    private void stopSensor(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        synchronized (rdisUtilityGamePadInfo) {
            Iterator it = rdisUtilityGamePadInfo.mStartedSensor.iterator();
            while (it.hasNext()) {
                Integer num = (Integer) it.next();
                List sensorList = getSensorList(rdisUtilityGamePadInfo, num.intValue());
                if (sensorList == null) {
                    Dbg.m1742e(logTag, "stopSensor(): invalid GamePadInfo!");
                    return;
                }
                if (sensorList.size() != 0) {
                    Sensor sensor = (Sensor) sensorList.get(0);
                    RdisManager rdisManager = this.mRdisManager;
                    RdisManager.sensor.unregisterListener(rdisUtilityGamePadInfo, sensor);
                }
                if (rdisUtilityGamePadInfo == this.mDefaultSensorGamePadInfo) {
                    this.mSensorManager.unregisterListener(rdisUtilityGamePadInfo.mRemoteSensorEventListener, this.mSensorManager.getDefaultSensor(num.intValue()));
                }
                rdisUtilityGamePadInfo.mRegisterdSensor.add(num);
                it.remove();
            }
            Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
        }
    }

    private void unregisterGamePadInfo(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        if (this.mActivityIsResumed) {
            stopSensor(rdisUtilityGamePadInfo);
            unsettingGamePad(rdisUtilityGamePadInfo);
            stopHIDEvent(rdisUtilityGamePadInfo);
        }
        Iterator it = rdisUtilityGamePadInfo.mRegisterdSensor.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        rdisUtilityGamePadInfo.mListener = null;
        rdisUtilityGamePadInfo.mRegisteredFlag = false;
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
    }

    private void unsettingGamePad(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        if (this.mKeyConfiguration != null) {
            this.mRdisManager.sendGeneralCommunication(rdisUtilityGamePadInfo.getMobileId(), "UNSET_KEY_CONFIG", HttpVersions.HTTP_0_9);
        }
        if (rdisUtilityGamePadInfo.mUserColor != 0) {
            this.mRdisManager.sendGeneralCommunication(rdisUtilityGamePadInfo.getMobileId(), "UNSET_USER_COLOR", HttpVersions.HTTP_0_9);
        }
    }

    private void updateDefaultSensors(RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        if (rdisUtilityGamePadInfo != null) {
            for (Integer intValue : rdisUtilityGamePadInfo.mStartedSensor) {
                this.mSensorManager.unregisterListener(rdisUtilityGamePadInfo.mRemoteSensorEventListener, this.mSensorManager.getDefaultSensor(intValue.intValue()));
            }
        }
        for (Integer intValue2 : this.mDefaultSensorGamePadInfo.mStartedSensor) {
            this.mSensorManager.registerListener(this.mDefaultSensorGamePadInfo.mRemoteSensorEventListener, this.mSensorManager.getDefaultSensor(intValue2.intValue()), 2);
        }
    }

    public void destroy() {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Dbg.m1744i(logTag, "RdisUtility::destroy: ");
        this.mRdisManager.unregisterListener(this.mRdisClientListener);
        this.mRdisManager.unregisterDefaultSensorListener();
        this.mRdisManager.unregisterRemoteControllerListener();
        this.mRdisManager.destroyGeneralCommunication();
        this.mRdisManager.destroy();
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
    }

    public boolean keyDownEventDelivery(int i, KeyEvent keyEvent) {
        if (keyEvent == null) {
            Dbg.m1742e(logTag, "event is null!");
        } else {
            for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
                if (keyEvent.getDeviceId() == rdisUtilityGamePadInfo.mClient.getKeyDeviceId()) {
                    if (rdisUtilityGamePadInfo.mListener != null) {
                        return rdisUtilityGamePadInfo.mListener.onKeyDown(i, keyEvent);
                    }
                    Dbg.m1744i(logTag, "Not supported in this version!");
                    return false;
                }
            }
            if (!(this.mRemoconGamePadInfo == null || this.mRemoconGamePadInfo.mListener == null)) {
                return this.mRemoconGamePadInfo.mListener.onKeyDown(i, keyEvent);
            }
        }
        return false;
    }

    public boolean keyUpEventDelivery(int i, KeyEvent keyEvent) {
        if (keyEvent == null) {
            Dbg.m1742e(logTag, "event is null!");
        } else {
            for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
                if (keyEvent.getDeviceId() == rdisUtilityGamePadInfo.mClient.getKeyDeviceId()) {
                    if (rdisUtilityGamePadInfo.mListener != null) {
                        return rdisUtilityGamePadInfo.mListener.onKeyUp(i, keyEvent);
                    }
                    Dbg.m1744i(logTag, "EventListener is not registered.");
                    return false;
                }
            }
            if (!(this.mRemoconGamePadInfo == null || this.mRemoconGamePadInfo.mListener == null)) {
                return this.mRemoconGamePadInfo.mListener.onKeyUp(i, keyEvent);
            }
        }
        return false;
    }

    public void pause() {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Dbg.m1744i(logTag, "RdisUtility::pause: ");
        this.mActivityIsResumed = false;
        for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
            if (rdisUtilityGamePadInfo.mGamePadInfoFlag) {
                stopSensor(rdisUtilityGamePadInfo);
                unsettingGamePad(rdisUtilityGamePadInfo);
                stopHIDEvent(rdisUtilityGamePadInfo);
            }
        }
        if (this.mRemoconGamePadInfo != null) {
            stopSensor(this.mRemoconGamePadInfo);
        }
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
    }

    public boolean registerGamePad(RdisUtilityGamePad rdisUtilityGamePad, RdisUtilityEventListener rdisUtilityEventListener, int[] iArr, int i) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        if (rdisUtilityGamePad == null) {
            Dbg.m1742e(logTag, "gamePad is null!");
            return false;
        }
        if (iArr != null && iArr.length > 0) {
            Dbg.m1744i(logTag, "sensorType.length= " + iArr.length);
            Dbg.m1744i(logTag, "sensorType[0] = " + iArr[0]);
            if (!isSensorTypeValid(iArr, rdisUtilityGamePad.getSensorType())) {
                Dbg.m1742e(logTag, "sensorType includes invalid value");
                return false;
            }
        }
        Dbg.m1744i(logTag, "registerGamePad: ");
        if (rdisUtilityGamePad.isDefaultGamePad()) {
            return registerGamePadInfo(this.mRemoconGamePadInfo, rdisUtilityEventListener, iArr, i);
        }
        for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
            if (rdisUtilityGamePadInfo.getMobileId() == rdisUtilityGamePad.getDeviceId()) {
                return registerGamePadInfo(rdisUtilityGamePadInfo, rdisUtilityEventListener, iArr, i);
            }
        }
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
        return false;
    }

    public void resume() {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Dbg.m1744i(logTag, "RdisUtility::resume: ");
        this.mActivityIsResumed = true;
        for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
            if (rdisUtilityGamePadInfo.mGamePadInfoFlag) {
                startSensor(rdisUtilityGamePadInfo);
                settingGamePad(rdisUtilityGamePadInfo);
                startHIDEvent(rdisUtilityGamePadInfo);
            }
        }
        if (this.mRemoconGamePadInfo != null) {
            startSensor(this.mRemoconGamePadInfo);
        }
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
    }

    public boolean touchEventDelivery(MotionEvent motionEvent) {
        if (motionEvent == null) {
            Dbg.m1742e(logTag, "event is null!");
        } else {
            for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
                int deviceId = motionEvent.getDeviceId();
                if (deviceId != rdisUtilityGamePadInfo.mClient.getMouseDeviceId()) {
                    if (deviceId == rdisUtilityGamePadInfo.mClient.getTouchPanelDeviceId()) {
                    }
                }
                if (rdisUtilityGamePadInfo.mListener != null) {
                    return rdisUtilityGamePadInfo.mListener.onTouchEvent(motionEvent);
                }
                Dbg.m1744i(logTag, "EventListener is not registered.");
                return false;
            }
            if (!(this.mRemoconGamePadInfo == null || this.mRemoconGamePadInfo.mListener == null)) {
                return this.mRemoconGamePadInfo.mListener.onTouchEvent(motionEvent);
            }
        }
        return false;
    }

    public void unregisterGamePad(RdisUtilityGamePad rdisUtilityGamePad) {
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] start");
        Dbg.m1744i(logTag, "unregisterGamePad: ");
        if (rdisUtilityGamePad == null) {
            Dbg.m1742e(logTag, "gamePad is null");
            return;
        }
        if (!rdisUtilityGamePad.isDefaultGamePad()) {
            for (RdisUtilityGamePadInfo rdisUtilityGamePadInfo : this.mGamePadInfoList) {
                if (rdisUtilityGamePadInfo.getMobileId() == rdisUtilityGamePad.getDeviceId()) {
                    unregisterGamePadInfo(rdisUtilityGamePadInfo);
                    break;
                }
            }
        }
        unregisterGamePadInfo(this.mRemoconGamePadInfo);
        Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] end");
    }
}
