package com.parrot.freeflight.ui.widgets.graph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.util.LongSparseArray;
import android.view.View.MeasureSpec;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.util.Locale;
import org.mortbay.jetty.HttpVersions;

public class Axis extends GraphElement {
    private Align align;
    private Paint axisPaint = new Paint();
    private Rect axisRect = new Rect();
    private float axisStrokeWidth;
    private Paint labelBgPaint;
    private AxisLabelFormatter labelFormatter;
    private float labelHeight;
    private float labelPaddingRight;
    private float labelPaddingTop;
    private Paint labelPaint;
    private float labelWidth;
    private Rect margin = new Rect();
    private double offset;
    private GraphView parent;
    private Series series;
    private boolean showLabels;
    private int tickCount;
    private int tickCountForScaled;
    private float tickHeight;
    private Paint tickPaint;
    private TickStyle tickStyle;
    private String title;
    private Rect titleBounds = new Rect();
    private Paint titlePaint;
    private PointF titlePos = new PointF();
    private Paint zeroLevelPaint;

    public interface AxisLabelFormatter {
        String onFormat(Series series, double d);

        String onFormat(Series series, long j);
    }

    public enum Align {
        LEFT,
        RIGHT,
        BOTTOM
    }

    public static class DefaultAxisLabelFormatter implements AxisLabelFormatter {
        private static final int CACHE_SIZE = 40;
        private LongSparseArray<String> xValueCache = new LongSparseArray(40);
        private LongSparseArray<String> yValueCache = new LongSparseArray(40);

        public String onFormat(Series series, double d) {
            double d2 = ((double) ((float) d)) / 1000.0d;
            String str = (String) this.yValueCache.get((long) d);
            if (str == null) {
                if (d2 > series.getMinY()) {
                    str = String.format(Locale.ENGLISH, "%.1f", new Object[]{Double.valueOf(d2)});
                } else {
                    str = HttpVersions.HTTP_0_9;
                }
                this.yValueCache.put((long) d, str);
            }
            return str;
        }

        public String onFormat(Series series, long j) {
            long minX = (long) (10.0d * (((double) ((j - series.getMinX()) / 100)) / 10.0d));
            String str = (String) this.xValueCache.get(j);
            if (str == null) {
                if (minX % 10 == 0) {
                    str = String.format(Locale.ENGLISH, "%d", new Object[]{Long.valueOf((long) r2)});
                } else {
                    str = String.format(Locale.ENGLISH, "%.1f", new Object[]{Double.valueOf(r2)});
                }
                this.xValueCache.put(j, str);
            }
            return str;
        }
    }

    public enum TickStyle {
        NONE,
        TICK,
        LINES
    }

    public Axis(GraphView graphView, Series series) {
        this.parent = graphView;
        this.series = series;
        this.axisPaint.setColor(graphView.axesColor);
        this.axisPaint.setStrokeWidth(graphView.chartStrokeWidth);
        this.axisPaint.setStyle(Style.STROKE);
        this.tickPaint = new Paint(this.axisPaint);
        this.tickPaint.setStrokeWidth(1.0f);
        this.titlePaint = new Paint();
        this.titlePaint.setTextSize(graphView.axisTitleTextSize);
        this.titlePaint.setAntiAlias(true);
        this.titlePaint.setStyle(Style.FILL_AND_STROKE);
        this.titlePaint.setColor(series.getColor());
        this.titlePaint.setTextAlign(android.graphics.Paint.Align.CENTER);
        this.labelBgPaint = new Paint(this.titlePaint);
        this.labelBgPaint.setColor(graphView.axesColor);
        this.labelBgPaint.setTextSize(graphView.axisLabelTextSize);
        this.labelPaint = new Paint(this.titlePaint);
        this.labelPaint.setTextAlign(android.graphics.Paint.Align.LEFT);
        this.labelPaint.setStrokeWidth(this.labelBgPaint.getStrokeWidth() + 1.0f);
        this.labelPaint.setColor(series.getColor());
        this.labelPaint.setAlpha(150);
        this.zeroLevelPaint = new Paint();
        this.zeroLevelPaint.setColor(series.getColor());
        this.zeroLevelPaint.setStrokeWidth(graphView.zeroLevelStrokeWidth);
        this.align = Align.LEFT;
        this.tickStyle = TickStyle.LINES;
        this.tickHeight = graphView.tickSize;
        this.tickCount = 5;
        this.tickCountForScaled = 5;
        this.offset = series.getMinY() < 0.0d ? Math.abs(series.getMinY()) : series.getMinY();
        this.labelFormatter = new DefaultAxisLabelFormatter();
    }

