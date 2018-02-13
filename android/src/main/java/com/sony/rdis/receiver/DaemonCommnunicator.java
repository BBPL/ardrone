package com.sony.rdis.receiver;

import android.hardware.Sensor;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.os.Process;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import com.sony.rdis.common.Dbg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.util.StringUtil;

public class DaemonCommnunicator {
    private static final String DAEMON_SOCKET = "com.sony.rdis.daemon.receiver";
    private static final int SOCKETCMD_REPORT_APPLICATION_ID = 8391173;
    private static final int SOCKETCMD_REPORT_CONNECTION = 8390656;
    private static final int SOCKETCMD_REPORT_DEFAULT_SENSOR_CHANGED = 8391177;
    private static final int SOCKETCMD_REPORT_MOBILE_CONNECTION = 8391170;
    private static final int SOCKETCMD_REPORT_MOBILE_DISCONNECTION = 8391171;
    private static final int SOCKETCMD_REPORT_REMOCON_CONNECT = 8391186;
    private static final int SOCKETCMD_REPORT_REMOCON_DISCONNECT = 8391187;
    private static final int SOCKETCMD_REPORT_SENSOR_DEFAULT_ID = 8391189;
    private static final int SOCKETCMD_REPORT_SENSOR_EVENT = 8391176;
    private static final int SOCKETCMD_REPORT_TOUCH_EVENT = 8391188;
    private static final int SOCKETCMD_REPORT_VERSION = 8391168;
    private static final int SOCKETCMD_REQUEST_APPLICATION_ID = 8391172;
    private static final int SOCKETCMD_REQUEST_DELAY_OF_HAL_SENSOT = 8391182;
    private static final int SOCKETCMD_REQUEST_SENSOR_START_FROM_HAL = 8391178;
    private static final int SOCKETCMD_REQUEST_SENSOR_START_FROM_LIB = 8391174;
    private static final int SOCKETCMD_REQUEST_SENSOR_STOP_FROM_LIB = 8391175;
    private static final int SOCKETCMD_REQUEST_SENSOT_STOP_FROM_HAL = 8391179;
    private static final int SOCKETCMD_REQUEST_START_HAL_SENSOR = 8391180;
    private static final int SOCKETCMD_REQUEST_STOP_HAL_SENSOR = 8391181;
    private static final int SOCKETCMD_REQUEST_VERSION = 8391169;
    private static final int SOCKETCMD_REQUEST_VIBRATION = 8391190;
    private static final String logTag = "RDIS_LIB";
    private final int HEADER_SIZE = 12;
    private final int INTERVAL_OF_RETRY = 1000;
    private final int MAX_PACKET_SIZE = AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START;
    private final int MAX_PAYLOAD_SIZE = 1048564;
    private final int NUM_OF_RETRY = 0;
    private final int SYNC_BYTE = 71;
    private final int VERSION_BUILD = 0;
    private final int VERSION_MAJOR = 1;
    private final int VERSION_MINOR = 0;
    private LocalSocket mDaemonSocket = null;
    private InputStream mInputStream = null;
    private DaemonCommnunicatorListener mListener = null;
    private OutputStream mOutputStream = null;
    private volatile boolean mRunning = true;
    private Thread mThread = null;

    class C12571 implements Runnable {
        C12571() {
        }

        public void run() {
            DaemonCommnunicator.this.threadLoop();
        }
    }

    public DaemonCommnunicator(DaemonCommnunicatorListener daemonCommnunicatorListener) {
        Dbg.m1744i(logTag, "RdisDaemonCommnunicator: ");
        if (daemonCommnunicatorListener == null) {
            Dbg.m1742e(logTag, "ERROR! listener is null!");
            return;
        }
        this.mListener = daemonCommnunicatorListener;
        this.mThread = new Thread(new C12571());
        this.mThread.start();
    }

    private boolean connect() {
        boolean z = true;
        synchronized (this) {
            Dbg.m1744i(logTag, "########## connect");
            if (this.mDaemonSocket == null || !this.mDaemonSocket.isConnected()) {
                try {
                    this.mDaemonSocket = new LocalSocket();
                    this.mDaemonSocket.connect(new LocalSocketAddress(DAEMON_SOCKET, Namespace.RESERVED));
                    this.mInputStream = this.mDaemonSocket.getInputStream();
                    this.mOutputStream = this.mDaemonSocket.getOutputStream();
                    reportConnection();
                } catch (Exception e) {
                    Dbg.printStackTrace(e);
                    try {
                        if (this.mDaemonSocket != null) {
                            this.mDaemonSocket.shutdownInput();
                            this.mDaemonSocket.shutdownOutput();
                            this.mDaemonSocket.close();
                            this.mDaemonSocket = null;
                        }
                    } catch (Exception e2) {
                        Dbg.printStackTrace(e2);
                    }
                    Dbg.m1742e(logTag, "DaemonSocket connection failed");
                    z = false;
                }
            }
        }
        return z;
    }

