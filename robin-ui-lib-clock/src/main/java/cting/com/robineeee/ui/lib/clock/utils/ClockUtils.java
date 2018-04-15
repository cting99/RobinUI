package cting.com.robineeee.ui.lib.clock.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import cting.com.robin.ui.lib.clock.R;

public class ClockUtils {
    public static final AnalogDecor ANALOG_DECOR_SILVER = new AnalogDecor.Builder()
            .dialBg(R.drawable.clock_longines_silver_dial_bg)
            .handHour(R.drawable.clock_longines_silver_hand_hour)
            .handMinute(R.drawable.clock_longines_silver_hand_minute)
            .handSecond(R.drawable.clock_longines_silver_hand_second)
            .build();

    public static final AnalogDecor ANALOG_DECOR_BLUE = new AnalogDecor.Builder()
            .dialBg(R.drawable.clock_longines_blue_dial_bg)
            .handHour(R.drawable.clock_longines_blue_hand_hour)
            .handMinute(R.drawable.clock_longines_blue_hand_minute)
            .handSecond(R.drawable.clock_longines_blue_hand_second)
            .build();

    public static Bitmap getBitmap(Resources res, @DrawableRes int id) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, opts);
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(res, id, opts);
        } catch (OutOfMemoryError e) {
        }
        return bitmap;
    }
}