    private void drawHorizontalLinesAndLabels(Canvas canvas) {
        if (this.tickStyle != TickStyle.NONE) {
            double graphIntervalWithVal = (double) getGraphIntervalWithVal((float) (this.series.getMaxY() - this.series.getMinY()), this.tickCount);
            double minY = this.series.getMinY() + (this.offset % graphIntervalWithVal);
            double maxY = this.series.getMaxY();
            double d = minY;
            while (d < maxY) {
                float width = (float) ((this.align == Align.LEFT ? this.axisRect.width() : this.width) + this.margin.left);
                float f = (float) (this.margin.left + this.width);
                float minY2 = (float) (((d - this.series.getMinY()) * this.series.getScaleY()) + ((double) (this.series.height - this.series.getDrawArea().bottom)));
                if (this.tickStyle == TickStyle.LINES) {
                    canvas.drawLine(width, minY2, f, minY2, this.tickPaint);
                } else if (this.align == Align.LEFT) {
                    canvas.drawLine(width, minY2, width + this.tickHeight, minY2, this.tickPaint);
                } else {
                    canvas.drawLine((float) this.axisRect.left, minY2, ((float) this.axisRect.left) - this.tickHeight, minY2, this.tickPaint);
                }
                if (this.showLabels && this.labelFormatter.onFormat(this.series, d) != null) {
                    canvas.save(1);
                    canvas.scale(1.0f, GroundOverlayOptions.NO_DIMENSION, width, minY2);
                    if (this.align == Align.LEFT) {
                        canvas.drawText(this.labelFormatter.onFormat(this.series, d), width - this.labelPaddingRight, this.labelPaddingTop + minY2, this.labelBgPaint);
                        canvas.drawText(this.labelFormatter.onFormat(this.series, d), width - this.labelPaddingRight, this.labelPaddingTop + minY2, this.labelPaint);
                    }
                    if (this.align == Align.RIGHT) {
                        canvas.drawText(this.labelFormatter.onFormat(this.series, d), ((float) this.axisRect.left) + this.labelPaddingRight, this.labelPaddingTop + minY2, this.labelBgPaint);
                        canvas.drawText(this.labelFormatter.onFormat(this.series, d), ((float) this.axisRect.left) + this.labelPaddingRight, minY2 + this.labelPaddingTop, this.labelPaint);
                    }
                    canvas.restore();
                }
                d += graphIntervalWithVal;
            }
        }
    }

    private void drawVerticalLinesAndLabels(Canvas canvas) {
        if (this.tickStyle != TickStyle.NONE) {
            canvas.save(2);
            canvas.clipRect(this.parent.graphArea.left, this.height, this.parent.graphArea.right, 0);
            long minX = this.series.getMinX();
            long maxX = this.series.getMaxX();
            double scaleX = this.series.getScaleX();
            Rect drawArea = this.series.getDrawArea();
            long graphIntervalWithVal = (long) (getGraphIntervalWithVal((float) ((maxX - minX) / 1000), this.tickCountForScaled) * 1000.0f);
            for (long j = minX; j < maxX; j += graphIntervalWithVal) {
                float scrollX = (float) (((((double) (j - minX)) * scaleX) + ((double) drawArea.left)) + ((double) this.series.getScrollX()));
                float f = (float) (this.series.height - drawArea.bottom);
                float f2 = (float) this.height;
                if (this.tickStyle == TickStyle.LINES) {
                    canvas.drawLine(scrollX, f, scrollX, f2, this.tickPaint);
                } else {
                    canvas.drawLine(scrollX, f, scrollX, f + this.tickHeight, this.tickPaint);
                }
                if (this.showLabels && canvas.getClipBounds().intersect((int) scrollX, (int) f, ((int) scrollX) + 1, ((int) f) + 1)) {
                    String onFormat = this.labelFormatter.onFormat(this.series, j);
                    if (onFormat != null) {
                        canvas.save(1);
                        canvas.scale(1.0f, GroundOverlayOptions.NO_DIMENSION, scrollX, f);
                        if (j - minX == 0) {
                            this.labelBgPaint.setTextAlign(android.graphics.Paint.Align.LEFT);
                        } else {
                            this.labelBgPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
                        }
                        canvas.drawText(onFormat, scrollX, (f + this.labelHeight) + this.labelPaddingTop, this.labelBgPaint);
                        canvas.restore();
                    }
                }
            }
            canvas.restore();
        }
    }

