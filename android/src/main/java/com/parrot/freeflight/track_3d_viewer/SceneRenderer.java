package com.parrot.freeflight.track_3d_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.Build.VERSION;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.FlightDataItem;
import com.parrot.freeflight.track_3d_viewer.ios_stuff.IOsUtils;
import com.parrot.freeflight.track_3d_viewer.ios_stuff.OpenGLInterleavedVertex;
import com.parrot.freeflight.track_3d_viewer.utils.ShaderHelper;
import com.parrot.freeflight.track_3d_viewer.utils.TextureHelper;
import com.parrot.freeflight.track_3d_viewer.utils.Utils;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SceneRenderer implements Renderer, OnTouchListener {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 2;
    private static final int COLOR_DATA_SIZE = 4;
    private static final float DRONE_ORIENTATION_FROM_NORTH = 180.0f;
    private static final float DRONE_ORIENTATION_THETA = -90.0f;
    private static final int FLOOR_MAX_INDEXES = 4;
    private static final float GAME_FAR = 100.0f;
    private static final float GAME_NEAR = 0.001f;
    private static final float NEAR_THRESHOLD = 0.1f;
    private static final int NORMAL_DATA_SIZE = 3;
    private static final float NORMAL_START_GRADIENT_COEF = 0.7f;
    private static final float PINCH_MULTIPLIER = 2.0f;
    private static final int POSITION_DATA_SIZE = 3;
    private static final float SCENE_SCALE = 2.0f;
    private static final float SKYDOME_RADIUS = 1.95f;
    private static final int STRIDE = 48;
    private static final String TAG = "SceneRenderer";
    private static final int TEXTURE_COORDINATE_DATA_SIZE = 2;
    private final float[] MVPMatrix;
    private int MVPMatrixHandle;
    boolean animationStartup;
    private int colorHandle;
    private final Context context;
    float currentPhi;
    float currentPsi;
    float currentTheta;
    private final PointF currentTouchPosition;
    int droneIndexBufferId;
    private int droneIndexesCount;
    private int droneTextureHandle;
    int droneVertexBufferId;
    private final List<FlightDataItem> flightDetails;
    int floorIndexBufferId;
    float floorRotationX;
    float floorRotationY;
    private int floorTextureHandle;
    float floorTranslationX;
    float floorTranslationZ;
    int floorVertexBufferId;
    private final GestureDetector gestureDetector;
    private final PointF lastPinchAveragePosition;
    float lastPinchDistance;
    private final PointF lastTouchPosition;
    private int lightsPosHandle;
    private final Bitmap mapImage;
    private final MediaPlayerController mediaPlayerController;
    private final float[] modelViewMatrix;
    private int normalHandle;
    private final PointF p1;
    private final PointF p2;
    private int positionHandle;
    private int programHandle;
    private final float[] projectionMatrix;
    private int roadIndexBufferId;
    private final short[] roadIndexes;
    private int roadVertexBufferId;
    private final OpenGLInterleavedVertex[] roadVertexes;
    boolean singleToutch;
    int sphereIndexBufferId;
    private int sphereIndexesCount;
    private int sphereTextureHandle;
    int sphereVertexBufferId;
    private int startGradientPosHandle;
    private int textcoordsHandle;
    private int textureUniformHandle;
    private int useLightHandle;
    private int useTextureHandle;
    private final boolean useWorkaroundsForSDK8;
    private int viewHeight;
    private int viewWidth;

    class C11881 extends SimpleOnGestureListener {
        C11881() {
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            SceneRenderer.this.touchesEnded(motionEvent);
            return true;
        }
    }

    public SceneRenderer(Context context, MediaPlayerController mediaPlayerController, Bitmap bitmap, List<FlightDataItem> list, OpenGLInterleavedVertex[] openGLInterleavedVertexArr, short[] sArr) {
        this.useWorkaroundsForSDK8 = VERSION.SDK_INT < 9;
        this.modelViewMatrix = new float[16];
        this.projectionMatrix = new float[16];
        this.MVPMatrix = new float[16];
        this.sphereIndexesCount = 0;
        this.floorTranslationZ = GroundOverlayOptions.NO_DIMENSION;
        this.floorTranslationX = 0.0f;
        this.floorRotationX = 90.0f;
        this.floorRotationY = 0.0f;
        this.lastTouchPosition = new PointF();
        this.lastPinchAveragePosition = new PointF();
        this.currentTouchPosition = new PointF();
        this.p1 = new PointF();
        this.p2 = new PointF();
        this.droneIndexesCount = 0;
        this.context = context;
        this.mediaPlayerController = mediaPlayerController;
        this.mapImage = bitmap;
        this.flightDetails = list;
        this.roadVertexes = openGLInterleavedVertexArr;
        this.roadIndexes = sArr;
        this.gestureDetector = new GestureDetector(context, new C11881());
    }

    private void applyMatrix(float[] fArr) {
        System.arraycopy(fArr, 0, this.MVPMatrix, 0, 16);
        Matrix.multiplyMM(this.MVPMatrix, 0, this.projectionMatrix, 0, this.MVPMatrix, 0);
        GLES20.glUniformMatrix4fv(this.MVPMatrixHandle, 1, false, this.MVPMatrix, 0);
    }

    private void drawDrone() {
        GLES20.glBindBuffer(34963, this.droneIndexBufferId);
        GLES20.glBindBuffer(34962, this.droneVertexBufferId);
        GLES20.glEnableVertexAttribArray(this.positionHandle);
        glVertexAttribPointerFix(this.positionHandle, 3, 5126, false, 48, 0);
        GLES20.glEnableVertexAttribArray(this.colorHandle);
        glVertexAttribPointerFix(this.colorHandle, 4, 5126, false, 48, 12);
        GLES20.glEnableVertexAttribArray(this.normalHandle);
        glVertexAttribPointerFix(this.normalHandle, 3, 5126, false, 48, 28);
        GLES20.glEnableVertexAttribArray(this.textcoordsHandle);
        glVertexAttribPointerFix(this.textcoordsHandle, 2, 5126, false, 48, 40);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.droneTextureHandle);
        GLES20.glUniform1i(this.textureUniformHandle, 0);
        glDrawElementsFix(4, this.droneIndexesCount, 5123, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glBindBuffer(34963, 0);
    }

    private void drawFloor() {
        GLES20.glBindBuffer(34963, this.floorIndexBufferId);
        GLES20.glBindBuffer(34962, this.floorVertexBufferId);
        GLES20.glEnableVertexAttribArray(this.positionHandle);
        glVertexAttribPointerFix(this.positionHandle, 3, 5126, false, 48, 0);
        GLES20.glEnableVertexAttribArray(this.textcoordsHandle);
        glVertexAttribPointerFix(this.textcoordsHandle, 2, 5126, false, 48, 12);
        GLES20.glEnableVertexAttribArray(this.colorHandle);
        glVertexAttribPointerFix(this.colorHandle, 4, 5126, false, 48, 20);
        GLES20.glEnableVertexAttribArray(this.normalHandle);
        glVertexAttribPointerFix(this.normalHandle, 3, 5126, false, 48, 36);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.floorTextureHandle);
        GLES20.glUniform1i(this.textureUniformHandle, 0);
        glDrawElementsFix(5, 4, 5123, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glBindBuffer(34963, 0);
    }

    private void drawRoads() {
        GLES20.glBindBuffer(34963, this.roadIndexBufferId);
        GLES20.glBindBuffer(34962, this.roadVertexBufferId);
        GLES20.glEnableVertexAttribArray(this.positionHandle);
        glVertexAttribPointerFix(this.positionHandle, 3, 5126, false, 48, 0);
        GLES20.glEnableVertexAttribArray(this.colorHandle);
        glVertexAttribPointerFix(this.colorHandle, 4, 5126, false, 48, 12);
        GLES20.glEnableVertexAttribArray(this.normalHandle);
        glVertexAttribPointerFix(this.normalHandle, 3, 5126, false, 48, 28);
        GLES20.glLineWidth(5.0f);
        glDrawElementsFix(3, this.roadIndexes.length, 5123, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glBindBuffer(34963, 0);
    }

    private void drawSkydome() {
        GLES20.glBindBuffer(34963, this.sphereIndexBufferId);
        GLES20.glBindBuffer(34962, this.sphereVertexBufferId);
        GLES20.glEnableVertexAttribArray(this.positionHandle);
        glVertexAttribPointerFix(this.positionHandle, 3, 5126, false, 48, 0);
        GLES20.glEnableVertexAttribArray(this.colorHandle);
        glVertexAttribPointerFix(this.colorHandle, 4, 5126, false, 48, 12);
        GLES20.glEnableVertexAttribArray(this.normalHandle);
        glVertexAttribPointerFix(this.normalHandle, 3, 5126, false, 48, 28);
        GLES20.glEnableVertexAttribArray(this.textcoordsHandle);
        glVertexAttribPointerFix(this.textcoordsHandle, 2, 5126, false, 48, 40);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.sphereTextureHandle);
        GLES20.glUniform1i(this.textureUniformHandle, 0);
        glDrawElementsFix(4, this.sphereIndexesCount, 5123, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glBindBuffer(34963, 0);
    }

    private float getStartGradientPos() {
        float f = this.floorRotationX < 90.0f ? this.floorRotationX : 180.0f - this.floorRotationX;
        return f < 0.0f ? 0.0f : f <= 1.0f ? (0.3f * ((1.0f - f) / 1.0f)) + NORMAL_START_GRADIENT_COEF : NORMAL_START_GRADIENT_COEF;
    }

    private void glDrawElementsFix(int i, int i2, int i3, int i4) {
        if (this.useWorkaroundsForSDK8) {
            fix.android.opengl.GLES20.glDrawElements(i, i2, i3, i4);
        } else {
            GLES20.glDrawElements(i, i2, i3, i4);
        }
    }

    private void glVertexAttribPointerFix(int i, int i2, int i3, boolean z, int i4, int i5) {
        if (this.useWorkaroundsForSDK8) {
            fix.android.opengl.GLES20.glVertexAttribPointer(i, i2, i3, z, i4, i5);
        } else {
            GLES20.glVertexAttribPointer(i, i2, i3, z, i4, i5);
        }
    }

    private void initMatrix() {
        Matrix.setIdentityM(this.modelViewMatrix, 0);
        Matrix.translateM(this.modelViewMatrix, 0, 0.0f, 0.0f, this.floorTranslationZ);
        Matrix.rotateM(this.modelViewMatrix, 0, this.floorRotationX, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(this.modelViewMatrix, 0, this.floorRotationY, 0.0f, 1.0f, 0.0f);
        int position = (int) (this.mediaPlayerController.getPosition() * ((float) (this.roadIndexes.length - 1)));
        Matrix.translateM(this.modelViewMatrix, 0, this.roadVertexes[position].position[0] * -2.0f, this.roadVertexes[position].position[1] * -2.0f, this.roadVertexes[position].position[2] * -2.0f);
        Matrix.scaleM(this.modelViewMatrix, 0, 2.0f, 2.0f, 2.0f);
        float sqrt = (float) Math.sqrt((double) (((this.modelViewMatrix[12] * this.modelViewMatrix[12]) + (this.modelViewMatrix[13] * this.modelViewMatrix[13])) + (this.modelViewMatrix[14] * this.modelViewMatrix[14])));
        if (sqrt > SKYDOME_RADIUS) {
            this.modelViewMatrix[14] = (sqrt - SKYDOME_RADIUS) + this.modelViewMatrix[14];
        }
    }

    private void rotateCubeAroundX(float f, float f2) {
        this.floorRotationX -= f * 0.1f;
        this.floorRotationY -= f2 * 0.1f;
        if (Math.abs(this.floorRotationX) > 360.0f) {
            this.floorRotationX -= (this.floorRotationX / Math.abs(this.floorRotationX)) * 360.0f;
        }
        if (Math.abs(this.floorRotationY) > 360.0f) {
            this.floorRotationY -= (this.floorRotationY / Math.abs(this.floorRotationY)) * 360.0f;
        }
    }

    private void setupDrone() throws IOException {
        float[] loadFloatArray = Utils.loadFloatArray("shaders/drone_vertexes.bin", this.context);
        short[] loadShortArray = Utils.loadShortArray("shaders/drone_indexes.bin", this.context);
        this.droneIndexesCount = loadShortArray.length;
        Buffer asFloatBuffer = ByteBuffer.allocateDirect(loadFloatArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        asFloatBuffer.put(loadFloatArray).position(0);
        Buffer asShortBuffer = ByteBuffer.allocateDirect(loadShortArray.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        asShortBuffer.put(loadShortArray).position(0);
        int[] iArr = new int[2];
        GLES20.glGenBuffers(2, iArr, 0);
        this.droneVertexBufferId = iArr[0];
        this.droneIndexBufferId = iArr[1];
        GLES20.glBindBuffer(34962, this.droneVertexBufferId);
        GLES20.glBufferData(34962, asFloatBuffer.capacity() * 4, asFloatBuffer, 35044);
        GLES20.glBindBuffer(34963, this.droneIndexBufferId);
        GLES20.glBufferData(34963, asShortBuffer.capacity() * 2, asShortBuffer, 35044);
        GLES20.glBindBuffer(34962, 0);
    }

    private void setupFloor() {
        float[] fArr = new float[]{GroundOverlayOptions.NO_DIMENSION, 0.0f, GroundOverlayOptions.NO_DIMENSION, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, GroundOverlayOptions.NO_DIMENSION, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, GroundOverlayOptions.NO_DIMENSION, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f};
        Buffer asFloatBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        asFloatBuffer.put(fArr).position(0);
        short[] sArr = new short[]{(short) 0, (short) 1, (short) 2, (short) 3};
        Buffer asShortBuffer = ByteBuffer.allocateDirect(sArr.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        asShortBuffer.put(sArr).position(0);
        int[] iArr = new int[2];
        GLES20.glGenBuffers(2, iArr, 0);
        this.floorVertexBufferId = iArr[0];
        this.floorIndexBufferId = iArr[1];
        GLES20.glBindBuffer(34962, this.floorVertexBufferId);
        GLES20.glBufferData(34962, asFloatBuffer.capacity() * 4, asFloatBuffer, 35044);
        GLES20.glBindBuffer(34963, this.floorIndexBufferId);
        GLES20.glBufferData(34963, asShortBuffer.capacity() * 2, asShortBuffer, 35044);
        GLES20.glBindBuffer(34962, 0);
    }

    private void setupRoad() {
        if (this.roadVertexes != null && this.roadIndexes != null) {
            float[] convert = OpenGLInterleavedVertex.convert(this.roadVertexes);
            Buffer asFloatBuffer = ByteBuffer.allocateDirect(convert.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            asFloatBuffer.put(convert).position(0);
            Buffer asShortBuffer = ByteBuffer.allocateDirect(this.roadIndexes.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
            asShortBuffer.put(this.roadIndexes).position(0);
            int[] iArr = new int[2];
            GLES20.glGenBuffers(2, iArr, 0);
            this.roadVertexBufferId = iArr[0];
            this.roadIndexBufferId = iArr[1];
            GLES20.glBindBuffer(34962, this.roadVertexBufferId);
            GLES20.glBufferData(34962, asFloatBuffer.capacity() * 4, asFloatBuffer, 35044);
            GLES20.glBindBuffer(34963, this.roadIndexBufferId);
            GLES20.glBufferData(34963, asShortBuffer.capacity() * 2, asShortBuffer, 35044);
            this.animationStartup = true;
            this.mediaPlayerController.setMaxTime(((FlightDataItem) this.flightDetails.get(this.flightDetails.size() - 1)).getTime() - ((FlightDataItem) this.flightDetails.get(0)).getTime());
        }
    }

    private void setupSkydome() throws IOException {
        float[] loadFloatArray = Utils.loadFloatArray("shaders/sphere_vertexes.bin", this.context);
        short[] loadShortArray = Utils.loadShortArray("shaders/sphere_indexes.bin", this.context);
        this.sphereIndexesCount = loadShortArray.length;
        Buffer asFloatBuffer = ByteBuffer.allocateDirect(loadFloatArray.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        asFloatBuffer.put(loadFloatArray).position(0);
        Buffer asShortBuffer = ByteBuffer.allocateDirect(loadShortArray.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        asShortBuffer.put(loadShortArray).position(0);
        int[] iArr = new int[2];
        GLES20.glGenBuffers(2, iArr, 0);
        this.sphereVertexBufferId = iArr[0];
        this.sphereIndexBufferId = iArr[1];
        GLES20.glBindBuffer(34962, this.sphereVertexBufferId);
        GLES20.glBufferData(34962, asFloatBuffer.capacity() * 4, asFloatBuffer, 35044);
        GLES20.glBindBuffer(34963, this.sphereIndexBufferId);
        GLES20.glBufferData(34963, asShortBuffer.capacity() * 2, asShortBuffer, 35044);
        GLES20.glBindBuffer(34962, 0);
    }

    private void touchesBegan(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() == 1) {
            this.lastTouchPosition.set(motionEvent.getX(), motionEvent.getY());
            this.lastPinchDistance = 0.0f;
            this.singleToutch = true;
        } else if (motionEvent.getPointerCount() == 2) {
            this.p1.set(motionEvent.getX(0), motionEvent.getY(0));
            this.p2.set(motionEvent.getX(1), motionEvent.getY(1));
            float f = this.p1.x - this.p2.x;
            float f2 = this.p1.y - this.p2.y;
            this.lastPinchDistance = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
            this.lastPinchAveragePosition.x = (this.p1.x + this.p2.x) / 2.0f;
            this.lastPinchAveragePosition.y = (this.p1.y + this.p2.y) / 2.0f;
            this.singleToutch = false;
        } else {
            this.singleToutch = false;
        }
        this.animationStartup = false;
    }

    private void touchesEnded(MotionEvent motionEvent) {
        this.floorRotationX = 15.0f;
        this.floorRotationY = 0.0f;
        this.floorTranslationZ = GroundOverlayOptions.NO_DIMENSION;
        this.floorTranslationX = 0.0f;
        this.lastTouchPosition.set(0.0f, 0.0f);
        this.lastPinchDistance = 0.0f;
        this.lastPinchAveragePosition.set(0.0f, 0.0f);
    }

    private void touchesMoved(MotionEvent motionEvent) {
        float f;
        float f2;
        if (motionEvent.getPointerCount() == 1) {
            this.currentTouchPosition.set(motionEvent.getX(), motionEvent.getY());
            f = this.lastTouchPosition.x;
            f2 = this.currentTouchPosition.x;
            float f3 = this.lastTouchPosition.y;
            float f4 = this.currentTouchPosition.y;
            this.lastTouchPosition.set(this.currentTouchPosition);
            if (this.singleToutch) {
                rotateCubeAroundX(f3 - f4, f - f2);
            }
        } else if (motionEvent.getPointerCount() == 2) {
            this.p1.set(motionEvent.getX(0), motionEvent.getY(0));
            this.p2.set(motionEvent.getX(1), motionEvent.getY(1));
            f = this.p1.x - this.p2.x;
            f2 = this.p1.y - this.p2.y;
            f = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
            if (this.lastPinchDistance != 0.0f) {
                int i = this.viewWidth;
                this.floorTranslationZ -= (2.0f * (this.lastPinchDistance - f)) / ((float) this.viewHeight);
                if (this.floorTranslationZ < -1.95f) {
                    this.floorTranslationZ = -1.95f;
                } else if (this.floorTranslationZ > -0.1f) {
                    this.floorTranslationZ = -0.1f;
                }
            }
            this.lastPinchDistance = f;
        }
    }

    public void onDrawFrame(GL10 gl10) {
        float GLKMathRadiansToDegrees;
        GLES20.glClear(16640);
        if (this.animationStartup && this.floorRotationX > 15.0f) {
            this.floorRotationX -= 1.0f;
        }
        if (!(this.roadVertexes == null || this.roadIndexes == null)) {
            GLKMathRadiansToDegrees = IOsUtils.GLKMathRadiansToDegrees((float) Math.asin((double) ((this.roadVertexes[(int) (this.mediaPlayerController.getPosition() * ((float) (this.roadIndexes.length - 1)))].position[1] * -2.0f) / (-this.floorTranslationZ))));
            if (this.floorRotationX < GLKMathRadiansToDegrees) {
                this.floorRotationX = GLKMathRadiansToDegrees;
            } else if (this.floorRotationX > 180.0f - GLKMathRadiansToDegrees) {
                this.floorRotationX = 180.0f - GLKMathRadiansToDegrees;
            }
        }
        GLES20.glUniform1i(this.useTextureHandle, 1);
        initMatrix();
        applyMatrix(this.modelViewMatrix);
        drawSkydome();
        if (!(this.roadVertexes == null || this.roadIndexes == null)) {
            GLES20.glUniform1i(this.useTextureHandle, 0);
            initMatrix();
            applyMatrix(this.modelViewMatrix);
            drawRoads();
            GLKMathRadiansToDegrees = this.mediaPlayerController.getPosition();
            int length = (int) (((float) (this.roadIndexes.length - 1)) * GLKMathRadiansToDegrees);
            Matrix.translateM(this.modelViewMatrix, 0, this.roadVertexes[length].position[0], this.roadVertexes[length].position[1], this.roadVertexes[length].position[2]);
            FlightDataItem flightDataItem = (FlightDataItem) this.flightDetails.get((int) (GLKMathRadiansToDegrees * ((float) (this.flightDetails.size() - 1))));
            this.currentPhi = flightDataItem.demo_phi / 1000.0f;
            this.currentTheta = flightDataItem.demo_theta / 1000.0f;
            this.currentPsi = 180.0f - (flightDataItem.demo_psi / 1000.0f);
            Matrix.rotateM(this.modelViewMatrix, 0, this.currentPsi, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(this.modelViewMatrix, 0, -this.currentTheta, 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(this.modelViewMatrix, 0, this.currentPhi, 0.0f, 0.0f, 1.0f);
            Matrix.rotateM(this.modelViewMatrix, 0, DRONE_ORIENTATION_THETA, 1.0f, 0.0f, 0.0f);
            Matrix.scaleM(this.modelViewMatrix, 0, 0.03f, 0.03f, 0.03f);
            applyMatrix(this.modelViewMatrix);
            GLES20.glUniform3f(this.lightsPosHandle, 1.0f, 1.0f, 1.0f);
            GLES20.glUniform1i(this.useTextureHandle, 1);
            GLES20.glUniform1i(this.useLightHandle, 1);
            drawDrone();
            GLES20.glUniform1i(this.useLightHandle, 0);
        }
        initMatrix();
        Matrix.translateM(this.modelViewMatrix, 0, 0.0f, -0.001f, 0.0f);
        applyMatrix(this.modelViewMatrix);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        GLES20.glUniform1f(this.startGradientPosHandle, getStartGradientPos());
        drawFloor();
        GLES20.glUniform1f(this.startGradientPosHandle, 0.0f);
        GLES20.glDisable(3042);
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.viewWidth = i;
        this.viewHeight = i2;
        GLES20.glViewport(0, 0, i, i2);
        System.arraycopy(IOsUtils.GLKMatrix4MakePerspective(IOsUtils.GLKMathDegreesToRadians(45.0f), ((float) i) / ((float) i2), GAME_NEAR, GAME_FAR), 0, this.projectionMatrix, 0, 16);
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(NORMAL_START_GRADIENT_COEF, 1.0f, 0.0f, 0.0f);
        GLES20.glEnable(2929);
        this.programHandle = ShaderHelper.createAndLinkProgram(ShaderHelper.compileShader(35633, Utils.readTextFileFromRawResource(this.context, C0984R.raw.vertex_shader)), ShaderHelper.compileShader(35632, Utils.readTextFileFromRawResource(this.context, C0984R.raw.fragment_shader)), new String[]{"a_Position", "a_Color", "a_Normal", "a_TexCoordinate"});
        this.sphereTextureHandle = TextureHelper.loadTexture(this.context.getResources(), C0984R.drawable.skydome);
        this.droneTextureHandle = TextureHelper.loadTexture(this.context.getResources(), C0984R.drawable.drone_texture);
        this.floorTextureHandle = TextureHelper.loadTexture(this.mapImage);
        GLES20.glUseProgram(this.programHandle);
        this.MVPMatrixHandle = GLES20.glGetUniformLocation(this.programHandle, "u_MVPMatrix");
        this.textureUniformHandle = GLES20.glGetUniformLocation(this.programHandle, "u_Texture");
        this.positionHandle = GLES20.glGetAttribLocation(this.programHandle, "a_Position");
        this.colorHandle = GLES20.glGetAttribLocation(this.programHandle, "a_Color");
        this.normalHandle = GLES20.glGetAttribLocation(this.programHandle, "a_Normal");
        this.textcoordsHandle = GLES20.glGetAttribLocation(this.programHandle, "a_TexCoordinate");
        this.useTextureHandle = GLES20.glGetUniformLocation(this.programHandle, "u_useTexture");
        this.useLightHandle = GLES20.glGetUniformLocation(this.programHandle, "u_UseLight");
        this.lightsPosHandle = GLES20.glGetUniformLocation(this.programHandle, "u_LightPos");
        this.startGradientPosHandle = GLES20.glGetUniformLocation(this.programHandle, "u_startGradientPos");
        GLES20.glUniform3f(this.lightsPosHandle, 1.0f, 1.0f, 1.0f);
        try {
            setupSkydome();
            setupDrone();
            setupFloor();
            setupRoad();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.gestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        switch (motionEvent.getAction() & 255) {
            case 0:
            case 5:
                touchesBegan(motionEvent);
                return true;
            case 1:
            case 6:
                return true;
            case 2:
                touchesMoved(motionEvent);
                return true;
            default:
                return false;
        }
    }

    public void updateFloorTexture(Bitmap bitmap) {
        if (bitmap.getWidth() != bitmap.getHeight()) {
            throw new IllegalArgumentException("bitmap width != height");
        }
        TextureHelper.updateTexture(this.floorTextureHandle, bitmap);
    }
}
