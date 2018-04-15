package com.cting.robin.ui.lib.common.utils;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;


public class PointUtils {

    public static final int getDistance(Path path, PathMeasure pathMeasure, PointF from, PointF to) {
        path.reset();
        path.moveTo(from.x, from.y);
        path.lineTo(to.x, to.y);
        pathMeasure.setPath(path, false);
        return (int) pathMeasure.getLength();
    }

    public static final int getDistance(PointF from, PointF to) {
        return getDistance(new Path(), new PathMeasure(), from, to);
    }

    public static float getPositionFromLine(Path path, PathMeasure pathMeasure, PointF from, PointF to,
                                            float distanceRatio, float[] desPoint) {
        path.reset();
        path.moveTo(from.x, from.y);
        path.lineTo(to.x, to.y);
        pathMeasure.setPath(path, false);
        float length = pathMeasure.getLength();
        pathMeasure.getPosTan(distanceRatio * length, desPoint, null);
        return length;
    }
}
