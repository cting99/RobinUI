package cting.com.robin.ui.lib.clock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cting.com.robin.robin.ui.lib.clock.R;
import cting.com.robin.ui.lib.clock.utils.ClockSubject;

public abstract class BaseClockView extends FrameLayout implements IClockView, ClockSubject.Listener {
    protected static final boolean DEBUG = true;
    protected String TAG = "cting/clock/";

    public static final int TYPE_DIGITAL = 1;
    public static final int TYPE_ANALOG = 2;

    protected boolean mHasSecond = false;

    protected SimpleDateFormat mFormat;
    protected String mTimeText;

    protected int mClockType = TYPE_DIGITAL;
    private boolean mIsVisibleAggregated = false;

    private Runnable mSecondTimer = new Runnable() {
        @Override
        public void run() {
            onTimeChanged(ClockSubject.updateTime());

            if (mHasSecond && mIsVisibleAggregated) {
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);

                if (getHandler() != null) {
                    getHandler().postAtTime(mSecondTimer, next);
                } else {
                    Log.w(TAG, "run: handler not ready");
                }
            } else {
                getHandler().removeCallbacks(mSecondTimer);
            }
        }
    };
    protected int mPrimaryColor;

    public BaseClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TAG += getClass().getSimpleName();
        readAttribute(context, attrs);
    }

    protected void readAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseClockView);
        mHasSecond = ta.getBoolean(R.styleable.BaseClockView_clock_showSecond, mHasSecond);
        mClockType = ta.getInt(R.styleable.BaseClockView_clock_type, mClockType);
        ta.recycle();

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
        mPrimaryColor = outValue.data;

    }


    @Override
    public int getCenterX() {
        return getWidth() >> 1;
    }

    @Override
    public int getCenterY() {
        return getHeight() >> 1;
    }

    @Override
    public int getDiameter() {
        return Math.min(getWidth(), getHeight());
    }

    @Override
    public void onTimeChanged(Calendar time) {
        if (mFormat == null) {
            mFormat = new SimpleDateFormat();
        }
        if (mHasSecond) {
            mFormat.applyPattern("hh:mm:ss");
        } else {
            mFormat.applyPattern("hh:mm");
        }
        mTimeText = mFormat.format(time.getTimeInMillis());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (DEBUG) Log.i(TAG, "onAttachedToWindow: ");
        ClockSubject.getInstance().addListener(getContext(), this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (DEBUG) Log.i(TAG, "onDetachedFromWindow: ");
        ClockSubject.getInstance().removeListener(getContext(), this);
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if (DEBUG) Log.i(TAG, "onVisibilityAggregated: isVisible=" + isVisible);
        mIsVisibleAggregated = isVisible;
        updateSecond();
    }

    public void setHasSecond(boolean hasSecond) {
        if (mHasSecond != hasSecond) {
            mHasSecond = hasSecond;
            onShowOrHideSecond();
            if (getHandler() != null) {
                updateSecond();
            }
        }
    }

    protected void onShowOrHideSecond() {
    }

    protected void updateSecond() {
        if (getHandler() == null) {
            Log.w(TAG, "updateSecond: not ready for second yet");
            return;
        }
        mSecondTimer.run();
    }
}
