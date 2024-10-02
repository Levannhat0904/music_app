package net.lenhat.musicapplication.ui.discovery.artist.more;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.lenhat.musicapplication.data.model.artist.Artist;

import java.util.List;

public class MoreArtistViewModel extends ViewModel {
    private final MutableLiveData<List<Artist>> mArtists = new MutableLiveData<>();

    public LiveData<List<Artist>> getArtist() {
        return mArtists;
    }

    public void setArtist(List<Artist> artist) {
        mArtists.postValue(artist);
    }
}