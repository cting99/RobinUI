package com.cting.robin.ui.miniwindow.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cting.robin.ui.miniwindow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.cting.robin.ui.miniwindow.music.MiniMusicContract.IPresenter;
import static com.cting.robin.ui.miniwindow.music.MiniMusicContract.IView;

public class MiniMusicFragment extends Fragment implements IView {

    @BindView(R.id.mini_window_music_title_text)
    TextView miniwindowMusicTitleText;
    @BindView(R.id.mini_window_music_play_pause_btn)
    ImageView miniWindowMusicPlayPauseBtn;
    Unbinder unbinder;

    IPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MiniMusicPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_window_fragment_music, container, false);
        unbinder = ButterKnife.bind(this, view);
        mPresenter.onAttachView(getContext(), this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetachView();
        unbinder.unbind();
    }

    @OnClick(R.id.mini_window_music_play_pause_btn)
    public void onMiniwindowMusicPlayPauseBtnClicked() {
        mPresenter.togglePlayPause();
    }

    @OnClick(R.id.mini_window_music_next_btn)
    public void onMiniwindowMusicNexBtnClicked() {
        mPresenter.next();
    }

    @OnClick(R.id.mini_window_music_prev_btn)
    public void onMiniwindowMusicPrevBtnClicked() {
        mPresenter.prev();
    }

    @Override
    public void updateTitle(String title) {
        miniwindowMusicTitleText.setText(title);
    }

    @Override
    public void updatePlayPauseButton() {
        if (mPresenter.isPlaying()) {
            miniWindowMusicPlayPauseBtn.setImageResource(R.drawable.mini_window_music_btn_pause);
        } else {
            miniWindowMusicPlayPauseBtn.setImageResource(R.drawable.mini_window_music_btn_play);
        }
    }

}
