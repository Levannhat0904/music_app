package net.lenhat.musicapplication.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Date;

@Entity(tableName = "recent_songs")
public class RecentSong extends Song {
    @ColumnInfo(name = "play_at")
    private Date mPlayAt;

    public Date getPlayAt() {
        return mPlayAt;
    }

    public void setPlayAt(Date playAt) {
        mPlayAt = playAt;
    }

    public static class Builder {
        private static RecentSong sRecentSong;

        public Builder(Song song) {
            sRecentSong = new RecentSong();
            sRecentSong.setId(song.getId());
            sRecentSong.setTitle(song.getTitle());
            sRecentSong.setArtist(song.getArtist());
            sRecentSong.setAlbum(song.getAlbum());
            sRecentSong.setDuration(song.getDuration());
            sRecentSong.setSource(song.getSource());
            sRecentSong.setImage(song.getImage());
            sRecentSong.setFavorite(song.isFavorite());
            sRecentSong.setCounter(song.getCounter());
            sRecentSong.setReplay(song.getReplay());
            sRecentSong.mPlayAt = new Date();
        }

        public Builder setPlayAt(Date playAt) {
            sRecentSong.mPlayAt = playAt;
            return this;
        }

        public RecentSong build() {
            return sRecentSong;
        }
    }
}
