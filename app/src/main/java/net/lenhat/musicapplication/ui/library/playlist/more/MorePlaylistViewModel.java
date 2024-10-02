package net.lenhat.musicapplication.ui.library.playlist.more;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;

import java.util.List;

public class MorePlaylistViewModel extends ViewModel {
    private final MutableLiveData<List<PlaylistWithSongs>> mPlaylistLiveData = new MutableLiveData<>();

    public LiveData<List<PlaylistWithSongs>> getPlaylistLiveData() {
        return mPlaylistLiveData;
    }

    public void setPlaylistLiveData(List<PlaylistWithSongs> playlistList) {
        mPlaylistLiveData.setValue(playlistList);
    }
}