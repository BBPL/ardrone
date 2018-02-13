package com.parrot.freeflight.ui.widgets.graph;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;
import android.view.View.MeasureSpec;
import java.util.ArrayList;
import java.util.List;

public class Series extends GraphElement {
    private static final String TAG = Series.class.getSimpleName();
    private Paint chartFillPaint;
    private Paint chartStrokePaint;
    private long[] distances;
    private boolean fillEnabled;
    private boolean forceInvalidate;
    private GraphView parent;
    private RectF pathBounds = new RectF();
    private RectF pathTempBounds = new RectF();
    private Path seriesFillPath;
    private Path seriesFillPathOrig;
    private Path seriesStrokePath;
    private Path seriesStrokePathOrig;
    private List<Pair<Long, Double>> sortedPairs;
    private Matrix transformPathMatrix;
    private double xCoef;
    private Long xMaxValue;
    private Long xMinValue;
    private double yCoef;
    private Double yMaxValue;
    private Double yMinValue;

    public Series(GraphView graphView) {
        this.parent = graphView;
        this.sortedPairs = new ArrayList();
        this.chartStrokePaint = new Paint();
        this.chartStrokePaint.setAntiAlias(true);
        this.chartStrokePaint.setColor(graphView.seriesLineColor);
        this.chartStrokePaint.setStyle(Style.STROKE);
        this.chartStrokePaint.setStrokeCap(Cap.SQUARE);
        this.chartStrokePaint.setStrokeWidth(graphView.chartStrokeWidth);
        this.chartFillPaint = new Paint();
        this.chartFillPaint.setColor(graphView.seriesFillColor);
        this.chartFillPaint.setStyle(Style.FILL_AND_STROKE);
        this.chartFillPaint.setStrokeCap(Cap.SQUARE);
        this.chartFillPaint.setStrokeWidth(graphView.chartStrokeWidth);
        this.fillEnabled = true;
        this.visible = true;
        this.forceInvalidate = false;
        this.transformPathMatrix = new Matrix();
    }

    private void calculateDataRange() {
        int size = this.sortedPairs.size();
        if (size > 0) {
            Long l = null;
            Double d = null;
            int i = 0;
            Double d2 = null;
            Long l2 = null;
            while (i < size) {
                Pair pair = (Pair) this.sortedPairs.get(i);
                Double valueOf = d2 == null ? (Double) pair.second : Double.valueOf(Math.min(((Double) pair.second).doubleValue(), d2.doubleValue()));
                d2 = d == null ? (Double) pair.second : Double.valueOf(Math.max(((Double) pair.second).doubleValue(), d.doubleValue()));
                Long valueOf2 = l == null ? (Long) pair.first : Long.valueOf(Math.min(((Long) pair.first).longValue(), l.longValue()));
                l = valueOf2;
                d = d2;
                i++;
                d2 = valueOf;
                l2 = l2 == null ? (Long) pair.first : Long.valueOf(Math.max(((Long) pair.first).longValue(), valueOf2.longValue()));
            }
            Double valueOf3 = d2.doubleValue() > 0.0d ? Double.valueOf(0.0d) : d2;
            this.xMaxValue = l2;
            this.xMinValue = l;
            this.yMaxValue = d;
            this.yMinValue = valueOf3;
        }
    }

    private void fixPosition() {
        float f;
        this.seriesStrokePath.computeBounds(this.pathBounds, false);
        if (this.pathBounds.left - ((float) this.parent.graphArea.left) > 0.0f) {
            f = -(this.pathBounds.left - ((float) this.parent.graphArea.left));
            Log.d("Series", "Fixing pos: " + f);
        } else {
            f = 0.0f;
        }
        scrollBy(f, 0.0f);
    }

