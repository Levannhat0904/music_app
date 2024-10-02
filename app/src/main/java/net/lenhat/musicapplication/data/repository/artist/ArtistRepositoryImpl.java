package net.lenhat.musicapplication.data.repository.artist;

import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.data.model.artist.ArtistList;
import net.lenhat.musicapplication.data.model.artist.ArtistSongCrossRef;
import net.lenhat.musicapplication.data.model.artist.ArtistWithSongs;
import net.lenhat.musicapplication.data.source.ArtistDataSource;
import net.lenhat.musicapplication.data.source.remote.RemoteArtistDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Callback;

public class ArtistRepositoryImpl implements ArtistRepository {
    private final ArtistDataSource.Remote remoteArtistDataSource = new RemoteArtistDataSource();
    private final ArtistDataSource.Local mLocalArtistDataSource;

    public ArtistRepositoryImpl(ArtistDataSource.Local localArtistDataSource) {
        mLocalArtistDataSource = localArtistDataSource;
    }

    @Override
    public void loadArtists(Callback<ArtistList> callback) {
        remoteArtistDataSource.loadArtists(callback);
    }

    @Override
    public Flowable<List<Artist>> getTopNArtists(int limit) {
        return mLocalArtistDataSource.getTopNArtists(limit);
    }

    @Override
    public Flowable<List<Artist>> getAllArtists() {
        return mLocalArtistDataSource.getAllArtists();
    }

    @Override
    public Single<ArtistWithSongs> getArtistWithSongs(int artistId) {
        return mLocalArtistDataSource.getArtistWithSongs(artistId);
    }

    @Override
    public Completable insertArtist(List<Artist> artists) {
        return mLocalArtistDataSource.insertArtist(artists);
    }

    @Override
    public Completable insertArtistSongCrossRef(List<ArtistSongCrossRef> artistSongCrossRefList) {
        return mLocalArtistDataSource.insertArtistSongCrossRef(artistSongCrossRefList);
    }

    @Override
    public Completable updateArtist(Artist artist) {
        return mLocalArtistDataSource.updateArtist(artist);
    }

    @Override
    public Completable deleteArtist(Artist artist) {
        return mLocalArtistDataSource.deleteArtist(artist);
    }
}
