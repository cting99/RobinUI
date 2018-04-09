package com.cting.robin.ui.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cting.robin.ui.music.model.MusicInfo;
import com.cting.robin.ui.music.service.MusicPlaybackService;
import com.cting.robin.ui.lib.music.utils.TimeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "ctitng/server";
    public static final int PROGRESS_MAX = 1000;
    @BindView(R.id.prev_btn)
    Button prevBtn;
    @BindView(R.id.toggle_btn)
    Button toggleBtn;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.artist_text)
    TextView artistText;
    @BindView(R.id.album_text)
    TextView albumeText;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.current_text)
    TextView seekStartText;
    @BindView(R.id.duration_text)
    TextView seekEndText;

    private IMusicPlaybackService mService;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMusicPlaybackService.Stub.asInterface(service);
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playback);
        ButterKnife.bind(this);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(PROGRESS_MAX);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent service = new Intent(this, MusicPlaybackService.class);
        bindService(service, mConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConn);
    }

    @OnClick(R.id.prev_btn)
    public void onPrevBtnClicked() {
    }

    @OnClick(R.id.toggle_btn)
    public void onToggleBtnClicked() {
        Log.i(TAG, "onToggleBtnClicked: ");
//        MusicPlaybackFragment.toggle(getApplicationContext());
        if (mService != null) {
            try {
                mService.playOrPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        updateUI();
    }

    @OnClick(R.id.next_btn)
    public void onNextBtnClicked() {
    }


    private void updateUI() {
        if (mService != null) {
            boolean isPlaying = false;
            MusicInfo info = null;
            long duration = 0;
            try {
                isPlaying = mService.isPlaying();
                info = mService.getCurrentMusicInfo();
                duration = mService.getDuration();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (info != null) {
                titleText.setText(info.getTitle());
                albumeText.setText(info.getAlbum());
                artistText.setText(info.getArtist());
            } else {
                titleText.setText("");
                albumeText.setText("");
                artistText.setText("");
            }


            seekEndText.setText(TimeHelper.formatMusicDuration(duration));
            toggleBtn.setText(isPlaying ? "pauseUpdate" : "play");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mService != null) {
            try {
                long duration = mService.getDuration();
                mService.seekTo(progress * duration / PROGRESS_MAX);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
