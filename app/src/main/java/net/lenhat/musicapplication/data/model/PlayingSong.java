package net.lenhat.musicapplication.data.model;

import androidx.media3.common.MediaItem;

import net.lenhat.musicapplication.data.model.playlist.Playlist;

@SuppressWarnings("unused")
public class PlayingSong {
    private Song mSong;
    private MediaItem mMediaItem;
    private Playlist mPlaylist;
    private int mCurrentPosition;
    private int mCurrentSongIndex;
    private int mNextSongIndex;

    public PlayingSong() {
        this(null, null);
    }

    public PlayingSong(Song song, Playlist playlist) {
        this(song, playlist, -1);
    }

    public PlayingSong(Song song, Playlist playlist, int currentIndex) {
        setSong(song);
        setPlaylist(playlist);
        setCurrentSongIndex(currentIndex);
    }

    public Song getSong() {
        return mSong;
    }

    public void setSong(Song song) {
        mSong = song;
        if(song != null) {
            setMediaItem(MediaItem.fromUri(song.getSource()));
        }
    }

    public MediaItem getMediaItem() {
        return mMediaItem;
    }

    public void setMediaItem(MediaItem mediaItem) {
        mMediaItem = mediaItem;
    }

    public Playlist getPlaylist() {
        return mPlaylist;
    }

    public void setPlaylist(Playlist playlist) {
        mPlaylist = playlist;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }

    public int getCurrentSongIndex() {
        return mCurrentSongIndex;
    }

    public void setCurrentSongIndex(int currentSongIndex) {
        mCurrentSongIndex = currentSongIndex;
    }

    public int getNextSongIndex() {
        return mNextSongIndex;
    }

    public void setNextSongIndex(int nextSongIndex) {
        mNextSongIndex = nextSongIndex;
    }
}
