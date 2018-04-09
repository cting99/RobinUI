package com.cting.robin.ui.music.list;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class SongEntry {
    private String album;
    private String albumId;
    private String title;
    private String displayName;
    private long duration;
    private String path;

    private SongEntry() {
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "SongEntry{" +
                "album='" + album + '\'' +
                ", title='" + title + '\'' +
                ", displayName='" + displayName + '\'' +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                '}';
    }

    public static Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final String[] PROJECTION = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
    };


    public static List<SongEntry> fromCursor(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            List<SongEntry> list = new ArrayList<>();
            SongEntry entry;
            while (cursor.moveToNext()) {
                entry = new SongEntry();
                entry.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                entry.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                entry.setDisplayName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                entry.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                entry.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                list.add(entry);
            }
            return list;
        }
        return null;
    }
}
