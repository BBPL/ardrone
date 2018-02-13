package com.parrot.freeflight.video;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import com.parrot.freeflight.ui.gl.GLBGVideoSprite;
import com.parrot.freeflight.ui.hud.Sprite;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VideoStageRenderer implements Renderer {
    private GLBGVideoSprite bgSprite = new GLBGVideoSprite();
    private long endTime;
    private float fps;
    private final String fragmentShaderCode = "precision mediump float;  \nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nuniform float fAlpha ;\nvoid main(){              \n vec4 color = texture2D(sTexture, vTextureCoord); \n gl_FragColor = vec4(color.xyz, color.w * fAlpha );\n //gl_FragColor = vec4(0.6, 0.7, 0.2, 1.0); \n}                         \n";
    private Map<Integer, Sprite> idSpriteMap;
    private float[] mProjMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private int program;
    private int screenHeight;
    private int screenWidth;
    private ArrayList<Sprite> sprites;
    private long startTime;
    private final String vertexShaderCode = "uniform mat4 uMVPMatrix;   \nattribute vec4 vPosition; \nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main(){              \n  gl_Position = uMVPMatrix * vPosition; \n  vTextureCoord = aTextureCoord;\n}                         \n";
    private boolean videoEnabled = true;

    public VideoStageRenderer() {
        this.bgSprite.setAlpha(1.0f);
        this.idSpriteMap = new Hashtable();
        this.sprites = new ArrayList(4);
    }

    private int loadShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] == 0) {
            Log.e("opengl", "Could not compile shader");
            Log.e("opengl", GLES20.glGetShaderInfoLog(glCreateShader));
            Log.e("opengl", str);
        }
        return glCreateShader;
    }

    public void addSprite(Integer num, Sprite sprite) {
        if (!this.idSpriteMap.containsKey(num)) {
            this.idSpriteMap.put(num, sprite);
            synchronized (this.sprites) {
                this.sprites.add(sprite);
            }
        }
    }

    public void addSpriteBegin(Integer num, Sprite sprite) {
        if (!this.idSpriteMap.containsKey(num)) {
            this.idSpriteMap.put(num, sprite);
            synchronized (this.sprites) {
                this.sprites.add(0, sprite);
            }
        }
    }

    public void clearSprites() {
        synchronized (this.sprites) {
            for (int i = 0; i < this.sprites.size(); i++) {
                ((Sprite) this.sprites.get(i)).freeResources();
            }
            this.sprites.clear();
        }
    }

    public void enableVideo(boolean z) {
        this.videoEnabled = z;
    }

    public float getFPS() {
        return this.fps;
    }

    public Sprite getSprite(Integer num) {
        return (Sprite) this.idSpriteMap.get(num);
    }

    public void onDrawFrame(GL10 gl10) {
        this.endTime = System.currentTimeMillis();
        long j = this.endTime - this.startTime;
        if (j < 33) {
            try {
                Thread.sleep(33 - j);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.startTime = System.currentTimeMillis();
        if (this.videoEnabled) {
            this.bgSprite.onDraw(gl10, 0.0f, 0.0f);
        } else {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(16384);
        }
        synchronized (this.sprites) {
            int size = this.sprites.size();
            for (int i = 0; i < size; i++) {
                Sprite sprite = (Sprite) this.sprites.get(i);
                if (sprite != null) {
                    if (!(sprite.isInitialized() || this.screenWidth == 0 || this.screenHeight == 0)) {
                        sprite.init(gl10, this.program);
                        sprite.surfaceChanged(gl10, this.screenWidth, this.screenHeight);
                        sprite.setViewAndProjectionMatrices(this.mVMatrix, this.mProjMatrix);
                    }
                    sprite.draw(gl10);
                }
            }
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        int i3 = 0;
        this.screenWidth = i;
        this.screenHeight = i2;
        GLES20.glViewport(0, 0, i, i2);
        Matrix.orthoM(this.mProjMatrix, 0, 0.0f, (float) i, 0.0f, (float) i2, 0.0f, 2.0f);
        this.bgSprite.setViewAndProjectionMatrices(this.mVMatrix, this.mProjMatrix);
        this.bgSprite.onSurfaceChanged(gl10, i, i2);
        synchronized (this.sprites) {
            int size = this.sprites.size();
            while (i3 < size) {
                Sprite sprite = (Sprite) this.sprites.get(i3);
                if (sprite != null) {
                    sprite.setViewAndProjectionMatrices(this.mVMatrix, this.mProjMatrix);
                    sprite.surfaceChanged(null, i, i2);
                }
                i3++;
            }
        }
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.startTime = System.currentTimeMillis();
        GLES20.glEnable(3042);
        GLES20.glDisable(2929);
        GLES20.glBlendFunc(770, 771);
        int loadShader = loadShader(35633, "uniform mat4 uMVPMatrix;   \nattribute vec4 vPosition; \nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main(){              \n  gl_Position = uMVPMatrix * vPosition; \n  vTextureCoord = aTextureCoord;\n}                         \n");
        int loadShader2 = loadShader(35632, "precision mediump float;  \nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nuniform float fAlpha ;\nvoid main(){              \n vec4 color = texture2D(sTexture, vTextureCoord); \n gl_FragColor = vec4(color.xyz, color.w * fAlpha );\n //gl_FragColor = vec4(0.6, 0.7, 0.2, 1.0); \n}                         \n");
        this.program = GLES20.glCreateProgram();
        GLES20.glAttachShader(this.program, loadShader);
        GLES20.glAttachShader(this.program, loadShader2);
        GLES20.glLinkProgram(this.program);
        this.bgSprite.init(gl10, this.program);
        synchronized (this.sprites) {
            for (loadShader2 = 0; loadShader2 < this.sprites.size(); loadShader2++) {
                ((Sprite) this.sprites.get(loadShader2)).init(gl10, this.program);
            }
        }
        Matrix.setLookAtM(this.mVMatrix, 0, 0.0f, 0.0f, 1.5f, 0.0f, 0.0f, -5.0f, 0.0f, 1.0f, 0.0f);
    }

    public void removeSprite(Integer num) {
        if (this.idSpriteMap.containsKey(num)) {
            Sprite sprite = (Sprite) this.idSpriteMap.get(num);
            synchronized (this.sprites) {
                this.sprites.remove(sprite);
                this.idSpriteMap.remove(num);
            }
        }
    }

    public boolean replaceSprite(int i, Sprite sprite) {
        Sprite sprite2 = (Sprite) this.idSpriteMap.get(Integer.valueOf(i));
        if (sprite2 == null) {
            return false;
        }
        synchronized (this.sprites) {
            this.sprites.set(this.sprites.indexOf(sprite2), sprite);
            this.idSpriteMap.put(Integer.valueOf(i), sprite);
        }
        return true;
    }
}
