package net.lenhat.musicapplication.data.source.local.playlist;

import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.model.playlist.PlaylistSongCrossRef;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;
import net.lenhat.musicapplication.data.source.PlaylistDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class LocalPlaylistDataSource implements PlaylistDataSource.Local {
    private final PlaylistDao mPlaylistDao;

    public LocalPlaylistDataSource(PlaylistDao playlistDao) {
        mPlaylistDao = playlistDao;
    }

    @Override
    public Flowable<List<Playlist>> getAll() {
        return mPlaylistDao.getAll();
    }

    @Override
    public Single<Playlist> findByName(String playlistName) {
        return mPlaylistDao.findByName(playlistName);
    }

    @Override
    public Flowable<List<PlaylistWithSongs>> getAllPlaylistWithSongs() {
        return mPlaylistDao.getAllPlaylistWithSongs();
    }

    @Override
    public Single<PlaylistWithSongs> findPlaylistWithSongByPlaylistId(int id) {
        return mPlaylistDao.findPlaylistWithSongByPlaylistId(id);
    }

    @Override
    public Completable insert(Playlist playlist) {
        return mPlaylistDao.insert(playlist.getName(),
                playlist.getArtwork(), playlist.getCreatedAt());
    }

    @Override
    public Completable insertPlaylistSongCrossRef(PlaylistSongCrossRef object) {
        return mPlaylistDao.insertPlaylistSongCrossRef(object);
    }

    @Override
    public Completable update(Playlist playlist) {
        return mPlaylistDao.update(playlist);
    }

    @Override
    public Completable delete(Playlist playlist) {
        return mPlaylistDao.delete(playlist);
    }
}
