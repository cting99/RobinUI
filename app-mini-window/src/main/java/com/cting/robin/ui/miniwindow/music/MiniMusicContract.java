package com.cting.robin.ui.miniwindow.music;


import android.content.Context;

public class MiniMusicContract {
    public interface IView {
        void updateTitle(String title);

        void updatePlayPauseButton();
    }

    public interface IPresenter {
        void onAttachView(Context context, IView ui);

        void onDetachView();

        void togglePlayPause();

        void prev();

        void next();

        boolean isPlaying();

    }


}
