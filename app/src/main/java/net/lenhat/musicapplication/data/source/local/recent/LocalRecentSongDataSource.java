package net.lenhat.musicapplication.data.source.local.recent;

import net.lenhat.musicapplication.data.model.RecentSong;
import net.lenhat.musicapplication.data.source.RecentSongDataSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class LocalRecentSongDataSource implements RecentSongDataSource {
    private final RecentSongDao mRecentSongDao;

    public LocalRecentSongDataSource(RecentSongDao recentSongDao) {
        mRecentSongDao = recentSongDao;
    }

    @Override
    public Flowable<List<RecentSong>> getAll() {
        return mRecentSongDao.getAll();
    }

    @Override
    public Completable insert(RecentSong... recentSong) {
        return mRecentSongDao.insert(recentSong);
    }
}
