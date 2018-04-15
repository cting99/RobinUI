package com.cting.robin.ui.lib.common.obj;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;

import java.util.Arrays;

public class RobinArc {
    public static final String TAG = "cting/measureArc";
    private int startAngle;
    private int swapAngle;
    private float[] startPoint = new float[2];
    private float[] endPoint = new float[2];
    private Path measurePath = new Path();

    public void computePointCoordinate(PathMeasure pathMeasure, RectF rectF) {
        if (measurePath == null || rectF == null || swapAngle == 0) {
            return;
        }
        measurePath.reset();
        measurePath.addArc(rectF, startAngle, swapAngle);
        pathMeasure.setPath(measurePath, false);
        pathMeasure.getPosTan(0, startPoint, null);
        pathMeasure.getPosTan(pathMeasure.getLength(), endPoint, null);
//        Log.i(TAG, "computePointCoordinate: " + toString());
    }

    public int getStartAngle() {
        return startAngle;
    }

    public int getSwapAngle() {
        return swapAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public void setSwapAngle(int swapAngle) {
        this.swapAngle = swapAngle;
    }

    public float[] getStartPoint() {
        return startPoint;
    }

    public float[] getEndPoint() {
        return endPoint;
    }

    @Override
    public String toString() {
        return "RobinArc{" +
                "(" + startAngle +
                ", " + swapAngle + ")" +
                ", start:" + Arrays.toString(startPoint) +
                ", end:" + Arrays.toString(endPoint) +
                '}';
    }


    public void slimeTo(Path path, RobinArc arc, float[] controlPoint) {
        if (arc == null || path == null || controlPoint == null) {
            return;
        }
        float from[] = startPoint;
        float to[] = arc.startPoint;
        path.moveTo(from[0], from[1]);
        path.quadTo(controlPoint[0], controlPoint[1], to[0], to[1]);

        from = arc.endPoint;
        to = endPoint;
        path.lineTo(from[0],from[1]);
        path.quadTo(controlPoint[0], controlPoint[1], to[0], to[1]);
        path.close();
    }
}
