package cting.com.robineeee.ui.lib.clock.hand;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import cting.com.robineeee.ui.lib.clock.IClockView;


public class ImageClockHand extends ClockHand {

    protected int handDrawableResId;
    private int bitmapHalfWidth;
    private int bitmapHalfHeight;
    private Drawable drawable;
//    private Bitmap handBitmap;

    public ImageClockHand(IClockView clockView, int calendarFiled, @DrawableRes int handDrawableResId) {
        super(clockView, calendarFiled);
        this.handDrawableResId = handDrawableResId;

        drawable = clockView.getResources().getDrawable(handDrawableResId);
        bitmapHalfWidth = drawable.getIntrinsicWidth() >> 1;
        bitmapHalfHeight = drawable.getIntrinsicHeight() >> 1;
//        handBitmap = getBitmap(handDrawableResId);
//        bitmapHalfWidth = handBitmap.getWidth() >> 1;
//        bitmapHalfHeight = handBitmap.getHeight() >> 1;
    }

    @Override
    public void onSizeChanged(int oldDiameter, int newDiameter) {
        bitmapHalfWidth = bitmapHalfWidth * newDiameter / oldDiameter;
        bitmapHalfHeight = bitmapHalfHeight * newDiameter / oldDiameter;
    }

    @Override
    public void draw(Canvas canvas) {
        int centerX = clockView.getCenterX();
        int centerY = clockView.getCenterY();
        canvas.save();
        canvas.rotate(time * ratio, centerX, centerY);
//        canvas.drawBitmap(handBitmap, centerX - bitmapHalfWidth, centerY - bitmapHalfHeight, paint);

        drawable.setBounds(centerX - bitmapHalfWidth, centerY - bitmapHalfHeight,
                centerX + bitmapHalfWidth, centerY + bitmapHalfHeight);
        drawable.draw(canvas);
        canvas.restore();
    }
}
