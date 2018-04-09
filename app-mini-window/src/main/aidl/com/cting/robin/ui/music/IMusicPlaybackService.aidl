// IMusicPlayback.aidl
package com.cting.robin.ui.music;

// Declare any non-default types here with import statements
import com.cting.robin.ui.music.model.MusicInfo;

interface IMusicPlaybackService {
    boolean isPlaying();
    void playOrPause();
    MusicInfo getCurrentMusicInfo();
    long getDuration();
    void seekTo(long position);

}
