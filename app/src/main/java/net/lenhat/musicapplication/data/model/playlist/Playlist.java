package net.lenhat.musicapplication.data.model.playlist;

import androidx.media3.common.MediaItem;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import net.lenhat.musicapplication.data.model.Song;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
@Entity(tableName = "playlists")
public class Playlist {
    @Ignore
    private static int sNextId = 1001;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlist_id")
    private int mId = 1001;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "artwork")
    private String mArtwork;
    @ColumnInfo(name = "created_at")
    private Date mCreatedAt;
    @Ignore
    private List<Song> mSongs = new ArrayList<>();
    @Ignore
    private final List<MediaItem> mMediaItems = new ArrayList<>();

    public Playlist() {
    }

    @Ignore
    public Playlist(int id, String name) {
        this(id, name, null, null, null);
    }

    @Ignore
    public Playlist(int id, String name, String artwork, Date createdAt, List<Song> songs) {
        setId(id);
        setName(name);
        setArtwork(artwork);
        setCreatedAt(createdAt);
        updateSongs(songs);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        if (id > 0) {
            mId = id;
        } else {
            mId = sNextId++;
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getArtwork() {
        return mArtwork;
    }

    public void setArtwork(String artwork) {
        mArtwork = artwork;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void updateSongs(List<Song> songs) {
        if (songs != null) {
            mSongs = songs;
            updateMediaItems();
        }
    }

    public List<MediaItem> getMediaItems() {
        return mMediaItems;
    }

    private void updateMediaItems() {
        mMediaItems.clear();
        for (Song song : mSongs) {
            mMediaItems.add(MediaItem.fromUri(song.getSource()));
        }
    }
}
