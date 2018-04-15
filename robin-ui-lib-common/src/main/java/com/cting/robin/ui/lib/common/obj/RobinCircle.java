package com.cting.robin.ui.lib.common.obj;

import android.animation.TypeEvaluator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public class RobinCircle {
    private float radius;
    private PointF center = new PointF();
    private Paint paint = new Paint();
    private RectF rect = new RectF();
    private RobinArc arc = new RobinArc();

    public RobinCircle() {
    }

    public RobinCircle(RobinCircle circle) {
        center(circle.getCenter());
        this.radius = circle.radius;
        this.paint.set(circle.paint);
    }

    public PointF getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    public RobinCircle center(float centerX, float centerY) {
        center.set(centerX, centerY);
        return this;
    }

    public RobinCircle center(PointF point) {
        center.set(point.x, point.y);
        return this;
    }

    public RobinCircle radius(float radius) {
        this.radius = radius;
        return this;
    }

    public RobinCircle paint(Paint paint) {
        this.paint.set(paint);
        return this;
    }

    public RobinCircle measureArc(PathMeasure pathMeasure, int startAngle, int swapAngle) {
        arc.setStartAngle(startAngle);
        arc.setSwapAngle(swapAngle);
        arc.computePointCoordinate(pathMeasure, getRectF());
        return this;
    }

    public RobinArc getArc() {
        return arc;
    }

    public void draw(Canvas canvas) {
        if (radius > 0) {
            canvas.drawCircle(center.x, center.y, radius, paint);
        }
    }

    public boolean onTouch(int x, int y) {
        float radiusRange = radius * 4 / 3;
        return (center.x - x) * (center.x - x) + (center.y - y) * (center.y - y) <= radiusRange * radiusRange;
    }

    public void drag(int x, int y) {
        center.set(x, y);
    }

    @Override
    public String toString() {
        return "RobinCircle{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }

    public RectF getRectF() {
        rect.set(center.x - radius,
                center.y - radius,
                center.x + radius,
                center.y + radius);
        return rect;
    }

    private static float calculatorF(float fraction, float fromValue, float toValue) {
        return fromValue + fraction * (toValue - fromValue);
    }
    public static class CenterEvaluator implements TypeEvaluator<PointF> {

        private static final String TAG = "cting/Circle/center";

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

            Log.i(TAG, "evaluate: fraction=" + fraction);
            return new PointF(calculatorF(fraction, startValue.x, endValue.x),
                    calculatorF(fraction, startValue.y, endValue.y));
        }
    }

    public static class PointEvaluator implements TypeEvaluator<Float> {

        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            return calculatorF(fraction, startValue, endValue);
        }
    }
}
