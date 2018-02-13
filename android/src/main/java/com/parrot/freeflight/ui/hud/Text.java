package com.parrot.freeflight.ui.hud;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;
import com.parrot.freeflight.ui.gl.GLSprite;
import com.parrot.freeflight.ui.gl.IBitmapLoader;
import com.parrot.freeflight.ui.hud.Sprite.Align;
import javax.microedition.khronos.opengles.GL10;

public class Text extends Sprite {
    private final IBitmapLoader bitmapLoader = new C12411();
    private boolean blink;
    private Paint paint;
    private long prevNano;
    private Resources res;
    private GLSprite sprite;
    private String text;
    private boolean updateTexture;

    class C12411 implements IBitmapLoader {
        private Bitmap bitmap;

        C12411() {
        }

        public void getBitmapDims(Holder<Integer> holder, Holder<Integer> holder2) {
            Bitmap loadBitmap = loadBitmap();
            holder.value = Integer.valueOf(loadBitmap.getWidth());
            holder2.value = Integer.valueOf(loadBitmap.getHeight());
        }

        public Bitmap loadBitmap() {
            if (this.bitmap == null || this.bitmap.isRecycled()) {
                this.bitmap = Text.this.createBitmapToRender();
            }
            return this.bitmap;
        }
    }

    public Text(Context context, String str, Align align) {
        super(align);
        this.res = context.getResources();
        this.paint = new Paint();
        this.paint.setColor(context.getResources().getColor(17170433));
        this.paint.setTextSize(24.0f);
        this.paint.setAntiAlias(true);
        this.paint.setSubpixelText(true);
        this.text = str;
        initSprite();
        this.updateTexture = false;
    }

    public Text(Context context, String str, Align align, Typeface typeface, int i) {
        super(align);
        this.res = context.getResources();
        this.paint = new Paint();
        this.paint.setColor(context.getResources().getColor(17170433));
        this.paint.setTextSize((float) i);
        this.paint.setAntiAlias(true);
        this.paint.setSubpixelText(true);
        this.paint.setTypeface(typeface);
        this.text = str;
        initSprite();
        this.updateTexture = false;
    }

    private Bitmap createBitmapToRender() {
        float textSize;
        float f = 1.0f;
        if (this.text.length() > 0) {
            f = this.paint.measureText(this.text);
            textSize = this.paint.getTextSize();
        } else {
            textSize = 1.0f;
        }
        Bitmap createBitmap = Bitmap.createBitmap(Math.round(f), Math.round(textSize), Config.ARGB_4444);
        createBitmap.eraseColor(0);
        createBitmap.setDensity(this.res.getDisplayMetrics().densityDpi);
        new Canvas(createBitmap).drawText(this.text, 0.0f, textSize - this.paint.getFontMetrics().bottom, this.paint);
        return createBitmap;
    }

    private void initSprite() {
        this.sprite = new GLSprite(this.bitmapLoader);
    }

    private void invalidate() {
        this.updateTexture = true;
    }

    public void blink(boolean z) {
        this.blink = z;
    }

    public void draw(GL10 gl10) {
        if (this.updateTexture) {
            this.sprite.updateTexture(createBitmapToRender());
            layout(this.surfaceWidth, this.surfaceHeight);
            this.updateTexture = false;
        }
        if (this.blink) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.prevNano > 500) {
                this.prevNano = currentTimeMillis;
                if (this.sprite.alpha >= 1.0f) {
                    this.sprite.setAlpha(0.0f);
                } else {
                    this.sprite.setAlpha(1.0f);
                }
            }
        }
        if (this.visible) {
            this.sprite.onDraw(gl10, (float) this.bounds.left, (float) ((this.surfaceHeight - this.bounds.top) - this.sprite.height));
        }
    }

    public void freeResources() {
        this.sprite.freeResources();
    }

    public float getBaseLine() {
        return this.paint.getFontMetrics().bottom;
    }

    public int getHeight() {
        return this.sprite.getHeight();
    }

    public int getWidth() {
        return this.sprite.getWidth();
    }

    public void init(GL10 gl10, int i) {
        this.sprite.init(gl10, i);
    }

    public boolean isInitialized() {
        return this.sprite.isReadyToDraw();
    }

    public void onAlphaChanged(float f) {
        this.sprite.setAlpha(f);
    }

    public boolean onTouchEvent(View view, MotionEvent motionEvent) {
        return false;
    }

    public void setBold(boolean z) {
        if (this.paint.getTypeface() == null || this.paint.getTypeface().isBold() != z) {
            this.paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, z ? 1 : 0));
            invalidate();
        }
    }

    public void setText(String str) {
        if (!this.text.equals(str)) {
            this.text = str;
            invalidate();
        }
    }

    public void setTextColor(int i) {
        if (i != this.paint.getColor()) {
            this.paint.setColor(i);
            invalidate();
        }
    }

    public void setTextSize(int i) {
        if (this.paint.getTextSize() != ((float) i)) {
            this.paint.setTextSize((float) i);
            invalidate();
        }
    }

    public void setTypeface(Typeface typeface) {
        this.paint.setTypeface(typeface);
        invalidate();
    }

    public void setViewAndProjectionMatrices(float[] fArr, float[] fArr2) {
        this.sprite.setViewAndProjectionMatrices(fArr, fArr2);
    }
}
