package net.lenhat.musicapplication.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@SuppressWarnings("unused")
@Entity(tableName = "songs")
public class Song {
    @SerializedName("id")
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    private String mId = "";
    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String mTitle;
    @SerializedName("album")
    @ColumnInfo(name = "album")
    private String mAlbum;
    @SerializedName("artist")
    @ColumnInfo(name = "artist")
    private String mArtist;
    @SerializedName("source")
    @ColumnInfo(name = "source")
    private String mSource;
    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String mImage;
    @SerializedName("duration")
    @ColumnInfo(name = "duration")
    private int mDuration;
    @SerializedName("favorite")
    @ColumnInfo(name = "favorite")
    private boolean mFavorite;
    @SerializedName("counter")
    @ColumnInfo(name = "counter")
    private int mCounter;
    @SerializedName("replay")
    @ColumnInfo(name = "replay")
    private int mReplay;

    public Song() {
    }

    @Ignore
    public Song(@NonNull String id) {
        setId(id);
    }

    @Ignore
    public Song(@NonNull String id, String title, String album,
                String artist, String source, String image,
                int duration, boolean favorite, int counter, int replay) {
        setId(id);
        mTitle = title;
        mAlbum = album;
        mArtist = artist;
        mSource = source;
        mImage = image;
        mDuration = duration;
        mFavorite = favorite;
        mCounter = counter;
        mReplay = replay;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String album) {
        mAlbum = album;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public int getCounter() {
        return mCounter;
    }

    public void setCounter(int counter) {
        mCounter = counter;
    }

    public int getReplay() {
        return mReplay;
    }

    public void setReplay(int replay) {
        mReplay = replay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return Objects.equals(mId, song.mId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId);
    }
}
