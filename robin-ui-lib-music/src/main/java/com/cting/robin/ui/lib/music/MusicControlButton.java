package com.cting.robin.ui.lib.music;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;

import com.cting.robin.ui.lib.music.utils.MusicHelper;

public class MusicControlButton extends AppCompatImageView implements View.OnClickListener {

    public static final String TAG = "cting/music/btn";
    protected int mKeyCode = -1;

    public MusicControlButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicControlButton);
        mKeyCode = ta.getInt(R.styleable.MusicControlButton_music_keyCode, mKeyCode);
        switch (mKeyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_STOP:
            case KeyEvent.KEYCODE_MEDIA_NEXT:
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                Log.i(TAG, "MusicControlButton: valid key:" + mKeyCode);
                break;
            default:
//                ta.recycle();
//                throw new RuntimeException("MusicControlButton need key code!");

        }
        ta.recycle();
        if (getBackground() == null) {
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true);
            setBackgroundResource(typedValue.resourceId);
        }

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == this) {
            MusicHelper.clickMediaButton(mKeyCode, getContext());
        }
    }

}
