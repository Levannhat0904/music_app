package net.lenhat.musicapplication.data.source.local.playlist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import net.lenhat.musicapplication.data.model.playlist.Playlist;
import net.lenhat.musicapplication.data.model.playlist.PlaylistSongCrossRef;
import net.lenhat.musicapplication.data.model.playlist.PlaylistWithSongs;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlaylistDao {
    @Query("SELECT * FROM playlists")
    Flowable<List<Playlist>> getAll();

    @Query("SELECT * FROM playlists WHERE name = :name")
    Single<Playlist> findByName(String name);

    @Transaction
    @Query("SELECT * FROM playlists")
    Flowable<List<PlaylistWithSongs>> getAllPlaylistWithSongs();

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlist_id = :id")
    Single<PlaylistWithSongs> findPlaylistWithSongByPlaylistId(int id);

    @Query("INSERT INTO playlists (name, artwork, created_at) " +
            "VALUES (:name, :artwork, :createdAt)")
    Completable insert(String name, String artwork, Date createdAt);

    @Insert
    Completable insertPlaylistSongCrossRef(PlaylistSongCrossRef object);

    @Update
    Completable update(Playlist playlist);

    @Delete
    Completable delete(Playlist playlist);
}
