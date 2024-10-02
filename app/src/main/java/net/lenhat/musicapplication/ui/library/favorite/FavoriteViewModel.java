package net.lenhat.musicapplication.ui.library.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.lenhat.musicapplication.data.model.Song;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> mFavoriteSongs = new MutableLiveData<>();

    public LiveData<List<Song>> getFavoriteSongs() {
        return mFavoriteSongs;
    }

    public void setFavoriteSongs(List<Song> favoriteSongs) {
        mFavoriteSongs.setValue(favoriteSongs);
    }
}