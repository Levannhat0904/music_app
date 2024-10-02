package net.lenhat.musicapplication.data.source.local.artist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.data.model.artist.ArtistSongCrossRef;
import net.lenhat.musicapplication.data.model.artist.ArtistWithSongs;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ArtistDao {
    @Query("SELECT * FROM artists")
    Flowable<List<Artist>> getAllArtists();

    @Query("SELECT * FROM artists ORDER BY interested DESC LIMIT :limit")
    Flowable<List<Artist>> getTopNArtists(int limit);

    @Transaction
    @Query("SELECT * FROM artists WHERE artist_id = :artistId")
    Single<ArtistWithSongs> getArtistWithSongs(int artistId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertArtist(List<Artist> artists);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertArtistSongCrossRef(List<ArtistSongCrossRef> artistSongCrossRefList);

    @Update
    Completable updateArtist(Artist artist);

    @Delete
    Completable deleteArtist(Artist artist);
}
