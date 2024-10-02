package net.lenhat.musicapplication.ui.discovery.artist.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.data.model.artist.ArtistWithSongs;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.repository.artist.ArtistRepository;
import net.lenhat.musicapplication.ui.viewmodel.SharedViewModel;

import io.reactivex.rxjava3.core.Single;

public class DetailArtistViewModel extends ViewModel {
    private final ArtistRepository mRepository;
    private final MutableLiveData<ArtistWithSongs> mArtistWithSongs = new MutableLiveData<>();

    public DetailArtistViewModel(ArtistRepository repository) {
        mRepository = repository;
    }

    public Single<ArtistWithSongs> getArtistWithSongs(int artistId) {
        return mRepository.getArtistWithSongs(artistId);
    }

    public void setArtistWithSongs(ArtistWithSongs artistWithSongs) {
        mArtistWithSongs.postValue(artistWithSongs);
    }

    public LiveData<ArtistWithSongs> getArtistWithSongs() {
        return mArtistWithSongs;
    }

    public Playlist createPlaylist() {
        ArtistWithSongs artistWithSongs = mArtistWithSongs.getValue();
        if (artistWithSongs != null) {
            Playlist playlist = new Playlist(-1, artistWithSongs.artist.getName());
            playlist.updateSongs(artistWithSongs.songs);
            SharedViewModel.getInstance().addPlaylist(playlist);
            return playlist;
        }
        return null;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final ArtistRepository mRepository;

        public Factory(ArtistRepository repository) {
            mRepository = repository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(DetailArtistViewModel.class)) {
                return (T) new DetailArtistViewModel(mRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}