    private boolean connectWithRetry() {
        boolean z = false;
        for (int i = 0; i < 1 && !z; i++) {
            if (i != 0) {
                sleep(1000);
            }
            z = connect();
        }
        return z;
    }

    private Sensor createSensorObject(int i, String str, String str2, int i2, int i3, float f, float f2, float f3) {
        try {
            Constructor[] declaredConstructors = Class.forName("android.hardware.Sensor").getDeclaredConstructors();
            declaredConstructors[0].setAccessible(true);
            Sensor sensor = (Sensor) declaredConstructors[0].newInstance(new Object[0]);
            Field declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mType");
            declaredField.setAccessible(true);
            declaredField.set(sensor, Integer.valueOf(i));
            declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mName");
            declaredField.setAccessible(true);
            declaredField.set(sensor, str);
            declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mVendor");
            declaredField.setAccessible(true);
            declaredField.set(sensor, str2);
            declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mVersion");
            declaredField.setAccessible(true);
            declaredField.set(sensor, Integer.valueOf(i2));
            declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mMaxRange");
            declaredField.setAccessible(true);
            declaredField.set(sensor, Float.valueOf(f));
            declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mResolution");
            declaredField.setAccessible(true);
            declaredField.set(sensor, Float.valueOf(f3));
            declaredField = Class.forName("android.hardware.Sensor").getDeclaredField("mPower");
            declaredField.setAccessible(true);
            declaredField.set(sensor, Float.valueOf(f2));
            Dbg.m1746v("DevInfo", "sensor (" + sensor.getName() + "," + sensor.getVendor() + "," + sensor.getVersion() + "," + sensor.getType() + "," + sensor.getMaximumRange() + "," + sensor.getResolution() + "," + sensor.getPower() + ") added");
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

    private void disconnect() {
        synchronized (this) {
            Dbg.m1744i(logTag, "########## disconnect");
            if (this.mDaemonSocket != null) {
                try {
                    this.mDaemonSocket.shutdownInput();
                    this.mDaemonSocket.shutdownOutput();
                    this.mDaemonSocket.close();
                } catch (Exception e) {
                    Dbg.printStackTrace(e);
                }
                this.mDaemonSocket = null;
            }
        }
    }

    private void parseMobleConnection(byte[] bArr, int i) {
        Exception e;
        try {
            String str = new String(bArr, 0, i, StringUtil.__UTF8Alt);
            Dbg.m1740d(logTag, "[" + new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getClassName() + ":" + new Throwable().getStackTrace()[0].getMethodName() + ":" + new Throwable().getStackTrace()[0].getLineNumber() + "] " + "json=" + str);
            JSONObject jSONObject = new JSONObject(str);
            int i2 = jSONObject.getInt("mobile_id");
            RdisClient rdisClient = new RdisClient(i2);
            try {
                int i3 = jSONObject.getInt("num_of_sensors");
                JSONArray jSONArray = jSONObject.getJSONArray("sensors");
                if (i3 != jSONArray.length()) {
                    Dbg.m1742e(logTag, "NumObSensors is wrong!");
                }
                for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i4);
                    Sensor createSensorObject = createSensorObject(jSONObject2.getInt("sensor_type"), jSONObject2.getString("sensor_name"), jSONObject2.getString("sensor_vendor"), jSONObject2.getInt("sensor_version"), jSONObject2.getInt("sensor_min_delay"), Float.parseFloat(jSONObject2.getString("sensor_max_range")), Float.parseFloat(jSONObject2.getString("sensor_power")), Float.parseFloat(jSONObject2.getString("sensor_resolution")));
                    if (createSensorObject == null) {
                        Dbg.m1742e(logTag, "sensor object refrection error! ");
                    } else {
                        rdisClient.addSensor(createSensorObject);
                    }
                }
                rdisClient.addHid(new HumanInterfaceDeviceInfomation(1, jSONObject.getString("key_device_name")));
                rdisClient.addHid(new HumanInterfaceDeviceInfomation(2, jSONObject.getString("touch_device_name")));
                rdisClient.addHid(new HumanInterfaceDeviceInfomation(3, jSONObject.getString("mouse_device_name")));
                rdisClient.setGpCommunication(jSONObject.getBoolean("enable_gp_com"), jSONObject.getString("protocol_name"));
                Dbg.m1744i(logTag, "Connected!  mobileId=" + i2);
                if (this.mListener != null) {
                    this.mListener.mobileConnection(i2, rdisClient);
                }
            } catch (UnsupportedEncodingException e2) {
                e = e2;
                Dbg.printStackTrace(e);
            } catch (JSONException e3) {
                e = e3;
                Dbg.printStackTrace(e);
            }
        } catch (UnsupportedEncodingException e4) {
            e = e4;
            Dbg.printStackTrace(e);
        } catch (JSONException e5) {
            e = e5;
            Dbg.printStackTrace(e);
        }
    }

