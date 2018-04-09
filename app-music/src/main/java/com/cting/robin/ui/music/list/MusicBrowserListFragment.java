package com.cting.robin.ui.music.list;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cting.robin.ui.lib.music.utils.TimeHelper;
import com.cting.robin.ui.music.R;
import com.cting.robin.ui.music.service.MusicPlaybackService;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MusicBrowserListFragment extends ListFragment {
    public static final String TAG = "cting/music/list";

    AsyncQueryHandler mQueryHandler;
    MediaBrowserAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new MediaBrowserAdapter(getContext(), null);
        setListAdapter(mAdapter);
        mQueryHandler = new MyQueryHandler(getContext().getContentResolver());
        Log.i(TAG, "onActivityCreated: query");
        mQueryHandler.startQuery(0, null, SongEntry.URI, SongEntry.PROJECTION, null,
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) mAdapter.getItem(position);
        if (cursor != null) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            MusicPlaybackService.play(getContext(), path);
        }
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_browser_list, container, false);
        return view;
    }*/

    @Override
    public void onResume() {
        super.onResume();
        Log.i("cting/music/list", "onCreateView: startQuery");

    }

    private class MyQueryHandler extends AsyncQueryHandler {
        public MyQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            Log.i(TAG, "onQueryComplete: cursor=" + cursor);
            mAdapter.changeCursor(cursor);
        }
    }


    class MediaBrowserAdapter extends CursorAdapter {

        public MediaBrowserAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.music_browser_list_item, null, false);
            ViewHolder vh = new ViewHolder(view);
            view.setTag(vh);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder != null) {
                viewHolder.update(cursor);
            }
        }
    }

    class ViewHolder {
        @BindView(R.id.music_album_img)
        ImageView musicAlbumImg;
        @BindView(R.id.music_title_text)
        TextView musicTitleText;
        @BindView(R.id.music_artist_text)
        TextView musicArtistText;
        @BindView(R.id.music_duration_text)
        TextView musicDurationText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void update(Cursor cursor) {
            musicTitleText.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicArtistText.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            musicDurationText.setText(TimeHelper.formatDuration(duration));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long albumFile = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            Log.i(TAG, "update: path=" + path + ",albumFile=" + albumFile);
//            musicAlbumImg.setImageURI(FileProvider.getUriForFile(getContext(),));
        }
    }


}
