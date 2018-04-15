package cting.com.robineeee.ui.lib.clock.hand;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Calendar;

import cting.com.robineeee.ui.lib.clock.IClockView;

public class ClockHand {
    private int calendarFiled;
    protected int time;
    protected int ratio;
    protected Paint paint;
    protected IClockView clockView;

    public ClockHand(IClockView clockView, int calendarFiled) {
        this(clockView, calendarFiled, new Paint());
    }

    public ClockHand(IClockView clockView, int calendarFiled, Paint paint) {
        this.clockView = clockView;
        this.calendarFiled = calendarFiled;
        if (!paint.isAntiAlias()) {
            paint.setAntiAlias(true);
        }
        this.paint = paint;
        this.ratio = getRadio();
    }

    private int getRadio() {
        switch (calendarFiled) {
            case Calendar.HOUR_OF_DAY:
                return 360 / 12;
            case Calendar.MINUTE:
                return 360 / 60;
            case Calendar.SECOND:
                return 360 / 60;
            default:
                throw new RuntimeException("filed " + calendarFiled + " not allowed!");
        }
    }

    public void setTime(Calendar calendar) {
        this.time = calendar.get(calendarFiled);
    }

    public void onSizeChanged(int oldDiameter, int newDiameter) {

    }

    public void draw(Canvas canvas) {
        int centerX = clockView.getCenterX();
        int centerY = clockView.getCenterY();
        canvas.save();
        canvas.rotate(time * ratio, centerX, centerY);
        canvas.drawLine(centerX, centerX, centerX, getHandEnd(), paint);
        canvas.restore();
    }

    private int getHandEnd() {
        int padding = (int) paint.getStrokeWidth();
        int diameter = clockView.getDiameter();
        switch (calendarFiled) {
            case Calendar.HOUR_OF_DAY:
                return padding + diameter / 4;
            case Calendar.MINUTE:
                return padding + diameter / 8;
            case Calendar.SECOND:
                return padding + diameter / 16;
        }
        return padding;
    }
}
