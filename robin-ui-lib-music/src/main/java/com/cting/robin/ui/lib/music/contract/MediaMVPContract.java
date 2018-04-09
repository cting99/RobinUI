package com.cting.robin.ui.lib.music.contract;


import android.content.Context;

public class MediaMVPContract {
    public interface IView {
        void updateTitle(String title);

        void updatePlayPauseButton();

        void setCurrentPosition(long mediaPosition);

        void setDuration(long duration);


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
