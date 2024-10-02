package net.lenhat.musicapplication.ui.library;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;
import net.lenhat.musicapplication.data.repository.playlist.PlaylistRepositoryImpl;
import net.lenhat.musicapplication.data.repository.recent.RecentSongRepository;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class LibraryViewModel extends ViewModel {
    private final SongRepositoryImpl mSongRepository;
    private final RecentSongRepository mRecentSongRepository;
    private final PlaylistRepositoryImpl mPlaylistRepository;
    private final MutableLiveData<List<Song>> mRecentSongs = new MutableLiveData<>();
    private final MutableLiveData<List<Song>> mFavoriteSongs = new MutableLiveData<>();

    public LibraryViewModel(SongRepositoryImpl songRepository,
                            RecentSongRepository recentSongRepository,
                            PlaylistRepositoryImpl playlistRepository) {
        mSongRepository = songRepository;
        mRecentSongRepository = recentSongRepository;
        mPlaylistRepository = playlistRepository;
    }

    public Flowable<List<Song>> loadRecentSongs() {
        return mRecentSongRepository.getAllRecentSongs().map(ArrayList::new);
    }

    public Flowable<List<Song>> loadFavoriteSongs() {
        return mSongRepository.getFavoriteSongs();
    }

    public Flowable<List<PlaylistWithSongs>> loadPlaylistsWithSongs() {
        return mPlaylistRepository.getAllPlaylistWithSongs();
    }

    public void setRecentSongs(List<Song> recentSongs) {
        mRecentSongs.postValue(recentSongs);
    }

    public LiveData<List<Song>> getRecentSongs() {
        return mRecentSongs;
    }

    public void setFavoriteSongs(List<Song> favoriteSongs) {
        mFavoriteSongs.postValue(favoriteSongs);
    }

    public LiveData<List<Song>> getFavoriteSongs() {
        return mFavoriteSongs;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final SongRepositoryImpl mSongRepository;
        private final RecentSongRepository mRecentSongRepository;
        private final PlaylistRepositoryImpl mPlaylistRepository;

        public Factory(SongRepositoryImpl songRepository,
                       RecentSongRepository recentSongRepository,
                       PlaylistRepositoryImpl playlistRepository) {
            mSongRepository = songRepository;
            mRecentSongRepository = recentSongRepository;
            mPlaylistRepository = playlistRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(LibraryViewModel.class)) {
                return (T) new LibraryViewModel(mSongRepository,
                        mRecentSongRepository, mPlaylistRepository);
            } else {
                throw new IllegalArgumentException(
                        "Unknown ViewModel class: " + modelClass.getName());
            }
        }
    }
}