package com.parrot.freeflight.track_3d_viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import java.util.List;

public class ThumbnailsView extends View {
    private static final int ARROW_HEIGHT = 10;
    private static final int ARROW_WIDTH = 10;
    private final Paint bmpPaint = new Paint();
    private final GestureDetector gestureDetector;
    private final SimpleOnGestureListener gestureListener = new C11891();
    private int left;
    private final Paint linePaint = new Paint();
    private int right;
    private IThumbnailClickListener thumbnailClickListener;
    private List<ThumbnailItem> thumbnailsData = null;

    class C11891 extends SimpleOnGestureListener {
        C11891() {
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return ThumbnailsView.this.checkClick((int) motionEvent.getX(), (int) motionEvent.getY());
        }
    }

    public interface IThumbnailClickListener {
        void onThumbnailClicked(ThumbnailItem thumbnailItem);
    }

    public static class ThumbnailItem {
        public final Bitmap bitmap;
        public final float length;
        public final float pos;
        public final Object tag;

        public ThumbnailItem(float f, float f2, Bitmap bitmap, Object obj) {
            this.pos = f;
            this.length = f2;
            this.bitmap = bitmap;
            this.tag = obj;
        }
    }

    public ThumbnailsView(Context context) {
        super(context);
        this.gestureDetector = new GestureDetector(context, this.gestureListener);
        setClickable(true);
        init();
    }

    public ThumbnailsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.gestureDetector = new GestureDetector(context, this.gestureListener);
        setClickable(true);
        init();
    }

    private boolean checkClick(int i, int i2) {
        if (i >= this.left && i <= this.right) {
            int i3 = this.right - this.left;
            float f = i3 == 0 ? 0.0f : ((float) (i - this.left)) / ((float) i3);
            if (this.thumbnailsData != null) {
                for (ThumbnailItem thumbnailItem : this.thumbnailsData) {
                    float width = getWidth() == 0 ? 0.0f : ((float) thumbnailItem.bitmap.getWidth()) / ((float) getWidth());
                    if (f >= thumbnailItem.pos - (width / 2.0f)) {
                        if (f < (width / 2.0f) + thumbnailItem.pos) {
                            if (this.thumbnailClickListener != null) {
                                this.thumbnailClickListener.onThumbnailClicked(thumbnailItem);
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void drawThumbnail(Canvas canvas, Bitmap bitmap, int i, int i2) {
        if (i >= canvas.getWidth()) {
            i = canvas.getWidth() - 1;
        }
        int height = canvas.getHeight();
        Path path = new Path();
        path.reset();
        path.moveTo((float) i, (float) height);
        path.lineTo((float) (i + 10), (float) (height - 10));
        path.lineTo((float) (i - 10), (float) (height - 10));
        this.linePaint.setStyle(Style.FILL_AND_STROKE);
        canvas.drawPath(path, this.linePaint);
        this.linePaint.setStyle(Style.STROKE);
        height = i - (bitmap.getWidth() / 2);
        int width = height < 1 ? 1 : bitmap.getWidth() + height >= canvas.getWidth() ? (canvas.getWidth() - bitmap.getWidth()) - 1 : height;
        int height2 = ((canvas.getHeight() - bitmap.getHeight()) - 10) - 1;
        canvas.drawRect((float) (width - 1), (float) (height2 - 1), (float) (bitmap.getWidth() + width), (float) (bitmap.getHeight() + height2), this.linePaint);
        canvas.drawBitmap(bitmap, (float) width, (float) height2, this.bmpPaint);
    }

    private int getMaxBitmapHeight() {
        if (this.thumbnailsData == null) {
            return 0;
        }
        int i = 0;
        for (ThumbnailItem thumbnailItem : this.thumbnailsData) {
            if (thumbnailItem.bitmap.getHeight() > i) {
                i = thumbnailItem.bitmap.getHeight();
            }
        }
        return i;
    }

    private void init() {
        this.linePaint.setColor(-1);
        this.bmpPaint.setFilterBitmap(true);
    }

    public IThumbnailClickListener getThumbnailClickListener() {
        return this.thumbnailClickListener;
    }

    public List<ThumbnailItem> getThumbnailsData() {
        return this.thumbnailsData;
    }

    protected void onDraw(Canvas canvas) {
        int i = this.right - this.left;
        if (this.thumbnailsData != null) {
            for (ThumbnailItem thumbnailItem : this.thumbnailsData) {
                int i2 = (int) (((float) i) * thumbnailItem.length);
                drawThumbnail(canvas, thumbnailItem.bitmap, ((int) (((float) i) * thumbnailItem.pos)) + this.left, i2);
            }
        }
        super.onDraw(canvas);
    }

    protected void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i2);
        int size = mode == 0 ? Integer.MAX_VALUE : MeasureSpec.getSize(i2);
        if (mode == Integer.MIN_VALUE || mode == 0) {
            setMeasuredDimension(MeasureSpec.getSize(i), Math.min((getMaxBitmapHeight() + 10) + 2, size));
        } else {
            super.onMeasure(i, i2);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.gestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void setLeftRight(int i, int i2) {
        this.left = i;
        this.right = i2;
        invalidate();
    }

    public void setLinesColor(int i) {
        this.linePaint.setColor(i);
        invalidate();
    }

    public void setThumbnailClickListener(IThumbnailClickListener iThumbnailClickListener) {
        this.thumbnailClickListener = iThumbnailClickListener;
    }

    public void setThumbnailsData(List<ThumbnailItem> list) {
        this.thumbnailsData = list;
        invalidate();
    }
}
