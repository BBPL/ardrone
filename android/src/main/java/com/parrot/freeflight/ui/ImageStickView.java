package com.parrot.freeflight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.common.primitives.Ints;
import com.parrot.freeflight.C0984R;

public class ImageStickView extends ViewGroup {
    private static final int DEF_VALUE = Integer.MIN_VALUE;
    private int baseViewId;

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        private int anchorBottom = ImageStickView.DEF_VALUE;
        private int anchorLeft = ImageStickView.DEF_VALUE;
        private int anchorRight = ImageStickView.DEF_VALUE;
        private int anchorTop = ImageStickView.DEF_VALUE;

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0984R.styleable.ImageStickView_Layout);
            this.anchorLeft = obtainStyledAttributes.getInteger(1, ImageStickView.DEF_VALUE);
            this.anchorTop = obtainStyledAttributes.getInteger(0, ImageStickView.DEF_VALUE);
            this.anchorRight = obtainStyledAttributes.getInteger(2, ImageStickView.DEF_VALUE);
            this.anchorBottom = obtainStyledAttributes.getInteger(3, ImageStickView.DEF_VALUE);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            if (layoutParams instanceof LayoutParams) {
                LayoutParams layoutParams2 = (LayoutParams) layoutParams;
                this.anchorTop = layoutParams2.anchorTop;
                this.anchorBottom = layoutParams2.anchorBottom;
                this.anchorLeft = layoutParams2.anchorLeft;
                this.anchorRight = layoutParams2.anchorRight;
            }
        }
    }

    public ImageStickView(Context context) {
        super(context);
        initView(context, null);
    }

    public ImageStickView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, attributeSet);
    }

    public ImageStickView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, attributeSet);
    }

    private void checkBaseView() {
        if (findViewById(this.baseViewId) == null) {
            throw new IllegalStateException("Base Image View musn't be null");
        }
    }

    private int getHeightMeasureSpecForChild(Matrix matrix, int i, LayoutParams layoutParams) {
        int mappedVertAnchor = layoutParams.anchorTop == DEF_VALUE ? 0 : getMappedVertAnchor(matrix, layoutParams.anchorTop);
        if (layoutParams.anchorBottom != DEF_VALUE) {
            i = getMappedVertAnchor(matrix, layoutParams.anchorBottom);
        }
        mappedVertAnchor = i - mappedVertAnchor;
        return layoutParams.width == -2 ? MeasureSpec.makeMeasureSpec(mappedVertAnchor, DEF_VALUE) : MeasureSpec.makeMeasureSpec(mappedVertAnchor, Ints.MAX_POWER_OF_TWO);
    }

    private int getMappedHorAnchor(Matrix matrix, int i) {
        float[] fArr = new float[]{(float) i, 0.0f};
        matrix.mapPoints(fArr);
        return (int) fArr[0];
    }

    private int getMappedVertAnchor(Matrix matrix, int i) {
        float[] fArr = new float[]{0.0f, (float) i};
        matrix.mapPoints(fArr);
        return (int) fArr[1];
    }

    private int getWidthMeasureSpecForChild(Matrix matrix, int i, LayoutParams layoutParams) {
        int mappedHorAnchor = layoutParams.anchorLeft == DEF_VALUE ? 0 : getMappedHorAnchor(matrix, layoutParams.anchorLeft);
        if (layoutParams.anchorRight != DEF_VALUE) {
            i = getMappedHorAnchor(matrix, layoutParams.anchorRight);
        }
        mappedHorAnchor = i - mappedHorAnchor;
        return layoutParams.width == -2 ? MeasureSpec.makeMeasureSpec(mappedHorAnchor, DEF_VALUE) : MeasureSpec.makeMeasureSpec(mappedHorAnchor, Ints.MAX_POWER_OF_TWO);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0984R.styleable.ImageStickView);
            this.baseViewId = obtainStyledAttributes.getResourceId(0, DEF_VALUE);
            obtainStyledAttributes.recycle();
        }
    }

    private void layoutChild(Matrix matrix, View view, LayoutParams layoutParams) {
        int mappedHorAnchor;
        int measuredWidth;
        int mappedVertAnchor;
        int measuredHeight;
        if (layoutParams.anchorLeft != DEF_VALUE) {
            mappedHorAnchor = getMappedHorAnchor(matrix, layoutParams.anchorLeft);
            measuredWidth = view.getMeasuredWidth() + mappedHorAnchor;
        } else if (layoutParams.anchorRight != DEF_VALUE) {
            measuredWidth = getMappedHorAnchor(matrix, layoutParams.anchorRight);
            mappedHorAnchor = measuredWidth - view.getMeasuredWidth();
        } else {
            mappedHorAnchor = getMappedHorAnchor(matrix, 0);
            measuredWidth = view.getMeasuredWidth() + mappedHorAnchor;
        }
        if (layoutParams.anchorTop != DEF_VALUE) {
            mappedVertAnchor = getMappedVertAnchor(matrix, layoutParams.anchorTop);
            measuredHeight = view.getMeasuredHeight() + mappedVertAnchor;
        } else if (layoutParams.anchorBottom != DEF_VALUE) {
            measuredHeight = getMappedVertAnchor(matrix, layoutParams.anchorBottom);
            mappedVertAnchor = measuredHeight - view.getMeasuredHeight();
        } else {
            mappedVertAnchor = getMappedVertAnchor(matrix, 0);
            measuredHeight = view.getMeasuredHeight() + mappedVertAnchor;
        }
        view.layout(mappedHorAnchor, mappedVertAnchor, measuredWidth, measuredHeight);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public int getBaseViewId() {
        return this.baseViewId;
    }

    protected void onAttachedToWindow() {
        checkBaseView();
        super.onAttachedToWindow();
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = 0;
        ImageView imageView = (ImageView) findViewById(this.baseViewId);
        imageView.layout(0, 0, i3 - i, i4 - i2);
        Matrix imageMatrix = imageView.getImageMatrix();
        int childCount = getChildCount();
        while (i5 < childCount) {
            View childAt = getChildAt(i5);
            if (!(childAt.getId() == this.baseViewId || childAt.getVisibility() == 8)) {
                layoutChild(imageMatrix, childAt, (LayoutParams) childAt.getLayoutParams());
            }
            i5++;
        }
    }

    protected void onMeasure(int i, int i2) {
        checkBaseView();
        ImageView imageView = (ImageView) findViewById(this.baseViewId);
        imageView.measure(i, i2);
        int measuredWidth = imageView.getMeasuredWidth();
        int measuredHeight = imageView.getMeasuredHeight();
        setMeasuredDimension(measuredWidth, measuredHeight);
        Matrix imageMatrix = imageView.getImageMatrix();
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (!(childAt.getId() == this.baseViewId || childAt.getVisibility() == 8)) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                childAt.measure(getWidthMeasureSpecForChild(imageMatrix, measuredWidth, layoutParams), getHeightMeasureSpecForChild(imageMatrix, measuredHeight, layoutParams));
            }
        }
    }

    public void setBaseViewId(int i) {
        this.baseViewId = i;
    }
}
