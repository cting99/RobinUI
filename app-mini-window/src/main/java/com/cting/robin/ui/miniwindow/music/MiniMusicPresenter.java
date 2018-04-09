package com.cting.robin.ui.miniwindow.music;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.cting.robin.ui.music.IMusicPlaybackService;
import com.cting.robin.ui.music.model.MusicInfo;

import static com.cting.robin.ui.miniwindow.music.MiniMusicContract.*;

public class MiniMusicPresenter implements IPresenter {
    public static final String TAG = "cting/mini/music/p";
    private IView view;

    private IMusicPlaybackService mMusicService;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: " + name);
            mMusicService = IMusicPlaybackService.Stub.asInterface(service);
            refreshUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " + name);
            mMusicService = null;
        }
    };

    /*private void startMusicService(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.cting.robinui.music",
                "com.cting.robinui.music.service.MusicPlaybackService");
        context.startService(intent);
    }*/

    private void bindMusicService(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.cting.robinui.music",
                "com.cting.robinui.music.service.MusicPlaybackService");
        context.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onAttachView(Context context, IView view) {
        this.view = view;
        bindMusicService(context);
    }

    @Override
    public void onDetachView() {
        this.view = null;
    }

    @Override
    public void togglePlayPause() {
        if (mMusicService != null) {
            try {
                mMusicService.playOrPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        refreshUI();
    }

    @Override
    public void prev() {
        refreshUI();

    }

    @Override
    public void next() {
        refreshUI();

    }

    @Override
    public boolean isPlaying() {
        if (mMusicService != null) {
            try {
                return mMusicService.isPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private void refreshUI() {
        if (view != null) {
            MusicInfo info = null;
            if (mMusicService != null) {
                try {
                    info = mMusicService.getCurrentMusicInfo();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if (info != null) {
                view.updateTitle(info.getTitle());
            }
            view.updatePlayPauseButton();
        }
    }
}
