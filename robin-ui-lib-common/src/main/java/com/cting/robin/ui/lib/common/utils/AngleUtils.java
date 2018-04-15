package com.cting.robin.ui.lib.common.utils;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class AngleUtils {

    public static final int INVALID_ANGLE = -10000;

    public static final float getGradient(PointF from, PointF to) {
        float xOffset = to.x - from.x;
        if (xOffset == 0) {
            return 0;
        }
        float yOffset = to.y - from.y;
        return yOffset / xOffset;

    }
    public static final int getAngle(PointF from, PointF to) {
        float xOffset = to.x - from.x;
        float yOffset = to.y - from.y;
        double radian = -1;
        if (xOffset != 0 && yOffset != 0) {
            radian = Math.atan2(yOffset, xOffset);
        }
//        Log.i("cting/", "getAngle: xOffset=" + xOffset
//                + ",yOffset=" + yOffset
//                + ",radian=" + radian);
        if (radian == -1) {
            return INVALID_ANGLE;
        }
        int angle = (int) (360 * (radian / (2 * Math.PI)));
        return angle;
    }
}
