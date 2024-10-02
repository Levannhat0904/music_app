package net.lenhat.musicapplication.ui.discovery.mostheard.more;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MoreMostHeardViewModel extends ViewModel {
    private final SongRepositoryImpl mSongRepository;
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();

    public MoreMostHeardViewModel(SongRepositoryImpl songRepository) {
        mSongRepository = songRepository;
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs.setValue(songs);
    }

    public Flowable<List<Song>> loadTop40MostHeardSongs() {
        return mSongRepository.getTopNMostHeardSongs(40);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final SongRepositoryImpl mSongRepository;

        public Factory(SongRepositoryImpl songRepository) {
            mSongRepository = songRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MoreMostHeardViewModel.class)) {
                return (T) new MoreMostHeardViewModel(mSongRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}