package com.jnsw.android.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;

import com.jnsw.core.util.L;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fox on 2015/8/22.
 */
public class RightLeftDragerLayout extends LinearLayout {
    private static final float TOUCH_SLOP_SENSITIVITY = 1.f;
    private final ViewDragHelper mLeftDragger;
    private final ViewDragHelper mRightDragger;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragCallback mRightCallback;

    public void setDragListener(RightLeftDragListener mListener) {
        this.mListener = mListener;
    }

    private RightLeftDragListener mListener;

    private int mMinDrawerMargin;
    private static final int MIN_DRAWER_MARGIN = 64; // dp
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    public RightLeftDragerLayout(Context context) {
        this(context, null);
    }

    public RightLeftDragerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightLeftDragerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLeftCallback = new ViewDragCallback(Gravity.LEFT);
        mRightCallback = new ViewDragCallback(Gravity.RIGHT);


        final float density = getResources().getDisplayMetrics().density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);
        final float minVel = MIN_FLING_VELOCITY * density;

        mLeftDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, mLeftCallback);
        mLeftDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mLeftDragger.setMinVelocity(minVel);
        mLeftCallback.setDragger(mLeftDragger);

        mRightDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, mRightCallback);
        mRightDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        mRightDragger.setMinVelocity(minVel);
        mRightCallback.setDragger(mRightDragger);


    }

    @IntDef({STATE_IDLE, STATE_DRAGGING, STATE_SETTLING})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {
    }


    /**
     * Indicates that any drawers are in an idle, settled state. No animation is in progress.
     */
    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;

    /**
     * Indicates that a drawer is currently being dragged by the user.
     */
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;

    /**
     * Indicates that a drawer is in the process of settling to a final position.
     */
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;

    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = GravityCompat.getAbsoluteGravity(gravity,
                ViewCompat.getLayoutDirection(child));
        return (absGravity & (Gravity.LEFT | Gravity.RIGHT)) != 0;
    }

    boolean checkDrawerViewAbsoluteGravity(View drawerView, int checkFor) {
        final int absGravity = getDrawerViewAbsoluteGravity(drawerView);
        return (absGravity & checkFor) == checkFor;
    }

    int getDrawerViewAbsoluteGravity(View drawerView) {
        final int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
        return GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
    }

    boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }
    private float mInitialMotionX;
    private float mInitialMotionY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        // "|" used deliberately here; both methods should be invoked.
        final boolean interceptForDrag = mLeftDragger.shouldInterceptTouchEvent(ev) |
                mRightDragger.shouldInterceptTouchEvent(ev);

        boolean interceptForTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;
//                if (mScrimOpacity > 0) {
                final View child = mLeftDragger.findTopChildUnder((int) x, (int) y);
                if (child != null && isContentView(child)) {
//                    interceptForTap = true;
                    interceptForTap =false;
                }
//                }
//                mDisallowInterceptRequested = false;
//                mChildrenCanceledTouch = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // If we cross the touch slop, don't perform the delayed peek for an edge touch.
                if (mLeftDragger.checkTouchSlop(ViewDragHelper.DIRECTION_ALL)) {
                    mLeftCallback.removeCallbacks();
                    mRightCallback.removeCallbacks();
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
//                closeDrawers(true);
//                mDisallowInterceptRequested = false;
//                mChildrenCanceledTouch = false;
            }
        }

        return interceptForDrag || interceptForTap;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mLeftDragger.processTouchEvent(ev);
        mRightDragger.processTouchEvent(ev);

        final int action = ev.getAction();
        boolean wantTouchEvents = true;

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;
//                mDisallowInterceptRequested = false;
//                mChildrenCanceledTouch = false;
                break;
            }

            case MotionEvent.ACTION_UP: {
                final float x = ev.getX();
                final float y = ev.getY();
                boolean peekingOnly = true;
                final View touchedView = mLeftDragger.findTopChildUnder((int) x, (int) y);
                if (touchedView != null && isContentView(touchedView)) {
                    final float dx = x - mInitialMotionX;
                    final float dy = y - mInitialMotionY;
//                    touchedView.performAccessibilityAction(action,null);
                    final int slop = mLeftDragger.getTouchSlop();
//                    if (dx * dx + dy * dy < slop * slop) {
                        // Taps close a dimmed open drawer but only if it isn't locked open.
//                        final View openDrawer = findOpenDrawer();
//                        if (openDrawer != null) {
//                            peekingOnly = getDrawerLockMode(openDrawer) == LOCK_MODE_LOCKED_OPEN;
//                        }
//                    }
                }

//                closeDrawers(peekingOnly);
//                mDisallowInterceptRequested = false;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
//                closeDrawers(true);
//                mDisallowInterceptRequested = false;
//                mChildrenCanceledTouch = false;
                break;
            }
        }

        return wantTouchEvents;
    }
