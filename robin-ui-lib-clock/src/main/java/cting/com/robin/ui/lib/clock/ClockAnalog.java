package cting.com.robin.ui.lib.clock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Calendar;

import cting.com.robin.robin.ui.lib.clock.R;
import cting.com.robin.ui.lib.clock.hand.ImageClockHand;
import cting.com.robin.ui.lib.clock.utils.AnalogDecor;
import cting.com.robin.ui.lib.clock.utils.ClockUtils;

public class ClockAnalog extends ClockPrototype {
    public static final int DECOR_TYPE_BLUE = 1;
    public static final int DECOR_TYPE_SILVER = 2;
    public static final double DECOR_TYPE_DARK=3;
    private AnalogDecor mDecor;

    public ClockAnalog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(mDecor.getDialBg());
        mClockType = TYPE_ANALOG;
        mHasSecond = true;
    }

    @Override
    protected void readAttribute(Context context, AttributeSet attrs) {
        super.readAttribute(context, attrs);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ClockAnalog);
        int decorType = ta.getInt(R.styleable.ClockAnalog_clock_analog_decor, DECOR_TYPE_BLUE);
        if (decorType == DECOR_TYPE_SILVER) {
            mDecor = new AnalogDecor(ClockUtils.ANALOG_DECOR_SILVER);
        } else {
            mDecor = new AnalogDecor(ClockUtils.ANALOG_DECOR_BLUE);
        }
        mDecor.setDialBg(ta.getResourceId(R.styleable.ClockAnalog_clock_dial_background, -1));
        mDecor.setHandHour(ta.getResourceId(R.styleable.ClockAnalog_clock_hand_hour, -1));
        mDecor.setHandMinute(ta.getResourceId(R.styleable.ClockAnalog_clock_hand_minute, -1));
        mDecor.setHandSecond(ta.getResourceId(R.styleable.ClockAnalog_clock_hand_second, -1));
        ta.recycle();
    }

    @Override
    protected void initHands() {
        m24HourHand = new ImageClockHand(this, Calendar.HOUR_OF_DAY, mDecor.getHandHour());
        mMinuteHand = new ImageClockHand(this, Calendar.MINUTE, mDecor.getHandMinute());
        mSecondHand = new ImageClockHand(this, Calendar.SECOND, mDecor.getHandSecond());
    }

    @Override
    protected void drawAnalogClock(Canvas canvas) {
        //draw hour
        m24HourHand.draw(canvas);

        //draw minute
        mMinuteHand.draw(canvas);

        //draw second if needed
        if (mHasSecond) {
            mSecondHand.draw(canvas);
        }
    }
}
