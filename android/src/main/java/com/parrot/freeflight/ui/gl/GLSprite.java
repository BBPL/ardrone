package com.parrot.freeflight.ui.gl;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Build.VERSION;
import android.util.Log;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;
import com.parrot.freeflight.utils.TextureUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

public class GLSprite {
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int INDEX_BUFFER = 1;
    private static final String TAG = GLSprite.class.getSimpleName();
    private static final int TEXTURE_COORDS_SIZE = 2;
    private static final int VERTEX_BUFFER = 0;
    private static final int VERTEX_COORDS_SIZE = 3;
    private static final int _COUNT = 4;
    public float alpha;
    protected int[] buffers = new int[]{-1, -1};
    private int clampMode = 33071;
    private int fAlphaHandle;
    private int filteringMode = 9729;
    public int height = -1;
    public int imageHeight;
    public int imageWidth;
    private Buffer indexes;
    private final IBitmapLoader loader;
    private float[] mMMatrix = new float[16];
    private float[] mMVMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mProjMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];
    private float[] mTempMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private int mvpMatrixHandle;
    private Bitmap newTexture;
    private int positionHandle;
    private float prevX;
    private float prevY;
    protected int program;
    protected boolean readyToDraw;
    private boolean recalculateMatrix;
    private float rotation = 0.0f;
    private int textureHandle;
    public int textureHeight;
    public int textureWidth;
    protected int[] textures = new int[]{-1};
    private boolean updateVertexBuffer;
    private final boolean useWorkaroundsForSDK8;
    private Buffer vertices;
    public int width = -1;

    public GLSprite(IBitmapLoader iBitmapLoader) {
        this.loader = iBitmapLoader;
        this.useWorkaroundsForSDK8 = VERSION.SDK_INT < 9;
        this.updateVertexBuffer = false;
        this.recalculateMatrix = true;
        this.alpha = 1.0f;
        this.readyToDraw = false;
    }

    private void checkGlError(String str) {
        while (true) {
            int glGetError = GLES20.glGetError();
            if (glGetError != 0) {
                try {
                    throw new RuntimeException(str + ": glError " + glGetError);
                } catch (Throwable e) {
                    Log.w(TAG, Log.getStackTraceString(e));
                }
            } else {
                return;
            }
        }
    }

    private ShortBuffer createIndex() {
        short[] sArr = new short[]{(short) 0, (short) 1, (short) 2, (short) 3};
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(sArr.length * 2);
        allocateDirect.order(ByteOrder.nativeOrder());
        ShortBuffer asShortBuffer = allocateDirect.asShortBuffer();
        asShortBuffer.put(sArr);
        asShortBuffer.position(0);
        return asShortBuffer;
    }

    private FloatBuffer createVertex(float f, float f2) {
        float f3 = ((float) this.imageWidth) / ((float) this.textureWidth);
        float f4 = ((float) this.imageHeight) / ((float) this.textureHeight);
        float[] fArr = new float[]{f, 0.0f, 0.0f, f3, f4, f, f2, 0.0f, f3, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, f4, 0.0f, f2, 0.0f, 0.0f, 0.0f};
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        return asFloatBuffer;
    }

    @SuppressLint({"NewApi"})
    private void initTexture() {
        if (this.textures[0] != -1) {
            GLES20.glDeleteTextures(1, this.textures, 0);
        }
        if (this.buffers[0] != -1) {
            GLES20.glDeleteBuffers(this.buffers.length, this.buffers, 0);
        }
        GLES20.glGenTextures(1, this.textures, 0);
        GLES20.glBindTexture(3553, this.textures[0]);
        checkGlError("glBindTexture");
        GLES20.glTexParameterf(3553, 10241, (float) this.filteringMode);
        GLES20.glTexParameterf(3553, 10240, (float) this.filteringMode);
        GLES20.glTexParameteri(3553, 10242, this.clampMode);
        GLES20.glTexParameteri(3553, 10243, this.clampMode);
        if (this.loader != null) {
            Bitmap loadBitmap = this.loader.loadBitmap();
            Bitmap makeTexture = TextureUtils.makeTexture(loadBitmap, false);
            GLUtils.texImage2D(3553, 0, makeTexture, 0);
            this.width = loadBitmap.getWidth();
            this.height = loadBitmap.getHeight();
            this.imageWidth = this.width;
            this.imageHeight = this.height;
            this.textureWidth = makeTexture.getWidth();
            this.textureHeight = makeTexture.getHeight();
            makeTexture.recycle();
            loadBitmap.recycle();
            checkGlError("texImage2D");
        }
        GLES20.glGenBuffers(this.buffers.length, this.buffers, 0);
        this.vertices = createVertex((float) this.width, (float) this.height);
        GLES20.glBindBuffer(34962, this.buffers[0]);
        checkGlError("glBindBuffer buffers[0]");
        GLES20.glBufferData(34962, 80, this.vertices, 35044);
        checkGlError("glBufferData vertices");
        if (this.useWorkaroundsForSDK8) {
            fix.android.opengl.GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 20, 0);
        } else {
            GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 20, 0);
        }
        GLES20.glEnableVertexAttribArray(this.positionHandle);
        if (this.useWorkaroundsForSDK8) {
            fix.android.opengl.GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 20, 12);
        } else {
            GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 20, 12);
        }
        GLES20.glEnableVertexAttribArray(this.textureHandle);
        this.indexes = createIndex();
        GLES20.glBindBuffer(34963, this.buffers[1]);
        GLES20.glBufferData(34963, 8, this.indexes, 35044);
    }

    public void freeResources() {
        if (this.newTexture != null) {
            this.newTexture.recycle();
            this.newTexture = null;
        }
    }

    public int getHeight() {
        if (this.height != -1 || this.loader == null) {
            return this.height;
        }
        Holder holder = new Holder();
        Holder holder2 = new Holder();
        this.loader.getBitmapDims(holder, holder2);
        return ((Integer) holder2.value).intValue();
    }

    public float getRotation() {
        return this.rotation;
    }

    public int getWidth() {
        if (this.width != -1 || this.loader == null) {
            return this.width;
        }
        Holder holder = new Holder();
        this.loader.getBitmapDims(holder, new Holder());
        return ((Integer) holder.value).intValue();
    }

    public void init(GL10 gl10, int i) {
        this.program = i;
        GLES20.glUseProgram(i);
        checkGlError("glUseProgram program");
        this.positionHandle = GLES20.glGetAttribLocation(i, "vPosition");
        this.textureHandle = GLES20.glGetAttribLocation(i, "aTextureCoord");
        this.mvpMatrixHandle = GLES20.glGetUniformLocation(i, "uMVPMatrix");
        this.fAlphaHandle = GLES20.glGetUniformLocation(i, "fAlpha");
        checkGlError("glGetAttribLocation");
        initTexture();
    }

    public boolean isReadyToDraw() {
        return this.readyToDraw;
    }

    @SuppressLint({"NewApi"})
    public void onDraw(GL10 gl10, float f, float f2) {
        if (this.readyToDraw) {
            if (!(this.prevX == f && this.prevY == f2)) {
                this.recalculateMatrix = true;
                this.prevX = f;
                this.prevY = f2;
            }
            GLES20.glUseProgram(this.program);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.textures[0]);
            onUpdateTexture();
            GLES20.glBindBuffer(34962, this.buffers[0]);
            if (this.updateVertexBuffer) {
                GLES20.glBufferSubData(34962, 0, 80, this.vertices);
                this.updateVertexBuffer = false;
            }
            if (this.useWorkaroundsForSDK8) {
                fix.android.opengl.GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 20, 0);
                fix.android.opengl.GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 20, 12);
            } else {
                GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 20, 0);
                GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 20, 12);
            }
            if (this.recalculateMatrix) {
                Matrix.setIdentityM(this.mMMatrix, 0);
                Matrix.translateM(this.mMMatrix, 0, f, f2, 0.0f);
                if (this.rotation != 0.0f) {
                    Matrix.setIdentityM(this.mRotationMatrix, 0);
                    Matrix.rotateM(this.mRotationMatrix, 0, this.rotation, 0.0f, 0.0f, 1.0f);
                    Matrix.translateM(this.mRotationMatrix, 0, (float) ((-this.imageWidth) / 2), (float) ((-this.imageHeight) / 2), 0.0f);
                    Matrix.translateM(this.mMMatrix, 0, (float) (this.imageWidth / 2), (float) (this.imageHeight / 2), 0.0f);
                    Matrix.multiplyMM(this.mTempMatrix, 0, this.mMMatrix, 0, this.mRotationMatrix, 0);
                }
                Matrix.multiplyMM(this.mMVMatrix, 0, this.mVMatrix, 0, this.rotation == 0.0f ? this.mMMatrix : this.mTempMatrix, 0);
                Matrix.multiplyMM(this.mMVPMatrix, 0, this.mProjMatrix, 0, this.mMVMatrix, 0);
                this.recalculateMatrix = false;
            }
            GLES20.glUniformMatrix4fv(this.mvpMatrixHandle, 1, false, this.mMVPMatrix, 0);
            if (this.alpha < 1.0f) {
                GLES20.glUniform1f(this.fAlphaHandle, this.alpha);
            } else {
                GLES20.glUniform1f(this.fAlphaHandle, 1.0f);
            }
            GLES20.glBindBuffer(34963, this.buffers[1]);
            if (this.useWorkaroundsForSDK8) {
                GLES20.glDrawArrays(5, 0, 4);
            } else {
                GLES20.glDrawElements(5, 4, 5123, 0);
            }
            checkGlError("glDrawElements");
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.recalculateMatrix = true;
    }

    protected void onUpdateTexture() {
        if (this.newTexture != null) {
            this.textureWidth = this.newTexture.getWidth();
            this.textureHeight = this.newTexture.getHeight();
            GLUtils.texImage2D(3553, 0, this.newTexture, 0);
            this.vertices = createVertex((float) this.width, (float) this.height);
            this.updateVertexBuffer = true;
            freeResources();
        }
    }

    public void setAlpha(float f) {
        if (f > 1.0f) {
            this.alpha = 1.0f;
        }
        if (f < 0.0f) {
            this.alpha = 0.0f;
        }
        this.alpha = f;
    }

    public void setClampMode(int i) {
        this.clampMode = i;
    }

    public void setFilteringMode(int i) {
        this.filteringMode = i;
    }

    public void setRotation(float f) {
        this.rotation = f;
        this.recalculateMatrix = true;
    }

    public void setSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        this.vertices = createVertex((float) i, (float) i2);
        this.updateVertexBuffer = true;
    }

    public void setViewAndProjectionMatrices(float[] fArr, float[] fArr2) {
        this.mVMatrix = fArr;
        this.mProjMatrix = fArr2;
        this.recalculateMatrix = true;
        this.readyToDraw = true;
    }

    public void updateTexture(Bitmap bitmap) {
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.imageWidth = this.width;
        this.imageHeight = this.height;
        this.newTexture = TextureUtils.makeTexture(bitmap, false);
    }
}
