package net.lenhat.musicapplication.data.source.local.recent;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import net.lenhat.musicapplication.data.model.RecentSong;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RecentSongDao {
    @Query("SELECT * FROM recent_songs ORDER BY play_at DESC LIMIT 30")
    Flowable<List<RecentSong>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(RecentSong... recentSong);
}
