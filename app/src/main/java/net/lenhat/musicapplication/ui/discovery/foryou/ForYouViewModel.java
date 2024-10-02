package net.lenhat.musicapplication.ui.discovery.foryou;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class ForYouViewModel extends ViewModel {
    private final SongRepositoryImpl mSongRepository;
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();

    public ForYouViewModel(SongRepositoryImpl songRepository) {
        mSongRepository = songRepository;
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs.setValue(songs);
    }

    public Flowable<List<Song>> loadTop15ForYouSongs() {
        return mSongRepository.getTopNForYouSongs(15);
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
            if (modelClass.isAssignableFrom(ForYouViewModel.class)) {
                return (T) new ForYouViewModel(mSongRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}