    private void generateGraphPaths() {
        if (this.sortedPairs != null && this.sortedPairs.size() != 0 && this.parent.graphArea != null) {
            int size = this.sortedPairs.size();
            if (this.seriesStrokePathOrig == null || this.seriesFillPathOrig == null) {
                this.seriesStrokePath = new Path();
                this.seriesFillPath = new Path();
                this.seriesStrokePathOrig = new Path();
                this.seriesFillPathOrig = new Path();
            } else {
                this.seriesStrokePath.reset();
                this.seriesFillPath.reset();
                this.seriesStrokePathOrig.reset();
                this.seriesStrokePathOrig.reset();
            }
            float f = (float) (this.height - this.parent.graphArea.bottom);
            this.seriesFillPathOrig.moveTo((float) this.parent.graphArea.left, f);
            for (int i = 0; i < size; i++) {
                Pair pair = (Pair) this.sortedPairs.get(i);
                float longValue = ((float) (((double) (((Long) pair.first).longValue() - this.xMinValue.longValue())) * this.xCoef)) + ((float) this.parent.graphArea.left);
                float doubleValue = ((float) ((((Double) pair.second).doubleValue() - this.yMinValue.doubleValue()) * this.yCoef)) + f;
                if (i == 0) {
                    this.seriesStrokePathOrig.moveTo(longValue, doubleValue);
                } else {
                    this.seriesStrokePathOrig.lineTo(longValue, doubleValue);
                }
                this.seriesFillPathOrig.lineTo(longValue, doubleValue);
            }
            this.seriesFillPathOrig.lineTo(((float) (((double) (this.xMaxValue.longValue() - this.xMinValue.longValue())) * this.xCoef)) + ((float) this.parent.graphArea.left), (float) (this.height - this.parent.graphArea.bottom));
            this.seriesFillPathOrig.close();
            this.seriesFillPathOrig.transform(this.transformPathMatrix, this.seriesFillPath);
            this.seriesStrokePathOrig.transform(this.transformPathMatrix, this.seriesStrokePath);
            this.seriesStrokePathOrig.computeBounds(this.pathBounds, true);
        }
    }

    private void onScale(float f, PointF pointF, boolean z) {
        this.seriesStrokePathOrig.computeBounds(this.pathBounds, true);
        if (pointF != null) {
            this.transformPathMatrix.setScale(f, 1.0f, pointF.x, pointF.y);
        }
        this.transformPathMatrix.mapRect(this.pathTempBounds, this.pathBounds);
        if (f < 1.0f && this.pathTempBounds.width() < ((float) this.parent.graphArea.width())) {
            this.transformPathMatrix.setScale(((float) this.parent.graphArea.width()) / this.pathBounds.width(), 1.0f, pointF.x, pointF.y);
        }
        this.seriesFillPathOrig.transform(this.transformPathMatrix, this.seriesFillPath);
        this.seriesStrokePathOrig.transform(this.transformPathMatrix, this.seriesStrokePath);
        if (z) {
            this.seriesFillPathOrig.transform(this.transformPathMatrix);
            this.seriesStrokePathOrig.transform(this.transformPathMatrix);
        }
        this.seriesStrokePath.computeBounds(this.pathBounds, false);
        if (this.pathBounds.right < ((float) this.parent.graphArea.right)) {
            scrollBy(((float) this.parent.graphArea.right) - this.pathBounds.right, 0.0f);
        }
        if (this.pathBounds.left >= ((float) this.parent.graphArea.left)) {
            scrollBy(this.pathBounds.left - ((float) this.parent.graphArea.left), 0.0f);
        }
    }

    private void recalculateCoefs(int i, int i2) {
        this.xCoef = ((double) i) / ((double) (this.xMaxValue.longValue() - this.xMinValue.longValue()));
        this.yCoef = ((double) i2) / (this.yMaxValue.doubleValue() - this.yMinValue.doubleValue());
    }

    public void commitScale(float f, PointF pointF) {
        onScale(f, pointF, true);
    }

    public void draw(Canvas canvas) {
        if (this.visible) {
            if (this.fillEnabled) {
                canvas.drawPath(this.seriesFillPath, this.chartFillPaint);
            }
            canvas.drawPath(this.seriesStrokePath, this.chartStrokePaint);
        }
    }

    public RectF getBounds() {
        this.seriesStrokePath.computeBounds(this.pathBounds, false);
        return this.pathBounds;
    }

    public int getColor() {
        return this.chartStrokePaint.getColor();
    }

    public Rect getDrawArea() {
        return this.parent.graphArea;
    }

    public int getFillColor() {
        return this.chartFillPaint.getColor();
    }

    public int getHeight() {
        return this.height;
    }

    public long getMaxX() {
        return this.xMaxValue.longValue();
    }

    public double getMaxY() {
        return this.yMaxValue.doubleValue();
    }

    public long getMinX() {
        return this.xMinValue.longValue();
    }

    public double getMinY() {
        return this.yMinValue.doubleValue();
    }

    public double getScaleX() {
        return this.xCoef * ((double) (this.pathBounds.width() / ((float) getDrawArea().width())));
    }

