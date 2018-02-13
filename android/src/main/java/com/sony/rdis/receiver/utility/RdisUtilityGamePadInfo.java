package com.sony.rdis.receiver.utility;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import com.sony.btv.remotesensor.RemoteSensor;
import com.sony.btv.remotesensor.RemoteSensorEvent;
import com.sony.btv.remotesensor.RemoteSensorEventListener;
import com.sony.rdis.common.Dbg;
import com.sony.rdis.receiver.RdisClient;
import com.sony.rdis.receiver.RdisOnTouchListener;
import com.sony.rdis.receiver.RdisRemoteController;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RdisUtilityGamePadInfo implements SensorEventListener, RdisOnTouchListener {
    private static final String logTag = "RDIS_UTIL";
    public RdisClient mClient = null;
    public RdisUtilityGamePad mGamePad = null;
    public boolean mGamePadInfoFlag = false;
    public RdisUtilityEventListener mListener = null;
    public String mMacAddress = null;
    private int mMobileId = -1;
    public String mNickname = null;
    public List<Integer> mRegisterdSensor = null;
    public boolean mRegisteredFlag = false;
    public RdisRemoteController mRemoteController = null;
    public final RemoteSensorEventListener mRemoteSensorEventListener = new C12661();
    public List<Integer> mStartedSensor = null;
    public int mUserColor = 0;

    class C12661 implements RemoteSensorEventListener {
        C12661() {
        }

        public void onAccuracyChanged(RemoteSensor remoteSensor, int i) {
            RdisUtilityGamePadInfo.this.onAccuracyChanged(RdisUtilityGamePadInfo.this.remoteSensorToAndroidSensor(remoteSensor), i);
        }

        public void onSensorChanged(RemoteSensorEvent remoteSensorEvent) {
            try {
                Constructor[] declaredConstructors = Class.forName("android.hardware.SensorEvent").getDeclaredConstructors();
                declaredConstructors[0].setAccessible(true);
                SensorEvent sensorEvent = (SensorEvent) declaredConstructors[0].newInstance(new Object[]{Integer.valueOf(3)});
                sensorEvent.values[0] = remoteSensorEvent.values[0];
                sensorEvent.values[1] = remoteSensorEvent.values[1];
                sensorEvent.values[2] = remoteSensorEvent.values[2];
                sensorEvent.sensor = RdisUtilityGamePadInfo.this.remoteSensorToAndroidSensor(remoteSensorEvent.sensor);
                sensorEvent.accuracy = remoteSensorEvent.accuracy;
                sensorEvent.timestamp = remoteSensorEvent.timestamp;
                RdisUtilityGamePadInfo.this.onSensorChanged(sensorEvent);
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
        }
    }

    public RdisUtilityGamePadInfo(int i) {
        Dbg.m1744i(logTag, "RdisUtilityGamePadInfo: ");
        this.mMobileId = i;
        this.mRegisterdSensor = new ArrayList();
        this.mStartedSensor = new ArrayList();
    }

    private Sensor remoteSensorToAndroidSensor(RemoteSensor remoteSensor) {
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
            return sensor;
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
        return null;
    }

    public int getMobileId() {
        return this.mMobileId;
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
        if (this.mListener != null) {
            this.mListener.onAccuracyChanged(sensor, i);
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.mListener != null) {
            this.mListener.onSensorChanged(sensorEvent);
        }
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        if (this.mListener != null) {
            this.mListener.onTouchEvent(motionEvent);
        }
    }
}
