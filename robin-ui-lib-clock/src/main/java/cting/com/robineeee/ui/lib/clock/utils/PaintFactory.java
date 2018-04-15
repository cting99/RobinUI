package cting.com.robineeee.ui.lib.clock.utils;

import android.graphics.Color;
import android.graphics.Paint;

public class PaintFactory {
    public static final PaintBuilder READ_STROKE_PEN = new PaintBuilder()
            .color(Color.RED)
            .style(Paint.Style.STROKE)
            .strokeWidth(3);

    public static final PaintBuilder WHITE_FILL_PEN = new PaintBuilder()
            .color(Color.WHITE)
            .style(Paint.Style.FILL)
            .strokeWidth(3);


    public static class PaintBuilder {
        private Paint paint;

        public PaintBuilder() {
            this.paint = new Paint();
        }

        public PaintBuilder(PaintBuilder builder) {
            this.paint = new Paint(builder.build());
        }

        public PaintBuilder(Paint paint) {
            this.paint = new Paint(paint);
        }

        public PaintBuilder color(int color) {
            paint.setColor(color);
            return this;
        }

        public PaintBuilder style(Paint.Style style) {
            paint.setStyle(style);
            return this;
        }

        public PaintBuilder strokeWidth(int width) {
            paint.setStrokeWidth(width);
            return this;
        }

        public PaintBuilder textSize(float textSize) {
            paint.setTextSize(textSize);
            return this;
        }

        public Paint build() {
            paint.setAntiAlias(true);
            return paint;
        }
    }
}
