package com.cting.robin.ui.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.cting.robin.ui.music.IMusicPlaybackService;
import com.cting.robin.ui.music.R;
import com.cting.robin.ui.music.model.MusicInfo;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class MusicPlaybackService extends Service implements IMusicHelper {

    public static final String CMD_PLAY = "CMD_PLAY";
    public static final String CMD_PAUSE = "CMD_PAUSE";
    public static final String CMD_STOP = "CMD_STOP";
    public static final String CMD_TOGGLE = "CMD_TOGGLE";
    public static final String CMD = "command";
    public static final String TAG = "cting/music/helper";
    private static final String EXTRA_DATA_SOURCE = "EXTRA_DATA_SOURCE";

    private MediaPlayer mPlayer;
    private MusicInfo mMusicInfo;
    ArrayList<IMusicPlayCallback> mCallbacks = new ArrayList<>();

    @Override
    public void prev() {
    }

    @Override
    public void toggle() {
        if (isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    @Override
    public void next() {

    }

    @Override
    public void stop() {
        mPlayer.stop();
        mPlayer.release();
    }

    @Override
    public void setDataSource(Uri uri) {
        setPlayer(uri);
    }

    @Override
    public void setDataSource(String path) {
        setPlayer(path);
    }

    @Override
    public String getArtist() {
        if (mMusicInfo != null) {
            return mMusicInfo.getArtist();
        }
        return null;
    }

    @Override
    public String getTitle() {
        if (mMusicInfo != null) {
            return mMusicInfo.getTitle();
        }
        return null;
    }

    @Override
    public String getAlbum() {
        if (mMusicInfo != null) {
            return mMusicInfo.getAlbum();
        }
        return null;
    }

    @Override
    public MusicInfo getMusicInfo() {
        return mMusicInfo;
    }

    @Override
    public long getDuration() {
        if (mPlayer != null) {
            return mPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        if (mPlayer != null) {
            return mPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(long position) {
        if (mPlayer != null) {
            if (position < 0) {
                position = 0;
            } else if (position > getDuration()) {
                position = getDuration();
            }
            mPlayer.seekTo((int) position);
        }
    }

    @Override
    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    @Override
    public void register(IMusicPlayCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegister(IMusicPlayCallback callback) {
        if (mCallbacks.contains(callback)) {
            mCallbacks.remove(callback);
        }
    }

    private void pause() {
        mPlayer.pause();
    }

    private void play() {
        mPlayer.start();

    }

    private void setPlayer(Uri uri) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(this, uri);
            mPlayer.prepare();

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, uri);
            mMusicInfo = MusicInfo.fromRetriever(retriever);
            Log.i(TAG, "setPlayer: " + mMusicInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPlayer(String path) {
        try {
            mPlayer.reset();
            Uri uri = Uri.parse(path);
            if (path.startsWith("content://")) {
                mPlayer.setDataSource(this, uri);
            } else {
                mPlayer.setDataSource(path);
            }
            mPlayer.prepare();

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, uri);
            mMusicInfo = MusicInfo.fromRetriever(retriever);
            Log.i(TAG, "setPlayer: " + mMusicInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.this_means_war);
            setPlayer(uri);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        String cmd = intent.getStringExtra(CMD);
        if (CMD_TOGGLE.equals(cmd)) {
            toggle();
        } else if (CMD_PLAY.equals(cmd)) {
            String dataSource = intent.getStringExtra(EXTRA_DATA_SOURCE);
            if (!TextUtils.isEmpty(dataSource)) {
                setPlayer(dataSource);
            }
            play();
        } else if (CMD_PAUSE.equals(cmd)) {
            pause();
        } else if (CMD_STOP.equals(cmd)) {
            pause();
        }
        return START_NOT_STICKY;
    }

    public static void play(Context context, String path) {
        Intent service = new Intent(context, MusicPlaybackService.class);
        service.putExtra(CMD, CMD_PLAY);
        service.putExtra(EXTRA_DATA_SOURCE, path);
        context.startService(service);
    }
/*
    public static void toggle(Context context) {
        Intent intent = new Intent(context, MusicPlaybackService.class);
        intent.putExtra(CMD, CMD_TOGGLE);
        context.startService(intent);
    }
*/

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: " + intent);
        if (getPackageName().equals(intent.getPackage())) {
            return mBinder;
        } else {
            return mStub;
        }
    }

    private IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public IMusicHelper getMusicHelper() {
            return MusicPlaybackService.this;
        }
    }

    private MyPlaybackStub mStub = new MyPlaybackStub(this);

    private class MyPlaybackStub extends IMusicPlaybackService.Stub {
        WeakReference<IMusicHelper> helper;

        public MyPlaybackStub(IMusicHelper service) {
            this.helper = new WeakReference<IMusicHelper>(service);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return helper.get().isPlaying();
        }

        @Override
        public void playOrPause() throws RemoteException {
            helper.get().toggle();
        }

        @Override
        public MusicInfo getCurrentMusicInfo() throws RemoteException {
            return helper.get().getMusicInfo();
        }

        @Override
        public long getDuration() throws RemoteException {
            return helper.get().getDuration();
        }

        @Override
        public void seekTo(long position) throws RemoteException {
            helper.get().seekTo(position);
        }
    }

    /*
    class MyPlaybackStub extends IMusicPlaybackService.Stub{

        WeakReference<MusicPlaybackService> mService;
        public MyPlaybackStub(MusicPlaybackService helper) {
            mService = new WeakReference<MusicPlaybackService>(helper);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mService.get().isPlaying();
        }

        @Override
        public void playOrPause() throws RemoteException {
            mService.get().toggle();
        }

        @Override
        public MusicInfo getCurrentMusicInfo() throws RemoteException {
            return mService.get().mMusicInfo;
        }

        @Override
        public long getDuration() throws RemoteException {
            return mService.get().getDuration();
        }

        @Override
        public void seekTo(long position) throws RemoteException {
            mService.get().seek(position);
        }
    }

    private IBinder mBinder= new MyPlaybackStub(this);


    // in activity or fragment:
    private IMusicPlayback mService;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder helper) {
            mService = IMusicPlayback.Stub.asInterface(helper);
            Log.i(TAG, "onServiceConnected: " + name + ",get helper:" + mService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: " + name);
            mService = null;
        }
    };

    Intent helper = new Intent(this, MusicPlaybackService.class);
    bindService(helper, mConn, BIND_AUTO_CREATE);

    unbindService(mConn);
    */
}
