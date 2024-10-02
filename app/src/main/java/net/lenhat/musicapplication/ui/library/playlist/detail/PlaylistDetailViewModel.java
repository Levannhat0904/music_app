package net.lenhat.musicapplication.ui.library.playlist.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;

public class PlaylistDetailViewModel extends ViewModel {
    private final MutableLiveData<PlaylistWithSongs> mPlaylistWithSongs = new MutableLiveData<>();

    public void setPlaylistWithSongs(PlaylistWithSongs playlistWithSongs) {
        mPlaylistWithSongs.setValue(playlistWithSongs);
    }

    public LiveData<PlaylistWithSongs> getPlaylistWithSongs() {
        return mPlaylistWithSongs;
    }
}