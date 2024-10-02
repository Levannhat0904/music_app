package net.lenhat.musicapplication.data.source;

import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.data.model.artist.ArtistList;
import net.lenhat.musicapplication.data.model.artist.ArtistSongCrossRef;
import net.lenhat.musicapplication.data.model.artist.ArtistWithSongs;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

public interface ArtistDataSource {
    interface Local {
        Flowable<List<Artist>> getAllArtists();

        Flowable<List<Artist>> getTopNArtists(int limit);

        Single<ArtistWithSongs> getArtistWithSongs(int artistId);

        Completable insertArtist(List<Artist> artists);

        Completable insertArtistSongCrossRef(List<ArtistSongCrossRef> artistSongCrossRefList);

        Completable updateArtist(Artist artist);

        Completable deleteArtist(Artist artist);
    }

    interface Remote {
        void loadArtists(Callback<ArtistList> callback);
    }
}
