package net.lenhat.musicapplication.data.repository.recent;

import net.lenhat.musicapplication.data.model.RecentSong;
import net.lenhat.musicapplication.data.source.RecentSongDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class RecentSongRepositoryImpl implements RecentSongRepository {
    private final RecentSongDataSource mRecentSongDataSource;

    public RecentSongRepositoryImpl(RecentSongDataSource recentSongDataSource) {
        mRecentSongDataSource = recentSongDataSource;
    }

    @Override
    public Flowable<List<RecentSong>> getAllRecentSongs() {
        return mRecentSongDataSource.getAll();
    }

    @Override
    public Completable insertRecentSong(RecentSong... songs) {
        return mRecentSongDataSource.insert(songs);
    }
}
