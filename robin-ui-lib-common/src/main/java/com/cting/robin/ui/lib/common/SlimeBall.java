package com.cting.robin.ui.lib.common;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.cting.common.R;
import com.cting.robin.ui.lib.common.obj.RobinCircle;
import com.cting.robin.ui.lib.common.utils.AngleUtils;
import com.cting.robin.ui.lib.common.utils.PointUtils;

import java.util.ArrayList;


public class SlimeBall extends View implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    public static final String TAG = "cting/RobinCircle";
    public static final boolean DEBUG = false;
    public static final float LIMIT_DISTANCE_RATIO = 8;
    public static final float LIMIT_RADIUS_RATIO = 0.4f;
    public static final float CONTROL_POINT_RATIO = 0.35f;
    public static final int RESET_STATE_ANIMATION_DURATION = 300;

    private RobinCircle mHandleCircle;
    private RobinCircle mTargetCircle;
    private float mRadius = 0;
    private float mLimitRadius = 0;
    private float mLimitDistance = 0;
    private Paint mSrcPaint;
    private Paint mSlimePaint;
    private Path mSlimePath = new Path();

    private boolean mIsDragging = false;
    private boolean mIsAnimating = false;
    private boolean mIsDragBreak = false;
    private int mTouchY;
    private int mTouchX;

    private PathMeasure mPathMeasure = new PathMeasure();
    private Path mMeasurePath = new Path();
    private float[] mSlimeControlPoint = new float[2];
    RobinCircle.PointEvaluator mRadiusEvaluator = new RobinCircle.PointEvaluator();

    //for debug
    private ArrayList<String> mDebugTexts;
    private int mDebugTextHeight;
    private int mTextSize;
    private float mCenterDistance;

    public SlimeBall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
        initPaint();
        initCircle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup parent = (ViewGroup) getParent();
        Log.i(TAG, "onAttachedToWindow: rootView=" + parent);
        if (parent != null && parent.getClipChildren()) {
            parent.setClipChildren(false);
        }
    }

    protected void initData() {
        mRadius = getResources().getDimensionPixelOffset(R.dimen.slime_ball_handle_circle_radius);
        mTextSize = getResources().getDimensionPixelOffset(R.dimen.slime_ball_text_size);
        mLimitDistance = mRadius * LIMIT_DISTANCE_RATIO;
        mLimitRadius = mRadius * LIMIT_RADIUS_RATIO;
    }

    protected void initPaint() {
        mSrcPaint = new Paint();
        mSrcPaint.setColor(Color.RED);
        mSrcPaint.setStyle(Paint.Style.FILL);
        mSrcPaint.setTextSize(mTextSize / 3);
        mSrcPaint.setAntiAlias(true);
        Rect rect = new Rect();
        mSrcPaint.getTextBounds("X", 0, 1, rect);
        mDebugTextHeight = rect.height() * 2;

        mSlimePaint = new Paint(mSrcPaint);
        mSlimePaint.setTextSize(mTextSize);

    }

    protected void initCircle() {
        mTargetCircle = new RobinCircle().paint(mSrcPaint);
        mHandleCircle = new RobinCircle(mTargetCircle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = (int) (mRadius * 3);
        int height = width;
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float centerX = w / 2;
        float centerY = h / 2;
        mTargetCircle.center(centerX, centerY).radius(mRadius);
        mHandleCircle.center(centerX, centerY).radius(mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mSlimePath, mSlimePaint);
        mTargetCircle.draw(canvas);
        if (DEBUG) {
            Log.i(TAG, "onDraw: mIsDragging=" + mIsDragging + ",mIsAnimating=" + mIsAnimating);
        }
        if (mIsDragging || mIsAnimating) {
            if (mHandleCircle != null) {
                mHandleCircle.draw(canvas);
            }
        }

        if (DEBUG && mDebugTexts != null && mDebugTexts.size() > 0) {
            //test for center line
//            canvas.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y, mSrcPaint);
            int baseLine = mDebugTextHeight;
            for (String text : mDebugTexts) {
                canvas.drawText(text, 0, baseLine, mSrcPaint);
                baseLine += mDebugTextHeight;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsAnimating) {
            return false;
        }
        mTouchX = (int) event.getX();
        mTouchY = (int) event.getY();
        Log.i(TAG, "onTouchEvent: (" + mTouchX + "," + mTouchY + ")");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsDragging = mHandleCircle.onTouch(mTouchX, mTouchY);
                Log.i(TAG, "onTouchEvent: DOWN----mIsDragging=" + mIsDragging);
                onDrag();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: MOVE----(" + mTouchX + "," + mTouchY + ")");
                onDrag();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "onTouchEvent: UP  ----");
                mIsDragging = false;
                resetState(true);
                break;
        }

        invalidate();
        return mIsDragging || super.onTouchEvent(event);
    }

    private void onDrag() {
        if (mIsDragging) {
            mHandleCircle.drag(mTouchX, mTouchY);
            updateTargetRadius(mHandleCircle.getCenter(), mTargetCircle.getCenter(), -1);
            updateSlime(mTargetCircle, mHandleCircle);
        }
    }

    private void updateSlime(RobinCircle fromCircle, RobinCircle toCircle) {
        mMeasurePath.reset();
        mSlimePath.reset();
        PointF fromPoint = fromCircle.getCenter();
        PointF toPoint = toCircle.getCenter();

        int angle = AngleUtils.getAngle(fromPoint, toPoint);
        if (angle != AngleUtils.INVALID_ANGLE) {
            fromCircle.measureArc(mPathMeasure, angle + 90, -180);
            toCircle.measureArc(mPathMeasure, angle + 90, 180);
            fromCircle.getArc().slimeTo(mSlimePath, toCircle.getArc(), mSlimeControlPoint);
        }

        if (DEBUG) {
            if (mDebugTexts == null) {
                mDebugTexts = new ArrayList<>(5);
            } else {
                mDebugTexts.clear();
            }
            mDebugTexts.add("rem: " + mTargetCircle);
            mDebugTexts.add("src: " + mHandleCircle);
            mDebugTexts.add("limit radius: " + mLimitRadius);
            mDebugTexts.add("limit distance: " + mLimitDistance);
            mDebugTexts.add("control point(" + mSlimeControlPoint[0] + "," + mSlimeControlPoint[1] + ")");
            mDebugTexts.add("angle=" + angle);
            mDebugTexts.add("distance: " + mCenterDistance);
            mDebugTexts.add("touch(" + mTouchX + "," + mTouchY + ")");
        }
    }

    private void updateTargetRadius(PointF from, PointF to, float animFraction) {
        mCenterDistance = PointUtils.getPositionFromLine(mMeasurePath, mPathMeasure, from, to,
                CONTROL_POINT_RATIO, mSlimeControlPoint);

        if (mIsDragBreak) {
        } else if (mCenterDistance >= mLimitDistance) {
            Toast.makeText(getContext(), "Drag break!", Toast.LENGTH_SHORT).show();
//            mTargetCircle.radius(0);
            mIsDragBreak = true;
        }
        if (mCenterDistance < mLimitDistance) {
            float radius = -1;
            if (mIsDragging) {
                radius = mRadiusEvaluator.evaluate(mCenterDistance / mLimitDistance, mRadius, mLimitRadius);
            } else if (mIsAnimating) {
                radius = mRadiusEvaluator.evaluate(animFraction, mTargetCircle.getRadius(), mRadius);
            }
            if (radius > 0) {
                mTargetCircle.radius(radius);
            }
        }
    }

    private void resetState(boolean anim) {
        if (anim) {
            animateCircleState();
        } else {
            mTargetCircle.radius(mHandleCircle.getRadius());
            mHandleCircle.center(mTargetCircle.getCenter());
        }
    }

    private void animateCircleState() {
        PointF start = mHandleCircle.getCenter();
        PointF end = mTargetCircle.getCenter();
        Log.i(TAG, "onTouchEvent: from " + start + " to " + end);
        ValueAnimator animator = ValueAnimator.ofObject(new RobinCircle.CenterEvaluator(), start, end);
        animator.addUpdateListener(this);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(this);
        animator.setDuration(RESET_STATE_ANIMATION_DURATION);
        animator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mIsAnimating = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mIsAnimating = false;
        mIsDragBreak = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mIsAnimating = false;
        mIsDragBreak = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        PointF p = (PointF) animation.getAnimatedValue();
        Log.i(TAG, "onAnimationUpdate: " + p);
        mHandleCircle.center(p);
        updateTargetRadius(mHandleCircle.getCenter(), mTargetCircle.getCenter(), animation.getAnimatedFraction());
        updateSlime(mTargetCircle, mHandleCircle);
        invalidate();
    }


}
