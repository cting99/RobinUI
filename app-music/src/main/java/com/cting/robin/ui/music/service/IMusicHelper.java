package com.cting.robin.ui.music.service;

import android.net.Uri;

import com.cting.robin.ui.music.model.MusicInfo;

public interface IMusicHelper {

    void prev();

    void toggle();

    void next();

    void stop();

    void setDataSource(Uri uri);

    void setDataSource(String path);

    String getArtist();

    String getTitle();

    String getAlbum();

    MusicInfo getMusicInfo();

    long getDuration();

    long getCurrentPosition();

    void seekTo(long position);

    boolean isPlaying();

    void register(IMusicPlayCallback callback);

    void unRegister(IMusicPlayCallback callback);
}
