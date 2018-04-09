package cting.com.robin.ui.lib.clock;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cting.com.robin.robin.ui.lib.clock.R;
import cting.com.robin.ui.lib.clock.utils.ClockSubject;

public class ClockDigital extends BaseClockView implements ClockSubject.Listener {
    public static final String TAG = "cting/clock/digital";

    private TextView mTimeText;
    private TextView mDateText;
    private ImageView mBackgroundImg;
    private int mLayoutId;

    public ClockDigital(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mClockType = TYPE_DIGITAL;
        setWillNotDraw(true);
        inflate(getContext(), mLayoutId, this);
    }

    @Override
    protected void readAttribute(Context context, AttributeSet attrs) {
        super.readAttribute(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClockDigital);
        mLayoutId = ta.getResourceId(R.styleable.ClockDigital_clock_digital_layout, R.layout.clock_digital_layout);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTimeText = (TextView) findViewById(R.id.clock_digital_time_text);
        mDateText = (TextView) findViewById(R.id.clock_digital_date_text);
        mBackgroundImg = (ImageView) findViewById(R.id.clock_digital_bg);
    }

    @Override
    public void onTimeChanged(Calendar time) {
        SimpleDateFormat format = new SimpleDateFormat();
        long millis = time.getTimeInMillis();

        if (mHasSecond) {
            format.applyPattern("hh:mm:ss");
        } else {
            format.applyPattern("hh:mm");
        }
        mTimeText.setText(format.format(millis));

        format.applyPattern("M/d c");
        mDateText.setText(format.format(millis));

        if (DEBUG) Log.i(TAG, "onTimeChanged: " + mTimeText.getText() + "," + mDateText.getText());
    }

    public void setBackground(@DrawableRes int backgroundResId) {
        if (mBackgroundImg != null) {
            mBackgroundImg.setImageResource(backgroundResId);
        }
    }
}
