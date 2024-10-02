package net.lenhat.musicapplication.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Entity(tableName = "albums")
public class Album {
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "album_id")
    private int mId;
    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String mName;
    @SerializedName("size")
    @ColumnInfo(name = "size")
    private int mSize;
    @SerializedName("artwork")
    @ColumnInfo(name = "artwork")
    private String mArtwork;
    @SerializedName("songs")
    @Ignore
    private final List<String> mSongs = new ArrayList<>();

    public Album() {
    }

    @Ignore
    public Album(int id, String name, int size, String artwork) {
        mId = id;
        mName = name;
        mSize = size;
        mArtwork = artwork;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public String getArtwork() {
        return mArtwork;
    }

    public void setArtwork(String artwork) {
        mArtwork = artwork;
    }

    public void setSongs(List<String> songs) {
        if (songs != null && !songs.isEmpty()) {
            mSongs.clear();
            mSongs.addAll(songs);
        }
    }

    public List<String> getSongs() {
        return mSongs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album album = (Album) o;
        return mId == album.mId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId);
    }
}
