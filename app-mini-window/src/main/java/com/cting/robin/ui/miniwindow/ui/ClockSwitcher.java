package com.cting.robin.ui.miniwindow.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;
import android.widget.ViewAnimator;

import cting.com.robineeee.ui.lib.clock.ClockAnalog;
import cting.com.robineeee.ui.lib.clock.ClockDigital;


public class ClockSwitcher extends ViewAnimator {

    public static final String TAG = "cting/mini/clock";
    public static final boolean DEBUG = false;
    private final GestureDetector mDetector;


    public ClockSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetector(getContext(), new MultiViewGesture());

         RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                 Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         rotateAnimation.setDuration(500);
         setInAnimation(rotateAnimation);

//         setOutAnimation(rotateAnimation);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    private class MultiViewGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(TAG, "onDown: ");
            return true;
        }

        private static final int FLING_MIN_DISTANCE = 120;
        private static final int FLING_MIN_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i(TAG, "onFling: ");
            if (Math.abs(e1.getX() - e2.getX()) > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                boolean toLeft = e1.getY() - e2.getY() > 0;
                if (DEBUG)
                    Toast.makeText(getContext(), "onFling: to " + (toLeft ? "left" : "right"), Toast.LENGTH_SHORT).show();
                return switchAnalogs();
//                return flingHorizontal(toLeft);
            } else if (Math.abs(e1.getY() - e2.getY()) > FLING_MIN_DISTANCE && Math.abs(velocityY) > FLING_MIN_VELOCITY) {
                boolean down = e1.getY() - e2.getY() < 0;
                if (DEBUG)
                    Toast.makeText(getContext(), "onFling: to " + (down ? "down" : "up"), Toast.LENGTH_SHORT).show();
                return switchDigitalAndAnalog();
//                return flingVertical(down);
            }
            return false;
        }
    }

    public boolean switchDigitalAndAnalog() {
        View view = getCurrentView();
        View nextView = getNextView();
        if (view instanceof ClockDigital) {
            showNext();
            return true;
        } else if (view instanceof ClockAnalog) {
            if (nextView instanceof ClockDigital) {
                showNext();
                return true;
            } else {
                showPrevious();
                return true;
            }
        }
        return false;
    }

    public boolean switchAnalogs() {
        View view = getCurrentView();
        View nextView = getNextView();
        if (view instanceof ClockAnalog) {
            if (nextView instanceof ClockAnalog) {
                showNext();
                return true;
            } else {
                showPrevious();
                return true;
            }
        }
        return false;

    }


    private View getPreviousView() {
        return getViewAt(getDisplayedChild() - 1);
    }

    private View getNextView() {
        return getViewAt(getDisplayedChild() + 1);
    }

    private View getViewAt(int index) {
        if (index >= getChildCount()) {
            index = 0;
        } else if (index < 0) {
            index = getChildCount() - 1;
        }
        return getChildAt(index);
    }


    /*

    private boolean flingHorizontal(boolean toLeft) {
        applyRotation(true, 0, (toLeft ? 90 : -90));
        return true;
    }

    private boolean flingVertical(boolean down) {
        applyRotation(false, 0, (down ? 90 : -90));
        return true;
    }

    private void applyRotation(boolean rotateY, float start, float end) {
        if (mDuringAnimate) {
            return;
        }

        final float centerX = getWidth() >> 1;
        final float centerY = getHeight() >> 1;

        final Rotate3DAnimation rotation = new Rotate3DAnimation(rotateY, start, end,
                centerX, centerY, 310.0f, true);
        rotation.setDuration(300);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(mAnimationListener);
        startAnimation(rotation);
    }

    private boolean mDuringAnimate = false;
    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mDuringAnimate = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mDuringAnimate = false;
            Rotate3DAnimation oldAnim = (Rotate3DAnimation) animation;
            boolean rotateY = oldAnim.currentRotateY();
            final float centerX = getWidth() >> 1;
            final float centerY = getHeight() >> 1;
            if (rotateY) {
                switchAnalogs();
            } else {
                switchDigitalAndAnalog();
            }
            Rotate3DAnimation rotation = new Rotate3DAnimation(rotateY, oldAnim.getOldEndAngle(), oldAnim.getNewEndAngle(),
                    centerX, centerY, 310.0f, false);
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            startAnimation(rotation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    * */
}
