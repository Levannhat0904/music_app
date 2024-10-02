package net.lenhat.musicapplication.data.source.local.song;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import net.lenhat.musicapplication.data.model.Song;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface SongDao {
    @Query("SELECT * FROM songs")
    Single<List<Song>> getAllSongs();

    @Query("SELECT * FROM songs WHERE favorite = 1")
    Flowable<List<Song>> getFavoriteSongs();

    @Query("SELECT * FROM songs WHERE song_id = :id")
    Flowable<Song> getSongById(int id);

    @Query("SELECT * FROM songs ORDER BY counter DESC LIMIT :limit")
    Flowable<List<Song>> getTopNMostHeardSongs(int limit);

    @Query("SELECT * FROM songs ORDER BY replay DESC LIMIT :limit")
    Flowable<List<Song>> getTopNForYouSongs(int limit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertSongs(Song... songs);

    @Delete
    Completable deleteSong(Song song);

    @Update
    Completable updateSong(Song song);
}
