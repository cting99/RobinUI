package com.cting.robin.ui.lib.music;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MusicRevolvingRecord extends AppCompatImageView implements View.OnClickListener {

    public interface Callback {
        void onToggle(boolean isRevolving);

        boolean isPlaying();
    }

    public static final String TAG = "cting/music/record";
    public static final int DEGREE_INCRESS_DELTA = 1;
    public static final int ROTATE_UPDATE_DELAY_MILLIONS = 10;

    int mWidth;
    int mHeight;
    int mCentX = -1;
    int mCentY = -1;
    int mRadius = -1;

    private Path mCirclePath = new Path();
    private PaintFlagsDrawFilter mDrawFilter;
    private float mDegree = 0;
    private Matrix mMatrix;
    private Runnable mRotateRun;
    boolean mIsRevolving = false;

    private Callback mCallback;


    public MusicRevolvingRecord(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_CROP);
        mMatrix = new Matrix();
        setImageMatrix(mMatrix);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        mRotateRun = new Runnable() {
            @Override
            public void run() {
                if (mIsRevolving) {
                    mDegree += DEGREE_INCRESS_DELTA;
                    mDegree %= 360;
                    mMatrix.setRotate(mDegree, mCentX, mCentY);
                    invalidate();
                    postDelayed(mRotateRun, ROTATE_UPDATE_DELAY_MILLIONS);
                }
            }
        };
        setOnClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow: ");
        stopRevolve();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCentX = mWidth / 2;
        mCentY = mHeight / 2;
        setupClipPath();
    }

    private void setupClipPath() {
        int radius = Math.min(mWidth, mHeight) / 2;
        if (mRadius != radius) {
            mRadius = radius;
            mCirclePath.reset();
            mCirclePath.addCircle(mCentX, mCentY, mRadius, Path.Direction.CCW);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(mDrawFilter);
        canvas.concat(mMatrix);
        canvas.clipPath(mCirclePath);
        super.onDraw(canvas);
    }

    private void startRevolve() {
        mIsRevolving = true;
        post(mRotateRun);
    }

    private void stopRevolve() {
        mIsRevolving = false;
        removeCallbacks(mRotateRun);
    }

    @Override
    public void onClick(View v) {
        mIsRevolving = !mIsRevolving;
        Log.i(TAG, "onClick: newState=" + mIsRevolving);
        if (mCallback != null) {
            mCallback.onToggle(mIsRevolving);
        } else {
            if (mIsRevolving) {
                stopRevolve();
            } else {
                startRevolve();
            }
        }
    }


    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public void update() {
        if (mCallback == null) {
            return;
        }
        if (mCallback.isPlaying()) {
            startRevolve();
        } else {
            stopRevolve();
        }
    }

    /*
    private Paint testPaint;

    private void getTestPaint() {
        if (testPaint == null) {
            testPaint = new Paint();
            testPaint.setAntiAlias(true);
            testPaint.setColor(Color.RED);
            testPaint.setStrokeWidth(5);
            testPaint.setStyle(Paint.Style.STROKE);
        }
    }
    */
}
