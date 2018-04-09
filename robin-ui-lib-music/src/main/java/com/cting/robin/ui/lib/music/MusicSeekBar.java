package com.cting.robin.ui.lib.music;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cting.robin.ui.lib.music.utils.TimeHelper;

import java.lang.ref.WeakReference;


public class MusicSeekBar extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    public interface Callback {
        void dragTo(long mediaPosition);

        long getCurrentMediaPosition();

        boolean isPlaying();
    }

    private static final int MSG_UPDATE_CURRENT_PROGRESS = 1;
    public static final long DELAY_MILLIS = 1000;
    private static final int CHECK_COUNT = 3;
    private int mCheckCount = 0;

    public static final int SEEK_BAR_MAX = 1000;
    public static final String TAG = "cting/music/seek";

    SeekBar seekBar;
    TextView currentText;
    TextView durationText;

    private long mMediaDuration = 0;
    private Callback mCallback;
    private Handler mHandler;

    private boolean mShowTime = true;


    public MusicSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mHandler = new MyHandler(this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicSeekBar);
        mShowTime = ta.getBoolean(R.styleable.MusicSeekBar_music_seekbar_show_time, mShowTime);
        ta.recycle();
        inflate(context, R.layout.music_seekbar_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        seekBar = findViewById(R.id.seekBar);
        currentText = findViewById(R.id.current_text);
        durationText = findViewById(R.id.duration_text);

        seekBar.setMax(SEEK_BAR_MAX);
        seekBar.setOnSeekBarChangeListener(this);
        currentText.setVisibility(mShowTime ? VISIBLE : GONE);
        durationText.setVisibility(mShowTime ? VISIBLE : GONE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: ");
        postUpdate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow: ");
        pauseUpdate();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mCheckCount = CHECK_COUNT;
        }
        postUpdate();
        long musicPosition = seekBarProgressToMediaPosition(progress);
        String currentText = TimeHelper.formatDuration(musicPosition);
//        Log.i(TAG, "onProgressChanged: " + currentText + "," + progress + "/" + musicPosition + ",user touch:" + fromUser);
        if (TextUtils.equals(currentText, this.currentText.getText())) {
            return;
        }
        this.currentText.setText(currentText);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        pauseUpdate();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long musicPosition = seekBarProgressToMediaPosition(seekBar.getProgress());
        if (mCallback != null) {
            mCallback.dragTo(musicPosition);
        }
        postUpdate();
    }

    private boolean isPlaying() {
        return mMediaDuration > 0 && mCallback != null && mCallback.isPlaying();
    }

    private void postUpdate() {
        if (isPlaying()) {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_CURRENT_PROGRESS, DELAY_MILLIS);
        } else {
            if (mCheckCount < CHECK_COUNT) {
                mCheckCount++;
//                Log.i(TAG, "postUpdate: check " + mCheckCount + " time");
                postUpdate();
            } else {
                mCheckCount = 0;
            }
        }
    }

    private void pauseUpdate() {
        mHandler.removeMessages(MSG_UPDATE_CURRENT_PROGRESS);
    }

    public void update() {
        postUpdate();
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public void setDuration(long duration) {
        Log.i(TAG, "setDuration: " + duration);
        this.mMediaDuration = duration;
        durationText.setText(TimeHelper.formatDuration(duration));
    }

    public void setCurrentProgress(long musicPosition) {
//        Log.i(TAG, "setCurrentProgress: " + musicPosition);
        seekBar.setProgress(mediaPositionToSeekBarProgress(musicPosition), true);
        currentText.setText(TimeHelper.formatDuration(musicPosition));
    }

    private int mediaPositionToSeekBarProgress(long mediaPosition) {
        return (int) (SEEK_BAR_MAX * mediaPosition / mMediaDuration);
    }

    private long seekBarProgressToMediaPosition(int seekBarProgress) {
        return mMediaDuration * seekBarProgress / SEEK_BAR_MAX;
    }

    private static class MyHandler extends Handler {
        WeakReference<MusicSeekBar> seekBar;

        private MyHandler(MusicSeekBar seekBar) {
            this.seekBar = new WeakReference<>(seekBar);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_CURRENT_PROGRESS:
                    if (seekBar.get() != null) {
                        seekBar.get().setCurrentProgress(seekBar.get().getCallback().getCurrentMediaPosition());
                    }
                    break;
            }
        }
    }
}
