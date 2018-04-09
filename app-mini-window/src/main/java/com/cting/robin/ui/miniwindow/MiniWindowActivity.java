package com.cting.robin.ui.miniwindow;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.cting.robin.ui.miniwindow.clock.MiniClockFragment;
import com.cting.robin.ui.miniwindow.music.MiniMusicFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cting.com.robin.support.common.activities.BasePermissionCheckActivity;

public class MiniWindowActivity extends BasePermissionCheckActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    public static final String TAG = "cting/mini/app";
    public static final int FRAGMENT_COUNT = 4;

    private static final int POSITION_CLOCK = 0;
    private static final int POSITION_MUSIC = 1;


    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private MiniClockFragment mClockFragment;
    private Fragment mMusicFragment;


    @Override
    protected void onPermissionReady() {
        super.onPermissionReady();
        setContentView(R.layout.mini_window_activity);
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(this);
        switchToFragment(POSITION_CLOCK);

    }

    @Override
    protected String[] getRequestPermission() {
        return new String[]{
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_SMS,
        };
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mini_window_nav_menu_clock:
                switchToFragment(POSITION_CLOCK);
                return true;
            case R.id.mini_window_nav_menu_music:
//                switchToFragment(POSITION_MUSIC);
                return true;
            case R.id.mini_window_nav_menu_alarm:
                return true;
            case R.id.mini_window_nav_menu_call:
                return true;
        }
        return false;
    }

    private void switchToFragment(int position) {
        Fragment fragment = null;
        if (position == POSITION_CLOCK) {
            if (mClockFragment == null) {
                mClockFragment = new MiniClockFragment();
            }
            fragment = mClockFragment;
        } else if (position == POSITION_MUSIC) {
            if (mMusicFragment == null) {
                mMusicFragment = new MiniMusicFragment();
            }
            fragment = mMusicFragment;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mini_window_fragment_container, fragment)
                    .commit();
        }
    }
}