    private void drawZeroLevel(Canvas canvas) {
        float scaleY = (float) ((this.offset * this.series.getScaleY()) + ((double) (this.series.height - this.series.getDrawArea().bottom)));
        Canvas canvas2 = canvas;
        canvas2.drawLine((float) this.parent.graphArea.left, scaleY, (float) this.parent.graphArea.right, scaleY, this.zeroLevelPaint);
    }

    private float getGraphIntervalWithVal(float f, int i) {
        int floor = (int) Math.floor((double) (f / ((float) i)));
        if (floor >= 1 && floor < 2) {
            return 1.0f;
        }
        if (floor >= 2 && floor < 5) {
            return 2.0f;
        }
        if (floor >= 5 && floor < 10) {
            return 5.0f;
        }
        if (floor < 10) {
            return 0.5f;
        }
        int i2 = floor;
        while (i2 % ((int) 10.0f) != 0) {
            i2--;
        }
        return (float) i2;
    }

    public void draw(Canvas canvas) {
        if (this.visible) {
            switch (this.align) {
                case LEFT:
                    canvas.drawLine((float) (this.margin.left + this.axisRect.width()), (float) ((this.axisRect.bottom + this.margin.bottom) + (this.parent.getHeight() - this.parent.graphArea.bottom)), (float) (this.margin.left + this.axisRect.width()), (float) this.axisRect.top, this.axisPaint);
                    drawHorizontalLinesAndLabels(canvas);
                    drawZeroLevel(canvas);
                    break;
                case RIGHT:
                    canvas.drawLine((float) this.axisRect.left, (float) ((this.axisRect.bottom + this.margin.bottom) + (this.parent.getHeight() - this.parent.graphArea.bottom)), (float) this.axisRect.left, (float) this.axisRect.top, this.axisPaint);
                    drawHorizontalLinesAndLabels(canvas);
                    break;
                case BOTTOM:
                    int i = ((int) this.axisStrokeWidth) / 2;
                    int i2 = (int) this.axisStrokeWidth;
                    Canvas canvas2 = canvas;
                    canvas2.drawLine((float) ((this.axisRect.left + this.margin.left) - i), (float) this.axisRect.top, (float) ((this.axisRect.right - (this.parent.getWidth() - this.parent.graphArea.right)) + i2), (float) this.axisRect.top, this.axisPaint);
                    drawVerticalLinesAndLabels(canvas);
                    break;
            }
            if (this.title != null) {
                canvas.save(1);
                canvas.scale(1.0f, GroundOverlayOptions.NO_DIMENSION, this.titlePos.x, this.titlePos.y);
                canvas.drawText(this.title, this.titlePos.x, this.titlePos.y, this.titlePaint);
                canvas.restore();
            }
        }
    }

    public Align getAlignment() {
        return this.align;
    }

    public int getAxisHeight() {
        return Math.abs(this.axisRect.height());
    }

    public int getAxisWidth() {
        return Math.abs(this.axisRect.width());
    }

