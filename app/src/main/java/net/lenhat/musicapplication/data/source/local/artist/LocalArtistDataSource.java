package net.lenhat.musicapplication.data.source.local.artist;

import net.lenhat.musicapplication.data.model.artist.Artist;
import net.lenhat.musicapplication.data.model.artist.ArtistSongCrossRef;
import net.lenhat.musicapplication.data.model.artist.ArtistWithSongs;
import net.lenhat.musicapplication.data.source.ArtistDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class LocalArtistDataSource implements ArtistDataSource.Local {
    private final ArtistDao mArtistDao;

    public LocalArtistDataSource(ArtistDao artistDao) {
        mArtistDao = artistDao;
    }

    @Override
    public Flowable<List<Artist>> getAllArtists() {
        return mArtistDao.getAllArtists();
    }

    @Override
    public Flowable<List<Artist>> getTopNArtists(int limit) {
        return mArtistDao.getTopNArtists(limit);
    }

    @Override
    public Single<ArtistWithSongs> getArtistWithSongs(int artistId) {
        return mArtistDao.getArtistWithSongs(artistId);
    }

    @Override
    public Completable insertArtist(List<Artist> artists) {
        return mArtistDao.insertArtist(artists);
    }

    @Override
    public Completable insertArtistSongCrossRef(List<ArtistSongCrossRef> artistSongCrossRefList) {
        return mArtistDao.insertArtistSongCrossRef(artistSongCrossRefList);
    }

    @Override
    public Completable updateArtist(Artist artist) {
        return mArtistDao.updateArtist(artist);
    }

    @Override
    public Completable deleteArtist(Artist artist) {
        return mArtistDao.deleteArtist(artist);
    }
}
