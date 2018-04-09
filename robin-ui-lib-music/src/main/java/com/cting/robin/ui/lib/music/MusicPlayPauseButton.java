package com.cting.robin.ui.lib.music;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.cting.robin.ui.lib.music.utils.MusicHelper;


public class MusicPlayPauseButton extends MusicControlButton {

    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateImage();
        }
    };
    private final int mPlayIconResId;
    private final int mPauseIconResId;

    public MusicPlayPauseButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicPlayPauseButton);
        mPlayIconResId = ta.getResourceId(R.styleable.MusicPlayPauseButton_music_play_icon, R.drawable.music_ic_circular_play);
        mPauseIconResId = ta.getResourceId(R.styleable.MusicPlayPauseButton_music_pause_icon, R.drawable.music_ic_circular_pause);
        ta.recycle();
        mKeyCode = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
    }

    @Override
    public void onClick(View v) {
        if (v == this) {
            MusicHelper.clickMediaButton(mKeyCode, getContext());
            getHandler().removeCallbacks(mUpdateRunnable);
            getHandler().postDelayed(mUpdateRunnable, 650);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateImage();
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if (isVisible) {
            updateImage();
        }
    }

    private void updateImage() {
        if (isMusicActive()) {
            setImageResource(mPauseIconResId);
        } else {
            setImageResource(mPlayIconResId);
        }
    }

    private boolean isMusicActive() {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            return audioManager.isMusicActive();
        } else {
            return false;
        }
    }
}
