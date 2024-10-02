package net.lenhat.musicapplication.data.repository.playlist;

import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.model.playlist.PlaylistSongCrossRef;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;
import net.lenhat.musicapplication.data.source.PlaylistDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class PlaylistRepositoryImpl implements PlaylistRepository.Local,
        PlaylistRepository.Remote {
    private final PlaylistDataSource.Local mLocalDataSource;

    public PlaylistRepositoryImpl(PlaylistDataSource.Local localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public Flowable<List<Playlist>> getAll() {
        return mLocalDataSource.getAll();
    }

    @Override
    public Single<Playlist> findByName(String playlistName) {
        return mLocalDataSource.findByName(playlistName);
    }

    @Override
    public Flowable<List<PlaylistWithSongs>> getAllPlaylistWithSongs() {
        return mLocalDataSource.getAllPlaylistWithSongs();
    }

    @Override
    public Single<PlaylistWithSongs> findPlaylistWithSongByPlaylistId(int id) {
        return mLocalDataSource.findPlaylistWithSongByPlaylistId(id);
    }

    @Override
    public Completable insert(Playlist playlist) {
        return mLocalDataSource.insert(playlist);
    }

    @Override
    public Completable insertPlaylistSongCrossRef(PlaylistSongCrossRef object) {
        return mLocalDataSource.insertPlaylistSongCrossRef(object);
    }

    @Override
    public Completable update(Playlist playlist) {
        return mLocalDataSource.update(playlist);
    }

    @Override
    public Completable delete(Playlist playlist) {
        return mLocalDataSource.delete(playlist);
    }
}
