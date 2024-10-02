package net.lenhat.musicapplication.ui.home.recommended.more;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.lenhat.musicapplication.data.model.Song;

import java.util.List;

public class MoreRecommendedViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs.setValue(songs);
    }
}