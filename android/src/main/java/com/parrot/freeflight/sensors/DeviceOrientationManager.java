package com.parrot.freeflight.sensors;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.util.FloatMath;
import android.util.Log;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceOrientationManager implements Runnable {
    private static final float EPSILON = 1.0E-9f;
    private static final float HPF_COEFFICIENT = 0.8f;
    private static final float NS2S = 1.0E-9f;
    private static final String TAG = DeviceOrientationManager.class.getSimpleName();
    private static float[] deltaMatrix = new float[9];
    private static float[] deltaVector = new float[4];
    private static float[] normValues = new float[3];
    static float[] resultMatrix = new float[9];
    static float[] resultMatrix2 = new float[9];
    private static float[] tempGyroMatrix;
    private static boolean warningShown = false;
    static float[] xM = new float[9];
    static float[] yM = new float[9];
    static float[] zM = new float[9];
    private float[] accMagOrientation;
    private float[] accMagRotationMatrix;
    private boolean acceleroAvailable;
    private SensorEventListener acceleroEventListener;
    private float[] acceleroValues;
    private DeviceOrientationChangeDelegate delegate;
    private Timer fuseTimer;
    private float[] fusedOrientation;
    private boolean gyroAvailable;
    private SensorEventListener gyroEventListener;
    private float[] gyroOrientation;
    private float[] gyroRotationMatrix;
    private float[] gyroValues;
    private boolean initState = true;
    private float magneticHeading;
    private int magnetoAccuracy;
    private boolean magnetoAvailable;
    private SensorEventListener magnetoEventListener;
    private float[] magnetoValues;
    private Handler sensorHandler;
    private SensorManagerWrapper sensorManager;
    private float timestamp;
    private Thread workerThread;

    class C11731 implements SensorEventListener {
        C11731() {
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == 1) {
                DeviceOrientationManager.this.onAcceleroChanged(sensorEvent);
            }
        }
    }

    class C11742 implements SensorEventListener {
        C11742() {
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == 2) {
                DeviceOrientationManager.this.onMagnetoChanged(sensorEvent);
            }
        }
    }

    class C11753 implements SensorEventListener {
        C11753() {
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == 4) {
                DeviceOrientationManager.this.onGyroChanged(sensorEvent);
            }
        }
    }

    class CalculateFusedOrientationTask extends TimerTask {
        CalculateFusedOrientationTask() {
        }

        public void run() {
            if (DeviceOrientationManager.this.fusedOrientation == null) {
                DeviceOrientationManager.this.fusedOrientation = new float[3];
            }
            if (DeviceOrientationManager.this.gyroOrientation != null && DeviceOrientationManager.this.accMagOrientation != null) {
                DeviceOrientationManager.this.fusedOrientation[0] = (DeviceOrientationManager.this.gyroOrientation[0] * DeviceOrientationManager.HPF_COEFFICIENT) + (DeviceOrientationManager.this.accMagOrientation[0] * 0.19999999f);
                DeviceOrientationManager.this.fusedOrientation[1] = (DeviceOrientationManager.this.gyroOrientation[1] * DeviceOrientationManager.HPF_COEFFICIENT) + (DeviceOrientationManager.this.accMagOrientation[1] * 0.19999999f);
                DeviceOrientationManager.this.fusedOrientation[2] = (DeviceOrientationManager.this.gyroOrientation[2] * DeviceOrientationManager.HPF_COEFFICIENT) + (DeviceOrientationManager.this.accMagOrientation[2] * 0.19999999f);
                DeviceOrientationManager.this.gyroRotationMatrix = DeviceOrientationManager.this.getRotationMatrixFromOrientation(DeviceOrientationManager.this.fusedOrientation);
                System.arraycopy(DeviceOrientationManager.this.fusedOrientation, 0, DeviceOrientationManager.this.gyroOrientation, 0, 3);
                if (DeviceOrientationManager.this.delegate != null) {
                    DeviceOrientationManager.this.delegate.onDeviceOrientationChanged(DeviceOrientationManager.this.fusedOrientation, DeviceOrientationManager.this.magneticHeading, DeviceOrientationManager.this.magnetoAccuracy);
                }
            }
        }
    }

    DeviceOrientationManager(SensorManagerWrapper sensorManagerWrapper, DeviceOrientationChangeDelegate deviceOrientationChangeDelegate) {
        this.delegate = deviceOrientationChangeDelegate;
        this.accMagRotationMatrix = new float[16];
        this.gyroOrientation = new float[3];
        this.gyroOrientation[0] = 0.0f;
        this.gyroOrientation[1] = 0.0f;
        this.gyroOrientation[2] = 0.0f;
        this.gyroRotationMatrix = new float[9];
        this.gyroRotationMatrix[0] = 1.0f;
        this.gyroRotationMatrix[1] = 0.0f;
        this.gyroRotationMatrix[2] = 0.0f;
        this.gyroRotationMatrix[3] = 0.0f;
        this.gyroRotationMatrix[4] = 1.0f;
        this.gyroRotationMatrix[5] = 0.0f;
        this.gyroRotationMatrix[6] = 0.0f;
        this.gyroRotationMatrix[7] = 0.0f;
        this.gyroRotationMatrix[8] = 1.0f;
        this.sensorManager = sensorManagerWrapper;
        checkSensors(this.sensorManager);
    }

    private void checkSensors(SensorManagerWrapper sensorManagerWrapper) {
        this.acceleroAvailable = sensorManagerWrapper.isAcceleroAvailable();
        this.gyroAvailable = sensorManagerWrapper.isGyroAvailable();
        this.magnetoAvailable = sensorManagerWrapper.isMagnetoAvailable();
    }

    private float[] getRotationMatrixFromOrientation(float[] fArr) {
        float sin = FloatMath.sin(fArr[1]);
        float cos = FloatMath.cos(fArr[1]);
        float sin2 = FloatMath.sin(fArr[2]);
        float cos2 = FloatMath.cos(fArr[2]);
        float sin3 = FloatMath.sin(fArr[0]);
        float cos3 = FloatMath.cos(fArr[0]);
        xM[0] = 1.0f;
        xM[1] = 0.0f;
        xM[2] = 0.0f;
        xM[3] = 0.0f;
        xM[4] = cos;
        xM[5] = sin;
        xM[6] = 0.0f;
        xM[7] = -sin;
        xM[8] = cos;
        yM[0] = cos2;
        yM[1] = 0.0f;
        yM[2] = sin2;
        yM[3] = 0.0f;
        yM[4] = 1.0f;
        yM[5] = 0.0f;
        yM[6] = -sin2;
        yM[7] = 0.0f;
        yM[8] = cos2;
        zM[0] = cos3;
        zM[1] = sin3;
        zM[2] = 0.0f;
        zM[3] = -sin3;
        zM[4] = cos3;
        zM[5] = 0.0f;
        zM[6] = 0.0f;
        zM[7] = 0.0f;
        zM[8] = 1.0f;
        matrixMultiplication(resultMatrix2, xM, yM);
        matrixMultiplication(resultMatrix, zM, resultMatrix2);
        return resultMatrix;
    }

    private void getRotationVectorFromGyro(float[] fArr, float[] fArr2, float f) {
        float sqrt = FloatMath.sqrt(((fArr[0] * fArr[0]) + (fArr[1] * fArr[1])) + (fArr[2] * fArr[2]));
        if (sqrt > 1.0E-9f) {
            normValues[0] = fArr[0] / sqrt;
            normValues[1] = fArr[1] / sqrt;
            normValues[2] = fArr[2] / sqrt;
        }
        sqrt *= f;
        float sin = FloatMath.sin(sqrt);
        sqrt = FloatMath.cos(sqrt);
        fArr2[0] = normValues[0] * sin;
        fArr2[1] = normValues[1] * sin;
        fArr2[2] = sin * normValues[2];
        fArr2[3] = sqrt;
    }

    private void initSensorListeners() {
        this.acceleroEventListener = new C11731();
        this.magnetoEventListener = new C11742();
        this.gyroEventListener = new C11753();
    }

    private float[] matrixMultiplication(float[] fArr, float[] fArr2, float[] fArr3) {
        fArr[0] = ((fArr2[0] * fArr3[0]) + (fArr2[1] * fArr3[3])) + (fArr2[2] * fArr3[6]);
        fArr[1] = ((fArr2[0] * fArr3[1]) + (fArr2[1] * fArr3[4])) + (fArr2[2] * fArr3[7]);
        fArr[2] = ((fArr2[0] * fArr3[2]) + (fArr2[1] * fArr3[5])) + (fArr2[2] * fArr3[8]);
        fArr[3] = ((fArr2[3] * fArr3[0]) + (fArr2[4] * fArr3[3])) + (fArr2[5] * fArr3[6]);
        fArr[4] = ((fArr2[3] * fArr3[1]) + (fArr2[4] * fArr3[4])) + (fArr2[5] * fArr3[7]);
        fArr[5] = ((fArr2[3] * fArr3[2]) + (fArr2[4] * fArr3[5])) + (fArr2[5] * fArr3[8]);
        fArr[6] = ((fArr2[6] * fArr3[0]) + (fArr2[7] * fArr3[3])) + (fArr2[8] * fArr3[6]);
        fArr[7] = ((fArr2[6] * fArr3[1]) + (fArr2[7] * fArr3[4])) + (fArr2[8] * fArr3[7]);
        fArr[8] = ((fArr2[6] * fArr3[2]) + (fArr2[7] * fArr3[5])) + (fArr2[8] * fArr3[8]);
        return fArr;
    }

    private void registerSensorListeners() {
        initSensorListeners();
        if (this.sensorManager.isAcceleroAvailable() && this.sensorManager.registerListener(this.acceleroEventListener, 1, this.sensorHandler)) {
            Log.d(TAG, "Accelerometer [OK]");
        }
        if (this.sensorManager.isMagnetoAvailable() && this.sensorManager.registerListener(this.magnetoEventListener, 2, this.sensorHandler)) {
            Log.d(TAG, "Magnetometer [OK]");
        }
        if (this.sensorManager.isGyroAvailable() && this.sensorManager.registerListener(this.gyroEventListener, 4, this.sensorHandler)) {
            this.initState = true;
            this.fuseTimer = new Timer();
            this.fuseTimer.scheduleAtFixedRate(new CalculateFusedOrientationTask(), 1000, 30);
            Log.d(TAG, "Gyroscope [OK]");
        }
    }

    private void unregisterSensorListeners() {
        this.sensorManager.unregisterListener(this.acceleroEventListener);
        this.sensorManager.unregisterListener(this.magnetoEventListener);
        this.sensorManager.unregisterListener(this.gyroEventListener);
        if (this.fuseTimer != null) {
            this.fuseTimer.cancel();
        }
    }

    public void computeAccMagOrientation() {
        if (this.acceleroValues != null && this.magnetoValues != null && SensorManager.getRotationMatrix(this.accMagRotationMatrix, null, this.acceleroValues, this.magnetoValues)) {
            if (this.accMagOrientation == null) {
                this.accMagOrientation = new float[3];
            }
            SensorManager.getOrientation(this.accMagRotationMatrix, this.accMagOrientation);
            this.magneticHeading = this.accMagOrientation[0];
            if (this.accMagOrientation[0] < 0.0f) {
                this.magneticHeading = (float) (((double) this.magneticHeading) + 6.283185307179586d);
            }
        } else if (this.acceleroValues != null) {
            if (this.accMagOrientation == null) {
                this.accMagOrientation = new float[3];
            }
            this.accMagOrientation[2] = ((float) Math.atan2((double) this.acceleroValues[0], (double) FloatMath.sqrt((this.acceleroValues[1] * this.acceleroValues[1]) + (this.acceleroValues[2] * this.acceleroValues[2])))) * GroundOverlayOptions.NO_DIMENSION;
            this.accMagOrientation[1] = ((float) Math.atan2((double) this.acceleroValues[1], (double) FloatMath.sqrt((this.acceleroValues[0] * this.acceleroValues[0]) + (this.acceleroValues[2] * this.acceleroValues[2])))) * GroundOverlayOptions.NO_DIMENSION;
        }
    }

    public void destroy() {
        this.sensorManager.onDestroy();
    }

    public boolean isAcceleroAvailable() {
        return this.acceleroAvailable;
    }

    public boolean isGyroAvailable() {
        return this.gyroAvailable;
    }

    public boolean isMagnetoAvailable() {
        return this.magnetoAvailable;
    }

    public boolean isRunning() {
        return this.workerThread != null && this.workerThread.isAlive();
    }

    protected void onAcceleroChanged(SensorEvent sensorEvent) {
        if (this.acceleroValues == null) {
            this.acceleroValues = new float[3];
        }
        System.arraycopy(sensorEvent.values, 0, this.acceleroValues, 0, 3);
        computeAccMagOrientation();
        if (!this.gyroAvailable && this.delegate != null && this.accMagOrientation != null) {
            this.delegate.onDeviceOrientationChanged(this.accMagOrientation, this.magneticHeading, this.magnetoAccuracy);
        }
    }

    public void onCreate() {
        this.sensorManager.onCreate();
    }

    @TargetApi(9)
    protected void onGyroChanged(SensorEvent sensorEvent) {
        if (this.acceleroValues != null && this.magnetoValues != null) {
            if (this.initState) {
                this.gyroRotationMatrix = matrixMultiplication(new float[9], this.gyroRotationMatrix, getRotationMatrixFromOrientation(this.magnetoValues));
                this.initState = false;
            }
            if (this.timestamp != 0.0f) {
                if (this.gyroValues == null) {
                    this.gyroValues = new float[3];
                }
                float f = (float) sensorEvent.timestamp;
                float f2 = this.timestamp;
                System.arraycopy(sensorEvent.values, 0, this.gyroValues, 0, 3);
                getRotationVectorFromGyro(this.gyroValues, deltaVector, ((f - f2) * 1.0E-9f) / 2.0f);
            }
            this.timestamp = (float) sensorEvent.timestamp;
            SensorManager.getRotationMatrixFromVector(deltaMatrix, deltaVector);
            if (tempGyroMatrix == null) {
                tempGyroMatrix = new float[9];
            }
            this.gyroRotationMatrix = matrixMultiplication(tempGyroMatrix, this.gyroRotationMatrix, deltaMatrix);
            SensorManager.getOrientation(this.gyroRotationMatrix, this.gyroOrientation);
        }
    }

    protected void onMagnetoChanged(SensorEvent sensorEvent) {
        if (this.magnetoValues == null) {
            this.magnetoValues = new float[3];
        }
        System.arraycopy(sensorEvent.values, 0, this.magnetoValues, 0, 3);
        this.magnetoAccuracy = sensorEvent.accuracy;
        if (!warningShown && this.magnetoAccuracy == 0) {
            warningShown = true;
            Log.w(TAG, "Magnetometer sensor data is unreliable. Calibration is required!");
        }
    }

    public void pause() {
        this.sensorManager.onPause();
        if (this.workerThread != null) {
            try {
                this.sensorHandler.getLooper().quit();
                this.workerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            } finally {
                this.workerThread = null;
            }
        }
        Log.d(TAG, "Device Orientation Manager has been paused");
    }

    public void resume() {
        resume(true, true);
    }

    public void resume(boolean z, boolean z2) {
        this.sensorManager.onResume();
        if (this.magnetoAvailable) {
            this.magnetoAvailable = z;
        }
        if (this.gyroAvailable) {
            this.gyroAvailable = z2;
        }
        if (this.workerThread != null) {
            throw new RuntimeException("Sensor thread already started");
        }
        this.workerThread = new Thread(this, "Sensor Data Processing Thread");
        this.workerThread.start();
        Log.d(TAG, "Device Orientation Manager has been resumed");
    }

    public void run() {
        Looper.prepare();
        this.sensorHandler = new Handler();
        registerSensorListeners();
        Looper.loop();
        unregisterSensorListeners();
        this.sensorHandler = null;
    }
}
