package net.lenhat.musicapplication.data.source;

import net.lenhat.musicapplication.data.model.RecentSong;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface RecentSongDataSource {
    Flowable<List<RecentSong>> getAll();

    Completable insert(RecentSong... recentSong);
}
