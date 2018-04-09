package com.cting.robin.ui.music.playback;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.cting.robin.ui.music.model.MusicInfo;
import com.cting.robin.ui.music.service.IMusicHelper;
import com.cting.robin.ui.music.service.MusicPlaybackService;

public class MediaPlaybackContract {

    public interface Ui {

        void updatePosition(long position);

        void setDuration(long duration);

//        void setTitle(String title);
//
//        void setAlbum(String album);
//
//        void setArtist(String artist);

        void refresh(MusicInfo info);

    }

    public static class Presenter{

        private IMusicHelper helper;

        private Ui ui;
        private ServiceConnection mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("cting/music/presenter", "onServiceConnected: " + name);
                if (service instanceof MusicPlaybackService.MyBinder) {
                    helper = ((MusicPlaybackService.MyBinder) service).getMusicHelper();
                    refreshUi();
                }
                /*if (service instanceof IMusicPlaybackService) {
                    helper=
                }*/
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                helper = null;
            }
        };

        private void bindMusicService(Context context) {
            Intent intent = new Intent(context, MusicPlaybackService.class);
            intent.setPackage(context.getPackageName());
            context.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
        }

        public void onCreateView(Context context, Ui ui) {
            this.ui = ui;
            bindMusicService(context);
        }

        public void onDestroyView(Context context) {
            this.ui = null;
            context.unbindService(mConn);
        }

        public boolean isPlaying() {
            if (helper != null) {
                return helper.isPlaying();
            }
            return false;
        }

        public void togglePlayPause() {
            if (helper != null) {
                helper.toggle();
            }
            refreshUi();
        }

        public void prev() {
            if (helper != null) {
                helper.prev();
            }
            refreshUi();
        }

        public void next() {
            if (helper != null) {
                helper.next();
            }
            refreshUi();
        }

        public long getCurrentPosition() {
            if (helper != null) {
                return helper.getCurrentPosition();
            }
            return 0;
        }

        public void seekTo(long position) {
            if (helper != null) {
                helper.seekTo(position);
            }
        }

        public void refreshUi() {
            if (ui != null && helper != null) {
                ui.refresh(helper.getMusicInfo());
                ui.setDuration(helper.getDuration());
                ui.updatePosition(helper.getCurrentPosition());
            }
        }
    }
}
