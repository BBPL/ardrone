package com.parrot.freeflight.ui.widgets.graph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.ui.widgets.graph.Axis.Align;
import java.util.ArrayList;
import java.util.List;

public class GraphView extends View implements OnTouchListener, OnGestureListener {
    private List<Axis> axes;
    protected int axesColor;
    protected float axisLabelTextSize;
    private float axisMargin;
    protected float axisStrokeWidth;
    protected float axisTitleTextSize;
    protected float chartStrokeWidth;
    private PointF finger0;
    private PointF finger1;
    private GestureDetector gestureDetector;
    protected Rect graphArea;
    private Rect graphClipRect;
    private float graphScale;
    protected int gridLineColor;
    private MediaThumbsOverlay mediaOverlay;
    private Mode mode;
    private Rect padding;
    private PointF pivotPoint;
    private Pointer pointer;
    protected int pointerColor;
    protected float pointerDotRadius;
    protected float pointerSize;
    protected float pointerStrokeWidth;
    private int prevScrollerX;
    private float scale;
    private Scroller scroller;
    private List<Series> series;
    protected int seriesFillColor;
    protected int seriesLineColor;
    private double startDistance;
    protected int thumbHeight;
    protected int thumbWidth;
    protected float tickSize;
    protected float zeroLevelStrokeWidth;
    private boolean zoomEnabled;