    private void parseMobleDisconnection(byte[] bArr, int i) {
        try {
            int i2 = new JSONObject(new String(bArr, 0, i, StringUtil.__UTF8Alt)).getInt("mobile_id");
            Dbg.m1744i(logTag, "disconnected!  mobileId=" + i2);
            if (this.mListener != null) {
                this.mListener.mobileDisconnection(i2);
            }
        } catch (Exception e) {
            Dbg.printStackTrace(e);
        } catch (Exception e2) {
            Dbg.printStackTrace(e2);
        }
    }

    private void parseRecvData(int i, byte[] bArr, int i2) {
        switch (i) {
            case SOCKETCMD_REPORT_VERSION /*8391168*/:
                parseVersion(bArr, i2);
                return;
            case SOCKETCMD_REQUEST_VERSION /*8391169*/:
                reportVersion();
                return;
            case SOCKETCMD_REPORT_MOBILE_CONNECTION /*8391170*/:
                parseMobleConnection(bArr, i2);
                return;
            case SOCKETCMD_REPORT_MOBILE_DISCONNECTION /*8391171*/:
                parseMobleDisconnection(bArr, i2);
                return;
            case SOCKETCMD_REQUEST_APPLICATION_ID /*8391172*/:
                reportApplicationId();
                return;
            case SOCKETCMD_REPORT_SENSOR_EVENT /*8391176*/:
                parseSensorEvent(bArr, i2);
                return;
            case SOCKETCMD_REPORT_REMOCON_CONNECT /*8391186*/:
                parseRemoconConnect(bArr, i2);
                return;
            case SOCKETCMD_REPORT_TOUCH_EVENT /*8391188*/:
                parseTouchEvent(bArr, i2);
                return;
            case SOCKETCMD_REPORT_SENSOR_DEFAULT_ID /*8391189*/:
                parseSensorDefaultId(bArr, i2);
                return;
            default:
                Dbg.m1742e(logTag, "unknown cmd receved.   cmd: 0x" + Integer.toHexString(i));
                return;
        }
    }

