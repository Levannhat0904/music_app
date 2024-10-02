package net.lenhat.musicapplication.ui.home.recommended;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.SongList;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedSongViewModel extends ViewModel {
    private final SongRepositoryImpl mSongRepository;
    private final MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();

    public RecommendedSongViewModel(SongRepositoryImpl songRepository) {
        mSongRepository = songRepository;
        loadSongs();
    }

    private void loadSongs() {
        mSongRepository.loadSongs(new Callback<SongList>() {
            @Override
            public void onResponse(@NonNull Call<SongList> call,
                                   @NonNull Response<SongList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> songs = response.body().getSongs();
                    setSongs(songs);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SongList> call, @NonNull Throwable throwable) {
                mSongs.postValue(new ArrayList<>());
            }
        });
    }

    Completable saveSongIntoDB(List<Song> songs) {
        if (songs == null) {
            return Completable.complete();
        }
        Song[] songArray = songs.toArray(new Song[0]);
        return mSongRepository.saveSongs(songArray);
    }

    public void setSongs(List<Song> songs) {
        if (songs != null) {
            mSongs.postValue(songs);
        }
    }

    public LiveData<List<Song>> getSongs() {
        return mSongs;
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
            if (modelClass.isAssignableFrom(RecommendedSongViewModel.class)) {
                return (T) new RecommendedSongViewModel(mSongRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
