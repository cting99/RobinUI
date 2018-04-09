package com.cting.robin.ui.music;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.cting.robin.ui.music.list.MusicBrowserListFragment;
import com.cting.robin.ui.music.playback.MusicPlaybackFragment;
import com.cting.robin.ui.music.service.MusicPlaybackService;

import butterknife.BindView;
import butterknife.ButterKnife;
import cting.com.robin.support.common.activities.BasePermissionCheckActivity;

public class MusicActivity extends BasePermissionCheckActivity {

    public static final String TAG = "cting/music/app";

    private static final int POSITION_MUSIC_BROWSER_LIST = 0;
    private static final int POSITION_MUSIC_PLAYBACK = 1;

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private SectionsPagerAdapter mAdapter;
    private ViewPager.OnPageChangeListener mPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            navigation.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    pager.setCurrentItem(POSITION_MUSIC_PLAYBACK);
                    return true;
                case R.id.navigation_dashboard:
                    pager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    pager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

//


    @Override
    protected void onPermissionReady() {
        super.onPermissionReady();

        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);

        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);
        pager.addOnPageChangeListener(mPageChangeListener);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent service = new Intent(this, MusicPlaybackService.class);
        startService(service);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == POSITION_MUSIC_PLAYBACK) {
                return new MusicPlaybackFragment();
            } else if (position == POSITION_MUSIC_BROWSER_LIST) {
                return new MusicBrowserListFragment();
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