    private void parseRemoconConnect(byte[] bArr, int i) {
        Exception e;
        try {
            JSONObject jSONObject = new JSONObject(new String(bArr, 0, i, StringUtil.__UTF8Alt));
            int i2 = jSONObject.getInt("mobile_id");
            RdisRemoteController rdisRemoteController = new RdisRemoteController(i2);
            try {
                int i3 = jSONObject.getInt("num_of_sensors");
                if (i3 > 0) {
                    JSONArray jSONArray = jSONObject.getJSONArray("sensors");
                    if (i3 != jSONArray.length()) {
                        Dbg.m1742e(logTag, "mumObSensors is wrong!");
                    }
                    for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i4);
                        Sensor createSensorObject = createSensorObject(jSONObject2.getInt("sensor_type"), jSONObject2.getString("sensor_name"), jSONObject2.getString("sensor_vendor"), jSONObject2.getInt("sensor_version"), jSONObject2.getInt("sensor_min_delay"), Float.parseFloat(jSONObject2.getString("sensor_max_range")), Float.parseFloat(jSONObject2.getString("sensor_power")), Float.parseFloat(jSONObject2.getString("sensor_resolution")));
                        if (createSensorObject == null) {
                            Dbg.m1742e(logTag, "sensor object refrection error! ");
                        } else {
                            rdisRemoteController.addSensor(createSensorObject);
                        }
                    }
                }
                if (this.mListener != null) {
                    this.mListener.remoconConnection(rdisRemoteController);
                }
                Dbg.m1744i(logTag, "RemoteController connected! mobileId=" + i2);
            } catch (UnsupportedEncodingException e2) {
                e = e2;
                Dbg.printStackTrace(e);
            } catch (JSONException e3) {
                e = e3;
                Dbg.printStackTrace(e);
            }
        } catch (UnsupportedEncodingException e4) {
            e = e4;
            Dbg.printStackTrace(e);
        } catch (JSONException e5) {
            e = e5;
            Dbg.printStackTrace(e);
        }
    }

    private void parseSensorDefaultId(byte[] bArr, int i) {
        try {
            int i2 = new JSONObject(new String(bArr, 0, i, StringUtil.__UTF8Alt)).getInt("mobile_id");
            if (this.mListener != null) {
                this.mListener.defaultSensorChange(i2);
            }
            Dbg.m1744i(logTag, "default sensor was changed!: mobileId:" + i2);
        } catch (Exception e) {
            Dbg.printStackTrace(e);
        } catch (Exception e2) {
            Dbg.printStackTrace(e2);
        }
    }

    private void parseSensorEvent(byte[] bArr, int i) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        wrap.order(ByteOrder.BIG_ENDIAN);
        int i2 = wrap.getInt();
        int i3 = wrap.getInt();
        float f = wrap.getFloat();
        float f2 = wrap.getFloat();
        float f3 = wrap.getFloat();
        int i4 = wrap.getInt();
        wrap.getInt();
        if (this.mListener != null) {
            this.mListener.sensorEvent(i2, i3, new float[]{f, f2, f3}, i4);
        }
    }

    private void parseTouchEvent(byte[] bArr, int i) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        wrap.order(ByteOrder.BIG_ENDIAN);
        int i2 = wrap.getInt();
        int i3 = wrap.getInt();
        int[] iArr = new int[i3];
        int[] iArr2 = new int[i3];
        PointerCoords[] pointerCoordsArr = new PointerCoords[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            iArr[i4] = i4;
            iArr2[i4] = wrap.getInt();
            pointerCoordsArr[i4] = new PointerCoords();
            pointerCoordsArr[i4].x = wrap.getFloat();
            pointerCoordsArr[i4].y = wrap.getFloat();
            pointerCoordsArr[i4].pressure = wrap.getFloat();
            pointerCoordsArr[i4].size = wrap.getFloat();
        }
        long currentTimeMillis = System.currentTimeMillis();
        MotionEvent obtain = MotionEvent.obtain(currentTimeMillis, currentTimeMillis, iArr2[0], i3, iArr, pointerCoordsArr, 0, 0.0f, 0.0f, 0, 0, 0, 0);
        if (this.mListener != null) {
            this.mListener.touchEvent(i2, obtain);
        }
    }

    private void parseVersion(byte[] bArr, int i) {
        try {
            JSONObject jSONObject = new JSONObject(new String(bArr, 0, i, StringUtil.__UTF8Alt));
            int i2 = jSONObject.getInt("major");
            int i3 = jSONObject.getInt("minor");
            Dbg.m1744i(logTag, "Daemon's VERSION = " + i2 + "." + i3 + "." + jSONObject.getInt("build"));
        } catch (Exception e) {
            Dbg.printStackTrace(e);
        } catch (Exception e2) {
            Dbg.printStackTrace(e2);
        }
    }

    private void reportApplicationId() {
        int myPid = Process.myPid();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("application_id", myPid);
            write(SOCKETCMD_REPORT_APPLICATION_ID, jSONObject.toString());
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
        }
    }

    private void reportConnection() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("kind_of_client", "LIB");
            write(SOCKETCMD_REPORT_CONNECTION, jSONObject.toString());
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
        }
    }

    private void reportVersion() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("major", 1);
            jSONObject.put("minor", 0);
            jSONObject.put("build", 0);
            write(SOCKETCMD_REPORT_VERSION, jSONObject.toString());
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
        }
    }

    private void requestSensorStop(int i, int i2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("mobile_id", i);
            jSONObject.put("sensor_type", i2);
            jSONObject.put("index", 0);
            write(SOCKETCMD_REQUEST_SENSOR_STOP_FROM_LIB, jSONObject.toString());
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
        }
    }

    private boolean requestSensorStrat(int i, int i2, int i3) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("mobile_id", i);
            jSONObject.put("sensor_type", i2);
            jSONObject.put("sensor_rate", i3);
            jSONObject.put("index", 0);
            return write(SOCKETCMD_REQUEST_SENSOR_START_FROM_LIB, jSONObject.toString());
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
            return false;
        }
    }

    private void requestVibration(int i, int i2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("mobile_id", i);
            jSONObject.put("mode", "ONESHOT");
            jSONObject.put("milliseconds", i2);
            jSONObject.put("pattern_len", 0);
            jSONObject.put("repeat", -1);
            Dbg.m1744i(logTag, "requestVibration()!");
            write(SOCKETCMD_REQUEST_VIBRATION, jSONObject.toString());
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
        }
    }

    private void sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (Throwable e) {
            Dbg.m1743e(logTag, "Interrupted while doing initial sleeping.", e);
        }
    }

    private void threadLoop() {
        if (connectWithRetry()) {
            byte[] bArr = new byte[12];
            while (this.mRunning) {
                int read;
                int i = 0;
                while (i < 12) {
                    try {
                        read = this.mInputStream.read(bArr, i, 12 - i);
                        if (read < 0) {
                            throw new IOException();
                        }
                        i += read;
                    } catch (Exception e) {
                        Dbg.printStackTrace(e);
                        disconnect();
                        return;
                    }
                }
                ByteBuffer wrap = ByteBuffer.wrap(bArr);
                wrap.order(ByteOrder.BIG_ENDIAN);
                if (wrap.get() != (byte) 71) {
                    throw new IOException();
                }
                wrap.position(4);
                read = wrap.getInt();
                int i2 = wrap.getInt();
                byte[] bArr2 = new byte[i2];
                if (i2 > 0) {
                    i = 0;
                    while (i < i2) {
                        int read2 = this.mInputStream.read(bArr2, i, i2 - i);
                        if (read2 < 0) {
                            throw new IOException();
                        }
                        i += read2;
                    }
                }
                parseRecvData(read, bArr2, i2);
            }
            Dbg.m1744i(logTag, "EXIT from THREAD LOOP.");
            disconnect();
            return;
        }
        if (this.mListener != null) {
            this.mListener.daemonConnectionFail();
        }
        Dbg.m1742e(logTag, "connection error!");
    }

    private boolean write(int i, String str) {
        if (this.mDaemonSocket == null) {
            Dbg.m1742e(logTag, "socket error.");
            return false;
        }
        int i2 = 0;
        byte[] bArr = null;
        if (str != null) {
            try {
                bArr = str.getBytes("UTF-8");
                i2 = bArr.length;
                if (i2 > 1048564) {
                    Dbg.m1742e(logTag, String.format("payload is too big! %d, cmd:0x%08x", new Object[]{Integer.valueOf(i2), Integer.valueOf(i)}));
                    return false;
                }
            } catch (Exception e) {
                Dbg.printStackTrace(e);
                return false;
            }
        }
        byte b = (byte) ((-16777216 & i) >> 24);
        byte b2 = (byte) ((16711680 & i) >> 16);
        byte b3 = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >> 8);
        byte b4 = (byte) (i & 255);
        byte b5 = (byte) ((-16777216 & i2) >> 24);
        byte b6 = (byte) ((16711680 & i2) >> 16);
        byte b7 = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i2) >> 8);
        byte b8 = (byte) (i2 & 255);
        try {
            this.mOutputStream.write(new byte[]{(byte) 71, (byte) 0, (byte) 0, (byte) 0, b, b2, b3, b4, b5, b6, b7, b8});
            if (!(i2 == 0 || bArr == null)) {
                this.mOutputStream.write(bArr);
            }
            return true;
        } catch (Exception e2) {
            Dbg.printStackTrace(e2);
            return false;
        }
    }

    public boolean startSensor(int i, int i2, int i3) {
        Dbg.m1744i(logTag, "startSensor" + i + " - " + i2 + " - " + i3);
        return requestSensorStrat(i, i2, i3);
    }

    public void stopSensor(int i, int i2) {
        Dbg.m1744i(logTag, "stopSensor" + i + " - " + i2);
        requestSensorStop(i, i2);
    }

    public void stopThread() {
        Dbg.m1744i(logTag, "stopThread: ");
        disconnect();
        this.mRunning = false;
    }

    public void vibrate(int i, long j) {
        requestVibration(i, (int) j);
    }
}