//    View findOpenDrawer() {
//        final int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = getChildAt(i);
//            if (((LayoutParams) child.getLayoutParams()).knownOpen) {
//                return child;
//            }
//        }
//        return null;
//    }

    void dispatchOnDrawerClosed(View drawerView) {
//        final LayoutParams lp = (LayoutParams) drawerView.getLayoutParams();
//        if (lp.knownOpen) {
//            lp.knownOpen = false;
//            if (mListener != null) {
//                mListener.onDrawerClosed(drawerView);
//            }
//
//            updateChildrenImportantForAccessibility(drawerView, false);
//
//            // Only send WINDOW_STATE_CHANGE if the host has window focus. This
//            // may change if support for multiple foreground windows (e.g. IME)
//            // improves.
//            if (hasWindowFocus()) {
//                final View rootView = getRootView();
//                if (rootView != null) {
//                    rootView.sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
//                }
//            }
//        }
    }

    public interface RightLeftDragListener {
        public void onLeftSlide(View drawerView, float slideOffset);

        public void onRightSlide(View drawerView, float slideOffset);

        public void onLeftOpened(View drawerView);

        public void onRightOpened(View drawerView);

        /**
         * Called when a drawer has settled in a completely closed state.
         *
         * @param drawerView Drawer view that is now closed
         */
        public void onLeftClosed(View drawerView);

        public void onRightClosed(View drawerView);

        /**
         * Called when the drawer motion state changes. The new state will
         * be one of {@link #STATE_IDLE}, {@link #STATE_DRAGGING} or {@link #STATE_SETTLING}.
         *
         * @param newState The new drawer motion state
         */
        public void onLeftStateChanged(@State int newState);

        public void onRightStateChanged(@State int newState);
    }

    private void updateChildrenImportantForAccessibility(View drawerView, boolean isDrawerOpen) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (!isDrawerOpen && !isDrawerView(child)
                    || isDrawerOpen && child == drawerView) {
                // Drawer is closed and this is a content view or this is an
                // open drawer view, so it should be visible.
                ViewCompat.setImportantForAccessibility(child,
                        ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
            } else {
                ViewCompat.setImportantForAccessibility(child,
                        ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
            }
        }
    }

    void updateDrawerState(int forGravity, @State int activeState, View activeDrawer) {
        final int leftState = mLeftDragger.getViewDragState();
        final int rightState = mRightDragger.getViewDragState();

        final int state;
        if (leftState == STATE_DRAGGING || rightState == STATE_DRAGGING) {
            state = STATE_DRAGGING;
        } else if (leftState == STATE_SETTLING || rightState == STATE_SETTLING) {
            state = STATE_SETTLING;
        } else {
            state = STATE_IDLE;
        }

//        if (activeDrawer != null && activeState == STATE_IDLE) {
//            final LayoutParams lp = (LayoutParams) activeDrawer.getLayoutParams();
//            if (lp.onScreen == 0) {
//                dispatchOnDrawerClosed(activeDrawer);
//            } else if (lp.onScreen == 1) {
//                dispatchOnDrawerOpened(activeDrawer);
//            }
//        }
//
//        if (state != mDrawerState) {
//            mDrawerState = state;
//
//            if (mListener != null) {
//                mListener.onDrawerStateChanged(state);
//            }
//        }
    }
    View findDrawerWithGravity(int gravity) {
        final int absHorizGravity = GravityCompat.getAbsoluteGravity(
                gravity, ViewCompat.getLayoutDirection(this)) & Gravity.HORIZONTAL_GRAVITY_MASK;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final int childAbsGravity = getDrawerViewAbsoluteGravity(child);
            if ((childAbsGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == absHorizGravity) {
                return child;
            }
        }
        return null;
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;

        private final Runnable mPeekRunnable = new Runnable() {
            @Override
            public void run() {
                peekDrawer();
            }
        };

        public ViewDragCallback(int gravity) {
            mAbsGravity = gravity;
        }

        public void setDragger(ViewDragHelper dragger) {
            mDragger = dragger;
        }

        public void removeCallbacks() {
            RightLeftDragerLayout.this.removeCallbacks(mPeekRunnable);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // Only capture views where the gravity matches what we're looking for.
            // This lets us use two ViewDragHelpers, one for each side drawer.
            return isDrawerView(child) && checkDrawerViewAbsoluteGravity(child, mAbsGravity);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            updateDrawerState(mAbsGravity, state, mDragger.getCapturedView());
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            float offset;
//            final int childWidth = changedView.getWidth();
//
//            // This reverses the positioning shown in onLayout.
//            if (checkDrawerViewAbsoluteGravity(changedView, Gravity.LEFT)) {
//                offset = (float) (childWidth + left) / childWidth;
//            } else {
//                final int width = getWidth();
//                offset = (float) (width - left) / childWidth;
//            }
//            setDrawerViewOffset(changedView, offset);
//            changedView.setVisibility(offset == 0 ? INVISIBLE : VISIBLE);
            invalidate();
        }

//        @Override
//        public void onViewCaptured(View capturedChild, int activePointerId) {
//            final LayoutParams lp = (LayoutParams) capturedChild.getLayoutParams();
//            lp.isPeeking = false;
//
//            closeOtherDrawer();
//        }

//        private void closeOtherDrawer() {
//            final int otherGrav = mAbsGravity == Gravity.LEFT ? Gravity.RIGHT : Gravity.LEFT;
//            final View toClose = findDrawerWithGravity(otherGrav);
//            if (toClose != null) {
//                closeDrawer(toClose);
//            }
//        }

//        @Override
//        public void onViewReleased(View releasedChild, float xvel, float yvel) {
//            // Offset is how open the drawer is, therefore left/right values
//            // are reversed from one another.
//            final float offset = getDrawerViewOffset(releasedChild);
//            final int childWidth = releasedChild.getWidth();
//
//            int left;
//            if (checkDrawerViewAbsoluteGravity(releasedChild, Gravity.LEFT)) {
//                left = xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth;
//            } else {
//                final int width = getWidth();
//                left = xvel < 0 || xvel == 0 && offset > 0.5f ? width - childWidth : width;
//            }
//
//            mDragger.settleCapturedViewAt(left, releasedChild.getTop());
//            invalidate();
//        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
//            postDelayed(mPeekRunnable, PEEK_DELAY);
        }

        private void peekDrawer() {
            final View toCapture;
            final int childLeft;
            final int peekDistance = mDragger.getEdgeSize();
//            final boolean leftEdge = mAbsGravity == Gravity.LEFT;
//            if (leftEdge) {
//                toCapture = findDrawerWithGravity(Gravity.LEFT);
//                childLeft = (toCapture != null ? -toCapture.getWidth() : 0) + peekDistance;
//            } else {
//                toCapture = findDrawerWithGravity(Gravity.RIGHT);
//                childLeft = getWidth() - peekDistance;
//            }
//            // Only peek if it would mean making the drawer more visible and the drawer isn't locked
//            if (toCapture != null && ((leftEdge && toCapture.getLeft() < childLeft) ||
//                    (!leftEdge && toCapture.getLeft() > childLeft)) &&
//                    getDrawerLockMode(toCapture) == LOCK_MODE_UNLOCKED) {
//                final LayoutParams lp = (LayoutParams) toCapture.getLayoutParams();
//                mDragger.smoothSlideViewTo(toCapture, childLeft, toCapture.getTop());
//                lp.isPeeking = true;
//                invalidate();
//
//                closeOtherDrawer();
//
//                cancelChildViewTouch();
//            }
        }

//        @Override
//        public boolean onEdgeLock(int edgeFlags) {
//            if (ALLOW_EDGE_LOCK) {
//                final View drawer = findDrawerWithGravity(mAbsGravity);
//                if (drawer != null && !isDrawerOpen(drawer)) {
//                    closeDrawer(drawer);
//                }
//                return true;
//            }
//            return false;
//        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            final View toCapture;
            if ((edgeFlags & ViewDragHelper.EDGE_LEFT) == ViewDragHelper.EDGE_LEFT) {
                toCapture = findDrawerWithGravity(Gravity.LEFT);
            } else {
                toCapture = findDrawerWithGravity(Gravity.RIGHT);
            }

            if (toCapture != null ) {
                mDragger.captureChildView(toCapture, pointerId);
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return isDrawerView(child) ? child.getWidth() : 0;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (checkDrawerViewAbsoluteGravity(child, Gravity.LEFT)) {
                return Math.max(-child.getWidth(), Math.min(left, 0));
            } else {
                final int width = getWidth();
                return Math.max(width - child.getWidth(), Math.min(left, width));
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }
    }
}
