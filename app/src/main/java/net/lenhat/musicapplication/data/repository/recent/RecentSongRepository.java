package net.lenhat.musicapplication.data.repository.recent;

import net.lenhat.musicapplication.data.model.RecentSong;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface RecentSongRepository {
    Flowable<List<RecentSong>> getAllRecentSongs();

    Completable insertRecentSong(RecentSong... songs);
}
