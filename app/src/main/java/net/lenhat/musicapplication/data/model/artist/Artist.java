package net.lenhat.musicapplication.data.model.artist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@SuppressWarnings("unused")
@Entity(tableName = "artists")
public class Artist {
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "artist_id")
    private int mId;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String mName;

    @SerializedName("avatar")
    @ColumnInfo(name = "avatar")
    private String mAvatar;

    @SerializedName("interested")
    @ColumnInfo(name = "interested")
    private int mInterested;

    public Artist() {

    }

    public Artist(int id, String name, String avatar, int interested) {
        mId = id;
        mName = name;
        mAvatar = avatar;
        mInterested = interested;
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

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public int getInterested() {
        return mInterested;
    }

    public void setInterested(int interested) {
        mInterested = interested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;
        Artist artist = (Artist) o;
        return mId == artist.mId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId);
    }
}
