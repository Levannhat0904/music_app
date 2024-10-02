package net.lenhat.musicapplication.data.source;

import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.model.playlist.PlaylistSongCrossRef;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface PlaylistDataSource {
    interface Local {
        Flowable<List<Playlist>> getAll();

        Single<Playlist> findByName(String playlistName);

        Flowable<List<PlaylistWithSongs>> getAllPlaylistWithSongs();

        Single<PlaylistWithSongs> findPlaylistWithSongByPlaylistId(int id);

        Completable insert(Playlist playlist);

        Completable insertPlaylistSongCrossRef(PlaylistSongCrossRef object);

        Completable update(Playlist playlist);

        Completable delete(Playlist playlist);
    }

    interface Remote {
        // todo
    }
}
