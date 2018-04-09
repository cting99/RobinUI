package com.cting.robin.ui.music.model;

import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {
    private String album;
    private String artist;
    private String author;
    private String title;
    private String date;
    private String duration;

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    /*public String formatMusicDuration() {
        //This Means War ---from [Here And Now] by Nickelback
        return title + " --- from [" + album + "] by " + artist;
    }*/
    public static MusicInfo fromRetriever(MediaMetadataRetriever retriever) {
        MusicInfo info = new MusicInfo();
        info.album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        info.artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        info.author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
        info.title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        info.date = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        info.duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return info;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.album);
        dest.writeString(this.artist);
        dest.writeString(this.author);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.duration);
    }

    public MusicInfo() {
    }

    protected MusicInfo(Parcel in) {
        this.album = in.readString();
        this.artist = in.readString();
        this.author = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.duration = in.readString();
    }

    public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel source) {
            return new MusicInfo(source);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };
}