    public int getTickCount() {
        return this.tickCount;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        Rect rect;
        switch (this.align) {
            case LEFT:
                if (this.title != null) {
                    rect = this.axisRect;
                    rect.top = (int) (((float) rect.top) - (this.titlePaint.getTextSize() * 1.4f));
                }
                if (this.showLabels) {
                    rect = this.axisRect;
                    rect.right = (int) (((double) rect.right) + (((double) this.labelWidth) * 1.4d));
                }
                this.titlePos.x = (float) (this.axisRect.right + this.margin.left);
                this.titlePos.y = ((float) this.axisRect.top) + (this.titlePaint.getTextSize() * 0.4f);
                if (this.showLabels) {
                    this.labelPaint.setTextAlign(android.graphics.Paint.Align.RIGHT);
                    this.labelBgPaint.setTextAlign(android.graphics.Paint.Align.RIGHT);
                    this.labelPaddingRight = this.labelHeight * 0.4f;
                    this.labelPaddingTop = this.labelHeight / 2.0f;
                    break;
                }
                break;
            case RIGHT:
                if (this.title != null) {
                    rect = this.axisRect;
                    rect.top = (int) (((float) rect.top) - (this.titlePaint.getTextSize() * 1.4f));
                }
                if (this.showLabels) {
                    rect = this.axisRect;
                    rect.left = (int) (((float) rect.left) - (this.labelWidth * 1.4f));
                }
                this.titlePos.x = (float) (this.width - this.axisRect.width());
                this.titlePos.y = ((float) this.axisRect.top) + (this.titlePaint.getTextSize() * 0.4f);
                if (this.showLabels) {
                    this.labelPaint.setTextAlign(android.graphics.Paint.Align.LEFT);
                    this.labelBgPaint.setTextAlign(android.graphics.Paint.Align.LEFT);
                    this.labelPaddingRight = this.labelHeight * 0.4f;
                    this.labelPaddingTop = this.labelHeight / 2.0f;
                    break;
                }
                break;
            case BOTTOM:
                if (this.title != null) {
                    rect = this.axisRect;
                    rect.top = (int) (((float) rect.top) + (((float) this.titleBounds.height()) * 1.2f));
                }
                this.titlePos.x = (((float) getAxisWidth()) / 2.0f) + (((float) this.titleBounds.width()) / 2.0f);
                this.titlePos.y = (float) this.axisRect.bottom;
                if (this.showLabels) {
                    this.labelPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
                    this.labelBgPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
                    this.labelPaddingTop = this.labelHeight * 0.2f;
                    break;
                }
                break;
        }
        updateTicks();
    }

    public void onMeasure(int i, int i2) {
        if (this.title != null) {
            this.titlePaint.getTextBounds(this.title, 0, this.title.length(), this.titleBounds);
        }
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        if (this.showLabels) {
            String onFormat = this.labelFormatter.onFormat(this.series, this.series.getMaxX());
            String onFormat2 = this.labelFormatter.onFormat(this.series, this.series.getMaxY());
            String onFormat3 = this.labelFormatter.onFormat(this.series, this.series.getMinX());
            String onFormat4 = this.labelFormatter.onFormat(this.series, this.series.getMinY());
            if (this.align == Align.BOTTOM) {
                this.labelWidth = Math.max(this.labelPaint.measureText(onFormat), this.labelPaint.measureText(onFormat3));
            } else {
                this.labelWidth = Math.max(this.labelPaint.measureText(onFormat2), this.labelPaint.measureText(onFormat4));
            }
            this.labelHeight = this.titlePaint.getTextSize();
        }
        this.axisStrokeWidth = this.axisPaint.getStrokeWidth();
        switch (this.align) {
            case LEFT:
                this.axisRect.set(0, MeasureSpec.getSize(i2), (int) this.axisStrokeWidth, 0);
                break;
            case RIGHT:
                this.axisRect.set((int) (((float) size) - this.axisStrokeWidth), MeasureSpec.getSize(i2), size, 0);
                break;
            case BOTTOM:
                this.axisRect.set(0, (int) ((this.showLabels ? this.labelHeight : 0.0f) + this.axisStrokeWidth), size, 0);
                break;
        }
        setSize(size, size2);
    }

    public void setAlignment(Align align) {
        this.align = align;
        if (align == Align.BOTTOM) {
            this.titlePaint.setColor(this.labelBgPaint.getColor());
        }
    }

    public void setAxisLabelFormatter(AxisLabelFormatter axisLabelFormatter) {
        if (axisLabelFormatter == null) {
            throw new IllegalArgumentException();
        }
        this.labelFormatter = axisLabelFormatter;
    }

    public void setMarginBottom(int i) {
        this.margin.bottom = i;
    }

    public void setMarginLeft(int i) {
        this.margin.left = i;
    }

    public void setTickCount(int i) {
        this.tickCount = i;
        this.tickCountForScaled = i;
        this.parent.invalidate();
    }

    public void setTickStyle(TickStyle tickStyle) {
        this.tickStyle = tickStyle;
        this.parent.invalidate();
    }

    public void setTitle(String str) {
        this.title = str;
        this.parent.requestLayout();
    }

    public void setVisible(boolean z) {
        this.visible = z;
        this.parent.requestLayout();
        this.parent.invalidate();
    }

    public void showLabels(boolean z) {
        this.showLabels = z;
        this.parent.invalidate();
    }

    public void updateTicks() {
        this.tickCountForScaled = (int) ((this.series.getBounds().width() / ((float) this.parent.graphArea.width())) * ((float) this.tickCount));
    }
}
