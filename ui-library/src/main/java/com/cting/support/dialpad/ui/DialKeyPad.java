package com.cting.support.dialpad.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cting.support.R;
import com.cting.support.dialpad.utils.DialUtils;


public class DialKeyPad extends ViewGroup implements View.OnClickListener {

    public static final int COLUMN_COUNT = 3;
    public static final int ROW_COUNT = 4;
    public static final String TAG = "cting/DialKeyPad";

    private TextView textView;
//    private class DialKey{
//        int keyCode;
//        @DrawableRes int iconId;
//    }

    private static final int[] DIAL_KEYS = new int[]{
            KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4,
            KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8,
            KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_STAR, KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_POUND,
    };
    private static final int KEY_COUNT = DIAL_KEYS.length;
    int[] srcIds = new int[]{
            R.drawable.dial_num_1, R.drawable.dial_num_2, R.drawable.dial_num_3, R.drawable.dial_num_4,
            R.drawable.dial_num_5, R.drawable.dial_num_6, R.drawable.dial_num_7, R.drawable.dial_num_8,
            R.drawable.dial_num_9, R.drawable.dial_num_star, R.drawable.dial_num_0, R.drawable.dial_num_pound,
    };
    private final int defaultWidth;
    private final int defaultHeight;

    public DialKeyPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "DialKeyPad: ");

        defaultWidth = getResources().getDimensionPixelOffset(R.dimen.dial_key_width);
        defaultHeight = getResources().getDimensionPixelOffset(R.dimen.dial_key_height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate: ");
        for (int i = 0; i < KEY_COUNT; i++) {
            addView(newKeyView(DIAL_KEYS[i], srcIds[i]));
        }
//        addView(newKeyView(0,R.drawable.dial_num_0_normal));
    }

    private boolean isVisible(View view) {
        return view != null && view.getVisibility() == VISIBLE;
    }

    @Override
    public void onClick(View v) {
        int keyCode = (int) v.getTag();
        DialUtils.pressKey(textView, keyCode);
    }

    public void setEditable(TextView textView) {
        this.textView = textView;
    }

    private View newKeyView(int keyCode, int resId) {
        View v= new View(getContext());
        v.setBackgroundResource(resId);
        v.setOnClickListener(this);
        v.setTag(keyCode);
        return v;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isVisible(this)) {
            Log.i(TAG, "onMeasure: not visible");
            return;
        }
        int width = getKeySize(widthMeasureSpec, defaultWidth);
        int height = getKeySize(heightMeasureSpec, defaultHeight);

        int childWidth = width / COLUMN_COUNT;
        int childHeight = height / ROW_COUNT;
//        Log.i(TAG, "onMeasure: width=" + width + ",height=" + height);
//        Log.i(TAG, "onMeasure: childWidth=" + childWidth + ",childHeight=" + childHeight);
        measureChildren(childWidth, childHeight);
        setMeasuredDimension(width, height);
    }

    private int getKeySize(int measureSpec, int defaultSize) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
//        Log.i(TAG, "getKeySize: size=" + size);
        switch (mode) {
            case MeasureSpec.AT_MOST:
                //wrap_content
//                Log.i(TAG, "getKeySize: AT_MOST--wrap_content");
//                size = defaultWidth;
                break;
            case MeasureSpec.EXACTLY:
                //match_parent
//                Log.i(TAG, "getKeySize: EXACTLY--match_parent");
                break;
            case MeasureSpec.UNSPECIFIED:
//                Log.i(TAG, "getKeySize: UNSPECIFIED");
                break;
        }
        return size;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isVisible(this)) {
            Log.i(TAG, "onLayout: not visible");
            return;
        }
        View child = getChildAt(0);
        int childWidth = child.getMeasuredWidth();
        int childHeight = child.getMeasuredHeight();
        int paddingHorizontal = getMeasuredWidth() - childWidth * COLUMN_COUNT;
        int paddingVertical = getMeasuredHeight() - childHeight * ROW_COUNT;
        int paddingLeft = paddingHorizontal / 2;
        int paddingTop = paddingVertical / 2;

        int index = 0;
        int left;
        int top = paddingTop;
//        Log.i(TAG, "onLayout: "
//                + ",width=" + getMeasuredWidth()
//                + ",height=" + getMeasuredHeight()
//                + ",childWidth=" + childWidth
//                + ",childHeight=" + childHeight
//                + ",paddingLeft=" + paddingLeft
//                + ",paddingTop=" + paddingTop
//        );
        for (int i = 0; i < ROW_COUNT; i++) {
            left = paddingLeft;
            for (int j = 0; j < COLUMN_COUNT; j++) {
                child = getChildAt(index);
                child.layout(left, top, left + childWidth, top + childHeight);
                left += childWidth;
                index++;
            }
            top += childHeight;
        }
    }



}
