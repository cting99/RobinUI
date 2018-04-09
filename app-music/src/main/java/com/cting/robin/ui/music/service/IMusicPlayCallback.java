package com.cting.robin.ui.music.service;


import com.cting.robin.ui.music.model.MusicInfo;

public interface IMusicPlayCallback {

    void onSongChange(MusicInfo info);

    void onPlayStateChange(boolean isPlaying);
}
