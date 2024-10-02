package net.lenhat.musicapplication.data.source.local.song;

import net.lenhat.musicapplication.data.model.Song;
import net.lenhat.musicapplication.data.source.SongDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class LocalSongDataSource implements SongDataSource.Local {
    private final SongDao mSongDao;

    public LocalSongDataSource(SongDao songDao) {
        mSongDao = songDao;
    }

    @Override
    public Single<List<Song>> getSongs() {
        return mSongDao.getAllSongs();
    }

    @Override
    public Flowable<List<Song>> getFavoriteSongs() {
        return mSongDao.getFavoriteSongs();
    }

    @Override
    public Flowable<List<Song>> getTopNMostHeardSongs(int limit) {
        return mSongDao.getTopNMostHeardSongs(limit);
    }

    @Override
    public Flowable<List<Song>> getTopNForYouSongs(int limit) {
        return mSongDao.getTopNForYouSongs(limit);
    }

    @Override
    public Completable saveSongs(Song... songs) {
        return mSongDao.insertSongs(songs);
    }

    @Override
    public Completable updateSong(Song song) {
        return mSongDao.updateSong(song);
    }
}