    private enum Mode {
        NORMAL,
        ZOOMING
    }

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0984R.attr.GraphStyle);
    }

    public GraphView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.seriesLineColor = -65536;
        this.seriesFillColor = 2063597823;
        this.gridLineColor = -3355444;
        this.axesColor = -16777216;
        this.pointerColor = -16711936;
        this.pointerSize = 3.0f;
        this.axisStrokeWidth = 4.0f;
        this.zeroLevelStrokeWidth = 2.0f;
        this.chartStrokeWidth = 4.0f;
        this.pointerStrokeWidth = 2.0f;
        this.pointerDotRadius = 5.0f;
        this.tickSize = 15.0f;
        this.axisLabelTextSize = 18.0f;
        this.axisTitleTextSize = 18.0f;
        this.axisMargin = 32.0f;
        this.thumbWidth = 50;
        this.thumbHeight = 38;
        this.prevScrollerX = 0;
        this.scale = 1.0f;
        this.graphScale = 1.0f;
        init(attributeSet, i);
    }

    private void applyStyle(Series series) {
        series.setStrokeWidth(this.chartStrokeWidth);
    }

    private double distance(PointF pointF, PointF pointF2) {
        return Math.sqrt(Math.pow((double) (pointF.x - pointF2.x), 2.0d) + Math.pow((double) (pointF.y - pointF2.y), 2.0d));
    }

    private void drawAxes(Canvas canvas) {
        for (int i = 0; i < this.axes.size(); i++) {
            ((Axis) this.axes.get(i)).draw(canvas);
        }
    }

    private void drawCharts(Canvas canvas, int i, int i2) {
        if (!this.scroller.isFinished()) {
            this.scroller.computeScrollOffset();
            int currX = this.scroller.getCurrX();
            int i3 = this.prevScrollerX;
            this.prevScrollerX = this.scroller.getCurrX();
            setGraphScrollByX((float) (currX - i3));
        }
        for (i3 = 0; i3 < this.series.size(); i3++) {
            ((Series) this.series.get(i3)).draw(canvas);
        }
    }

    @SuppressLint({"NewApi"})
    private void init(AttributeSet attributeSet, int i) {
        if (VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
        this.graphArea = new Rect();
        this.graphClipRect = new Rect();
        this.series = new ArrayList();
        this.axes = new ArrayList();
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, C0984R.styleable.GraphView, i, C0984R.style.GraphStyle);
        this.seriesLineColor = obtainStyledAttributes.getColor(1, this.seriesLineColor);
        this.seriesFillColor = obtainStyledAttributes.getColor(2, this.seriesFillColor);
        this.gridLineColor = obtainStyledAttributes.getColor(3, this.gridLineColor);
        this.axesColor = obtainStyledAttributes.getColor(4, this.axesColor);
        this.pointerColor = obtainStyledAttributes.getColor(5, this.pointerColor);
        this.pointerSize = obtainStyledAttributes.getDimension(6, this.pointerSize);
        this.axisStrokeWidth = obtainStyledAttributes.getDimension(7, this.axisStrokeWidth);
        this.zeroLevelStrokeWidth = obtainStyledAttributes.getDimension(8, this.zeroLevelStrokeWidth);
        this.chartStrokeWidth = obtainStyledAttributes.getDimension(9, this.chartStrokeWidth);
        this.pointerStrokeWidth = obtainStyledAttributes.getDimension(10, this.pointerStrokeWidth);
        this.pointerDotRadius = obtainStyledAttributes.getDimension(12, this.pointerDotRadius);
        this.tickSize = obtainStyledAttributes.getDimension(13, this.tickSize);
        this.axisLabelTextSize = obtainStyledAttributes.getDimension(14, this.axisLabelTextSize);
        this.axisTitleTextSize = obtainStyledAttributes.getDimension(15, this.axisTitleTextSize);
        this.axisMargin = obtainStyledAttributes.getDimension(16, this.axisMargin);
        this.thumbWidth = obtainStyledAttributes.getDimensionPixelSize(17, this.thumbWidth);
        this.thumbHeight = obtainStyledAttributes.getDimensionPixelSize(18, this.thumbHeight);
        obtainStyledAttributes.recycle();
        if (this.padding == null) {
            this.padding = new Rect(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        if (isInEditMode()) {
            float[] fArr = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 417.0f, 1115.0f, 1443.0f, 1402.0f, 1394.0f, 1378.0f, 1393.0f, 1381.0f, 1420.0f, 1217.0f, 1168.0f, 1194.0f, 1222.0f, 1209.0f, 1258.0f, 1230.0f, 1249.0f, 1215.0f, 1454.0f, 1480.0f, 1493.0f, 1509.0f, 1478.0f, 1512.0f, 1212.0f, 470.0f, 165.0f, 165.0f};
            int length = fArr.length;
            List arrayList = new ArrayList(length);
            for (int i2 = 0; i2 < length; i2++) {
                arrayList.add(new Pair(Long.valueOf(new long[]{1356551655000L, 1356551657000L, 1356551658000L, 1356551659000L, 1356551660000L, 1356551661000L, 1356551662000L, 1356551663000L, 1356551664000L, 1356551665000L, 1356551666000L, 1356551667000L, 1356551668000L, 1356551669000L, 1356551670000L, 1356551671000L, 1356551672000L, 1356551673000L, 1356551675000L, 1356551676000L, 1356551677000L, 1356551678000L, 1356551679000L, 1356551680000L, 1356551681000L, 1356551682000L, 1356551683000L, 1356551684000L, 1356551685000L, 1356551686000L, 1356551687000L, 1356551688000L, 1356551689000L}[i2]), Double.valueOf((double) fArr[i2])));
            }
            Series series = new Series(this);
            series.setSortedPairs(arrayList);
            series.setVisible(true);
            addSeries(series);
            Pointer pointer = new Pointer(this, series);
            setPointer(pointer);
            addAxis(new Axis(this, series));
            Axis axis = new Axis(this, series);
            axis.setAlignment(Align.BOTTOM);
            addAxis(axis);
            pointer.moveTo(1356551661000L);
        }
        setOnTouchListener(this);
        this.finger0 = new PointF();
        this.finger1 = new PointF();
        this.pivotPoint = new PointF();
        this.mode = Mode.NORMAL;
        this.zoomEnabled = false;
        setClickable(true);
        this.gestureDetector = new GestureDetector(getContext(), this);
        this.scroller = new Scroller(getContext());
    }

    private void setGraphScale(float f, PointF pointF, boolean z) {
        int size = this.series.size();
        for (int i = 0; i < size; i++) {
            Series series = (Series) this.series.get(i);
            if (z) {
                series.commitScale(f, pointF);
            } else {
                series.tryScale(f, pointF);
            }
        }
        invalidate();
    }

    private void setGraphScrollByX(float f) {
        int size = this.series.size();
        for (int i = 0; i < size; i++) {
            ((Series) this.series.get(i)).scrollBy(f, 0.0f);
        }
        invalidate();
    }

    private void updateAxes() {
        for (int i = 0; i < this.axes.size(); i++) {
            ((Axis) this.axes.get(i)).updateTicks();
        }
    }

    public void addAxis(Axis axis) {
        if (!this.axes.contains(axis)) {
            this.axes.add(axis);
        }
    }

    public void addSeries(Series series) {
        this.series.add(series);
        applyStyle(series);
        invalidate();
    }

    public void deleteSeries(Series series) {
        this.series.remove(series);
        invalidate();
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int i = this.padding.left;
        int i2 = this.padding.right;
        int height = getHeight();
        int i3 = this.padding.top;
        int i4 = this.padding.bottom;
        canvas.save();
        canvas.scale(1.0f, GroundOverlayOptions.NO_DIMENSION, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
        canvas.clipRect(0, 0, getWidth(), getHeight());
        drawAxes(canvas);
        canvas.scale(1.0f, GroundOverlayOptions.NO_DIMENSION, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
        canvas.clipRect(this.graphClipRect);
        canvas.scale(1.0f, GroundOverlayOptions.NO_DIMENSION, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
        drawCharts(canvas, (width - i) - i2, (height - i3) - i4);
        if (this.mediaOverlay != null) {
            this.mediaOverlay.draw(canvas);
        }
        if (this.pointer != null) {
            this.pointer.draw(canvas);
        }
        canvas.restore();
    }

    @SuppressLint({"NewApi"})
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.scroller.forceFinished(true);
        this.prevScrollerX = 0;
        this.scroller.fling(0, 0, (int) f, (int) f2, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        if (VERSION.SDK_INT >= 11) {
            this.scroller.setFriction(ViewConfiguration.getScrollFriction());
        }
        invalidate();
        return true;
    }

    @SuppressLint({"WrongCall"})
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.graphArea.set((int) this.axisStrokeWidth, 0, (int) (((float) getMeasuredWidth()) - this.axisStrokeWidth), getMeasuredHeight());
        Axis axis = null;
        Axis axis2 = null;
        int i5 = 0;
        int i6 = 0;
        Object obj = null;
        while (i5 < this.axes.size()) {
            Axis axis3;
            int i7;
            Object obj2;
            Axis axis4 = (Axis) this.axes.get(i5);
            if (axis4.isVisible()) {
                Align alignment = axis4.getAlignment();
                Rect rect;
                if (alignment == Align.LEFT) {
                    i6++;
                    if (i6 > 1) {
                        rect = this.graphArea;
                        rect.left = (int) (((float) rect.left) + this.axisMargin);
                        axis4.setMarginLeft((int) (((float) this.graphArea.left) - this.axisStrokeWidth));
                        axis4.onLayout(z, i, i2, i3, i4);
                    }
                    if (i6 == 1) {
                        axis4.setMarginLeft(0);
                        axis4.onLayout(z, i, i2, i3, i4);
                        this.graphArea.top = axis4.height - axis4.getAxisHeight();
                        obj = 1;
                        axis2 = axis4;
                    }
                    rect = this.graphArea;
                    rect.left = axis4.getAxisWidth() + rect.left;
                    axis3 = axis2;
                    axis4 = axis;
                    i7 = i6;
                    obj2 = obj;
                } else if (alignment == Align.BOTTOM) {
                    axis4.onLayout(z, i, i2, i3, i4);
                    rect = this.graphArea;
                    rect.bottom -= axis4.getAxisHeight();
                    axis3 = axis2;
                    i7 = i6;
                    obj2 = obj;
                } else {
                    if (alignment == Align.RIGHT) {
                        axis4.onLayout(z, i, i2, i3, i4);
                        rect = this.graphArea;
                        rect.right -= axis4.getAxisWidth();
                        if (obj == null) {
                            this.graphArea.top = axis4.height - axis4.getAxisHeight();
                            axis3 = axis2;
                            axis4 = axis;
                            i7 = i6;
                            int i8 = 1;
                        }
                    }
                    axis3 = axis2;
                    axis4 = axis;
                    i7 = i6;
                    obj2 = obj;
                }
            } else {
                axis3 = axis2;
                axis4 = axis;
                i7 = i6;
                obj2 = obj;
            }
            axis = axis4;
            axis2 = axis3;
            i5++;
            i6 = i7;
            obj = obj2;
        }
        this.graphClipRect.set((int) (((float) this.graphArea.left) - this.chartStrokeWidth), (int) (((float) this.graphArea.top) - this.chartStrokeWidth), (int) (((float) this.graphArea.right) + this.chartStrokeWidth), (int) (((float) this.graphArea.bottom) + this.chartStrokeWidth));
        if (axis != null) {
            if (i6 > 0) {
                axis.setMarginLeft(axis2.getAxisWidth());
            } else {
                axis.setMarginLeft(0);
            }
        }
        for (int i9 = 0; i9 < this.series.size(); i9++) {
            ((Series) this.series.get(i9)).onLayout(z, i, i2, i3, i4);
        }
        if (this.mediaOverlay != null) {
            this.mediaOverlay.onLayout(z, i6, i2, i3, i4);
        }
        super.onLayout(z, i, i2, i3, i4);
        updateAxes();
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    @SuppressLint({"WrongCall"})
    protected void onMeasure(int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < this.series.size(); i4++) {
            ((Series) this.series.get(i4)).onMeasure(i, i2);
        }
        while (i3 < this.axes.size()) {
            ((Axis) this.axes.get(i3)).onMeasure(i, i2);
            i3++;
        }
        if (this.pointer != null) {
            this.pointer.onMeasure(i, i2);
        }
        if (this.mediaOverlay != null) {
            this.mediaOverlay.onMeasure(i, i2);
        }
        super.onMeasure(i, i2);
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mode != Mode.NORMAL) {
            return false;
        }
        setGraphScrollByX(-f);
        return true;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return this.mediaOverlay != null ? this.mediaOverlay.onSingleTapUp(motionEvent) : false;
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5 = 0;
        super.onSizeChanged(i, i2, i3, i4);
        for (int i6 = 0; i6 < this.series.size(); i6++) {
            ((Series) this.series.get(i6)).onSizeChanged(i, i2, i3, i4);
        }
        if (this.pointer != null) {
            this.pointer.onSizeChanged(i, i2, i3, i4);
        }
        while (i5 < this.axes.size()) {
            ((Axis) this.axes.get(i5)).onSizeChanged(i, i2, i3, i4);
            i5++;
        }
        if (this.mediaOverlay != null) {
            this.mediaOverlay.onSizeChanged(i, i2, i3, i4);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.mediaOverlay != null && this.mediaOverlay.onTouch(view, motionEvent)) {
            return true;
        }
        if (!this.zoomEnabled) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (this.gestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        switch (actionMasked) {
            case 2:
                this.finger0.x = motionEvent.getX(0);
                this.finger0.y = motionEvent.getY(0);
                if (this.mode != Mode.ZOOMING) {
                    return true;
                }
                this.finger1.x = motionEvent.getX(1);
                this.finger1.y = motionEvent.getY(1);
                double distance = distance(this.finger0, this.finger1);
                if (this.startDistance != 0.0d) {
                    this.scale = (float) (distance / this.startDistance);
                }
                setGraphScale(this.scale * this.graphScale, this.pivotPoint, false);
                return true;
            case 5:
                if (!this.scroller.isFinished()) {
                    this.scroller.forceFinished(true);
                }
                this.finger0.x = motionEvent.getX(0);
                this.finger0.y = motionEvent.getY(0);
                this.finger1.x = motionEvent.getX(1);
                this.finger1.y = motionEvent.getY(1);
                this.pivotPoint.x = this.finger0.x + ((this.finger1.x - this.finger0.x) / 2.0f);
                this.pivotPoint.y = this.finger0.y + ((this.finger1.y - this.finger0.y) / 2.0f);
                this.startDistance = distance(this.finger0, this.finger1);
                this.mode = Mode.ZOOMING;
                this.graphScale = 1.0f;
                return true;
            case 6:
                this.graphScale = this.scale * this.graphScale;
                this.mode = Mode.NORMAL;
                setGraphScale(this.graphScale, this.pivotPoint, true);
                updateAxes();
                return true;
            default:
                return false;
        }
    }

    public void setGraphArea(Rect rect) {
        this.graphArea = rect;
    }

    public void setMediaOverlay(MediaThumbsOverlay mediaThumbsOverlay) {
        this.mediaOverlay = mediaThumbsOverlay;
        requestLayout();
        invalidate();
    }

    public void setPointer(Pointer pointer) {
        this.pointer = pointer;
    }

    public void setZoomEnabled(boolean z) {
        this.zoomEnabled = z;
    }
}
