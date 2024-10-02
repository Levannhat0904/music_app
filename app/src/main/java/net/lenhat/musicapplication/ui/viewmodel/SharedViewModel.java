package net.lenhat.musicapplication.ui.viewmodel;

import static net.lenhat.musicapplication.utils.AppUtils.DefaultPlaylistName.DEFAULT;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.lenhat.musicapplication.data.model.PlayingSong;
import net.lenhat.musicapplication.data.model.RecentSong;
import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;
import net.lenhat.musicapplication.data.repository.recent.RecentSongRepository;
import net.lenhat.musicapplication.data.repository.song.SongRepositoryImpl;
import net.lenhat.musicapplication.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@SuppressWarnings("unused")
public class SharedViewModel extends ViewModel {
    private final SongRepositoryImpl mSongRepository;
    private final RecentSongRepository mRecentSongRepository;
    private static volatile SharedViewModel sInstance;
    private final Map<String, Playlist> mPlaylistMap = new HashMap<>();
    private final MutableLiveData<Playlist> mPlaylist = new MutableLiveData<>();
    private final MutableLiveData<PlayingSong> mPlayingSongLiveData = new MutableLiveData<>();
    private final PlayingSong mPlayingSong = new PlayingSong();
    private String mPlaylistName;
    private final MutableLiveData<Integer> mIndexToPlay = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSongLoaded = new MutableLiveData<>();

    public static SharedViewModel getInstance() {
        return sInstance;
    }

    private SharedViewModel() {
        this(null, null);
    }

    private SharedViewModel(SongRepositoryImpl songRepository,
                            RecentSongRepository recentSongRepository) {
        mSongRepository = songRepository;
        mRecentSongRepository = recentSongRepository;
        synchronized (this) {
            if (sInstance == null) {
                sInstance = this;
            }
        }
        initPlaylists();
    }

    private void initPlaylists() {
        for (AppUtils.DefaultPlaylistName playlistName : AppUtils.DefaultPlaylistName.values()) {
            Playlist playlist = new Playlist(-1, playlistName.getValue());
            mPlaylistMap.put(playlistName.getValue(), playlist);
        }
    }

    public Flowable<List<Song>> loadRecentSongs() {
        return mRecentSongRepository.getAllRecentSongs().map(ArrayList::new);
    }

    public Completable saveRecentSong(Song song) {
        if (song == null) {
            return null;
        }
        RecentSong recentSong = new RecentSong.Builder(song).build();
        return mRecentSongRepository.insertRecentSong(recentSong);
    }

    public Completable updateSongFavoriteStatus(Song song) {
        return mSongRepository.updateSong(song);
    }

    public Completable updateSongInDB(Song song) {
        song.setCounter(song.getCounter() + 1);
        song.setReplay(song.getReplay() + 1);
        return mSongRepository.updateSong(song);
    }

    public void setupPreviousSessionPlayingSong(String songId, String playlistName) {
        mPlaylistName = playlistName;
        int index;
        Playlist playlist = getPlaylist(playlistName);
        if (playlist == null) {
            playlist = getPlaylist(DEFAULT.getValue());
        }
        if (songId != null && playlist != null) {
            // gọi thiết lập playlist trước khi thực hiện các hành động khác
            // để đảm bảo playlist đã tồn tại khi gọi thiết lập index to play
            setCurrentPlaylist(playlistName);
            mPlayingSong.setPlaylist(playlist);
            List<Song> songList = playlist.getSongs();
            index = songList.indexOf(new Song(songId));
            if (index >= 0 && songList.size() > index) {
                Song song = songList.get(index);
                mPlayingSong.setSong(song);
                mPlayingSong.setCurrentSongIndex(index);
            }
            // todo: update song when config changed
            setPlayingSong(mPlayingSong);
            setIndexToPlay(index);
        }
    }

//    public void setPlaylistName(String playlistName) {
//        mPlaylistName = playlistName;
//        setPlaylist(playlistName);
//    }

    public void setPlaylistSongs(List<PlaylistWithSongs> playlistWithSongs) {
        if (playlistWithSongs != null) {
            for (PlaylistWithSongs element : playlistWithSongs) {
                Playlist playlist = element.playlist;
                playlist.updateSongs(element.songs);
                addPlaylist(playlist);
            }
//            setSongLoaded(true);
        }
    }

    public LiveData<Playlist> getCurrentPlaylist() {
        return mPlaylist;
    }

    public void setCurrentPlaylist(String playlistName) {
        Playlist playlist = getPlaylist(playlistName);
        if (playlist != null) {
            mPlaylist.setValue(playlist);
            mPlayingSong.setPlaylist(playlist);
        }
    }

    public Playlist getPlaylist(String playlistName) {
        return mPlaylistMap.getOrDefault(playlistName, null);
    }

    public LiveData<PlayingSong> getPlayingSong() {
        return mPlayingSongLiveData;
    }

    public void setPlayingSong(PlayingSong playingSong) {
        mPlayingSongLiveData.setValue(playingSong);
    }

    public void setupPlaylist(List<Song> songs, String playlistName) {
        if (songs == null || songs.isEmpty()) {
            return;
        }
        Playlist playlist = getPlaylist(playlistName);
        if (playlist != null) {
            playlist.updateSongs(songs);
            mPlaylistMap.put(playlistName, playlist);
        }
    }

    public void addPlaylist(Playlist playlist) {
        mPlaylistMap.put(playlist.getName(), playlist);
    }

    public void setPlayingSong(int index) {
        if (index > -1 && mPlayingSong.getPlaylist().getSongs().size() > index) {
            Song song = mPlayingSong.getPlaylist().getSongs().get(index);
            mPlayingSong.setSong(song);
            mPlayingSong.setCurrentSongIndex(index);
            mPlayingSongLiveData.setValue(mPlayingSong);
        }
    }

    public LiveData<Integer> getIndexToPlay() {
        return mIndexToPlay;
    }

    public void setIndexToPlay(int index) {
        mIndexToPlay.setValue(index);
    }

    public LiveData<Boolean> isSongLoaded() {
        return isSongLoaded;
    }

    public void setSongLoaded(boolean isLoaded) {
        isSongLoaded.setValue(isLoaded);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final SongRepositoryImpl mSongRepository;
        private final RecentSongRepository mRecentSongRepository;

        public Factory(SongRepositoryImpl songRepository,
                       RecentSongRepository recentSongRepository) {
            mSongRepository = songRepository;
            mRecentSongRepository = recentSongRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SharedViewModel.class)) {
                return (T) new SharedViewModel(mSongRepository, mRecentSongRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
