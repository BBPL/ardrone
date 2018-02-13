package com.parrot.freeflight.ui.widgets.graph;

import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.util.ArrayList;
import java.util.List;

public class MediaThumbsOverlay extends GraphElement {
    private List<Pair<Long, Drawable>> data;
    private int dataSize;
    private int flagLineHeight = (this.thumbHeight + this.thumbMargin);
    private GraphView parent;
    private Series series;
    private Paint shadowPaint;
    private Paint strokePaint = new Paint();
    private OnMediaThumbClickedListener thumbClickListener;
    private int thumbHeight;
    private int thumbMargin = (this.thumbHeight / 2);
    private int thumbWidth;

    public interface OnMediaThumbClickedListener {
        void onThumbClicked(GraphView graphView, Drawable drawable, int i);
    }

    public MediaThumbsOverlay(GraphView graphView, Series series) {
        this.parent = graphView;
        this.series = series;
        this.thumbWidth = graphView.thumbWidth;
        this.thumbHeight = graphView.thumbHeight;
        this.strokePaint.setColor(-16777216);
        this.strokePaint.setStrokeWidth(2.0f);
        this.shadowPaint = new Paint(0);
        this.shadowPaint.setColor(2131759120);
        this.shadowPaint.setMaskFilter(new BlurMaskFilter(2.0f, Blur.OUTER));
    }

    private boolean canDrawLeft(float f, float f2) {
        return f - ((float) this.thumbWidth) > this.series.getBounds().left;
    }

    private boolean canDrawUp(float f, float f2) {
        return (((float) ((int) (((float) this.flagLineHeight) / 2.0f))) + f2) + ((float) this.thumbHeight) < ((float) (((this.series.getMaxY() - this.series.getMinY()) * this.series.getScaleY()) + ((double) (this.series.height - this.series.getDrawArea().bottom))));
    }

    private List<Pair<Long, Drawable>> fixDrawableData(List<Pair<Long, Drawable>> list) {
        int size = list.size();
        List<Pair<Long, Drawable>> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            Pair pair = (Pair) list.get(i);
            if (pair.second == null) {
                Shape rectShape = new RectShape();
                rectShape.resize((float) this.thumbWidth, (float) this.thumbHeight);
                arrayList.add(new Pair(pair.first, new ShapeDrawable(rectShape)));
            } else {
                arrayList.add(pair);
            }
        }
        return arrayList;
    }

    public void draw(Canvas canvas) {
        if (this.series.visible) {
            for (int i = 0; i < this.dataSize; i++) {
                int i2;
                int i3;
                int i4;
                int i5;
                Pair pair = (Pair) this.data.get(i);
                Long l = (Long) pair.first;
                float longValue = (float) ((((double) (l.longValue() - this.series.getMinX())) * this.series.getScaleX()) + ((double) this.series.getBounds().left));
                float yValue = (float) (((this.series.getYValue(l.longValue()) - this.series.getMinY()) * this.series.getScaleY()) + ((double) (this.series.height - this.series.getDrawArea().bottom)));
                boolean canDrawUp = canDrawUp(longValue, yValue);
                boolean canDrawLeft = canDrawLeft(longValue, yValue);
                if (canDrawUp) {
                    canvas.drawLine(longValue, yValue, longValue, yValue + ((float) this.flagLineHeight), this.strokePaint);
                } else {
                    canvas.drawLine(longValue, yValue, longValue, yValue - ((float) this.flagLineHeight), this.strokePaint);
                }
                Drawable drawable = (Drawable) pair.second;
                if (canDrawLeft) {
                    i2 = (int) longValue;
                    i3 = (int) (longValue - ((float) this.thumbWidth));
                } else {
                    i2 = (int) (longValue + ((float) this.thumbWidth));
                    i3 = (int) longValue;
                }
                if (canDrawUp) {
                    i4 = (int) (((float) this.thumbMargin) + yValue);
                    i5 = ((int) (yValue + ((float) this.thumbMargin))) + this.thumbHeight;
                } else {
                    i4 = (int) ((yValue - ((float) this.thumbMargin)) - ((float) this.thumbHeight));
                    i5 = (int) (yValue - ((float) this.thumbMargin));
                }
                drawable.setFilterBitmap(true);
                drawable.setBounds(i3, i4, i2, i5);
                Rect bounds = drawable.getBounds();
                if (canvas.getClipBounds().intersects(i3, i4, i2, i5)) {
                    canvas.save(1);
                    canvas.scale(1.0f, GroundOverlayOptions.NO_DIMENSION, bounds.exactCenterX(), bounds.exactCenterY());
                    canvas.drawRect(bounds, this.shadowPaint);
                    drawable.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void onMeasure(int i, int i2) {
        setSize(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
    }

    boolean onSingleTapUp(MotionEvent motionEvent) {
        for (int i = 0; i < this.dataSize; i++) {
            Drawable drawable = (Drawable) ((Pair) this.data.get(i)).second;
            if (drawable.getBounds().contains((int) motionEvent.getX(), (int) (((float) this.height) - motionEvent.getY()))) {
                if (this.thumbClickListener != null) {
                    this.thumbClickListener.onThumbClicked(this.parent, drawable, i);
                }
                return true;
            }
        }
        return false;
    }

    boolean onTouch(View view, MotionEvent motionEvent) {
        int i;
        switch (motionEvent.getActionMasked()) {
            case 0:
                for (i = 0; i < this.dataSize; i++) {
                    Drawable drawable = (Drawable) ((Pair) this.data.get(i)).second;
                    if (drawable.getBounds().contains((int) motionEvent.getX(), (int) (((float) this.height) - motionEvent.getY()))) {
                        drawable.setColorFilter(-7829368, Mode.DARKEN);
                        this.parent.invalidate();
                    }
                }
                break;
            case 1:
                for (i = 0; i < this.dataSize; i++) {
                    ((Drawable) ((Pair) this.data.get(i)).second).setColorFilter(0, Mode.DARKEN);
                    this.parent.invalidate();
                }
                break;
        }
        return false;
    }

    public void setOnMediaThumbClickListener(OnMediaThumbClickedListener onMediaThumbClickedListener) {
        this.thumbClickListener = onMediaThumbClickedListener;
    }

    public void setThumbs(List<Pair<Long, Drawable>> list) {
        if (list == null) {
            throw new IllegalArgumentException();
        }
        this.data = fixDrawableData(list);
        this.dataSize = list.size();
    }
}
