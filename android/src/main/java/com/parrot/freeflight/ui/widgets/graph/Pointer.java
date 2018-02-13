package com.parrot.freeflight.ui.widgets.graph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View.MeasureSpec;

public class Pointer extends GraphElement {
    private float circleRadius;
    private GraphView parent;
    private float prevX;
    private Series series;
    private Paint strokePaint = new Paint();
    private float f350x;
    private float f351y;

    public Pointer(GraphView graphView, Series series) {
        this.parent = graphView;
        this.series = series;
        this.strokePaint.setColor(graphView.pointerColor);
        this.strokePaint.setStrokeWidth(graphView.pointerStrokeWidth);
        this.circleRadius = graphView.pointerDotRadius;
    }

    public void draw(Canvas canvas) {
        if (this.visible) {
            canvas.drawLine(this.f350x, (float) this.height, this.f350x, ((float) this.series.height) - this.series.getBounds().bottom, this.strokePaint);
            canvas.drawCircle(this.f350x, this.f351y, this.circleRadius, this.strokePaint);
        }
    }

    public void moveTo(long j) {
        this.prevX = this.f350x;
        this.f350x = (float) ((((double) (j - this.series.getMinX())) * this.series.getScaleX()) + ((double) this.series.getBounds().left));
        this.f351y = (float) (((this.series.getYValue(j) - this.series.getMinY()) * this.series.getScaleY()) + ((double) (this.series.height - this.series.getDrawArea().bottom)));
        this.parent.invalidate((int) (this.prevX - this.circleRadius), 0, (int) (this.prevX + this.circleRadius), this.height);
        this.parent.invalidate((int) (this.f350x - this.circleRadius), 0, (int) (this.f350x + this.circleRadius), this.height);
    }

    public void moveTo(Long l) {
        moveTo(l.longValue());
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void onMeasure(int i, int i2) {
        setSize(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
    }
}