    public double getScaleY() {
        return this.yCoef;
    }

    public float getScrollX() {
        return this.pathBounds.left - ((float) getDrawArea().left);
    }

    public List<Pair<Long, Double>> getSortedPairs() {
        return this.sortedPairs;
    }

    public int getWidth() {
        return this.width;
    }

    public double getYValue(long j) {
        if (j < this.xMinValue.longValue() || j > this.xMaxValue.longValue()) {
            Log.w(TAG, "getYValue: Position is out of range");
            return 0.0d;
        }
        int length = this.distances.length;
        int i = -1;
        long longValue = this.xMinValue.longValue();
        while (longValue <= j && i < length - 1) {
            int i2 = i + 1;
            longValue = this.distances[i2] + longValue;
            i = i2;
        }
        int i3 = i > 0 ? i - 1 : i;
        if (i3 + 1 >= this.sortedPairs.size()) {
            return ((Double) ((Pair) this.sortedPairs.get(i3)).second).doubleValue();
        }
        longValue = ((Long) ((Pair) this.sortedPairs.get(i3)).first).longValue();
        long longValue2 = ((Long) ((Pair) this.sortedPairs.get(i3 + 1)).first).longValue();
        double doubleValue = ((Double) ((Pair) this.sortedPairs.get(i3)).second).doubleValue();
        return (((((Double) ((Pair) this.sortedPairs.get(i3 + 1)).second).doubleValue() - doubleValue) * ((double) (j - longValue))) / ((double) (longValue2 - longValue))) + doubleValue;
    }

    public boolean isFillEnabled() {
        return this.fillEnabled;
    }

    public boolean isVisible() {
        return this.visible;
    }

    protected void onDataChanged() {
        calculateDataRange();
        recalculateCoefs(this.parent.graphArea.width(), this.parent.graphArea.height());
        generateGraphPaths();
        int size = this.sortedPairs.size();
        this.distances = new long[size];
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                this.distances[i] = ((Long) ((Pair) this.sortedPairs.get(i)).first).longValue() - this.xMinValue.longValue();
            } else {
                this.distances[i] = ((Long) ((Pair) this.sortedPairs.get(i)).first).longValue() - ((Long) ((Pair) this.sortedPairs.get(i - 1)).first).longValue();
            }
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        recalculateCoefs(this.parent.graphArea.width(), this.parent.graphArea.height());
        if (z || this.forceInvalidate) {
            if (this.forceInvalidate) {
                this.forceInvalidate = false;
            }
            generateGraphPaths();
            return;
        }
        fixPosition();
    }

    public void onMeasure(int i, int i2) {
        setSize(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
    }

    protected void onScrollBy(float f, float f2) {
        this.seriesStrokePath.computeBounds(this.pathBounds, false);
        if (f > 0.0f && ((float) this.parent.graphArea.left) - this.pathBounds.left <= f) {
            f = ((float) this.parent.graphArea.left) - this.pathBounds.left;
        } else if (f < 0.0f && ((float) this.parent.graphArea.right) - this.pathBounds.right >= f) {
            f = ((float) this.parent.graphArea.right) - this.pathBounds.right;
        }
        this.transformPathMatrix.setTranslate(f, f2);
        this.seriesFillPathOrig.transform(this.transformPathMatrix);
        this.seriesStrokePathOrig.transform(this.transformPathMatrix);
        this.seriesFillPath.transform(this.transformPathMatrix);
        this.seriesStrokePath.transform(this.transformPathMatrix);
        this.seriesStrokePath.computeBounds(this.pathBounds, false);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void scrollBy(float f, float f2) {
        onScrollBy(f, f2);
    }

    public void setColor(int i) {
        this.chartStrokePaint.setColor(i);
    }

    public void setFillColor(int i) {
        this.chartFillPaint.setColor(i);
    }

    public void setFillEnabled(boolean z) {
        this.fillEnabled = z;
    }

    public void setSortedPairs(List<Pair<Long, Double>> list) {
        this.sortedPairs = list;
        this.forceInvalidate = true;
        onDataChanged();
    }

    public void setStrokeWidth(float f) {
        this.chartStrokePaint.setStrokeWidth(f);
        this.chartFillPaint.setStrokeWidth(f);
    }

    public void setVisible(boolean z) {
        this.visible = z;
        this.parent.invalidate();
    }

    public void tryScale(float f, PointF pointF) {
        onScale(f, pointF, false);
    }
}
