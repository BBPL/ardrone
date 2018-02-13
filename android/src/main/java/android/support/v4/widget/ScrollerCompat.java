package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.widget.Scroller;

class ScrollerCompat {
    Scroller mScroller;

    static class ScrollerCompatImplIcs extends ScrollerCompat {
        public ScrollerCompatImplIcs(Context context) {
            super(context);
        }

        public float getCurrVelocity() {
            return ScrollerCompatIcs.getCurrVelocity(this.mScroller);
        }
    }

    ScrollerCompat(Context context) {
        this.mScroller = new Scroller(context);
    }

    public static ScrollerCompat from(Context context) {
        return VERSION.SDK_INT >= 14 ? new ScrollerCompatImplIcs(context) : new ScrollerCompat(context);
    }

    public void abortAnimation() {
        this.mScroller.abortAnimation();
    }

    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.mScroller.fling(i, i2, i3, i4, i5, i6, i7, i8);
    }

    public float getCurrVelocity() {
        return 0.0f;
    }

    public int getCurrX() {
        return this.mScroller.getCurrX();
    }

    public int getCurrY() {
        return this.mScroller.getCurrY();
    }

    public int getDuration() {
        return this.mScroller.getDuration();
    }

    public boolean isFinished() {
        return this.mScroller.isFinished();
    }

    public void startScroll(int i, int i2, int i3, int i4) {
        this.mScroller.startScroll(i, i2, i3, i4);
    }

    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        this.mScroller.startScroll(i, i2, i3, i4, i5);
    }
}
