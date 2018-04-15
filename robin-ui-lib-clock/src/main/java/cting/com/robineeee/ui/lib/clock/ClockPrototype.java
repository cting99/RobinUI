package cting.com.robineeee.ui.lib.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Calendar;

import cting.com.robin.ui.lib.clock.R;
import cting.com.robineeee.ui.lib.clock.hand.ClockHand;
import cting.com.robineeee.ui.lib.clock.utils.PaintFactory;
import cting.com.robineeee.ui.lib.clock.utils.ClockSubject;

public class ClockPrototype extends BaseClockView implements ClockSubject.Listener {

    public static final boolean DEBUG_FRAME = false;
    public static final boolean DEBUG_DRAW_DIAL_TEXT = false;
    public static final boolean DEBUG_MEASURE = true;

    protected ClockHand m24HourHand;
    protected ClockHand mMinuteHand;
    protected ClockHand mSecondHand;

    private int diameter;
    private int mCenterX;
    private int mCenterY;

    private float mTimeTextSize;
    private Rect mTextRect = new Rect();
    private Paint mTimePaint;

    private Rect mDialTextRect = new Rect();
    private Paint mDialPaint;

    private static final int DIAL_TEXT_COUNT = 12;
    private float mDegreeDelta = 360 / DIAL_TEXT_COUNT;

    public ClockPrototype(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        diameter = getResources().getDimensionPixelOffset(R.dimen.clock_view_best_diameter);
        mTimeTextSize = getResources().getDimensionPixelSize(R.dimen.clock_time_best_text_size);
        setWillNotDraw(false);
        initHands();
        initPaints();
    }

    protected void initHands() {
        m24HourHand = new ClockHand(this, Calendar.HOUR_OF_DAY,
                new PaintFactory.PaintBuilder(PaintFactory.WHITE_FILL_PEN)
                        .strokeWidth(7)
                        .color(mPrimaryColor)
                        .build());
        mMinuteHand = new ClockHand(this, Calendar.MINUTE,
                new PaintFactory.PaintBuilder(PaintFactory.WHITE_FILL_PEN)
                        .strokeWidth(4)
                        .color(mPrimaryColor)
                        .build());
        mSecondHand = new ClockHand(this, Calendar.SECOND,
                new PaintFactory.PaintBuilder(PaintFactory.WHITE_FILL_PEN)
                        .strokeWidth(2)
                        .color(mPrimaryColor)
                        .build());
    }

    private void initPaints() {
        mTimePaint = new PaintFactory.PaintBuilder(PaintFactory.READ_STROKE_PEN)
                .color(mPrimaryColor)
                .build();

        mDialPaint = new PaintFactory.PaintBuilder(mTimePaint)
                .style(Paint.Style.FILL)
                .strokeWidth(1)
                .build();

        updatePaintTextSize();

    }

