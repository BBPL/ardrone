package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.common.primitives.Ints;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;

public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR = new C00301();
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS = new int[]{16842931};
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new C00312();
    private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
    private int mActivePointerId = -1;
    private PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable = new C00323();
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mIgnoreGutter;
    private boolean mInLayout;
    private float mInitialMotionX;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private int mSeenPositionMax;
    private int mSeenPositionMin;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    interface Decor {
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    static final class C00301 implements Comparator<ItemInfo> {
        C00301() {
        }

        public int compare(ItemInfo itemInfo, ItemInfo itemInfo2) {
            return itemInfo.position - itemInfo2.position;
        }
    }

    static final class C00312 implements Interpolator {
        C00312() {
        }

        public float getInterpolation(float f) {
            float f2 = f - 1.0f;
            return (f2 * (((f2 * f2) * f2) * f2)) + 1.0f;
        }
    }

    class C00323 implements Runnable {
        C00323() {
        }

        public void run() {
            ViewPager.this.setScrollState(0);
            ViewPager.this.populate();
        }
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ViewPager.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName(ViewPager.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            boolean z = true;
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
            if (ViewPager.this.mAdapter == null || ViewPager.this.mAdapter.getCount() <= 1) {
                z = false;
            }
            accessibilityNodeInfoCompat.setScrollable(z);
            if (ViewPager.this.mAdapter != null && ViewPager.this.mCurItem >= 0 && ViewPager.this.mCurItem < ViewPager.this.mAdapter.getCount() - 1) {
                accessibilityNodeInfoCompat.addAction(4096);
            }
            if (ViewPager.this.mAdapter != null && ViewPager.this.mCurItem > 0 && ViewPager.this.mCurItem < ViewPager.this.mAdapter.getCount()) {
                accessibilityNodeInfoCompat.addAction(8192);
            }
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (super.performAccessibilityAction(view, i, bundle)) {
                return true;
            }
            switch (i) {
                case 4096:
                    if (ViewPager.this.mAdapter == null || ViewPager.this.mCurItem < 0 || ViewPager.this.mCurItem >= ViewPager.this.mAdapter.getCount() - 1) {
                        return false;
                    }
                    ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
                    return true;
                case 8192:
                    if (ViewPager.this.mAdapter == null || ViewPager.this.mCurItem <= 0 || ViewPager.this.mCurItem >= ViewPager.this.mAdapter.getCount()) {
                        return false;
                    }
                    ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
                    return true;
                default:
                    return false;
            }
        }
    }

    public interface PageTransformer {
        void transformPage(View view, float f);
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new C00331());
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        static final class C00331 implements ParcelableCompatCreatorCallbacks<SavedState> {
            C00331() {
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel);
            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
            }
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader);
            this.loader = classLoader;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, i);
        }
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        public void onPageSelected(int i) {
        }
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View view, View view2) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            LayoutParams layoutParams2 = (LayoutParams) view2.getLayoutParams();
            return layoutParams.isDecor != layoutParams2.isDecor ? layoutParams.isDecor ? 1 : -1 : layoutParams.position - layoutParams2.position;
        }
    }

    public ViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViewPager();
    }

    private void calculatePageOffsets(ItemInfo itemInfo, int i, ItemInfo itemInfo2) {
        int i2;
        int i3;
        int i4;
        float f;
        int count = this.mAdapter.getCount();
        int width = getWidth();
        float f2 = width > 0 ? ((float) this.mPageMargin) / ((float) width) : 0.0f;
        if (itemInfo2 != null) {
            i2 = itemInfo2.position;
            float f3;
            int i5;
            ItemInfo itemInfo3;
            float pageWidth;
            if (i2 < itemInfo.position) {
                i3 = i2 + 1;
                f3 = (itemInfo2.offset + itemInfo2.widthFactor) + f2;
                i4 = 0;
                while (i3 <= itemInfo.position && i4 < this.mItems.size()) {
                    i5 = i4;
                    itemInfo3 = (ItemInfo) this.mItems.get(i4);
                    width = i5;
                    while (i3 > itemInfo3.position && width < this.mItems.size() - 1) {
                        i4 = width + 1;
                        i5 = i4;
                        itemInfo3 = (ItemInfo) this.mItems.get(i4);
                        width = i5;
                    }
                    i5 = i3;
                    f = f3;
                    i2 = i5;
                    while (i2 < itemInfo3.position) {
                        pageWidth = (this.mAdapter.getPageWidth(i2) + f2) + f;
                        i2++;
                        f = pageWidth;
                    }
                    itemInfo3.offset = f;
                    f += itemInfo3.widthFactor + f2;
                    i4 = i2 + 1;
                    f3 = f;
                    i3 = i4;
                    i4 = width;
                }
            } else if (i2 > itemInfo.position) {
                width = this.mItems.size() - 1;
                i4 = i2 - 1;
                f3 = itemInfo2.offset;
                i3 = i4;
                i4 = width;
                while (i3 >= itemInfo.position && i4 >= 0) {
                    i5 = i4;
                    itemInfo3 = (ItemInfo) this.mItems.get(i4);
                    width = i5;
                    while (i3 < itemInfo3.position && width > 0) {
                        i4 = width - 1;
                        i5 = i4;
                        itemInfo3 = (ItemInfo) this.mItems.get(i4);
                        width = i5;
                    }
                    i5 = i3;
                    f = f3;
                    i2 = i5;
                    while (i2 > itemInfo3.position) {
                        pageWidth = f - (this.mAdapter.getPageWidth(i2) + f2);
                        i2--;
                        f = pageWidth;
                    }
                    f -= itemInfo3.widthFactor + f2;
                    itemInfo3.offset = f;
                    i4 = i2 - 1;
                    f3 = f;
                    i3 = i4;
                    i4 = width;
                }
            }
        }
        int size = this.mItems.size();
        f = itemInfo.offset;
        i4 = itemInfo.position - 1;
        this.mFirstOffset = itemInfo.position == 0 ? itemInfo.offset : -3.4028235E38f;
        this.mLastOffset = itemInfo.position == count + -1 ? (itemInfo.offset + itemInfo.widthFactor) - 1.0f : Float.MAX_VALUE;
        for (i2 = i - 1; i2 >= 0; i2--) {
            ItemInfo itemInfo4 = (ItemInfo) this.mItems.get(i2);
            while (i4 > itemInfo4.position) {
                f -= this.mAdapter.getPageWidth(i4) + f2;
                i4--;
            }
            f -= itemInfo4.widthFactor + f2;
            itemInfo4.offset = f;
            if (itemInfo4.position == 0) {
                this.mFirstOffset = f;
            }
            i4--;
        }
        float f4 = (itemInfo.offset + itemInfo.widthFactor) + f2;
        i3 = itemInfo.position + 1;
        for (i2 = i + 1; i2 < size; i2++) {
            itemInfo4 = (ItemInfo) this.mItems.get(i2);
            while (i3 < itemInfo4.position) {
                f4 += this.mAdapter.getPageWidth(i3) + f2;
                i3++;
            }
            if (itemInfo4.position == count - 1) {
                this.mLastOffset = (itemInfo4.widthFactor + f4) - 1.0f;
            }
            itemInfo4.offset = f4;
            f4 += itemInfo4.widthFactor + f2;
            i3++;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    private void completeScroll(boolean z) {
        int scrollY;
        boolean z2 = this.mScrollState == 2;
        if (z2) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int scrollX = getScrollX();
            scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (!(scrollX == currX && scrollY == currY)) {
                scrollTo(currX, currY);
            }
        }
        this.mPopulatePending = false;
        boolean z3 = z2;
        scrollY = 0;
        while (scrollY < this.mItems.size()) {
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(scrollY);
            if (itemInfo.scrolling) {
                itemInfo.scrolling = false;
                z2 = true;
            } else {
                z2 = z3;
            }
            scrollY++;
            z3 = z2;
        }
        if (!z3) {
            return;
        }
        if (z) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
        } else {
            this.mEndScrollRunnable.run();
        }
    }

    private int determineTargetPage(int i, float f, int i2, int i3) {
        if (Math.abs(i3) <= this.mFlingDistance || Math.abs(i2) <= this.mMinimumVelocity) {
            i = (this.mSeenPositionMin < 0 || this.mSeenPositionMin >= i || f >= 0.5f) ? (this.mSeenPositionMax < 0 || this.mSeenPositionMax <= i + 1 || f < 0.5f) ? (int) ((((float) i) + f) + 0.5f) : i - 1 : i + 1;
        } else if (i2 <= 0) {
            i++;
        }
        if (this.mItems.size() <= 0) {
            return i;
        }
        return Math.max(((ItemInfo) this.mItems.get(0)).position, Math.min(i, ((ItemInfo) this.mItems.get(this.mItems.size() - 1)).position));
    }

    private void enableLayers(boolean z) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewCompat.setLayerType(getChildAt(i), z ? 2 : 0, null);
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect rect, View view) {
        if (rect == null) {
            rect = new Rect();
        }
        if (view == null) {
            rect.set(0, 0, 0, 0);
        } else {
            rect.left = view.getLeft();
            rect.right = view.getRight();
            rect.top = view.getTop();
            rect.bottom = view.getBottom();
            ViewPager parent = view.getParent();
            while ((parent instanceof ViewGroup) && parent != this) {
                ViewGroup viewGroup = parent;
                rect.left += viewGroup.getLeft();
                rect.right += viewGroup.getRight();
                rect.top += viewGroup.getTop();
                rect.bottom += viewGroup.getBottom();
                parent = viewGroup.getParent();
            }
        }
        return rect;
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int width = getWidth();
        float scrollX = width > 0 ? ((float) getScrollX()) / ((float) width) : 0.0f;
        float f = width > 0 ? ((float) this.mPageMargin) / ((float) width) : 0.0f;
        int i = -1;
        Object obj = 1;
        int i2 = 0;
        float f2 = 0.0f;
        float f3 = 0.0f;
        ItemInfo itemInfo = null;
        while (i2 < this.mItems.size()) {
            ItemInfo itemInfo2 = (ItemInfo) this.mItems.get(i2);
            if (obj != null || itemInfo2.position == i + 1) {
                i = i2;
            } else {
                itemInfo2 = this.mTempItem;
                itemInfo2.offset = (f2 + f3) + f;
                itemInfo2.position = i + 1;
                itemInfo2.widthFactor = this.mAdapter.getPageWidth(itemInfo2.position);
                i = i2 - 1;
            }
            float f4 = itemInfo2.offset;
            f2 = itemInfo2.widthFactor;
            if (obj == null && scrollX < f4) {
                return itemInfo;
            }
            if (scrollX < (f2 + f4) + f || i == this.mItems.size() - 1) {
                return itemInfo2;
            }
            int i3 = itemInfo2.position;
            f2 = itemInfo2.widthFactor;
            f3 = f4;
            obj = null;
            i2 = i + 1;
            i = i3;
            itemInfo = itemInfo2;
        }
        return itemInfo;
    }

    private boolean isGutterDrag(float f, float f2) {
        return (f < ((float) this.mGutterSize) && f2 > 0.0f) || (f > ((float) (getWidth() - this.mGutterSize)) && f2 < 0.0f);
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, actionIndex) == this.mActivePointerId) {
            actionIndex = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = MotionEventCompat.getX(motionEvent, actionIndex);
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private boolean pageScrolled(int i) {
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
        int width = getWidth();
        int i2 = this.mPageMargin;
        float f = ((float) this.mPageMargin) / ((float) width);
        int i3 = infoForCurrentScrollPosition.position;
        float f2 = ((((float) i) / ((float) width)) - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + f);
        width = (int) (((float) (width + i2)) * f2);
        this.mCalledSuper = false;
        onPageScrolled(i3, f2, width);
        if (this.mCalledSuper) {
            return true;
        }
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }

    private boolean performDrag(float f) {
        boolean z;
        float f2;
        boolean z2 = true;
        boolean z3 = false;
        float f3 = this.mLastMotionX;
        this.mLastMotionX = f;
        float scrollX = ((float) getScrollX()) + (f3 - f);
        int width = getWidth();
        float f4 = ((float) width) * this.mFirstOffset;
        float f5 = ((float) width) * this.mLastOffset;
        ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
        ItemInfo itemInfo2 = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
        if (itemInfo.position != 0) {
            f4 = itemInfo.offset * ((float) width);
            z = false;
        } else {
            z = true;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            f2 = itemInfo2.offset * ((float) width);
            z2 = false;
        } else {
            f2 = f5;
        }
        if (scrollX < f4) {
            if (z) {
                z3 = this.mLeftEdge.onPull(Math.abs(f4 - scrollX) / ((float) width));
            }
        } else if (scrollX > f2) {
            if (z2) {
                z3 = this.mRightEdge.onPull(Math.abs(scrollX - f2) / ((float) width));
            }
            f4 = f2;
        } else {
            f4 = scrollX;
        }
        this.mLastMotionX += f4 - ((float) ((int) f4));
        scrollTo((int) f4, getScrollY());
        pageScrolled((int) f4);
        return z3;
    }

    private void recomputeScrollPosition(int i, int i2, int i3, int i4) {
        if (i2 <= 0 || this.mItems.isEmpty()) {
            ItemInfo infoForPosition = infoForPosition(this.mCurItem);
            int min = (int) ((infoForPosition != null ? Math.min(infoForPosition.offset, this.mLastOffset) : 0.0f) * ((float) i));
            if (min != getScrollX()) {
                completeScroll(false);
                scrollTo(min, getScrollY());
                return;
            }
            return;
        }
        int scrollX = (int) ((((float) getScrollX()) / ((float) (i2 + i4))) * ((float) (i + i3)));
        scrollTo(scrollX, getScrollY());
        if (!this.mScroller.isFinished()) {
            this.mScroller.startScroll(scrollX, 0, (int) (infoForPosition(this.mCurItem).offset * ((float) i)), 0, this.mScroller.getDuration() - this.mScroller.timePassed());
        }
    }

    private void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    private void scrollToItem(int i, boolean z, int i2, boolean z2) {
        int max;
        ItemInfo infoForPosition = infoForPosition(i);
        if (infoForPosition != null) {
            max = (int) (Math.max(this.mFirstOffset, Math.min(infoForPosition.offset, this.mLastOffset)) * ((float) getWidth()));
        } else {
            max = 0;
        }
        if (z) {
            smoothScrollTo(max, 0, i2);
            if (z2 && this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageSelected(i);
            }
            if (z2 && this.mInternalPageChangeListener != null) {
                this.mInternalPageChangeListener.onPageSelected(i);
                return;
            }
            return;
        }
        if (z2 && this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(i);
        }
        if (z2 && this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(i);
        }
        completeScroll(false);
        scrollTo(max, 0);
    }

    private void setScrollState(int i) {
        boolean z = true;
        if (this.mScrollState != i) {
            this.mScrollState = i;
            if (i == 1) {
                this.mSeenPositionMax = -1;
                this.mSeenPositionMin = -1;
            }
            if (this.mPageTransformer != null) {
                if (i == 0) {
                    z = false;
                }
                enableLayers(z);
            }
            if (this.mOnPageChangeListener != null) {
                this.mOnPageChangeListener.onPageScrollStateChanged(i);
            }
        }
    }

    private void setScrollingCacheEnabled(boolean z) {
        if (this.mScrollingCacheEnabled != z) {
            this.mScrollingCacheEnabled = z;
        }
    }

    public void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        int size = arrayList.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getVisibility() == 0) {
                    ItemInfo infoForChild = infoForChild(childAt);
                    if (infoForChild != null && infoForChild.position == this.mCurItem) {
                        childAt.addFocusables(arrayList, i, i2);
                    }
                }
            }
        }
        if ((descendantFocusability == 262144 && size != arrayList.size()) || !isFocusable()) {
            return;
        }
        if (((i2 & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && arrayList != null) {
            arrayList.add(this);
        }
    }

    ItemInfo addNewItem(int i, int i2) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = i;
        itemInfo.object = this.mAdapter.instantiateItem((ViewGroup) this, i);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(i);
        if (i2 < 0 || i2 >= this.mItems.size()) {
            this.mItems.add(itemInfo);
        } else {
            this.mItems.add(i2, itemInfo);
        }
        return itemInfo;
    }

    public void addTouchables(ArrayList<View> arrayList) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    childAt.addTouchables(arrayList);
                }
            }
        }
    }

    public void addView(View view, int i, android.view.ViewGroup.LayoutParams layoutParams) {
        android.view.ViewGroup.LayoutParams generateLayoutParams = !checkLayoutParams(layoutParams) ? generateLayoutParams(layoutParams) : layoutParams;
        LayoutParams layoutParams2 = (LayoutParams) generateLayoutParams;
        layoutParams2.isDecor |= view instanceof Decor;
        if (!this.mInLayout) {
            super.addView(view, i, generateLayoutParams);
        } else if (layoutParams2 == null || !layoutParams2.isDecor) {
            layoutParams2.needsMeasure = true;
            addViewInLayout(view, i, generateLayoutParams);
        } else {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
    }

    public boolean arrowScroll(int i) {
        boolean pageLeft;
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i);
        if (findNextFocus == null || findNextFocus == findFocus) {
            if (i == 17 || i == 1) {
                pageLeft = pageLeft();
            } else {
                if (i == 66 || i == 2) {
                    pageLeft = pageRight();
                }
                pageLeft = false;
            }
        } else if (i == 17) {
            pageLeft = (findFocus == null || getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left < getChildRectInPagerCoordinates(this.mTempRect, findFocus).left) ? findNextFocus.requestFocus() : pageLeft();
        } else {
            if (i == 66) {
                pageLeft = (findFocus == null || getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left > getChildRectInPagerCoordinates(this.mTempRect, findFocus).left) ? findNextFocus.requestFocus() : pageRight();
            }
            pageLeft = false;
        }
        if (pageLeft) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
        }
        return pageLeft;
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
        this.mFakeDragBeginTime = uptimeMillis;
        return true;
    }

    protected boolean canScroll(View view, boolean z, int i, int i2, int i3) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (i2 + scrollX >= childAt.getLeft() && i2 + scrollX < childAt.getRight() && i3 + scrollY >= childAt.getTop() && i3 + scrollY < childAt.getBottom()) {
                    if (canScroll(childAt, true, i, (i2 + scrollX) - childAt.getLeft(), (i3 + scrollY) - childAt.getTop())) {
                        return true;
                    }
                }
            }
        }
        return z && ViewCompat.canScrollHorizontally(view, -i);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    public void computeScroll() {
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll(true);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int currX = this.mScroller.getCurrX();
        int currY = this.mScroller.getCurrY();
        if (!(scrollX == currX && scrollY == currY)) {
            scrollTo(currX, currY);
            if (!pageScrolled(currX)) {
                this.mScroller.abortAnimation();
                scrollTo(0, currY);
            }
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    void dataSetChanged() {
        boolean z = this.mItems.size() < (this.mOffscreenPageLimit * 2) + 1 && this.mItems.size() < this.mAdapter.getCount();
        boolean z2 = false;
        int i = this.mCurItem;
        boolean z3 = z;
        int i2 = 0;
        while (i2 < this.mItems.size()) {
            int i3;
            boolean z4;
            int i4;
            boolean z5;
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(i2);
            int itemPosition = this.mAdapter.getItemPosition(itemInfo.object);
            if (itemPosition == -1) {
                i3 = i2;
                z4 = z2;
                i4 = i;
                z5 = z3;
            } else if (itemPosition == -2) {
                this.mItems.remove(i2);
                i2--;
                if (!z2) {
                    this.mAdapter.startUpdate((ViewGroup) this);
                    z2 = true;
                }
                this.mAdapter.destroyItem((ViewGroup) this, itemInfo.position, itemInfo.object);
                if (this.mCurItem == itemInfo.position) {
                    i3 = i2;
                    z4 = z2;
                    i4 = Math.max(0, Math.min(this.mCurItem, this.mAdapter.getCount() - 1));
                    z5 = true;
                } else {
                    i3 = i2;
                    z4 = z2;
                    i4 = i;
                    z5 = true;
                }
            } else if (itemInfo.position != itemPosition) {
                if (itemInfo.position == this.mCurItem) {
                    i = itemPosition;
                }
                itemInfo.position = itemPosition;
                i3 = i2;
                z4 = z2;
                i4 = i;
                z5 = true;
            } else {
                i3 = i2;
                z4 = z2;
                i4 = i;
                z5 = z3;
            }
            z3 = z5;
            i = i4;
            z2 = z4;
            i2 = i3 + 1;
        }
        if (z2) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (z3) {
            i4 = getChildCount();
            for (i2 = 0; i2 < i4; i2++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i2).getLayoutParams();
                if (!layoutParams.isDecor) {
                    layoutParams.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(i, false, true);
            requestLayout();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || executeKeyEvent(keyEvent);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem && childAt.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                    return true;
                }
            }
        }
        return false;
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    public void draw(Canvas canvas) {
        int i = 0;
        super.draw(canvas);
        int overScrollMode = ViewCompat.getOverScrollMode(this);
        if (overScrollMode == 0 || (overScrollMode == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1)) {
            int width;
            if (!this.mLeftEdge.isFinished()) {
                overScrollMode = canvas.save();
                i = (getHeight() - getPaddingTop()) - getPaddingBottom();
                width = getWidth();
                canvas.rotate(BitmapDescriptorFactory.HUE_VIOLET);
                canvas.translate((float) ((-i) + getPaddingTop()), this.mFirstOffset * ((float) width));
                this.mLeftEdge.setSize(i, width);
                i = this.mLeftEdge.draw(canvas) | 0;
                canvas.restoreToCount(overScrollMode);
            }
            if (!this.mRightEdge.isFinished()) {
                overScrollMode = canvas.save();
                width = getWidth();
                int height = getHeight();
                int paddingTop = getPaddingTop();
                int paddingBottom = getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) width));
                this.mRightEdge.setSize((height - paddingTop) - paddingBottom, width);
                i |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(overScrollMode);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (i != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mMarginDrawable;
        if (drawable != null && drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
            int xVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
            this.mPopulatePending = true;
            int width = getWidth();
            int scrollX = getScrollX();
            ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
            setCurrentItemInternal(determineTargetPage(infoForCurrentScrollPosition.position, ((((float) scrollX) / ((float) width)) - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, xVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, xVelocity);
            endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            switch (keyEvent.getKeyCode()) {
                case 21:
                    return arrowScroll(17);
                case 22:
                    return arrowScroll(66);
                case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                    if (VERSION.SDK_INT >= 11) {
                        if (KeyEventCompat.hasNoModifiers(keyEvent)) {
                            return arrowScroll(2);
                        }
                        if (KeyEventCompat.hasModifiers(keyEvent, 1)) {
                            return arrowScroll(1);
                        }
                    }
                    break;
            }
        }
        return false;
    }

    public void fakeDragBy(float f) {
        if (this.mFakeDragging) {
            this.mLastMotionX += f;
            float scrollX = ((float) getScrollX()) - f;
            int width = getWidth();
            float f2 = ((float) width) * this.mLastOffset;
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
            ItemInfo itemInfo2 = (ItemInfo) this.mItems.get(this.mItems.size() - 1);
            float f3 = itemInfo.position != 0 ? itemInfo.offset * ((float) width) : ((float) width) * this.mFirstOffset;
            float f4 = itemInfo2.position != this.mAdapter.getCount() + -1 ? itemInfo2.offset * ((float) width) : f2;
            if (scrollX >= f3) {
                f3 = scrollX > f4 ? f4 : scrollX;
            }
            this.mLastMotionX += f3 - ((float) ((int) f3));
            scrollTo((int) f3, getScrollY());
            pageScrolled((int) f3);
            MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
            this.mVelocityTracker.addMovement(obtain);
            obtain.recycle();
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    protected int getChildDrawingOrder(int i, int i2) {
        if (this.mDrawingOrder == 2) {
            i2 = (i - 1) - i2;
        }
        return ((LayoutParams) ((View) this.mDrawingOrderedChildren.get(i2)).getLayoutParams()).childIndex;
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    ItemInfo infoForAnyChild(View view) {
        while (true) {
            ViewPager parent = view.getParent();
            if (parent == this) {
                return infoForChild(view);
            }
            if (parent != null && (parent instanceof View)) {
                view = parent;
            }
        }
        return null;
    }

    ItemInfo infoForChild(View view) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(view, itemInfo.object)) {
                return itemInfo;
            }
        }
        return null;
    }

    ItemInfo infoForPosition(int i) {
        for (int i2 = 0; i2 < this.mItems.size(); i2++) {
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(i2);
            if (itemInfo.position == i) {
                return itemInfo;
            }
        }
        return null;
    }

    void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mFlingDistance = (int) (25.0f * f);
        this.mCloseEnough = (int) (2.0f * f);
        this.mDefaultGutterSize = (int) (f * 16.0f);
        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = getScrollX();
            int width = getWidth();
            float f = ((float) this.mPageMargin) / ((float) width);
            ItemInfo itemInfo = (ItemInfo) this.mItems.get(0);
            float f2 = itemInfo.offset;
            int size = this.mItems.size();
            int i = itemInfo.position;
            int i2 = ((ItemInfo) this.mItems.get(size - 1)).position;
            int i3 = 0;
            int i4 = i;
            float f3 = f2;
            int i5 = i4;
            while (i5 < i2) {
                float f4;
                while (i5 > itemInfo.position && i3 < size) {
                    i3++;
                    itemInfo = (ItemInfo) this.mItems.get(i3);
                }
                if (i5 == itemInfo.position) {
                    f4 = (itemInfo.offset + itemInfo.widthFactor) * ((float) width);
                    f3 = (itemInfo.offset + itemInfo.widthFactor) + f;
                } else {
                    float pageWidth = this.mAdapter.getPageWidth(i5);
                    f4 = (f3 + pageWidth) * ((float) width);
                    f3 += pageWidth + f;
                }
                if (((float) this.mPageMargin) + f4 > ((float) scrollX)) {
                    this.mMarginDrawable.setBounds((int) f4, this.mTopPageBounds, (int) ((((float) this.mPageMargin) + f4) + 0.5f), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (f4 <= ((float) (scrollX + width))) {
                    i5++;
                } else {
                    return;
                }
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 3 || action == 1) {
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            if (this.mVelocityTracker == null) {
                return false;
            }
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return false;
            }
        }
        switch (action) {
            case 0:
                float x = motionEvent.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                this.mLastMotionY = motionEvent.getY();
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                this.mIsUnableToDrag = false;
                this.mScroller.computeScrollOffset();
                if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    populate();
                    this.mIsBeingDragged = true;
                    setScrollState(1);
                    break;
                }
                completeScroll(false);
                this.mIsBeingDragged = false;
                break;
            case 2:
                action = this.mActivePointerId;
                if (action != -1) {
                    action = MotionEventCompat.findPointerIndex(motionEvent, action);
                    float x2 = MotionEventCompat.getX(motionEvent, action);
                    float f = x2 - this.mLastMotionX;
                    float abs = Math.abs(f);
                    float y = MotionEventCompat.getY(motionEvent, action);
                    float abs2 = Math.abs(y - this.mLastMotionY);
                    if (f == 0.0f || isGutterDrag(this.mLastMotionX, f) || !canScroll(this, false, (int) f, (int) x2, (int) y)) {
                        if (abs > ((float) this.mTouchSlop) && abs > abs2) {
                            this.mIsBeingDragged = true;
                            setScrollState(1);
                            this.mLastMotionX = f > 0.0f ? this.mInitialMotionX + ((float) this.mTouchSlop) : this.mInitialMotionX - ((float) this.mTouchSlop);
                            setScrollingCacheEnabled(true);
                        } else if (abs2 > ((float) this.mTouchSlop)) {
                            this.mIsUnableToDrag = true;
                        }
                        if (this.mIsBeingDragged && performDrag(x2)) {
                            ViewCompat.postInvalidateOnAnimation(this);
                            break;
                        }
                    }
                    this.mLastMotionX = x2;
                    this.mInitialMotionX = x2;
                    this.mLastMotionY = y;
                    this.mIsUnableToDrag = true;
                    return false;
                }
                break;
            case 6:
                onSecondaryPointerUp(motionEvent);
                break;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        return this.mIsBeingDragged;
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        LayoutParams layoutParams;
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int childCount = getChildCount();
        int i5 = i3 - i;
        int i6 = i4 - i2;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int i7 = 0;
        int i8 = 0;
        while (i8 < childCount) {
            int i9;
            int measuredWidth;
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() != 8) {
                layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isDecor) {
                    int max;
                    int i10;
                    i9 = layoutParams.gravity;
                    int i11 = layoutParams.gravity;
                    switch (i9 & 7) {
                        case 1:
                            i9 = paddingLeft;
                            max = Math.max((i5 - childAt.getMeasuredWidth()) / 2, paddingLeft);
                            paddingLeft = paddingRight;
                            break;
                        case 3:
                            i9 = paddingLeft + childAt.getMeasuredWidth();
                            max = paddingLeft;
                            paddingLeft = paddingRight;
                            break;
                        case 5:
                            measuredWidth = (i5 - paddingRight) - childAt.getMeasuredWidth();
                            i9 = paddingLeft;
                            max = measuredWidth;
                            paddingLeft = paddingRight + childAt.getMeasuredWidth();
                            break;
                        default:
                            i9 = paddingLeft;
                            max = paddingLeft;
                            paddingLeft = paddingRight;
                            break;
                    }
                    switch (i11 & 112) {
                        case 16:
                            i10 = paddingBottom;
                            paddingBottom = Math.max((i6 - childAt.getMeasuredHeight()) / 2, paddingTop);
                            measuredWidth = i10;
                            break;
                        case 48:
                            i10 = paddingBottom;
                            paddingBottom = paddingTop;
                            paddingTop = childAt.getMeasuredHeight() + paddingTop;
                            measuredWidth = i10;
                            break;
                        case ExifTagConstants.FLASH_VALUE_OFF_RED_EYE_REDUCTION /*80*/:
                            measuredWidth = childAt.getMeasuredHeight() + paddingBottom;
                            paddingBottom = (i6 - paddingBottom) - childAt.getMeasuredHeight();
                            break;
                        default:
                            measuredWidth = paddingBottom;
                            paddingBottom = paddingTop;
                            break;
                    }
                    paddingRight = max + scrollX;
                    childAt.layout(paddingRight, paddingBottom, childAt.getMeasuredWidth() + paddingRight, childAt.getMeasuredHeight() + paddingBottom);
                    paddingBottom = i7 + 1;
                    paddingRight = i9;
                    i7 = paddingTop;
                    paddingTop = paddingLeft;
                    i10 = paddingBottom;
                    paddingBottom = measuredWidth;
                    measuredWidth = i10;
                    i8++;
                    paddingLeft = paddingRight;
                    paddingRight = paddingTop;
                    paddingTop = i7;
                    i7 = measuredWidth;
                }
            }
            measuredWidth = i7;
            i7 = paddingTop;
            paddingTop = paddingRight;
            paddingRight = paddingLeft;
            i8++;
            paddingLeft = paddingRight;
            paddingRight = paddingTop;
            paddingTop = i7;
            i7 = measuredWidth;
        }
        for (i9 = 0; i9 < childCount; i9++) {
            View childAt2 = getChildAt(i9);
            if (childAt2.getVisibility() != 8) {
                layoutParams = (LayoutParams) childAt2.getLayoutParams();
                if (!layoutParams.isDecor) {
                    ItemInfo infoForChild = infoForChild(childAt2);
                    if (infoForChild != null) {
                        i8 = ((int) (infoForChild.offset * ((float) i5))) + paddingLeft;
                        if (layoutParams.needsMeasure) {
                            layoutParams.needsMeasure = false;
                            childAt2.measure(MeasureSpec.makeMeasureSpec((int) (layoutParams.widthFactor * ((float) ((i5 - paddingLeft) - paddingRight))), Ints.MAX_POWER_OF_TWO), MeasureSpec.makeMeasureSpec((i6 - paddingTop) - paddingBottom, Ints.MAX_POWER_OF_TWO));
                        }
                        childAt2.layout(i8, paddingTop, childAt2.getMeasuredWidth() + i8, childAt2.getMeasuredHeight() + paddingTop);
                    }
                }
            }
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = i6 - paddingBottom;
        this.mDecorChildCount = i7;
        this.mFirstLayout = false;
    }

    protected void onMeasure(int i, int i2) {
        int i3;
        setMeasuredDimension(getDefaultSize(0, i), getDefaultSize(0, i2));
        int measuredWidth = getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        int paddingLeft = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            LayoutParams layoutParams;
            int i5;
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8) {
                layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams != null && layoutParams.isDecor) {
                    int i6 = layoutParams.gravity & 7;
                    int i7 = layoutParams.gravity & 112;
                    i5 = Integer.MIN_VALUE;
                    i3 = Integer.MIN_VALUE;
                    Object obj = (i7 == 48 || i7 == 80) ? 1 : null;
                    Object obj2 = (i6 == 3 || i6 == 5) ? 1 : null;
                    if (obj != null) {
                        i5 = Ints.MAX_POWER_OF_TWO;
                    } else if (obj2 != null) {
                        i3 = Ints.MAX_POWER_OF_TWO;
                    }
                    if (layoutParams.width != -2) {
                        i7 = Ints.MAX_POWER_OF_TWO;
                        i5 = layoutParams.width != -1 ? layoutParams.width : paddingLeft;
                    } else {
                        i7 = i5;
                        i5 = paddingLeft;
                    }
                    if (layoutParams.height != -2) {
                        i3 = Ints.MAX_POWER_OF_TWO;
                        if (layoutParams.height != -1) {
                            measuredWidth = layoutParams.height;
                            childAt.measure(MeasureSpec.makeMeasureSpec(i5, i7), MeasureSpec.makeMeasureSpec(measuredWidth, i3));
                            if (obj != null) {
                                measuredHeight -= childAt.getMeasuredHeight();
                            } else if (obj2 != null) {
                                paddingLeft -= childAt.getMeasuredWidth();
                            }
                        }
                    }
                    measuredWidth = measuredHeight;
                    childAt.measure(MeasureSpec.makeMeasureSpec(i5, i7), MeasureSpec.makeMeasureSpec(measuredWidth, i3));
                    if (obj != null) {
                        measuredHeight -= childAt.getMeasuredHeight();
                    } else if (obj2 != null) {
                        paddingLeft -= childAt.getMeasuredWidth();
                    }
                }
            }
        }
        this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(paddingLeft, Ints.MAX_POWER_OF_TWO);
        this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, Ints.MAX_POWER_OF_TWO);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        i3 = getChildCount();
        for (i5 = 0; i5 < i3; i5++) {
            View childAt2 = getChildAt(i5);
            if (childAt2.getVisibility() != 8) {
                layoutParams = (LayoutParams) childAt2.getLayoutParams();
                if (layoutParams == null || !layoutParams.isDecor) {
                    childAt2.measure(MeasureSpec.makeMeasureSpec((int) (layoutParams.widthFactor * ((float) paddingLeft)), Ints.MAX_POWER_OF_TWO), this.mChildHeightMeasureSpec);
                }
            }
        }
    }

    protected void onPageScrolled(int i, float f, int i2) {
        int paddingLeft;
        int paddingRight;
        int i3;
        if (this.mDecorChildCount > 0) {
            int scrollX = getScrollX();
            paddingLeft = getPaddingLeft();
            paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            i3 = 0;
            while (i3 < childCount) {
                int i4;
                View childAt = getChildAt(i3);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isDecor) {
                    int i5;
                    switch (layoutParams.gravity & 7) {
                        case 1:
                            i5 = paddingRight;
                            paddingRight = Math.max((width - childAt.getMeasuredWidth()) / 2, paddingLeft);
                            i4 = paddingLeft;
                            paddingLeft = i5;
                            break;
                        case 3:
                            i4 = childAt.getWidth() + paddingLeft;
                            i5 = paddingRight;
                            paddingRight = paddingLeft;
                            paddingLeft = i5;
                            break;
                        case 5:
                            i5 = paddingRight + childAt.getMeasuredWidth();
                            paddingRight = (width - paddingRight) - childAt.getMeasuredWidth();
                            i4 = paddingLeft;
                            paddingLeft = i5;
                            break;
                        default:
                            i4 = paddingLeft;
                            i5 = paddingLeft;
                            paddingLeft = paddingRight;
                            paddingRight = i5;
                            break;
                    }
                    paddingRight = (paddingRight + scrollX) - childAt.getLeft();
                    if (paddingRight != 0) {
                        childAt.offsetLeftAndRight(paddingRight);
                    }
                } else {
                    i4 = paddingLeft;
                    paddingLeft = paddingRight;
                }
                i3++;
                paddingRight = paddingLeft;
                paddingLeft = i4;
            }
        }
        if (this.mSeenPositionMin < 0 || i < this.mSeenPositionMin) {
            this.mSeenPositionMin = i;
        }
        if (this.mSeenPositionMax < 0 || FloatMath.ceil(((float) i) + f) > ((float) this.mSeenPositionMax)) {
            this.mSeenPositionMax = i + 1;
        }
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(i, f, i2);
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(i, f, i2);
        }
        if (this.mPageTransformer != null) {
            paddingRight = getScrollX();
            i3 = getChildCount();
            for (paddingLeft = 0; paddingLeft < i3; paddingLeft++) {
                View childAt2 = getChildAt(paddingLeft);
                if (!((LayoutParams) childAt2.getLayoutParams()).isDecor) {
                    this.mPageTransformer.transformPage(childAt2, ((float) (childAt2.getLeft() - paddingRight)) / ((float) getWidth()));
                }
            }
        }
        this.mCalledSuper = true;
    }

    protected boolean onRequestFocusInDescendants(int i, Rect rect) {
        int i2;
        int i3 = -1;
        int childCount = getChildCount();
        if ((i & 2) != 0) {
            i3 = 1;
            i2 = 0;
        } else {
            i2 = childCount - 1;
            childCount = -1;
        }
        while (i2 != childCount) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() == 0) {
                ItemInfo infoForChild = infoForChild(childAt);
                if (infoForChild != null && infoForChild.position == this.mCurItem && childAt.requestFocus(i, rect)) {
                    return true;
                }
            }
            i2 += i3;
        }
        return false;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(savedState.adapterState, savedState.loader);
                setCurrentItemInternal(savedState.position, false, true);
                return;
            }
            this.mRestoredCurItem = savedState.position;
            this.mRestoredAdapterState = savedState.adapterState;
            this.mRestoredClassLoader = savedState.loader;
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public Parcelable onSaveInstanceState() {
        Parcelable savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        if (this.mAdapter != null) {
            savedState.adapterState = this.mAdapter.saveState();
        }
        return savedState;
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3) {
            recomputeScrollPosition(i, i3, this.mPageMargin, this.mPageMargin);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (this.mFakeDragging) {
            return true;
        }
        if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        float x;
        int xVelocity;
        switch (motionEvent.getAction() & 255) {
            case 0:
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                populate();
                this.mIsBeingDragged = true;
                setScrollState(1);
                x = motionEvent.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                break;
            case 1:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    xVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                    this.mPopulatePending = true;
                    int width = getWidth();
                    int scrollX = getScrollX();
                    ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
                    setCurrentItemInternal(determineTargetPage(infoForCurrentScrollPosition.position, ((((float) scrollX) / ((float) width)) - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, xVelocity, (int) (MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId)) - this.mInitialMotionX)), true, true, xVelocity);
                    this.mActivePointerId = -1;
                    endDrag();
                    z = this.mRightEdge.onRelease() | this.mLeftEdge.onRelease();
                    break;
                }
                break;
            case 2:
                if (!this.mIsBeingDragged) {
                    xVelocity = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                    float x2 = MotionEventCompat.getX(motionEvent, xVelocity);
                    float abs = Math.abs(x2 - this.mLastMotionX);
                    x = Math.abs(MotionEventCompat.getY(motionEvent, xVelocity) - this.mLastMotionY);
                    if (abs > ((float) this.mTouchSlop) && abs > x) {
                        this.mIsBeingDragged = true;
                        this.mLastMotionX = x2 - this.mInitialMotionX > 0.0f ? this.mInitialMotionX + ((float) this.mTouchSlop) : this.mInitialMotionX - ((float) this.mTouchSlop);
                        setScrollState(1);
                        setScrollingCacheEnabled(true);
                    }
                }
                if (this.mIsBeingDragged) {
                    z = performDrag(MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId))) | 0;
                    break;
                }
                break;
            case 3:
                if (this.mIsBeingDragged) {
                    scrollToItem(this.mCurItem, true, 0, false);
                    this.mActivePointerId = -1;
                    endDrag();
                    z = this.mRightEdge.onRelease() | this.mLeftEdge.onRelease();
                    break;
                }
                break;
            case 5:
                xVelocity = MotionEventCompat.getActionIndex(motionEvent);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, xVelocity);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, xVelocity);
                break;
            case 6:
                onSecondaryPointerUp(motionEvent);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                break;
        }
        if (z) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    boolean pageLeft() {
        if (this.mCurItem <= 0) {
            return false;
        }
        setCurrentItem(this.mCurItem - 1, true);
        return true;
    }

    boolean pageRight() {
        if (this.mAdapter == null || this.mCurItem >= this.mAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    void populate() {
        populate(this.mCurItem);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void populate(int r14) {
        /*
        r13 = this;
        r0 = 0;
        r1 = r13.mCurItem;
        if (r1 == r14) goto L_0x0236;
    L_0x0005:
        r0 = r13.mCurItem;
        r0 = r13.infoForPosition(r0);
        r13.mCurItem = r14;
        r1 = r0;
    L_0x000e:
        r0 = r13.mAdapter;
        if (r0 != 0) goto L_0x0013;
    L_0x0012:
        return;
    L_0x0013:
        r0 = r13.mPopulatePending;
        if (r0 != 0) goto L_0x0012;
    L_0x0017:
        r0 = r13.getWindowToken();
        if (r0 == 0) goto L_0x0012;
    L_0x001d:
        r0 = r13.mAdapter;
        r0.startUpdate(r13);
        r0 = r13.mOffscreenPageLimit;
        r2 = 0;
        r3 = r13.mCurItem;
        r3 = r3 - r0;
        r7 = java.lang.Math.max(r2, r3);
        r2 = r13.mAdapter;
        r8 = r2.getCount();
        r2 = r8 + -1;
        r3 = r13.mCurItem;
        r0 = r0 + r3;
        r9 = java.lang.Math.min(r2, r0);
        r3 = 0;
        r0 = 0;
        r2 = r0;
    L_0x003e:
        r0 = r13.mItems;
        r0 = r0.size();
        if (r2 >= r0) goto L_0x0233;
    L_0x0046:
        r0 = r13.mItems;
        r0 = r0.get(r2);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
        r4 = r0.position;
        r5 = r13.mCurItem;
        if (r4 < r5) goto L_0x0117;
    L_0x0054:
        r4 = r0.position;
        r5 = r13.mCurItem;
        if (r4 != r5) goto L_0x0233;
    L_0x005a:
        if (r0 != 0) goto L_0x0230;
    L_0x005c:
        if (r8 <= 0) goto L_0x0230;
    L_0x005e:
        r0 = r13.mCurItem;
        r0 = r13.addNewItem(r0, r2);
        r6 = r0;
    L_0x0065:
        if (r6 == 0) goto L_0x00bc;
    L_0x0067:
        r5 = 0;
        r3 = r2 + -1;
        if (r3 < 0) goto L_0x011c;
    L_0x006c:
        r0 = r13.mItems;
        r0 = r0.get(r3);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
    L_0x0074:
        r10 = r6.widthFactor;
        r4 = r13.mCurItem;
        r4 = r4 + -1;
        r12 = r3;
        r3 = r5;
        r5 = r4;
        r4 = r2;
        r2 = r12;
    L_0x007f:
        if (r5 < 0) goto L_0x008c;
    L_0x0081:
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r11 = r11 - r10;
        r11 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1));
        if (r11 < 0) goto L_0x0147;
    L_0x0088:
        if (r5 >= r7) goto L_0x0147;
    L_0x008a:
        if (r0 != 0) goto L_0x011f;
    L_0x008c:
        r5 = r6.widthFactor;
        r3 = r4 + 1;
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1));
        if (r0 >= 0) goto L_0x00b9;
    L_0x0096:
        r0 = r13.mItems;
        r0 = r0.size();
        if (r3 >= r0) goto L_0x0177;
    L_0x009e:
        r0 = r13.mItems;
        r0 = r0.get(r3);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
    L_0x00a6:
        r2 = r13.mCurItem;
        r2 = r2 + 1;
        r12 = r2;
        r2 = r5;
        r5 = r12;
    L_0x00ad:
        if (r5 >= r8) goto L_0x00b9;
    L_0x00af:
        r7 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r7 < 0) goto L_0x01a4;
    L_0x00b5:
        if (r5 <= r9) goto L_0x01a4;
    L_0x00b7:
        if (r0 != 0) goto L_0x017a;
    L_0x00b9:
        r13.calculatePageOffsets(r6, r4, r1);
    L_0x00bc:
        r1 = r13.mAdapter;
        r2 = r13.mCurItem;
        if (r6 == 0) goto L_0x01de;
    L_0x00c2:
        r0 = r6.object;
    L_0x00c4:
        r1.setPrimaryItem(r13, r2, r0);
        r0 = r13.mAdapter;
        r0.finishUpdate(r13);
        r0 = r13.mDrawingOrder;
        if (r0 == 0) goto L_0x01e1;
    L_0x00d0:
        r0 = 1;
        r2 = r0;
    L_0x00d2:
        if (r2 == 0) goto L_0x00df;
    L_0x00d4:
        r0 = r13.mDrawingOrderedChildren;
        if (r0 != 0) goto L_0x01e5;
    L_0x00d8:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r13.mDrawingOrderedChildren = r0;
    L_0x00df:
        r3 = r13.getChildCount();
        r0 = 0;
        r1 = r0;
    L_0x00e5:
        if (r1 >= r3) goto L_0x01ec;
    L_0x00e7:
        r4 = r13.getChildAt(r1);
        r0 = r4.getLayoutParams();
        r0 = (android.support.v4.view.ViewPager.LayoutParams) r0;
        r0.childIndex = r1;
        r5 = r0.isDecor;
        if (r5 != 0) goto L_0x010c;
    L_0x00f7:
        r5 = r0.widthFactor;
        r6 = 0;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 != 0) goto L_0x010c;
    L_0x00fe:
        r5 = r13.infoForChild(r4);
        if (r5 == 0) goto L_0x010c;
    L_0x0104:
        r6 = r5.widthFactor;
        r0.widthFactor = r6;
        r5 = r5.position;
        r0.position = r5;
    L_0x010c:
        if (r2 == 0) goto L_0x0113;
    L_0x010e:
        r0 = r13.mDrawingOrderedChildren;
        r0.add(r4);
    L_0x0113:
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x00e5;
    L_0x0117:
        r0 = r2 + 1;
        r2 = r0;
        goto L_0x003e;
    L_0x011c:
        r0 = 0;
        goto L_0x0074;
    L_0x011f:
        r11 = r0.position;
        if (r5 != r11) goto L_0x0141;
    L_0x0123:
        r11 = r0.scrolling;
        if (r11 != 0) goto L_0x0141;
    L_0x0127:
        r11 = r13.mItems;
        r11.remove(r2);
        r11 = r13.mAdapter;
        r0 = r0.object;
        r11.destroyItem(r13, r5, r0);
        r2 = r2 + -1;
        r4 = r4 + -1;
        if (r2 < 0) goto L_0x0145;
    L_0x0139:
        r0 = r13.mItems;
        r0 = r0.get(r2);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
    L_0x0141:
        r5 = r5 + -1;
        goto L_0x007f;
    L_0x0145:
        r0 = 0;
        goto L_0x0141;
    L_0x0147:
        if (r0 == 0) goto L_0x015f;
    L_0x0149:
        r11 = r0.position;
        if (r5 != r11) goto L_0x015f;
    L_0x014d:
        r0 = r0.widthFactor;
        r3 = r3 + r0;
        r2 = r2 + -1;
        if (r2 < 0) goto L_0x015d;
    L_0x0154:
        r0 = r13.mItems;
        r0 = r0.get(r2);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
        goto L_0x0141;
    L_0x015d:
        r0 = 0;
        goto L_0x0141;
    L_0x015f:
        r0 = r2 + 1;
        r0 = r13.addNewItem(r5, r0);
        r0 = r0.widthFactor;
        r3 = r3 + r0;
        r4 = r4 + 1;
        if (r2 < 0) goto L_0x0175;
    L_0x016c:
        r0 = r13.mItems;
        r0 = r0.get(r2);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
        goto L_0x0141;
    L_0x0175:
        r0 = 0;
        goto L_0x0141;
    L_0x0177:
        r0 = 0;
        goto L_0x00a6;
    L_0x017a:
        r7 = r0.position;
        if (r5 != r7) goto L_0x019e;
    L_0x017e:
        r7 = r0.scrolling;
        if (r7 != 0) goto L_0x019e;
    L_0x0182:
        r7 = r13.mItems;
        r7.remove(r3);
        r7 = r13.mAdapter;
        r0 = r0.object;
        r7.destroyItem(r13, r5, r0);
        r0 = r13.mItems;
        r0 = r0.size();
        if (r3 >= r0) goto L_0x01a2;
    L_0x0196:
        r0 = r13.mItems;
        r0 = r0.get(r3);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
    L_0x019e:
        r5 = r5 + 1;
        goto L_0x00ad;
    L_0x01a2:
        r0 = 0;
        goto L_0x019e;
    L_0x01a4:
        if (r0 == 0) goto L_0x01c2;
    L_0x01a6:
        r7 = r0.position;
        if (r5 != r7) goto L_0x01c2;
    L_0x01aa:
        r0 = r0.widthFactor;
        r2 = r2 + r0;
        r3 = r3 + 1;
        r0 = r13.mItems;
        r0 = r0.size();
        if (r3 >= r0) goto L_0x01c0;
    L_0x01b7:
        r0 = r13.mItems;
        r0 = r0.get(r3);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
        goto L_0x019e;
    L_0x01c0:
        r0 = 0;
        goto L_0x019e;
    L_0x01c2:
        r0 = r13.addNewItem(r5, r3);
        r3 = r3 + 1;
        r0 = r0.widthFactor;
        r2 = r2 + r0;
        r0 = r13.mItems;
        r0 = r0.size();
        if (r3 >= r0) goto L_0x01dc;
    L_0x01d3:
        r0 = r13.mItems;
        r0 = r0.get(r3);
        r0 = (android.support.v4.view.ViewPager.ItemInfo) r0;
        goto L_0x019e;
    L_0x01dc:
        r0 = 0;
        goto L_0x019e;
    L_0x01de:
        r0 = 0;
        goto L_0x00c4;
    L_0x01e1:
        r0 = 0;
        r2 = r0;
        goto L_0x00d2;
    L_0x01e5:
        r0 = r13.mDrawingOrderedChildren;
        r0.clear();
        goto L_0x00df;
    L_0x01ec:
        if (r2 == 0) goto L_0x01f5;
    L_0x01ee:
        r0 = r13.mDrawingOrderedChildren;
        r1 = sPositionComparator;
        java.util.Collections.sort(r0, r1);
    L_0x01f5:
        r0 = r13.hasFocus();
        if (r0 == 0) goto L_0x0012;
    L_0x01fb:
        r0 = r13.findFocus();
        if (r0 == 0) goto L_0x022e;
    L_0x0201:
        r0 = r13.infoForAnyChild(r0);
    L_0x0205:
        if (r0 == 0) goto L_0x020d;
    L_0x0207:
        r0 = r0.position;
        r1 = r13.mCurItem;
        if (r0 == r1) goto L_0x0012;
    L_0x020d:
        r0 = 0;
    L_0x020e:
        r1 = r13.getChildCount();
        if (r0 >= r1) goto L_0x0012;
    L_0x0214:
        r1 = r13.getChildAt(r0);
        r2 = r13.infoForChild(r1);
        if (r2 == 0) goto L_0x022b;
    L_0x021e:
        r2 = r2.position;
        r3 = r13.mCurItem;
        if (r2 != r3) goto L_0x022b;
    L_0x0224:
        r2 = 2;
        r1 = r1.requestFocus(r2);
        if (r1 != 0) goto L_0x0012;
    L_0x022b:
        r0 = r0 + 1;
        goto L_0x020e;
    L_0x022e:
        r0 = 0;
        goto L_0x0205;
    L_0x0230:
        r6 = r0;
        goto L_0x0065;
    L_0x0233:
        r0 = r3;
        goto L_0x005a;
    L_0x0236:
        r1 = r0;
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.ViewPager.populate(int):void");
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate((ViewGroup) this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo itemInfo = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, itemInfo.position, itemInfo.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        PagerAdapter pagerAdapter2 = this.mAdapter;
        this.mAdapter = pagerAdapter;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            this.mFirstLayout = true;
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else {
                populate();
            }
        }
        if (this.mAdapterChangeListener != null && pagerAdapter2 != pagerAdapter) {
            this.mAdapterChangeListener.onAdapterChanged(pagerAdapter2, pagerAdapter);
        }
    }

    void setChildrenDrawingOrderEnabledCompat(boolean z) {
        if (this.mSetChildrenDrawingOrderEnabled == null) {
            try {
                this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
            } catch (Throwable e) {
                Log.e(TAG, "Can't find setChildrenDrawingOrderEnabled", e);
            }
        }
        try {
            this.mSetChildrenDrawingOrderEnabled.invoke(this, new Object[]{Boolean.valueOf(z)});
        } catch (Throwable e2) {
            Log.e(TAG, "Error changing children drawing order", e2);
        }
    }

    public void setCurrentItem(int i) {
        this.mPopulatePending = false;
        setCurrentItemInternal(i, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int i, boolean z) {
        this.mPopulatePending = false;
        setCurrentItemInternal(i, z, false);
    }

    void setCurrentItemInternal(int i, boolean z, boolean z2) {
        setCurrentItemInternal(i, z, z2, 0);
    }

    void setCurrentItemInternal(int i, boolean z, boolean z2, int i2) {
        boolean z3 = false;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(false);
        } else if (z2 || this.mCurItem != i || this.mItems.size() == 0) {
            if (i < 0) {
                i = 0;
            } else if (i >= this.mAdapter.getCount()) {
                i = this.mAdapter.getCount() - 1;
            }
            int i3 = this.mOffscreenPageLimit;
            if (i > this.mCurItem + i3 || i < this.mCurItem - i3) {
                for (int i4 = 0; i4 < this.mItems.size(); i4++) {
                    ((ItemInfo) this.mItems.get(i4)).scrolling = true;
                }
            }
            if (this.mCurItem != i) {
                z3 = true;
            }
            populate(i);
            scrollToItem(i, z, i2, z3);
        } else {
            setScrollingCacheEnabled(false);
        }
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener onPageChangeListener) {
        OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = onPageChangeListener;
        return onPageChangeListener2;
    }

    public void setOffscreenPageLimit(int i) {
        if (i < 1) {
            Log.w(TAG, "Requested offscreen page limit " + i + " too small; defaulting to " + 1);
            i = 1;
        }
        if (i != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = i;
            populate();
        }
    }

    void setOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
        this.mAdapterChangeListener = onAdapterChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setPageMargin(int i) {
        int i2 = this.mPageMargin;
        this.mPageMargin = i;
        int width = getWidth();
        recomputeScrollPosition(width, width, i, i2);
        requestLayout();
    }

    public void setPageMarginDrawable(int i) {
        setPageMarginDrawable(getContext().getResources().getDrawable(i));
    }

    public void setPageMarginDrawable(Drawable drawable) {
        this.mMarginDrawable = drawable;
        if (drawable != null) {
            refreshDrawableState();
        }
        setWillNotDraw(drawable == null);
        invalidate();
    }

    public void setPageTransformer(boolean z, PageTransformer pageTransformer) {
        int i = 1;
        if (VERSION.SDK_INT >= 11) {
            boolean z2 = pageTransformer != null;
            int i2 = z2 != (this.mPageTransformer != null) ? 1 : 0;
            this.mPageTransformer = pageTransformer;
            setChildrenDrawingOrderEnabledCompat(z2);
            if (z2) {
                if (z) {
                    i = 2;
                }
                this.mDrawingOrder = i;
            } else {
                this.mDrawingOrder = 0;
            }
            if (i2 != 0) {
                populate();
            }
        }
    }

    void smoothScrollTo(int i, int i2) {
        smoothScrollTo(i, i2, 0);
    }

    void smoothScrollTo(int i, int i2, int i3) {
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int i4 = i - scrollX;
        int i5 = i2 - scrollY;
        if (i4 == 0 && i5 == 0) {
            completeScroll(false);
            populate();
            setScrollState(0);
            return;
        }
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int width = getWidth();
        int i6 = width / 2;
        float f = (float) i6;
        float f2 = (float) i6;
        float distanceInfluenceForSnapDuration = distanceInfluenceForSnapDuration(Math.min(1.0f, (((float) Math.abs(i4)) * 1.0f) / ((float) width)));
        int abs = Math.abs(i3);
        if (abs > 0) {
            width = Math.round(1000.0f * Math.abs(((f2 * distanceInfluenceForSnapDuration) + f) / ((float) abs))) * 4;
        } else {
            width = (int) (((((float) Math.abs(i4)) / ((((float) width) * this.mAdapter.getPageWidth(this.mCurItem)) + ((float) this.mPageMargin))) + 1.0f) * 100.0f);
        }
        this.mScroller.startScroll(scrollX, scrollY, i4, i5, Math.min(width, MAX_SETTLE_DURATION));
        ViewCompat.postInvalidateOnAnimation(this);
    }

    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }
}
