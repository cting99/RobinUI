package com.cting.robin.ui.music.playback;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cting.robin.ui.music.R;
import com.cting.robin.ui.music.model.MusicInfo;
import com.cting.robin.ui.music.playback.MediaPlaybackContract.Ui;
import com.cting.robin.ui.lib.music.MusicSeekBar;
import com.cting.robin.ui.lib.music.MusicRevolvingRecord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MusicPlaybackFragment extends Fragment implements Ui {

    public static final String TAG = "cting/music/MusicPlaybackFragment";
    private static final int PROGRESS_MAX = 1000;

    @BindView(R.id.prev_btn)
    ImageButton prevBtn;
    @BindView(R.id.toggle_btn)
    ImageButton toggleBtn;
    @BindView(R.id.next_btn)
    ImageButton nextBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.artist_text)
    TextView artistText;
    @BindView(R.id.album_text)
    TextView albumText;
    @BindView(R.id.media_seek_bar)
    MusicSeekBar mediaSeekBar;
    @BindView(R.id.revolvingRecord)
    MusicRevolvingRecord record;

    Unbinder unbinder;

    MediaPlaybackContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MediaPlaybackContract.Presenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_music_playback, container, false);
        Log.i(TAG, "onCreateView: ");
        unbinder = ButterKnife.bind(this, view);

        mediaSeekBar.setCallback(new MusicSeekBar.Callback() {

            @Override
            public void dragTo(long mediaPosition) {
                mPresenter.seekTo(mediaPosition);
            }

            @Override
            public long getCurrentMediaPosition() {
                return mPresenter.getCurrentPosition();
            }

            @Override
            public boolean isPlaying() {
                return mPresenter.isPlaying();
            }
        });

        record.setCallback(new MusicRevolvingRecord.Callback() {

            @Override
            public void onToggle(boolean isRevolving) {
                mPresenter.togglePlayPause();
            }

            @Override
            public boolean isPlaying() {
                return mPresenter.isPlaying();
            }
        });
        mPresenter.onCreateView(getContext(), this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
        mPresenter.onDestroyView(getContext());
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.refreshUi();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }

    @OnClick(R.id.prev_btn)
    public void onPrevBtnClicked() {
        mPresenter.prev();
    }

    @OnClick(R.id.toggle_btn)
    public void onToggleBtnClicked() {
        Log.i(TAG, "onToggleBtnClicked: ");
        mPresenter.togglePlayPause();
    }

    @OnClick(R.id.next_btn)
    public void onNextBtnClicked() {
        mPresenter.next();
    }


    @Override
    public void updatePosition(long position) {
        mediaSeekBar.setCurrentProgress(position);
    }

    @Override
    public void setDuration(long duration) {
        mediaSeekBar.setDuration(duration);
    }

    @Override
    public void refresh(MusicInfo info) {
        if (mPresenter.isPlaying()) {
//            toggleBtn.setText("PAUSE");
            toggleBtn.setBackgroundResource(R.drawable.music_ic_circular_pause);
        } else {
//            toggleBtn.setText("PLAY");
            toggleBtn.setBackgroundResource(R.drawable.music_ic_circular_play);
        }
        if (info != null) {
            titleText.setText(info.getTitle());
            albumText.setText(info.getAlbum());
            artistText.setText(info.getArtist());
        }
        mediaSeekBar.update();
        record.update();
    }
}