    protected void updatePaintTextSize() {
        mTimePaint.setTextSize(mHasSecond ? mTimeTextSize : mTimeTextSize * 6 / 5);
        String textBoundFormat = mHasSecond ? "88:88:88" : "88:88";
        mTimePaint.getTextBounds("textBoundFormat", 0, textBoundFormat.length(), mTextRect);

        mDialPaint.setTextSize(mTimeTextSize / 3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newSize = Math.min(measuredSize(widthMeasureSpec), measuredSize(heightMeasureSpec));
        if (newSize != diameter) {
            if (DEBUG_MEASURE) Log.i(TAG, "onMeasure: diameter from " + diameter + " to " + newSize);
            mTimeTextSize = mTimeTextSize * newSize / diameter;
            updatePaintTextSize();
            m24HourHand.onSizeChanged(diameter,newSize);
            mMinuteHand.onSizeChanged(diameter,newSize);
            mSecondHand.onSizeChanged(diameter,newSize);
            diameter = newSize;
        }
        setMeasuredDimension(diameter, diameter);
    }

    private int measuredSize(int spec) {
        int size = MeasureSpec.getSize(spec);
        int mode = MeasureSpec.getMode(spec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                if (DEBUG_MEASURE) Log.i(TAG, "measuredSize: mode=UNSPECIFIED" + ",size=" + size);
                // parent doesn't constraint this view,it can be decide by self in xml
//                return diameter;
                break;
            case MeasureSpec.AT_MOST:
                if (DEBUG_MEASURE) Log.i(TAG, "measuredSize: mode=AT_MOST" + ",size=" + size);
                // wrap_content as large as this view want
                return diameter;
            case MeasureSpec.EXACTLY:
                // decide by parent
                if (DEBUG_MEASURE) Log.i(TAG, "measuredSize: mode=UNSPECIFIED" + ",size=" + size);
                break;
        }
        return size;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = getWidth() >> 1;
        mCenterY = getHeight() >> 1;
    }

    @Override
    public int getCenterX() {
        return mCenterX;
    }

    @Override
    public int getCenterY() {
        return mCenterY;
    }

    @Override
    public int getDiameter() {
        return diameter;
    }

    @Override
    protected void onShowOrHideSecond() {
        updatePaintTextSize();
    }

    @Override
    public void onTimeChanged(Calendar time) {
        super.onTimeChanged(time);
        m24HourHand.setTime(time);
        mMinuteHand.setTime(time);
        mSecondHand.setTime(time);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mClockType == TYPE_DIGITAL) {
            drawDigitalClock(canvas);
        } else if (mClockType == TYPE_ANALOG) {
            drawAnalogClock(canvas);
        }
    }

    protected void drawDigitalClock(Canvas canvas) {
        drawEnvironment(canvas);

        // draw circle
        canvas.drawCircle(mCenterX, mCenterY, (diameter >> 1) - 3, mTimePaint);

        // draw time text
        int textLeft = (getWidth() - mTextRect.width()) >> 1;
        int textBottom = (getHeight() + mTextRect.height()) >> 1;
        canvas.drawText(mTimeText, textLeft, textBottom, mTimePaint);
    }

    protected void drawAnalogClock(Canvas canvas) {
        drawEnvironment(canvas);

        //  draw clock dial
        drawDial(canvas);

        // draw hands: hour/minute/second
        m24HourHand.draw(canvas);
        mMinuteHand.draw(canvas);
        if (mHasSecond) {
            mSecondHand.draw(canvas);
        }
    }

    private void drawDial(Canvas canvas) {
        // draw circle
        canvas.drawCircle(mCenterX, mCenterY, (diameter >> 1) - 3, mTimePaint);

        int paddingDial = (int) mDialPaint.getStrokeWidth() * 3 / 2;
        canvas.save();
        for (int i = 0; i < DIAL_TEXT_COUNT; i++) {
            canvas.rotate(mDegreeDelta, mCenterX, mCenterY);
            int lineBottom = diameter / 16;
            // draw 12 lines
            canvas.drawLine(mCenterX, paddingDial, mCenterX, lineBottom, mDialPaint);
            // draw 12 numbers from 1 to 12
            if (DEBUG_DRAW_DIAL_TEXT) {
                String dialText = String.valueOf(i + 1);
                mDialPaint.getTextBounds(dialText, 0, dialText.length(), mDialTextRect);
                int textLeft = (getWidth() - mDialTextRect.width()) >> 1;
                int textBottom = mDialTextRect.height();
                canvas.drawText(dialText, textLeft, paddingDial + lineBottom + textBottom, mDialPaint);
            }
        }
        canvas.restore();
    }

    private void drawEnvironment(Canvas canvas) {
        if (DEBUG_FRAME) {
            canvas.drawRect(0, 0, diameter, diameter, mTimePaint);
            canvas.drawPoint(mCenterX, mCenterX, mTimePaint);
            canvas.drawLine(mCenterX, 0, mCenterX, diameter, mTimePaint);
            canvas.drawLine(0, mCenterX, diameter, mCenterX, mTimePaint);
        }
    }

}
