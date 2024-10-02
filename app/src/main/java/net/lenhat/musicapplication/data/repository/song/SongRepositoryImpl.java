package net.lenhat.musicapplication.data.repository.song;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.model.SongList;
import net.lenhat.musicapplication.data.source.local.song.LocalSongDataSource;
import net.lenhat.musicapplication.data.source.remote.SongRemoteDataSourceImpl;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

public class SongRepositoryImpl implements SongRepository.Local, SongRepository.Remote {
    private final LocalSongDataSource mLocalSongDataSource;

    public SongRepositoryImpl(LocalSongDataSource localSongDataSource) {
        mLocalSongDataSource = localSongDataSource;
    }

    @Override
    public void loadSongs(Callback<SongList> callback) {
        SongRemoteDataSourceImpl remoteDataSource = new SongRemoteDataSourceImpl();
        remoteDataSource.loadSongs(callback);
    }

    @Override
    public Single<List<Song>> getSongs() {
        return mLocalSongDataSource.getSongs();
    }

    @Override
    public Flowable<List<Song>> getFavoriteSongs() {
        return mLocalSongDataSource.getFavoriteSongs();
    }

    @Override
    public Flowable<List<Song>> getTopNMostHeardSongs(int limit) {
        return mLocalSongDataSource.getTopNMostHeardSongs(limit);
    }

    @Override
    public Flowable<List<Song>> getTopNForYouSongs(int limit) {
        return mLocalSongDataSource.getTopNForYouSongs(limit);
    }

    @Override
    public Completable saveSongs(Song... songs) {
        return mLocalSongDataSource.saveSongs(songs);
    }

    @Override
    public Completable updateSong(Song song) {
        return mLocalSongDataSource.updateSong(song);
    }
}
