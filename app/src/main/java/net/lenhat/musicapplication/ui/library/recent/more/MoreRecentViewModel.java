package net.lenhat.musicapplication.ui.library.recent.more;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.lenhat.musicapplication.data.model.Song;

import java.util.List;

public class MoreRecentViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mRecentSongs = new MutableLiveData<>();

    public LiveData<List<Song>> getRecentSongs() {
        return mRecentSongs;
    }

    public void setRecentSongs(List<Song> recentSongs) {
        mRecentSongs.setValue(recentSongs);
    }
}